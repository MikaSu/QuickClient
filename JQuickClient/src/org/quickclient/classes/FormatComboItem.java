package org.quickclient.classes;

import com.documentum.fc.client.IDfFormat;
import com.documentum.fc.common.DfException;

public class FormatComboItem   {

	private String name;
	private String description;

	public FormatComboItem(IDfFormat f) throws DfException {
		this.name = f.getName();
		this.description = f.getDescription();
	}
	
	public String toString() {
		return description + " (" + name + ")";
		
	}

	public String getName() {
		return name;
	}

}
