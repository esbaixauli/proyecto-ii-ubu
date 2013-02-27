package vista;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JToolBar;
import javax.swing.JSeparator;

import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.TipoCaso;
import vista.panels.PanelAtributos;
import vista.panels.ScrollPanelAtbo;

public class NuevoTipoFrame extends JFrame {

	private JFrame me = this;
	private JPanel contentPane;
	private JTextField textField;
	private ResourceBundle b = ResourceBundle.getBundle(
			"vista.internacionalizacion.Recursos", Locale.getDefault());
	private JFrame padre;
	private JButton btnTecnicas;
	
	private JToolBar toolBar;
	private ScrollPanelAtbo panelProblema;
	private ScrollPanelAtbo panelSolucion;
	private JSeparator separator;
	private JSeparator separator_1;
	private JSeparator separator_2;
	
	private TipoCaso tc;

	/**
	 * Create the frame.
	 */
	public NuevoTipoFrame(final JFrame padre) {
		setTitle(b.getString("newcasetype"));
		this.padre = padre;
		tc = new TipoCaso();
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 767, 361);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{136, 120, 139, 0, 350, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 20, 115, 111, 23, 9, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
								
								separator = new JSeparator();
								GridBagConstraints gbc_separator = new GridBagConstraints();
								gbc_separator.insets = new Insets(0, 0, 5, 5);
								gbc_separator.gridx = 0;
								gbc_separator.gridy = 0;
								contentPane.add(separator, gbc_separator);
								
								separator_2 = new JSeparator();
								GridBagConstraints gbc_separator_2 = new GridBagConstraints();
								gbc_separator_2.insets = new Insets(0, 0, 5, 5);
								gbc_separator_2.gridx = 0;
								gbc_separator_2.gridy = 1;
								contentPane.add(separator_2, gbc_separator_2);
						
								JLabel lblNombretipo = new JLabel(b.getString("atname") + ":");
								GridBagConstraints gbc_lblNombretipo = new GridBagConstraints();
								gbc_lblNombretipo.anchor = GridBagConstraints.EAST;
								gbc_lblNombretipo.insets = new Insets(0, 0, 5, 5);
								gbc_lblNombretipo.gridx = 0;
								gbc_lblNombretipo.gridy = 2;
								contentPane.add(lblNombretipo, gbc_lblNombretipo);
				
						textField = new JTextField();
						GridBagConstraints gbc_textField = new GridBagConstraints();
						gbc_textField.anchor = GridBagConstraints.NORTH;
						gbc_textField.fill = GridBagConstraints.HORIZONTAL;
						gbc_textField.insets = new Insets(0, 0, 5, 5);
						gbc_textField.gridx = 1;
						gbc_textField.gridy = 2;
						contentPane.add(textField, gbc_textField);
						textField.setColumns(10);
				
				panelProblema = new ScrollPanelAtbo(b.getString("problem"),Color.RED);
				GridBagConstraints gbc_panelProblema = new GridBagConstraints();
				gbc_panelProblema.fill = GridBagConstraints.BOTH;
				gbc_panelProblema.insets = new Insets(0, 0, 5, 0);
				gbc_panelProblema.gridwidth = 5;
				gbc_panelProblema.gridx = 0;
				gbc_panelProblema.gridy = 3;
				contentPane.add(panelProblema, gbc_panelProblema);
				
				panelSolucion = new ScrollPanelAtbo(b.getString("solution"), Color.BLUE);
				GridBagConstraints gbc_panelSolucion = new GridBagConstraints();
				gbc_panelSolucion.fill = GridBagConstraints.BOTH;
				gbc_panelSolucion.insets = new Insets(0, 0, 5, 0);
				gbc_panelSolucion.gridwidth = 5;
				gbc_panelSolucion.gridx = 0;
				gbc_panelSolucion.gridy = 4;
				contentPane.add(panelSolucion, gbc_panelSolucion);
				

		

		btnTecnicas = new JButton(b.getString("managemethods"));
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
		GridBagConstraints gbc_btnTecnicas = new GridBagConstraints();
		gbc_btnTecnicas.anchor = GridBagConstraints.NORTH;
		gbc_btnTecnicas.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnTecnicas.insets = new Insets(0, 0, 5, 0);
		gbc_btnTecnicas.gridwidth = 5;
		gbc_btnTecnicas.gridx = 0;
		gbc_btnTecnicas.gridy = 5;
		contentPane.add(btnTecnicas, gbc_btnTecnicas);
		
				toolBar = new JToolBar();
				toolBar.setFloatable(false);
				GridBagConstraints gbc_toolBar = new GridBagConstraints();
				gbc_toolBar.insets = new Insets(0, 0, 5, 0);
				gbc_toolBar.gridwidth = 5;
				gbc_toolBar.gridx = 0;
				gbc_toolBar.gridy = 6;
				contentPane.add(toolBar, gbc_toolBar);
				
						JButton btnGuardar = new JButton(new ImageIcon("res/save_32.png"));
						btnGuardar.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								if (comprobarTextField() && comprobarPaneles()) {

								}
							}

