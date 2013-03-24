package vista.estadisticas;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;

import servidorcbr.modelo.TipoUsuario;
import servidorcbr.modelo.Usuario;
import javax.swing.JTabbedPane;

public class EstadisticasFrame extends JFrame {

	private JPanel contentPane;
	EnumEstadisticas tipoEst;
	Usuario u;
	private ResourceBundle b = ResourceBundle.getBundle(
			"vista.internacionalizacion.Recursos", Locale.getDefault());
	JFrame me=this, padre;
	
	/**
	 * Crear el frame.
	 */
	public EstadisticasFrame(Usuario u,JFrame padre) {
		setResizable(false);
		setIconImage(new ImageIcon("res/logocbr.png").getImage());
		this.u=u;
		this.padre=padre;
		setTitle(b.getString("stats"));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 995, 390);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		tabbedPane.addTab(b.getString("own"),new PanelEstadisticas(EnumEstadisticas.PROPIAS, u) );
		if(u.getTipo().equals(TipoUsuario.ADMINISTRADOR)){
			tabbedPane.addTab(b.getString("byuserandcase"),new PanelEstadisticas(EnumEstadisticas.USUARIOYCASO, u) );
		}
		cierreVentana();
	}
	
	private void cierreVentana() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				padre.setEnabled(true);
				me.dispose();
			}
		});
	}

}
