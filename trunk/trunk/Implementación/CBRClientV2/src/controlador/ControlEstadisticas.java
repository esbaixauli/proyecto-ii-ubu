package controlador;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.util.List;

import com.itextpdf.text.Document;

import controlador.util.EscritorPDF;

import servidorcbr.modelo.Estadistica;
import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.Usuario;

public class ControlEstadisticas {

	
	/** Obtiene las estadisticas de un usuario para un tipo de caso.
	 * @param u Usuario. Existe un usuario especial, llamado 'ver todos' si la estadística es global.
	 * @param tc Tipo de caso. Existe un tipo especial, llamado 'ver todos' si la estadística es global.
	 * @return las estadísticas pedidas.
	 * @throws MalformedURLException Si no se puede conectar al servlet (dirección equivocada).
	 * @throws IOException Si no se puede conectar al servlet.
	 */
	public static Estadistica getEstadistica(Usuario u,TipoCaso tc) throws MalformedURLException, IOException {
		Estadistica est =null;
		URLConnection con = ControlConexion.getServletCon("ServletEstadisticas");
		OutputStream outputStream = con.getOutputStream();
	    ObjectOutputStream oos = new ObjectOutputStream(outputStream);
	    oos.writeObject("getDeUsuario");
	    oos.writeObject(u);
	    oos.writeObject(tc);
	    InputStream inputStream = con.getInputStream();
	    ObjectInputStream inputDelServlet = new ObjectInputStream(inputStream);
	    try{
	    	est = (Estadistica) inputDelServlet.readObject();
	    }catch(ClassNotFoundException e){}
	    inputDelServlet.close();
	    inputStream.close();
		return est;
	}
	
	/** Borra las estadisticas de un usuario para un tipo de caso.
	 * @param u Usuario. Existe un usuario especial, llamado 'ver todos' si la estadística es global.
	 * @param tc Tipo de caso. Existe un tipo especial, llamado 'ver todos' si la estadística es global.
	 * @throws MalformedURLException Si no se puede conectar al servlet (dirección equivocada).
	 * @throws IOException Si no se puede conectar al servlet.
	 */
	public static void limpiarEstadistica(Usuario u, TipoCaso tc) throws MalformedURLException, IOException{
		URLConnection con = ControlConexion.getServletCon("ServletEstadisticas");
		OutputStream outputStream = con.getOutputStream();
	    ObjectOutputStream oos = new ObjectOutputStream(outputStream);
	    oos.writeObject("limpiar");
	    oos.writeObject(u);
	    oos.writeObject(tc);
	    InputStream inputStream = con.getInputStream();
	    ObjectInputStream inputDelServlet = new ObjectInputStream(inputStream);   
	    inputDelServlet.readBoolean();
	   
	}
	
	
	/** Escribe las estadisticas en un fichero PDF
	 * @param ruta La ruta en la que escribir.
	 * @param titulo El titulo del fichero (Titulo en la primera pagina del informe).
	 * @param texto El texto con las estadisticas.
	 * @param img Lista de gráficos de las estadisticas.
	 * @return true si ha habido éxito, false si no.
	 */
	public static boolean escribirEstadisticasPDF(String ruta, String titulo, List<String> texto, List<Image> img){
		try{
		Document d = EscritorPDF.comenzarPDF(ruta, titulo);
		//Escribo cada una de las lineas del texto
		for(String lineaActual:texto){
			EscritorPDF.addTexto(d, lineaActual);
		}
		//Escribo las imagenes de los graficos
		for(Image i: img){
			EscritorPDF.addImagen(d, i);
		}
		EscritorPDF.terminarPDF(d);
		}catch(Exception e){
			return false;
		}
		return true;
	}
}
