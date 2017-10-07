package org.quickclient.actions;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.SwingHelper;

import com.documentum.fc.client.IDfRelation;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfUser;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLogger;

public class Subscribe implements IQuickAction {

	private List<String> idlist;

	@Override
	public void execute() throws QCActionException {
		final DocuSessionManager smanager = DocuSessionManager.getInstance();
		IDfSession session = null;
		try {
			session = smanager.getSession();
			final IDfUser luser = session.getUser(null);
			for (int i = 0; i < idlist.size(); i++) {
				final String objid = idlist.get(i);
				final IDfSysObject obj = (IDfSysObject) session.getObject(new DfId(objid));
				final IDfRelation relation = (IDfRelation) session.getObjectByQualification("dm_relation where relation_name = 'dm_subscription' and parent_id = '" + obj.getString("r_object_id") + "' and child_id = '" + luser.getString("r_object_id") + "'");
				if (relation == null) {
					final IDfRelation newrelation = (IDfRelation) session.newObject("dm_relation");
					newrelation.setParentId(new DfId(obj.getString("r_object_id")));
					newrelation.setChildId(new DfId(luser.getString("r_object_id")));
					newrelation.setRelationName("dm_subscription");
					newrelation.save();
				} else {
					JOptionPane.showMessageDialog(null, "Object is already subscribed", "ou", JOptionPane.INFORMATION_MESSAGE);
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
