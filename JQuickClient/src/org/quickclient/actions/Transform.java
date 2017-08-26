package org.quickclient.actions;

import java.util.List;

import javax.swing.JTable;

import org.quickclient.classes.SwingHelper;
import org.quickclient.gui.TransformFrame;

public class Transform implements IQuickAction {

	private List<String> idlist;

	@Override
	public void execute() throws QCActionException {

		for (int i = 0; i < idlist.size(); i++) {
			final String objid = idlist.get(i);
			final TransformFrame tf = new TransformFrame(objid);
			SwingHelper.centerJFrame(tf);
			tf.setVisible(true);
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
