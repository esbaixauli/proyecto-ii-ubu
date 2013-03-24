package vista;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;

import servidorcbr.modelo.Atributo;
import vista.panels.PanelAtributos;
import java.awt.FlowLayout;
import javax.swing.ScrollPaneConstants;

public class ConfigAtributoFrame extends JFrame {

	private JPanel contentPane;
	private Atributo a, temp;
	
	private JFrame padre,me=this;
	
	private JButton btnGuardar,btnSalir;

	private ResourceBundle b = ResourceBundle.getBundle(
			"vista.internacionalizacion.Recursos", Locale.getDefault());
	private JScrollPane scrollPane;
	/**
	 * Create the frame.
	 */
	public ConfigAtributoFrame(Atributo at,final JFrame padre) {
		contentPane = new JPanel();
		setContentPane(contentPane);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JToolBar toolBar = new JToolBar();
		toolBar.setLayout(new FlowLayout());
		toolBar.setFloatable(false);
		getContentPane().add(toolBar, BorderLayout.SOUTH);
		
		 setResizable(false);
			setTitle(at.getNombre());
			a = at;
			this.padre=padre;
			temp = new Atributo();
			temp.setEsProblema(a.getEsProblema());
			temp.setMetrica(a.getMetrica());
			temp.setNombre(a.getNombre());
			temp.setParamMetrica(a.getParamMetrica());
			temp.setPeso(a.getPeso());
			temp.setTipo(a.getTipo());
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
			btnGuardar = new JButton(new ImageIcon("res/save_32.png"));
			btnGuardar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					a.setPeso(temp.getPeso());
					a.setMetrica(temp.getMetrica());
					a.setParamMetrica(temp.getParamMetrica());
					padre.setEnabled(true);
					me.dispose();
				}
			});
			toolBar.add(btnGuardar);
			btnSalir = new JButton(new ImageIcon("res/left_32.png"));
			btnSalir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					if (JOptionPane.showConfirmDialog(null,
							b.getString("exitnosave"), b.getString("exitnosave"),
							JOptionPane.YES_NO_OPTION) == 0) {
						padre.setEnabled(true);
						padre.setVisible(true);
						me.dispose();
					}
				}
			});
			toolBar.add(btnSalir);
			
			scrollPane = new JScrollPane();
			scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			getContentPane().add(scrollPane, BorderLayout.CENTER);
			JPanel panel = new PanelAtributos(0, getContentPane(), new JScrollPane(), temp);
			scrollPane.add(panel);
			cierreVentana();
	}
	
	private void cierreVentana() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				padre.setEnabled(true);
				me.dispose();
			}
		});
	}
	

}
