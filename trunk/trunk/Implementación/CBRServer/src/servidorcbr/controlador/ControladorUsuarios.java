package servidorcbr.controlador;

import java.util.HashMap;
import java.util.List;

import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.Usuario;
import servidorcbr.modelo.excepciones.PersistenciaException;
import servidorcbr.persistencia.sql.SQLFacade;

public class ControladorUsuarios {

	//Devuelve una tabla hash con los usuarios del sistema.
	public static HashMap<String,Usuario> getUsuarios() throws PersistenciaException{
		return SQLFacade.getInstance().getUsuarios();
	}
	
	//Devuelve los tipos de caso asociados a un usuario.
	//(Un administrador tiene todos los tipos)
	public static List<TipoCaso> getTipos(String usuario) throws PersistenciaException{
		return SQLFacade.getInstance().getTipos(usuario);
	}
	
	//Devuelve el usuario si se encontró y la contraseña es correcta,
	//o null en caso contrario.
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
	
	// Devuelve cierto si el usuario se eliminó, o falso si no.
	public static boolean removeUsuario (Usuario u) throws PersistenciaException {
		return SQLFacade.getInstance().removeUsuario(u);
	}
	
	public static boolean addUsuario (Usuario u) throws PersistenciaException {
		return SQLFacade.getInstance().addUsuario(u);
	}
	
	public static boolean modUsuario (Usuario u) throws PersistenciaException {
		return SQLFacade.getInstance().modUsuario(u);
	}
	
}
