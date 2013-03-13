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
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblNombre = new JLabel(a.getNombre()+" ("
		+b.getString(TraductorTipos.persistenciaAVista(a.getTipo()))+"):");
		add(lblNombre);
		
		
		establecerTfTipo();
		add(textField);
		textField.setColumns(10);
		if(configurado){
			JButton btnConfigurar = new JButton(b.getString("configure"));
			btnConfigurar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					JFrame caf =new ConfigAtributoFrame(a, padre);
					caf.setVisible(true);
					padre.setEnabled(false);
				}
			});
			add(btnConfigurar);
		}
		
	}
	private void establecerTfTipo(){
		if(a.getTipo().equals("S")){
			textField = new JTextField();
		}else if(a.getTipo().equals("I")){
			textField = new JFormattedTextField(new Integer(1));
		}else{
			textField = new JFormattedTextField(new DefaultFormatterFactory(
					new NumberFormatter(new DecimalFormat())));
		}
	}
}
