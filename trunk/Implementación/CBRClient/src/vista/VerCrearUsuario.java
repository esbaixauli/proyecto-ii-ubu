package vista;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import servidorcbr.modelo.Usuario;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JTextField;
import javax.swing.JComboBox;

public class VerCrearUsuario extends JFrame {

	private JPanel contentPane;
	private JFrame me = this;
	private JTextField nombreTextField;
	private JPasswordField passwordField;
	private Usuario user;
	private ResourceBundle b = ResourceBundle.getBundle(
            "vista.internacionalizacion.Recursos", Locale.getDefault());

	/**
	 * Create the frame.
	 */
	public VerCrearUsuario(Usuario u, final JFrame padre) {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                padre.setEnabled(true);
                me.setVisible(false);
                me.dispose();
            }
        });
		setBounds(100, 100, 300, 159);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		user = u;
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		//JLabel lblNombre = new JLabel(b.getString("username");
		JLabel lblNombre = new JLabel("nombre");
		GridBagConstraints gbc_lblNombre = new GridBagConstraints();
		gbc_lblNombre.anchor = GridBagConstraints.WEST;
		gbc_lblNombre.insets = new Insets(0, 0, 5, 5);
		gbc_lblNombre.gridx = 0;
		gbc_lblNombre.gridy = 0;
		panel.add(lblNombre, gbc_lblNombre);
		
		nombreTextField = new JTextField();
		GridBagConstraints gbc_nombreTextField = new GridBagConstraints();
		gbc_nombreTextField.insets = new Insets(0, 0, 5, 0);
		gbc_nombreTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_nombreTextField.gridx = 1;
		gbc_nombreTextField.gridy = 0;
		panel.add(nombreTextField, gbc_nombreTextField);
		nombreTextField.setColumns(20);
		
		//JLabel lblPassword = new JLabel(b.getString("password");
		JLabel lblPassword = new JLabel("password");
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.anchor = GridBagConstraints.WEST;
		gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassword.gridx = 0;
		gbc_lblPassword.gridy = 1;
		panel.add(lblPassword, gbc_lblPassword);
		
		passwordField = new JPasswordField();
		GridBagConstraints gbc_passwordTextField = new GridBagConstraints();
		gbc_passwordTextField.insets = new Insets(0, 0, 5, 0);
		gbc_passwordTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordTextField.gridx = 1;
		gbc_passwordTextField.gridy = 1;
		panel.add(passwordField, gbc_passwordTextField);
		passwordField.setColumns(10);
		
		//JLabel lblTipo = new JLabel(b.getString("usertype"));
		JLabel lblTipo = new JLabel("tipo");
		GridBagConstraints gbc_lblTipo = new GridBagConstraints();
		gbc_lblTipo.anchor = GridBagConstraints.WEST;
		gbc_lblTipo.insets = new Insets(0, 0, 0, 5);
		gbc_lblTipo.gridx = 0;
		gbc_lblTipo.gridy = 2;
		panel.add(lblTipo, gbc_lblTipo);
		
		JComboBox<String> comboBox = new JComboBox<String>();
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 2;
		//comboBox.addItem(b.getString("basicuser"));
		//comboBox.addItem(b.getString("advuser"));
		//comboBox.addItem(b.getString("administrator"));
		comboBox.addItem("Usuario básico");
		comboBox.addItem("Usuario avanzado");
		comboBox.addItem("Administrador");
		panel.add(comboBox, gbc_comboBox);
		
		if (u == null) {
			//setTitle(b.getString("newuser"));
			setTitle("usuario nuevo");
		} else {
			//setTitle(b.getString("moduser"));
			setTitle("modificar usuario");
			nombreTextField.setText(u.getNombre());
			nombreTextField.setEnabled(false);
			passwordField.setText(u.getPassword());
			switch (u.getTipo()) {
			case ADMINISTRADOR:
				comboBox.setSelectedIndex(2);
				break;
			case UAVANZADO:
				comboBox.setSelectedIndex(1);
				break;
			default:
				comboBox.setSelectedIndex(0);
			}
		}
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.SOUTH);
		
		JButton btnOk = new JButton(b.getString("ok"));
		panel_1.add(btnOk);
		
		JButton btnCancel = new JButton(b.getString("cancel"));
		panel_1.add(btnCancel);
	}

}
