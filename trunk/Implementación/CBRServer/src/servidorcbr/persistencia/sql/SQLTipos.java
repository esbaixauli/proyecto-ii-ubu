package servidorcbr.persistencia.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.Estadistica;
import servidorcbr.modelo.Parametro;
import servidorcbr.modelo.Tecnica;
import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.excepciones.PersistenciaException;

/**
 * Clase que gestiona el acceso a la base de datos relacional (HSQLDB) para la gestión de
 * tipos de caso.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
public class SQLTipos {
	
	/**
	 * Conexión a la base de datos.
	 */
	private Connection conn;
	
	/**
	 * Constructor de la clase. Visibilidad protected para que solo se pueda instanciar desde
	 * la clase SQLFacade.
	 * @param conn Conexión a la base de datos.
	 */
	protected SQLTipos (Connection conn) {
		this.conn = conn;
	}
	
	/**
	 * Recupera todos los tipos de caso a los que está asociado un usuario.
	 * @param usuario El nombre del usuario.
	 * @return La lista de tipos de caso asociados al usuario.
	 * @throws PersistenciaException Si se produjo algún error en la operación.
	 */
	public List<TipoCaso> getTipos(String usuario) throws PersistenciaException{
		try {
			PreparedStatement ps = conn.prepareStatement("select caso.* from caso join " +
					"caso_usuario on id_caso=caso.id"
					+ " join usuario on id_usuario=usuario.id where usuario.nombre=?;");
			ps.setString(1, usuario);
			return getTipos(ps);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PersistenciaException(e);
		}
	}
	
	/**
	 * Recupera todos los tipos de caso almacenados en la base de datos.
	 * @return Una lista con todos los tipos de caso.
	 * @throws PersistenciaException Si se produjo algún error en la operación.
	 */
	public List<TipoCaso> getTipos() throws PersistenciaException{
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement("SELECT * FROM caso;");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PersistenciaException(e);
		}
		return getTipos(ps);
	}
	
	/**
	 * Busca el id de un objeto del modelo en la base de datos, dado su nombre.
	 * @param tabla El tipo de objeto buscado. Puede ser "caso", "usuario" o "tecnica".
	 * @param nombre El nombre del objeto del que se quiere el id.
	 * @return El id del objeto buscado.
	 * @throws SQLException Si se produjo algún error en la operación.
	 * @throws PersistenciaException Si no encuentra el objeto buscado.
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

	/**
	 * Borra los parámetros asociados a las técnicas de un tipo de caso.
	 * @param idCaso Id del tipo de caso del que se quieren borrar los parámetros.
	 * @throws SQLException Si se produjo algún error en la operación.
	 */
	private void removeParamTipo(int idCaso) throws SQLException{
		PreparedStatement ps;
		ResultSet rs;
		ps = conn.prepareStatement("SELECT id_parametro FROM caso_tecnica_parametro WHERE id_caso=? AND id_parametro!=0;");
		ps.setInt(1, idCaso);
		rs = ps.executeQuery();
		while(rs.next()){
			ps= conn.prepareStatement("DELETE FROM parametro WHERE id=?");
			ps.setInt(1,rs.getInt("id_parametro"));
			ps.executeUpdate();
		}
	}
	
	/**
	 * Elimina un tipo de caso de la base de datos.
	 * @param nombre El nombre del tipo de caso a eliminar.
	 * @return Si la operación tuvo éxito o no.
	 * @throws PersistenciaException Si se produjo algún error durante la operación.
	 */
	public boolean removeTipo (String nombre) throws PersistenciaException {
		boolean exito = false;
		try {
			PreparedStatement ps;
			
			int id= buscarId("caso", nombre);
			
			removeParamTipo(id);
			
			ps = conn.prepareStatement("DELETE FROM caso WHERE id=?;");
			ps.setInt(1, id);
			exito= (ps.executeUpdate()>0);	
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PersistenciaException(e);
		}
		return exito;
	}
	
	/**
	 * Añade un nuevo tipo de caso a la base de datos.
	 * @param tc El tipo de caso a añadir.
	 * @return Si la operación tuvo éxito o no.
	 * @throws PersistenciaException Si se produjo algún error durante la operación.
	 */
	public boolean addTipo (TipoCaso tc) throws PersistenciaException {
		int exito = 0; 
		
		try { 
			PreparedStatement psT = conn
					.prepareStatement("INSERT INTO caso (nombre) VALUES ?;");
			psT.setString(1, tc.getNombre());
			exito = psT.executeUpdate();

			int id = buscarId("caso", tc.getNombre());
			
			exito += terminaAddTipo(id, tc);
			
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new PersistenciaException(ex);
		}
		
		return (exito == tc.getAtbos().size()+1);
	}

	/**
	 * Inserta en la base de datos los atributos y técnicas que forman parte de un tipo de caso.
	 * @param id El id del tipo de caso.
	 * @param tc El tipo de caso (que contiene la información sobre los atributos).
	 * @return El número de atributos insertados.
	 * @throws SQLException Si se produjo algún error durante la operación.
	 * @throws PersistenciaException Si no encuentra en la base de datos algo que debería estar.
	 */
	private int terminaAddTipo(int id, TipoCaso tc)	throws SQLException, PersistenciaException {
		int exito = 0;
		for (Atributo a : tc.getAtbos().values()) {
			PreparedStatement psA = conn
					.prepareStatement("INSERT INTO atributo VALUES ?,?,?,?,?,?,?;");
			psA.setString(1, a.getNombre());
			psA.setString(2, a.getTipo());
			psA.setDouble(3, a.getPeso());
			psA.setString(4, a.getMetrica());
			psA.setDouble(5, a.getParamMetrica());
			psA.setInt(6, id);
			psA.setBoolean(7, a.getEsProblema());
			exito += psA.executeUpdate();
		}
		
		addTecnicasCaso(id, tc.getTecnicasRecuperacion(), "rec");
		addTecnicasCaso(id, tc.getTecnicasReutilizacion(), "reu");
		addTecnicasCaso(id, tc.getTecnicasRevision(), "rev");
		addTecnicasCaso(id, tc.getTecnicasRetencion(), "ret");
		
		addTecnicasDefault(id, tc);
		return exito;
	}
	
	/**
	 * Almacena en la base de datos las técnicas por defecto de cada una de las etapas para un
	 * tipo de caso dado.
	 * @param id El id del tipo de caso.
	 * @param tc El tipo de caso que contiene la información a guardar en la base de datos.
	 * @throws SQLException Si se produjo algún error durante la operación.
	 * @throws PersistenciaException Si no encuentra alguna de las técnicas en la base de datos.
	 */
	private void addTecnicasDefault(int id, TipoCaso tc) throws SQLException, PersistenciaException {
		int[] ids = new int[4];
		
		ids[0] = buscarId("tecnica", tc.getDefaultRec().getNombre());
		ids[1] = buscarId("tecnica", tc.getDefaultReu().getNombre());
		ids[2] = buscarId("tecnica", tc.getDefaultRev().getNombre());
		ids[3] = buscarId("tecnica", tc.getDefaultRet().getNombre());
		
		PreparedStatement ps = conn.prepareStatement("UPDATE caso SET defaultRec=?,defaultReu=?,defaultRev=?,defaultRet=? WHERE id=?;");
		ps.setInt(1, ids[0]);
		ps.setInt(2, ids[1]);
		ps.setInt(3, ids[2]);
		ps.setInt(4, ids[3]);
		ps.setInt(5, id);
		ps.executeUpdate();
	}

	/**
	 * Rellena una lista de tipos de caso dado un PreparedStatement (método en el que delegan
	 * los otros métodos de recuperación de tipos de caso).
	 * @param ps El PreparedStatement que contiene la consulta sql que devuelve los tipos requeridos.
	 * @return La lista de tipos de caso ya rellenados.
	 * @throws PersistenciaException Si se produjo algún error durante la operación.
	 */
	private List<TipoCaso> getTipos(PreparedStatement ps) throws PersistenciaException {
		List<TipoCaso> lista = null;
		try {
			ResultSet rs = ps.executeQuery();
			lista = new ArrayList<TipoCaso>();
			while (rs.next()) {
				TipoCaso tc = new TipoCaso();
				tc.setNombre(rs.getString("nombre"));
				
				PreparedStatement ps2 = 
						conn.prepareStatement("SELECT * FROM atributo WHERE caso="+rs.getInt("id")+";");
				ResultSet rs2 = ps2.executeQuery();
				getAtbosCaso(tc, rs2);
				
				ps2 = conn.prepareStatement("SELECT * FROM caso_tecnica_parametro JOIN tecnica ON caso_tecnica_parametro.id_tecnica=tecnica.id WHERE caso_tecnica_parametro.id_caso="+rs.getInt("id")+";");
				rs2 = ps2.executeQuery();
				getTecnicasCaso(tc, rs, rs2, rs.getInt("id"));
				
				lista.add(tc);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PersistenciaException(e);
		}
		
		return lista;
	}
	
	/**
	 * Rellena un tipo de caso con los atributos que lo componen.
	 * @param tc El tipo de caso a rellenar.
	 * @param rs2 El ResultSet que contiene la información de los atributos.
	 * @throws SQLException Si se produjo algún error durante la operación.
	 */
	private void getAtbosCaso(TipoCaso tc, ResultSet rs2) throws SQLException{
		HashMap<String, Atributo> atbos = new HashMap<String, Atributo> ();
		while (rs2.next()) {
			Atributo a = new Atributo();
			a.setNombre(rs2.getString("nombre"));
			a.setTipo(rs2.getString("tipo"));
			a.setPeso(rs2.getDouble("peso"));
			a.setMetrica(rs2.getString("metrica"));
			a.setParamMetrica(rs2.getDouble("parammetrica"));
			a.setEsProblema(rs2.getBoolean("problema"));
			atbos.put(a.getNombre(), a);
		}
		tc.setAtbos(atbos);
	}
	
	/**
	 * Rellena un objeto de tipo TipoCaso con las técnicas asignadas a él en la base de datos.
	 * @param tc El tipo de caso a rellenar.
	 * @param rsCaso El ResultSet con la información del caso.
	 * @param rsTec El ResultSet con la información de las técnicas.
	 * @param idCaso El id del caso en la base de datos.
	 * @throws SQLException Si se produjo algún error durante la operación.
	 */
	private void getTecnicasCaso(TipoCaso tc, ResultSet rsCaso, ResultSet rsTec, int idCaso) throws SQLException {
		ArrayList<Tecnica> lRec = new ArrayList<Tecnica>();
		ArrayList<Tecnica> lReu = new ArrayList<Tecnica>();
		ArrayList<Tecnica> lRev = new ArrayList<Tecnica>();
		ArrayList<Tecnica> lRet = new ArrayList<Tecnica>();
		Tecnica t = null;
		boolean repe = false;
		while (rsTec.next()) {
			switch (rsTec.getString("tipo")) {
			case "rec":
				for (Tecnica tGuardada : lRec) {
					if (tGuardada.getNombre().equals(rsTec.getString("tecnica.nombre"))) {
						addParametroTecnica(tGuardada, rsTec.getInt("caso_tecnica_parametro.id_parametro"));
						repe = true;
					}
				}
				if (!repe) {
					t = addTecnicaParametros(lRec, idCaso, rsTec.getInt("tecnica.id"), rsTec.getString("tecnica.nombre"));
					if (rsTec.getInt("tecnica.id") == rsCaso.getInt("caso.defaultRec")) {
						tc.setDefaultRec(t);
					}
				} else {
					repe = false;
				}
				break;
			case "reu":
				for (Tecnica tGuardada : lReu) {
					if (tGuardada.getNombre().equals(rsTec.getString("tecnica.nombre"))) {
						addParametroTecnica(tGuardada, rsTec.getInt("caso_tecnica_parametro.id_parametro"));
						repe = true;
					}
				}
				if (!repe) {
					t = addTecnicaParametros(lReu, idCaso, rsTec.getInt("tecnica.id"), rsTec.getString("tecnica.nombre"));
					if (rsTec.getInt("tecnica.id") == rsCaso.getInt("caso.defaultReu")) {
						tc.setDefaultReu(t);
					}
				} else {
					repe = false;
				}
				break;
			case "rev":
				for (Tecnica tGuardada : lRev) {
					if (tGuardada.getNombre().equals(rsTec.getString("tecnica.nombre"))) {
						addParametroTecnica(tGuardada, rsTec.getInt("caso_tecnica_parametro.id_parametro"));
						repe = true;
					}
				}
				if (!repe) {
					t = addTecnicaParametros(lRev, idCaso, rsTec.getInt("tecnica.id"), rsTec.getString("tecnica.nombre"));
					if (rsTec.getInt("tecnica.id") == rsCaso.getInt("caso.defaultRev")) {
						tc.setDefaultRev(t);
					}
				} else {
					repe = false;
				}
				break;
			case "ret":	
				for (Tecnica tGuardada : lRet) {
					if (tGuardada.getNombre().equals(rsTec.getString("tecnica.nombre"))) {
						addParametroTecnica(tGuardada, rsTec.getInt("caso_tecnica_parametro.id_parametro"));
						repe = true;
					}
				}
				if (!repe) {
					t = addTecnicaParametros(lRet, idCaso, rsTec.getInt("tecnica.id"), rsTec.getString("tecnica.nombre"));
					if (rsTec.getInt("tecnica.id") == rsCaso.getInt("caso.defaultRet")) {
						tc.setDefaultRet(t);
					}
				} else {
					repe = false;
				}
				break;
			default:	
			}
		}
		tc.setTecnicasRecuperacion(lRec);
		tc.setTecnicasReutilizacion(lReu);
		tc.setTecnicasRevision(lRev);
		tc.setTecnicasRetencion(lRet);
	}
	
	/**
	 * Añade un parámetro a una técnica ya guardada en la lista correspondiente.
	 * @param tGuardada La técnica a la que añadir el parámetro.
	 * @param idParam Id del parámetro en la base de datos.
	 * @throws SQLException 
	 */
	private void addParametroTecnica(Tecnica tGuardada, int idParam) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM parametro WHERE id=?;");
		ps.setInt(1, idParam);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Parametro p = new Parametro();
			p.setNombre(rs.getString("nombre"));
			p.setValor(rs.getDouble("valor"));
			tGuardada.getParams().add(p);
		}
	}

	/**
	 * Recupera de la base de datos una técnica con todos sus parámetros y la añade a la lista
	 * de técnicas de su tipo.
	 * @param lista La lista a la que se añade la técnica.
	 * @param idCaso El id del tipo de caso al que se asocian las técnicas.
	 * @param idTecnica El id de la técnica a recuperar.
	 * @param nomTecnica El nombre de la técnica a recuperar.
	 * @return La técnica recuperada.
	 * @throws SQLException Si se produce algún error durante la operación.
	 */
	private Tecnica addTecnicaParametros(List<Tecnica> lista, int idCaso, int idTecnica,
			String nomTecnica) throws SQLException {
		Tecnica t = new Tecnica();
		t.setNombre(nomTecnica);
		List<Parametro> listaP = new ArrayList<Parametro>(2);
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM caso_tecnica_parametro JOIN parametro ON caso_tecnica_parametro.id_parametro=parametro.id WHERE id_caso=? AND id_tecnica=?;");
		ps.setInt(1, idCaso);
		ps.setInt(2, idTecnica);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			if (rs.getInt("parametro.id") != 0) {
				Parametro p = new Parametro();
				p.setNombre(rs.getString("parametro.nombre"));
				p.setValor(rs.getDouble("parametro.valor"));
				listaP.add(p);
			}
		}
		t.setParams(listaP);
		lista.add(t);
		return t;
	}

	/**
	 * Guarda en la base de datos la relación de técnicas de un determinado tipo y parámetros
	 * de configuración asociados a un tipo de caso. 
	 * @param idCaso El id del tipo de caso.
	 * @param lista La lista de técnicas a asociar.
	 * @param tipo La etapa a la que corresponden las técnicas. Puede ser "rec", "reu", "rev" o "ret".
	 * @throws SQLException Si se produjo algún error en la operación.
	 * @throws PersistenciaException Si añade una técnica o parámetro nuevos y no los encuentra
	 * después de insertarlos.
	 */
	private void addTecnicasCaso (int idCaso, List<Tecnica> lista, String tipo) throws SQLException, PersistenciaException {
		for (Tecnica t : lista) {
			if (t.getEnabled()) {
				int id;
				try {
					id = buscarId("tecnica", t.getNombre());
				} catch (PersistenciaException ex) {
					id = addTecnica(t, tipo);
				}

				if (t.getParams() != null && (! t.getParams().isEmpty())) {
					for (Parametro p : t.getParams()) {
						int idp = addParametro(p);

						PreparedStatement ps = conn.prepareStatement("INSERT INTO caso_tecnica_parametro VALUES (?,?,?);");
						ps.setInt(1, idCaso);
						ps.setInt(2, id);
						ps.setInt(3, idp);
						ps.executeUpdate();
					}
				} else {
					PreparedStatement ps = conn.prepareStatement("INSERT INTO caso_tecnica_parametro VALUES (?,?,0);");
					ps.setInt(1, idCaso);
					ps.setInt(2, id);
					ps.executeUpdate();
				}
			}
		}
	}

	/**
	 * Inserta un parámetro (para configurar una técnica) en la base de datos.
	 * @param p El parámetro a insertar.
	 * @return El id del nuevo parámetro.
	 * @throws SQLException Si se produjo algún error en la operación.
	 * @throws PersistenciaException Si no encuentra el id del parámetro una vez insertado.
	 */
	private int addParametro(Parametro p) throws SQLException,
			PersistenciaException {
		ResultSet rs;
		PreparedStatement psp = conn.prepareStatement("INSERT INTO parametro(nombre,valor) VALUES(?,?);");
		psp.setString(1, p.getNombre());
		psp.setDouble(2, p.getValor());
		psp.executeUpdate();
		
		psp = conn.prepareStatement("SELECT MAX(id) FROM parametro WHERE nombre=?;");
		psp.setString(1, p.getNombre());
		rs = psp.executeQuery();
		int idp = -1;
		while (rs.next()) {
			idp = rs.getInt(1);
		}
		if (idp == -1) {
			throw new PersistenciaException("Error al insertar el parámetro "+p.getNombre());
		}
		return idp;
	}

	/**
	 * Inserta en la base de datos una técnica que anteriormente no estaba.
	 * @param t La técnica a añadir.
	 * @param tipo La etapa a la que corresponde. Puede ser "rec", "reu", "rev" o "ret".
	 * @return El Id de la técnica insertada.
	 * @throws SQLException Si se produce algún error en la operación.
	 * @throws PersistenciaException Si no encuentra la técnica una vez insertada.
	 */
	private int addTecnica(Tecnica t, String tipo) throws SQLException,
			PersistenciaException {
		PreparedStatement pst = conn.prepareStatement("INSERT INTO tecnica(nombre,tipo) VALUES (?,?);");
		pst.setString(1, t.getNombre());
		pst.setString(2, tipo);
		pst.executeUpdate();
		return buscarId("tecnica", t.getNombre());
	}

	/**
	 * Modifica un tipo de caso en la base de datos. Mantiene los usuarios asignados a él y
	 * las estadísticas generadas hasta el momento.
	 * @param tc El tipo de caso modificado.
	 * @return Si la operación tuvo éxito o no.
	 * @throws PersistenciaException Si se produjo algún error en la operación.
	 */
	public boolean updateTipoCaso(TipoCaso tc) throws PersistenciaException{
		int exito = 0;
		try {
			int id = buscarId("caso", tc.getNombre());
			List<Integer> usuarios=new ArrayList<Integer>();
			List<Estadistica> estadisticas = new ArrayList<Estadistica>();
			PreparedStatement psUsuarios = conn.prepareStatement("Select id_usuario,ejecTotales,mediacalidad," +
					"ejecSatisfactorias,ejecInusables,fechaUltima,calidadUltima"
						+" from caso_usuario where id_caso=?");
			psUsuarios.setInt(1, id);
			ResultSet rs=psUsuarios.executeQuery();
			while(rs.next() ){
				usuarios.add(rs.getInt(1));
				Estadistica e = new Estadistica();
				e.setEjecTotales(rs.getLong(2));
				e.setMediaCalidad(rs.getDouble(3));
				e.setEjecSatisfactorias(rs.getLong(4));
				e.setEjecInusables(rs.getLong(5));
				e.setFechaUltima(rs.getDate(6));
				e.setCalidadUltima(rs.getLong(7));
				estadisticas.add(e);
			}
			
			removeTipo(tc.getNombre());

			PreparedStatement ps = conn.prepareStatement("INSERT INTO caso(id, nombre) VALUES (?,?);");
			ps.setInt(1, id);
			ps.setString(2, tc.getNombre());
			exito += ps.executeUpdate();
			
			for(int i=0;i<usuarios.size();i++){
				psUsuarios = conn.prepareStatement("Insert into caso_usuario (id_caso,id_usuario," +
						"ejecTotales,mediacalidad,ejecSatisfactorias,ejecInusables,fechaUltima,calidadUltima)" +
						" values (?,?,?,?,?,?,?,?)");
				psUsuarios.setInt(1, id);
				psUsuarios.setInt(2, usuarios.get(i));
				psUsuarios.setLong(3, estadisticas.get(i).getEjecTotales());
				psUsuarios.setDouble(4, estadisticas.get(i).getMediaCalidad());
				psUsuarios.setLong(5, estadisticas.get(i).getEjecSatisfactorias());
				psUsuarios.setLong(6,  estadisticas.get(i).getEjecInusables());
				if(estadisticas.get(i).getFechaUltima()!=null){
					psUsuarios.setDate(7,  new java.sql.Date (estadisticas.get(i).getFechaUltima().getTime()));
				}else{
					psUsuarios.setNull(7, java.sql.Types.DATE); 
				}
				psUsuarios.setLong(8,  estadisticas.get(i).getCalidadUltima());
				psUsuarios.executeUpdate();
			}
			
			exito += terminaAddTipo(id, tc);
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new PersistenciaException(ex);
		}
		return (exito == tc.getAtbos().size()+1);
	}
}
