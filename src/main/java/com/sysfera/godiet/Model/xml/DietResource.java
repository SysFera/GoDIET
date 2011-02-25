package com.sysfera.godiet.Model.xml;

import com.sysfera.godiet.Model.states.StateController;
import com.sysfera.godiet.Model.xml.generated.Agent;
import com.sysfera.godiet.Model.xml.generated.Node;


/**
 *  Use to manage and control all DIET elements (SeD, MA, LA, MA_DAG)
 * @author phi
 *
 */
public class DietResource {
	
	//Agent description
	private Agent dietAgentDescription;
	private StateController stateController;
	
	
	private Node pluggedOn;
	
	

	
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


	/**
	 * @param pluggedOn the pluggedOn to set
	 */
	public void setPluggedOn(Node pluggedOn) {
		this.pluggedOn = pluggedOn;
	}


	/**
	 * Return the physical resource on which agent is plugged on
	 * @return the pluggedOn
	 */
	public Node getPluggedOn() {
		return pluggedOn;
	}
	
	/**
	 *  Set the agent Description.
	 * @param dietAgent
	 */
	public void setDietAgent(Agent dietAgent) {
		this.dietAgentDescription = dietAgent;
	}
	
}
