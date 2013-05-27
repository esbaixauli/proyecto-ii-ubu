package vista;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.IOException;
import java.util.List;

import javax.swing.BoxLayout;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.Usuario;
import vista.cicloCBR.IntroducirConsultaCBRPanel;
import vista.componentesGenericos.FrameEstandar;
import vista.panels.ListaCasos;
import controlador.ControlTipos;

/** Frame genérico para escoger un tipo de caso de una lista. Reutilizable,
 * ya que un valor estático indica qué acción se realizará al escoger
 * un elemento.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
@SuppressWarnings("serial")
public class ElegirTipoCasoFrame extends FrameEstandar {

	/**
	 * Contentpane del panel
	 */
	private JPanel contentPane;
	/**
	 * Tipo de acción a realizar. Debe tener como valor una de las
	 * siguientes constantes:
	 * CICLO_CONFIGURADO,CICLO_BASICO,INTRODUCIR_MANUAL,VER_ESTADISTICAS.
	 */
	private int tipo;
	
	/**
	 * Casos disponibles para mostrar.
	 */
	private List<TipoCaso> datos;
	
	/**
	 * Usuario que consulta los casos. 
	 */
	private Usuario user;

	/**
	 * Constante para ciclo configurado.
	 */
	public static final int CICLO_CONFIGURADO = 1;
	/**
	 * Constante para ciclo básico.
	 */
	public static final int CICLO_BASICO = 2;
	/**
	 * Constante para introducir manualmente.
	 */
	public static final int INTRODUCIR_MANUAL = 3;
	/**
	 * Constante para ver las estadísticas.
	 */
	public static final int VER_ESTADISTICAS = 4;

	
	
	/**Constructor del frame.
	 * @param u Usuario que está escogiendo un tipo de caso.
	 * @param padre Frame padre de este.
	 * @param tipo Tipo de acción a realizar.Debe tener como valor una de las
	 * siguientes constantes, disponibles en esta clase:
	 * CICLO_CONFIGURADO,CICLO_BASICO,INTRODUCIR_MANUAL,VER_ESTADISTICAS.
	 */
	public ElegirTipoCasoFrame(Usuario u, MainFrame padre, final int tipo) {
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

	/**Establece la acción a realizar por la lista al elegir un elemento.
	 * @param l Lista de casos.
	 */
	private void establecerControlLista(ListaCasos l) {

		switch (tipo) {
		case CICLO_CONFIGURADO:
			l.addMouseListener(new MouseAdapter() {

				public void mouseClicked(MouseEvent arg0) {
					@SuppressWarnings("rawtypes")
					JList list = (JList) arg0.getSource();
					if (arg0.getClickCount() >= 2) {
						IntroducirConsultaCBRPanel p = new IntroducirConsultaCBRPanel(datos
								.get(list.getSelectedIndex()), true, padre, user);
						padre.addPanel(p.getName(),p);
						me.setVisible(false);
						me.dispose();
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
						IntroducirConsultaCBRPanel p = new IntroducirConsultaCBRPanel(datos
								.get(list.getSelectedIndex()),false, padre, user);
						padre.addPanel(p.getName(),p);
						me.setVisible(false);
						me.dispose();
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
						
						padre.addPanel(new InsertarCasoPanel(datos.get(list.getSelectedIndex()),padre));
						padre.setEnabled(true);
						
						me.setVisible(false);
						me.dispose();
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
