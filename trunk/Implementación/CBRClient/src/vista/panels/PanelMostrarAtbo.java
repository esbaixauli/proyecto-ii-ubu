package vista.panels;

import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import java.awt.FlowLayout;
import javax.swing.JLabel;

public class PanelMostrarAtbo extends JPanel {

	/**
	 * Crea el panel.
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
