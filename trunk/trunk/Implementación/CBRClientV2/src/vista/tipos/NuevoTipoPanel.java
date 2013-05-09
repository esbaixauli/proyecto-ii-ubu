package vista.tipos;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;

import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.TipoCaso;
import vista.MainFrame;
import vista.componentesGenericos.FrameEstandar;
import vista.componentesGenericos.PanelEstandar;
import vista.panels.PanelAtributos;
import vista.panels.ScrollPanelAtbo;
import controlador.ControlTipos;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import javax.swing.border.EtchedBorder;

@SuppressWarnings("serial")
public class NuevoTipoPanel extends PanelEstandar {
	private JPanel contentPane;
	private JTextField textField;
	private ScrollPanelAtbo panelProblema;
	private ScrollPanelAtbo panelSolucion;
	JButton btnSiguiente;

	protected TipoCaso tc;
	private JPanel panel;
	private JPanel panel_1;
	private JPanel panel_2;
	private JPanel panel_3;
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
		
				textField = new JTextField();
				panel.add(textField);
				textField.setColumns(10);
		
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
											tc.setNombre(textField.getText().trim());
											
											rellenaTC();
											//Configuro las tecnicas
											padre.addPanel(b.getString("managemethods")+": "+tc.getNombre(), 
													new GestionTecnicasPanel(tc, padre, true));
											
											padre.removePanel(getName());
											
										}
									}

									private boolean comprobarTextField() {
										String texto = textField.getText();
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

	private boolean comprobarPaneles() {
		return comprobarPanel(panelProblema) && comprobarPanel(panelSolucion)
				&& comprobarNombresRepetidos();
	}

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

	protected void rellenaTC() {
		tc.setNombre(textField.getText().substring(0,Math.min(18,textField.getText().length())));
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

	protected JTextField getTextFieldNombre() {
		return textField;
	}

	protected ScrollPanelAtbo getPanelProblema() {
		return panelProblema;
	}

	protected ScrollPanelAtbo getPanelSolucion() {
		return panelSolucion;
	}

}
