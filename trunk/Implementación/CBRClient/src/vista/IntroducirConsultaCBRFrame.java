package vista;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.Tecnica;
import servidorcbr.modelo.TipoCaso;
import vista.configtecnicas.DiverseByMedianConfigFrame;
import vista.configtecnicas.FilterBasedConfigFrame;
import vista.configtecnicas.NNConfigFrame;
import vista.panels.PanelIntroducirValorAtbo;

import java.awt.FlowLayout;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import controlador.ControlCBR;
import controlador.ControlTecnicas;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;

import javax.swing.border.TitledBorder;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class IntroducirConsultaCBRFrame extends JFrame {

	private JPanel contentPane;

	private JFrame cRec, cReu, cRev, cRet;

	private ResourceBundle b = ResourceBundle.getBundle(
			"vista.internacionalizacion.Recursos", Locale.getDefault());

	private TipoCaso tc;
	private JFrame padre, me = this;
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

	/**
	 * Create the frame.
	 */
	public IntroducirConsultaCBRFrame(TipoCaso tic, boolean configurado,
			JFrame padre) {
		setIconImage(new ImageIcon("res/logocbr.png").getImage());
		setResizable(false);
		this.tc = tic;
		this.configurado = configurado;
		this.padre = padre;
		setTitle("CBR: " + tc.getNombre());
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		cierreVentana();

		setBounds(100, 100, 512, 431);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(2, 2));

		scrollPane = new JScrollPane();
		
		contentPane.add(scrollPane,BorderLayout.NORTH);

		panelAtbos = new JPanel();
		scrollPane.setViewportView(panelAtbos);
		panelAtbos.setLayout(new BoxLayout(panelAtbos, BoxLayout.Y_AXIS));
		if (configurado) {
			panelMet = new JPanel();
			panelMet.setBorder(new TitledBorder(UIManager
					.getBorder("TitledBorder.border"), b
					.getString("managemethods"), TitledBorder.CENTER,
					TitledBorder.TOP, null, null));
			contentPane.add(panelMet,BorderLayout.CENTER);
			GridBagLayout gbl_panelMet = new GridBagLayout();
			gbl_panelMet.columnWidths = new int[] { 30, 145, 35, 87, 0, 0 };
			gbl_panelMet.rowHeights = new int[] { 14, 23, 14, 23, 14, 23, 14,
					23, 0, 0 };
			gbl_panelMet.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0,
					0.0, Double.MIN_VALUE };
			gbl_panelMet.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0,
					0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
			panelMet.setLayout(gbl_panelMet);

			lblRec = new JLabel(b.getString("retrieval"));
			GridBagConstraints gbc_lblRec = new GridBagConstraints();
			gbc_lblRec.insets = new Insets(0, 0, 5, 5);
			gbc_lblRec.gridx = 1;
			gbc_lblRec.gridy = 0;
			panelMet.add(lblRec, gbc_lblRec);

			comboBoxRec = new JComboBox<String>();
			GridBagConstraints gbc_comboBoxRec = new GridBagConstraints();
			gbc_comboBoxRec.fill = GridBagConstraints.HORIZONTAL;
			gbc_comboBoxRec.insets = new Insets(0, 0, 5, 5);
			gbc_comboBoxRec.gridx = 1;
			gbc_comboBoxRec.gridy = 1;
			panelMet.add(comboBoxRec, gbc_comboBoxRec);
			comboBoxRec.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent arg0) {
					estableceFrameTecRec();
				}
			});

			btnConfigRec = new JButton(b.getString("configure"));
			GridBagConstraints gbc_btnConfigRec = new GridBagConstraints();
			gbc_btnConfigRec.insets = new Insets(0, 0, 5, 5);
			gbc_btnConfigRec.gridx = 3;
			gbc_btnConfigRec.gridy = 1;
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
			gbc_lblReu.gridx = 1;
			gbc_lblReu.gridy = 2;
			panelMet.add(lblReu, gbc_lblReu);

			comboBoxReu = new JComboBox<String>();
			GridBagConstraints gbc_comboBoxReu = new GridBagConstraints();
			gbc_comboBoxReu.fill = GridBagConstraints.HORIZONTAL;
			gbc_comboBoxReu.insets = new Insets(0, 0, 5, 5);
			gbc_comboBoxReu.gridx = 1;
			gbc_comboBoxReu.gridy = 3;
			panelMet.add(comboBoxReu, gbc_comboBoxReu);
			comboBoxReu.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent arg0) {
					estableceFrameTecReu();
				}
			});

			btnConfigReu = new JButton(b.getString("configure"));
			GridBagConstraints gbc_btnConfigReu = new GridBagConstraints();
			gbc_btnConfigReu.insets = new Insets(0, 0, 5, 5);
			gbc_btnConfigReu.gridx = 3;
			gbc_btnConfigReu.gridy = 3;
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
			gbc_lblRev.gridx = 1;
			gbc_lblRev.gridy = 4;
			panelMet.add(lblRev, gbc_lblRev);

			comboBoxRev = new JComboBox<String>();
			GridBagConstraints gbc_comboBoxRev = new GridBagConstraints();
			gbc_comboBoxRev.fill = GridBagConstraints.HORIZONTAL;
			gbc_comboBoxRev.insets = new Insets(0, 0, 5, 5);
			gbc_comboBoxRev.gridx = 1;
			gbc_comboBoxRev.gridy = 5;
			panelMet.add(comboBoxRev, gbc_comboBoxRev);
			comboBoxRev.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent arg0) {
					estableceFrameTecRev();
				}
			});

			btnConfigRev = new JButton(b.getString("configure"));
			GridBagConstraints gbc_btnConfigRev = new GridBagConstraints();
			gbc_btnConfigRev.insets = new Insets(0, 0, 5, 5);
			gbc_btnConfigRev.gridx = 3;
			gbc_btnConfigRev.gridy = 5;
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
			gbc_lblRet.gridx = 1;
			gbc_lblRet.gridy = 6;
			panelMet.add(lblRet, gbc_lblRet);

			comboBoxRet = new JComboBox<String>();
			GridBagConstraints gbc_comboBoxRet = new GridBagConstraints();
			gbc_comboBoxRet.fill = GridBagConstraints.HORIZONTAL;
			gbc_comboBoxRet.insets = new Insets(0, 0, 5, 5);
			gbc_comboBoxRet.gridx = 1;
			gbc_comboBoxRet.gridy = 7;
			panelMet.add(comboBoxRet, gbc_comboBoxRet);
			comboBoxRet.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent arg0) {
					estableceFrameTecRet();
				}
			});

			btnConfigRet = new JButton(b.getString("configure"));
			GridBagConstraints gbc_btnConfigRet = new GridBagConstraints();
			gbc_btnConfigRet.insets = new Insets(0, 0, 5, 5);
			gbc_btnConfigRet.gridx = 3;
			gbc_btnConfigRet.gridy = 7;
			panelMet.add(btnConfigRet, gbc_btnConfigRet);
			btnConfigRet.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					cRet.setVisible(true);
					me.setEnabled(false);
				}
			});
	
		}
		btnEjecutarCiclo = new JButton(b.getString("executeCBR"));
		btnEjecutarCiclo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HashMap<String,Serializable> query = new HashMap<String,Serializable>();
				for (Component c : panelAtbos.getComponents()) {
					PanelIntroducirValorAtbo p = (PanelIntroducirValorAtbo) c;
					query.put(p.getKey(), p.getValue());
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
		
		panelEjec = new JPanel();
		panelEjec.add(btnEjecutarCiclo);
		contentPane.add(panelEjec, BorderLayout.SOUTH);
		rellenarAtributos();
		if (configurado) {
			rellenarListas();
			estableceFrameTecRec();
			estableceFrameTecRet();
			estableceFrameTecReu();
			estableceFrameTecRev();
		} else {
			me.setBounds(me.getX(), me.getY(),me.getWidth(), scrollPane.getHeight() + 250);
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
		int i=0;
		for (Atributo at : tc.getAtbos().values()) {
			if (at.getEsProblema()) {
				panelAtbos
						.add(new PanelIntroducirValorAtbo(at, configurado, me));
			}
			i++;
		}
		scrollPane.setPreferredSize(new Dimension(scrollPane.getWidth(),Math.min(i*30, 150)));
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

	private void cierreVentana() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				padre.setEnabled(true);
				me.dispose();
			}
		});
	}

}
