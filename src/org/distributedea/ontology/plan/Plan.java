package org.distributedea.ontology.plan;

import jade.content.Concept;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.agentdescription.AgentDescriptions;
import org.distributedea.ontology.iteration.Iteration;

import com.thoughtworks.xstream.XStream;

/**
 * Representation of the newly created agents and given {@link Iteration}
 * @author stepan
 *
 */
public class Plan implements Concept {

	private static final long serialVersionUID = 1L;
	
	private Iteration iteration;
	private AgentDescriptions newAgents;

	
	@Deprecated
	public Plan() { // only for Jade
	}

	/**
	 * Constructor for any new method
	 * @param iteration
	 */
	public Plan(Iteration iteration) {
		setIteration(iteration);
		
	}
	/**
	 * Constructor for one new method
	 * @param iteration
	 */
	public Plan(Iteration iteration, AgentDescription newAgent) {
		setIteration(iteration);
		addAgentsToCreate(newAgent);
	}
	/**
	 * Constructor for the list of new methods
	 * @param iteration
	 */
	public Plan(Iteration iteration, AgentDescriptions newAgents) {
		if (iteration == null || ! iteration.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Iteration.class.getSimpleName() + " is not valid");
		}
		if (newAgents == null || ! newAgents.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					AgentDescriptions.class.getSimpleName() + " is not valid");
		}
		this.iteration = iteration;
		this.newAgents = newAgents;
	}

	public Iteration getIteration() {
		return iteration;
	}
	@Deprecated
	public void setIteration(Iteration iteration) {
		if (iteration == null || ! iteration.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Iteration.class.getSimpleName() + " is not valid");
		}
		this.iteration = iteration;
	}

	public AgentDescriptions getNewAgents() {
		return newAgents;
	}
	@Deprecated
	public void setNewAgents(AgentDescriptions newAgents) {
		if (newAgents == null || ! newAgents.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					AgentDescriptions.class.getSimpleName() + " is not valid");
		}
		this.newAgents = newAgents;
	}
	/**
	 * Adds method to create
	 * @param agentToCreate
	 */
	public void addAgentsToCreate(AgentDescription agentToCreate) {
		if (agentToCreate == null ||
				! agentToCreate.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					AgentDescription.class.getSimpleName() + " is not valid");
		}
		
		if (this.newAgents == null) {
			this.newAgents = new AgentDescriptions();
		}
		
		this.newAgents.addAgentDescriptions(agentToCreate);
	}
	
	public static Plan concat(Plan plan1, Plan plan2) {
		
		if (plan1 == null) {
			return plan2;
			
		} else if (plan2 == null) {
			return plan1;
		}
		
		Iteration iteration1 = plan1.getIteration();
		Iteration iteration2 = plan2.getIteration();
		
		if (iteration1.getIterationNumber() != iteration2.getIterationNumber()) {
			return null;
		}
		
		List<AgentDescription> newAgents = new ArrayList<>();
		newAgents.addAll(plan1.getNewAgents().getAgentDescriptions());
		newAgents.addAll(plan2.getNewAgents().getAgentDescriptions());
		
		return new Plan(iteration1, new AgentDescriptions(newAgents));
	}
	
	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (iteration == null || ! iteration.valid(logger)) {
			return false;
		}
		if (newAgents == null) {
			return false;
		}
		for (AgentDescription descriptionI :
				newAgents.getAgentDescriptions()) {
			if (! descriptionI.valid(logger)) {
				return false;
			}
		}
		return true;
	}
	/**
	 * Exports structure as the XML String to the file
	 * 
	 * @throws IOException
	 */
	public void exportXML(File dir) throws IOException {

		if (dir == null || ! dir.isDirectory()) {
			throw new IllegalArgumentException("Argument " +
					File.class.getSimpleName() + " is not valid");
		}
		
		String planFileName = dir.getAbsolutePath() + File.separator +
				iteration.exportNumOfIteration() + ".txt";
		
		String xml = exportXML();

		PrintWriter fileWr = new PrintWriter(planFileName);
		fileWr.println(xml);
		fileWr.close();
	}
	
	/**
	 * Exports to the XML String
	 */
	public String exportXML() {

		XStream xstream = new XStream();
		xstream.setMode(XStream.NO_REFERENCES);

		return xstream.toXML(this);
	}
	
	/**
	 * Import the {@link Plan} from the file
	 * 
	 * @throws IOException
	 */
	public static Plan importXML(File file)
			throws IOException {

		if (file == null || ! file.isFile()) {
			throw new IllegalArgumentException("Argument " +
					File.class.getSimpleName() + " is not valid");
		}
		
		Scanner scanner = new Scanner(file);
		String xml = scanner.useDelimiter("\\Z").next();
		scanner.close();

		return importXML(xml);
		
	}

	/**
	 * Import the {@link RePlan} from the String
	 */
	public static Plan importXML(String xml) {

		XStream xstream = new XStream();
		xstream.setMode(XStream.NO_REFERENCES);

		return (Plan) xstream.fromXML(xml);
	}
}
