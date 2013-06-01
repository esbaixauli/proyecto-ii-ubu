package servidorcbr.modelo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Clase que representa un tipo de caso, que tiene un nombre, un conjunto de atributos, una
 * lista de técnicas de cada etapa del ciclo CBR y una técnica por defecto para cada etapa
 * del ciclo.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
public class TipoCaso implements Serializable {
	
	/**
	 * Requerido por la interfaz Serializable.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * El nombre del tipo de caso. 
	 */
	private String nombre;
	
	/**
	 * HashMap que contiene los atributos que forman este tipo de caso. 
	 * Mapea el nombre del atributo -> Atributo.
	 */
	private HashMap<String, Atributo> atbos;
	
	/**
	 * Lista de técnicas de recuperación disponibles para este tipo de caso. 
	 */
	private List<Tecnica> tecnicasRecuperacion;
	
	/**
	 * Lista de técnicas de reutilización disponibles para este tipo de caso.
	 */
	private List<Tecnica> tecnicasReutilizacion;
	
	/**
	 * Lista de técnicas de revisión disponibles para este tipo de caso. 
	 */
	private List<Tecnica> tecnicasRevision;
	
	/**
	 * Lista de técnicas de retención disponibles para este tipo de caso.
	 */
	private List<Tecnica> tecnicasRetencion;
	
	/**
	 * Técnica de recuperación por defecto. 
	 */
	private Tecnica defaultRec;
	
	/**
	 * Técnica de reutilización por defecto.
	 */
	private Tecnica defaultReu;
	
	/**
	 * Técnica de revisión por defecto. 
	 */
	private Tecnica defaultRev;
	
	/**
	 * Técnica de retención por defecto.
	 */
	private Tecnica defaultRet;
	
	/**
	 * Devuelve el nombre del tipo de caso.
	 * @return El nombre del tipo de caso.
	 */
	public String getNombre() {
		return nombre;
	}
	
	/**
	 * Establece el nombre del tipo de caso.
	 * @param nombre El nombre del tipo de caso.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	/**
	 * Devuelve los atributos que forman parte del tipo de caso.
	 * @return Un HashMap que mapea nombre del atributo -> Atributo.
	 */
	public HashMap<String, Atributo> getAtbos() {
		return atbos;
	}
	
	/**
	 * Establece los atributos que forman parte del tipo de caso.
	 * @param atbos Un HashMap que contenga los atributos del tipo de caso.
	 */
	public void setAtbos(HashMap<String, Atributo> atbos) {
		this.atbos = atbos;
	}
	
	/**
	 * Devuelve la lista de técnicas de recuperación disponibles.
	 * @return La lista de técnicas de recuperación.
	 */
	public List<Tecnica> getTecnicasRecuperacion() {
		return tecnicasRecuperacion;
	}
	
	/**
	 * Establece la lista de técnicas de recuperación disponibles.
	 * @param tecnicasRecuperacion La lista de técnicas de recuperación.
	 */
	public void setTecnicasRecuperacion(List<Tecnica> tecnicasRecuperacion) {
		this.tecnicasRecuperacion = tecnicasRecuperacion;
	}
	
	/**
	 * Devuelve la lista de técnicas de reutilización disponibles.
	 * @return La lista de técnicas de reutilización.
	 */
	public List<Tecnica> getTecnicasReutilizacion() {
		return tecnicasReutilizacion;
	}
	
	/**
	 * Establece la lista de técnicas de reutilización disponibles.
	 * @param tecnicasReutilizacion La lista de técnicas de reutilización.
	 */
	public void setTecnicasReutilizacion(List<Tecnica> tecnicasReutilizacion) {
		this.tecnicasReutilizacion = tecnicasReutilizacion;
	}
	
	/**
	 * Devuelve la lista de técnicas de revisión disponibles.
	 * @return La lista de técnicas de revisión.
	 */
	public List<Tecnica> getTecnicasRevision() {
		return tecnicasRevision;
	}
	
	/**
	 * Establece la lista de técnicas de revisión disponibles.
	 * @param tecnicasRevision La lista de técnicas de revisión.
	 */
	public void setTecnicasRevision(List<Tecnica> tecnicasRevision) {
		this.tecnicasRevision = tecnicasRevision;
	}
	
	/**
	 * Devuelve la lista de técnicas de retención disponibles.
	 * @return La lista de técnicas de retención
	 */
	public List<Tecnica> getTecnicasRetencion() {
		return tecnicasRetencion;
	}

	/**
	 * Establece la lista de técnicas de retención disponibles.
	 * @param tecnicasRetencion La lista de técnicas de retención.
	 */
	public void setTecnicasRetencion(List<Tecnica> tecnicasRetencion) {
		this.tecnicasRetencion = tecnicasRetencion;
	}
	
	/**
	 * Devuelve la técnica de recuperación por defecto.
	 * @return La técnica de recuperación por defecto.
	 */
	public Tecnica getDefaultRec() {
		return defaultRec;
	}

	/**
	 * Establece la técnica de recuperación por defecto.
	 * @param defaultRec La técnica de recuperación.
	 */
	public void setDefaultRec(Tecnica defaultRec) {
		this.defaultRec = defaultRec;
	}

	/**
	 * Devuelve la técnica por defecto de reutilización.
	 * @return La técnica de reutilización por defecto.
	 */
	public Tecnica getDefaultReu() {
		return defaultReu;
	}

	/**
	 * Establece la técnica de reutilización por defecto.
	 * @param defaultReu La técnica de reutilización.
	 */
	public void setDefaultReu(Tecnica defaultReu) {
		this.defaultReu = defaultReu;
	}

	/**
	 * Devuelve la técnica de revisión por defecto.
	 * @return La técnica de revisión por defecto.
	 */
	public Tecnica getDefaultRev() {
		return defaultRev;
	}

	/**
	 * Establece la técnica de revisión por defecto.
	 * @param defaultRev La técnica de revisión.
	 */
	public void setDefaultRev(Tecnica defaultRev) {
		this.defaultRev = defaultRev;
	}

	/**
	 * Devuelve la técnica de retención por defecto.
	 * @return La técnica de retención por defecto.
	 */
	public Tecnica getDefaultRet() {
		return defaultRet;
	}

	/**
	 * Establece la técnica de retención por defecto.
	 * @param defaultRet La técnica de retención.
	 */
	public void setDefaultRet(Tecnica defaultRet) {
		this.defaultRet = defaultRet;
	}

	@Override
	public String toString() {
		return nombre;
	}
	
}
