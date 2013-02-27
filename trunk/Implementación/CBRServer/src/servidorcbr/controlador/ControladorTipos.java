package servidorcbr.controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import servidorcbr.controlador.generadorClases.GeneradorClases;
import servidorcbr.modelo.Atributo;
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
		boolean exito;
		exito = SQLFacade.getInstance().addTipo(tc);
		exito = exito &&
		GeneradorClases.crearClase(creaMapa(tc.getAtbos(),true),tc.getNombre()+"Description") &&
		GeneradorClases.crearClase(creaMapa(tc.getAtbos(),false),tc.getNombre()+"Solution");
		return exito;
		
	}
	
	//Crea un mapa tipo de atributo:lista de atributos de ese tipo
	private static HashMap<String,ArrayList<String>> creaMapa(HashMap<String,Atributo> atbos,
							boolean problema){
		HashMap<String, ArrayList<String>> mapa = new HashMap<String, ArrayList<String>>();
		for(String tipo: GeneradorClases.establecerTipos()){
		mapa.put(tipo,new ArrayList<String>() );
		}
		for(Atributo ac :atbos.values()){
			if(mapa.containsKey(ac.getTipo()) && problema == ac.getEsProblema()){
				mapa.get(ac.getTipo()).add(ac.getNombre());
			}
		}
		return mapa;
	}
	
	public static boolean removeTipo (TipoCaso tc) throws PersistenciaException {
		return SQLFacade.getInstance().removeTipo(tc.getNombre());
	}
	
}
