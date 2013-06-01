package servidorcbr.servlets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jcolibri.cbrcore.CBRCase;

import servidorcbr.controlador.ControladorCasos;
import servidorcbr.controlador.ControladorEstadisticas;
import servidorcbr.controlador.ControladorTipos;
import servidorcbr.controlador.cicloCBR.LanzadorCBR;
import servidorcbr.controlador.generadorClases.RellenadorClases;
import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.Usuario;
import servidorcbr.modelo.excepciones.PersistenciaException;
import servidorcbr.persistencia.sql.SQLFacade;


/**
 * Servlet implementation class ServletCBR. Se encarga de gestionar las peticiones del cliente
 * relacionadas con el ciclo CBR.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 */
@WebServlet("/ServletCBR")
public class ServletCBR extends HttpServlet {
	
	/**
	 * Requerido por la interfaz Serializable (implementada por HttpServlet).
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Instancia de LanzadorCBR para lanzar las distintas etapas del ciclo. 
	 */
	private LanzadorCBR lanzador = new LanzadorCBR();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletCBR() {
		super();

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletOutputStream sos = response.getOutputStream();
		ServletInputStream sis = request.getInputStream();
		ObjectOutputStream oos = new ObjectOutputStream(sos);
		ObjectInputStream ois = new ObjectInputStream(sis);
		String tipo = null;
		try {
			tipo = (String) ois.readObject();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		if (tipo.equals("retrieve")) {
			iniciaRetrieval(oos, ois);
		} else if (tipo.equals("reuse")) {
			iniciaReuse(oos, ois);
		} else if (tipo.equals("retain")) {
			iniciaRetain(response, oos, ois);
		}else if(tipo.equals("completo")){
			cicloCompleto(oos, ois);
		}
		oos.close();
		sos.close();
	}

	/**
	 * Inicia una ejecución del ciclo completo. Para ello, recibe el tipo de caso y el query
	 * del cliente, lanza la recuperación y con el resultado lanza la reutilización. Como la 
	 * revisión es manual, devuelve la salida de la reutilización al cliente.
	 * @param oos ObjectOutputStream al que escribir la salida de la reutilización.
	 * @param ois ObjectInputStream del que leer el tipo de caso y el query.
	 * @throws IOException Si se produce un error de conexión.
	 */
	private void cicloCompleto(ObjectOutputStream oos, ObjectInputStream ois)
			throws IOException {
		TipoCaso tc = null;
		HashMap<String,Serializable> query = null;
		try {
			tc = (TipoCaso) ois.readObject();
			query = (HashMap<String,Serializable>) ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		Collection<CBRCase> casos = lanzador.reuse(tc,lanzador.retrieve(tc, query));
		List<HashMap<String,Serializable>> casosH = new ArrayList<HashMap<String,Serializable>>(casos.size());
		for (CBRCase caso : casos) {
			casosH.add(RellenadorClases.rellenarHash(tc, caso));
		}
		oos.writeObject(casosH);
	}

	/**
	 * Lanza la etapa de retención, es decir, almacena el caso nuevo en Hbase.
	 * @param response Respuesta del servlet, por si hay que escribir un error.
	 * @param oos ObjectOutputStream al que escribir si la operación ha tenido éxito o no.
	 * @param ois ObjectInputStream del que leer el tipo de caso, el caso y el usuario.
	 * @throws IOException Si se produce un error de conexión.
	 */
	private void iniciaRetain(HttpServletResponse response,
			ObjectOutputStream oos, ObjectInputStream ois) throws IOException {
		TipoCaso tc = null;
		HashMap<String,Serializable> caso = null;
		Usuario u = null;
		try {
			tc = (TipoCaso) ois.readObject();
			caso = (HashMap<String,Serializable>) ois.readObject();
			u = (Usuario) ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			boolean result = ControladorCasos.retain(tc, caso);
			if (result) {
				ControladorEstadisticas.updateEstadistica(u, tc, (Integer) caso.get("META_QUALITY"));
			}
			oos.writeBoolean(result);
		} catch (PersistenciaException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Inicia la reutilización de un conjunto de casos. Después envía al cliente los casos
	 * adaptados.
	 * @param oos ObjectOutputStream al que escrir los casos adaptados.
	 * @param ois ObjectInputStream del que leer el tipo de caso, el query y los casos sin adaptar.
	 * @throws IOException Si se produce un error de conexión.
	 */
	private void iniciaReuse(ObjectOutputStream oos, ObjectInputStream ois)
			throws IOException {
		TipoCaso tc = null;
		HashMap<String,Serializable> query = null;
		List<HashMap<String,Serializable>> casos = null;
		try {
			tc = (TipoCaso) ois.readObject();
			query = (HashMap<String,Serializable>) ois.readObject();
			casos = (List<HashMap<String,Serializable>>) ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Collection<CBRCase> result = lanzador.reuse(tc, casos, query);
		List<HashMap<String,Serializable>> casosH = new ArrayList<HashMap<String,Serializable>>(casos.size());
		for (CBRCase caso : result) {
			casosH.add(RellenadorClases.rellenarHash(tc, caso));
		}
		oos.writeObject(casosH);
	}

	/**Inicia la fase de retrieval a petición del cliente.
	 * @param oos Outputstream de este servlet.
	 * @param ois Inputstream de este servlet.
	 * @throws IOException Si se produce un error de conexión con el cliente.
	 */
	private void iniciaRetrieval(ObjectOutputStream oos, ObjectInputStream ois)
			throws IOException {
		TipoCaso tc = null;
		HashMap<String,Serializable> query = null;
		try {
			tc = (TipoCaso) ois.readObject();
			query = (HashMap<String,Serializable>) ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Collection<CBRCase> casos = lanzador.retrieve(tc, query);
		List<HashMap<String,Serializable>> casosH = new ArrayList<HashMap<String,Serializable>>(casos.size());
		for (CBRCase caso : casos) {
			casosH.add(RellenadorClases.rellenarHash(tc, caso));
		}
		oos.writeObject(casosH);
	}

}
