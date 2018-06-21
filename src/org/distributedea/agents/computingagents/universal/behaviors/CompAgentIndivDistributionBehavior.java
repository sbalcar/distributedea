package org.distributedea.agents.computingagents.universal.behaviors;

import java.util.logging.Level;

import org.distributedea.agents.computingagents.universal.Agent_ComputingAgent;
import org.distributedea.agents.computingagents.universal.queuesofindividuals.IReadyToSendIndividualsModel;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.islandmodel.IslandModelConfiguration;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problemwrapper.ProblemStruct;
import org.distributedea.services.ComputingAgentService;

import jade.core.behaviours.TickerBehaviour;

public class CompAgentIndivDistributionBehavior extends TickerBehaviour {

	private Agent_ComputingAgent agent;
	private IReadyToSendIndividualsModel readyToSendIndividuals;
	
	private MethodDescription methDescription;
	private ProblemStruct problemStruct;
	private IslandModelConfiguration islandModelConf;
	private IAgentLogger logger;
	
	public CompAgentIndivDistributionBehavior(Agent_ComputingAgent agent,
			IReadyToSendIndividualsModel readyToSendIndividuals,
			MethodDescription methDescription, ProblemStruct problemStruct,
			IslandModelConfiguration islandModelConf, IAgentLogger logger) {
		super(agent, islandModelConf.getIndividualBroadcastPeriodMS());
		
		this.agent = agent;
		this.readyToSendIndividuals = readyToSendIndividuals;
		
		this.methDescription = methDescription;
		this.problemStruct = problemStruct;
		this.islandModelConf = islandModelConf;
		this.logger = logger;
	}

	private static final long serialVersionUID = 1L;

	@Override
	protected void onTick() {
		 logger.log(Level.INFO, "Tick to send individual");
		 
		 IProblem problem = problemStruct.getProblem();
		 
		 IndividualEvaluated individualEval =
				 readyToSendIndividuals.getIndividual(problem);
		 
		 if (individualEval == null) {
			 logger.log(Level.WARNING, "Any Individual to send availible");
			 return;
		 }
		 
		 IndividualWrapper individualWrapper = new IndividualWrapper(
				 problemStruct.getJobID(), methDescription, individualEval);
		
		 if (islandModelConf.isIndividualDistribution()) {
			 ComputingAgentService.sendIndividualToNeighbours(
					 agent, individualWrapper, islandModelConf, logger);
		 }
		 ComputingAgentService.sendIndividualToMonitor(
				 agent, individualWrapper, logger);		 
	}

}
