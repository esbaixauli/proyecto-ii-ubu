package vista.panels;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

/**Panel para mostrar un atributo y su valor como sólo lectura.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
@SuppressWarnings("serial")
public class PanelMostrarAtbo extends JPanel {

	/** Crea el panel.
	 * @param nombre Nombre del atributo.
	 * @param valor Valor a mostrar para el atributo.
	 */
	public PanelMostrarAtbo(String nombre,Object valor) {
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblNombreatbo = new JLabel(nombre+":");
		add(lblNombreatbo);
		
		JLabel lblNewLabel = new JLabel(valor+"");
		add(lblNewLabel);
	}

}
