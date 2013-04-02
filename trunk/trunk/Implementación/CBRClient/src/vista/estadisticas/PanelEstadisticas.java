package vista.estadisticas;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JList;

import controlador.ControlEstadisticas;
import controlador.ControlTipos;
import controlador.ControlUsuarios;

import servidorcbr.modelo.Estadistica;
import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.TipoUsuario;
import servidorcbr.modelo.Usuario;
import vista.InsertarCasoFrame;
import vista.panels.ListaCasos;
import vista.panels.ListaUsuarios;
import weka.classifiers.lazy.LBR;
import weka.gui.ExtensionFileFilter;

import javax.swing.SpringLayout;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BoxLayout;
import net.miginfocom.swing.MigLayout;
import javax.swing.ScrollPaneConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PanelEstadisticas extends JPanel {

	private ListaCasos listaC = null;
	private ListaUsuarios listaU = null;
	private EnumEstadisticas tipo;
	private Estadistica est;
	private Usuario u;
	private ResourceBundle b = ResourceBundle.getBundle(
			"vista.internacionalizacion.Recursos", Locale.getDefault());
	private SpringLayout springLayout;
	private JPanel panelEstInterno;
	private final JPanel panel_listas;
	
	//Componentes de las estadisticas (labels y gráficos)
	
	private JLabel lblEjecucionestotales;
	private JLabel lblMediaCalidad;
	private JLabel lblUltimaej;
	private JLabel lblCalidadultima;
	private JLabel lblEjecsatis;
	private JLabel lblPctjesatis;
	private JLabel lblEjecinusables;
	private JLabel lblPctjeinusables;
	private JPanel panelInuExitoTotal;
	private JTabbedPane tabbedPaneGraficos;
	private JPanel panelMediaCalidad;
	private JToolBar toolBar;
	private JButton btnLimpiar;
	private JButton btnExportar;
	
	
	/**
	 * Anchura de los graficos al exportarlos.
	 */
	private static final int ANCHOCHART=450;
	/**
	 * Altura de los graficos al exportarlos.
	 */
	private static final int ALTOCHART=250;
	
	/**
	 * Crea el panel.
	 */
	public PanelEstadisticas(EnumEstadisticas tipo, Usuario usuario) {
		this.u = usuario;
		this.tipo = tipo;
		springLayout = new SpringLayout();
		setLayout(springLayout);
		panel_listas=new JPanel();
		springLayout.putConstraint(SpringLayout.NORTH, panel_listas, 0, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, panel_listas, 0, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, panel_listas, 379, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.EAST, panel_listas, 350, SpringLayout.WEST, this);
		add(panel_listas);
		
		panelEstInterno = new JPanel();
		springLayout.putConstraint(SpringLayout.NORTH, panelEstInterno, 0, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, panelEstInterno, 350, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, panelEstInterno, 379, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.EAST, panelEstInterno, -10, SpringLayout.EAST, this);
		add(panelEstInterno);
						GridBagLayout gbl_panelEstInterno = new GridBagLayout();
						gbl_panelEstInterno.columnWidths = new int[]{30, 105, 42, 45, 257, 0, 79, 0, 0};
						gbl_panelEstInterno.rowHeights = new int[]{88, 16, 14, 47, 0, 14, 14, 14, 14, 14, 14, 0, 0, 123, 0, 0};
						gbl_panelEstInterno.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
						gbl_panelEstInterno.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
						panelEstInterno.setLayout(gbl_panelEstInterno);
						
						JLabel lblInforme = new JLabel(b.getString("report"));
						lblInforme.setFont(new Font("Tahoma", Font.BOLD, 13));
						GridBagConstraints gbc_lblInforme = new GridBagConstraints();
						gbc_lblInforme.anchor = GridBagConstraints.NORTHWEST;
						gbc_lblInforme.insets = new Insets(0, 0, 5, 5);
						gbc_lblInforme.gridx = 1;
						gbc_lblInforme.gridy = 2;
						panelEstInterno.add(lblInforme, gbc_lblInforme);
								
								tabbedPaneGraficos = new JTabbedPane(JTabbedPane.TOP);
								GridBagConstraints gbc_tabbedPaneGraficos = new GridBagConstraints();
								gbc_tabbedPaneGraficos.gridheight = 11;
								gbc_tabbedPaneGraficos.gridwidth = 3;
								gbc_tabbedPaneGraficos.insets = new Insets(0, 0, 5, 5);
								gbc_tabbedPaneGraficos.fill = GridBagConstraints.BOTH;
								gbc_tabbedPaneGraficos.gridx = 4;
								gbc_tabbedPaneGraficos.gridy = 1;
								panelEstInterno.add(tabbedPaneGraficos, gbc_tabbedPaneGraficos);
								
								panelInuExitoTotal = new JPanel();
								tabbedPaneGraficos.addTab(b.getString("qualitychart"), null, panelInuExitoTotal, null);
								panelInuExitoTotal.setLayout(new BorderLayout(0, 0));
								
								panelMediaCalidad = new JPanel();
								panelMediaCalidad.setLayout(new BorderLayout(0, 0));
								tabbedPaneGraficos.addTab(b.getString("avgvslatest"), null, panelMediaCalidad, null);
						
								lblEjecucionestotales = new JLabel(b.getString("totalexec")+":");
								GridBagConstraints gbc_lblEjecucionestotales = new GridBagConstraints();
								gbc_lblEjecucionestotales.gridwidth = 2;
								gbc_lblEjecucionestotales.anchor = GridBagConstraints.NORTHWEST;
								gbc_lblEjecucionestotales.insets = new Insets(0, 0, 5, 5);
								gbc_lblEjecucionestotales.gridx = 1;
								gbc_lblEjecucionestotales.gridy = 4;
								panelEstInterno.add(lblEjecucionestotales, gbc_lblEjecucionestotales);
						
						lblMediaCalidad = new JLabel(b.getString("avgquality")+":");
						GridBagConstraints gbc_lblMediaCalidad = new GridBagConstraints();
						gbc_lblMediaCalidad.gridwidth = 2;
						gbc_lblMediaCalidad.anchor = GridBagConstraints.NORTHWEST;
						gbc_lblMediaCalidad.insets = new Insets(0, 0, 5, 5);
						gbc_lblMediaCalidad.gridx = 1;
						gbc_lblMediaCalidad.gridy = 5;
						panelEstInterno.add(lblMediaCalidad, gbc_lblMediaCalidad);
								
								lblUltimaej = new JLabel(b.getString("lastexec")+":");
								GridBagConstraints gbc_lblUltimaej = new GridBagConstraints();
								gbc_lblUltimaej.gridwidth = 2;
								gbc_lblUltimaej.anchor = GridBagConstraints.NORTHWEST;
								gbc_lblUltimaej.insets = new Insets(0, 0, 5, 5);
								gbc_lblUltimaej.gridx = 1;
								gbc_lblUltimaej.gridy = 6;
								panelEstInterno.add(lblUltimaej, gbc_lblUltimaej);
								
								lblCalidadultima = new JLabel(b.getString("latestquality")+":");
								GridBagConstraints gbc_lblCalidadultima = new GridBagConstraints();
								gbc_lblCalidadultima.gridwidth = 2;
								gbc_lblCalidadultima.anchor = GridBagConstraints.NORTHWEST;
								gbc_lblCalidadultima.insets = new Insets(0, 0, 5, 5);
								gbc_lblCalidadultima.gridx = 1;
								gbc_lblCalidadultima.gridy = 7;
								panelEstInterno.add(lblCalidadultima, gbc_lblCalidadultima);
						
								lblEjecsatis = new JLabel(b.getString("successexec")+":");
								GridBagConstraints gbc_lblEjecsatis = new GridBagConstraints();
								gbc_lblEjecsatis.gridwidth = 2;
								gbc_lblEjecsatis.anchor = GridBagConstraints.NORTHWEST;
								gbc_lblEjecsatis.insets = new Insets(0, 0, 5, 5);
								gbc_lblEjecsatis.gridx = 1;
								gbc_lblEjecsatis.gridy = 8;
								panelEstInterno.add(lblEjecsatis, gbc_lblEjecsatis);
						
						lblPctjesatis = new JLabel(b.getString("pctgesuccess")+":");
						GridBagConstraints gbc_lblPctjesatis = new GridBagConstraints();
						gbc_lblPctjesatis.gridwidth = 2;
						gbc_lblPctjesatis.anchor = GridBagConstraints.NORTHWEST;
						gbc_lblPctjesatis.insets = new Insets(0, 0, 5, 5);
						gbc_lblPctjesatis.gridx = 1;
						gbc_lblPctjesatis.gridy = 9;
						panelEstInterno.add(lblPctjesatis, gbc_lblPctjesatis);
						
						lblEjecinusables = new JLabel(b.getString("invalidexec")+":");
						GridBagConstraints gbc_lblEjecinusables = new GridBagConstraints();
						gbc_lblEjecinusables.gridwidth = 2;
						gbc_lblEjecinusables.anchor = GridBagConstraints.NORTHWEST;
						gbc_lblEjecinusables.insets = new Insets(0, 0, 5, 5);
						gbc_lblEjecinusables.gridx = 1;
						gbc_lblEjecinusables.gridy = 10;
						panelEstInterno.add(lblEjecinusables, gbc_lblEjecinusables);
				
				lblPctjeinusables = new JLabel(b.getString("pctgeinvalid")+":");
				GridBagConstraints gbc_lblPctjeinusables = new GridBagConstraints();
				gbc_lblPctjeinusables.gridwidth = 2;
				gbc_lblPctjeinusables.anchor = GridBagConstraints.NORTHWEST;
				gbc_lblPctjeinusables.insets = new Insets(0, 0, 5, 5);
				gbc_lblPctjeinusables.gridx = 1;
				gbc_lblPctjeinusables.gridy = 11;
				panelEstInterno.add(lblPctjeinusables, gbc_lblPctjeinusables);
				
				if(tipo.equals(EnumEstadisticas.USUARIOYCASO)){
					toolBar = new JToolBar();
					toolBar.setBorder(BorderFactory.createEtchedBorder() );
					toolBar.setFloatable(false);
					GridBagConstraints gbc_toolBar = new GridBagConstraints();
					gbc_toolBar.gridwidth = 3;
					gbc_toolBar.insets = new Insets(0, 0, 5, 5);
					gbc_toolBar.gridx = 4;
					gbc_toolBar.gridy = 12;
					panelEstInterno.add(toolBar, gbc_toolBar);

					btnLimpiar = new JButton(new ImageIcon("res/trash_32.png"));
					btnLimpiar.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							Usuario escogido = listaU.getSelectedValue();
							//Las cadenas user y case representan lo que se muestra al usuario para confirmar.
							String user="",tcase="";
							if(escogido==null){
								//Compruebo el usuario escogido
								escogido=u;
							}
							user=escogido.getNombre();
							int index = listaU.getSelectedIndex();
							if(index==0){
								escogido = new Usuario(); escogido.setNombre("ver todos");
								escogido.setTipo(TipoUsuario.ADMINISTRADOR);
							}
							//Si el caso no ha sido escogido o se han escogido 'todos'
							TipoCaso caso = listaC.getSelectedValue();
							if(caso!=null){
								tcase=caso.getNombre();
							}
							if(listaC.getSelectedIndex()<=0){
								caso = new TipoCaso(); caso.setNombre("ver todos");
								tcase=b.getString("seeall");
							}
							//Confirmo la operación
							int confirmar= JOptionPane.showConfirmDialog(null,
									b.getString("wishtodelete")+user+b.getString("andcase")+tcase+"?", "Info",
									JOptionPane.INFORMATION_MESSAGE);
							if(confirmar==JOptionPane.YES_OPTION){
								try {
									ControlEstadisticas.limpiarEstadistica(escogido, caso);
									refrescarEstadisticas(new Estadistica());
								} catch (IOException e) {
									JOptionPane.showMessageDialog(null,
											b.getString("operror"), "Error",
											JOptionPane.ERROR_MESSAGE);
								}
							}
						}
					});
					toolBar.add(btnLimpiar);
					
					btnExportar = new JButton(new ImageIcon("res/clipboard_32.png"));
					btnExportar.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							JFileChooser jfc = new JFileChooser();
							//Permito guardar en png
							jfc.setFileFilter(new ExtensionFileFilter("PNG","png"));
							if(jfc.showSaveDialog(null)== JFileChooser.APPROVE_OPTION){
								try {
									//Obtengo el chart
									JFreeChart chart =((ChartPanel)((JPanel)tabbedPaneGraficos.
											getSelectedComponent()).getComponent(0)).getChart();
									//Me aseguro de que el fichero se guarda como png, aunque el usuario no lo indique
									File f = new File(jfc.getSelectedFile().getAbsolutePath()+".png");
									ChartUtilities.saveChartAsPNG(f,chart,ANCHOCHART, ALTOCHART);
								} catch (IOException e) {
									JOptionPane.showMessageDialog(null,
											b.getString("operror"), "Error",
											JOptionPane.ERROR_MESSAGE);
								}
							}
						}
					});
					toolBar.add(btnExportar);
				}
				
		escogerLista();
		establecerComportamientoLista();
	
		TipoCaso vertodostc=new TipoCaso(); vertodostc.setNombre("ver todos");
		try {
			est = ControlEstadisticas.getEstadistica(u,vertodostc);
		} catch (IOException e) {}
		refrescarEstadisticas(est);
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
					//Compruebo que usuario se ha escogido
					Usuario escogido = listaU.getSelectedValue(); int index = listaU.getSelectedIndex();
					if(escogido==null){
						escogido=u;
					}
					//Si en lugar de un usuario, se escogio "Ver todos", mando un usuario especial de nombre
					// ver todos. Lo mismo se aplica al caso.
					//Notese que un usuario creado por programa nunca puede tener un nombre con espacios.
					if(index==0){
						escogido = new Usuario(); escogido.setNombre("ver todos");
						escogido.setTipo(TipoUsuario.ADMINISTRADOR);
					}
					 TipoCaso caso = new TipoCaso(); caso.setNombre("ver todos");
					 
					try {
						//Obtengo los casos del usuario escogido (Pueden ser los casos de todos)
						List<TipoCaso> casos = ControlTipos.obtenerTiposCaso(escogido);
						if(casos==null){
							casos = new LinkedList<TipoCaso>();
						}
						TipoCaso verTodosC = new TipoCaso();
						verTodosC.setNombre(b.getString("seeall"));
						casos.add(0,verTodosC);
						
						listaC.refrescarDatos(casos);
						listaC.setSelectedIndex(0);
						est = ControlEstadisticas.getEstadistica(escogido,caso);
						refrescarEstadisticas(est);
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null,
								b.getString("connecterror"), "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			});
		}if(listaC!=null){
			listaC.addMouseListener(new MouseAdapter() {

				@SuppressWarnings("unchecked")
				public void mouseClicked(MouseEvent arg0) {
					JList<TipoCaso> listaC = (JList<TipoCaso>) arg0.getSource();
					TipoCaso caso = listaC.getSelectedValue();
					int index=listaC.getSelectedIndex();
					if(listaC.getSelectedIndex()==0){
						caso = new TipoCaso(); caso.setNombre("ver todos");
					}
					Usuario escogido;
					if(listaU==null || listaU.getSelectedValue()==null){
						escogido=u;
					}else{
						escogido=listaU.getSelectedValue();
						if(listaU.getSelectedIndex()==0){
							escogido= new Usuario();
							escogido.setNombre("ver todos");
							escogido.setTipo(TipoUsuario.ADMINISTRADOR);
						}
					}
					try{
						est = ControlEstadisticas.getEstadistica(escogido, caso);
						refrescarEstadisticas(est);
					}catch(IOException e){
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
				if(usuarios == null){
					usuarios =new HashMap<String,Usuario>();
				}
				Usuario verTodos= new Usuario();
				verTodos.setTipo(TipoUsuario.ADMINISTRADOR);
				verTodos.setNombre(b.getString("seeall"));
				listaU = new ListaUsuarios(usuarios);
				//Introduzco a "ver todos" en la posición 0
				listaU.addUsuario(verTodos, 0);
				JScrollPane scrollpaneU = new  JScrollPane();
				scrollpaneU.setPreferredSize(new Dimension(200, getPreferredSize().height));
				scrollpaneU.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				scrollpaneU.setViewportView(listaU);
				panel_listas.add(scrollpaneU);	
			}

			List<TipoCaso> casos = null;
			casos = ControlTipos.obtenerTiposCaso(u);
			TipoCaso verTodosC = new TipoCaso();
			if(casos ==null){
				casos = new LinkedList<>();
			}
			verTodosC.setNombre(b.getString("seeall"));
			casos.add(0,verTodosC);
			panel_listas.setLayout(new BoxLayout(panel_listas, BoxLayout.X_AXIS));
			listaC = new ListaCasos(casos);
			JScrollPane scrollpaneC = new  JScrollPane();
			scrollpaneC.setPreferredSize(new Dimension(200, getPreferredSize().height));
			scrollpaneC.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scrollpaneC.setViewportView(listaC);
			panel_listas.add(scrollpaneC);

		} catch (IOException e) {	}
	}
	
	/**Permite refrescar las etiquetas con los valores de las estadisticas.
	 * @param est Nuevos valores para las estadisticas.
	 */
	private void refrescarEstadisticas(Estadistica est){
		lblEjecucionestotales.setText(b.getString("totalexec")+":"+est.getEjecTotales());
		lblCalidadultima.setText(b.getString("latestquality")+":"+est.getCalidadUltima());
		lblEjecinusables.setText(b.getString("invalidexec")+":"+est.getEjecInusables());
		lblEjecsatis.setText(b.getString("successexec")+":"+est.getEjecSatisfactorias());
		//Uso String.format para establecer la precision en los label
		lblMediaCalidad.setText(b.getString("avgquality")+":"+String.format("%.2f", est.getMediaCalidad()));
		
		if(est.getFechaUltima()!=null){
			lblUltimaej.setText(b.getString("lastexec")+":"+est.getFechaUltima());
		}else{
			lblUltimaej.setText(b.getString("lastexec")+":");
		}
		if(est.getEjecTotales()!=0){
			lblPctjeinusables.setText(b.getString("pctgeinvalid")+
					":"+ String.format("%.2f",((double)est.getEjecInusables()/(double)est.getEjecTotales())*100) +"%");
			lblPctjesatis.setText(b.getString("pctgesuccess")+":"+
					String.format("%.2f",((double)est.getEjecSatisfactorias()/(double)est.getEjecTotales())*100) +"%");
		}else{
			lblPctjeinusables.setText(b.getString("pctgeinvalid")+":");
			lblPctjesatis.setText(b.getString("pctgesuccess")+":");
		}
		
		refrescarGraficos(est);
	}
	
	/**Refresca los gráficos cuando se actualizan las estadísticas al elegir una nueva.
	 * Invocado desde refrescarEstadisticas.ç
	 * @param est Estadisticas actualizadas
	 */
	private void refrescarGraficos(Estadistica est){
		//Grafico de tipo circular
		refrescarGraficoCircular(est);
        //Gráfico de barras
       refrescarGraficoBarras(est);
        
	}
	
	/**Refresca el gráfico de barras cuando cambian las estadísticas.
	 * @param est Estadisticas actualizadas
	 */
	private void refrescarGraficoBarras(Estadistica est){
		 DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		 dataset.setValue(est.getCalidadUltima(), b.getString("latestquality"),"");
		 dataset.setValue(est.getMediaCalidad(), b.getString("avgquality"),"");

		  JFreeChart chart = ChartFactory.createBarChart3D("", 
				  "", "", dataset, PlotOrientation.HORIZONTAL, true, false, false);
		  chart.getTitle().setPaint(Color.blue);
		chart.getCategoryPlot().getRangeAxis().setRange(0,100);
		
		  panelMediaCalidad.removeAll();
		  panelMediaCalidad.add(new ChartPanel(chart),BorderLayout.CENTER);
		  panelMediaCalidad.revalidate();
	      panelMediaCalidad.repaint();
	}
	
	/**Refresca el gráfico circular cuando cambian las estadísticas.
	 * @param est Estadisticas actualizadas
	 */
	private void refrescarGraficoCircular(Estadistica est){
		
		//Creo el dataset
		DefaultPieDataset dataset = new DefaultPieDataset();
		dataset.setValue(b.getString("invshort"),est.getEjecInusables());
		dataset.setValue(b.getString("sucshort"), est.getEjecSatisfactorias());
		//Otras ejecuciones = ejec. totales - satisfactorias - inusables
		long otras = est.getEjecTotales()-est.getEjecInusables()-est.getEjecSatisfactorias();
		/*En caso de que haya habido un error al introducir estadisticas (Por ejemplo, un fallo
		 * en la conexión), me aseguro de que el gráfico no obtenga valores negativos con Max
		 */
		dataset.setValue(b.getString("other"), Math.max(otras, 0));
		JFreeChart chart4 = ChartFactory.createPieChart3D("", dataset, false, false, false);
        PiePlot3D plot4 = (PiePlot3D) chart4.getPlot();
        chart4.getTitle().setPaint(Color.blue); 
        plot4.setForegroundAlpha(0.6f);
        panelInuExitoTotal.removeAll();
        panelInuExitoTotal.add(new ChartPanel(chart4),BorderLayout.CENTER);
        panelInuExitoTotal.revalidate();
        panelInuExitoTotal.repaint();
     
	}
	
	/** Obtiene el texto de las estadisticas que se estan mostrando.
	 * @return Lista de cadenas de texto con las estadisticas mostradas. 
	 */
	public List<String> getTexto(){
		List<String> texto = new ArrayList<String>();
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
	
	/** Obtiene los graficos correspondientes a las estadisticas mostradas.
	 * @return Lista de imagenes de los graficos mostrados.
	 */
	public List<Image> getGraficos(){
		List<Image> graficos = new ArrayList<Image>();
		//Por cada pestaña en mi tabbedpane de graficos
		for(int i=0;i<tabbedPaneGraficos.getComponentCount();i++){
			//Obtengo el chart
			JFreeChart chart =((ChartPanel)((JPanel)tabbedPaneGraficos.
					getComponent(i)).getComponent(0)).getChart();
			graficos.add(chart.createBufferedImage(ANCHOCHART, ALTOCHART));
		}
		return graficos;
	}
	
}
