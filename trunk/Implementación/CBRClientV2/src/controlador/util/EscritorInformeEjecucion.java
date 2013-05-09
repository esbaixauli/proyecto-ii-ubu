package controlador.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.ResourceBundle;

import servidorcbr.modelo.Parametro;
import servidorcbr.modelo.Tecnica;
import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.Usuario;

/**Escribe un informe de la ejecución del ciclo.
 * @author Rubén
 *
 */
public class EscritorInformeEjecucion {

	public static boolean escribirInforme(String ruta, Usuario u, TipoCaso tc){
		try{
			
		ResourceBundle b = ResourceBundle.getBundle(
				"vista.internacionalizacion.Recursos", Locale.getDefault());
		boolean exito=false;
		PrintWriter pw = new PrintWriter(ruta);
		pw.println(b.getString("executionreport"));
	
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
		escribirParametros(tc.getDefaultReu(), pw);
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
	
	private static void escribirParametros(Tecnica t, PrintWriter fw) throws IOException{
		for(Parametro p: t.getParams()){
			fw.println("["+p.getNombre()+","+p.getValor()+"]");
		}
	}
	
}
