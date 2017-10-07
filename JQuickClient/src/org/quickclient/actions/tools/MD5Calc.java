package org.quickclient.actions.tools;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.swing.JTable;

import org.quickclient.actions.IQuickAction;
import org.quickclient.actions.QCActionException;
import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.SwingHelper;
import org.quickclient.gui.TextViewer;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLogger;
import com.documentum.fc.common.IDfId;

public class MD5Calc implements IQuickAction {

	private List<String> idlist;

	@Override
	public void execute() throws QCActionException {
		final DocuSessionManager smanager = DocuSessionManager.getInstance();
		IDfSession session = null;
		final StringBuilder sb = new StringBuilder();
		try {
			session = smanager.getSession();
			for (int i = 0; i < idlist.size(); i++) {
				final String objid = idlist.get(i);
				final IDfId id = new DfId(objid);
				final IDfSysObject obj = (IDfSysObject) session.getObject(id);
				if (obj != null) {
					final String objname = obj.getObjectName();
					final ByteArrayInputStream bais = obj.getContent();
					final String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(bais);
					sb.append(objname);
					sb.append(": ");
					sb.append(md5);
					sb.append("\n");
				}
			}
			final TextViewer tv = new TextViewer(false, "MD5Sums");
			tv.setText(sb.toString());
			tv.setVisible(true);
		} catch (final DfException ex) {
			DfLogger.error(this, ex.getMessage(), null, ex);
			SwingHelper.showErrorMessage("Error occurred!", ex.getMessage());
		} catch (final IOException ex) {
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
