package vista;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import controlador.ControlConexion;
import controlador.ControlLogin;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import servidorcbr.modelo.Usuario;


import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Window.Type;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class LoginFrame extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldNombre;
	private JTextField textFieldPass;
	ResourceBundle bundle = ResourceBundle.getBundle(
            "vista.internacionalizacion.Recursos", getLocale());
	private JTextField ipTextField;
	private JTextField puertoTextField;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginFrame frame = new LoginFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private final LoginFrame me = this;
	/**
	 * Create the frame.
	 */
	public LoginFrame() {
		
	
		setResizable(false);
		setTitle(bundle.getString("login"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 243, 270);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
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
		
		JLabel lblNombre = new JLabel(bundle.getString("name"));
		contentPane.add(lblNombre, "2, 6, right, default");
		
		textFieldNombre = new JTextField();
		contentPane.add(textFieldNombre, "4, 6, fill, default");
		textFieldNombre.setColumns(10);
		
		JLabel lblContrasea = new JLabel(bundle.getString("password"));
		contentPane.add(lblContrasea, "2, 8, right, default");
		
		textFieldPass = new JPasswordField();
		contentPane.add(textFieldPass, "4, 8, fill, default");
		textFieldPass.setColumns(10);
		
		final JComboBox comboBox = new JComboBox();
		
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Español", "English"}));
		contentPane.add(comboBox, "2, 10, 3, 1, fill, default");
		
		JLabel lblIp = new JLabel("IP:");
		contentPane.add(lblIp, "2, 12, right, default");
		
		ipTextField = new JTextField();
		ipTextField.setText("localhost");
		contentPane.add(ipTextField, "4, 12, fill, default");
		ipTextField.setColumns(10);
		
		JButton btnAcceso = new JButton(bundle.getString("login"));
		
		
		btnAcceso.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(comboBox.getSelectedIndex() == 0){
					Locale.setDefault(new Locale("es", "ES"));
				}else{
					Locale.setDefault(Locale.US);
				}
				if(textFieldNombre.getText().isEmpty() || textFieldPass.getText().isEmpty()){
					JOptionPane.showMessageDialog(null, 
							bundle.getString("emptylogin"),"Error", JOptionPane.ERROR_MESSAGE);
				}else{
					ControlConexion.setCon(ipTextField.getText().trim(), puertoTextField.getText().trim());
					Usuario u =enviarServlet(textFieldNombre.getText(),textFieldPass.getText());
					if(u!=null){
						JFrame main = new MainFrame(u);
						main.setVisible(true);
						me.dispose();
					}
				}
			}
		
			private Usuario enviarServlet(String nombre, String password){
				Usuario u=null;
				try{
					 u= ControlLogin.validarLogin(nombre, password);
				    if(u==null){
				    	JOptionPane.showMessageDialog(null, 
								bundle.getString("usernotfound"),"Error", JOptionPane.ERROR_MESSAGE);
								
				    }
				
				}catch(Exception ex){
					JOptionPane.showMessageDialog(null, 
					bundle.getString("connecterror"),"Error", JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
			    return u;
			}
		});
		
		me.getRootPane().setDefaultButton(btnAcceso);
		
		JLabel lblPuerto = new JLabel(bundle.getString("port"));
		contentPane.add(lblPuerto, "2, 14, right, default");
		
		puertoTextField = new JTextField();
		puertoTextField.setText("8080");
		contentPane.add(puertoTextField, "4, 14, left, default");
		puertoTextField.setColumns(10);
		
		contentPane.add(btnAcceso, "2, 16, 3, 1");
	}

}
