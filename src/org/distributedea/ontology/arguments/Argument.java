package org.distributedea.ontology.arguments;

import org.distributedea.logging.IAgentLogger;

import com.thoughtworks.xstream.XStream;

import jade.content.Concept;

/**
 * Structure represents one argument for Agent
 * @author stepan
 *
 */
public class Argument implements Concept {
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String value;
//	private Boolean sendOnlyValue;
	
	@Deprecated
	public Argument() { // only for Jade
	}
	
	/**
	 * Constructor
	 * @param name
	 * @param value
	 */
	public Argument(String name, String value) {
		this.value = value;
		this.name = name;
	}

	/**
	 * Copy constructor
	 * @param argument
	 */
	public Argument(Argument argument) {
		
		setName(argument.getName());
		setValue(argument.getValue());
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Exports value as integer
	 * @return
	 */
	public int exportValueAsInteger() {
		return Integer.parseInt(value);
	}
	
	/**
	 * Exports value as double
	 * @return
	 */
	public double exportValueAsDouble() {
		return Double.parseDouble(value);
	}
	
	/**
	 * Export value as Class
	 * @return
	 */
	public Class<?> exportValueAsClass() {
		
		try {
			return Class.forName(this.value);
			
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	
	@Override
	public boolean equals(Object other) {
		
	    if (!(other instanceof Argument)) {
	        return false;
	    }
	    
	    Argument argumentOuther = (Argument)other;
	    
	    boolean areAgentNameEqual =
	    		this.getName().equals(argumentOuther.getName());
	    boolean areAgentValuesEqual =
	    		this.getValue().equals(argumentOuther.getValue());
	    
	    return areAgentNameEqual && areAgentValuesEqual;
	}
	
    @Override
    public int hashCode() {
    	return toString().hashCode();
    }
    
	@Override
	public String toString() {
		return name + "-" + value + "-";// + sendOnlyValue;
	}
	
	/**
	 * Exports to the XML String
	 */
	public String exportXML() {

		XStream xstream = new XStream();
		xstream.setMode(XStream.NO_REFERENCES);

		return xstream.toXML(this);
	}
	
	/**
	 * Import the {@link Argument} from the String
	 */
	public static Argument importXML(String xml) {

		XStream xstream = new XStream();
		xstream.setMode(XStream.NO_REFERENCES);

		return (Argument) xstream.fromXML(xml);
	}
	
	
	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		return true;
	}

	/**
	 * Returns clone
	 * @return
	 */
	public Argument deepClone() {
		return new Argument(this);
	}
	
}
