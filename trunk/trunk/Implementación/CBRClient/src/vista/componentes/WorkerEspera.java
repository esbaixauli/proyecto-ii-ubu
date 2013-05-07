package vista.componentes;

import javax.swing.JFrame;
import javax.swing.SwingWorker;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.Usuario;
import vista.ResultadosConsultaFrame;

import controlador.ControlCBR;

public class WorkerEspera extends SwingWorker<List<HashMap<String,Serializable>>, String> {

	/**
	 * Splash que se muestra durante la espera.
	 */
	private Splash splash;
	
	/**
	 * Frame al que se vuelve al terminar la espera. 
	 */
	private FrameEstandar frame;
	
	/**
	 * Tipo de caso del retrieval.
	 */
	private TipoCaso tc;
	/**
	 * Consulta realizada en el retrieval.
	 */
	private HashMap<String,Serializable> query;
	
	private List<HashMap<String,Serializable>> result;
	private Usuario user;
	
	/** Constructor. Crea un worker para gestionar la espera del retrieval.
	 * @param f Frame al que se vuelve al terminar la espera.
	 * @param tic Tipo de caso del retrieval.
	 * @param q Consulta realizada en el retrieval.
	 */
	public WorkerEspera(FrameEstandar f, TipoCaso tic,HashMap<String,Serializable> q, Usuario u) {
		tc=tic;
		query=q;
		frame = f;
		user = u;
	}
	
	@Override
	protected List<HashMap<String, Serializable>> doInBackground() throws Exception {
		result = ControlCBR.retrieve(tc, query);
		return result;
	}
	
	@Override
	public void done(){
		JFrame res = new ResultadosConsultaFrame(frame.getPadre(), result,
				ResultadosConsultaFrame.RETRIEVE, tc, query, user);
		res.setVisible(true);
		frame.setVisible(false);
		frame.dispose();
	}

}
