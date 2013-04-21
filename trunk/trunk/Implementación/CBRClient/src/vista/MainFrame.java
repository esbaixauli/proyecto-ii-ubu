package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import servidorcbr.modelo.TipoUsuario;
import servidorcbr.modelo.Usuario;
import vista.estadisticas.EstadisticasFrame;
import java.awt.Font;
import java.awt.GridLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.FlowLayout;

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
		setBounds(100, 100, 468, 210);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedpane = new JTabbedPane(JTabbedPane.TOP);
		tabbedpane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		getContentPane().add(tabbedpane, BorderLayout.CENTER);

		
		JPanel panel_adm = new JPanel();
		tabbedpane.addTab( bundle.getString("admin"), panel_adm);
		panel_adm.setLayout(new GridLayout(0, 2, 0, 0));
		
		JPanel panelGestionarTipos = new JPanel();
		panel_adm.add(panelGestionarTipos);
		panelGestionarTipos.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("228px"),},
			new RowSpec[] {
				RowSpec.decode("100px"),
				RowSpec.decode("50px"),}));
		
		JButton btnGestionarTipos = new JButton((new ImageIcon("res/gear_64.png")));
		btnGestionarTipos.setPreferredSize(new Dimension(150, btnGestionarTipos.getPreferredSize().height));
		panelGestionarTipos.add(btnGestionarTipos, "1, 1, center, bottom");
		btnGestionarTipos.setToolTipText(bundle.getString("managecasetypes"));
		
		JPanel panel = new JPanel();
		panelGestionarTipos.add(panel, "1, 2, center, center");
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBackground(Color.GRAY);
		
		JLabel lblManage = new JLabel(bundle.getString("managecasetypes"));
		lblManage.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblManage.setForeground(Color.WHITE);
		panel.add(lblManage);
		lblManage.setHorizontalAlignment(SwingConstants.CENTER);
		btnGestionarTipos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				GestionTiposFrame gestion = new GestionTiposFrame(u,me);
				gestion.setVisible(true);
				me.setEnabled(false);
			}
		});
		
		JPanel panelUsuarios = new JPanel();
		panel_adm.add(panelUsuarios);
		panelUsuarios.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("228px"),},
			new RowSpec[] {
				RowSpec.decode("100px"),
				RowSpec.decode("50px"),}));
		
		JButton btnGestionarusuarios = new JButton((new ImageIcon("res/user_64.png")));
		btnGestionarusuarios.setPreferredSize(new Dimension(150, btnGestionarusuarios.getPreferredSize().height));
		panelUsuarios.add(btnGestionarusuarios, "1, 1, center, bottom");
		btnGestionarusuarios.setToolTipText(bundle.getString("manageusers"));
		
		JPanel panel_1 = new JPanel();
		panelUsuarios.add(panel_1, "1, 2, center, center");
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.setBackground(Color.GRAY);
		
		JLabel lblMngusers = new JLabel(bundle.getString("manageusers"));
		lblMngusers.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblMngusers.setForeground(Color.WHITE);
		panel_1.add(lblMngusers);
		
		btnGestionarusuarios.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFrame f = new GestionUsuariosFrame(me);
				f.setVisible(true);
				me.setEnabled(false);
			}
		});
		
		JPanel panel_cbr = new JPanel();
		tabbedpane.addTab("CBR",panel_cbr);
		panel_cbr.setLayout(new GridLayout(0, 2, 0, 0));
		
		JPanel panelCicloCo = new JPanel();
		panel_cbr.add(panelCicloCo);
		panelCicloCo.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("228px"),},
			new RowSpec[] {
				RowSpec.decode("100px"),
				RowSpec.decode("50px"),}));
		
		JButton btnCicloConfigurado = new JButton(new ImageIcon("res/cbr_configurado_64.png"));
		btnCicloConfigurado.setPreferredSize(new Dimension(150, btnCicloConfigurado.getPreferredSize().height));
		panelCicloCo.add(btnCicloConfigurado, "1, 1, center, bottom");
		btnCicloConfigurado.setToolTipText(bundle.getString("configuredcycle"));
		
		JPanel panel_lblconf = new JPanel();
		panelCicloCo.add(panel_lblconf, "1, 2, center, center");
		panel_lblconf.setBackground(Color.GRAY);
		panel_lblconf.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		JLabel lblCbrco = new JLabel(bundle.getString("configuredcycle"));
		lblCbrco.setForeground(Color.WHITE);
		lblCbrco.setFont(new Font("Tahoma", Font.ITALIC, 11));
		panel_lblconf.add(lblCbrco);
		btnCicloConfigurado.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ElegirTipoCasoFrame f = new ElegirTipoCasoFrame(u, me,ElegirTipoCasoFrame.CICLO_CONFIGURADO);
				f.setVisible(true);
				me.setEnabled(false);
			}
		});
	
		
		
		JPanel panelCicloBas = new JPanel();
		panel_cbr.add(panelCicloBas);
		
		String spec = "100px";
		//TODO
				if(u.getTipo().equals(TipoUsuario.UBASICO)){
					panel_cbr.setLayout(new FlowLayout());
					panel_cbr.removeAll();
					panel_cbr.add(panelCicloBas);
					spec ="90px";
				}
				
				panelCicloBas.setLayout(new FormLayout(new ColumnSpec[] {
						ColumnSpec.decode("228px"),},
					new RowSpec[] {
						RowSpec.decode(spec),
						RowSpec.decode("50px"),}));
		
		JButton btnCicloPorDefecto = new JButton(new ImageIcon("res/cbr_basico_64.png"));
		btnCicloPorDefecto.setPreferredSize(new Dimension(150, btnCicloPorDefecto.getPreferredSize().height));
		panelCicloBas.add(btnCicloPorDefecto, "1, 1, center, bottom");
		btnCicloPorDefecto.setToolTipText(bundle.getString("defaultcycle"));
		btnCicloPorDefecto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ElegirTipoCasoFrame f = new ElegirTipoCasoFrame(u, me, ElegirTipoCasoFrame.CICLO_BASICO);
				f.setVisible(true);
				me.setEnabled(false);
			}
		});
		
		JPanel panel_cbrpordefecto = new JPanel();
		panelCicloBas.add(panel_cbrpordefecto, "1, 2, center, center");
		panel_cbrpordefecto.setBackground(Color.GRAY);
		panel_cbrpordefecto.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		JLabel lblCbr = new JLabel(bundle.getString("defaultcycle"));
		lblCbr.setForeground(Color.WHITE);
		lblCbr.setFont(new Font("Tahoma", Font.ITALIC, 11));
		panel_cbrpordefecto.add(lblCbr);
		
		JPanel panel_casebase = new JPanel();
		tabbedpane.addTab(bundle.getString("casebases"),panel_casebase);
		panel_casebase.setLayout(new GridLayout(0, 2, 0, 0));
		
		JPanel panelEst = new JPanel();
		panel_casebase.add(panelEst);
		panelEst.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("228px"),},
			new RowSpec[] {
				RowSpec.decode("100px"),
				RowSpec.decode("50px"),}));
		
		JButton btnEstadisticas = new JButton(new ImageIcon("res/statistics_64.png"));
		btnEstadisticas.setPreferredSize(new Dimension(150, btnEstadisticas.getPreferredSize().height));
		panelEst.add(btnEstadisticas, "1, 1, center, bottom");
		btnEstadisticas.setToolTipText(bundle.getString("stats"));
		
		JPanel panel_4 = new JPanel();
		panelEst.add(panel_4, "1, 2, center, center");
		panel_4.setBackground(Color.GRAY);
		panel_4.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		JLabel lblEstadisticas = new JLabel(bundle.getString("stats"));
		lblEstadisticas.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblEstadisticas.setForeground(Color.WHITE);
		panel_4.add(lblEstadisticas);
		btnEstadisticas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFrame est= new EstadisticasFrame(u, me);
				est.setVisible(true);
				me.setEnabled(false);
			}
		});
		
		JPanel panelIntro = new JPanel();
		panel_casebase.add(panelIntro);
		panelIntro.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("228px"),},
			new RowSpec[] {
				RowSpec.decode("100px"),
				RowSpec.decode("50px"),}));
		
		JButton btnIntroducir = new JButton(new ImageIcon("res/importar_64.png"));
		btnIntroducir.setPreferredSize(new Dimension(150, btnIntroducir.getPreferredSize().height));
		panelIntro.add(btnIntroducir, "1, 1, center, bottom");
		btnIntroducir.setToolTipText(bundle.getString("insertcases"));
		
		JPanel panel_5 = new JPanel();
		panelIntro.add(panel_5, "1, 2, center, center");
		panel_5.setBackground(Color.GRAY);
		panel_5.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		JLabel lblInsertar = new JLabel(bundle.getString("insertcases"));
		lblInsertar.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblInsertar.setForeground(Color.WHITE);
		panel_5.add(lblInsertar);
		btnIntroducir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ElegirTipoCasoFrame f = new ElegirTipoCasoFrame(u, me, ElegirTipoCasoFrame.INTRODUCIR_MANUAL);
				f.setVisible(true);
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
		centrar();
	}
	
	/**
	 * Centra la ventana en pantalla.
	 */
	private void centrar() {
	    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (int) ((dimension.getWidth() - getWidth()) / 2);
	    int y = (int) ((dimension.getHeight() - getHeight()) / 2);
	    setLocation(x, y);
	}
}
