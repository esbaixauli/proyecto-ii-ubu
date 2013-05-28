package vista.tipos.configtecnicas;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import servidorcbr.modelo.Parametro;
import servidorcbr.modelo.Tecnica;
import vista.MainFrame;
import vista.componentesGenericos.FrameEstandar;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**Frame para configurar la recuperación K-NN. Esta técnica evalúa
 * según su metrica cada atributo en cada caso de la base y devuelve
 * los K más similares, donde K está configurado por el usuario.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
@SuppressWarnings("serial")
public class NNConfigFrame extends FrameEstandar {

	/**
	 * Contentpane de la clase.
	 */
	private JPanel contentPane;
	/**
	 * Bundle de internacionalización.
	 */
	private ResourceBundle b = ResourceBundle.getBundle(
            "vista.internacionalizacion.Recursos", Locale.getDefault());


	/** Crea el frame.
	 * @param t Objeto Técnica correspondiente al frame, que se actualizará.
	 * @param padre Frame padre de este.
	 */
	public NNConfigFrame(final Tecnica t, final MainFrame padre) {
		super(padre);me=this;
		setBounds(100, 100, 350, 157);
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
		//spinner para determinar la k en k-NN
		final JSpinner spinner = new JSpinner(new SpinnerNumberModel(5, 1, 500, 1));
		if (t.getParams() != null && ! (t.getParams().isEmpty())) {
			spinner.setValue((int) t.getParams().get(0).getValor());
		}
		contentPane.add(spinner, "2, 4");
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.GRAY);
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
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
		setLocationRelativeTo(padre);
	}

}
