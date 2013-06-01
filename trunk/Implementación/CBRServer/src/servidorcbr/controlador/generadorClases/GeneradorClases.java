package servidorcbr.controlador.generadorClases;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * Clase estática que genera dos clases por cada tipo de caso. Una de las clases contiene los
 * atributos del problema y la otra los de la solución. Ambas implementan CaseComponent.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
public class GeneradorClases {

	/**
	 * Establece los tipos permitidos para un atributo de la clase. De momento, son String,
	 * Integer o Double.
	 * @return Una lista con las iniciales de los tipos permitidos.
	 */
	public static ArrayList<String> establecerTipos() {
		ArrayList<String> tiposAtbo = new ArrayList<String>();
		tiposAtbo.add("S");
		tiposAtbo.add("I");
		tiposAtbo.add("D");
		return tiposAtbo;
	}

	/**
	 * Obtiene el descriptor de la clase, dada una cadena que identifica su tipo.
	 * @param entrada Puede ser "S", "D" o "I".
	 * @return El descriptor correspondiente a String, Double o Integer, respectivamente.
	 */
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
	
	/**
	 * Crea dinámicamente una clase dados una serie de atributos y un nombre. Una vez creada,
	 * escribe el fichero .class en HDFS para que CargadorClases sea capaz de encontrarlo.
	 * Las clases se guardan en la ruta /classes/generadas/ del sistema de ficheros HDFS.
	 * @param at Un HashMap que mapea un tipo de datos a una lista de nombres de atributos
	 * de ese tipo.
	 * @param nombre El nombre deseado para la clase.
	 * @return Si la operación ha tenido éxito o no.
	 */
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

	/**
	 * Escribe el array de bytes que representa un fichero .class en HDFS.
	 * @param nombre El nombre de la clase.
	 * @param clase El array de bytes que representa el fichero a escribir.
	 * @param conf El objeto de configuración que premite acceder a HDFS.
	 * @throws IOException Si hay algún error de comunicación con HDFS.
	 */
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

	/**
	 * Añade un atributo a una clase a medio construir.
	 * @param nombre El nombre de la clase.
	 * @param cw El ClassWriter que se encarga de generar el código dinámicamente.
	 * @param tipo El tipo de datos del atributo.
	 * @param atbo El nombre del atributo.
	 */
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

	/**
	 * Hace que la clase implemente CaseComponent e implementa el método getIdAttribute() con
	 * código dinámico que devuelve en cada caso un nuevo Attribute (sin tenerlo como variable
	 * de instancia).
	 * @param cw El ClassWriter que está generando el código.
	 * @param nombre El nombre de la clase.
	 */
	private static void implementarCaseComponent(ClassWriter cw, String nombre) {
		String desc = Type.getDescriptor(jcolibri.cbrcore.Attribute.class);
		
		//Creo atributo META_ID
		desc = Type.getDescriptor(Long.class);
		cw.visitField(Opcodes.ACC_PUBLIC, "META_ID", desc, null, null);
		
		// Creo setter
		MethodVisitor mv2 = cw.visitMethod(Opcodes.ACC_PUBLIC, "setMETA_ID", 
				"(" + desc + ")V", null, null);
		crearSetter(mv2, nombre, "META_ID", desc);
		
		// Creo getter
		mv2 = cw.visitMethod(Opcodes.ACC_PUBLIC, "getMETA_ID",
				"()" + desc, null, null);
		crearGetter(mv2, nombre, "META_ID", desc);
		
		// Modificadores de acceso, nombre, descriptor del tipo, genericidad,
		// modificador final
		desc = Type.getDescriptor(jcolibri.cbrcore.Attribute.class);
		MethodVisitor
		mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "getIdAttribute", "()" + desc, null, null);
		mv.visitCode();
		Label l0 = new Label();
		mv.visitLabel(l0);
		mv.visitLineNumber(20, l0);
		mv.visitTypeInsn(Opcodes.NEW, "jcolibri/cbrcore/Attribute");
		mv.visitInsn(Opcodes.DUP);
		mv.visitLdcInsn("META_ID");
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;");
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "jcolibri/cbrcore/Attribute", "<init>", "(Ljava/lang/String;Ljava/lang/Class;)V");
		mv.visitInsn(Opcodes.ARETURN);
		Label l1 = new Label();
		mv.visitLabel(l1);
		mv.visitLocalVariable("this", "Lgeneradas/"+nombre+";", null, l0, l1, 0);
		mv.visitMaxs(4, 1);
		mv.visitEnd();
		
		
	}

	/**
	 * Crea un constructor vacío para la clase.
	 * @param cw El ClassWriter que está generando el código.
	 */
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

	/**
	 * Crea un getter para un atributo dado.
	 * @param mv El visitante para el método.
	 * @param clase El nombre de la clase.
	 * @param nombre El nombre del atributo.
	 * @param tipo El tipo de datos del atributo.
	 */
	private static void crearGetter(MethodVisitor mv, String clase,
			String nombre, String tipo) {
		mv.visitCode();
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, "generadas/" + clase, nombre, tipo);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(1, 1);
		mv.visitEnd();
	}

	/**
	 * Crea un setter para un atributo dado.
	 * @param mv El visitante para el método.
	 * @param clase El nombre de la clase.
	 * @param nombre El nombre del atributo.
	 * @param tipo El tipo de datos del atributo.
	 */
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
