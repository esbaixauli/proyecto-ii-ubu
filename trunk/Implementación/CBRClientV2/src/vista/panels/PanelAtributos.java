package vista.panels;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import servidorcbr.modelo.Atributo;
import vista.TraductorTipos;

/**Panel que muestra un atributo, permitiéndo escoger métrica, tipo de datos, peso y
 * parámetro de métrica.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
@SuppressWarnings("serial")
public class PanelAtributos extends JPanel {
	/**
	 * Textfield del nombre de este atributo.
	 */
	private JTextField textFieldNombre;
	/**
	 * Referencia al panel.
	 */
	private JPanel me = this;
	/**
	 * Número de atributo.
	 */
	@SuppressWarnings("unused") //Es necesario para mostrarse en un momento dado.
	private int numero;
	/**
	 * Textfield de peso del atributo.
	 */
	private JFormattedTextField formattedTextFieldPeso;
	/**
	 * Textfield del parámetro de la métrica del atributo.
	 */
	private JFormattedTextField formattedTextFieldParam;
	/**
	 * Desplegable para escoger una métrica para el atributo.
	 */
	@SuppressWarnings("rawtypes")
	private JComboBox comboBoxMetricas;
	/**
	 * Desplegable para el tipo de atributo.
	 */
	@SuppressWarnings("rawtypes")
	private JComboBox comboBoxTipo;
	/**
	 * Etiqueta de parámetros.
	 */
	private JLabel lblParmetro;
	/**
	 * Scroll para el panel.
	 */
	@SuppressWarnings("unused") //Puede ser usado si se desea que haya scroll. Se
	//Considera que es mejor dejarlo para posteriores cambios al no alterar la estructura.
	private Container scroll;

	/**
	 * Bundle de internacionalización.
	 */
	private ResourceBundle b = ResourceBundle.getBundle(
			"vista.internacionalizacion.Recursos", Locale.getDefault());
	/**
	 * Etiqueta para borrar este atributo, si es posible (El atributo 0 del problema
	 * o de la solución es imborrable).
	 */
	private JLabel lblBorrar= new JLabel("");
	
	/** Constructor del panel.
	 * @param numero Numero del atributo. Este número es sólo una indicación visual
	 * al usuario, no es almacenado de ningún modo en el sistema.
	 * @param padre Contenedor donde se halla este panel.
	 * @param scroll Scrollpane si existe, donde ubicar el panel.
	 * @param a Atributo a mostrar.
	 */
	public PanelAtributos(final int numero, final Container padre, final Container scroll,
			Atributo a){
		this(numero, padre, scroll,a.getEsProblema());
		setAtributo(a, true);
	}

	/** Constructor del panel.
	 * @param numero Numero del atributo. Este número es sólo una indicación visual
	 * al usuario, no es almacenado de ningún modo en el sistema.
	 * @param padre Contenedor donde se halla este panel.
	 * @param scroll Scrollpane si existe, donde ubicar el panel.
	 * @param problema true si el atributo a representar es del problema, 
	 * false si es de la solución.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PanelAtributos(final int numero, final Container padre, final Container scroll, boolean problema) {
		super();
		this.scroll =scroll;
		this.numero = numero;
		TitledBorder titled = BorderFactory.createTitledBorder(	BorderFactory.createEtchedBorder(), 
				b.getString("attribute") +" "+ numero,TitledBorder.CENTER,TitledBorder.TOP);
			
		setBorder(titled);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 59, 148, 69, 59, 49, 50, 25, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 20, 20, 0, 1, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, 0.0, Double.MIN_VALUE };
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
		gbc_comboBoxMetricas.insets = new Insets(0, 0, 5, 5);
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
		
			
				lblBorrar.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {
						padre.remove(me);
						
						try{
							if(scroll!=null){
								((JScrollPane)scroll).getVerticalScrollBar().setValue(0);
							}
						}catch(Exception e){}
						padre.repaint();
					}
				});
				lblBorrar.setIcon(new ImageIcon("res/delete_32.png"));
				GridBagConstraints gbc_lblBorrar = new GridBagConstraints();
				gbc_lblBorrar.insets = new Insets(0, 0, 5, 0);
				gbc_lblBorrar.anchor = GridBagConstraints.NORTH;
				gbc_lblBorrar.fill = GridBagConstraints.HORIZONTAL;
				gbc_lblBorrar.gridx = 7;
				gbc_lblBorrar.gridy = 0;
				add(lblBorrar, gbc_lblBorrar);

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
		
		//Si este panel pertenece a un atributo de la solución
		if(!problema){
			formattedTextFieldPeso.setEnabled(false);
			lblPeso.setEnabled(false);
		}

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
		gbc_formattedTextFieldParam.insets = new Insets(0, 0, 5, 5);
		gbc_formattedTextFieldParam.gridx = 6;
		gbc_formattedTextFieldParam.gridy = 1;
		add(formattedTextFieldParam, gbc_formattedTextFieldParam);

		formattedTextFieldParam
				.setFormatterFactory(new DefaultFormatterFactory(
						new NumberFormatter(new DecimalFormat())));
				rellenarMetricas(comboBoxMetricas,"");
	}

	/**Rellena los tipos de datos del combobox.
	 * @param comboBox Combobox de tipo de datos.
	 */
	private void rellenarTipos(JComboBox<String> comboBox) {
		comboBox.addItem(b.getString("integer"));
		comboBox.addItem(b.getString("string"));
		comboBox.addItem(b.getString("decimal"));
	}

	/**Rellena las métricas disponibles para un tipo de datos de un atributo.
	 * @param comboBox Combobox de métricas a rellenar.
	 * @param tipo Tipo de datos del atributo.
	 */
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

	/**Comprueba que todos los campos esten llenos. El de los parametros de
	metricas puede no estarlo si no se requieren parametros.
	El de los pesos puede no estarlo si es una solución y por tanto está deshabilitado.
	También soluciona problemas como nombres con espacios o tabuladores.
	 * @return true si todos los campos son válidos en cuanto a estar llenos, false si no.
	 */
	public boolean comprobarLleno() {
		String cadNombre = textFieldNombre.getText().replaceAll("//s","");
		if(cadNombre.equals(" ")){
			cadNombre="";
		}
		return !(cadNombre.isEmpty()
				|| (formattedTextFieldPeso.isEnabled() && formattedTextFieldPeso.getText().isEmpty())
				|| (comboBoxMetricas.getSelectedIndex() == -1) || (formattedTextFieldParam
				.isEnabled() && formattedTextFieldParam.getText().isEmpty()));
	}
	
	/**Obtiene un objeto Atributo correspondiente con lo mostrado en pantalla.
	 * @return null si el panel no puede construir un atributo válido (Hay campos vacíos),
	 * o un objeto Atributo si es posible.
	 */
	public Atributo getAtributo() {
		if (!comprobarLleno())
			return null;
		Atributo a = new Atributo();
		//Longitud maxima de 18, sin caracteres como espacios o tabuladores.
		a.setNombre(textFieldNombre.getText().substring(0, Math.min(textFieldNombre.getText().length(),18)).replace("\\s",""));
		a.setTipo(TraductorTipos.indiceATipo(comboBoxTipo.getSelectedIndex()));
		a.setMetrica(TraductorTipos.indiceAMétrica(
				comboBoxMetricas.getSelectedIndex(), comboBoxTipo.getSelectedIndex()));
	
		try {
			a.setPeso(NumberFormat.getInstance(b.getLocale()).parse(formattedTextFieldPeso.getText()).doubleValue() );
		//Si hay parámetro para la métrica.
		if (!formattedTextFieldParam.getText().isEmpty())
			a.setParamMetrica(NumberFormat.getInstance(b.getLocale()).parse(formattedTextFieldParam.getText()).doubleValue() );
		}catch (ParseException e) {}
		return a;
	}
	
	/**
	 * Rellena el panel con un atributo.
	 * @param a atributo con el que se rellena.
	 * @param desactivar permite desactivar o no las partes del panel no modificables (tipo,nombre, etc).
	 */
	@SuppressWarnings("unchecked")
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
