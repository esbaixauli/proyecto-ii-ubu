package vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controlador.ControlTecnicas;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JButton;

import servidorcbr.modelo.Tecnica;
import servidorcbr.modelo.TipoCaso;
import vista.panels.PanelTecnica;

public class GestionTecnicasFrame extends JFrame {

	private JPanel contentPane;
	private ResourceBundle b = ResourceBundle.getBundle(
            "vista.internacionalizacion.Recursos", Locale.getDefault());
	private final JFrame me = this;
	private List<Tecnica> tecnicasRec;
	private List<Tecnica> tecnicasReu;
	private List<Tecnica> tecnicasRev;
	private List<Tecnica> tecnicasRet;

	/**
	 * Create the frame.
	 */
	public GestionTecnicasFrame(final TipoCaso tc, final JFrame padre) {
		setTitle(b.getString("managemethods"));
		inicializaListas(tc);
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
		
		final PanelTecnica panelRec = new PanelTecnica(b.getString("retrieval"), tecnicasRec, tc.getDefaultRec(), tc, this);
		gbc_panel.gridy = 0;
		contentPane.add(panelRec, gbc_panel);
		
		final PanelTecnica panelReu = new PanelTecnica(b.getString("reuse"), tecnicasReu, tc.getDefaultReu(), tc, this);
		gbc_panel.gridy = 1;
		contentPane.add(panelReu, gbc_panel);
		
		final PanelTecnica panelRev = new PanelTecnica(b.getString("revise"), tecnicasRev, tc.getDefaultRev(), tc, this);
		gbc_panel.gridy = 2;
		contentPane.add(panelRev, gbc_panel);

		final PanelTecnica panelRet = new PanelTecnica(b.getString("retain"), tecnicasRet, tc.getDefaultRet(), tc, this);
		gbc_panel.gridy = 3;
		contentPane.add(panelRet, gbc_panel);
		
		JPanel btnsPanel = new JPanel();
		gbc_panel.gridy = 4;
		contentPane.add(btnsPanel, gbc_panel);
		
		JButton btnAceptar = new JButton(b.getString("ok"));
		btnAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelRec.actualizaLista();
				tc.setTecnicasRecuperacion(tecnicasRec);
				tc.setDefaultRec(panelRec.getDefaultTecnica());
				
				panelReu.actualizaLista();
				tc.setTecnicasReutilizacion(tecnicasReu);
				tc.setDefaultReu(panelReu.getDefaultTecnica());
				
				panelRev.actualizaLista();
				tc.setTecnicasRevision(tecnicasRev);
				tc.setDefaultRev(panelRev.getDefaultTecnica());
				
				panelRet.actualizaLista();
				tc.setTecnicasRetencion(tecnicasRet);
				tc.setDefaultRet(panelRet.getDefaultTecnica());
				
				padre.setEnabled(true);
				me.dispose();
			}
		});
		btnsPanel.add(btnAceptar);
		me.getRootPane().setDefaultButton(btnAceptar);
		
		JButton btnCancelar = new JButton(b.getString("cancel"));
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				padre.setEnabled(true);
				me.dispose();
			}
		});
		btnsPanel.add(btnCancelar);
	}
	
	private void inicializaListas (TipoCaso tc) {
		if (tc.getTecnicasRecuperacion() == null) {
			tecnicasRec = ControlTecnicas.getTecnicasRec();
		} else {
			tecnicasRec = tc.getTecnicasRecuperacion();
		}
		if (tc.getTecnicasReutilizacion() == null) {
			tecnicasReu = ControlTecnicas.getTecnicasReu();
		} else {
			tecnicasReu = tc.getTecnicasReutilizacion();
		}
		if (tc.getTecnicasRevision() == null) {
			tecnicasRev = ControlTecnicas.getTecnicasRev();
		} else {
			tecnicasRev = tc.getTecnicasRevision();
		}
		if (tc.getTecnicasRetencion() == null) {
			tecnicasRet = ControlTecnicas.getTecnicasRet();
		} else {
			tecnicasRet = tc.getTecnicasRetencion();
		}
	}

}
