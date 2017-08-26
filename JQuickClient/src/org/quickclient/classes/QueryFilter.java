package org.quickclient.classes;

public class QueryFilter {
	
	
	public static int FILTER_TYPE_EXACT_MATCH = 0;
	public static int FILTER_TYPE_BEGINS_WITH = 1;
	public static int FILTER_TYPE_ENDS_WITH = 2;
	public static int FILTER_TYPE_CONTAINS = 3;
	public static int FILTER_TYPE_REGEX = 4;
	public static int FILTER_TYPE_MAX_ROWS = 5;
	
	private String attributename = "";
	private String requiredvalue = "";
	private boolean matchcase = true;
	private int filtertype = 0;
	private int maxcount = 0;
	
	
	public boolean isMatchcase() {
		return matchcase;
	}
	public void setMatchcase(boolean matchcase) {
		this.matchcase = matchcase;
	}
	public String getAttributename() {
		return attributename;
	}
	public void setAttributename(String attributename) {
		this.attributename = attributename;
	}
	public String getRequiredvalue() {
		return requiredvalue;
	}
	public void setRequiredvalue(String requiredvalue) {
		this.requiredvalue = requiredvalue;
	}
	public int getFiltertype() {
		return filtertype;
	}
	public void setFiltertype(int filtertype) {
		this.filtertype = filtertype;
	}
	
	public String toString() {
		
		return attributename + "=" + requiredvalue + " match case: " + matchcase + " validation type: " + filtertype;
	}
	public int getMaxcount() {
		
		return maxcount;
	}
	public void setMaxcount(int maxcount) {
		this.maxcount = maxcount;
	}
	

}
