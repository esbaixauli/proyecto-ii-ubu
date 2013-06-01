package servidorcbr.controlador.cicloCBR.ejecucion;

import servidorcbr.modelo.Atributo;
import jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import jcolibri.method.retrieve.NNretrieval.similarity.local.EqualsStringIgnoreCase;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import jcolibri.method.retrieve.NNretrieval.similarity.local.MaxString;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Threshold;

/**
 * Clase que convierte las cadenas que describen las métricas en la clase TipoCaso a las clases
 * que implementan las métricas en sí en el framework jColibri.
 * Esta clase debe ser ampliada si se desean introducir nuevas metricas.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
public class ConversorMetricas {
	
	/**
	 * Devuelve una instancia de la clase que implementa la métrica con la que está configurado
	 * un atributo. Todas estas clases implementan la interfaz LocalSimilarityFunction.
	 * @param at El atributo del que se quiere obtener la métrica.
	 * @return Una instancia de la clase correspondiente a la métrica.
	 */
	public static LocalSimilarityFunction obtenerMetrica(Atributo at){
		switch(at.getMetrica()){
			case "equalignorecase":
				return new EqualsStringIgnoreCase();
			case "substring":
				return new MaxString();
			case "interval":
				return new Interval(at.getParamMetrica());
			case "threshold":
				return new Threshold(at.getParamMetrica());
			default:
				return new Equal();
		}
	}

}
