package servidorcbr.modelo;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;

/**
 * Parte de un CBRCase que representa la calidad del caso. Implementa la interfaz CaseComponent
 * para poder ser asignado al CBRCase mediante su método setJustificationOfSolution().
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
public class Calidad implements CaseComponent {

	/**
	 * La calidad del caso, representada por un valor entre 0 y 100.
	 */
	public int calidad;
	
	/**
	 * Devuelve la calidad del caso.
	 * @return La calidad del caso.
	 */
	public int getCalidad() {
		return calidad;
	}
	
	/**
	 * Establece la calidad del caso.
	 * @param c La calidad del caso.
	 */
	public void setCalidad(int c) {
		calidad = c;
	}
	
	/* (non-Javadoc)
	 * @see jcolibri.cbrcore.CaseComponent#getIdAttribute()
	 */
	@Override
	public Attribute getIdAttribute() {
		return new Attribute("calidad", getClass());
	}

}
