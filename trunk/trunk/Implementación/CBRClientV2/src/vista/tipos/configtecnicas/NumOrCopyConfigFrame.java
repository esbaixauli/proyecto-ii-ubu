package vista.tipos.configtecnicas;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.Parametro;
import servidorcbr.modelo.Tecnica;
import servidorcbr.modelo.TipoCaso;
import vista.MainFrame;
import vista.componentesGenericos.FrameEstandar;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.border.EtchedBorder;
import java.awt.Color;

@SuppressWarnings("serial")
public class NumOrCopyConfigFrame extends FrameEstandar {

	private JPanel contentPane;
	private JFrame me;
	@SuppressWarnings("unused")
	private TipoCaso tc;
	private JComboBox<String> comboBoxOrigen; 
	private JComboBox<String> comboBoxDestino; 
	private PanelPostAdaptacion panelPostAdaptacion;
	
	/** Constructor del frame.
	 * @param esCopyMethod Especifica si este frame sirve para configurar la técnica Direct Copy Method o
	 * la técnica Numeric Proportion. Cierto si Es copy Method, falso si no.
	 * @param t La técnica a configurar.
	 * @param tc
	 * @param padre
	 */
	public NumOrCopyConfigFrame(boolean esCopyMethod,final Tecnica t,TipoCaso tc, final MainFrame padre) {
		super(padre);me=this;
		this.tc=tc;
		setTitle(b.getString("configuremethod"));
		setBounds(100, 100, 418, 253);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2, BorderLayout.NORTH);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.Y_AXIS));
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.GRAY);
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_2.add(panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{120, 59, 120, 0};
		gbl_panel_1.rowHeights = new int[]{30, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		JLabel lblSource = new JLabel(b.getString("source")+":");
		lblSource.setForeground(Color.WHITE);
		GridBagConstraints gbc_lblSource = new GridBagConstraints();
		gbc_lblSource.anchor = GridBagConstraints.EAST;
		gbc_lblSource.insets = new Insets(0, 0, 0, 5);
		gbc_lblSource.gridx = 1;
		gbc_lblSource.gridy = 0;
		panel_1.add(lblSource, gbc_lblSource);
		
				comboBoxOrigen = new JComboBox<String>();
				GridBagConstraints gbc_comboBoxOrigen = new GridBagConstraints();
				gbc_comboBoxOrigen.fill = GridBagConstraints.HORIZONTAL;
				gbc_comboBoxOrigen.gridx = 2;
				gbc_comboBoxOrigen.gridy = 0;
				panel_1.add(comboBoxOrigen, gbc_comboBoxOrigen);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(Color.GRAY);
		panel_3.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_2.add(panel_3);
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[]{120, 59, 120, 0};
		gbl_panel_3.rowHeights = new int[]{30, 0};
		gbl_panel_3.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_3.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_3.setLayout(gbl_panel_3);
		
		JLabel lblDestination = new JLabel(b.getString("destination")+":");
		lblDestination.setForeground(Color.WHITE);
		GridBagConstraints gbc_lblDestination = new GridBagConstraints();
		gbc_lblDestination.anchor = GridBagConstraints.EAST;
		gbc_lblDestination.insets = new Insets(0, 0, 0, 5);
		gbc_lblDestination.gridx = 1;
		gbc_lblDestination.gridy = 0;
		panel_3.add(lblDestination, gbc_lblDestination);
		
		comboBoxDestino = new JComboBox<String>();
		GridBagConstraints gbc_comboBoxDestino = new GridBagConstraints();
		gbc_comboBoxDestino.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxDestino.gridx = 2;
		gbc_comboBoxDestino.gridy = 0;
		panel_3.add(comboBoxDestino, gbc_comboBoxDestino);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.GRAY);
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		contentPane.add(panel,BorderLayout.SOUTH);
		
		
		JButton btnOk = new JButton(b.getString("ok"));
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<Parametro> l = new ArrayList<Parametro>();
				//campo origen
				Parametro p1 = new Parametro();
				p1.setNombre(comboBoxOrigen.getSelectedItem()+"");
				p1.setValor(1);
				//campo de destino
				Parametro p2 = new Parametro();
				if (comboBoxDestino.isEnabled()) {
					p2.setNombre(comboBoxDestino.getSelectedItem()+"");
				} else {
					p2.setNombre(p1.getNombre());
				}
				p2.setValor(2);
				//Combinar o no, y qué tipo de combinación
				Parametro p3 = new Parametro();
				p3.setNombre(panelPostAdaptacion.getCombinar());
				p3.setValor(3);
				l.add(p1);
				l.add(p2);
				l.add(p3);
				t.setParams(l);
				padre.setEnabled(true);
				me.setVisible(false);
				me.dispose();
			}
		});
		me.getRootPane().setDefaultButton(btnOk);
		panel.add(btnOk);
		
		JButton btnCancel = new JButton(b.getString("cancel"));
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				padre.setEnabled(true);
				me.setVisible(false);
				me.dispose();
			}
		});
		panel.add(btnCancel);
		
		panelPostAdaptacion = new PanelPostAdaptacion();
		contentPane.add(panelPostAdaptacion, BorderLayout.CENTER);
		//Relleno el combobox con los atributos del problema
		for(Atributo a : tc.getAtbos().values()){
			if(a.getEsProblema() && (esCopyMethod ||
					(a.getTipo().equals("D")||a.getTipo().equals("I") ))){
				comboBoxOrigen.addItem(a.getNombre());
			}
		}
		//Si estoy configurando DirectCopy, no hay atributo destino.
		if(esCopyMethod){
			comboBoxDestino.setEnabled(false);
			lblDestination.setEnabled(false);
		}else{	//Si estoy configurando Numeric proportion, el atributo destino es de la solución.
			for(Atributo a : tc.getAtbos().values()){
				if(!a.getEsProblema() && (a.getTipo().equals("D") || a.getTipo().equals("I"))){
					comboBoxDestino.addItem(a.getNombre());
				}
			}
		}
		setLocationRelativeTo(padre);
	}

}
