package com.sysfera.godiet.Model.xml;

import com.sysfera.godiet.Model.xml.generated.Agent;
import com.sysfera.godiet.Model.xml.generated.Node;

/**
 * Use to manage and control all DIET elements (SeD, MA, LA, MA_DAG)
 * 
 * @author phi
 * 
 */
public class DietResourceManager extends SoftwareManager {

	// Agent description
	private Agent agentManaged;

	/**
	 * Set the agent to manage.
	 * 
	 * @param dietAgent
	 */
	public void setDietAgent(Agent dietAgent) {
		this.agentManaged = dietAgent;
	}

	/**
	 * The agent that is managed
	 * 
	 * @return the agent description
	 */
	public Agent getDietAgent() {
		return agentManaged;
	}

	@Override
	public Node getPluggedOn() {
		if (agentManaged != null) {
			return agentManaged.getConfig().getServer();
		}else return null;

	}

}
