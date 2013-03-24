package controlador;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.util.HashMap;

import servidorcbr.modelo.Estadistica;
import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.Usuario;

public class ControlEstadisticas {

	
	public static Estadistica getEstadistica(Usuario u,TipoCaso tc) throws MalformedURLException, IOException {
		Estadistica est =null;
		URLConnection con = ControlConexion.getServletCon("ServletEstadisticas");
		OutputStream outputStream = con.getOutputStream();
	    ObjectOutputStream oos = new ObjectOutputStream(outputStream);
	    oos.writeObject("getDeUsuario");
	    oos.writeObject(u);
	    oos.writeObject(tc);
	    InputStream inputStream = con.getInputStream();
	    ObjectInputStream inputDelServlet = new ObjectInputStream(inputStream);
	    try{
	    	est = (Estadistica) inputDelServlet.readObject();
	    }catch(ClassNotFoundException e){}
	    inputDelServlet.close();
	    inputStream.close();
		return est;
	}
	
	public static void limpiarEstadistica(Usuario u, TipoCaso tc) throws MalformedURLException, IOException{
		URLConnection con = ControlConexion.getServletCon("ServletEstadisticas");
		OutputStream outputStream = con.getOutputStream();
	    ObjectOutputStream oos = new ObjectOutputStream(outputStream);
	    oos.writeObject("limpiar");
	    oos.writeObject(u);
	    oos.writeObject(tc);
	    InputStream inputStream = con.getInputStream();
	    ObjectInputStream inputDelServlet = new ObjectInputStream(inputStream);   
	    inputDelServlet.readBoolean();
	   
	}
}
