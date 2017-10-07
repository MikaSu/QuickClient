package org.quickclient.actions.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import org.quickclient.actions.IQuickAction;
import org.quickclient.actions.QCActionException;
import org.quickclient.classes.AclBrowserData;
import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.SwingHelper;
import org.quickclient.gui.ACLBrowserFrame;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLogger;
import com.documentum.fc.common.IDfId;

public class ChangePermit implements IQuickAction {

	private List<String> idlist;

	@Override
	public void execute() throws QCActionException {

		final AclBrowserData x = new AclBrowserData();

		final ArrayList<String> myVector = (ArrayList<String>) idlist;
		final ActionListener al = new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				final DocuSessionManager smanager = DocuSessionManager.getInstance();
				IDfSession session = null;
				try {
					session = smanager.getSession();
					for (int i = 0; i < myVector.size(); i++) {
						final String objid = myVector.get(i);
						final IDfId id = new DfId(objid);
						final IDfSysObject obj = (IDfSysObject) session.getObject(id);
						obj.setString("acl_domain", x.getAclDomain());
						obj.setString("acl_name", x.getAclName());
						if (obj.isCheckedOut()) {
							obj.saveLock();
						} else {
							obj.save();
						}
					}
				} catch (final DfException ex) {
					DfLogger.error(this, null, null, ex);
					SwingHelper.showErrorMessage("Error occurred!", ex.getMessage());
				} finally {
					if (session != null) {
						smanager.releaseSession(session);
					}
				}
			}
		};
		final ACLBrowserFrame frame = new ACLBrowserFrame(al, x, true);

		frame.setVisible(true);

	}

	@Override
	public void setIdList(final List<String> idlist) {
		this.idlist = idlist;

	}

	@Override
	public void setTable(final JTable t) {
		// a
	}

}
