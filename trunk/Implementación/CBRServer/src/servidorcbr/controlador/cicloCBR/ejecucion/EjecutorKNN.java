package servidorcbr.controlador.cicloCBR.ejecucion;

import java.util.Collection;

import org.apache.catalina.tribes.util.TcclThreadFactory;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import servidorcbr.controlador.CargadorClases;
import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.Tecnica;
import servidorcbr.modelo.TipoCaso;

public class EjecutorKNN implements EjecutorTecnica {

	private static TipoCaso tc;

	public EjecutorKNN(TipoCaso tc) {
		this.tc = tc;
	}

	@Override
	public void ejecutar(Collection<CBRCase> casos, CBRQuery query) throws ClassNotFoundException {
		NNConfig simConfig = getSimilaridadGlobalConfig();
		simConfig.setDescriptionSimFunction(new Average());
		NNScoringMethod.evaluateSimilarity(casos,query,simConfig);
	}

	public static NNConfig getSimilaridadGlobalConfig() throws ClassNotFoundException{
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
		return config;
	}
}
