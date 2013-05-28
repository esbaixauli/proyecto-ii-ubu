package vista.usuarios;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import servidorcbr.modelo.Usuario;
import vista.MainFrame;
import vista.componentesGenericos.FrameEstandar;
import vista.panels.ListaUsuarios;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import controlador.ControlUsuarios;

/**Frame que controla todas las operaciones de gestión de usuarios, permitiendo
 * seleccionarlos, crear,modificar y borrar.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
@SuppressWarnings("serial")
public class GestionUsuariosFrame extends FrameEstandar {

	/**
	 * Contentpane del frame.
	 */
	private JPanel contentPane;

	/**
	 * Mapa de usuarios. Claves:String con el nombre de usuario. Valores:Objeto usuario.
	 */
	private HashMap<String, Usuario> users = null;
	/**
	 * JList de usuarios a mostrar.
	 */
	protected ListaUsuarios list;
	
	/**
	 * Panel de ver y crear usuarios. Gestiona estas operaciones.
	 */
	private JPanel panelVerCrearUsuario;
	
	/**Crea el frame.
	 * @param padre Frame padre de este.
	 */
	public GestionUsuariosFrame(final MainFrame padre) {
		super(padre);me=this;
		setTitle(b.getString("manageusers"));
		setBounds(100, 100, 690, 464);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		obtenerDatosServidor();
		contentPane.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("233px"),
				ColumnSpec.decode("438px"),},
			new RowSpec[] {
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("394px"),}));
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, "2, 2, fill, fill");
		panel_1.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		panel_1.add(scrollPane, BorderLayout.CENTER);

		list = new ListaUsuarios(users);
		scrollPane.setViewportView(list);

		JPanel panel = new JPanel();
		panel_1.add(panel, BorderLayout.SOUTH);
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBackground(Color.GRAY);

		JButton btnVer = new JButton(new ImageIcon("res/search_32.png"));
		btnVer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (list.getSelectedValue() != null) {
					remove(panelVerCrearUsuario);
					panelVerCrearUsuario = new VerCrearUsuarioPanel(list.getSelectedValue(), padre,(GestionUsuariosFrame) me);
					contentPane.add(panelVerCrearUsuario, "3, 2, fill, fill");
					revalidate();
					repaint();
				}
			}
		});
		btnVer.setToolTipText(b.getString("seeuser"));
		panel.add(btnVer);

		JButton btnNuevo = new JButton(new ImageIcon("res/document_32.png"));
		btnNuevo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				remove(panelVerCrearUsuario);
				panelVerCrearUsuario = new VerCrearUsuarioPanel(null, padre,(GestionUsuariosFrame) me);
				contentPane.add(panelVerCrearUsuario, "3, 2, fill, fill");
				
				revalidate();
				repaint();
			}
		});
		btnNuevo.setToolTipText(b.getString("newuser"));
		panel.add(btnNuevo);

		JButton btnEliminar = new JButton(new ImageIcon("res/delete_32.png"));
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Usuario u = list.getSelectedValue();
				if (u == null) {
					JOptionPane.showMessageDialog(null,
							b.getString("noselection"),
							b.getString("noselection"),
							JOptionPane.INFORMATION_MESSAGE);
					return;
				} else if (u.getNombre().equals("root")) {
					JOptionPane.showMessageDialog(null,
							b.getString("rootdeletion"),
							b.getString("rootdeletion"),
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				int opcion = JOptionPane.showConfirmDialog(null,
						b.getString("userdelsure"), b.getString("userdelsure"),
						JOptionPane.YES_NO_OPTION);
				try {
					if (opcion == 0) {
						if (ControlUsuarios.removeUsuario(u)) {
							JOptionPane.showMessageDialog(null,
									b.getString("opsuccess"),
									b.getString("opsuccess"),
									JOptionPane.INFORMATION_MESSAGE);
								limpiarPanelVerCrearUsuario();
						} else {
							JOptionPane.showMessageDialog(null,
									b.getString("opunsuccess"),
									b.getString("opunsuccess"),
									JOptionPane.INFORMATION_MESSAGE);
						}
						users = ControlUsuarios.getUsuarios();
						list.refrescarDatos(users);
					}
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(null,
							b.getString("connecterror"), "Error",
							JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
			}
		});
		btnEliminar.setToolTipText(b.getString("deluser"));
		panel.add(btnEliminar);
		
		panelVerCrearUsuario = new VerCrearUsuarioPanel(null, padre,(GestionUsuariosFrame) me);
		contentPane.add(panelVerCrearUsuario, "3, 2, fill, fill");
		setLocationRelativeTo(padre);
	}

	/**Obtiene los datos del servidor al refrescar
	 * @return Mapa de usuarios devuelto por el servidor. Puede ser nulo si hay
	 * un error en la operación.
	 * Claves:String con el nombre de usuario. Valores:Objeto usuario.
	 */
	protected HashMap<String, Usuario> obtenerDatosServidor() {
		try {
			this.users = ControlUsuarios.getUsuarios();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, b.getString("connecterror"),
					"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			padre.setEnabled(true);
			
			me.dispose();
		}
		return users;
	}

	@Override
	public void setEnabled(boolean flag) {
		super.setEnabled(flag);
		if (flag) {
			try {
				users = ControlUsuarios.getUsuarios();
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(null,
						b.getString("connecterror"), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
			list.refrescarDatos(users);
		}
	}
	
	/**
	 * Pone en blanco el panel de ver y crear usuario.
	 */
	protected void limpiarPanelVerCrearUsuario(){
		remove(panelVerCrearUsuario);
		panelVerCrearUsuario = new JPanel();
		contentPane.add(panelVerCrearUsuario, "3, 2, fill, fill");
		revalidate();
		repaint();
	}

}
