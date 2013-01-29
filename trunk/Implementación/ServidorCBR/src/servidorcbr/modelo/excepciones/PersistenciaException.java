package servidorcbr.modelo.excepciones;

public class PersistenciaException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public PersistenciaException () {
		super();
	}
	
	public PersistenciaException (String msg) {
		super(msg);
	}
	
	public PersistenciaException (Throwable t) {
		super(t);
	}
	
}
