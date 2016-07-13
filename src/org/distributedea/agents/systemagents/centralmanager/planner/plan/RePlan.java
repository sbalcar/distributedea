package org.distributedea.agents.systemagents.centralmanager.planner.plan;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.bind.JAXBException;

import org.distributedea.agents.systemagents.centralmanager.planner.modes.Iteration;
import org.distributedea.ontology.agentdescription.AgentDescription;

import com.thoughtworks.xstream.XStream;

public class RePlan {

	private Iteration teration;
	private List<AgentDescription> agentsToKill;
	private List<AgentDescription> agentsToCreate;
	
	@SuppressWarnings("unused")
	private RePlan() {} // only for XStream
	
	public RePlan(Iteration teration) {
		this.teration = teration;
	}
	
	public RePlan(Iteration teration, AgentDescription agentToKill,
			AgentDescription agentToCreate) {
		this.teration = teration;
		addAgentsToKill(agentToKill);
		addAgentsToCreate(agentToCreate);
	}
	
	public Iteration getIteration() {
		return this.teration;
	}
	
	public List<AgentDescription> getAgentsToKill() {
		if (this.agentsToKill == null) {
			return new ArrayList<>();
		}
		return this.agentsToKill;
	}
	public void addAgentsToKill(AgentDescription agentToKill) {
		
		if (this.agentsToKill == null) {
			this.agentsToKill = new ArrayList<>();
		}
		
		this.agentsToKill.add(agentToKill);
	}
	public boolean containsAgentToKill(AgentDescription agentToKill) {
		
		if (agentsToKill == null) {
			return false;
		}
		for (AgentDescription agentDescI : agentsToKill) {
			if (agentDescI.exportAgentName().equals(
					agentToKill.exportAgentName())) {
				return true;
			}
		}
		return false;
	}
	
	public List<AgentDescription> getAgentsToCreate() {
		if (this.agentsToCreate == null) {
			return new ArrayList<>();
		}
		return this.agentsToCreate;
	}
	public void addAgentsToCreate(AgentDescription agentToCreate) {
		
		if (this.agentsToCreate == null) {
			this.agentsToCreate = new ArrayList<>();
		}
		
		this.agentsToCreate.add(agentToCreate);
	}
	
	
	/**
	 * Exports structure as the XML String to the file
	 * 
	 * @throws FileNotFoundException
	 * @throws JAXBException 
	 */
	public void exportXML(File dir) throws FileNotFoundException, JAXBException {

		if (dir == null) {
			return;
		}
		
		String replanFileName = dir.getAbsolutePath() + File.separator +
				teration.exportNumOfIteration() + ".txt";
		
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
