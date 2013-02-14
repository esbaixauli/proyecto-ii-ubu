package vista;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JButton;

public class NuevoTipoFrame extends JFrame {

	private JFrame me=this;
	private JPanel contentPane;
	private JTextField textField;
	private JTable table;
	private ResourceBundle b = ResourceBundle.getBundle(
            "vista.internacionalizacion.Recursos", Locale.getDefault());
	private JFrame padre;
	private JScrollPane scrollPane;
	private JButton btnTecnicas;
	private JButton btnNewButton;
	private JButton btnNewButton_1;
	/**
	 * Create the frame.
	 */
	public NuevoTipoFrame(final JFrame padre) {
		setTitle(b.getString("newcasetype"));
		this.padre=padre;
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 456, 272);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		JLabel lblNombretipo = new JLabel(b.getString("atname")+":");
		contentPane.add(lblNombretipo, "4, 2, right, default");
		
		String columnNames[]={b.getString("atname"),b.getString("attype"),b.getString("atweight"),b.getString("atmetric")};		
		
		textField = new JTextField();
		contentPane.add(textField, "6, 2, 5, 1, fill, default");
		textField.setColumns(10);
		
		scrollPane = new JScrollPane();
		contentPane.add(scrollPane, "4, 6, 20, 1, fill, fill");
		table = new TablaAtributos(columnNames);
		scrollPane.setViewportView(table);
		
		btnTecnicas = new JButton("New button");
		contentPane.add(btnTecnicas, "4, 8, 19, 1");
		
		btnNewButton = new JButton("New button");
		contentPane.add(btnNewButton, "10, 10");
		
		btnNewButton_1 = new JButton("New button");
		contentPane.add(btnNewButton_1, "12, 10");
		
		addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                padre.setEnabled(true);
                me.dispose();
            }
        });
	}

}
