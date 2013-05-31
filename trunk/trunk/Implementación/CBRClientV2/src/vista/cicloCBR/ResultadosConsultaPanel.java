package vista.cicloCBR;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serializable;
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
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.Usuario;
import vista.MainFrame;
import vista.componentesGenericos.PanelEstandar;
import vista.panels.PanelIntroducirValorAtbo;
import vista.panels.PanelMostrarAtbo;
import controlador.ControlCBR;

/**Panel de resultados de una etapa del ciclo CBR. Genérico para
 * cualquier etapa que lo requiera. Muestra los casos tratados en esa
 * etapa y permite navegar por ellos.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
@SuppressWarnings("serial")
public class ResultadosConsultaPanel extends PanelEstandar {
	/**
	 * Referencia a los paneles para moverse por ellos.
	 */
	private ResultadosConsultaPanel me, previo, siguiente;
	/**
	 * Spinner para elegir ir a un tipo de caso directamente.
	 */
	private JSpinner spinner;
	/**
	 * Casos a mostrar.
	 */
	private List<HashMap<String, Serializable>> casos;
	/**
	 * Panel de atributos del problema.
	 */
	private JPanel panelProblema;
	/**
	 * Panel de atributos de la solución.
	 */
	private JPanel panelSol;
	/**
	 * Etapa del ciclo.
	 */
	private int etapa;
	/**
	 * Tipo de caso de la consulta CBR.
	 */
	private TipoCaso tc;
	/**
	 * Consulta realizada por el usuario, en formato mapa con clave-valor
	 * de tipo {"nombre de atributo",valor}
	 */
	private HashMap<String,Serializable> query;
	/**
	 * Usuario que ejecutó la consulta.
	 */
	private Usuario user;
	
	/**
	 * Constante para mostrar los datos de una etapa del ciclo concreta.
	 */
	public final static int RETRIEVE=0,REVISE=1,REUSE=2;
	/**
	 * Posición en la lista de casos del caso en el que nos encontramos actualmente. Inicialmente 0.
	 */
	@SuppressWarnings("unused") //Necesario si se desea implementar botones de anterior/siguiente.
	private int actual=0;
	
	/** Este frame muestra los resultados de una consulta, ya sea en la etapa de retrieval o en la
	 * revisión.
	 * @param padre El padre de este frame.
	 * @param lcasos Los casos recuperados a mostrar.
	 * @param etapa entero que depende de la etapa en que se usa el panel.
	 * Correspondiente a las constantes de este frame RETRIEVAL,REUSE,REVISE.
	 * @param tc El tipo de caso al que pertenecen los casos.
	 * @param hquery Consulta introducida por el usuario. Mapa clave-valor de entradas 
	 * {"nombre de atributo",valor}.
	 * @param u Usuario que ejecutó la consulta.
	 * @param pprevio Panel de la etapa previa en el ciclo. Puede ser null si es la 
	 * primera etapa.
	 */
	public ResultadosConsultaPanel(final MainFrame padre,
			List<HashMap<String, Serializable>> lcasos,final int etapa, final TipoCaso tc,
			HashMap<String,Serializable> hquery, Usuario u, ResultadosConsultaPanel pprevio) {
		super(padre);me = this;
		setSize(1060, 350); 
		previo = pprevio;
		this.tc=tc;
		this.etapa=etapa;
		query = hquery;
		this.casos=lcasos;
		user = u;
		setLayout(new BorderLayout());
		JLabel lblTc = new JLabel();
		final JButton btnContinuar = new JButton();
		switch(etapa){
			case RETRIEVE:
				setName("CBR-"+b.getString("retrieval")+": "+tc.getNombre());
				lblTc.setText(b.getString("method") + ": " + tc.getDefaultRec().getNombre());
				btnContinuar.setText(b.getString("reuse"));
				break;
			case REUSE:
				setName("CBR-"+b.getString("reuse")+": "+tc.getNombre());
				lblTc.setText(b.getString("method") + ": " + tc.getDefaultReu().getNombre());
				btnContinuar.setText(b.getString("revise"));
				break;
			case REVISE:
				setName("CBR-"+b.getString("revise")+": "+tc.getNombre());
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
		add(panelCambiarCaso, BorderLayout.NORTH);
		panelCambiarCaso.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_4 = new JPanel();
		panelCambiarCaso.add(panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_3 = new JPanel();
		panel_4.add(panel_3, BorderLayout.SOUTH);
		panel_3.setBackground(Color.GRAY);
		panel_3.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
				JButton buttonPrimero = new JButton("|<");
				panel_3.add(buttonPrimero);
				
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
						
								JButton buttonUltimo = new JButton(">|");
								panel_3.add(buttonUltimo);
								
								JPanel panel_5 = new JPanel();
								panel_4.add(panel_5, BorderLayout.NORTH);
								
								panel_5.add(lblTc);
								
								JPanel panel_6 = new JPanel();
								panelCambiarCaso.add(panel_6, BorderLayout.WEST);
								panel_6.setLayout(new BoxLayout(panel_6, BoxLayout.Y_AXIS));
								
								JPanel panel_8 = new JPanel();
								panel_6.add(panel_8);
								
								final JButton btnAnteriorpanel = new JButton(new ImageIcon("res/anterior_48.png"));
								if (previo == null) {
									btnAnteriorpanel.setEnabled(false);
								}
								btnAnteriorpanel.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										me.setVisible(false);
										padre.removePanel(getName());
										padre.addPanel(previo);
										previo.setVisible(true);
									}
								});
								panel_6.add(btnAnteriorpanel);
								
								JPanel panel_9 = new JPanel();
								panel_6.add(panel_9);
								
								JPanel panel_7 = new JPanel();
								panelCambiarCaso.add(panel_7, BorderLayout.EAST);
								panel_7.setLayout(new BoxLayout(panel_7, BoxLayout.Y_AXIS));
								
								JPanel panel_11 = new JPanel();
								panel_7.add(panel_11);
								
								final JButton btnSiguientepanel = new JButton(new ImageIcon("res/siguiente_48.png"));
								btnSiguientepanel.setEnabled(false);
								btnSiguientepanel.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										me.setVisible(false);
										padre.removePanel(getName());
										padre.addPanel(siguiente);
										siguiente.setVisible(false);
									}
								});
								panel_7.add(btnSiguientepanel);
								
								JPanel panel_10 = new JPanel();
								panel_7.add(panel_10);
								buttonUltimo.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										cambiarDeCaso(casos.size()-1);
									}
								});
				buttonPrimero.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						cambiarDeCaso(0);
					}
				});
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.GRAY);
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		add(panel, BorderLayout.SOUTH);
		
		btnContinuar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnContinuar.setEnabled(false);
				btnSiguientepanel.setEnabled(true);
				switch (etapa) {
				case RETRIEVE:
					try {
						casos = ControlCBR.reuse(tc, query, casos);
						siguiente = new ResultadosConsultaPanel(padre, casos, REUSE, tc, query, user, me);
						padre.addPanel(siguiente);
						me.setVisible(false);
						padre.removePanel(getName());
					} catch (IOException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null,	b.getString("connecterror"), 
								"Error",JOptionPane.ERROR_MESSAGE);
					}
					break;
				case REUSE:
					siguiente = new ResultadosConsultaPanel(padre, casos, REVISE, tc, query, user, me);
					padre.addPanel(siguiente);
					me.setVisible(false);
					padre.removePanel(getName());
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
							padre.removePanel(getName());
						} else {
							JFrame fin = new FrameFinCBR(padre, tc, user);
							fin.setVisible(true);
							padre.removePanel(getName());
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
		add(panel_1, BorderLayout.CENTER);
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
			buttonPrimero.setEnabled(false);
			buttonUltimo.setEnabled(false);
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
				PanelIntroducirValorAtbo pa = new PanelIntroducirValorAtbo(a, false);
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
			PanelIntroducirValorAtbo pa = new PanelIntroducirValorAtbo(a, false);
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
	
	/**Establece los casos a mostrar en el panel.
	 * @param c Lista de casos. Cada caso es un mapa de la forma {"nombre de atributo",valor}
	 */
	public void setCasos (List<HashMap<String,Serializable>> c) {
		casos = c;
	}
}
