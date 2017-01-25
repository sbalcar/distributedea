package org.distributedea.agents.systemagents.initiator;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.configuration.AgentConfigurations;
import org.distributedea.ontology.configuration.Argument;
import org.distributedea.ontology.configuration.Arguments;
import org.distributedea.ontology.configurationinput.InputAgentConfiguration;
import org.distributedea.ontology.configurationinput.InputAgentConfigurations;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Tool which can read {@link InputAgentConfigurations} from XML file
 * @author stepan
 *
 */
public class XmlConfigurationProvider {

	public static String AGENT = "agent";
	public static String NAME = "name";
	public static String TYPE = "type";
	public static String ARGUMENT = "argument";
	public static String KEY = "key";
	public static String VALUE = "value";
	public static String SENDONLYVALUE = "sendOnlyValue";
	

	/**
	 * Get {@link AgentConfigurations} from XML file
	 * 
	 * @param agent
	 * @param filePath
	 * @return
	 */
	public static InputAgentConfigurations getConfiguration(File xmlFile, IAgentLogger logger) {
		
		List<InputAgentConfiguration> agentConfigurations =
				new ArrayList<>();
		
		try {

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			
			Document document = dBuilder.parse(xmlFile);
			document.getDocumentElement().normalize();
			
			NodeList nodeList = document.getElementsByTagName(AGENT);

			for (int nodeIndex = 0; nodeIndex < nodeList.getLength(); nodeIndex++) {
				Node nNode = nodeList.item(nodeIndex);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					InputAgentConfiguration agentConf = getAgent(nNode);
					agentConfigurations.add(agentConf);
				}
			}
			return new InputAgentConfigurations(agentConfigurations);
		} catch (Exception e) {
			logger.logThrowable("Unexpected error occured:", e);
			return null;
		}
	}

	/**
	 * Get agentConfiguration from XML Dom Node
	 * 
	 * @param nNode
	 * @return
	 */
	private static InputAgentConfiguration getAgent(Node nNode) {
		
		Element eElement = (Element) nNode;
		String name = eElement.getAttribute(NAME);
		String type = eElement.getAttribute(TYPE);
		Arguments arguments = getArguments(eElement);
		
		Class<?> agentClass = null;
		try {
			agentClass =  Class.forName(type);
		} catch (ClassNotFoundException e) {
		}
		
		return new InputAgentConfiguration(name, agentClass, arguments);
	}

	/**
	 * Get Arguments from XML Dom Element
	 * 
	 * @param agentElement
	 * @return
	 */
	private static Arguments getArguments(Element agentElement) {
		
		List<Argument> arguments = new ArrayList<Argument>();
		
		NodeList argumentsNodeList =
				agentElement.getElementsByTagName(ARGUMENT);
		
		for (int argIndex = 0; argIndex < argumentsNodeList.getLength(); argIndex++) {
			
			Node argNode = argumentsNodeList.item(argIndex);
			
			if (argNode.getNodeType() == Node.ELEMENT_NODE) {

				Element argElement = (Element) argNode;
				
				String key = argElement.getAttribute(KEY);
				String value = argElement.getAttribute(VALUE);
				
				Argument argument = new Argument(key, value);
				arguments.add(argument);
			}
		}
		return new Arguments(arguments);
	}
}
