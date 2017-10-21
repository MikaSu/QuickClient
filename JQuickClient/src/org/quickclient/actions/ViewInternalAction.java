package org.quickclient.actions;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.swing.JTable;

import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.SwingHelper;
import org.quickclient.gui.TextViewer;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLogger;
import com.documentum.fc.common.IDfId;

public class ViewInternalAction implements IQuickAction {

	private List<String> idlist;

	@Override
	public void execute() throws QCActionException {
		final DocuSessionManager smanager = DocuSessionManager.getInstance();
		for (final Object objid : idlist) {
			IDfSession session = null;
			try {
				final IDfId id = new DfId((String) objid);
				session = smanager.getSession();
				final IDfSysObject obj = (IDfSysObject) session.getObject(id);
				final ByteArrayInputStream bais = obj.getContent();
				final String contentType = obj.getContentType();
				final int length = bais.available();
				final byte[] buff = new byte[length];
				final int bytesread = bais.read(buff);
				if (bytesread >= 0) {
					final String joo = new String(buff);
					final TextViewer viewer = new TextViewer(true);
					SwingHelper.centerJFrame(viewer);
					viewer.setText(joo);
					viewer.setObjid(objid);
					viewer.setContentType(contentType);
					viewer.setVisible(true);
				}
			} catch (final DfException ex) {
				DfLogger.error(ex.getMessage(), null, null, ex);
				SwingHelper.showErrorMessage("Error occurred!", ex.getMessage());
			} catch (final IOException ex) {
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
		// NN

	}

}
