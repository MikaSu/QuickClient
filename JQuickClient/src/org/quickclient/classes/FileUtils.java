package org.quickclient.classes;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import com.documentum.fc.client.IDfFormat;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLogger;
import com.documentum.fc.common.IDfId;

public class FileUtils {

	private static void openfile(final File fileToOpen) {
		try {
			Desktop.getDesktop().open(fileToOpen);
		} catch (final IOException ex) {
			DfLogger.error(FileUtils.class, "", null, ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
		}

	}

	public static void viewFile(final String objid) {
		IDfSession session = null;
		final DocuSessionManager smanager = DocuSessionManager.getInstance();
		try {

			final IDfId id = new DfId(objid);
			session = smanager.getSession();
			final IDfSysObject obj = (IDfSysObject) session.getObject(id);
			final IDfId contentid = obj.getContentsId();
			final IDfFormat f = obj.getFormat();
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
			DfLogger.info(FileUtils.class, "open file: " + filePath, null, null);
			final File fileToOpen = new File(filePath);
			openfile(fileToOpen);

		} catch (final DfException ex) {
			DfLogger.error(FileUtils.class, "", null, ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
		} finally {
			if (session != null) {
				smanager.releaseSession(session);
			}
		}
	}

}
