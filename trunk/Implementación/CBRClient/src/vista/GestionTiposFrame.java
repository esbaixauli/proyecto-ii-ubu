package vista;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.JList;

import controlador.ControlTipos;

import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.Usuario;

import java.awt.FlowLayout;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;
import javax.swing.BoxLayout;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GestionTiposFrame extends JFrame {

	private final JFrame me=this;
	private JPanel contentPane;
	private final JFrame padre;
	private Usuario u;
	private int ancho=250;
	
	ResourceBundle bundle = ResourceBundle.getBundle(
            "vista.internacionalizacion.Recursos", Locale.getDefault());
	/**
	 * Create the frame.
	 */
	public GestionTiposFrame(final Usuario u,final JFrame padre) {
		this.u=u;
		this.padre=padre;
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		List<TipoCaso> tipos=null;
		try {
			tipos = ControlTipos.obtenerTiposCaso(u);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, 
					bundle.getString("connecterror"),"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			JFrame login = new LoginFrame();
			login.setVisible(true);
			padre.dispose();
			me.dispose();
		
		}
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		final ListaCasos list = new ListaCasos(tipos);
		
		
		contentPane.add(list);
		
		
		JToolBar toolBar = new JToolBar("ToolBar",JToolBar.HORIZONTAL);


		toolBar.setFloatable(false);
		
		JButton btnNuevobut = new JButton(new ImageIcon("res/document_32.png"));
		btnNuevobut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFrame nuevo = new NuevoTipoFrame(me);
				nuevo.setVisible(true);
				me.setEnabled(false);
			}
		});
		btnNuevobut.setToolTipText(bundle.getString("newcasetype"));
		toolBar.add(btnNuevobut);
		
		JButton btnBorrarbut = new JButton(new ImageIcon("res/delete_32.png"));
		btnBorrarbut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(list.getSelectedValue()==null){
					JOptionPane.showMessageDialog(null, 
							bundle.getString("noselection"),bundle.getString("noselection"), JOptionPane.INFORMATION_MESSAGE);
					return;
				}
			 int opcion= JOptionPane.showConfirmDialog(null, 
						bundle.getString("delsure"),bundle.getString("delsure"),JOptionPane.YES_NO_OPTION);
			 if(opcion==0){
				try {
					if(ControlTipos.borrarTiposCaso(list.getSelectedValue())){
						JOptionPane.showMessageDialog(null, 
								bundle.getString("opsuccess"),bundle.getString("opsuccess"), JOptionPane.INFORMATION_MESSAGE);
					}else{
						JOptionPane.showMessageDialog(null, 
								bundle.getString("opunsuccess"),bundle.getString("opunsuccess")+bundle.getString("delunsuccesscause")
								, JOptionPane.INFORMATION_MESSAGE);
					}
					
					list.refrescarDatos(ControlTipos.obtenerTiposCaso(u));
				} catch (MalformedURLException e) {
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, 
							bundle.getString("connecterror"),"Error", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}
			}
		});
		
		btnBorrarbut.setToolTipText(bundle.getString("delcasetype"));
		toolBar.add(btnBorrarbut);
		
		contentPane.add(toolBar);

		
		list.setFixedCellWidth(ancho);
		addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                padre.setEnabled(true);
                me.dispose();
            }
        });
		
	}

}
