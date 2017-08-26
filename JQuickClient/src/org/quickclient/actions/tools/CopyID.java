package org.quickclient.actions.tools;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.List;

import javax.swing.JTable;

import org.quickclient.actions.IQuickAction;
import org.quickclient.actions.QCActionException;


public class CopyID implements IQuickAction {
	private List<String> idlist;
	@Override
	public void execute() throws QCActionException {
		String ids = idlist.toString();
		if (idlist.size()==1) {
			ids = idlist.get(0);
			StringSelection s = new StringSelection(ids);
			Clipboard cp = Toolkit.getDefaultToolkit().getSystemClipboard();
			cp.setContents(s, s);

		} else {
		StringSelection s = new StringSelection(ids);
		Clipboard cp = Toolkit.getDefaultToolkit().getSystemClipboard();
		cp.setContents(s, s);
		}
	}

	@Override
	public void setIdList(List<String> idlist) {
		this.idlist = idlist;
	}

	@Override
	public void setTable(JTable arg0) {
		

	}

}
