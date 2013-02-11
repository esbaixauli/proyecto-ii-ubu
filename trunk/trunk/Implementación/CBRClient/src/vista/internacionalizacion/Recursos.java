package vista.internacionalizacion;

import java.util.ListResourceBundle;

public class Recursos extends ListResourceBundle {

	@Override
	protected Object[][] getContents() {
		return contents;
	}
	static final Object[][] contents = {
		{"name", "Username:"}, 
		{"password", "Password:"},
		{"emptylogin","Username and password may not be empty."},
		{"connecterror","Error while connecting to server."},
		{"usernotfound","User not found."},
		{"login","Log in"}
	};
}
