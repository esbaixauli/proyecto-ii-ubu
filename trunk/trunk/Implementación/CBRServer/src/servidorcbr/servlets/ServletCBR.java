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
 * Servlet implementation class ServletCBR
 */
@WebServlet("/ServletCBR")
public class ServletCBR extends HttpServlet {
	private static final long serialVersionUID = 1L;
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
		} else if (tipo.equals("retain")) {
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
				System.out.println("Lanzamos retain");
				boolean result = ControladorCasos.retain(tc, caso);
				System.out.println("Result: "+result);
				if (result) {
					ControladorEstadisticas.updateEstadistica(u, tc, (Integer) caso.get("META_QUALITY"));
					System.out.println("Estadisticas guardadas");
				}
				oos.writeBoolean(result);
			} catch (PersistenciaException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
		}else if(tipo.equals("completo")){
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
		oos.close();
		sos.close();
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


}
