package vista.tablas;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import servidorcbr.modelo.TipoCaso;

@SuppressWarnings("serial")
public class CasosTableCellRenderer extends DefaultTableCellRenderer {
	TipoCaso tc;

	public CasosTableCellRenderer(TipoCaso tc) {
		super();
		this.tc = tc;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		Component c= super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		String nombreCol = table.getColumnName(column);
		if(tc.getAtbos().get(nombreCol).getEsProblema()){
			c.setForeground(Color.red);
		}else{
			c.setForeground(Color.blue);
		}
		return c;
	}
}
