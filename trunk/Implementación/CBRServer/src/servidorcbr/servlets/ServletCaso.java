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

import servidorcbr.controlador.ControladorCasos;
import servidorcbr.controlador.ControladorTipos;
import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.Usuario;
import servidorcbr.modelo.excepciones.PersistenciaException;


/**
 * Servlet implementation class ServletTipo
 */
@WebServlet("/ServletCaso")
public class ServletCaso extends HttpServlet {
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
				TipoCaso tc = null;
				List<HashMap<String,Object>> casos = null;
				try {
					tc = (TipoCaso) ois.readObject();
					casos = (List<HashMap<String,Object>>) ois.readObject();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				oos.writeBoolean(ControladorCasos.putCasos(tc, casos));
			} else if (tipo.equals("getCasos")) {
				Usuario u = null;
				try {
					u = (Usuario) ois.readObject();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				List<TipoCaso> l = ControladorTipos.getTipos(u);
				oos.writeObject(l);
			}
		} catch (PersistenciaException ex) {
			ex.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		oos.close();
		sos.close();
	}

}
