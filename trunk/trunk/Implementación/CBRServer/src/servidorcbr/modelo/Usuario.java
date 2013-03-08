package servidorcbr.modelo;

import java.io.Serializable;

public class Usuario implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String nombre;
	private String password;
	private TipoUsuario tipo;
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public TipoUsuario getTipo() {
		return tipo;
	}
	
	public void setTipo(TipoUsuario tipo) {
		this.tipo = tipo;
	}
	
	@Override
	public String toString() {
		return nombre + " (" + tipo + ")";
	}

}
