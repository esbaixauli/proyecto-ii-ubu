package servidorcbr.modelo;

import java.util.HashMap;
import java.util.List;

public class TipoCaso {
	
	private String nombre;
	private HashMap<String, Atributo> atbos;
	private List<String> tecnicasRecuperacion;
	private List<String> tecnicasReutilizacion;
	private List<String> tecnicasRevision;
	private List<String> tecnicasRetencion;
	
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
	
	public List<String> getTecnicasRecuperacion() {
		return tecnicasRecuperacion;
	}
	
	public void setTecnicasRecuperacion(List<String> tecnicasRecuperacion) {
		this.tecnicasRecuperacion = tecnicasRecuperacion;
	}
	
	public List<String> getTecnicasReutilizacion() {
		return tecnicasReutilizacion;
	}
	
	public void setTecnicasReutilizacion(List<String> tecnicasReutilizacion) {
		this.tecnicasReutilizacion = tecnicasReutilizacion;
	}
	
	public List<String> getTecnicasRevision() {
		return tecnicasRevision;
	}
	
	public void setTecnicasRevision(List<String> tecnicasRevision) {
		this.tecnicasRevision = tecnicasRevision;
	}
	
	public List<String> getTecnicasRetencion() {
		return tecnicasRetencion;
	}

	public void setTecnicasRetencion(List<String> tecnicasRetencion) {
		this.tecnicasRetencion = tecnicasRetencion;
	}
	
}
