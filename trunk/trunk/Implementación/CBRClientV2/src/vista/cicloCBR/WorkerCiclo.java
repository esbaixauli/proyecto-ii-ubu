package vista.cicloCBR;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.swing.SwingWorker;

import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.Usuario;
import vista.componentesGenericos.PanelEstandar;
import controlador.ControlCBR;

/**
 * Swingworker que realiza el ciclo CBR completo. Esta consulta se realiza en un hilo
 * separado por ser muy pesada y bloquear el interfaz si se hiciera en el hilo del GUI.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 * @see javax.swing.SwingWorker
 */
public class WorkerCiclo extends SwingWorker<List<HashMap<String,Serializable>>, String> {
	
	/**
	 * Panel al que se vuelve al terminar la espera. 
	 */
	private PanelEstandar panel;
	
	/**
	 * Tipo de caso del retrieval.
	 */
	private TipoCaso tc;
	/**
	 * Consulta realizada en el retrieval.
	 */
	private HashMap<String,Serializable> query;
	/**
	 * Resultado del ciclo CBR.
	 */
	private List<HashMap<String,Serializable>> result;
	/**
	 * Usuario que ejecuta el ciclo.
	 */
	private Usuario user;
	
	/** Constructor. Crea un worker para gestionar la espera del ciclo CBR.
	 * @param p Panel al que se vuelve al terminar la espera.
	 * @param tic Tipo de caso del retrieval.
	 * @param q Consulta realizada en el retrieval.
	 * @param u Usuario que ejecuta el ciclo.
	 */
	public WorkerCiclo(PanelEstandar p, TipoCaso tic,HashMap<String,Serializable> q, Usuario u) {
		tc=tic;
		query=q;
		panel = p;
		user = u;
	}
	
	/**
	 * Ejecuta la llamada al servidor en segundo plano.
	 * @throws Exception Si hay un error en la operación.
	 */
	@Override
	protected List<HashMap<String, Serializable>> doInBackground() throws Exception {
		result = ControlCBR.cicloCompleto(tc, query);
		return result;
	}
	
	/**
	 * Se ejecuta desde el EDT cuando la operación en segundo plano termina.
	 * Sustituye el panel de introducir la consulta por el de mostrar el resultado.
	 */
	@Override
	public void done(){
		panel.getPadre().removePanel(panel.getName());
		panel.getPadre().addPanel(new ResultadosConsultaPanel(panel.getPadre(), result,
				ResultadosConsultaPanel.REVISE, tc, query, user, null));
	}

}
