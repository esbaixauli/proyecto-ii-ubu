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

public abstract class EjecutorTecnicaRetrieval {
	protected TipoCaso tc;
	
	public EjecutorTecnicaRetrieval(TipoCaso tc) {
		super();
		this.tc = tc;
	}
	
	public abstract Collection<CBRCase> 
	ejecutar(Collection<CBRCase> casos, CBRQuery query) throws ClassNotFoundException;
	
	protected NNConfig getSimilaridadGlobalConfig() throws ClassNotFoundException{
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
			}
		}
		config.setDescriptionSimFunction(new Average());
		return config;
	}
	
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
