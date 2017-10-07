package org.quickclient.actions.tools;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JTable;

import org.quickclient.actions.IQuickAction;
import org.quickclient.actions.QCActionException;
import org.quickclient.classes.ConfigService;
import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.SwingHelper;

import com.documentum.fc.client.IDfFormat;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLogger;
import com.documentum.fc.common.IDfId;

public class CompareLocal implements IQuickAction {

	private List<String> idlist;

	private void compareFiles(final String filePath, final File selFile) {
		try {
			final String docbasemd5 = org.quickclient.classes.Utils.md5sum(new File(filePath));
			final String localmd5 = org.quickclient.classes.Utils.md5sum(selFile);
			if (docbasemd5.equals(localmd5)) {
				SwingHelper.showInfoMessage("Files are identical", "md5: " + docbasemd5);
			} else {
				SwingHelper.showInfoMessage("Files differ", " repository md5: " + docbasemd5 + "\n local: " + localmd5);
			}
		} catch (final IOException ex) {
			DfLogger.error(this, ex.getMessage(), null, ex);
		}

	}

	@Override
	public void execute() throws QCActionException {
		final DocuSessionManager smanager = DocuSessionManager.getInstance();
		IDfSession session = null;
		try {
			session = smanager.getSession();
			for (int i = 0; i < idlist.size(); i++) {
				final String objid = idlist.get(i);
				final IDfId id = new DfId(objid);
				final IDfSysObject obj = (IDfSysObject) session.getObject(id);
				final IDfId contentid = obj.getContentsId();
				File selFile;
				final JFileChooser fc = new JFileChooser();
				fc.showOpenDialog(null);
				final IDfFormat f = obj.getFormat();
				selFile = fc.getSelectedFile();
				if (selFile != null) {
					String viewpath = ConfigService.getInstance().getParameter("viewpath");
					final String psep = System.getProperty("file.separator");
					if (viewpath.length() < 2) {
						viewpath = System.getProperty("java.io.tmpdir");
					}
					if (!viewpath.endsWith(psep)) {
						viewpath = viewpath + psep;
					}
					String filePath = null;
					if (f == null) {
						filePath = obj.getFile(viewpath + contentid);
					} else {
						filePath = obj.getFile(viewpath + contentid + "." + f.getDOSExtension());
					}

					compareFiles(filePath, selFile);

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
		// A
	}

}
