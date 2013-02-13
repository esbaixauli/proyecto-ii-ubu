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
					+ " join usuario on id_usuario=usuario.id where usuario.nombre=?;");
			ps.setString(0, usuario);
			return getTipos(ps);
		} catch (SQLException e) {
			
			throw new PersistenciaException(e);
		}
	}
	
	public List<TipoCaso> getTipos() throws PersistenciaException{
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement("SELECT * FROM caso;");
		} catch (SQLException e) {
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
				lista.add(tc);
				
				PreparedStatement ps2 = 
						conn.prepareStatement("SELECT * FROM atributo WHERE caso="+rs.getInt("id")+";");
				ResultSet rs2 = ps2.executeQuery();
				addAtbosCaso(tc, rs2);
				
				ps2 = conn.prepareStatement("SELECT * FROM caso_tecnica JOIN tecnica ON caso_tecnica.id_tecnica=tecnica.id WHERE caso_tecnica.id_caso="+rs.getInt("id")+";");
				rs2 = ps2.executeQuery();
				addTecnicasCaso(tc,rs2);
				
				lista.add(tc);
			}
			
		} catch (SQLException e) {
			throw new PersistenciaException(e);
		}
		
		return lista;
	}
	
	/*Auxiliar. A�ade los atributos al caso.*/
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
	
	/*Auxiliar. A�ade las tecnicas al caso.*/
	private void addTecnicasCaso(TipoCaso tc,ResultSet rs2) throws SQLException {
		ArrayList<String> lRec = new ArrayList<String>();
		ArrayList<String> lReu = new ArrayList<String>();
		ArrayList<String> lRev = new ArrayList<String>();
		ArrayList<String> lRet = new ArrayList<String>();
		while (rs2.next()) {
			switch (rs2.getString("tipo")) {
			case "rec":	lRec.add(rs2.getString("nombre"));
						break;
			case "reu":	lReu.add(rs2.getString("nombre"));
						break;
			case "rev":	lRev.add(rs2.getString("nombre"));
						break;
			case "ret":	lRet.add(rs2.getString("nombre"));
						break;
			default:	
			}
		}
		tc.setTecnicasRecuperacion(lRec);
		tc.setTecnicasReutilizacion(lReu);
		tc.setTecnicasRevision(lRev);
		tc.setTecnicasRetencion(lRet);
	}
	
	public boolean removeTipo (String nombre) throws PersistenciaException {
		boolean exito = false;
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT id FROM caso WHERE nombre=?;");
			ps.setString(0, nombre);
			ResultSet rs = ps.executeQuery();
			
			int id = -1;
			while (rs.next()) {
				id = rs.getInt("id");
			}
			ps = conn.prepareStatement("DELETE FROM caso_tecnica WHERE id_caso=?;");
			ps.setInt(0, id);
			ps.executeUpdate();
			
			ps = conn.prepareStatement("DELETE FROM caso WHERE id=?;");
			ps.setInt(0, id);
			exito= (ps.executeUpdate()>0);
			
		} catch (SQLException e) {
			throw new PersistenciaException(e);
		}
		return exito;
	}
	
	public boolean addUsuario(Usuario u) throws PersistenciaException{
		try{
			PreparedStatement ps = conn.prepareStatement(
					"insert into usuario (nombre,password,tipo) values ?,?,?;");
			ps.setString(0, u.getNombre());
			ps.setString(1, u.getPassword());
			ps.setString(2,traducirTipoUsuario(u.getTipo()));
			return (ps.executeUpdate()==1);
		}catch(SQLException e){
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
			throw new PersistenciaException(e);
		}
		return usuarios;		
	}
	
	/*Auxiliar. Traduce un tipo de usuario a la notaci�n de la aplicaci�n.*/
	private TipoUsuario traducirTipoUsuario(String t){
		
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
					.prepareStatement("INSERT INTO caso (nombre) VALUES ?;");
			psT.setString(0, tc.getNombre());
			exito = psT.executeUpdate();

			PreparedStatement pst = conn
					.prepareStatement("SELECT id FROM caso WHERE nombre=?;");
			pst.setString(0, tc.getNombre());
			ResultSet rs = pst.executeQuery();
			int id = -1;
			while (rs.next()) {
				id = rs.getInt(0);
			}

			for (Atributo a : tc.getAtbos().values()) {
				PreparedStatement psA = conn
						.prepareStatement("INSERT INTO atributo VALUES ?,?,?,?,?;");
				psA.setString(0, a.getNombre());
				psA.setString(1, a.getTipo());
				psA.setDouble(2, a.getPeso());
				psA.setString(3, a.getMetrica());
				psA.setInt(4, id);
				exito += psA.executeUpdate();
			}
		} catch (SQLException ex) {
			throw new PersistenciaException(ex);
		}
		
		return (exito == tc.getAtbos().size()+1);
	}
	
	public boolean removeUsuario (Usuario u) throws PersistenciaException {
		int exito = 0;
		int count = 0;
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT usuario WHERE nombre=?;");
			ps.setString(0, u.getNombre());
			ResultSet rs = ps.executeQuery();
			
			int id = -1;
			while (rs.next()) {
				id = rs.getInt(0);
			}
			
			ps = conn.prepareStatement("SELECT COUNT(*) FROM caso_usuario WHERE id_usuario=?;");
			ps.setInt(0, id);
			while (rs.next()) {
				count = rs.getInt(0);
			}
			
			ps = conn.prepareStatement("DELETE FROM caso_usuario WHERE id_usuario=?;");
			ps.setInt(0, id);
			exito += ps.executeUpdate();
			
			ps = conn.prepareStatement("DELETE FROM usuario WHERE id=?;");
			ps.setInt(0, id);
			exito += ps.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new PersistenciaException(ex);
		}

		return (exito == count+1);
	}
	
}
