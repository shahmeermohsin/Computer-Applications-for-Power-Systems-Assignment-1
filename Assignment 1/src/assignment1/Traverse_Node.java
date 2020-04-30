package assignment1;


//Importing Libraries:
import java.util.Objects;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Traverse_Node 
{
	
	//Node Type of the Node
	String Nodetype;	
	//Name of the Node:
	String name;
	//ID of the Node:
	String ID;
	//Type of conducting equipment:
	String CE_type;		
	//List of the terminals (empty for node of type terminal):
	ArrayList<String> terminalList=new ArrayList<String>();	
	//Number of attached Terminals:
	int num_attachTerm;	
	//0 if the node has been visited by the traversal algorithm:
	int traversal_flag;		



}