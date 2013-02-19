package vista;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.text.ParseException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
	private JButton buttonMas;
	private JPanel panel;
	private JButton buttonMenos;
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
				ColumnSpec.decode("default:grow"),
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
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		JLabel lblNombretipo = new JLabel(b.getString("atname")+":");
		contentPane.add(lblNombretipo, "4, 2, right, default");
		
		String columnNames[]={b.getString("atname"),b.getString("attype"),b.getString("atweight"),
				b.getString("atmetric"), b.getString("atmetricparam")};		
		
		textField = new JTextField();
		contentPane.add(textField, "6, 2, 5, 1, fill, default");
		textField.setColumns(10);
		
		panel = new JPanel();
		contentPane.add(panel, "2, 8, fill, fill");
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		buttonMas = new JButton("+");
		buttonMas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DefaultTableModel model = ((DefaultTableModel) table.getModel());
				model.addRow(new Object[table.getColumnCount()]);
			}
		});
		panel.add(buttonMas);
		
		buttonMenos = new JButton("-");
		buttonMenos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel model = ((DefaultTableModel) table.getModel());
				if(table.getSelectedRow()==-1){
					model.removeRow(model.getRowCount()-1);
				}else{
					model.removeRow(table.getSelectedRow());
				}
			}
		});
		buttonMenos.setHorizontalAlignment(SwingConstants.TRAILING);
		panel.add(buttonMenos);	
		
		scrollPane = new JScrollPane();
		contentPane.add(scrollPane, "4, 8, 20, 1, fill, fill");
		table = new TablaAtributos(columnNames, new DefaultTableModel(columnNames,1));
		scrollPane.setViewportView(table);
		
		btnTecnicas = new JButton("New button");
		contentPane.add(btnTecnicas, "4, 12, 19, 1");
		
		btnNewButton = new JButton("New button");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(comprobarTextField() && comprobarTabla()){
					
				}
			}
			
			private boolean comprobarTextField(){
				String texto = textField.getText();
				for(int i=0;i<texto.length();i++){
					if(! Character.isLetterOrDigit(texto.charAt(i))){
					  	JOptionPane.showMessageDialog(null, 
								b.getString("incorrectformat"),"Error", JOptionPane.ERROR_MESSAGE);
					  	return false;
					}
				}
				return true;
			}
			
			private boolean comprobarTabla(){
				DefaultTableModel dm = ((DefaultTableModel) table.getModel());
				for(int row = 0;row < dm.getRowCount();row++) {
					
					for(int col = 0;col < dm.getColumnCount();col++) {
						System.out.println(dm.getColumnClass(col));
						Object c = dm.getValueAt(row, col);
						if(c== null){
						  	JOptionPane.showMessageDialog(null, 
									b.getString("emptyatt"),"Error", JOptionPane.ERROR_MESSAGE);		
						  	return false;
						}
					}
				}
				return true;
			}
		});
		contentPane.add(btnNewButton, "10, 14");
		
		btnNewButton_1 = new JButton("New button");
		contentPane.add(btnNewButton_1, "12, 14");
		
		addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                padre.setEnabled(true);
                me.dispose();
            }
        });
	}

}
