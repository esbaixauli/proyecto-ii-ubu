package servidorcbr.modelo;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;

public class Calidad implements CaseComponent {

	public int calidad;
	
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
