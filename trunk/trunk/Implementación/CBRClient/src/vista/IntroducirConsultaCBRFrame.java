package vista;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.Tecnica;
import servidorcbr.modelo.TipoCaso;
import vista.configtecnicas.DiverseByMedianConfigFrame;
import vista.configtecnicas.FilterBasedConfigFrame;
import vista.configtecnicas.NNConfigFrame;
import vista.panels.PanelIntroducirValorAtbo;

import java.awt.FlowLayout;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JLabel;

import controlador.ControlTecnicas;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

@SuppressWarnings("serial")
public class IntroducirConsultaCBRFrame extends JFrame {

	private JPanel contentPane;
	
	private JFrame cRec,cReu,cRev,cRet;

	private ResourceBundle b = ResourceBundle.getBundle(
			"vista.internacionalizacion.Recursos", Locale.getDefault());
	
	private TipoCaso tc;
	private JFrame padre,me=this;
	private JPanel panelAtbos;
	private boolean configurado;
	private JComboBox<String> comboBoxRec;
	private JButton btnConfigRec;
	private JComboBox<String> comboBoxReu;
	private JButton btnConfigReu;
	private JButton btnConfigRev;
	private JButton btnConfigRet;
	private JComboBox<String> comboBoxRev;
	private JComboBox<String> comboBoxRet;
	private JLabel lblRec;
	private JLabel lblReu;
	private JLabel lblRev;
	private JLabel lblRet;
	private JButton btnEjecutarCiclo;
	
