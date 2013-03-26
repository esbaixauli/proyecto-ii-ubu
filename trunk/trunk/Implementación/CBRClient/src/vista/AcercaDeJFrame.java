package vista;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import vista.componentes.FrameEstandar;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.border.EtchedBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class AcercaDeJFrame extends JFrame {

	private JPanel contentPane;
	private JFrame me=this,padre;
	private ResourceBundle b = ResourceBundle.getBundle(
			"vista.internacionalizacion.Recursos", Locale.getDefault());

	/**
	 * Crea el frame.
	 */
	public AcercaDeJFrame(JFrame padre) {
		this.padre=padre;me=this;
		setResizable(false);
		setTitle(b.getString("about"));
		setMenuBar(null);
		setIconImage(new ImageIcon("res/logocbr.png").getImage());
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.WEST);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JLabel lblLogoUBU = new JLabel(new ImageIcon("res/logoubu.png"));
		panel.add(lblLogoUBU);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		contentPane.add(panel_1, BorderLayout.SOUTH);
		
		JButton btnHadoop = new JButton(new ImageIcon("res/hadoop_64.png"));
		btnHadoop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				navegarEnlace("http://hadoop.apache.org/");
			}
		});
		panel_1.add(btnHadoop);
		
		JButton btnJcolibri = new JButton(new ImageIcon("res/jcolibri_64.png"));
		btnJcolibri.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				navegarEnlace("http://gaia.fdi.ucm.es/research/colibri/jcolibri");
			}
		});
		panel_1.add(btnJcolibri);
		
		JButton btnJfreechart = new JButton(new ImageIcon("res/jfreechart_64.png"));
		btnJfreechart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				navegarEnlace("http://www.jfree.org/jfreechart/");
			}
		});
		panel_1.add(btnJfreechart);
		
		JButton btnWeka = new JButton(new ImageIcon("res/weka_64.png"));
		btnWeka.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				navegarEnlace("http://www.cs.waikato.ac.nz/ml/weka/");
			}
		});
		panel_1.add(btnWeka);
		
		JButton btnJtattoo = new JButton(new ImageIcon("res/jtattoo_64.png"));
		btnJtattoo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				navegarEnlace("http://www.jtattoo.net/");
			}
		});
		panel_1.add(btnJtattoo);

		cierreVentana();
	}
	
	/**
	 * Permite abrir una URL en el navegador por defecto del SO, si este lo permite.
	 * @param url Cadena con la url a abrir.
	 */
	private void navegarEnlace(String url){
		 try {
	         java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
	       }//Si el SO no lo permite, simplemente el link no funciona.
	       catch (Exception e) {}
	}
	
	
	/**
	 * Gestiona el cierre de esta ventana.
	 */
	private void cierreVentana() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				padre.setEnabled(true);
				padre.toFront();
				me.setVisible(false);
				me.dispose();
			}
		});
	}

}
