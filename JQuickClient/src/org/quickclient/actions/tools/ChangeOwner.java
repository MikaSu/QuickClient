package org.quickclient.actions.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JTable;

import org.quickclient.actions.IQuickAction;
import org.quickclient.actions.QCActionException;
import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.SwingHelper;
import org.quickclient.classes.UserorGroupSelectorData;
import org.quickclient.gui.UserorGroupSelectorFrame;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLogger;
import com.documentum.fc.common.IDfId;

public class ChangeOwner implements IQuickAction {

	private List<String> idlist;

	@Override
	public void execute() throws QCActionException {

		final UserorGroupSelectorData x = new UserorGroupSelectorData();
		IDfSession session = null;
		final DocuSessionManager smanager = DocuSessionManager.getInstance();

		try {
			for (int i = 0; i < idlist.size(); i++) {
				final String objid = idlist.get(i);
				session = smanager.getSession();
				final IDfId id = new DfId(objid);
				final IDfSysObject obj = (IDfSysObject) session.getObject(id);
				final ActionListener al = new ActionListener() {

					@Override
					public void actionPerformed(final ActionEvent e) {
						try {
							obj.setString("owner_name", x.getUserorGroupname());
							if (obj.isCheckedOut()) {
								obj.saveLock();
							} else {
								obj.save();
							}

						} catch (final DfException ex) {
							DfLogger.error(this, null, null, ex);
							SwingHelper.showErrorMessage("Error occurred!", ex.getMessage());
						}

					}
				};

				final UserorGroupSelectorFrame frame = new UserorGroupSelectorFrame(al, x);
				SwingHelper.centerJFrame(frame);
				frame.setVisible(true);
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

	@Override
	public void setIdList(final List<String> idlist) {
		this.idlist = idlist;

	}

	@Override
	public void setTable(final JTable t) {
		// A
	}

}
