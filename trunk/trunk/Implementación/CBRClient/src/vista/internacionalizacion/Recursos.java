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
		{"nocasetypes","No case types available."},
		{"availablecasetypes","Available case types"},
		
		{"newcasetype","New case type."},
		{"delcasetype","Delete selected case type."},

		{"noselection","No case type has been selected."},
		{"delsure","Are you sure you want to delete the selected case type? Case data will be discarded."},
		{"opsuccess","Operation completed successfully."},
		{"opunsuccess","The operation couldn't be completed."},
		{"delunsuccesscause","The case type may have already been deleted by another user."},
		
		{"attribute","Attribute"},
		{"atname","Name"},
		{"atweight","Weight"},
		{"attype","Type"},
		{"atmetric","Metric"},
		{"atmetricparam","Metric parameter"},
		
		{"integer","Integer"},
		{"string","Text string"},
		{"decimal","Decimal number"},

		{"exitnosave","Exit without saving?"},
		{"problem","Problem attributes"},
		{"solution","Solution attributes"},
		{"managemethods","Manage methods"},
		
		{"incorrectformat","Incorrect format in case name. Only letters [A-Z][a-z] and digits [0-9] are allowed."},
		{"emptyatt","Please fill in every attribute field."},
		
		{"equal","Equality"},
		{"equalignorecase","Equality (Ignora Mayusculas)"},
		{"substring","Longest matching substring"},
		{"interval","Interval"},
		{"threshold","Threshold"},
		{"enterparam","This metric requires a numeric parameter. Please indicate it below."},
		
		{"retrieval","Retrieval"},
		{"reuse","Reuse"},
		{"revise","Revise"},
		{"retain","Retain"},
		
		{"ok","Ok"},
		{"cancel","Cancel"},

		{"configuremethod","Configure method"},
		{"default","Default"},
		
		{"login","Log in"}
	};
}
