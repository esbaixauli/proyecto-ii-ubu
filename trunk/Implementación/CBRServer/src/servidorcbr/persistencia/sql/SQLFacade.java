package servidorcbr.persistencia.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import servidorcbr.modelo.Estadistica;
import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.Usuario;
import servidorcbr.modelo.excepciones.PersistenciaException;

/**
 * Fachada de acceso a base de datos relacional (HSQLDB).
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
public class SQLFacade {
	
	/**
	 * Conexión con la base de datos. 
	 */
	private Connection conn;
	
	/**
	 * Referencia estática a esta misma clase para evitar que existan varias instancias de la
	 * misma. Forma parte del patrón de diseño Singleton.
	 */
	private static SQLFacade instance=null;

	/**
	 *  Referencia a SQLTipos para delegar las operaciones de gestión de tipos de caso.
	 */
	private SQLTipos sqlTipos;
	
	/**
	 *  Referencia a SQLUsuarios para delegar las operaciones de gestión de usuarios.
	 */
	private SQLUsuarios sqlUsuarios;
	
	/**
	 * Referencia a SQLOtros para delegar las operaciones de gestión de estadísticas.
	 */
	private SQLOtros sqlOtros;
	
	/**
	 * Constructor privado de la clase, para evitar que existan múltiples instancias de esta
	 * clase. Carga el driver JDBC y establece la conexión con la base de datos. Después
	 * instancia el resto de clases de este paquete para delegar en ellos.
	 * @throws PersistenciaException Si se produce un error de conexión.
	 */
	private SQLFacade () throws PersistenciaException {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			conn = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/xdb", "SA", "");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PersistenciaException(e);
		}
		
		sqlTipos = new SQLTipos(conn);
		sqlUsuarios = new SQLUsuarios(conn);
		sqlOtros = new SQLOtros(conn);
	}
	
	/**
	 * Devuelve la única instancia de esta clase. Forma parte del patrón de diseño Singleton.
	 * @return La instancia de esta clase.
	 * @throws PersistenciaException Si se produjo un error de conexión.
	 */
	public static SQLFacade getInstance () throws PersistenciaException {
		if (instance == null)
			instance = new SQLFacade();
		return instance;
	}
	
	/**
	 * Cierra la conexión con la base de datos.
	 */
	public void close() {
		try {
			conn.close();
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * Recupera todos los tipos de caso a los que está asociado un usuario.
	 * @param usuario El nombre del usuario.
	 * @return La lista de tipos de caso asociados al usuario.
	 * @throws PersistenciaException Si se produjo algún error en la operación.
	 */
	public  List<TipoCaso> getTipos(String usuario) throws PersistenciaException{
		return sqlTipos.getTipos(usuario);
	}
	
	/**
	 * Recupera todos los tipos de caso almacenados en la base de datos.
	 * @return Una lista con todos los tipos de caso.
	 * @throws PersistenciaException Si se produjo algún error en la operación.
	 */
	public List<TipoCaso> getTipos() throws PersistenciaException{
		return sqlTipos.getTipos();
	}
	
	/**
	 * Elimina un tipo de caso de la base de datos.
	 * @param nombre El nombre del tipo de caso a eliminar.
	 * @return Si la operación tuvo éxito o no.
	 * @throws PersistenciaException Si se produjo algún error durante la operación.
	 */
	public boolean removeTipo (String nombre) throws PersistenciaException {
		return sqlTipos.removeTipo(nombre);
	}
	
	/**
	 * Añade un nuevo usuario a la base de datos.
	 * @param u El usuario a añadir.
	 * @param casos La lista de tipos de caso a los que está asignado inicialmente.
	 * @return Si la operación tuvo éxito o no.
	 * @throws PersistenciaException Si se produjo algún error en la operación.
	 */
	public boolean addUsuario(Usuario u,List<String> casos) throws PersistenciaException{
		return sqlUsuarios.addUsuario(u,casos);
	}
	
	/**
	 * Devuelve un HashMap que contiene todos los usuarios del sistema. Mapea el nombre de
	 * cada usuario con el objeto de tipo Usuario correspondiente.
	 * @return El HashMap con todos los usuarios del sistema.
	 * @throws PersistenciaException Si se produjo algún error en la operación.
	 */
	public HashMap<String, Usuario> getUsuarios() throws PersistenciaException {
		return sqlUsuarios.getUsuarios();
	}
	
	/**
	 * Añade un nuevo tipo de caso a la base de datos.
	 * @param tc El tipo de caso a añadir.
	 * @return Si la operación tuvo éxito o no.
	 * @throws PersistenciaException Si se produjo algún error durante la operación.
	 */
	public boolean addTipo (TipoCaso tc) throws PersistenciaException {
		return sqlTipos.addTipo(tc);
	}
	
	/**
	 * Elimina un usuario de la base de datos.
	 * @param u El usuario a eliminar.
	 * @return Si la operación tuvo éxito o no.
	 * @throws PersistenciaException Si se produjo algún error durante la operación.
	 */
	public boolean removeUsuario (Usuario u) throws PersistenciaException {
		return sqlUsuarios.removeUsuario(u);
	}
	
	/**
	 * Modifica un tipo de caso en la base de datos. Mantiene los usuarios asignados a él y
	 * las estadísticas generadas hasta el momento.
	 * @param tc El tipo de caso modificado.
	 * @return Si la operación tuvo éxito o no.
	 * @throws PersistenciaException Si se produjo algún error en la operación.
	 */
	public boolean modifyTipo(TipoCaso tc) throws PersistenciaException {
		return sqlTipos.updateTipoCaso(tc);
	}
	
	/**
	 * Modifica un usuario, cambiando su contraseña, su tipo o los tipos de caso a los que 
	 * está asociado.
	 * @param u El usuario modificado.
	 * @param casos La lista de casos a los que está asociado.
	 * @return Si la operación tuvo éxito o no.
	 * @throws PersistenciaException Si se produjo algún error durante la operación.
	 */
	public boolean modUsuario (Usuario u,List<String> casos)  throws PersistenciaException {
		return sqlUsuarios.modUsuario(u,casos);
	}
	
	/** 
	 * Obtiene las estadísticas de ejecución de un usuario y tipo concretos.
	 * Si el usuario o el tipo tienen por nombre "ver todos", se calculan la media/suma/maximo (según la estadistica)
	 * de cada valor para todos los usuarios o casos.
	 * @param u El usuario para el que se quieren las estadísticas
	 * @param tc El caso para el que se quieren las estadísticas
	 * @return La estadística solicitada.
	 * @throws PersistenciaException en caso de que el caso/usuario no existan o se produzca un error de conexión.
	 */
	public Estadistica getEstadistica(Usuario u, TipoCaso tc) throws PersistenciaException{
		return sqlOtros.getEstadistica(u,tc);
	}
	
	/**
	 * Limpia las estadisticas para un tipo de caso y/o un usuario concretos
	 * @param u El usuario. Puede ser el valor "ver todos" si se desea borrar para todos los usuarios del caso.
	 * @param tc El tipo de caso. Puede ser el valor "ver todos" si se desea borrar para todos los casos del usuario .
	 * @throws PersistenciaException Si ha habido un error en la operación 
	 * (Normalmente Usuario/caso inexistente o error de conexión).
	 */
	public void limpiarEstadistica(Usuario u, TipoCaso tc) throws PersistenciaException{
		sqlOtros.limpiarEstadistica(u,tc);
	}

	/** 
	 * Actualiza las estadísticas de un usuario tras una ejecución del ciclo CBR.
	 * @param u Usuario que realizó la ejecución.
	 * @param tc Tipo de caso de la consulta.
	 * @param calidad Calidad de la solución dada, evaluada por el usuario.
	 * @throws PersistenciaException En caso de cualquier error en la conexión 
	 * con la base de datos.
	 */
	public void updateEstadistica(Usuario u, TipoCaso tc, int calidad) throws PersistenciaException {
		sqlOtros.updateEstadistica(u, tc, calidad);
	}
	
}
