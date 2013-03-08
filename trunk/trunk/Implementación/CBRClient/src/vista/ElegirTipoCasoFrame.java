package vista;

import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.awt.event.MouseAdapter;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.Usuario;
import vista.panels.ListaCasos;
import controlador.ControlTipos;

@SuppressWarnings("serial")
public class ElegirTipoCasoFrame extends JFrame {

	private JPanel contentPane;
	private int tipo;
	private JFrame padre, me = this;
	private List<TipoCaso> datos;

	public static final int CICLO_CONFIGURADO = 1;
	public static final int CICLO_BASICO = 2;
	public static final int INTRODUCIR_MANUAL = 3;
	public static final int VER_ESTADISTICAS = 4;

	private ResourceBundle b = ResourceBundle.getBundle(
			"vista.internacionalizacion.Recursos", Locale.getDefault());

	/**
	 * Create the frame.
	 */
	public ElegirTipoCasoFrame(Usuario u, JFrame padre, final int tipo) {
		assert (tipo >= 1 && tipo <= 4);
		setIconImage(new ImageIcon("res/logocbr.png").getImage());
		setResizable(false);
		this.padre = padre;
		switch (tipo) {
		case CICLO_CONFIGURADO:
			setTitle(b.getString("configuredcycle"));
			break;
		case CICLO_BASICO:
			setTitle(b.getString("defaultcycle"));
			break;
		case INTRODUCIR_MANUAL:
			setTitle(b.getString("insertcases"));
			break;
		case VER_ESTADISTICAS:
			setTitle(b.getString("stats"));
			break;
		}

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 237, 300);
		this.tipo = tipo;
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		try {
			datos = ControlTipos.obtenerTiposCaso(u);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, b.getString("connecterror"),
					"Error", JOptionPane.ERROR_MESSAGE);
		}

		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane);
		ListaCasos l = new ListaCasos(datos);
		establecerControlLista(l);
		scrollPane.setViewportView(l);
		cierreVentana();
	}

	private void establecerControlLista(ListaCasos l) {

		switch (tipo) {
		case CICLO_CONFIGURADO:
			l.addMouseListener(new MouseAdapter() {

				public void mouseClicked(MouseEvent arg0) {
					@SuppressWarnings("rawtypes")
					JList list = (JList) arg0.getSource();
					if (arg0.getClickCount() >= 2) {
						JFrame f = new IntroducirConsultaCBRFrame(datos
								.get(list.getSelectedIndex()), true, me);
						f.setVisible(true);
						me.setEnabled(false);
					}
				}
			});
			break;
		case CICLO_BASICO:
			l.addMouseListener(new MouseAdapter() {

				public void mouseClicked(MouseEvent arg0) {
					@SuppressWarnings("rawtypes")
					JList list = (JList) arg0.getSource();
					if (arg0.getClickCount() >= 2) {
						JFrame f = new IntroducirConsultaCBRFrame(datos
								.get(list.getSelectedIndex()), false, me);
						f.setVisible(true);
						me.setEnabled(false);
					}
				}
			});
			break;
		case INTRODUCIR_MANUAL:
			;
			break;
		case VER_ESTADISTICAS:
			;
			break;
		}

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
