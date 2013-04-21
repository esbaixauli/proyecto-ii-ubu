package vista;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import servidorcbr.modelo.Usuario;
import vista.componentes.FrameEstandar;
import vista.panels.ListaUsuarios;
import controlador.ControlUsuarios;
import java.awt.Color;
import javax.swing.border.EtchedBorder;

@SuppressWarnings("serial")
public class GestionUsuariosFrame extends FrameEstandar {

	private JPanel contentPane;

	private HashMap<String, Usuario> users = null;
	private ListaUsuarios list;

	/**
	 * Create the frame.
	 */
	public GestionUsuariosFrame(final JFrame padre) {
		super(padre);me=this;
		setTitle(b.getString("manageusers"));
		setBounds(100, 100, 314, 314);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		try {
			this.users = ControlUsuarios.getUsuarios();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, b.getString("connecterror"),
					"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			JFrame login = new LoginFrame();
			login.setVisible(true);
			padre.dispose();
			me.dispose();
		}

		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);

		list = new ListaUsuarios(users);
		scrollPane.setViewportView(list);

		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBackground(Color.GRAY);
		contentPane.add(panel, BorderLayout.SOUTH);

		JButton btnVer = new JButton(new ImageIcon("res/search_32.png"));
		btnVer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (list.getSelectedValue() != null) {
					JFrame f = new VerCrearUsuarioFrame(list.getSelectedValue(), me);
					f.setVisible(true);
					me.setEnabled(false);
				}
			}
		});
		btnVer.setToolTipText(b.getString("seeuser"));
		panel.add(btnVer);

		JButton btnNuevo = new JButton(new ImageIcon("res/document_32.png"));
		btnNuevo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame f = new VerCrearUsuarioFrame(null, me);
				f.setVisible(true);
				me.setEnabled(false);
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
		setLocationRelativeTo(padre);
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

}
