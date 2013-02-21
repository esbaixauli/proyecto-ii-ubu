package vista;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.UIManager;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

public class ScrollPanelAtbo extends JPanel {
	private JScrollPane scrollPane;
	private JPanel panelAtbo;
	private int numeroAtributo=0;
	private JButton buttonMas;
	
	public JPanel getPanelAtbo(){
		return panelAtbo;
	}
	
	public ScrollPanelAtbo(String título, Color color){
		super();
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
		JPanel panelAtboInicial = new PanelAtributos(numeroAtributo,panelAtbo);
		panelAtbo.add(panelAtboInicial);
		scrollPane.setViewportView(panelAtbo);
		
		
		buttonMas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				numeroAtributo++;
				panelAtbo.add(new PanelAtributos(numeroAtributo,panelAtbo));
				panelAtbo.repaint();
				scrollPane.repaint();
				panelAtbo.revalidate();
			}

		});
	}
}
