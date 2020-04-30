package assignment1;

//Importing Libraries:
import java.util.Objects;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

//Defining the class:
public class Busbar
{
	public ArrayList<Traverse_Node> toBusbar1=new ArrayList<Traverse_Node>();
	public ArrayList<Traverse_Node> toBusbar2=new ArrayList<Traverse_Node>();
	public ArrayList<Traverse_Node> toBusbar3=new ArrayList<Traverse_Node>();
	public ArrayList<Traverse_Node> toBusbar4=new ArrayList<Traverse_Node>();
	public ArrayList<Traverse_Node> toBusbar6=new ArrayList<Traverse_Node>();

	public ArrayList<ArrayList<Traverse_Node>> toBusbars= new ArrayList<ArrayList<Traverse_Node>>();

	public int index;
	public String ConductingEquipID;
	public String ConnectingEqID;
	
	
	//Constructor
	public Busbar(int id, String CondEquipmentID, String ConnNodeID) 
	{
		
		toBusbars.add(toBusbar1);
		toBusbars.add(toBusbar2);
		toBusbars.add(toBusbar3);
		toBusbars.add(toBusbar4);
		toBusbars.add(toBusbar6);
		
		index=id;
		ConductingEquipID=CondEquipmentID;
		ConnectingEqID=ConnNodeID;

	}	
}	