package vista.panels;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import servidorcbr.modelo.Atributo;
import vista.TraductorTipos;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**Panel que permite introducir el valor de la Query para un atributo, así como
 * configurar su métrica/peso/parámetro en un ciclo configurado.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
@SuppressWarnings("serial")
public class PanelIntroducirValorAtbo extends JPanel {
	/**
	 * Atributo correspondiente al valor que va a ser rellenado por el panel.
	 */
	private Atributo a;
	/**
	 * Textfield con el valor a rellenar.
	 */
	private JTextField textField = new JTextField();
	/**
	 * Bundle de internacionalización.
	 */
	private ResourceBundle b = ResourceBundle.getBundle(
			"vista.internacionalizacion.Recursos", Locale.getDefault());
	/**
	 * Textfield de parámetro de la métrica para el ciclo configurado.
	 */
	private JTextField textFieldParam=new JFormattedTextField(new DefaultFormatterFactory(
			new NumberFormatter(new DecimalFormat())));
	/**
	 * Desplegable de escoger métrica para el ciclo configurado.
	 */
	private JComboBox<String> comboBox;
	/**
	 * Textfield de peso del atributo para el ciclo configurado.
	 */
	private JFormattedTextField textFieldPeso;
	

	/**Crea el panel.
	 * @param a Atributo para el que se va a rellenar el panel.
	 * @param configurado true si se pueden configurar valores como la métrica o 
	 * el peso, false si no.
	 */
	public PanelIntroducirValorAtbo(final Atributo a, boolean configurado) {
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		this.a = a;
		setPreferredSize(new Dimension(572, 86));
		
		comboBox = new JComboBox<String>();
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if(!a.getTipo().equals("S") && comboBox.getSelectedIndex()>0){
					textFieldParam.setEnabled(true);
				}else{
					textFieldParam.setEnabled(false);
				}
			}
		});
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("max(29dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("52px"),
				ColumnSpec.decode("59px"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("61px"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("74px"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("212px"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.PARAGRAPH_GAP_ROWSPEC,
				RowSpec.decode("20px"),
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("20px"),}));
		
		JLabel lblNombre = new JLabel(a.getNombre() + " ("
				+ b.getString(TraductorTipos.persistenciaAVista(a.getTipo()))
				+ "):");
		//Si el nombre del atributo es muy largo, pon puntos suspensivos.
		if(lblNombre.getText().length()>25){
			lblNombre.setText(lblNombre.getText().substring(0,22)+"...):");
		}
		add(lblNombre, "1, 2, 6, 1, right, center");
		textField= establecerComponentesTipo();
		add(textField, "8, 2, fill, center");
		textField.setColumns(10);
		
		JPanel panelPeso = new JPanel();
		add(panelPeso, "10, 2, fill, center");
		GridBagLayout gbl_panelPeso = new GridBagLayout();
		gbl_panelPeso.columnWidths = new int[]{150, 22, 0};
		gbl_panelPeso.rowHeights = new int[]{20, 0};
		gbl_panelPeso.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panelPeso.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panelPeso.setLayout(gbl_panelPeso);
		
		JLabel lblNewLabel = new JLabel(b.getString("atweight"));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		panelPeso.add(lblNewLabel, gbc_lblNewLabel);
		
		textFieldPeso = new JFormattedTextField(new DefaultFormatterFactory(
				new NumberFormatter(new DecimalFormat()) ));
		GridBagConstraints gbc_textFieldPeso = new GridBagConstraints();
		gbc_textFieldPeso.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldPeso.anchor = GridBagConstraints.NORTH;
		gbc_textFieldPeso.gridx = 1;
		gbc_textFieldPeso.gridy = 0;
		panelPeso.add(textFieldPeso, gbc_textFieldPeso);
		textFieldPeso.setColumns(5);
		
		try {
			textFieldPeso.setText(NumberFormat.getNumberInstance().format(a.getPeso())+"");
			textFieldPeso.commitEdit();
		} catch (ParseException e) {
			textFieldPeso.setText("0");
		}
		
		JLabel lblMetrica = new JLabel(b.getString("atmetric"));
		add(lblMetrica, "4, 4, right, center");
		add(comboBox, "6, 4, 3, 1, fill, center");
		
		JPanel panelParamMet = new JPanel();
		add(panelParamMet, "10, 4, fill, fill");
		GridBagLayout gbl_panelParamMet = new GridBagLayout();
		gbl_panelParamMet.columnWidths = new int[]{150, 22, 0};
		gbl_panelParamMet.rowHeights = new int[]{20, 0};
		gbl_panelParamMet.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panelParamMet.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panelParamMet.setLayout(gbl_panelParamMet);
		

		
		JLabel lblMParam = new JLabel(b.getString("atmetricparam"));
		GridBagConstraints gbc_lblMParam = new GridBagConstraints();
		gbc_lblMParam.anchor = GridBagConstraints.EAST;
		gbc_lblMParam.insets = new Insets(0, 0, 0, 5);
		gbc_lblMParam.gridx = 0;
		gbc_lblMParam.gridy = 0;
		panelParamMet.add(lblMParam, gbc_lblMParam);
		GridBagConstraints gbc_textFieldParam = new GridBagConstraints();
		gbc_textFieldParam.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldParam.anchor = GridBagConstraints.NORTH;
		gbc_textFieldParam.gridx = 1;
		gbc_textFieldParam.gridy = 0;
		panelParamMet.add(textFieldParam, gbc_textFieldParam);
		
		
		textFieldParam.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				if(textFieldParam.getText().isEmpty()){
					textFieldParam.setText("0");
				}
			}
		});
		textFieldParam.setColumns(5);
		
		if(!configurado){
			comboBox.setVisible(false);
			lblMetrica.setVisible(false);
			lblMParam.setVisible(false);
			panelParamMet.setVisible(false);
			textFieldParam.setVisible(false);
			textFieldPeso.setVisible(false);
			lblNewLabel.setVisible(false);
			setPreferredSize(new Dimension(getWidth(), 50));
		}
	}

	/**
	 * Auxiliar. Establece los componentes del atributo en función de su tipo de datos.
	 * @return Textfield según el tipo del atributo.
	 */
	private JTextField establecerComponentesTipo() {
		comboBox.addItem(b.getString("equal"));
		
		if (a.getTipo().equals("S")) {
			textField = new JTextField();
			comboBox.addItem(b.getString("equalignorecase"));
			comboBox.addItem(b.getString("substring"));
		} else {
			comboBox.addItem(b.getString("interval"));
			comboBox.addItem(b.getString("threshold"));
			if (a.getTipo().equals("I")) {
				textField = new JFormattedTextField(new Integer(1));
			} else {
				textField = new JFormattedTextField(new DefaultFormatterFactory(
						new NumberFormatter(new DecimalFormat())));
			}
		}
		comboBox.setSelectedItem(b.getString(a.getMetrica()));
		textFieldParam.setText(NumberFormat.getInstance(b.getLocale()).format(a.getParamMetrica()));
		return textField;
	}
	
	/** Obtiene la métrica escogida para el atributo, en formato de programa (Independiente de internacionalización).
	 * @return la métrica escogida en el combobox de métricas.
	 */
	public String getMetrica(){
		return TraductorTipos.indiceAMétrica(comboBox.getSelectedIndex(),a.getTipo());
	}
	
	/** Obtiene el parámetro de la métrica seleccionada, si es que lo tiene.
	 * @return 0 si la métrica no tiene parámetro, o el valor del textfield formateado correspondiente si lo tiene.
	 * @see JFormattedTextField
	 */
	public double getParamMetrica(){
		//Si el textfield está habilitado
		if(textFieldParam.isEnabled()){
			try {
				return  NumberFormat.getNumberInstance().parse(
						textFieldParam.getText()).doubleValue();
			} catch (ParseException e) {
				return 0;
			}
		}else{
			return 0;
		}
	}
		
	/**Obtiene el peso del atributo de este panel.
	 * @return El peso del atributo de este panel.
	 */
	public double getPeso(){
		return Double.parseDouble(textFieldPeso.getText());
	}
	
	/**Obtiene el nombre del atributo de este panel.
	 * @return nombre del atributo.
	 */
	public String getKey() {
		return a.getNombre();
	}

	/**Obtiene el valor del campo rellenado en la query para este atributo.
	 * @return El valor del campo rellenado.
	 */
	public Serializable getValue() {
		try {
			if (textField.getText().equals("")) {
				return null;
			}
			if (a.getTipo().equals("S")) {
				return textField.getText();
			} else if (a.getTipo().equals("I")) {
				return new Integer(NumberFormat.getIntegerInstance().parse(textField.getText()).intValue());
			} else {
				return NumberFormat.getNumberInstance().parse(
						textField.getText()).doubleValue();

			}
		} catch (ParseException e) {
			return 0;
		}
	}
	
	/**Establece el valor del campo de este panel.
	 * @param valor el valor a introducir.
	 */
	public void setValue(Object valor){
		if(valor!=null){
			textField.setText(valor+"");
			if(!a.getTipo().equals("S")){//Si no es una cadena de texto
				try {
					if (valor.getClass().isAssignableFrom(Double.class)) {
						textField.setText(NumberFormat.getNumberInstance().format((double) valor));
					} else {
						textField.setText(NumberFormat.getNumberInstance().parse(valor+"")+"");
					}
					((JFormattedTextField) textField).commitEdit();
					//Si falla el parseo el textfield queda a 0.
				} catch (ParseException e) {
					textField.setText("0");
				}
			}
		}
	}
	
}
