package vista.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import servidorcbr.modelo.Atributo;

/**Panel que muestra los atributos cuando se crea/modifica un tipo de caso.
 * Posee un scrollpane interno que muestra el panel de cada uno de los atributos,
 * y un botón para añadir paneles de atributo.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
@SuppressWarnings("serial")
public class ScrollPanelAtbo extends JPanel {
	/**
	 * Scrollpane interno para mostrar los atributos creados.
	 */
	private JScrollPane scrollPane;
	/**
	 * Panel interno que se halla como viewport del scroll.
	 */
	private JPanel panelAtbo;
	/**
	 * Número del último atributo colocado. Usado para mostrar una etiqueta
	 * "Atributo 0,1,n" en cada atributo.
	 */
	private int numeroAtributo=0;
	/**
	 * Botón para añadir atributos.
	 */
	private JButton buttonMas;
	/**
	 * Panel del atributo 0. El atributo 0 no puede ser borrado bajo ninguna
	 * circunstancia (Tanto el problema como la solución han de tener al menos un
	 * atributo).
	 */
	private JPanel panelAtboInicial;
	
	
	/**Devuelve el panel en viewport del scrollpane.
	 * @return panel de atributo.
	 */
	public JPanel getPanelAtbo(){
		return panelAtbo;
	}
	
	
	
	/** Crea el panel.
	 * @param título Título del panel
	 * @param color Color del título del panel.
	 * @param problema true si el panel muestra atributos de tipo problema,
	 * false si no.
	 */
	public ScrollPanelAtbo(String título, Color color,final boolean problema){
		super();
		setPreferredSize(new Dimension(500,160));
		setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), título, TitledBorder.LEADING, TitledBorder.TOP, null, color));
		scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.gridwidth = 4;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 1;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		buttonMas = new JButton(new ImageIcon("res/plus_16.png"));
		add(buttonMas);
		this.add(scrollPane);

		panelAtbo = new JPanel();
		panelAtbo.setLayout(new BoxLayout(panelAtbo, BoxLayout.Y_AXIS));
		panelAtboInicial = new PanelAtributos(numeroAtributo,panelAtbo,scrollPane,problema);

		panelAtbo.add(panelAtboInicial);
		scrollPane.setViewportView(panelAtbo);
		
		
		buttonMas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				numeroAtributo++;
				panelAtbo.add(new PanelAtributos(numeroAtributo,panelAtbo,scrollPane,problema));
				repintar();
			}

		});
		
	
	}
	
	/**
	 * Repinta y revalida los componentes internos del panel. No se realiza sobreescribiendo
	 * el método repaint para tener más control de cuándo se realiza esta tarea.
	 */
	private void repintar(){
		panelAtbo.repaint();
		scrollPane.repaint();
		panelAtbo.revalidate();
	}
	
	/**
	 * Desactiva los componentes para añadir atributos del panel.
	 */
	public void desactivarComponentes(){
		buttonMas.setEnabled(false);
		panelAtbo.remove(panelAtboInicial);
		repintar();
	}
	
	/**Añade programáticamente un atributo.
	 * @param a El atributo a añadir
	 * @param desactivar cierto si se desea desactivar las partes del panel no editable, 
	 * false si no.
	 */
	public void addAtributo(Atributo a, boolean desactivar){
		numeroAtributo++;
		PanelAtributos p = new PanelAtributos(numeroAtributo,panelAtbo,scrollPane,a.getEsProblema());
		p.setAtributo(a, desactivar);
		panelAtbo.add(p);
		repintar();
		
	}
}
