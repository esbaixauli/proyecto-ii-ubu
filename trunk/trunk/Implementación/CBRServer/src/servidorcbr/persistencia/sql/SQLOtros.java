package servidorcbr.persistencia.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import servidorcbr.modelo.Estadistica;
import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.TipoUsuario;
import servidorcbr.modelo.Usuario;
import servidorcbr.modelo.excepciones.PersistenciaException;

public class SQLOtros {

	private Connection conn;
	
	protected SQLOtros (Connection conn) {
		this.conn = conn;
	}
	
	
	/** Obtiene las estadísticas de ejecución de un usuario y tipo concretos.
	 * Si el usuario o el tipo tienen por nombre "ver todos", se calculan la media/suma/maximo (según la estadistica)
	 * de cada valor para todos los usuarios o casos.
	 * @param u El usuario para el que se quieren las estadísticas
	 * @param tc El caso para el que se quieren las estadísticas
	 * @return La estadística solicitada.
	 * @throws PersistenciaException en caso de que el caso/usuario no existan o se produzca un error de conexión.
	 */
	public Estadistica getEstadistica(Usuario u, TipoCaso tc) throws PersistenciaException{
		String consulta= "Select sum(ejecTotales),sum(mediacalidad*ejecTotales)/coalesce(nullif(sum(ejecTotales),0),1)," +
				"sum(ejecSatisfactorias),sum(ejecInusables),max(fechaUltima)"+
							"from caso_usuario";
		Estadistica es=null;
		try{
		//Si se pide para un usuario en concreto
		if(! u.getNombre().equals("ver todos")){
			//Busco el id de ese usuario
			int idU = buscarId("usuario",u.getNombre());
			//No necesito usar las funciones de (?) de preparedstatement porque el dato ha sido obtenido de forma interna
			//(No hay riesgo de inyección SQL)
			consulta+=" where id_usuario="+idU;
			//Si se pide tanto un usuario como un tipo de caso concretos
			if(!tc.getNombre().equals("ver todos")){
				int idc= buscarId("caso",tc.getNombre());
				consulta+=" and id_caso="+idc;
			}
		//Si se pide para todos los usuarios, pero un tipo de caso concreto
		}else if(!tc.getNombre().equals("ver todos")){
			int idc= buscarId("caso",tc.getNombre());
			consulta+=" where id_caso="+idc;
		}
		PreparedStatement ps = conn.prepareStatement(consulta);
		ResultSet rs = ps.executeQuery();
		
		es = rellenarEstadistica(rs);
		}catch(SQLException ex){
			throw new PersistenciaException(ex);
		}
		return es;
	}
	
	/**Rellena un objeto Estadística con los resultados de un resultset.
	 * @param rs el resultset con la estadistica.
	 * @return el objeto Estadística.
	 * @throws SQLException si hay un error en el resultset.
	 */
	private Estadistica rellenarEstadistica(ResultSet rs) throws SQLException{
		Estadistica es=null;
		while(rs.next()){
			es=new Estadistica();
			es.setEjecTotales(rs.getLong(1));
			es.setMediaCalidad(rs.getDouble(2));
			es.setEjecSatisfactorias(rs.getLong(3));
			es.setEjecInusables(rs.getLong(4));
			es.setFechaUltima(rs.getDate(5));
			//Busco la calidad correspondiente a esa fecha
			ResultSet rs2= conn.prepareStatement("select max(calidadUltima) from caso_usuario where fechaUltima="
			+rs.getDate(5)).executeQuery();
			while(rs2.next()){
				es.setCalidadUltima(rs2.getLong(1));
			}
		}
		return es;
	}
	
	/**Auxiliar.Busca el id por nombre de una tabla.
	 * @param tabla La tabla en la que buscar.
	 * @param nombre del elemento a buscar.
	 * @return el id del elemento.
	 * @throws SQLException en caso de error de conexión sql.
	 * @throws PersistenciaException en caso de no encontrar el elementos.
	 */
	private int buscarId(String tabla, String nombre) throws SQLException, PersistenciaException {
		PreparedStatement ps = conn.prepareStatement("SELECT id FROM "+tabla+" WHERE nombre=?;");
		ps.setString(1, nombre);
		ResultSet rs = ps.executeQuery();
		int id = -1;
		while (rs.next()) {
			id = rs.getInt("id");
		}
		if (id == -1) {
			throw new PersistenciaException("Error al buscar el "+tabla+" "+nombre);
		}
		return id;	
	}
	
