package servidorcbr.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.excepciones.PersistenciaException;
import servidorcbr.persistencia.hbase.HbaseFacade;

/**
 * Clase estática de controlador para gestión de casos: insertar casos y etapa de retención. 
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
public class ControladorCasos {

	/**
	 * Inserta una lista de casos en Hbase.
	 * @param tc El tipo de caso del que son instancia los casos.
	 * @param casos Lista de casos a insertar.
	 * @return Si hay éxito o no.
	 * @throws PersistenciaException Si se produce un error en la operación.
	 */
	public static boolean putCasos (TipoCaso tc, List<HashMap<String,Serializable>> casos) throws PersistenciaException {
		return HbaseFacade.getInstance().putCasos(tc, casos);
	}

	/**
	 * Etapa de retención del Ciclo CBR. Simplemente, inserta el caso nuevo en la Base de Casos.
	 * @param tc El tipo de caso del que es instancia el caso a retener.
	 * @param caso El caso a insertar en la Base de Casos.
	 * @return Si hay éxito o no.
	 * @throws PersistenciaException Si se produce un error en la operación.
	 */
	public static boolean retain(TipoCaso tc, HashMap<String, Serializable> caso) throws PersistenciaException {
		List<HashMap<String, Serializable>> casos = new ArrayList<HashMap<String, Serializable>>(1);
		casos.add(caso);
		return HbaseFacade.getInstance().putCasos(tc, casos);
	}
	
}
