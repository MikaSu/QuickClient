package org.quickclient.actions.tools;

import java.util.List;

import javax.swing.JTable;

import org.quickclient.actions.IQuickAction;
import org.quickclient.actions.QCActionException;
import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.SwingHelper;
import org.quickclient.gui.DumpFrame;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.IDfId;


public class Dump implements IQuickAction {

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
				IDfId id = new DfId(objid);
				DumpFrame dumpframe = new DumpFrame();
				dumpframe.setstrID(objid);
				dumpframe.setId(id);
				dumpframe.initData();
				SwingHelper.centerJFrame(dumpframe);
				dumpframe.setVisible(true);
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
