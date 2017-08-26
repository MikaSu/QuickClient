package org.quickclient.gui;

import javax.swing.table.DefaultTableModel;


public class DefaultTableModelEx extends DefaultTableModel {

	private static final long serialVersionUID = 4295939648774062651L;
	String singlelineresult = "";

	public void setSinglelineresult(String singlelineresult) {
		this.singlelineresult = singlelineresult;
	}

	public String toString() {
		if (singlelineresult.length() > 0)
			return singlelineresult;
		else
			return "multiple rows: [" + this.getRowCount() + "]";

	}

}
