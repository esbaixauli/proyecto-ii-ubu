package controlador.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import servidorcbr.modelo.TipoCaso;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class LectorCaso {
	
	private static List<HashMap<String,Object>> casos;
	private static TipoCaso tic;
	
	public static List<HashMap<String,Object>> leerCaso(File f,TipoCaso tc) throws IOException {
		BufferedReader reader = new BufferedReader(
                new FileReader(f));
		casos = new ArrayList<HashMap<String,Object>>();
		Instances instancias = new Instances(reader);
		tic=tc;
		if(instancias.numInstances()<=0 || instancias.instance(0).numAttributes()!= tc.getAtbos().size()+1){
			return null;
		}
		//por cada fila de las instancias (cada Instancia es un caso)
		for(int i=0; i<instancias.numInstances();i++){
			casos.add(new HashMap<String,Object>());
			Instance instancia=instancias.instance(i);
			//size +1 por la calidad
			for(int j = 0; j<tc.getAtbos().size()+1;j++){
				insertarAtributo(instancia,i,j);
			}
			//de nuevo, tc.getAtbos().size()+1, es decir, numero de atributos
			//+ 1 atbo que es la calidad
			if(casos.get(i).size() != tc.getAtbos().size()+1 ){
				return null;
			}
		}
		return casos;
	}
	
	/**Inserta un atributo como par clave-valor en el hashmap de la fila correspondiente.
	 * @param instancia Instancia correspondiente a un caso, con todos los atributos del caso.
	 * @param i Fila correspondiente al caso.
	 * @param j Columna del atributo a insertar.
	 */
	private static void insertarAtributo(Instance instancia,int i,int j){
		Attribute at = instancia.attribute(j);
		String valor = instancia.toString(j);
		String nombre = at.name();
		Object o=valor;
		if(tic.getAtbos().containsKey(nombre)){
			if(at.isNumeric()){
				try{
					String tipoCaso = tic.getAtbos().get(nombre).getTipo();
					o =Double.parseDouble(valor);
					if(tipoCaso.equals("I")){
						o=Math.round((Double) o);
					}
				}catch(NumberFormatException ex){
					return;
				}
			}
			casos.get(i).put(nombre,o);
		}else if(nombre.equalsIgnoreCase("META_QUALITY") && at.isNumeric() ){
			casos.get(i).put("META_QUALITY",o);
		}
	} 
	
	
	
}
