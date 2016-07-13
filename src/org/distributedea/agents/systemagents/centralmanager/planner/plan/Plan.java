package org.distributedea.agents.systemagents.centralmanager.planner.plan;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.bind.JAXBException;

import org.distributedea.agents.systemagents.centralmanager.planner.modes.Iteration;
import org.distributedea.ontology.agentdescription.AgentDescription;

import com.thoughtworks.xstream.XStream;

public class Plan {

	private Iteration iteration;
	private List<AgentDescription> newAgents;

	@Deprecated
	public Plan() {}

	public Plan(Iteration iteration, List<AgentDescription> newAgents) {
		this.iteration = iteration;
		this.newAgents = newAgents;
	}
	public Plan(Iteration iteration) {
		this.iteration = iteration;
		this.newAgents = new ArrayList<>();
	}

	public Iteration getIteration() {
		return iteration;
	}
	@Deprecated
	public void setIteration(Iteration iteration) {
		this.iteration = iteration;
	}

	public List<AgentDescription> getNewAgents() {
		return newAgents;
	}
	@Deprecated
	public void setNewAgents(List<AgentDescription> newAgents) {
		this.newAgents = newAgents;
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
		newAgents.addAll(plan1.getNewAgents());
		newAgents.addAll(plan2.getNewAgents());
		
		return new Plan(iteration1, newAgents);
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
	 * @throws FileNotFoundException
	 */
	public static Plan importXML(File file)
			throws FileNotFoundException {

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
