package org.quickclient.actions.tools;

import java.util.List;

import javax.swing.JTable;

import org.quickclient.actions.IQuickAction;
import org.quickclient.actions.QCActionException;
import org.quickclient.classes.DocuSessionManager;
import org.quickclient.gui.AttrEditorFrame3Text;

import com.documentum.fc.client.IDfSession;


public class AttrEdit implements IQuickAction {

	private List<String> idlist;

	@Override
	public void setIdList(List<String> idlist) {
		this.idlist = idlist;

	}

	@Override
	public void execute() throws QCActionException {
		DocuSessionManager smanager = DocuSessionManager.getInstance();
		IDfSession session = null;
		try {
			session = smanager.getSession();
			for (int i = 0; i < idlist.size(); i++) {
				String objid = idlist.get(i);

				AttrEditorFrame3Text attredit = new AttrEditorFrame3Text(objid);
				// AttrEditorFrame3Text attredit = new AttrEditorFrame3Text(objid);
				attredit.setVisible(true);
			}
		}  finally {
			if (session != null) {
				smanager.releaseSession(session);
			}
		}

	}

	@Override
	public void setTable(JTable t) {

	}

}
