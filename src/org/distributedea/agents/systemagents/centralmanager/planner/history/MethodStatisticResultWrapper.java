package org.distributedea.agents.systemagents.centralmanager.planner.history;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import org.distributedea.agents.systemagents.centralmanager.planner.modes.Iteration;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.monitor.MethodStatisticResult;

public class MethodStatisticResultWrapper {

	private final Iteration iteration;
	
	private AgentDescription agentDescription;
	
	private final MethodStatisticResult methodStatisticResult;
	
	
	public MethodStatisticResultWrapper(Iteration iteration,
			AgentDescription agentDescription,
			MethodStatisticResult methodStatisticResult) {
		
		this.iteration = iteration;
		this.agentDescription = agentDescription;
		this.methodStatisticResult = methodStatisticResult;
	}


	public Iteration getIteration() {
		return iteration;
	}

	
	public AgentDescription getAgentDescription() {
		return agentDescription;
	}


	public MethodStatisticResult getMethodStatisticResult() {
		return methodStatisticResult;
	}	
	
	public boolean equals(Object other) {
		
	    if (!(other instanceof MethodStatisticResultWrapper)) {
	        return false;
	    }
	    
	    MethodStatisticResultWrapper methodOuther = (MethodStatisticResultWrapper)other;
	    
	    boolean areIterationEqual = methodOuther.iteration == this.iteration;
	    
	    if (methodOuther.methodStatisticResult == null &&
	    		this.methodStatisticResult == null) {
	    	return areIterationEqual;
	    }
	    
	    boolean aregMethodsEqual = methodOuther.methodStatisticResult
	    		.equals(this.methodStatisticResult);
	    
	    return aregMethodsEqual && areIterationEqual;
	}
	
    @Override
    public int hashCode() {
    	return toString().hashCode();
    }
    
	@Override
	public String toString() {
		
		if (methodStatisticResult == null) {
			return "" + iteration;
		}
		return iteration + methodStatisticResult.toString();
	}

	void exportXML(File file) throws FileNotFoundException, JAXBException {
		
		 Iteration iteration = getIteration();
		 
		 String iterationStr = iteration.exportIterationToString();
		 String statisticDirName = file.getAbsolutePath() +
				 File.separator + iterationStr;
		 File statisticDir = new File(statisticDirName);
		 if (! statisticDir.exists()) {
			 statisticDir.mkdir();
		 }

		 AgentDescription descriptionI = getAgentDescription();
		 descriptionI.exportXML(new File(statisticDirName));

		 MethodStatisticResult resultI = getMethodStatisticResult();
		 resultI.exportXML(new File(statisticDirName));
	}
	
	public static MethodStatisticResultWrapper importXML(File statisticDir) throws FileNotFoundException {
						
        Iteration iterat = Iteration.importIterationToString(
        		statisticDir.getName());

		String statisticDirName = statisticDir.getAbsolutePath();

        AgentDescription descriptionI = AgentDescription.importXML(
        		new File(statisticDirName + File.separator + "description.xml"));
        
		MethodStatisticResult resultI = MethodStatisticResult.importXML(
				new File(statisticDirName + File.separator + "statistic.xml"));

        return new MethodStatisticResultWrapper(iterat, descriptionI, resultI);

	}
}
