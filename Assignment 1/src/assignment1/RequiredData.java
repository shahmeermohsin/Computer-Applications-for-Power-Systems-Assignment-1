package assignment1;


// In this class, the data required from the CIM objects will be stored in Arraylists. There are 3 Arraylists for the same:
// 1) An Arraylist with the required data that can be directly accessed by the CIM model.
// 2) An Arraylist with the required data that is accessed with the rdf description.
// 3) An Arraylist having the name of the CIM object and the first two Arraylists.

// Importing the Libraries:
import java.util.ArrayList;
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


public class RequiredData 
{
	
	
	//Consolidated Array (Array no 3 defined above):
	ArrayList<ArrayList<Object>> RequiredDataArray = new ArrayList<ArrayList<Object>>();
    
	//The following function adds the required data that is accessed with the RDF description:
		public static void RDFRequiredData_Add(ArrayList<String[]> RDFRequiredData, String CIMData, String RDFData) 
		{
			String[] temp = {CIMData,RDFData};
			RDFRequiredData.add(temp);
		}
		
		//The following function adds the required data that can be directly accessed by the CIM model:
		public static void CIMRequiredData_Add(ArrayList<String> CIMDataRequired, String temp)
		{
			CIMDataRequired.add(temp);
		}
	
	public RequiredData() 
	{
	//Arrays for the Base Voltage data:
	ArrayList<String> BaseVoltage_CIMRequiredData = new ArrayList<String>();
	ArrayList<String[]> BaseVoltage_RDFRequiredData = new ArrayList<String[]>();
	ArrayList<Object> BaseVoltageArray = new ArrayList<Object>();	
	
	//Adding BaseVoltage to RequiredDataArray:
	CIMRequiredData_Add(BaseVoltage_CIMRequiredData, "cim:BaseVoltage.nominalVoltage");
	RDFRequiredData_Add(BaseVoltage_RDFRequiredData, "", "rdf:ID");
	BaseVoltageArray.add("BaseVoltage");
	BaseVoltageArray.add(BaseVoltage_CIMRequiredData);
	BaseVoltageArray.add(BaseVoltage_RDFRequiredData);
	RequiredDataArray.add(BaseVoltageArray);
	
	//Arrays for the Breaker data:
	ArrayList<String> Breaker_CIMRequiredData = new ArrayList<String>();
	ArrayList<String[]> Breaker_RDFRequiredData = new ArrayList<String[]>();
	ArrayList<Object> BreakerArray = new ArrayList<Object>();
	
	//Adding Breaker to RequiredDataArray:
	RDFRequiredData_Add(Breaker_RDFRequiredData, "", "rdf:ID");
	CIMRequiredData_Add(Breaker_CIMRequiredData, "cim:IdentifiedObject.name");
	CIMRequiredData_Add(Breaker_CIMRequiredData, "cim:Switch.open");	
	RDFRequiredData_Add(Breaker_RDFRequiredData,"cim:Equipment.EquipmentContainer", "rdf:resource");
	RDFRequiredData_Add(Breaker_RDFRequiredData, "cim:Breaker.BaseVoltage","rdf:resource"); 
	BreakerArray.add("Breaker");
	BreakerArray.add(Breaker_CIMRequiredData);
	BreakerArray.add(Breaker_RDFRequiredData);
	RequiredDataArray.add(BreakerArray);
	
	//Arrays for the EnergyConsumer data:
	ArrayList<String> EnergyConsumer_CIMRequiredData = new ArrayList<String>();
	ArrayList<String[]> EnergyConsumer_RDFRequiredData = new ArrayList<String[]>();
	ArrayList<Object> EnergyConsumerArray = new ArrayList<Object>();
	
	//Adding EnergyConsumer to RequiredDataArray:
	RDFRequiredData_Add(EnergyConsumer_RDFRequiredData, "", "rdf:ID");
	CIMRequiredData_Add(EnergyConsumer_CIMRequiredData, "cim:IdentifiedObject.name");
	CIMRequiredData_Add(EnergyConsumer_CIMRequiredData, "cim:EnergyConsumer.p");
	CIMRequiredData_Add(EnergyConsumer_CIMRequiredData, "cim:EnergyConsumer.q");
	RDFRequiredData_Add(EnergyConsumer_RDFRequiredData,"cim:Equipment.EquipmentContainer", "rdf:resource"); 
	RDFRequiredData_Add(EnergyConsumer_RDFRequiredData, "cim:EnergyConsumer.BaseVoltage","rdf:resource"); 
	EnergyConsumerArray.add("EnergyConsumer");
	EnergyConsumerArray.add(EnergyConsumer_CIMRequiredData);
	EnergyConsumerArray.add(EnergyConsumer_RDFRequiredData);
	RequiredDataArray.add(EnergyConsumerArray);
	
	//Arrays for the GeneratingUnit data:
	ArrayList<String> GeneratingUnit_CIMRequiredData = new ArrayList<String>();
	ArrayList<String[]> GeneratingUnit_RDFRequiredData = new ArrayList<String[]>();
	ArrayList<Object> GeneratingUnitArray = new ArrayList<Object>();
	
	//Adding GeneratingUnit to RequiredDataArray:
	RDFRequiredData_Add(GeneratingUnit_RDFRequiredData, "", "rdf:ID");
	CIMRequiredData_Add(GeneratingUnit_CIMRequiredData, "cim:IdentifiedObject.name");
	RDFRequiredData_Add(GeneratingUnit_RDFRequiredData,"cim:Equipment.EquipmentContainer", "rdf:resource"); 
	CIMRequiredData_Add(GeneratingUnit_CIMRequiredData, "cim:GeneratingUnit.maxOperatingP");
	CIMRequiredData_Add(GeneratingUnit_CIMRequiredData, "cim:GeneratingUnit.minOperatingP");
	GeneratingUnitArray.add("GeneratingUnit");
	GeneratingUnitArray.add(GeneratingUnit_CIMRequiredData);
	GeneratingUnitArray.add(GeneratingUnit_RDFRequiredData);
	RequiredDataArray.add(GeneratingUnitArray);
    
	//Arrays for the PowerTransformer data:
	ArrayList<String> PowerTransformer_CIMRequiredData = new ArrayList<String>();
	ArrayList<String[]> PowerTransformer_RDFRequiredData = new ArrayList<String[]>();
	ArrayList<Object> PowerTransformerArray = new ArrayList<Object>();
	
	//Adding PowerTransformer to RequiredDataArray:
	RDFRequiredData_Add(PowerTransformer_RDFRequiredData, "", "rdf:ID");
	CIMRequiredData_Add(PowerTransformer_CIMRequiredData, "cim:IdentifiedObject.name");
	RDFRequiredData_Add(PowerTransformer_RDFRequiredData,"cim:Equipment.EquipmentContainer", "rdf:resource"); 
	PowerTransformerArray.add("PowerTransformer");
	PowerTransformerArray.add(PowerTransformer_CIMRequiredData);
	PowerTransformerArray.add(PowerTransformer_RDFRequiredData);
	RequiredDataArray.add(PowerTransformerArray);
	
	//Arrays for the PowerTransformerEnd data:
	ArrayList<String> PowerTransformerEnd_CIMRequiredData = new ArrayList<String>();
	ArrayList<String[]> PowerTransformerEnd_RDFRequiredData = new ArrayList<String[]>();
	ArrayList<Object> PowerTransformerEndArray = new ArrayList<Object>();
	
	//Adding PowerTransformer to RequiredDataArray:
	RDFRequiredData_Add(PowerTransformerEnd_RDFRequiredData, "", "rdf:ID");	
	CIMRequiredData_Add(PowerTransformerEnd_CIMRequiredData, "cim:IdentifiedObject.name");
	CIMRequiredData_Add(PowerTransformerEnd_CIMRequiredData, "cim:PowerTransformerEnd.r"); 
	CIMRequiredData_Add(PowerTransformerEnd_CIMRequiredData, "cim:PowerTransformerEnd.x"); 
	CIMRequiredData_Add(PowerTransformerEnd_CIMRequiredData, "cim:PowerTransformerEnd.ratedS"); 
	RDFRequiredData_Add(PowerTransformerEnd_RDFRequiredData, "cim:PowerTransformerEnd.PowerTransformer", "rdf:resource");
	RDFRequiredData_Add(PowerTransformerEnd_RDFRequiredData, "cim:TransformerEnd.BaseVoltage", "rdf:resource");
	PowerTransformerEndArray.add("PowerTransformerEnd");
	PowerTransformerEndArray.add(PowerTransformerEnd_CIMRequiredData);
	PowerTransformerEndArray.add(PowerTransformerEnd_RDFRequiredData);
	RequiredDataArray.add(PowerTransformerEndArray);

	//Arrays for the RatioTapChanger data:
	ArrayList<String> RatioTapChanger_CIMRequiredData = new ArrayList<String>();
	ArrayList<String[]> RatioTapChanger_RDFRequiredData = new ArrayList<String[]>();
	ArrayList<Object> RatioTapChangerArray = new ArrayList<Object>();
		
	//Adding RatioTapChanger to RequiredDataArray:
	RDFRequiredData_Add(RatioTapChanger_RDFRequiredData, "", "rdf:ID");
	CIMRequiredData_Add(RatioTapChanger_CIMRequiredData, "cim:IdentifiedObject.name");
	CIMRequiredData_Add(RatioTapChanger_CIMRequiredData,"cim:TapChanger.normalStep"); 
	RatioTapChangerArray.add("RatioTapChanger");
	RatioTapChangerArray.add(RatioTapChanger_CIMRequiredData);
	RatioTapChangerArray.add(RatioTapChanger_RDFRequiredData);
	RequiredDataArray.add(RatioTapChangerArray);
	
	//Arrays for the RegulatingControl data:
	ArrayList<String> RegulatingControl_CIMRequiredData = new ArrayList<String>();
	ArrayList<String[]> RegulatingControl_RDFRequiredData = new ArrayList<String[]>();
	ArrayList<Object> RegulatingControlArray = new ArrayList<Object>();
	
	//Adding RegulatingControl to RequiredDataArray:
	RDFRequiredData_Add(RegulatingControl_RDFRequiredData, "", "rdf:ID");
	CIMRequiredData_Add(RegulatingControl_CIMRequiredData, "cim:IdentifiedObject.name");
	CIMRequiredData_Add(RegulatingControl_CIMRequiredData, "cim:RegulatingControl.targetValue"); 
	RegulatingControlArray.add("RegulatingControl");
	RegulatingControlArray.add(RegulatingControl_CIMRequiredData);
	RegulatingControlArray.add(RegulatingControl_RDFRequiredData);
	RequiredDataArray.add(RegulatingControlArray);
	
	//Arrays for the Sub-station data:
	ArrayList<String> Substation_CIMRequiredData = new ArrayList<String>();
	ArrayList<String[]> Substation_RDFRequiredData = new ArrayList<String[]>();
    ArrayList<Object> SubstationArray = new ArrayList<Object>();
	
	//Adding Substation to RequiredDataArray:
	RDFRequiredData_Add(Substation_RDFRequiredData, "", "rdf:ID");
	CIMRequiredData_Add(Substation_CIMRequiredData, "cim:IdentifiedObject.name");
	RDFRequiredData_Add(Substation_RDFRequiredData,"cim:Substation.Region", "rdf:resource"); 
	SubstationArray.add("Substation");
	SubstationArray.add(Substation_CIMRequiredData);
	SubstationArray.add(Substation_RDFRequiredData);
	RequiredDataArray.add(SubstationArray);
	
	
	//Arrays for the SynchronousMachine data:
	ArrayList<String> SynchronousMachine_CIMRequiredData = new ArrayList<String>();
	ArrayList<String[]> SynchronousMachine_RDFRequiredData = new ArrayList<String[]>();
	ArrayList<Object> SynchronousMachineArray = new ArrayList<Object>();
	
	//Adding SynchronousMachine to RequiredDataArray:
	RDFRequiredData_Add(SynchronousMachine_RDFRequiredData, "", "rdf:ID");
	CIMRequiredData_Add(SynchronousMachine_CIMRequiredData, "cim:IdentifiedObject.name");
	CIMRequiredData_Add(SynchronousMachine_CIMRequiredData, "cim:RotatingMachine.ratedS");
	CIMRequiredData_Add(SynchronousMachine_CIMRequiredData, "cim:RotatingMachine.p");
	CIMRequiredData_Add(SynchronousMachine_CIMRequiredData, "cim:RotatingMachine.q");
	RDFRequiredData_Add(SynchronousMachine_RDFRequiredData, "cim:RotatingMachine.GeneratingUnit", "rdf:resource");
	RDFRequiredData_Add(SynchronousMachine_RDFRequiredData, "cim:RegulatingCondEq.RegulatingControl", "rdf:resource");
	RDFRequiredData_Add(SynchronousMachine_RDFRequiredData, "cim:Equipment.EquipmentContainer", "rdf:resource");
	RDFRequiredData_Add(SynchronousMachine_RDFRequiredData, "cim:SynchronousMachine.BaseVoltage","rdf:resource"); 
	SynchronousMachineArray.add("SynchronousMachine");
	SynchronousMachineArray.add(SynchronousMachine_CIMRequiredData);
	SynchronousMachineArray.add(SynchronousMachine_RDFRequiredData);
	RequiredDataArray.add(SynchronousMachineArray);
    
	//Arrays for the VoltageLevel data:
	ArrayList<String> VoltageLevel_CIMRequiredData = new ArrayList<String>();
	ArrayList<String[]> VoltageLevel_RDFRequiredData = new ArrayList<String[]>();
	ArrayList<Object> VoltageLevelArray = new ArrayList<Object>();
	
	//Adding VoltageLevel to RequiredDataArray:
	RDFRequiredData_Add(VoltageLevel_RDFRequiredData, "", "rdf:ID");	
	CIMRequiredData_Add(VoltageLevel_CIMRequiredData, "cim:IdentifiedObject.name");
	RDFRequiredData_Add(VoltageLevel_RDFRequiredData, "cim:VoltageLevel.Substation", "rdf:resource");
	RDFRequiredData_Add(VoltageLevel_RDFRequiredData, "cim:VoltageLevel.BaseVoltage", "rdf:resource");	
	VoltageLevelArray.add("VoltageLevel");
	VoltageLevelArray.add(VoltageLevel_CIMRequiredData);
	VoltageLevelArray.add(VoltageLevel_RDFRequiredData);
	RequiredDataArray.add(VoltageLevelArray);
	
	//Arrays for the Terminal data:
	ArrayList<String> Terminal_CIMRequiredData = new ArrayList<String>();
	ArrayList<String[]> Terminal_RDFRequiredData = new ArrayList<String[]>();
	ArrayList<Object> TerminalArray = new ArrayList<Object>();
	
	//Adding Terminal to RequiredDataArray:
	RDFRequiredData_Add(Terminal_RDFRequiredData, "", "rdf:ID");	
	CIMRequiredData_Add(Terminal_CIMRequiredData, "cim:IdentifiedObject.name");
	RDFRequiredData_Add(Terminal_RDFRequiredData, "cim:Terminal.ConductingEquipment", "rdf:resource");
	RDFRequiredData_Add(Terminal_RDFRequiredData, "cim:Terminal.ConnectivityNode", "rdf:resource");
	TerminalArray.add("Terminal");
	TerminalArray.add(Terminal_CIMRequiredData);
	TerminalArray.add(Terminal_RDFRequiredData);
	RequiredDataArray.add(TerminalArray);
	
	//Arrays for the ConnectivityNode data:
	ArrayList<String> ConnectivityNode_CIMRequiredData = new ArrayList<String>();
	ArrayList<String[]> ConnectivityNode_RDFRequiredData = new ArrayList<String[]>();
	ArrayList<Object> ConnectivityNodeArray = new ArrayList<Object>();
	
	//Adding ConnectivityNode to RequiredDataArray:
	RDFRequiredData_Add(ConnectivityNode_RDFRequiredData, "", "rdf:ID");	
	CIMRequiredData_Add(ConnectivityNode_CIMRequiredData, "cim:IdentifiedObject.name");
	ConnectivityNodeArray.add("ConnectivityNode");
	ConnectivityNodeArray.add(ConnectivityNode_CIMRequiredData);
	ConnectivityNodeArray.add(ConnectivityNode_RDFRequiredData);
	RequiredDataArray.add(ConnectivityNodeArray);
	
	//Arrays for the BusbarSection data:
	ArrayList<String> BusbarSection_CIMRequiredData = new ArrayList<String>();
	ArrayList<String[]> BusbarSection_RDFRequiredData = new ArrayList<String[]>();
	ArrayList<Object> BusbarSectionArray = new ArrayList<Object>();
	
	//Adding BusbarSection to RequiredDataArray:
	RDFRequiredData_Add(BusbarSection_RDFRequiredData, "", "rdf:ID");	
	CIMRequiredData_Add(BusbarSection_CIMRequiredData, "cim:IdentifiedObject.name");
	BusbarSectionArray.add("BusbarSection");
	BusbarSectionArray.add(BusbarSection_CIMRequiredData);
	BusbarSectionArray.add(BusbarSection_RDFRequiredData);
	RequiredDataArray.add(BusbarSectionArray);
	
	//Arrays for the LinearShuntCompensator data:
	ArrayList<String> LinearShuntCompensator_CIMRequiredData = new ArrayList<String>();
	ArrayList<String[]> LinearShuntCompensator_RDFRequiredData = new ArrayList<String[]>();
	ArrayList<Object> LinearShuntCompensatorArray = new ArrayList<Object>();
	
	//Adding LinearShuntCompensator to RequiredDataArray:
	RDFRequiredData_Add(LinearShuntCompensator_RDFRequiredData, "", "rdf:ID");	
	CIMRequiredData_Add(LinearShuntCompensator_CIMRequiredData, "cim:IdentifiedObject.name");
	CIMRequiredData_Add(LinearShuntCompensator_CIMRequiredData, "cim:LinearShuntCompensator.bPerSection");
	CIMRequiredData_Add(LinearShuntCompensator_CIMRequiredData, "cim:ShuntCompensator.nomU");
	LinearShuntCompensatorArray.add("LinearShuntCompensator");
	LinearShuntCompensatorArray.add(LinearShuntCompensator_CIMRequiredData);
	LinearShuntCompensatorArray.add(LinearShuntCompensator_RDFRequiredData);
	RequiredDataArray.add(LinearShuntCompensatorArray);
	
	//Arrays for the ACLineSegment data:
	ArrayList<String> ACLineSegment_CIMRequiredData = new ArrayList<String>();
	ArrayList<String[]> ACLineSegment_RDFRequiredData = new ArrayList<String[]>();
	ArrayList<Object> ACLineSegmentArray = new ArrayList<Object>();
	
	//Adding ACLineSegment to RequiredDataArray:
	RDFRequiredData_Add(ACLineSegment_RDFRequiredData, "", "rdf:ID");	
	CIMRequiredData_Add(ACLineSegment_CIMRequiredData, "cim:IdentifiedObject.name");
	CIMRequiredData_Add(ACLineSegment_CIMRequiredData, "cim:ACLineSegment.r");
	CIMRequiredData_Add(ACLineSegment_CIMRequiredData, "cim:ACLineSegment.x");
	CIMRequiredData_Add(ACLineSegment_CIMRequiredData, "cim:ACLineSegment.bch");
	CIMRequiredData_Add(ACLineSegment_CIMRequiredData, "cim:Conductor.length");
	RDFRequiredData_Add(ACLineSegment_RDFRequiredData, "cim:ConductingEquipment.BaseVoltage", "rdf:resource");
	ACLineSegmentArray.add("ACLineSegment");
	ACLineSegmentArray.add(ACLineSegment_CIMRequiredData);
	ACLineSegmentArray.add(ACLineSegment_RDFRequiredData);
	RequiredDataArray.add(ACLineSegmentArray);
	}
		
}