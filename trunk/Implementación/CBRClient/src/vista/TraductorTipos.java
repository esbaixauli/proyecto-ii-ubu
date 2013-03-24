package vista;


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
	
	
	public static String indiceATipo(int i){
		switch(i){
		case 0: return "I";
		case 1: return "S";
		default:return "D";
		}
	}
	
	public static String indiceAMétrica(int i, String tipo){
		if(tipo.equals("S")){
			return indiceAMétrica(i,1);
		}else{
			return indiceAMétrica(i,0);
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
