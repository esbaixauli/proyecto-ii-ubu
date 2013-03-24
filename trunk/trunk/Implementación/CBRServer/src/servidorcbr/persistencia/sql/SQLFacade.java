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

public class SQLFacade {
	
	private Connection conn;
	private static SQLFacade instance=null;
	private SQLTipos sqlTipos;
	private SQLUsuarios sqlUsuarios;
	private SQLOtros sqlOtros;
	
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
		return sqlTipos.getTipos(usuario);
	}
	
	public List<TipoCaso> getTipos() throws PersistenciaException{
		return sqlTipos.getTipos();
	}
	
	public boolean removeTipo (String nombre) throws PersistenciaException {
		return sqlTipos.removeTipo(nombre);
	}
	
	public boolean addUsuario(Usuario u,List<String> casos) throws PersistenciaException{
		return sqlUsuarios.addUsuario(u,casos);
	}
	
	public HashMap<String, Usuario> getUsuarios() throws PersistenciaException {
		return sqlUsuarios.getUsuarios();
	}
	
	public boolean addTipo (TipoCaso tc) throws PersistenciaException {
		return sqlTipos.addTipo(tc);
	}
	
	public boolean removeUsuario (Usuario u) throws PersistenciaException {
		return sqlUsuarios.removeUsuario(u);
	}
	
	public boolean modifyTipo(TipoCaso tc) throws PersistenciaException {
		return sqlTipos.updateTipoCaso(tc);
	}
	
	public boolean modUsuario (Usuario u,List<String> casos)  throws PersistenciaException {
		return sqlUsuarios.modUsuario(u,casos);
	}
	
	public Estadistica getEstadistica(Usuario u, TipoCaso tc) throws PersistenciaException{
		return sqlOtros.getEstadistica(u,tc);
	}
	
	public void limpiarEstadistica(Usuario u, TipoCaso tc) throws PersistenciaException{
		sqlOtros.limpiarEstadistica(u,tc);
	}
	
}
