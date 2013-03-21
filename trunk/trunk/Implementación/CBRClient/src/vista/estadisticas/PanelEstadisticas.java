package vista.estadisticas;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JList;

import controlador.ControlTipos;
import controlador.ControlUsuarios;

import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.Usuario;
import vista.InsertarCasoFrame;
import vista.panels.ListaCasos;
import vista.panels.ListaUsuarios;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.FlowLayout;

public class PanelEstadisticas extends JPanel {

	private ListaCasos listaC = null;
	private ListaUsuarios listaU = null;
	private EnumEstadisticas tipo;
	private Usuario u;
	private ResourceBundle b = ResourceBundle.getBundle(
			"vista.internacionalizacion.Recursos", Locale.getDefault());
	
	private final JPanel panel_listas;

	/**
	 * Create the panel.
	 */
	public PanelEstadisticas(EnumEstadisticas tipo, Usuario u) {
		this.u = u;
		setLayout(new BorderLayout(0, 0));
		this.tipo = tipo;
		
		panel_listas=new JPanel();
		add(panel_listas,BorderLayout.WEST);
		panel_listas.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JPanel panelEstInterno = new JPanel();
		add(panelEstInterno, BorderLayout.CENTER);
		GridBagLayout gbl_panelEstInterno = new GridBagLayout();
		gbl_panelEstInterno.columnWidths = new int[]{30, 91, 125, 169, 0};
		gbl_panelEstInterno.rowHeights = new int[]{30, 14, 47, 14, 14, 14, 14, 0, 14, 0, 14, 0, 0, 0, 0, 0};
		gbl_panelEstInterno.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panelEstInterno.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelEstInterno.setLayout(gbl_panelEstInterno);
		
		JLabel lblInforme = new JLabel("New label");
		GridBagConstraints gbc_lblInforme = new GridBagConstraints();
		gbc_lblInforme.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblInforme.insets = new Insets(0, 0, 5, 5);
		gbc_lblInforme.gridx = 1;
		gbc_lblInforme.gridy = 1;
		panelEstInterno.add(lblInforme, gbc_lblInforme);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.gridheight = 5;
		gbc_panel.gridx = 3;
		gbc_panel.gridy = 2;
		panelEstInterno.add(panel, gbc_panel);
		
		JLabel lblEjecucionestotales = new JLabel("EjecucionesTotales");
		GridBagConstraints gbc_lblEjecucionestotales = new GridBagConstraints();
		gbc_lblEjecucionestotales.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblEjecucionestotales.insets = new Insets(0, 0, 5, 5);
		gbc_lblEjecucionestotales.gridx = 1;
		gbc_lblEjecucionestotales.gridy = 3;
		panelEstInterno.add(lblEjecucionestotales, gbc_lblEjecucionestotales);
		
		JLabel lblMediaCalidad = new JLabel("MediaCalidad");
		GridBagConstraints gbc_lblMediaCalidad = new GridBagConstraints();
		gbc_lblMediaCalidad.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblMediaCalidad.insets = new Insets(0, 0, 5, 5);
		gbc_lblMediaCalidad.gridx = 1;
		gbc_lblMediaCalidad.gridy = 4;
		panelEstInterno.add(lblMediaCalidad, gbc_lblMediaCalidad);
		
		JLabel lblUltimaej = new JLabel("UltimaEj");
		GridBagConstraints gbc_lblUltimaej = new GridBagConstraints();
		gbc_lblUltimaej.anchor = GridBagConstraints.WEST;
		gbc_lblUltimaej.insets = new Insets(0, 0, 5, 5);
		gbc_lblUltimaej.gridx = 1;
		gbc_lblUltimaej.gridy = 5;
		panelEstInterno.add(lblUltimaej, gbc_lblUltimaej);
		
		JLabel lblCalidadultima = new JLabel("CalidadUltima");
		GridBagConstraints gbc_lblCalidadultima = new GridBagConstraints();
		gbc_lblCalidadultima.anchor = GridBagConstraints.WEST;
		gbc_lblCalidadultima.insets = new Insets(0, 0, 5, 5);
		gbc_lblCalidadultima.gridx = 1;
		gbc_lblCalidadultima.gridy = 6;
		panelEstInterno.add(lblCalidadultima, gbc_lblCalidadultima);
		
		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.gridheight = 6;
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 3;
		gbc_panel_1.gridy = 8;
		panelEstInterno.add(panel_1, gbc_panel_1);
		
		JLabel lblEjecsatis = new JLabel("EjecSatis");
		GridBagConstraints gbc_lblEjecsatis = new GridBagConstraints();
		gbc_lblEjecsatis.anchor = GridBagConstraints.WEST;
		gbc_lblEjecsatis.insets = new Insets(0, 0, 5, 5);
		gbc_lblEjecsatis.gridx = 1;
		gbc_lblEjecsatis.gridy = 10;
		panelEstInterno.add(lblEjecsatis, gbc_lblEjecsatis);
		
		JLabel lblPctjesatis = new JLabel("PctjeSatis");
		GridBagConstraints gbc_lblPctjesatis = new GridBagConstraints();
		gbc_lblPctjesatis.anchor = GridBagConstraints.WEST;
		gbc_lblPctjesatis.insets = new Insets(0, 0, 5, 5);
		gbc_lblPctjesatis.gridx = 1;
		gbc_lblPctjesatis.gridy = 11;
		panelEstInterno.add(lblPctjesatis, gbc_lblPctjesatis);
		
		JLabel lblEjecinusables = new JLabel("EjecInusables");
		GridBagConstraints gbc_lblEjecinusables = new GridBagConstraints();
		gbc_lblEjecinusables.anchor = GridBagConstraints.WEST;
		gbc_lblEjecinusables.insets = new Insets(0, 0, 5, 5);
		gbc_lblEjecinusables.gridx = 1;
		gbc_lblEjecinusables.gridy = 12;
		panelEstInterno.add(lblEjecinusables, gbc_lblEjecinusables);
		
		JLabel lblPctjeinusables = new JLabel("PctjeInusables");
		GridBagConstraints gbc_lblPctjeinusables = new GridBagConstraints();
		gbc_lblPctjeinusables.anchor = GridBagConstraints.WEST;
		gbc_lblPctjeinusables.insets = new Insets(0, 0, 5, 5);
		gbc_lblPctjeinusables.gridx = 1;
		gbc_lblPctjeinusables.gridy = 13;
		panelEstInterno.add(lblPctjeinusables, gbc_lblPctjeinusables);
		escogerLista();
		establecerComportamientoLista();
	}
	
