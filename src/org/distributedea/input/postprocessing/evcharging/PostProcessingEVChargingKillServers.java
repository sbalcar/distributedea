package org.distributedea.input.postprocessing.evcharging;

import java.net.URL;
import java.util.Vector;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.input.postprocessing.PostProcessing;

public class PostProcessingEVChargingKillServers  extends PostProcessing {

	private int port;
	private int serverCount;
	
	public PostProcessingEVChargingKillServers(int port, int serverCount) {
		this.port = port;
		this.serverCount = serverCount;
	}

	@Override
	public void run(Batch batch) throws Exception {
		
		for (int i = 0; i < serverCount; i++) {
			String url = "http://127.0.0.1:" + (port + i);
			
	        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
	        config.setServerURL(new URL(url));
	        XmlRpcClient server = new XmlRpcClient();
	        server.setConfig(config);
	        
	        Vector<String> params = new Vector<String>();
	        server.execute("quit", params);
		}

	}

}
