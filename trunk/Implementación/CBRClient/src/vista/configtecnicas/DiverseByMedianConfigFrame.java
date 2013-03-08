package vista.configtecnicas;

import java.awt.BorderLayout;

import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JSeparator;
import java.awt.Insets;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JTextField;

import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.Parametro;
import servidorcbr.modelo.Tecnica;
import servidorcbr.modelo.TipoCaso;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DiverseByMedianConfigFrame extends JFrame {

	private JPanel contentPane;
	private List<JTextField> textFields;
	private List<String> atbos;
	private ResourceBundle b = ResourceBundle.getBundle(
            "vista.internacionalizacion.Recursos", Locale.getDefault());
	private final JFrame me = this;
	private final NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());

	/**
	 * Create the frame.
	 */
	public DiverseByMedianConfigFrame(final Tecnica t, TipoCaso tc, final JFrame padre) {
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
		setTitle("Diverse by median retrieval");
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JLabel lblUmbralesPara = new JLabel(b.getString("attributethresholds"));
		contentPane.add(lblUmbralesPara, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.SOUTH);
		
		JButton btnOk = new JButton(b.getString("ok"));
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<Parametro> params = new ArrayList<Parametro>(atbos.size());
				try {
					for (int i=0; i<atbos.size(); i++) {
						if (textFields.get(i).getText().equals("")) {
							throw new Exception();
						}
						double d = nf.parse(textFields.get(i).getText()).doubleValue();
						Parametro p = new Parametro();
						p.setNombre(atbos.get(i));
						p.setValor(d);
						params.add(p);
					}
					t.setParams(params);
					padre.setEnabled(true);
					me.dispose();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, 
							b.getString("emptyfield"),"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		panel_1.add(btnOk);
		
		me.getRootPane().setDefaultButton(btnOk);
		
		JButton btnCancel = new JButton(b.getString("cancel"));
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				padre.setEnabled(true);
				me.dispose();
			}
		});
		panel_1.add(btnCancel);
		
		int i=0;
		textFields = new ArrayList<JTextField>(tc.getAtbos().values().size());
		atbos = new ArrayList<String>(tc.getAtbos().values().size());
		for (Atributo a : tc.getAtbos().values()) {
			if (a.getEsProblema()) {
				atbos.add(a.getNombre());

				JSeparator separator = new JSeparator();
				GridBagConstraints gbc_separator = new GridBagConstraints();
				gbc_separator.insets = new Insets(0, 0, 5, 5);
				gbc_separator.gridx = 0;
				gbc_separator.gridy = i;
				panel.add(separator, gbc_separator);

				i++;

				JLabel lblAtbo = new JLabel(a.getNombre()+":");
				GridBagConstraints gbc_lblAtbo = new GridBagConstraints();
				gbc_lblAtbo.insets = new Insets(0, 0, 0, 5);
				gbc_lblAtbo.anchor = GridBagConstraints.EAST;
				gbc_lblAtbo.gridx = 1;
				gbc_lblAtbo.gridy = i;
				panel.add(lblAtbo, gbc_lblAtbo);

				JFormattedTextField tff = new JFormattedTextField();
				tff.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat())));
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.gridx = 2;
				gbc_textField.gridy = i;
				panel.add(tff, gbc_textField);
				tff.setColumns(10);
				textFields.add(tff);
				if (t.getParams() == null) {
					tff.setText("1");
				} else {
					for (Parametro p : t.getParams()) {
						if (p.getNombre().equals((a.getNombre()))) {
							tff.setText(nf.format(p.getValor()));
						}
					}
					if (tff.getText().equals("")) {
						tff.setText("1");
					}
				}

				i++;
			}
		}
	}

}
