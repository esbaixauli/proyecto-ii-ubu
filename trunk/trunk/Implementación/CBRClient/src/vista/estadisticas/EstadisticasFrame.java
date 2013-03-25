package vista.estadisticas;

import java.awt.BorderLayout;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import servidorcbr.modelo.TipoUsuario;
import servidorcbr.modelo.Usuario;
import vista.componentes.FrameEstandar;

@SuppressWarnings("serial")
public class EstadisticasFrame extends FrameEstandar {

	private JPanel contentPane;
	EnumEstadisticas tipoEst;
	Usuario u;
	private ResourceBundle b = ResourceBundle.getBundle(
			"vista.internacionalizacion.Recursos", Locale.getDefault());
	
	/**
	 * Crea el frame.
	 */
	public EstadisticasFrame(Usuario u,JFrame padre) {
		super(padre);me=this;
		this.u=u;
		
		setBounds(100, 100, 995, 500);
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
	}
}
