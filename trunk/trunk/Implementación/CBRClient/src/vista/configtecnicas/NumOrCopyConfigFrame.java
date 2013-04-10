package vista.configtecnicas;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.Parametro;
import servidorcbr.modelo.Tecnica;
import servidorcbr.modelo.TipoCaso;
import vista.componentes.FrameEstandar;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class NumOrCopyConfigFrame extends FrameEstandar {

	private JPanel contentPane;
	private JFrame me;
	private TipoCaso tc;
	private JComboBox<String> comboBoxOrigen; 
	private JComboBox<String> comboBoxDestino; 
	
	/** Constructor del frame.
	 * @param esCopyMethod Especifica si este frame sirve para configurar la técnica Direct Copy Method o
	 * la técnica Numeric Proportion.
	 * @param t La técnica a configurar.
	 * @param tc
	 * @param padre
	 */
	public NumOrCopyConfigFrame(boolean esCopyMethod,final Tecnica t,TipoCaso tc, final JFrame padre) {
		super(padre);me=this;
		this.tc=tc;
		setTitle(b.getString("configuremethod"));
		setBounds(100, 100, 345, 211);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{109, 198, 0};
		gbl_contentPane.rowHeights = new int[]{36, 20, 20, 31, 33, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblSource = new JLabel(b.getString("source")+":");
		GridBagConstraints gbc_lblSource = new GridBagConstraints();
		gbc_lblSource.anchor = GridBagConstraints.EAST;
		gbc_lblSource.insets = new Insets(0, 0, 5, 5);
		gbc_lblSource.gridx = 0;
		gbc_lblSource.gridy = 1;
		contentPane.add(lblSource, gbc_lblSource);

		comboBoxOrigen = new JComboBox<String>();
		
		GridBagConstraints gbc_comboBoxOrigen = new GridBagConstraints();
		gbc_comboBoxOrigen.anchor = GridBagConstraints.NORTH;
		gbc_comboBoxOrigen.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxOrigen.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxOrigen.gridx = 1;
		gbc_comboBoxOrigen.gridy = 1;
		contentPane.add(comboBoxOrigen, gbc_comboBoxOrigen);
		
		JLabel lblDestination = new JLabel(b.getString("destination")+":");
		GridBagConstraints gbc_lblDestination = new GridBagConstraints();
		gbc_lblDestination.anchor = GridBagConstraints.EAST;
		gbc_lblDestination.insets = new Insets(0, 0, 5, 5);
		gbc_lblDestination.gridx = 0;
		gbc_lblDestination.gridy = 2;
		contentPane.add(lblDestination, gbc_lblDestination);
		
		comboBoxDestino = new JComboBox<String>();
		GridBagConstraints gbc_comboBoxDestino = new GridBagConstraints();
		gbc_comboBoxDestino.anchor = GridBagConstraints.NORTH;
		gbc_comboBoxDestino.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxDestino.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxDestino.gridx = 1;
		gbc_comboBoxDestino.gridy = 2;
		contentPane.add(comboBoxDestino, gbc_comboBoxDestino);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridwidth = 2;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 4;
		contentPane.add(panel, gbc_panel);
		
		
		JButton btnOk = new JButton(b.getString("ok"));
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<Parametro> l = new ArrayList<Parametro>(1);
				Parametro p1 = new Parametro();
				p1.setNombre(comboBoxOrigen.getSelectedItem()+"");
				p1.setValor(1);
				Parametro p2 = new Parametro();
				p2.setNombre(comboBoxDestino.getSelectedItem()+"");
				p2.setValor(2);
				l.add(p1);
				l.add(p2);
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
		//Relleno el combobox con los atributos del problema
		for(Atributo a : tc.getAtbos().values()){
			if(a.getEsProblema()){
				comboBoxOrigen.addItem(a.getNombre());
			}
		}
		//Si estoy configurando DirectCopy, no hay atributo destino.
		if(esCopyMethod){
			comboBoxDestino.setEnabled(false);
			lblDestination.setEnabled(false);
		}else{	//Si estoy configurando Numeric proportion, el atributo destino es de la solución.
			for(Atributo a : tc.getAtbos().values()){
				if(!a.getEsProblema()){
					comboBoxDestino.addItem(a.getNombre());
				}
			}
		}
	}

}
