package org.distributedea.agents.systemagents.datamanager;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.distributedea.agents.systemagents.Agent_DataManager;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.job.JobID;

/**
 * Tool ensures work with file system for {@link Agent_DataManager}
 * @author stepan
 *
 */
public class FilesystemInitTool {
	
	/**
	 * Creates log space in filesystem for given {@link JobID}
	 * @param jobID
	 */
	public static void createLogSpaceForJobRun(JobID jobID) {
		
		String logDirectoryName = FileNames.getLogBatchDirectory(jobID.getBatchID());
		File logDirectory = new File(logDirectoryName);
		if (! logDirectory.isDirectory()) {
			logDirectory.mkdir();
		}
		
		String logBatchDirectoryName = FileNames.getInputBatchDirectory(jobID.getBatchID());
		File logBatchDirectory = new File(logBatchDirectoryName);
		if (! logBatchDirectory.isDirectory()) {
			logBatchDirectory.mkdir();
		}	
		
		String logCADirectoryName = FileNames.getComputingAgentLogDirectory(jobID);
		File logCADirectory = new File(logCADirectoryName);
		if (! logCADirectory.isDirectory()) {
			logCADirectory.mkdir();
		}
		
		String logCARunDirectoryName = FileNames.getComputingAgentRunLogDirectory(jobID);
		File logCARunDirectory = new File(logCARunDirectoryName);
		if (! logCARunDirectory.isDirectory()) {
			logCARunDirectory.mkdir();
		}
		
		String logCAResultDirectoryName = FileNames.getComputingAgentLogSolutionDirectory(jobID);
		File logCAResultDirectory = new File(logCAResultDirectoryName);
		if (! logCAResultDirectory.isDirectory()) {
			logCAResultDirectory.mkdir();
		}
		
		String logCAImprovementDirectoryName = FileNames.getComputingAgentLogImprovementOfDistributionDirectory(jobID);
		File logCAImprovementDirectory = new File(logCAImprovementDirectoryName);
		if (! logCAImprovementDirectory.isDirectory()) {
			logCAImprovementDirectory.mkdir();
		}
		
	}
	
	private static void clearResultSpaceForJob(String batchID, String jobID, IAgentLogger logger) {
		
		String resultJobDirectoryName =
				FileNames.getResultDirectoryForJob(new JobID(batchID, jobID, 0));
		
		File resultJobDir = new File(resultJobDirectoryName);
		if (resultJobDir.isDirectory()) {
			try {
				FileUtils.deleteDirectory(resultJobDir);
			} catch (IOException e) {
				logger.logThrowable("Can not delete result directory", e);
			}
		}
		
		try {
			resultJobDir.mkdir();
		} catch (Exception e) {
			logger.logThrowable("Can not create result directory", e);
		}
	}

	public static void createResultSpaceForJob(String batchID, String jobID, IAgentLogger logger) {
		
		String resultJobDirectoryName =
				FileNames.getResultDirectoryForJob(new JobID(batchID, jobID, 0));
		File resultJobDir = new File(resultJobDirectoryName);
				
		try {
			if (! resultJobDir.isDirectory()) {
				resultJobDir.mkdir();
			}
		} catch (Exception e) {
			logger.logThrowable("Can not create result directory", e);
		}

	}
	
	/**
	 * Exists directory for given JobRun
	 * @param jobID
	 * @return
	 */
	public static boolean existsResultSpaceForJobRun(JobID jobID) {

		String resultDirectory = FileNames.getResultDirectoryForJobRun(jobID);
		
		return new File(resultDirectory).isDirectory();
	}
	
	/**
	 * Creates result space in filesystem for given {@link Batch}
	 * @param batchID
	 */
	public static void createResultSpaceForBatch(String batchID) {
		
		String resultDirectoryForBatchName = FileNames.getResultDirectory(batchID);
		File resultDirectoryForBatch = new File(resultDirectoryForBatchName);
		if (! resultDirectoryForBatch.isDirectory()) {
			resultDirectoryForBatch.mkdir();
		}
		
		String resultDirectoryForJobsName = FileNames.getResultDirectoryForJobs(batchID);
		File resultDirectoryForJobs = new File(resultDirectoryForJobsName);
		if (! resultDirectoryForJobs.isDirectory()) {
			resultDirectoryForJobs.mkdir();
		}
				
		String resultPostProcDirName = FileNames.getResultDirectoryForMatlab(batchID);
		File resultPostProcDir = new File(resultPostProcDirName);
		if (! resultPostProcDir.isDirectory()) {
			resultPostProcDir.mkdir();
		}
		
		String resultInputParamDirName = FileNames.getResultDirectoryWithCopyOfInputParameters(batchID);
		File resultInputParamDirectory = new File(resultInputParamDirName);
		if (! resultInputParamDirectory.isDirectory()) {
			resultInputParamDirectory.mkdir();
		}
		
		String resultInputBatchCopyDirName = FileNames.getResultDirectoryWithCopyOfInputBatch(batchID);
		File resultInputBatchCopyDirectory = new File(resultInputBatchCopyDirName);
		if (! resultInputBatchCopyDirectory.isDirectory()) {
			resultInputBatchCopyDirectory.mkdir();
		}
	}
	
