package vista.internacionalizacion;

import java.util.ListResourceBundle;

public class Recursos_es_ES extends ListResourceBundle {

	@Override
	protected Object[][] getContents() {
		return contents;
	}
	static final Object[][] contents = {
		{"name", "Usuario:"}, 
		{"password", "Contrase�a:"},
		{"emptylogin","El nombre de usuario y la contrase�a no pueden estar vac�os."},
		{"connecterror","Error al conectar con el servidor."},
		{"usernotfound","Usuario no encontrado."},
		{"login","Acceder"}
	};
}
