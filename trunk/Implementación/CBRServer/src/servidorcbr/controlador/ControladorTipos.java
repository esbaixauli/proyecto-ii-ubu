package servidorcbr.controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import servidorcbr.controlador.generadorClases.GeneradorClases;
import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.Usuario;
import servidorcbr.modelo.excepciones.PersistenciaException;
import servidorcbr.persistencia.hbase.HbaseFacade;
import servidorcbr.persistencia.sql.SQLFacade;

/**
 * Clas estática de la capa de controlador para la gestión de tipos de caso.
 * Permite añadir, borrar, recuperar o modificar tipos de caso.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
public class ControladorTipos {

	/**
	 * Recupera todos los tipos de caso almacenados en el sistema.
	 * @return La lista de tipos de caso.
	 * @throws PersistenciaException Si se produce algún error en la operación.
	 */
	public static List<TipoCaso> getTipos() throws PersistenciaException {
		return SQLFacade.getInstance().getTipos();
	}

	/**
	 * Recupera todos los tipos de caso a los que está asignado un determinado usuario.
	 * @param u El usuario en cuestión.
	 * @return La lista de tipos de caso.
	 * @throws PersistenciaException Si se produce algún error en la operación.
	 */
	public static List<TipoCaso> getTipos(Usuario u)
			throws PersistenciaException {
		return SQLFacade.getInstance().getTipos(u.getNombre());
	}

	/**
	 * Añade un nuevo tipo de caso al sistema.
	 * @param tc El tipo de caso a añadir.
	 * @return Si la operación tuvo éxito o no.
	 * @throws PersistenciaException Si hubo algún error en la operación.
	 */
	public static boolean addTipo(TipoCaso tc) throws PersistenciaException {
		boolean exito;
		exito = SQLFacade.getInstance().addTipo(tc);
		exito = exito
				&& HbaseFacade.getInstance().createTable(tc)
				&& GeneradorClases.crearClase(creaMapa(tc.getAtbos(), true),
						tc.getNombre() + "Description")
				&& GeneradorClases.crearClase(creaMapa(tc.getAtbos(), false),
						tc.getNombre() + "Solution");
		return exito;
	}

	/**
	 * Crea un HashMap "tipo de atributo":"lista de atributos de ese tipo".
	 * @param atbos El HashMap que contiene los atributos de un tipo de caso.
	 * @param problema Si se quiere un mapa con los atributos son del problema (true) o de la solución (false).
	 * @return El HashMap creado.
	 */
	private static HashMap<String, ArrayList<String>> creaMapa(
			HashMap<String, Atributo> atbos, boolean problema) {
		HashMap<String, ArrayList<String>> mapa = new HashMap<String, ArrayList<String>>();
		for (String tipo : GeneradorClases.establecerTipos()) {
			mapa.put(tipo, new ArrayList<String>());
		}
		for (Atributo ac : atbos.values()) {
			if (mapa.containsKey(ac.getTipo())
					&& problema == ac.getEsProblema()) {
				mapa.get(ac.getTipo()).add(ac.getNombre());
			}
		}
		return mapa;
	}

	/**
	 * Elimina un tipo de caso del sistema.
	 * @param tc El tipo de caso a eliminar.
	 * @return Si la operación tuvo éxito o no.
	 * @throws PersistenciaException Si hubo algún error en la operación.
	 */
	public static boolean removeTipo(TipoCaso tc) throws PersistenciaException {
		return SQLFacade.getInstance().removeTipo(tc.getNombre())
				&& HbaseFacade.getInstance().dropTable(tc);
	}

	/**
	 * Modifica un tipo de caso del sistema.
	 * @param tc El tipo de caso modificado.
	 * @return Si la operación tuvo éxito o no.
	 * @throws PersistenciaException Si hubo algún error en la operación.
	 */
	public static boolean modifyTipo(TipoCaso tc) throws PersistenciaException {
		return SQLFacade.getInstance().modifyTipo(tc);
	}

}
