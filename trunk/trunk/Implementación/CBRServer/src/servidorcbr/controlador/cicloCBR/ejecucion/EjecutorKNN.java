package servidorcbr.controlador.cicloCBR.ejecucion;

import java.util.Collection;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import jcolibri.method.retrieve.selection.SelectCases;
import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.TipoCaso;

public class EjecutorKNN extends EjecutorTecnicaRetrieval {


	public EjecutorKNN(TipoCaso tic) {
		super(tic);
	}

	@Override
	public Collection<CBRCase> ejecutar(Collection<CBRCase> casos, CBRQuery query) throws ClassNotFoundException {
		NNConfig simConfig = getSimilaridadGlobalConfig();
		Attribute[] atts = null;
		for (CBRCase caso : casos) {
			atts = jcolibri.util.AttributeUtils.getAttributes(caso.getDescription().getClass());
			break;
		}
		for (Attribute at : atts) {
			Atributo a;
			if(tc.getAtbos().containsKey(at.getName())) {
				a = tc.getAtbos().get(at.getName());
				simConfig.addMapping(at, ConversorMetricas.obtenerMetrica(a));
			}
		}
		Collection<RetrievalResult> cr = NNScoringMethod.evaluateSimilarity(casos,query,simConfig);
		return SelectCases.selectTopK(cr,(int) tc.getDefaultRec().getParams().get(0).getValor());
	}
	
}