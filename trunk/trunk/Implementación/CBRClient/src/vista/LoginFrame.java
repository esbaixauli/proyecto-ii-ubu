package vista;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import servidorcbr.modelo.Usuario;
import controlador.ControlConexion;
import controlador.ControlLogin;

@SuppressWarnings("serial")
public class LoginFrame extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldNombre;
	private JTextField textFieldPass;
	ResourceBundle bundle = ResourceBundle.getBundle(
			"vista.internacionalizacion.Recursos", getLocale());
	private JTextField ipTextField;
	private JTextField puertoTextField;
	private JComboBox<String> comboBox;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					try {
					
						UIManager
								.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");

					} catch (UnsupportedLookAndFeelException e) {
						UIManager.setLookAndFeel(UIManager
								.getCrossPlatformLookAndFeelClassName());
					} catch (ClassNotFoundException ex) {
						UIManager.setLookAndFeel(UIManager
								.getCrossPlatformLookAndFeelClassName());
					}
					LoginFrame frame = new LoginFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private final LoginFrame me = this;
	private JLabel lblImagen;

	/**
	 * Create the frame.
	 */
	public LoginFrame() {

		setResizable(false);
		setTitle(bundle.getString("login"));
		setIconImage(new ImageIcon("res/logocbr.png").getImage());

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 447, 258);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 39, 178, 35, 55, 96, 53, 0 };
		gbl_contentPane.rowHeights = new int[] { 20, 20, 20, 20, 0, 14, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 2;
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.gridheight = 6;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 0;
		contentPane.add(panel, gbc_panel);

		lblImagen = new JLabel(new ImageIcon("res/logocbr.png"));
		panel.add(lblImagen);

		JButton btnAcceso = new JButton(bundle.getString("login"));

		btnAcceso.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (comboBox.getSelectedIndex() == 0) {
					Locale.setDefault(new Locale("es", "ES"));
				} else {
					Locale.setDefault(Locale.US);
				}
				if (textFieldNombre.getText().isEmpty()
						|| textFieldPass.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null,
							bundle.getString("emptylogin"), "Error",
							JOptionPane.ERROR_MESSAGE);
				} else {

					ControlConexion.setCon(ipTextField.getText().trim(),
							puertoTextField.getText().trim());

					Usuario u = enviarServlet(textFieldNombre.getText(),
							textFieldPass.getText());
					if (u != null) {
						JFrame main = new MainFrame(u);
						main.setVisible(true);
						me.dispose();
					}
				}
			}

			private Usuario enviarServlet(String nombre, String password) {
				Usuario u = null;
				try {
					u = ControlLogin.validarLogin(nombre, password);
					if (u == null) {
						JOptionPane.showMessageDialog(null,
								bundle.getString("usernotfound"), "Error",
								JOptionPane.ERROR_MESSAGE);

					}

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null,
							bundle.getString("connecterror"), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
				return u;
			}
		});

		JLabel lblNombre = new JLabel(bundle.getString("name"));
		GridBagConstraints gbc_lblNombre = new GridBagConstraints();
		gbc_lblNombre.anchor = GridBagConstraints.NORTHEAST;
		gbc_lblNombre.insets = new Insets(0, 0, 5, 5);
		gbc_lblNombre.gridx = 3;
		gbc_lblNombre.gridy = 2;
		contentPane.add(lblNombre, gbc_lblNombre);

		textFieldNombre = new JTextField();
		GridBagConstraints gbc_textFieldNombre = new GridBagConstraints();
		gbc_textFieldNombre.anchor = GridBagConstraints.NORTH;
		gbc_textFieldNombre.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldNombre.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldNombre.gridx = 4;
		gbc_textFieldNombre.gridy = 2;
		contentPane.add(textFieldNombre, gbc_textFieldNombre);
		textFieldNombre.setColumns(10);

		JLabel lblContrasea = new JLabel(bundle.getString("password"));
		GridBagConstraints gbc_lblContrasea = new GridBagConstraints();
		gbc_lblContrasea.anchor = GridBagConstraints.NORTHEAST;
		gbc_lblContrasea.insets = new Insets(0, 0, 5, 5);
		gbc_lblContrasea.gridx = 3;
		gbc_lblContrasea.gridy = 3;
		contentPane.add(lblContrasea, gbc_lblContrasea);

		textFieldPass = new JPasswordField();
		GridBagConstraints gbc_textFieldPass = new GridBagConstraints();
		gbc_textFieldPass.anchor = GridBagConstraints.NORTH;
		gbc_textFieldPass.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldPass.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldPass.gridx = 4;
		gbc_textFieldPass.gridy = 3;
		contentPane.add(textFieldPass, gbc_textFieldPass);
		textFieldPass.setColumns(10);

		JLabel lblIp = new JLabel("IP:");
		GridBagConstraints gbc_lblIp = new GridBagConstraints();
		gbc_lblIp.anchor = GridBagConstraints.NORTHEAST;
		gbc_lblIp.insets = new Insets(0, 0, 5, 5);
		gbc_lblIp.gridx = 3;
		gbc_lblIp.gridy = 4;
		contentPane.add(lblIp, gbc_lblIp);

		ipTextField = new JTextField();
		ipTextField.setText("localhost");
		GridBagConstraints gbc_ipTextField = new GridBagConstraints();
		gbc_ipTextField.anchor = GridBagConstraints.NORTH;
		gbc_ipTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_ipTextField.insets = new Insets(0, 0, 5, 5);
		gbc_ipTextField.gridx = 4;
		gbc_ipTextField.gridy = 4;
		contentPane.add(ipTextField, gbc_ipTextField);
		ipTextField.setColumns(10);

		JLabel lblPuerto = new JLabel(bundle.getString("port"));
		GridBagConstraints gbc_lblPuerto = new GridBagConstraints();
		gbc_lblPuerto.anchor = GridBagConstraints.NORTHEAST;
		gbc_lblPuerto.insets = new Insets(0, 0, 5, 5);
		gbc_lblPuerto.gridx = 3;
		gbc_lblPuerto.gridy = 5;
		contentPane.add(lblPuerto, gbc_lblPuerto);

		puertoTextField = new JTextField();
		puertoTextField.setText("8080");
		GridBagConstraints gbc_puertoTextField = new GridBagConstraints();
		gbc_puertoTextField.anchor = GridBagConstraints.NORTH;
		gbc_puertoTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_puertoTextField.insets = new Insets(0, 0, 5, 5);
		gbc_puertoTextField.gridx = 4;
		gbc_puertoTextField.gridy = 5;
		contentPane.add(puertoTextField, gbc_puertoTextField);
		puertoTextField.setColumns(10);

		comboBox = new JComboBox<String>();

		comboBox.setModel(new DefaultComboBoxModel<String>(new String[] { "Español",
				"English" }));
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 6;
		contentPane.add(comboBox, gbc_comboBox);

		me.getRootPane().setDefaultButton(btnAcceso);
		GridBagConstraints gbc_btnAcceso = new GridBagConstraints();
		gbc_btnAcceso.insets = new Insets(0, 0, 5, 5);
		gbc_btnAcceso.anchor = GridBagConstraints.NORTH;
		gbc_btnAcceso.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnAcceso.gridwidth = 2;
		gbc_btnAcceso.gridx = 3;
		gbc_btnAcceso.gridy = 6;
		contentPane.add(btnAcceso, gbc_btnAcceso);
	}

}
