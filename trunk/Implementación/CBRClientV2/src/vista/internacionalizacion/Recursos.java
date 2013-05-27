package vista.internacionalizacion;

import java.util.ListResourceBundle;

/** Bundle de recursos para el idioma por defecto (En inglés).
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
public class Recursos extends ListResourceBundle {

	@Override
	protected Object[][] getContents() {
		return contents;
	}
	/**
	 * Cadenas de internacionalización del bundle.
	 */
	static final Object[][] contents = {
		{"name", "Username:"}, 
		{"password", "Password:"},
		{"usertype", "User type:"},
		{"basicuser", "Basic user"},
		{"advuser", "Advanced user"},
		{"administrator", "Administrator"},
		{"loadfromfile","Load from file"},
		{"manualinsert","Manual insert"},
		
		{"open","Open"},
		{"exporterror","Failed to export to selected path."},
		{"emptylogin","Username and password may not be empty."},
		{"connecterror","Error while connecting to server."},
		{"moderror","Error while modifying."},
		{"operror","Error during the operation"},
		{"filenotfounderror","Error: File not found"},
		{"inserterror","Error while inserting. Check the connection status and that the case name is not repeated."},
		{"usernotfound","User not found or wrong password."},
		{"notconfigured","Inserting a case type is not possible until its methods have been defined."},
		{"executeCBR","Execute cycle"},
		
		{"loadedcases","Cases loaded:"},
		
		{"port","Port:"},
		{"admin","Administration"},
		
		{"managecasetypes","Manage case types"},
		{"insertcases","Insert cases"},
		{"manageusers","Manage users"},
		{"defaultcycle","Default CBR cycle"},
		{"configuredcycle","Configured CBR cycle"},
		{"stats","Statistics"},
		{"exit","Exit"},
		{"logout","Log out"},
		{"mainmenu","Main Menu"},
		{"nocasetypes","No case types available."},
		{"availablecasetypes","Available case types"},
		{"casestoretrieve","Number of cases to retrieve"},
		
		{"seecasetype","Examine case type"},
		{"newcasetype","New case type"},
		{"delcasetype","Delete the selected case type"},

		{"availableusers","Available users"},
		{"nousers","No users available"},
		
		{"seeuser","Examine the selected user."},
		{"newuser","New user"},
		{"deluser","Delete the selected user."},
		{"moduser","Modify user"},
		
		{"noselection","No item has been selected."},
		{"userdelsure","Are you sure you want to delete the selected user?"},
		{"rootdeletion","For security reasons, you are not allowed to delete user 'root'."},
		{"delsure","Are you sure you want to delete the selected case type? Case data will be discarded."},
		{"opsuccess","Operation completed successfully."},
		{"opunsuccess","The operation couldn't be completed."},
		{"delunsuccesscause","The case type may have already been deleted by another user."},
		
		{"repeatedatt","The name of each attribute may not be repeated."},
		
		{"attribute","Attribute"},
		{"atname","Name"},
		{"atweight","Weight"},
		{"attype","Type"},
		{"atmetric","Metric"},
		{"atmetricparam","Metric parameter"},
		{"attpredicates","Attribute predicates"},
		
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
		{"none","None"},

		{"configuremethod","Configure method"},
		{"configure","Configure"},
		{"default","Default"},

		{"attributethresholds","Attribute thresholds"},
		{"negativethreshold","Threshold values can not be negative."},
		{"emptyfield","No fields can remain empty"},
		
		{"login","Log in"},
		
		{"own","Own"},
		{"byuserandcase","By user and case"},
		{"global","Global"},
		
		{"report","Stats Report"},
		{"totalexec","Total CBR executions"},
		{"avgquality","Average quality of solution"},
		{"latestquality","Latest execution quality"},
		{"lastexec","Latest execution date"},
		{"successexec","Successful (>50% quality) solutions"},
		{"invalidexec","Unusable (0% quality) solutions"},
		{"pctgesuccess","Percentage of successful"},
		{"pctgeinvalid","Percentage of unusable"},
		{"seeall","--Global--"},
		{"wishtodelete","Do you wish to clean the stats for user:"},
		{"andcase"," and case type:"},
		
		{"method","Method"},
		
		{"sucshort","Successful"},
		{"invshort","Unusable"},
		{"other","Other"},
		
		{"qualitychart","Quality chart"},
		{"avgvslatest","Latest vs Avg"},
		{"quickstats","Case information"},
		{"stepbystep","Step by step"},	
		{"export","Export"},
		{"casebases","Casebases"},
		
		{"file","File"},
		{"help","Help"},
		{"about","About..."},
		
		{"searchfilter","Filter:"},
		{"filtererror","Can't filter by the selected text string. The search contains"+
		" invalid characters of metacharacters incorrectly used."},
		{"helperror","Error while locating help. The requested help page is not available."},
		
		{"ubu","Burgos University. Computer Engineering. Computer systems"},
		{"authors","Authors"},
		{"tutors","Tutors"},
		
		{"source","Source"},
		{"destination","Destination"},
		{"delete","Delete"},
		
		
		{"numeric","Numérics"},
		{"combinebyavg","Combine by average"},
		{"combinebymode","Combine by mode"},
		{"combine","Combine results"},
		{"nocases","No cases found for this query."},
		{"qualityboundaries","META_QUALITY must be a value between 0 and 100."},
		{"continue","Continue"},
		
		{"executionreport","Execution report"},
		{"defaultmethods","Default methods"}
	};
}
