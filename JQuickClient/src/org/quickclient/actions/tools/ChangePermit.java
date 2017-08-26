package org.quickclient.actions.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

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
	public void setIdList(List<String> idlist) {
		this.idlist = idlist;

	}

	@Override
	public void execute() throws QCActionException {
		
			final String newOwner = "";
			final AclBrowserData x = new AclBrowserData();

			final Vector myVector = (Vector) idlist;
			ActionListener al = new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					// //System.out.println(e);
					// //System.out.println(e.getSource().toString());
					DocuSessionManager smanager = DocuSessionManager.getInstance();
					IDfSession session = null;
					try {
						session = smanager.getSession();
						for (int i = 0; i < myVector.size(); i++) {
							String objid = (String) myVector.get(i);
							IDfId id = new DfId(objid);
							IDfSysObject obj = (IDfSysObject) session.getObject(id);
							obj.setString("acl_domain", x.getAclDomain());
							obj.setString("acl_name", x.getAclName());
							if (obj.isCheckedOut()) {
								obj.saveLock();
							} else {
								obj.save();
							}
						}
					} catch (DfException ex) {
						DfLogger.error(this,null,null,ex);
						SwingHelper.showErrorMessage("Error occurred!", ex.getMessage());
					} finally {
						if (session != null) {
							smanager.releaseSession(session);
						}
					}
				}
			};
			ACLBrowserFrame frame = new ACLBrowserFrame(al, x, true);

			frame.setVisible(true);


	}

	@Override
	public void setTable(JTable t) {

	}

}
