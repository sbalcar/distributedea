package org.distributedea.ontology.configuration;

import jade.content.Concept;


public class Argument implements Concept {
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String value;
	private Boolean sendOnlyValue = false;
	
	public Argument(String name, String value) {
		this.value = value;
		this.name = name;
	}
	
	public Argument() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Boolean getSendOnlyValue() {
		return sendOnlyValue;
	}

	public void setSendOnlyValue(Boolean sendOnlyValue) {
		this.sendOnlyValue = sendOnlyValue;
	}

	
	
	@Override
	public boolean equals(Object other) {
		
	    if (!(other instanceof Argument)) {
	        return false;
	    }
	    
	    Argument argumentOuther = (Argument)other;
	    
	    boolean areAgentNameEqual =
	    		this.getName().equals(argumentOuther.getName());
	    boolean areAgentValuesEqual =
	    		this.getValue().equals(argumentOuther.getValue());
	    boolean areSendOnlyValuesEqual =
	    		this.getSendOnlyValue() == argumentOuther.getSendOnlyValue();
	    
	    if (areAgentNameEqual && areAgentValuesEqual && 
	    		areSendOnlyValuesEqual) {
	    	return true;
	    } else {
	    	return false;
	    }
	}
	
    @Override
    public int hashCode() {
    	return toString().hashCode();
    }
    
	@Override
	public String toString() {
		return name + value + sendOnlyValue;
	}
}
