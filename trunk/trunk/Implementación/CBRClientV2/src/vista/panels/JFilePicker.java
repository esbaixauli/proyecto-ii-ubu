package vista.panels;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

/** Filepicker para escoger un fichero y mostrar su ruta.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
@SuppressWarnings("serial")
public class JFilePicker extends JPanel {
	
	
	/**
	 * Etiqueta para el picker. Si no se desea un nombre en especial, puede quedar
	 * con una cadena vacía.
	 */
	private JLabel label;
	/**
	 * Textfield con la ruta del fichero escogido.
	 */
	private JTextField textField;
	/**
	 * Botón de abrir o guardar fichero.
	 */
	private JButton button;
	
	/**
	 * Filechooser encargado de las operaciones de apertura y guardado.
	 */
	private JFileChooser fileChooser;
	
	/**
	 * Modo de uso (Abrir/Guardar). Correspondiente con las constantes MODE_OPEN
	 * y MODE_SAVE de esta clase.
	 */
	private int mode;
	/**
	 * Constante para apertura de ficheros.
	 */
	public static final int MODE_OPEN = 1;
	/**
	 * Constante para guardado de ficheros.
	 */
	public static final int MODE_SAVE = 2;
	
	/**Constructor de la clase.
	 * @param textFieldLabel Etiqueta de título.
	 * @param buttonLabel Etiqueta del botón de abrir/guardar.
	 * @param mode Modo de apertura. Se corresponde con las constantes MODE_OPEN
	 * y MODE_SAVE de esta clase.
	 */
	public JFilePicker(String textFieldLabel, String buttonLabel, int mode) {

		
		fileChooser = new JFileChooser();
		this.mode=mode;
		
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		// creates the GUI
		label = new JLabel(textFieldLabel);
		
		textField = new JTextField(25);
		button = new JButton(buttonLabel);
		
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				buttonActionPerformed(evt);				
			}
		});
		
		add(label);
		add(textField);
		add(button);
		addFileTypeFilter("WEKA arff","arff");
	}
	
	/** Gestión de la acción por el botón.
	 * @param evt Evento que llega al pulsar.
	 */
	private void buttonActionPerformed(ActionEvent evt) {
		if (mode == MODE_OPEN) {
			if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				textField.setText(fileChooser.getSelectedFile().getAbsolutePath());
			}
		} else if (mode == MODE_SAVE) {
			if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				textField.setText(fileChooser.getSelectedFile().getAbsolutePath());
			}
		}
	}

	/**Añadir filtro de tipo de fichero al diálogo de abrir/guardar.
	 * @param description Descripción del tipo de fichero.
	 * @param extension Extensión Extensión del tipo de fichero. p.Ej:"txt"
	 */
	public void addFileTypeFilter(String description, String extension) {
		fileChooser.setFileFilter(new FileNameExtensionFilter(description, extension));	
	}
	
	/**Establecer modo de uso, guardar o abrir.
	 * @param mode Se corresponde con las constantes MODE_OPEN
	 * y MODE_SAVE de esta clase.
	 */
	public void setMode(int mode) {
		this.mode = mode;
	}
	
	/**Obtiene la ruta al fichero escogido.
	 * @return ruta al fichero escogido.
	 */
	public String getSelectedFilePath() {
		return textField.getText();
	}
	
	/**Obtiene el filechooser asociado a la clase.
	 * @return filechooser de la clase.
	 */
	public JFileChooser getFileChooser() {
		return this.fileChooser;
	}
}