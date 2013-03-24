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

import servidorcbr.controlador.ControladorEstadisticas;
import servidorcbr.controlador.ControladorTipos;
import servidorcbr.modelo.Estadistica;
import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.Usuario;
import servidorcbr.modelo.excepciones.PersistenciaException;


/**
 * Servlet implementation class ServletEstadisticas
 */
@WebServlet("/ServletEstadisticas")
public class ServletEstadisticas extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletEstadisticas() {
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
			 if (tipo.equals("getDeUsuario") || tipo.equals("limpiar")) {
				Usuario u = null;
				TipoCaso tc=null;
				try {
					u = (Usuario) ois.readObject();
					tc = (TipoCaso) ois.readObject();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				if(tipo.equals("getDeUsuario")){
					Estadistica stat = ControladorEstadisticas.getEstadistica(u, tc);
					oos.writeObject(stat);
				}else{
					ControladorEstadisticas.limpiarEstadistica(u,tc);
					oos.writeBoolean(true);
				}
			 }
		} catch (PersistenciaException ex) {
			ex.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		oos.close();
		sos.close();
	}

}
