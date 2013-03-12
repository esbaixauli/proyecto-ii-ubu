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
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

/*Panel que permite introducir el valor de la Query para un atributo*/
@SuppressWarnings("serial")
public class PanelIntroducirValorAtbo extends JPanel {
	private Atributo a;
	private JTextField textField;
	private boolean configurado;
	private ResourceBundle b = ResourceBundle.getBundle(
			"vista.internacionalizacion.Recursos", Locale.getDefault());
	/**
	 * Create the panel.
	 */
	public PanelIntroducirValorAtbo(final Atributo a, boolean configurado, final JFrame padre) {
		
		this.a=a;
		this.configurado=configurado;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{191, 60, 72, 0, 0};
		gridBagLayout.rowHeights = new int[]{23, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		
		establecerTfTipo();
		
		JLabel lblNombre = new JLabel(a.getNombre()+" ("
		+b.getString(TraductorTipos.persistenciaAVista(a.getTipo()))+"):");
		GridBagConstraints gbc_lblNombre = new GridBagConstraints();
		gbc_lblNombre.anchor = GridBagConstraints.EAST;
		gbc_lblNombre.insets = new Insets(0, 0, 0, 5);
		gbc_lblNombre.gridx = 0;
		gbc_lblNombre.gridy = 0;
		add(lblNombre, gbc_lblNombre);
		if(configurado){
			JButton btnConfigurar = new JButton(b.getString("configure"));
			btnConfigurar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					JFrame caf =new ConfigAtributoFrame(a, padre);
					caf.setVisible(true);
					padre.setEnabled(false);
				}
			});
			GridBagConstraints gbc_btnConfigurar = new GridBagConstraints();
			gbc_btnConfigurar.fill = GridBagConstraints.HORIZONTAL;
			gbc_btnConfigurar.insets = new Insets(0, 0, 0, 5);
			gbc_btnConfigurar.anchor = GridBagConstraints.NORTH;
			gbc_btnConfigurar.gridx = 2;
			gbc_btnConfigurar.gridy = 0;
			add(btnConfigurar, gbc_btnConfigurar);
		}
		
	}
	private void establecerTfTipo(){
		
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.insets = new Insets(0, 0, 0, 5);
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
	
		if(a.getTipo().equals("S")){
			textField = new JTextField();
		}else if(a.getTipo().equals("I")){
			textField = new JFormattedTextField(new Integer(1));
		}	else{
			textField = new JFormattedTextField(new DefaultFormatterFactory(
					new NumberFormatter(new DecimalFormat())));
		}
		textField.setColumns(10);
		add(textField, gbc_textField);
	}
}
