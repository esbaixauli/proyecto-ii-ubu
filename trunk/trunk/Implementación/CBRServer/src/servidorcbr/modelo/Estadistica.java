package servidorcbr.modelo;

import java.io.Serializable;
import java.util.Date;

/**
 * Clase que encapsula la información relativa a las estadísticas de uso del CBR. Representa
 * las estadísticas de un usuario para un tipo de caso, de todos los usuarios para un tipo de
 * caso, de un usuario para todos sus tipos de caso o de todos los usuarios para todos los
 * tipos de caso.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
public class Estadistica implements Serializable {
	
	/**
	 * Requerido por la interfaz Serializable.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Número de ejecuciones totales. 
	 */
	private long ejecTotales;
	
	/**
	 * Media de la calidad en todas las ejecuciones hasta el momento.
	 */
	private double mediaCalidad;
	
	/**
	 * Número de ejecuciones con calidad > 50.
	 */
	private long ejecSatisfactorias;
	
	/**
	 * Número de ejecuciones con calidad = 0.
	 */
	private long ejecInusables;
	
	/**
	 * Fecha de la última ejecución del ciclo CBR.
	 */
	private Date fechaUltima;
	
	/**
	 * Calidad de la última ejecución del ciclo CBR.
	 */
	private long calidadUltima;
	
	/**
	 * Devuelve el número de ejecuciones totales del ciclo CBR.
	 * @return El número de ejecuciones totales.
	 */
	public long getEjecTotales() {
		return ejecTotales;
	}
	
	/**
	 * Establece el número de ejecuciones totales del ciclo CBR.
	 * @param ejecTotales El número de ejecuciones totales.
	 */
	public void setEjecTotales(long ejecTotales) {
		this.ejecTotales = ejecTotales;
	}
	
	/**
	 * Devuelve la media de calidad de todas las ejecuciones.
	 * @return La calidad media.
	 */
	public double getMediaCalidad() {
		return mediaCalidad;
	}
	
	/**
	 * Establece la media de calidad de todas las ejecuciones.
	 * @param mediaCalidad La calidad media.
	 */
	public void setMediaCalidad(double mediaCalidad) {
		this.mediaCalidad = mediaCalidad;
	}
	
	/**
	 * Devuelve el número de ejecuciones con calidad mayor que 50.
	 * @return El número de ejecuciones con calidad mayor que 50.
	 */
	public long getEjecSatisfactorias() {
		return ejecSatisfactorias;
	}
	
	/**
	 * Establece el número de ejecuciones con calidad mayor que 50.
	 * @param ejecSatisfactorias El número de ejecuciones con calidad mayor que 50.
	 */
	public void setEjecSatisfactorias(long ejecSatisfactorias) {
		this.ejecSatisfactorias = ejecSatisfactorias;
	}
	
	/**
	 * Devuelve el número de ejecuciones con calidad 0.
	 * @return El número de ejecuciones con calidad 0.
	 */
	public long getEjecInusables() {
		return ejecInusables;
	}
	
	/**
	 * Establece el número de ejecuciones con calidad 0.
	 * @param ejecInusables El número de ejecuciones con calidad 0.
	 */
	public void setEjecInusables(long ejecInusables) {
		this.ejecInusables = ejecInusables;
	}
	
	/**
	 * Devuelve la fecha de la última ejecución del ciclo CBR.
	 * @return La fecha de la última ejecución.
	 */
	public Date getFechaUltima() {
		return fechaUltima;
	}
	
	/**
	 * Establece la fecha de la última ejecución del ciclo CBR.
	 * @param fechaUltima La fecha de la última ejecución.
	 */
	public void setFechaUltima(Date fechaUltima) {
		this.fechaUltima = fechaUltima;
	}
	
	/**
	 * Devuelve la calidad de la última ejecución del ciclo CBR.
	 * @return La calidad de la última ejecución.
	 */
	public long getCalidadUltima() {
		return calidadUltima;
	}
	
	/**
	 * Establece la calidad de la última ejecución del ciclo CBR.
	 * @param calidadUltima La calidad de la última ejecución.
	 */
	public void setCalidadUltima(long calidadUltima) {
		this.calidadUltima = calidadUltima;
	}
	
}
