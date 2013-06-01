package servidorcbr.servlets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import servidorcbr.controlador.ControladorCasos;
import servidorcbr.controlador.ControladorTipos;
import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.Usuario;
import servidorcbr.modelo.excepciones.PersistenciaException;


/**
 * Servlet implementation class ServletCaso. Se encarga de gestionar las peticiones del cliente
 * relacionadas con inserción o recuperación de casos.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 */
@WebServlet("/ServletCaso")
public class ServletCaso extends HttpServlet {
	
	/**
	 * Requerido por la interfaz Serializable (implementada por HttpServlet).
	 */
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletCaso() {
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
		try {
			String tipo = null;
			try {
				tipo = (String) ois.readObject();
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
			if (tipo.equals("putCasos")) {
				putCasos(oos, ois);
			} else if (tipo.equals("getCasos")) {
				getCasos(oos, ois);
			}
		} catch (PersistenciaException ex) {
			ex.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		oos.close();
		sos.close();
	}

	/**
	 * Recupera los tipos de caso asociados a un usuario. 
	 * @param oos El ObjectOutputStream al que escribir la lista de casos.
	 * @param ois El ObjectInputStream del que leer el usuario.
	 * @throws IOException Si hay un error de conexión.
	 * @throws PersistenciaException Si hay un error en la capa de persistencia.
	 */
	private void getCasos(ObjectOutputStream oos, ObjectInputStream ois)
			throws IOException, PersistenciaException {
		Usuario u = null;
		try {
			u = (Usuario) ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		List<TipoCaso> l = ControladorTipos.getTipos(u);
		oos.writeObject(l);
	}

	/**
	 * Almacena una lista de casos en Hbase.
	 * @param oos ObjectOutputStream al que escribir si la operación ha tenido éxito o no.
	 * @param ois ObjectInputStream del que leer el tipo de caso y la lista de casos.
	 * @throws IOException Si hay un error de conexión.
	 * @throws PersistenciaException Si hay un error de persistencia.
	 */
	private void putCasos(ObjectOutputStream oos, ObjectInputStream ois)
			throws IOException, PersistenciaException {
		TipoCaso tc = null;
		List<HashMap<String,Serializable>> casos = null;
		try {
			tc = (TipoCaso) ois.readObject();
			casos = (List<HashMap<String,Serializable>>) ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		oos.writeBoolean(ControladorCasos.putCasos(tc, casos));
	}

}
