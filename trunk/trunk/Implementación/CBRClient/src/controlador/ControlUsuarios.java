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

}
