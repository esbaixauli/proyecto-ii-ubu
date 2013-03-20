package servidorcbr.controlador.generadorClases;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.TipoCaso;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CaseComponent;

//Clase de utilleria, que permite rellenar las clases de un tipo de caso con sus valores
public class RellenadorClases {


	/** Adapta una lista de atributos con valores a una lista de CBRCases de jColibri.
	 * @param tc El tipo de caso al que pertenecen dichos atributos.
	 * @param c los valores a adaptar.
	 * @return una lista de CBRCases.
	 * @throws ClassNotFoundException
	 * @see {@link CBRCase}
	 */
	public static List<CBRCase>	rellenarLista(TipoCaso tc,List<HashMap<String,Object>> c)
			throws ClassNotFoundException{
		List<CBRCase> lista = new ArrayList<CBRCase>();
		//Cargo las clases del problema y la solucion
		Class<?> desc=CargadorClases.cargarClaseProblema(tc.getNombre());
		Class<?> sol=CargadorClases.cargarClaseProblema(tc.getNombre());
		Class<?> tipo;
		try{
			//Por cada instancia de caso
			for(HashMap<String, Object> caso:c){
				//Creo una instancia del problema y de la solucion
				CaseComponent instanciaDesc = (CaseComponent) desc.newInstance();
				CaseComponent instanciaSol = (CaseComponent) sol.newInstance();
				//Por cada par atributo-valor
				for(Entry<String, Object> par  :caso.entrySet()){
					//Si dicho atributo esta contenido en el tipo de caso
					if(tc.getAtbos().containsKey(par.getKey())){
						Atributo a = tc.getAtbos().get(par.getKey());
						switch(a.getTipo()){
							case "S":tipo=String.class;break;
							case "I":tipo=Integer.class;break;
							case "D":tipo=Double.class;break;
							default:tipo=String.class;
						}
						//Si es un problema invoca al setter de dicho atributo en la clase del problema
						if(a.getEsProblema()){
							desc.getDeclaredMethod("set"+a.getNombre().substring(0,1).toUpperCase()+
									a.getNombre().substring(1),tipo).invoke(instanciaDesc,par.getValue());
							//Si además es el id del componente
							if(a.getNombre().equals("id")){	
								desc.getDeclaredMethod("setIdAttribute",Attribute.class)
								.invoke(instanciaDesc,new Attribute("id", instanciaDesc.getClass()));
							}
							//Si no en la de la solución
						}else{
							desc.getDeclaredMethod("set"+a.getNombre().substring(0,1).toUpperCase()+
									a.getNombre().substring(1),tipo).invoke(instanciaSol,par.getValue());
							if(a.getNombre().equals("id")){
								desc.getDeclaredMethod("setIdAttribute",Attribute.class).
								invoke(instanciaSol,new Attribute("id", instanciaSol.getClass()));
							}
						}
					
					}
				}
				//Añado el caso a la lista
				CBRCase cbrCase = new CBRCase();
				cbrCase.setDescription(instanciaDesc);
				cbrCase.setSolution(instanciaSol);
				lista.add(cbrCase);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return lista;
	}
	
}
