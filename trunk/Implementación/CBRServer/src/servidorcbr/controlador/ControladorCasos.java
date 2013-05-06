package servidorcbr.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.excepciones.PersistenciaException;
import servidorcbr.persistencia.hbase.HbaseFacade;

public class ControladorCasos {

	public static boolean putCasos (TipoCaso tc, List<HashMap<String,Serializable>> casos) throws PersistenciaException {
		return HbaseFacade.getInstance().putCasos(tc, casos);
	}

	public static boolean retain(TipoCaso tc, HashMap<String, Serializable> caso) throws PersistenciaException {
		List<HashMap<String, Serializable>> casos = new ArrayList<HashMap<String, Serializable>>(1);
		casos.add(caso);
		return HbaseFacade.getInstance().putCasos(tc, casos);
	}
	
}
