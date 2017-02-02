package org.distributedea.ontology.method;

import org.distributedea.agents.systemagents.centralmanager.structures.problemtools.ProblemTools;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.configurationinput.InputAgentConfigurations;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;

public interface IMethods {

	public Methods exportInputMethodDescriptions();
	
	public InputAgentConfigurations exportInputAgentConfigurations();
	public ProblemTools exportProblemTools();
	
	public InputMethodDescription exportRandomSelectedAgentDescription();
	
	public boolean valid(IAgentLogger logger);
	
	public IMethods deepClone();
}
