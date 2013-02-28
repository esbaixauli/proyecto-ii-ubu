package servidorcbr.persistencia.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.Parametro;
import servidorcbr.modelo.Tecnica;
import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.excepciones.PersistenciaException;

public class SQLTipos {
	
	private Connection conn;
	
	protected SQLTipos (Connection conn) {
		this.conn = conn;
	}
	
	public List<TipoCaso> getTipos(String usuario) throws PersistenciaException{
		try {
			PreparedStatement ps = conn.prepareStatement("select caso.* from caso join " +
					"caso_usuario on id_caso=caso.id"
					+ " join usuario on id_usuario=usuario.id where usuario.nombre='?';");
			ps.setString(1, usuario);
			return getTipos(ps);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PersistenciaException(e);
		}
	}
	
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
	
	public boolean removeTipo (String nombre) throws PersistenciaException {
		boolean exito = false;
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT id FROM caso WHERE nombre=?;");
			ps.setString(1, nombre);
			ResultSet rs = ps.executeQuery();
			
			int id = -1;
			while (rs.next()) {
				id = rs.getInt("id");
			}
			if (id == -1) {
				throw new PersistenciaException("Error al eliminar el Tipo de Caso "+nombre);
			}
			
			ps = conn.prepareStatement("SELECT id_parametro FROM caso_tecnica_parametro WHERE id_caso=?;");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			
			ps = conn.prepareStatement("DELETE FROM caso_tecnica_parametro WHERE id_caso=?;");
			ps.setInt(1, id);
			ps.executeUpdate();
			
			ps = conn.prepareStatement("DELETE FROM caso WHERE id=?;");
			ps.setInt(1, id);
			exito= (ps.executeUpdate()>0);
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PersistenciaException(e);
		}
		return exito;
	}
	
	public boolean addTipo (TipoCaso tc) throws PersistenciaException {
		int exito = 0; 
		
		try { 
			PreparedStatement psT = conn
					.prepareStatement("INSERT INTO caso (nombre) VALUES ?;");
			psT.setString(1, tc.getNombre());
			exito = psT.executeUpdate();

			PreparedStatement pst = conn
					.prepareStatement("SELECT id FROM caso WHERE nombre=?;");
			pst.setString(1, tc.getNombre());
			ResultSet rs = pst.executeQuery();
			int id = -1;
			while (rs.next()) {
				id = rs.getInt(1);
			}
			if (id == -1) {
				throw new PersistenciaException("Error al insertar Tipo de Caso "+tc.getNombre());
			}

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
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new PersistenciaException(ex);
		}
		
		return (exito == tc.getAtbos().size()+1);
	}
	
	private void addTecnicasDefault(int id, TipoCaso tc) throws SQLException {
		int[] ids = new int[4];
		
		PreparedStatement ps = conn.prepareStatement("SELECT id FROM tecnica WHERE nombre=?;");
		ps.setString(1, tc.getDefaultRec().getNombre());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ids[0] = rs.getInt(1);
		}
		
		ps = conn.prepareStatement("SELECT id FROM tecnica WHERE nombre=?;");
		ps.setString(1, tc.getDefaultReu().getNombre());
		rs = ps.executeQuery();
		while (rs.next()) {
			ids[1] = rs.getInt(1);
		}
		
		ps = conn.prepareStatement("SELECT id FROM tecnica WHERE nombre=?;");
		ps.setString(1, tc.getDefaultRev().getNombre());
		rs = ps.executeQuery();
		while (rs.next()) {
			ids[2] = rs.getInt(1);
		}
		
		ps = conn.prepareStatement("SELECT id FROM tecnica WHERE nombre=?;");
		ps.setString(1, tc.getDefaultRet().getNombre());
		rs = ps.executeQuery();
		while (rs.next()) {
			ids[3] = rs.getInt(1);
		}
		
		ps = conn.prepareStatement("UPDATE caso SET defaultRec=?,defaultReu=?,defaultRev=?,defaultRet=? WHERE id=?;");
		ps.setInt(1, ids[0]);
		ps.setInt(2, ids[1]);
		ps.setInt(3, ids[2]);
		ps.setInt(4, ids[3]);
		ps.setInt(5, id);
		ps.executeUpdate();
	}

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
				getTecnicasCaso(tc, rs, rs2, rs.getInt("id"), -1);
				
				lista.add(tc);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PersistenciaException(e);
		}
		
		return lista;
	}
	
	/*Auxiliar. Recoge los atributos del caso.*/
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
	
	/*Auxiliar. Recoge las tecnicas asociadas al caso.*/
	private void getTecnicasCaso(TipoCaso tc, ResultSet rsCaso, ResultSet rsTec, int idCaso, int defaultTec) throws SQLException {
		ArrayList<Tecnica> lRec = new ArrayList<Tecnica>();
		ArrayList<Tecnica> lReu = new ArrayList<Tecnica>();
		ArrayList<Tecnica> lRev = new ArrayList<Tecnica>();
		ArrayList<Tecnica> lRet = new ArrayList<Tecnica>();
		Tecnica t = null;
		while (rsTec.next()) {
			switch (rsTec.getString("tipo")) {
			case "rec":	t = addTecnicaParametros(lRec, idCaso, rsTec.getInt("tecnica.id"), rsTec.getString("tecnica.nombre"), rsCaso.getInt("caso.defaultRec"));
						if (rsTec.getInt("tecnica.id") == rsCaso.getInt("caso.defaultRec")) {
							tc.setDefaultRec(t);
						}
						break;
			case "reu":	t = addTecnicaParametros(lReu, idCaso, rsTec.getInt("tecnica.id"), rsTec.getString("tecnica.nombre"), rsCaso.getInt("caso.defaultReu"));
						if (rsTec.getInt("tecnica.id") == rsCaso.getInt("caso.defaultReu")) {
							tc.setDefaultReu(t);
						}
						break;
			case "rev":	t = addTecnicaParametros(lRev, idCaso, rsTec.getInt("tecnica.id"), rsTec.getString("tecnica.nombre"), rsCaso.getInt("caso.defaultRev"));
						if (rsTec.getInt("tecnica.id") == rsCaso.getInt("caso.defaultRev")) {
							tc.setDefaultRev(t);
						}
						break;
			case "ret":	t = addTecnicaParametros(lRet, idCaso, rsTec.getInt("tecnica.id"), rsTec.getString("tecnica.nombre"), rsCaso.getInt("caso.defaultRet"));
						if (rsTec.getInt("tecnica.id") == rsCaso.getInt("caso.defaultRet")) {
							tc.setDefaultRet(t);
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
	
	// Auxiliar. Añade una tecnica con todos sus parámetros a la lista de técnicas de su tipo
	private Tecnica addTecnicaParametros(List<Tecnica> lista, int idCaso, int idTecnica, String nomTecnica, int defaultTec) throws SQLException {
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
	
	// Auxiliar: guarda en la BD la relación de técnicas y parámetros asociados a un caso
	private void addTecnicasCaso (int idCaso, List<Tecnica> lista, String tipo) throws SQLException, PersistenciaException {
		for (Tecnica t : lista) {
			int id = -1;
			PreparedStatement pst = conn.prepareStatement("SELECT id FROM tecnica WHERE nombre=?;");
			pst.setString(1, t.getNombre());
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				id = rs.getInt("id");
			}
			if (id == -1) { // hay que insertar la tecnica
				id = addTecnica(t, tipo);
			}
			
			if (t.getParams() != null && (! t.getParams().isEmpty())) {
				for (Parametro p : t.getParams()) {
					int idp = addParametro(p);

					PreparedStatement ps = conn.prepareStatement("INSERT INTO caso_tecnica_parametro VALUES (?,?,?);");
					ps.setInt(1, idCaso);
					ps.setInt(2, id);
					ps.setInt(3, idp);
					ps.executeQuery();
				}
			} else {
				PreparedStatement ps = conn.prepareStatement("INSERT INTO caso_tecnica_parametro VALUES (?,?,0);");
				ps.setInt(1, idCaso);
				ps.setInt(2, id);
				ps.executeUpdate();
			}
		}
	}

	private int addParametro(Parametro p) throws SQLException,
			PersistenciaException {
		ResultSet rs;
		PreparedStatement psp = conn.prepareStatement("INSERT INTO parametro(nombre,valor) VALUES(?,?);");
		psp.setString(1, p.getNombre());
		psp.setDouble(2, p.getValor());
		psp.executeUpdate();
		
		psp = conn.prepareStatement("SELECT MAX(id) FROM parametro WHERE nombre=?;");
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

	private int addTecnica(Tecnica t, String tipo) throws SQLException,
			PersistenciaException {
		int idt = -1;
		PreparedStatement pst;
		ResultSet rs;
		pst = conn.prepareStatement("INSERT INTO tecnica(nombre,tipo) VALUES (?,?);");
		pst.setString(1, t.getNombre());
		pst.setString(2, tipo);
		pst.executeUpdate();
		
		pst = conn.prepareStatement("SELECT id FROM tecnica WHERE nombre=?;");
		pst.setString(1, t.getNombre());
		rs = pst.executeQuery();
		while (rs.next()) {
			idt = rs.getInt("id");
		}
		if (idt == -1) {
			throw new PersistenciaException("Error al insertar la técnica "+t.getNombre());
		}
		return idt;
	}

}
