package servidorcbr.controlador.cicloCBR.ejecucion;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.CaseComponent;
import jcolibri.method.reuse.CombineQueryAndCasesMethod;
import jcolibri.method.reuse.DirectAttributeCopyMethod;
import jcolibri.method.reuse.NumericDirectProportionMethod;
import servidorcbr.controlador.generadorClases.CargadorClases;
import servidorcbr.controlador.generadorClases.RellenadorClases;
import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.Calidad;
import servidorcbr.modelo.Parametro;
import servidorcbr.modelo.Tecnica;
import servidorcbr.modelo.TipoCaso;

/**
 * Ejecutor de las técnicas de reutilización. Hace de fachada de las clases de jColibri para
 * utilizarlas desde servidorcbr.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
public class EjecutorTecnicaReuse {
	
	/**
	 * El tipo de caso sobre el que se va a aplicar la reutilización.
	 */
	private TipoCaso tc;
	
	/**
	 * Constructor de la clase. Recibe el tipo de caso.
	 * @param tc El tipo de caso sobre el que se va a aplicar la reutilización.
	 */
	public EjecutorTecnicaReuse(TipoCaso tc){
		this.tc=tc;
	}
	
	/**
	 * Para las técnicas que requieren copiar un atributo sobre otro, éste es el origen.
	 */
	private String origen;
	
	/**
	 * Para las técnicas que requieren copiar un atributo sobre otro, éste es el destino.
	 */
	private String destino;
	
	/** 
	 * Ejecuta la técnica de adaptación por defecto para este tipo de caso.
	 * @param casos La lista de casos para los que se ejecuta la técnica.
	 * @param query La consulta a realizar.
	 * @return resultado de la adaptación
	 * @throws ClassNotFoundException si no existe el tipo de caso 
	 * (Ha sido borrado por otro usuario mientras se hacia la consulta).
	 */
	public Collection<CBRCase> ejecutar(Collection<CBRCase> casos, CBRQuery query) throws ClassNotFoundException{

		if(tc.getDefaultReu().getNombre().equalsIgnoreCase("CombineQueryAndCasesMethod")){
			casos = CombineQueryAndCasesMethod.combine(query, casos);
			if (tc.getDefaultReu().getParams().size() > 0
					&& tc.getDefaultReu().getParams().get(0).getValor() == 3) {
				return combinar(casos);
			}
			return casos;
		}else{
			//Esta tecnica requiere un atbo origen y un atbo destino
			setOrigenYDestino();
			if (origen == null || destino == null) {
				return casos;
			}
			if(tc.getDefaultReu().getNombre().equalsIgnoreCase("DirectAttributeCopyMethod")){
				DirectAttributeCopyMethod.copyAttribute(
						new Attribute(origen,query.getDescription().getClass())
						, new Attribute(destino,query.getDescription().getClass()) 
						, query, casos);
			}else if(tc.getDefaultReu().getNombre().equalsIgnoreCase("NumericDirectProportionMethod")){
				NumericDirectProportionMethod.directProportion(
						new Attribute(origen, query.getDescription().getClass()),
						new Attribute(destino, casos.iterator().next().getSolution().getClass()),
						query, casos);
			}
			if (tc.getDefaultReu().getParams().size() > 2
					&& !tc.getDefaultReu().getParams().get(2).getNombre().equals("NOCOMBINAR")) {
				return combinar(casos);
			}
		}
		return casos;
	}

	
	/**
	 * Auxiliar. Para las técnicas que requieren copiar un atributo a otro,
	 * busca qué atributo se debe copiar a cual.
	 */
	private void setOrigenYDestino() {
		Parametro p1 = tc.getDefaultReu().getParams().get(0);	
		//Un valor de 0 en la configuración indica que la técnica no está configurada.
		if(p1.getValor()==0){
			// Si no está configurado, no hace nada
			origen = null;
			return;
		}else{
			Parametro p2 = tc.getDefaultReu().getParams().get(1);
			// Si el parametro 1 es el origen (y el 2 el destino)
			if(p1.getValor()==1){
				origen =p1.getNombre();
				destino=p2.getNombre();
			}else{ //Si es al reves
				origen =p2.getNombre();
				destino=p1.getNombre();
			}
		}
	}

	/** 
	 * Auxiliar. Devuelve el primer atributo del problema o de la solución que encuentre en el tipo de caso.
	 * Este método se emplea si no existen valores
	 * configuración para la técnica de adaptación, en cuyo caso toma
	 * valores de atributos aleatorios.
	 * @param problema Si se busca un atributo problema es true, false si se busca de la solución.
	 * @return un atributo cualquiera
	 */
	private String getAtboAleatorio(boolean problema){
		for(Atributo a:tc.getAtbos().values()){
			if(problema== a.getEsProblema()){
				return a.getNombre();
			}
		}
		return null;
	}

	
	/** 
	 * Realiza una combinación de los resultados, si la técnica así lo indica en sus parámetros.
	 * @param casos La colección de casos a combinar (o no).
	 * @return El caso resultado de combinarlos si había que hacerlo, o la colección completa si no.
	 */
	private Collection<CBRCase> combinar(Collection<CBRCase> casos){
		Tecnica t = tc.getDefaultReu();
		String combinacion;
		//Si la tecnica no está configurada (solo tiene un parametro, con valor 0).
		if(t.getParams().get(0).getValor() == 0){
			return casos;
		}//En la combine query and cases, el parametro en 0 debe tener un valor de 3 para que se combine
		if(t.getNombre().equalsIgnoreCase("CombineQueryAndCasesMethod")){
			combinacion=t.getParams().get(0).getNombre();
			
		}else{//En las demás, es el parametro 2 el que tiene un valor de 3.
			combinacion=t.getParams().get(2).getNombre();
		}
		return combinar(combinacion,casos);
		
	}
	
	/**
	 * Realiza la combinación de los casos según el modo pedido.
	 * @param combinacion El tipo de combinación requerida, puede ser "MEDIA" o "MODA".
	 * @param casos La colección de casos a combinar.
	 * @return Una collection con un solo CBRCase, el resultado de combinar los existentes.
	 */
	private Collection<CBRCase> combinar(String combinacion,Collection<CBRCase> casos){
		try{
			CBRCase combinado;
			HashMap<String,Serializable> calculos = new HashMap<String,Serializable>();
			if(combinacion.equals("MEDIA")){
				//Por cada atributo
				for(Atributo a: tc.getAtbos().values()){
					//Si es un numerico
					if(a.getTipo().equals("I") || a.getTipo().equals("D")){
						List<Double> values=new ArrayList<Double>();
						Class<?> ccaso;
						//Por cada caso
						for(CBRCase caso: casos){
							CaseComponent casodessol= caso.getDescription();
							//Si el atbo es del problema, cojo la parte del problema del caso, si no, la solución
							if(!a.getEsProblema()){
								casodessol = caso.getSolution();
							}
							ccaso = casodessol.getClass();
							//Obtengo dinamicamente el valor de este atributo para este caso
							Object o = ccaso.getMethod("get"+a.getNombre().substring(0,1).toUpperCase()+
									a.getNombre().substring(1), (Class<?>[]) null).invoke(casodessol, (Object[]) null);
							Double d = null;
							if (a.getTipo().equals("I")) {
								d = new Double((Integer) o);
							} else {
								d = (Double) o;
							}
							values.add(d);
						}//Calculo la media
						Double media = media(values);
						if(a.getTipo().equals("I")){
							//lo inserto en un hashmap {"NombreAtbo",valor}
							calculos.put(a.getNombre(),new Long(Math.round(media)).intValue());
						}else{
							calculos.put(a.getNombre(), media);
						}
					}else{//Para las cadenas, se hace la moda
						calculos.put(a.getNombre(),calcularModa(casos, a));
					}
				}
			}else if(combinacion.equals("MODA")){
				for(Atributo a: tc.getAtbos().values()){
					calculos.put(a.getNombre(),calcularModa(casos,a));
				}
			}
			List<Double> cals = new ArrayList<Double>();
			for (CBRCase c: casos) {
				cals.add(new Double(((Calidad) c.getJustificationOfSolution()).getCalidad()));
			}
			calculos.put("META_QUALITY", new Integer((int) Math.round(media(cals))));
			return getListaCBRCase(calculos);
			//Ante cualquier excepción, devuelve los casos sin adaptar (Cancela la adaptación)
		}catch(Exception ex){ex.printStackTrace();}
		return casos;
	}

	/** Auxiliar. Obtiene una lista de CBRCase que contiene unicamente un CBRCase, correspondiente
	 * al hashmap de calculos que recibe.
	 * @param calculos Mapa {nombredeatbo,valor} para generar un CBRCase.
	 * @return Lista de CBRCase con el objeto generado a través del mapa de cálculos.
	 * @throws ClassNotFoundException Si no se encontró el tipo de caso correspondiente al mapa de cálculos.
	 */
	private Collection<CBRCase> getListaCBRCase(HashMap<String, Serializable> calculos)
			throws ClassNotFoundException {
		CBRCase combinado;
		combinado = RellenadorClases.rellenarCaso(tc, calculos);
		//Para respetar la firma del método
		List<CBRCase> listaComb = new ArrayList<CBRCase>(1);
		listaComb.add(combinado);
		return listaComb;
	}

	/** Auxiliar. Calcula la moda de un atributo para un conjunto de casos
	 * @param casos Los casos en los que se va a calcular la moda.
	 * @param a El atributo para el que se va a calcular.
	 * @return El objeto que devuelve la moda.
	 * @throws Exception en caso de que la clase no se encuentre.
	 */
	private Serializable calcularModa(Collection<CBRCase> casos, Atributo a)
			throws Exception {
		Serializable obj;
		try{
		List<String> values=new ArrayList<String>();
		Class<?> ccaso;
		for(CBRCase caso: casos){
			CaseComponent casodessol= caso.getDescription();
			//Si el atbo es del problema, cojo la parte del problema del caso, si no, la solución
			if(!a.getEsProblema()){
				casodessol = caso.getSolution();
			}
			ccaso = casodessol.getClass();
			String d = ccaso.getMethod("get"+a.getNombre().substring(0,1).toUpperCase()+
					a.getNombre().substring(1), (Class<?>[]) null).invoke(casodessol,(Object[]) null) +"";
			values.add(d);
		}
		String moda = moda(values);
		if(a.getTipo().equals("I")){
			obj= Integer.parseInt(moda);
		}else if(a.getTipo().equals("D")){
			obj= Double.parseDouble(moda);
		}else{
			obj=moda;
		}
		}catch(Exception e){
			throw new Exception(e);
		}
		return obj;
	}
	
	/**Auxiliar. Calcula la media aritmética de un conjunto de doubles.
	 * @param values Los valores del conjunto.
	 * @return Media aritmética del conjunto
	 */
	private double media(List<Double> values){
		double suma=0, den=0;
		for(Double v:values){
			suma+=v;
			den++;
		}
		return suma/den;
	}
	
	/**Auxiliar. Calcula la moda de un conjunto de doubles.
	 * @param values Los valores del conjunto.
	 * @return Moda del conjunto
	 */
	private String moda(List<String> values){
		int max=0;
		String val=values.get(0);
		for(String a:values){
			if(max< Collections.frequency(values, a)){
				val=a;
			}
		}
		return val;
	}
	
}

