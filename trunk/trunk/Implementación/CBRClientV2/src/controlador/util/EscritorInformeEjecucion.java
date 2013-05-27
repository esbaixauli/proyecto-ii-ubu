package controlador.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import servidorcbr.modelo.Parametro;
import servidorcbr.modelo.Tecnica;
import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.Usuario;

/**Clase de utillería para escribir informes de la ejecución del ciclo en formato .txt.
 * @author Rubén Antón García, Enrique Sainz Baixauli.
 *
 */
public class EscritorInformeEjecucion {

	/**Escribe el informe de resultados de la ejecución de un ciclo CBR en un fichero txt.
	 * @param ruta Ruta donde se escribirá el informe.
	 * @param u Usuario que ejecutó el ciclo.
	 * @param tc Tipo de caso para el que se ejecutó el ciclo.
	 * @return true si se tuvo éxito al escribir, false si no.
	 */
	public static boolean escribirInforme(String ruta, Usuario u, TipoCaso tc){
		try{
			
		ResourceBundle b = ResourceBundle.getBundle(
				"vista.internacionalizacion.Recursos", Locale.getDefault());
	
		PrintWriter pw = new PrintWriter(ruta);
		pw.println(b.getString("executionreport"));
		pw.println(SimpleDateFormat.getDateInstance().format(new Date()));
		pw.println("--------");
		pw.println(b.getString("name") +": "+ u.getNombre());
		pw.println(tc.getNombre());
		pw.println("--------");
		pw.println("");
		pw.println(b.getString("retrieval"));
		pw.println(b.getString("method")+":"+tc.getDefaultRec().getNombre());
		escribirParametros(tc.getDefaultRec(), pw);
		pw.println(b.getString("reuse"));
		pw.println(b.getString("method")+":"+tc.getDefaultReu().getNombre());
		pw.println(b.getString("revise"));
		pw.println(b.getString("method")+":"+tc.getDefaultRev().getNombre());
		escribirParametros(tc.getDefaultRev(), pw);
		pw.println(b.getString("retain"));
		pw.println(b.getString("method")+":"+tc.getDefaultRet().getNombre());
		escribirParametros(tc.getDefaultRet(), pw);
		
		pw.close();
		return true;
		}catch(IOException ex){}
		return false;
	}
	
	/** Auxiliar. Escribe los parámetros de una técnica en el informe. Este método
	 * asume que dichos parámetros se hallan en un formato legible para el usuario, por
	 * lo que es responsabilidad del resto del sistema garantizar esto.
	 * @param t Técnica cuyos parámetros se escribirán.
	 * @param fw PrintWriter asociado al fichero donde se está escribiendo el informe.
	 * @throws IOException
	 */
	private static void escribirParametros(Tecnica t, PrintWriter fw) throws IOException{
		for(Parametro p: t.getParams()){
			fw.println("["+p.getNombre()+","+p.getValor()+"]");
		}
	}
	
}
