package controlador;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class ControlConexion {
	private static String iip;
	private static String ipuerto;
	
	//Establece los parametros de conexion.
	public static void setCon(String ip, String puerto){
	iip=ip;
	ipuerto=puerto;
	}
	

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
	
	public static void closeCon(URLConnection con){
		((HttpURLConnection) con).disconnect();
	}
	
}
