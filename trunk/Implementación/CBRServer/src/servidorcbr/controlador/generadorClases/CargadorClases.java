package servidorcbr.controlador.generadorClases;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import jcolibri.cbrcore.CaseComponent;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class CargadorClases {

	
	public static Class<? extends CaseComponent> cargarClaseProblema(String nombre) throws ClassNotFoundException{
		return cargarClase(nombre,"Description");
	}
	
	public static Class<? extends CaseComponent> cargarClaseSolucion(String nombre) throws ClassNotFoundException{
		return cargarClase(nombre,"Solution");
	}
	
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
