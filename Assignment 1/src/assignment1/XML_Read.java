package assignment1;

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
import org.w3c.dom.*;
import java.util.HashMap;
import java.io.File;


//The following class will read the XML file:
public class XML_Read 
{
	
	
	//The following method parses the EQ file once it is called in the main function:
		public static void parse_EQ (NodeList NodeList, String TableName, ArrayList<String> CIM_RequiredData, ArrayList<String[]> RDF_RequiredData)
		{
			//Loop on all the nodes of the node list
			for (int i = 0; i < NodeList.getLength(); i++) 
			{
				
				//Initializing an ArrayList for the CIM Required Data:
				ArrayList<String> attributes =new ArrayList<String>();
				//Adding the CIM required data to the attributes ArrayList:
				attributes.addAll(convertCimAttributes(CIM_RequiredData));
				//Adding the RDF required data to the attributes ArrayList:
				attributes.addAll(convertRdfAttributes(RDF_RequiredData));
				
				
				//Initializing an ArrayList for the CIM Required Values:
				ArrayList<String> values =new ArrayList<String>();
				//Adding the CIM required data to the values ArrayList:
				values.addAll(ExtractNode_CIM(NodeList.item(i), CIM_RequiredData));
				//Adding the RDF required data to the values ArrayList:
				values.addAll(ExtractNode_RDF(NodeList.item(i), RDF_RequiredData));
				//Inserting the Required Data and Corresponding Values to the SQL Table (tablename):
				Database_SQL.InsertValuesToTable(TableName, attributes, values);
				//SQLdatabase.InsertValuesToTable(TableName, attributes, values);
				//System.out.println(attributes);
				//System.out.println(values);
				System.out.print("\n");
			}
			
		}
		
		//The following method parses the SSH file once it is called in the main function:
		public static void parse_SSH (NodeList NodeList, String table, ArrayList<String> CIM_RequiredData, ArrayList<String[]> RDF_RequiredData)
		{
			//Loop on all the nodes of the node list
			for (int i = 0; i < NodeList.getLength(); i++) 
			{			
				//Initializing an ArrayList for the CIM Required Data:
				ArrayList<String> attributes =new ArrayList<String>();
				//Initializing an ArrayList for the CIM Required Values:
				ArrayList<String> values =new ArrayList<String>();
				//Adding the CIM required data to the attributes ArrayList:
				attributes.addAll(convertCimAttributes(CIM_RequiredData));
				//Adding the RDF required data to the attributes ArrayList:
				attributes.addAll(convertRdfAttributes(RDF_RequiredData));
				//Adding the CIM required data to the values ArrayList:
				values.addAll(ExtractNode_CIM(NodeList.item(i), CIM_RequiredData));
				//Adding the RDF required data to the values ArrayList:
				values.addAll(ExtractNode_RDF(NodeList.item(i), RDF_RequiredData));
				
						
				//Looping for every attribute that doesn't have a value in the the EQ file:
				for (int k = 0; k < values.size(); k++) 
				{
					if (values.get(k)!="null") 
					{
						//Updating the table with the new values from the SSH file:
						Database_SQL.update(table,((Element)NodeList.item(i)).getAttribute("rdf:about"), attributes.get(k), values.get(k));
					}
				}
				
				System.out.print("\n");
			}
			
		}
		
	//Function to extract the required data that is directly accessible from the CIM model
	public static ArrayList<String> ExtractNode_CIM (Node Node, ArrayList<String> RequiredData) 
	{
		
		//Array in which the extracted required data is stored:
		ArrayList<String> resultArray = new ArrayList<String>();
			
		//Loop on the required data within a certain Node:
		for (String Required_Data : RequiredData ) {
			
			//Try Block:
			try 
			{
				//Get the content of the Node by its tag name:
				System.out.println(Required_Data + " : " + ((Element)Node).getElementsByTagName(Required_Data).item(0).getTextContent());
				resultArray.add("'"+ ((Element)Node).getElementsByTagName(Required_Data).item(0).getTextContent() + "'");
			}
			
			//Catch an Exception:
			catch (Exception exception) 
			{
				//For an exception, the returned result is NULL:
				resultArray.add("null");
			}
			
		}
		return resultArray;	
	}
		
