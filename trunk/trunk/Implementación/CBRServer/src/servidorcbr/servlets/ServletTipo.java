package servidorcbr.servlets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import servidorcbr.controlador.ControladorTipos;
import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.Usuario;
import servidorcbr.modelo.excepciones.PersistenciaException;


/**
 * Servlet implementation class ServletTipo. Se encarga de gestionar las peticiones del cliente
 * relacionadas con la gestión de tipos de caso.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 */
@WebServlet("/ServletTipo")
public class ServletTipo extends HttpServlet {

	/**
	 * Requerido por la interfaz Serializable (implementada por HttpServlet). 
	 */
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletTipo() {
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
			if (tipo.equals("getTipos")) {
				getTipos(oos);
			} else if (tipo.equals("getTiposU")) {
				getTiposU(oos, ois);
			} else if (tipo.equals("newTipo")) {
				addTipo(oos, ois);
			} else if (tipo.equals("removeTipo")) {
				removeTipo(oos, ois);
			} else if(tipo.equals("modTipo")){
				modTipo(oos, ois);
			}
		} catch (PersistenciaException ex) {
			ex.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		oos.close();
		sos.close();
	}

	/**
	 * Modifica un tipo de caso a petición del cliente.
	 * @param oos ObjectOutputStream al que escribir si la operación ha tenido éxito o no.
	 * @param ois ObjectInputStream del que leer el tipo de caso modificado.
	 * @throws IOException Si hay un error de conexión.
	 * @throws PersistenciaException Si hay un error de persistencia.
	 */
	private void modTipo(ObjectOutputStream oos, ObjectInputStream ois)
			throws IOException, PersistenciaException {
		TipoCaso tc = null;
		try {
			tc = (TipoCaso) ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		boolean exito = ControladorTipos.modifyTipo(tc);
		oos.writeBoolean(exito);
	}

	/**
	 * Elimina un tipo de caso a petición del cliente.
	 * @param oos ObjectOutputStream al que escribir si la operación ha tenido éxito o no.
	 * @param ois ObjectInputStream del que leer el tipo de caso a eliminar.
	 * @throws IOException Si ha habido un error de conexión.
	 * @throws PersistenciaException Si ha habido un error de persistencia.
	 */
	private void removeTipo(ObjectOutputStream oos, ObjectInputStream ois)
			throws IOException, PersistenciaException {
		TipoCaso tc = null;
		try {
			tc = (TipoCaso) ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		boolean exito = ControladorTipos.removeTipo(tc);
		oos.writeBoolean(exito);
	}

	/**
	 * Añade un nuevo tipo de caso a petición del cliente.
	 * @param oos ObjectOutputStream al que escribir si la operación ha tenido éxito o no.
	 * @param ois ObjectInputStream del que leer el nuevo tipo de caso. 
	 * @throws IOException Si ha habido un error de conexión.
	 * @throws PersistenciaException Si ha habido un error de persistencia.
	 */
	private void addTipo(ObjectOutputStream oos, ObjectInputStream ois)
			throws IOException, PersistenciaException {
		TipoCaso tc = null;
		try {
			tc = (TipoCaso) ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		boolean exito = ControladorTipos.addTipo(tc);
		oos.writeBoolean(exito);
	}

	/**
	 * Envía al cliente todos los tipos de caso asociados a un usuario.
	 * @param oos ObjectOutputStream al que escribir los tipos de caso.
	 * @param ois ObjectInputStream del que leer el usuario.
	 * @throws IOException Si se ha producido un error de conexión.
	 * @throws PersistenciaException Si se ha producido un error de persistencia.
	 */
	private void getTiposU(ObjectOutputStream oos, ObjectInputStream ois)
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
	 * Envía al cliente una lista con todos los tipos de caso presentes en el sistema.
	 * @param oos ObjectOutputStream al que escribir la lista.
	 * @throws PersistenciaException Si se ha producido un error de persistencia.
	 * @throws IOException Si se ha producido un error de conexión.
	 */
	private void getTipos(ObjectOutputStream oos) throws PersistenciaException,
			IOException {
		List<TipoCaso> l = ControladorTipos.getTipos();
		oos.writeObject(l);
	}

}
