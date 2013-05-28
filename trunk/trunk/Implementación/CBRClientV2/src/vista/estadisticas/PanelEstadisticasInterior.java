package vista.estadisticas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import javax.swing.border.EtchedBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import servidorcbr.modelo.Estadistica;
import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.TipoUsuario;
import servidorcbr.modelo.Usuario;
import vista.panels.ListaCasos;
import vista.panels.ListaUsuarios;
import weka.gui.ExtensionFileFilter;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import controlador.ControlEstadisticas;
import controlador.ControlTipos;
import controlador.ControlUsuarios;

/**Panel que muestra las estadísticas, genérico para la opción de usuario y caso o para 
 * ver las estadísticas propias. Muestra datos y gráficas exportables de estadísticas.
 * También permite generar informes de las mismas en pdf, o borrar las estadísticas
 * registradas.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
@SuppressWarnings("serial")
public class PanelEstadisticasInterior extends JPanel {

	/**
	 * Lista de casos seleccionables para ver sus estadísticas.
	 */
	private ListaCasos listaC = null;
	/**
	 * Lista de usuarios seleccionables para ver sus estadísticas.
	 */
	private ListaUsuarios listaU = null;
	/**
	 * Tipo de estadísticas a visualizar.
	 */
	private EnumEstadisticas tipo;
	/**
	 * Estadística mostrada en cada momento.
	 */
	private Estadistica est;
	/**
	 * Usuario que pide ver las estadísticas.
	 */
	private Usuario u;
	/**
	 * Bundle de internacionalización.
	 */
	private ResourceBundle b = ResourceBundle.getBundle(
			"vista.internacionalizacion.Recursos", Locale.getDefault());
	/**
	 * Layout interno.
	 */
	private SpringLayout springLayout;
	/**
	 * Panel interno que contiene la representación visual de las estadísticas.
	 */
	private JPanel panelEstInterno;
	/**
	 * Panel que contiene las listas seleccionables de casos y usuarios.
	 */
	private final JPanel panel_listas;

	// Componentes de las estadisticas (labels y gráficos)

	/**
	 * Label de la estadística Ejecuciones totales.
	 */
	private JLabel lblEjecucionestotales;
	/**
	 * Label de la estadística Ejecuciones totales.
	 */
	private JLabel lblMediaCalidad;
	/**
	 * Label de la estadística Ejecuciones totales.
	 */
	private JLabel lblUltimaej;
	/**
	 * Label de la estadística Calidad de la última ejecución.
	 */
	private JLabel lblCalidadultima;
	/**
	 * Label de la estadística Ejecuciones satisfactorias.
	 */
	private JLabel lblEjecsatis;
	/**
	 * Label de la estadística Porcentaje de Ejecuciones satisfactorias.
	 */
	private JLabel lblPctjesatis;
	/**
	 * Label de la estadística Ejecuciones inusables.
	 */
	private JLabel lblEjecinusables;
	/**
	 * Label de la estadística Porcentaje de Ejecuciones inusables.
	 */
	private JLabel lblPctjeinusables;
	/**
	 * Panel que contiene un gráfico en cada momento.
	 */
	private JPanel panelInuExitoTotal;
	/**
	 * Tabbedpane para las pestañas de cada gráfico.
	 */
	private JTabbedPane tabbedPaneGraficos;
	/**
	 * Panel de media de calidad.
	 */
	private JPanel panelMediaCalidad;
	/**
	 * Barra de herramientas para guardar gráficos, borrar estadísticas, etc.
	 */
	private JPanel toolBarPanel;
	/**
	 * Botón de limpiar estadísticas.
	 */
	private JButton btnLimpiar;
	/**
	 * Botón de exportar gráfico como png.
	 */
	private JButton btnExportarImg;

	/**
	 * Anchura de los graficos al exportarlos.
	 */
	private static final int ANCHOCHART = 450;
	/**
	 * Altura de los graficos al exportarlos.
	 */
	private static final int ALTOCHART = 250;
	/**
	 * Panel con los labels de estadísticas (Informe).
	 */
	private JPanel panelInforme;
	/**
	 * Panel con el título del panel de estadísticas.
	 */
	private JPanel panelTitulo;
	/**
	 * Panel de la sección de gráficos.
	 */
	private JPanel panelGraf;

	
	/** Constructor del panel de estadísticas.
	 * @param tipo Tipo de estadísticas a mostrar: Propias o con selección usuario-caso.
	 * @param usuario Usuario que realiza la petición de ver estadísticas.
	 */
	public PanelEstadisticasInterior(EnumEstadisticas tipo, Usuario usuario) {
		this.u = usuario;
		this.tipo = tipo;
		springLayout = new SpringLayout();
		setLayout(springLayout);
		panel_listas = new JPanel();
		springLayout.putConstraint(SpringLayout.NORTH, panel_listas, 0, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, panel_listas, 0,
				SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, panel_listas, 0, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.EAST, panel_listas, 350,
				SpringLayout.WEST, this);
		add(panel_listas);

		panelEstInterno = new JPanel();
		springLayout.putConstraint(SpringLayout.NORTH, panelEstInterno, 0, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, panelEstInterno, 350,
				SpringLayout.WEST, this);
		add(panelEstInterno);
		panelEstInterno.setLayout(new BorderLayout(0, 0));
		
		//Panel de título

		panelTitulo = new JPanel();
		panelTitulo.setBackground(Color.GRAY);
		panelTitulo
				.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelEstInterno.add(panelTitulo, BorderLayout.NORTH);

		JLabel lblInforme = new JLabel(b.getString("report"));
		lblInforme.setForeground(Color.WHITE);
		panelTitulo.add(lblInforme);
		lblInforme.setFont(new Font("Tahoma", Font.BOLD, 13));

		toolBarPanel = new JPanel();
		springLayout.putConstraint(SpringLayout.SOUTH, panelEstInterno, -6, SpringLayout.NORTH, toolBarPanel);
		springLayout.putConstraint(SpringLayout.EAST, panelEstInterno, 0,
				SpringLayout.EAST, toolBarPanel);
		springLayout.putConstraint(SpringLayout.WEST, toolBarPanel, 0, SpringLayout.EAST, panel_listas);
		springLayout.putConstraint(SpringLayout.EAST, toolBarPanel, 0, SpringLayout.EAST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, toolBarPanel, 0, SpringLayout.SOUTH, this);

		panelGraf = new JPanel();
		panelEstInterno.add(panelGraf, BorderLayout.CENTER);
		panelGraf.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("max(41dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("362px"),},
			new RowSpec[] {
				RowSpec.decode("165px"),
				RowSpec.decode("236px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));

		JPanel panelRelleno = new JPanel();
		panelRelleno.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelGraf.add(panelRelleno, "3, 1, fill, fill");

		panelInforme = new JPanel();
		panelRelleno.add(panelInforme);
		panelInforme.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("251px"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(7dlu;default)"),},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("14px"),
				RowSpec.decode("14px"),
				RowSpec.decode("14px"),
				RowSpec.decode("14px"),
				RowSpec.decode("14px"),
				RowSpec.decode("14px"),
				RowSpec.decode("14px"),
				RowSpec.decode("14px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(8dlu;default)"),}));

		lblEjecucionestotales = new JLabel(b.getString("totalexec") + ":");
		panelInforme.add(lblEjecucionestotales, "3, 3, left, center");

		lblMediaCalidad = new JLabel(b.getString("avgquality") + ":");
		panelInforme.add(lblMediaCalidad, "3, 4, left, center");

		lblUltimaej = new JLabel(b.getString("lastexec") + ":");
		panelInforme.add(lblUltimaej, "3, 5, left, center");

		lblCalidadultima = new JLabel(b.getString("latestquality") + ":");
		panelInforme.add(lblCalidadultima, "3, 6, left, center");

		lblEjecsatis = new JLabel(b.getString("successexec") + ":");
		panelInforme.add(lblEjecsatis, "3, 7, left, center");

		lblPctjesatis = new JLabel(b.getString("pctgesuccess") + ":");
		panelInforme.add(lblPctjesatis, "3, 8, left, center");

		lblEjecinusables = new JLabel(b.getString("invalidexec") + ":");
		panelInforme.add(lblEjecinusables, "3, 9, left, center");

		lblPctjeinusables = new JLabel(b.getString("pctgeinvalid") + ":");
		panelInforme.add(lblPctjeinusables, "3, 10, left, center");

		tabbedPaneGraficos = new JTabbedPane(JTabbedPane.TOP);
		panelGraf.add(tabbedPaneGraficos, "3, 2, fill, fill");

		panelInuExitoTotal = new JPanel();
		tabbedPaneGraficos.addTab(b.getString("qualitychart"), null,
				panelInuExitoTotal, null);
		panelInuExitoTotal.setLayout(new BorderLayout(0, 0));

		panelMediaCalidad = new JPanel();
		panelMediaCalidad.setLayout(new BorderLayout(0, 0));
		tabbedPaneGraficos.addTab(b.getString("avgvslatest"), null,
				panelMediaCalidad, null);
		
		add(toolBarPanel);
		toolBarPanel.setBackground(Color.GRAY);
		toolBarPanel.setBorder(BorderFactory.createEtchedBorder());

		btnExportarImg = new JButton(new ImageIcon("res/clipboard_32.png"));
		btnExportarImg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jfc = new JFileChooser();
				// Permito guardar en png
				jfc.setFileFilter(new ExtensionFileFilter("PNG", "png"));
				if (jfc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					try {
						// Obtengo el chart
						JFreeChart chart = ((ChartPanel) ((JPanel) tabbedPaneGraficos
								.getSelectedComponent()).getComponent(0))
								.getChart();
						// Me aseguro de que el fichero se guarda como png,
						// aunque el usuario no lo indique
						File f = new File(jfc.getSelectedFile()
								.getAbsolutePath() + ".png");
						ChartUtilities.saveChartAsPNG(f, chart, ANCHOCHART,
								ALTOCHART);
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null,
								b.getString("operror"), "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		
		JButton btnInforme = new JButton(new ImageIcon("res/exportar_32.png"));
		btnInforme.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				generarInforme();
			}
		});
		btnInforme.setToolTipText(b.getString("export"));
		btnExportarImg.setToolTipText(b.getString("export")+ " .png");
		
		toolBarPanel.add(btnInforme);
		toolBarPanel.add(btnExportarImg);
		

		if (tipo.equals(EnumEstadisticas.USUARIOYCASO)) {

			btnLimpiar = new JButton(new ImageIcon("res/trash_32.png"));
			btnLimpiar.setToolTipText(b.getString("delete"));
			btnLimpiar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					Usuario escogido = listaU.getSelectedValue();
					// Las cadenas user y case representan lo que se muestra al
					// usuario para confirmar.
					String user = "", tcase = "";
					if (escogido == null) {
						// Compruebo el usuario escogido
						escogido = u;
					}
					user = escogido.getNombre();
					int index = listaU.getSelectedIndex();
					if (index == 0) {
						escogido = new Usuario();
						escogido.setNombre("ver todos");
						escogido.setTipo(TipoUsuario.ADMINISTRADOR);
					}
					// Si el caso no ha sido escogido o se han escogido 'todos'
					TipoCaso caso = listaC.getSelectedValue();
					if (caso != null) {
						tcase = caso.getNombre();
					}
					if (listaC.getSelectedIndex() <= 0) {
						caso = new TipoCaso();
						caso.setNombre("ver todos");
						tcase = b.getString("seeall");
					}
					// Confirmo la operación
					int confirmar = JOptionPane.showConfirmDialog(
							null,
							b.getString("wishtodelete") + user
									+ b.getString("andcase") + tcase + "?",
							"Info", JOptionPane.INFORMATION_MESSAGE);
					if (confirmar == JOptionPane.YES_OPTION) {
						try {
							ControlEstadisticas.limpiarEstadistica(escogido,
									caso);
							refrescarEstadisticas(new Estadistica());
						} catch (IOException e) {
							JOptionPane.showMessageDialog(null,
									b.getString("operror"), "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			});
			toolBarPanel.add(btnLimpiar);

		}

		escogerLista();
		establecerComportamientoLista();

		TipoCaso vertodostc = new TipoCaso();
		vertodostc.setNombre("ver todos");
		try {
			est = ControlEstadisticas.getEstadistica(u, vertodostc);
		} catch (IOException e) {
		}
		refrescarEstadisticas(est);
		refrescarGraficos(est);

	}

	/**
	 * Genera el informe de estadísticas
	 */
	private void generarInforme() {
		JFileChooser jfc = new JFileChooser();
		//Permito guardar en pdf
		jfc.setFileFilter(new ExtensionFileFilter("PDF","pdf"));
		if(jfc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
			//Obtengo la pestaña de estadisticas que tiene el foco actualmente
			//Obtengo el texto y la lista de graficos del panel que tiene el foco.
			if(!ControlEstadisticas.escribirEstadisticasPDF(jfc.getSelectedFile().getAbsolutePath()+".pdf",
					b.getString("report"),getTexto(), getGraficos())){
				//Si la operacion falla lo indico al exportar
				JOptionPane.showMessageDialog(null, 
						b.getString("exporterror"),"Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		
	}

	/**
	 * Hace que se refresquen los gráficos y labels al actualizar un elemento de
	 * la lista
	 */
	private void establecerComportamientoLista() {
		if (listaU != null) {
			listaU.addMouseListener(new MouseAdapter() {

				@SuppressWarnings("unchecked")
				public void mouseClicked(MouseEvent arg0) {
					JList<Usuario> listaU = (JList<Usuario>) arg0.getSource();
					// Compruebo que usuario se ha escogido
					Usuario escogido = listaU.getSelectedValue();
					int index = listaU.getSelectedIndex();
					if (escogido == null) {
						escogido = u;
					}
					// Si en lugar de un usuario, se escogio "Ver todos", mando
					// un usuario especial de nombre
					// ver todos. Lo mismo se aplica al caso.
					// Notese que un usuario creado por programa nunca puede
					// tener un nombre con espacios.
					if (index == 0) {
						escogido = new Usuario();
						escogido.setNombre("ver todos");
						escogido.setTipo(TipoUsuario.ADMINISTRADOR);
					}
					TipoCaso caso = new TipoCaso();
					caso.setNombre("ver todos");

					try {
						// Obtengo los casos del usuario escogido (Pueden ser
						// los casos de todos)
						List<TipoCaso> casos = ControlTipos
								.obtenerTiposCaso(escogido);
						if (casos == null) {
							casos = new LinkedList<TipoCaso>();
						}
						TipoCaso verTodosC = new TipoCaso();
						verTodosC.setNombre(b.getString("seeall"));
						casos.add(0, verTodosC);

						listaC.refrescarDatos(casos);
						listaC.setSelectedIndex(0);
						est = ControlEstadisticas
								.getEstadistica(escogido, caso);
						refrescarEstadisticas(est);
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null,
								b.getString("connecterror"), "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			});
		}
		if (listaC != null) {
			listaC.addMouseListener(new MouseAdapter() {

				@SuppressWarnings({ "unchecked", "unused" }) //controlado.
				public void mouseClicked(MouseEvent arg0) {
					JList<TipoCaso> listaC = (JList<TipoCaso>) arg0.getSource();
					TipoCaso caso = listaC.getSelectedValue();
					int index = listaC.getSelectedIndex();
					if (listaC.getSelectedIndex() == 0) {
						caso = new TipoCaso();
						caso.setNombre("ver todos");
					}
					Usuario escogido;
					if (listaU == null || listaU.getSelectedValue() == null) {
						escogido = u;
					} else {
						escogido = listaU.getSelectedValue();
						if (listaU.getSelectedIndex() == 0) {
							escogido = new Usuario();
							escogido.setNombre("ver todos");
							escogido.setTipo(TipoUsuario.ADMINISTRADOR);
						}
					}
					try {
						est = ControlEstadisticas
								.getEstadistica(escogido, caso);
						refrescarEstadisticas(est);
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null,
								b.getString("connecterror"), "Error",
								JOptionPane.ERROR_MESSAGE);
					}
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
				HashMap<String, Usuario> usuarios = null;
				usuarios = ControlUsuarios.getUsuarios();
				if (usuarios == null) {
					usuarios = new HashMap<String, Usuario>();
				}
				Usuario verTodos = new Usuario();
				verTodos.setTipo(TipoUsuario.ADMINISTRADOR);
				verTodos.setNombre(b.getString("seeall"));
				listaU = new ListaUsuarios(usuarios);
				// Introduzco a "ver todos" en la posición 0
				listaU.addUsuario(verTodos, 0);
				JScrollPane scrollpaneU = new JScrollPane();
				scrollpaneU.setPreferredSize(new Dimension(200,
						getPreferredSize().height));
				scrollpaneU
						.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				scrollpaneU.setViewportView(listaU);
				panel_listas.add(scrollpaneU);
			}

			List<TipoCaso> casos = null;
			casos = ControlTipos.obtenerTiposCaso(u);
			TipoCaso verTodosC = new TipoCaso();
			if (casos == null) {
				casos = new LinkedList<>();
			}
			verTodosC.setNombre(b.getString("seeall"));
			casos.add(0, verTodosC);
			panel_listas
					.setLayout(new BoxLayout(panel_listas, BoxLayout.X_AXIS));
			listaC = new ListaCasos(casos);
		
			JScrollPane scrollpaneC = new JScrollPane();
			scrollpaneC.setPreferredSize(new Dimension(200,
					getPreferredSize().height));
			scrollpaneC
					.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scrollpaneC.setViewportView(listaC);
			panel_listas.add(scrollpaneC);

		} catch (IOException e) {
		}
	}

	/**
	 * Permite refrescar las etiquetas con los valores de las estadisticas.
	 * @param est Nuevos valores para las estadisticas.
	 *            
	 */
	private void refrescarEstadisticas(Estadistica est) {
		lblEjecucionestotales.setText(b.getString("totalexec") + ":"
				+ est.getEjecTotales());
		lblCalidadultima.setText(b.getString("latestquality") + ":"
				+ est.getCalidadUltima());
		lblEjecinusables.setText(b.getString("invalidexec") + ":"
				+ est.getEjecInusables());
		lblEjecsatis.setText(b.getString("successexec") + ":"
				+ est.getEjecSatisfactorias());
		// Uso String.format para establecer la precision en los label
		lblMediaCalidad.setText(b.getString("avgquality") + ":"
				+ String.format("%.2f", est.getMediaCalidad()));

		if (est.getFechaUltima() != null) {
			lblUltimaej.setText(b.getString("lastexec") + ":"
					+ est.getFechaUltima());
		} else {
			lblUltimaej.setText(b.getString("lastexec") + ":");
		}
		if (est.getEjecTotales() != 0) {
			lblPctjeinusables.setText(b.getString("pctgeinvalid")
					+ ":"
					+ String.format("%.2f",
							((double) est.getEjecInusables() / (double) est
									.getEjecTotales()) * 100) + "%");
			lblPctjesatis
					.setText(b.getString("pctgesuccess")
							+ ":"
							+ String.format("%.2f", ((double) est
									.getEjecSatisfactorias() / (double) est
									.getEjecTotales()) * 100) + "%");
		} else {
			lblPctjeinusables.setText(b.getString("pctgeinvalid") + ":");
			lblPctjesatis.setText(b.getString("pctgesuccess") + ":");
		}

		refrescarGraficos(est);
	}

	/**
	 * Refresca los gráficos cuando se actualizan las estadísticas al elegir una
	 * nueva. Invocado desde refrescarEstadisticas
	 * 
	 * @param est Estadisticas actualizadas
	 *            
	 */
	private void refrescarGraficos(Estadistica est) {
		// Grafico de tipo circular
		refrescarGraficoCircular(est);
		// Gráfico de barras
		refrescarGraficoBarras(est);

	}

	/**
	 * Refresca el gráfico de barras cuando cambian las estadísticas.
	 * @param est Estadisticas actualizadas
	 *            
	 */
	private void refrescarGraficoBarras(Estadistica est) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.setValue(est.getCalidadUltima(), b.getString("latestquality"),
				"");
		dataset.setValue(est.getMediaCalidad(), b.getString("avgquality"), "");

		chart = ChartFactory.createBarChart3D("", "", "", dataset,
				PlotOrientation.HORIZONTAL, true, false, false);
		chart.getTitle().setPaint(Color.blue);
		chart.getCategoryPlot().getRangeAxis().setRange(0, 100);

		panelMediaCalidad.removeAll();
		panelMediaCalidad.add(new ChartPanel(chart), BorderLayout.CENTER);

	
		panelMediaCalidad.revalidate();
		panelMediaCalidad.repaint();
	}

	private JFreeChart chart4;
	private JFreeChart chart;

	/**
	 * Refresca el gráfico circular cuando cambian las estadísticas.
	 * @param est Estadisticas actualizadas  
	 */
	private void refrescarGraficoCircular(Estadistica est) {

		// Creo el dataset
		DefaultPieDataset dataset = new DefaultPieDataset();
		dataset.setValue(b.getString("invshort"), est.getEjecInusables());
		dataset.setValue(b.getString("sucshort"), est.getEjecSatisfactorias());
		// Otras ejecuciones = ejec. totales - satisfactorias - inusables
		long otras = est.getEjecTotales() - est.getEjecInusables()
				- est.getEjecSatisfactorias();
		/*
		 * En caso de que haya habido un error al introducir estadisticas (Por
		 * ejemplo, un fallo en la conexión), me aseguro de que el gráfico no
		 * obtenga valores negativos con Max
		 */
		dataset.setValue(b.getString("other"), Math.max(otras, 0));
		chart4 = ChartFactory
				.createPieChart3D("", dataset, false, false, false);
		PiePlot3D plot4 = (PiePlot3D) chart4.getPlot();
		chart4.getTitle().setPaint(Color.blue);
		plot4.setForegroundAlpha(0.6f);
		panelInuExitoTotal.removeAll();
		panelInuExitoTotal.add(new ChartPanel(chart4), BorderLayout.CENTER);
		panelInuExitoTotal.revalidate();
		panelInuExitoTotal.repaint();

	}

	/**
	 * Obtiene el texto de las estadisticas que se estan mostrando.
	 * @return Lista de cadenas de texto con las estadisticas mostradas.
	 */
	public List<String> getTexto() {
		List<String> texto = new ArrayList<String>();
		if(listaC!=null && listaC.getSelectedValue()!=null){
			texto.add(listaC.getSelectedValue().getNombre());
		}
		if(listaU == null || listaU.getSelectedValue()==null){
			texto.add(u.getNombre());
		}else{
			texto.add(listaU.getSelectedValue().getNombre());
		}
		texto.add("---------");
		texto.add(lblEjecucionestotales.getText());
		texto.add(lblMediaCalidad.getText());
		texto.add(lblUltimaej.getText());
		texto.add(lblCalidadultima.getText());
		texto.add(lblEjecsatis.getText());
		texto.add(lblPctjesatis.getText());
		texto.add(lblEjecinusables.getText());
		texto.add(lblPctjeinusables.getText());
		return texto;
	}

	/**
	 * Obtiene los graficos correspondientes a las estadisticas mostradas.
	 * @return Lista de imagenes de los graficos mostrados.
	 */
	public List<Image> getGraficos() {
		List<Image> graficos = new ArrayList<Image>();
		// Por cada pestaña en mi tabbedpane de graficos
		for (int i = 0; i < tabbedPaneGraficos.getComponentCount(); i++) {
			// Obtengo el chart
			JFreeChart chart = ((ChartPanel) ((JPanel) tabbedPaneGraficos
					.getComponent(i)).getComponent(0)).getChart();
			graficos.add(chart.createBufferedImage(ANCHOCHART, ALTOCHART));
		}
		return graficos;
	}
}
