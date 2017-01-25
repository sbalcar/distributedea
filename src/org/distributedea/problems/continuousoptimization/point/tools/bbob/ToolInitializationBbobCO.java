package org.distributedea.problems.continuousoptimization.point.tools.bbob;

import java.io.File;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.dataset.DatasetContinuousOpt;
import org.distributedea.problems.continuousoptimization.bbobv1502.BbobException;
import org.distributedea.problems.continuousoptimization.bbobv1502.BbobTools;
import org.distributedea.problems.continuousoptimization.bbobv1502.IJNIfgeneric;
import org.distributedea.problems.continuousoptimization.bbobv1502.JNIfgeneric;

public class ToolInitializationBbobCO {

	public static void initialization(DatasetContinuousOpt problemContinousOpt, AgentConfiguration agentConf,
			IJNIfgeneric fgeneric, BbobTools bbobTools, IAgentLogger logger) throws Exception {
		
    	System.setProperty("java.library.path", "." + File.separator + "lib");
    	    	
    	int dim = problemContinousOpt.getDimension();
    	
    	String functionIDString = problemContinousOpt.getFunctionID();
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
