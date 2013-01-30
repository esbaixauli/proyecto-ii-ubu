package servidorcbr.controlador;

import java.util.List;

import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.Usuario;
import servidorcbr.modelo.excepciones.PersistenciaException;
import servidorcbr.persistencia.sql.SQLFacade;

public class ControladorTipos {
	
	public static List<TipoCaso> getTipos () throws PersistenciaException {
		return SQLFacade.getInstance().getTipos();
	}
	
	public static List<TipoCaso> getTipos (Usuario u) throws PersistenciaException {
		return SQLFacade.getInstance().getTipos(u.getNombre());
	}
	
	public static boolean addTipo (TipoCaso tc) throws PersistenciaException {
		return SQLFacade.getInstance().addTipo(tc);
	}
	
	public static boolean removeTipo (TipoCaso tc) throws PersistenciaException {
		return SQLFacade.getInstance().removeTipo(tc.getNombre());
	}
	
}
