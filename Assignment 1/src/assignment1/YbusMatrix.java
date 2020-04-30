package assignment1;

//This class is for making the admittance matrix:


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

public class YbusMatrix 
{
	//Base power of the system used to calculate the per unit values
	public double BasePower=500;
	
	//Y-Bus Matrix Object:
	public Complex[][] matrix;
	
	//This arraylist will store all busbars of the system:
	public ArrayList<Busbar> busbar_array;
	
	
	//Method to print the value of the Y-Matrix
	public void print_matrix () 
		{
		
		    int busbarsize=busbar_array.size();
		    
			System.out.println("\n \n        Printing YBUS Matrix\n \n");
            
			//Loop on all the coefficient of the Y-Matrix
			for (int j = 0; j<busbarsize;j++)
			{
				for (int k = 0; k<busbarsize;k++) 
				{
					String temp="Y" + j + k + ": ";
					
					String MattoStr=matrix[j][k].ComplextoString();	
					
					System.out.println(temp + MattoStr);
				}
			}
		}
	
	// This function calculates the admittance of Conducting Equipment
	public Complex Admittance_Calculation (Traverse_Node Node ,boolean same)
	{
		
		
		Complex Admittance = new Complex(0,0);
		
		//If the Conducting Equipment is a Linear Shunt Compensator, the admittance is found as follows:
				if (Node.CE_type == "LinearShuntCompensator") 
				{
					
					//Getting the value of b and the normalized voltage
					String[] variables={"LinearShuntCompensator_bPerSection","ShuntCompensator_nomU"};
					
					ArrayList<String[]> res = Database_SQL.selectwhere(Node.CE_type, variables, "ID", Node.ID);
					for (String[] re : res)
					{						
						//The admittance is as b*u*u/basepower:
						Admittance = new Complex (0, Double.parseDouble(re[0])*Math.pow(Double.parseDouble(re[1]),2)/BasePower);
			
					}
				}
				
				
				//If the Conducting Equipment is an End Consumer
				if (Node.CE_type == "EnergyConsumer") 
				{
					
					//Get the active and reactive power of the load with a SQL "select where" statement 
					String[] variable = {"EnergyConsumer_p","EnergyConsumer_q"};
					
					ArrayList<String[]> select_results = Database_SQL.selectwhere(Node.CE_type, variable, "ID", Node.ID);
					for (String[] select_result : select_results) 
					{
												
						//The admittance is the conjugate of the consumer power/base power:
						Admittance = new Complex (Double.parseDouble(select_result[0]), Double.parseDouble(select_result[1])).conj().scaling(1/BasePower);
					
					}
				}
				
				
				//If the Conducting Equipment is a Power Transformer, this is how we find the admittance:
				if (Node.CE_type == "PowerTransformer") 
				{
					
					//Getting the value of R, X and the rated S of the Power Transformer with a SQL "select_where" statement:
					String[] var = {"PowerTransformerEnd_r","PowerTransformerEnd_x","PowerTransformerEnd_ratedS","TransformerEnd_BaseVoltage"};
								
					//In case of different results do not take into account the values equals to zero
					for (String[] select_result : Database_SQL.selectwhere("PowerTransformerEnd", var, "PowerTransformerEnd_PowerTransformer", Node.ID))
					{
						if(Double.parseDouble(select_result[0]) != 0 )
						{
							if( Double.parseDouble(select_result[1]) != 0)
							{
							double coeff = 100*Double.parseDouble(select_result[1])/Math.sqrt(Math.pow(Double.parseDouble(select_result[0]), 2) + Math.pow(Double.parseDouble(select_result[1]), 2)); 
							
							//The impedance is equal to BasePower.j.(x%/(S)) 
							Complex impedance = new Complex (0, coeff*BasePower/Double.parseDouble(select_result[2]));
							
							//The admittance is the inverse of the impedance:
							Admittance = impedance.inv();
							}
						}
					}
					
				}
		
		//If the Conducting Equipment is an AC Line 
		if (Node.CE_type == "ACLineSegment") {
			
			
			//Geting the values of the the resistance, reactance, b of the capacitor, length of the line and the base voltage of the line 
			String[] attributes = {"ACLineSegment_r","ACLineSegment_x","ACLineSegment_bch","Conductor_length","ConductingEquipment_BaseVoltage"};
			
			ArrayList<String[]> res = Database_SQL.selectwhere(Node.CE_type, attributes, "ID", Node.ID);

			for (String[] re : res) 
			{
			
				String[] temp={"BaseVoltage_nominalVoltage"};
				
				//Getting the value of the base voltage:
				double Ub = Double.parseDouble(String.join("",Database_SQL.selectwhere("BaseVoltage", temp , "ID", re[4]).get(0)));
				
				//The impedance is calculated as follows:
				Complex impedance = new Complex (Double.parseDouble(re[0]),Double.parseDouble(re[1])).scaling(Double.parseDouble(re[3]));
				
				
				//The admittance is the inverse of the impedance:
				Admittance = impedance.scaling(1/(Ub*Ub/BasePower)).inv();
				
				//While calculating the admittance of a single bus we have to take into account the capacitor
				if (same) 
				{
					
					//The admittance is equal to the linear capacitor multiplied by the length of the line and divided by two since the capacitors are modeled as if there are located at each end of the line
					Admittance.add(new Complex (0, Double.parseDouble(re[2])*Double.parseDouble(re[3])*Ub*Ub/BasePower/2));
					
				}
			}
					
		}
		
		
		
		//Print the name of the Conducting Equipment and the value of the admittance
		System.out.println(Node.CE_type + " " + Node.name + "admittance : " + Admittance);

		return Admittance;
	}
	
	
	//Constructor of the class:
	public YbusMatrix (ArrayList<Busbar> busbararray) 
	{
		
		
		//Size of busbar_array:
		int size_busbararray=busbararray.size();
		
		//busbar_array initialization:
		busbar_array = busbararray;
		
		//Initialization of all the values:
		matrix = new Complex[size_busbararray][size_busbararray];
		
		//Initializing the busbar array with all zeros:
		for (int i = 0; i<size_busbararray; i++) 
		{
			for (int j = 0; j<size_busbararray; j++)
			{
				matrix[i][j] = new Complex(0,0);
			}
		}
		
		
		//Looping on every busbar:
		for (Busbar Busbar : busbararray) 
		{
			
			
			
			int tobusbarsize=Busbar.toBusbars.size();
			
			//Method described in the class NetworkTrav that travels from a busbar to all the other busbars and to the ending Conducting Equipment attached to this busbar
			NetworkTrav.Busbartravel(Busbar	, this);
			
			//Loop on all the busbars attached to the concerned busbar
			for (int i = 0;  i<  tobusbarsize; i++)
			{
				
				
				//Print the results
				System.out.println("\nFrom bus " + Busbar.index + " to " + i + " : ");
				
				
				//If the indexes of the Y-Matrix are not the same then the value is equal to the opposite of the admittance between the two busbars
				if (!(Busbar.index == i))
				{

					for (Traverse_Node Node : Busbar.toBusbars.get(i))
					{
						
						Complex admittancecal=Admittance_Calculation(Node, false);
						
						int barid=Busbar.index;
						//Substract all the admittances between the busbars
						matrix[barid][i] = matrix[barid][i].subtract(admittancecal);

					}
				}
				
				
				//If the indexes of the Y-Matrix are the same then the value is equal to the sum of all the admittances connected to the busbar
				else
				{
					
					
					for (int j = 0;  j<  tobusbarsize; j++) 
					{
						for (Traverse_Node Node : Busbar.toBusbars.get(j)) 
						{
							
							Complex admittancecal=Admittance_Calculation(Node, false);
							
							int barid=Busbar.index;

							//Add all the admittances attached to the busbar
							matrix[barid][i] = matrix[barid][i].add(admittancecal);
								
						}		
					}
					
				}
				
				
					
			}
		}
		
		
		
		
	}
	
		
}