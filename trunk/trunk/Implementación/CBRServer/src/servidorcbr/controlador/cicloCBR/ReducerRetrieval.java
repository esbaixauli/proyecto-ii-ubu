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

import jcolibri.cbrcore.Attribute;
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
import servidorcbr.modelo.Calidad;
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
				caso = obtenerCaso(tc, key, row, cdesc, csolution);
				casos.add(caso);
				ids.add(i, row.getRow());
				i++;
			}
			System.out.println("Reducer ejecutandse, nº de casos: "+casos.size());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		casos = EjecutorTecnicaRetrieval.ejecutarRetrieval(casos, query, tc);
		int i = 0;
		for (CBRCase caso : casos) {
			Put put = new Put(ids.get(i++));
			HashMap<String,Serializable> campos = RellenadorClases.rellenarHash(tc, caso);
			for (Entry<String,Serializable> par : campos.entrySet()) {
				if (par.getKey().equals("META_ID")) {
					//put.add(cf, Bytes.toBytes("META_ID"), Bytes.toBytes((Long) campos.get("META_ID")));
				} else {
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
				}
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

	private CBRCase obtenerCaso(TipoCaso tc, ImmutableBytesWritable key, Result row, Class<? extends CaseComponent> cdesc, Class<? extends CaseComponent> csol)
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
			byte[] val = row.getValue(cf, Bytes.toBytes(atb.getNombre()));
			Class<?> tipo = null, clase = null;;
			Object valor = null;
			CaseComponent cc = null;
			if (atb.getEsProblema()) {
				clase = cdesc;
				cc = desc;
			} else {
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
			} catch (Exception e) {
			}
		}
		
		// Asignamos el ID de la fila al ID del caso
		try {
			cdesc.getDeclaredMethod("setMETA_ID", Long.class).invoke(desc,
					new Long(Bytes.toLong(key.copyBytes())));
			cdesc.getDeclaredMethod("setIdAttribute", Attribute.class).invoke(desc,
					new Attribute("META_ID", cdesc));
		} catch (Exception e) {
		}
		
		// Asignamos la calidad al caso
		int cal = Bytes.toInt(row.getValue(cf, Bytes.toBytes("META_QUALITY")));
		Calidad c = new Calidad();
		c.setCalidad(cal);
		
		CBRCase caso = new CBRCase();
		caso.setDescription(desc);
		caso.setSolution(solution);
		caso.setJustificationOfSolution(c);
		return caso;
	}

	/**
	 * Carga el objeto TipoCaso serializado en la carpeta "tcs/" de HDFS.
	 * @param fs FileSystem que representa a HDFS.
	 * @param nombreCaso Nombre del caso, que coincide con el nombre del fichero (con extensión .tc).
	 * @return El TipoCaso encontrado.
	 */
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
	
	/**
	 * Carga el query serializado como HashMap en HDFS y lo convierte a CBRQuery.
	 * @param fs FileSystem que representa a HDFS.
	 * @param tc Tipo de caso que modela el query.
	 * @param nombreFichero Nombre del fichero donde está serializado (dentro de "queries/").
	 * @return El CBRQuery obtenido.
	 */
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
}