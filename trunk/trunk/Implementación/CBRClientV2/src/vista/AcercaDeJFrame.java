package vista;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.Color;

/**Frame de 'Acerca de...'
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
@SuppressWarnings("serial")
public class AcercaDeJFrame extends JFrame {

	/**
	 * Content pane del frame.
	 */
	private JPanel contentPane;
	/**
	 * Referencia al frame.
	 */
	private JFrame me=this;
	
	/**
	 * Referencia al padre del frame. 
	 */
	private JFrame padre;
	/**
	 * Bundle de localización.
	 */
	private ResourceBundle b = ResourceBundle.getBundle(
			"vista.internacionalizacion.Recursos", Locale.getDefault());

	
	/**Crea el frame.
	 * @param padre Padre de este frame.
	 */
	public AcercaDeJFrame(JFrame padre) {
		this.padre=padre;me=this;
		setResizable(false);
		setTitle(b.getString("about"));
		setMenuBar(null);
		setIconImage(new ImageIcon("res/logocbr.png").getImage());
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 675, 300);
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
		panel_1.setBackground(Color.GRAY);
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
		
		JButton btnitext = new JButton(new ImageIcon("res/itext_64.png"));
		btnitext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				navegarEnlace("http://itextpdf.com/");
			}
		});
		panel_1.add(btnitext);
		
		JButton btnOxygen = new JButton(new ImageIcon("res/oxygen_64.png"));
		btnOxygen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				navegarEnlace("http://www.oxygen-icons.org/");
			}
		});
		panel_1.add(btnOxygen);
		
		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("1px"),
				ColumnSpec.decode("12px"),
				ColumnSpec.decode("499px"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				RowSpec.decode("1px"),
				RowSpec.decode("66px"),
				RowSpec.decode("14px"),
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("14px"),
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("14px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,
				RowSpec.decode("14px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JSeparator separator = new JSeparator();
		panel_2.add(separator, "1, 1, center, center");
		
		JLabel lblUniversidadDeBurgos = new JLabel(b.getString("ubu"));
		panel_2.add(lblUniversidadDeBurgos, "3, 3, center, center");
		
		JSeparator separator_1 = new JSeparator();
		panel_2.add(separator_1, "1, 1, center, center");
		
		JLabel label = new JLabel("2012-2013");
		panel_2.add(label, "3, 5, center, center");
		
		JLabel lblAutores = new JLabel(b.getString("authors")+": Rubén Antón García, Enrique Sainz Baixauli");
		panel_2.add(lblAutores, "3, 7, center, center");
		
		JLabel lblTutores = new JLabel(b.getString("tutors")+": Álvaro Herrero, Belén Vaquerizo");
		panel_2.add(lblTutores, "3, 9, center, center");

		cierreVentana();
		setLocationRelativeTo(padre);
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
