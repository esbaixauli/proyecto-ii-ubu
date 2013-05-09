package vista.tipos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.Usuario;
import vista.LoginFrame;
import vista.MainFrame;
import vista.componentesGenericos.FrameEstandar;
import vista.panels.ListaCasos;
import controlador.ControlTipos;
import java.awt.BorderLayout;
import java.awt.Color;

@SuppressWarnings("serial")
public class GestionTiposFrame extends FrameEstandar{

	private JPanel contentPane;
	private Usuario u;
	private int ancho=250;
	private List<TipoCaso> tipos=null;
	private ListaCasos list;
	/**
	 * Crea el frame.
	 */
	public GestionTiposFrame(final Usuario u,final MainFrame padre) {
		super(padre);me=this;
		setTitle(b.getString("managecasetypes"));
		this.u=u;
		setBounds(100, 100, 314, 314);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
	    
		try {
			tipos = ControlTipos.obtenerTiposCaso(u);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, 
					b.getString("connecterror"),"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			JFrame login = new LoginFrame();
			login.setVisible(true);
			padre.dispose();
			me.dispose();
		
		}
		 contentPane.setLayout(new BorderLayout(0, 0));
		 
		 JScrollPane scrollPane = new JScrollPane();
		 scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		 contentPane.add(scrollPane, BorderLayout.CENTER);
		 list = new ListaCasos(tipos);
		 scrollPane.setViewportView(list);
		 
		 		
		 		list.setFixedCellWidth(ancho);
		
		
		JPanel toolBarPanel = new JPanel();
		toolBarPanel.setBackground(Color.GRAY);
		toolBarPanel.setBorder(BorderFactory.createEtchedBorder() );


		
		
		JButton btnNuevobut = new JButton(new ImageIcon("res/document_32.png"));
		btnNuevobut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				padre.addPanel(b.getString("newcasetype"),new NuevoTipoPanel(padre));
				padre.setEnabled(true);
				me.setVisible(false);
				me.dispose();
			}
		});
		
		JButton btnVerbut = new JButton(new ImageIcon("res/search_32.png"));
		btnVerbut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				padre.setEnabled(true);
				if(list.getSelectedValue()==null){
					JOptionPane.showMessageDialog(null, 
							b.getString("noselection"),b.getString("noselection"), JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				TipoCaso tcEscogido = tipos.get(list.getSelectedIndex());
				padre.addPanel(b.getString("seecasetype")+": "+tcEscogido.getNombre(),new VerTipoPanel(padre,tcEscogido));
				me.setVisible(false);
				me.dispose();
			}
		});
		toolBarPanel.add(btnVerbut);
		btnVerbut.setToolTipText(b.getString("seecasetype"));
		btnNuevobut.setToolTipText(b.getString("newcasetype"));
		toolBarPanel.add(btnNuevobut);
		
		JButton btnBorrarbut = new JButton(new ImageIcon("res/delete_32.png"));
		btnBorrarbut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(list.getSelectedValue()==null){
					JOptionPane.showMessageDialog(null, 
							b.getString("noselection"),b.getString("noselection"), JOptionPane.INFORMATION_MESSAGE);
					return;
				} else if (list.getSelectedValue().getNombre().equals("root")) {
					JOptionPane.showMessageDialog(null, 
							b.getString("rootdeletion"),b.getString("rootdeletion"), JOptionPane.INFORMATION_MESSAGE);
					return;
				}
			 int opcion= JOptionPane.showConfirmDialog(null, 
						b.getString("userdelsure"),b.getString("delsure"),JOptionPane.YES_NO_OPTION);
			 if(opcion==0){
				try {
					if(ControlTipos.borrarTiposCaso(list.getSelectedValue())){
						JOptionPane.showMessageDialog(null, 
								b.getString("opsuccess"),b.getString("opsuccess"), JOptionPane.INFORMATION_MESSAGE);
					}else{
						JOptionPane.showMessageDialog(null, 
								b.getString("opunsuccess"),b.getString("opunsuccess")+b.getString("delunsuccesscause")
								, JOptionPane.INFORMATION_MESSAGE);
					}
					
					list.refrescarDatos(ControlTipos.obtenerTiposCaso(u));
				} catch (MalformedURLException e) {
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, 
							b.getString("connecterror"),"Error", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}
			}
		});
		
		btnBorrarbut.setToolTipText(b.getString("delcasetype"));
		toolBarPanel.add(btnBorrarbut);
		
		contentPane.add(toolBarPanel, BorderLayout.SOUTH);
		setLocationRelativeTo(padre);
	}

	
	@Override
	public void setEnabled(boolean boo) {
		super.setEnabled(boo);
		if(boo){
			try {
				tipos=ControlTipos.obtenerTiposCaso(u);
			} catch (IOException ex){
				JOptionPane.showMessageDialog(null, 
						b.getString("connecterror"),"Error", JOptionPane.ERROR_MESSAGE);
			}
			list.refrescarDatos(tipos);
		}
	}
}
