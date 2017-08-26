/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quickclient.exportlib;

/**
 *
 * @author miksuoma
 */
class AttrInfo {

	private int datatype = 99;
	private String attrName = "";
	private boolean isrep = false;

	/**
	 * @return the datatype
	 */
	public int getDatatype() {
		return datatype;
	}

	/**
	 * @param datatype
	 *            the datatype to set
	 */
	public void setDatatype(final int datatype) {
		this.datatype = datatype;
	}

	/**
	 * @return the attrName
	 */
	public String getAttrName() {
		return attrName;
	}

	/**
	 * @param attrName
	 *            the attrName to set
	 */
	public void setAttrName(final String attrName) {
		this.attrName = attrName;
	}

	/**
	 * @return the isrep
	 */
	public boolean isIsrep() {
		return isrep;
	}

	/**
	 * @param isrep
	 *            the isrep to set
	 */
	public void setIsrep(final boolean isrep) {
		this.isrep = isrep;
	}

}
