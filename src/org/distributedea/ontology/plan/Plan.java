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
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescription.MethodDescriptions;

import com.thoughtworks.xstream.XStream;

/**
 * Representation of the newly created agents and given {@link Iteration}
 * @author stepan
 *
 */
public class Plan implements Concept {

	private static final long serialVersionUID = 1L;
	
	private Iteration iteration;
	private MethodDescriptions newAgents;

	
	@Deprecated
	public Plan() { // only for Jade
	}

	/**
	 * Constructor for any new method
	 * @param iteration
	 */
	public Plan(Iteration iteration) {
		setIteration(iteration);
		this.newAgents = new MethodDescriptions();
		
	}
	/**
	 * Constructor for one new method
	 * @param iteration
	 */
	public Plan(Iteration iteration, MethodDescription newAgent) {
		setIteration(iteration);
		addAgentsToCreate(newAgent);
	}
	/**
	 * Constructor for the list of new methods
	 * @param iteration
	 */
	public Plan(Iteration iteration, MethodDescriptions newAgents) {
		if (iteration == null || ! iteration.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Iteration.class.getSimpleName() + " is not valid");
		}
		if (newAgents == null || ! newAgents.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodDescriptions.class.getSimpleName() + " is not valid");
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

	public MethodDescriptions getNewAgents() {
		return newAgents;
	}
	@Deprecated
	public void setNewAgents(MethodDescriptions newAgents) {
		if (newAgents == null || ! newAgents.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodDescriptions.class.getSimpleName() + " is not valid");
		}
		this.newAgents = newAgents;
	}
	
	/**
	 * Adds method to create
	 * @param agentToCreate
	 */
	public void addAgentsToCreate(MethodDescription agentToCreate) {
		if (agentToCreate == null ||
				! agentToCreate.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodDescription.class.getSimpleName() + " is not valid");
		}
		
		if (this.newAgents == null) {
			this.newAgents = new MethodDescriptions();
		}
		
		this.newAgents.addAgentDescriptions(agentToCreate);
	}
	
	/**
	 * Adds method to create
	 * @param agentToCreate
	 */
	public void addAgentsToCreate(MethodDescriptions agentDescriptions) {
		if (agentDescriptions == null ||
				! agentDescriptions.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodDescription.class.getSimpleName() + " is not valid");
		}
		
		for (MethodDescription agentDescriptionI :
			agentDescriptions.getAgentDescriptions()) {
			
			addAgentsToCreate(agentDescriptionI);
		}
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
		
		List<MethodDescription> newAgents = new ArrayList<>();
		newAgents.addAll(plan1.getNewAgents().getAgentDescriptions());
		newAgents.addAll(plan2.getNewAgents().getAgentDescriptions());
		
		return new Plan(iteration1, new MethodDescriptions(newAgents));
	}
	
	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (iteration == null || ! iteration.valid(logger)) {
			return false;
		}
		if (newAgents == null || ! newAgents.valid(logger)) {
			return false;
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
