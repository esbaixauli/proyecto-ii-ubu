package servidorcbr.controlador.generadorClases;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.CaseComponent;
import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.Calidad;
import servidorcbr.modelo.TipoCaso;

/**
 * Clase de utilería que permite rellenar las clases generadas dinámicamente con sus valores
 * concretos. También proporciona métodos para convertir una instancia de estas clases en un
 * HashMap.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
public class RellenadorClases {

	/** 
	 * Adapta varios conjuntos de atributos con valores a una lista de CBRCases de jColibri.
	 * @param tc El tipo de caso al que pertenecen dichos atributos.
	 * @param c los valores a adaptar.
	 * @return Una lista de CBRCases.
	 * @throws ClassNotFoundException Si no encuentra la clase correspondiente a ese tipo de caso.
	 * @see jcolibri.cbrcore.CBRCase
	 */
	public static List<CBRCase>	rellenarLista(TipoCaso tc,List<HashMap<String,Serializable>> c, CBRQuery q)
			throws ClassNotFoundException{
		List<CBRCase> lista = new ArrayList<CBRCase>();
		//Cargo las clases del problema y la solucion
		Class<? extends CaseComponent> desc = q.getDescription().getClass();
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
	
	/**
	 * Adapta un conjunto de atributos con valores a un CBRCase de jColibri.
	 * @param tc  El tipo de caso al que pertenecen dichos atributos.
	 * @param caso El conjunto de valores a adaptar.
	 * @return Un CBRCase con los valores contenidos en el caso.
	 * @throws ClassNotFoundException Si no encuentra la clase correspondiente a ese tipo de caso.
	 * @see jcolibri.cbrcore.CBRCase
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
	
	/**
	 * Rellena un CBRQuery de jColibri con los valores contenidos en un HashMap.
	 * @param tc El tipo de caso que define el query a rellenar.
	 * @param query El conjunto de valores con los que rellenar el query.
	 * @return Un objeto de tipo CBRQuery con los valores del query proporcionado.
	 */
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
			Atributo a = tc.getAtbos().get(par.getKey());
			tipo = getTipoClass(a);
			try {
				if (a.getTipo().equals("I")) {
					desc.getDeclaredMethod("set"+a.getNombre().substring(0,1).toUpperCase()+
							a.getNombre().substring(1),tipo).invoke(instanciaDesc,Integer.parseInt(par.getValue().toString()));
				} else { 
					desc.getDeclaredMethod("set"+a.getNombre().substring(0,1).toUpperCase()+
						a.getNombre().substring(1),tipo).invoke(instanciaDesc,par.getValue());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		q.setDescription(instanciaDesc);
		return q;
	}
	
	/**
	 * Rellena un HashMap (nombre de atributo -> valor del atributo) con los valores contenidos
	 * en un CBRCase.
	 * @param tc El tipo de caso que define la estructura del caso.
	 * @param caso El CBRCase a convertir a HashMap.
	 * @return El HashMap que contiene los valores contenidos en el CBRCase.
	 */
	public static HashMap<String,Serializable> rellenarHash(TipoCaso tc, CBRCase caso) {
		HashMap<String,Serializable> campos = new HashMap<String,Serializable>();
		for (Atributo a : tc.getAtbos().values()) {
			CaseComponent cc = null;
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
	
	/**
	 * Rellena un objeto de tipo CBRCase con los valores contenidos en un HashMap.
	 * @param tc El tipo de caso que define la estructura del caso.
	 * @param caso El HashMap con los valores del caso concreto.
	 * @param desc La clase que implementa CaseComponent y define la Description del caso.
	 * @param sol La clase que implementa CaseComponent y define la Solution del caso.
	 * @return El CBRCase con los valores contenidos en el HashMap.
	 */
	private static CBRCase obtenerCaso(TipoCaso tc, HashMap<String, Serializable> caso,
			Class<? extends CaseComponent> desc, Class<? extends CaseComponent> sol) {
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

	/**
	 * Obtiene la clase que envuelve el tipo de datos del atributo a.
	 * @param a El atributo del que se quiere obtener el tipo.
	 * @return La clase que envuelve el tipo de datos.
	 */
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
