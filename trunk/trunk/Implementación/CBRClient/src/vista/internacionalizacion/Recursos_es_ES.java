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
		
		{"login","Acceder"}
	};
}
