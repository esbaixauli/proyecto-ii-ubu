package vista.panels;

import javax.swing.JPanel;

import servidorcbr.modelo.Atributo;
import vista.ConfigAtributoFrame;
import vista.TraductorTipos;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.Serializable;
import javax.swing.JComboBox;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.border.EtchedBorder;
import javax.swing.SpringLayout;
import java.awt.BorderLayout;

/*Panel que permite introducir el valor de la Query para un atributo*/
@SuppressWarnings("serial")
public class PanelIntroducirValorAtbo extends JPanel {
	private Atributo a;
	private JTextField textField = new JTextField();
	private boolean configurado;
	private ResourceBundle b = ResourceBundle.getBundle(
			"vista.internacionalizacion.Recursos", Locale.getDefault());
	private JTextField textFieldParam=new JFormattedTextField(new DefaultFormatterFactory(
			new NumberFormatter(new DecimalFormat())));
	private JComboBox<String> comboBox;
	
	/**
	 * Crea el panel.
	 */
	public PanelIntroducirValorAtbo(final Atributo a, boolean configurado,
			final JFrame padre) {
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		this.a = a;
		this.configurado = configurado;
		setPreferredSize(new Dimension(531, 86));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{77, 55, 189, 81, 55, 50, 0, 0};
		gridBagLayout.rowHeights = new int[]{12, 0, 20, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
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
		
		JLabel lblNombre = new JLabel(a.getNombre() + " ("
				+ b.getString(TraductorTipos.persistenciaAVista(a.getTipo()))
				+ "):");
		GridBagConstraints gbc_lblNombre = new GridBagConstraints();
		gbc_lblNombre.gridwidth = 2;
		gbc_lblNombre.anchor = GridBagConstraints.EAST;
		gbc_lblNombre.insets = new Insets(0, 0, 5, 5);
		gbc_lblNombre.gridx = 1;
		gbc_lblNombre.gridy = 1;
		add(lblNombre, gbc_lblNombre);
		textField= establecerComponentesTipo();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.gridwidth = 2;
		gbc_textField.anchor = GridBagConstraints.NORTH;
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.gridx = 3;
		gbc_textField.gridy = 1;
		add(textField, gbc_textField);
		textField.setColumns(10);
		
		
		
		JLabel lblMetrica = new JLabel(b.getString("atmetric"));
		GridBagConstraints gbc_lblMetrica = new GridBagConstraints();
		gbc_lblMetrica.anchor = GridBagConstraints.EAST;
		gbc_lblMetrica.insets = new Insets(0, 0, 0, 5);
		gbc_lblMetrica.gridx = 1;
		gbc_lblMetrica.gridy = 2;
		add(lblMetrica, gbc_lblMetrica);
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.anchor = GridBagConstraints.NORTH;
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.insets = new Insets(0, 0, 0, 5);
		gbc_comboBox.gridx = 2;
		gbc_comboBox.gridy = 2;
		add(comboBox, gbc_comboBox);
		

		
		JLabel lblMParam = new JLabel(b.getString("atmetricparam"));
		GridBagConstraints gbc_lblMParam = new GridBagConstraints();
		gbc_lblMParam.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblMParam.insets = new Insets(0, 0, 0, 5);
		gbc_lblMParam.gridx = 3;
		gbc_lblMParam.gridy = 2;
		add(lblMParam, gbc_lblMParam);
		
		
		textFieldParam.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				if(textFieldParam.getText().isEmpty()){
					textFieldParam.setText("0");
				}
			}
		});
		GridBagConstraints gbc_textFieldParam = new GridBagConstraints();
		gbc_textFieldParam.insets = new Insets(0, 0, 0, 5);
		gbc_textFieldParam.anchor = GridBagConstraints.NORTH;
		gbc_textFieldParam.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldParam.gridx = 4;
		gbc_textFieldParam.gridy = 2;
		add(textFieldParam, gbc_textFieldParam);
		textFieldParam.setColumns(5);
		
		if(!configurado){
			comboBox.setVisible(false);
			lblMetrica.setVisible(false);
			lblMParam.setVisible(false);
			textFieldParam.setVisible(false);
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
			return  Double.parseDouble(textFieldParam.getText());
		}else{
			return 0;
		}
	}
		
	
	public String getKey() {
		return a.getNombre();
	}

	public Serializable getValue() {
		if(textField.getText().equals("")){
			return null;
		}
		if (a.getTipo().equals("S")) {
			return textField.getText();
		} else if (a.getTipo().equals("I")) {
			return Integer.parseInt(textField.getText());
		} else {
			return Double.parseDouble(textField.getText());
		}
	}
}
