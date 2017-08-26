package org.quickclient.gui;

import javax.swing.tree.DefaultMutableTreeNode;

import org.quickclient.classes.DokuData;

/**
 *
 * @author miksuoma
 *
 */
public class QuickClientMutableTreeNode extends DefaultMutableTreeNode {

	private DokuData ddata;
	private String objType = "dm_folder";
	private String specialString = "";

	/** Creates a new instance of QuickClientMutableTreeNode */
	public QuickClientMutableTreeNode() {
		super();
	}

	public QuickClientMutableTreeNode(final Object userObject) {
		super(userObject);
	}

	public QuickClientMutableTreeNode(final Object userObject, final boolean allowsChildren) {
		super(userObject, allowsChildren);
	}

	public DokuData getDokuData() {
		return ddata;
	}

	public String getDokuDataID() {
		return ddata.getObjID();
	}

	public String getObjType() {
		return objType;
	}

	/**
	 * @return the specialString
	 */
	public String getSpecialString() {
		return specialString;
	}

	public void setDokuData(final DokuData data) {
		this.ddata = data;
	}

	public void setObjType(final String objType) {
		this.objType = objType;
	}

	/**
	 * @param specialString
	 *            the specialString to set
	 */
	public void setSpecialString(final String specialString) {
		this.specialString = specialString;
	}
}
