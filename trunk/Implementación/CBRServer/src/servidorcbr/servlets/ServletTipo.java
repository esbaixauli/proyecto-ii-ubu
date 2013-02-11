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
 * Servlet implementation class ServletTipo
 */
@WebServlet("/ServletTipo")
public class ServletTipo extends HttpServlet {
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
			if (request.getAttribute("tipo").equals("getTipos")) {
				List<TipoCaso> l = ControladorTipos.getTipos();
				oos.writeObject(l);
			} else if (request.getAttribute("tipo").equals("getTiposU")) {
				Usuario u = null;
				try {
					u = (Usuario) ois.readObject();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				List<TipoCaso> l = ControladorTipos.getTipos(u);
				oos.writeObject(l);
			} else if (request.getAttribute("tipo").equals("newTipo")) {
				TipoCaso tc = null;
				try {
					tc = (TipoCaso) ois.readObject();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				boolean exito = ControladorTipos.addTipo(tc);
				oos.writeBoolean(exito);
			} else if (request.getAttribute("tipo").equals("removeTipo")) {
				TipoCaso tc = null;
				try {
					tc = (TipoCaso) ois.readObject();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				boolean exito = ControladorTipos.removeTipo(tc);
				oos.writeBoolean(exito);
			}
		} catch (PersistenciaException ex) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		oos.close();
		sos.close();
	}

}
