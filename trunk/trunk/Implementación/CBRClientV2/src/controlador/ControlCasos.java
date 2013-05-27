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


/**Controlador de casos. Permite realizar operaciones sobre
 * los casos (no los tipos) en los sistemas del servidor.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
public class ControlCasos {
	
	/** Inserta casos en la base de casos del servidor.
	 * @param tc 
	 * @param casos Una lista de hashmap. Cada elemento de la lista es un caso, y el hashmap sigue
	 * una estructura <nombreatributo,valor>.
	 * @return true si ha habido éxito en la operación.
	 * @throws MalformedURLException En caso de que la url del servlet esté mal formada.
	 * @throws IOException Si hay un error de conexión con el servidor.
	 */
	public static boolean insertarCasos(TipoCaso tc, List<HashMap<String,Serializable>> casos) throws MalformedURLException, IOException{
		URLConnection con = ControlConexion.getServletCon("ServletCaso");
		OutputStream outputStream = con.getOutputStream();
	    ObjectOutputStream oos = new ObjectOutputStream(outputStream);
	    oos.writeObject("putCasos");
	    oos.writeObject(tc);
	    oos.writeObject(casos);
	    InputStream inputStream = con.getInputStream();
	    ObjectInputStream inputDelServlet = new ObjectInputStream(
		          inputStream);
	    return inputDelServlet.readBoolean();
	    
	}

}
