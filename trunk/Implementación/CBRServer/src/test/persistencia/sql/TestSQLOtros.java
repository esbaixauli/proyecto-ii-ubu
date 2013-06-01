package test.persistencia.sql;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.Estadistica;
import servidorcbr.modelo.Tecnica;
import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.TipoUsuario;
import servidorcbr.modelo.Usuario;
import servidorcbr.modelo.excepciones.PersistenciaException;
import servidorcbr.persistencia.sql.SQLFacade;

/**
 * Suite de test que comprueba que la gestión de estadísticas funciona correctamente.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
public class TestSQLOtros {

	/**
	 * Usuarios de prueba. 
	 */
	private Usuario u, u2;
	
	/**
	 * Tipos de caso de prueba.
	 */
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
		
		HashMap<String,Atributo> atbos = new HashMap<String,Atributo>();
		Atributo a = new Atributo();
		a.setNombre("atbo1");
		a.setTipo("I");
		atbos.put(a.getNombre(), a);
		test.setAtbos(atbos);
		
		List<Tecnica> tRec = new ArrayList<Tecnica>(1);
		Tecnica t1 = new Tecnica();
		t1.setNombre("t1");
		tRec.add(t1);
		test.setDefaultRec(t1);
		test.setTecnicasRecuperacion(tRec);
		
		List<Tecnica> tReu = new ArrayList<Tecnica>(1);
		Tecnica t2 = new Tecnica();
		t2.setNombre("t2");
		tReu.add(t2);
		test.setDefaultReu(t2);
		test.setTecnicasReutilizacion(tReu);
		
		List<Tecnica> tRev = new ArrayList<Tecnica>(1);
		Tecnica t3 = new Tecnica();
		t3.setNombre("t3");
		tRev.add(t3);
		test.setDefaultRev(t3);
		test.setTecnicasRevision(tRev);
		
		List<Tecnica> tRet = new ArrayList<Tecnica>(1);
		Tecnica t4 = new Tecnica();
		t4.setNombre("t4");
		tRet.add(t4);
		test.setDefaultRet(t4);
		test.setTecnicasRetencion(tRet);
		
		test2 = new TipoCaso();
		test2.setNombre("test2");
		test2.setAtbos(atbos);
		test2.setDefaultRec(t1);
		test2.setTecnicasRecuperacion(tRec);
		test2.setDefaultReu(t2);
		test2.setTecnicasReutilizacion(tReu);
		test2.setDefaultRev(t3);
		test2.setTecnicasRevision(tRev);
		test2.setDefaultRet(t4);
		test2.setTecnicasRetencion(tRet);
		
		u2 = new Usuario();
		u2.setNombre("testerBasico");
		u2.setPassword("abcd");
		u2.setTipo(TipoUsuario.UBASICO);
		
		List<String> casos = new ArrayList<String>(2);
		casos.add("test");
		casos.add("test2");
		
		try {
			SQLFacade.getInstance().addTipo(test);
			SQLFacade.getInstance().addTipo(test2);
			SQLFacade.getInstance().addUsuario(u, casos);
			SQLFacade.getInstance().addUsuario(u2, casos);
		} catch (PersistenciaException e) {e.printStackTrace();}
		
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
			SQLFacade.getInstance().removeUsuario(u2);
		} catch (PersistenciaException e) {
			e.printStackTrace();
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
		} catch (PersistenciaException ex) {
			ex.printStackTrace();
		}
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
			SQLFacade.getInstance().updateEstadistica(u2, test, 50);
			e = SQLFacade.getInstance().getEstadistica(u2, test);
		} catch (PersistenciaException ex) {
			ex.printStackTrace();
		}
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
		try{
			SQLFacade.getInstance().updateEstadistica(u, test, 10);
			SQLFacade.getInstance().updateEstadistica(u, test2, 20);
			SQLFacade.getInstance().limpiarEstadistica(u, test);
			e = SQLFacade.getInstance().getEstadistica(u, test);
			e2 = SQLFacade.getInstance().getEstadistica(u, test2);
		} catch (PersistenciaException ex) {
			ex.printStackTrace();
		}
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
		e.setCalidadUltima(5);
		try{
			SQLFacade.getInstance().updateEstadistica(u, test, 10);
			SQLFacade.getInstance().updateEstadistica(u, test2, 20);
			test.setNombre("ver todos");
			SQLFacade.getInstance().limpiarEstadistica(u, test);
			e = SQLFacade.getInstance().getEstadistica(u, test);
			e2 = SQLFacade.getInstance().getEstadistica(u, test2);
		} catch (PersistenciaException ex) {
			ex.printStackTrace();
		}
		assertEquals("Borrar Est Global:No se limpia la estadistica 1", 0, e.getCalidadUltima());
		assertEquals("Borrar Est Global: No se limpia la estadistica 2", 0, e2.getCalidadUltima());
	}
}
