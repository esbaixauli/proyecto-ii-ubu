package vista;

import controlador.ControlCBR;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.Estadistica;
import servidorcbr.modelo.Tecnica;
import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.TipoUsuario;
import servidorcbr.modelo.Usuario;
import vista.componentes.FrameEstandar;
import vista.componentes.Splash;
import vista.componentes.WorkerEspera;
import vista.configtecnicas.CombineQueryConfigFrame;
import vista.configtecnicas.DiverseByMedianConfigFrame;
import vista.configtecnicas.FilterBasedConfigFrame;
import vista.configtecnicas.NNConfigFrame;
import vista.configtecnicas.NumOrCopyConfigFrame;
import vista.panels.PanelIntroducirValorAtbo;
import controlador.ControlEstadisticas;
import controlador.ControlTecnicas;
import javax.swing.border.EtchedBorder;
import java.awt.Font;

@SuppressWarnings("serial")
public class IntroducirConsultaCBRFrame extends FrameEstandar {

	private JPanel contentPane;

	private JFrame cRec, cReu, cRev, cRet;


	private TipoCaso tc;
	private boolean configurado;
	private JComboBox<String> comboBoxRec;
	private JButton btnConfigRec;
	private JComboBox<String> comboBoxReu;
	private JButton btnConfigReu;
	private JButton btnConfigRev;
	private JButton btnConfigRet;
	private JComboBox<String> comboBoxRev;
	private JComboBox<String> comboBoxRet;
	private JLabel lblRec;
	private JLabel lblReu;
	private JLabel lblRev;
	private JLabel lblRet;
	private JButton btnEjecutarCiclo;
	private JPanel panelAtbos;
	private JScrollPane scrollPane;
	private JPanel panelMet;
	private JPanel panelEjec;
	private JPanel panel;
	private JLabel lblEjecTotales;
	private JPanel panel_1;
	private JLabel lblMediaCalidad;
	private JButton btnPasoapaso;
	private JPanel panel_2;
	private JPanel panel_3;
	private JPanel panel_4;
	private JPanel panel_5;
	private Usuario user;

	/**
	 * Create the frame.
	 */
	public IntroducirConsultaCBRFrame(TipoCaso tic, boolean configurado,
			final JFrame padre, Usuario u) {
		super(padre);me=this;
		this.tc = tic;
		this.configurado = configurado;
		user = u;
		setTitle("CBR: " + tc.getNombre());
		setBounds(100, 100, 573, 525);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(2, 2));

		scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		contentPane.add(scrollPane,BorderLayout.NORTH);

		panelAtbos = new JPanel();
		scrollPane.setViewportView(panelAtbos);
		panelAtbos.setLayout(new BoxLayout(panelAtbos, BoxLayout.Y_AXIS));
		//El panel de tecnicas solo existe en un ciclo configurado
		if (configurado) {
			panelMet = new JPanel();
			panelMet.setBorder(new TitledBorder(UIManager
					.getBorder("TitledBorder.border"), b
					.getString("managemethods"), TitledBorder.CENTER,
					TitledBorder.TOP, null, Color.BLUE));
			contentPane.add(panelMet,BorderLayout.CENTER);
			GridBagLayout gbl_panelMet = new GridBagLayout();
			gbl_panelMet.columnWidths = new int[] { 101, 0, 145, 35, 87, 72, 0, 0 };
			gbl_panelMet.rowHeights = new int[] { 15, 14, 23, 14, 23, 14, 23, 14,
					19, 92, 0 };
			gbl_panelMet.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0,
					0.0, 0.0, Double.MIN_VALUE };
			gbl_panelMet.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
					0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
			panelMet.setLayout(gbl_panelMet);
			
			panel_2 = new JPanel();
			panel_2.setBackground(Color.GRAY);
			panel_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			GridBagConstraints gbc_panel_2 = new GridBagConstraints();
			gbc_panel_2.gridwidth = 5;
			gbc_panel_2.insets = new Insets(0, 0, 5, 5);
			gbc_panel_2.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel_2.gridx = 1;
			gbc_panel_2.gridy = 1;
			panelMet.add(panel_2, gbc_panel_2);
			
