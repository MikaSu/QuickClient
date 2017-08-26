/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quickclient.classes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.documentum.fc.client.IDfUser;

public class ConfigService {
	Logger log = Logger.getLogger(ConfigService.class);
	public static String DEFAULT_IMPORT_FORMAT = "defaultimportformat";
	public static String DEFAULT_IMPORT_TYPE = "defaultimporttype";
	public static String DEFAULT_SEARCH_TYPE = "defaultsearchtype";
	public static String PARAM_THUMBNAIL_SIZE = "thumbnail_size";
	private Properties props;
	private boolean showThumbnails = false;
	private boolean showAllVersions = false;
	private IDfUser loggedInUser;
	private String homeFolderId;
	private static ConfigService instance = null;
	private Vector<ListConfiguration> attributes;
	private String orderByString;
	private String scriptdir;
	private String plugindir;
	private String componentdir;
	private ScriptHelper scripthelper;
	private PluginHelper pluginhelper;
	private HashMap<String, String> preferredFormats;
	private JDesktopPane desktopPane;
	private String username;
	private String docbasename;
	private String dqlfile;
	private boolean dqldocbase = false;
	private String currentlistconfig;

	public ListConfiguration getAttributes(String config) {
		System.out.println("getting ListConfiguration config: " + config);
		for (ListConfiguration lc : attributes) {
			System.out.println(lc);
			if (lc.configid.equals(config))
				return lc;
		}
		if (attributes.get(0) != null)
			return attributes.get(0); // fallback to first
		else
			return null;
	}

	public ArrayList<String> getListConfigNames() {
		ArrayList<String> configs = new ArrayList<String>();
		for (ListConfiguration lc : attributes) {
			configs.add(lc.configid);
		}
		return configs;
	}

	public String getParameter(String xx) {

		String prop = props.getProperty(xx);
		if (prop != null) {
			return props.getProperty(xx);
		} else {
			if (!xx.contains("password"))
				if (!xx.contains("username"))
					if (!xx.contains("viewpath"))
					JOptionPane.showMessageDialog(desktopPane, "Missing parameter: " + xx + " in quickclient.properties", prop, JOptionPane.ERROR_MESSAGE);
			return "";
		}
	}

	public ConfigService() {
		props = new Properties();
		preferredFormats = new HashMap<java.lang.String, java.lang.String>();
		readProperties();
		readScripts();
		readPlugins();
	}

	public static ConfigService getInstance() {
		if (instance == null) {
			instance = new ConfigService();
		}
		return instance;
	}

	public IDfUser getLoggedInUser() {
		return loggedInUser;
	}

	public void setDesktop(JDesktopPane desktopPane) {
		this.desktopPane = desktopPane;
	}

	public JDesktopPane getDesktop() {
		return desktopPane;
	}

	public void setDocbaseName(String docbaseName) {
		this.docbasename = docbaseName;
		attributes = XMLConfigReader.readAttributes(docbaseName); // TODO
																	// temporary
																	// hack.
		for (ListConfiguration lc : attributes) {
			this.currentlistconfig = lc.configid;
		}
	}

	public void setLoggedInUser(IDfUser loggedInUser) {
		this.loggedInUser = loggedInUser;
	}

