package vista;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.TipoCaso;
import vista.panels.ScrollPanelAtbo;

public class VerTipoFrame extends NuevoTipoFrame {
	TipoCaso tc;

	public VerTipoFrame(final JFrame padre, TipoCaso tc) {
		super(padre);
		setTitle(tc.getNombre());
		this.tc = tc;
		getTextFieldNombre().setText(tc.getNombre());
		getTextFieldNombre().setEnabled(false);
		setAtbosProblema(tc);
		setAtbosSolucion(tc);
	    for( ActionListener al : btnGuardar.getActionListeners() ) {
	        btnGuardar.removeActionListener( al );
	    }
	    btnGuardar.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				//TODO
				
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
