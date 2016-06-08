package org.distributedea.ontology.configuration;

import jade.core.AID;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.Configuration;
import org.distributedea.agents.computingagents.Agent_BruteForce;


public class AgentConfiguration {
	
	private String agentName;
	private String agentType;
	private List<Argument> arguments;

	private int containerID;
	private int numberOfContainer = 0;
	private int numberOfAgent = 0;
	
	public AgentConfiguration() {}
	
	public AgentConfiguration(String agentName, String agentType,
			List<Argument> arguments) {
		
		this.agentName = agentName;
		this.agentType = agentType;
		this.arguments = arguments;
	}

	public AgentConfiguration(Class<?> agentType,
			List<Argument> arguments) {
		
		this.agentName = agentType.getSimpleName();
		this.agentType = agentType.getName();
		this.arguments = arguments;
	}
	
	public AgentConfiguration(AgentConfiguration agentConfI) {
		
		setAgentName(agentConfI.getAgentName());
		setAgentType(agentConfI.getAgentType());
		
		List<Argument> argumentsClone = new ArrayList<>();
		for (Argument argumentI : agentConfI.getArguments()) {
			
			Argument argumentCloneI = new Argument(argumentI);
			argumentsClone.add(argumentCloneI);
		}
		setArguments(argumentsClone);
		
		setContainerID(agentConfI.getContainerID());
		setNumberOfContainer(agentConfI.getNumberOfContainer());
		setNumberOfAgent(agentConfI.getNumberOfAgent());
	}

	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	
	public String getAgentType() {
		return agentType;
	}
	public void setAgentType(String agentType) {
		this.agentType = agentType;
	}
	public Class<?> exportAgentType() {
		try {
			return Class.forName(getAgentType());
		} catch (ClassNotFoundException e1) {
			return null;
		}
	}
	
	public List<Argument> getArguments() {
		return arguments;
	}
	public void setArguments(List<Argument> arguments) {
		this.arguments = arguments;
	}	
	
	
	public int getContainerID() {
		return containerID;
	}
	public void setContainerID(int containerID) {
		this.containerID = containerID;
	}

	public int getNumberOfContainer() {
		return numberOfContainer;
	}
	public void setNumberOfContainer(int numberOfContainer) {
		this.numberOfContainer = numberOfContainer;
	}
	public void incrementNumberOfContainer() {
		this.numberOfContainer++;
	}
	
	public int getNumberOfAgent() {
		return numberOfAgent;
	}
	public void setNumberOfAgent(int numberOfAgent) {
		this.numberOfAgent = numberOfAgent;
	}
	public void incrementNumberOfAgent() {
		this.numberOfAgent++;
	}
	
	public boolean exportIsComputingAgent() {
		
		String namespace = Agent_BruteForce.class.getPackage().getName();
		if (agentType.startsWith(namespace)) {
			return true;
		}
		
		return false;
	}
	
	public boolean exportIsAgentWitoutSuffix() {
		
		Class<?> agentTypeClass = exportAgentType();
		
		// starts agents which are in system only one-times
		List<Class<?>> uniqueAgentList = Configuration.agentsWithoutSuffix();
		
		if (uniqueAgentList.contains(agentTypeClass)) {
			return true;
		}
		
		return false;
	}
	
	public String exportAgentname() {
		
		if (exportIsAgentWitoutSuffix()) {
			return getAgentName();
		}
		
		String  agentChar = "";
		if (numberOfAgent >= 0) {
			agentChar = "" + numberOfAgent;
		}
		
		String agentNameWitID = agentName + agentChar;
		String containerNameWitID = exportContainerSuffix();
		
		String agentFullName = agentNameWitID +
				Configuration.CONTAINER_NUMBER_PREFIX +
				containerNameWitID;
		
		return agentFullName;
	}
	
	public String exportContainerSuffix() {
		
		String  containerChar = "";
		if (numberOfContainer >= 0) {
			char cChar = (char) ('a' + numberOfContainer);
			containerChar = "" + cChar;
		}
		
		return containerID + containerChar;
	}
	
	public AID exportAgentAID() {
		
		String agentName = exportAgentname();
		return new AID(agentName, false);
	}
	
	public AgentConfiguration deepClone() {
		return new AgentConfiguration(this);
	}
	
	@Override
	public boolean equals(Object other) {
		
	    if (!(other instanceof AgentConfiguration)) {
	        return false;
	    }
	    
	    AgentConfiguration acOuther = (AgentConfiguration)other;
	    
	    boolean aregetAgentNamesEqual =
	    		this.getAgentName().equals(acOuther.getAgentName());
	    boolean areAgentTypeEqual =
	    		this.getAgentType().equals(acOuther.getAgentType());
	    
	    if (getArguments() == null && acOuther.getArguments() == null) {
	    	return aregetAgentNamesEqual && areAgentTypeEqual;
	    }
	    
	    if ((getArguments() == null && acOuther.getArguments() != null) ||
	    		(getArguments() != null && acOuther.getArguments() == null)) {
	    	return false;
	    }
	    
	    if (getArguments().size() != acOuther.getArguments().size()) {
	    	return false;
	    }
    	for (int argumentIndex = 0;
    			argumentIndex < getArguments().size(); argumentIndex++) {
    		
    		Argument argThis = getArguments().get(argumentIndex);
    		Argument argOther = acOuther.getArguments().get(argumentIndex);
    		
    		if (! argThis.equals(argOther)) {
    			return false;
    		}
    	}
	    
	    return true;
	}
	
    @Override
    public int hashCode() {
    	return toString().hashCode();
    }
    
	@Override
	public String toString() {
		
		String value = agentName + agentType;
		if (arguments != null) {
			for (Argument argumentI : arguments) {
				value += argumentI.toString();
			}
		}
		return value;
	}
}
