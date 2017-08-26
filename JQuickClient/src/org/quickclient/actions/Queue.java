package org.quickclient.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.SwingHelper;
import org.quickclient.classes.UserorGroupSelectorData;
import org.quickclient.gui.UserorGroupSelectorFrame;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLogger;
import com.documentum.fc.common.DfTime;
import com.documentum.fc.common.IDfId;
import com.documentum.fc.common.IDfTime;

public class Queue implements IQuickAction {

	private List<String> idlist;

	@Override
	public void execute() throws QCActionException {
		final DocuSessionManager smanager = DocuSessionManager.getInstance();
		IDfSession session = null;
		try {
			session = smanager.getSession();
			for (int i = 0; i < idlist.size(); i++) {
				final String newOwner = "";
				final UserorGroupSelectorData x = new UserorGroupSelectorData();

				final String objid = idlist.get(i);
				session = smanager.getSession();
				final IDfId id = new DfId(objid);
				final IDfSysObject obj = (IDfSysObject) session.getObject(id);
				final String user = session.getUser(null).getUserName();
				final ActionListener al = new ActionListener() {

					@Override
					public void actionPerformed(final ActionEvent e) {
						// //System.out.println(e);
						// //System.out.println(e.getSource().toString());
						try {
							final Calendar cal = Calendar.getInstance();
							cal.add(Calendar.WEEK_OF_YEAR, 4);
							final IDfTime time = new DfTime(cal.getTime());
							final String message = JOptionPane.showInputDialog("Please enter message for receiver", "Notification from " + user);
							obj.queue(x.getUserorGroupname(), "Notification", 1, true, time, message);
						} catch (final DfException ex) {
							DfLogger.error(this, ex.getMessage(), null, ex);
							SwingHelper.showErrorMessage("Error occurred!", ex.getMessage());
						}
					}
				};
				final UserorGroupSelectorFrame frame = new UserorGroupSelectorFrame(al, x);
				SwingHelper.centerJFrame(frame);
				frame.setVisible(true);
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

	}

}
