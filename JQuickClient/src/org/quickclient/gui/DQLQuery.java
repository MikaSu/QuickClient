package org.quickclient.gui;

import groovy.model.DefaultTableModel;

public class DQLQuery {

	public static final String SELECT = "select";
	public static final String UPDATE = "update";
	public String dql;
	public boolean iserror;
	public DefaultTableModel model;
	public boolean isexecuted;
	public String querytype;

}
