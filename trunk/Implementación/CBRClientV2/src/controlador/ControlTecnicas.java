package controlador;

import java.util.ArrayList;
import java.util.List;
import servidorcbr.modelo.Tecnica;

/** Controlador para las técnicas de un tipo de caso. Ofrece métodos
 * para recuperar los tipos y buscarlos.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
public class ControlTecnicas {

	/**Obtiene las técnicas de recuperación.
	 * @return lista de técnicas.
	 */
	public static List<Tecnica> getTecnicasRec () {
		List<Tecnica> lista = new ArrayList<Tecnica>();
		lista.add(new Tecnica("DiverseByMedianRetrieval"));
		lista.add(new Tecnica("FilterBasedRetrieval"));
		lista.add(new Tecnica("NNretrieval"));
		return lista;
	}
	/**Obtiene las técnicas de reutilización.
	 * @return lista de técnicas.
	 */
	public static List<Tecnica> getTecnicasReu () {
		List<Tecnica> lista = new ArrayList<Tecnica>();
		lista.add(new Tecnica("CombineQueryAndCasesMethod"));
		lista.add(new Tecnica("DirectAttributeCopyMethod"));
		lista.add(new Tecnica("NumericDirectProportionMethod"));
		return lista;
	}
	/**Obtiene las técnicas de revisión.
	 * @return lista de técnicas.
	 */
	public static List<Tecnica> getTecnicasRev () {
		List<Tecnica> lista = new ArrayList<Tecnica>();
		lista.add(new Tecnica("DefineNewIdsMethod"));
		return lista;
	}
	/**Obtiene las técnicas de retención.
	 * @return lista de técnicas.
	 */
	public static List<Tecnica> getTecnicasRet () {
		List<Tecnica> lista = new ArrayList<Tecnica>();
		lista.add(new Tecnica("StoreCasesMethod"));
		return lista;
	}
	
	/**
	 * Devuelve una lista con los posibles predicados a aplicar a un atributo mediante FilterBased.
	 * Los predicados se codifican en la bd en un decimal de la siguiente forma:
	 * 0: Equal
	 * 1: NotEqual
	 * 2: OntologyCompatible
	 * 3: QueryLess
	 * 4: QueryLessOrEqual
	 * 5: QueryMore
	 * 6: QueryMoreOrEqual
	 * 7+: Threshold (el umbral será el valor del decimal -7)
	 * @return Lista de predicados implementados en JColibri.
	 */
	public static List<String> getFilterPredicates () {
		List<String> lista = new ArrayList<String>();
		lista.add("Equal");
		lista.add("NotEqual");
		lista.add("OntologyCompatible");
		lista.add("QueryLess");
		lista.add("QueryLessOrEqual");
		lista.add("QueryMore");
		lista.add("QueryMoreOrEqual");
		lista.add("Threshold");
		return lista;
	}
	
	/** Busca si una técnica está presente en un conjunto de técnicas.
	 * @param ltc Lista de técnicas.
	 * @param buscada Nombre de la técnica buscada.
	 * @return La técnica si se encontró, o null si no.
	 */
	public static Tecnica buscaTecnica(List<Tecnica> ltc, String buscada){
		for(Tecnica t :ltc){
			if(t.getNombre().equals(buscada)){
				return t;
			}
		}
		return null;
	}
	
}
