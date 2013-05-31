package servidorcbr.controlador;

import servidorcbr.modelo.Estadistica;
import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.Usuario;
import servidorcbr.modelo.excepciones.PersistenciaException;
import servidorcbr.persistencia.sql.SQLFacade;

/**
 * Clase estática de la capa de controlador para la gestión de estadísticas.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
public class ControladorEstadisticas {

	
	/**
	 * Recupera de la base de datos el objeto estadística correspondiente a un usuario y un tipo
	 * de caso. También se pueden obtener estadísticas de todos los usuarios o de todos los tipos
	 * de caso cambiando el nombre del usuario o del tipo de caso por "ver todos".
	 * @param u El usuario del que se quieren estadísticas, o "ver todos" para ver las globales.
	 * @param tc El tipo de caso del que se quieren estadísticas, o "ver todos" para ver las globales.
	 * @return El objeto Estadistica que contiene la información solicitada.
	 * @throws PersistenciaException Si se produce algún error en la operación.
	 */
	public static Estadistica getEstadistica(Usuario u, TipoCaso tc) throws PersistenciaException{
		return SQLFacade.getInstance().getEstadistica(u,tc);
	}
	
	/**
	 * Limpia las estadísticas correspondientes a un usuario y un tipo de caso. También puede
	 * limpiar las estadísticas de todos los usuarios para un tipo de caso (con nombre de usuario
	 * "ver todos"), las estadísticas de todos los tipos de caso para un mismo usuario (con nombre
	 * de tipo de caso "ver todos") o todas las estadísticas almacenadas en el sistema (con ambos). 
	 * @param u El usuario. Puede ser "ver todos" para eliminar las de todos los usuarios.
	 * @param tc El tipo de caso. Puede ser "ver todos" para eliminar las de todos los tipos de caso.
	 * @throws PersistenciaException Si se produce algún error en la operación.
	 */
	public static void limpiarEstadistica(Usuario u, TipoCaso tc) throws PersistenciaException{
		SQLFacade.getInstance().limpiarEstadistica(u,tc);
	}
	
	/**
	 * Actualiza las estadísticas correspondientes a un usuario y un tipo de caso con una nueva
	 * ejecución. Añade una ejecución de la calidad indicada a las estadísticas existentes.
	 * @param u El usuario que ha ejecutado el ciclo del que proviene esta estadística.
	 * @param tc El tipo de caso sobre el que se ha ejecutado el ciclo.
	 * @param calidad La calidad que el usuario ha dado a esta ejecución.
	 * @throws PersistenciaException Si se produce algún error en la operación.
	 */
	public static void updateEstadistica(Usuario u, TipoCaso tc, int calidad) throws PersistenciaException {
		SQLFacade.getInstance().updateEstadistica(u, tc, calidad);
	}
}
