package org.openmucextensions.app.gateway;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestConfigurationUtil {

	ConfigurationUtil config;
	File configFile;
	
	@Before
	public void setUp() throws Exception {
		configFile = File.createTempFile("junit", "testcase");
	}

	@Test
	public void testLoadConfiguration() throws Exception {
		
		createTestConfiguration(configFile);
		List<Wiring> wirings = ConfigurationUtil.loadConfiguration(configFile.getCanonicalPath());
		assertThat(wirings.size(), is(2));
		assertThat(wirings.get(0).getInputChannelId(), is("input1"));
		assertThat(wirings.get(0).getOutputChannelId(), is("output1"));
		assertThat(wirings.get(1).getInputChannelId(), is("input2"));
		assertThat(wirings.get(1).getOutputChannelId(), is("output2"));
		
	}
	
	@After
	public void cleanUp() {
		configFile.delete();
	}
	
	private void createTestConfiguration(File file) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"); writer.newLine();
		writer.write("<wirings>"); writer.newLine();
		writer.write("<wiring><input>input1</input><output>output1</output></wiring>"); writer.newLine();
		writer.write("<wiring><input>input2</input><output>output2</output></wiring>"); writer.newLine();
		writer.write("</wirings>"); writer.newLine();
		writer.flush();
		writer.close();
	}

}
