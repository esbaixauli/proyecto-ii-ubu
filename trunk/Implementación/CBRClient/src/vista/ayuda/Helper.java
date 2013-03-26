package vista.ayuda;


import java.net.URL;
import javax.help.HelpSet;
import javax.help.JHelp;
import javax.swing.JFrame;

/**
 *
 *
 */
public class Helper {
    
    public static void getHelp (String topicID) {
      JHelp helpViewer = null;
      try {
         // Obtengo el classloader de la clase.
	 ClassLoader cl = Helper.class.getClassLoader();
         // Creo una URL que referencia el helpset 
	 URL url = HelpSet.findHelpSet(cl, "res/jhelpset.hs");
         // Crea un helper a partir de dicha url
         helpViewer = new JHelp(new HelpSet(cl, url));
         // Establece el punto de inicio de la ayuda.
         helpViewer.setCurrentID("Simple.Introduction");
        } catch (Exception e) {}

      // Creo un frame y lo asigno el viewer
      JFrame frame = new JFrame();
      frame.setSize(500,500);
      // Add the created helpViewer to it.
      frame.getContentPane().add(helpViewer);
      // Establecemos la operación de cierre.
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      // Lo hacemos visible
      frame.setVisible(true);
    }
    
}
