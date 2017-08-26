/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.quickclient.exportlib;

import java.util.ArrayList;

/**
 * 
 * @author miksuoma
 */
public class TypeInfo {
	private String typeName;
	private final ArrayList<AttrInfo> attrInfo = new ArrayList<AttrInfo>();

	/**
	 * @return the typeName
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * @param typeName
	 *            the typeName to set
	 */
	public void setTypeName(final String typeName) {
		this.typeName = typeName;
	}

	/**
	 * Appends info of attribute type information
	 * 
	 * @param info
	 *            info
	 */
	public void appendAttrInfo(final AttrInfo info) {
		attrInfo.add(info);
	}

	/**
	 * Gets attribute info
	 * 
	 * @return attrInfo
	 */
	public ArrayList<AttrInfo> getAttributeInfo() {
		return attrInfo;
	}
}