	//Method to extract the required data that are accessible from the rdf description
	public static ArrayList<String> ExtractNode_RDF (Node Node, ArrayList<String[]> Required_Data) 
	{
		
		//Array to store the results:
		ArrayList<String> Resultstorage = new ArrayList<String>();
		
		//Loop on the required data
		for (String[] RequiredData : Required_Data )
		{
			
			//Variable to store the result:
			String result = new String();
			Element temp=null;
			try 
			{
				

			 //In case of RDF resource:
		      if (RequiredData[0]!="") 
				 {
		    	  Node Child;
					//Looping on on all the child of the node, from the fist child until there is no child left
					for(Child = ((Element)Node).getFirstChild(); Child != null; Child = Child.getNextSibling()) 
					{
				        if (RequiredData[0].equals(Child.getNodeName())) 
				        {
				        	if(Child instanceof Element)
				        	{
				        		 temp=(Element) Child;
				        	}
				        }
				    }
					//Get the attribute described by the rdf tag:
					result = temp.getAttribute(RequiredData[1]);
					//result = Get_Child(((Element)Node), RequiredData[0]).getAttribute(RequiredData[1]);
					Resultstorage.add("'"+ result + "'");
		
					System.out.println(RequiredData[0] + "  " + RequiredData[1] + " : " + result);
				 }
		      //For RDF ID data:
			  if (RequiredData[0]=="") 
			  {
				result = ((Element)Node).getAttribute(RequiredData[1]);
				if (result != "") 
				{
				//Printing the rdf ID: 
				System.out.println(RequiredData[1] + " : " + result);
				
				//Storing the RDF ID in resultArray with a # in the beginning: 
				Resultstorage.add("'"+"#"+ result + "'");
				}
			  }
			  
			}
			
			catch(Exception exception)
			{
				//in case of exception, the returned result is null
				Resultstorage.add("null");
			}
		}
		
		return Resultstorage;
	}
	
	Document doc;
	


	
	//Function to convert the attributes of the XML to an form suitable for the SQL Database:
	public static ArrayList<String> convertRdfAttributes (ArrayList<String[]> rdfArray)
	{
		ArrayList<String> NewRDFList = new ArrayList<String>();
		
		//Loop for every Element of the cim data
		for (String[]Element : rdfArray) 
		{
			if (Element[0]!="")  
			{
			//split for at ":" and replace "." by "_" to be adapted to SQL language
				NewRDFList.add(Element[0].split(":")[1].split("\\.")[0] + "_" + Element[0].split(":")[1].split("\\.")[1]);
			}
			//case of ID attribute
			if (Element[0]=="") 
			{
				NewRDFList.add(Element[1].split(":")[1]);
			}
		}
		
		return NewRDFList;
	}


//Function to convert the attributes of the XML to an form suitable for the SQL Database:
public static ArrayList<String> convertCimAttributes (ArrayList<String> CIMArray)
{
	ArrayList<String> NewArray = new ArrayList<String>();
	
	//Loop for every Element of the CIM data
	for (String Element : CIMArray) 
	{
		//Replacing the "." by "_" to be adapted with SQL language
		Element = Element.split(":")[1].split("\\.")[0] + "_" + Element.split("\\.")[1];
		NewArray.add(Element);
	}
	
	return NewArray;
}
	
	
	/*Initializing a Hashmap to which all the CIM objects in the EQ file will be added by tagname*/
	HashMap<String, NodeList> NodeList_HashMap= new HashMap<String, NodeList>();
	
	//The following method i.e. XMLList fills up the HashMap (NodeList_HashMap) with all the needed CIM objects:
	public void XMLList ()
	{
		NodeList_HashMap.put("ACLineSegment", doc.getElementsByTagName("cim:ACLineSegment"));
		NodeList_HashMap.put("BaseVoltage", doc.getElementsByTagName("cim:BaseVoltage"));
		NodeList_HashMap.put("Breaker", doc.getElementsByTagName("cim:Breaker"));
		NodeList_HashMap.put("BusbarSection", doc.getElementsByTagName("cim:BusbarSection"));
		NodeList_HashMap.put("ConnectivityNode", doc.getElementsByTagName("cim:ConnectivityNode"));
		NodeList_HashMap.put("EnergyConsumer", doc.getElementsByTagName("cim:EnergyConsumer"));
		NodeList_HashMap.put("GeneratingUnit", doc.getElementsByTagName("cim:GeneratingUnit") );
		NodeList_HashMap.put("LinearShuntCompensator", doc.getElementsByTagName("cim:LinearShuntCompensator") );
		NodeList_HashMap.put("PowerTransformer", doc.getElementsByTagName("cim:PowerTransformer"));
		NodeList_HashMap.put("PowerTransformerEnd", doc.getElementsByTagName("cim:PowerTransformerEnd"));
		NodeList_HashMap.put("RatioTapChanger", doc.getElementsByTagName("cim:RatioTapChanger"));
		NodeList_HashMap.put("RegulatingControl",  doc.getElementsByTagName("cim:RegulatingControl"));
		NodeList_HashMap.put("Substation", doc.getElementsByTagName("cim:Substation"));
		NodeList_HashMap.put("SynchronousMachine", doc.getElementsByTagName("cim:SynchronousMachine"));
		NodeList_HashMap.put("Terminal", doc.getElementsByTagName("cim:Terminal"));
		NodeList_HashMap.put("VoltageLevel", doc.getElementsByTagName("cim:VoltageLevel"));
	}
	
	
	 //The Load_XML function simply loads the XML file:
		public void Load_XML (String RequiredFile_XML) 
			{
			try 
			{
				//Creating a Document object:
				Document documentobject =  DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File (RequiredFile_XML));
				//Normalizing the XML file after loading it:
				documentobject.getDocumentElement().normalize();
				//Equating this.doc to documentobject:
			    doc = documentobject;
			}
			catch(Exception exception)
			{
				exception.printStackTrace();
			}
		}
			

}
