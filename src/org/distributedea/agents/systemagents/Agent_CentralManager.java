package org.distributedea.agents.systemagents;

import jade.content.onto.Ontology;
import jade.core.AID;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.agents.computingagents.computingagent.ComputingAgentService;
import org.distributedea.agents.systemagents.centralmanager.Scheduler;
import org.distributedea.agents.systemagents.manageragent.ManagerAgentService;
import org.distributedea.configuration.AgentConfiguration;
import org.distributedea.configuration.AgentConfigurations;
import org.distributedea.configuration.XmlConfigurationProvider;
import org.distributedea.ontology.ComputingOntology;
import org.distributedea.ontology.LogOntology;
import org.distributedea.ontology.ManagementOntology;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problem.ProblemTSP;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.tsp.permutation.ProblemToolSimpleSwap;

public class Agent_CentralManager extends Agent_DistributedEA {

	private static final long serialVersionUID = 1L;

	private ProblemTool problemTool = new ProblemToolSimpleSwap();
	
	/**
     * Returns list of all ontologies that are used by CentralManager agent.
     */
	@Override
	public List<Ontology> getOntologies() {
		
		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(LogOntology.getInstance());
		ontologies.add(ManagementOntology.getInstance());
		ontologies.add(ComputingOntology.getInstance());
		
		return ontologies;
	}
	
	@Override
	protected void setup() {
		
		initAgent();
		registrDF();
		
		try {
			Thread.sleep(15 * 1000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		AID [] aidManagerAgents = searchDF(Agent_ManagerAgent.class.getName());
		
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
		
		
		Scheduler scheduler = new Scheduler();
		scheduler.run(this, aidManagerAgents, configurations, logger);
		

		BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
		
		logger.log(Level.INFO, "Welcome in DistribudetEA");
		while (true) {
			
			String line = null;
			try {
				line = buffer.readLine();
			} catch (IOException e) {
				logger.logThrowable("Problem with reading from command line", e);
			}
			
			if (line.equals("kill")) {
				logger.log(Level.INFO, "Killing everything");
				
				for (AID aManagerI : aidManagerAgents) {
					ManagerAgentService.sendKillContainer(this, aManagerI, logger);
				}
				
			} else if (line.equals("start")) {
				logger.log(Level.INFO, "Starting everything");
				
				AID computingAgent = new AID("Agent_HillClimbing-17", false);

				String tspFileName = org.distributedea.Configuration.getInputFile("it16862.tsp");

				Problem problemM = problemTool.readProblem(tspFileName, logger);
				ProblemTSP problem = (ProblemTSP) problemM;
				problem.setProblemToolClass(ProblemToolSimpleSwap.class.getName());
				
				ComputingAgentService.sendStartComputing(this, computingAgent, problem, logger);
				
			} else {
				logger.log(Level.INFO, "I don't understand you \n" + 
						"   start - Starting computing \n" +
						"   kill  - Killing everything");
			}
			
		}	
	}

	
}
