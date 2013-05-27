package controlador;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URLConnection;

import servidorcbr.modelo.Usuario;

public class ControlLogin {

	
	
	/** Valida el login de usuario.
	 * @param nombre Nombre del usuario a buscar.
	 * @param password password del supuesto usuario.
	 * @return el usuario, si existe, o null, si no.
	 * @throws IOException En caso de error al conectar con el servidor.
	 * @throws ClassNotFoundException -
	 */
	public static Usuario validarLogin( String nombre, String password) throws IOException, ClassNotFoundException{
		URLConnection con = ControlConexion.getServletCon("ServletLogin");
		 OutputStream outputStream = con.getOutputStream();
	     ObjectOutputStream oos = new ObjectOutputStream(outputStream);
	     oos.writeObject(nombre);
	     oos.writeObject(password);
	     oos.flush();
	     oos.close();
	     InputStream inputStream = con.getInputStream();
	      ObjectInputStream inputDelServlet = new ObjectInputStream(
	          inputStream);
	      Usuario resultado = (Usuario) inputDelServlet.readObject();
	     
	     
	      inputDelServlet.close();
	      inputStream.close();		
	      ControlConexion.closeCon(con);
		  return resultado;
	}
	
	
}
