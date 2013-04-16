package servidorcbr.controlador.cicloCBR;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.CaseComponent;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import servidorcbr.controlador.cicloCBR.ejecucion.EjecutorDiverseByMedian;
import servidorcbr.controlador.cicloCBR.ejecucion.EjecutorFilterBased;
import servidorcbr.controlador.cicloCBR.ejecucion.EjecutorKNN;
import servidorcbr.controlador.cicloCBR.ejecucion.EjecutorTecnicaRetrieval;
import servidorcbr.controlador.generadorClases.CargadorClases;
import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.TipoCaso;

public class ReducerRetrieval extends
		Reducer<ImmutableBytesWritable, Result, ImmutableBytesWritable, Text> {

	@Override
	public void reduce(ImmutableBytesWritable key, Iterable<Result> values, Context context) {
		// Convierte los values a objetos del problema y se los manda a ejecutar
		Collection<CBRCase> casos = new ArrayList<CBRCase>();
		TipoCaso tc = cargarTipoCaso(context.getConfiguration().get("tipocaso"));
		try {
			Class<? extends CaseComponent> cdesc = CargadorClases.cargarClaseProblema(tc.getNombre());
			Class<? extends CaseComponent> csolution = CargadorClases.cargarClaseSolucion(tc.getNombre());
			for (Result row : values) {
				CBRCase caso;
				caso = obtenerCaso(tc, row, cdesc, csolution);
				casos.add(caso);
			}
			System.out.println("## Reducer "+key.toString()+" ejecutandose, nº de casos: " + casos.size());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		//Cargar desde serialización el objeto CBRQuery
		// ejecutarRetrieval(casos, query, context);TODO
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

	private TipoCaso cargarTipoCaso(String nombreCaso) {
		TipoCaso tc = null;
		try {
			Configuration conf = new Configuration();
			conf.addResource(new Path("/etc/hadoop/core-site.xml"));
		    conf.addResource(new Path("/etc/hadoop/hdfs-site.xml"));
			FileSystem fs = FileSystem.get(conf);
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

	private Collection<CBRCase> ejecutarRetrieval(Collection<CBRCase> casos,
			CBRQuery query, Context c) throws ClassNotFoundException {
		// cargar tipo de caso (serializado en un fichero en hdfs)
		TipoCaso tc = cargarTipoCaso(c.getConfiguration().get("caso"));
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
		return ejecutor.ejecutar(casos, query);
	}

}