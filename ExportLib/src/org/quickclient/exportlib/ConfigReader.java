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
 * @author miksuoma
 */
public class ConfigReader {

    private static String configFile = "";
    private static Properties props = null;

    private ConfigReader() {
    }

    public static ConfigReader getInstance() {
        return ConfigReaderHolder.INSTANCE;
    }

    private static class ConfigReaderHolder {

        private static final ConfigReader INSTANCE = new ConfigReader();
    }

    public void setConfigFile(String configfile) {
        configFile = configfile;
    }

    public void readProps() {

        FileInputStream fis = null;
        try {
            System.out.println(configFile);
            fis = new FileInputStream(configFile);
            props = new Properties();
            props.load(fis);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            System.exit(1);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
    }

    public String getProperty(String property) {
        return props.getProperty(property);
    }

    public Properties getAllProps() {
        return props;
    }
}
