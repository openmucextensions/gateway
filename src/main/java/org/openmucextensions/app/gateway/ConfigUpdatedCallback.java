package org.openmucextensions.app.gateway;

import java.util.List;

public interface ConfigUpdatedCallback {
	
	/**
	 * This callback will be called if the configuration has been updated recently. The
	 * passed wirings list contains all wirings and replaces an older wirings list passed
	 * in the past. If the configuration didn't contain any wirings, an empty list will
	 * be passed.
	 * 
	 * @param wirings
	 */
	public void configurationUpdated(List<Wiring> wirings);
	
}
