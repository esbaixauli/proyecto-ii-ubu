package servidorcbr.persistencia.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import servidorcbr.modelo.Estadistica;
import servidorcbr.modelo.TipoCaso;
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
	
}
