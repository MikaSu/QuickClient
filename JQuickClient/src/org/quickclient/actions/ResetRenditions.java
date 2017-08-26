package org.quickclient.actions;

import java.util.List;

import javax.swing.JTable;

import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.SwingHelper;

import com.documentum.fc.client.DfClient;
import com.documentum.fc.client.IDfClient;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfLogger;
import com.documentum.services.dam.df.transform.ICTSService;

public class ResetRenditions implements IQuickAction {

	private List<String> idlist;

	@Override
	public void execute() throws QCActionException {
		final DocuSessionManager smanager = DocuSessionManager.getInstance();
		for (int i = 0; i < idlist.size(); i++) {
			final String objid = idlist.get(i);
			IDfSession session = null;
			try {
				session = smanager.getSession();

				ICTSService ctsService = null;
				final IDfClient client = DfClient.getLocalClient();
				ctsService = (ICTSService) client.newService(ICTSService.class.getName(), smanager.getSMgr());
				ctsService.resetRenditions(session, objid);
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
		// not needed on this function
	}

}
