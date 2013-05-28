package vista.panels;

import java.awt.SystemColor;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import servidorcbr.modelo.TipoCaso;

/** Lista de usuarios. Puede usarse en varias partes del sistema, pero
 * no es un componente totalmente independiente del contexto y por tanto 'genérico'.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
@SuppressWarnings("serial")
public class ListaCasos extends JList<TipoCaso> {

	/**
	 * Bundle de internacionalización.
	 */
	private ResourceBundle bundle = ResourceBundle.getBundle(
			"vista.internacionalizacion.Recursos", Locale.getDefault());
	/**
	 * Variable interna para controlar que se indique que no hay tipos de caso
	 * visibles si la lista de casos está vacía.
	 */
	private int filas = 1;

	/**Constructor de la lista.
	 * @param datos Tipos de caso a mostrar al usuario por la lista.
	 */
	public ListaCasos(List<TipoCaso> datos) {
		super();
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		setBorder(new TitledBorder(null,
				bundle.getString("availablecasetypes"), TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		refrescarDatos(datos);
		setVisibleRowCount(6);

		setCellRenderer(new AltCellRenderer());
		setBackground(SystemColor.controlHighlight);
	}

	/**Refresca los datos de la lista con los valores que se le pasan por parámetro.
	 * @param datos Lista de tipos de caso a mostrar.
	 */
	public void refrescarDatos(List<TipoCaso> datos) {
		if (datos == null || datos.isEmpty()) {
			TipoCaso vacio[] = new TipoCaso[filas];
			for (int i = 0; i < filas; i++) {
				vacio[i] = new TipoCaso();
				vacio[i].setNombre(" ");
			}
			vacio[0].setNombre(bundle.getString("nocasetypes"));
			setListData(vacio);
			setEnabled(false);
		} else {
			setListData(datos.toArray(new TipoCaso[datos.size()]));
			setEnabled(true);
		}
	}

}
