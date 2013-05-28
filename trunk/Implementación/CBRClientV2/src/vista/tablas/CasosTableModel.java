package vista.tablas;

import javax.swing.table.DefaultTableModel;

import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.TipoCaso;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
/** TableModel asociado a una tabla que muestra casos de un tipo concreto.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
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
	
	/**Constructor del tablemodel. Requiere el tipo de caso del que se van a mostrar
	 * casos en la tabla para saber qué atributos son problema y cuales solución.
	 * @param casos Casos a mostrar por la tabla. Cada caso es un mapa clave-valor con
	 * entradas de tipo {"nombre del atributo", valor}.
	 * @param tc Tipo de caso al que pertenecen los casos.
	 */
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
	
	/**
	 * Rellena el tablemodel con los casos iniciales.
	 */
	private void rellenarCasos(){
		
		for(int i=0;i<casos.size();i++){
			for(Entry<String, Serializable> o : casos.get(i).entrySet()){
				insertarEnNombre(o.getValue(),i,o.getKey());
			}
		}
	}
	
	/**Inserta un valor en una fila (Por índice) y una columna con un nombre dado.
	 * @param s Valor a colocar.
	 * @param row Índice de la fila en la que insertar.
	 * @param colName Nombre de la columna en la que insertar.
	 */
	private void insertarEnNombre(Serializable s, int row, String colName){
		int i=0;
			for(i=0; i<getColumnCount();i++){
				if(colName.equals(getColumnName(i))){
					setValueAt(s,row,i);
				}
			}
	}
}
