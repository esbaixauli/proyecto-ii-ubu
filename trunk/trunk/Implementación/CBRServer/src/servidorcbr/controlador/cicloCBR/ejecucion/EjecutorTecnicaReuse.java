package servidorcbr.controlador.cicloCBR.ejecucion;

import java.util.Collection;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.method.reuse.CombineQueryAndCasesMethod;
import jcolibri.method.reuse.DirectAttributeCopyMethod;
import jcolibri.method.reuse.NumericDirectProportionMethod;
import servidorcbr.controlador.generadorClases.CargadorClases;
import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.Parametro;
import servidorcbr.modelo.TipoCaso;

public class EjecutorTecnicaReuse {
	private TipoCaso tc;
	public EjecutorTecnicaReuse(TipoCaso tc){
		this.tc=tc;
	}
	
	/** Ejecuta la técnica de adaptación por defecto para este tipo de caso.
	 * @param casos La lista de casos para los que se ejecuta la técnica.
	 * @param query La consulta a realizar.
	 * @return resultado de la adaptación
	 * @throws ClassNotFoundException si no existe el tipo de caso 
	 * (Ha sido borrado por otro usuario mientras se hacia la consulta).
	 */
	public  Collection<CBRCase> ejecutar(Collection<CBRCase> casos, CBRQuery query) throws ClassNotFoundException{

		if(tc.getDefaultReu().getNombre().equalsIgnoreCase("CombineQueryAndCasesMethod")){
			return CombineQueryAndCasesMethod.combine(query, casos);
		}else{

			if(tc.getDefaultReu().getNombre().equalsIgnoreCase("DirectAttributeCopyMethod")){
				//Esta tecnica requiere un atbo origen y un atbo destino
				String origen=null,destino=null;
				setOrigenYDestino(origen,destino);
				DirectAttributeCopyMethod.copyAttribute(
						new Attribute(origen,CargadorClases.cargarClaseProblema(tc.getNombre()))
						, new Attribute(destino,CargadorClases.cargarClaseProblema(tc.getNombre())) 
						, query, casos);
			}else if(tc.getDefaultReu().getNombre().equalsIgnoreCase("NumericDirectProportionMethod")){
				//Esta tecnica requiere un atbo origen y un atbo destino
				String origen=null,destino=null;
				setOrigenYDestino(origen,destino);
				NumericDirectProportionMethod.directProportion(new Attribute(origen,
						CargadorClases.cargarClaseProblema(tc.getNombre())),
						new Attribute(destino,CargadorClases.cargarClaseSolucion(tc.getNombre()))
						, query, casos);
			}	
		}	
		
		return null;
	}

	
	/** Auxiliar. Para las técnicas que requieren copiar un atributo a otro,
	 *  busca qué atributo se debe copiar a cual.
	 * @param origen Se rellena con el nombre del atributo que será origen.
	 * @param destino Se rellena con el nombre del atributo que será destino.
	 */
	private void setOrigenYDestino(String origen,String destino) {
		Parametro p1 = tc.getDefaultReu().getParams().get(0);
		
		//Un valor de 0 en la configuración indica que la técnica no está configurada.
		if(p1.getValor()==0){
		  if(tc.getDefaultReu().getNombre().equalsIgnoreCase("DirectAttributeCopyMethod")){
			  //En la tecnica directattributecopy se copia un atributo problema del query
			  //a su equivalente en los casos obtenidos del retrieval
			  origen=getAtboAleatorio(true);
			  destino=origen;
		  }else{
			  //En la tecnica numeric proportion se requiere un origen (atbo del problema) y
			  //un destino (atbo de la solución)
			  origen=getAtboAleatorio(true);
			  destino=getAtboAleatorio(false);
		  }
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

	/** Auxiliar. Devuelve el primer atributo del problema o de la solución que encuentre en el tipo de caso.
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
}
