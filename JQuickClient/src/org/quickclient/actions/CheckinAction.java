package org.quickclient.actions;

import java.util.List;

import javax.swing.JTable;

import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.SwingHelper;
import org.quickclient.gui.CheckinFrame;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.IDfId;

public class CheckinAction implements IQuickAction {

	private List<String> idlist;
	private JTable t;

	@Override
	public void execute() throws QCActionException {
		final DocuSessionManager smanager = DocuSessionManager.getInstance();
		IDfSession session = null;
		try {
			session = smanager.getSession();
			for (int i = 0; i < idlist.size(); i++) {
				final String objid = idlist.get(i);
				if (objid.length() == 16) {

					final IDfId id = new DfId(objid);
					final CheckinFrame ciFrame = new CheckinFrame(id, t);

					SwingHelper.centerJFrame(ciFrame);
					ciFrame.setVisible(true);
				}
			}
		} finally {
			if (session != null) {
				smanager.releaseSession(session);
			}
		}
	}

	@Override
	public void setIdList(final List<String> idlist) {
		this.idlist = idlist;

	}

	@Override
	public void setTable(final JTable t) {
		this.t = t;
	}

}
