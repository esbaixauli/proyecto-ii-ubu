package vista;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

public class TablaAtributos extends JTable {
	ResourceBundle b = ResourceBundle.getBundle(
            "vista.internacionalizacion.Recursos", Locale.getDefault());
	private String tipo;

	public TablaAtributos(String[] columnNames){
		super(new Object[3][columnNames.length],columnNames);
		JComboBox<String> comboBox = new JComboBox<String>();
		rellenarTipos(comboBox);
		TableColumn columnaTipos = getColumnModel().getColumn(1);
		columnaTipos.setCellEditor(new DefaultCellEditor(comboBox));
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				
			}
		});
	}
	
	private void rellenarTipos(JComboBox<String> comboBox){
		comboBox.addItem(b.getString("integer"));
		comboBox.addItem(b.getString("string"));
		comboBox.addItem(b.getString("decimal"));
	}
	
	private void rellenarMetricas(JComboBox<String> comboBox,String tipo){
		comboBox.removeAllItems();
		comboBox.addItem(b.getString("equal"));
		if(tipo.equals("S")){
			comboBox.addItem(b.getString("equalignorecase"));
			comboBox.addItem(b.getString("substring"));
		}else{
			comboBox.addItem(b.getString("interval"));
			comboBox.addItem(b.getString("threshold"));
		}
		comboBox.setSelectedIndex(0);
	}
}
