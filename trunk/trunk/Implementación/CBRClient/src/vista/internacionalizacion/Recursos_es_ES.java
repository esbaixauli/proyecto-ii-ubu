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
		{"moderror","Error al modificar."},
		{"inserterror","Error al insertar. Compruebe la conexión y que el nombre del tipo de caso esté repetido."},
		{"usernotfound","Usuario no encontrado o contraseña incorrecta."},
		{"notconfigured","No es posible insertar un tipo de caso hasta que no se hayan definido sus técnicas."},
		{"port","Puerto:"},
		{"admin","Administración"},
		{"executeCBR","Ejecutar ciclo"},
		
		{"managecasetypes","Gestionar tipos de caso"},
		{"insertcases","Insertar casos"},
		{"manageusers","Gestionar usuarios"},
		{"defaultcycle","Ciclo CBR por defecto"},
		{"configuredcycle","Ciclo CBR configurado"},
		{"stats","Estadísticas"},
		{"exit","Salir"},
		{"mainmenu","Menú principal"},
		{"nocasetypes","No hay tipos de caso disponibles."},
		{"availablecasetypes","Tipos de caso disponibles"},
		
		{"seecasetype","Examinar tipo de caso."},
		{"newcasetype","Nuevo tipo de caso."},
		{"delcasetype","Eliminar el tipo de caso seleccionado."},
		
		{"noselection","No se ha escogido ningún tipo de caso."},
		{"delsure","¿Está seguro de que desea borrar el tipo de caso? Se perderan los casos relacionados."},
		{"opsuccess","Operación completada con éxito."},
		{"opunsuccess","No se pudo completar la operación."},
		{"delunsuccesscause","Es posible que el tipo de caso haya sido borrado por otro usuario."},
		
		{"repeatedatt","El nombre de cada atributo no puede repetirse."},
		
		{"attribute","Atributo"},
		{"atname","Nombre"},
		{"atweight","Peso"},
		{"attype","Tipo"},
		{"atmetric","Métrica"},
		{"atmetricparam","Parámetro de la métrica"},
		{"managemethods","Gestionar técnicas"},
		
		{"emptyatt","Por favor, rellene todos los campos de cada atributo."},
		{"incorrectformat","Formato incorrecto en el nombre. Sólo se permiten Letras [A-Z][a-z] y dígitos[0-9]"},
		{"integer","Número entero"},
		{"string","Cadena de texto"},
		{"decimal","Número decimal"},
		{"exitnosave","¿Salir sin guardar?"},
		{"problem","Atributos del problema"},
		{"solution","Atributos de la solución"},
		
		{"equal","Igualdad"},
		{"equalignorecase","Igualdad (Ignora Mayusculas)"},
		{"substring","Subcadena más larga"},
		{"interval","Intervalo"},
		{"threshold","Umbral"},

		{"enterparam","Esta métrica requiere un parámetro numérico. Por favor, indíquelo debajo."},
		
		{"retrieval","Recuperación"},
		{"reuse","Reutilización"},
		{"revise","Revisión"},
		{"retain","Retención"},
		
		{"ok","Aceptar"},
		{"cancel","Cancelar"},

		{"configuremethod","Configurar técnica"},
		{"configure","Configurar"},
		{"default","Por defecto"},
		
		{"attributethresholds","Umbrales para los atributos"},
		{"emptyfield","No puede quedar ningún campo vacío"},

		{"login","Acceder"}
	};
}
