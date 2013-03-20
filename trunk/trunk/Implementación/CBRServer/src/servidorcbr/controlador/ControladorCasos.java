package servidorcbr.controlador;

import java.util.HashMap;
import java.util.List;

import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.excepciones.PersistenciaException;
import servidorcbr.persistencia.hbase.HbaseFacade;

public class ControladorCasos {

	public static boolean putCasos (TipoCaso tc, List<HashMap<String,Object>> casos) throws PersistenciaException {
		return HbaseFacade.getInstance().putCasos(tc, casos);
	}
	
}
