package vista.configtecnicas;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import servidorcbr.modelo.Parametro;
import servidorcbr.modelo.Tecnica;
import vista.componentes.FrameEstandar;
import vista.panels.PanelPostAdaptacion;

@SuppressWarnings("serial")
public class CombineQueryConfigFrame extends FrameEstandar {

	private JPanel contentPane;
	private JFrame me;

	private PanelPostAdaptacion panelPostAdaptacion;
	
	/** Constructor del frame.
	 * @param t La técnica a configurar.
	 * @param tc
	 * @param padre
	 */
	public CombineQueryConfigFrame(final Tecnica t, final JFrame padre) {
		super(padre);me=this;
		setTitle(b.getString("configuremethod"));
		setBounds(100, 100, 418, 187);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2, BorderLayout.NORTH);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.Y_AXIS));
		
		JPanel panel = new JPanel();
		contentPane.add(panel,BorderLayout.SOUTH);
		
		
		JButton btnOk = new JButton(b.getString("ok"));
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<Parametro> l = new ArrayList<Parametro>();
				//Combinar o no, y qué tipo de combinación
				Parametro p1 = new Parametro();
				p1.setNombre(panelPostAdaptacion.getCombinar());
				p1.setValor(3);
				l.add(p1);
				t.setParams(l);
				padre.setEnabled(true);
				me.setVisible(false);
				me.dispose();
			}
		});
		me.getRootPane().setDefaultButton(btnOk);
		panel.add(btnOk);
		
		JButton btnCancel = new JButton(b.getString("cancel"));
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				padre.setEnabled(true);
				me.setVisible(false);
				me.dispose();
			}
		});
		panel.add(btnCancel);
		
		panelPostAdaptacion = new PanelPostAdaptacion();
		contentPane.add(panelPostAdaptacion, BorderLayout.CENTER);
		
	}

}
