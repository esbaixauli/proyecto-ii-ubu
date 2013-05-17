package test.servidorcbr.controlador.generadorClases;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;

/**
 * Clase Mockup para pruebas del cargador de clases. Implementa el interfaz
 * de jColibri casecomponent y posee 3 atributos, una cadena, un entero y un double.
 * @see jcolibri.cbrcore.CaseComponent;
 */
public class MockUpCaseComponent implements CaseComponent {

	/**
	 * Cadena de prueba
	 */
	private String a1;
	/**
	 * Entero de prueba
	 */
	private Integer a2;
	/**
	 * Decimal en punto flotante de prueba
	 */
	private Double a3;
	
	/* (non-Javadoc)
	 * @see jcolibri.cbrcore.CaseComponent#getIdAttribute()
	 */
	@Override
	public Attribute getIdAttribute() {
		// TODO Auto-generated method stub
		return new Attribute("a1",getClass());
	}

	/**getter del atributo 1 de prueba
	 * @return a1
	 */
	public String getA1() {
		return a1;
	}

	/**setter del atributo 1 de prueba
	 * @param a1 atributo que se establece
	 */
	public void setA1(String a1) {
		this.a1 = a1;
	}
	/**getter del atributo 2 de prueba
	 * @return a2
	 */
	public Integer getA2() {
		return a2;
	}

	/**setter del atributo 2 de prueba
	 * @param a1 atributo que se establece
	 */
	public void setA2(Integer a2) {
		this.a2 = a2;
	}
	/**getter del atributo 3 de prueba
	 * @return a3
	 */
	public Double getA3() {
		return a3;
	}
	
	/**setter del atributo 3 de prueba
	 * @param a1 atributo que se establece
	 */
	public void setA3(Double a3) {
		this.a3 = a3;
	}
	

}
