package vista.tablas;

import javax.swing.table.DefaultTableModel;

import servidorcbr.modelo.TipoCaso;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
@SuppressWarnings("serial")
public class CasosTableModel extends DefaultTableModel {


	private List<HashMap<String,Object>> casos;
	public CasosTableModel( List<HashMap<String,Object>> casos,TipoCaso tc){
		super(tc.getAtbos().keySet().toArray(new String[1]),casos.size());
		this.casos = casos;
		rellenarCasos();
		
	}
	
	@Override
	public void setValueAt(Object o, int row, int col){	
		super.setValueAt(o, row, col);
		casos.get(row).put(getColumnName(col), o);
	}
	
	private void rellenarCasos(){
		
		for(int i=0;i<casos.size();i++){
			for(Entry<String, Object> o : casos.get(i).entrySet()){
				insertarEnNombre(o.getValue(),i,o.getKey());
			}
		}
	}
	
	private void insertarEnNombre(Object o, int row, String colName){
		for(int i=0; i<getColumnCount();i++){
			if(colName.equals(getColumnName(i))){
				setValueAt(o,row,i);
			}
		}
	}
}
