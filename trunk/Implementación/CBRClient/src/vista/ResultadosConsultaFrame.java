package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EtchedBorder;

import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.TipoCaso;
import vista.componentes.FrameEstandar;
import vista.panels.PanelIntroducirValorAtbo;
import vista.panels.PanelMostrarAtbo;

@SuppressWarnings("serial")
public class ResultadosConsultaFrame extends FrameEstandar {
	private JFrame me;
	private JSpinner spinner;
	private List<HashMap<String, Serializable>> casos;
	private JPanel panelResultados;
	private int etapa;
	private TipoCaso tc;
	
	public final static int RETRIEVE=0,REVISE=1,REUSE=2;
	/**
	 * Posición en la lista de casos del caso en el que nos encontramos actualmente. Inicialmente 0.
	 */
	private int actual=0;
	
	/** Este frame muestra los resultados de una consulta, ya sea en la etapa de retrieval o en la
	 * revisión.
	 * @param padre El padre de este frame.
	 * @param lcasos Los casos recuperados a mostrar.
	 * @param etapa entero que depende de la etapa en que se usa el panel.
	 * Correspondiente a las constantes de este frame RETRIEVAL,REUSE,REVISE.
	 * @param tipo El tipo de caso al que pertenecen los casos.
	 */
	public ResultadosConsultaFrame(JFrame padre,
			List<HashMap<String, Serializable>> lcasos,int etapa, TipoCaso tc) {
		super(padre);
		this.tc=tc;
		this.etapa=etapa;
		me = this;
		this.casos=lcasos;
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		switch(etapa){
			case RETRIEVE:setTitle(b.getString("retrieval"));break;
			case REUSE:setTitle(b.getString("reuse"));break;
			case REVISE:setTitle(b.getString("revise"));break;
		}

		JPanel panelCambiarCaso = new JPanel();
		panelCambiarCaso.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		getContentPane().add(panelCambiarCaso, BorderLayout.NORTH);
		panelCambiarCaso.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JButton buttonAnterior = new JButton("<<");
		buttonAnterior.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(actual-1 >= 0){
					cambiarDeCaso(actual-1);
				}
			}
		});
		panelCambiarCaso.add(buttonAnterior);
		
		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(1, 1, casos.size(), 1));
		
		panelCambiarCaso.add(spinner);

		
		JLabel lblnumero = new JLabel("/"+casos.size());
		panelCambiarCaso.add(lblnumero);
		
		JButton btnGo = new JButton(b.getString("go"));
		btnGo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cambiarDeCaso( ((int)spinner.getValue()) -1);
			}
		});
		panelCambiarCaso.add(btnGo);

		JButton buttonSiguiente = new JButton(">>");
		buttonSiguiente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(actual+1<casos.size()){
					cambiarDeCaso(actual+1);
				}
			}
		});
		panelCambiarCaso.add(buttonSiguiente);
		
		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		panelResultados = new JPanel();
		scrollPane.setViewportView(panelResultados);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		
		JButton btnContinuar = new JButton(b.getString("continue"));
		panel.add(btnContinuar);
		if(casos!=null && (!casos.isEmpty())){
			panelResultados.setLayout(new BoxLayout(panelResultados, BoxLayout.Y_AXIS));
			cambiarDeCaso(0);
		}else{
			panelResultados.setLayout(new BorderLayout());
			panelCambiarCaso.setEnabled(false);
			panelResultados.add(new JLabel(b.getString("nocases")));
		
		}
	}
	
	/** Auxiliar. Muestra en pantalla el caso seleccionado con los botones anterior-siguiente o con la opción Ir.
	 * @param i El número del caso seleccionado (empezando por el 0).
	 */
	private void cambiarDeCaso(int i){
		panelResultados.removeAll();
		establecerPanelesCaso(i);
		actual = i;
		spinner.setValue(i);
		panelResultados.revalidate();
		panelResultados.repaint();
	}
	
	/**Auxiliar. Establece los paneles de cada atributo del tipo de caso al cambiar de uno a otro.
	 * @param i El indice del caso al que se cambia.
	 */
	private void establecerPanelesCaso(int i){
		if(etapa == REVISE){
			for(Atributo a: tc.getAtbos().values() ){
				PanelIntroducirValorAtbo pa = new PanelIntroducirValorAtbo(a, false, me);
				
				pa.setValue(casos.get(i).get(a.getNombre()));
			}
		}else{
			for(Entry<String, Serializable> atbo: casos.get(i).entrySet()){
				panelResultados.add(new PanelMostrarAtbo(atbo.getKey(),atbo.getValue()));
			}
		}
	}
}
