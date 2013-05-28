package vista;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableRowSorter;

import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.TipoCaso;
import vista.componentesGenericos.PanelEstandar;
import vista.panels.JFilePicker;
import vista.panels.PanelIntroducirValorAtbo;
import vista.tablas.CasosTableCellRenderer;
import vista.tablas.CasosTableModel;
import controlador.ControlCasos;
import controlador.util.LectorCaso;

/**Panel para introducir casos manualmente. Permite introducir cada
 * uno de los atributos de forma manual con el subpanel inferior, o cargarlos
 * desde un fichero WEKA .arff, editarlos en una tabla como se desee y finalmente
 * introducirlos.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
@SuppressWarnings("serial")
public class InsertarCasoPanel extends PanelEstandar {

	/**
	 * File picker para escoger un fichero a cargar.
	 */
	private JFilePicker pickerFichero;
	/**
	 * Contentpane interno al panel.
	 */
	private JPanel contentPane;

	/**
	 * Panel para introducir datos manuales.
	 */
	private JPanel panelManual;
	/**
	 * Etiqueta de casos cargados.
	 */
	private JLabel lblCargadas;
	/**
	 * Tipo de caso al que se insertarán casos.
	 */
	private TipoCaso tc;
	/**
	 * Tabla de casos.
	 */
	private JTable table;
	/**
	 * Casos a insertar. Se actualiza automáticamente según lo que se
	 * introduzca en la tabla.
	 */
	private List<HashMap<String,Serializable>> casos;
	/**
	 * Textfield para filtrar casos.
	 */
	private JTextField textFieldFilter;
	
	/**
	 * Panel de filtrado de casos.
	 */
	private JPanel panelFiltro;
	


	/** Constructor del panel.
	 * @param tc Tipo de caso para el que el usuario insertará casos.
	 * @param padre Frame padre del panel.
	 */
	public InsertarCasoPanel(final TipoCaso tc, final MainFrame padre) {
		super(padre);me=this;
		
		setName(b.getString("insertcases")+": "+tc.getNombre());
		this.tc=tc;
		contentPane = new JPanel();
		add(contentPane);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{594, 0};
		gbl_contentPane.rowHeights = new int[]{66, 200, 0, 93, 0, 15, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JPanel panelFichero = new JPanel();
		pickerFichero = new JFilePicker("",b.getString("open"),JFilePicker.MODE_OPEN);
		pickerFichero.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		pickerFichero.setBackground(Color.GRAY);
		panelFichero.setBorder(new TitledBorder(null,b.getString("loadfromfile") , TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panelFichero = new GridBagConstraints();
		gbc_panelFichero.anchor = GridBagConstraints.NORTH;
		gbc_panelFichero.fill = GridBagConstraints.HORIZONTAL;
		gbc_panelFichero.insets = new Insets(0, 0, 5, 0);
		gbc_panelFichero.gridx = 0;
		gbc_panelFichero.gridy = 0;
		contentPane.add(panelFichero, gbc_panelFichero);
		panelFichero.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panelFichero.add(pickerFichero);
		JButton btnInsertar = new JButton(b.getString("loadfromfile"));
		btnInsertar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {//Intenta leer una lista de casos de fichero
					casos=
					LectorCaso.leerCaso(new File(pickerFichero.getSelectedFilePath()), tc);
					if(casos!=null){ //Si se pudo, muéstralos en la tabla
						table.setModel(new CasosTableModel(casos, tc));
						lblCargadas.setText(b.getString("loadedcases")+casos.size());
						table.setDefaultRenderer(Object.class, new CasosTableCellRenderer(tc));
						panelFiltro.setVisible(true); //Si hay datos, muestra el panel
						//De filtro.
					}else{
						throw new IOException();
					}
				} catch (FileNotFoundException e) {
					JOptionPane.showMessageDialog(null, 
							b.getString("filenotfounderror"),"Error", JOptionPane.ERROR_MESSAGE);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, 
							b.getString("operror"),"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		panelFichero.add(btnInsertar);
		
		JPanel panelInsTabla = new JPanel();
		panelInsTabla.setMinimumSize(new Dimension(panelInsTabla.getWidth(),200));
		GridBagConstraints gbc_panelEst = new GridBagConstraints();
		gbc_panelEst.insets = new Insets(0, 0, 5, 0);
		gbc_panelEst.fill = GridBagConstraints.BOTH;
		gbc_panelEst.gridx = 0;
		gbc_panelEst.gridy = 1;
		contentPane.add(panelInsTabla, gbc_panelEst);
		panelInsTabla.setLayout(new BoxLayout(panelInsTabla, BoxLayout.Y_AXIS));
		
		panelFiltro = new JPanel();
		panelInsTabla.add(panelFiltro);
		panelFiltro.setVisible(false);
		panelFiltro.setBackground(Color.LIGHT_GRAY);
		panelFiltro.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		JLabel lblSeachfilter = new JLabel(b.getString("searchfilter"));
		panelFiltro.add(lblSeachfilter);
		
		textFieldFilter = new JTextField();
		panelFiltro.add(textFieldFilter);
		textFieldFilter.setColumns(10);
		//Filtra por expresión regular el contenido de la tabla.
		JButton btnFilter = new JButton(new ImageIcon("res/search_16.png"));
		btnFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String cadenaBusqueda = textFieldFilter.getText();
				@SuppressWarnings("unchecked") //Sabemos que es ese tipo de sorter.
				TableRowSorter<CasosTableModel> sorter = (TableRowSorter<CasosTableModel>)
						table.getRowSorter();
				if(! cadenaBusqueda.isEmpty()){
					try{
					sorter.setRowFilter(RowFilter.regexFilter(cadenaBusqueda));
					}catch(PatternSyntaxException ex){
						JOptionPane.showMessageDialog(null, b.getString("filtererror"),
								"Error",JOptionPane.INFORMATION_MESSAGE);
					}
				}else{
					sorter.setRowFilter(null);
				}
			}
		});
		panelFiltro.add(btnFilter);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		panelInsTabla.add(scrollPane_1);
		
			scrollPane_1.setPreferredSize(new Dimension(0, 150));
			
			table = new JTable();
			table.setAutoCreateRowSorter(true);
			scrollPane_1.setViewportView(table);
			
			JLabel label = new JLabel("New label");
			scrollPane_1.setColumnHeaderView(label);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_2.setBackground(Color.GRAY);
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_2.insets = new Insets(0, 0, 5, 0);
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 2;
		contentPane.add(panel_2, gbc_panel_2);
		
		lblCargadas = new JLabel(b.getString("loadedcases")+0);
		lblCargadas.setForeground(Color.WHITE);
		panel_2.add(lblCargadas);
		//Este botón termina la importación desde fichero.
		JButton btnInsertFichero = new JButton(b.getString("insertcases"));
		panel_2.add(btnInsertFichero);
		btnInsertFichero.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				insertarCasos(casos);
			}
			
		});
		
		//Scroll que contiene la inserción manual.
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(scrollPane.getWidth(),120));
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 2;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 3;
		contentPane.add(scrollPane, gbc_scrollPane);
		
		panelManual = new JPanel();
		panelManual.setBorder(new TitledBorder(null, b.getString("manualinsert"), 
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		scrollPane.setViewportView(panelManual);
		panelManual.setLayout(new BoxLayout(panelManual, BoxLayout.Y_AXIS));
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.setBackground(Color.GRAY);
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.anchor = GridBagConstraints.NORTH;
		gbc_panel_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 5;
		contentPane.add(panel_1, gbc_panel_1);
		//Este botón inserta manualmente casos.
		JButton btnInsManual = new JButton(b.getString("manualinsert"));
		panel_1.add(btnInsManual);
		btnInsManual.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				List<HashMap<String,Serializable>> casos = new ArrayList<HashMap<String,Serializable>>();
				HashMap<String, Serializable> caso = new HashMap<String, Serializable>();
				for(int i=0; i<panelManual.getComponentCount();i++){
					PanelIntroducirValorAtbo p = (PanelIntroducirValorAtbo) panelManual.getComponent(i);
					Object valor = p.getValue();
					if(valor!=null){
						caso.put(p.getKey(),p.getValue());
					}else{
						JOptionPane.showMessageDialog(null,
								b.getString("emptyatt"), "Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
				casos.add(caso);
				insertarCasos(casos);
			}
		});
		
		//Crea paneles para insertar un caso.
		for(Atributo at : tc.getAtbos().values()){
			JPanel p = new PanelIntroducirValorAtbo(at, false);
			panelManual.add(p);
		}
		Atributo calidad= new Atributo();
		calidad.setNombre("META_QUALITY"); //Calidad del caso.
		calidad.setTipo("I"); //Entero.
		calidad.setMetrica("equal"); //Igualdad.
		panelManual.add(new PanelIntroducirValorAtbo(calidad, false));
		
		
	}
	
	/** Inserta la lista de casos enviándola al servidor.
	 * @param lcasos Lista de casos. Cada caso es un mapa clave valor de tipo:
	 * {"nombre del atributo", valor}
	 */
	private void insertarCasos(List<HashMap<String,Serializable>> lcasos){
		try {
			//Comprueba la validez de la lista de casos antes de pedir insertar
			//al servidor.
			if (lcasos!=null && !lcasos.isEmpty() && ControlCasos.insertarCasos(tc, lcasos)) {
				JOptionPane.showMessageDialog(null, 
						b.getString("opsuccess"),b.getString("opsuccess"), JOptionPane.INFORMATION_MESSAGE);
				padre.removePanel(b.getString("insertcases"));
			} else {
				JOptionPane.showMessageDialog(null, 
						b.getString("opunsuccess"),b.getString("opunsuccess"), JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null,
					b.getString("connecterror"), "Error",
					JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}
	


}
