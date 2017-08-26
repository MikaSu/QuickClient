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
import com.documentum.fc.client.IDfVirtualDocument;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLogger;
import com.documentum.fc.common.IDfId;
import com.documentum.operations.IDfCheckoutNode;
import com.documentum.operations.IDfCheckoutOperation;

public class CheckoutAction implements IQuickAction {

	private List<String> idlist;
	private JTable t;

	@Override
	public void execute() throws QCActionException {
		if (idlist.size() == 1) {
			IDfSession session = null;
			final String objid = idlist.get(0);
			final DocuSessionManager smanager = DocuSessionManager.getInstance();
			if (objid.length() == 16) {
				try {
					session = smanager.getSession();
					final IDfId id = new DfId(objid);
					final IDfSysObject obj = (IDfSysObject) session.getObject(id);
					boolean docheckout = true;
					if (!obj.getBoolean("i_has_folder")) {
						final int answer = JOptionPane.showConfirmDialog(null, "Select object is old version, continue with Checkout?", "Confirm", JOptionPane.YES_NO_OPTION);
						if (answer == JOptionPane.NO_OPTION) {
							docheckout = false;
						}

					}
					if (docheckout) {
						final IDfClientX clientx = new DfClientX();
						final IDfCheckoutOperation operation = clientx.getCheckoutOperation();
						operation.setOperationMonitor(new OperationMonitor());
						if (obj.isCheckedOut() == true) {
							JOptionPane.showMessageDialog(null, "Already checked out.", "Error occured!", JOptionPane.ERROR_MESSAGE);
							return;
						} else {
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
			} else {
				JOptionPane.showMessageDialog(null, "User Error.", "Error occured!", JOptionPane.ERROR_MESSAGE);

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
