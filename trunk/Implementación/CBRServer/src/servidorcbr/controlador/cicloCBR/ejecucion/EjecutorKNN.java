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

/**
 * Ejecutor concreto de la técnica de recuperación "K-vecinos más cercanos". Sirve de fachada
 * para las clases de jcolibri desde servidorcbr.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
public class EjecutorKNN extends EjecutorTecnicaRetrieval {

	/**
	 * Constructor de la clase. Recibe el tipo de caso sobre el que se va a aplicar.
	 * @param tic El tipo de caso.
	 */
	public EjecutorKNN(TipoCaso tic) {
		super(tic);
	}

	/* (non-Javadoc)
	 * @see servidorcbr.controlador.cicloCBR.ejecucion.EjecutorTecnicaRetrieval#ejecutar(java.util.Collection, jcolibri.cbrcore.CBRQuery)
	 */
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
		if (tc.getDefaultRec().getParams().isEmpty()) {
			return SelectCases.selectTopK(cr, 5);
		}
		return SelectCases.selectTopK(cr,(int) tc.getDefaultRec().getParams().get(0).getValor());
	}
	
}