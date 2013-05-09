package vista.tipos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import controlador.ControlTipos;

import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.TipoCaso;
import vista.MainFrame;
import vista.panels.ScrollPanelAtbo;

@SuppressWarnings("serial")
public class VerTipoPanel extends NuevoTipoPanel {

	
	
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

	
	
	private void setAtbosProblema(TipoCaso tc) {
		setAtbos(tc, true, getPanelProblema());
	}

	private void setAtbosSolucion(TipoCaso tc) {
		setAtbos(tc, false, getPanelSolucion());
	}

	private void setAtbos(TipoCaso tc, boolean problema, ScrollPanelAtbo p) {
		p.desactivarComponentes();
		for (Atributo a : tc.getAtbos().values()) {
			if(a.getEsProblema() == problema){
				p.addAtributo(a, true);
			}
		}
	}

}
