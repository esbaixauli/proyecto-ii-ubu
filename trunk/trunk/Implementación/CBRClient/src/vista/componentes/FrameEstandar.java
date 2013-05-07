package vista.componentes;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.HelpSetException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import vista.AcercaDeJFrame;

import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URL;

@SuppressWarnings("serial")
public class FrameEstandar extends JFrame {

	protected FrameEstandar me;
	protected JFrame padre;
	protected ResourceBundle b = ResourceBundle.getBundle(
			"vista.internacionalizacion.Recursos", Locale.getDefault());
	
	private HelpBroker hb;
    private HelpSet helpset;
	
	private JMenuBar menuBar;
	private JMenuItem mntmHelp;
	
	public FrameEstandar(JFrame padre){
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
