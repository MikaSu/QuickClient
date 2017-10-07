package org.quickclient.actions.tools;

import java.util.List;

import javax.swing.JTable;

import org.quickclient.actions.IQuickAction;
import org.quickclient.actions.QCActionException;
import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.SwingHelper;
import org.quickclient.gui.TxtPopup;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLogger;
import com.documentum.fc.common.IDfId;

public class ShowFilesystemPath implements IQuickAction {

	private List<String> idlist;

	@Override
	public void execute() throws QCActionException {
		final DocuSessionManager smanager = DocuSessionManager.getInstance();
		IDfSession session = null;
		try {
			session = smanager.getSession();
			final String username = session.getUser(null).getUserName();
			for (int i = 0; i < idlist.size(); i++) {
				IDfCollection col = null;
				final String objid = idlist.get(i);
				final IDfId id = new DfId(objid);
				final IDfSysObject obj = (IDfSysObject) session.getObject(id);
				final String contentID = obj.getString("i_contents_id");
				final IDfQuery qry = new DfQuery();
				qry.setDQL("execute get_path for '" + contentID + "'");
				col = qry.execute(session, DfQuery.DF_QUERY);
				final String value = col.getString("_names");

				while (col.next()) {
					final String result = col.getString(value);
					final TxtPopup popup = new TxtPopup(500, 200, result);
					popup.setVisible(true);
					SwingHelper.centerJFrame(popup);
				}

				try {
					col.close();
				} catch (final DfException ex) {
					DfLogger.error(this, null, null, ex);
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
		// a
	}

}
