package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.border.TitledBorder;
import javax.swing.ScrollPaneConstants;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class ResultadosConsultaFrame extends FrameEstandar {
	private JFrame me;
	private JSpinner spinner;
	private List<HashMap<String, Serializable>> casos;
	private JPanel panelProblema;
	private JPanel panelSol;
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
		super(padre);me = this;
		setSize(1060, 350); setLocationRelativeTo(null);
		this.tc=tc;
		this.etapa=etapa;

		this.casos=lcasos;
		getContentPane().setLayout(new BorderLayout());
		switch(etapa){
			case RETRIEVE:setTitle(b.getString("retrieval"));break;
			case REUSE:setTitle(b.getString("reuse"));break;
			case REVISE:setTitle(b.getString("revise"));break;
		}
		int tam=1;
		if(casos!=null){
		 tam =  casos.size();
		}
		
		JPanel panelCambiarCaso = new JPanel();
		panelCambiarCaso.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		getContentPane().add(panelCambiarCaso, BorderLayout.NORTH);
		panelCambiarCaso.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelCambiarCaso.add(panel_3);
		
				JButton buttonAnterior = new JButton("<<");
				panel_3.add(buttonAnterior);
				
				JPanel panel_2 = new JPanel();
				panel_2.setBackground(Color.LIGHT_GRAY);
				panel_3.add(panel_2);
				panel_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
				
				spinner = new JSpinner();
				panel_2.add(spinner);
				spinner.setPreferredSize(new Dimension(35,(int) spinner.getPreferredSize().getHeight()));
				spinner.setModel(new SpinnerNumberModel(1, 1,tam, 1));
				
						
						JLabel lblnumero = new JLabel("/"+Math.max(tam-1,0));
						panel_2.add(lblnumero);
						
						JButton btnGo = new JButton(b.getString("go"));
						panel_2.add(btnGo);
						
								JButton buttonSiguiente = new JButton(">>");
								panel_3.add(buttonSiguiente);
								buttonSiguiente.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										if(actual+1<casos.size()){
											cambiarDeCaso(actual+1);
										}
									}
								});
						btnGo.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								cambiarDeCaso( ((int)spinner.getValue()) -1);
							}
						});
				buttonAnterior.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if(actual-1 >= 0){
							cambiarDeCaso(actual-1);
						}
					}
				});
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		
		JButton btnContinuar = new JButton(b.getString("continue"));
		panel.add(btnContinuar);
		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panel_1.add(scrollPane);
		
		panelProblema = new JPanel();
		scrollPane.setViewportView(panelProblema);
		panelProblema.setBorder(new TitledBorder(null, b.getString("problem"), TitledBorder.CENTER, TitledBorder.TOP, null, Color.RED));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panel_1.add(scrollPane_1);
		
		panelSol = new JPanel();
		scrollPane_1.setViewportView(panelSol);
		panelSol.setBorder(new TitledBorder(null, b.getString("solution"), TitledBorder.CENTER, TitledBorder.TOP, null, Color.BLUE));
		if(casos!=null && (!casos.isEmpty())){
			panelProblema.setLayout(new BoxLayout(panelProblema, BoxLayout.Y_AXIS));
			panelSol.setLayout(new BoxLayout(panelSol, BoxLayout.Y_AXIS));
			cambiarDeCaso(0);
		}else{
			panelProblema.setLayout(new FlowLayout());
			btnContinuar.setEnabled(false);
			btnGo.setEnabled(false);
			buttonAnterior.setEnabled(false);
			buttonSiguiente.setEnabled(false);
			spinner.setEnabled(false);
			panelProblema.add(new JLabel(b.getString("nocases")));
		
		}
	}
	
	/** Auxiliar. Muestra en pantalla el caso seleccionado con los botones anterior-siguiente o con la opción Ir.
	 * @param i El número del caso seleccionado (empezando por el 0).
	 */
	private void cambiarDeCaso(int i){
		panelProblema.removeAll();
		panelSol.removeAll();
		establecerPanelesCaso(i);
		actual = i;
		spinner.setValue(i+1);
		panelProblema.revalidate();
		panelProblema.repaint();
		panelSol.revalidate();
		panelSol.repaint();
	}
	
	/**Auxiliar. Establece los paneles de cada atributo del tipo de caso al cambiar de uno a otro.
	 * @param i El indice del caso al que se cambia.
	 */
	private void establecerPanelesCaso(int i){
		if(etapa == REVISE){
			for(Atributo a: tc.getAtbos().values() ){
				PanelIntroducirValorAtbo pa = new PanelIntroducirValorAtbo(a, false, me);
				pa.setValue(casos.get(i).get(a.getNombre()));
				if(a.getEsProblema()){//Segun sea problema o no va en su panel correspondiente
					panelProblema.add(pa);
				}else{
					panelSol.add(pa);
				}
			}
		}else{
			for(Entry<String, Serializable> atbo: casos.get(i).entrySet()){
				JPanel pa = new PanelMostrarAtbo(atbo.getKey(),atbo.getValue());
				if(tc.getAtbos().get(atbo.getKey()).getEsProblema()){
					panelProblema.add(pa);
				}else{
					panelSol.add(pa);
				}
			}
		}
	}
}
