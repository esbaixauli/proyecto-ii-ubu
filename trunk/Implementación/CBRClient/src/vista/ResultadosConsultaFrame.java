package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EtchedBorder;

import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.Usuario;
import vista.componentes.FrameEstandar;
import vista.panels.PanelIntroducirValorAtbo;
import vista.panels.PanelMostrarAtbo;
import javax.swing.border.TitledBorder;
import javax.swing.ScrollPaneConstants;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

import controlador.ControlCBR;
import controlador.ControlCasos;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

@SuppressWarnings("serial")
public class ResultadosConsultaFrame extends FrameEstandar {
	private JFrame me;
	private JSpinner spinner;
	private List<HashMap<String, Serializable>> casos;
	private JPanel panelProblema;
	private JPanel panelSol;
	private int etapa;
	private TipoCaso tc;
	private HashMap<String,Serializable> query;
	private Usuario user;
	
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
	public ResultadosConsultaFrame(final JFrame padre,
			List<HashMap<String, Serializable>> lcasos,final int etapa, final TipoCaso tc,
			HashMap<String,Serializable> hquery, Usuario u) {
		super(padre);me = this;
		setSize(1060, 350); setLocationRelativeTo(null);
		this.tc=tc;
		this.etapa=etapa;
		query = hquery;
		this.casos=lcasos;
		user = u;
		getContentPane().setLayout(new BorderLayout());
		JLabel lblTc = new JLabel();
		JButton btnContinuar = new JButton();
		switch(etapa){
			case RETRIEVE:
				setTitle(b.getString("retrieval"));
				lblTc.setText(b.getString("method") + ": " + tc.getDefaultRec().getNombre());
				btnContinuar.setText(b.getString("reuse"));
				break;
			case REUSE:
				setTitle(b.getString("reuse"));
				lblTc.setText(b.getString("method") + ": " + tc.getDefaultReu().getNombre());
				btnContinuar.setText(b.getString("revise"));
				break;
			case REVISE:
				setTitle(b.getString("revise"));
				lblTc.setText(b.getString("method") + ": " + tc.getDefaultRev().getNombre());
				btnContinuar.setText(b.getString("retain"));
				break;
		}
		int tam=1;
		if(casos!=null){
		 tam =  casos.size();
		}
		
		JPanel panelCambiarCaso = new JPanel();
		panelCambiarCaso.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		getContentPane().add(panelCambiarCaso, BorderLayout.NORTH);
		panelCambiarCaso.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JPanel panel_4 = new JPanel();
		panelCambiarCaso.add(panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_3 = new JPanel();
		panel_4.add(panel_3, BorderLayout.SOUTH);
		panel_3.setBackground(Color.GRAY);
		panel_3.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
				JButton buttonAnterior = new JButton("<<");
				panel_3.add(buttonAnterior);
				
				JPanel panel_2 = new JPanel();
				panel_2.setBackground(Color.LIGHT_GRAY);
				panel_3.add(panel_2);
				panel_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
				
				spinner = new JSpinner();
				spinner.addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent e) {
						cambiarDeCaso( ((int)spinner.getValue()) -1);
					}
				});
				panel_2.add(spinner);
				spinner.setPreferredSize(new Dimension(35,(int) spinner.getPreferredSize().getHeight()));
				spinner.setModel(new SpinnerNumberModel(1, 1,Math.max(tam, 1), 1));
				
						
						JLabel lblnumero = new JLabel("/"+Math.max(tam,0));
						panel_2.add(lblnumero);
						
								JButton buttonSiguiente = new JButton(">>");
								panel_3.add(buttonSiguiente);
								
								JPanel panel_5 = new JPanel();
								panel_4.add(panel_5, BorderLayout.NORTH);
								
								panel_5.add(lblTc);
								buttonSiguiente.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										if(actual+1<casos.size()){
											cambiarDeCaso(actual+1);
										}
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
		panel.setBackground(Color.GRAY);
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		getContentPane().add(panel, BorderLayout.SOUTH);
		
		btnContinuar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch (etapa) {
				case RETRIEVE:
					try {
						casos = ControlCBR.reuse(tc, query, casos);
						JFrame reuse = new ResultadosConsultaFrame(padre, casos, REUSE, tc, query, user);
						reuse.setVisible(true);
						me.setVisible(false);
						me.dispose();
					} catch (IOException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null,	b.getString("connecterror"), 
								"Error",JOptionPane.ERROR_MESSAGE);
					}
					break;
				case REUSE:
					JFrame revise = new ResultadosConsultaFrame(padre, casos, REVISE, tc, query, user);
					revise.setVisible(true);
					me.setVisible(false);
					me.dispose();
					break;
				case REVISE:
					try {
						HashMap<String,Serializable> caso = new HashMap<String,Serializable>();
						for (Component c : panelProblema.getComponents()) {
							PanelIntroducirValorAtbo p = (PanelIntroducirValorAtbo) c;
							caso.put(p.getKey(), p.getValue());
						}
						for (Component c : panelSol.getComponents()) {
							PanelIntroducirValorAtbo p = (PanelIntroducirValorAtbo) c;
							caso.put(p.getKey(), p.getValue());
						}
						if ((Integer) caso.get("META_QUALITY") > 100
								|| (Integer) caso.get("META_QUALITY") < 0) {
							JOptionPane.showMessageDialog(null,	b.getString("emptyfield"), 
									b.getString("emptyfield"),JOptionPane.ERROR_MESSAGE);
						}
						if (ControlCBR.retain(tc, caso, user)) {
							JFrame fin = new FrameFinCBR(padre, tc, user);
							fin.setVisible(true);
							me.setVisible(false);
							me.dispose();
						} else {
							JOptionPane.showMessageDialog(null,	b.getString("opunsuccess"), 
									b.getString("opunsuccess"),JOptionPane.ERROR_MESSAGE);
						}
					} catch (IOException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null,	b.getString("connecterror"), 
								"Error",JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
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
			buttonAnterior.setEnabled(false);
			buttonSiguiente.setEnabled(false);
			spinner.setEnabled(false);
			panelProblema.add(new JLabel(b.getString("nocases")));
		
		}
		setLocationRelativeTo(padre);
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
			Atributo a = new Atributo();
			a.setNombre("META_QUALITY");
			a.setTipo("I");
			a.setMetrica("equal");
			PanelIntroducirValorAtbo pa = new PanelIntroducirValorAtbo(a, false, me);
			pa.setValue(50);
			panelSol.add(pa);
		}else{
			for(Entry<String, Serializable> par: casos.get(i).entrySet()){
				JPanel pa = new PanelMostrarAtbo(par.getKey(),par.getValue());
				if (!par.getKey().equals("META_QUALITY")
						&& !par.getKey().equals("META_ID")) {
					if(tc.getAtbos().get(par.getKey()).getEsProblema()){
						panelProblema.add(pa);
					}else{
						panelSol.add(pa);
					}
				}
			}
			JPanel pa = new PanelMostrarAtbo("META_QUALITY", casos.get(i).get("META_QUALITY"));
			panelSol.add(pa);
		}
	}
	
	public void setCasos (List<HashMap<String,Serializable>> c) {
		casos = c;
	}
}
