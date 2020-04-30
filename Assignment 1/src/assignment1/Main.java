package assignment1;


import javax.swing.JOptionPane;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

//import com.sun.javafx.collections.MappingChange.Map;



public class Main {
	
	public static void main (String[] args)
	{
			
			
			
			String fileNameEQ = JOptionPane.showInputDialog("Enter EQ File Path");		
			String fileNameSSH = JOptionPane.showInputDialog("Enter SSH File Path");
			
			//String fileNameEQ = new String("C:\\\\Users\\\\14bee\\\\OneDrive\\\\Desktop\\\\Comp App\\\\Assignment_EQ_reduced.xml");		
			//String fileNameSSH = new String("C:\\\\Users\\\\14bee\\\\OneDrive\\\\Desktop\\\\Comp App\\\\Assignment_SSH_reduced.xml");
			
			//Instance of the required data from the class RequiredData for the parsing
			RequiredData data = new RequiredData();
			
			
			//Creating the XML reader for each files from the class XML_Read
			XML_Read readerEQ = new XML_Read();
			readerEQ.Load_XML(fileNameEQ);
			
			
			XML_Read readerSSH = new XML_Read();
			readerSSH.Load_XML(fileNameSSH);
			
			readerEQ.XMLList();
			readerSSH.XMLList();
			
			System.out.println(" \n\n          Parsing EQ and SSH files               **");
			
			//Creating the matrix busbars 
			ArrayList<Busbar> busbar_array = new ArrayList<Busbar>();

			
			//For the reduced Network :
			
			busbar_array.add(new Busbar (0, "#_64901aec-5a8a-4bcb-8ca7-a3ddbfcd0e6c", "#_4836f99b-c6e9-4ee8-a956-b1e3da882d46"));
			busbar_array.add(new Busbar (1, "#_ef45b632-3028-4afe-bc4c-a4fa323d83fe", "#_ae99bd74-26b1-443a-b1a5-656320283a36"));
			busbar_array.add(new Busbar (2, "#_5caf27ed-d2f8-458a-834a-6b3193a982e6", "#_bf851342-832e-4ea2-b2ad-b09729b3af23"));
			busbar_array.add(new Busbar (3, "#_fd649fe1-bdf5-4062-98ea-bbb66f50402d", "#_0f074167-d8ad-40ed-b0fa-7dc7e9f5f77c"));
			busbar_array.add(new Busbar (4, "#_364c9ca2-0d1d-4363-8f46-e586f8f66a8c", "#_1695eb20-9044-4133-a3fd-2147f55f170d"));
			
			
			try {
				
				//Creating SQL database
				Database_SQL.getconnection();
				Database_SQL.create_DB("pow");
				
				//Parsing for all the CIM objects stored in the class Required Data
				for (ArrayList<Object> object : data.RequiredDataArray)
                {
				
					System.out.println("\n \n Parsing " + (String) object.get(0) + "\n\n\n");

					//Get the Cim required Data
					ArrayList<String> cimRequiredData = (ArrayList<String>)object.get(1);
					//Get the Cim required Data
					ArrayList<String[]> rdfRequiredData = (ArrayList<String[]>)object.get(2);
										
				
					//Create a Table in the SQL Database for the Cim object with the required data as attributes
					Database_SQL.make_table((String) object.get(0), "ID", XML_Read.convertCimAttributes(cimRequiredData),XML_Read.convertRdfAttributes(rdfRequiredData));			
					//System.out.println(readerEQ.NodeList_HashMap.get((String) object.get(0)));

					//System.out.println("\n \n Parsing " + (String) object.get(0) + "\n\n\n");
					
					//Parsing and storing in the SQL Database the needed data of the Cim object from the two files
					XML_Read.parse_EQ (readerEQ.NodeList_HashMap.get((String) object.get(0)), (String) object.get(0), cimRequiredData, rdfRequiredData);
					XML_Read.parse_SSH (readerSSH.NodeList_HashMap.get((String) object.get(0)), (String) object.get(0), cimRequiredData, rdfRequiredData);

					
				}
				

				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			
			
			
			
			//System.out.println("Required Data:    "+data.RequiredDataArray);

			//Updating BaseVoltage for the Tables "Braker", "SynchronousMachine" and "EnergyConsumer"
			Database_SQL.updatejoin("Breaker", "VoltageLevel", "Equipment_EquipmentContainer", "ID", "Breaker_BaseVoltage", "VoltageLevel_BaseVoltage");
			Database_SQL.updatejoin("SynchronousMachine", "VoltageLevel", "Equipment_EquipmentContainer", "ID", "SynchronousMachine_BaseVoltage", "VoltageLevel_BaseVoltage");
			Database_SQL.updatejoin("EnergyConsumer", "VoltageLevel", "Equipment_EquipmentContainer", "ID", "EnergyConsumer_BaseVoltage", "VoltageLevel_BaseVoltage");
			//System.out.println(NetworkTrav.CEArray);
			
			//Network Traversal
			
			System.out.println("\n \n\n  **                Network Traversal\n\n");

			
			//Printing the Connectivity Nodes (CN) from the class NetworkTrav and the needed data for the Network Traversal Algorithm
			System.out.println("\n\n**              CN array");
			for(Traverse_Node CN : NetworkTrav.CNArray) 
			{
				System.out.println("\n"+"name:"+CN.name+"\nID:"+CN.ID+"\nnode type:"+CN.Nodetype+"\nCE type:"+CN.CE_type+""
						+ "\nTerminal list:"+CN.terminalList+"\nnum attached terminals:"+CN.num_attachTerm);

			}
			
			//Printing the Conductivity Equipment (CE) from the class NetworkTrav and the needed data EnergyConsumer

			System.out.println("\n\n\n**              CE array");
			for(Traverse_Node CE : NetworkTrav.CEArray) 
			{
				System.out.println("\n"+"name:"+CE.name+"\nID:"+CE.ID+"\nnode type:"+CE.Nodetype+"\nCE type:"+CE.CE_type+"\nTerminal list:"+CE.terminalList+"\nnum attached terminals:"+CE.num_attachTerm);

			}
			
			
			//Creating the Y-Matrix
			YbusMatrix YbusMatrix = new YbusMatrix(busbar_array);
					
			//Printing the Y-Matrix
			System.out.println("\n \n\n\n**              Ybus Matrix");
			YbusMatrix.print_matrix();
			
			
	}	
}