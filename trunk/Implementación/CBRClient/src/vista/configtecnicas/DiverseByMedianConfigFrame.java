package vista.configtecnicas;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JButton;
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
import javax.swing.BoxLayout;
import javax.swing.border.EtchedBorder;
import java.awt.Color;
import java.awt.GridLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

@SuppressWarnings("serial")
public class DiverseByMedianConfigFrame extends FrameEstandar {

	private JPanel contentPane;
	private List<JTextField> textFields;
	private List<String> atbos;
	private ResourceBundle b = ResourceBundle.getBundle(
            "vista.internacionalizacion.Recursos", Locale.getDefault());
	
	private final NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());

	/**
	 * Create the frame.
	 */
	public DiverseByMedianConfigFrame(final Tecnica t, TipoCaso tc, final JFrame padre) {
		super(padre);me=this;
		setBounds(100, 100, 350, 300);
		setTitle("Diverse by median retrieval");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JLabel lblUmbralesPara = new JLabel(b.getString("attributethresholds"));
		contentPane.add(lblUmbralesPara, BorderLayout.NORTH);
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		JPanel panel = new JPanel();
		scrollPane.setViewportView(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.GRAY);
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
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


				JPanel panelAt = new JPanel();
				panelAt.setBorder(new EtchedBorder());
				panelAt.setMaximumSize(new Dimension(328, 50));
				
				panelAt.setLayout(new FormLayout(new ColumnSpec[] {
						ColumnSpec.decode("164px"),
						ColumnSpec.decode("164px"),},
					new RowSpec[] {
						RowSpec.decode("50px"),}));
				i++;

				JLabel lblAtbo = new JLabel(a.getNombre()+": ");
			
				panelAt.add(lblAtbo, "1, 1, right, fill");

				JFormattedTextField tff = new JFormattedTextField();
				tff.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat())));
				
				panelAt.add(tff, "2, 1, left, center");
				tff.setColumns(10);
				panel.add(panelAt);

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
		setLocationRelativeTo(padre);
	}

}
