package servidorcbr.persistencia.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.Parametro;
import servidorcbr.modelo.Tecnica;
import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.TipoUsuario;
import servidorcbr.modelo.Usuario;
import servidorcbr.modelo.excepciones.PersistenciaException;

public class SQLFacade {
	
	private Connection conn;
	private static SQLFacade instance=null;
	
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
	}
	
	public static SQLFacade getInstance () throws PersistenciaException {
		if (instance == null)
			instance = new SQLFacade();
		return instance;
	}
	
	public void close() {
		try {
			conn.close();
		} catch (Exception e) {
			
		}
	}
	
	public  List<TipoCaso> getTipos(String usuario) throws PersistenciaException{
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
				addAtbosCaso(tc, rs2);
				
				ps2 = conn.prepareStatement("SELECT * FROM caso_tecnica_parametro JOIN tecnica ON caso_tecnica_parametro.id_tecnica=tecnica.id WHERE caso_tecnica_parametro.id_caso="+rs.getInt("id")+";");
				rs2 = ps2.executeQuery();
				addTecnicasCaso(tc, rs, rs2, rs.getInt("id"), -1);
				
				lista.add(tc);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PersistenciaException(e);
		}
		
		return lista;
	}

	/*Auxiliar. Añade los atributos al caso.*/
	private void addAtbosCaso(TipoCaso tc, ResultSet rs2) throws SQLException{
		HashMap<String, Atributo> atbos = new HashMap<String, Atributo> ();
		while (rs2.next()) {
			Atributo a = new Atributo();
			a.setNombre(rs2.getString("nombre"));
			a.setPeso(rs2.getDouble("peso"));
			a.setTipo(rs2.getString("tipo"));
			atbos.put(a.getNombre(), a);
		}
		tc.setAtbos(atbos);
	}
	
	/*Auxiliar. Añade las tecnicas al caso.*/
	private void addTecnicasCaso(TipoCaso tc, ResultSet rsCaso, ResultSet rsTec, int idCaso, int defaultTec) throws SQLException {
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
			Parametro p = new Parametro();
			p.setNombre(rs.getString("parametro.nombre"));
			p.setValor(rs.getDouble("parametro.valor"));
			listaP.add(p);
		}
		t.setParams(listaP);
		lista.add(t);
		return t;
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
	
	public boolean addUsuario(Usuario u) throws PersistenciaException{
		try{
			PreparedStatement ps = conn.prepareStatement(
					"insert into usuario (nombre,password,tipo) values ?,?,?;");
			ps.setString(1, u.getNombre());
			ps.setString(2, u.getPassword());
			ps.setString(3,traducirTipoUsuario(u.getTipo()));
			return (ps.executeUpdate()==1);
		}catch(SQLException e){
			e.printStackTrace();
			throw new PersistenciaException(e);
		}
	}
	
	public HashMap<String, Usuario> getUsuarios() throws PersistenciaException{
		
		HashMap<String, Usuario> usuarios = new HashMap<String, Usuario>();
		try{
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM usuario;");
		ResultSet rs = ps.executeQuery();
		Usuario u;
		while(rs.next()){
			u = new Usuario();
			u.setNombre(rs.getString("nombre"));
			u.setPassword(rs.getString("password"));
			u.setTipo(traducirTipoUsuario(rs.getString("tipo")));
			usuarios.put(u.getNombre(), u);
		}
		}catch(SQLException e){
			e.printStackTrace();
			throw new PersistenciaException(e);
		}
		return usuarios;		
	}
	
	/*Auxiliar. Traduce un tipo de usuario a la notaci�n de la aplicaci�n.*/
	private TipoUsuario traducirTipoUsuario(String t){
		t = t.trim();
		if(t.equals("A")){
			return TipoUsuario.ADMINISTRADOR;
		}else if(t.equals("UA")){
			return TipoUsuario.UAVANZADO;
		}else{
			return TipoUsuario.UBASICO;
		}

	}
	
	/*Auxiliar. Traduce un tipo de usuario a la notaci�n de la BD.*/
	private String traducirTipoUsuario(TipoUsuario t){
		String res;
		if(t.equals(TipoUsuario.ADMINISTRADOR)){
			res="A";
		}else if(t.equals(TipoUsuario.UAVANZADO)){
			res="UA";
		}else{
			res="UB";
		}
		return res;
	}
	
	public boolean addTipo (TipoCaso tc) throws PersistenciaException {
		int exito = 0; 
		
		try { 
			PreparedStatement psT = conn
					.prepareStatement("INSERT INTO caso (nombre) VALUES '?';");
			psT.setString(1, tc.getNombre());
			exito = psT.executeUpdate();

			PreparedStatement pst = conn
					.prepareStatement("SELECT id FROM caso WHERE nombre='?';");
			pst.setString(1, tc.getNombre());
			ResultSet rs = pst.executeQuery();
			int id = -1;
			while (rs.next()) {
				id = rs.getInt(0);
			}

			for (Atributo a : tc.getAtbos().values()) {
				PreparedStatement psA = conn
						.prepareStatement("INSERT INTO atributo VALUES ?,?,?,?,?;");
				psA.setString(1, a.getNombre());
				psA.setString(2, a.getTipo());
				psA.setDouble(3, a.getPeso());
				psA.setString(4, a.getMetrica());
				psA.setInt(5, id);
				exito += psA.executeUpdate();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new PersistenciaException(ex);
		}
		
		return (exito == tc.getAtbos().size()+1);
	}
	
	public boolean removeUsuario (Usuario u) throws PersistenciaException {
		int exito = 0;
		int count = 0;
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT usuario WHERE nombre='?';");
			ps.setString(1, u.getNombre());
			ResultSet rs = ps.executeQuery();
			
			int id = -1;
			while (rs.next()) {
				id = rs.getInt(0);
			}
			
			ps = conn.prepareStatement("SELECT COUNT(*) FROM caso_usuario WHERE id_usuario=?;");
			ps.setInt(1, id);
			while (rs.next()) {
				count = rs.getInt(0);
			}
			
			ps = conn.prepareStatement("DELETE FROM caso_usuario WHERE id_usuario=?;");
			ps.setInt(1, id);
			exito += ps.executeUpdate();
			
			ps = conn.prepareStatement("DELETE FROM usuario WHERE id=?;");
			ps.setInt(1, id);
			exito += ps.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new PersistenciaException(ex);
		}

		return (exito == count+1);
	}
	
}
