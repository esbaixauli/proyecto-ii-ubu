package vista.cicloCBR;

import javax.swing.SwingWorker;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.Usuario;
import vista.componentesGenericos.PanelEstandar;

import controlador.ControlCBR;

/**Swingworker que realiza la consulta CBR. Esta consulta se realiza en un hilo
 * separado por ser muy pesada y bloquear el interfaz si se hiciera en el hilo del GUI.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 * @see javax.swing.SwingWorker
 */
public class WorkerEspera extends SwingWorker<List<HashMap<String,Serializable>>, String> {


	
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
	
	private List<HashMap<String,Serializable>> result;
	/**
	 * Usuario que realiza la petición.
	 */
	private Usuario user;
	
	/** Constructor. Crea un worker para gestionar la espera del retrieval.
	 * @param f Frame al que se vuelve al terminar la espera.
	 * @param tic Tipo de caso del retrieval.
	 * @param q Consulta realizada en el retrieval.
	 */
	public WorkerEspera(PanelEstandar p, TipoCaso tic,HashMap<String,Serializable> q, Usuario u) {
		tc=tic;
		query=q;
		panel = p;
		user = u;
	}
	
	@Override
	protected List<HashMap<String, Serializable>> doInBackground() throws Exception {
		result = ControlCBR.retrieve(tc, query);
		return result;
	}
	
	@Override
	public void done(){
		panel.getPadre().addPanel(new ResultadosConsultaPanel(panel.getPadre(), result,
				ResultadosConsultaPanel.RETRIEVE, tc, query, user, null));
		panel.getPadre().removePanel(panel.getName());
	}

}
