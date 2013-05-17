package test.persistencia.sql;


import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import servidorcbr.modelo.Estadistica;
import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.TipoUsuario;
import servidorcbr.modelo.Usuario;
import servidorcbr.modelo.excepciones.PersistenciaException;
import servidorcbr.persistencia.sql.SQLFacade;



public class TestSQLOtros extends TestCase {

	private Usuario u;
	private TipoCaso test, test2;
	/**
	 * Pre-test.
	 */
	@Before
	public void setUp(){
		test = new TipoCaso();
		u = new Usuario();
		u.setNombre("tester");
		u.setTipo(TipoUsuario.ADMINISTRADOR);
		u.setPassword("tester");
		test.setNombre("test");
		
		test2 = new TipoCaso();
		test2.setNombre("test2");
		List<String> casos = new ArrayList<>(1);
		casos.add("test");
		casos.add("test2");
		try {
			SQLFacade.getInstance().addTipo(test);
			SQLFacade.getInstance().addUsuario(u, casos);
		} catch (PersistenciaException e) {}
		
	}
	/**
	 * Post-test.
	 */
	@After
	public void tearDown(){
	 try {
		 SQLFacade.getInstance().removeTipo("test");
		 SQLFacade.getInstance().removeTipo("test2");
		if(u!=null)
		 SQLFacade.getInstance().removeUsuario(u);
	 } catch (PersistenciaException e) {
		}
	}
	
	/**
	 * Comprueba que las estadísticas se actualizan al añadir al admin.
	 * El admin no tiene filas que le relacionen con un caso
	 * hasta que se ejecute uno de forma explicita. Esto es así ya que un admin
	 * puede ver todos los casos, y no tendría sentido almacenar una relacion para cada
	 * admin y caso.
	 */
	@Test
	public void actualizarEstadisticasAdmin(){
		Estadistica e=null;
		try {
			SQLFacade.getInstance().updateEstadistica(u, test, 50);
			e = SQLFacade.getInstance().getEstadistica(u, test);
		} catch (PersistenciaException ex) {}
		assertNotNull("Actualizar Estadisticas Admin:"+"Error. No se pudo obtener el obj de estadísticas",e);
		assertEquals("La estadística introducida no coincide. Esto ocurre"+
		"cuando la estadística no ha quedado inicializada y por tanto vale 0",50,e.getCalidadUltima());
		
	}
	
	/**
	 * Comprueba que se actualizan las estadisticas de un usuario no admin.
	 * Los usuarios básicos y avanzados siempre tienen asociación con las estadísticas,
	 * a diferencia de los admin.
	 */
	@Test
	public void actualizarEstadisticasBasico(){
		Estadistica e=null;
		try {
			u.setTipo(TipoUsuario.UBASICO);
			SQLFacade.getInstance().updateEstadistica(u, test, 50);
			e = SQLFacade.getInstance().getEstadistica(u, test);
		} catch (PersistenciaException ex) {}
		assertEquals("Actualizar Estadisticas Usuario:"+"La estadística introducida no coincide. Esto ocurre"+
		"cuando la estadística no ha quedado inicializada y por tanto vale 0",50,e.getCalidadUltima());
	}
	
	/**
	 * Comprueba que se borran las estadisticas del usuario para un caso, sin
	 * afectar al resto.
	 */
	@Test
	public void borrarEstadisticasU(){
		Estadistica e=null,e2=null;
		e=new Estadistica();
		e.setCalidadUltima(5);
		try{
		SQLFacade.getInstance().updateEstadistica(u, test, 10);
		SQLFacade.getInstance().updateEstadistica(u, test2, 20);
		SQLFacade.getInstance().limpiarEstadistica(u, test);
		e = SQLFacade.getInstance().getEstadistica(u, test);
		e2 = SQLFacade.getInstance().getEstadistica(u, test2);
		}catch(PersistenciaException ex){}
		assertEquals("Borrar Est Usuario: No se limpia la estadistica", 0, e.getCalidadUltima());
		assertEquals("Borrar Est Usuario: Borrar la estadistica altera a otra",20,e2.getCalidadUltima());
	}
	
	/**
	 * Si el tipo de caso es "ver todos" se limpian las estadisticas de todos.
	 */
	@Test
	public void borrarEstadisticasGlobal(){
		Estadistica e=null,e2=null;
		e=new Estadistica();
		test.setNombre("ver todos");
		e.setCalidadUltima(5);
		try{
		SQLFacade.getInstance().updateEstadistica(u, test, 10);
		SQLFacade.getInstance().updateEstadistica(u, test2, 20);
		SQLFacade.getInstance().limpiarEstadistica(u, test);
		e = SQLFacade.getInstance().getEstadistica(u, test);
		e2 = 	e = SQLFacade.getInstance().getEstadistica(u, test2);
		}catch(PersistenciaException ex){}
		assertEquals("Borrar Est Global:No se limpia la estadistica 1", 0, e.getCalidadUltima());
		assertEquals("Borrar Est Global: No se limpia la estadistica 2", 0, e2.getCalidadUltima());
	}
}
