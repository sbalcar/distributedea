package org.distributedea.ontology.method;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.systemagents.centralmanager.structures.problemtools.ProblemTools;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.configurationinput.InputAgentConfiguration;
import org.distributedea.ontology.configurationinput.InputAgentConfigurations;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;
import org.distributedea.problems.IProblemTool;

import jade.content.Concept;

public class MethodsTwoSets implements IMethods, Concept{

	private static final long serialVersionUID = 1L;

	/**
	 * Declares the set of algorithm
	 */
	private InputAgentConfigurations algorithms;
	
	/**
	 * Declares the set of {@link ProblemTool}
	 */
	private ProblemTools problemTools;

	
	@Deprecated
	public MethodsTwoSets() { // Only for JADE
	}
	
	/**
	 * Constructor
	 * @param algorithms
	 * @param problemTools
	 */
	public MethodsTwoSets(InputAgentConfigurations algorithms,
			ProblemTools problemTools) {
		if (algorithms == null || ! algorithms.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					InputAgentConfigurations.class.getSimpleName() + " is not valid");
		}
		if (problemTools == null || ! problemTools.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					ProblemTools.class.getSimpleName() + " is not valid");
		}

		this.algorithms = algorithms;
		this.problemTools = problemTools;
	}
	
	public MethodsTwoSets(MethodsTwoSets methodsTwoSets) {
		if (methodsTwoSets == null || ! methodsTwoSets.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodsTwoSets.class.getSimpleName() + " is not valid");
		}
		
		this.algorithms = methodsTwoSets.getAlgorithms().deepClone();
		this.problemTools = methodsTwoSets.getProblemTools().deepClone();
	}
	
	public InputAgentConfigurations getAlgorithms() {
		return algorithms;
	}
	@Deprecated
	public void setAlgorithms(InputAgentConfigurations algorithms) {
		this.algorithms = algorithms;
	}

	public ProblemTools getProblemTools() {
		return problemTools;
	}	
	@Deprecated
	public void setProblemTools(ProblemTools problemTools) {
		if (problemTools == null || ! problemTools.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					ProblemTools.class.getSimpleName() + " is not valid");
		}
		this.problemTools = problemTools;
	}

	
	@Override
	public InputAgentConfigurations exportInputAgentConfigurations() {
		return algorithms.deepClone();
	}
	public ProblemTools exportProblemTools() {
		return this.problemTools.deepClone();
	}
	
	@Override
	public Methods exportInputMethodDescriptions() {
		
		List<InputMethodDescription> methods = new ArrayList<>();
		
		for (InputAgentConfiguration confI :
			getAlgorithms().getAgentConfigurations()) {
			
			for (Class<?> probToolI :
				getProblemTools().getProblemTools()) {
				
				InputMethodDescription methodI =
						new InputMethodDescription(confI.deepClone(), probToolI);
				methods.add(methodI);
			}
		}
		return new Methods(methods);
	}

	@Override
	public InputMethodDescription exportRandomMethodDescription() {
		
		InputAgentConfiguration agentConf =
				getAlgorithms().exportRandomAgentConfiguration();
		IProblemTool problemTool = getProblemTools()
				.exportRandomSelectedProblemTool(new TrashLogger());
		
		return new InputMethodDescription(agentConf.deepClone(),
				problemTool.getClass());
	}
	
	
	@Override
	public boolean valid(IAgentLogger logger) {
		
		if (algorithms == null || ! algorithms.valid(logger)) {
			return false;
		}
		if (problemTools == null || ! problemTools.valid(logger)) {
			return false;
		}
		
		return true;
	}

	@Override
	public IMethods deepClone() {

		return new MethodsTwoSets(this);
	}

}
