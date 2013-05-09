package vista.tablas;

import javax.swing.table.DefaultTableModel;

import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.TipoCaso;

import java.io.Serializable;
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
	private List<HashMap<String,Serializable>> casos;
	
	public CasosTableModel( List<HashMap<String,Serializable>> casos,TipoCaso tc){
		super(tc.getAtbos().keySet().toArray(new String[1]),casos.size());
		super.addColumn("META_QUALITY");
		this.casos = casos;
		this.tc=tc;
		rellenarCasos();

	}
	
	@Override
	public void setValueAt(Object o, int row, int col){
		Serializable s = (Serializable) o;
		try{
			String tipo;
			Atributo a = tc.getAtbos().get(getColumnName(col));
			if(a!=null){ 	//Si no existe un atributo en el tipo con ese nombre,
				tipo = a.getTipo();
			}else{ 			//Es porque es la calidad del caso.
				tipo="I";
			}
			if(tipo.equals("I")){
				Integer.parseInt(s+"");
			}else if(tipo.equals("D")){
				Double.parseDouble(s+"");
			}
			super.setValueAt(s, row, col);
			casos.get(row).put(getColumnName(col), s);
		}catch(Exception ex){}//Si hay una excepción (campo mal rellenado), no se inserta el valor y se queda el que había.
	}
	
	private void rellenarCasos(){
		
		for(int i=0;i<casos.size();i++){
			for(Entry<String, Serializable> o : casos.get(i).entrySet()){
				insertarEnNombre(o.getValue(),i,o.getKey());
			}
		}
	}
	
	private void insertarEnNombre(Serializable s, int row, String colName){
		int i=0;
			for(i=0; i<getColumnCount();i++){
				if(colName.equals(getColumnName(i))){
					setValueAt(s,row,i);
				}
			}
	}
}
