package org.distributedea.agents.systemagents;

import jade.content.onto.Ontology;
import jade.core.AID;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
import org.distributedea.ontology.problem.ProblemTSP;
import org.distributedea.ontology.problem.tsp.PositionGPS;

public class Agent_CentralManager extends Agent_DistributedEA {

	private static final long serialVersionUID = 1L;

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
				configProvider.getConfiguration(this, fileName);

		List<AgentConfiguration> agentConfigurations = configuration
				.getAgentConfigurations();
		
		AgentConfiguration[] configurations = 
				agentConfigurations.toArray(
				new AgentConfiguration[agentConfigurations.size()]);
		
		
		Scheduler scheduler = new Scheduler();
		scheduler.run(this, aidManagerAgents, configurations);
		

		BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
		
		logInfo("Welcome in DistribudetEA");
		while (true) {
			
			String line = null;
			try {
				line = buffer.readLine();
			} catch (IOException e) {
				logException("Problem with reading from command line", e);
			}
			
			if (line.equals("kill")) {
				logInfo("Killing everything");
				
				for (AID aManagerI : aidManagerAgents) {
					ManagerAgentService.sendKillContainer(this, aManagerI);
				}
				
			} else if (line.equals("start")) {
				logInfo("Starting everything");
				
				AID computingAgent = new AID("Agent_TSP1-17", false);

				String tspFileName = org.distributedea.Configuration.getInputFile("it16862.tsp");

				ProblemTSP problem = readProblemTSP(tspFileName);
				
				ComputingAgentService.sendStartComputing(this, computingAgent, problem);
				
			} else {
				logInfo("I don't understand you /n" + 
						"   start - Starting computing" +
						"   kill  - Killing everything");
			}
			
		}	
	}
	
	/**
	 * Reads TSP Problem from the file
	 * 
	 * @param tspFileName
	 * @return
	 */
	private ProblemTSP readProblemTSP(String tspFileName) {
	
		List<PositionGPS> positions = new ArrayList<PositionGPS>();
		positions.add(new PositionGPS());
		
		BufferedReader br = null;
		 
		try {
 
			String sCurrentLine;
 
			br = new BufferedReader(new FileReader(tspFileName));
 
			while ((sCurrentLine = br.readLine()) != null) {
				
				if (sCurrentLine.startsWith("NAME") ||
					sCurrentLine.startsWith("COMMENT") ||
					sCurrentLine.startsWith("TYPE") ||
					sCurrentLine.startsWith("DIMENSION") ||
					sCurrentLine.startsWith("EDGE_WEIGHT_TYPE") ||
					sCurrentLine.startsWith("NODE_COORD_SECTION") ||
					sCurrentLine.startsWith("EOF") ) {
					
					logInfo(sCurrentLine);
				} else {
					String delims = "[ ]+";
					String[] tokens = sCurrentLine.split(delims);
					
					int number = Integer.parseInt(tokens[0]);
					double latitude = Double.parseDouble(tokens[1]);
					double longitude = Double.parseDouble(tokens[2]);
							
					PositionGPS positionI = new PositionGPS();
					positionI.setNumber(number);
					positionI.setLatitude(latitude /1000);
					positionI.setLongitude(longitude /1000);
					
					positions.add(positionI);
				}
				
			}
 
		} catch (IOException exception) {
			logException("Problem with reading " + tspFileName + " file", exception);
		} finally {
			try {
				if (br != null){
					br.close();
				}
			} catch (IOException ex) {
				logException("Problem with closing the file: " + tspFileName, ex);
			}
		}
		
		ProblemTSP problem = new ProblemTSP();
		problem.setPositions(positions);
		
		return problem;
	}
	
}
