package controlador;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.util.List;

import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.TipoUsuario;
import servidorcbr.modelo.Usuario;

public class ControlTipos {

	public static List<TipoCaso> obtenerTiposCaso(Usuario u) throws MalformedURLException, IOException{
		List<TipoCaso> casos=null;
		URLConnection con = ControlConexion.getServletCon("ServletTipo");
		OutputStream outputStream = con.getOutputStream();
	    ObjectOutputStream oos = new ObjectOutputStream(outputStream);
	    String tipo;
		if(u.getTipo().equals(TipoUsuario.ADMINISTRADOR)){
		     tipo = "getTipos";
				oos.writeObject(tipo);
		}else{
			 tipo = "getTiposU";
				oos.writeObject(tipo);
				oos.writeObject(u);
		}
	     InputStream inputStream = con.getInputStream();
	      ObjectInputStream inputDelServlet = new ObjectInputStream(
	          inputStream);
	      try{
	      casos = (List<TipoCaso>) inputDelServlet.readObject();
	      }catch(ClassNotFoundException e){
	    	  
	      }
	      inputDelServlet.close();
	      inputStream.close();	
	      
		return casos;
	}
}
