package controlador;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;

import servidorcbr.modelo.Usuario;

public class ControlUsuarios {
	
	public static HashMap<String,Usuario> getUsuarios() throws MalformedURLException, IOException {
		HashMap<String,Usuario> users = null;
		URLConnection con = ControlConexion.getServletCon("ServletUsuarios");
		OutputStream outputStream = con.getOutputStream();
	    ObjectOutputStream oos = new ObjectOutputStream(outputStream);
	    oos.writeObject("getUsuarios");
	    InputStream inputStream = con.getInputStream();
	    ObjectInputStream inputDelServlet = new ObjectInputStream(inputStream);
	    try{
	    	users = (HashMap<String,Usuario>) inputDelServlet.readObject();
	    }catch(ClassNotFoundException e){

	    }
	    inputDelServlet.close();
	    inputStream.close();
		return users;
	}
	
	public static boolean newUsuario (Usuario u, List<String> casos){
		boolean exito = false;
		try{
		URLConnection con = ControlConexion.getServletCon("ServletUsuarios");
		OutputStream outputStream = con.getOutputStream();
	    ObjectOutputStream oos = new ObjectOutputStream(outputStream);
	    oos.writeObject("newUsuario");
	    oos.writeObject(u);
	    oos.writeObject(casos);
	    InputStream inputStream = con.getInputStream();
	    ObjectInputStream inputDelServlet = new ObjectInputStream(inputStream);
	    try{
	    	exito = (boolean) inputDelServlet.readObject();
	    }catch(ClassNotFoundException e){

	    }
	    inputDelServlet.close();
	    inputStream.close();
		}catch(IOException ex){
			exito=false;
			
		}
		return exito;
	}
	
	public static boolean modUsuario (Usuario u, List<String> casos) {
		boolean exito = false;
		try{
		
		URLConnection con = ControlConexion.getServletCon("ServletUsuarios");
		OutputStream outputStream = con.getOutputStream();
	    ObjectOutputStream oos = new ObjectOutputStream(outputStream);
	    oos.writeObject("modUsuario");
	    oos.writeObject(u);
	    oos.writeObject(casos);
	    InputStream inputStream = con.getInputStream();
	    ObjectInputStream inputDelServlet = new ObjectInputStream(inputStream);
	    try{
	    	exito = (boolean) inputDelServlet.readObject();
	    }catch(ClassNotFoundException e){

	    }
	    inputDelServlet.close();
	    inputStream.close();
	    }catch(IOException ex){
	    	exito=false;
	    }
		return exito;
	}
	
	public static boolean removeUsuario (Usuario u) throws IOException {
		boolean exito = false;
		URLConnection con = ControlConexion.getServletCon("ServletUsuarios");
		OutputStream outputStream = con.getOutputStream();
	    ObjectOutputStream oos = new ObjectOutputStream(outputStream);
	    oos.writeObject("removeUsuario");
	    oos.writeObject(u);
	    InputStream inputStream = con.getInputStream();
	    ObjectInputStream inputDelServlet = new ObjectInputStream(inputStream);
	    try{
	    	exito = (boolean) inputDelServlet.readObject();
	    }catch(ClassNotFoundException e){

	    }
	    inputDelServlet.close();
	    inputStream.close();
		return exito;
	}

}
