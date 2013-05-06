package servidorcbr.controlador.cicloCBR.ejecucion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import servidorcbr.controlador.generadorClases.CargadorClases;
import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.Parametro;
import servidorcbr.modelo.TipoCaso;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.retrieve.DiverseByMedianRetrieval.ExpertClerkMedianScoring;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.selection.SelectCases;

public class EjecutorDiverseByMedian extends EjecutorTecnicaRetrieval{
	
	public EjecutorDiverseByMedian(TipoCaso tc) {
		super(tc);
	}

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
	
	
	private HashMap<Attribute,Double> getUmbrales(HashMap<String,Attribute> attributes){
		HashMap<Attribute,Double> umbrales=new HashMap<Attribute, Double>();
		for(Parametro param :tc.getDefaultRec().getParams()){
			umbrales.put( attributes.get(param.getNombre()),param.getValor());
		}
		return umbrales;
	}
	
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
