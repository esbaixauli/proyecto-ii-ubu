package test.persistencia.sql;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.excepciones.PersistenciaException;
import servidorcbr.persistencia.sql.SQLFacade;

public class TestSQLTipos {
	private TipoCaso tc;
	/**
	 * Pre-test.
	 */
	@Before
	public void setUp(){
		tc = new TipoCaso();
		tc.setNombre("test");
		HashMap<String, Atributo> atbos = new HashMap<String,Atributo>();
		Atributo a1 = new Atributo(); a1.setNombre("a1"); a1.setTipo("S");
		Atributo a2 = new Atributo(); a1.setNombre("a2"); a1.setTipo("S");
		atbos.put("a1", a1);
		atbos.put("a2", a2);
		tc.setAtbos(atbos);
		
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
		SQLFacade.getInstance().addTipo(tc2);
	}

	
	/**
	 * Comprueba que el tipo borrado no se haya en peticiones sucesivas.
	 */
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
	}catch(PersistenciaException ex){}	}
}
