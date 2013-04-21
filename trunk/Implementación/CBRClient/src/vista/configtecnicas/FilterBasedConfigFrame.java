package vista.configtecnicas;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.Parametro;
import servidorcbr.modelo.Tecnica;
import servidorcbr.modelo.TipoCaso;
import vista.componentes.FrameEstandar;
import controlador.ControlTecnicas;
import java.awt.Color;
import javax.swing.border.EtchedBorder;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

@SuppressWarnings("serial")
public class FilterBasedConfigFrame extends FrameEstandar {

	private JPanel contentPane;
	private ResourceBundle b = ResourceBundle.getBundle(
            "vista.internacionalizacion.Recursos", Locale.getDefault());
	private final NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());
	private final List<JTextField> textFields;
	private final List<JComboBox> comboBoxes;
	private List<String> atbos;
	

	/**
	 * Crea el frame.
	 */
	public FilterBasedConfigFrame(final Tecnica t, TipoCaso tc, final JFrame padre) {
		super(padre);me=this;
		
		setBounds(100, 100, 450, 300);
		setTitle("Filter based retrieval");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JLabel lblPredicados = new JLabel(b.getString("attpredicates"));
		contentPane.add(lblPredicados, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBackground(Color.GRAY);
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
						JOptionPane.showMessageDialog(null, 
								b.getString("negativethreshold"),"Error", JOptionPane.ERROR_MESSAGE);
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
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		JPanel panel_1 = new JPanel();
		scrollPane.setViewportView(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));
		
		
	
		
	
		textFields = new ArrayList<JTextField>(tc.getAtbos().size());
		comboBoxes = new ArrayList<JComboBox>(tc.getAtbos().size());
		atbos = new ArrayList<String>(tc.getAtbos().size());
		for (Atributo a : tc.getAtbos().values()) {
			if (a.getEsProblema()) {
				atbos.add(a.getNombre());
				//Añadimos cada componente al panel interno panelAt
				JPanel panelAt = new JPanel();
				panelAt.setBorder(new EtchedBorder());
				panelAt.setMaximumSize(new Dimension(418, 50));
				panel_1.add(panelAt);
				panelAt.setLayout(new FormLayout(new ColumnSpec[] {
						ColumnSpec.decode("109px"),
						ColumnSpec.decode("170px"),
						ColumnSpec.decode("139px"),},
					new RowSpec[] {
						RowSpec.decode("50px"),}));
	

				JLabel lblAtbo = new JLabel(a.getNombre());
				
				panelAt.add(lblAtbo, "1, 1, center, center");

				JComboBox comboBox = new JComboBox();
				
				comboBox.addItem("["+b.getString("none")+"]");
				for (String predicado : ControlTecnicas.getFilterPredicates()) {
					comboBox.addItem(predicado);
				}
				panelAt.add(comboBox, "2, 1, fill, center");
				
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
				
				panelAt.add(tff, "3, 1, center, center");
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

				
			}
		}
		setLocationRelativeTo(padre);
	}

}
