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
import servidorcbr.modelo.Usuario;

public class ControlCBR {
	
	/**Envía la petición de recuperar. Usado en un ciclo paso a paso.
	 * @param tc El tipo de caso al que pertenecen los casos.
	 * @param query Consulta del usuario.
	 * @return Casos recuperados.
	 * @throws MalformedURLException Servlet incorrecto.
	 * @throws IOException Error de conexión con el servlet.
	 */
	@SuppressWarnings("unchecked")
	public static List<HashMap<String,Serializable>> retrieve(TipoCaso tc, HashMap<String,Serializable> query) throws MalformedURLException, IOException{
		return enviarMensajeInicio(tc, query, "retrieve");
	}
	
	/**Envía la petición de adaptación de los casos. Usado en un ciclo paso a paso.
	 * @param tc El tipo de caso al que pertenecen los casos.
	 * @param casos Lista de casos a adaptar.
	 * @return Casos adaptados.
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static List<HashMap<String,Serializable>> reuse(TipoCaso tc, HashMap<String,Serializable> query, List<HashMap<String, Serializable>> casos) throws IOException{
		URLConnection con = ControlConexion.getServletCon("ServletCBR");
		OutputStream outputStream = con.getOutputStream();
	    ObjectOutputStream oos = new ObjectOutputStream(outputStream);
	    oos.writeObject("reuse");
	    oos.writeObject(tc);
	    oos.writeObject(query);
	    oos.writeObject(casos);
	    InputStream inputStream = con.getInputStream();
	    ObjectInputStream inputDelServlet = new ObjectInputStream(inputStream);
	    try {
			return (List<HashMap<String,Serializable>>)inputDelServlet.readObject();
		} catch (ClassNotFoundException e) {}
	    return null;
	}
	
	/**Envía la petición de revisión de un caso, para que el caso revisado quede en el servidor. 
	 * @param tc El tipo de caso al que pertenece el caso.
	 * @param caso El caso revisado.
	 * @return Si la revisión tiene éxito y el nuevo caso se ha introducido en BD.
	 * @throws IOException Si no se ha podido conectar.
	 */
	public static boolean revise(TipoCaso tc,HashMap<String, Serializable> caso) throws IOException{
		URLConnection con = ControlConexion.getServletCon("ServletCBR");
		OutputStream outputStream = con.getOutputStream();
	    ObjectOutputStream oos = new ObjectOutputStream(outputStream);
	    oos.writeObject("revise");
	    oos.writeObject(tc);
	    oos.writeObject(caso);
	    InputStream inputStream = con.getInputStream();
	    ObjectInputStream inputDelServlet = new ObjectInputStream(inputStream);
		return inputDelServlet.readBoolean();   
	}

	/**Envía el mensaje de Retención del ciclo cbr.
	 * @param tc Tipo de Caso el caso
	 * @param caso Instancia concreta del caso a retener.
	 * @param u Usuario que realiza la retención.
	 * @return true si éxito.
	 * @throws IOException Si no se establece comunicación con el servlet.
	 */
	public static boolean retain(TipoCaso tc, HashMap<String, Serializable> caso, Usuario u) throws IOException {
		URLConnection con = ControlConexion.getServletCon("ServletCBR");
		OutputStream outputStream = con.getOutputStream();
	    ObjectOutputStream oos = new ObjectOutputStream(outputStream);
	    oos.writeObject("retain");
	    oos.writeObject(tc);
	    oos.writeObject(caso);
	    oos.writeObject(u);
	    InputStream inputStream = con.getInputStream();
	    ObjectInputStream inputDelServlet = new ObjectInputStream(inputStream);
		return inputDelServlet.readBoolean();
	}
	
	/**Ejecuta un ciclo completo hasta la revisión (que es manual), en una sola acción.
	 * @param tc Tipo de caso del ciclo.
	 * @param query Consulta del usuario.
	 * @return Casos a revisar por el usuario procedentes del ciclo.
	 * @throws MalformedURLException -
	 * @throws IOException Error de conexión con el servlet.
	 */
	public static List<HashMap<String,Serializable>> cicloCompleto(TipoCaso tc, HashMap<String,Serializable> query) throws MalformedURLException, IOException{
		return enviarMensajeInicio(tc, query, "completo");
	}

	/**Auxiliar. Envía un mensaje para iniciar el ciclo, ya sea un ciclo completo o paso a paso,
	 * comenzando por el retrieve.
	 * @param tc Tipo de caso del problema.
	 * @param query Consulta del usuario.
	 * @param mensaje "retrieve" para paso a paso, "completo" para ciclo completo.
	 * @return Lista de casos recuperados (del retrieval o hasta la revisión, segun el mensaje).
	 * @throws MalformedURLException -
	 * @throws IOException Producida al conectar con el servlet.
	 */
	private static List<HashMap<String, Serializable>> enviarMensajeInicio(
			TipoCaso tc, HashMap<String, Serializable> query, String mensaje)
			throws MalformedURLException, IOException {
		URLConnection con = ControlConexion.getServletCon("ServletCBR");
		OutputStream outputStream = con.getOutputStream();
	    ObjectOutputStream oos = new ObjectOutputStream(outputStream);
	    oos.writeObject(mensaje);
	    oos.writeObject(tc);
	    oos.writeObject(query);
	    InputStream inputStream = con.getInputStream();
	    ObjectInputStream inputDelServlet = new ObjectInputStream(
		          inputStream);
	    try {
			return (List<HashMap<String,Serializable>>)inputDelServlet.readObject();
		} catch (ClassNotFoundException e) {}
	    return null;
	}

}
