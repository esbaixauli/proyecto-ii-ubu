package vista;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import servidorcbr.modelo.Tecnica;
import servidorcbr.modelo.TipoCaso;
import vista.componentes.FrameEstandar;
import vista.panels.PanelTecnica;
import controlador.ControlTecnicas;
import javax.swing.border.EtchedBorder;
import java.awt.Color;

@SuppressWarnings("serial")
public class GestionTecnicasFrame extends FrameEstandar {

	private JPanel contentPane;
	
	private List<Tecnica> tecnicasRec;
	private List<Tecnica> tecnicasReu;
	private List<Tecnica> tecnicasRev;
	private List<Tecnica> tecnicasRet;

	/**
	 * Create the frame.
	 */
	public GestionTecnicasFrame(final TipoCaso tc, final JFrame padre) {
		//Establezco referencias al padre y a la propia ventana
		super(padre);me = this;
		setTitle(b.getString("managemethods"));
		inicializaListas(tc);
	
		setBounds(100, 100, 550, 524);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;

		final PanelTecnica panelRec = new PanelTecnica(
				b.getString("retrieval"), tecnicasRec, tc.getDefaultRec(), tc,
				this);
		gbc_panel.gridy = 0;
		contentPane.add(panelRec, gbc_panel.clone());

		final PanelTecnica panelReu = new PanelTecnica(b.getString("reuse"),
				tecnicasReu, tc.getDefaultReu(), tc, this);
		gbc_panel.gridy = 1;
		contentPane.add(panelReu, gbc_panel.clone());

		final PanelTecnica panelRev = new PanelTecnica(b.getString("revise"),
				tecnicasRev, tc.getDefaultRev(), tc, this);
		gbc_panel.gridy = 2;
		contentPane.add(panelRev, gbc_panel.clone());

		final PanelTecnica panelRet = new PanelTecnica(b.getString("retain"),
				tecnicasRet, tc.getDefaultRet(), tc, this);
		gbc_panel.gridy = 3;
		contentPane.add(panelRet, gbc_panel.clone());

		JPanel btnsPanel = new JPanel();
		btnsPanel.setBackground(Color.GRAY);
		btnsPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		gbc_panel.gridy = 4;
		contentPane.add(btnsPanel, gbc_panel.clone());

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
		setLocationRelativeTo(padre);
	}

	private void marcaTecnicasEnabled(List<Tecnica> entera,List<Tecnica> marcada){
		for (Tecnica ac : entera) {
			for (Tecnica t : marcada) {
				if (!t.getNombre().equals(ac.getNombre())) {
					ac.setEnabled(false);
				}else{
					ac.setEnabled(true);
					break;
				}
			}
		}
	}
	
	private void inicializaListas(TipoCaso tc) {
		tecnicasRec = ControlTecnicas.getTecnicasRec();
		tecnicasReu = ControlTecnicas.getTecnicasReu();
		tecnicasRev = ControlTecnicas.getTecnicasRev();
		tecnicasRet = ControlTecnicas.getTecnicasRet();
		if (tc.getTecnicasRecuperacion() != null) {
			marcaTecnicasEnabled(tecnicasRec,tc.getTecnicasRecuperacion());
			
		}
		if (tc.getTecnicasReutilizacion() != null) {
			marcaTecnicasEnabled(tecnicasReu,tc.getTecnicasReutilizacion());
		}
		if (tc.getTecnicasRevision() != null) {
			marcaTecnicasEnabled(tecnicasRev,tc.getTecnicasRevision());
		}
		if (tc.getTecnicasRetencion() != null) {
			marcaTecnicasEnabled(tecnicasRet,tc.getTecnicasRetencion());
		}
	}

}
