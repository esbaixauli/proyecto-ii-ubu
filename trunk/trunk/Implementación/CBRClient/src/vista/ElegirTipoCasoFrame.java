package vista;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.Usuario;
import vista.componentes.FrameEstandar;
import vista.panels.ListaCasos;
import controlador.ControlTipos;

@SuppressWarnings("serial")
public class ElegirTipoCasoFrame extends FrameEstandar {

	private JPanel contentPane;
	private int tipo;
	
	private List<TipoCaso> datos;
	private Usuario user;

	public static final int CICLO_CONFIGURADO = 1;
	public static final int CICLO_BASICO = 2;
	public static final int INTRODUCIR_MANUAL = 3;
	public static final int VER_ESTADISTICAS = 4;

	
	/**
	 * Crea el frame.
	 */
	public ElegirTipoCasoFrame(Usuario u, JFrame padre, final int tipo) {
		//Establezco referencias al padre y a la propia ventana
		super(padre);me = this; user = u;
		assert (tipo >= 1 && tipo <= 4);
		
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
		setLocationRelativeTo(padre);
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
								.get(list.getSelectedIndex()), true, me, user);
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
								.get(list.getSelectedIndex()), false, me, user);
						f.setVisible(true);
						me.setEnabled(false);
					}
				}
			});
			break;
		case INTRODUCIR_MANUAL:
			l.addMouseListener(new MouseAdapter() {

				public void mouseClicked(MouseEvent arg0) {
					@SuppressWarnings("rawtypes")
					JList list = (JList) arg0.getSource();
					if (arg0.getClickCount() >= 2) {
						JFrame f = new InsertarCasoFrame(datos.get(list.getSelectedIndex()),me);
						f.setVisible(true);
						me.setEnabled(false);
					}
				}
			});
			break;
		case VER_ESTADISTICAS:
			;
			break;
		}

	}
	
	

	
}
