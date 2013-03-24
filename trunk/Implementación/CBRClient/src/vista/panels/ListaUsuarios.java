package vista.panels;

import java.awt.SystemColor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.Usuario;

public class ListaUsuarios extends JList<Usuario> {

	private ResourceBundle b = ResourceBundle.getBundle(
			"vista.internacionalizacion.Recursos", Locale.getDefault());
	private int filas = 1;
	private Usuario[] listData;
	
	public ListaUsuarios(HashMap<String,Usuario> datos) {
		super();
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		setBorder(new TitledBorder(null,
				b.getString("availableusers"), TitledBorder.LEADING,
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
			vacio[0].setNombre(b.getString("nousers"));
			setListData(vacio);
			listData=vacio;
			setEnabled(false);
		} else {
			listData=datos.values().toArray(new Usuario[datos.size()]);
			setListData(listData);
			setEnabled(true);
		}
	}
	
	/**Añade un usuario a la lista.
	 * @param u El usuario a añadir.
	 * @param indice La posición en la que se desea añadir.
	 */
	public void addUsuario(Usuario u, int indice){
		Vector<Usuario> v = new Vector<Usuario>(Arrays.asList(listData));
		v.add(0,u);
		setListData(v);
	}

}