	public void readProperties() {
		FileInputStream in = null;

		try {
			in = new FileInputStream("quickclient.properties");
		} catch (FileNotFoundException ex) {
			
		}
		try {
			in = new FileInputStream("quickclient.properties");
			props.load(in);
			in.close();
		} catch (FileNotFoundException ex) {
			log.error(ex);
		} catch (IOException ex) {
			log.error(ex);
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Enumeration<?> e = props.propertyNames();
		while (e.hasMoreElements()) {
			String joo = (String) e.nextElement();
			if (joo.startsWith("preferredformat")) {
				String xx = props.getProperty(joo);
				String splitti[] = xx.split(",");
				preferredFormats.put(splitti[0], splitti[1]);
			}
		}
		scriptdir = props.getProperty("scriptdir");
		plugindir = props.getProperty("plugindir");
		dqlfile = props.getProperty("dqlfile");
		String hoo = props.getProperty("dqlfileindocbase", "false");
		if (hoo.equalsIgnoreCase("true")) {
			dqldocbase = true;
		} else {
			dqldocbase = false;
		}
		orderByString = props.getProperty("orderbyattributes", "object_name, r_object_id");

	}

	public void saveProperties() {

		FileOutputStream out = null;
		try {
			out = new FileOutputStream("quickclient.properties");
			props.setProperty("showthumbnails", Boolean.toString(showThumbnails));
			props.store(out, "QuickClient properties file, see http://www.fortica.fi/quickclient");
			out.close();
		} catch (FileNotFoundException ex) {
			log.error(ex);
		} catch (IOException ex) {
			log.error(ex);
		}

	}

	public String getHomeFolderId() {
		return homeFolderId;
	}

	public void setHomeFolderId(String homeFolderId) {
		this.homeFolderId = homeFolderId;
	}

	public boolean isShowThumbnails() {
		return showThumbnails;
	}

	public void setShowThumbnails(boolean showThumbnails) {
		this.showThumbnails = showThumbnails;
	}

	public String getScriptdir() {
		return scriptdir;
	}

	public void setUserName(String loginUserName) {
		this.username = loginUserName;
	}

	private void readScripts() {
		// System.out.println("scriptdir: " + scriptdir);
		if (scriptdir != null) {
			ScriptHelper helper = new ScriptHelper();
			File sdir = new File(scriptdir);
			File dirs[] = sdir.listFiles();
			if (dirs != null) {
				for (int i = 0; i < dirs.length; i++) {
					File x = dirs[i];
					if (x.isDirectory()) {
						// System.out.println(x.toString() + " is a directory");
						try {
							FileInputStream in = new FileInputStream(x.toString() + "/" + "script.properties");
							Properties newprops = new Properties();
							newprops.load(in);
							in.close();
							// System.out.println(newprops.getProperty("menuname"));
							ScriptConfig sc = new ScriptConfig();
							sc.setMenuName(newprops.getProperty("menuname"));
							sc.setTooltip(newprops.getProperty("tooltip", "detault tooltip"));
							sc.setFilePath(x.toString() + "/" + "groovy.script");
							helper.add(sc);
						} catch (FileNotFoundException ex) {
							log.error(ex);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			scripthelper = helper;
		}
	}

	private void readPlugins() {
		// System.out.println("plugindir: " + plugindir);
		if (plugindir != null) {
			PluginHelper helper = new PluginHelper();
			File sdir = new File(plugindir);
			File dirs[] = sdir.listFiles();
			if (dirs != null) {
				for (int i = 0; i < dirs.length; i++) {
					File x = dirs[i];
					if (x.isDirectory()) {
						// System.out.println(x.toString() + " is a directory");
						try {
							FileInputStream in = new FileInputStream(x.toString() + "/" + "plugin.properties");
							Properties newprops = new Properties();
							newprops.load(in);
							in.close();
							// System.out.println(newprops.getProperty("menuname"));
							PluginConfig pluginConfig = new PluginConfig();
							pluginConfig.setMenuName(newprops.getProperty("menuname"));
							pluginConfig.setTooltip(newprops.getProperty("tooltip", "detault tooltip"));
							pluginConfig.setFilePath(x.toString() + "/" + newprops.getProperty("jar"));
							pluginConfig.setClassName(newprops.getProperty("classname"));
							helper.add(pluginConfig);
						} catch (FileNotFoundException ex) {
							log.error(ex);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			pluginhelper = helper;
		}
	}

	public ScriptHelper getScripthelper() {
		return scripthelper;
	}

	public PluginHelper getPluginhelper() {
		return pluginhelper;
	}

	public HashMap getPreferredFormats() {
		return preferredFormats;
	}

	public String getUsername() {
		return username;
	}

	public String getDocbasename() {
		return docbasename;
	}

	public String getDqlfile() {
		return dqlfile;
	}

	public boolean isDqldocbase() {
		return dqldocbase;
	}

	public void setDqldocbase(boolean dqldocbase) {
		this.dqldocbase = dqldocbase;
		if (dqldocbase) {
			props.setProperty("dqlfileindocbase", "true");
		} else {
			props.setProperty("dqlfileindocbase", "false");
		}
		saveProperties();
	}

	public String getCurrentListConfigName() {

		return currentlistconfig;
	}

	public void setCurrentListConfigName(String string) {
		this.currentlistconfig = string;

	}
}
