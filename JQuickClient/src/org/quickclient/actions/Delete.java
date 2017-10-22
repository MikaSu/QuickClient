package org.quickclient.actions;

import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.DokuData;
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
	private JTable table;

	private boolean deleteFolder(final IDfSysObject obj) throws DfException {
		boolean success = false;
		final int answer2 = JOptionPane.showConfirmDialog(null, "Selected object '" + obj.getObjectName() + "' is folder, should all contents be deleted??!", "Confirm", JOptionPane.YES_NO_OPTION);
		if (answer2 == JOptionPane.YES_OPTION) {
			final IDfClientX clientx = new DfClientX();
			final IDfDeleteOperation deleop = clientx.getDeleteOperation();
			deleop.setVersionDeletionPolicy(IDfDeleteOperation.ALL_VERSIONS);
			deleop.enableDeepDeleteFolderChildren(false);
			deleop.enableDeepDeleteVirtualDocumentsInFolders(false);
			deleop.setOperationMonitor(new OperationMonitor());
			deleop.setDeepFolders(true);
			deleop.add(obj);
			success = deleop.execute();
			final IDfList errors = deleop.getErrors();
			if (errors != null && errors.getCount() > 0) {
				deleteFromTable(obj.getObjectId().getId());

			}
		} else {
			final IDfClientX clientx = new DfClientX();
			final IDfDeleteOperation deleop = clientx.getDeleteOperation();
			deleop.setOperationMonitor(new OperationMonitor());
			deleop.setDeepFolders(false);
			deleop.add(obj);
			success = deleop.execute();
			final IDfList errors = deleop.getErrors();
			if (errors != null && errors.getCount() > 0) {
				deleteFromTable(obj.getObjectId().getId());

			}
		}
		return success;
	}

	private void deleteFromTable(final String id) {
		final int rowcount = table.getRowCount();
		final DefaultTableModel tablemodel = (DefaultTableModel) table.getModel();
		for (int i = 0; i < rowcount; i++) {
			final Vector v = (Vector) tablemodel.getDataVector().elementAt(i);
			final DokuData d = (DokuData) v.lastElement();
			if (id.equals(d.getObjID())) {
				tablemodel.removeRow(i);
				break;
			}
		}
		table.validate();

	}

	@Override
	public void execute() throws QCActionException {
		final DocuSessionManager smanager = DocuSessionManager.getInstance();
		IDfSession session = null;
		final int answer1 = JOptionPane.showConfirmDialog(null, "Delete ALL versions?", "Choose", JOptionPane.YES_NO_OPTION);
		try {
			session = smanager.getSession();
			for (final String objid : idlist) {

				final IDfSysObject obj = (IDfSysObject) session.getObject(new DfId(objid));
				if (objid.startsWith("0b") || objid.startsWith("0c")) {
					final int answer2 = JOptionPane.showConfirmDialog(null, "Delete Folder " + obj.getObjectName() + " ?", "Choose", JOptionPane.YES_NO_OPTION);
					if (answer2 == JOptionPane.YES_OPTION) {
						deleteFolder(obj);
					}
				} else {
					final IDfClientX clientx = new DfClientX();
					final IDfDeleteOperation deleop = clientx.getDeleteOperation();
					final IDfOperationNode node = deleop.add(obj);
					if (answer1 == JOptionPane.YES_OPTION) {
						deleop.setVersionDeletionPolicy(IDfDeleteOperation.ALL_VERSIONS);
					} else {
						deleop.setVersionDeletionPolicy(IDfDeleteOperation.SELECTED_VERSIONS);
					}

					deleop.setOperationMonitor(new OperationMonitor());
					deleop.execute();
					final IDfList errors = deleop.getErrors();

					if (errors != null && errors.getCount() == 0) {

						deleteFromTable(obj.getObjectId().getId());

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
	public void setTable(final JTable table) {
		this.table = table;

	}

}
