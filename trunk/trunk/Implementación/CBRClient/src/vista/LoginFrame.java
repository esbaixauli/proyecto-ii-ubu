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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import servidorcbr.modelo.Usuario;


import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Window.Type;

public class LoginFrame extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldNombre;
	private JTextField textFieldPass;
	ResourceBundle bundle = ResourceBundle.getBundle(
            "vista.internacionalizacion.Recursos", getLocale());
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

	/**
	 * Create the frame.
	 */
	public LoginFrame() {
		
		
		setResizable(false);
		setTitle(bundle.getString("login"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 242, 204);
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
		
		textFieldPass = new JTextField();
		contentPane.add(textFieldPass, "4, 8, fill, default");
		textFieldPass.setColumns(10);
		
		final JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Español", "English"}));
		contentPane.add(comboBox, "2, 10, 3, 1, fill, default");
		
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
				}
				enviarServlet(textFieldNombre.getText(),textFieldPass.getText());
			
			}
			
			private URLConnection getServletCon() throws MalformedURLException,
		      IOException {
		    URL urlServlet = new URL("http://localhost/CBRServer/ServletLogin");
		    URLConnection con = urlServlet.openConnection();
		    con.setDoInput(true);
		    con.setDoOutput(true);
		    con.setUseCaches(false);
		    con.setRequestProperty("Content-Type",
		        "application/x-java-serialized-object");
		    return con;
		  }

			
			private void enviarServlet(String nombre, String password){
				try{
					URLConnection con = getServletCon();
					 OutputStream outputStream = con.getOutputStream();
				     ObjectOutputStream oos = new ObjectOutputStream(outputStream);
				     oos.writeObject(nombre);
				     oos.writeObject(password);
				     oos.flush();
				     oos.close();
				     InputStream inputStream = con.getInputStream();
				      ObjectInputStream inputDelServlet = new ObjectInputStream(
				          inputStream);
				      Usuario resultado = (Usuario) inputDelServlet.readObject();
				      System.out.println(resultado.toString());
				      inputDelServlet.close();
				      inputStream.close();
				
				}catch(Exception ex){
					JOptionPane.showMessageDialog(null, 
					bundle.getString("connecterror"),"Error", JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
				
			}
		});
		
		contentPane.add(btnAcceso, "2, 12, 3, 1");
	}

}
