package vista.tipos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.TipoCaso;
import vista.MainFrame;
import vista.componentesGenericos.PanelEstandar;
import vista.panels.PanelAtributos;
import vista.panels.ScrollPanelAtbo;

/**Panel general de nuevo tipo de caso. Contiene todos los subcomponentes
 * necesarios para gestionar la creación de un nuevo tipo.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
/**
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
@SuppressWarnings("serial")
public class NuevoTipoPanel extends PanelEstandar {
	/**
	 * Contentpane del panel.
	 */
	private JPanel contentPane;
	/**
	 * Textfield del nombre del tipo.
	 */
	private JTextField textFieldN;
	/**
	 * Panel de atributos de problema.
	 */
	private ScrollPanelAtbo panelProblema;
	/**
	 * Panel de atributos de solución.
	 */
	private ScrollPanelAtbo panelSolucion;
	/**
	 * Botón de continuar con el trámite.
	 */
	JButton btnSiguiente;

	/**
	 * Tipo de caso que se está generando.
	 */
	protected TipoCaso tc;
	/**
	 * Subpanel para la mejor distribución visual del panel.
	 */
	private JPanel panel;
	/**
	 * Subpanel para la mejor distribución visual del panel.
	 */
	private JPanel panel_1;
	/**
	 * Subpanel para la mejor distribución visual del panel.
	 */
	private JPanel panel_2;
	/**
	 * Subpanel para la mejor distribución visual del panel.
	 */
	private JPanel panel_3;
	/**
	 * Subpanel para la mejor distribución visual del panel.
	 */
	private JPanel panel_4;

	/**
	 * Create the frame.
	 */
	public NuevoTipoPanel(final MainFrame padre) {
		super(padre);me=this;
		setName(b.getString("newcasetype"));
		tc = new TipoCaso();
		setBounds(100, 100, 825, 474);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		add(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		panel = new JPanel();
		panel.setBackground(Color.GRAY);
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		contentPane.add(panel, BorderLayout.NORTH);
				
						JLabel lblNombretipo = new JLabel(b.getString("atname") + ":");
						lblNombretipo.setForeground(Color.WHITE);
						panel.add(lblNombretipo);
		
				textFieldN = new JTextField();
				panel.add(textFieldN);
				textFieldN.setColumns(10);
		
		panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.CENTER);
				panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));
		
				panelProblema = new ScrollPanelAtbo(b.getString("problem"), Color.RED,true);
				panel_1.add(panelProblema);
		
		panel_4 = new JPanel();
		panel_1.add(panel_4);

		panelSolucion = new ScrollPanelAtbo(b.getString("solution"), Color.BLUE,false);
		panel_1.add(panelSolucion);
		
		panel_2 = new JPanel();
		contentPane.add(panel_2, BorderLayout.SOUTH);
				GridBagLayout gbl_panel_2 = new GridBagLayout();
				gbl_panel_2.columnWidths = new int[]{745, 0};
				gbl_panel_2.rowHeights = new int[]{0, 23, 50, 0};
				gbl_panel_2.columnWeights = new double[]{0.0, Double.MIN_VALUE};
				gbl_panel_2.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
				panel_2.setLayout(gbl_panel_2);
				
				panel_3 = new JPanel();
				panel_3.setBackground(Color.GRAY);
				panel_3.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
				GridBagConstraints gbc_panel_3 = new GridBagConstraints();
				gbc_panel_3.fill = GridBagConstraints.HORIZONTAL;
				gbc_panel_3.gridx = 0;
				gbc_panel_3.gridy = 2;
				panel_2.add(panel_3, gbc_panel_3);
						JButton btnSalir = new JButton(new ImageIcon("res/left_32.png"));
						btnSalir.setText(b.getString("exit"));
						panel_3.add(btnSalir);
						
								btnSiguiente = new JButton(new ImageIcon("res/ok_32.png"));
								btnSiguiente.setText(b.getString("managemethods"));
								panel_3.add(btnSiguiente);
								btnSiguiente.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										
										if (comprobarTextField() && comprobarPaneles() ) {
											HashMap<String, Atributo> atbos = new HashMap<String,Atributo>();
											//Obtengo los atributos del problema
											for (int i = 0; i < panelProblema.getPanelAtbo()
													.getComponentCount(); i++) {
												Atributo actual = ((PanelAtributos) panelProblema
														.getPanelAtbo().getComponent(i)).getAtributo();
												actual.setEsProblema(true);
												atbos.put(actual.getNombre(),actual);
											}
											//Obtengo los atributos de la solución
											for (int i = 0; i < panelSolucion.getPanelAtbo()
													.getComponentCount(); i++) {
												Atributo actual = ((PanelAtributos) panelSolucion
														.getPanelAtbo().getComponent(i)).getAtributo();
												actual.setEsProblema(false);
												atbos.put(actual.getNombre(),actual);
											}
											//Establezco los atributos y el nombre del tipo de caso
											tc.setAtbos(atbos);
											tc.setNombre(textFieldN.getText().trim());
											
											rellenaTC();
											//Configuro las tecnicas
											padre.addPanel(b.getString("managemethods")+": "+tc.getNombre(), 
													new GestionTecnicasPanel(tc, padre, true));
											
											padre.removePanel(getName());
											
										}
									}

									/** Comprueba si el textfield de nombre es válido.
									 * @return true si es válido, false si no.
									 */
									private boolean comprobarTextField() {
										String texto = textFieldN.getText();
										if (texto.isEmpty()) {
											JOptionPane.showMessageDialog(null,
													b.getString("incorrectformat"), "Error",
													JOptionPane.ERROR_MESSAGE);
											return false;
										}
										for (int i = 0; i < texto.length(); i++) {
											if (!Character.isLetterOrDigit(texto.charAt(i))) {
												JOptionPane.showMessageDialog(null,
														b.getString("incorrectformat"), "Error",
														JOptionPane.ERROR_MESSAGE);
												return false;
											}
										}
										return true;
									}
								});
						btnSalir.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {

								if (JOptionPane.showConfirmDialog(null,
										b.getString("exitnosave"), b.getString("exitnosave"),
										JOptionPane.YES_NO_OPTION) == 0) {
									padre.setEnabled(true);
									padre.setVisible(true);
									padre.removePanel(me.getName());
								}
							}
						});

		
	}

	/**Comprueba la validez del contenido de los paneles de atributos de problema y 
	 * solución.
	 * @return true si ambos son válidos, false si no.
	 */
	private boolean comprobarPaneles() {
		return comprobarPanel(panelProblema) && comprobarPanel(panelSolucion)
				&& comprobarNombresRepetidos();
	}

	/**Comprueba si hay nombres de atributo repetidos.
	 * @return true si NO hay nombres repetidos y por tanto todos los nombres son
	 * válidos, false si alguno no es válido.
	 */
	private boolean comprobarNombresRepetidos() {
		HashMap<String, String> h = new HashMap<String, String>();
		PanelAtributos p;
		for (int i = 0; i < panelProblema.getPanelAtbo().getComponentCount(); i++) {
			p = (PanelAtributos) panelProblema.getPanelAtbo().getComponent(i);
			Atributo a = p.getAtributo();
			if (a != null && h.containsKey(a.getNombre())) {
				JOptionPane.showMessageDialog(null, b.getString("repeatedatt"),
						"Error", JOptionPane.ERROR_MESSAGE);
				return false;
			} else {

				h.put(a.getNombre(), a.getNombre());
			}
		}
		for (int i = 0; i < panelSolucion.getPanelAtbo().getComponentCount(); i++) {
			p = (PanelAtributos) panelSolucion.getPanelAtbo().getComponent(i);
			Atributo a = p.getAtributo();
			if (a != null && h.containsKey(a.getNombre())) {
				JOptionPane.showMessageDialog(null, b.getString("repeatedatt"),
						"Error", JOptionPane.ERROR_MESSAGE);
				return false;
			} else {
				h.put(a.getNombre(), a.getNombre());
			}
		}
		return true;
	}

	/** Comprueba que un panel (problema o solución) haya sido rellenado correctamente.
	 * @param pan Panel de problema o solución a comprobar.
	 * @return cierto si el panel ha sido bien rellenado, false si no.
	 */
	private boolean comprobarPanel(ScrollPanelAtbo pan) {
		PanelAtributos p;
		for (int i = 0; i < pan.getPanelAtbo().getComponentCount(); i++) {
			p = (PanelAtributos) pan.getPanelAtbo().getComponent(i);
			if (!p.comprobarLleno()) {
				JOptionPane.showMessageDialog(null, b.getString("emptyatt"),
						"Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		return true;
	}

	/**
	 * Rellena el tipo de caso asociado a este panel. Asume que se ha comprobado
	 * previamente que todos los componentes de este panel han sido rellenados por
	 * el usuario correctamente.
	 */
	protected void rellenaTC() {
		tc.setNombre(textFieldN.getText().substring(0,Math.min(18,textFieldN.getText().length())));
		HashMap<String, Atributo> atbos = new HashMap<String, Atributo>();
		for (Component c : panelProblema.getPanelAtbo().getComponents()) {
			PanelAtributos p = (PanelAtributos) c;
			Atributo a = p.getAtributo();
			a.setEsProblema(true);
			atbos.put(a.getNombre(), a);
		}
		for (Component c : panelSolucion.getPanelAtbo().getComponents()) {
			PanelAtributos p = (PanelAtributos) c;
			Atributo a = p.getAtributo();
			a.setEsProblema(false);
			atbos.put(a.getNombre(), a);
		}
		tc.setAtbos(atbos);
	}

	/**Obtiene el textfield de nombre del tipo.
	 * @return Textfield de nombre de tipo de caso.
	 */
	protected JTextField getTextFieldNombre() {
		return textFieldN;
	}

	/**Obtiene el panel de atributos del problema.
	 * @return Panel de atributos del problema.
	 */
	protected ScrollPanelAtbo getPanelProblema() {
		return panelProblema;
	}

	/**Obtiene el panel de atributos de la solución.
	 * @return Panel de atributos de la solución.
	 */
	protected ScrollPanelAtbo getPanelSolucion() {
		return panelSolucion;
	}

}