	/**Limpia las estadisticas para un tipo de caso y/o un usuario concretos
	 * @param u El usuario. Puede ser el valor "ver todos" si se desea borrar para todos los usuarios del caso.
	 * @param tc El tipo de caso. Puede ser el valor "ver todos" si se desea borrar para todos los casos del usuario .
	 * @throws PersistenciaException si ha habido un error en la operación 
	 * (Normalmente Usuario/caso inexistente o error de conexión).
	 */
	public void limpiarEstadistica(Usuario u, TipoCaso tc)throws PersistenciaException{
		String consulta = "update caso_usuario set ejectotales=0,mediacalidad=0,ejecsatisfactorias=0," +
				"ejecinusables=0,fechaultima=null,calidadultima=0";
		try {
			//Si es para un usuario concreto
			if(!u.getNombre().equals("ver todos")){

				consulta+=" where id_usuario="+buscarId("usuario",u.getNombre());
				//Si es para un usuario concreto y un tipo concreto
				if(!tc.getNombre().equals("ver todos")){
					consulta+= " and id_caso="+buscarId("caso", tc.getNombre());
				}
				//Si es solo para un tipo concreto
			}else if(!tc.getNombre().equals("ver todos")){
				consulta+="where id_caso="+buscarId("caso", tc.getNombre());
			}
			conn.prepareStatement(consulta).executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PersistenciaException(e);
		}

	}
	
	/** Actualiza las estadísticas de un usuario tras una ejecución del ciclo CBR.
	 * @param u Usuario que realizó la ejecución.
	 * @param tc Tipo de caso de la consulta.
	 * @param calidad Calidad de la solución dada, evaluada por el usuario.
	 * @throws PersistenciaException En caso de cualquier error en la conexión 
	 * con la base de datos.
	 */
	public void updateEstadistica(Usuario u, TipoCaso tc,int calidad) throws PersistenciaException{
		java.util.Date fecha = new Date();
		try{
		int idU= buscarId("usuario", u.getNombre());
		int idC= buscarId("caso",tc.getNombre());
		
		//Si es un administrador y es su primera consulta, se le asocia al tipo de caso.
		if(u.getTipo().equals(TipoUsuario.ADMINISTRADOR)
				&& getEstadistica(u, tc).getEjecTotales()==0){
			insertarEstadisticaAdmin(fecha, calidad, idU, idC);
		}else{
			modificarEstadistica(fecha, calidad, idU, idC);
		}
		}catch(SQLException ex){
			ex.printStackTrace();
			throw new PersistenciaException(ex);
		}
	}


	/**Auxiliar. Actualiza una fila de la tabla 
	 * de estadísticas con los valores indicados cuando se hace una nueva
	 * ejecución del ciclo.
	 * @param fecha La fecha de la nueva ejecución.
	 * @param calidad La calidad de la solución dada.
	 * @param idU El identificador del usuario que realizó la consulta.
	 * @param idC El identificador del tipo de caso de la consulta.
	 * @throws SQLException En caso de que no se consiga actualizar.
	 */
	private void modificarEstadistica(Date fecha, int calidad, int idU, int idC)
			throws SQLException {
		String cal="";
		if(calidad>50){
			cal="ejecSatisfactorias=ejecSatisfactorias+1,";
		}else if(calidad == 0){
			cal="ejecInusables=ejecInusables+1,";
		}
		String sql = "update caso_usuario set ejecTotales=ejecTotales+1," +
				"mediacalidad=(mediacalidad+?)/ejecTotales+1,"+
				cal+"fechaultima=?,calidadUltima=? where id_caso="+idC+
				" and id_usuario="+idU+";";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, calidad);
		java.sql.Date sqlDate = new java.sql.Date(fecha.getTime());
		ps.setDate(2, sqlDate);
		ps.setLong(3, (long)calidad);
		ps.executeUpdate();
	}


	/**Auxiliar. Inserta una nueva fila en la tabla caso_usuario correspondiente
	 * a las estadísticas de un administrador para este caso. Se usa en caso
	 * de que un administrador nunca haya usado un caso y por tanto no tenga
	 * vinculación con él. (Ya que, a diferencia de los usuarios corrientes, los
	 * administradores no se vinculan a los casos, sino que pueden verlos todos).
	 * @param fecha La fecha de la ejecución causante de la estadística.
	 * @param calidad calidad del caso ejecutado.
	 * @param idU id del usuario (Que ha de ser un administrador).
	 * @param idC id del caso.
	 * @throws SQLException En caso de que falle la inserción.
	 */
	private void insertarEstadisticaAdmin(Date fecha, int calidad, int idU,
			int idC) throws SQLException {
		String cal;
		if(calidad>50){
			cal="1,0,";
		}else if(calidad==0){
			cal="0,1,";
		}else{
			cal="0,0,";
		}
		
		String sql = "insert into caso_usuario (id_caso,id_usuario," +
				"ejectotales,mediacalidad,ejecsatisfactorias," +
			"ejecinusables,fechaultima,calidadultima) values "+
				idC+","+idU+","+
				"1,?,"+cal+"?,?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setDouble(1, (double) calidad);
		java.sql.Date sqlDate = new java.sql.Date(fecha.getTime());
		ps.setDate(2, sqlDate);
		ps.setLong(3, (long)calidad);
		ps.executeUpdate();
	}
	
}