						lblRec = new JLabel(b.getString("retrieval"));
						lblRec.setForeground(Color.WHITE);
						lblRec.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 11));
						panel_2.add(lblRec);

			comboBoxRec = new JComboBox<String>();
			GridBagConstraints gbc_comboBoxRec = new GridBagConstraints();
			gbc_comboBoxRec.gridwidth = 4;
			gbc_comboBoxRec.fill = GridBagConstraints.HORIZONTAL;
			gbc_comboBoxRec.insets = new Insets(0, 0, 5, 5);
			gbc_comboBoxRec.gridx = 1;
			gbc_comboBoxRec.gridy = 2;
			panelMet.add(comboBoxRec, gbc_comboBoxRec);
			
			
						btnConfigRec = new JButton(b.getString("configure"));
						GridBagConstraints gbc_btnConfigRec = new GridBagConstraints();
						gbc_btnConfigRec.insets = new Insets(0, 0, 5, 5);
						gbc_btnConfigRec.gridx = 5;
						gbc_btnConfigRec.gridy = 2;
						panelMet.add(btnConfigRec, gbc_btnConfigRec);
						btnConfigRec.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								cRec.setVisible(true);
								me.setEnabled(false);
							}
						});
			
			panel_3 = new JPanel();
			panel_3.setBackground(Color.GRAY);
			panel_3.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			GridBagConstraints gbc_panel_3 = new GridBagConstraints();
			gbc_panel_3.gridwidth = 5;
			gbc_panel_3.insets = new Insets(0, 0, 5, 5);
			gbc_panel_3.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel_3.gridx = 1;
			gbc_panel_3.gridy = 3;
			panelMet.add(panel_3, gbc_panel_3);
			
						lblReu = new JLabel(b.getString("reuse"));
						lblReu.setForeground(Color.WHITE);
						lblReu.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 11));
						panel_3.add(lblReu);

			comboBoxReu = new JComboBox<String>();
			GridBagConstraints gbc_comboBoxReu = new GridBagConstraints();
			gbc_comboBoxReu.gridwidth = 4;
			gbc_comboBoxReu.fill = GridBagConstraints.HORIZONTAL;
			gbc_comboBoxReu.insets = new Insets(0, 0, 5, 5);
			gbc_comboBoxReu.gridx = 1;
			gbc_comboBoxReu.gridy = 4;
			panelMet.add(comboBoxReu, gbc_comboBoxReu);
		
			
						btnConfigReu = new JButton(b.getString("configure"));
						GridBagConstraints gbc_btnConfigReu = new GridBagConstraints();
						gbc_btnConfigReu.insets = new Insets(0, 0, 5, 5);
						gbc_btnConfigReu.gridx = 5;
						gbc_btnConfigReu.gridy = 4;
						panelMet.add(btnConfigReu, gbc_btnConfigReu);
						btnConfigReu.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								cReu.setVisible(true);
								me.setEnabled(false);
							}
						});
			
			panel_4 = new JPanel();
			panel_4.setBackground(Color.GRAY);
			panel_4.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			GridBagConstraints gbc_panel_4 = new GridBagConstraints();
			gbc_panel_4.gridwidth = 5;
			gbc_panel_4.insets = new Insets(0, 0, 5, 5);
			gbc_panel_4.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel_4.gridx = 1;
			gbc_panel_4.gridy = 5;
			panelMet.add(panel_4, gbc_panel_4);
			
						lblRev = new JLabel(b.getString("revise"));
						lblRev.setForeground(Color.WHITE);
						lblRev.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 11));
						panel_4.add(lblRev);

			comboBoxRev = new JComboBox<String>();
			GridBagConstraints gbc_comboBoxRev = new GridBagConstraints();
			gbc_comboBoxRev.gridwidth = 4;
			gbc_comboBoxRev.fill = GridBagConstraints.HORIZONTAL;
			gbc_comboBoxRev.insets = new Insets(0, 0, 5, 5);
			gbc_comboBoxRev.gridx = 1;
			gbc_comboBoxRev.gridy = 6;
			panelMet.add(comboBoxRev, gbc_comboBoxRev);
			
			
						btnConfigRev = new JButton(b.getString("configure"));
						GridBagConstraints gbc_btnConfigRev = new GridBagConstraints();
						gbc_btnConfigRev.insets = new Insets(0, 0, 5, 5);
						gbc_btnConfigRev.gridx = 5;
						gbc_btnConfigRev.gridy = 6;
						panelMet.add(btnConfigRev, gbc_btnConfigRev);
						btnConfigRev.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								cRev.setVisible(true);
								me.setEnabled(false);
							}
						});
			
			panel_5 = new JPanel();
			panel_5.setBackground(Color.GRAY);
			panel_5.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			GridBagConstraints gbc_panel_5 = new GridBagConstraints();
			gbc_panel_5.gridwidth = 5;
			gbc_panel_5.insets = new Insets(0, 0, 5, 5);
			gbc_panel_5.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel_5.gridx = 1;
			gbc_panel_5.gridy = 7;
			panelMet.add(panel_5, gbc_panel_5);
			
						lblRet = new JLabel(b.getString("retain"));
						lblRet.setForeground(Color.WHITE);
						lblRet.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 11));
						panel_5.add(lblRet);

			comboBoxRet = new JComboBox<String>();
			GridBagConstraints gbc_comboBoxRet = new GridBagConstraints();
			gbc_comboBoxRet.gridwidth = 4;
			gbc_comboBoxRet.fill = GridBagConstraints.HORIZONTAL;
			gbc_comboBoxRet.insets = new Insets(0, 0, 5, 5);
			gbc_comboBoxRet.gridx = 1;
			gbc_comboBoxRet.gridy = 8;
			panelMet.add(comboBoxRet, gbc_comboBoxRet);
			
			
						btnConfigRet = new JButton(b.getString("configure"));
						GridBagConstraints gbc_btnConfigRet = new GridBagConstraints();
						gbc_btnConfigRet.insets = new Insets(0, 0, 5, 5);
						gbc_btnConfigRet.gridx = 5;
						gbc_btnConfigRet.gridy = 8;
						panelMet.add(btnConfigRet, gbc_btnConfigRet);
			btnConfigRet.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					cRet.setVisible(true);
					me.setEnabled(false);
				}
			});
	
		}
		
		panelEjec = new JPanel();
		panelEjec.setLayout(new BoxLayout(panelEjec, BoxLayout.Y_AXIS));
		
		panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"),  b.getString("quickstats"), TitledBorder.CENTER, TitledBorder.TOP, null, Color.BLUE));
		panelEjec.add(panel_1);
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		lblEjecTotales = new JLabel(b.getString("totalexec")+":");
		panel_1.add(lblEjecTotales);
		
		lblMediaCalidad = new JLabel(b.getString("avgquality")+":");
		panel_1.add(lblMediaCalidad);
		
		panel = new JPanel();
		panel.setBackground(Color.GRAY);
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelEjec.add(panel);
		btnEjecutarCiclo = new JButton(b.getString("executeCBR"));
		panel.add(btnEjecutarCiclo);
		
		btnPasoapaso = new JButton(b.getString("stepbystep"));
		btnPasoapaso.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HashMap<String,Serializable> query = new HashMap<String,Serializable>();
				for (Component c : panelAtbos.getComponents()) {
					PanelIntroducirValorAtbo p = (PanelIntroducirValorAtbo) c;
					query.put(p.getKey(), p.getValue());
					Atributo a =tc.getAtbos().get(p.getKey());
					a.setMetrica(p.getMetrica());
					a.setParamMetrica(p.getParamMetrica());
					a.setPeso(p.getPeso());
				}
			
				try {
					//Momentaneamente el l&f se vuelve el estándar para mostrar el splash.
					cambiarLookAndFeel(false);
					//Creo un splash (Imagen de espera)
					Splash s = new Splash();
					
					WorkerEspera<List<HashMap<String,Serializable>>,String> we = 
					new WorkerEspera<List<HashMap<String,Serializable>>,String>
					(s, me, tc, query);
					
					s.setLocationRelativeTo(me);
					s.setVisible(true);
					me.setEnabled(false);	
					//Los controles quedan deshabilitados hasta que se haya ejecutado
					//la operación.
					List<HashMap<String,Serializable>> ret = we.get();
					JFrame res = new ResultadosConsultaFrame(padre, ret, ResultadosConsultaFrame.RETRIEVE, tc, query, user);
					res.setVisible(true);
					
					me.setVisible(false);
					me.dispose();
				} catch (Exception e1) {
					cambiarLookAndFeel(true);
					me.setEnabled(true);
					JOptionPane.showMessageDialog(null,
							b.getString("connecterror"), "Error",JOptionPane.ERROR_MESSAGE);
				
				}
			}
		});
		panel.add(btnPasoapaso);
		btnEjecutarCiclo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HashMap<String,Serializable> query = new HashMap<String,Serializable>();
				for (Component c : panelAtbos.getComponents()) {
					PanelIntroducirValorAtbo p = (PanelIntroducirValorAtbo) c;
					query.put(p.getKey(), p.getValue());
					Atributo a =tc.getAtbos().get(p.getKey());
					a.setMetrica(p.getMetrica());
					a.setParamMetrica(p.getParamMetrica());
				}
			
				try {
					ControlCBR.retrieve(tc, query);
					// TODO: Ir al panel de resultados
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null,
							b.getString("connecterror"), "Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		contentPane.add(panelEjec, BorderLayout.SOUTH);
		rellenarAtributos();
		if (configurado) {
			rellenarListas();
			estableceFrameTecRec();
			estableceFrameTecRet();
			estableceFrameTecReu();
			estableceFrameTecRev();
			
			
			comboBoxReu.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent arg0) {
					estableceFrameTecReu();
				}
			});
			
			comboBoxRev.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent arg0) {
					estableceFrameTecRev();
				}
			});
			
			comboBoxRec.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent arg0) {
					estableceFrameTecRec();
				}
			});
			
			comboBoxRet.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent arg0) {
					estableceFrameTecRet();
				}
			});
		} else {
			me.setBounds(me.getX(), me.getY(),me.getWidth(), scrollPane.getPreferredSize().height+150);
		}
		
		pedirEstadisticas();
		setLocationRelativeTo(padre);
	}
	
	private void pedirEstadisticas(){
		Estadistica e=null;
		Usuario us = new Usuario();
		us.setNombre("ver todos");
		us.setTipo(TipoUsuario.ADMINISTRADOR);
		try {
			e=ControlEstadisticas.getEstadistica(us, tc);
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null,
					b.getString("connecterror"), "Error",JOptionPane.ERROR_MESSAGE);
		}if(e!=null){
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			lblEjecTotales.setText(b.getString("totalexec")+":"+e.getEjecTotales());
			lblMediaCalidad.setText(b.getString("avgquality")+":"+df.format(e.getMediaCalidad()));
		}
	}

	private void estableceFrameTecRec() {

		String tec = (String) comboBoxRec.getSelectedItem();
		asignaTecnicaCB(tec, "rec");
		Tecnica t = tc.getDefaultRec();

		switch (tec) {
		case "DiverseByMedianRetrieval":
			cRec = new DiverseByMedianConfigFrame(t, tc, me);
			;
			break;
		case "FilterBasedRetrieval":
			cRec = new FilterBasedConfigFrame(t, tc, me);
			;
			break;
		case "NNretrieval":
			cRec = new NNConfigFrame(t, me);
			;
			break;
		default: btnConfigRec.setEnabled(false);
			;
		}
	}

	private void estableceFrameTecReu() {
		String tec = (String) comboBoxReu.getSelectedItem();
		asignaTecnicaCB(tec, "reu");
		Tecnica t = tc.getDefaultReu();
		//Decido qué frame se abre según la técnica
		switch (tec) {
		case "DirectAttributeCopyMethod":
			cReu = new NumOrCopyConfigFrame(true,t, tc, me);
			;
			break;
		case "NumericDirectProportionMethod":
			cReu = new NumOrCopyConfigFrame(false,t, tc, me);
			;
			break;
		case "CombineQueryAndCasesMethod":
			cReu = new CombineQueryConfigFrame(t, me);
			;
			break;
		default:btnConfigReu.setEnabled(false);
			;
		}
		
	}

	private void estableceFrameTecRev() {
		String tec = (String) comboBoxRev.getSelectedItem();
		asignaTecnicaCB(tec, "rev");
		// En caso de crear nuevas técnicas,se debe seguir el switch de
		// estableceFrameTecRec
		btnConfigRev.setEnabled(false);
	}

	private void estableceFrameTecRet() {
		String tec = (String) comboBoxRet.getSelectedItem();
		asignaTecnicaCB(tec, "ret");
		// En caso de crear nuevas técnicas,se debe seguir el switch de
		// estableceFrameTecRec
		btnConfigRet.setEnabled(false);
	}

	private void rellenarAtributos() {
		//Si hay un solo atbo de consulta se usa un tam de 60 para el scrollpane, si no, el normal de 86
		int tam=0;
		for (Atributo at : tc.getAtbos().values()) {
			if (at.getEsProblema()) {
				JPanel p =new PanelIntroducirValorAtbo(at, configurado, me);
				panelAtbos.add(p);
				tam+=60;
			}
		}
		scrollPane.setPreferredSize(new Dimension(scrollPane.getWidth(),Math.min(tam,86)));
	}

	// Auxiliar. Asigna la técnica de un combobox como técnica por defecto del
	// tipo de caso.
	private void asignaTecnicaCB(String tecnicaCB, String tipoTec) {
		List<Tecnica> disponibles = null;
		switch (tipoTec) {
		case "rec":
			disponibles = tc.getTecnicasRecuperacion();
			break;
		case "rev":
			disponibles = tc.getTecnicasRevision();
			break;
		case "reu":
			disponibles = tc.getTecnicasReutilizacion();
			break;
		case "ret":
			disponibles = tc.getTecnicasRetencion();
			break;
		}
		Tecnica t = ControlTecnicas.buscaTecnica(disponibles, tecnicaCB);
		switch (tipoTec) {
		case "rec":
			tc.setDefaultRec(t);
			break;
		case "rev":
			tc.setDefaultRev(t);
			break;
		case "reu":
			tc.setDefaultReu(t);
			break;
		case "ret":
			tc.setDefaultRet(t);
			break;
		}
	}

	private void rellenarListas() {
		for (Tecnica t : tc.getTecnicasRecuperacion()) {
			comboBoxRec.addItem(t.getNombre());
		}
		comboBoxRec.setSelectedItem(tc.getDefaultRec().getNombre());
		for (Tecnica t : tc.getTecnicasReutilizacion()) {
			comboBoxReu.addItem(t.getNombre());
		}
		comboBoxReu.setSelectedItem(tc.getDefaultReu().getNombre());
		for (Tecnica t : tc.getTecnicasRevision()) {
			comboBoxRev.addItem(t.getNombre());
		}
		comboBoxRev.setSelectedItem(tc.getDefaultRev().getNombre());
		for (Tecnica t : tc.getTecnicasRetencion()) {
			comboBoxRet.addItem(t.getNombre());
		}
		comboBoxRet.setSelectedItem(tc.getDefaultRet().getNombre());
	}
	
	
	/**Cambia momentaneamente el look and feel para mostrar un splash.
	 * @param acryl
	 */
	private void cambiarLookAndFeel(boolean acryl){
		try{
		if(acryl){
			UIManager
					.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
		}else{
			UIManager
					.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			
		}}catch(Exception ex){}
	}
}
