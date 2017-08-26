package org.quickclient.actions;

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
import com.documentum.operations.IDfCancelCheckoutNode;
import com.documentum.operations.IDfCancelCheckoutOperation;

public class CancelCheckoutAction implements IQuickAction {

	private List<String> idlist;
	private JTable t;

	@Override
	public void execute() throws QCActionException {
		IDfSession session = null;
		final DocuSessionManager smanager = DocuSessionManager.getInstance();
		for (final String objid : idlist) {
			if (objid.length() == 16) {
				try {
					final IDfId id = new DfId(objid);
					session = smanager.getSession();
					final IDfSysObject obj = (IDfSysObject) session.getObject(id);
					final IDfClientX clientx = new DfClientX();
					final IDfCancelCheckoutOperation operation = clientx.getCancelCheckoutOperation();
					operation.setKeepLocalFile(false);

					if (obj.isCheckedOut() == false) {
						// //System.out.println("Object is not checked out.");
					} else {
						IDfCancelCheckoutNode node;
						if (obj.isVirtualDocument()) {
							final IDfVirtualDocument vDoc = obj.asVirtualDocument("CURRENT", false);
							node = (IDfCancelCheckoutNode) operation.add(vDoc);
						} else {
							node = (IDfCancelCheckoutNode) operation.add(obj);
						}
						operation.execute();
						final int row = t.getSelectedRow();
						t.setValueAt("", row, 0);
						t.validate();

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
