package vista.tipos.configtecnicas;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;
import javax.swing.border.TitledBorder;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.BorderLayout;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;

@SuppressWarnings("serial")
public class PanelPostAdaptacion extends JPanel {
	private ResourceBundle b = ResourceBundle.getBundle(
			"vista.internacionalizacion.Recursos", Locale.getDefault());
	private JRadioButton  rdbtnCombineAvg;
	private JRadioButton rdbtnCombineMode;
	private JCheckBox chckbxCombine;
	
	private static final String COMBINARMEDIA="MEDIA"
			,COMBINARMODA="MODA",NOCOMBINAR="NOCOMBINAR";
	
	/**
	 * Crea el panel.
	 */
	public PanelPostAdaptacion() {
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setLayout(new BorderLayout(0, 0));
		
		chckbxCombine = new JCheckBox(b.getString("combine"));
		chckbxCombine.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if(chckbxCombine.isSelected()){
					rdbtnCombineAvg.setEnabled(true);
					rdbtnCombineMode.setEnabled(true);
					rdbtnCombineAvg.setSelected(true);
				}else{
					rdbtnCombineAvg.setEnabled(false);
					rdbtnCombineMode.setEnabled(false);
					rdbtnCombineAvg.setSelected(false);
				}
			}
		});
		add(chckbxCombine,BorderLayout.NORTH);
		
		JPanel panelNum = new JPanel();
		panelNum.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"),b.getString("numeric"), TitledBorder.CENTER, TitledBorder.TOP, null, null));
		add(panelNum, BorderLayout.CENTER);
		
		rdbtnCombineAvg = new JRadioButton(b.getString("combinebyavg"));
		rdbtnCombineAvg.setEnabled(false);
		
		panelNum.add(rdbtnCombineAvg);
		
		rdbtnCombineMode = new JRadioButton(b.getString("combinebymode"));
		rdbtnCombineMode.setEnabled(false);
		panelNum.add(rdbtnCombineMode);
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(rdbtnCombineMode);
		bg.add(rdbtnCombineAvg);
	}

	/** Obtiene el modo de combinar casos actualmente seleccionado
	 * @return un entero correspondiente a las constantes COMBINARMEDIA,COMBINARMODA,NOCOMBINAR de esta clase.
	 */
	public String getCombinar(){
		if(chckbxCombine.isSelected()){
			if(rdbtnCombineAvg.isSelected()){
				return COMBINARMEDIA;
			}else{
				return COMBINARMODA;
			}
		}else{
			return NOCOMBINAR;
		}
	}
}
