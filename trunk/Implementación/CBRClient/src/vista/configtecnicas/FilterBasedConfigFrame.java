package vista.configtecnicas;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.Parametro;
import servidorcbr.modelo.Tecnica;
import servidorcbr.modelo.TipoCaso;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.awt.GridBagLayout;
import javax.swing.JSeparator;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.JComboBox;

import controlador.ControlTecnicas;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FilterBasedConfigFrame extends JFrame {

	private JPanel contentPane;
	private ResourceBundle b = ResourceBundle.getBundle(
            "vista.internacionalizacion.Recursos", Locale.getDefault());
	private final JFrame me = this;
	private final NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());
	private final List<JTextField> textFields;
	private final List<JComboBox<String>> comboBoxes;
	private List<String> atbos;

	/**
	 * Create the frame.
	 */
	public FilterBasedConfigFrame(final Tecnica t, TipoCaso tc, final JFrame padre) {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				padre.setEnabled(true);
				me.setVisible(false);
				me.dispose();
			}
		});
		setBounds(100, 100, 350, 300);
		setTitle("Filter based retrieval");
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		//JLabel lblPredicados = new JLabel(b.getString("attpredicates"));
		JLabel lblPredicados = new JLabel("Predicados");
		contentPane.add(lblPredicados, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		
		JButton btnOk = new JButton(b.getString("ok"));
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<Parametro> params = new ArrayList<Parametro>(atbos.size());
				try {
					for (int i=0; i<atbos.size(); i++) {
						Parametro p = new Parametro();
						p.setNombre(atbos.get(i));
						if (comboBoxes.get(i).getSelectedIndex() == 0) {
							p = null;
						} else if (comboBoxes.get(i).getSelectedIndex() < 8) {
							// resta 1 porque el 0 significa "ningún filtro para este atbo"
							p.setValor(comboBoxes.get(i).getSelectedIndex() - 1);
						} else {
							if (textFields.get(i).getText().equals(""))
								throw new Exception("empty");
							double d = nf.parse(textFields.get(i).getText()).doubleValue();
							if (d <= 0) {
								throw new Exception("negative");
							}
							p.setValor(d+7);
						}
						if (p != null) {
							params.add(p);
						}
					}
					t.setParams(params);
					padre.setEnabled(true);
					me.dispose();
				} catch (Exception ex) {
					if (ex.getMessage().equals("empty")) {
						JOptionPane.showMessageDialog(null, 
								b.getString("emptyfield"),"Error", JOptionPane.ERROR_MESSAGE);
					} else {
						//JOptionPane.showMessageDialog(null, 
						//		b.getString("negativethreshold"),"Error", JOptionPane.ERROR_MESSAGE);
						JOptionPane.showMessageDialog(null, 
								"Los umbrales tienen que ser positivos","Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		me.getRootPane().setDefaultButton(btnOk);
		panel.add(btnOk);
		
		JButton btnCancel = new JButton(b.getString("cancel"));
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				padre.setEnabled(true);
				me.dispose();
			}
		});
		panel.add(btnCancel);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.CENTER);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{0, 0, 0, 0};
		gbl_panel_1.rowHeights = new int[]{0, 0, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		int i=0;
		textFields = new ArrayList<JTextField>(tc.getAtbos().size());
		comboBoxes = new ArrayList<JComboBox<String>>(tc.getAtbos().size());
		atbos = new ArrayList<String>(tc.getAtbos().size());
		for (Atributo a : tc.getAtbos().values()) {
			if (a.getEsProblema()) {
				atbos.add(a.getNombre());

				JSeparator separator = new JSeparator();
				GridBagConstraints gbc_separator = new GridBagConstraints();
				gbc_separator.insets = new Insets(0, 0, 5, 5);
				gbc_separator.gridx = 0;
				gbc_separator.gridy = i;
				panel_1.add(separator, gbc_separator);

				i++;

				JLabel lblAtbo = new JLabel(a.getNombre());
				GridBagConstraints gbc_lblAtbo = new GridBagConstraints();
				gbc_lblAtbo.anchor = GridBagConstraints.WEST;
				gbc_lblAtbo.insets = new Insets(0, 0, 0, 5);
				gbc_lblAtbo.gridx = 1;
				gbc_lblAtbo.gridy = i;
				panel_1.add(lblAtbo, gbc_lblAtbo);

				JComboBox<String> comboBox = new JComboBox<String>();
				GridBagConstraints gbc_comboBox = new GridBagConstraints();
				gbc_comboBox.insets = new Insets(0, 0, 0, 5);
//				gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
				gbc_comboBox.anchor = GridBagConstraints.WEST;
				gbc_comboBox.gridx = 2;
				gbc_comboBox.gridy = i;
				//comboBox.addItem("["+b.getString("none")+"]");
				comboBox.addItem("[Ninguno]");
				for (String predicado : ControlTecnicas.getFilterPredicates()) {
					comboBox.addItem(predicado);
				}
				panel_1.add(comboBox, gbc_comboBox);
				comboBox.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent arg0) {
						int i = comboBoxes.indexOf(arg0.getSource());
						if (arg0.getItem().toString().equals("Threshold")) {
							textFields.get(i).setEnabled(true);
						} else {
							textFields.get(i).setEnabled(false);
						}
					}
				});
				comboBoxes.add(comboBox);

				JFormattedTextField tff = new JFormattedTextField();
				tff.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat())));
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.insets = new Insets(0, 5, 0, 5);
				gbc_textField.anchor = GridBagConstraints.WEST;
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.gridx = 3;
				gbc_textField.gridy = i;
				panel_1.add(tff, gbc_textField);
				tff.setColumns(10);
				tff.setEnabled(false);
				textFields.add(tff);
				
				if (t.getParams() != null) {
					for (Parametro p : t.getParams()) {
						if (p.getNombre().equals(a.getNombre())) {
							if (p.getValor() < 7) {
								comboBox.setSelectedIndex((int) p.getValor()+1);
							} else {
								comboBox.setSelectedIndex(8);
								tff.setText(nf.format(p.getValor()-7));
							}
						}
					}
				}

				i++;
			}
		}
	}

}
