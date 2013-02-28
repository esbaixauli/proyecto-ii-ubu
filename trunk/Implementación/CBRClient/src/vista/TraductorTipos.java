package vista;

import java.lang.Character.Subset;
import java.util.Locale;
import java.util.ResourceBundle;

public class TraductorTipos {

	public static String persistenciaAVista(String c){
		String res;
		if(c.equals("S")){
			res="string";
		}else if(c.equals("I")){
			res="integer";
		}else{
			res="decimal";
		}
		return res;
	}
	
	public static String vistaAPersistencia(String c){
		return c.substring(0,1);
		
	}
	
	private static ResourceBundle b = ResourceBundle.getBundle(
            "vista.internacionalizacion.Recursos", Locale.getDefault());
	
	public static String indiceATipo(int i){
		switch(i){
		case 0: return "I";
		case 1: return "S";
		default:return "D";
		}
	}
	
	
	public static String indiceAMétrica(int i, int tipo){
			
		if(tipo == 1){
			switch(i){
			case 0: return "equal";
			case 1: return "equalignorecase";
			default: return "substring";
			}
		}else{
			switch(i){
			case 0: return "equal";
			case 1: return "interval";
				default: return "threshold";
			}
		}
		
	}
}
