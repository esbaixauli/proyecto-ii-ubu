package vista.internacionalizacion;

import java.util.ListResourceBundle;

public class Recursos_es_ES extends ListResourceBundle {

	@Override
	protected Object[][] getContents() {
		return contents;
	}
	static final Object[][] contents = {
		{"name", "Usuario:"}, 
		{"password", "Contraseña:"},
		{"emptylogin","El nombre de usuario y la contraseña no pueden estar vacíos."},
		{"connecterror","Error al conectar con el servidor."},
		{"usernotfound","Usuario no encontrado o contraseña incorrecta."},
		{"port","Puerto:"},
		
		{"managecasetypes","Gestionar tipos de caso"},
		{"manageusers","Gestionar usuarios"},
		{"defaultcycle","Ciclo CBR por defecto"},
		{"configuredcycle","Ciclo CBR configurado"},
		{"stats","Estadísticas"},
		{"exit","Salir"},
		{"mainmenu","Menú principal"},
		{"nocasetypes","No hay tipos de caso disponibles."},
		{"availablecasetypes","Tipos de caso disponibles"},
		
		{"newcasetype","Nuevo tipo de caso."},
		{"delcasetype","Eliminar el tipo de caso seleccionado."},
		
		{"noselection","No se ha escogido ningún tipo de caso."},
		{"delsure","¿Está seguro de que desea borrar el tipo de caso? Se perderan los casos relacionados."},
		{"opsuccess","Operación completada con éxito."},
		{"opunsuccess","No se pudo completar la operación."},
		{"delunsuccesscause","Es posible que el tipo de caso haya sido borrado por otro usuario."},
		
		{"atname","Nombre"},
		{"atweight","Peso"},
		{"attype","Tipo"},
		{"atmetric","Métrica"},
		{"atmetricparam","Parámetro de la métrica"},
		
		{"emptyatt","Por favor, rellene todos los campos de cada atributo."},
		{"incorrectformat","Formato incorrecto. Sólo se permiten Letras [A-Z][a-z] y dígitos[0-9]"},
		{"integer","Número entero"},
		{"string","Cadena de texto"},
		{"decimal","Número decimal"},
		
		{"equal","Igualdad"},
		{"equalignorecase","Igualdad (Ignora Mayusculas)"},
		{"substring","Subcadena más larga"},
		{"interval","Intervalo"},
		{"threshold","Umbral"},

		{"enterparam","Esta métrica requiere un parámetro numérico. Por favor, indíquelo debajo."},
		
		{"login","Acceder"}
	};
}
