package servidorcbr.modelo;

import java.io.Serializable;

/**
 * Clase que representa un atributo de un tipo de caso.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
public class Atributo implements Serializable {
	
	/**
	 * Requerido por la interfaz Serializable.
	 */
	private static final long serialVersionUID = 2L;
	
	/**
	 * Nombre del atributo. 
	 */
	private String nombre;
	
	/**
	 * Cadena que representa el tipo del atributo. Puede ser "S" (String), "I" (Integer) o
	 * "D" (Double). 
	 */
	private String tipo;
	
	/**
	 * El peso que tiene este atributo en el tipo de caso. 
	 */
	private double peso;
	
	/**
	 * Métrica con la que este atributo deberá ser comparado. 
	 */
	private String metrica;
	
	/**
	 * Parámetro requerido por la métrica (en métricas de tipo Umbral o Interavalo). 
	 */
	private double paramMetrica;
	
	/**
	 * Si el atributo forma parte del problema (true) o de la solución (false). 
	 */
	private boolean esProblema;
	
	/**
	 * Devuelve si el atributo forma parte del problema o de la solución.
	 * @return True si forma parte del problema, false si forma parte de la solución.
	 */
	public boolean getEsProblema() {
		return esProblema;
	}

	/**
	 * Establece que el atributo sea parte del problema o de la solución.
	 * @param esProblema True para ser parte del problema, false para ser de la solución.
	 */
	public void setEsProblema(boolean esProblema) {
		this.esProblema = esProblema;
	}

	/**
	 * Devuelve la métrica con la que este atributo debe ser comparado.
	 * @return String que identifica la métrica a utilizar.
	 */
	public String getMetrica() {
		return metrica;
	}

	/**
	 * Establece la métrica con la que este atributo debe ser comparado.
	 * @param metrica String que identifica la métrica a utilizar.
	 */
	public void setMetrica(String metrica) {
		this.metrica = metrica;
	}

	/**
	 * Devuelve el parámetro necesario para configurar la métrica (en métricas Umbral o Intervalo).
	 * @return El parámetro que configura la métrica.
	 */
	public double getParamMetrica() {
		return paramMetrica;
	}

	/**
	 * Establece el parámetro necesario para configurar la métrica (en métricas Umbral o Intervalo).
	 * @param paramMetrica El parámetro que configura la métrica.
	 */
	public void setParamMetrica(double paramMetrica) {
		this.paramMetrica = paramMetrica;
	}

	/**
	 * Devuelve el nombre del atributo.
	 * @return El nombre del atributo.
	 */
	public String getNombre() {
		return nombre;
	}
	
	/**
	 * Establece el nombre del atributo.
	 * @param nombre El nombre del atributo.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	/**
	 * Devuelve el tipo de datos del atributo.
	 * @return El tipo de datos: "S" para String, "I" para Integer o "D" para Double.
	 */
	public String getTipo() {
		return tipo;
	}
	
	/**
	 * Establece el tipo de datos del atributo.
	 * @param tipo El tipo de datos: "S" para String, "I" para Integer o "D" para Double.
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	/**
	 * Devuelve el peso del atributo en el tipo de caso.
	 * @return El peso del atributo.
	 */
	public double getPeso() {
		return peso;
	}
	
	/**
	 * Establece el peso del atributo en el tipo de caso.
	 * @param peso El peso del atributo.
	 */
	public void setPeso(double peso) {
		this.peso = peso;
	}
	
}
