package vista;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.TipoUsuario;
import servidorcbr.modelo.Usuario;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JTextField;
import javax.swing.JComboBox;

import controlador.ControlTipos;
import controlador.ControlUsuarios;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.peer.TextFieldPeer;
import java.io.IOException;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class VerCrearUsuario extends JFrame {

	private JPanel contentPane;
	private JFrame me = this;
	private JTextField nombreTextField;
	private JPasswordField passwordField;
	private Usuario user;
	private ResourceBundle b = ResourceBundle.getBundle(
            "vista.internacionalizacion.Recursos", Locale.getDefault());

	private HashMap<String,JCheckBox> asociaciones = new HashMap<String,JCheckBox>();
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
		setBounds(100, 100, 300, 285);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		user = u;
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel lblNombre = new JLabel(b.getString("name"));
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
		
		JLabel lblPassword = new JLabel(b.getString("password"));
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
		
		JLabel lblTipo = new JLabel(b.getString("usertype"));
		GridBagConstraints gbc_lblTipo = new GridBagConstraints();
		gbc_lblTipo.anchor = GridBagConstraints.WEST;
		gbc_lblTipo.insets = new Insets(0, 0, 5, 5);
		gbc_lblTipo.gridx = 0;
		gbc_lblTipo.gridy = 2;
		panel.add(lblTipo, gbc_lblTipo);
		
		final JComboBox<String> comboBox = new JComboBox<String>();
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 2;
		comboBox.addItem(b.getString("basicuser"));
		comboBox.addItem(b.getString("advuser"));
		comboBox.addItem(b.getString("administrator"));
		panel.add(comboBox, gbc_comboBox);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 2;
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 3;
		panel.add(scrollPane, gbc_scrollPane);
		
		JPanel panelTipos = new JPanel();
		panelTipos.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), b.getString("managecasetypes"), TitledBorder.CENTER, TitledBorder.TOP, null, null));
		scrollPane.setViewportView(panelTipos);
		panelTipos.setLayout(new BoxLayout(panelTipos, BoxLayout.Y_AXIS));
	
		setIconImage(new ImageIcon("res/logocbr.png").getImage());
		try{
			
			for(TipoCaso tc: ControlTipos.obtenerTiposCaso(null)){
				JCheckBox jcb = new JCheckBox(tc.getNombre());
				panelTipos.add(jcb);
				asociaciones.put(tc.getNombre(),jcb);
			}
			//Si estoy modificando el usuario, que muestre los casos que tenia asociados antes.
			if(u!=null){
				for(TipoCaso tc: ControlTipos.obtenerTiposCaso(u)){
					if(asociaciones.containsKey(tc.getNombre())){
						asociaciones.get(tc.getNombre()).setSelected(true);
					}
				}
				
			}
		}catch(IOException ex){
			panelTipos.add(new JLabel(b.getString("connecterror")));
		}
		if (u == null) {
			setTitle(b.getString("newuser"));
		} else {
			
			setTitle(b.getString("moduser"));
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
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean exito=false;
				List<String> casosEscogidos = new ArrayList<String>();
				for(JCheckBox jcbx :asociaciones.values()){
					if(jcbx.isSelected()){
						casosEscogidos.add(jcbx.getText());
					}
				}
				try {
					String password = new String(passwordField.getPassword());
					password=password.substring(0,Math.min(password.length(), 20));
					String username= nombreTextField.getText().trim().replaceAll("\\s","");
					username = username.substring(0, Math.min(username.length(),20));
					if (username == null || username.isEmpty()
							|| password.isEmpty()) {
						throw new NullPointerException();
					}
					if (user == null) {
						// crear usuario nuevo
						
						user = new Usuario();
						user.setNombre(username);
						
						user.setPassword(password);
						switch (comboBox.getSelectedIndex()) {
						case 0:
							user.setTipo(TipoUsuario.UBASICO);
							break;
						case 1:
							user.setTipo(TipoUsuario.UAVANZADO);
							break;
						default:
							user.setTipo(TipoUsuario.ADMINISTRADOR);
							break;
						}
						
						exito=ControlUsuarios.newUsuario(user,casosEscogidos);
						if(!exito){
							JOptionPane.showMessageDialog(null,
									b.getString("operror"), "Error",
									JOptionPane.ERROR_MESSAGE);
							exito=false;
							user=null;
						}
					} else {
						// modificar usuario existente
						user.setPassword(password);
						switch (comboBox.getSelectedIndex()) {
						case 0:
							user.setTipo(TipoUsuario.UBASICO);
							break;
						case 1:
							user.setTipo(TipoUsuario.UAVANZADO);
							break;
						default:
							user.setTipo(TipoUsuario.ADMINISTRADOR);
							break;
						}
						exito= ControlUsuarios.modUsuario(user,casosEscogidos);
						if(!exito){
							JOptionPane.showMessageDialog(null,
									b.getString("moderror"), "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					}
					if(exito){
						padre.setEnabled(true);
					me.setVisible(false);
					me.dispose();
					}
				} catch (NullPointerException ex) {
					JOptionPane.showMessageDialog(null,
							b.getString("emptyfield"), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		panel_1.add(btnOk);
		
		JButton btnCancel = new JButton(b.getString("cancel"));
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				padre.setEnabled(true);
				me.setVisible(false);
				me.dispose();
			}
		});
		panel_1.add(btnCancel);
	}

}
