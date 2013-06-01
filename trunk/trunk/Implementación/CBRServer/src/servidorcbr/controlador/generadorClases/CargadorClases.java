package servidorcbr.controlador.generadorClases;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import jcolibri.cbrcore.CaseComponent;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * Clase de utilería que carga las clases que definen la descripción y la solución de cada caso.
 * Estas clases han sido generadas dinámicamente, por lo que las copia desde HDFS al sistema
 * de ficheros local y las carga desde ahí mediante un URLClassLoader.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
public class CargadorClases {

	/**
	 * Carga la clase que describe la Descripción (problema) de un tipo de caso concreto.
	 * @param nombre El nombre del tipo de caso.
	 * @return La clase cargada, que implementa CaseComponent.
	 * @throws ClassNotFoundException Si no encuentra la clase.
	 */
	public static Class<? extends CaseComponent> cargarClaseProblema(String nombre) throws ClassNotFoundException{
		return cargarClase(nombre,"Description");
	}
	
	/**
	 * Carga la clase que describe la Solución de un tipo de caso concreto.
	 * @param nombre El nombre del tipo de caso.
	 * @return La clase cargada, que implementa CaseComponent.
	 * @throws ClassNotFoundException Si no encuentra la clase.
	 */
	public static Class<? extends CaseComponent> cargarClaseSolucion(String nombre) throws ClassNotFoundException{
		return cargarClase(nombre,"Solution");
	}
	
	/**
	 * Método en el que delegan los dos anteriores para realizar la carga de la clase.
	 * Este método espera que las clases estén en la ruta /generadas/ de HDFS (paquete generadas)
	 * y las copia al directorio local determinado por la variable de configuración cbr.class.dir
	 * @param nombre Nombre del tipo de caso.
	 * @param tipo Puede ser "Description" o "Solution".
	 * @return La clase cargada.
	 * @throws ClassNotFoundException Si no encuentra la clase.
	 */
	@SuppressWarnings("unchecked")
	private static Class<? extends CaseComponent> cargarClase(String nombre,String tipo) throws ClassNotFoundException{
		Configuration conf = new Configuration();
		Class<? extends CaseComponent> clase = null;
		conf.addResource(new Path("/etc/hadoop/core-site.xml"));
	    conf.addResource(new Path("/etc/hadoop/hdfs-site.xml"));
	    String folder = conf.get("cbr.class.dir");
	    try {
	    	FileSystem fs = FileSystem.get(conf);
	    	Path inFile = new Path("/classes/generadas/"+nombre+tipo+".class");
	    	Path outFile = new Path(folder+"/generadas/"+nombre+tipo+".class");
	    	fs.copyToLocalFile(inFile, outFile);
	    	URL[] url = new URL[1];
	    	url[0]= new URL("file:"+folder+"/");
	    	URLClassLoader cLoader = URLClassLoader.newInstance(url, CaseComponent.class.getClassLoader());
	    	clase = (Class<? extends CaseComponent>) cLoader.loadClass("generadas."+nombre+tipo);
	    } catch (IOException ex) {
	    	ex.printStackTrace();
	    }
		return clase;
	}

}
