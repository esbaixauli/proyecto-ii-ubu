package vista.componentesGenericos;

import java.awt.Component;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Icon;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import org.jfree.base.log.PadMessage;

@SuppressWarnings("serial")
public class TabbedPaneCerrable extends JTabbedPane {
	
	
	public TabbedPaneCerrable() {
		super();
		cambiarElementos();
	}

	public TabbedPaneCerrable(int tabPlacement, int tabLayoutPolicy) {
		super(tabPlacement, tabLayoutPolicy);
		cambiarElementos();
	}

	public TabbedPaneCerrable(int tabPlacement) {
		super(tabPlacement);
		cambiarElementos();
	}

	@Override
	public void addTab(String title, Component component) {
		addTab(title,null,component,"");
	}
	
	@Override
	public void addTab(String title, Icon icon, Component component, String tip) {
		int encontrado=-1;
		if(!title.startsWith("CBR")){//Si no es un ciclo CBR se intenta no añadir un tab,sino traer
			for(int i=0;i<getTabCount();i++){//al frente el que ya hay
				//Si existe ya un tab con ese nombre
				if(((PanelTitulo)getTabComponentAt(i)).getTitulo().equals(title)){
					encontrado=i;break; //No inserto.
				}
			}
		}
		if(encontrado==-1){//Si no habia un tab con ese nombre lo creo
			super.addTab(title, icon, component, tip);
			encontrado=getTabCount()-1;
			setTabComponentAt(encontrado, new PanelTitulo(title,this,component));
		}
		setSelectedIndex(encontrado);
		
		

		PanelTitulo pt =  (PanelTitulo) getTabComponentAt(getSelectedIndex());
		if(pt!=null){// cambio la fuente al color de selección
			pt.setSelectedColor(true);
		}
	}
	
	
	@Override
	public void setComponentAt(int index, Component component) {
		
		super.setComponentAt(index, component);
	};
	
	/**
	 * Gestiona el listener de cambio de elementos en el tabbedpane.
	 */
	private void cambiarElementos(){
		addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
			
				for(int i=0;i<getTabCount();i++){
					PanelTitulo actual =  (PanelTitulo) getTabComponentAt(i);
					if(actual!=null){
						if(i!= getSelectedIndex()){ //Si no está escogido
							actual.setSelectedColor(false);// le cambio la fuente al color de no selección
						}else{
							actual.setSelectedColor(true);
						
						}
					}
				}

			}
		});
	}
	


	
}
