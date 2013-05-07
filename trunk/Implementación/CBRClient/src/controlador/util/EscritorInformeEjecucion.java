package controlador.util;

import java.io.FileWriter;
import java.io.IOException;
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
		FileWriter fw = new FileWriter(ruta);
		fw.write(b.getString("executionreport"));
		fw.write("--------");
		fw.write(b.getString("name") +": "+ u.getNombre());
		fw.write(tc.getNombre());
		fw.write("--------");
		fw.write("");
		fw.write(b.getString("retrieval"));
		fw.write(b.getString("method")+":"+tc.getDefaultRec());
		escribirParametros(tc.getDefaultRec(), fw);
		fw.write(b.getString("reuse"));
		fw.write(b.getString("method")+":"+tc.getDefaultReu());
		escribirParametros(tc.getDefaultReu(), fw);
		fw.write(b.getString("revise"));
		fw.write(b.getString("method")+":"+tc.getDefaultRev());
		escribirParametros(tc.getDefaultRev(), fw);
		fw.write(b.getString("retain"));
		fw.write(b.getString("method")+":"+tc.getDefaultRet());
		escribirParametros(tc.getDefaultRet(), fw);
		
		fw.close();
		return true;
		}catch(IOException ex){}
		return false;
	}
	
	private static void escribirParametros(Tecnica t, FileWriter fw) throws IOException{
		for(Parametro p: t.getParams()){
			fw.write("["+p.getNombre()+","+p.getValor()+"]");
		}
	}
	
}
