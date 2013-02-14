package vista;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import servidorcbr.modelo.TipoCaso;

public class ListaCasos extends JList<TipoCaso> {

	private ResourceBundle bundle = ResourceBundle.getBundle(
            "vista.internacionalizacion.Recursos", Locale.getDefault());
	private int filas=6;
	public ListaCasos(List<TipoCaso> datos){
		super();
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		setBorder(new TitledBorder(null, bundle.getString("availablecasetypes"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		refrescarDatos(datos);
		setVisibleRowCount(6);
	}
	
	//Refresca los datos de la lista con los valores que se le pasan por parámetro.
	public void refrescarDatos(List<TipoCaso> datos){
		if(datos==null || datos.isEmpty()){
			TipoCaso vacio []=new TipoCaso[filas];
			for(int i=0;i<filas;i++){
				vacio[i]=new TipoCaso();
				vacio[i].setNombre(" ");
			}
			vacio[0].setNombre(bundle.getString("nocasetypes"));
			setListData(vacio);
			setEnabled(false);
		}else{
			setListData( datos.toArray(new TipoCaso [datos.size()]));
		}
	}
}
