package vista.tipos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.TipoCaso;
import vista.MainFrame;
import vista.panels.ScrollPanelAtbo;

/** Panel para mostrar un tipo de caso, y modificar alguna carácterística de él.
 * En concreto, se permite cambiar las métricas, pesos y parámetros de cada atributo,
 * y se enlaza con la ventana de configuración de técnicas para poder modificar también
 * estas.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
@SuppressWarnings("serial")
public class VerTipoPanel extends NuevoTipoPanel {

	
	
	/**Crea el panel.
	 * @param padre Ventana padre de este panel.
	 * @param tic Tipo de caso que se está modificando.
	 */
	public VerTipoPanel(final MainFrame padre, TipoCaso tic) {
		super(padre);
		setName(b.getString("seecasetype")+": "+tic.getNombre());
		super.tc= tic;
		getTextFieldNombre().setText(tc.getNombre());
		getTextFieldNombre().setEnabled(false);
		setAtbosProblema(tc);
		setAtbosSolucion(tc);
	    for( ActionListener al : btnSiguiente.getActionListeners() ) {
	        btnSiguiente.removeActionListener( al );
	    }
	    btnSiguiente.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				rellenaTC();
				
				padre.addPanel(b.getString("managemethods")+": "+tc.getNombre(), new GestionTecnicasPanel(tc, padre, false));
				
				padre.removePanel(me.getName());
				
				
			}
	    	
	    });
	}

	
	
	/**Establece el nuevo estado de los atributos del problema una vez que el usuario
	 * los modifica.
	 * @param tc Tipo de caso a modificar.
	 */
	private void setAtbosProblema(TipoCaso tc) {
		setAtbos(tc, true, getPanelProblema());
	}

	/**Establece el nuevo estado de los atributos de la solución una vez que el usuario
	 * los modifica.
	 * @param tc Tipo de caso a modificar.
	 */
	private void setAtbosSolucion(TipoCaso tc) {
		setAtbos(tc, false, getPanelSolucion());
	}

	/** Establece los valores iniciales de un panel de atributos (De problema o solución).
	 * @param tc Tipo de caso a modificar en esta ventana.
	 * @param problema cierto si se están estableciendo los atributos del problema, false si
	 * son los de la solución.
	 * @param p Panel de atributos que se rellenará con los valores iniciales.
	 */
	private void setAtbos(TipoCaso tc, boolean problema, ScrollPanelAtbo p) {
		p.desactivarComponentes();
		for (Atributo a : tc.getAtbos().values()) {
			if(a.getEsProblema() == problema){
				p.addAtributo(a, true);
			}
		}
	}

}
