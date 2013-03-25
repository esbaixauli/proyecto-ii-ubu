package controlador;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;

import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.TipoUsuario;
import servidorcbr.modelo.Usuario;

public class ControlCBR {
	
	public static boolean retrieve(TipoCaso tc, HashMap<String,Serializable> query) throws MalformedURLException, IOException{
		URLConnection con = ControlConexion.getServletCon("ServletCBR");
		OutputStream outputStream = con.getOutputStream();
	    ObjectOutputStream oos = new ObjectOutputStream(outputStream);
	    oos.writeObject("retrieve");
	    oos.writeObject(tc);
	    oos.writeObject(query);
	    InputStream inputStream = con.getInputStream();
	    ObjectInputStream inputDelServlet = new ObjectInputStream(
		          inputStream);
	    return inputDelServlet.readBoolean();
	    
	}

}
