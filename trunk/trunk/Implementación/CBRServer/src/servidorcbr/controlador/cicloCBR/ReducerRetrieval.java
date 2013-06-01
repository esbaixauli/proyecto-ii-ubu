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

/**
 * Reducer de la etapa de recuperación: recibe los casos almacenados en su región, los convierte
 * a CBRCase, les aplica la técnica de recuperación elegida por el usuario y guarda los restantes
 * en una tabla temporal de Hbase creada por LanzadorCBR.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
public class ReducerRetrieval extends
		TableReducer<ImmutableBytesWritable, Result, ImmutableBytesWritable> {
	
	/**
	 * ColumnFamily en la que están almacenados todos los campos del caso.
	 */
	private final byte[] cf = Bytes.toBytes("campos");

	/* (non-Javadoc)
	 * @see org.apache.hadoop.mapreduce.Reducer#reduce(KEYIN, java.lang.Iterable, org.apache.hadoop.mapreduce.Reducer.Context)
	 */
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
			Class<? extends CaseComponent> cdesc = query.getDescription().getClass();
			Class<? extends CaseComponent> csolution = CargadorClases.cargarClaseSolucion(tc.getNombre());
			int i = 0;
			for (Result row : values) {
				CBRCase caso = obtenerCaso(tc, key, row, cdesc, csolution);
				casos.add(caso);
				ids.add(i, row.getRow());
				i++;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		casos = EjecutorTecnicaRetrieval.ejecutarRetrieval(casos, query, tc);
		int i = 0;
		for (CBRCase caso : casos) {
			Put put = new Put(ids.get(i++));
			HashMap<String,Serializable> campos = RellenadorClases.rellenarHash(tc, caso);
			for (Entry<String,Serializable> par : campos.entrySet()) {
				if (par.getKey().equals("META_QUALITY")) {
					put.add(cf, Bytes.toBytes("META_QUALITY"), Bytes.toBytes((Integer) campos.get("META_QUALITY")));
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

	/**
	 * Lee la fila de la tabla en Hbase y la convierte en un CBRCase.
	 * @param tc El tipo de caso correspondiente.
	 * @param key Id de la fila en Hbase.
	 * @param row La fila de Hbase.
	 * @param cdesc La clase que representa la descripción del caso (implementa CaseComponent). 
	 * @param csol La clase que representa la solución del caso (implementa CaseComponent).
	 * @return El caso en forma de objeto CBRCase.
	 * @throws ClassNotFoundException Si no encuentra alguna de las clases.
	 */
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Asignamos la calidad al caso
		Calidad c = new Calidad();
		try {
			int cal = Bytes.toInt(row.getValue(cf, Bytes.toBytes("META_QUALITY")));
			c.setCalidad(cal);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
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