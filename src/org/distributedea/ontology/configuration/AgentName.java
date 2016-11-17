package org.distributedea.ontology.configuration;

import org.distributedea.Configuration;
import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.logging.IAgentLogger;

import com.thoughtworks.xstream.XStream;

import jade.content.Concept;

/**
 * Ontology represents name of agent
 * @author stepan
 *
 */
public class AgentName implements Concept {
	
	private static final long serialVersionUID = 1L;

	private String agentName;

	private String containerID = "";
	
	private int numberOfContainer = 0;
	private int numberOfAgent = 0;
	
	
	@Deprecated
	public AgentName() {   // only for Jade
	}
	
	/**
	 * Constructor
	 * @param agentName
	 */
	public AgentName(String agentName) {
		this.agentName = agentName;
	}
	
	/**
	 * Copy constructor
	 * @param agentName
	 */
	public AgentName(AgentName agentName) {
		
		this.agentName = agentName.getAgentName();
		
		this.containerID = agentName.getContainerID();
		
		this.numberOfContainer = agentName.getNumberOfContainer();
		this.numberOfAgent = agentName.getNumberOfAgent();
	}

	
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		if (agentName == null) {
			throw new IllegalArgumentException("Argument " + String.class.getSimpleName() +
					" is not valid");
		}
		this.agentName = agentName;
	}
	
	public String getContainerID() {
		return containerID;
	}
	public void setContainerID(String containerID) {
		if (containerID == null) {
			throw new IllegalArgumentException("Argument " + String.class.getSimpleName() +
					" is not valid");
		}
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
	
	/**
	 * Exports name of {@link Agent_DistributedEA}
	 * @return
	 */
	public String exportAgentname(boolean isAgentWitoutSuffix) {
		
		if (isAgentWitoutSuffix) {
			return getAgentName();
		}
		
		String containerNameWitID = exportContainerSuffix();
		
		String agentFullName = agentName + numberOfAgent +
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
		
		if (containerID == null) {
			return containerChar;
		} else {
			return containerID + containerChar;
		}
	}
	
	@Override
	public boolean equals(Object other) {
		
	    if (!(other instanceof AgentName)) {
	        return false;
	    }
	    
	    AgentName anOuther = (AgentName)other;
	    
	    boolean areAgentNamesEqual =
	    		this.getAgentName().equals(anOuther.getAgentName());
	    
	    boolean areContainerIDEqual =
	    		this.containerID.equals(anOuther.getContainerID());
	    boolean areNumberOfContainerEqual =
	    		this.numberOfContainer == anOuther.getNumberOfContainer();
	    boolean areNumberOfAgentEqual =
	    		this.numberOfAgent == anOuther.getNumberOfAgent();

	    
	    return areAgentNamesEqual && areContainerIDEqual &&
	    		areNumberOfContainerEqual && areNumberOfAgentEqual;
	}
	
    @Override
    public int hashCode() {
    	return toString().hashCode();
    }
    
	@Override
	public String toString() {
		
		return exportAgentname(false);
	}
	
	/**
	 * Exports to the XML String
	 */
	public String exportXML() {

		XStream xstream = new XStream();
		xstream.setMode(XStream.NO_REFERENCES);

		return xstream.toXML(this);
	}
	
	/**
	 * Import the {@link AgentName} from the String
	 */
	public static AgentName importXML(String xml) {

		XStream xstream = new XStream();
		xstream.setMode(XStream.NO_REFERENCES);

		return (AgentName) xstream.fromXML(xml);
	}
	
	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (this.agentName == null ||
				this.agentName.isEmpty()) {
			return false;
		}
		return true;
	}
	
	/**
	 * Returns Clone
	 * @return
	 */
	public AgentName deepClone() {
		return new AgentName(this);
	}
	
}
