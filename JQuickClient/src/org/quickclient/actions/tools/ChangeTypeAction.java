package org.quickclient.actions.tools;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import org.quickclient.actions.IQuickAction;
import org.quickclient.actions.QCActionException;
import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.SwingHelper;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLogger;
import com.documentum.fc.common.IDfId;


public class ChangeTypeAction implements IQuickAction {

	private List<String> idlist;

	@Override
	public void setIdList(List<String> idlist) {
		this.idlist = idlist;

	}

	@Override
	public void execute() throws QCActionException {
		DocuSessionManager smanager = DocuSessionManager.getInstance();
		IDfSession session = null;
		String newObjectType = (String) JOptionPane.showInputDialog("Enter new Type");

		for (int i = 0; i < idlist.size(); i++) {
			IDfCollection col = null;
			try {
				session = smanager.getSession();
				String objid = idlist.get(i);
				IDfId id = new DfId(objid);
				IDfSysObject obj = (IDfSysObject) session.getObject(id);
				String objectType = obj.getString("r_object_type");

				if (newObjectType != null) {
					if (newObjectType.length() > 0) {
						String Query = "change " + objectType + "(ALL) objects to " + newObjectType + " where r_object_id = '" + objid + "'";
						IDfQuery qry = new DfQuery();
						qry.setDQL(Query);
						col = qry.execute(session, DfQuery.DF_QUERY);
					}
				}
			} catch (DfException ex) {
				DfLogger.error(this, ex.getMessage(), null, ex);
				SwingHelper.showErrorMessage("Error occurred!", ex.getMessage());
			} finally {
				if (col != null)
					try {
						col.close();
					} catch (DfException ex) {
						DfLogger.error(this, ex.getMessage(), null, ex);
					}
				if (session != null) {
					smanager.releaseSession(session);
				}
			}
		}

	}

	@Override
	public void setTable(JTable t) {

	}

}
