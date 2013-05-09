package vista.panels;

import java.awt.Component;
import java.awt.SystemColor;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

class AltCellRenderer extends DefaultListCellRenderer {

	public AltCellRenderer() {
		super();
		setOpaque(true);
	}

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