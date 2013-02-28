package vista;

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
