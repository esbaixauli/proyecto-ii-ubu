package vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import servidorcbr.modelo.TipoUsuario;
import servidorcbr.modelo.Usuario;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import net.miginfocom.swing.MigLayout;
import java.awt.BorderLayout;
import javax.swing.UIManager;
import java.awt.Color;

public class MainFrame extends JFrame{

	
	private final MainFrame me = this;
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
		setResizable(false);
		setBounds(100, 100, 207, 269);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, bundle.getString("mainmenu"), TitledBorder.CENTER, TitledBorder.TOP, null, null));
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JPanel panel_adm = new JPanel();
		panel_adm.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Administraci\u00F3n", TitledBorder.CENTER, TitledBorder.TOP, null, UIManager.getColor("ProgressBar.selectionBackground")));
		panel.add(panel_adm);
		panel_adm.setLayout(new BoxLayout(panel_adm, BoxLayout.Y_AXIS));
		
		JButton btnGestionarTipos = new JButton(bundle.getString("managecasetypes"));
		btnGestionarTipos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				GestionTiposFrame gestion = new GestionTiposFrame(u,me);
				gestion.setVisible(true);
				me.setEnabled(false);
			}
		});
		panel_adm.add(btnGestionarTipos);
		
		JButton btnGestionarusuarios = new JButton(bundle.getString("manageusers"));
		panel_adm.add(btnGestionarusuarios);
		
		JButton btnEstadisticas = new JButton(bundle.getString("stats"));
		panel_adm.add(btnEstadisticas);
		
		JPanel panel_cbr = new JPanel();
		panel_cbr.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "CBR", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(51, 153, 255)));
		panel.add(panel_cbr);
		panel_cbr.setLayout(new BoxLayout(panel_cbr, BoxLayout.Y_AXIS));
		
		JButton btnCicloConfigurado = new JButton(bundle.getString("configuredcycle"));
		panel_cbr.add(btnCicloConfigurado);
		
		JButton btnCicloPorDefecto = new JButton(bundle.getString("defaultcycle"));
		panel_cbr.add(btnCicloPorDefecto);
		
		JButton btnSalir = new JButton(bundle.getString("exit"));
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				me.dispose();
			}
		});
		panel.add(btnSalir);
		
		if(! u.getTipo().equals(TipoUsuario.ADMINISTRADOR)){
			panel_adm.setVisible(false);
		
		}else{
			if(u.getTipo().equals(TipoUsuario.UBASICO)){
			btnCicloConfigurado.setEnabled(false);
			}
		}
		
	}

}
