package controlador.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.TipoCaso;


public class LectorCaso {

	public static List<Object> leerCaso(File f,TipoCaso tc) throws FileNotFoundException {
		List<Object> casos = new ArrayList<Object>();
		try{
		URL[] url = { new URL("file:C:/Users/Ruben/workspace/CBRServer/") };
		URLClassLoader classLoader = new URLClassLoader(url);
		Class<?> clase = classLoader.loadClass("generadas."
				+ tc.getNombre());
		classLoader.close();
		Scanner lineScan;
		
		lineScan = new Scanner(f);
		
		while (lineScan.hasNextLine()) {
			Scanner s = new Scanner(lineScan.nextLine());
			s.useDelimiter(",");
			Object c = clase.newInstance();
			for (Atributo actual : tc.getAtbos().values()) {
				invocarSetter(actual,clase,s,c);	
			}
			casos.add(c);
			s.close();
		}
		lineScan.close();
		}catch(Exception ex){
			
		}
		return casos;
	}
	
	private static void invocarSetter(Atributo a,Class<?> clase,Scanner s, Object c) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		Class<?> tipo;
		switch(a.getTipo()){
		case "S":
			tipo=String.class;
			clase.getDeclaredMethod("set"+a.getNombre(), tipo).invoke(c,
					s.next());break;
		case "I":
			tipo=Integer.class;
			clase.getDeclaredMethod("set"+a.getNombre(), tipo).invoke(c,
					s.nextInt());break;
		case "D":
			tipo=Double.class;
			clase.getDeclaredMethod("set"+a.getNombre(), tipo).invoke(c,
					s.nextDouble());break;
		}
	}
}
