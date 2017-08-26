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
	public void setIdList(List<String> idlist) {
		this.idlist = idlist;

	}

	@Override
	public void execute() throws QCActionException {
		DocuSessionManager smanager = DocuSessionManager.getInstance();
		IDfSession session = null;
		try {
			session = smanager.getSession();
			String username = session.getUser(null).getUserName();
			for (int i = 0; i < idlist.size(); i++) {
				IDfCollection col = null;
				String objid = idlist.get(i);
				IDfId id = new DfId(objid);
				IDfSysObject obj = (IDfSysObject) session.getObject(id);
				String contentID = obj.getString("i_contents_id");
				IDfQuery qry = new DfQuery();
				qry.setDQL("execute get_path for '" + contentID + "'");
				col = qry.execute(session, DfQuery.DF_QUERY);
				String value = col.getString("_names");

				while (col.next()) {
					String result = col.getString(value);
					// JOptionPane.showMessageDialog(null, result,
					// "File located in:", JOptionPane.INFORMATION_MESSAGE);
					TxtPopup popup = new TxtPopup(500, 200, result);
					popup.setVisible(true);
					SwingHelper.centerJFrame(popup);
				}
				if (col != null) {
					try {
						col.close();
					} catch (DfException ex) {
						DfLogger.error(this,null,null,ex);
					}

				}
			}
		} catch (DfException ex) {
			DfLogger.error(this, ex.getMessage(), null, ex);
			SwingHelper.showErrorMessage("Error occurred!", ex.getMessage());
		} finally {
			if (session != null) {
				smanager.releaseSession(session);
			}
		}

	}

	@Override
	public void setTable(JTable t) {

	}

}
