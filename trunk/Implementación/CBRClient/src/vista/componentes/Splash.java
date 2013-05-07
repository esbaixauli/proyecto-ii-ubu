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
import java.awt.FlowLayout;
import java.net.MalformedURLException;
import java.net.URL;

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
		setOpacity(0.8f);
		setBounds(100, 100, 195, 195);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon("res/espera.gif"));
		contentPane.add(label);
	}
	


}