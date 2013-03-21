package vista.estadisticas;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;

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
		this.u=u;
		this.padre=padre;
		setTitle(b.getString("stats"));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JTabbedPane tabbedPanePropias = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addTab(b.getString("own"), null, tabbedPanePropias, null);
		
		JTabbedPane tabbedPaneUsuario = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addTab(b.getString("byuser"), null, tabbedPaneUsuario, null);
		
		JTabbedPane tabbedPaneCaso = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addTab(b.getString("bycase"), null, tabbedPaneCaso, null);
		
		JTabbedPane tabbedPaneTotales = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addTab(b.getString("global"), null, tabbedPaneTotales, null);	
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
