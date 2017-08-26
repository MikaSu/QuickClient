package org.quickclient.actions.rendition;

import java.util.List;

import javax.swing.JTable;

import org.quickclient.actions.IQuickAction;
import org.quickclient.actions.QCActionException;
import org.quickclient.gui.ImportRenditionFrame;


public class ImportRendition implements IQuickAction {

	private List<String> idlist;

	@Override
	public void setIdList(List<String> idlist) {
		this.idlist = idlist;

	}

	@Override
	public void execute() throws QCActionException {
		for (int i = 0; i < idlist.size(); i++) {
			String id = idlist.get(i);
			ImportRenditionFrame frame = new ImportRenditionFrame(id);
			frame.setVisible(true);
		}
	}

	@Override
	public void setTable(JTable t) {
		// TODO Auto-generated method stub

	}

}
