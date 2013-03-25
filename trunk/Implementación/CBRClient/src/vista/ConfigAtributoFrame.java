package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;

import servidorcbr.modelo.Atributo;
import vista.componentes.FrameEstandar;
import vista.panels.PanelAtributos;

@SuppressWarnings("serial")
public class ConfigAtributoFrame extends FrameEstandar {

	private JPanel contentPane;
	private Atributo a, temp;
	
	private JButton btnGuardar,btnSalir;

	private ResourceBundle b = ResourceBundle.getBundle(
			"vista.internacionalizacion.Recursos", Locale.getDefault());
	private JScrollPane scrollPane;
	/**
	 * Create the frame.
	 */
	public ConfigAtributoFrame(Atributo at,final JFrame padre) {
		//Establezco referencias al padre y a la propia ventana
		super(padre);me = this;
		contentPane = new JPanel();
		setContentPane(contentPane);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JToolBar toolBar = new JToolBar();
		toolBar.setLayout(new FlowLayout());
		toolBar.setFloatable(false);
		getContentPane().add(toolBar, BorderLayout.SOUTH);
		
		
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
			
	}
	
	

}
