package org.quickclient.actions;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JTable;

import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.SwingHelper;

import com.documentum.com.DfClientX;
import com.documentum.com.IDfClientX;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfVirtualDocument;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLogger;
import com.documentum.fc.common.IDfId;
import com.documentum.operations.IDfCheckoutNode;
import com.documentum.operations.IDfCheckoutOperation;

public class EditAction implements IQuickAction {

	private List<String> idlist;
	private JTable t;

	private void editObject(final File fileToOpen) {
		try {
			final Desktop desktop = Desktop.getDesktop();
			desktop.edit(fileToOpen);

		} catch (final IOException ex) {
			DfLogger.error(this, ex.getMessage(), null, ex);
			SwingHelper.showErrorMessage("Error occurred!", ex.getMessage());
		}

	}

	@Override
	public void execute() throws QCActionException {
		if (idlist.size() > 1) {
			return;
		}
		final String objid = idlist.get(0);
		final DocuSessionManager smanager = DocuSessionManager.getInstance();
		if (objid.length() == 16) {
			IDfSession session = null;
			try {
				final IDfId id = new DfId(objid);
				session = smanager.getSession();
				final IDfSysObject obj = (IDfSysObject) session.getObject(id);
				final IDfClientX clientx = new DfClientX();
				final IDfCheckoutOperation operation = clientx.getCheckoutOperation();
				if (!obj.isCheckedOut()) {
					// //System.out.println("Object is already checked
					// out.");
					return;
				}
				IDfCheckoutNode node;
				if (obj.isVirtualDocument()) {
					final IDfVirtualDocument vDoc = obj.asVirtualDocument("CURRENT", false);
					node = (IDfCheckoutNode) operation.add(vDoc);
				} else {
					node = (IDfCheckoutNode) operation.add(obj);
				}
				operation.execute();
				if (t != null) {
					final int row = t.getSelectedRow();
					t.setValueAt(session.getLoginUserName(), row, 0);
					t.validate();

				}
				final String filePath = node.getFilePath();
				final File fileToOpen = new File(filePath);
				editObject(fileToOpen);

			} catch (final DfException ex) {
				DfLogger.error(this, ex.getMessage(), null, ex);
				SwingHelper.showErrorMessage("Error occurred!", ex.getMessage());
			} finally {
				if (session != null) {
					smanager.releaseSession(session);
				}
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
