package servidorcbr.modelo;

import java.io.Serializable;

public class Atributo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String nombre;
	private String tipo;
	private double peso;
	private String metrica;
	
	public String getMetrica() {
		return metrica;
	}

	public void setMetrica(String metrica) {
		this.metrica = metrica;
	}

	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getTipo() {
		return tipo;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public double getPeso() {
		return peso;
	}
	
	public void setPeso(double peso) {
		this.peso = peso;
	}
	
}
