package servidorcbr.controlador;

import servidorcbr.modelo.Estadistica;
import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.Usuario;
import servidorcbr.modelo.excepciones.PersistenciaException;
import servidorcbr.persistencia.sql.SQLFacade;

public class ControladorEstadisticas {

	
	public static Estadistica getEstadistica(Usuario u, TipoCaso tc) throws PersistenciaException{
		return SQLFacade.getInstance().getEstadistica(u,tc);
	}
	
	public static void limpiarEstadistica(Usuario u, TipoCaso tc) throws PersistenciaException{
		SQLFacade.getInstance().limpiarEstadistica(u,tc);
	}
}
