package controlador;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**Controlador de conexión. Gestiona las operaciones de conexión 
 * con el servidor.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
public class ControlConexion {
	
	/**
	 * Dirección IP del servidor.
	 */
	private static String iip;
	/**
	 * Puerto TCP de la conexión.
	 */
	private static String ipuerto;
	

	/**	Establece los parametros de conexion.
	 * @param ip Dirección IP del servidor.
	 * @param puerto Puerto TCP al que conectarse.
	 */
	public static void setCon(String ip, String puerto){
	iip=ip;
	ipuerto=puerto;
	}
	

	/** Obtiene conexión con un servlet del servidor.
	 * @param servlet El nombre del servlet al que conectarse.
	 * @return Conexión con un servlet.
	 * @throws MalformedURLException En caso de que el nombre del servlet esté mal formado.
	 * @throws IOException Si hay un error de conexión.
	 */
	public static URLConnection getServletCon(String servlet) throws MalformedURLException,
    IOException {
  URL urlServlet = new URL("http://"+iip+":"+ipuerto+"/CBRServer/"+servlet);
  URLConnection con = urlServlet.openConnection();
  con.setDoInput(true);
  con.setDoOutput(true);
  con.setUseCaches(false);
  con.setRequestProperty("Content-Type",
      "application/x-java-serialized-object");
  return con;
}
	
	/**Cierra la conexión con un servlet del servidor.
	 * @param con Conexión a cerrar.
	 */
	public static void closeCon(URLConnection con){
		((HttpURLConnection) con).disconnect();
	}
	
}
