package vista.panels;

import java.awt.Component;
import java.awt.SystemColor;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**Renderer para pintar cada fila de una lista de un color, alternativamente.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
@SuppressWarnings("serial")
class AltCellRenderer extends DefaultListCellRenderer {

	/**
	 * Constructor de la clase.
	 */
	public AltCellRenderer() {
		super();
		setOpaque(true);
	}

	@SuppressWarnings("rawtypes") //Controlado. Depende de la firma de la superclase.
	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		Component c = super.getListCellRendererComponent(list, value,
				index, isSelected, cellHasFocus);
		if (!isSelected) {
			if (index % 2 != 0) {
				c.setBackground(SystemColor.inactiveCaption);

			} else {
				c.setBackground(SystemColor.WHITE);
			}
		} else {
			c.setBackground(SystemColor.textHighlight);
		}

		return c;
	}
}