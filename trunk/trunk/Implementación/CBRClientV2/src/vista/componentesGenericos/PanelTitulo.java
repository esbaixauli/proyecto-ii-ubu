package vista.componentesGenericos;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTabbedPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BoxLayout;

public class PanelTitulo extends JPanel {
	private JTabbedPane pane;
	private Component contained;
	private JLabel lblTitulo;
	
	/**
	 * Create el panel.
	 * @param label Título del panel.
	 */
	public PanelTitulo(String label, JTabbedPane tabbedPane,Component contenido) {
		setOpaque(false);
		this.pane = tabbedPane;
		this.setName(label);
		this.contained = contenido;
		setLayout(new BorderLayout(0, 0));
		
		lblTitulo = new JLabel(label);
		lblTitulo.setForeground(Color.white);
		add(lblTitulo, BorderLayout.WEST);
		
		final JLabel labelCerrar = new JLabel("");
		labelCerrar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				pane.remove(contained);
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				labelCerrar.setIcon(new ImageIcon("res/cerrars_12.png"));
				setCursor (Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				labelCerrar.setIcon(new ImageIcon("res/cerrar_12.png"));
				setCursor (Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
			
		});
		labelCerrar.setIcon(new ImageIcon("res/cerrar_12.png"));
		add(labelCerrar, BorderLayout.EAST);
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		add(panel, BorderLayout.CENTER);
	}
	
	/**Cambia el color del texto del panel si se ha elegido el elemento o no.
	 * @param selected true si está seleccionado, false si no.
	 */
	public void setSelectedColor(boolean selected){
		if(selected){
			lblTitulo.setForeground(Color.white);
		}else{
			lblTitulo.setForeground(Color.darkGray);
		}
		
	}
	
	/**Devuelve el titulo de este tab.
	 * @return El titulo del tab.
	 */
	public String getTitulo(){
		return lblTitulo.getText();
	}
	

}
