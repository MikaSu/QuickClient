package org.quickclient.actions;

import java.util.List;

import javax.swing.JTable;

import org.quickclient.classes.SwingHelper;
import org.quickclient.gui.PropertiesFrame;

public class Properties implements IQuickAction {

	private List<String> idlist;

	@Override
	public void execute() throws QCActionException {

		for (int i = 0; i < idlist.size(); i++) {
			final String objid = idlist.get(i);
			final PropertiesFrame frame = new PropertiesFrame();
			frame.setID(objid);
			frame.initData();
			SwingHelper.centerJFrame(frame);
			frame.setVisible(true);
		}
	}

	@Override
	public void setIdList(final List<String> idlist) {
		this.idlist = idlist;

	}

	@Override
	public void setTable(final JTable t) {
		// not needed on this function
	}

}
