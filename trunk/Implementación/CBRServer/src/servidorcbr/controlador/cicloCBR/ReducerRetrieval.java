package servidorcbr.controlador.cicloCBR;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.CaseComponent;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import servidorcbr.controlador.cicloCBR.ejecucion.EjecutorDiverseByMedian;
import servidorcbr.controlador.cicloCBR.ejecucion.EjecutorFilterBased;
import servidorcbr.controlador.cicloCBR.ejecucion.EjecutorKNN;
import servidorcbr.controlador.cicloCBR.ejecucion.EjecutorTecnicaRetrieval;
import servidorcbr.controlador.generadorClases.CargadorClases;
import servidorcbr.controlador.generadorClases.RellenadorClases;
import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.TipoCaso;

public class ReducerRetrieval extends
		TableReducer<ImmutableBytesWritable, Result, ImmutableBytesWritable> {
	
	private final byte[] cf = Bytes.toBytes("campos");

	@Override
	public void reduce(ImmutableBytesWritable key, Iterable<Result> values, Context context) {
		// Convierte los values a objetos del problema y se los manda a ejecutar
		Collection<CBRCase> casos = new ArrayList<CBRCase>();
		Configuration conf = context.getConfiguration();
	    FileSystem fs = null;
	    ArrayList<byte[]> ids = new ArrayList<byte[]>();
	    try {
			fs = FileSystem.get(conf);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		TipoCaso tc = cargarTipoCaso(fs, conf.get("tipocaso"));
		CBRQuery query = cargarQuery(fs, tc, conf.get("query"));
		try {
			Class<? extends CaseComponent> cdesc = CargadorClases.cargarClaseProblema(tc.getNombre());
			Class<? extends CaseComponent> csolution = CargadorClases.cargarClaseSolucion(tc.getNombre());
			int i = 0;
			for (Result row : values) {
				CBRCase caso;
				caso = obtenerCaso(tc, row, cdesc, csolution);
				casos.add(caso);
				ids.add(i, row.getRow());
				i++;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		casos = ejecutarRetrieval(casos, query, tc);
		int i = 0;
		for (CBRCase caso : casos) {
			Put put = new Put(ids.get(i++));
			HashMap<String,Serializable> campos = RellenadorClases.rellenarHash(tc, caso);
			for (Entry<String,Serializable> par : campos.entrySet()) {
				switch (tc.getAtbos().get(par.getKey()).getTipo()) {
				case "I":
					int valorI = (Integer) par.getValue();
					put.add(cf, Bytes.toBytes(par.getKey()), Bytes.toBytes(valorI));
					break;
				case "D":
					double valorD = (Double) par.getValue();
					put.add(cf, Bytes.toBytes(par.getKey()), Bytes.toBytes(valorD));
					break;
				case "S":
					String valorS = (String) par.getValue();
					put.add(cf, Bytes.toBytes(par.getKey()), Bytes.toBytes(valorS));
					break;
				}
				try {
					context.write(null, put);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private CBRCase obtenerCaso(TipoCaso tc, Result row, Class<? extends CaseComponent> cdesc, Class<? extends CaseComponent> csol)
			throws ClassNotFoundException {
		CaseComponent desc = null, solution = null;
		try {
			desc = cdesc.newInstance();
			solution = csol.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		for (Atributo atb : tc.getAtbos().values()) {
			byte[] val = null;
			Class<?> tipo = null, clase = null;;
			Object valor = null;
			CaseComponent cc = null;
			if (atb.getEsProblema()) {
				val = row.getValue(Bytes.toBytes("problema"), Bytes.toBytes(atb.getNombre()));
				clase = cdesc;
				cc = desc;
			} else {
				val = row.getValue(Bytes.toBytes("solucion"), Bytes.toBytes(atb.getNombre()));
				clase = csol;
				cc = solution;
			}
			switch (atb.getTipo()) {
			case "S":
				tipo = String.class;
				valor = Bytes.toString(val);
				break;
			case "D":
				tipo = Double.class;
				valor = Bytes.toDouble(val);
				break;
			case "I":
				tipo = Integer.class;
				valor = Bytes.toInt(val);
				break;
			default:
				return null;
			}
			String mName = "set" 
						+ atb.getNombre().substring(0, 1).toUpperCase() 
						+ atb.getNombre().substring(1, atb.getNombre().length());
			try {
				Method m = clase.getMethod(mName, tipo);
				m.invoke(cc, valor);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		CBRCase caso = new CBRCase();
		caso.setDescription(desc);
		caso.setSolution(solution);
		return caso;
	}

	private TipoCaso cargarTipoCaso(FileSystem fs, String nombreCaso) {
		TipoCaso tc = null;
		try {
			Path inFile = new Path("tcs/"+nombreCaso+".tc");
			FSDataInputStream in = fs.open(inFile);
			ObjectInputStream ois = new ObjectInputStream(in);
			tc = (TipoCaso) ois.readObject();
			ois.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) { }
		return tc;
	}
	
	private CBRQuery cargarQuery (FileSystem fs, TipoCaso tc, String nombreFichero) {
		HashMap<String,Serializable> h = null;
		try {
			Path inFile = new Path("queries/"+nombreFichero);
			FSDataInputStream in = fs.open(inFile);
			ObjectInputStream ois = new ObjectInputStream(in);
			h = (HashMap<String,Serializable>) ois.readObject();
			ois.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) { }
		return RellenadorClases.rellenarQuery(tc, h);
	}

	private Collection<CBRCase> ejecutarRetrieval(Collection<CBRCase> casos,
			CBRQuery query, TipoCaso tc) {
		EjecutorTecnicaRetrieval ejecutor = null;
		switch (tc.getDefaultRec().getNombre()) {
		case "DiverseByMedianRetrieval":
			ejecutor = new EjecutorDiverseByMedian(tc);
			break;
		case "NNretrieval":
			ejecutor = new EjecutorKNN(tc);
			break;
		case "FilterBasedRetrieval":
			ejecutor = new EjecutorFilterBased(tc);
			break;
		}
		try {
			casos = ejecutor.ejecutar(casos, query);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return casos;
	}

}