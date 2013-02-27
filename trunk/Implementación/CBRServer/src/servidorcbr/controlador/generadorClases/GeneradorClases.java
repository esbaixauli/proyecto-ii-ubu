package servidorcbr.controlador.generadorClases;

import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.ArrayList;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class GeneradorClases {

	
	// Auxiliar, establece los tipos permitidos para un atributo de la clase
	public static ArrayList<String> establecerTipos() {
		ArrayList<String> tiposAtbo = new ArrayList<String>();
		tiposAtbo.add("String");
		tiposAtbo.add("Integer");
		tiposAtbo.add("Double");
		tiposAtbo.add("Object");
		return tiposAtbo;
	}

	// Auxiliar, permite obtener el descriptor de una clase a partir de la
	// cadena
	// que identifica a su tipo
	private static String getDescriptor(String entrada) {
		String descriptor;
		if (entrada.equals("String")) {
			descriptor = Type.getDescriptor(String.class);
		} else if (entrada.equals("Double")) {
			descriptor = Type.getDescriptor(Double.class);
		} else if (entrada.equals("Integer")) {
			descriptor = Type.getDescriptor(Integer.class);
		} else {
			descriptor = Type.getDescriptor(Object.class);
		}
		return descriptor;
	}
	/* Permite crear una clase-
	/* @param at Hashmap cuya clave es el tipo de datos. p.ej: String y cuyo valor es una
	 * lista con los nombres de atributos de ese tipo de datos.
	 * @param nombre nombre de la clase.*/
	public static boolean crearClase(HashMap<String, ArrayList<String>> at,
			String nombre) {
		ClassWriter cw = new ClassWriter(0);

		// Version java, modificadores de clase,
		// paquete/nombre,genericidad,superclase,interfaces implementadas
		cw.visit(Opcodes.V1_7, Opcodes.ACC_PUBLIC, "generadas/" + nombre, null,
				"java/lang/Object", new String[] { "jcolibri/cbrcore/CaseComponent" });

		// Creo un constructor vacio para la clase
		crearConstructor(cw);

		// Creo un atributo y un metodo para implementar CaseComponent
		implementarCaseComponent(cw, nombre);

		// Lista de nombres de los parametros del tipo actual a procesar
		ArrayList<String> actuales;

		ArrayList<String> tiposAtbo = establecerTipos();
		for (String tipo : tiposAtbo) {
			// Obtengo todos los objetos de ese tipo que tiene la clase creo sus
			// atributos, getters y setters.
			if (at.containsKey(tipo)) {
				actuales = at.get(tipo);
				String descriptor;
				for (String atbo : actuales) {
					// Descriptor del tipo de atributo
					descriptor = getDescriptor(tipo);
					// Visitante para los m�todos, permite definir su c�digo.
					MethodVisitor mv;
					// Modificadores de acceso, nombre, descriptor del tipo,
					// genericidad, modificador final
					cw.visitField(Opcodes.ACC_PUBLIC, atbo, descriptor, null,
							null);
					// Modificadores de acceso, nombre, firma,genericidad,
					// excepciones
					mv = cw.visitMethod(Opcodes.ACC_PUBLIC,
							"get" + Character.toUpperCase(atbo.charAt(0))
									+ atbo.substring(1), "()" + descriptor,
							null, null);
					crearGetter(mv, nombre, atbo, descriptor);
					mv = cw.visitMethod(Opcodes.ACC_PUBLIC,
							"set" + Character.toUpperCase(atbo.charAt(0))
									+ atbo.substring(1), "(" + descriptor
									+ ")V", null, null);
					crearSetter(mv, nombre, atbo, descriptor);

				}
			}
		}

		// Termino de generar la clase
		cw.visitEnd();
		byte[] clase = cw.toByteArray();
		// Genero el fichero
		try {
			FileOutputStream f = new FileOutputStream("generadas/" + nombre
					+ ".class");
			System.out.println("generando...");
			f.write(clase);
			System.out.println("Clase generada.");
			f.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// Implementa atbos y m�todos para el interfaz CaseComponent
	private static void implementarCaseComponent(ClassWriter cw, String nombre) {
		String desc = Type.getDescriptor(jcolibri.cbrcore.Attribute.class);
		// Modificadores de acceso, nombre, descriptor del tipo, genericidad,
		// modificador final
		cw.visitField(Opcodes.ACC_PUBLIC, "id", desc, null, null);

		MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "getIdAttribute",
				"()" + desc, null, null);
		crearGetter(mv, nombre, "id", desc);
		mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "setIdAttribute", "(" + desc
				+ ")V", null, null);
		crearSetter(mv, nombre, "id", desc);
	}

	// Crea un constructor vac�o para la clase
	private static void crearConstructor(ClassWriter cw) {
		MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V",
				null, null);
		mv.visitCode();
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>",
				"()V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(1, 1);
		mv.visitEnd();
	}

	// Visitante para el m�todo, nombre completo de la clase, nombre del atbo,
	// tipo del atbo
	private static void crearGetter(MethodVisitor mv, String clase,
			String nombre, String tipo) {
		mv.visitCode();
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, "generadas/" + clase, nombre, tipo);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(1, 1);
		mv.visitEnd();
	}

	private static void crearSetter(MethodVisitor mv, String clase,
			String nombre, String tipo) {
		mv.visitCode();
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, "generadas/" + clase, nombre, tipo);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(2, 2);
		mv.visitEnd();
	}
	//EJEMPLO
	public static void main(String[] args) {
		//Contiene un Hashmap tipo:lista de parametros de ese tipo
		HashMap<String, ArrayList<String>> h = new HashMap<String, ArrayList<String>>();
		//lista de parametros "String"
		ArrayList<String> cadenas = new ArrayList<String>();
		cadenas.add("atbo1");
		cadenas.add("atbo2");
		h.put("String", cadenas);
		//lista de parametros "Integer"
		ArrayList<String> enteros = new ArrayList<String>();
		enteros.add("miid");
		h.put("Integer", enteros);
		
		String nombreClase = "Prueba";
		//Creo la clase. Sus atbos son atbo1,atbo2 (strings) y miid (integer)
		GeneradorClases.crearClase(h, nombreClase);
		try {
			URL[] url = { new URL("file:C:/Users/Rubén/workspace/CBRServer/") };
			URLClassLoader classLoader = new URLClassLoader(url);
			Class<?> clase = classLoader.loadClass("generadas."
					+ nombreClase);
			CaseComponent c = (CaseComponent) clase.newInstance();
			
			//Obtengo un Integer "miid" que declare al principio.
			clase.getDeclaredMethod("setMiid", Integer.class).invoke(c,
					new Integer(5));
			//Establezco que a partir de dicho "miid" (Integer) se ha de crear un atributo
			//de tipo Attribute que contenga dicho integer.
			clase.getDeclaredMethod("setIdAttribute", jcolibri.cbrcore.Attribute.class).invoke(c,
					new Attribute("miid", c.getClass()));
			//Compruebo que el attribute es dicho "miid"
			System.out.println("El id es de tipo "+ c.getIdAttribute().getType()+" y su nombre"
					+" es "+ c.getIdAttribute().getName());

			clase.getDeclaredMethod("setAtbo1", String.class).invoke(c,
					"Correcto");

			Method m = clase.getDeclaredMethod("getAtbo1", null);
			System.out.println("El resultado del get es:" + m.invoke(c, null));
			System.out.println("La clase invocada es:" + clase.getSimpleName());
			classLoader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
