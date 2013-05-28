package vista.tipos;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import servidorcbr.modelo.Tecnica;
import servidorcbr.modelo.TipoCaso;
import vista.MainFrame;
import vista.componentesGenericos.PanelEstandar;
import vista.panels.PanelTecnica;
import controlador.ControlTecnicas;
import controlador.ControlTipos;

/** Panel general de gestión dew técnicas.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
@SuppressWarnings("serial")
public class GestionTecnicasPanel extends PanelEstandar {

	/**
	 * Contentpane del panel.
	 */
	private JPanel contentPane;
	
	/**
	 * Lista de técnicas de recuperación de este tipo de caso. Su contenido será
	 * cambiado dinámicamente en este panel
	 */
	private List<Tecnica> tecnicasRec;
	/**
	 * Lista de técnicas de reutilización de este tipo de caso. Su contenido será
	 * cambiado dinámicamente en este panel
	 */
	private List<Tecnica> tecnicasReu;
	/**
	 * Lista de técnicas de revisión de este tipo de caso. Su contenido será
	 * cambiado dinámicamente en este panel
	 */
	private List<Tecnica> tecnicasRev;
	/**
	 * Lista de técnicas de retención de este tipo de caso. Su contenido será
	 * cambiado dinámicamente en este panel
	 */
	private List<Tecnica> tecnicasRet;

	/**
	 * Cierto si es un tipo de caso nuevo (Aún no insertado en el servidor).
	 */
	private boolean nuevocaso;

	/**Crea el panel. Requiere un tipo de caso al que se asocian las técnicas aquí
	 * configuradas.
	 * @param tc Tipo de caso asociado a este panel
	 * @param padre Ventana padre de este panel.
	 * @param nuevo Cierto si el tipo de caso aún no está registrado en el servidor.
	 * (Caso nuevo).
	 */
	public GestionTecnicasPanel(final TipoCaso tc, final MainFrame padre, boolean nuevo) {
		//Establezco referencias al padre y al propio panel
		super(padre);me = this;
		nuevocaso=nuevo;
		setName(b.getString("managemethods")+": "+tc.getNombre());
		inicializaListas(tc);
	
		setBounds(100, 100, 550, 524);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		add(contentPane);
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
				padre);
		gbc_panel.gridy = 0;
		contentPane.add(panelRec, gbc_panel.clone());

		final PanelTecnica panelReu = new PanelTecnica(b.getString("reuse"),
				tecnicasReu, tc.getDefaultReu(), tc, padre);
		gbc_panel.gridy = 1;
		contentPane.add(panelReu, gbc_panel.clone());

		final PanelTecnica panelRev = new PanelTecnica(b.getString("revise"),
				tecnicasRev, tc.getDefaultRev(), tc, padre);
		gbc_panel.gridy = 2;
		contentPane.add(panelRev, gbc_panel.clone());

		final PanelTecnica panelRet = new PanelTecnica(b.getString("retain"),
				tecnicasRet, tc.getDefaultRet(), tc, padre);
		gbc_panel.gridy = 3;
		contentPane.add(panelRet, gbc_panel.clone());

		JPanel btnsPanel = new JPanel();
		btnsPanel.setBackground(Color.GRAY);
		btnsPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		gbc_panel.gridy = 4;
		contentPane.add(btnsPanel, gbc_panel.clone());

		//Establece la configuración de técnicas final al aceptar.
		JButton btnAceptar = new JButton(b.getString("ok"));
		btnAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Actualiza las técnicas para cada etapa.
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
				
				if(nuevocaso){//Si estoy creando un nuevo tipo de caso
					try{
						//Inserto el tipo de caso
						ControlTipos.addTipo(tc);
						padre.removePanel(me.getName());
						}catch(java.io.IOException exc){
							JOptionPane.showMessageDialog(null,
									b.getString("inserterror"), "Error",
									JOptionPane.ERROR_MESSAGE);
							exc.printStackTrace();
						}
				}else{//Si estoy modificando
					try{
						ControlTipos.modificarTipo(tc);
						padre.removePanel(me.getName());
					}catch(IOException ex){
						JOptionPane.showMessageDialog(null,
								b.getString("moderror"), "Error",
								JOptionPane.ERROR_MESSAGE);
						ex.printStackTrace();
					}
				}
				//Reactiva la ventana padre si es necesario.
				padre.setEnabled(true);
				//Y se elimina este panel del padre.
				padre.removePanel(getName());
			}
		});
		btnsPanel.add(btnAceptar);
		

		JButton btnCancelar = new JButton(b.getString("cancel"));
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				padre.setEnabled(true);
				padre.removePanel(getName());
			}
		});
		btnsPanel.add(btnCancelar);
	
	}

	/**Marca qué técnicas han de estar habilitadas, de acuerdo con lista de técnicas
	 * suministrada.
	 * @param entera Lista completa de técnicas.
	 * @param marcada Lista de técnicas marcadas como habilitadas.
	 */
	private void marcaTecnicasEnabled(List<Tecnica> entera,List<Tecnica> marcada){
		for (Tecnica ac : entera) {
			for (Tecnica t : marcada) {
				if (!t.getNombre().equals(ac.getNombre())) {
					ac.setEnabled(false);
				}else{
					ac.setEnabled(true);
					ac.setParams(t.getParams());
					break;
				}
			}
		}
	}
	
	/**Inicializa las listas para un tipo de caso concreto, obteniendo la lista
	 * de técnicas en cada etapa para este tipo de caso.
	 * @param tc Tipo de caso.
	 */
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
