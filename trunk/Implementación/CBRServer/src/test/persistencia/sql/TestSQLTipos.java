package test.persistencia.sql;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.Tecnica;
import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.excepciones.PersistenciaException;
import servidorcbr.persistencia.sql.SQLFacade;

/**
 * Suite de test que comprueba que la gestión de tipos funciona correctamente.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
public class TestSQLTipos {
	
	/**
	 * Tipo de caso de prueba. 
	 */
	private TipoCaso tc;
	
	/**
	 * Lista de técnicas de recuperación de prueba. 
	 */
	private List<Tecnica> tRec;
	
	/**
	 * Lista de técnicas de reutilización de prueba. 
	 */
	private List<Tecnica> tReu;
	
	/**
	 * Lista de técnicas de revisión de prueba. 
	 */
	private List<Tecnica> tRev;
	
	/**
	 * Lista de técnicas de retención de prueba. 
	 */
	private List<Tecnica> tRet;

	/**
	 * Pre-test.
	 */
	@Before
	public void setUp(){
		tc = new TipoCaso();
		tc.setNombre("test");
		HashMap<String, Atributo> atbos = new HashMap<String,Atributo>();
		Atributo a1 = new Atributo(); a1.setNombre("a1"); a1.setTipo("S");
		Atributo a2 = new Atributo(); a2.setNombre("a2"); a2.setTipo("S");
		atbos.put("a1", a1);
		atbos.put("a2", a2);
		tc.setAtbos(atbos);
		
		tRec = new ArrayList<Tecnica>(1);
		Tecnica t1 = new Tecnica();
		t1.setNombre("t1");
		tRec.add(t1);
		tc.setDefaultRec(t1);
		tc.setTecnicasRecuperacion(tRec);
		
		tReu = new ArrayList<Tecnica>(1);
		Tecnica t2 = new Tecnica();
		t2.setNombre("t2");
		tReu.add(t2);
		tc.setDefaultReu(t2);
		tc.setTecnicasReutilizacion(tReu);
		
		tRev = new ArrayList<Tecnica>(1);
		Tecnica t3 = new Tecnica();
		t3.setNombre("t3");
		tRev.add(t3);
		tc.setDefaultRev(t3);
		tc.setTecnicasRevision(tRev);
		
		tRet = new ArrayList<Tecnica>(1);
		Tecnica t4 = new Tecnica();
		t4.setNombre("t4");
		tRet.add(t4);
		tc.setDefaultRet(t4);
		tc.setTecnicasRetencion(tRet);
	}
	
	/**
	 * Post-test.
	 */
	@After
	public void tearDown(){
		if(tc!=null){
			try {
				SQLFacade.getInstance().removeTipo(tc.getNombre());
			} catch (PersistenciaException e) {}
		}
	}
	
	/**
	 * Comprueba que se puede añadir y recuperar el mismo usuario.
	 */
	@Test
	public void testAñadir() {
		try {
			SQLFacade.getInstance().addTipo(tc);
			boolean encontrado=false;
			for(TipoCaso actual : SQLFacade.getInstance().getTipos()){
				if(tc.getNombre().equals(actual.getNombre())){
					encontrado =true;break;
				}
			}
			assertTrue("Añadir tc: No se encuentra el tc añadido", encontrado);
		} catch (PersistenciaException e) {
		}
		
	}
	
	/**
	 * Comprueba que no se pueden añadir atributos con el mismo nombre
	 * @throws PersistenciaException 
	 */
	@Test  (expected=PersistenciaException.class)
	public void testAñadirMismoNombre() throws PersistenciaException {
		SQLFacade.getInstance().addTipo(tc);
		TipoCaso tc2 = new TipoCaso();
		tc2.setNombre(tc.getNombre());
		HashMap<String, Atributo> atbos = new HashMap<String,Atributo>();
		tc2.setAtbos(atbos);
		tc2.setTecnicasRecuperacion(tRec);
		tc2.setTecnicasReutilizacion(tReu);
		tc2.setTecnicasRevision(tRev);
		tc2.setTecnicasRetencion(tRet);
		tc2.setDefaultRec(tRec.get(0));
		tc2.setDefaultReu(tReu.get(0));
		tc2.setDefaultRev(tRev.get(0));
		tc2.setDefaultRet(tRet.get(0));
		SQLFacade.getInstance().addTipo(tc2);
	}

	
	/**
	 * Comprueba que el tipo borrado no se haya en peticiones sucesivas.
	 */
	@Test
	public void testBorrarPersistente(){
		try {
			SQLFacade.getInstance().addTipo(tc);
			boolean encontrado=false;
			for(TipoCaso actual : SQLFacade.getInstance().getTipos()){
				if(tc.getNombre().equals(actual.getNombre())){
					encontrado =true;break;
				}
			}
			assertTrue("Borrar tipo: No se encuentra el tipo añadido", encontrado);
			encontrado = false;
			SQLFacade.getInstance().removeTipo(tc.getNombre());
			for(TipoCaso actual : SQLFacade.getInstance().getTipos()){
				if(tc.getNombre().equals(actual.getNombre())){
					encontrado =true;break;
				}
			}
			assertFalse("Borrar tipo: El tc borrado persiste", encontrado);
		} catch (PersistenciaException e) {}
		
	}
	
	/**
	 * Comprueba que se permite guardan las modificaciones a un atbo.
	 */
	@Test
	public void modificarTiposAtbo(){
		try{
			tc.getAtbos().get("a1").setPeso(2);
			SQLFacade.getInstance().addTipo(tc);
			tc.getAtbos().get("a1").setPeso(3);
			SQLFacade.getInstance().modifyTipo(tc);
			for(TipoCaso actual : SQLFacade.getInstance().getTipos()){
				if(tc.getNombre().equals(actual.getNombre())){
					assertTrue("Modificar tipo: No se alteran los atributos asociados a un tipo"
							,3 == actual.getAtbos().get("a1").getPeso() );
				}
			}
		} catch(PersistenciaException ex) {
		}
	}
}
