package vista.configtecnicas;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.Parametro;
import servidorcbr.modelo.Tecnica;
import servidorcbr.modelo.TipoCaso;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.JButton;
import javax.swing.SpinnerNumberModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LuceneConfigFrame extends JFrame {

	private JPanel contentPane;
	private final JFrame me = this;
	private ResourceBundle b = ResourceBundle.getBundle(
            "vista.internacionalizacion.Recursos", Locale.getDefault());

	/**
	 * Create the frame.
	 */
	public LuceneConfigFrame(final Tecnica t, TipoCaso tc, final JFrame padre) {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				padre.setEnabled(true);
				me.setVisible(false);
				me.dispose();
			}
		});
		setBounds(100, 100, 350, 209);
		setResizable(false);
		setTitle("Lucene Retrieval");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		JLabel lblAtributoDeTexto = new JLabel("Atributo de texto para buscar:");
		contentPane.add(lblAtributoDeTexto, "2, 2");
		
		final JComboBox<String> comboBox = new JComboBox<String>();
		for (Atributo a : tc.getAtbos().values()) {
			if (a.getEsProblema() && a.getTipo().equals("S")) {
				comboBox.addItem(a.getNombre());
			}
		}
		contentPane.add(comboBox, "2, 4, fill, default");
		
		final JCheckBox chckbxNormalized = new JCheckBox("Normalized");
		contentPane.add(chckbxNormalized, "2, 6");
		
		JLabel lblNmeroDeCasos = new JLabel("Número de casos a recuperar:");
		contentPane.add(lblNmeroDeCasos, "2, 8");
		
		final JSpinner spinner = new JSpinner(new SpinnerNumberModel(5, 1, 500, 1));
		contentPane.add(spinner, "2, 10");
		
		JPanel panel = new JPanel();
		contentPane.add(panel, "2, 12, fill, fill");
		
		JButton btnOk = new JButton(b.getString("ok"));
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<Parametro> lista = new ArrayList<Parametro>(3);
				
				Parametro p1 = new Parametro();
				p1.setNombre("searchField");
				p1.setValor(comboBox.getSelectedIndex());
				lista.add(p1);
				
				Parametro p2 = new Parametro();
				p2.setNombre("normalized");
				if (chckbxNormalized.isSelected()) {
					p2.setValor(1);
				} else {
					p2.setValor(0);
				}
				lista.add(p2);
				
				Parametro p3 = new Parametro();
				p3.setNombre("k");
				p3.setValor((int) spinner.getValue());
				lista.add(p3);
				
				t.setParams(lista);
				padre.setEnabled(true);
				me.setVisible(false);
				me.dispose();
			}
		});
		panel.add(btnOk);
		me.getRootPane().setDefaultButton(btnOk);
		
		JButton btnCancel = new JButton(b.getString("cancel"));
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				padre.setEnabled(true);
				me.setVisible(false);
				me.dispose();
			}
		});
		panel.add(btnCancel);
	}

}
