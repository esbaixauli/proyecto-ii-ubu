package servidorcbr.persistencia.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import servidorcbr.modelo.TipoUsuario;
import servidorcbr.modelo.Usuario;
import servidorcbr.modelo.excepciones.PersistenciaException;

public class SQLUsuarios {

	private Connection conn;
	
	protected SQLUsuarios (Connection conn) {
		this.conn = conn;
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
	
	public HashMap<String, Usuario> getUsuarios() throws PersistenciaException {
		
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
	
	public boolean removeUsuario (Usuario u) throws PersistenciaException {
		int exito = 0;
		int count = 0;
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM usuario WHERE nombre=?;");
			ps.setString(1, u.getNombre());
			ResultSet rs = ps.executeQuery();
			
			int id = -1;
			while (rs.next()) {
				id = rs.getInt(1);
			}
			if (id == -1) {
				throw new PersistenciaException("Error al eliminar el usuario "+u.getNombre());
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
	
	public boolean modUsuario (Usuario u) throws PersistenciaException {
		int count = 0;
		try {
			PreparedStatement ps = conn.prepareStatement("UPDATE usuario SET password=?,tipo=? WHERE nombre=?;");
			ps.setString(1, u.getPassword());
			ps.setString(2, traducirTipoUsuario(u.getTipo()));
			ps.setString(3, u.getNombre());
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PersistenciaException(e);
		}
		return (count == 1);
	}
	
	/*Auxiliar. Traduce un tipo de usuario a la notación de la aplicación.*/
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
		
}
