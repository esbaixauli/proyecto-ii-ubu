package servidorcbr.controlador.cicloCBR.ejecucion;

import servidorcbr.modelo.Atributo;
import jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import jcolibri.method.retrieve.NNretrieval.similarity.local.EqualsStringIgnoreCase;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import jcolibri.method.retrieve.NNretrieval.similarity.local.MaxString;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Threshold;

//Permite obtener objetos Métrica de jColibri. Esta clase debe ser ampliada si se desean introducir nuevas metricas.
public class ConversorMetricas {
	
	public static LocalSimilarityFunction obtenerMetrica(Atributo at){
		switch(at.getMetrica()){
			case "equalignorecase":return new EqualsStringIgnoreCase();
			case "substring": return new MaxString();
			case "interval": return new Interval(at.getParamMetrica());
			case "threshold": return new Threshold(at.getParamMetrica());
			default: return new Equal();
		}
	}

}
