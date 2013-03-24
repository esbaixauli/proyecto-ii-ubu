package vista;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.TipoCaso;
import vista.panels.JFilePicker;
import vista.panels.PanelIntroducirValorAtbo;
import vista.tablas.CasosTableCellRenderer;
import vista.tablas.CasosTableModel;
import controlador.ControlCasos;
import controlador.util.LectorCaso;

@SuppressWarnings("serial")
public class InsertarCasoFrame extends JFrame {

	private JFilePicker pickerFichero;
	private JPanel contentPane;
	private ResourceBundle b = ResourceBundle.getBundle(
			"vista.internacionalizacion.Recursos", Locale.getDefault());

	private JPanel panelManual;
	private JLabel lblCargadas;
	private TipoCaso tc;
	private JTable table;
	private List<HashMap<String,Object>> casos;
	
	private JFrame me=this, padre;
	/**
	 * Create the frame.
	 */
	public InsertarCasoFrame(final TipoCaso tc, final JFrame padre) {
		setResizable(false);
		this.padre=padre;
		cierreVentana();
		setIconImage(new ImageIcon("res/logocbr.png").getImage());
		setTitle(b.getString("insertcases"));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 573, 380);
		this.tc=tc;
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{557, 0};
		gbl_contentPane.rowHeights = new int[]{66, 111, 0, 93, 0, 15, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JPanel panelFichero = new JPanel();
		pickerFichero = new JFilePicker("",b.getString("open"),JFilePicker.MODE_OPEN);
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
				try {
					casos=
					LectorCaso.leerCaso(new File(pickerFichero.getSelectedFilePath()), tc);
					if(casos!=null){
						table.setModel(new CasosTableModel(casos, tc));
						lblCargadas.setText(b.getString("loadedcases")+casos.size());
						table.setDefaultRenderer(Object.class, new CasosTableCellRenderer(tc));
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
		
		JScrollPane scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 0;
		gbc_scrollPane_1.gridy = 1;
		contentPane.add(scrollPane_1, gbc_scrollPane_1);
		
		table = new JTable();
		table.setAutoCreateRowSorter(true);
		scrollPane_1.setViewportView(table);
		
		JLabel label = new JLabel("New label");
		scrollPane_1.setColumnHeaderView(label);
		
		JPanel panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(0, 0, 5, 0);
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 2;
		contentPane.add(panel_2, gbc_panel_2);
		
		lblCargadas = new JLabel(b.getString("loadedcases")+0);
		panel_2.add(lblCargadas);
		
		JButton btnInsertFichero = new JButton(b.getString("insertcases"));
		panel_2.add(btnInsertFichero);
		btnInsertFichero.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				insertarCasos(casos);
			}
			
		});
		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
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
		
		JButton btnInsManual = new JButton(b.getString("manualinsert"));
		btnInsManual.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				List<HashMap<String,Object>> casos = new ArrayList<HashMap<String,Object>>();
				HashMap<String, Object> caso = new HashMap<String, Object>();
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
		GridBagConstraints gbc_btnInsManual = new GridBagConstraints();
		gbc_btnInsManual.insets = new Insets(0, 0, 5, 0);
		gbc_btnInsManual.gridx = 0;
		gbc_btnInsManual.gridy = 4;
		contentPane.add(btnInsManual, gbc_btnInsManual);
		
		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.anchor = GridBagConstraints.NORTH;
		gbc_panel_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 5;
		contentPane.add(panel_1, gbc_panel_1);
		
		
		for(Atributo at : tc.getAtbos().values()){
			JPanel p = new PanelIntroducirValorAtbo(at, false, this);
			panelManual.add(p);
		}
		
	}
	
	private void insertarCasos(List<HashMap<String,Object>> lcasos){
		try {
			
			if (lcasos!=null && !lcasos.isEmpty() && ControlCasos.insertarCasos(tc, lcasos)) {
				JOptionPane.showMessageDialog(null, 
						b.getString("opsuccess"),b.getString("opsuccess"), JOptionPane.INFORMATION_MESSAGE);
				padre.setEnabled(true);
				me.setVisible(false);
				me.dispose();
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
	
	private void cierreVentana() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				padre.setEnabled(true);
				me.dispose();
			}
		});
	}

}
