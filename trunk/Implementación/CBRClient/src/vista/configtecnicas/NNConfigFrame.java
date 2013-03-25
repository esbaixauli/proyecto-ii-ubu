package vista.configtecnicas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import servidorcbr.modelo.Parametro;
import servidorcbr.modelo.Tecnica;
import vista.componentes.FrameEstandar;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

@SuppressWarnings("serial")
public class NNConfigFrame extends FrameEstandar {

	private JPanel contentPane;
	private ResourceBundle b = ResourceBundle.getBundle(
            "vista.internacionalizacion.Recursos", Locale.getDefault());

	/**
	 * Create the frame.
	 */
	public NNConfigFrame(final Tecnica t, final JFrame padre) {
		super(padre);me=this;
		setBounds(100, 100, 350, 130);
		setTitle("NN Retrieval");
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
				RowSpec.decode("default:grow"),}));
		
		JLabel lblNmeroDeCasos = new JLabel(b.getString("casestoretrieve")+":");
		contentPane.add(lblNmeroDeCasos, "2, 2");
		
		final JSpinner spinner = new JSpinner(new SpinnerNumberModel(5, 1, 500, 1));
		if (t.getParams() != null && ! (t.getParams().isEmpty())) {
			spinner.setValue((int) t.getParams().get(0).getValor());
		}
		contentPane.add(spinner, "2, 4");
		
		JPanel panel = new JPanel();
		contentPane.add(panel, "2, 6, fill, fill");
		
		JButton btnOk = new JButton(b.getString("ok"));
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<Parametro> l = new ArrayList<Parametro>(1);
				Parametro p = new Parametro();
				p.setNombre("k");
				p.setValor((int) spinner.getValue());
				l.add(p);
				t.setParams(l);
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
