package vista.configtecnicas;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import servidorcbr.modelo.Tecnica;
import servidorcbr.modelo.TipoCaso;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JLabel;

public class LuceneConfigFrame extends JFrame {

	private JPanel contentPane;
	private final JFrame me = this;

	/**
	 * Create the frame.
	 */
	public LuceneConfigFrame(Tecnica t, TipoCaso tc, JFrame padre) {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 350, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblAtributoDeTexto = new JLabel("Atributo de texto para buscar:");
		contentPane.add(lblAtributoDeTexto, "2, 2");
	}

}
