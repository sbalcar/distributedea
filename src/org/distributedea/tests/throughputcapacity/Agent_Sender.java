package org.distributedea.tests.throughputcapacity;

import jade.content.onto.Ontology;
import jade.core.behaviours.SimpleBehaviour;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.PlatformController;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.ResultOntology;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.datasetdescription.DatasetDescriptionMF;
import org.distributedea.ontology.datasetdescription.matrixfactorization.RatingIDsEmptySet;
import org.distributedea.ontology.datasetdescription.matrixfactorization.RatingIDsFullSet;
import org.distributedea.ontology.individuals.IndividualLatentFactors;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.individualwrapper.IndividualsWrappers;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.problem.ProblemMatrixFactorization;
import org.distributedea.ontology.problem.matrixfactorization.latentfactor.LatFactRange;
import org.distributedea.ontology.problemtooldefinition.ProblemToolDefinition;
import org.distributedea.problemtools.matrixfactorization.ProblemToolMFColaborative1RandomInMatrix;
import org.distributedea.problemtools.matrixfactorization.latentfactor.tools.ToolFitnessRMSEMF;
import org.distributedea.problemtools.matrixfactorization.latentfactor.tools.ToolGenerateIndividualMF;
import org.distributedea.problemtools.matrixfactorization.latentfactor.tools.ToolReadDatasetMF;

/**
 * Agent sender to test the throughput capacity of Jade 
 * @author stepan
 *
 */
public class Agent_Sender extends Agent_DistributedEA {

	private static final long serialVersionUID = 1L;

	@Override
	public List<Ontology> getOntologies() {

		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(ResultOntology.getInstance());
		return ontologies;
	}

	public IAgentLogger getLogger() {
		
		if (logger == null) {
			this.logger = new TrashLogger();
		}
		return logger;
	}
	
	@Override
	protected void setup() {
		
		initAgent();
		registrDF();
		
		this.addBehaviour(new SimpleBehaviourSendIndiv(this));
	}
	
	
	class SimpleBehaviourSendIndiv extends SimpleBehaviour {

		private static final long serialVersionUID = 1L;
		
		private Agent_DistributedEA agent;

		public SimpleBehaviourSendIndiv(Agent_DistributedEA agent) {
			this.agent = agent;
		}
		
		@Override
		public void action() {
			
			// starts receiver
			try {
				startReceiver();
			} catch (ControllerException e) {
				System.out.println("Error - cann't start receiver");
			}
			
			// sleep
			try {
				Thread.sleep(0*50*1000);
			} catch (InterruptedException e1) {
				System.out.println("Error - cann't sleep");
			}

			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			System.out.println("Sender:   " + dateFormat.format(cal.getTime()));
			

			ProblemMatrixFactorization problemMF =
					new ProblemMatrixFactorization(
					new LatFactRange(), new LatFactRange(), 10);

			File file =
//					new File("inputs" +  File.separator + "ml-100k" +  File.separator + "u.data");
//					new File("inputs" +  File.separator + "ml-1m" +  File.separator + "ratings.dat");
					new File("inputs" +  File.separator + "ml-10M100K" +  File.separator + "ratings.dat");

			DatasetDescriptionMF datasetDescr = new DatasetDescriptionMF(
					file, new RatingIDsFullSet(), file, new RatingIDsEmptySet(), null, null);
					
			DatasetMF datasetMF = ToolReadDatasetMF.readDatasetWithoutContent(
					datasetDescr, problemMF, new TrashLogger());
			System.out.println("Dataset readed");
			
			
			Calendar cal2 = Calendar.getInstance();
			System.out.println("Sender:   " + dateFormat.format(cal2.getTime()));
			
			
			// generate individuals
			IndividualsWrappers indivWrps = new IndividualsWrappers(
					new ArrayList<IndividualWrapper>());
			for (int i = 0; i < 16; i++) {
				indivWrps.addIndividualWrapper(
						readIndividualWrp(problemMF, datasetMF));
			}
			System.out.println("Sender:   - individuals generated");
			
			
			Calendar cal3 = Calendar.getInstance();
			System.out.println("Sender:   " + dateFormat.format(cal3.getTime()));
			
			//send individual to receiver
			//ReceiverService.sendAsObjectIndividualToReceiver(agent, indivWrps, logger);
			ReceiverService.sendAsOntologyIndividualToReceiver(agent, indivWrps, logger);
		}

		@Override
		public boolean done() {
			return true;
		}
		
	}
	
	private void startReceiver() throws ControllerException {
	
		// get a container controller
		PlatformController container = this.getContainerController();
		
		Object[] jadeArgs = null;
		AgentController createdAgent = container.createNewAgent(
				"Receiver", Agent_Receiver.class.getName(), jadeArgs);
		
		createdAgent.start();
		
		// provide agent time to register with DF etc.
		this.doWait(300);
	}
	
	private IndividualWrapper readIndividualWrp(
			ProblemMatrixFactorization problemMF, DatasetMF datasetMF) {
		
		IndividualLatentFactors individualLF = 
				ToolGenerateIndividualMF.generateIndividual(
						problemMF, datasetMF, new TrashLogger());
		
		double fitness = ToolFitnessRMSEMF.evaluateTrainingDataset(individualLF, problemMF,
				datasetMF, new TrashLogger());
		
		IndividualEvaluated indivE = new IndividualEvaluated(individualLF,
				fitness, null);
		
		JobID jobID = new JobID("batchName", "jobName", 0);
		
		AgentConfiguration agentConfiguration = new AgentConfiguration(
				"Sender", Agent_Sender.class, new Arguments());
		
		ProblemToolDefinition probToolDef =
				new ProblemToolDefinition(new ProblemToolMFColaborative1RandomInMatrix());
		MethodDescription methodDescription = new MethodDescription(
				agentConfiguration, problemMF, probToolDef);
		
		return new IndividualWrapper(jobID, methodDescription, indivE);
	}
}
