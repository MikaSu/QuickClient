package org.quickclient.classes;

import java.util.ArrayList;

import javax.swing.JMenuItem;

public class QCFunction extends JMenuItem {
	
	private String menuname = "";
	private String actionname = "";
	private boolean showOnTopMenu = false;
	private boolean showOnContextMenu = false;
	private ArrayList<String> allowOnTypes = new ArrayList<String>();
	private int minimumPrivilege = 0;
	private boolean allowmulti = false;
	private String implementingClass = "";
	private String preconditionClass = "";
	

	
	@Override
	public String toString() {
		return "QCFunction [menuname=" + menuname + ", actionname=" + actionname + ", showOnTopMenu=" + showOnTopMenu + ", showOnContextMenu=" + showOnContextMenu + ", allowOnTypes=" + allowOnTypes + ", minimumPrivilege=" + minimumPrivilege + ", allowmulti=" + allowmulti + ", implementingClass="
				+ implementingClass + ", preconditionClass=" + preconditionClass + "]";
	}
	public String getMenuname() {
		return menuname;
	}
	public void setMenuname(String menuname) {
		this.menuname = menuname;
	}
	public String getActionname() {
		return actionname;
	}
	public void setActionname(String actionname) {
		this.actionname = actionname;
	}
	public boolean isShowOnTopMenu() {
		return showOnTopMenu;
	}
	public void setShowOnTopMenu(boolean showOnTopMenu) {
		this.showOnTopMenu = showOnTopMenu;
	}
	public boolean isShowOnContextMenu() {
		return showOnContextMenu;
	}
	public void setShowOnContextMenu(boolean showOnContextMenu) {
		this.showOnContextMenu = showOnContextMenu;
	}
	public ArrayList<String> getAllowOnTypes() {
		return allowOnTypes;
	}
	public void setAllowOnTypes(ArrayList<String> allowOnTypes) {
		this.allowOnTypes = allowOnTypes;
	}
	public int getMinimumPrivilege() {
		return minimumPrivilege;
	}
	public void setMinimumPrivilege(int minimumPrivilege) {
		this.minimumPrivilege = minimumPrivilege;
	}
	public boolean isAllowmulti() {
		return allowmulti;
	}
	public void setAllowmulti(boolean allowmulti) {
		this.allowmulti = allowmulti;
	}
	public String getImplementingClass() {
		return implementingClass;
	}
	public void setImplementingClass(String implementingClass) {
		this.implementingClass = implementingClass;
	}
	public String getPreconditionClass() {
		return preconditionClass;
	}
	public void setPreconditionClass(String preconditionClass) {
		this.preconditionClass = preconditionClass;
	}
}
