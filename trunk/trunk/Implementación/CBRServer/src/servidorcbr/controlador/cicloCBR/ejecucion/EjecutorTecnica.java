package servidorcbr.controlador.cicloCBR.ejecucion;

import java.util.Collection;

import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRQuery;

public interface EjecutorTecnica {

	public void ejecutar(Collection<CBRCase> casos, CBRQuery query) throws ClassNotFoundException;
	
}
