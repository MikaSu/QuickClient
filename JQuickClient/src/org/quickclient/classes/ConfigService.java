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
	public static String DEFAULT_IMPORT_FORMAT = "defaultimportformat";
	public static String DEFAULT_IMPORT_TYPE = "defaultimporttype";
	public static String DEFAULT_SEARCH_TYPE = "defaultsearchtype";
	public static String PARAM_THUMBNAIL_SIZE = "thumbnail_size";
	private static ConfigService instance = null;

	public static ConfigService getInstance() {
		if (instance == null) {
			instance = new ConfigService();
		}
		return instance;
	}

	Logger log = Logger.getLogger(ConfigService.class);
	private final Properties props;
	private boolean showThumbnails = false;
	private final boolean showAllVersions = false;
	private IDfUser loggedInUser;
	private String homeFolderId;
	private Vector<ListConfiguration> attributes;
	private String orderByString;
	private String scriptdir;
	private String plugindir;
	private String componentdir;
	private ScriptHelper scripthelper;
	private PluginHelper pluginhelper;
	private final HashMap<String, String> preferredFormats;
	private JDesktopPane desktopPane;
	private String username;
	private String docbasename;
	private String dqlfile;
	private boolean dqldocbase = false;

	private String currentlistconfig;

	public ConfigService() {
		props = new Properties();
		preferredFormats = new HashMap<java.lang.String, java.lang.String>();
		readProperties();
		readScripts();
		readPlugins();
	}

	public ListConfiguration getAttributes(final String config) {
		System.out.println("getting ListConfiguration config: " + config);
		for (final ListConfiguration lc : attributes) {
			System.out.println(lc);
			if (lc.configid.equals(config)) {
				return lc;
			}
		}
		if (attributes.get(0) != null) {
			return attributes.get(0); // fallback to first
		} else {
			return null;
		}
	}

	public String getCurrentListConfigName() {

		return currentlistconfig;
	}

	public JDesktopPane getDesktop() {
		return desktopPane;
	}

	public String getDocbasename() {
		return docbasename;
	}

	public String getDqlfile() {
		return dqlfile;
	}

	public String getHomeFolderId() {
		return homeFolderId;
	}

	public ArrayList<String> getListConfigNames() {
		final ArrayList<String> configs = new ArrayList<String>();
		for (final ListConfiguration lc : attributes) {
			configs.add(lc.configid);
		}
		return configs;
	}

	public IDfUser getLoggedInUser() {
		return loggedInUser;
	}

	public String getParameter(final String xx) {

		final String prop = props.getProperty(xx);
		if (prop != null) {
			return props.getProperty(xx);
		} else {
			if (!xx.contains("password")) {
				if (!xx.contains("username")) {
					if (!xx.contains("viewpath")) {
						JOptionPane.showMessageDialog(desktopPane, "Missing parameter: " + xx + " in quickclient.properties", prop, JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			return "";
		}
	}

	public PluginHelper getPluginhelper() {
		return pluginhelper;
	}

	public HashMap getPreferredFormats() {
		return preferredFormats;
	}

	public String getScriptdir() {
		return scriptdir;
	}

	public ScriptHelper getScripthelper() {
		return scripthelper;
	}

	public String getUsername() {
		return username;
	}

	public boolean isDqldocbase() {
		return dqldocbase;
	}

	public boolean isShowThumbnails() {
		return showThumbnails;
	}

	private void readPlugins() {
		// System.out.println("plugindir: " + plugindir);
		if (plugindir != null) {
			final PluginHelper helper = new PluginHelper();
			final File sdir = new File(plugindir);
			final File dirs[] = sdir.listFiles();
			if (dirs != null) {
				for (final File x : dirs) {
					if (x.isDirectory()) {
						// System.out.println(x.toString() + " is a directory");
						try {
							final FileInputStream in = new FileInputStream(x.toString() + "/" + "plugin.properties");
							final Properties newprops = new Properties();
							newprops.load(in);
							in.close();
							// System.out.println(newprops.getProperty("menuname"));
							final PluginConfig pluginConfig = new PluginConfig();
							pluginConfig.setMenuName(newprops.getProperty("menuname"));
							pluginConfig.setTooltip(newprops.getProperty("tooltip", "detault tooltip"));
							pluginConfig.setFilePath(x.toString() + "/" + newprops.getProperty("jar"));
							pluginConfig.setClassName(newprops.getProperty("classname"));
							helper.add(pluginConfig);
						} catch (final FileNotFoundException ex) {
							log.error(ex);
						} catch (final IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			pluginhelper = helper;
		}
	}

	public void readProperties() {
		FileInputStream in = null;

		try {
			in = new FileInputStream("quickclient.properties");
		} catch (final FileNotFoundException ex) {

		}
		try {
			in = new FileInputStream("quickclient.properties");
			props.load(in);
			in.close();
		} catch (final FileNotFoundException ex) {
			log.error(ex);
		} catch (final IOException ex) {
			log.error(ex);
		} catch (final SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (final IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		final Enumeration<?> e = props.propertyNames();
		while (e.hasMoreElements()) {
			final String joo = (String) e.nextElement();
			if (joo.startsWith("preferredformat")) {
				final String xx = props.getProperty(joo);
				final String splitti[] = xx.split(",");
				preferredFormats.put(splitti[0], splitti[1]);
			}
		}
		scriptdir = props.getProperty("scriptdir");
		plugindir = props.getProperty("plugindir");
		dqlfile = props.getProperty("dqlfile");
		final String hoo = props.getProperty("dqlfileindocbase", "false");
		if (hoo.equalsIgnoreCase("true")) {
			dqldocbase = true;
		} else {
			dqldocbase = false;
		}
		orderByString = props.getProperty("orderbyattributes", "object_name, r_object_id");

	}

	private void readScripts() {
		// System.out.println("scriptdir: " + scriptdir);
		if (scriptdir != null) {
			final ScriptHelper helper = new ScriptHelper();
			final File sdir = new File(scriptdir);
			final File dirs[] = sdir.listFiles();
			if (dirs != null) {
				for (final File x : dirs) {
					if (x.isDirectory()) {
						// System.out.println(x.toString() + " is a directory");
						try {
							final FileInputStream in = new FileInputStream(x.toString() + "/" + "script.properties");
							final Properties newprops = new Properties();
							newprops.load(in);
							in.close();
							// System.out.println(newprops.getProperty("menuname"));
							final ScriptConfig sc = new ScriptConfig();
							sc.setMenuName(newprops.getProperty("menuname"));
							sc.setTooltip(newprops.getProperty("tooltip", "detault tooltip"));
							sc.setFilePath(x.toString() + "/" + "groovy.script");
							helper.add(sc);
						} catch (final FileNotFoundException ex) {
							log.error(ex);
						} catch (final IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			scripthelper = helper;
		}
	}

	public void saveProperties() {

		FileOutputStream out = null;
		try {
			out = new FileOutputStream("quickclient.properties");
			props.setProperty("showthumbnails", Boolean.toString(showThumbnails));
			props.store(out, "QuickClient properties file, see http://quickclient.org");
			out.close();
		} catch (final FileNotFoundException ex) {
			log.error(ex);
		} catch (final IOException ex) {
			log.error(ex);
		}

	}

	public void setCurrentListConfigName(final String string) {
		this.currentlistconfig = string;

	}

	public void setDesktop(final JDesktopPane desktopPane) {
		this.desktopPane = desktopPane;
	}

	public void setDocbaseName(final String docbaseName) {
		this.docbasename = docbaseName;
		attributes = XMLConfigReader.readAttributes(docbaseName); // TODO
																	// temporary
																	// hack.
		for (final ListConfiguration lc : attributes) {
			this.currentlistconfig = lc.configid;
		}
	}

	public void setDqldocbase(final boolean dqldocbase) {
		this.dqldocbase = dqldocbase;
		if (dqldocbase) {
			props.setProperty("dqlfileindocbase", "true");
		} else {
			props.setProperty("dqlfileindocbase", "false");
		}
		saveProperties();
	}

	public void setHomeFolderId(final String homeFolderId) {
		this.homeFolderId = homeFolderId;
	}

	public void setLoggedInUser(final IDfUser loggedInUser) {
		this.loggedInUser = loggedInUser;
	}

	public void setShowThumbnails(final boolean showThumbnails) {
		this.showThumbnails = showThumbnails;
	}

	public void setUserName(final String loginUserName) {
		this.username = loginUserName;
	}
}
