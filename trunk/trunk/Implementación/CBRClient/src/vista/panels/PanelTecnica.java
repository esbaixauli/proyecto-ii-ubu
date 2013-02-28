package vista.panels;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JLabel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;

import servidorcbr.modelo.Tecnica;
import servidorcbr.modelo.TipoCaso;
import vista.configtecnicas.DiverseByMedianConfigFrame;
import vista.configtecnicas.FilterBasedConfigFrame;
import vista.configtecnicas.LuceneConfigFrame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class PanelTecnica extends JPanel {

	private ResourceBundle b = ResourceBundle.getBundle(
            "vista.internacionalizacion.Recursos", Locale.getDefault());
	private List<Tecnica> lista;
	private List<JCheckBox> checkBoxes;
	private JComboBox comboBox;
	
	/**
	 * Create the panel.
	 */
	public PanelTecnica(String tipo, List<Tecnica> tecnicas, Tecnica defaultTec, TipoCaso tc, JFrame padre) {
		lista = tecnicas;
		checkBoxes = new ArrayList<JCheckBox>(tecnicas.size());
		
		setLayout(new BorderLayout(0, 0));
		setBorder(BorderFactory.createTitledBorder(tipo));
		
		final JPanel chkbxPanel = new JPanel();
		add(chkbxPanel, BorderLayout.CENTER);
		chkbxPanel.setLayout(new GridLayout(tecnicas.size(), 2, 1, 1));
		
		final JComboBox defaultComboBox = new JComboBox();
		defaultComboBox.setEditable(false);
		
		int enabled = 0;
		for (Tecnica tecnica : tecnicas) {
			JCheckBox jcb = new JCheckBox(tecnica.getNombre(), tecnica.getEnabled());
			if (tecnica.getEnabled()) {
				enabled++;
			}
			checkBoxes.add(jcb);
			if (tecnicas.size() == 1) {
				jcb.setEnabled(false);
			}
			chkbxPanel.add(jcb);
			
			JButton config = new JButton(b.getString("configuremethod"));
			chkbxPanel.add(config);
			configuraBotonConfig(config, tecnica, tc, padre);
			
			if (tecnica.getEnabled()) {
				defaultComboBox.addItem(tecnica.getNombre());
			}
			
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
		
		if (enabled < 2) {
			for (int i=0; i<tecnicas.size(); i++) {
				if (tecnicas.get(i).getEnabled()) {
					checkBoxes.get(i).setEnabled(false);
				}
			}
		}
		
		JPanel defPanel = new JPanel();
		add(defPanel, BorderLayout.SOUTH);
		JLabel def = new JLabel(b.getString("default")+":");
		defPanel.add(def);
		defPanel.add(defaultComboBox);
		comboBox = defaultComboBox;
		
		if (defaultTec != null) {
			comboBox.setSelectedItem(defaultTec.getNombre());
		}
	}
	
	public void actualizaLista () {
		for (int i=0; i<lista.size(); i++) {
			if (checkBoxes.get(i).getText().equals(lista.get(i).getNombre())) {
				lista.get(i).setEnabled(checkBoxes.get(i).isSelected());
			}
		}
	}
	
	public Tecnica getDefaultTecnica () {
		for (Tecnica t : lista) {
			if (t.getNombre().equals(comboBox.getSelectedItem())) {
				return t;
			}
		}
		return null;
	}
	
	private void configuraBotonConfig(JButton b, final Tecnica t, final TipoCaso tc, final JFrame padre) {
		switch (t.getNombre()) {
		case "DiverseByMedianRetrieval":
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFrame f = new DiverseByMedianConfigFrame(t, tc, padre);
					f.setVisible(true);
					padre.setEnabled(false);
				}
			});
			break;
		case "FilterBasedRetrieval":
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFrame f = new FilterBasedConfigFrame(t, tc, padre);
					f.setVisible(true);
					padre.setEnabled(false);
				}
			});
			break;
		case "LuceneRetrieval":
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFrame f = new LuceneConfigFrame(t, tc, padre);
					f.setVisible(true);
					padre.setEnabled(false);
				}
			});
			break;
		case "NNretrieval":
			break;
		default:
			b.setEnabled(false);
		}
	}

}
