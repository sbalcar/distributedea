package org.distributedea.ontology.configuration.inputconfiguration;

import jade.content.Concept;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.distributedea.agents.systemagents.initiator.XmlConfigurationProvider;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.agentdescription.inputdescription.InputAgentDescription;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.configuration.AgentConfigurations;


/**
 * Ontology represents list of {@link InputAgentDescription} elements.
 * @author stepan
 *
 */
public class InputAgentConfigurations implements Concept {
	
	private static final long serialVersionUID = 1L;
	
	private List<InputAgentConfiguration> agentConfigurations;

	
	@Deprecated
	public InputAgentConfigurations() { // only for Jade
		this.agentConfigurations = new ArrayList<>();
	}
	
	/**
	 * Constructor
	 * @param agentConfigurations
	 */
	public InputAgentConfigurations(List<InputAgentConfiguration> agentConfigurations) {
		if (agentConfigurations == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " can not be null");
		}
		IAgentLogger logger = new TrashLogger();
		for (InputAgentConfiguration agentConfigurationI : agentConfigurations) {
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
	public InputAgentConfigurations(InputAgentConfigurations agentConfigurations) {
		if (agentConfigurations == null ||
				! agentConfigurations.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}
		
		this.agentConfigurations = new ArrayList<>();
		for (InputAgentConfiguration agentConfI :
				agentConfigurations.getAgentConfigurations()) {
			
			InputAgentConfiguration agentConfCloneI =
					new InputAgentConfiguration(agentConfI);
			addAgentConfigurations(agentConfCloneI);
		}
	}

	public List<InputAgentConfiguration> getAgentConfigurations() {
		return agentConfigurations;
	}
	@Deprecated
	public void setAgentConfigurations(List<InputAgentConfiguration> agentConfigurations) {
		
		if (! new InputAgentConfigurations(agentConfigurations).valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");			
		}
		this.agentConfigurations = agentConfigurations;
	}

	public void addAgentConfigurations(InputAgentConfiguration agentConfiguration) {
		
		if (agentConfiguration == null ||
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
	public InputAgentConfiguration exportRandomAgentConfiguration() {
		if (agentConfigurations == null || agentConfigurations.isEmpty()) {
			return null;
		}
		Random ran = new Random();
		int indexAC = ran.nextInt(agentConfigurations.size());
		return agentConfigurations.get(indexAC);
	}
	public InputAgentConfiguration exportAgentConfigurations(int index) {
		
		if (this.agentConfigurations == null) {
			return null;
		}
		return agentConfigurations.get(index);
	}
	
	/**
	 * Exports {@link AgentConfiguration}s based of given agent class
	 * @param agentClass
	 * @return
	 */
	public List<InputAgentConfiguration> exportAgentConfigurations(Class<?> agentClass) {
		if (agentClass == null) {
			throw new IllegalArgumentException("Argument " +
					Class.class.getSimpleName() + " can not be null");
		}
		
		List<InputAgentConfiguration> selectedAgentConfigurations =
				new ArrayList<>();

		for (InputAgentConfiguration agentConfigurationI : getAgentConfigurations()) {
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
	public List<InputAgentConfiguration> exportsComplement(
			List<InputAgentConfiguration> agentConfigurations) {
		if (agentConfigurations == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " can not be null");
		}
		if (! new InputAgentConfigurations(agentConfigurations).valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}
		
		List<InputAgentConfiguration> complement = new ArrayList<>();
		for (InputAgentConfiguration agentConfI : getAgentConfigurations()) {
			if (! agentConfigurations.contains(agentConfI)) {
				complement.add(agentConfI.deepClone());
			}
		}
		
		return complement;
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
		for (InputAgentConfiguration agentConfigurationI : agentConfigurations) {
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
	public InputAgentConfigurations deepClone() {
		return new InputAgentConfigurations(this);
	}
	
	/**
	 * Import {@link AgentConfigurations} from XML file
	 * @param xmlMethodsFile
	 * @return
	 */
	public static InputAgentConfigurations importFromXML(File xmlMethodsFile) {
		
		InputAgentConfigurations configurations =
				XmlConfigurationProvider.getConfiguration(xmlMethodsFile, new TrashLogger());
		
		return configurations;
	}
}