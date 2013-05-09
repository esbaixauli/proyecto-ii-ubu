package vista.estadisticas;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import com.jtattoo.plaf.hifi.HiFiBorders.TabbedPaneBorder;

import controlador.ControlEstadisticas;

import servidorcbr.modelo.TipoUsuario;
import servidorcbr.modelo.Usuario;
import vista.AcercaDeJFrame;
import vista.MainFrame;
import vista.componentesGenericos.FrameEstandar;
import vista.componentesGenericos.PanelEstandar;
import weka.gui.ExtensionFileFilter;
import javax.swing.JToolBar;

@SuppressWarnings("serial")
public class EstadisticasPanel extends PanelEstandar {
	EnumEstadisticas tipoEst;
	Usuario u;
	private ResourceBundle b = ResourceBundle.getBundle(
			"vista.internacionalizacion.Recursos", Locale.getDefault());
	
	/**
	 * Crea el frame.
	 */
	public EstadisticasPanel(Usuario u,MainFrame padre) {
		super(padre);me=this;
		this.u=u;
		setName(b.getString("stats"));
		setBounds(100, 100, 995, 500);
		setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		add(tabbedPane);
		
		
		tabbedPane.addTab(b.getString("own"),new PanelEstadisticasInterior(EnumEstadisticas.PROPIAS, u) );
		if(u.getTipo().equals(TipoUsuario.ADMINISTRADOR)){
			tabbedPane.addTab(b.getString("byuserandcase"),new PanelEstadisticasInterior(EnumEstadisticas.USUARIOYCASO, u) );
		}
		
		
	}
	
	
	
}
