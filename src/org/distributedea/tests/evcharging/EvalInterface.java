package org.distributedea.tests.evcharging;


import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.distributedea.problems.evcharging.server.ServerThread;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Vector;

public class EvalInterface {

    XmlRpcClient server;
    
    String EVALUATE = "evaluate";
    String GET_INDIVIDUAL_SIZE = "get_individual_size";
    
    /**
     * Constructor of the evaluator interface.
     *
     * @param url The URL of the evaluating server e.g. http://localhost:8080
     * @throws java.net.MalformedURLException
     */
    public EvalInterface(String url) throws java.net.MalformedURLException {

        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL(url));
        this.server = new XmlRpcClient();
        this.server.setConfig(config);
    }

    /**
     * Evaluates the individual. Calls the Python server internally.
     *
     * @param x The individual
     * @return The fitness of the individual
     * @throws XmlRpcException
     */
    public Double eval(double[] x) throws XmlRpcException {

        Vector<String> params = new Vector<String>();
        params.addElement(Arrays.toString(x));

        Object[] result = (Object[])this.server.execute(EVALUATE, params);

        return (Double)result[0];
    }

    public int getIndividualSize() throws XmlRpcException {
        Vector<String> params = new Vector<String>();
        Object[] result = (Object[])this.server.execute(GET_INDIVIDUAL_SIZE, params);
        return (Integer)result[0];
    }

    public void killServer() {
        try {
            Vector<String> params = new Vector<String>();
            this.server.execute("quit", params);
        } catch (Exception ignored) { }
    }
    
    
    public static void main(String[] args) {

    	File file = new File("python/evcharging/config_advnn.json");
    	System.out.println(file.getAbsolutePath());
    	
        try {
        	Thread newThread = new ServerThread(file, 8080);
        	newThread.start();
        	
        	Thread.sleep(10000);
        	
            EvalInterface evaluator = new EvalInterface("http://127.0.0.1:8080");

            int N = evaluator.getIndividualSize();
            System.out.println(N);

            double[] ind = new double[N];
            for (int i = 0; i < N; i++) {
                ind[i] = Math.random();
            }

            System.out.println(Arrays.toString(ind));
            System.out.println(evaluator.eval(ind));

            evaluator.killServer();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}