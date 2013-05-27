package vista.estadisticas;

import java.awt.BorderLayout;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JTabbedPane;

import servidorcbr.modelo.TipoUsuario;
import servidorcbr.modelo.Usuario;
import vista.MainFrame;
import vista.componentesGenericos.PanelEstandar;

/**Panel de estadísticas de uso de los tipos de caso en CBR.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
@SuppressWarnings("serial")
public class EstadisticasPanel extends PanelEstandar {
	EnumEstadisticas tipoEst;
	/**
	 * Usuario que pide consultar las estadísticas. Se guarda para
	 * que se muestren sus estadísticas cuando escoge la opción "Propias".
	 */
	Usuario u;
	
	/**
	 * Bundle de internacionalización.
	 */
	private ResourceBundle b = ResourceBundle.getBundle(
			"vista.internacionalizacion.Recursos", Locale.getDefault());
	
	
	/**Constructor del panel de estadísticas.
	 * @param u Usuario que pide ver estadísticas.
	 * @param padre Ventana padre de este panel.
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
