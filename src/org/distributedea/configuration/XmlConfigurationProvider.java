package org.distributedea.configuration;

import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.configuration.Argument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class XmlConfigurationProvider {

	public static String AGENT = "agent";
	public static String NAME = "name";
	public static String TYPE = "type";
	public static String ARGUMENT = "argument";
	public static String KEY = "key";
	public static String VALUE = "value";
	public static String SENDONLYVALUE = "sendOnlyValue";
	

	/**
	 * Get AgentConfigurations from XML file
	 * 
	 * @param agent
	 * @param filePath
	 * @return
	 */
	public static AgentConfigurations getConfiguration(String filePath, AgentLogger logger) {
		
		List<AgentConfiguration> agentConfigurations =
				new ArrayList<AgentConfiguration>();
		
		try {

			File xmlFile = new File(filePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			
			Document document = dBuilder.parse(xmlFile);
			document.getDocumentElement().normalize();
			
			NodeList nodeList = document.getElementsByTagName(AGENT);

			for (int nodeIndex = 0; nodeIndex < nodeList.getLength(); nodeIndex++) {
				Node nNode = nodeList.item(nodeIndex);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					AgentConfiguration agentConf = getAgent(nNode);
					agentConfigurations.add(agentConf);
				}
			}
			return new AgentConfigurations(agentConfigurations);
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
	private static AgentConfiguration getAgent(Node nNode) {
		
		Element eElement = (Element) nNode;
		String name = eElement.getAttribute(NAME);
		String type = eElement.getAttribute(TYPE);
		List<Argument> arguments = getArguments(eElement);
		
		return new AgentConfiguration(name, type, arguments);
	}

	/**
	 * Get Arguments from XML Dom Element
	 * 
	 * @param agentElement
	 * @return
	 */
	private static List<Argument> getArguments(Element agentElement) {
		
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
				if (argElement.hasAttribute(SENDONLYVALUE)) {
					argument.setSendOnlyValue(true);
				}
				arguments.add(argument);
			}
		}
		return arguments;
	}
}
