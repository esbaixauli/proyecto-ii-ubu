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
		/*URL[] url = new URL[1];
		try {
			url[0]= new URL("hdfs://127.0.0.1:8020/"); //RUTA EN LA QUE SE BUSCA
		} catch (MalformedURLException e){
			e.printStackTrace();
		}*/
		Configuration conf = new Configuration();
		conf.addResource(new Path("/etc/hadoop/core-site.xml"));
	    conf.addResource(new Path("/etc/hadoop/hdfs-site.xml"));
	    Object clase = null;
	    try {
	    	FileSystem fs = FileSystem.get(conf);
	    	Path inFile = new Path("/classes/generadas/"+nombre+tipo+".cl");
	    	FSDataInputStream in = fs.open(inFile);
	    	ObjectInputStream ois = new ObjectInputStream(in);
	    	clase = ois.readObject();
	    	ois.close();
	    	in.close();
	    } catch (IOException ex) {
	    	ex.printStackTrace();
	    }
	    
		return (Class<?>) clase;
	}
	
	private byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(b);
        o.writeObject(obj);
        return b.toByteArray();
    }
	
}
