package vista.componentes;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import servidorcbr.modelo.TipoCaso;

import controlador.ControlCBR;

import java.awt.FlowLayout;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("serial")
public class Splash extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public Splash() {

		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		try {
			setOpacity(0.8f);
		} catch (UnsupportedOperationException uoe) {}
		setBounds(100, 100, 195, 195);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon("res/espera.gif"));
		contentPane.add(label);
	}
	
	public List<HashMap<String,Serializable>> retrieve(TipoCaso tc, HashMap<String,Serializable> q) throws IOException {
		return ControlCBR.retrieve(tc, q);
	}

}