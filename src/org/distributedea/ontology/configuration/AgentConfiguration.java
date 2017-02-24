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
import org.distributedea.ontology.arguments.Argument;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.configurationinput.InputAgentConfiguration;

/**
 * Ontology represents description of {@link Agent_DistributedEA}
 * @author stepan
 *
 */
public class AgentConfiguration implements Concept {
	
	private static final long serialVersionUID = 1L;
	
	private AgentName agentName;
	
	private String agentClassName;
	
	private Arguments arguments;
	
	
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
	public AgentConfiguration(AgentName agentName, Class<?> agentClass,
			Arguments arguments) {
		
		if (agentName == null ||
				! agentName.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Arguments " +
					AgentName.class.getSimpleName() + " is not valid");			
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
		
		this.agentName = new AgentName(agentName);
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
		this.agentName = new AgentName(agentClass.getSimpleName());
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
		
		this.agentName = new AgentName(agentConf.getAgentName());
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
		
		setAgentName(agentConf.getAgentName().deepClone());
		
		importAgentClass(agentConf.exportAgentClass());
		
		setArguments(agentConf.getArguments().deepClone());
	}

	public AgentName getAgentName() {
		return agentName;
	}
	public void setAgentName(AgentName agentName) {
		if (agentName == null ||
				! agentName.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					AgentName.class.getSimpleName() + " is not valid");
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

	public void incrementNumberOfContainer() {
		this.getAgentName().incrementNumberOfContainer();
	}
	
	public void incrementNumberOfAgent() {
		this.getAgentName().incrementNumberOfAgent();
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
		
		return getAgentName().exportAgentname(
				exportIsAgentWitoutSuffix());		
	}
	
	public String exportContainerSuffix() {
		
		return getAgentName().exportContainerSuffix();
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
				getAgentName().getAgentName(),
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
	    
	    return aregetAgentNamesEqual && areAgentTypeEqual &&
	    		areArgumentsEqual;
	}
	
    @Override
    public int hashCode() {
    	return toString().hashCode();
    }
    
	@Override
	public String toString() {
		
		return exportAgentname() + "-" + getArguments().toString();
	}
}
