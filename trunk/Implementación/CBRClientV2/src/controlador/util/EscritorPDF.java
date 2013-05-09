package controlador.util;

import java.awt.Color;
import java.awt.Image;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class EscritorPDF {
	  
	/**
	 * La fuente para los titulos.
	 */
	private static Font ftitulo = new Font(Font.FontFamily.TIMES_ROMAN, 18,
		      Font.BOLD);
	
	/** Crea un fichero pdf y lo deja abierto para que se introduzcan datos.
	 * @param ruta Ruta donde se crea el pdf.
	 * @param nombre Nombre del fichero
	 * @return El documento abierto para que se continue su edición.
	 * @throws FileNotFoundException Si no se puede escribir en la ruta.
	 * @throws DocumentException Si ocurre un error al editar el fichero.
	 */
	public static Document comenzarPDF(String ruta, String nombre) throws FileNotFoundException, DocumentException{
		  Document document = new Document();
	      PdfWriter.getInstance(document, new FileOutputStream(ruta));
	      document.open();
	      document.addTitle(nombre);
	      Paragraph titulo = new Paragraph();
	      addLineaEnBlanco(titulo, 1);
	      titulo.add(new Paragraph(nombre,ftitulo));
	      document.add(titulo);
	      return document;
	}
	
	
	/** Añade texto a un documento pdf abierto.
	 * @param document El documento al que añadir texto.
	 * @param texto La cadena de texto a añadir.
	 * @throws DocumentException Si ocurre un error al editar el fichero.
	 */
	public static void addTexto(Document document, String texto) throws DocumentException{
		document.add(new Paragraph(texto));
	}
	
	
	/** Añade una imagen a un pdf abierto.
	 * @param document El documento al que añadir una imagen.
	 * @param i La imagen a añadir.
	 * @throws IOException En caso de que la imagen pasada por parámetro no sea válida.
	 * @throws DocumentException Si ocurre un error al editar el fichero.
	 */
	public static void addImagen(Document document, Image i) throws IOException, DocumentException{
		addLineaEnBlanco(new Paragraph(), 1);
		//Convierto la imagen al formato de itext
		com.itextpdf.text.Image pdfI = com.itextpdf.text.Image.getInstance(i, Color.black) ;
		document.add(pdfI);
	}
	
	
	/** Termina el documento y lo cierra.
	 * @param document El documento.
	 */
	public static void terminarPDF(Document document){
		document.close();
	}
	
	/** Auxiliar. Añade lineas en blanco a un parrafo.
	 * @param p El parrafo al que añadir lineas.
	 * @param n El numero de lineas a añadir.
	 */
	private static void addLineaEnBlanco(Paragraph p, int n) {
	    for (int i = 0; i < n; i++) {
	      p.add(new Paragraph(" "));
	    }
	}
	 
}
