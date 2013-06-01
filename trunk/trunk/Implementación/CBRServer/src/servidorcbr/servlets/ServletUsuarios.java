package servidorcbr.servlets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import servidorcbr.controlador.ControladorUsuarios;
import servidorcbr.modelo.Usuario;
import servidorcbr.modelo.excepciones.PersistenciaException;

/**
 * Servlet implementation class ServletUsuarios. Se encarga de gestionar las peticiones del cliente
 * relacionadas con la gestión de usuarios.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 */
@WebServlet("/ServletUsuarios")
public class ServletUsuarios extends HttpServlet {
	
	/**
	 * Requerido por la interfaz Serializable (implementada por HttpServlet).
	 */
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletUsuarios() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
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
			if (tipo.equals("getUsuarios")) {
				getUsuarios(oos);
			} else if (tipo.equals("removeUsuario")) {
				removeUsuario(oos, ois);
			} else if (tipo.equals("newUsuario")) {
				addUsuario(oos, ois);
			} else if (tipo.equals("modUsuario")) {
				modUsuario(oos, ois);
			}
		} catch (PersistenciaException ex) {
			ex.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		oos.close();
		sos.close();
	}

	/**
	 * Modifica un usuario a petición del cliente.
	 * @param oos ObjectOutputStream al que escribir si la operación ha tenido éxito o no.
	 * @param ois ObjectInputStream del que leer el usuario modificado y la lista de casos asociada.
	 * @throws IOException Si se ha producido un error de conexión.
	 * @throws PersistenciaException Si se ha producido un error de persistencia.
	 */
	private void modUsuario(ObjectOutputStream oos, ObjectInputStream ois)
			throws IOException, PersistenciaException {
		Usuario u = null;
		List<String> casos=null;
		try {
			u = (Usuario) ois.readObject();
			casos = (List<String>) ois.readObject();
		} catch (ClassNotFoundException e) { }
		boolean exito = ControladorUsuarios.modUsuario(u,casos);
		oos.writeObject(exito);
	}

	/**
	 * Añade un usuario a petición del cliente.
	 * @param oos ObjectOutputStream al que escribir si la operación ha tenido éxito o no.
	 * @param ois ObjectInputStream del que leer el usuario y la lista de casos a asignar.
	 * @throws IOException Si se ha producido un error de conexión.
	 * @throws PersistenciaException Si se ha producido un error de persistencia.
	 */
	private void addUsuario(ObjectOutputStream oos, ObjectInputStream ois)
			throws IOException, PersistenciaException {
		Usuario u = null;
		List<String> casos=null;
		try {
			u = (Usuario) ois.readObject();
			casos = (List<String>) ois.readObject();
		} catch (ClassNotFoundException e) { }
		boolean exito = ControladorUsuarios.addUsuario(u,casos);
		oos.writeObject(exito);
	}

	/**
	 * Elimina un usuario del sistema a petición del cliente.
	 * @param oos ObjectOutputStream al que escribir si la operación ha tenido éxito o no.
	 * @param ois ObjectInputStream del que leer el usuario a eliminar.
	 * @throws IOException Si se ha producido un error de conexión.
	 * @throws PersistenciaException Si se ha producido un error de persistencia.
	 */
	private void removeUsuario(ObjectOutputStream oos, ObjectInputStream ois)
			throws IOException, PersistenciaException {
		Usuario u = null;
		try {
			u = (Usuario) ois.readObject();
		} catch (ClassNotFoundException e) { }
		boolean exito = ControladorUsuarios.removeUsuario(u);
		oos.writeObject(exito);
	}

	/**
	 * Envía al cliente una lista con todos los usuarios del sistema.
	 * @param oos ObjectOutputStream al que escribir la lista.
	 * @throws PersistenciaException Si se ha producido un error de persistencia.
	 * @throws IOException Si se ha producido un error de conexión.
	 */
	private void getUsuarios(ObjectOutputStream oos)
			throws PersistenciaException, IOException {
		HashMap<String,Usuario> l = ControladorUsuarios.getUsuarios();
		oos.writeObject(l);
	}

}
