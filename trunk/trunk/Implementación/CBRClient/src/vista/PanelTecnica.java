package vista;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JLabel;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import servidorcbr.modelo.Tecnica;

import java.awt.GridLayout;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class PanelTecnica extends JPanel {

	private ResourceBundle b = ResourceBundle.getBundle(
            "vista.internacionalizacion.Recursos", Locale.getDefault());
	
	/**
	 * Create the panel.
	 */
	public PanelTecnica(String tipo, List<Tecnica> tecnicas) {
		setLayout(new BorderLayout(0, 0));
		setBorder(BorderFactory.createTitledBorder(tipo));
		
		final JPanel chkbxPanel = new JPanel();
		add(chkbxPanel, BorderLayout.CENTER);
		chkbxPanel.setLayout(new GridLayout(tecnicas.size(), 2, 1, 1));
		
		final JComboBox defaultComboBox = new JComboBox();
		defaultComboBox.setEditable(false);
		
		for (Tecnica tecnica : tecnicas) {
			JCheckBox jcb = new JCheckBox(tecnica.getNombre(), true);
			if (tecnicas.size() == 1) {
				jcb.setEnabled(false);
			}
			chkbxPanel.add(jcb);
			
			//JButton config = new JButton(b.getString("configuremethod"));
			JButton config = new JButton("Configurar técnica");
			chkbxPanel.add(config);
			configuraBotonConfig(config, tecnica);
			
			defaultComboBox.addItem(tecnica.getNombre());
			
			jcb.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					JCheckBox jcb = (JCheckBox) e.getSource();
					if (e.getStateChange() == ItemEvent.SELECTED) {
						defaultComboBox.addItem(jcb.getText());
						if (defaultComboBox.getItemCount() == 2) {
							for (Component c : chkbxPanel.getComponents()) {
								if (! c.isEnabled()) {
									c.setEnabled(true);
								}
							}
						}
					} else {
						defaultComboBox.removeItem(jcb.getText());
						if (defaultComboBox.getItemCount() == 1) {
							for (Component c : chkbxPanel.getComponents()) {
								try {
									if (((JCheckBox) c).isSelected()) {
										c.setEnabled(false);
									}
								} catch (ClassCastException ex) {
									ex.printStackTrace();
								}
							}
						}
					}
				}
			});
		}
		
		JPanel defPanel = new JPanel();
		add(defPanel, BorderLayout.SOUTH);
		//JLabel def = new JLabel(b.getString("defaultmethod"));
		JLabel def = new JLabel("Por defecto:");
		defPanel.add(def);
		defPanel.add(defaultComboBox);

	}
	
	private void configuraBotonConfig(JButton b, Tecnica t) {
		switch (t.getNombre()) {
		case "DiverseByMedianRetrieval":
			break;
		case "FilterBasedRetrieval":
			break;
		case "LuceneRetrieval":
			break;
		case "NNretrieval":
			break;
		case "CombineQueryAndCasesMethod":
			break;
		case "DirectAttributeCopyMethod":
			break;
		case "NumericDirectProportionMethod":
			break;
		default:
			b.setEnabled(false);
		}
	}

}
