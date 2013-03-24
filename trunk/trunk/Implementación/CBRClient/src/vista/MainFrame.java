package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import servidorcbr.modelo.TipoUsuario;
import servidorcbr.modelo.Usuario;
import vista.estadisticas.EstadisticasFrame;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.border.EtchedBorder;

@SuppressWarnings("serial")
public class MainFrame extends JFrame{

	
	private final MainFrame me = this;
	@SuppressWarnings("unused")
	private final Usuario u;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the application.
	 */
	public MainFrame(Usuario u) {
		this.u=u;
		initialize(u);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(final Usuario u) {
		Locale l= Locale.getDefault();
		ResourceBundle bundle = ResourceBundle.getBundle(
	            "vista.internacionalizacion.Recursos", l);
		setTitle(bundle.getString("mainmenu"));
		setIconImage(new javax.swing.ImageIcon("res/logocbr.png").getImage());
		setResizable(false);
		setBounds(100, 100, 449, 208);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedpane = new JTabbedPane(JTabbedPane.TOP);
		tabbedpane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		getContentPane().add(tabbedpane, BorderLayout.CENTER);

		
		JPanel panel_adm = new JPanel();
		tabbedpane.addTab( bundle.getString("admin"), panel_adm);
		GridBagLayout gbl_panel_adm = new GridBagLayout();
		gbl_panel_adm.columnWidths = new int[]{54, 147, 35, 152, 0};
		gbl_panel_adm.rowHeights = new int[]{31, 58, 28, 0};
		gbl_panel_adm.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_adm.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_adm.setLayout(gbl_panel_adm);
		
		JButton btnGestionarTipos = new JButton((new ImageIcon("res/gear_64.png")));
		btnGestionarTipos.setToolTipText(bundle.getString("managecasetypes"));
		btnGestionarTipos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				GestionTiposFrame gestion = new GestionTiposFrame(u,me);
				gestion.setVisible(true);
				me.setEnabled(false);
			}
		});
		GridBagConstraints gbc_btnGestionarTipos = new GridBagConstraints();
		gbc_btnGestionarTipos.fill = GridBagConstraints.BOTH;
		gbc_btnGestionarTipos.insets = new Insets(0, 0, 5, 5);
		gbc_btnGestionarTipos.gridx = 1;
		gbc_btnGestionarTipos.gridy = 1;
		panel_adm.add(btnGestionarTipos, gbc_btnGestionarTipos);
		
		JButton btnGestionarusuarios = new JButton((new ImageIcon("res/user_64.png")));
		btnGestionarusuarios.setToolTipText(bundle.getString("manageusers"));
		
		btnGestionarusuarios.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFrame f = new GestionUsuariosFrame(me);
				f.setVisible(true);
				me.setEnabled(false);
			}
		});
		GridBagConstraints gbc_btnGestionarusuarios = new GridBagConstraints();
		gbc_btnGestionarusuarios.fill = GridBagConstraints.BOTH;
		gbc_btnGestionarusuarios.insets = new Insets(0, 0, 5, 0);
		gbc_btnGestionarusuarios.gridx = 3;
		gbc_btnGestionarusuarios.gridy = 1;
		panel_adm.add(btnGestionarusuarios, gbc_btnGestionarusuarios);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBackground(new Color(224, 255, 255));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.insets = new Insets(0, 0, 0, 5);
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 2;
		panel_adm.add(panel, gbc_panel);
		
		JLabel lblManage = new JLabel(bundle.getString("managecasetypes"));
		panel.add(lblManage);
		lblManage.setHorizontalAlignment(SwingConstants.CENTER);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.setBackground(new Color(224, 255, 255));
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 3;
		gbc_panel_1.gridy = 2;
		panel_adm.add(panel_1, gbc_panel_1);
		
		JLabel lblMngusers = new JLabel(bundle.getString("manageusers"));
		panel_1.add(lblMngusers);
		
		JPanel panel_cbr = new JPanel();
		tabbedpane.addTab("CBR",panel_cbr);
		GridBagLayout gbl_panel_cbr = new GridBagLayout();
		gbl_panel_cbr.columnWidths = new int[]{54, 147, 35, 152, 0};
		gbl_panel_cbr.rowHeights = new int[]{31, 58, 28, 0};
		gbl_panel_cbr.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_cbr.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_cbr.setLayout(gbl_panel_cbr);
		
		JButton btnCicloConfigurado = new JButton(new ImageIcon("res/cbr_configurado_64.png"));
		btnCicloConfigurado.setToolTipText(bundle.getString("configuredcycle"));
		btnCicloConfigurado.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ElegirTipoCasoFrame f = new ElegirTipoCasoFrame(u, me,ElegirTipoCasoFrame.CICLO_CONFIGURADO);
				f.setVisible(true);
				me.setEnabled(false);
			}
		});
		GridBagConstraints gbc_btnCicloConfigurado = new GridBagConstraints();
		gbc_btnCicloConfigurado.fill = GridBagConstraints.BOTH;
		gbc_btnCicloConfigurado.insets = new Insets(0, 0, 5, 5);
		gbc_btnCicloConfigurado.gridx = 1;
		gbc_btnCicloConfigurado.gridy = 1;
		panel_cbr.add(btnCicloConfigurado, gbc_btnCicloConfigurado);
		
		JButton btnCicloPorDefecto = new JButton(new ImageIcon("res/cbr_basico_64.png"));
		btnCicloPorDefecto.setToolTipText(bundle.getString("defaultcycle"));
		btnCicloPorDefecto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ElegirTipoCasoFrame f = new ElegirTipoCasoFrame(u, me, ElegirTipoCasoFrame.CICLO_BASICO);
				f.setVisible(true);
				me.setEnabled(false);
			}
		});
		GridBagConstraints gbc_btnCicloPorDefecto = new GridBagConstraints();
		gbc_btnCicloPorDefecto.fill = GridBagConstraints.BOTH;
		gbc_btnCicloPorDefecto.insets = new Insets(0, 0, 5, 0);
		if(u.getTipo().equals(TipoUsuario.UBASICO)){
			//Centro el boton para los usuarios basicos
			gbc_btnCicloPorDefecto.gridx = 2;	
		}else{
			gbc_btnCicloPorDefecto.gridx = 3;	
		}
		gbc_btnCicloPorDefecto.gridy = 1;
		panel_cbr.add(btnCicloPorDefecto, gbc_btnCicloPorDefecto);
		
		JPanel panel_lblconf = new JPanel();
		panel_lblconf.setBackground(new Color(224, 255, 255));
		panel_lblconf.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		GridBagConstraints gbc_panel_lblconf = new GridBagConstraints();
		gbc_panel_lblconf.insets = new Insets(0, 0, 0, 5);
		gbc_panel_lblconf.fill = GridBagConstraints.BOTH;
		gbc_panel_lblconf.gridx = 1;
		gbc_panel_lblconf.gridy = 2;
		panel_cbr.add(panel_lblconf, gbc_panel_lblconf);
		
		JLabel lblCbrco = new JLabel(bundle.getString("configuredcycle"));
		panel_lblconf.add(lblCbrco);
		
		JPanel panel_cbrpordefecto = new JPanel();
		panel_cbrpordefecto.setBackground(new Color(224, 255, 255));
		panel_cbrpordefecto.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		GridBagConstraints gbc_panel_cbrpordefecto = new GridBagConstraints();
		gbc_panel_cbrpordefecto.fill = GridBagConstraints.BOTH;
		
		if(u.getTipo().equals(TipoUsuario.UBASICO)){
			//Centro el panel para los usuarios basicos
			gbc_panel_cbrpordefecto.gridx = 2;
		}else{
			gbc_panel_cbrpordefecto.gridx = 3;	
		}
		gbc_panel_cbrpordefecto.gridy = 2;
		panel_cbr.add(panel_cbrpordefecto, gbc_panel_cbrpordefecto);
		
		JLabel lblCbr = new JLabel(bundle.getString("defaultcycle"));
		panel_cbrpordefecto.add(lblCbr);
		
		JPanel panel_casebase = new JPanel();
		tabbedpane.addTab(bundle.getString("casebases"),panel_casebase);
		GridBagLayout gbl_panel_casebase = new GridBagLayout();
		gbl_panel_casebase.columnWidths = new int[]{54, 147, 35, 152, 0};
		gbl_panel_casebase.rowHeights = new int[]{31, 58, 28, 0};
		gbl_panel_casebase.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_casebase.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_casebase.setLayout(gbl_panel_casebase);
		
		JButton btnEstadisticas = new JButton(new ImageIcon("res/statistics_64.png"));
		btnEstadisticas.setToolTipText(bundle.getString("stats"));
		GridBagConstraints gbc_btnEstadisticas = new GridBagConstraints();
		gbc_btnEstadisticas.fill = GridBagConstraints.BOTH;
		gbc_btnEstadisticas.insets = new Insets(0, 0, 5, 5);
		gbc_btnEstadisticas.gridx = 1;
		gbc_btnEstadisticas.gridy = 1;
		panel_casebase.add(btnEstadisticas, gbc_btnEstadisticas);
		
		JButton btnIntroducir = new JButton(new ImageIcon("res/importar_64.png"));
		btnIntroducir.setToolTipText(bundle.getString("insertcases"));
		GridBagConstraints gbc_btnIntroducir = new GridBagConstraints();
		gbc_btnIntroducir.fill = GridBagConstraints.BOTH;
		gbc_btnIntroducir.insets = new Insets(0, 0, 5, 5);
		gbc_btnIntroducir.gridx = 3;
		gbc_btnIntroducir.gridy = 1;
		panel_casebase.add(btnIntroducir, gbc_btnIntroducir);
		btnIntroducir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ElegirTipoCasoFrame f = new ElegirTipoCasoFrame(u, me, ElegirTipoCasoFrame.INTRODUCIR_MANUAL);
				f.setVisible(true);
				me.setEnabled(false);
			}
		});
		
		JPanel panel_4 = new JPanel();
		panel_4.setBackground(new Color(224, 255, 255));
		panel_4.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		GridBagConstraints gbc_panel_4 = new GridBagConstraints();
		gbc_panel_4.fill = GridBagConstraints.BOTH;
		gbc_panel_4.insets = new Insets(0, 0, 0, 5);
		gbc_panel_4.gridx = 1;
		gbc_panel_4.gridy = 2;
		panel_casebase.add(panel_4, gbc_panel_4);
		
		JLabel lblEstadisticas = new JLabel(bundle.getString("stats"));
		panel_4.add(lblEstadisticas);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBackground(new Color(224, 255, 255));
		panel_5.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		GridBagConstraints gbc_panel_5 = new GridBagConstraints();
		gbc_panel_5.fill = GridBagConstraints.BOTH;
		gbc_panel_5.insets = new Insets(0, 0, 0, 5);
		gbc_panel_5.gridx = 3;
		gbc_panel_5.gridy = 2;
		panel_casebase.add(panel_5, gbc_panel_5);
		
		JLabel lblInsertar = new JLabel(bundle.getString("insertcases"));
		panel_5.add(lblInsertar);
		btnEstadisticas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFrame est= new EstadisticasFrame(u, me);
				est.setVisible(true);
				me.setEnabled(false);
			}
		});
		
		//Sólo los administradores ven la pestaña de gestión
		if(! u.getTipo().equals(TipoUsuario.ADMINISTRADOR)){
			tabbedpane.remove(0);
			//Los usuarios básicos no pueden acceder al ciclo configurado.
			if(u.getTipo().equals(TipoUsuario.UBASICO)){
				btnCicloConfigurado.setVisible(false);
				panel_lblconf.setVisible(false);
			}
			
		}
		
	}
}
