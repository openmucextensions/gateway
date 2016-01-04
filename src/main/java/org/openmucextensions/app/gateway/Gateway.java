package org.openmucextensions.app.gateway;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.openmuc.framework.dataaccess.DataAccessService;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Gateway {
	
	private static final Logger logger = LoggerFactory.getLogger(Gateway.class);
	
	private String configFilename = "./conf/wirings.xml";
	
	private DataAccessService dataAccessService = null;
	private List<Wiring> wirings = null;
	
	protected void activate(ComponentContext context) {
		
		if(System.getProperty("org.openmucextensions.app.gateway.configFilename") != null) {
			configFilename = System.getProperty("org.openmucextensions.app.gateway.configFilename");
		}
		
		File configFile = new File(configFilename);
		
		if (configFile.exists()) {
			try {

				wirings = ConfigurationUtil.loadConfiguration(configFilename);

				for (Wiring wiring : wirings) {
					wiring.wire(dataAccessService);
				}

				logger.debug("Applied {} wires from configuration file {}", wirings.size(), configFilename);

			} catch (IOException e) {
				logger.error("Error while trying to load configuration from file {}: {}", configFilename,
						e.getMessage());
			}
			logger.info("Gateway application activated");
		} else {
			logger.error("Configuration file {} dones't exist", configFilename);
			try {
				ConfigurationUtil.createTestConfiguration(configFile);
				logger.error("Created new file {}. Please edit file to configure wirings", configFilename);
			} catch (IOException e) {
				logger.warn("Error while trying to create confguration file {}: {}", configFilename, e.getMessage());
			}	
		}
	}
	
	protected void deactivate(ComponentContext context) {
		
		if(wirings != null) {
			for (Wiring wiring : wirings) {
				wiring.unwire();
			}
		}
		
		logger.info("Gateway application deactivated");
	}
	
	protected void setDataAccessService(DataAccessService service) {
		this.dataAccessService = service;
	}
	
	protected void unsetDataAccessService(DataAccessService service) {
		this.dataAccessService = null;
	}
	
}
