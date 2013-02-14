package servidorcbr.modelo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class TipoCaso implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String nombre;
	private HashMap<String, Atributo> atbos;
	private List<Tecnica> tecnicasRecuperacion;
	private List<Tecnica> tecnicasReutilizacion;
	private List<Tecnica> tecnicasRevision;
	private List<Tecnica> tecnicasRetencion;
	private Tecnica defaultRec;
	private Tecnica defaultReu;
	private Tecnica defaultRev;
	private Tecnica defaultRet;
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public HashMap<String, Atributo> getAtbos() {
		return atbos;
	}
	
	public void setAtbos(HashMap<String, Atributo> atbos) {
		this.atbos = atbos;
	}
	
	public List<Tecnica> getTecnicasRecuperacion() {
		return tecnicasRecuperacion;
	}
	
	public void setTecnicasRecuperacion(List<Tecnica> tecnicasRecuperacion) {
		this.tecnicasRecuperacion = tecnicasRecuperacion;
	}
	
	public List<Tecnica> getTecnicasReutilizacion() {
		return tecnicasReutilizacion;
	}
	
	public void setTecnicasReutilizacion(List<Tecnica> tecnicasReutilizacion) {
		this.tecnicasReutilizacion = tecnicasReutilizacion;
	}
	
	public List<Tecnica> getTecnicasRevision() {
		return tecnicasRevision;
	}
	
	public void setTecnicasRevision(List<Tecnica> tecnicasRevision) {
		this.tecnicasRevision = tecnicasRevision;
	}
	
	public List<Tecnica> getTecnicasRetencion() {
		return tecnicasRetencion;
	}

	public void setTecnicasRetencion(List<Tecnica> tecnicasRetencion) {
		this.tecnicasRetencion = tecnicasRetencion;
	}
	
	public Tecnica getDefaultRec() {
		return defaultRec;
	}

	public void setDefaultRec(Tecnica defaultRec) {
		this.defaultRec = defaultRec;
	}

	public Tecnica getDefaultReu() {
		return defaultReu;
	}

	public void setDefaultReu(Tecnica defaultReu) {
		this.defaultReu = defaultReu;
	}

	public Tecnica getDefaultRev() {
		return defaultRev;
	}

	public void setDefaultRev(Tecnica defaultRev) {
		this.defaultRev = defaultRev;
	}

	public Tecnica getDefaultRet() {
		return defaultRet;
	}

	public void setDefaultRet(Tecnica defaultRet) {
		this.defaultRet = defaultRet;
	}

	@Override
	public String toString() {
		return nombre;
	}
	
}