	/**
	 * Create the frame.
	 */
	public IntroducirConsultaCBRFrame(TipoCaso tic, boolean configurado,JFrame padre) {
		setResizable(false);
		this.tc=tic;
		this.configurado=configurado;
		this.padre=padre;
		setTitle("CBR: "+tc.getNombre());
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		cierreVentana();
		
		setBounds(100, 100, 397, 379);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 212, 154, 0};
		gbl_contentPane.rowHeights = new int[]{160, 0, 0, 0, 0, 0, 0, 0, 0, 0, 41, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		panelAtbos = new JPanel();
		scrollPane.setViewportView(panelAtbos);
		panelAtbos.setLayout(new BoxLayout(panelAtbos, BoxLayout.Y_AXIS));
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 0;
		contentPane.add(scrollPane, gbc_scrollPane);
		
		lblRec = new JLabel(b.getString("retrieval"));
		GridBagConstraints gbc_lblRec = new GridBagConstraints();
		gbc_lblRec.anchor = GridBagConstraints.WEST;
		gbc_lblRec.insets = new Insets(0, 0, 5, 5);
		gbc_lblRec.gridx = 1;
		gbc_lblRec.gridy = 2;
		contentPane.add(lblRec, gbc_lblRec);
		
		comboBoxRec = new JComboBox<String>();
		comboBoxRec.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				estableceFrameTecRec();
			}
		});
		GridBagConstraints gbc_comboBoxRec = new GridBagConstraints();
		gbc_comboBoxRec.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxRec.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxRec.gridx = 1;
		gbc_comboBoxRec.gridy = 3;
		contentPane.add(comboBoxRec, gbc_comboBoxRec);
		
		btnConfigRec = new JButton(b.getString("configure"));
		btnConfigRec.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cRec.setVisible(true);
				me.setEnabled(false);
			}
		});
		GridBagConstraints gbc_btnConfigRec = new GridBagConstraints();
		gbc_btnConfigRec.insets = new Insets(0, 0, 5, 0);
		gbc_btnConfigRec.gridx = 2;
		gbc_btnConfigRec.gridy = 3;
		contentPane.add(btnConfigRec, gbc_btnConfigRec);
		
		lblReu = new JLabel(b.getString("reuse"));
		GridBagConstraints gbc_lblReu = new GridBagConstraints();
		gbc_lblReu.anchor = GridBagConstraints.WEST;
		gbc_lblReu.insets = new Insets(0, 0, 5, 5);
		gbc_lblReu.gridx = 1;
		gbc_lblReu.gridy = 4;
		contentPane.add(lblReu, gbc_lblReu);
		
		comboBoxReu = new JComboBox<String>();
		comboBoxReu.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				estableceFrameTecReu();
			}
		});
		GridBagConstraints gbc_comboBoxReu = new GridBagConstraints();
		gbc_comboBoxReu.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxReu.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxReu.gridx = 1;
		gbc_comboBoxReu.gridy = 5;
		contentPane.add(comboBoxReu, gbc_comboBoxReu);
		
		btnConfigReu = new JButton(b.getString("configure"));
		btnConfigReu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cReu.setVisible(true);
				me.setEnabled(false);
			}
		});
		GridBagConstraints gbc_btnConfigReu = new GridBagConstraints();
		gbc_btnConfigReu.insets = new Insets(0, 0, 5, 0);
		gbc_btnConfigReu.gridx = 2;
		gbc_btnConfigReu.gridy = 5;
		contentPane.add(btnConfigReu, gbc_btnConfigReu);
		
		lblRev = new JLabel(b.getString("revise"));
		GridBagConstraints gbc_lblRev = new GridBagConstraints();
		gbc_lblRev.anchor = GridBagConstraints.WEST;
		gbc_lblRev.insets = new Insets(0, 0, 5, 5);
		gbc_lblRev.gridx = 1;
		gbc_lblRev.gridy = 6;
		contentPane.add(lblRev, gbc_lblRev);
		
		comboBoxRev = new JComboBox<String>();
		comboBoxRev.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				estableceFrameTecRev();
			}
		});
		GridBagConstraints gbc_comboBoxRev = new GridBagConstraints();
		gbc_comboBoxRev.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxRev.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxRev.gridx = 1;
		gbc_comboBoxRev.gridy = 7;
		contentPane.add(comboBoxRev, gbc_comboBoxRev);
		
		btnConfigRev = new JButton(b.getString("configure"));
		btnConfigRev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cRev.setVisible(true);
				me.setEnabled(false);
			}
		});
		GridBagConstraints gbc_btnConfigRev = new GridBagConstraints();
		gbc_btnConfigRev.insets = new Insets(0, 0, 5, 0);
		gbc_btnConfigRev.gridx = 2;
		gbc_btnConfigRev.gridy = 7;
		contentPane.add(btnConfigRev, gbc_btnConfigRev);
		
		lblRet = new JLabel(b.getString("retain"));
		GridBagConstraints gbc_lblRet = new GridBagConstraints();
		gbc_lblRet.anchor = GridBagConstraints.WEST;
		gbc_lblRet.insets = new Insets(0, 0, 5, 5);
		gbc_lblRet.gridx = 1;
		gbc_lblRet.gridy = 8;
		contentPane.add(lblRet, gbc_lblRet);
		
		comboBoxRet = new JComboBox<String>();
		comboBoxRet.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				estableceFrameTecRet();
			}
		});
		GridBagConstraints gbc_comboBoxRet = new GridBagConstraints();
		gbc_comboBoxRet.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxRet.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxRet.gridx = 1;
		gbc_comboBoxRet.gridy = 9;
		contentPane.add(comboBoxRet, gbc_comboBoxRet);
		
		btnConfigRet = new JButton(b.getString("configure"));
		btnConfigRet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cRet.setVisible(true);
				me.setEnabled(false);
			}
		});
		GridBagConstraints gbc_btnConfigRet = new GridBagConstraints();
		gbc_btnConfigRet.insets = new Insets(0, 0, 5, 0);
		gbc_btnConfigRet.gridx = 2;
		gbc_btnConfigRet.gridy = 9;
		contentPane.add(btnConfigRet, gbc_btnConfigRet);
		
		btnEjecutarCiclo = new JButton(b.getString("executeCBR"));
		GridBagConstraints gbc_btnEjecutarCiclo = new GridBagConstraints();
		gbc_btnEjecutarCiclo.anchor = GridBagConstraints.NORTH;
		gbc_btnEjecutarCiclo.insets = new Insets(0, 0, 5, 0);
		gbc_btnEjecutarCiclo.gridx = 2;
		gbc_btnEjecutarCiclo.gridy = 10;
		contentPane.add(btnEjecutarCiclo, gbc_btnEjecutarCiclo);
		rellenarAtributos();
		rellenarListas();
		estableceFrameTecRec();
		estableceFrameTecRet();
		estableceFrameTecReu();
		estableceFrameTecRev();
	}
	
	private void estableceFrameTecRec(){
		
		String tec = (String) comboBoxRec.getSelectedItem();
		asignaTecnicaCB(tec,"rec");
		Tecnica t = tc.getDefaultRec();
	
		switch(tec){
		case "DiverseByMedianRetrieval":
			cRec =new DiverseByMedianConfigFrame(t, tc, me);
			;break;
		case "FilterBasedRetrieval":
			cRec =new FilterBasedConfigFrame(t, tc, me);
			;break;
		case "NNretrieval":
			cRec =new NNConfigFrame(t, me);
			;break;
		default: ;
		}
	}
	
	private void estableceFrameTecReu(){
		String tec = (String) comboBoxReu.getSelectedItem();
		asignaTecnicaCB(tec,"reu");
		//En caso de crear nuevas técnicas,se debe seguir el switch de estableceFrameTecRec
		btnConfigReu.setEnabled(false);
	}
	
	private void estableceFrameTecRev(){
		String tec = (String) comboBoxRev.getSelectedItem();
		asignaTecnicaCB(tec,"rev");
		//En caso de crear nuevas técnicas,se debe seguir el switch de estableceFrameTecRec
		btnConfigRev.setEnabled(false);
	}
	
	private void estableceFrameTecRet(){
		String tec = (String) comboBoxRet.getSelectedItem();
		asignaTecnicaCB(tec,"ret");
		//En caso de crear nuevas técnicas,se debe seguir el switch de estableceFrameTecRec
		btnConfigRet.setEnabled(false);
	}
	
	
	
	private void rellenarAtributos(){
		for(Atributo at : tc.getAtbos().values()){
			if(at.getEsProblema()){
			panelAtbos.add(new PanelIntroducirValorAtbo(at, configurado,me));
			}
		}
	}
	
	//Auxiliar. Asigna la técnica de un combobox como técnica por defecto del tipo de caso.
	private void asignaTecnicaCB(String tecnicaCB,String tipoTec){
		List<Tecnica> disponibles=null;
		switch(tipoTec){
		case "rec":disponibles = tc.getTecnicasRecuperacion();break;
		case "rev":disponibles = tc.getTecnicasRevision();break;
		case "reu":disponibles = tc.getTecnicasReutilizacion();break;
		case "ret":disponibles = tc.getTecnicasRetencion();break;
		}
		Tecnica t = ControlTecnicas.buscaTecnica(disponibles, tecnicaCB);
		switch(tipoTec){
		case "rec":tc.setDefaultRec(t);break;
		case "rev":tc.setDefaultRev(t);break;
		case "reu":tc.setDefaultReu(t);break;
		case "ret":tc.setDefaultRet(t);break;
		}
	}
	
	private void rellenarListas(){
		for(Tecnica t: tc.getTecnicasRecuperacion()){
			comboBoxRec.addItem(t.getNombre());
		}
			comboBoxRec.setSelectedItem(tc.getDefaultRec().toString());
		for(Tecnica t: tc.getTecnicasReutilizacion()){
			comboBoxReu.addItem(t.getNombre());
		}
		comboBoxReu.setSelectedItem(tc.getDefaultReu().toString());
		for(Tecnica t: tc.getTecnicasRevision()){
			comboBoxRev.addItem(t.getNombre());
		}
		comboBoxRev.setSelectedItem(tc.getDefaultRev().toString());
		for(Tecnica t: tc.getTecnicasRetencion()){
			comboBoxRet.addItem(t.getNombre());
		}
		comboBoxRet.setSelectedItem(tc.getDefaultRet().toString());
	}
	
	private void cierreVentana(){
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				padre.setEnabled(true);
				me.dispose();
			}
		});
	}
	

}
