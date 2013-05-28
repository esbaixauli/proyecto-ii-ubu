package vista.panels;

import java.awt.SystemColor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import servidorcbr.modelo.Usuario;

/** Lista de usuarios. Puede usarse en varias partes del sistema, pero
 * no es un componente totalmente independiente del contexto y por tanto 'genérico'.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
@SuppressWarnings("serial")
public class ListaUsuarios extends JList<Usuario> {

	/**
	 * Bundle de internacionalización.
	 */
	private ResourceBundle b = ResourceBundle.getBundle(
			"vista.internacionalizacion.Recursos", Locale.getDefault());
	/**
	 * Variable interna para controlar que se indique que no hay tipos de caso
	 * visibles si la lista de casos está vacía.
	 */
	private int filas = 1;
	/**
	 * Vector interno de usuarios a mostrar.
	 */
	private Usuario[] listData;
	
	/**Constructor de la lista.
	 * @param datos Usuarios a mostrar por la lista. Mapa de clave "Nombre de usuario"
	 * y valor Objeto de tipo usuario.
	 */
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
	
	/**Refresca los datos de la lista con los valores que se le pasan por parámetro.
	 * @param datos Mapa de usuarios. Clave: Nombre de usuario. Valor:Objeto de usuario.
	 */
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
