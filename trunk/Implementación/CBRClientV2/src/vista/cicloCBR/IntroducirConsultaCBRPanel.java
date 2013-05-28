package vista.cicloCBR;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.Estadistica;
import servidorcbr.modelo.Tecnica;
import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.TipoUsuario;
import servidorcbr.modelo.Usuario;
import vista.MainFrame;
import vista.componentesGenericos.PanelEstandar;
import vista.panels.PanelIntroducirValorAtbo;
import vista.tipos.configtecnicas.CombineQueryConfigFrame;
import vista.tipos.configtecnicas.DiverseByMedianConfigFrame;
import vista.tipos.configtecnicas.FilterBasedConfigFrame;
import vista.tipos.configtecnicas.NNConfigFrame;
import vista.tipos.configtecnicas.NumOrCopyConfigFrame;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import controlador.ControlCBR;
import controlador.ControlEstadisticas;
import controlador.ControlTecnicas;

/** Panel para introducir consultas CBR. Permite introducir
 * una consulta CBR configurada o no. En las configuradas se permite introducir
 * pesos para los atributos de la consulta, métrica que usará cada atributo y
 * escoger la técnica que se usará en cada etapa del ciclo.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
@SuppressWarnings("serial")
public class IntroducirConsultaCBRPanel extends PanelEstandar {

	/**
	 * Frame para configurar las técnicas de una etapa del ciclo.
	 */
	private JFrame cRec, cReu, cRev, cRet;

	/**
	 * Tipo de caso de la consulta.
	 */
	private TipoCaso tc;
	/**
	 * Cierto si es un ciclo configurado.
	 */
	private boolean configurado;
	/**
	 * Combobox de técnicas de recuperación.
	 */
	private JComboBox<String> comboBoxRec;
	/**
	 * Botón de configurar la técnica de recuperación.
	 */
	private JButton btnConfigRec;
	/**
	 * Combobox de técnicas de reutilización.
	 */
	private JComboBox<String> comboBoxReu;
	/**
	 * Botón de configurar la técnica de reutilización.
	 */
	private JButton btnConfigReu;
	/**
	 * Botón de configurar la técnica de revisión.
	 */
	private JButton btnConfigRev;
	/**
	 * Botón de configurar la técnica de retención.
	 */
	private JButton btnConfigRet;
	/**
	 * Combobox de técnicas de revisión.
	 */
	private JComboBox<String> comboBoxRev;
	/**
	 * Combobox de técnicas de retención.
	 */
	private JComboBox<String> comboBoxRet;
	/**
	 * Label con el texto internacionalizado de recuperación.
	 */
	private JLabel lblRec;
	/**
	 * Label con el texto internacionalizado de reutilización.
	 */
	private JLabel lblReu;
	/**
	 * Label con el texto internacionalizado de revisión.
	 */
	private JLabel lblRev;

	 /**
	 * Label con el texto internacionalizado de retención.
	 */	
	private JLabel lblRet;
	/**
	 * Botón que ejecuta el ciclo.
	 */
	private JButton btnEjecutarCiclo;
	/**
	 * Panel de atributos.
	 */
	private JPanel panelAtbos;
	/**
	 * ScrollPane en el que se introduce la consulta
	 */
	private JScrollPane scrollPane;
	/**
	 * Panel de técnicas en el ciclo configurado.
	 */
	private JPanel panelMet;
	
	/**
	 * Panel de ejecución. Es en el que se muestran las opciones de ejecución. 
	 */
	private JPanel panelEjec;
	/**
	 * Panel principal interno al de ejecución.
	 */
	private JPanel panel;
	/**
	 * Etiqueta de ejecuciones totales CBR.
	 */
	private JLabel lblEjecTotales;
	/**
	 * Panel de estadísticas del tipo de caso.
	 */
	private JPanel panelEstad;
	/**
	 * Etiqueta con la media de calidad. 
	 */
	private JLabel lblMediaCalidad;

	/**
	 * Botón de ciclo paso a paso.
	 */
	private JButton btnPasoapaso;
	/**
	 * Panel interno en el ciclo configurado. (Para el correcto funcionamiento del layout).
	 */
	private JPanel panel_2;
	/**
	 * Panel interno en el ciclo configurado. (Para el correcto funcionamiento del layout).
	 */
	private JPanel panel_3;
	/**
	 * Panel interno en el ciclo configurado. (Para el correcto funcionamiento del layout).
	 */
	private JPanel panel_4;
	/**
	 * Panel interno en el ciclo configurado. (Para el correcto funcionamiento del layout).
	 */
	private JPanel panel_5;
	
	/**
	 * Usuario que ejecuta la consulta.
	 */
	private Usuario user;
	/**
	 * Etiqueta con el icono que indica una ejecución en curso.
	 */
	private JLabel lblIcon;
	/**
	 * Panel interno en el ciclo configurado. (Para el correcto funcionamiento del layout).
	 */
	private JPanel panel_6;
	/**
	 * Panel principal de la ventana.
	 */
	private JPanel panelPrincipal;
	/**
	 * Etiqueta que muestra la última ejecución del caso.
	 */
	private JLabel lblUltimaej;
	/**
	 * Etiqueta que muestra la técnica de retención por defecto del tipo de caso.
	 */
	private JLabel lblDefret;
	/**
	 * Separador.
	 */
	private JSeparator separator;
	/**
	 * Etiqueta que muestra la técnica de recuperación por defecto del tipo de caso.
	 */
	private JLabel lblRecuDef;
	/**
	 * Etiqueta que muestra la técnica de reutilización por defecto del tipo de caso.
	 */
	private JLabel lblReuDef;
	/**
	 * Etiqueta que muestra la técnica de revisión por defecto del tipo de caso.
	 */
	private JLabel lblRevDef;
	/**
	 * Etiqueta que muestra la técnica de retención por defecto del tipo de caso.
	 */
	private JLabel lblRetDef;
	/**
	 * Label con el texto 'Estadísticas' internacionalizado.
	 */
	private JLabel lblStat;


	/** Constructor del panel de consulta CBR.
	 * @param tic Tipo de caso de la consulta.
	 * @param configurado true si es un ciclo configurado, false si no.
	 * @param padre Frame principal de la aplicación.
	 * @param u Usuario que ejecuta el ciclo.
	 */
	public IntroducirConsultaCBRPanel(TipoCaso tic, boolean configurado,
			final MainFrame padre, Usuario u) {
		super(padre);
		me = this;

		setLayout(new BorderLayout(0, 0));
		panelPrincipal = new JPanel();
		panelPrincipal.setLayout(new BorderLayout(0, 0));
		add(panelPrincipal, BorderLayout.CENTER);

		this.tc = tic;
		this.configurado = configurado;
		user = u;
		scrollPane = new JScrollPane();
		setName("CBR:" + tc.getNombre());
		setBounds(100, 100, 573, 525);

		// El panel de tecnicas solo existe en un ciclo configurado
		if (configurado) {
			panel_6 = new JPanel();
			panelPrincipal.add(panel_6, BorderLayout.CENTER);
			panelMet = new JPanel();
			panel_6.add(panelMet);
			panelMet.setBorder(new TitledBorder(UIManager
					.getBorder("TitledBorder.border"), b
					.getString("managemethods"), TitledBorder.CENTER,
					TitledBorder.TOP, null, Color.BLUE));

			GridBagLayout gbl_panelMet = new GridBagLayout();
			gbl_panelMet.columnWidths = new int[] { 0, 145, 35, 87, 72, 0 };
			gbl_panelMet.rowHeights = new int[] { 15, 14, 23, 14, 23, 14, 23,
					14, 19, 0 };
			gbl_panelMet.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0,
					0.0, Double.MIN_VALUE };
			gbl_panelMet.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0,
					0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
			panelMet.setLayout(gbl_panelMet);

			panel_2 = new JPanel();
			panel_2.setBackground(Color.GRAY);
			panel_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			GridBagConstraints gbc_panel_2 = new GridBagConstraints();
			gbc_panel_2.gridwidth = 5;
			gbc_panel_2.insets = new Insets(0, 0, 5, 0);
			gbc_panel_2.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel_2.gridx = 0;
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
			gbc_comboBoxRec.gridx = 0;
			gbc_comboBoxRec.gridy = 2;
			panelMet.add(comboBoxRec, gbc_comboBoxRec);

			btnConfigRec = new JButton(b.getString("configure"));
			GridBagConstraints gbc_btnConfigRec = new GridBagConstraints();
			gbc_btnConfigRec.insets = new Insets(0, 0, 5, 0);
			gbc_btnConfigRec.gridx = 4;
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
			gbc_panel_3.insets = new Insets(0, 0, 5, 0);
			gbc_panel_3.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel_3.gridx = 0;
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
			gbc_comboBoxReu.gridx = 0;
			gbc_comboBoxReu.gridy = 4;
			panelMet.add(comboBoxReu, gbc_comboBoxReu);

			btnConfigReu = new JButton(b.getString("configure"));
			GridBagConstraints gbc_btnConfigReu = new GridBagConstraints();
			gbc_btnConfigReu.insets = new Insets(0, 0, 5, 0);
			gbc_btnConfigReu.gridx = 4;
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
			gbc_panel_4.insets = new Insets(0, 0, 5, 0);
			gbc_panel_4.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel_4.gridx = 0;
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
			gbc_comboBoxRev.gridx = 0;
			gbc_comboBoxRev.gridy = 6;
			panelMet.add(comboBoxRev, gbc_comboBoxRev);

			btnConfigRev = new JButton(b.getString("configure"));
			GridBagConstraints gbc_btnConfigRev = new GridBagConstraints();
			gbc_btnConfigRev.insets = new Insets(0, 0, 5, 0);
			gbc_btnConfigRev.gridx = 4;
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
			gbc_panel_5.insets = new Insets(0, 0, 5, 0);
			gbc_panel_5.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel_5.gridx = 0;
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
			gbc_comboBoxRet.insets = new Insets(0, 0, 0, 5);
			gbc_comboBoxRet.gridx = 0;
			gbc_comboBoxRet.gridy = 8;
			panelMet.add(comboBoxRet, gbc_comboBoxRet);

			btnConfigRet = new JButton(b.getString("configure"));
			GridBagConstraints gbc_btnConfigRet = new GridBagConstraints();
			gbc_btnConfigRet.gridx = 4;
			gbc_btnConfigRet.gridy = 8;
			panelMet.add(btnConfigRet, gbc_btnConfigRet);
			btnConfigRet.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					cRet.setVisible(true);
					me.setEnabled(false);
				}
			});

			scrollPane.setPreferredSize(new Dimension(100, 150));
			panelPrincipal.add(scrollPane, BorderLayout.NORTH);
		} else {
			panelPrincipal.add(scrollPane, BorderLayout.CENTER);
		}

		panelEjec = new JPanel();
		panelPrincipal.add(panelEjec, BorderLayout.SOUTH);
		panelEjec.setLayout(new BoxLayout(panelEjec, BoxLayout.Y_AXIS));

		panel = new JPanel();
		panel.setBackground(Color.GRAY);
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelEjec.add(panel);
		btnEjecutarCiclo = new JButton(b.getString("executeCBR"));
		panel.add(btnEjecutarCiclo);

		btnPasoapaso = new JButton(b.getString("stepbystep"));
		btnPasoapaso.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HashMap<String, Serializable> query = new HashMap<String, Serializable>();
				for (Component c : panelAtbos.getComponents()) {
					PanelIntroducirValorAtbo p = (PanelIntroducirValorAtbo) c;
					query.put(p.getKey(), p.getValue());
					Atributo a = tc.getAtbos().get(p.getKey());
					a.setMetrica(p.getMetrica());
					a.setParamMetrica(p.getParamMetrica());
					a.setPeso(p.getPeso());
				}

				try {
					lblIcon.setVisible(true);
					WorkerEspera we = new WorkerEspera(me, tc, query, user);
					padre.setEnabled(false);

					we.execute();
				} catch (Exception e1) {
					e1.printStackTrace();
					padre.setEnabled(true);
					JOptionPane.showMessageDialog(null,
							b.getString("connecterror"), "Error",
							JOptionPane.ERROR_MESSAGE);

				}
			}
		});

		lblIcon = new JLabel("");
		lblIcon.setIcon(new ImageIcon("res/espera.gif"));
		lblIcon.setVisible(false);
		panel.add(lblIcon);
		panel.add(btnPasoapaso);

		panelEstad = new JPanel();
		panelEstad.setBackground(new Color(176, 196, 222));
		add(panelEstad, BorderLayout.WEST);
		panelEstad.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), b.getString("quickstats"),
				TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0,
						128)));
		panelEstad.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.UNRELATED_GAP_COLSPEC,
				ColumnSpec.decode("275px"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("14px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("14px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));

		lblStat = new JLabel(b.getString("stats") + ":");
		lblStat.setForeground(new Color(0, 0, 128));
		lblStat.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 11));
		panelEstad.add(lblStat, "2, 3, left, default");

		lblEjecTotales = new JLabel(b.getString("totalexec") + ":");
		panelEstad.add(lblEjecTotales, "2, 5, left, center");

		lblUltimaej = new JLabel(b.getString("lastexec") + ":");
		panelEstad.add(lblUltimaej, "2, 7, fill, top");

		lblMediaCalidad = new JLabel(b.getString("avgquality") + ":");
		panelEstad.add(lblMediaCalidad, "2, 9, left, center");

		separator = new JSeparator();
		panelEstad.add(separator, "2, 13");

		lblDefret = new JLabel(b.getString("defaultmethods"));
		lblDefret.setForeground(new Color(0, 0, 128));
		lblDefret.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 11));
		panelEstad.add(lblDefret, "2, 17, left, default");

		lblRecuDef = new JLabel(b.getString("retrieval") + ": "
				+ tc.getDefaultRec().getNombre());
		panelEstad.add(lblRecuDef, "2, 19");

		lblReuDef = new JLabel(b.getString("reuse") + ": "
				+ tc.getDefaultReu().getNombre());
		panelEstad.add(lblReuDef, "2, 21");

		lblRevDef = new JLabel(b.getString("revise") + ": "
				+ tc.getDefaultRev().getNombre());
		panelEstad.add(lblRevDef, "2, 23");

		lblRetDef = new JLabel(b.getString("retain") + ": "
				+ tc.getDefaultRet().getNombre());
		panelEstad.add(lblRetDef, "2, 25");

		btnEjecutarCiclo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HashMap<String, Serializable> query = new HashMap<String, Serializable>();
				for (Component c : panelAtbos.getComponents()) {
					PanelIntroducirValorAtbo p = (PanelIntroducirValorAtbo) c;
					query.put(p.getKey(), p.getValue());
					Atributo a = tc.getAtbos().get(p.getKey());
					a.setMetrica(p.getMetrica());
					a.setParamMetrica(p.getParamMetrica());
				}

				try {
					ControlCBR.retrieve(tc, query);
					// TODO: Ir al panel de resultados
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null,
							b.getString("connecterror"), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		scrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		panelAtbos = new JPanel();

		panelAtbos.setBackground(Color.LIGHT_GRAY);
		scrollPane.setViewportView(panelAtbos);
		panelAtbos.setLayout(new BoxLayout(panelAtbos, BoxLayout.Y_AXIS));
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
		}
		pedirEstadisticas();

	}

	/**
	 *Método interno para pedir las estadísticas del caso al servidor. 
	 */
	private void pedirEstadisticas() {
		Estadistica e = null;
		Usuario us = new Usuario();
		us.setNombre("ver todos");
		us.setTipo(TipoUsuario.ADMINISTRADOR);
		try {
			e = ControlEstadisticas.getEstadistica(us, tc);
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, b.getString("connecterror"),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
		if (e != null) {
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			lblEjecTotales.setText(b.getString("totalexec") + ":"
					+ e.getEjecTotales());
			lblMediaCalidad.setText(b.getString("avgquality") + ":"
					+ df.format(e.getMediaCalidad()));
			if (e.getFechaUltima() != null) {
				lblUltimaej.setText(b.getString("lastexec") + ":"
						+ e.getFechaUltima());
			}
		}
	}

	/**
	 * Auxiliar. Establece los botones de configurar técnicas de recuperación 
	 * en un ciclo configurado.
	 */
	private void estableceFrameTecRec() {

		String tec = (String) comboBoxRec.getSelectedItem();
		asignaTecnicaCB(tec, "rec");
		Tecnica t = tc.getDefaultRec();

		switch (tec) {
		case "DiverseByMedianRetrieval":
			cRec = new DiverseByMedianConfigFrame(t, tc, padre);
			;
			break;
		case "FilterBasedRetrieval":
			cRec = new FilterBasedConfigFrame(t, tc, padre);
			;
			break;
		case "NNretrieval":
			cRec = new NNConfigFrame(t, padre);
			;
			break;
		default:
			btnConfigRec.setEnabled(false);
			;
		}
	}

	/**
	 * Auxiliar. Establece los botones de configurar técnicas de reutilización
	 * en un ciclo configurado.
	 */
	private void estableceFrameTecReu() {
		String tec = (String) comboBoxReu.getSelectedItem();
		asignaTecnicaCB(tec, "reu");
		Tecnica t = tc.getDefaultReu();
		// Decido qué frame se abre según la técnica
		switch (tec) {
		case "DirectAttributeCopyMethod":
			cReu = new NumOrCopyConfigFrame(true, t, tc, padre);
			;
			break;
		case "NumericDirectProportionMethod":
			cReu = new NumOrCopyConfigFrame(false, t, tc, padre);
			;
			break;
		case "CombineQueryAndCasesMethod":
			cReu = new CombineQueryConfigFrame(t, padre);
			;
			break;
		default:
			btnConfigReu.setEnabled(false);
			;
		}

	}

	/**
	 * Auxiliar. Establece los botones de configurar técnicas de revisión
	 * en un ciclo configurado.
	 */
	private void estableceFrameTecRev() {
		String tec = (String) comboBoxRev.getSelectedItem();
		asignaTecnicaCB(tec, "rev");
		// En caso de crear nuevas técnicas,se debe seguir el switch de
		// estableceFrameTecRec
		btnConfigRev.setEnabled(false);
	}
	
	/**
	 * Auxiliar. Establece los botones de configurar técnicas de retención 
	 * en un ciclo configurado.
	 */
	private void estableceFrameTecRet() {
		String tec = (String) comboBoxRet.getSelectedItem();
		asignaTecnicaCB(tec, "ret");
		// En caso de crear nuevas técnicas,se debe seguir el switch de
		// estableceFrameTecRec
		btnConfigRet.setEnabled(false);
	}

	/**
	 * Pinta los paneles que muestran el campo a rellenar de cada atributo en
	 * la consulta.
	 */
	private void rellenarAtributos() {
		for (Atributo at : tc.getAtbos().values()) {
			if (at.getEsProblema()) {
				JPanel p = new PanelIntroducirValorAtbo(at, configurado);
				p.setMaximumSize(new Dimension(620, p.getPreferredSize().height));
				panelAtbos.add(p);
			}
		}
	}

	
	/**Auxiliar. Asigna la técnica de un combobox como técnica por defecto del
	 * tipo de caso.
	 * @param tecnicaCB tecnica.
	 * @param tipoTec "rec","rev","reu","ret" según la etapa del ciclo al que pertenece
	 * la técnica.
	 */
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

	/**
	 * Rellena los desplegables de técnicas en un ciclo configurado.
	 */
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

}
