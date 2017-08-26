package org.quickclient.actions;

import java.util.List;

import javax.swing.JTable;

import org.quickclient.classes.ConfigService;
import org.quickclient.classes.DocuSessionManager;
import org.quickclient.gui.VDMFrameInternal;

import com.documentum.fc.client.IDfSession;

public class VDEdit implements IQuickAction {

	private List<String> idlist;

	@Override
	public void execute() throws QCActionException {
		final DocuSessionManager smanager = DocuSessionManager.getInstance();
		IDfSession session = null;
		try {
			session = smanager.getSession();
			for (int i = 0; i < idlist.size(); i++) {
				final String objid = idlist.get(i);
				final VDMFrameInternal vdmfram = new VDMFrameInternal(objid);
				ConfigService.getInstance().getDesktop().add(vdmfram);
				vdmfram.setResizable(true);
				vdmfram.setVisible(true);
			}
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
		// not needed on this function
	}

}
