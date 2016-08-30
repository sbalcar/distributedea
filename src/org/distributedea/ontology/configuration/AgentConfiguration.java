package org.distributedea.ontology.configuration;

import jade.content.Concept;
import jade.core.AID;
import jade.core.Agent;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.Configuration;
import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.agents.computingagents.Agent_BruteForce;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.configuration.inputconfiguration.InputAgentConfiguration;

/**
 * Ontology represents description of {@link Agent_DistributedEA}
 * @author stepan
 *
 */
public class AgentConfiguration implements Concept {
	
	private static final long serialVersionUID = 1L;
	
	private String agentName;
	private String agentClassName;
	private Arguments arguments;

	private String containerID = "";
	
	private int numberOfContainer = 0;
	private int numberOfAgent = 0;
	
	@Deprecated
	public AgentConfiguration() {   // only for Jade
		this.arguments = new Arguments(new ArrayList<Argument>());
	}
	
	/**
	 * Constructor
	 * @param agentName
	 * @param agentClass
	 * @param arguments
	 */
	public AgentConfiguration(String agentName, Class<?> agentClass,
			Arguments arguments) {
		if (agentName == null || agentName.isEmpty()) {
			throw new IllegalArgumentException("Arguments " +
					String.class.getSimpleName() + " is not valid");			
		}
		if (arguments == null ||
				! arguments.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Arguments " +
				Arguments.class.getSimpleName() + " is not valid");
		}
		
		this.agentName = agentName;
		this.importAgentClass(agentClass);
		this.arguments = arguments;
	}

	/**
	 * Constructor, name is simple name of {@link Agent} Class
	 * @param agentType
	 * @param arguments
	 */
	public AgentConfiguration(Class<?> agentClass,
			Arguments arguments) {
		if (agentClass == null) {
			throw new IllegalArgumentException("Argument " +
					Class.class.getSimpleName() + " is not valid");
		}
		if (arguments == null ||
				! arguments.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Arguments.class.getSimpleName() + " is not valid");
		}
		this.agentName = agentClass.getSimpleName();
		this.importAgentClass(agentClass);
		this.arguments = arguments;
	}

	/**
	 * Constructor
	 * @param agentConf
	 */
	public AgentConfiguration(InputAgentConfiguration agentConf) {
		if (agentConf == null || ! agentConf.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					InputAgentConfiguration.class.getSimpleName() + " is not valid");
		}
		
		this.agentName = agentConf.getAgentName();
		this.importAgentClass(agentConf.exportAgentClass());
		this.arguments = agentConf.getArguments().deepClone();
	}
	
	/**
	 * Copy constructor
	 * @param agentConf
	 */
	public AgentConfiguration(AgentConfiguration agentConf) {
		
		if (agentConf == null || ! agentConf.valid(new TrashLogger())) {
			throw new IllegalArgumentException();
		}
		
		setAgentName(agentConf.getAgentName());
		importAgentClass(agentConf.exportAgentClass());
		
		setArguments(agentConf.getArguments().deepClone());
		
		setContainerID(agentConf.getContainerID());
		setNumberOfContainer(agentConf.getNumberOfContainer());
		setNumberOfAgent(agentConf.getNumberOfAgent());
	}

	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		if (agentName == null || agentName.isEmpty() ||
				agentName.contains(" ")) {
			throw new IllegalArgumentException("Argument " +
					String.class.getSimpleName() + " is not valid");
		}
		this.agentName = agentName;
	}
	@Deprecated
	public String getAgentClassName() {
		return agentClassName;
	}
	@Deprecated
	public void setAgentClassName(String agentType) {
		this.agentClassName = agentType;
	}
	/**
	 * Exports {@link Class} of agent
	 * @return
	 */
	public Class<?> exportAgentClass() {
		try {
			return Class.forName(getAgentClassName());
		} catch (ClassNotFoundException e1) {
			return null;
		}
	}
	/**
	 * Imports {@link Class} of agent
	 * @param agentClass
	 */
	public void importAgentClass(Class<?> agentClass) {
		if (agentClass == null) {
			throw new IllegalArgumentException("Argument " +
					Class.class.getSimpleName() + " can not be null");
		}
		this.agentClassName = agentClass.getName();
	}
	
	public Arguments getArguments() {
		return arguments;
	}
	public void setArguments(Arguments arguments) {
		if (arguments == null ||
				! arguments.valid(new TrashLogger())) {
			throw new IllegalArgumentException();
		}
		this.arguments = arguments;
	}

	public String getContainerID() {
		return containerID;
	}
	public void setContainerID(String containerID) {
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
		if (agentClassName.startsWith(namespace)) {
			return true;
		}
		
		return false;
	}
	
	public boolean exportIsAgentWitoutSuffix() {
		
		Class<?> agentTypeClass = exportAgentClass();
		
		// starts agents which are in system only one-times
		List<Class<?>> uniqueAgentList = Configuration.agentsWithoutSuffix();
		
		if (uniqueAgentList.contains(agentTypeClass)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Exports name of {@link Agent_DistributedEA}
	 * @return
	 */
	public String exportAgentname() {
		
		if (exportIsAgentWitoutSuffix()) {
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
	
	/**
	 * Exports Jade AID specification of Agent
	 * @return
	 */
	public AID exportAgentAID() {
		
		String agentName = exportAgentname();
		try {
			return new AID(agentName, false);
		} catch(Exception e) {
			return null;
		}
	}
	
	/**
	 * Exports {@link InputAgentConfiguration}
	 * @return
	 */
	public InputAgentConfiguration exportInputAgentConfiguration() {
		if (! valid(new TrashLogger())) {
			return null;
		}
		return new InputAgentConfiguration(
				getAgentName(),
				exportAgentClass(),
				getArguments().deepClone());
	}
	
	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (exportAgentClass() == null) {
			return false;
		}
		if (arguments == null || ! arguments.valid(logger)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Returns Clone
	 * @return
	 */
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
	    		this.exportAgentClass() == acOuther.exportAgentClass();
	    boolean areArgumentsEqual =
	    		this.getArguments().equals(acOuther.getArguments());
	    
	    boolean areContainerIDEqual =
	    		this.containerID.equals(acOuther.getContainerID());
	    boolean areNumberOfContainerEqual =
	    		this.numberOfContainer == acOuther.getNumberOfContainer();
	    boolean areNumberOfAgentEqual =
	    		this.numberOfAgent == acOuther.getNumberOfAgent();

	    
	    return aregetAgentNamesEqual && areAgentTypeEqual &&
	    		areArgumentsEqual && areContainerIDEqual &&
	    		areNumberOfContainerEqual && areNumberOfAgentEqual;
	}
	
    @Override
    public int hashCode() {
    	return toString().hashCode();
    }
    
	@Override
	public String toString() {
		
		return exportAgentname();
	}
}
