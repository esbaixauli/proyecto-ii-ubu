package vista.panels;

import java.awt.Container;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JPanel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField;
import javax.swing.JComboBox;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import javax.swing.JButton;

import servidorcbr.modelo.Atributo;
import vista.TraductorTipos;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class PanelAtributos extends JPanel {
	private JTextField textFieldNombre;
	private JPanel me = this;
	private int numero;
	private JFormattedTextField formattedTextFieldPeso;
	private JFormattedTextField formattedTextFieldParam;
	private JComboBox comboBoxMetricas;
	private JComboBox comboBoxTipo;
	private JLabel lblParmetro;
	private Container scroll;

	private ResourceBundle b = ResourceBundle.getBundle(
			"vista.internacionalizacion.Recursos", Locale.getDefault());
	private Container padre;
	
	private JLabel lblBorrar= new JLabel("");
	
	public PanelAtributos(final int numero, final Container padre, final Container scroll,
			Atributo a){
		this(numero, padre, scroll);
		setAtributo(a, true);
	}

	public PanelAtributos(final int numero, final Container padre, final Container scroll) {
		super();
		this.scroll =scroll;
		this.padre = padre;
		this.numero = numero;
		setBorder(new TitledBorder(null, b.getString("attribute") + " "
				+ numero, TitledBorder.CENTER, TitledBorder.TOP, null, null));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 59, 148, 69, 59, 49, 50, 25, 0 };
		gridBagLayout.rowHeights = new int[] { 20, 20, 0, 1, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel lblNombreDelAtributo = new JLabel(b.getString("atname") + ":");
		GridBagConstraints gbc_lblNombreDelAtributo = new GridBagConstraints();
		gbc_lblNombreDelAtributo.anchor = GridBagConstraints.EAST;
		gbc_lblNombreDelAtributo.insets = new Insets(0, 0, 5, 5);
		gbc_lblNombreDelAtributo.gridx = 0;
		gbc_lblNombreDelAtributo.gridy = 0;
		add(lblNombreDelAtributo, gbc_lblNombreDelAtributo);

		textFieldNombre = new JTextField();
		GridBagConstraints gbc_textFieldNombre = new GridBagConstraints();
		gbc_textFieldNombre.anchor = GridBagConstraints.NORTH;
		gbc_textFieldNombre.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldNombre.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldNombre.gridwidth = 2;
		gbc_textFieldNombre.gridx = 1;
		gbc_textFieldNombre.gridy = 0;
		add(textFieldNombre, gbc_textFieldNombre);
		textFieldNombre.setColumns(10);

		JLabel lblMtrica = new JLabel(b.getString("atmetric") + ":");
		GridBagConstraints gbc_lblMtrica = new GridBagConstraints();
		gbc_lblMtrica.anchor = GridBagConstraints.EAST;
		gbc_lblMtrica.insets = new Insets(0, 0, 5, 5);
		gbc_lblMtrica.gridx = 3;
		gbc_lblMtrica.gridy = 0;
		add(lblMtrica, gbc_lblMtrica);

		comboBoxMetricas = new JComboBox();
		GridBagConstraints gbc_comboBoxMetricas = new GridBagConstraints();
		gbc_comboBoxMetricas.anchor = GridBagConstraints.NORTH;
		gbc_comboBoxMetricas.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxMetricas.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxMetricas.gridwidth = 3;
		gbc_comboBoxMetricas.gridx = 4;
		gbc_comboBoxMetricas.gridy = 0;
		add(comboBoxMetricas, gbc_comboBoxMetricas);

		comboBoxMetricas.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent arg0) {
				String escogido = arg0.getItem().toString();
				if (escogido.equals(b.getString("interval"))
						|| escogido.equals(b.getString("threshold"))) {
					lblParmetro.setEnabled(true);
					formattedTextFieldParam.setEnabled(true);
				} else {
					lblParmetro.setEnabled(false);
					formattedTextFieldParam.setEnabled(false);
					formattedTextFieldParam.setText("");
				}
			}
		});

		JLabel lblTipo = new JLabel(b.getString("attype"));
		GridBagConstraints gbc_lblTipo = new GridBagConstraints();
		gbc_lblTipo.anchor = GridBagConstraints.EAST;
		gbc_lblTipo.insets = new Insets(0, 0, 5, 5);
		gbc_lblTipo.gridx = 0;
		gbc_lblTipo.gridy = 1;
		add(lblTipo, gbc_lblTipo);
		if(numero==0){
			lblBorrar.setVisible(false);
		}
		
		comboBoxTipo = new JComboBox();
		GridBagConstraints gbc_comboBoxTipo = new GridBagConstraints();
		gbc_comboBoxTipo.gridwidth = 2;
		gbc_comboBoxTipo.anchor = GridBagConstraints.NORTH;
		gbc_comboBoxTipo.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxTipo.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxTipo.gridx = 1;
		gbc_comboBoxTipo.gridy = 1;
		add(comboBoxTipo, gbc_comboBoxTipo);

		rellenarTipos(comboBoxTipo);

		comboBoxTipo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				rellenarMetricas(comboBoxMetricas, arg0.getItem().toString());
				comboBoxMetricas.setSelectedIndex(0);
			}
		});

		JLabel lblPeso = new JLabel(b.getString("atweight") + ":");
		GridBagConstraints gbc_lblPeso = new GridBagConstraints();
		gbc_lblPeso.anchor = GridBagConstraints.EAST;
		gbc_lblPeso.insets = new Insets(0, 0, 5, 5);
		gbc_lblPeso.gridx = 3;
		gbc_lblPeso.gridy = 1;
		add(lblPeso, gbc_lblPeso);

		formattedTextFieldPeso = new JFormattedTextField();
		GridBagConstraints gbc_formattedTextFieldPeso = new GridBagConstraints();
		gbc_formattedTextFieldPeso.anchor = GridBagConstraints.NORTH;
		gbc_formattedTextFieldPeso.fill = GridBagConstraints.HORIZONTAL;
		gbc_formattedTextFieldPeso.insets = new Insets(0, 0, 5, 5);
		gbc_formattedTextFieldPeso.gridx = 4;
		gbc_formattedTextFieldPeso.gridy = 1;
		add(formattedTextFieldPeso, gbc_formattedTextFieldPeso);
		formattedTextFieldPeso.setFormatterFactory(new DefaultFormatterFactory(
				new NumberFormatter(new DecimalFormat())));

		lblParmetro = new JLabel(b.getString("atmetricparam") + ":");
		lblParmetro.setEnabled(false);
		GridBagConstraints gbc_lblParmetro = new GridBagConstraints();
		gbc_lblParmetro.anchor = GridBagConstraints.EAST;
		gbc_lblParmetro.insets = new Insets(0, 0, 5, 5);
		gbc_lblParmetro.gridx = 5;
		gbc_lblParmetro.gridy = 1;
		add(lblParmetro, gbc_lblParmetro);

		formattedTextFieldParam = new JFormattedTextField();
		formattedTextFieldParam.setEnabled(false);
		GridBagConstraints gbc_formattedTextFieldParam = new GridBagConstraints();
		gbc_formattedTextFieldParam.anchor = GridBagConstraints.NORTH;
		gbc_formattedTextFieldParam.fill = GridBagConstraints.HORIZONTAL;
		gbc_formattedTextFieldParam.insets = new Insets(0, 0, 5, 0);
		gbc_formattedTextFieldParam.gridx = 6;
		gbc_formattedTextFieldParam.gridy = 1;
		add(formattedTextFieldParam, gbc_formattedTextFieldParam);

		formattedTextFieldParam
				.setFormatterFactory(new DefaultFormatterFactory(
						new NumberFormatter(new DecimalFormat())));
		
			
				lblBorrar.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {
						padre.remove(me);
						
						try{
							((JScrollPane)scroll).getVerticalScrollBar().setValue(0);
						}catch(Exception e){}
						padre.repaint();
					}
				});
				lblBorrar.setIcon(new ImageIcon("res/delete_32.png"));
				GridBagConstraints gbc_lblBorrar = new GridBagConstraints();
				gbc_lblBorrar.insets = new Insets(0, 0, 5, 0);
				gbc_lblBorrar.anchor = GridBagConstraints.NORTH;
				gbc_lblBorrar.fill = GridBagConstraints.HORIZONTAL;
				gbc_lblBorrar.gridx = 6;
				gbc_lblBorrar.gridy = 2;
				add(lblBorrar, gbc_lblBorrar);
				rellenarMetricas(comboBoxMetricas,"");
	}

	private void rellenarTipos(JComboBox<String> comboBox) {
		comboBox.addItem(b.getString("integer"));
		comboBox.addItem(b.getString("string"));
		comboBox.addItem(b.getString("decimal"));
	}

	private void rellenarMetricas(JComboBox<String> comboBox, String tipo) {
		comboBox.removeAllItems();
		comboBox.addItem(b.getString("equal"));
		if (tipo.equals(b.getString("string"))) {
			comboBox.addItem(b.getString("equalignorecase"));
			comboBox.addItem(b.getString("substring"));
		} else {
			comboBox.addItem(b.getString("interval"));
			comboBox.addItem(b.getString("threshold"));
		}
		comboBox.setSelectedIndex(0);
	}

	// Comprueba que todos los campos esten llenos. El de los parametros de
	// metricas puede no estarlo si no
	// se requieren parametros.
	public boolean comprobarLleno() {
		return !(textFieldNombre.getText().isEmpty()
				|| formattedTextFieldPeso.getText().isEmpty()
				|| (comboBoxMetricas.getSelectedIndex() == -1) || (formattedTextFieldParam
				.isEnabled() && formattedTextFieldParam.getText().isEmpty()));
	}
	
	public Atributo getAtributo() {
		if (!comprobarLleno())
			return null;
		Atributo a = new Atributo();
		a.setNombre(textFieldNombre.getText());
		a.setTipo(TraductorTipos.indiceATipo(comboBoxTipo.getSelectedIndex()));
		a.setMetrica(TraductorTipos.indiceAMétrica(
				comboBoxMetricas.getSelectedIndex(), comboBoxTipo.getSelectedIndex()));
	
		try {
			a.setPeso(NumberFormat.getInstance(b.getLocale()).parse(formattedTextFieldPeso.getText()).doubleValue() );
		
		if (!formattedTextFieldParam.getText().isEmpty())
			a.setParamMetrica(NumberFormat.getInstance(b.getLocale()).parse(formattedTextFieldParam.getText()).doubleValue() );
		}catch (ParseException e) {}
		return a;
	}
	
	//rellena el panel con un atributo.
	//@param a atributo con el que se rellena.
	//@param desactivar permite desactivar o no las partes del panel no modificables (tipo,nombre, etc).
	public void setAtributo(Atributo a, boolean desactivar){
		textFieldNombre.setText(a.getNombre());
		comboBoxTipo.setSelectedItem(b.getString(TraductorTipos.persistenciaAVista(a.getTipo())));
		rellenarMetricas(comboBoxMetricas, b.getString(TraductorTipos.persistenciaAVista(a.getTipo())));
		comboBoxMetricas.setSelectedItem(b.getString(a.getMetrica()));
		formattedTextFieldPeso.setText(NumberFormat.getInstance(b.getLocale()).format(a.getPeso()));
		formattedTextFieldParam.setText(NumberFormat.getInstance(b.getLocale()).format(a.getParamMetrica()));
		if(desactivar){
			textFieldNombre.setEnabled(false);
			comboBoxTipo.setEnabled(false);
			lblBorrar.setVisible(false);
		}
	
		
		
		
	}
}
