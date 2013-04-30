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
	
	/**Envía la petición de recuperar. Usado en un ciclo paso a paso.
	 * @param tc El tipo de caso al que pertenecen los casos.
	 * @param query Consulta del usuario.
	 * @return Casos recuperados.
	 * @throws MalformedURLException Servlet incorrecto.
	 * @throws IOException Error de conexión con el servlet.
	 */
	public static List<HashMap<String,Serializable>> retrieve(TipoCaso tc, HashMap<String,Serializable> query) throws MalformedURLException, IOException{
		URLConnection con = ControlConexion.getServletCon("ServletCBR");
		OutputStream outputStream = con.getOutputStream();
	    ObjectOutputStream oos = new ObjectOutputStream(outputStream);
	    oos.writeObject("retrieve");
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
	
	/**Envía la petición de adaptación de los casos. Usado en un ciclo paso a paso.
	 * @param tc El tipo de caso al que pertenecen los casos.
	 * @param casos Lista de casos a adaptar.
	 * @return Casos adaptados.
	 * @throws IOException
	 */
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

}
