package configuration;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

public class TestConfiguration {

	public static void main(String[] args) throws ConfigurationException {
		
//		// create new configuration file
//		XMLConfiguration config = new XMLConfiguration();
//		config.setProperty("key", "value");
//		config.save("./config.xml");
		
		XMLConfiguration config = new XMLConfiguration("./config.xml");
		
		int numberOfWirings = config.getList("wirings.wiring.input").size();
		System.out.println("Found " + numberOfWirings + " wiring(s)");
		
		for (int index = 0; index < numberOfWirings; index++) {
			String path = "wirings.wiring(" + index + ")";
			String input = config.getString(path + ".input");
			String output = config.getString(path + ".output");
			config.getString(path + ".test"); // returns null because element not available
			
			System.out.println("Wiring: input=" + input + " output=" + output);
		}
		
	}

}
