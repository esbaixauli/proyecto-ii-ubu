package servidorcbr.modelo;

import java.io.Serializable;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;

public class Calidad implements Serializable, CaseComponent {

	private static final long serialVersionUID = 1L;
	private int calidad;
	
	public int getCalidad() {
		return calidad;
	}
	
	public void setCalidad(int c) {
		calidad = c;
	}
	
	@Override
	public Attribute getIdAttribute() {
		return new Attribute("calidad", getClass());
	}

}
