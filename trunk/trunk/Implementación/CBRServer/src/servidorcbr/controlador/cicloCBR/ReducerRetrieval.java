package servidorcbr.controlador.cicloCBR;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
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
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import servidorcbr.controlador.cicloCBR.ejecucion.EjecutorDiverseByMedian;
import servidorcbr.controlador.cicloCBR.ejecucion.EjecutorFilterBased;
import servidorcbr.controlador.cicloCBR.ejecucion.EjecutorKNN;
import servidorcbr.controlador.cicloCBR.ejecucion.EjecutorTecnicaRetrieval;
import servidorcbr.controlador.generadorClases.CargadorClases;
import servidorcbr.modelo.TipoCaso;

public class ReducerRetrieval
		extends
		Reducer<ImmutableBytesWritable, Result, ImmutableBytesWritable, Text> {

	@Override
	public void reduce(ImmutableBytesWritable key, Iterable<Result> values,
			Context context) {
		// Convierte los values a objetos del problema y se los manda a ejecutar
		Collection<CBRCase> casos = new ArrayList<CBRCase>();
		BufferedWriter out = null;
		TipoCaso tc = cargarTipoCaso(context.getConfiguration().get("tipocaso"));
		try {
			out = new BufferedWriter(new FileWriter("log.txt", true));
			out.write("--> Inicio Reducer\n");
			out.flush();
			out.write("--> Tipo de caso: "+tc.getNombre()+"\n");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			for (Result row : values) {
				out.write(row.toString()+"\n");
				CBRCase caso;
				caso = obtenerCaso(tc, row, context);
				casos.add(caso);
			}
			out.write("<-- Fin Reducer\n");
			out.flush();
			out.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Cargar desde serialización el objeto CBRQuery
		// ejecutarRetrieval(casos, query, context);TODO
	}

	private CBRCase obtenerCaso(TipoCaso tc, Result row, Context context)
			throws ClassNotFoundException {
		Class<?> cdesc = CargadorClases.cargarClaseProblema(tc.getNombre());
		Class<?> csolution = CargadorClases.cargarClaseSolucion(tc.getNombre());
		try {
			CaseComponent desc = (CaseComponent) cdesc.newInstance();
			CaseComponent solution = (CaseComponent) csolution.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		// parsear la row y convertir cada cosa en su formato (Hace falta que
		// venga el nombre de cada atbo)
		// TODO
		return null;
	}

	private TipoCaso cargarTipoCaso(String nombreCaso) {
		TipoCaso tc = null;
		try {
			Configuration conf = new Configuration();
			FileSystem fs = FileSystem.get(conf);
			Path inFile = new Path("/"+nombreCaso+".tc");
			FSDataInputStream in = fs.open(inFile);
			ObjectInputStream ois = new ObjectInputStream(in);
			tc = (TipoCaso) ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) { }
		return tc;
	}

	private Collection<CBRCase> ejecutarRetrieval(Collection<CBRCase> casos,
			CBRQuery query, Context c) throws ClassNotFoundException {
		// cargar tipo de caso (serializable)
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
