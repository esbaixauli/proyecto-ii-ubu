package servidorcbr.persistencia.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import servidorcbr.modelo.Estadistica;
import servidorcbr.modelo.TipoUsuario;
import servidorcbr.modelo.Usuario;
import servidorcbr.modelo.excepciones.PersistenciaException;

/**
 * Clase que gestiona el acceso a la base de datos relacional (HSQLDB) para la gestión de
 * usuarios.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
public class SQLUsuarios {

	/**
	 * Conexión a la base de datos.
	 */
	private Connection conn;

	/**
	 * Constructor de la clase. Visibilidad protected para que solo se pueda instanciar desde
	 * la clase SQLFacade.
	 * @param conn Conexión a la base de datos.
	 */
	protected SQLUsuarios(Connection conn) {
		this.conn = conn;
	}

	/**
	 * Añade un nuevo usuario a la base de datos.
	 * @param u El usuario a añadir.
	 * @param casos La lista de tipos de caso a los que está asignado inicialmente.
	 * @return Si la operación tuvo éxito o no.
	 * @throws PersistenciaException Si se produjo algún error en la operación.
	 */
	public boolean addUsuario(Usuario u, List<String> casos)
			throws PersistenciaException {
		boolean exito = false;
		try {
			PreparedStatement ps = conn
					.prepareStatement("insert into usuario (nombre,password,tipo) values ?,?,?;");
			ps.setString(1, u.getNombre());
			ps.setString(2, u.getPassword());
			ps.setString(3, traducirTipoUsuario(u.getTipo()));

			exito = ps.executeUpdate() == 1;
			enlazarCasos(u, casos);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PersistenciaException(e);
		}
		return exito;
	}

	/**
	 * Devuelve un HashMap que contiene todos los usuarios del sistema. Mapea el nombre de
	 * cada usuario con el objeto de tipo Usuario correspondiente.
	 * @return El HashMap con todos los usuarios del sistema.
	 * @throws PersistenciaException Si se produjo algún error en la operación.
	 */
	public HashMap<String, Usuario> getUsuarios() throws PersistenciaException {

		HashMap<String, Usuario> usuarios = new HashMap<String, Usuario>();
		try {
			PreparedStatement ps = conn
					.prepareStatement("SELECT * FROM usuario;");
			ResultSet rs = ps.executeQuery();
			Usuario u;
			while (rs.next()) {
				u = new Usuario();
				u.setNombre(rs.getString("nombre"));
				u.setPassword(rs.getString("password"));
				u.setTipo(traducirTipoUsuario(rs.getString("tipo")));
				usuarios.put(u.getNombre(), u);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PersistenciaException(e);
		}
		return usuarios;
	}

	/**
	 * Obtiene el Id en base de datos de un usuario, dado su nombre.
	 * @param u El nombre del usuario cuyo id se necesita.
	 * @return El id del usuario.
	 * @throws SQLException Si se produjo algún error en las sentencias SQL.
	 * @throws PersistenciaException Si el usuario no se pudo encontrar.
	 */
	private int obtenerId(String u) throws SQLException, PersistenciaException {
		int id = -1;
		PreparedStatement ps = conn
				.prepareStatement("SELECT * FROM usuario WHERE nombre=?;");
		ps.setString(1, u);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			id = rs.getInt(1);
		}
		if (id == -1) {
			throw new PersistenciaException("Error al eliminar el usuario " + u);
		}
		return id;
	}

	/**
	 * Elimina un usuario de la base de datos.
	 * @param u El usuario a eliminar.
	 * @return Si la operación tuvo éxito o no.
	 * @throws PersistenciaException Si se produjo algún error durante la operación.
	 */
	public boolean removeUsuario(Usuario u) throws PersistenciaException {
		int exito = 0;
		try {
			PreparedStatement ps;

			int id = obtenerId(u.getNombre());

			ps = conn.prepareStatement("DELETE FROM usuario WHERE id=?;");
			ps.setInt(1, id);
			exito = ps.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new PersistenciaException(ex);
		}

		return (exito == 1);
	}

	/**
	 * Modifica un usuario, cambiando su contraseña, su tipo o los tipos de caso a los que 
	 * está asociado.
	 * @param u El usuario modificado.
	 * @param casos La lista de casos a los que está asociado.
	 * @return Si la operación tuvo éxito o no.
	 * @throws PersistenciaException Si se produjo algún error durante la operación.
	 */
	public boolean modUsuario(Usuario u, List<String> casos)
			throws PersistenciaException {
		int count = 0;
		try {
			PreparedStatement ps = conn
					.prepareStatement("UPDATE usuario SET password=?,tipo=? WHERE nombre=?;");
			ps.setString(1, u.getPassword());
			ps.setString(2, traducirTipoUsuario(u.getTipo()));
			ps.setString(3, u.getNombre());
			count = ps.executeUpdate();
			//Los administradores no necesitan que se les asocien casos, pueden verlos todos.
			if(!u.getTipo().equals(TipoUsuario.ADMINISTRADOR)){
				enlazarCasos(u, casos);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PersistenciaException(e);
		}
		return (count == 1);
	}

	
	/**
	 * Auxiliar. Establece la relación usuario-n-----n-casos.
	 * @param u El usuario para establecer sus relaciones.
	 * @param casos Los casos con los que se relaciona.
	 * @throws PersistenciaException Si se produce un error de conexión.
	 */
	private void enlazarCasos(Usuario u, List<String> casos)
			throws PersistenciaException {
		try {
			//Obtengo el id de usuario
			int id = obtenerId(u.getNombre());
			int idCaso=-1;
			//Obtengo un mapa con las estadisticas por id que ya existian para cada caso (Para no sobreescribirlas)
			//Esto es necesario porque los casos tras la actualizacion pueden ser distintos a los antiguos.
			HashMap<Integer,Estadistica> estadisticas = obtenerEstadisticas(id);
			
			//Borro las relaciones anteriores
			PreparedStatement borrarps = conn.prepareStatement("delete from caso_usuario where id_usuario=?;");
			borrarps.setInt(1, id);
			borrarps.executeUpdate();
	
			//Por cada caso nuevo
			for (String caso : casos) {
				PreparedStatement ps = conn.prepareStatement("SELECT id from caso where nombre=?;");
				ps.setString(1, caso);
				ResultSet rs= ps.executeQuery();
				while (rs.next()) {
					idCaso = rs.getInt(1);
				}if(idCaso==-1){
					throw new PersistenciaException();
				}
				//Si este caso ya estaba antes de la modificacion, restablezco sus estadisticas
				if(estadisticas.containsKey(idCaso)){
					ps = conn.prepareStatement("insert into caso_usuario(id_caso,id_usuario," +
							"ejectotales,mediacalidad,ejecsatisfactorias,ejecinusables,fechaultima,calidadultima)"+
							" values ?,?,?,?,?,?,?,?;");
					ps.setLong(3, estadisticas.get(idCaso).getEjecTotales());
					ps.setDouble(4,estadisticas.get(idCaso).getMediaCalidad());
					ps.setLong(5, estadisticas.get(idCaso).getEjecSatisfactorias());
					ps.setLong(6, estadisticas.get(idCaso).getEjecInusables());
					if(estadisticas.get(idCaso).getFechaUltima()!=null){
						ps.setDate(7,new java.sql.Date(estadisticas.get(idCaso).getFechaUltima().getTime()));
					}else{
						ps.setDate(7,null);
					}
					ps.setLong(8,estadisticas.get(idCaso).getCalidadUltima());
				}else{//Si es un caso nuevo, no inserto estadisticas para él.
					ps = conn.prepareStatement("insert into caso_usuario(id_caso,id_usuario)"+
							" values ?,?;");
				}
				ps.setInt(1, idCaso);
				ps.setInt(2, id);
				ps.executeUpdate();
			
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new PersistenciaException(e);
			
		}
	}
	
	

	
	/**
	 * Auxiliar. Traduce un tipo de usuario a la notación de la aplicación.
	 * @param t el tipo de usuario "A","UA","UB"
	 * @return el tipo de usuario en forma de enum
	 */
	private TipoUsuario traducirTipoUsuario(String t) {
		t = t.trim();
		if (t.equals("A")) {
			return TipoUsuario.ADMINISTRADOR;
		} else if (t.equals("UA")) {
			return TipoUsuario.UAVANZADO;
		} else {
			return TipoUsuario.UBASICO;
		}

	}

	
	/**
	 * Auxiliar. Traduce un tipo de usuario a la notación de la BD.
	 * @param t Enum del tipo de usuario
	 * @return cadena con el tipo de usuario. "A", "UA" o "UB"
	 */
	private String traducirTipoUsuario(TipoUsuario t) {
		String res;
		if (t.equals(TipoUsuario.ADMINISTRADOR)) {
			res = "A";
		} else if (t.equals(TipoUsuario.UAVANZADO)) {
			res = "UA";
		} else {
			res = "UB";
		}
		return res;
	}
	
	/**
	 * Obtiene las estadisticas de un usuario.
	 * @param id Id del usuario.
	 * @return Mapa cuya clave es el id de cada caso con las estadísticas de los casos del usuario.
	 * @throws SQLException en caso de usuario no encontrado.
	 */
	public HashMap<Integer,Estadistica> obtenerEstadisticas(int id) throws SQLException{
		PreparedStatement psUsuarios = conn.prepareStatement("Select ejecTotales,mediacalidad," +
				"ejecSatisfactorias,ejecInusables,fechaUltima,calidadUltima,id_caso"
					+" from caso_usuario where id_usuario="+id);
		HashMap<Integer,Estadistica> estadisticas = new HashMap<Integer, Estadistica>();
		ResultSet rs=psUsuarios.executeQuery();
		while(rs.next() ){
			Estadistica e = new Estadistica();
			e.setEjecTotales(rs.getLong(1));
			e.setMediaCalidad(rs.getDouble(2));
			e.setEjecSatisfactorias(rs.getLong(3));
			e.setEjecInusables(rs.getLong(4));
			e.setFechaUltima(rs.getDate(5));
			e.setCalidadUltima(rs.getLong(6));
			estadisticas.put(rs.getInt(7),e);
		}
		return estadisticas;
	}

}
