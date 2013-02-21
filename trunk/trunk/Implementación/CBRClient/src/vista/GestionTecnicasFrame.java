package vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controlador.ControlTecnicas;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JButton;

public class GestionTecnicasFrame extends JFrame {

	private JPanel contentPane;
	private ResourceBundle b = ResourceBundle.getBundle(
            "vista.internacionalizacion.Recursos", Locale.getDefault());

	/**
	 * Create the frame.
	 */
	public GestionTecnicasFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 550, 524);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		
		//PanelTecnica panelRec = new PanelTecnica(b.getString("retrieval"), ControlTecnicas.getTecnicasRec());
		PanelTecnica panelRec = new PanelTecnica("retrieval", ControlTecnicas.getTecnicasRec());
		gbc_panel.gridy = 0;
		contentPane.add(panelRec, gbc_panel);
		
		//PanelTecnica panelReu = new PanelTecnica(b.getString("reuse"), ControlTecnicas.getTecnicasReu());
		PanelTecnica panelReu = new PanelTecnica("reuse", ControlTecnicas.getTecnicasReu());
		gbc_panel.gridy = 1;
		contentPane.add(panelReu, gbc_panel);
		
		//PanelTecnica panelRev = new PanelTecnica(b.getString("revise"), ControlTecnicas.getTecnicasRev());
		PanelTecnica panelRev = new PanelTecnica("revise", ControlTecnicas.getTecnicasRev());
		gbc_panel.gridy = 2;
		contentPane.add(panelRev, gbc_panel);

		//PanelTecnica panelRet = new PanelTecnica(b.getString("retain"), ControlTecnicas.getTecnicasRet());
		PanelTecnica panelRet = new PanelTecnica("retain", ControlTecnicas.getTecnicasRet());
		gbc_panel.gridy = 3;
		contentPane.add(panelRet, gbc_panel);
		
		JPanel btnsPanel = new JPanel();
		gbc_panel.gridy = 4;
		contentPane.add(btnsPanel, gbc_panel);
		
		//JButton btnAceptar = new JButton(b.getString("ok"));
		JButton btnAceptar = new JButton("Aceptar");
		btnsPanel.add(btnAceptar);
		
		//JButton btnCancelar = new JButton(b.getString("cancel"));
		JButton btnCancelar = new JButton("Cancelar");
		btnsPanel.add(btnCancelar);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GestionTecnicasFrame frame = new GestionTecnicasFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
