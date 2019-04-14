import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class EvalInterface {

    XmlRpcClient server;

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
    @SuppressWarnings("unchecked")
    public Double eval(double[] x) throws XmlRpcException {

        Vector params = new Vector();
        params.addElement(Arrays.toString(x));

        Object[] result = (Object[])this.server.execute("evaluate", params);

        return (Double)result[0];
    }

    public int getIndividualSize() throws XmlRpcException {
        Vector params = new Vector();
        Object[] result = (Object[])this.server.execute("get_individual_size", params);
        return (Integer)result[0];
    }

    public void killServer() {
        try {
            Vector params = new Vector();
            this.server.execute("quit", params);
        } catch (Exception ignored) { }
    }

    public static void main(String[] args) {

        try {

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