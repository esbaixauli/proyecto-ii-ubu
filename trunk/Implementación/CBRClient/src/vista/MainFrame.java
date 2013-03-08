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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import servidorcbr.modelo.TipoUsuario;
import servidorcbr.modelo.Usuario;

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
		setBounds(100, 100, 227, 286);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, bundle.getString("mainmenu"), TitledBorder.CENTER, TitledBorder.TOP, null, null));
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JPanel panel_adm = new JPanel();
		panel_adm.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), bundle.getString("admin"), TitledBorder.CENTER, TitledBorder.TOP, null, SystemColor.textHighlight));
		panel.add(panel_adm);
		GridBagLayout gbl_panel_adm = new GridBagLayout();
		gbl_panel_adm.columnWidths = new int[]{87, 0};
		gbl_panel_adm.rowHeights = new int[]{23, 23, 23, 0};
		gbl_panel_adm.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel_adm.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_adm.setLayout(gbl_panel_adm);
		
		JButton btnGestionarTipos = new JButton(bundle.getString("managecasetypes"));
		btnGestionarTipos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				GestionTiposFrame gestion = new GestionTiposFrame(u,me);
				gestion.setVisible(true);
				me.setEnabled(false);
			}
		});
		GridBagConstraints gbc_btnGestionarTipos = new GridBagConstraints();
		gbc_btnGestionarTipos.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnGestionarTipos.insets = new Insets(0, 0, 5, 0);
		gbc_btnGestionarTipos.gridx = 0;
		gbc_btnGestionarTipos.gridy = 0;
		panel_adm.add(btnGestionarTipos, gbc_btnGestionarTipos);
		
		JButton btnGestionarusuarios = new JButton(bundle.getString("manageusers"));
		btnGestionarusuarios.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFrame f = new GestionUsuariosFrame(me);
				f.setVisible(true);
				me.setEnabled(false);
			}
		});
		GridBagConstraints gbc_btnGestionarusuarios = new GridBagConstraints();
		gbc_btnGestionarusuarios.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnGestionarusuarios.insets = new Insets(0, 0, 5, 0);
		gbc_btnGestionarusuarios.gridx = 0;
		gbc_btnGestionarusuarios.gridy = 1;
		panel_adm.add(btnGestionarusuarios, gbc_btnGestionarusuarios);
		
		JButton btnEstadisticas = new JButton(bundle.getString("stats"));
		GridBagConstraints gbc_btnEstadisticas = new GridBagConstraints();
		gbc_btnEstadisticas.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnEstadisticas.gridx = 0;
		gbc_btnEstadisticas.gridy = 2;
		panel_adm.add(btnEstadisticas, gbc_btnEstadisticas);
		
		JPanel panel_cbr = new JPanel();
		panel_cbr.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "CBR", TitledBorder.CENTER, TitledBorder.TOP, null, SystemColor.textHighlight));
		panel.add(panel_cbr);
		GridBagLayout gbl_panel_cbr = new GridBagLayout();
		gbl_panel_cbr.columnWidths = new int[]{87, 0};
		gbl_panel_cbr.rowHeights = new int[]{23, 0, 23, 0};
		gbl_panel_cbr.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel_cbr.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_cbr.setLayout(gbl_panel_cbr);
		
		JButton btnCicloConfigurado = new JButton(bundle.getString("configuredcycle"));
		btnCicloConfigurado.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ElegirTipoCasoFrame f = new ElegirTipoCasoFrame(u, me,ElegirTipoCasoFrame.CICLO_CONFIGURADO);
				f.setVisible(true);
				me.setEnabled(false);
			}
		});
		GridBagConstraints gbc_btnCicloConfigurado = new GridBagConstraints();
		gbc_btnCicloConfigurado.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnCicloConfigurado.insets = new Insets(0, 0, 5, 0);
		gbc_btnCicloConfigurado.gridx = 0;
		gbc_btnCicloConfigurado.gridy = 0;
		panel_cbr.add(btnCicloConfigurado, gbc_btnCicloConfigurado);
		
		JButton btnIntroducir = new JButton(bundle.getString("insertcases"));
		GridBagConstraints gbc_btnIntroducir = new GridBagConstraints();
		gbc_btnIntroducir.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnIntroducir.insets = new Insets(0, 0, 5, 0);
		gbc_btnIntroducir.gridx = 0;
		gbc_btnIntroducir.gridy = 1;
		panel_cbr.add(btnIntroducir, gbc_btnIntroducir);
		
		JButton btnCicloPorDefecto = new JButton(bundle.getString("defaultcycle"));
		btnCicloPorDefecto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ElegirTipoCasoFrame f = new ElegirTipoCasoFrame(u, me, ElegirTipoCasoFrame.CICLO_BASICO);
				f.setVisible(true);
				me.setEnabled(false);
			}
		});
		GridBagConstraints gbc_btnCicloPorDefecto = new GridBagConstraints();
		gbc_btnCicloPorDefecto.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnCicloPorDefecto.gridx = 0;
		gbc_btnCicloPorDefecto.gridy = 2;
		panel_cbr.add(btnCicloPorDefecto, gbc_btnCicloPorDefecto);
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{87, 0};
		gbl_panel_1.rowHeights = new int[]{23, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		JButton btnSalir = new JButton(bundle.getString("exit"));
		GridBagConstraints gbc_btnSalir = new GridBagConstraints();
		gbc_btnSalir.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSalir.anchor = GridBagConstraints.NORTH;
		gbc_btnSalir.gridx = 0;
		gbc_btnSalir.gridy = 0;
		panel_1.add(btnSalir, gbc_btnSalir);
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				me.dispose();
			}
		});
		
		if(! u.getTipo().equals(TipoUsuario.ADMINISTRADOR)){
			panel_adm.setVisible(false);
		
		}else{
			if(u.getTipo().equals(TipoUsuario.UBASICO)){
			btnCicloConfigurado.setEnabled(false);
			}
		}
		
	}

}
