package vista;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.Usuario;
import vista.componentes.FrameEstandar;
import vista.estadisticas.PanelEstadisticas;
import weka.gui.ExtensionFileFilter;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JLabel;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.BoxLayout;

import controlador.ControlEstadisticas;
import controlador.util.EscritorInformeEjecucion;

@SuppressWarnings("serial")
public class FrameFinCBR extends FrameEstandar {

	public FrameFinCBR(final JFrame padre,final TipoCaso tc, final Usuario u) {
		super(padre);
		setSize(315, 130);
		getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblExito = new JLabel(b.getString("opsuccess"));
		
		lblExito.setIcon( UIManager.getIcon("OptionPane.informationIcon"));
		getContentPane().add(lblExito);
		
		JButton btnNewButton = new JButton(b.getString("ok"));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				padre.setEnabled(true);
				padre.toFront();
				me.setVisible(false);
				me.dispose();
			}
		});
		
		getContentPane().add(btnNewButton);
		
		JMenuBar barra = getJMenuBar();
		JMenu menuEx = new JMenu(b.getString("file"));
		JMenuItem itemEx = new JMenuItem(b.getString("executionreport"));
		itemEx.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jfc = new JFileChooser();
				//Permito guardar en txt
				jfc.setFileFilter(new ExtensionFileFilter("txt","txt"));
				if(jfc.showSaveDialog(me) == JFileChooser.APPROVE_OPTION){
					//Llamo al escritor para que genere un informe
					if(!EscritorInformeEjecucion.
							escribirInforme(jfc.getSelectedFile().getAbsolutePath(), u, tc)){
						//Si error, notificalo al usuario
						JOptionPane.showMessageDialog(null, 
								b.getString("exporterror"),"Error", JOptionPane.ERROR_MESSAGE);
					}
					
				}
				
			}
		});
		
		menuEx.add(itemEx);
		barra.add(menuEx, 0);
	}

}
