package vista;

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
import vista.panels.ScrollPanelAtbo;

@SuppressWarnings("serial")
public class VerTipoFrame extends NuevoTipoFrame {

	private JFrame me =this;
		private ResourceBundle b = ResourceBundle.getBundle(
				"vista.internacionalizacion.Recursos", Locale.getDefault());
	public VerTipoFrame(final JFrame padre, TipoCaso tic) {
		super(padre);
		setTitle(tc.getNombre());
		super.tc= tic;
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
				try{
				rellenaTC();
				ControlTipos.modificarTipo(tc);
				padre.setEnabled(true);
				me.setVisible(false);
				me.dispose();
				}catch(IOException ex){
					JOptionPane.showMessageDialog(null,
							b.getString("moderror"), "Error",
							JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
				
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
