package vista.componentesGenericos;

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
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import vista.AcercaDeJFrame;
import vista.MainFrame;

import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URL;

@SuppressWarnings("serial")
public class PanelEstandar extends JPanel{

	protected PanelEstandar me;
	protected MainFrame padre;
	protected ResourceBundle b = ResourceBundle.getBundle(
			"vista.internacionalizacion.Recursos", Locale.getDefault());
	
	private HelpBroker hb;
    private HelpSet helpset;
	
	private JMenuBar menuBar;
	private JMenuItem mntmHelp;
	
	public PanelEstandar(MainFrame padre){
		super();
		me=this;
		this.padre=padre;

	
	}
	

	
	
	/**
	 * Devuelve el padre del frame actual.
	 */
	public MainFrame getPadre() {
		return padre;
	}
	


}
