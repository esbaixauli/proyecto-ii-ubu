package vista;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import servidorcbr.modelo.Usuario;
import vista.panels.ListaUsuarios;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JList;

import controlador.ControlTipos;
import controlador.ControlUsuarios;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GestionUsuariosFrame extends JFrame {

	private JPanel contentPane;
	private JFrame me = this;
	private JFrame padre = null;
	private HashMap<String, Usuario> users = null;
	private ResourceBundle b = ResourceBundle.getBundle(
			"vista.internacionalizacion.Recursos", Locale.getDefault());
	private ListaUsuarios list;

	/**
	 * Create the frame.
	 */
	public GestionUsuariosFrame(final JFrame padre) {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				padre.setEnabled(true);
				me.setVisible(false);
				me.dispose();
			}
		});
		setBounds(100, 100, 314, 314);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		this.padre = padre;
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
		contentPane.add(panel, BorderLayout.SOUTH);

		JButton btnVer = new JButton(new ImageIcon("res/search_32.png"));
		btnVer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (list.getSelectedValue() != null) {
					JFrame f = new VerCrearUsuario(list.getSelectedValue(), me);
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
				JFrame f = new VerCrearUsuario(null, me);
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
						b.getString("userdelsure"), b.getString("delsure"),
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
