package vista;

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
import vista.componentes.FrameEstandar;
import vista.panels.PanelAtributos;
import vista.panels.ScrollPanelAtbo;
import controlador.ControlTipos;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import javax.swing.border.EtchedBorder;

@SuppressWarnings("serial")
public class NuevoTipoFrame extends FrameEstandar {
	private JPanel contentPane;
	private JTextField textField;
	private JButton btnTecnicas;
	private ScrollPanelAtbo panelProblema;
	private ScrollPanelAtbo panelSolucion;
	JButton btnGuardar;

	protected TipoCaso tc;
	private JPanel panel;
	private JPanel panel_1;
	private JPanel panel_2;
	private JPanel panel_3;

	/**
	 * Create the frame.
	 */
	public NuevoTipoFrame(final JFrame padre) {
		super(padre);me=this;
		setTitle(b.getString("newcasetype"));
		tc = new TipoCaso();
		setBounds(100, 100, 767, 389);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
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

		panelSolucion = new ScrollPanelAtbo(b.getString("solution"), Color.BLUE,false);
		panel_1.add(panelSolucion);
		
		panel_2 = new JPanel();
		contentPane.add(panel_2, BorderLayout.SOUTH);
				GridBagLayout gbl_panel_2 = new GridBagLayout();
				gbl_panel_2.columnWidths = new int[]{745, 0};
				gbl_panel_2.rowHeights = new int[]{23, 50, 0};
				gbl_panel_2.columnWeights = new double[]{0.0, Double.MIN_VALUE};
				gbl_panel_2.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
				panel_2.setLayout(gbl_panel_2);
				
						btnTecnicas = new JButton(b.getString("managemethods"));
						GridBagConstraints gbc_btnTecnicas = new GridBagConstraints();
						gbc_btnTecnicas.fill = GridBagConstraints.HORIZONTAL;
						gbc_btnTecnicas.insets = new Insets(0, 0, 5, 0);
						gbc_btnTecnicas.gridx = 0;
						gbc_btnTecnicas.gridy = 0;
						panel_2.add(btnTecnicas, gbc_btnTecnicas);
						btnTecnicas.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								if (comprobarPaneles()) {
									rellenaTC();
									JFrame f = new GestionTecnicasFrame(tc, me);
									f.setVisible(true);
									me.setEnabled(false);
									
								}
							}
						});
				
				panel_3 = new JPanel();
				panel_3.setBackground(Color.GRAY);
				panel_3.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
				GridBagConstraints gbc_panel_3 = new GridBagConstraints();
				gbc_panel_3.fill = GridBagConstraints.HORIZONTAL;
				gbc_panel_3.gridx = 0;
				gbc_panel_3.gridy = 1;
				panel_2.add(panel_3, gbc_panel_3);
						
								btnGuardar = new JButton(new ImageIcon("res/save_32.png"));
								panel_3.add(btnGuardar);
								btnGuardar.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										if( tc.getTecnicasRecuperacion()==null){
											JOptionPane.showMessageDialog(null,
													b.getString("notconfigured"), "Error",
													JOptionPane.ERROR_MESSAGE);
											return;
										}
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
											try{
											//Inserto el tipo de caso
											ControlTipos.addTipo(tc);
											}catch(java.io.IOException exc){
												JOptionPane.showMessageDialog(null,
														b.getString("inserterror"), "Error",
														JOptionPane.ERROR_MESSAGE);
												exc.printStackTrace();
											}
											//Activo al padre y cierro la ventana actual
											padre.setEnabled(true);
											me.dispose();
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
						JButton btnSalir = new JButton(new ImageIcon("res/left_32.png"));
						panel_3.add(btnSalir);
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

		removeWindowListener(getWindowListeners()[0]);
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				if (JOptionPane.showConfirmDialog(null,
						b.getString("exitnosave"), b.getString("exitnosave"),
						JOptionPane.YES_NO_OPTION) == 0) {
					padre.setEnabled(true);
					padre.setVisible(true);
					me.dispose();
				}
			}
		});
		setLocationRelativeTo(padre);
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