	/**
	 * Creates result space in filesystem for given {@link JobID}
	 * @param jobID
	 */
	public static void createResultSpaceForJobRun(JobID jobID) {
		
		String batchID = jobID.getBatchID();
		
		createResultSpaceForBatch(batchID);
		
		String resultDirectoryForJobName = FileNames.getResultDirectoryForJob(jobID);
		File resultDirectoryForJob = new File(resultDirectoryForJobName);
		if (! resultDirectoryForJob.isDirectory()) {
			resultDirectoryForJob.mkdir();
		}
		
		String resultDirectoryForJobRunName = FileNames.getResultDirectoryForJobRun(jobID);
		File resultDirectoryForJobRun = new File(resultDirectoryForJobRunName);
		if (! resultDirectoryForJobRun.isDirectory()) {
			resultDirectoryForJobRun.mkdir();
		}
		
		String monitoringDirName = FileNames.getResultDirectoryMonitoringDirectory(jobID);
		File monitoringDirectory = new File(monitoringDirName);
		if (! monitoringDirectory.isDirectory()) {
			monitoringDirectory.mkdir();
		}
	}

	public static void copyInputBatchDescriptionToResultDir(String batchID) {
		
		String inputBatchDescriptionFileName = FileNames.getInputBatchDirectory(batchID) +
				File.separator + "description.txt";
		File inputBatchDescriptionFile = new File(inputBatchDescriptionFileName);
		
		String batchDescriptionCopyOFileName =
				FileNames.getResultDirectoryWithCopyOfInputBatch(batchID) +
				File.separator + "description.txt";
		File batchDescriptionCopyOFile =
				new File(batchDescriptionCopyOFileName);

		if (! batchDescriptionCopyOFile.exists()) {
			try {
				FileUtils.copyFile(inputBatchDescriptionFile, batchDescriptionCopyOFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Moves input file with {@link Job} to directory with results
	 * @param jobIDToMove
	 * @throws IOException
	 */
	public static void moveInputJobToResultDir(JobID jobIDToMove) throws IOException {
		
		String batchID = jobIDToMove.getBatchID();
		String jobID = jobIDToMove.getJobID();
		
		String inputJobFileName = FileNames.getInputJobFile(batchID, jobID);
		File inputJobFile = new File(inputJobFileName);

		String resultDirectoryWithCopyOfInputBatchName =
				FileNames.getResultDirectoryWithCopyOfInputBatch(batchID);
		File resultDirectoryWithCopyOfInputBatch =
				new File(resultDirectoryWithCopyOfInputBatchName);

		// removing old copy
		String newInputJobFileName = resultDirectoryWithCopyOfInputBatchName +
				File.separator + inputJobFile.getName();
		File newInputJobFile = new File(newInputJobFileName);
		if (newInputJobFile.exists()) {
			newInputJobFile.delete();
		}
		
		FileUtils.moveToDirectory(inputJobFile, resultDirectoryWithCopyOfInputBatch, true);
	}
	
	public static void moveInputPostProcToResultDir(Class<?> postProcToMove, String batchID) throws IOException {
		
		String resultDirectoryForBatchName = FileNames.getInputBatchDirectory(batchID);
		File postProcFile = new File(resultDirectoryForBatchName + File.separator +
				postProcToMove.getSimpleName() + "." + FileNames.POSTPROCESSING_SUFIX);
		
		String resultDirectoryWithCopyOfInputBatchName =
				FileNames.getResultDirectoryWithCopyOfInputBatch(batchID);
		File resultDirectoryWithCopyOfInputBatch =
				new File(resultDirectoryWithCopyOfInputBatchName);
		
		FileUtils.moveToDirectory(postProcFile, resultDirectoryWithCopyOfInputBatch, true);
	}
	
	/**
	 * Clears log directory
	 * @param logger
	 * @throws IOException
	 */
	public static void clearLogDir(IAgentLogger logger) throws IOException {
		
		String logGlobalDirName = FileNames.getGlobalLogDirectory();
		
		File logGlobalDirectory = new File(logGlobalDirName);
		try {
			logGlobalDirectory.mkdir();
		} catch (Exception e) {
			logger.logThrowable("Can not create log directory", e);
			throw new IOException("Can not create log directory");
		}
		
		
		String logDirName = FileNames.getLogDirectory();
		
		File logDir = new File(logDirName);
		if (logDir.isDirectory()) {
			try {
				FileUtils.deleteDirectory(new File(logDirName));
			} catch (IOException e) {
				logger.logThrowable("Can not delete log directory", e);
				throw new IOException("Can not delete log directory");
			}
		}
		
		File logDirectory = new File(logDirName);
		try {
			logDirectory.mkdir();
		} catch (Exception e) {
			logger.logThrowable("Can not create log directory", e);
			throw new IOException("Can not create log directory");
		}
		
		try {
			FileUtils.touch(new File(logDirName + File.separator +
					"description.txt"));
		} catch (Exception e) {
			logger.logThrowable("Can not create description file in log directory", e);
			throw new IOException("Can not create description file in log directory");
		}
		
		File bbobDirectory = new File(logDirName + File.separator + "bbob");
		try {
			bbobDirectory.mkdir();
		} catch (Exception e) {
			logger.logThrowable("Can not create bbob log directory", e);
			throw new IOException("Can not create bbob log directory");
		}
	}
	
	/**
	 * Clears result directory
	 * @param logger
	 * @throws IOException
	 */
	public static void clearResultDir(IAgentLogger logger) throws IOException {	
	}
	
}
