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
		{"usernotfound","User not found or wrong password."},
		{"port","Port:"},
		
		{"managecasetypes","Manage case types"},
		{"manageusers","Manage users"},
		{"defaultcycle","Default CBR cycle"},
		{"configuredcycle","Configured CBR cycle"},
		{"stats","Statistics"},
		{"exit","Exit"},
		{"mainmenu","Main Menu"},
		
		{"login","Log in"}
	};
}
