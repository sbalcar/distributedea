package org.distributedea.ontology.plan;

import jade.content.Concept;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescription.MethodDescriptions;

import com.thoughtworks.xstream.XStream;

/**
 * Representation of the newly created and killed agents and given {@link Iteration}
 * @author stepan
 *
 */
public class RePlan implements Concept {

	private static final long serialVersionUID = 1L;
	
	private Iteration iteration;
	private MethodDescriptions agentsToKill;
	private MethodDescriptions agentsToCreate;
	
	@Deprecated
	public RePlan() { // only for Jade
		this.agentsToKill = new MethodDescriptions();
		this.agentsToCreate = new MethodDescriptions();
	}
	
	/**
	 * Constructor for no change situation
	 * @param teration
	 */
	public RePlan(Iteration teration) {
		if (teration == null || ! teration.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
				Iteration.class.getSimpleName() + " is not valid");
		}
		this.iteration = teration;
		this.agentsToKill = new MethodDescriptions();
		this.agentsToCreate = new MethodDescriptions();
	}
	
	/**
	 * Constructor for one method to create and one to kill
	 * @param teration
	 * @param agentToKill
	 * @param agentToCreate
	 */
	public RePlan(Iteration teration, MethodDescription agentToKill,
			MethodDescription agentToCreate) {
		if (teration == null || ! teration.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
				Iteration.class.getSimpleName() + " is not valid");
		}
		this.iteration = teration;
		addAgentsToKill(agentToKill);
		addAgentsToCreate(agentToCreate);
	}
	
	/**
	 * Constructor
	 * @param teration
	 * @param agentsToKill
	 * @param agentsToCreate
	 */
	public RePlan(Iteration iteration, MethodDescriptions agentsToKill,
			MethodDescriptions agentsToCreate) {
		if (iteration == null || ! iteration.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
				Iteration.class.getSimpleName() + " is not valid");
		}
		if (agentsToKill == null || ! agentsToKill.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
				MethodDescriptions.class.getSimpleName() + " is not valid");
		}
		if (agentsToCreate == null || ! agentsToCreate.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
				MethodDescriptions.class.getSimpleName() + " is not valid");
		}
		this.iteration = iteration;
		this.agentsToKill = agentsToKill;
		this.agentsToCreate = agentsToCreate;
	}

	/**
	 * Copy Constructor
	 * @param rePlan
	 */
	public RePlan(RePlan rePlan) {
		if (rePlan == null || ! rePlan.valid(new TrashLogger())) {
			throw new IllegalArgumentException();
		}
		
		this.iteration = rePlan.getIteration().deepClone();
		this.agentsToCreate = rePlan.agentsToCreate.deepClone();
		this.agentsToKill = rePlan.agentsToKill.deepClone();
	}
	
	/**
	 * Returns {@link Iteration} of current replanning
	 * @return
	 */
	public Iteration getIteration() {
		return this.iteration;
	}
	@Deprecated
	public void setIteration(Iteration iteration) {
		if (iteration == null) {
			throw new IllegalArgumentException();
		}
		this.iteration = iteration;
	}
	
	/**
	 * Returns methods to kill
	 * @return
	 */
	public MethodDescriptions getAgentsToKill() {
		return this.agentsToKill;
	}
	@Deprecated
	public void setAgentsToKill(MethodDescriptions agentsToKill) {
		if (agentsToKill == null || ! agentsToKill.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodDescriptions.class.getSimpleName() + " is not valid");
		}
		this.agentsToKill = agentsToKill;
	}
	/**
	 * Adds method to kill
	 * @param agentToKill
	 */
	public void addAgentsToKill(MethodDescription agentToKill) {
		if (agentToKill == null || ! agentToKill.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodDescriptions.class.getSimpleName() + " is not valid");
		}
			
		if (this.agentsToKill == null) {
			this.agentsToKill = new MethodDescriptions();
		}
		
		this.agentsToKill.addAgentDescriptions(agentToKill);
	}
	public boolean containsAgentToKill(MethodDescription agentToKill) {
		if (agentToKill == null || ! agentToKill.valid(new TrashLogger())) {
			return false;
		}
		if (agentsToKill == null) {
			return false;
		}
		return agentsToKill.containsMethodDescription(agentToKill);
	}
	
	/**
	 * Returns agents to create
	 * @return
	 */
	public MethodDescriptions getAgentsToCreate() {
		return this.agentsToCreate;
	}
	@Deprecated
	public void setAgentsToCreate(MethodDescriptions agentsToCreate) {
		if (agentsToCreate == null ||
				! agentsToCreate.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodDescriptions.class.getSimpleName() + " is not valid");
		}
		this.agentsToCreate = agentsToCreate;
	}
	
	/**
	 * Adds method to create
	 * @param agentToCreate
	 */
	public void addAgentsToCreate(MethodDescription agentToCreate) {
		if (agentToCreate == null || ! agentToCreate.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodDescriptions.class.getSimpleName() + " is not valid");
		}
		
		if (this.agentsToCreate == null) {
			this.agentsToCreate = new MethodDescriptions();
		}
		
		this.agentsToCreate.addAgentDescriptions(agentToCreate);
	}

	public boolean containsAgentToCreate(MethodDescription agentToCreate) {
		
		if (agentToCreate == null || ! agentToCreate.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodDescription.class.getSimpleName() + " is not valid");
		}
		if (agentsToCreate == null) {
			return false;
		}
		return agentsToCreate.containsMethodDescription(agentToCreate);
	}
	
	public boolean isEmpty() {
		return agentsToKill.isEmpty() && agentsToCreate.isEmpty();
	}
	
	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (iteration == null || ! iteration.valid(logger)) {
			return false;
		}
		if (agentsToCreate == null || ! agentsToCreate.valid(logger)) {
			return false;
		}
		if (agentsToKill == null || ! agentsToKill.valid(logger)) {
			return false;
		}
		return true;
	}
	
	public RePlan deepClone() {
		return new RePlan(this);
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
		
		String replanFileName = dir.getAbsolutePath() + File.separator +
				iteration.exportNumOfIteration() + ".txt";
		
		String xml = exportXML();

		PrintWriter file = new PrintWriter(replanFileName);
		file.println(xml);
		file.close();
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
	 * Import the {@link RePlan} from the file
	 * 
	 * @throws FileNotFoundException
	 */
	public static RePlan importXML(File file)
			throws FileNotFoundException {

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
	public static RePlan importXML(String xml) {

		XStream xstream = new XStream();
		xstream.setMode(XStream.NO_REFERENCES);

		return (RePlan) xstream.fromXML(xml);
	}
}
