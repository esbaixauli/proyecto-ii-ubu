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

/**Controlador de tipos de caso en el cliente. Ofrece métodos
 * para conectar con el servlet de tipos.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
public class ControlTipos {
	
	/**Pide al servidor borrar el tipo de caso del sistema.
	 * (Se borra en BD SQL, HSQLDB y clases generadas dinámicamente).
	 * @param t Tipo de caso.
	 * @return true si ha habido éxito en la operación
	 * @throws MalformedURLException Error al conectar con el servlet (dirección mal formada).
	 * @throws IOException Error genérico al conectar con el servlet.
	 */
	public static boolean borrarTiposCaso(TipoCaso t) throws MalformedURLException, IOException{
		URLConnection con = ControlConexion.getServletCon("ServletTipo");
		OutputStream outputStream = con.getOutputStream();
	    ObjectOutputStream oos = new ObjectOutputStream(outputStream);
	    oos.writeObject("removeTipo");
	    oos.writeObject(t);
	    InputStream inputStream = con.getInputStream();
	    ObjectInputStream inputDelServlet = new ObjectInputStream(
		          inputStream);
	    return inputDelServlet.readBoolean();
	    
	}

	/** Pide al servlet que envíe los tipos de caso de un usuario al cliente.
	 * @param u Usuario del que obtener los tipos de caso asociados. Si es un
	 * administrador, se obtienen todos los tipos del sistema.
	 * @return Lista de tipos de caso del usuario.
	 * @throws MalformedURLException Error al conectar con el servlet (dirección mal formada).
	 * @throws IOException Error genérico al conectar con el servlet.
	 */
	@SuppressWarnings("unchecked")
	public static List<TipoCaso> obtenerTiposCaso(Usuario u) throws MalformedURLException, IOException{
		List<TipoCaso> casos=null;
		URLConnection con = ControlConexion.getServletCon("ServletTipo");
		OutputStream outputStream = con.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(outputStream);
		String tipo;
		if(u==null || u.getTipo().equals(TipoUsuario.ADMINISTRADOR)){
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

	/**Añade un nuevo tipo de caso al servidor.
	 * @param tc Tipo de caso a añadir.
	 * @return true si éxito en la operación, false si no.
	 * @throws MalformedURLException Error al conectar con el servlet (dirección mal formada).
	 * @throws IOException Error genérico al conectar con el servlet.
	 */
	public static boolean addTipo (TipoCaso tc) throws MalformedURLException, IOException {
		return enviarTipo("newTipo",tc);
	}
	
	/**Envía un mensaje relativo a tipos de caso al servidor. Método privado que gestiona
	 * estas operaciones para fomentar la reusabilidad.
	 * @param addOrRemove Cadena de texto que identifica el tipo de mensaje. Puede ser
	 * 'newTipo' si se pide crear un tipo de caso nuevo, modTipo si se quiere modificar uno existente
	 * o 'removeTipo' si se desea borrar un tipo de caso.
	 * @param tc tipo de caso a añadir, modificar o borrar.
	 * @return true si éxito en la operación, false si no.
	 * @throws MalformedURLException Error al conectar con el servlet (dirección mal formada).
	 * @throws IOException Error genérico al conectar con el servlet.
	 */
	private static boolean enviarTipo(String addOrRemove, TipoCaso tc) throws MalformedURLException, IOException {
		URLConnection con = ControlConexion.getServletCon("ServletTipo");
		OutputStream outputStream = con.getOutputStream();
	    ObjectOutputStream oos = new ObjectOutputStream(outputStream);
	    oos.writeObject(addOrRemove);
	    oos.writeObject(tc);
	    InputStream inputStream = con.getInputStream();
	    ObjectInputStream inputDelServlet = new ObjectInputStream(inputStream);
	    return inputDelServlet.readBoolean();
	}
	
	/**Pide al servidor modificar un tipo de caso.
	 * @param tc Tipo de caso a modificar.
	 * @return true si éxito en la operación, false si no.
	 * @throws MalformedURLException Error al conectar con el servlet (dirección mal formada).
	 * @throws IOException Error genérico al conectar con el servlet.
	 */
	public static boolean modificarTipo(TipoCaso tc) throws MalformedURLException, IOException{
			return enviarTipo("modTipo",tc);
	}
}
