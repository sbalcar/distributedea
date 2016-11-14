package org.distributedea.agents.systemagents.centralmanager.structures.history;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.monitor.MethodStatisticResult;

/**
 * Structure represents one {@link MethodStatisticResult} specified by
 * iteration and Agent
 * @author stepan
 *
 */
public class MethodStatisticResultWrapper {

	private final Iteration iteration;
	
	private MethodDescription agentDescription;
	
	private final MethodStatisticResult methodStatisticResult;
	
	
	/**
	 * Constructor
	 * @param iteration
	 * @param agentDescription
	 * @param methodStatisticResult
	 */
	public MethodStatisticResultWrapper(Iteration iteration,
			MethodDescription agentDescription,
			MethodStatisticResult methodStatisticResult) {
		
		this.iteration = iteration;
		this.agentDescription = agentDescription;
		this.methodStatisticResult = methodStatisticResult;
	}


	/**
	 * Returns iteration
	 * @return
	 */
	public Iteration getIteration() {
		return iteration;
	}

	/**
	 * Returns agent description
	 * @return
	 */
	public MethodDescription getAgentDescription() {
		return agentDescription;
	}

	/**
	 * Returns one statistic of specified Agent during
	 * scpecified {@link Iteration}
	 * @return
	 */
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

	/**
	 * Exports to XML
	 * @param file
	 * @throws FileNotFoundException
	 * @throws JAXBException
	 */
	void exportXML(File file) throws IOException {
		
		 Iteration iteration = getIteration();
		 
		 String iterationStr = iteration.exportIterationToString();
		 String statisticDirName = file.getAbsolutePath() +
				 File.separator + iterationStr;
		 File statisticDir = new File(statisticDirName);
		 if (! statisticDir.exists()) {
			 statisticDir.mkdir();
		 }

		 MethodDescription descriptionI = getAgentDescription();
		 descriptionI.exportXML(new File(statisticDirName));

		 MethodStatisticResult resultI = getMethodStatisticResult();
		 resultI.exportXML(new File(statisticDirName));
	}
	
	/**
	 * Imports {@link MethodStatisticResultWrapper} from XML
	 * @param statisticDir
	 * @return
	 * @throws IOException
	 */
	public static MethodStatisticResultWrapper importXML(File statisticDir) throws IOException {
						
        Iteration iterat = Iteration.importIterationToString(
        		statisticDir.getName());

		String statisticDirName = statisticDir.getAbsolutePath();

        MethodDescription descriptionI = MethodDescription.importXML(
        		new File(statisticDirName + File.separator + "description.xml"));
        
		MethodStatisticResult resultI = MethodStatisticResult.importXML(
				new File(statisticDirName + File.separator + "statistic.xml"));

        return new MethodStatisticResultWrapper(iterat, descriptionI, resultI);

	}
}
