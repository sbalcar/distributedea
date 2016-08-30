package org.distributedea.ontology.configuration;

import jade.content.Concept;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.configuration.inputconfiguration.InputAgentConfiguration;
import org.distributedea.ontology.configuration.inputconfiguration.InputAgentConfigurations;

/**
 * Ontology represents list of {@link AgentDescription}.
 * @author stepan
 *
 */
public class AgentConfigurations implements Concept {
	
	private static final long serialVersionUID = 1L;
	
	private List<AgentConfiguration> agentConfigurations;

	
	@Deprecated
	public AgentConfigurations() { // only for Jade
		this.agentConfigurations = new ArrayList<>();
	}
	
	/**
	 * Constructor
	 * @param agentConfigurations
	 */
	public AgentConfigurations(List<AgentConfiguration> agentConfigurations) {
		if (agentConfigurations == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " can not be null");
		}
		IAgentLogger logger = new TrashLogger();
		for (AgentConfiguration agentConfigurationI : agentConfigurations) {
			if (agentConfigurationI == null ||
					! agentConfigurationI.valid(logger)) {
				throw new IllegalArgumentException("Argument " +
						List.class.getSimpleName() + " is not valid");
			}
		}
		this.agentConfigurations = agentConfigurations;
	}
	
	/**
	 * Copy Constructor
	 * @param agentConfigurations
	 */
	public AgentConfigurations(AgentConfigurations agentConfigurations) {
		if (agentConfigurations == null ||
				! agentConfigurations.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}
		
		for (AgentConfiguration agentConfI :
				agentConfigurations.getAgentConfigurations()) {
			
			AgentConfiguration agentConfCloneI =
					new AgentConfiguration(agentConfI);
			addAgentConfigurations(agentConfCloneI);
		}
	}

	public List<AgentConfiguration> getAgentConfigurations() {
		return agentConfigurations;
	}
	@Deprecated
	public void setAgentConfigurations(List<AgentConfiguration> agentConfigurations) {
		
		if (! new AgentConfigurations(agentConfigurations).valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");			
		}
		this.agentConfigurations = agentConfigurations;
	}

	public void addAgentConfigurations(AgentConfiguration agentConfiguration) {
		
		if (agentConfigurations == null ||
				! agentConfiguration.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					AgentConfiguration.class.getSimpleName() + " is not valid");
		}
		agentConfigurations.add(agentConfiguration);
	}

	public int exportNumberOfAgentConfigurations() {
		if (agentConfigurations == null) {
			return 0;
		}
		return agentConfigurations.size();
	}
	
	/**
	 * Exports {@link AgentConfiguration}s based of given agent class
	 * @param agentClass
	 * @return
	 */
	public List<AgentConfiguration> exportAgentConfigurations(Class<?> agentClass) {
		if (agentClass == null) {
			throw new IllegalArgumentException("Argument " +
					Class.class.getSimpleName() + " can not be null");
		}
		
		List<AgentConfiguration> selectedAgentConfigurations =
				new ArrayList<AgentConfiguration>();

		for (AgentConfiguration agentConfigurationI : getAgentConfigurations()) {
			Class<?> agentClassI = agentConfigurationI.exportAgentClass();

			if (agentClassI == agentClass) {
				selectedAgentConfigurations.add(agentConfigurationI);
			}
		}
		return selectedAgentConfigurations;
	}

	/**
	 * Returns clone items of complement from given list
	 * @param agentConfigurations
	 * @return
	 */
	public List<AgentConfiguration> exportsComplement(
			List<AgentConfiguration> agentConfigurations) {
		if (agentConfigurations == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " can not be null");
		}
		if (new AgentConfigurations(agentConfigurations).valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}
		
		List<AgentConfiguration> complement = new ArrayList<>();
		for (AgentConfiguration agentConfI : getAgentConfigurations()) {
			if (! agentConfigurations.contains(agentConfI)) {
				complement.add(agentConfI.deepClone());
			}
		}
		
		return complement;
	}
	
	/**
	 * Exports {@link InputAgentConfigurations}
	 * @return
	 */
	public InputAgentConfigurations exportInputAgentConfigurations() {
		
		List<InputAgentConfiguration> inputAgentConfs = new ArrayList<>();
		
		for (AgentConfiguration agentConfI : getAgentConfigurations()) {
			InputAgentConfiguration inputAgentConfI =
					agentConfI.exportInputAgentConfiguration();
			inputAgentConfs.add(inputAgentConfI.deepClone());
		}
		
		return new InputAgentConfigurations(inputAgentConfs);
	}
	
	/**
	 * Tests validity
	 * @param logger
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		
		if (agentConfigurations == null) {
			return false;
		}
		for (AgentConfiguration agentConfigurationI : agentConfigurations) {
			if (agentConfigurationI == null ||
					! agentConfigurationI.valid(logger)) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Returns Clone
	 * @return
	 */
	public AgentConfigurations deepClone() {
		return new AgentConfigurations(this);
	}
	
}
