package controlador.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import servidorcbr.modelo.TipoCaso;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

/** Clase de utillería para leer casos de ficheros WEKA arff.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
public class LectorCaso {
	
	/**
	 * Lista de casos leidos actualmente.
	 */
	private static List<HashMap<String,Serializable>> casos;
	/**
	 * Tipo de caso asociado al lector.
	 */
	private static TipoCaso tic;
	
	/** Lee los casos de un tipo de caso concreto, procedentes de un fichero arff de WEKA.
	 * @param f Referencia al fichero del que leer.
	 * @param tc Tipo de caso del que se quieren leer casos.
	 * @return Lista de casos leídos. Cada caso se encapsula en un mapa clave-valor del tipo
	 * {"Nombre del atributo",valor del atributo}.
	 * @throws IOException En caso de error de lectura del fichero.
	 */
	public static List<HashMap<String,Serializable>> leerCaso(File f,TipoCaso tc) throws IOException {
		BufferedReader reader = new BufferedReader(
                new FileReader(f));
		casos = new ArrayList<HashMap<String,Serializable>>();
		Instances instancias = new Instances(reader);
		tic=tc;
		if(instancias.numInstances()<=0 || instancias.instance(0).numAttributes()!= tc.getAtbos().size()+1){
			return null;
		}
		//por cada fila de las instancias (cada Instancia es un caso)
		for(int i=0; i<instancias.numInstances();i++){
			casos.add(new HashMap<String,Serializable>());
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
		Serializable s=valor;
		if(tic.getAtbos().containsKey(nombre)){
			if(at.isNumeric()){
				try{
					String tipoCaso = tic.getAtbos().get(nombre).getTipo();
					s =Double.parseDouble(valor);
					if(tipoCaso.equals("I")){
						s = new Integer((int) Math.round((Double) s));
					}
				}catch(NumberFormatException ex){
					ex.printStackTrace();
					return;
				}
			}
			casos.get(i).put(nombre,s);
		}else if(nombre.equalsIgnoreCase("META_QUALITY") && at.isNumeric() ){
			casos.get(i).put("META_QUALITY", Integer.parseInt(s+""));
		}
	} 
	
	
	
}
