package vista.usuarios;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.TipoUsuario;
import servidorcbr.modelo.Usuario;
import vista.MainFrame;
import vista.componentesGenericos.PanelEstandar;
import controlador.ControlTipos;
import controlador.ControlUsuarios;

/** Panel de ver y crear usuarios. Gestiona estas operaciones.
 * Esta clase se encarga de mostrar los desplegables, campos de texto, etc. necesarios
 * para crear un usuario o modificar uno ya existente. También permite asociar los 
 * tipos de caso de un usuario.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
@SuppressWarnings("serial")
public class VerCrearUsuarioPanel extends PanelEstandar {

	/**
	 * Contentpane del panel.
	 */
	private JPanel contentPane;
	/**
	 * Campo de texto con el nombre del usuario a añadir/modificar.
	 */
	private JTextField nombreTextField;
	/**
	 * Campo de texto cifrado con la contraseña del usuario a añadir/modificar.
	 */
	private JPasswordField passwordField;
	/**
	 * Usuario que se está creando/modificando.
	 */
	private Usuario user;

	
	/**
	 * Asociaciones de este usuario con cada tipo de caso. Se presentan en un mapa de
	 * claves: 'Nombre del tipo de caso' y valores:Checkbox para marcar si el usuario
	 * puede acceder a esta técnica o no.
	 */
	private HashMap<String,JCheckBox> asociaciones = new HashMap<String,JCheckBox>();

	/**Crea el frame.
	 * @param u Usuario a añadir o modificar. Null si se desea añadir un usuario nuevo,
	 * o un objeto Usuario correspondiente al usuario a modificar.
	 * @param padre Frame padre de este (Ventana principal).
	 * @param frameUsuarios Frame al que pertenece este panel (Frame de usuarios).
	 */
	public VerCrearUsuarioPanel(Usuario u, final MainFrame padre,final GestionUsuariosFrame frameUsuarios) {
		super(padre);
		String title;
		if(u==null){//si estoy creando un usuario
			title = b.getString("newuser");
		}else{//si estoy modificando
			title = b.getString("moduser");
		}

		setBorder(new TitledBorder(null, title, TitledBorder.CENTER, TitledBorder.TOP, null, null));
		setSize(320,290);
		setBounds(100, 100, 300, 290);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		add(contentPane);
		
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
		if (u != null) {

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
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.setBackground(Color.GRAY);
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
						}else{
							frameUsuarios.list.refrescarDatos(frameUsuarios.obtenerDatosServidor());
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
						frameUsuarios.limpiarPanelVerCrearUsuario();
						
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
				frameUsuarios.limpiarPanelVerCrearUsuario();
			}
		});
		panel_1.add(btnCancel);
		
	}
	
	/**Devuelve el usuario de este frame.
	 * @return el usuario correspondiente al frame.
	 */
	public Usuario getUser() {
		return user;
	}

}
