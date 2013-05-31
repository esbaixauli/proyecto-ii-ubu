package test.persistencia.sql;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.TipoUsuario;
import servidorcbr.modelo.Usuario;
import servidorcbr.modelo.excepciones.PersistenciaException;
import servidorcbr.persistencia.sql.SQLFacade;

public class TestSQLUsuarios {
	private Usuario u;
	/**
	 * Pre-test.
	 */
	@Before
	public void setUp(){
		u=new Usuario();
		u.setNombre("test");
		u.setTipo(TipoUsuario.UBASICO);
	}
	/**
	 * Post-test.
	 */
	@After
	public void tearDown(){
		if(u!=null){
			try {
				SQLFacade.getInstance().removeUsuario(u);
			} catch (PersistenciaException e) {
			}
		}
		
	}
	
	/**
	 * Comprueba que un usuario basico es añadido correctamente sin casos.
	 */
	@Test
	public void comprobarAñadirBasico() {
		ArrayList<String> casos = new ArrayList<String>();
		Usuario a=null;
		try {
			SQLFacade.getInstance().addUsuario(u,casos);
			a = SQLFacade.getInstance().getUsuarios().get(u.getNombre());
			List<TipoCaso> l = SQLFacade.getInstance().getTipos(u.getNombre());
			assertTrue("Añadir basico:"+"La lista de casos no es correcta",
					(l==null)||(l.isEmpty()));
		} catch (PersistenciaException e) {
		}
		assertEquals("Añadir basico:"+"El usuario recuperado no es"
		+" igual al esperado",u.getNombre(), a.getNombre());
	}
	
	/**
	 * Comprueba que si se añade un admin sus casos asignados son todos.
	 */
	@Test
	public void comprobarAñadirAdmin(){
		ArrayList<String> casos = new ArrayList<String>();
		u.setTipo(TipoUsuario.ADMINISTRADOR);
		try {
			SQLFacade.getInstance().addUsuario(u,casos);
			//Casos del admin
			List<TipoCaso> l = SQLFacade.getInstance().getTipos(u.getNombre());
			//Casos de todos
			List<TipoCaso> todos = SQLFacade.getInstance().getTipos();
			assertTrue("Añadir admin:"+"Un admin no puede ver todos los casos",
					l.size()==todos.size());
		} catch (PersistenciaException e) {
		}
	
	}

	/**
	 * Comprueba que la asociacion del usuario con los tipos se elimina al borrar.
	 */
	@Test
	public void borrarUsuarioCascadaTipo(){
		try {
			ArrayList<String> casos = new ArrayList<String>();
			SQLFacade.getInstance().addUsuario(u,casos);
			SQLFacade.getInstance().removeUsuario(u);
			String nombre = u.getNombre();
			u=null;
			assertTrue("Borrar usuario:"+
			"La lista de tipos del usuario no se borra en cascada", 
			SQLFacade.getInstance().getTipos(nombre).isEmpty());
		} catch (PersistenciaException e) {}
		
	}
	
	/**
	 * Comprueba que al borrar el usuario no aparece en sucesivas peticiones.
	 */
	@Test
	public void borrarNuevasPeticiones(){
		try {
			ArrayList<String> casos = new ArrayList<String>();
			SQLFacade.getInstance().addUsuario(u,casos);
			int tam = SQLFacade.getInstance().getUsuarios().size();
			SQLFacade.getInstance().removeUsuario(u);
			int tam2 = SQLFacade.getInstance().getUsuarios().size();
			u=null;
			
			assertTrue("Borrar usuario:"+"Error al borrar,el usuario aun existe",tam == tam2+1);
		} catch (PersistenciaException e) {}
	
	}
	
	/**
	 * Comprueba las modificaciones de usuario. Al modificar, el atributo cambia.
	 */
	@Test 
	public void modificarUsuario(){
		try {
			ArrayList<String> casos = new ArrayList<String>();
			u.setPassword("AA");
			SQLFacade.getInstance().addUsuario(u,casos);
			u.setPassword("EE");
			SQLFacade.getInstance().modUsuario(u, casos);
			u = SQLFacade.getInstance().getUsuarios().get(u.getNombre());
			assertTrue("Modificar usuario:"+"Error al modificar, el atributo no se altera",
					u.getPassword().equals("EE"));
		} catch (PersistenciaException e) {}
		
	}
	
	/**
	 * Comprueba que no se pueden añadir usuarios del mismo nombre.
	 * @throws PersistenciaException -
	 */
	@Test (expected=PersistenciaException.class)
	public void anadirUsuarioMismoNombre() throws PersistenciaException{
		ArrayList<String> casos = new ArrayList<String>();
		Usuario copia = new Usuario();
		copia.setTipo(TipoUsuario.ADMINISTRADOR);
		copia.setNombre(u.getNombre());
		try{
			SQLFacade.getInstance().addUsuario(u,casos);
		}catch(PersistenciaException ex){}
		SQLFacade.getInstance().addUsuario(copia, casos);
	}
	
}
