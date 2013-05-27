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

/**Controlador de usuarios en el cliente. Permite enviar peticiones al servidor
 * relativas a los usuarios.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
public class ControlUsuarios {
	
	/** Obtiene la lista completa de usuarios del servidor.
	 * @return usuarios.
	 * @throws MalformedURLException Si la dirección del servlet está mal formada.
	 * @throws IOException En caso de error de conexión.
	 */
	@SuppressWarnings("unchecked")
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

	/**Gestiona un alta de usuario.
	 * @param u El usuario.
	 * @param casos Casos asociados al usuario.
	 * @return true si la operación ha tenido éxito.
	 */
	public static boolean newUsuario (Usuario u, List<String> casos){
		return altaModUsuario(u, casos, "newUsuario");
	}
	
	/**Gestiona una modificación de usuario.
	 * @param u El usuario.
	 * @param casos Casos asociados al usuario.
	 * @return true si la operación ha tenido éxito.
	 */
	public static boolean modUsuario (Usuario u, List<String> casos) {
		return altaModUsuario(u, casos,"modUsuario");
	}

	/**Gestiona un alta o modificación de usuario.
	 * @param u El usuario.
	 * @param casos Casos asociados al usuario.
	 * @param tipoMensaje "modUsuario" si modificar, "newUsuario" si crear.
	 * @return true si la operación ha tenido éxito.
	 */
	private static boolean altaModUsuario(Usuario u, List<String> casos, String tipoMensaje) {
		boolean exito = false;
		try{
		
		URLConnection con = ControlConexion.getServletCon("ServletUsuarios");
		OutputStream outputStream = con.getOutputStream();
	    ObjectOutputStream oos = new ObjectOutputStream(outputStream);
	    oos.writeObject(tipoMensaje);
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
	
	/**Elimina un usuario del sistema.
	 * @param u Usuario a eliminar.
	 * @return true si éxito en el borrado.
	 * @throws IOException En caso de error de conexión con el servidor.
	 */
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
