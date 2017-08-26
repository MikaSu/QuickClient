package org.quickclient.classes;

public class ListAttribute {
	public ListAttribute(String type, String attribute) {
		this.type = type;
		this.attribute = attribute;
	}

	public ListAttribute(String type, String attribute, String label) {
		this.type = type;
		this.attribute = attribute;
		this.label = label;
	}

	public String type = "";
	public String attribute = "";
	public String label = null;

	@Override
	public String toString() {
		if (label != null)
			return label;
		else
			return attribute;
	}

	public String getLabel() {
		if (label == null)
			return attribute;
		else
			return label;
	}

}
