package assignment1;

//This class implements the algorithm in the paper on Canvas:


import java.util.ArrayList;
import java.util.Stack;
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

public class NetworkTrav {
	
	//These are the indices:
		//Defining the current node object:
		static Traverse_Node CurrentNode=new Traverse_Node();
		//Defining the Starting Node which is of type Traverse_Node:
		static Traverse_Node StartingNode=new Traverse_Node();
		//Defining the previous node object:
		static Traverse_Node PreviousNode=new Traverse_Node();
		//Next Node object:
		static Traverse_Node NextNode=new Traverse_Node();
		
		//These stack data-structures store the node visited during the network traversal and also keep track of the order in which these nodes have been visited
		//A stack for all data
		static Stack<Traverse_Node> AllDataStack = new Stack<Traverse_Node>(); 	
		//Stack for connectivity nodes:
		static Stack<Traverse_Node> Stack_CN = new Stack<Traverse_Node>();
		//Stack for conductivity equipment:
		static Stack<Traverse_Node> Stack_CE = new Stack<Traverse_Node>();
		
		
		//These lists will use the SQL methods defined in the class Database_SQL 
		//to select all the nodes traversed and to then store them in different arrays according to their type
		//Connectivity Equipment ArrayList:
		static ArrayList<Traverse_Node> CNArray =Database_SQL.SelectCN();
		//Terminal ArrayList:
		static ArrayList<Traverse_Node> TEArray = Database_SQL.SelectTE();
		//Conducting Equipment  ArrayList:
		static ArrayList<Traverse_Node> CEArray = Database_SQL.selectCE();



		//Function that takes a busbar and the Y-matrix and travels the network to build the Y-matrix
		public static void Busbartravel (Busbar busbar, YbusMatrix YbusMatrix) 
		{
			
			//Initializing the arrays:
			TEArray = Database_SQL.SelectTE();
			CNArray = Database_SQL.SelectCN();
			CEArray = Database_SQL.selectCE();
		
			
			//Initializing the starting busbar as the current node:
			for (Traverse_Node Node : CNArray) 
			{
				if (Node.ID.equals(busbar.ConnectingEqID)) 
				{
					PreviousNode = new Traverse_Node();
					StartingNode = Node;
					CurrentNode = StartingNode;
					
					
				}
			}
				
						
			//Print the result for the busbar
			System.out.println("\n \n                " + StartingNode.name);
			
			//Traveling the network for every Terminal connected to the Connectivity Node of the busbar
			for (String Bus_terminal : StartingNode.terminalList) 
			{
				for (Traverse_Node Terminal : TEArray) {
					if (Terminal.ID.equals(Bus_terminal)) 
					{
						//Make the terminal the current node
						CurrentNode = Terminal;
						PreviousNode = new Traverse_Node();
						//Calling the network traversal algorithm
						Traversal_Network(busbar, YbusMatrix);
					}
				}
				
			}
		}
	
	//initialization of the network traversal algorithm

