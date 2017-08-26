package org.quickclient.classes;

import java.util.Vector;

public class ListConfiguration {
	
	public String configid;
	public String orderby;
	private Vector<ListAttribute> attributes = new Vector<ListAttribute>();
	
	public ListConfiguration(String configid) {
		this.configid = configid;
	}
	public void append(ListAttribute a) {
		attributes.add(a);
	}

	public Vector<ListAttribute> get() {
		return this.attributes;
	}
	@Override
	public String toString() {
		return "ListConfiguration [configid=" + configid + ", attributes=" + attributes + "]";
	}
	
	public String getOrderBy() {
		return "r_modify_date";
	}
	
}
