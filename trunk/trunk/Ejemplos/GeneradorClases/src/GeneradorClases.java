import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.ArrayList;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class GeneradorClases {

	// Auxiliar, establece los tipos permitidos para un atributo de la clase
	private static ArrayList<String> establecerTipos() {
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

	public static void crearClase(HashMap<String, ArrayList<String>> at,
			String nombre) {
		ClassWriter cw = new ClassWriter(0);

		// Version java, modificadores de clase,
		// paquete/nombre,genericidad,superclase,interfaces implementadas
		cw.visit(Opcodes.V1_7, Opcodes.ACC_PUBLIC, "generadas/" + nombre, null,
				"java/lang/Object", new String[] { "CaseComponent" });

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
					// Visitante para los métodos, permite definir su código.
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
			FileOutputStream f = new FileOutputStream("bin/generadas/" + nombre
					+ ".class");
			System.out.println("generando...");
			f.write(clase);
			System.out.println("Clase generada.");
			f.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Implementa atbos y métodos para el interfaz CaseComponent
	private static void implementarCaseComponent(ClassWriter cw, String nombre) {
		String desc = Type.getDescriptor(Object.class);
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

	// Crea un constructor vacío para la clase
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

	// Visitante para el método, nombre completo de la clase, nombre del atbo,
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

	public static void main(String[] args) {
		HashMap<String, ArrayList<String>> h = new HashMap<String, ArrayList<String>>();
		ArrayList<String> cadenas = new ArrayList<String>();
		cadenas.add("atbo1");
		cadenas.add("atbo2");
		h.put("String", cadenas);
		String nombreClase = "Prueba";
		GeneradorClases.crearClase(h, nombreClase);
		try {
			URL[] url = { new URL("file:bin/generadas/") };

			Class<?> clase = new URLClassLoader(url).loadClass("generadas."
					+ nombreClase);
			CaseComponent c = (CaseComponent) clase.newInstance();
			c.getIdAttribute();

			clase.getDeclaredMethod("setAtbo1", String.class).invoke(c,
					"Correcto");

			Method m = clase.getDeclaredMethod("getAtbo1", null);
			System.out.println("El resultado del get es:" + m.invoke(c, null));
			System.out.println("La clase invocada es:" + clase.getSimpleName());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
