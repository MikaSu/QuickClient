package org.quickclient.actions;

import java.util.List;

import javax.swing.JTable;

public interface IQuickAction  {


	public void setIdList(List<String> idlist);
	public void setTable(JTable t);
	public void execute() throws QCActionException;

}