	/**
	 * Hace que se refresquen los gráficos y labels al actualizar un elemento de la lista 
	 */
	private void establecerComportamientoLista(){
		if(listaU !=null){
			listaU.addMouseListener(new MouseAdapter() {

				@SuppressWarnings("unchecked")
				public void mouseClicked(MouseEvent arg0) {
					JList<Usuario> listaU = (JList<Usuario>) arg0.getSource();
					Usuario escogido = listaU.getSelectedValue();
				}
			});
		}if(listaC!=null){
			listaC.addMouseListener(new MouseAdapter() {

				@SuppressWarnings("unchecked")
				public void mouseClicked(MouseEvent arg0) {
					JList<TipoCaso> listaC = (JList<TipoCaso>) arg0.getSource();
					TipoCaso caso = listaC.getSelectedValue();
				}
			});
		}
	}

	/**
	 * Escoge si va a haber o no lista de usuarios/casos, según el tipo de
	 * panel. Por ejemplo, panel de estadísticas propias o de estadísticas de
	 * cada caso.
	 */
	private void escogerLista() {
		try {
			if (tipo.equals(EnumEstadisticas.USUARIOYCASO)) {
				List<TipoCaso> casos = null;
				casos = ControlTipos.obtenerTiposCaso(u);
				listaC = new ListaCasos(casos);
				HashMap<String, Usuario> usuarios = null;
				usuarios = ControlUsuarios.getUsuarios();
				listaU = new ListaUsuarios(usuarios);
				panel_listas.add(listaU);
				panel_listas.add(listaC);
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, b.getString("connecterror"),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
