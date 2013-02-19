package vista;

import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.EventObject;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.text.NumberFormatter;

public class TablaAtributos extends JTable {
	ResourceBundle b = ResourceBundle.getBundle(
            "vista.internacionalizacion.Recursos", Locale.getDefault());
	private String tipo;
	

	public TablaAtributos(String[] columnNames, DefaultTableModel tm){
		super(tm);
		JComboBox<String> comboBox = new JComboBox<String>();
		
		rellenarTipos(comboBox);
		TableColumn columnaTipos = getColumnModel().getColumn(1);
		columnaTipos.setCellEditor(new DefaultCellEditor(comboBox));
		TableColumn columnaPeso = getColumnModel().getColumn(2);
		columnaPeso.setCellEditor(new NumberCellEditor());
	    getTableHeader().setReorderingAllowed(false); 
	    

		final JComboBox<String> comboBoxMet = new JComboBox<String>();

		TableColumn columnaMet = getColumnModel().getColumn(3);

		columnaMet.setCellEditor(new DefaultCellEditor(comboBoxMet));
		
		
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {				
				rellenarMetricas(comboBoxMet,arg0.getItem().toString());
				comboBoxMet.setSelectedIndex(0);
			}
		});
		
		comboBoxMet.addItemListener(new ItemListener(){
			String parametro;
			public void itemStateChanged(ItemEvent arg0){
				String escogido=arg0.getItem().toString();
				if(escogido.equals(b.getString("interval")) || escogido.equals(b.getString("threshold")) ){
					do{
					parametro= JOptionPane.showInputDialog(b.getString("enterparam"));
					}while(parsearParam(parametro)==false);
				}
			}
			private boolean parsearParam(String parametro){
				try{
				NumberFormat.getInstance(b.getLocale()).parse(parametro);
				return true;
				}catch(ParseException e){
					return false;
				}catch(NullPointerException ex){
					return false;
				}
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
		if(tipo.equals(b.getString("string"))){
			comboBox.addItem(b.getString("equalignorecase"));
			comboBox.addItem(b.getString("substring"));
		}else{
			comboBox.addItem(b.getString("interval"));
			comboBox.addItem(b.getString("threshold"));
		}
		comboBox.setSelectedIndex(0);
	}
	
	
	
	public class NumberCellEditor extends DefaultCellEditor {
		public NumberCellEditor(){
		    super(new JFormattedTextField());
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		    JFormattedTextField editor = (JFormattedTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);

		    if (value instanceof Number){
		        Locale myLocale = Locale.getDefault(); 

		        NumberFormat numberFormatB = NumberFormat.getInstance(myLocale);
		        numberFormatB.setMaximumFractionDigits(2);
		        numberFormatB.setMinimumFractionDigits(2);
		        numberFormatB.setMinimumIntegerDigits(1);

		        editor.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(
		                        new NumberFormatter(numberFormatB)));

		        editor.setHorizontalAlignment(SwingConstants.RIGHT);
		        editor.setValue(value);
		    }
		    return editor;
		}

		@Override
		public boolean stopCellEditing() {
		    try {
		        // try to get the value
		        this.getCellEditorValue();
		        return super.stopCellEditing();
		    } catch (Exception ex) {
		        return false;
		    }

		}

		@Override
		public Object getCellEditorValue() {
		    // get content of textField
		    String str = (String) super.getCellEditorValue();
		    if (str == null) {
		        return null;
		    }

		    if (str.length() == 0) {
		        return null;
		    }

		    // try to parse a number
		    try {
		        ParsePosition pos = new ParsePosition(0);
		        Number n = NumberFormat.getInstance().parse(str, pos);
		        if (pos.getIndex() != str.length()) {
		            throw new ParseException(
		                    "parsing incomplete", pos.getIndex());
		        }

		        // return an instance of column class
		        return new Float(n.floatValue());
		        
		    } catch (ParseException pex) {
		        throw new RuntimeException(pex);
		    }
		}
		}
	
	public boolean isCellEditable(int row, int col){
		if(col==4){
			return false;
		}
		return true;
	}
	
	
	
}
