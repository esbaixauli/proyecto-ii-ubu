package vista.componentes;

import javax.swing.JFrame;
import javax.swing.SwingWorker;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import servidorcbr.modelo.TipoCaso;

import controlador.ControlCBR;

public class WorkerEspera<T,V> extends SwingWorker<List<HashMap<String,Serializable>>, String> {

	/**
	 * Splash que se muestra durante la espera.
	 */
	private Splash splash;
	
	/**
	 * Frame al que se vuelve al terminar la espera. 
	 */
	private JFrame frame;
	
	/**
	 * Tipo de caso del retrieval.
	 */
	private TipoCaso tc;
	/**
	 * Consulta realizada en el retrieval.
	 */
	private HashMap<String,Serializable> query;
	
	/** Constructor. Crea un worker para gestionar la espera del retrieval.
	 * @param f Frame al que se vuelve al terminar la espera.
	 * @param tic Tipo de caso del retrieval.
	 * @param q Consulta realizada en el retrieval.
	 */
	public WorkerEspera(JFrame f, TipoCaso tic,HashMap<String,Serializable> q) {
		frame=f;
		tc=tic;
		query=q;
	}
	
	@Override
	protected List<HashMap<String, Serializable>> doInBackground() throws Exception {
		splash = new Splash();
		splash.setLocationRelativeTo(frame);
		splash.setVisible(true);
		return ControlCBR.retrieve(tc, query);
	}
	
	@Override
	public void done(){
		splash.setVisible(false);
		splash.dispose();
		frame.setEnabled(true);
		frame.toFront();
	}

}
