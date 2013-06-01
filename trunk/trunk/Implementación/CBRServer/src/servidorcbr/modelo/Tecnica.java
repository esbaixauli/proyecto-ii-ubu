package servidorcbr.modelo;

import java.io.Serializable;
import java.util.List;

/**
 * Clase que representa una técnica de Recuperación, Reutilización, Revisión o Retención.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
public class Tecnica implements Serializable {

	/**
	 * Requerido por la interfaz Serializable.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Nombre de la técnica.
	 */
	private String nombre;
	
	/**
	 * Lista de parámetros que configuran la técnica.
	 */
	private List<Parametro> params;
	
	/**
	 * Bandera que indica si una técnica está habilitada en el tipo de caso o no.
	 */
	private boolean enabled = true;
	
	/**
	 * Devuelve si la técnica está habilitada en el tipo de caso o no.
	 * @return True si la técnica está habilitada, false si no.
	 */
	public boolean getEnabled() {
		return enabled;
	}

	/**
	 * Establece si la técnica está habilitada en el tipo de caso o no.
	 * @param enabled True si la técnica está habilitada, false si no.
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Constructor vacío de la clase.
	 */
	public Tecnica () {
		
	}
	
	/**
	 * Constructor de la clase, dado el nombre de la técnica.
	 * @param nomb El nombre de la técnica.
	 */
	public Tecnica (String nomb) {
		nombre = nomb;
	}
	
	/**
	 * Devuelve el nombre de la técnica.
	 * @return El nombre de la técnica.
	 */
	public String getNombre() {
		return nombre;
	}
	
	/**
	 * Establece el nombre de la técnica.
	 * @param nombre El nombre de la técnica.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	/**
	 * Devuelve la lista de parámetros con los que está configurada la técnica.
	 * @return La lista de parámetros que configura la técnica.
	 */
	public List<Parametro> getParams() {
		return params;
	}
	
	/**
	 * Establece la lista de parámetros con la que se configura la técnica.
	 * @param params Lista de parámetros para configurar la técnica.
	 */
	public void setParams(List<Parametro> params) {
		this.params = params;
	}
	
}
