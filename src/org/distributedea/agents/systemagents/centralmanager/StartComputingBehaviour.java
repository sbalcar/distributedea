package org.distributedea.agents.systemagents.centralmanager;

import java.util.List;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.scheduler.Scheduler;
import org.distributedea.agents.systemagents.centralmanager.scheduler.SchedulerSimple;
import org.distributedea.configuration.AgentConfiguration;
import org.distributedea.configuration.AgentConfigurations;
import org.distributedea.configuration.XmlConfigurationProvider;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.problem.ProblemTSPPoint;
import org.distributedea.problems.tsp.gps.permutation.ProblemToolGPSEuc2D2opt;

import jade.core.behaviours.OneShotBehaviour;

public class StartComputingBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;

	private AgentLogger logger;
	
	public StartComputingBehaviour(AgentLogger logger) {
		this.logger = logger;
	}
	
	@Override
	public void action() {
		startCommand();
		
	}

	
	protected void startCommand() {
		
		String fileName = org.distributedea.Configuration.getMethodsFile();
		
		XmlConfigurationProvider configProvider =
				new XmlConfigurationProvider();
		AgentConfigurations configuration =
				configProvider.getConfiguration(fileName, logger);

		List<AgentConfiguration> agentConfigurations = configuration
				.getAgentConfigurations();
		
		AgentConfiguration[] configurations = 
				agentConfigurations.toArray(
				new AgentConfiguration[agentConfigurations.size()]);
				
		
		String inputFileName = "wi29.tsp";
		Class<?> problemToSolve = ProblemTSPPoint.class;
		Class<?> [] availableProblemTools =
			//{ProblemToolGPSEuc2DSimpleSwap.class, ProblemToolGPSEuc2D2opt.class};
			{ProblemToolGPSEuc2D2opt.class};
		
		
		
		String problemFileName =
				org.distributedea.Configuration.getInputFile(inputFileName);
		
		Agent_CentralManager centralManager = (Agent_CentralManager) myAgent;
		
		Scheduler scheduler = new SchedulerSimple();
		scheduler.agentInitialization(centralManager, configurations,
				problemFileName, problemToSolve, availableProblemTools,
				logger);

		while (true) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			scheduler.replan(centralManager, problemToSolve, logger);
			break;
		}

	}
}
