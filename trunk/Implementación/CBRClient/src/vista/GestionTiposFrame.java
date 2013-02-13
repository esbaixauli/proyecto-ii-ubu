package vista;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.JList;

import controlador.ControlTipos;

import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.Usuario;

import java.awt.FlowLayout;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class GestionTiposFrame extends JFrame {

	private final JFrame me=this;
	private JPanel contentPane;
	private final JFrame padre;
	private Usuario u;
	ResourceBundle bundle = ResourceBundle.getBundle(
            "vista.internacionalizacion.Recursos", Locale.getDefault());
	/**
	 * Create the frame.
	 */
	public GestionTiposFrame(Usuario u,final JFrame padre) {
		this.u=u;
		this.padre=padre;
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		List<TipoCaso> tipos=null;
		try {
			tipos = ControlTipos.obtenerTiposCaso(u);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, 
					bundle.getString("connecterror"),"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			JFrame login = new LoginFrame();
			login.setVisible(true);
			dispose();
		}
		JList<TipoCaso> list = new ListaCasos(tipos);
		contentPane.add(list, BorderLayout.NORTH);
		
		addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                padre.setEnabled(true);
                me.dispose();
            }
        });
		
	}

}
