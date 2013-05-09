package vista.cicloCBR;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.Usuario;
import vista.MainFrame;
import vista.componentesGenericos.FrameEstandar;
import vista.estadisticas.PanelEstadisticasInterior;
import weka.gui.ExtensionFileFilter;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JLabel;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.BoxLayout;

import controlador.ControlEstadisticas;
import controlador.util.EscritorInformeEjecucion;
import java.awt.Dialog.ModalExclusionType;

@SuppressWarnings("serial")
public class FrameFinCBR extends JFrame {

	private ResourceBundle b = ResourceBundle.getBundle(
			"vista.internacionalizacion.Recursos", Locale.getDefault());
	
	private MainFrame padre;
	private JFrame me=this;
	
	public FrameFinCBR(final MainFrame padre,final TipoCaso tc, final Usuario u) {
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.padre=padre;
		setSize(315, 130);
		getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		setTitle(b.getString("opsuccess"));
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