	public static void Traversal_Network(Busbar busbar, YbusMatrix YbusMatrix ) {
		
		//while condition for the while loop
		boolean condition = true;
	
		
		while (condition) {
			
			
			//Find the next node (Terminal):
			//Use the find next node method to find which node will be visited next
			//If the current node isn't a terminal i.e. it's a CE or CN:
			if (CurrentNode.Nodetype != "TE")  
			{ 
					//If the current node represents a CE or a CN, the next node will logically be a terminal:
				
					//Looping on all TEs in the Terminal list to figure out which one is attached to the current node (CN or CE) and if it has not been visited yet:
					for (String TE_id : CurrentNode.terminalList) 
					{
						for (Traverse_Node Node : TEArray)
						{
							//Check if the TE Node.id equals the  
							if(Node.ID.equals(TE_id)) 
							{
								//Check if the traversal flag for the node is zero:
								if(Node.traversal_flag==0)
								{
							       NextNode = Node;
								}
						
							} 
						}
					}
					
			}
			
			//Find the next node (CE or CN):
			//Find if the current node is a terminal:
			if (CurrentNode.Nodetype == "TE") 
			{
				String currentnodeid=CurrentNode.ID;
				//Find if the previous node is a Conducting equipment and not a Conducting Equipment:
				if (PreviousNode.Nodetype != "CE")  
				{
					//If the previous node is Connectivity Node then the next one will be a Conducting Equipment:
					//This loop will loop on every Conducting Equipment to find which one is connected to the Terminal in question:
					for (Traverse_Node Node : CEArray)
					{
						for (String CETerminal : Node.terminalList) 
						{
							//if the Node.terminalList of the CE reached by the looping is equal to the ID of the current node (terminal),
							//the particular CE reached by the looping is made the NextNode as shown below:
							if (currentnodeid.equals(CETerminal)) 
							{
								//Next node is now the Conducting Equipment determined by the loop:
	 							NextNode = Node;
	 							//return;
							}
							
						}
						
					}
				}
				
				//If the previous node is a Conducting Equipment and current node is a terminal,
				//the next node will be a CN:
				if (PreviousNode.Nodetype == "CE") 
				{
					//Loop on every CN to find which one is connected to the current node (Terminal):
					for (Traverse_Node Node : CNArray) 
					{
						for (String CN_terminal : Node.terminalList) 
						{
							//if the Node.terminalList of the CN reached by the looping is equal to the ID of the current node (terminal),
							//the particular CN reached by the looping is made the NextNode as shown below:
							if (currentnodeid.equals(CN_terminal)) 
							{
								NextNode = Node;
								//return;
							}
						}
					}
				
				} 
			} 			
			
			
						
			
			//Check if the current node is a terminal...If yes, make the traversal flag equal to 1 and push the current node in the stack for all data
			if (CurrentNode.Nodetype=="TE") 
			{
				
				//Push this node to the stack for all data and then mark the terminal as traversed and update the indices:
				AllDataStack.push(CurrentNode);
				CurrentNode.traversal_flag = 1;
				PreviousNode = CurrentNode;
				CurrentNode = NextNode;
				CurrentNode.traversal_flag = 1;	
			}
				
			
			
			//if statement for the case when the current node is a Conducting Equipment:
			if (CurrentNode.Nodetype=="CE") 
			{
				
				//Push the current node to the stack for all data:
				AllDataStack.push(CurrentNode);
				//Push the current node to the stack for the conducting equipment:
				Stack_CE.push(CurrentNode);				
				
				//If the node has only one Terminal (ending device) 
				//or is a breaker in its open state,
				//this means that the traversal algorithm has found an end device:
				if ((CurrentNode.num_attachTerm == 1)&&!PreviousNode.ID.equals("null")||(CurrentNode.Nodetype=="Breaker"&&Database_SQL.OpenBreaker(CurrentNode.ID)))
				{
					
					//This means that the network travel algorithm has found an end device
					//Publishing the results:
					publish(busbar,YbusMatrix);
					//Exiting the while loop:
					condition = false;
				}	
				
				//If the CE is not an end device, the indexes are updated and the loop continues:
				PreviousNode = CurrentNode;
				CurrentNode = NextNode;
				CurrentNode.traversal_flag = 1;			
			}
			
			//Check if the current node is a Connectivity Node:
			if (CurrentNode.Nodetype=="CN") 
			{
				
				//Push the current node to the stack for all data:
				AllDataStack.push(CurrentNode);
				//Push the current node to the stack for CN data:
				Stack_CN.push(CurrentNode);
					
				//Test if the Connectivity Node is attached to a Busbar
				//Looping on the Terminals in the terminal list of the CN
				for (String CNTerminal : CurrentNode.terminalList) 
				{
					//Looping on the CEs:
					for (Traverse_Node CENode : CEArray) 
					{
						//Loop on every Terminals in order to find the corresponding Terminal in the Terminal list of the Conducting Equipment
						for (String CE_terminal : CENode.terminalList) 
						{
							
							//If the corresponding Terminal is attached to a Busbar Section 
							if (CE_terminal.equals(CNTerminal) && CENode.CE_type.equals("BusbarSection")) 
							{
								for (Traverse_Node terminal : TEArray) 
								{
									
									//If the terminal is not attached to a busbar, update the indices let the loop run:
									if (!(CENode.CE_type=="BusbarSection")) 
									{
										if (CNTerminal==CE_terminal)
										{
										//If the Connectivity Node is not connected to a Bus Bar,  update the indices and let the loop run:
										PreviousNode = CurrentNode;
										CurrentNode = NextNode;
										CurrentNode.traversal_flag = 1;
										}
									}
									
									if (terminal.ID.equals(CE_terminal)) 
									{
										//If the Connectivity node is connected to a Busbar-> end the method
										//Push the node to the stack for all data and to the CE stack and mark the terminal as visited and publish the result with the publish method and exit the while loop
                                        //Push the node to the stack for all data:
										AllDataStack.push(terminal);
										//Mark the terminal as visited:
										terminal.traversal_flag = 1;
										//Push the CE Node to the CE stack:
										Stack_CE.push(CENode);
										//pushx the CE to the stack for all data:
										AllDataStack.push(CENode);
										//publish the result:
										publish(busbar, YbusMatrix);
										//end the loop:
										condition = false;
							
							
									} 
								}
								
								
							} 

							else 
							{
								//If there is no corresponding Terminal this means that all the Terminals of the Connectivity Node have been visited
								//Then update the indexes
								PreviousNode = CurrentNode;
								CurrentNode = NextNode;
								CurrentNode.traversal_flag = 1;			
							}
						}
						
						
					} 
				}
			}
			

		
		}
		
	}
	

		
		//Method to publish the result of the network traversal once the algorithm has traveled from a busbar to another busbar or to an end device
		public static void publish (Busbar busbar, YbusMatrix YbusMatrix) 
		{
			
			int index = busbar.index;

			if (!AllDataStack.isEmpty()) 
			{
				if(!Stack_CE.isEmpty())
			{
				
				
				while (!Stack_CE.isEmpty()) 
				{
					
					Traverse_Node Node = Stack_CE.pop();
					
					if (!busbar.toBusbars.get(index).contains(Node)) 
					{
						if((Node.CE_type.matches("PowerTransformer|ACLineSegment|LinearShuntCompensator|EnergyConsumer|SynchronousMachine|GeneratingUnit")))
						{
						
						busbar.toBusbars.get(index).add(Node);
						}
					}
					
					if (Node.CE_type.equals("BusbarSection")) 
					{
						for (Busbar Endingbar : YbusMatrix.busbar_array) 
						{
							if (Endingbar.ConductingEquipID.equals(Node.ID)) 
							{
								index = Endingbar.index;
							}
						}
						
					}
					
					
				}

				
			}
			
			}
			
		}

	}

