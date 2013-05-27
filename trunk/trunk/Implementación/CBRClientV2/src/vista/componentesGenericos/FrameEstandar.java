package vista.componentesGenericos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import vista.AcercaDeJFrame;
import vista.MainFrame;

/**Componente genérico. Representa un frame reutilizable en cualquier ventana
 * de la aplicación. Su utilidad reside en que establece automáticamente el
 * comportamiento de cierre, la internacionalización y la ayuda del frame, sin
 * escribir el código en cada ventana.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
@SuppressWarnings("serial")
public class FrameEstandar extends JFrame {

	/**
	 * Referencia al propio frame.
	 */
	protected FrameEstandar me;
	/**
	 * Referencia al frame padre (Ventana principal, generalmente).
	 */
	protected MainFrame padre;
	/**
	 * Bundle de internacionalización.
	 */
	protected ResourceBundle b = ResourceBundle.getBundle(
			"vista.internacionalizacion.Recursos", Locale.getDefault());
	
	/**
	 * Helpbroker de la ayuda.
	 */
	private HelpBroker hb;
    /**
     * Helpset de la ayuda.
     */
    private HelpSet helpset;
	
	/**
	 * Barra de menú genérica. Por defecto contiene la opción de ayuda.
	 */
	private JMenuBar menuBar;
	/**
	 * Item de menú de ayuda.
	 */
	private JMenuItem mntmHelp;
	
	/**Constructor del frame.
	 * @param padre Ventana padre de esta.
	 */
	public FrameEstandar(MainFrame padre){
		super();
		me=this;
		this.padre=padre;

		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setIconImage(new ImageIcon("res/logocbr.png").getImage());
		
		 menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnHelp = new JMenu(b.getString("help"));
		menuBar.add(mnHelp);
		
		mntmHelp = new JMenuItem(b.getString("help"));
		mntmHelp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		mnHelp.add(mntmHelp);
		
		JMenuItem mntmAbout = new JMenuItem(b.getString("about"));
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFrame f = new AcercaDeJFrame(me);
				me.setEnabled(false);
				f.setVisible(true);
				
			}
		});
		mnHelp.add(mntmAbout);
		cierreVentana();
		refrescar();
	}
	

	/**
	 * Gestiona la acción por defecto al cerrar la ventana.
	 */
	private void cierreVentana() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				padre.setEnabled(true);
				padre.toFront();
				me.setVisible(false);
				me.dispose();
			}
		});
	}
	
	/**
	 * Devuelve el padre del frame actual.
	 */
	public JFrame getPadre() {
		return padre;
	}
	

	/**
	 * Refresca la ayuda.
	 */
	protected final void refrescar(){
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
			// Pone ayuda a item de menu al pulsarlo y a F1 en ventana
			hb.enableHelpOnButton(mntmHelp,getClass().getSimpleName(), helpset);
		
		} catch (IllegalArgumentException e) {
			hb.enableHelpOnButton(mntmHelp, "MainFrame",helpset);
		} catch (Exception e) {
			
		}
	}
	
	@Override
	public void setEnabled(boolean v){
		super.setEnabled(v);
		refrescar();
	}

}
