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
			case "equalignorecase":return new EqualsStringIgnoreCase();break;
			case "substring": return new MaxString();break;
			case "interval": return new Interval(at.getParamMetrica());break;
			case "threshold": return new Threshold(at.getParamMetrica());break;
			default: return new Equal();
		}
	}

}
