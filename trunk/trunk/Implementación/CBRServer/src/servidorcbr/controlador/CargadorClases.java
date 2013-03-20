package servidorcbr.controlador;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class CargadorClases {

	
	public static Class<?> cargarClaseProblema(String nombre) throws ClassNotFoundException{
		return cargarClase(nombre,"Description");
	}
	
	public static Class<?> cargarClaseSolucion(String nombre) throws ClassNotFoundException{
		return cargarClase(nombre,"Solution");
	}
	
	private static Class<?> cargarClase(String nombre,String tipo) throws ClassNotFoundException{
		URL[] url = new URL[1];
		try {
			url[0]= new URL("file:C:/Users/Ruben/workspace/CBRServer/"); //RUTA EN LA QUE SE BUSCA
		} catch (MalformedURLException e){
			e.printStackTrace();
		}
		URLClassLoader classLoader = new URLClassLoader(url);
		
		Class<?> clase = classLoader.loadClass("generadas."+ nombre+tipo);
		
		try {
			classLoader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return clase;
	}
	
}
