package vista;

import java.util.List;

import javax.swing.JList;
import javax.swing.ListSelectionModel;

import servidorcbr.modelo.TipoCaso;

public class ListaCasos extends JList<TipoCaso> {

	public ListaCasos(List<TipoCaso> datos){
		super();
		setListData( datos.toArray(new TipoCaso [datos.size()]));
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
}
