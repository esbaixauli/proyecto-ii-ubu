package vista.panels;

import java.awt.SystemColor;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.Usuario;

public class ListaUsuarios extends JList<Usuario> {

	private ResourceBundle b = ResourceBundle.getBundle(
			"vista.internacionalizacion.Recursos", Locale.getDefault());
	private int filas = 1;
	
	public ListaUsuarios(HashMap<String,Usuario> datos) {
		super();
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		setBorder(new TitledBorder(null,
				//b.getString("availableusers"), TitledBorder.LEADING,
				"usuarios disponibles", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		refrescarDatos(datos);
		setVisibleRowCount(6);

		setCellRenderer(new AltCellRenderer());
		setBackground(SystemColor.controlHighlight);
	}
	
	// Refresca los datos de la lista con los valores que se le pasan por
	// parámetro.
	public void refrescarDatos(HashMap<String,Usuario> datos) {
		if (datos == null || datos.isEmpty()) {
			Usuario vacio[] = new Usuario[filas];
			for (int i = 0; i < filas; i++) {
				vacio[i] = new Usuario();
				vacio[i].setNombre(" ");
			}
			//vacio[0].setNombre(b.getString("nocasetypes"));
			vacio[0].setNombre("sin usuarios");
			setListData(vacio);
			setEnabled(false);
		} else {
			setListData(datos.values().toArray(new Usuario[datos.size()]));
			setEnabled(true);
		}
	}

}
