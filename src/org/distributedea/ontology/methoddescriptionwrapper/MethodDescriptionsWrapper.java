package org.distributedea.ontology.methoddescriptionwrapper;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.methoddescription.MethodDescription;

public class MethodDescriptionsWrapper {

	private List<MethodDescriptionWrapper> methodDescriptionsWrapper;

	
	public List<MethodDescriptionWrapper> getMethodDescriptionsWrapper() {
		return methodDescriptionsWrapper;
	}
	public void setMethodDescriptionsWrapper(
			List<MethodDescriptionWrapper> methodDescriptionsWrapper) {
		this.methodDescriptionsWrapper = methodDescriptionsWrapper;
	}

	public void addMethodDescriptionWrapper(MethodDescriptionWrapper description) {
			
		if (this.methodDescriptionsWrapper == null) {
			this.methodDescriptionsWrapper = new ArrayList<>();
		}
		this.methodDescriptionsWrapper.add(description);
	}
	
	public MethodDescriptionsWrapper exportExploitationMethodDescriptionsWrapper() {
		
		MethodDescriptionsWrapper exploitationMethods = new MethodDescriptionsWrapper();
		
		for (MethodDescriptionWrapper descriptionWrpI : methodDescriptionsWrapper) {
			
			MethodDescription descriptionI = descriptionWrpI.getMethodDescription();
			
			if (descriptionI.isExploitation()) {
				exploitationMethods.addMethodDescriptionWrapper(descriptionWrpI);
			}
		}
		
		return exploitationMethods;
	}
	
	public MethodDescriptionsWrapper exportExplorationMethodDescriptions() {
		
		MethodDescriptionsWrapper explorationMethods = new MethodDescriptionsWrapper();
		
		for (MethodDescriptionWrapper descriptionWrpI : methodDescriptionsWrapper) {
			
			MethodDescription descriptionI = descriptionWrpI.getMethodDescription();
			
			if (descriptionI.isExploration()) {
				explorationMethods.addMethodDescriptionWrapper(descriptionWrpI);
			}
		}
		
		return explorationMethods;
	}
	
	public List<AgentConfiguration> exportAgentConfigurations() {
		
		List<AgentConfiguration> configurations = new ArrayList<>();
		
		for (MethodDescriptionWrapper methDesWrpI : methodDescriptionsWrapper) {
			AgentConfiguration agentConfigurationI =
					methDesWrpI.getAgentConfiguration();
			
			configurations.add(agentConfigurationI);
		}
		
		return configurations;
	}
	
	public List<Class<?>> exportAgentTypes() {
		
		List<Class<?>> agentClass = new ArrayList<>();
		
		List<AgentConfiguration> configurations = exportAgentConfigurations();
		for (AgentConfiguration configurationI : configurations) {
			
			Class<?> agentTypeI = configurationI.exportAgentType();
			if (! agentClass.contains(agentTypeI)) {
				agentClass.add(agentTypeI);
			}
		}
		
		return agentClass;
	}
}