							private boolean comprobarTextField() {
								String texto = textField.getText();
								if(texto.isEmpty()){
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
						
						separator_1 = new JSeparator();
						GridBagConstraints gbc_separator_1 = new GridBagConstraints();
						gbc_separator_1.insets = new Insets(0, 0, 0, 5);
						gbc_separator_1.gridx = 3;
						gbc_separator_1.gridy = 7;
						contentPane.add(separator_1, gbc_separator_1);

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
	}
	
	private boolean comprobarPaneles() {
		return comprobarPanel(panelProblema) && comprobarPanel(panelSolucion) && comprobarNombresRepetidos();
	}
	
	private boolean comprobarNombresRepetidos(){
		HashMap<String,String> h = new HashMap<String,String>();
		PanelAtributos p;
		for(int i=0;i< panelProblema.getPanelAtbo().getComponentCount();i++){
			p = (PanelAtributos) panelProblema.getPanelAtbo().getComponent(i);
			Atributo a = p.getAtributo();
			if(a!=null && h.containsKey(a.getNombre())){
				JOptionPane.showMessageDialog(null,
						b.getString("repeatedatt"), "Error",
						JOptionPane.ERROR_MESSAGE);
				return false;
			}else{
			
				h.put(a.getNombre(),a.getNombre());
			}			
		}
		for(int i=0;i< panelSolucion.getPanelAtbo().getComponentCount();i++){
			p = (PanelAtributos) panelSolucion.getPanelAtbo().getComponent(i);
			Atributo a = p.getAtributo();
			if(a!=null && h.containsKey(a.getNombre())){
				JOptionPane.showMessageDialog(null,
						b.getString("repeatedatt"), "Error",
						JOptionPane.ERROR_MESSAGE);
				return false;
			}else{
				h.put(a.getNombre(),a.getNombre());
			}			
		}
		return true;
	}
	
	private boolean comprobarPanel(ScrollPanelAtbo pan ){
		PanelAtributos p;
		for (int i = 0; i < pan.getPanelAtbo().getComponentCount(); i++) {
			p = (PanelAtributos) pan.getPanelAtbo().getComponent(i);
			if (!p.comprobarLleno()) {
				JOptionPane.showMessageDialog(null,
						b.getString("emptyatt"), "Error",
						JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		return true;
	}
	
	private void rellenaTC() {
		tc.setNombre(textField.getText());
		HashMap<String,Atributo> atbos = new HashMap<String,Atributo>();
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
	
	protected JTextField getTextFieldNombre(){
		return textField;
	}
	
	protected ScrollPanelAtbo getPanelProblema(){
		return panelProblema;
	}
	protected ScrollPanelAtbo getPanelSolucion(){
		return panelSolucion;
	}
	

}
