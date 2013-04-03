package servidorcbr.controlador.generadorClases;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;

public class CargadorClases {

	
	public static Class<?> cargarClaseProblema(String nombre) throws ClassNotFoundException{
		return cargarClase(nombre,"Description");
	}
	
	public static Class<?> cargarClaseSolucion(String nombre) throws ClassNotFoundException{
		return cargarClase(nombre,"Solution");
	}
	
	private static Class<?> cargarClase(String nombre,String tipo) throws ClassNotFoundException{
		/*URL[] url = new URL[1];
		try {
			url[0]= new URL("hdfs://127.0.0.1:8020/"); //RUTA EN LA QUE SE BUSCA
		} catch (MalformedURLException e){
			e.printStackTrace();
		}*/
		/*Configuration conf = new Configuration();
		conf.addResource(new Path("/etc/hadoop/core-site.xml"));
	    conf.addResource(new Path("/etc/hadoop/hdfs-site.xml"));
	    URI[] url = DistributedCache.getCacheFiles(conf);
		URIClassLoader classLoader = new URLClassLoader(url);*/
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		
		Class<?> clase = classLoader.loadClass("generadas."+ nombre+tipo);
		
		/*try {
			classLoader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		return clase;
	}
	
}
