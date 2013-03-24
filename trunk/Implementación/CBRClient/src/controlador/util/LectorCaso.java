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
		//por cada fila de las instancias (cada Instancia es un caso)
		for(int i=0; i<instancias.numInstances();i++){
			casos.add(new HashMap<String,Object>());
			Instance instancia=instancias.instance(i);
			for(int j = 0; j<tc.getAtbos().size();j++){
				insertarAtributo(instancia,i,j);
			}
			if(casos.get(i).isEmpty()){
				return null;
			}
		}
		return casos;
	}
	
	private static void insertarAtributo(Instance instancia,int i,int j){
		Attribute at = instancia.attribute(j);
		String valor = instancia.toString(j);
		String nombre = at.name();
		Object o=valor;
		if(tic.getAtbos().containsKey(nombre)){
			if(at.isNumeric()){
				String tipoCaso = tic.getAtbos().get(nombre).getTipo();
				if(tipoCaso.equals("I")){
					o=Integer.parseInt(valor);
				}else{
					o =Double.parseDouble(valor);
				}
			}
			casos.get(i).put(nombre,o);
		}
	} 
	
	
	
}
