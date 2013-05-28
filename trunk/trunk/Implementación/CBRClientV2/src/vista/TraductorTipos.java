package vista;


/**Clase de utillería para traducir de la representación de tipos de atributo
 * de la vista p.Ej:"Entero"/"Integer" a la representación interna del programa.
 * p.Ej: "I".
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
public class TraductorTipos {

	/**Traduce un tipo de datos de la representación interna a la de la vista
	 * (pre-internacionalización).
	 * @param c S,I o D según el tipo de datos.
	 * @return Representación de esa cadena en la vista, independiente del lenguaje.
	 */
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
	
	/**Traduce un tipo de datos de la representación de la vista (Sin internacionalizar)
	 * a la de la base de datos.
	 * @param c La cadena "integer", "string" o "decimal"
	 * @return representación de dicha cadena internamente.
	 */
	public static String vistaAPersistencia(String c){
		return c.substring(0,1);
		
	}
	
	
	/**Traduce un índice de la vista a un tipo de datos concreto, en representación interna.
	 * @param i Índice de la vista. Por convención, I=0,S=1,D=2.
	 * @return representación interna del tipo de datos del índice.
	 */
	public static String indiceATipo(int i){
		switch(i){
		case 0: return "I";
		case 1: return "S";
		default:return "D";
		}
	}
	
	/** Traduce un índice a un nombre de métrica.
	 * @param i Índice de métrica. Por convención 0 es igualdad. Los siguientes
	 * varían según el tipo de datos.
	 * @param tipo representación interna del tipo de datos. "S","I" o "D".
	 * @return nombre de la métrica.
	 */
	public static String indiceAMétrica(int i, String tipo){
		if(tipo.equals("S")){
			return indiceAMétrica(i,1);
		}else{
			return indiceAMétrica(i,0);
		}
	}
	
	/** Traduce un índice a un nombre de métrica.
	 * @param i Índice de métrica. Por convención 0 es igualdad. Los siguientes
	 * varían según el tipo de datos.
	 * @param tipo 1 para cadenas, 0 para el resto de tipos de datos.
	 * @return Nombre de la métrica.
	 */
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
