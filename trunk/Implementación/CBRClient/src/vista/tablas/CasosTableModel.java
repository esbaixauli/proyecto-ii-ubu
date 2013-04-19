package vista.tablas;

import javax.swing.table.DefaultTableModel;

import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.TipoCaso;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
@SuppressWarnings("serial")
public class CasosTableModel extends DefaultTableModel {
	
	/**
	 * Tipo de caso al que pertenece la tabla correspondiente a este model.
	 */
	private TipoCaso tc;

	/**
	 * Lista interna de casos que contiene la tabla.
	 */
	private List<HashMap<String,Object>> casos;
	
	public CasosTableModel( List<HashMap<String,Object>> casos,TipoCaso tc){
		super(tc.getAtbos().keySet().toArray(new String[1]),casos.size());
		super.addColumn("META_QUALITY");
		this.casos = casos;
		this.tc=tc;
		rellenarCasos();

	}
	
	@Override
	public void setValueAt(Object o, int row, int col){
		try{
			String tipo;
			Atributo a = tc.getAtbos().get(getColumnName(col));
			if(a!=null){ //Si no existe un atributo en el tipo con ese nombre,
				tipo = a.getTipo(); //Es porque es la calidad del caso.
			}else{
				tipo="D";
			}
			if(tipo.equals("I")){
				Integer.parseInt(o+"");
			}else if(tipo.equals("D")){
				Double.parseDouble(o+"");
			}
			super.setValueAt(o, row, col);
			casos.get(row).put(getColumnName(col), o);
		}catch(Exception ex){}//Si hay una excepción (campo mal rellenado), no se inserta el valor y se queda el que había.
	}
	
	private void rellenarCasos(){
		
		for(int i=0;i<casos.size();i++){
			for(Entry<String, Object> o : casos.get(i).entrySet()){
				insertarEnNombre(o.getValue(),i,o.getKey());
			}
		}
	}
	
	private void insertarEnNombre(Object o, int row, String colName){
		int i=0;
			for(i=0; i<getColumnCount();i++){
				if(colName.equals(getColumnName(i))){
					setValueAt(o,row,i);
				}
			}
	}
}
