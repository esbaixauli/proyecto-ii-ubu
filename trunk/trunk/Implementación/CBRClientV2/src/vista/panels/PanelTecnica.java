package vista.panels;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import servidorcbr.modelo.Tecnica;
import servidorcbr.modelo.TipoCaso;
import vista.MainFrame;
import vista.tipos.configtecnicas.CombineQueryConfigFrame;
import vista.tipos.configtecnicas.DiverseByMedianConfigFrame;
import vista.tipos.configtecnicas.FilterBasedConfigFrame;
import vista.tipos.configtecnicas.NNConfigFrame;
import vista.tipos.configtecnicas.NumOrCopyConfigFrame;

/**Panel de técnica. Muestra un conjunto de técnicas (Las de una etapa), que pueden
 * ser marcadas como habilitadas para este tipo de caso en concreto y permite
 * escoger la técnica por defecto. Además permite configurar los parámetros de dichas 
 * técnicas mediante ventanas emergentes.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
@SuppressWarnings("serial")
public class PanelTecnica extends JPanel {

	/**
	 * Bundle de internacionalización.
	 */
	private ResourceBundle b = ResourceBundle.getBundle(
            "vista.internacionalizacion.Recursos", Locale.getDefault());
	/**
	 * Lista de técnicas a mostrar.
	 */
	private List<Tecnica> lista;
	/**
	 * Lista de Checkbox de técnica por defecto, hay un checkbox para cada técnica.
	 */
	private List<JCheckBox> checkBoxes;
	/**
	 * Desplegable de técnica por defecto.
	 */
	@SuppressWarnings("rawtypes") //Autogenerado por eclipse.
	private JComboBox comboBoxDef;
	
	/** Crea el panel.
	 * @param tipo Nombre de la etapa. P.Ej:Recuperación. Usado para establecer el título del panel.
	 * @param tecnicas Lista de técnicas a mostrar.
	 * @param defaultTec Técnica escogida inicialmente como "Por defecto".
	 * @param tc Tipo de caso al que se asociará esta lista de técnicas.
	 * @param padre Ventana padre de este panel.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" }) //Conversión implicita en comboboxes autogenerados por eclipse.
	public PanelTecnica(String tipo, List<Tecnica> tecnicas, Tecnica defaultTec, TipoCaso tc, MainFrame padre) {
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
									//ex.printStackTrace();
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
		defPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		add(defPanel, BorderLayout.SOUTH);
		JLabel def = new JLabel(b.getString("default")+":");
		defPanel.add(def);
		defPanel.add(defaultComboBox);
		comboBoxDef = defaultComboBox;
		
		if (defaultTec != null) {
			comboBoxDef.setSelectedItem(defaultTec.getNombre());
		}
	}
	
	/**
	 * Refresca la lista de técnicas según lo introducido por el usuario.
	 * Establece cada técnica de la lista como habilitada si el usuario ha marcado
	 * su checkbox correspondiente.
	 */
	public void actualizaLista () {
		for (int i=0; i<lista.size(); i++) {
			if (checkBoxes.get(i).getText().equals(lista.get(i).getNombre())) {
				lista.get(i).setEnabled(checkBoxes.get(i).isSelected());
			}
		}
	}
	
	/**Obtiene la técnica por defecto.
	 * @return Técnica por defecto.
	 */
	public Tecnica getDefaultTecnica () {
		for (Tecnica t : lista) {
			if (t.getNombre().equals(comboBoxDef.getSelectedItem())) {
				return t;
			}
		}
		return null;
	}
	
	/**Establece el comportamiento de cada botón del panel para que al pulsarse abra
	 * la ventana correspondiente a la técnica que se asocia al botón.
	 * @param b Botón a configurar.
	 * @param t Técnica que se asocia a este botón.
	 * @param tc Tipo de caso que posee esta técnica.
	 * @param padre Ventana padre de este panel.
	 */
	private void configuraBotonConfig(JButton b, final Tecnica t, final TipoCaso tc, final MainFrame padre) {
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
		case "NNretrieval":
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFrame f = new NNConfigFrame(t, padre);
					f.setVisible(true);
					padre.setEnabled(false);
				}
			});
			break;
		case "DirectAttributeCopyMethod":
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFrame f = new NumOrCopyConfigFrame(true,t,tc, padre);
					f.setVisible(true);
					padre.setEnabled(false);
				}
			});
			break;
		case "NumericDirectProportionMethod":
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFrame f = new NumOrCopyConfigFrame(false,t,tc, padre);
					f.setVisible(true);
					padre.setEnabled(false);
				}
			});
			break;	
		case "CombineQueryAndCasesMethod":
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFrame f = new CombineQueryConfigFrame(t, padre);
					f.setVisible(true);
					padre.setEnabled(false);
				}
			});
			break;	
		default:
			b.setEnabled(false);
		}
	}

}
