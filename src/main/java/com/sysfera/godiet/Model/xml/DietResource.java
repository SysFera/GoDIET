package com.sysfera.godiet.Model.xml;

import com.sysfera.godiet.Model.states.StateController;
import com.sysfera.godiet.Model.xml.generated.Agent;


/**
 *  Use to manage and control all DIET elements (SeD, MA, LA, MA_DAG)
 * @author phi
 *
 */
public class DietResource {
	
	//Agent description
	private Agent dietAgentDescription;
	private StateController stateController;
	
	public void setDietAgent(Agent dietAgent) {
		this.dietAgentDescription = dietAgent;
	}
	
	
	public void prepare()
	{
		stateController.prepare();
	}
	public void start()
	{
		stateController.start();
	}
	
	public  void stop()
	{
		stateController.stop();
	}
}
