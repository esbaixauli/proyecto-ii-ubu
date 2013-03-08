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

import controlador.ControlUsuarios;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GestionUsuariosFrame extends JFrame {

	private JPanel contentPane;
	private JFrame me = this;
	private JFrame padre = null;
	private HashMap<String,Usuario> users = null;
	private ResourceBundle b = ResourceBundle.getBundle(
            "vista.internacionalizacion.Recursos", Locale.getDefault());

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
			JOptionPane.showMessageDialog(null, 
					b.getString("connecterror"),"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			JFrame login = new LoginFrame();
			login.setVisible(true);
			padre.dispose();
			me.dispose();
		}
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		final ListaUsuarios list = new ListaUsuarios(users);
		scrollPane.setViewportView(list);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		
		JButton btnVer = new JButton(new ImageIcon("res/search_32.png"));
		btnVer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nombre = list.getSelectedValue().getNombre();
				JFrame f = new VerCrearUsuario(users.get(nombre), me);
				f.setVisible(true);
				me.setEnabled(false);
			}
		});
		//btnVer.setToolTipText(b.getString("seeuser"));
		btnVer.setToolTipText("ver usuario");
		panel.add(btnVer);
		
		JButton btnNuevo = new JButton(new ImageIcon("res/document_32.png"));
		btnNuevo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame f = new VerCrearUsuario(null, me);
				f.setVisible(true);
				me.setEnabled(false);
			}
		});
		//btnNuevo.setToolTipText(b.getString("newuser"));
		btnNuevo.setToolTipText("usuario nuevo");
		panel.add(btnNuevo);
		
		JButton btnEliminar = new JButton(new ImageIcon("res/delete_32.png"));
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		//btnEliminar.setToolTipText(b.getString("deluser"));
		btnEliminar.setToolTipText("borrar usuario");
		panel.add(btnEliminar);
	}

}
