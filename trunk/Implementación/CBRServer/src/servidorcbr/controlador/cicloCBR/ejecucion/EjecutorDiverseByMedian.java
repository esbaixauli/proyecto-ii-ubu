package servidorcbr.controlador.cicloCBR.ejecucion;

import java.util.Collection;
import java.util.HashMap;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.method.retrieve.DiverseByMedianRetrieval.ExpertClerkMedianScoring;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.selection.SelectCases;
import servidorcbr.controlador.generadorClases.CargadorClases;
import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.Parametro;
import servidorcbr.modelo.TipoCaso;

/**
 * Ejecutor concreto de la técnica de recuperación "DiverseByMedian". Sirve de fachada para
 * las clases de jcolibri desde servidorcbr.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
public class EjecutorDiverseByMedian extends EjecutorTecnicaRetrieval{
	
	/**
	 * Constructor de la clase. Recibe el tipo de caso sobre el que se ejecutará la recuperación.
	 * @param tc El tipo de caso.
	 */
	public EjecutorDiverseByMedian(TipoCaso tc) {
		super(tc);
	}

	/* (non-Javadoc)
	 * @see servidorcbr.controlador.cicloCBR.ejecucion.EjecutorTecnicaRetrieval#ejecutar(java.util.Collection, jcolibri.cbrcore.CBRQuery)
	 */
	@Override
	public Collection<CBRCase> ejecutar(Collection<CBRCase> casos,
			CBRQuery query) throws ClassNotFoundException {
		HashMap<Attribute,Double> umbrales=null; 
		HashMap<String,Attribute> h= new HashMap<String,Attribute>();
		NNConfig simConfig = getSimilaridadGlobalConfig(h);
		umbrales = getUmbrales(h);
		simConfig.setDescriptionSimFunction(new Average());
		Collection<CBRCase> filtrada = SelectCases.selectTopK(ExpertClerkMedianScoring.getDiverseByMedian(casos, simConfig, umbrales), 1);
		return filtrada;
	}
	
	
	/**
	 * Construye un HashMap que mapea cada Attribute que forma parte del tipo de caso con el
	 * Double que representa el umbral configurado para ese atributo en el tipo de caso (si es
	 * que lo hay). 
	 * @param attributes Un HashMap que mapea el nombre de cada atributo con su objeto Attribute.
	 * @return El hashmap con Attribute y umbrales.
	 */
	private HashMap<Attribute,Double> getUmbrales(HashMap<String,Attribute> attributes){
		HashMap<Attribute,Double> umbrales=new HashMap<Attribute, Double>();
		for(Parametro param :tc.getDefaultRec().getParams()){
			umbrales.put( attributes.get(param.getNombre()),param.getValor());
		}
		return umbrales;
	}
	
	/**
	 * Sobrecarga el método definido en la superclase para recibir un HashMap de String (nombre
	 * del atributo) a Attribute, que rellena con la información contenida en el tipo de caso.
	 * @param h El HashMap que mapea nombre de atributo -> objeto de la clase Attribute.
	 * @return El objeto NNConfig que mapea Attribute -> métrica a utilizar para compararlo.
	 * @throws ClassNotFoundException Si no encuentra la clase de descripción del problema.
	 */
	private NNConfig getSimilaridadGlobalConfig(HashMap<String,Attribute> h) throws ClassNotFoundException{
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
				h.put(actual.getNombre(),at);
			}
		}
		return config;
	}
	
}
