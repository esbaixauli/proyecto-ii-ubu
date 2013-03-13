package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.TipoCaso;
import vista.panels.JFilePicker;
import vista.panels.PanelIntroducirValorAtbo;
import controlador.util.LectorCaso;

@SuppressWarnings("serial")
public class InsertarCasoFrame extends JFrame {

	private JFilePicker pickerFichero;
	private JPanel contentPane;
	private ResourceBundle b = ResourceBundle.getBundle(
			"vista.internacionalizacion.Recursos", Locale.getDefault());

	private TipoCaso tc;
	/**
	 * Create the frame.
	 */
	public InsertarCasoFrame(final TipoCaso tc) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 519, 300);
		this.tc=tc;
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, b.getString("manualinsert"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		scrollPane.setViewportView(panel);
		
		JPanel panelFichero = new JPanel();
		pickerFichero = new JFilePicker("",b.getString("open"),JFilePicker.MODE_OPEN);
		panelFichero.setBorder(new TitledBorder(null,b.getString("loadfromfile") , TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panelFichero,BorderLayout.NORTH);
		panelFichero.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panelFichero.add(pickerFichero);
		JButton btnInsertar = new JButton(b.getString("insertcases"));
		btnInsertar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					LectorCaso.leerCaso(new File(pickerFichero.getSelectedFilePath()), tc);
				} catch (FileNotFoundException e) {
					JOptionPane.showMessageDialog(null, 
							b.getString("filenotfounderror"),"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		panelFichero.add(btnInsertar);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.SOUTH);
		
		JButton btnInsertmanual = new JButton(b.getString("manualinsert"));
		panel_1.add(btnInsertmanual);
		for(Atributo at : tc.getAtbos().values()){
			panel.add(new PanelIntroducirValorAtbo(at, false, this));
		}
		
	}

}
