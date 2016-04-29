package org.distributedea.ontology.configuration;

import java.util.List;


public class AgentConfiguration {
	
	private String agentName;
	private String agentType;
	private List<Argument> arguments;

	public AgentConfiguration() {}
	
	public AgentConfiguration(String agentName, String agentType,
			List<Argument> arguments) {
		
		this.agentName = agentName;
		this.agentType = agentType;
		this.arguments = arguments;
	}

	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public void editAgentName() {
		int index = agentName.lastIndexOf('_');
		this.agentName = agentName.substring(0, index);
	}
	
	public String getAgentType() {
		return agentType;
	}
	public void setAgentType(String agentType) {
		this.agentType = agentType;
	}
	
	public List<Argument> getArguments() {
		return arguments;
	}
	public void setArguments(List<Argument> arguments) {
		this.arguments = arguments;
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
