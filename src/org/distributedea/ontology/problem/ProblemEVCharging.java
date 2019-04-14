package org.distributedea.ontology.problem;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.dataset.DatasetEVCharging;

/**
 * Ontology for definiton of Electric Vehicle charging problem
 * @author stepan
 *
 */
public class ProblemEVCharging extends AProblem {

	private static final long serialVersionUID = 1L;

	private String serverIP;
	private int port;
	
	
	/**
	 * Constructor
	 */
	@Deprecated
	public ProblemEVCharging() {
	}

	/**
	 * Constructor
	 * @param serverIP
	 * @param port
	 */
	public ProblemEVCharging(String serverIP, int port) {
		setServerIP(serverIP);
		setPort(port);
	}

	/**
	 * Copy constructor
	 * @param problemEVCh
	 */
	public ProblemEVCharging(ProblemEVCharging problemEVCh) {
		if (problemEVCh == null || ! problemEVCh.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					ProblemEVCharging.class.getSimpleName() + " is not valid");
		}
		setServerIP(problemEVCh.getServerIP());
		setPort(problemEVCh.getPort());
	}

	
	public String getServerIP() {
		return serverIP;
	}
	@Deprecated
	public void setServerIP(String serverIP) {
		if (serverIP == null || serverIP.isEmpty()) {
			throw new IllegalArgumentException("Argument " +
					String.class.getSimpleName() + " is not valid");
		}
		this.serverIP = serverIP;
	}

	public int getPort() {
		return port;
	}
	@Deprecated
	public void setPort(int port) {
		if (port < 0) {
			throw new IllegalArgumentException("Argument " +
					Integer.class.getSimpleName() + " is not valid");
		}
		this.port = port;
	}

	
	@Override
	public boolean exportIsMaximizationProblem() {
		return false;
	}

	@Override
	public Class<?> exportDatasetClass() {
		return DatasetEVCharging.class;
	}

	@Override
	public boolean valid(IAgentLogger logger) {
		return true;
	}

	@Override
	public String toLogString() {
		return this.getClass().getSimpleName() + " " +
			"serverIP=" + serverIP + " " +
			"port=" + port;
	}
	
	@Override
	public AProblem deepClone() {
		return new ProblemEVCharging(this);
	}

}
