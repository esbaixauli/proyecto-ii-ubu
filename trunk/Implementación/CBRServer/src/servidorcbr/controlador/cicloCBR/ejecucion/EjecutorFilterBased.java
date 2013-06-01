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
import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.Parametro;
import servidorcbr.modelo.TipoCaso;

/**
 * Ejecutor concreto de la técnica de recuperación "FilterBased". Sirve de fachada para
 * las clases de jcolibri desde servidorcbr.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
public class EjecutorFilterBased extends EjecutorTecnicaRetrieval {

	/**
	 * Constructor de la clase. Recibe el tipo de caso sobre el que se va a aplicar.
	 * @param tc El tipo de caso.
	 */
	public EjecutorFilterBased(TipoCaso tc) {
		super(tc);
	}

	/* (non-Javadoc)
	 * @see servidorcbr.controlador.cicloCBR.ejecucion.EjecutorTecnicaRetrieval#ejecutar(java.util.Collection, jcolibri.cbrcore.CBRQuery)
	 */
	@Override
	public Collection<CBRCase> ejecutar(Collection<CBRCase> casos,
			CBRQuery query) throws ClassNotFoundException {
		FilterConfig simConfig=getFiltros(query);
		
		return FilterBasedRetrievalMethod.filterCases(casos, query, simConfig);
	}
	
	/**
	 * Construye un objeto de tipo FilterConfig, que contiene información sobre el filtro a
	 * aplicar sobre cada atributo.
	 * @param query Query introducido por el usuario.
	 * @return El FilterConfig que mapea Attribute -> filtro a utilizar.
	 */
	private FilterConfig getFiltros(CBRQuery query) {
		FilterConfig config = new FilterConfig();
		Class<?> clase = query.getDescription().getClass();
		for(Parametro p : tc.getDefaultRec().getParams()){
			Atributo actual = tc.getAtbos().get(p.getNombre());
			Field f = null;
			if(actual.getEsProblema()){
				try {
					f = clase.getDeclaredField(actual.getNombre());
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				}
				Attribute at = new Attribute(f);
				config.addPredicate(at, getFilterPredicate(p.getValor()));
			}
		}
		return config;
	}

	/** 
	 * Traduce un predicado de su representación interna a su clase correspondiente.
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
