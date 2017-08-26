package org.quickclient.actions;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.SwingHelper;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLogger;

public class Rename implements IQuickAction {

	private List<String> idlist;

	@Override
	public void execute() throws QCActionException {
		final DocuSessionManager smanager = DocuSessionManager.getInstance();
		IDfSession session = null;
		try {
			session = smanager.getSession();
			for (int i = 0; i < idlist.size(); i++) {
				final String objid = idlist.get(i);
				final IDfSysObject obj = (IDfSysObject) session.getObject(new DfId(objid));

				final String objectName = obj.getString("object_name");
				final String newObjectName = JOptionPane.showInputDialog("Enter New Name", objectName);
				if (newObjectName.length() > 0 && newObjectName != null) {
					obj.setString("object_name", newObjectName);
					if (obj.isCheckedOut()) {
						obj.saveLock();
					} else {
						obj.save();
					}
				}
			}
		} catch (final DfException ex) {
			DfLogger.error(this, ex.getMessage(), null, ex);
			SwingHelper.showErrorMessage("Error occurred!", ex.getMessage());
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
		// not needed on this function
	}

}
