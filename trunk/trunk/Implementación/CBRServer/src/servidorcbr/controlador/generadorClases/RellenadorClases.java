package servidorcbr.controlador.generadorClases;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.hadoop.hbase.util.Bytes;

import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.Calidad;
import servidorcbr.modelo.TipoCaso;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.CaseComponent;

//Clase de utilleria, que permite rellenar las clases de un tipo de caso con sus valores
public class RellenadorClases {


	/** Adapta varios conjuntos de atributos con valores a una lista de CBRCases de jColibri.
	 * @param tc El tipo de caso al que pertenecen dichos atributos.
	 * @param c los valores a adaptar.
	 * @return una lista de CBRCases.
	 * @throws ClassNotFoundException Si la clase correspondiente a ese tipo de caso no se halla en el sistema de ficheros.
	 * @see {@link CBRCase}
	 */
	public static List<CBRCase>	rellenarLista(TipoCaso tc,List<HashMap<String,Serializable>> c)
			throws ClassNotFoundException{
		List<CBRCase> lista = new ArrayList<CBRCase>();
		//Cargo las clases del problema y la solucion
		Class<? extends CaseComponent> desc=CargadorClases.cargarClaseProblema(tc.getNombre());
		Class<? extends CaseComponent> sol=CargadorClases.cargarClaseSolucion(tc.getNombre());
		try{
			//Por cada instancia de caso
			for(HashMap<String, Serializable> caso:c){
				CBRCase cbrCase = obtenerCaso(tc, caso, desc, sol);
				lista.add(cbrCase);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return lista;
	}
	
	/**Adapta un conjunto de atributos con valores a un CBRCase de jColibri.
	 * @param tc  El tipo de caso al que pertenecen dichos atributos.
	 * @param caso El valor a adaptar.
	 * @return un CBRCase
	 * @throws ClassNotFoundException Si la clase correspondiente a ese tipo de caso no se halla en el sistema de ficheros.
	 * @see {@link CBRCase}
	 */
	public static CBRCase rellenarCaso(TipoCaso tc, HashMap<String,Serializable> caso)
			throws ClassNotFoundException {
		//Cargo las clases del problema y la solucion
		Class<? extends CaseComponent> desc=CargadorClases.cargarClaseProblema(tc.getNombre());
		Class<? extends CaseComponent> sol=CargadorClases.cargarClaseSolucion(tc.getNombre());
		try{
			return obtenerCaso(tc, caso, desc, sol);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	
	public static CBRQuery rellenarQuery(TipoCaso tc, HashMap<String,Serializable> query) {
		CBRQuery q = new CBRQuery();
		Class<? extends CaseComponent> desc = null;
		Class<?> tipo = null;
		try {
			desc = CargadorClases.cargarClaseProblema(tc.getNombre());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		CaseComponent instanciaDesc = null;
		try {
			instanciaDesc = desc.newInstance();
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		}
		for (Entry<String,Serializable> par : query.entrySet()) {
			// Comprobación por si en el hashmap hay calidad: un query NO TIENE calidad
			if (!par.getKey().equals("META_QUALITY")) {
				Atributo a = tc.getAtbos().get(par.getKey());
				tipo = getTipoClass(a);
				try {
					desc.getDeclaredMethod("set"+a.getNombre().substring(0,1).toUpperCase()+
								a.getNombre().substring(1),tipo).invoke(instanciaDesc,par.getValue());
				} catch (Exception e) {
				}
			}
		}
		q.setDescription(instanciaDesc);
		return q;
	}
	
	public static HashMap<String,Serializable> rellenarHash(TipoCaso tc, CBRCase caso) {
		HashMap<String,Serializable> campos = new HashMap<String,Serializable>();
		for (Atributo a : tc.getAtbos().values()) {
			CaseComponent cc = null;
			Class<?> cclass = null;
			if (a.getEsProblema()) {
				cc = caso.getDescription();
			} else {
				cc = caso.getSolution();
			}
			Serializable s = null;
			try {
				s = (Serializable) cc.getClass().getMethod(
						"get"+a.getNombre().substring(0, 1).toUpperCase()+a.getNombre().substring(1, a.getNombre().length()), 
						(Class<?>[]) null
						).invoke(cc, (Object[]) null);
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException
					| SecurityException e) {
				e.printStackTrace();
			}
			campos.put(a.getNombre(), s);
		}
		Calidad cal = (Calidad) caso.getJustificationOfSolution();
		if (cal != null) {
			campos.put("META_QUALITY", cal.getCalidad());
		}
		return campos;
	}
	
	private static CBRCase obtenerCaso(TipoCaso tc, HashMap<String, Serializable> caso, Class<? extends CaseComponent>desc, Class<? extends CaseComponent>sol) {
		//Creo una instancia del problema y de la solucion
		CaseComponent instanciaDesc = null;
		CaseComponent instanciaSol = null;
		try {
			instanciaDesc = desc.newInstance();
			instanciaSol = sol.newInstance();
		} catch (InstantiationException e1) {
		} catch (IllegalAccessException e1) {
		}
		Class<?> tipo;
		CBRCase cbrCase = new CBRCase();
		//Por cada par atributo-valor
		for(Entry<String, Serializable> par  :caso.entrySet()){
			//Si dicho atributo esta contenido en el tipo de caso
			if(tc.getAtbos().containsKey(par.getKey())){
				// Al atributo META_ID no hay que hacerle nada, está ahí para cumplir la 
				// especificación de jcolibri (pero su valor da igual)
				if (!par.getKey().equals("META_ID")) {
					Atributo a = tc.getAtbos().get(par.getKey());
					tipo = getTipoClass(a);
					//Si es un problema invoca al setter de dicho atributo en la clase del problema
					try {
						if(a.getEsProblema()){
							desc.getDeclaredMethod("set"+a.getNombre().substring(0,1).toUpperCase()+
									a.getNombre().substring(1),tipo).invoke(instanciaDesc,par.getValue());
						}else{
							sol.getDeclaredMethod("set"+a.getNombre().substring(0,1).toUpperCase()+
									a.getNombre().substring(1),tipo).invoke(instanciaSol,par.getValue());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else if (par.getKey().equals("META_QUALITY")) {
				Calidad cal = new Calidad();
				cal.setCalidad((Integer) par.getValue());
				cbrCase.setJustificationOfSolution(cal);
			}
		}
		//Añado el caso a la lista
		cbrCase.setDescription(instanciaDesc);
		cbrCase.setSolution(instanciaSol);
		return cbrCase;
	}

	private static Class<?> getTipoClass(Atributo a) {
		Class<?> tipo;
		switch(a.getTipo()){
			case "S":tipo=String.class;break;
			case "I":tipo=Integer.class;break;
			case "D":tipo=Double.class;break;
			default:tipo=String.class;
		}
		return tipo;
	}
	
}
