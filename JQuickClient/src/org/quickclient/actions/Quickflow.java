package org.quickclient.actions;

import java.util.List;

import javax.swing.JTable;

import org.quickclient.classes.SwingHelper;
import org.quickclient.gui.SendToDistributionListFrame;

public class Quickflow implements IQuickAction {

	private List<String> idlist;

	@Override
	public void execute() throws QCActionException {

		final SendToDistributionListFrame frame = new SendToDistributionListFrame();
		frame.setObjids(idlist);
		SwingHelper.centerJFrame(frame);
		frame.setVisible(true);

	}

	@Override
	public void setIdList(final List<String> idlist) {
		this.idlist = idlist;

	}

	@Override
	public void setTable(final JTable t) {

	}

}
