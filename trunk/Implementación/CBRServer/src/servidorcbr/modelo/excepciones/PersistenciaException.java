package servidorcbr.modelo.excepciones;

public class PersistenciaException extends Exception {

	/**
	 * Requerido por la interfaz Serializable (implementada por Exception).
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor vacío de la clase. Llama al de Exception.
	 */
	public PersistenciaException () {
		super();
	}
	
	/**
	 * Constructor de la clase dado un mensaje. Llama al de Exception.
	 * @param msg Mensaje de error.
	 */
	public PersistenciaException (String msg) {
		super(msg);
	}
	
	/**
	 * Constructor de la clase dada una Excepción lanzable (Throwable) raiz del problema.
	 * @param t Raiz del problema.
	 */
	public PersistenciaException (Throwable t) {
		super(t);
	}
	
}
