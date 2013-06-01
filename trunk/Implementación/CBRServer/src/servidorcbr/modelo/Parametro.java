package servidorcbr.modelo;

import java.io.Serializable;

/**
 * Clase que representa un parámetro (nombre-valor) que configura a una técnica.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
public class Parametro implements Serializable {

	/**
	 * Requerido por la interfaz Serializable.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Nombre del parámetro. 
	 */
	private String nombre;
	
	/**
	 * Valor del parámetro.
	 */
	private double valor;
	
	/**
	 * Devuelve el nombre del parámetro.
	 * @return El nombre del parámetro.
	 */
	public String getNombre() {
		return nombre;
	}
	
	/**
	 * Establece el nombre del parámetro.
	 * @param nombre El nombre del parámetro.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	/**
	 * Devuelve el valor del parámetro.
	 * @return El valor del parámetro.
	 */
	public double getValor() {
		return valor;
	}
	
	/**
	 * Establece el valor del parámetro.
	 * @param valor El valor del parámetro.
	 */
	public void setValor(double valor) {
		this.valor = valor;
	}

}
