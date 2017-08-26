package org.quickclient.actions.tools;

import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JTable;

import org.quickclient.actions.IQuickAction;
import org.quickclient.actions.QCActionException;
import org.quickclient.classes.DocuSessionManager;

import com.documentum.fc.client.IDfFolder;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;

import fi.fortica.dctm.DocumentExporter;

public class ExportFolder implements IQuickAction {

	private List<String> idlist;

	@Override
	public void setIdList(List<String> idlist) {
		this.idlist = idlist;

	}

	@Override
	public void execute() throws QCActionException {
		DocuSessionManager smanager = DocuSessionManager.getInstance();
		IDfSession session = null;
		File selFile = null;
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Target folder for Export..");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			selFile = chooser.getSelectedFile();
		}
		try {
			session = smanager.getSession();
			for (int i = 0; i < idlist.size(); i++) {
				String id = idlist.get(i);
				if (id.startsWith("0b") || id.startsWith("0c")) {
					DocumentExporter de = new DocumentExporter(session);
					de.setTargetFSFolder(selFile.getAbsolutePath());
					IDfFolder f = (IDfFolder) session.getObject(new DfId(id));
					de.dumpDocument(f);
				} 
			}
		} catch (DfException e) {
			e.printStackTrace();
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
