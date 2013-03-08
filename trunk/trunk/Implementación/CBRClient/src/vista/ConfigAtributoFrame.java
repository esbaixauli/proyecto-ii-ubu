package vista;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class ConfigAtributoFrame extends JFrame {

	private JPanel contentPane;
	private Atributo a, temp;
	private JToolBar toolBar;
	private JFrame padre,me=this;
	
	private JButton btnGuardar;

	private ResourceBundle b = ResourceBundle.getBundle(
			"vista.internacionalizacion.Recursos", Locale.getDefault());
	/**
	 * Create the frame.
	 */
	public ConfigAtributoFrame(Atributo at,final JFrame padre) {
		setResizable(false);
		setTitle(at.getNombre());
		a = at;
		temp = new Atributo();
		temp.setEsProblema(a.getEsProblema());
		temp.setMetrica(a.getMetrica());
		temp.setNombre(a.getNombre());
		temp.setParamMetrica(a.getParamMetrica());
		temp.setPeso(a.getPeso());
		temp.setTipo(a.getTipo());
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 397, 236);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		PanelAtributos panel = new PanelAtributos(0, this, null, temp);
		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setBorder(BorderFactory.createEtchedBorder());
		GridBagConstraints gbc_toolBar = new GridBagConstraints();
		gbc_toolBar.insets = new Insets(0, 0, 5, 0);
		gbc_toolBar.gridwidth = 5;
		gbc_toolBar.gridx = 0;
		gbc_toolBar.gridy = 6;
		contentPane.add(toolBar, gbc_toolBar);

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
		JButton btnSalir = new JButton(new ImageIcon("res/left_32.png"));
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
		
		contentPane.add(panel, BorderLayout.CENTER);
	}
}
