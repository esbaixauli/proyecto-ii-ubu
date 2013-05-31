package servidorcbr.controlador;

import java.util.HashMap;
import java.util.List;

import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.Usuario;
import servidorcbr.modelo.excepciones.PersistenciaException;
import servidorcbr.persistencia.sql.SQLFacade;

/**
 * Clase estática de la capa de controlador encargada de gestionar los usuarios del sistema.
 * Permite añadir, eliminar, recuperar o modificar usuarios.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
public class ControladorUsuarios {

	/**
	 * Devuelve una tabla hash con los usuarios del sistema.
	 * @return El HashMap con los usuarios.
	 * @throws PersistenciaException Si se produjo algún error en la operación.
	 */
	public static HashMap<String,Usuario> getUsuarios() throws PersistenciaException{
		return SQLFacade.getInstance().getUsuarios();
	}
	
	/* Método no utilizado, está en ControladorTipos
	//Devuelve los tipos de caso asociados a un usuario.
	//(Un administrador tiene todos los tipos)
	public static List<TipoCaso> getTipos(String usuario) throws PersistenciaException{
		return SQLFacade.getInstance().getTipos(usuario);
	}*/
	
	/**
	 * Comprueba las credenciales de un usuario. Devuelve el objeto de tipo Usuario si encontró
	 * el nombre y la contraseña o null en caso contrario.
	 * @param nombre Nombre de usuario.
	 * @param password Contraseña.
	 * @return El usuario (si coinciden nombre y contraseña) o null (si no).
	 * @throws PersistenciaException Si se produjo algún error en la operación.
	 */
	public static Usuario login(String nombre, String password) throws PersistenciaException{
		Usuario resultado = null;
		SQLFacade fachada = SQLFacade.getInstance();
		if(fachada.getUsuarios().containsKey(nombre)){
			Usuario u= SQLFacade.getInstance().getUsuarios().get(nombre);
			if(u.getPassword().equals(password)){
				resultado = u;
			}
		}
		return resultado;
	}
	
	/**
	 * Elimina un usuario del sistema.
	 * @param u El usuario a eliminar.
	 * @return Si la operación tuvo éxito.
	 * @throws PersistenciaException Si se produjo algún error en la operación.
	 */
	public static boolean removeUsuario (Usuario u) throws PersistenciaException {
		return SQLFacade.getInstance().removeUsuario(u);
	}
	
	/**
	 * Añade un usuario nuevo al sistema.
	 * @param u El usuario a añadir.
	 * @param casos Los casos que se le asignan inicialmente.
	 * @return Si la operación tuvo éxito.
	 * @throws PersistenciaException Si se produjo algún error en la operación.
	 */
	public static boolean addUsuario (Usuario u,List<String> casos) throws PersistenciaException {
		return SQLFacade.getInstance().addUsuario(u,casos);
	}
	
	/**
	 * Modifica un usuario que existe en el sistema.
	 * @param u El usuario a modificar.
	 * @param casos Los casos que se le asignan.
	 * @return Si la operación tuvo éxito.
	 * @throws PersistenciaException Si se produjo algún error en la operación.
	 */
	public static boolean modUsuario (Usuario u,List<String> casos) throws PersistenciaException {
		return SQLFacade.getInstance().modUsuario(u,casos);
	}
	
}
