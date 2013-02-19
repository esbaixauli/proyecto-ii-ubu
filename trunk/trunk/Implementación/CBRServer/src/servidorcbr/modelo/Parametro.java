package servidorcbr.modelo;

import java.io.Serializable;

public class Parametro implements Serializable {

	private static final long serialVersionUID = 1L;
	private String nombre;
	private double valor;
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public double getValor() {
		return valor;
	}
	
	public void setValor(double valor) {
		this.valor = valor;
	}

}
