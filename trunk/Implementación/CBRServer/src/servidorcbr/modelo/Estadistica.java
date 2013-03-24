package servidorcbr.modelo;

import java.io.Serializable;
import java.util.Date;

public class Estadistica implements Serializable {
	private long ejecTotales;
	private double mediaCalidad;
	private long ejecSatisfactorias;
	private long ejecInusables;
	private Date fechaUltima;
	private long calidadUltima;
	
	public long getEjecTotales() {
		return ejecTotales;
	}
	public void setEjecTotales(long ejecTotales) {
		this.ejecTotales = ejecTotales;
	}
	public double getMediaCalidad() {
		return mediaCalidad;
	}
	public void setMediaCalidad(double mediaCalidad) {
		this.mediaCalidad = mediaCalidad;
	}
	public long getEjecSatisfactorias() {
		return ejecSatisfactorias;
	}
	public void setEjecSatisfactorias(long ejecSatisfactorias) {
		this.ejecSatisfactorias = ejecSatisfactorias;
	}
	public long getEjecInusables() {
		return ejecInusables;
	}
	public void setEjecInusables(long ejecInusables) {
		this.ejecInusables = ejecInusables;
	}
	public Date getFechaUltima() {
		return fechaUltima;
	}
	public void setFechaUltima(Date fechaUltima) {
		this.fechaUltima = fechaUltima;
	}
	public long getCalidadUltima() {
		return calidadUltima;
	}
	public void setCalidadUltima(long calidadUltima) {
		this.calidadUltima = calidadUltima;
	}
	
}
