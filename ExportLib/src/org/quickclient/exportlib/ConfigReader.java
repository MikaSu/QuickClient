/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quickclient.exportlib;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 *
 */
public class ConfigReader {

	private String configFile = "";
	private Properties props = null;

	private ConfigReader() {
	}

	public static ConfigReader getInstance() {
		return ConfigReaderHolder.INSTANCE;
	}

	private static class ConfigReaderHolder {

		private static final ConfigReader INSTANCE = new ConfigReader();
	}

	public void setConfigFile(final String configfile) {
		configFile = configfile;
	}

	public void readProps() {

		FileInputStream fis = null;
		try {
			System.out.println(configFile);
			fis = new FileInputStream(configFile);
			props = new Properties();
			props.load(fis);
		} catch (final FileNotFoundException ex) {
			ex.printStackTrace();
			System.exit(1);
		} catch (final IOException ex) {
			ex.printStackTrace();
			System.exit(1);
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (final IOException ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
	}

	public String getProperty(final String property) {
		return props.getProperty(property);
	}

	public Properties getAllProps() {
		return props;
	}
}
