package vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controlador.ControlTecnicas;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JButton;

import servidorcbr.modelo.TipoCaso;
import vista.panels.PanelTecnica;

public class GestionTecnicasFrame extends JFrame {

	private JPanel contentPane;
	private ResourceBundle b = ResourceBundle.getBundle(
            "vista.internacionalizacion.Recursos", Locale.getDefault());
	private final JFrame me = this;

	/**
	 * Create the frame.
	 */
	public GestionTecnicasFrame(TipoCaso tc, final JFrame padre) {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				padre.setEnabled(true);
				me.dispose();
			}
		});
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
		
		PanelTecnica panelRec = new PanelTecnica(b.getString("retrieval"), ControlTecnicas.getTecnicasRec(), tc, this);
		gbc_panel.gridy = 0;
		contentPane.add(panelRec, gbc_panel);
		
		PanelTecnica panelReu = new PanelTecnica(b.getString("reuse"), ControlTecnicas.getTecnicasReu(), tc, this);
		gbc_panel.gridy = 1;
		contentPane.add(panelReu, gbc_panel);
		
		PanelTecnica panelRev = new PanelTecnica(b.getString("revise"), ControlTecnicas.getTecnicasRev(), tc, this);
		gbc_panel.gridy = 2;
		contentPane.add(panelRev, gbc_panel);

		PanelTecnica panelRet = new PanelTecnica(b.getString("retain"), ControlTecnicas.getTecnicasRet(), tc, this);
		gbc_panel.gridy = 3;
		contentPane.add(panelRet, gbc_panel);
		
		JPanel btnsPanel = new JPanel();
		gbc_panel.gridy = 4;
		contentPane.add(btnsPanel, gbc_panel);
		
		JButton btnAceptar = new JButton(b.getString("ok"));
		btnsPanel.add(btnAceptar);
		
		JButton btnCancelar = new JButton(b.getString("cancel"));
		btnsPanel.add(btnCancelar);
	}

}
