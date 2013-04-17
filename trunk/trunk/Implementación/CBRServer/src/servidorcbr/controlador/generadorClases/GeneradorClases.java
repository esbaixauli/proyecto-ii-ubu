package servidorcbr.controlador.generadorClases;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.ArrayList;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class GeneradorClases {

	
	// Auxiliar, establece los tipos permitidos para un atributo de la clase
	public static ArrayList<String> establecerTipos() {
		ArrayList<String> tiposAtbo = new ArrayList<String>();
		tiposAtbo.add("S");
		tiposAtbo.add("I");
		tiposAtbo.add("D");
		return tiposAtbo;
	}

	// Auxiliar, permite obtener el descriptor de una clase a partir de la
	// cadena
	// que identifica a su tipo
	private static String getDescriptor(String entrada) {
		String descriptor = null;
		if (entrada.equals("S")) {
			descriptor = Type.getDescriptor(String.class);
		} else if (entrada.equals("D")) {
			descriptor = Type.getDescriptor(Double.class);
		} else if (entrada.equals("I")) {
			descriptor = Type.getDescriptor(Integer.class);
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
					crearAtbo(nombre, cw, tipo, atbo);
				}
			}
		}

		// Termino de generar la clase
		cw.visitEnd();
		byte[] clase = cw.toByteArray();
		
		// Genero el fichero
		Configuration conf = new Configuration();
		conf.addResource(new Path("/etc/hadoop/core-site.xml"));
	    conf.addResource(new Path("/etc/hadoop/hdfs-site.xml"));
		try {
			escribirClaseHDFS(nombre, clase, conf);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static void escribirClaseHDFS(String nombre, byte[] clase,
			Configuration conf) throws IOException {
		FileSystem fs = FileSystem.get(conf);
		Path p = new Path("/classes/generadas/");
		if (!fs.exists(p)) {
			fs.mkdirs(p);
		}
		p = new Path(p, nombre+".class");
		FSDataOutputStream out = fs.create(p, true);
		out.write(clase);
		out.close();
	}

	private static Class<?> crearClass(String nombre, byte[] clase)
			throws IOException, ClassNotFoundException {
		File folder = new File("classes/generadas/");
		if (!folder.exists())
			folder.mkdir();
		
		FileOutputStream f = new FileOutputStream(folder.getCanonicalPath()+"/"+nombre+ ".class");
		f.write(clase);
		f.close();
		
		String path = "file:"+folder.getAbsoluteFile().getParent()+"/";
		URL[] url = { new URL(path) };
		URLClassLoader classLoader = new URLClassLoader(url, Thread.currentThread().getContextClassLoader());
		Class<?> claseClass = classLoader.loadClass("generadas."
				+ nombre);
		classLoader.close();
		return claseClass;
	}

	private static void crearAtbo(String nombre, ClassWriter cw, String tipo,
			String atbo) {
		String descriptor;
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
	
}
