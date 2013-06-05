package servidorcbr.controlador.cicloCBR.ejecucion;

import java.util.Collection;

import servidorcbr.controlador.generadorClases.CargadorClases;
import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.TipoCaso;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;

/**
 * Clase abstracta que describe cómo deben ser los ejecutores de técnicas de recuperación.
 * Para implementar nuevas técnicas de recuperación habría que heredar de esta clase.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
public abstract class EjecutorTecnicaRetrieval {
	
	/**
	 * El tipo de caso sobre el que se aplica la recuperación. 
	 */
	protected TipoCaso tc;
	
	/**
	 * Constructor de la clase. Recibe el tipo de caso sobre el que se va a aplicar.
	 * @param tc El tipo de caso.
	 */
	public EjecutorTecnicaRetrieval(TipoCaso tc) {
		super();
		this.tc = tc;
	}
	
	/**
	 * Método que deben implementar las subclases. Aplica la técnica de recuperación a un
	 * conjunto de casos, dado un query introducido por el usuario.
	 * @param casos El conjunto de casos sobre el que se aplica.
	 * @param query El query introducido por el usuario.
	 * @return Los casos más similares al query (de acuerdo a la técnica).
	 * @throws ClassNotFoundException Si no encuentra las clases que implementan CaseComponent.
	 */
	public abstract Collection<CBRCase>	ejecutar(Collection<CBRCase> casos, CBRQuery query)
			throws ClassNotFoundException;
	
	/**
	 * Crea un objeto de configuración NNConfig, que almacena mapeos entre cada atributo del
	 * caso y la métrica con la que hay que compararlo.
	 * @return El objeto NNConfig.
	 * @throws ClassNotFoundException Si no encuentra la clase de descripción del problema.
	 */
	protected NNConfig getSimilaridadGlobalConfig() throws ClassNotFoundException {
		NNConfig config = new NNConfig();
		Attribute at;
		Class<?> clase;
		try {
			clase = CargadorClases.cargarClaseProblema(tc.getNombre());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw e;
		}
		for(Atributo actual: tc.getAtbos().values()){
			if(actual.getEsProblema()){
				at = new Attribute(actual.getNombre(),clase);//Nombre del atbo, *.class al que pertenece
				config.addMapping(at,ConversorMetricas.obtenerMetrica(actual));
				config.setWeight(at, actual.getPeso());
				Attribute a = new Attribute(actual.getNombre(), clase);
			}
		}
		config.setDescriptionSimFunction(new Average());
		return config;
	}
	
	/**
	 * Método fachada que instancia la subclase que corresponde a la técnica de recuperación
	 * configurada en el tipo de caso y llama a su método ejecutar.
	 * @param casos Los casos sobre los que hay que ejecutar la recuperación.
	 * @param query El query introducido por el usuario.
	 * @param tc El tipo de caso con su configuración.
	 * @return Los casos más similares al query (de acuerdo a la técnica de recuperación).
	 */
	public static Collection<CBRCase> ejecutarRetrieval(Collection<CBRCase> casos,
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

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return casos;
	}

}
