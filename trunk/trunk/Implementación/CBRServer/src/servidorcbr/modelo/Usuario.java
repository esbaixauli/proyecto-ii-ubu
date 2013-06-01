package servidorcbr.modelo;

import java.io.Serializable;

/**
 * Clase que representa a un usuario del sistema.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
public class Usuario implements Serializable {
	
	/**
	 * Requerido por la interfaz Serizalizable. 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * El nombre de este usuario. 
	 */
	private String nombre;
	
	/**
	 * La contraseña de este usuario. 
	 */
	private String password;
	
	/**
	 * El tipo de este usuario.
	 */
	private TipoUsuario tipo;
	
	/**
	 * Devuelve el nombre de este usuario.
	 * @return El nombre del usuario.
	 */
	public String getNombre() {
		return nombre;
	}
	
	/**
	 * Establece el nombre del usuario.
	 * @param nombre El nombre del usuario.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	/**
	 * Devuelve la contraseña de este usuario.
	 * @return La contraseña.
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Establece la contraseña de este usuario.
	 * @param password La nueva contraseña.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Devuelve el tipo de este usuario.
	 * @return El tipo de usuario.
	 */
	public TipoUsuario getTipo() {
		return tipo;
	}
	
	/**
	 * Establece el tipo de usuario del que se trata.
	 * @param tipo El tipo de usuario.
	 */
	public void setTipo(TipoUsuario tipo) {
		this.tipo = tipo;
	}
	
	@Override
	public String toString() {
		return nombre + " (" + tipo + ")";
	}

}
