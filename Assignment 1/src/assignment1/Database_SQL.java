package assignment1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class Database_SQL {
	
	

	
	static Connection conn=null;
	static Statement stmt = null;
	
	//The following function connects to SQL:
	public static void getconnection() 
	{
				
		try 
		{
			System.out.println("Connecting to the DataBase:  ");
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/pow", "root", "password");
			stmt = DriverManager.getConnection("jdbc:mysql://localhost/pow","root", "password").createStatement();
		}
		catch (Exception exception) 
		{
			exception.printStackTrace();
		}
		
	}
	
	//This method creates a SQL DB:
	public static void create_DB (String Name_database) 
	{
		
		try 
		{
			// Execute a query to create database
			System.out.println("Creating database "+ Name_database);
			stmt = conn.createStatement();
			stmt.executeUpdate("create database if not exists " + Name_database);
			System.out.println("Database " + Name_database + " created successfully...");			
			System.out.println("Database used : " + Name_database);
			
		} 
		catch (SQLException exc) 
		{
			exc.printStackTrace();
		}
	}
	
	//This function generates a table in the selected SQL DB:
	public static void make_table (String tablename, String key, ArrayList<String> CIMRequiredData, ArrayList<String> RDFRequiredData) 
	{
		//Add the cim and rdf based required data in one arraylist that represents the attributes of the table	
		ArrayList<String> data =new ArrayList<String>(); 
		data.addAll(CIMRequiredData);
		data.addAll(RDFRequiredData);
		System.out.println("Making table " + tablename + ":");

			try 
			{
				stmt = DriverManager.getConnection("jdbc:mysql://localhost/pow", "root", "password").createStatement();
				String query = "create table if not exists " + tablename + "("; // Create table 
				
				for (String temp : data) 
				{
					query+= temp+" varchar(255), ";
				}
							
				//Execute the SQL query
				stmt.executeUpdate(query+= "primary key (" + key + "))");
				ResultSet rs = stmt.executeQuery("select * from "+tablename);
				
				int i=0;
				while (rs.next()) 
				{
					//System.out.println(data);
					if(tablename=="BaseVoltage" ||tablename=="ConnectivityNode" ||tablename=="BusbarSection")
				    System.out.println(rs.getString(1)+"                 "+rs.getString(2)); 
					
					if(tablename=="PowerTransformer" ||tablename=="Substation" ||tablename=="RatioTapChanger"
							||tablename=="RegulatingControl" )
					System.out.println(rs.getString(1)+"                 "+rs.getString(2)+"                 "+rs.getString(3)); 
					
					if(tablename=="LinearShuntCompensator" ||tablename=="Terminal" ||tablename=="VoltageLevel")
					System.out.println(rs.getString(1)+"                 "+rs.getString(2)+"                 "+rs.getString(3)+"                 "+rs.getString(4));

					if(tablename=="Breaker" ||tablename=="GeneratingUnit"  )
					System.out.println(rs.getString(1)+"                 "+rs.getString(2)+"                 "+rs.getString(3)+"                 "+rs.getString(4)+"                 "+rs.getString(5));
					if(tablename=="EnergyConsumer")
					System.out.println(rs.getString(1)+"                 "+rs.getString(2)+"                 "+rs.getString(3)+"                 "+rs.getString(4)+"                 "+rs.getString(5)+"                 "+rs.getString(6));
				    
					if(tablename=="PowerTransformerEnd" ||tablename=="ACLineSegment")
				    System.out.println(rs.getString(1)+"                 "+rs.getString(2)+"                 "+rs.getString(3)+"                 "+rs.getString(4)+"                 "+rs.getString(5)+"                 "+rs.getString(6)
				     +rs.getString(7));
					
					if(tablename=="SynchronousMachine")
					    System.out.println(rs.getString(1)+"                 "+rs.getString(2)+"                 "+rs.getString(3)+"                 "+rs.getString(4)+"                 "+rs.getString(5)+"                 "+rs.getString(6)
					    +"                 "+rs.getString(7)+"                 "+rs.getString(8)+"                 "+rs.getString(9));

				}
				System.out.println("Table " + tablename + " successfully made");
				
			} 
			catch (SQLException exception) 
			{
				exception.printStackTrace();
			}

		
	}
	
	//Insert values to a table:
	public static void InsertValuesToTable (String tableName, ArrayList<String> attributelist, ArrayList<String> valuelist) 
	{
		System.out.println("Inserting values in table" + tableName + ":"); 
		String Query = "insert into " + tableName +"("; 
		
		try 
		 { 
			  stmt = DriverManager.getConnection("jdbc:mysql://localhost/pow", "root", "password").createStatement();
			  
			  for (int i=0; i<attributelist.size(); i++) 
			  { 
			      Query = Query +attributelist.get(i);
			  
			      if (i!=attributelist.size()-1) 
			      { 
				     Query+= ","; 
				  } 
			      if(i==attributelist.size()-1) 
			      { 
			    	 Query+= ")"; 
			      } 
			  }
			  
			  Query+= "values (";
			  
			  for (int j=0; j<valuelist.size(); j++) 
			  {
				  Query+= valuelist.get(j); 
				  if(j!=valuelist.size()-1) 
				  { 
					  Query+=","; 
					  } 
				  if (j==valuelist.size()-1) 
				  {
			      Query+= ")"; 
			  } 
			  }
			  
			  stmt.executeUpdate(Query); 
			  System.out.println("Table" + tableName +"has been updated successfully");
			  
			  } catch (SQLException e) { e.printStackTrace(); }

		 
	 }
	
	
	//Function to create a Traverse_Nodelist with all the Connectivity Node (CN) of the system and the needed data
	public static ArrayList<Traverse_Node> SelectCN() 
	{
	    
		 ArrayList<Traverse_Node> finalresult = new ArrayList<Traverse_Node>();
		//This list will to store the result
  		  ArrayList<Traverse_Node> result = new ArrayList<Traverse_Node>();		
	      
  		  try 
	      {
	    	//Create a SQL statement to select the Terminal elements of the SQL Database
			stmt = DriverManager.getConnection("jdbc:mysql://localhost/pow", "root", "password").createStatement();
			ResultSet rs = stmt.executeQuery("select ID, IdentifiedObject_name from ConnectivityNode");
			
			while (rs.next()==true) 
			{
				
				//The following object will store the data from the SQL Database i.e. name, ID and node type:
				Traverse_Node Node = new Traverse_Node();
				//Type of node:
				Node.Nodetype = "CN";
				//ID
				Node.ID = rs.getString("ID");
				//name:
				Node.name = rs.getString("IdentifiedObject_name");
				result.add(Node);
			}
			
			for (Traverse_Node Node : result) 
			{
				
				//For each node created with this method, create a SQL statement to add the terminals to the terminal list and update the number of terminal
				ResultSet rs2 = stmt.executeQuery("select ID from Terminal where Terminal_ConnectivityNode = " + "'" + Node.ID + "'");
				while (rs2.next()==true)
				{
				Node.terminalList.add(rs2.getString("ID"));
				Node.num_attachTerm = Node.terminalList.size();
			    }	
			}

		} 
	      catch (SQLException Exception) 
	      {
	    	  Exception.printStackTrace();
		  } 
  		  
	    return result;
	}

//This function creates a Traverse_Node list with all the Terminals (TE) of the system and the needed data:
public static ArrayList<Traverse_Node> SelectTE() 
{

    //The TE will be stored in the following list:
	ArrayList<Traverse_Node> TE = new ArrayList<Traverse_Node>();
	
	  //try-catch:
      try 
      { 
    	//SQL statement that will select the Terminal elements of the SQL Database:
		ResultSet result = DriverManager.getConnection("jdbc:mysql://localhost/pow", "root", "password").createStatement().executeQuery(""
				+ "select ID, IdentifiedObject_name from Terminal");
		while (result.next()==true) 
		{
			//Create an object of the type Traverse_Node to store the data from the SQL Database (name, ID, node type)
			Traverse_Node Node = new Traverse_Node();
			//The Node type is "TE" implying a terminal:
			Node.Nodetype = "TE";
			//The Node ID is "ID":
			Node.ID = result.getString("ID");
			//The name of the Node
			Node.name = result.getString("IdentifiedObject_name");
			TE.add(Node);
		}
	  } 
      catch (SQLException exception) 
      {
		exception.printStackTrace();
	  }  
	    return TE;	

}



//Function to make a list with all the Conducting Equipment (CE) of all CE types of the system and the needed data:
public static ArrayList<Traverse_Node> selectCE()
{


ArrayList<Traverse_Node> finalresult = new ArrayList<Traverse_Node>();
//Array that will have all the CE types:
ArrayList<String> types_CE = new ArrayList<String>();

types_CE.add("BusbarSection");
types_CE.add("LinearShuntCompensator");
types_CE.add("ACLineSegment");
types_CE.add("Breaker");
types_CE.add("PowerTransformer");
types_CE.add("SynchronousMachine");
types_CE.add("EnergyConsumer");
types_CE.add("GeneratingUnit");



//Loop on all the CE types
for (String typeCE : types_CE) 
{
ArrayList<Traverse_Node> result = new ArrayList<Traverse_Node>();
try 
  {
	stmt = conn.createStatement();
	//Create a SQL statement to select the Terminal elements of the SQL Database
	ResultSet rs =  stmt.executeQuery("select ID, IdentifiedObject_name from " + typeCE);
	
	while (rs.next()==true) 
	{
		
		//Create an object of the type Traverse_Node to store the data from the SQL Database (name, ID, node type)
		Traverse_Node Node = new Traverse_Node();
		//Type of Node
		Node.Nodetype = "CE";
		//Node Name
		Node.name = rs.getString("IdentifiedObject_name");
		//ID of CE:
		Node.ID = rs.getString("ID");
		//Determine the type of conductivity equipment:				
		Node.CE_type = typeCE;
	
		result.add(Node);
	}
	
	
	for (Traverse_Node Node : result) 
	{
		//For each node created with this method, create a SQL statement to add the terminals to the terminal list and update the number of terminals
		ResultSet rs1 = stmt.executeQuery("select ID from Terminal where Terminal_ConductingEquipment = " + "'" + Node.ID + "'");
		while (rs1.next()) 
		{	
          //Adding the terminals to the terminal list:
		  Node.terminalList.add(rs1.getString("ID"));
		  //Number of terminals update:
		  Node.num_attachTerm = Node.terminalList.size();
	    }
	}
	
  }
  
  catch (SQLException exception) 
  {
	  exception.printStackTrace();
  }   	

//Use the selectCEtype method to select the CE of a certain type
finalresult.addAll(result);
}

return finalresult;



}
	
	
	//Function to update the value of an attribute of a table 
	static void update(String table, String ID, String columnName, String value)
	{
		System.out.println("Updating values of the table. " + table);
		try 
		{
			
			//Update the SQL table with the value at the wanted column where the ID are the same
			DriverManager.getConnection("jdbc:mysql://localhost/pow", "root", "password").createStatement().executeUpdate(""
					+ "update " + table + " set " + columnName + " = " + value + "where ID = " + "'" + ID + "'");
			
		} 
		catch (SQLException exception)
		{
			exception.printStackTrace();
		}
				
	}
	
	//Check if a breaker is open or not
		public static boolean OpenBreaker (String ID)
		{
			try 
			{
				
				//Select the breaker by its ID
				//Execute the SQL query
				ResultSet resultset = DriverManager.getConnection("jdbc:mysql://localhost/pow", "root", "password").createStatement().executeQuery(""
						+ "select Switch_open  from Breaker where ID = '" + ID  + "'");
				while (resultset.next()==true) 
				{
					//Get the result of the SQL query
					if (resultset.getString("Switch_open").equals("false")) 
					{
						//Return false if the breaker is not open
						return false;
					}
				}

			} 
			
			catch (SQLException exception) 
			{
				exception.printStackTrace();
			}  
			return true;
		}
	
	//Function to mimic a SQL select where query:
		public static ArrayList<String[]> selectwhere(String table, String[] attributes, String condition, String value) 
		{
			String query = "select ";
			
			//Loop on all the attributes to select in the table
			for (int j = 0; j<attributes.length; j++) 
			{
				if (j != attributes.length - 1)  
				{
					query+=attributes[j] + ",";
				}
				
				if (j == attributes.length - 1) 
				{
					query+=attributes[j];
				} 
				
			}
			
			ArrayList<String[]> Final_Result = new ArrayList<String[]>();

		      try 
		      {
							
				//Get all the result of the SQL query
				ResultSet resultset = DriverManager.getConnection(""
				+ "jdbc:mysql://localhost/pow", "root", "password").createStatement().executeQuery(query+=" from " + table + " where " + condition + " = '" + value + "'");
				
				while (resultset.next()) 
				{
					int attributeslength=attributes.length;
					String[] result =  new String[attributeslength];
					
					//Loop on the required attributes
					for (int i = 0; i<attributeslength; i++) 
					{
						result[i] = resultset.getString(attributes[i]);
					}
					
					//Add results of loop to Final_Result
					Final_Result.add(result);
					
				}
				return Final_Result;
				
			} 
		      catch (SQLException Exception) 
		      {
		    	Exception.printStackTrace();
				return Final_Result;
			}  
			}  
			

			
	
	//Method to update a table using a SQL join statement
		static void updatejoin (String table1, String table2, String id1, String id2, String attribute1, String attribute2) 
		{
			System.out.println("Updating values in table " + table1 + " from " + table2);
			try 
			{
				//Execute the SQL query
				DriverManager.getConnection("jdbc:mysql://localhost/pow", "root", "password").createStatement().executeUpdate(""
						+ "update " + table1 +" t1 inner join " + table2 + " t2 on t1." + id1 + " = t2." + id2+ " set t1." + attribute1 + " = t2." + attribute2);			
				
			}
			catch(SQLException SQLException)
			{
				SQLException.printStackTrace();

			}
			System.out.println("The table," + table1 + "has been updated");
		}
		
		
	
}
	