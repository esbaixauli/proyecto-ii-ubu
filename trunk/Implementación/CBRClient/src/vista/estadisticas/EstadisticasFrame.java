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
import vista.componentes.FrameEstandar;
import weka.gui.ExtensionFileFilter;

@SuppressWarnings("serial")
public class EstadisticasFrame extends FrameEstandar {

	private JPanel contentPane;
	EnumEstadisticas tipoEst;
	Usuario u;
	JTabbedPane tabbedPane;
	private ResourceBundle b = ResourceBundle.getBundle(
			"vista.internacionalizacion.Recursos", Locale.getDefault());
	
	/**
	 * Crea el frame.
	 */
	public EstadisticasFrame(Usuario u,JFrame padre) {
		super(padre);me=this;
		this.u=u;
		setTitle(b.getString("stats"));
		setBounds(100, 100, 995, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		tabbedPane.addTab(b.getString("own"),new PanelEstadisticas(EnumEstadisticas.PROPIAS, u) );
		if(u.getTipo().equals(TipoUsuario.ADMINISTRADOR)){
			tabbedPane.addTab(b.getString("byuserandcase"),new PanelEstadisticas(EnumEstadisticas.USUARIOYCASO, u) );
		}
		
		crearOpcionExportar();
	}
	
	private void crearOpcionExportar(){
		JMenuBar barra = getJMenuBar();
		JMenu menuEx = new JMenu(b.getString("file"));
		JMenuItem itemEx = new JMenuItem(b.getString("export"));
		
		itemEx.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jfc = new JFileChooser();
				//Permito guardar en pdf
				jfc.setFileFilter(new ExtensionFileFilter("PDF","pdf"));
				if(jfc.showSaveDialog(me) == JFileChooser.APPROVE_OPTION){
					//Obtengo la pestaña de estadisticas que tiene el foco actualmente
					PanelEstadisticas actual = (PanelEstadisticas) tabbedPane.getSelectedComponent();
					//Obtengo el texto y la lista de graficos del panel que tiene el foco.
					if(!ControlEstadisticas.escribirEstadisticasPDF(jfc.getSelectedFile().getAbsolutePath()+".pdf",
							b.getString("report"),actual.getTexto(), actual.getGraficos())){
						//Si la operacion falla lo indico al exportar
						JOptionPane.showMessageDialog(null, 
								b.getString("exporterror"),"Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				
			}
		});
		menuEx.add(itemEx);
		barra.add(menuEx, 0);
		
	}
}
