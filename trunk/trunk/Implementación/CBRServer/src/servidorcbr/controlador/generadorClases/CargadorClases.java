package servidorcbr.controlador.generadorClases;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import servidorcbr.modelo.TipoCaso;

public class CargadorClases {

	
	public static Class<?> cargarClaseProblema(String nombre) throws ClassNotFoundException{
		return cargarClase(nombre,"Description");
	}
	
	public static Class<?> cargarClaseSolucion(String nombre) throws ClassNotFoundException{
		return cargarClase(nombre,"Solution");
	}
	
	private static Class<?> cargarClase(String nombre,String tipo) throws ClassNotFoundException{
		Configuration conf = new Configuration();
		Class<?> clase = null;
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
	    	URLClassLoader cLoader = URLClassLoader.newInstance(url);
	    	
	    	jcolibri.cbrcore.CaseComponent cc = new jcolibri.test.test8.TravelDescription();
	    	System.out.println("CaseComponent::TravelDescription: "+cc.toString());
	    	cLoader.loadClass("jcolibri.cbrcore.CaseComponent");
	    	
	    	clase = cLoader.loadClass("generadas."+nombre+tipo);
	    } catch (IOException ex) {
	    	ex.printStackTrace();
	    }
		return clase;
	}

}
