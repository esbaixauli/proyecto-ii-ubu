package vista.tablas;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import servidorcbr.modelo.TipoCaso;

/**Cellrenderer para una tabla que muestra casos de un tipo de caso concreto.
 * Los atributos de la solución se muestran en azul, y los del problema en rojo.
 * La calidad se muestra en gris.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 * @see javax.swing.table.DefaultTableCellRenderer
 */
@SuppressWarnings("serial")
public class CasosTableCellRenderer extends DefaultTableCellRenderer {
	/**
	 * Tipo de caso asociado a la tabla de la que esta clase es renderer.
	 */
	TipoCaso tc;

	/**Constructor del renderer. Requiere el tipo de caso del que se van a pintar
	 * casos en la tabla para saber qué atributos son problema y cuales solución.
	 * @param tc Tipo de caso asociado a la tabla de la que esta clase es renderer.
	 */
	public CasosTableCellRenderer(TipoCaso tc) {
		super();
		this.tc = tc;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		Component c= super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		String nombreCol="";
		try{ //Busca el nombre asociado al índice.
		nombreCol = table.getColumnName(column);
		}catch(Exception ex){ex.printStackTrace();}
		//Calidad en gris
		if(nombreCol.equals("META_QUALITY")){
			c.setForeground(Color.gray);
		}else{
			if(tc.getAtbos().get(nombreCol).getEsProblema()){
				c.setForeground(Color.red);
			}else{
				c.setForeground(Color.blue);
			}
		}
		return c;
	}
}
