package servidorcbr.modelo;

import java.io.Serializable;
import java.util.List;

public class Tecnica implements Serializable {

	private static final long serialVersionUID = 1L;
	private String nombre;
	private List<Parametro> params;
	
	public Tecnica () {
		
	}
	
	public Tecnica (String nomb) {
		nombre = nomb;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public List<Parametro> getParams() {
		return params;
	}
	
	public void setParams(List<Parametro> params) {
		this.params = params;
	}
	
}
