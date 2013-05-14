package servidorcbr.controlador.cicloCBR.ejecucion;

import java.lang.reflect.Field;
import java.util.Collection;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.method.retrieve.FilterBasedRetrieval.FilterBasedRetrievalMethod;
import jcolibri.method.retrieve.FilterBasedRetrieval.FilterConfig;
import jcolibri.method.retrieve.FilterBasedRetrieval.predicates.Equal;
import jcolibri.method.retrieve.FilterBasedRetrieval.predicates.FilterPredicate;
import jcolibri.method.retrieve.FilterBasedRetrieval.predicates.NotEqual;
import jcolibri.method.retrieve.FilterBasedRetrieval.predicates.OntologyCompatible;
import jcolibri.method.retrieve.FilterBasedRetrieval.predicates.QueryLess;
import jcolibri.method.retrieve.FilterBasedRetrieval.predicates.QueryLessOrEqual;
import jcolibri.method.retrieve.FilterBasedRetrieval.predicates.QueryMore;
import jcolibri.method.retrieve.FilterBasedRetrieval.predicates.QueryMoreOrEqual;
import jcolibri.method.retrieve.FilterBasedRetrieval.predicates.Threshold;
import servidorcbr.controlador.generadorClases.CargadorClases;
import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.Parametro;
import servidorcbr.modelo.TipoCaso;

public class EjecutorFilterBased extends EjecutorTecnicaRetrieval {

	public EjecutorFilterBased(TipoCaso tc) {
		super(tc);
	}

	@Override
	public Collection<CBRCase> ejecutar(Collection<CBRCase> casos,
			CBRQuery query) throws ClassNotFoundException {
		FilterConfig simConfig=getFiltros(query);
		
		return FilterBased.filterCases(casos, query, simConfig);
	}
	
	private FilterConfig getFiltros(CBRQuery query) {
		FilterConfig config = new FilterConfig();
		Class<?> clase = query.getDescription().getClass();
		for(Parametro p : tc.getDefaultRec().getParams()){
			Atributo actual = tc.getAtbos().get(p.getNombre());
			Field f = null;
			if(actual.getEsProblema()){
				try {
					f = query.getDescription().getClass().getDeclaredField(actual.getNombre());
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				}
				Attribute at = new Attribute(f);
				config.addPredicate(at, getFilterPredicate(p.getValor()));
				System.out.println("Predicado para: " + at.getName() + "(hc "+at.hashCode()+")" + config.getPredicate(at));
			}
		}
		return config;
	}

	/** Auxiliar. Traduce un predicado de su representación interna a su clase correspondiente.
	 * 0: Equal
	 * 1: NotEqual
	 * 2: OntologyCompatible
	 * 3: QueryLess
	 * 4: QueryLessOrEqual
	 * 5: QueryMore
	 * 6: QueryMoreOrEqual
	 * 7+: Threshold (el umbral será el valor del decimal -7)
	 * @param valor Entero que representa al predicado.
	 * @return FilterPredicate correspondiente.
	 */
	private FilterPredicate getFilterPredicate(double valor){
		FilterPredicate fp=new Equal();
		switch((int) valor){
			case 0:;break;
			case 1:fp=new NotEqual();break;
			case 2:fp=new OntologyCompatible();break;
			case 3:fp=new QueryLess();break;
			case 4:fp=new QueryLessOrEqual();break;
			case 5:fp=new QueryMore();break;
			case 6:fp=new QueryMoreOrEqual();break;
		}
		if(valor>=7){
			double param = valor - 7;
			fp=new Threshold(param);
		}
		
		return fp;
	}
	
}

