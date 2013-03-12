package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
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
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;

import servidorcbr.modelo.Atributo;
import vista.panels.PanelAtributos;
import javax.swing.border.EtchedBorder;

@SuppressWarnings("serial")
public class ConfigAtributoFrame extends JFrame {

	private JPanel contentPane;
	private JFrame me = this, padre;
	private Atributo a, temp;
	private PanelAtributos panel;

	private ResourceBundle b = ResourceBundle.getBundle(
			"vista.internacionalizacion.Recursos", Locale.getDefault());

	/**
	 * Create the frame.
	 * 
	 * @param padre
	 * @param a
	 */
	public ConfigAtributoFrame(Atributo at, final JFrame padre) {
		setIconImage(new ImageIcon("res/logocbr.png").getImage());
		setResizable(false);
		setTitle(at.getNombre());
		setBounds(100, 100, 622, 169);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		this.padre = padre;
		a = at;
		temp = new Atributo();
		temp.setEsProblema(a.getEsProblema());
		temp.setMetrica(a.getMetrica());
		temp.setNombre(a.getNombre());
		temp.setParamMetrica(a.getParamMetrica());
		temp.setPeso(a.getPeso());
		temp.setTipo(a.getTipo());
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		JToolBar toolBar = new JToolBar();
		toolBar.setBorder(BorderFactory.createEtchedBorder() );
		toolBar.setLayout(new FlowLayout(FlowLayout.CENTER));
		toolBar.setFloatable(false);
		contentPane.add(toolBar, BorderLayout.SOUTH);

		JButton btnGuardar = new JButton(new ImageIcon("res/save_32.png"));
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (panel.comprobarLleno()) {
					temp = panel.getAtributo();
					a.setPeso(temp.getPeso());
					a.setMetrica(temp.getMetrica());
					a.setParamMetrica(temp.getParamMetrica());
					padre.setEnabled(true);
					me.dispose();
				}
			}
		});
		toolBar.add(btnGuardar);

		JButton btnSalir = new JButton(new ImageIcon("res/left_32.png"));
		toolBar.add(btnSalir);
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cerrar();
			}
		});

		panel = new PanelAtributos(0, contentPane, null, temp);
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		contentPane.add(panel, BorderLayout.CENTER);
		cierreVentana();
	}

	private void cerrar() {
		if (JOptionPane.showConfirmDialog(null, b.getString("exitnosave"),
				b.getString("exitnosave"), JOptionPane.YES_NO_OPTION) == 0) {
			padre.setEnabled(true);
			padre.setVisible(true);
			me.dispose();
		}
	}

	private void cierreVentana() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				cerrar();
			}
		});
	}
}
