package org.quickclient.actions.rendition;

import java.util.List;

import javax.swing.JTable;

import org.quickclient.actions.IQuickAction;
import org.quickclient.actions.QCActionException;
import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.SwingHelper;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLogger;
import com.documentum.fc.common.DfTime;
import com.documentum.fc.common.IDfId;
import com.documentum.fc.common.IDfTime;


public class RequestPDF implements IQuickAction {

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
				IDfSysObject obj = (IDfSysObject) session.getObject(id);
				IDfTime date = new DfTime();
				obj.queue("dm_autorender_win31", "rendition", 0, false, date, "rendition_req_ps_pdf");
			}
		} catch (DfException ex) {
			DfLogger.error(this, ex.getMessage(), null, ex);
			SwingHelper.showErrorMessage("Error occurred!", ex.getMessage());
		} finally {
			if (session != null) {
				smanager.releaseSession(session);
			}
		}

	}

	@Override
	public void setTable(JTable t) {
		// TODO Auto-generated method stub
		
	}

}
