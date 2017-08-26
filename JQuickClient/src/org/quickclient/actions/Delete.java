package org.quickclient.actions;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.OperationMonitor;
import org.quickclient.classes.SwingHelper;

import com.documentum.com.DfClientX;
import com.documentum.com.IDfClientX;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLogger;
import com.documentum.fc.common.IDfList;
import com.documentum.operations.IDfDeleteOperation;
import com.documentum.operations.IDfOperationNode;

public class Delete implements IQuickAction {

	private List<String> idlist;

	@Override
	public void execute() throws QCActionException {
		final DocuSessionManager smanager = DocuSessionManager.getInstance();
		IDfSession session = null;
		try {
			session = smanager.getSession();

			boolean success = true;
			for (int i = 0; i < idlist.size(); i++) {
				final String objid = idlist.get(i);
				final IDfSysObject obj = (IDfSysObject) session.getObject(new DfId(objid));
				if (objid.startsWith("0b") || objid.startsWith("0c")) {
					final int answer = JOptionPane.showConfirmDialog(null, "Selected object '" + obj.getObjectName() + "' is folder, should all contents be deleted??!", "Confirm", JOptionPane.YES_NO_OPTION);
					if (answer == JOptionPane.YES_OPTION) {
						final IDfClientX clientx = new DfClientX();
						final IDfDeleteOperation deleop = clientx.getDeleteOperation();
						deleop.setVersionDeletionPolicy(IDfDeleteOperation.ALL_VERSIONS);
						deleop.enableDeepDeleteFolderChildren(false);
						deleop.enableDeepDeleteVirtualDocumentsInFolders(false);
						final IDfOperationNode node = deleop.add(obj);
						deleop.setOperationMonitor(new OperationMonitor());
						deleop.setDeepFolders(true);
						success = deleop.execute();
						final IDfList errors = deleop.getErrors();
						if (errors.getCount() > 0) {
							success = false;
						}
					} else {
						final IDfClientX clientx = new DfClientX();
						final IDfDeleteOperation deleop = clientx.getDeleteOperation();
						final IDfOperationNode node = deleop.add(obj);
						deleop.setOperationMonitor(new OperationMonitor());
						deleop.setDeepFolders(false);
						success = deleop.execute();
						final IDfList errors = deleop.getErrors();
						if (errors.getCount() > 0) {
							success = false;
						}
					}
				} else {
					final int answer = JOptionPane.showConfirmDialog(null, "Delete ALL versions?", "Choose", JOptionPane.YES_NO_OPTION);

					final IDfClientX clientx = new DfClientX();
					final IDfDeleteOperation deleop = clientx.getDeleteOperation();
					final IDfOperationNode node = deleop.add(obj);
					if (answer == JOptionPane.YES_OPTION) {
						deleop.setVersionDeletionPolicy(IDfDeleteOperation.ALL_VERSIONS);
					} else {
						deleop.setVersionDeletionPolicy(IDfDeleteOperation.SELECTED_VERSIONS);
					}

					deleop.setOperationMonitor(new OperationMonitor());
					success = deleop.execute();
					final IDfList errors = deleop.getErrors();
					if (errors.getCount() > 0) {
						success = false;
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
