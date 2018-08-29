package org.distributedea.problemtools.continuousoptimization.point.tools.bbob;

import java.io.File;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.dataset.DatasetContinuousOpt;
import org.distributedea.ontology.problem.ProblemContinuousOpt;
import org.distributedea.problemtools.continuousoptimization.bbobv1502.BbobException;
import org.distributedea.problemtools.continuousoptimization.bbobv1502.BbobTools;
import org.distributedea.problemtools.continuousoptimization.bbobv1502.IJNIfgeneric;
import org.distributedea.problemtools.continuousoptimization.bbobv1502.JNIfgeneric;

public class ToolInitializationBbobCO {

	public static void initialization(ProblemContinuousOpt problemCO, DatasetContinuousOpt datasetCO,
			AgentConfiguration agentConf, IJNIfgeneric fgeneric, BbobTools bbobTools,
			IAgentLogger logger) throws Exception {
		
    	System.setProperty("java.library.path", "." + File.separator + "lib");
    	    	
    	int dim = problemCO.getDimension();
    	
    	String functionIDString = problemCO.getFunctionID();
    	int functionID = Integer.parseInt(functionIDString.substring(1));
    	
    	String containerSuffix = agentConf.exportContainerSuffix();
    	
    	bbobTools = new BbobTools(containerSuffix, logger);
    	
    	try {
			fgeneric = bbobTools.getInstanceJNIfgeneric();
		} catch (BbobException e) {
			throw new Exception("Problem Tool can't initialize Bbob");
		}

    	
        JNIfgeneric.Params params = new JNIfgeneric.Params();
        
        String path = "log" + File.separator + "bbob" + File.separator +
        		"data" + bbobTools.getNumber();
        double ret = fgeneric.initBBOB(functionID, 1, dim, path, params);
        if (ret == 0.) {
            System.out.println("initBBOB finished OK.");
        } else {
            System.out.println("initBBOB returned error: " + ret);
        }
	}
}
