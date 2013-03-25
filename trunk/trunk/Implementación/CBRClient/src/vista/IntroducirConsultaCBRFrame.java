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
import vista.configtecnicas.DiverseByMedianConfigFrame;
import vista.configtecnicas.FilterBasedConfigFrame;
import vista.configtecnicas.NNConfigFrame;
import vista.panels.PanelIntroducirValorAtbo;
import controlador.ControlEstadisticas;
import controlador.ControlTecnicas;

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

	/**
	 * Create the frame.
	 */
	public IntroducirConsultaCBRFrame(TipoCaso tic, boolean configurado,
			final JFrame padre) {
		super(padre);me=this;
		this.tc = tic;
		this.configurado = configurado;
		setTitle("CBR: " + tc.getNombre());
		setBounds(100, 100, 573, 455);
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
			gbl_panelMet.columnWidths = new int[] { 90, 0, 145, 35, 87, 72, 0, 0 };
			gbl_panelMet.rowHeights = new int[] { 10, 14, 23, 14, 23, 14, 23, 14,
					23, 0, 0 };
			gbl_panelMet.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0,
					0.0, 0.0, Double.MIN_VALUE };
			gbl_panelMet.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
					0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
			panelMet.setLayout(gbl_panelMet);

			lblRec = new JLabel(b.getString("retrieval"));
			GridBagConstraints gbc_lblRec = new GridBagConstraints();
			gbc_lblRec.insets = new Insets(0, 0, 5, 5);
			gbc_lblRec.gridx = 2;
			gbc_lblRec.gridy = 1;
			panelMet.add(lblRec, gbc_lblRec);

			comboBoxRec = new JComboBox<String>();
			GridBagConstraints gbc_comboBoxRec = new GridBagConstraints();
			gbc_comboBoxRec.gridwidth = 4;
			gbc_comboBoxRec.fill = GridBagConstraints.HORIZONTAL;
			gbc_comboBoxRec.insets = new Insets(0, 0, 5, 5);
			gbc_comboBoxRec.gridx = 1;
			gbc_comboBoxRec.gridy = 2;
			panelMet.add(comboBoxRec, gbc_comboBoxRec);
			comboBoxRec.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent arg0) {
					estableceFrameTecRec();
				}
			});
			
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

			lblReu = new JLabel(b.getString("reuse"));
			GridBagConstraints gbc_lblReu = new GridBagConstraints();
			gbc_lblReu.insets = new Insets(0, 0, 5, 5);
			gbc_lblReu.gridx = 2;
			gbc_lblReu.gridy = 3;
			panelMet.add(lblReu, gbc_lblReu);

			comboBoxReu = new JComboBox<String>();
			GridBagConstraints gbc_comboBoxReu = new GridBagConstraints();
			gbc_comboBoxReu.gridwidth = 4;
			gbc_comboBoxReu.fill = GridBagConstraints.HORIZONTAL;
			gbc_comboBoxReu.insets = new Insets(0, 0, 5, 5);
			gbc_comboBoxReu.gridx = 1;
			gbc_comboBoxReu.gridy = 4;
			panelMet.add(comboBoxReu, gbc_comboBoxReu);
			comboBoxReu.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent arg0) {
					estableceFrameTecReu();
				}
			});
			
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

			lblRev = new JLabel(b.getString("revise"));
			GridBagConstraints gbc_lblRev = new GridBagConstraints();
			gbc_lblRev.insets = new Insets(0, 0, 5, 5);
			gbc_lblRev.gridx = 2;
			gbc_lblRev.gridy = 5;
			panelMet.add(lblRev, gbc_lblRev);

			comboBoxRev = new JComboBox<String>();
			GridBagConstraints gbc_comboBoxRev = new GridBagConstraints();
			gbc_comboBoxRev.gridwidth = 4;
			gbc_comboBoxRev.fill = GridBagConstraints.HORIZONTAL;
			gbc_comboBoxRev.insets = new Insets(0, 0, 5, 5);
			gbc_comboBoxRev.gridx = 1;
			gbc_comboBoxRev.gridy = 6;
			panelMet.add(comboBoxRev, gbc_comboBoxRev);
			comboBoxRev.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent arg0) {
					estableceFrameTecRev();
				}
			});
			
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

			lblRet = new JLabel(b.getString("retain"));
			GridBagConstraints gbc_lblRet = new GridBagConstraints();
			gbc_lblRet.insets = new Insets(0, 0, 5, 5);
			gbc_lblRet.gridx = 2;
			gbc_lblRet.gridy = 7;
			panelMet.add(lblRet, gbc_lblRet);

			comboBoxRet = new JComboBox<String>();
			GridBagConstraints gbc_comboBoxRet = new GridBagConstraints();
			gbc_comboBoxRet.gridwidth = 4;
			gbc_comboBoxRet.fill = GridBagConstraints.HORIZONTAL;
			gbc_comboBoxRet.insets = new Insets(0, 0, 5, 5);
			gbc_comboBoxRet.gridx = 1;
			gbc_comboBoxRet.gridy = 8;
			panelMet.add(comboBoxRet, gbc_comboBoxRet);
			comboBoxRet.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent arg0) {
					estableceFrameTecRet();
				}
			});
			
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
		panelEjec.add(panel);
		btnEjecutarCiclo = new JButton(b.getString("executeCBR"));
		panel.add(btnEjecutarCiclo);
		
		btnPasoapaso = new JButton(b.getString("stepbystep"));
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
		} else {
			me.setBounds(me.getX(), me.getY(),me.getWidth(), scrollPane.getPreferredSize().height+140);
		}
		
		pedirEstadisticas();
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
			lblEjecTotales.setText(b.getString("totalexec")+":"+e.getEjecTotales());
			lblMediaCalidad.setText(b.getString("avgquality")+":"+e.getMediaCalidad());
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
		default:
			;
		}
	}

	private void estableceFrameTecReu() {
		String tec = (String) comboBoxReu.getSelectedItem();
		asignaTecnicaCB(tec, "reu");
		// En caso de crear nuevas técnicas,se debe seguir el switch de
		// estableceFrameTecRec
		btnConfigReu.setEnabled(false);
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
		comboBoxRec.setSelectedItem(tc.getDefaultRec().toString());
		for (Tecnica t : tc.getTecnicasReutilizacion()) {
			comboBoxReu.addItem(t.getNombre());
		}
		comboBoxReu.setSelectedItem(tc.getDefaultReu().toString());
		for (Tecnica t : tc.getTecnicasRevision()) {
			comboBoxRev.addItem(t.getNombre());
		}
		comboBoxRev.setSelectedItem(tc.getDefaultRev().toString());
		for (Tecnica t : tc.getTecnicasRetencion()) {
			comboBoxRet.addItem(t.getNombre());
		}
		comboBoxRet.setSelectedItem(tc.getDefaultRet().toString());
	}
}
