package controlador;

import java.util.ArrayList;
import java.util.List;
import servidorcbr.modelo.Tecnica;

public class ControlTecnicas {

	public static List<Tecnica> getTecnicasRec () {
		List<Tecnica> lista = new ArrayList<Tecnica>();
		lista.add(new Tecnica("DiverseByMedianRetrieval"));
		lista.add(new Tecnica("FilterBasedRetrieval"));
		lista.add(new Tecnica("LuceneRetrieval"));
		lista.add(new Tecnica("NNretrieval"));
		return lista;
	}

	public static List<Tecnica> getTecnicasReu () {
		List<Tecnica> lista = new ArrayList<Tecnica>();
		lista.add(new Tecnica("CombineQueryAndCasesMethod"));
		lista.add(new Tecnica("DirectAttributeCopyMethod"));
		lista.add(new Tecnica("NumericDirectProportionMethod"));
		return lista;
	}

	public static List<Tecnica> getTecnicasRev () {
		List<Tecnica> lista = new ArrayList<Tecnica>();
		lista.add(new Tecnica("DefineNewIdsMethod"));
		return lista;
	}

	public static List<Tecnica> getTecnicasRet () {
		List<Tecnica> lista = new ArrayList<Tecnica>();
		lista.add(new Tecnica("StoreCasesMethod"));
		return lista;
	}
	
}
