package servidorcbr.servlets;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import servidorcbr.controlador.ControladorUsuarios;
import servidorcbr.modelo.Usuario;
import servidorcbr.modelo.excepciones.PersistenciaException;

@WebServlet("/ServletLogin")
public class ServletLogin extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	 protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
	      response.setContentType("application/x-java-serialized-object");
	      try{
	      InputStream inputStream = request.getInputStream();
	      ObjectInputStream input = new ObjectInputStream(inputStream);
	      String nombre = (String) input.readObject();
	      String password = (String) input.readObject();
	      Usuario u = ControladorUsuarios.login(nombre, password);
	      
	      OutputStream outputStream = response.getOutputStream();
	      ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
	      objectOutputStream.writeObject(u);
	      objectOutputStream.flush();
	      objectOutputStream.close();
	      
	      }catch(Exception ex){
	    	  ex.printStackTrace();
	      }
	      	
		}

}
