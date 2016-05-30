package org.distributedea.agents.systemagents.datamanager;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.distributedea.Configuration;
import org.distributedea.ontology.job.JobID;

public class FilesystemTool {

	public static void createLogSpaceForJobRun(JobID jobID) {
		
		String logDirectoryName = Configuration.getLogBatchDirectory(jobID.getBatchID());
		File logDirectory = new File(logDirectoryName);
		if (! logDirectory.isDirectory()) {
			logDirectory.mkdir();
		}
		
		String logBatchDirectoryName = Configuration.getInputBatchDirectory(jobID.getBatchID());
		File logBatchDirectory = new File(logBatchDirectoryName);
		if (! logBatchDirectory.isDirectory()) {
			logBatchDirectory.mkdir();
		}	
		
		String logCADirectoryName = Configuration.getComputingAgentLogDirectory(jobID);
		File logCADirectory = new File(logCADirectoryName);
		if (! logCADirectory.isDirectory()) {
			logCADirectory.mkdir();
		}
		
		String logCARunDirectoryName = Configuration.getComputingAgentRunLogDirectory(jobID);
		File logCARunDirectory = new File(logCARunDirectoryName);
		if (! logCARunDirectory.isDirectory()) {
			logCARunDirectory.mkdir();
		}
		
		String logCAResultDirectoryName = Configuration.getComputingAgentLogSolutionDirectory(jobID);
		File logCAResultDirectory = new File(logCAResultDirectoryName);
		if (! logCAResultDirectory.isDirectory()) {
			logCAResultDirectory.mkdir();
		}
		
		String logCAImprovementDirectoryName = Configuration.getComputingAgentLogImprovementOfDistributionDirectory(jobID);
		File logCAImprovementDirectory = new File(logCAImprovementDirectoryName);
		if (! logCAImprovementDirectory.isDirectory()) {
			logCAImprovementDirectory.mkdir();
		}
		
	}
	
	public static void createResultSpaceForBatch(String batchID) {
				
		String resultBatchDirName = Configuration.getResultDirectory(batchID);
		File resultBatchDirectory = new File(resultBatchDirName);
		if (! resultBatchDirectory.isDirectory()) {
			resultBatchDirectory.mkdir();
		}
		
		String resultInputParamDirName = Configuration.getResultDirectoryWithCopyOfInputParameters(batchID);
		File resultInputParamDirectory = new File(resultInputParamDirName);
		if (! resultInputParamDirectory.isDirectory()) {
			resultInputParamDirectory.mkdir();
		}
		
		String resultInputBatchCopyDirName = Configuration.getResultDirectoryWithCopyOfInputBatch(batchID);
		File resultInputBatchCopyDirectory = new File(resultInputBatchCopyDirName);
		if (! resultInputBatchCopyDirectory.isDirectory()) {
			resultInputBatchCopyDirectory.mkdir();
		}
	}
	
	public static void copyBatchDescriptionToResultDir(String batchID) {
		
		String batchDescriptionFileName = Configuration.getBatchDescriptionFile(batchID);
		File batchDescriptionFile = new File(batchDescriptionFileName);
		
		String resultDirectoryWithCopyOfInputBatchName =Configuration.getResultDirectoryWithCopyOfInputBatch(batchID);
		File resultDirectoryWithCopyOfInputBatch = new File(resultDirectoryWithCopyOfInputBatchName);
		
		try {
			FileUtils.copyFileToDirectory(batchDescriptionFile, resultDirectoryWithCopyOfInputBatch);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void moveInputJobToResultDir(JobID jobIDStruct) {
		
		String batchID = jobIDStruct.getBatchID();
		String jobID = jobIDStruct.getJobID();
		
		String inputJobFileName = Configuration.getInputJobFile(batchID, jobID);
		File inputJobFile = new File(inputJobFileName);

		String resultDirectoryWithCopyOfInputBatchName =Configuration.getResultDirectoryWithCopyOfInputBatch(batchID);
		File resultDirectoryWithCopyOfInputBatch = new File(resultDirectoryWithCopyOfInputBatchName);

		// removing old copy
		String newInputJobFileName = resultDirectoryWithCopyOfInputBatchName + File.separator + inputJobFile.getName();
		File newInputJobFile = new File(newInputJobFileName);
		if (newInputJobFile.exists()) {
			newInputJobFile.delete();
		}
		
		try {
			FileUtils.moveToDirectory(inputJobFile, resultDirectoryWithCopyOfInputBatch, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void copyMatlabToResultDir(String batchID) {
		
		File postProcDir = new File("matlab");
		
		String resultPostProcDirName = Configuration.getResultDirectoryForMatlab(batchID);
		File resultPostProcDir = new File(resultPostProcDirName);
		
		try {
			FileUtils.copyDirectory(postProcDir, resultPostProcDir);
			//FileUtils.cleanDirectory(postProcDir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
