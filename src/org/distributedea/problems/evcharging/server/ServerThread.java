package org.distributedea.problems.evcharging.server;

import java.io.File;

import org.distributedea.tests.evcharging.ExecuteShellCommand;

public class ServerThread extends Thread {
	
	private File datasetFile;
	private int portNumber;
	
	public ServerThread(File datasetFile, int portNumber)
	{
		this.datasetFile = datasetFile;
		this.portNumber = portNumber;
	}

    public void run() {
    	
    	System.out.println("Server evcharging start on port " + portNumber);
    	
    	String com = "./python/evcharging/runServer.sh " + datasetFile.getAbsolutePath() + " " + portNumber;
        ExecuteShellCommand exeCom = new ExecuteShellCommand();
        exeCom.executeCommand(com);
    }
}
