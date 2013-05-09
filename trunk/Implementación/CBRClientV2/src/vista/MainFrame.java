package vista;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;


import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URL;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import servidorcbr.modelo.TipoUsuario;
import servidorcbr.modelo.Usuario;
import vista.componentesGenericos.PanelTitulo;
import vista.componentesGenericos.TabbedPaneCerrable;
import vista.estadisticas.EstadisticasPanel;
import vista.tipos.GestionTiposFrame;
import vista.usuarios.GestionUsuariosFrame;

import javax.swing.JToolBar;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class MainFrame extends JFrame{

	
	private final MainFrame me = this;

	private final Usuario u;

	private ResourceBundle b;
	
	private HelpBroker hb;
    private HelpSet helpset;
    
    private JMenuItem mntmHelp;
    private JMenuItem mntmCicloconfigurado;
    
    private JMenu mnAdmin;
    private JMenu mnCBr;
    private JMenu mnBasescasos;
	
    private TabbedPaneCerrable tabbedPanePrincipal;
    
   


	/**
	 * Create the application.
	 */
	public MainFrame(Usuario usuario) {
		this.u=usuario;
		Locale l= Locale.getDefault();
		b = ResourceBundle.getBundle(
	            "vista.internacionalizacion.Recursos", l);
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu(b.getString("file"));
		menuBar.add(mnFile);
		
		mnCBr = new JMenu("CBR");
		mnFile.add(mnCBr);
		
		JMenuItem mntmCicloBasico = new JMenuItem(b.getString("defaultcycle"));
		mntmCicloBasico.setIcon(new ImageIcon("res/menu/cbr_basico_16.png"));
		mntmCicloBasico.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ElegirTipoCasoFrame f = new ElegirTipoCasoFrame(u, me, ElegirTipoCasoFrame.CICLO_BASICO);
				setEnabled(false);
				f.setVisible(true);
			}
		});
		mnCBr.add(mntmCicloBasico);
		
		mntmCicloconfigurado = new JMenuItem(b.getString("configuredcycle"));
		mntmCicloconfigurado.setIcon(new ImageIcon("res/menu/cbr_configurado_16.png"));
		mntmCicloconfigurado.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ElegirTipoCasoFrame f = new ElegirTipoCasoFrame(u, me, ElegirTipoCasoFrame.CICLO_CONFIGURADO);
				setEnabled(false);
				f.setVisible(true);
			}
		});
		mnCBr.add(mntmCicloconfigurado);
		
		JSeparator separator = new JSeparator();
		mnFile.add(separator);
		
		JMenuItem mntmLogout = new JMenuItem(b.getString("logout"));
		mntmLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame login = new LoginFrame();
				login.setVisible(true);
				me.dispose();
			}
		});
		mnFile.add(mntmLogout);
		
		JMenuItem mntmSalir = new JMenuItem(b.getString("exit"));
		mntmSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mnFile.add(mntmSalir);
		
		mnAdmin = new JMenu(b.getString("admin"));
		menuBar.add(mnAdmin);
		
		JMenuItem mntmUsuarios = new JMenuItem(b.getString("manageusers"));
		mntmUsuarios.setIcon(new ImageIcon("res/menu/usuario_16.png"));
		mntmUsuarios.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame p = new GestionUsuariosFrame(me);
				p.setVisible(true);
				me.setEnabled(false);
			}
		});
		mnAdmin.add(mntmUsuarios);
		
		JMenuItem mntmTiposcaso = new JMenuItem(b.getString("managecasetypes"));
		mntmTiposcaso.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame ftc = new GestionTiposFrame(u, me);
				ftc.setVisible(true);
				me.setEnabled(false);
			}
		});
		mntmTiposcaso.setIcon(new ImageIcon("res/menu/tipos_16.png"));
		mnAdmin.add(mntmTiposcaso);
		
		mnBasescasos = new JMenu(b.getString("casebases"));
		menuBar.add(mnBasescasos);
		
		JMenuItem mntmInsertarcasos = new JMenuItem(b.getString("insertcases"));
		mntmInsertarcasos.setIcon(new ImageIcon("res/menu/insertar_16.png"));
		mntmInsertarcasos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ElegirTipoCasoFrame f = new ElegirTipoCasoFrame(u, me, ElegirTipoCasoFrame.INTRODUCIR_MANUAL);
				setEnabled(false);
				f.setVisible(true);
			}
		});
		mnBasescasos.add(mntmInsertarcasos);
		
		JMenuItem mntmEstadisticas = new JMenuItem(b.getString("stats"));
		mntmEstadisticas.setIcon(new ImageIcon("res/menu/estad_16.png"));
		mntmEstadisticas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addPanel(b.getString("stats"), new EstadisticasPanel(u, me));
			}
		});
		mnBasescasos.add(mntmEstadisticas);
		
		JMenu mnHelp = new JMenu(b.getString("help"));
		menuBar.add(mnHelp);
		
		mntmHelp = new JMenuItem(b.getString("help"));
		mntmHelp.setIcon(new ImageIcon("res/menu/ayuda_16.png"));
		mnHelp.add(mntmHelp);
		mntmHelp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		
		JMenuItem mntmAbout = new JMenuItem(b.getString("about"));
		mnHelp.add(mntmAbout);
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFrame f = new AcercaDeJFrame(me);
				me.setEnabled(false);
				f.setVisible(true);
				
			}
		});
		
		initialize(u);
		
		ocultarPorTipo();
	}

	/**
	 * Oculta ciertas opciones de usuario si este no tiene los privilegios suficientes.
	 */
	private void ocultarPorTipo() {
		if(!u.getTipo().equals(TipoUsuario.ADMINISTRADOR)){
			mnAdmin.setVisible(false);
			if(u.getTipo().equals(TipoUsuario.UBASICO)){
				mntmCicloconfigurado.setVisible(false);
			}
		}
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(final Usuario u) {
	
	
		setTitle(b.getString("mainmenu"));
		setIconImage(new javax.swing.ImageIcon("res/logocbr.png").getImage());
		setResizable(false);
		setBounds(100, 100, 1000, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		tabbedPanePrincipal = new TabbedPaneCerrable(JTabbedPane.TOP);
		tabbedPanePrincipal.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		getContentPane().add(tabbedPanePrincipal, BorderLayout.CENTER);

		refrescarAyuda();
		
		centrar();
	}
	
	/**
	 * Centra la ventana en pantalla.
	 */
	private void centrar() {
	    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (int) ((dimension.getWidth() - getWidth()) / 2);
	    int y = (int) ((dimension.getHeight() - getHeight()) / 2);
	    setLocation(x, y);
	}
	
	/**
	 * Refresca la ayuda.
	 */
	protected final void refrescarAyuda(){
		try {			
			// Carga el fichero de ayuda
			String helpse="res/help/help_set.hs";
			if(! b.getLocale().equals(new Locale("es", "ES")))
				helpse="res/help/help_set_en.hs";
			File fichero = new File(helpse);
			URL hsURL = fichero.toURI().toURL();

			// Crea el HelpSet y el HelpBroker
			helpset = new HelpSet(getClass().getClassLoader(), hsURL);
			hb = helpset.createHelpBroker();
			//Si no hay pestañas en foco, muestra la ayuda de caracter general
			if(tabbedPanePrincipal.getSelectedIndex()==-1){
			// Pone ayuda a item de menu al pulsarlo y a F1 en ventana
				hb.enableHelpOnButton(mntmHelp,getClass().getSimpleName(), helpset);
			}else{
				hb.enableHelpOnButton(mntmHelp,tabbedPanePrincipal.getSelectedComponent().getClass().getSimpleName(), helpset);
			}
		} catch (IllegalArgumentException e) {
			hb.enableHelpOnButton(mntmHelp, "MainFrame",helpset);
		} catch (Exception e) {
		
		}
	}
	
	/**Obtiene el tabbedpane del área de trabajo principal.
	 * @return tabbedpane del área de trabajo principal.
	 */
	public TabbedPaneCerrable getTabbedPanePrincipal() {
		return tabbedPanePrincipal;
	}
	
	/**Elimina el panel por su nombre de los tabbedpane.
	 * @param nombre
	 */
	public void removePanel(String nombre){
		for(int i=0; i< tabbedPanePrincipal.getTabCount();i++){
			Component c= tabbedPanePrincipal.getTabComponentAt(i);
			if(c.getName().equals(nombre)){
				tabbedPanePrincipal.remove(i);
			}
		}
	}
	

	/**Añade un panel a la ventana de trabajo. Su nombre será el incorporado en el método de JPanel.
	 * @param p El panel.
	 * @see javax.swing.JPanel
	 */
	public void addPanel(JPanel p){
		addPanel(p.getName(),p);
	}
	
	
	/**Añade un panel a la ventana de trabajo.
	 * @param nombre Nombre del panel.
	 * @param p El panel.
	 */
	public void addPanel(String nombre, JPanel p){
		tabbedPanePrincipal.addTab(nombre,p);
		me.toFront();
		me.setEnabled(true);
	}
	
	@Override
	public void setEnabled(boolean b) {
		super.setEnabled(b);
		refrescarAyuda();
	}


}
