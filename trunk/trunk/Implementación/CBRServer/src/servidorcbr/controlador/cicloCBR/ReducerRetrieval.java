package servidorcbr.controlador.cicloCBR;

import java.util.ArrayList;
import java.util.Collection;

import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.CaseComponent;

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
		Reducer<ImmutableBytesWritable, Text, ImmutableBytesWritable, IntWritable> {

	@Override
	public void reduce(ImmutableBytesWritable key, Iterable<Text> values,
			Context context) {
		// Convierte los values a objetos del problema y se los manda a ejecutar
		Collection<CBRCase> casos = new ArrayList<CBRCase>();
		try {
			for (Text row : values) {
				CBRCase caso;
				caso = obtenerCaso(row, context);
				casos.add(caso);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		//Cargar desde serialización el objeto CBRQuery
		// ejecutarRetrieval(casos, query, context);TODO
	}

	private CBRCase obtenerCaso(Text row, Context context)
			throws ClassNotFoundException {

		TipoCaso tc = cargarTipoCaso(context.getConfiguration().get("caso"));
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
		// cargar tipo de caso(serializable)
		// TODO
		return null;
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
