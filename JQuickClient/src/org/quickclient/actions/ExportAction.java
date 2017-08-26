package org.quickclient.actions;

import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JTable;

import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.ExportListener;
import org.quickclient.classes.FileExport;
import org.quickclient.classes.SwingHelper;
import org.quickclient.gui.ExportData;
import org.quickclient.gui.ExportDialog;

import com.documentum.fc.client.IDfFormat;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLogger;
import com.documentum.fc.common.IDfId;

public class ExportAction implements IQuickAction {

	private List<String> idlist;

	@Override
	public void execute() throws QCActionException {
		String exportName = "";
		File selFile;
		IDfSession session = null;
		final DocuSessionManager smanager = DocuSessionManager.getInstance();
		if (idlist.size() == 1) {
			try {
				final String objid = idlist.get(0);

				final IDfId id = new DfId(objid);
				if (id.getTypePart() == IDfId.DM_FOLDER || id.getTypePart() == IDfId.DM_CABINET) {
					final ExportData x = new ExportData();
					final ExportListener listener = new ExportListener();
					listener.setExportData(x);
					listener.setObjid(objid);
					final ExportDialog frame = new ExportDialog(listener, x);
					SwingHelper.centerJFrame(frame);
					frame.setVisible(true);
				} else if (id.getTypePart() == IDfId.DM_DOCUMENT) {
					session = smanager.getSession();
					final IDfSysObject obj = (IDfSysObject) session.getObject(id);
					exportName = obj.getObjectName();
					final IDfFormat format = obj.getFormat();
					final String strFormat = format.getDOSExtension();
					// //System.out.println(strFormat);
					if (!exportName.endsWith("." + strFormat)) {
						exportName = exportName + "." + strFormat;
					}

					final JFileChooser chooser = new JFileChooser();

					// chooser.setDialogTitle("Documentum Export");
					// chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
					chooser.setSelectedFile(new File(exportName));
					final int returnVal = chooser.showSaveDialog(null);

					if (returnVal == JFileChooser.APPROVE_OPTION) {
						selFile = chooser.getSelectedFile();
						final FileExport fex = new FileExport(selFile, obj);
						fex.start();
					}
					/*
					 * obj.getFile(selFile.toString());
					 */
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
	}

	@Override
	public void setIdList(final List<String> idlist) {
		this.idlist = idlist;

	}

	@Override
	public void setTable(final JTable t) {
		// not needed on this action
	}
}
