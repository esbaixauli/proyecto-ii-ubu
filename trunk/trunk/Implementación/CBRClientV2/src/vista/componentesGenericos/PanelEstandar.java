package vista.componentesGenericos;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JPanel;

import vista.MainFrame;

/**Panel estandar. Componente genérico reutilizable. Su utilidad
 * consiste en establecer la internacionalización y características
 * comunes de cada panel de la aplicación automáticamente, sin que se
 * tenga que escribir de nuevo en cada uno.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
@SuppressWarnings("serial")
public class PanelEstandar extends JPanel{

	protected PanelEstandar me;
	protected MainFrame padre;
	protected ResourceBundle b = ResourceBundle.getBundle(
			"vista.internacionalizacion.Recursos", Locale.getDefault());

	
	
	public PanelEstandar(MainFrame padre){
		super();
		me=this;
		this.padre=padre;
	}
	
	/**
	 * Devuelve el padre del frame actual.
	 */
	public MainFrame getPadre() {
		return padre;
	}
	


}
