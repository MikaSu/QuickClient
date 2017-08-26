package org.quickclient.classes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import org.quickclient.gui.ExportData;

import com.documentum.com.DfClientX;
import com.documentum.com.IDfClientX;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLogger;
import com.documentum.fc.common.IDfId;
import com.documentum.operations.IDfExportNode;
import com.documentum.operations.IDfExportOperation;
import com.documentum.operations.IDfFile;


public class ExportListener implements ActionListener {

	private String objid = "";
	private DocuSessionManager smanager = DocuSessionManager.getInstance();
	private ExportData exportInfo = null;
	
	
	
	public void setObjid(String objid) {
		this.objid = objid;
	}



	public void setExportData(ExportData x) {
		this.exportInfo = x;
	}



	@Override
	public void actionPerformed(ActionEvent arg0) {
		IDfCollection col = null; 
		try {
			IDfId id = new DfId(objid);
			IDfSession session = smanager.getSession();
			IDfClientX clientx = new DfClientX();
			IDfExportOperation operation = clientx.getExportOperation();
			operation.setDestinationDirectory(exportInfo.getExportDir()); // TODO
																	// dialogi
			operation.disableRegistryUpdates(true);

			String qualification;

			qualification = "select 1 as isfolder, object_name, r_object_id from dm_folder where folder(ID('" + objid + "')) union select 0 as isfolder, object_name, r_object_id from dm_document where folder(ID('" + objid + "'))";

			IDfQuery q = clientx.getQuery(); // Create query object
			q.setDQL(qualification); // Give it the query
			col = q.execute(session, IDfQuery.DF_READ_QUERY); // Execute
																// synchronously

			while (col.next()) {
				int isfolder = col.getInt("isfolder");
				String ids = col.getString("r_object_id");
				String name = col.getString("object_name");

				if (isfolder == 0) {
					IDfFile destFile = clientx.getFile(exportInfo.getExportDir() + System.getProperty("file.separator") + name);
					if (!destFile.exists()) {
						IDfId idObj = clientx.getId(ids);
						IDfSysObject myObj = (IDfSysObject) session.getObject(idObj);
						IDfExportNode node = (IDfExportNode) operation.add(myObj);
					}

				} else {
					if (exportInfo.isDeepexport()) {
						// System.out.println(x.getExportDir());
						// System.out.println(System.getProperty("file.separator"));
						// System.out.println(name);
						String folderpath = exportInfo.getExportDir() + System.getProperty("file.separator") + name;
						// System.out.println(folderpath);
						expFolder(ids, exportInfo.isExportrenditionds(), folderpath);
					}

				}
			}
			operation.execute();

		} catch (DfException ee) {
			ee.printStackTrace();
		} finally {
			if (col != null) {
				try {
					col.close();
				} catch (DfException ex) {
					DfLogger.error(this, ex.getMessage(), null, ex);

				}

			}
		}
	}

	
	
	public void expFolder(String folderid, boolean exprenditions, String folderPath) {
		IDfCollection col = null; // create a collection for the images
									// to be exported
		try {
			File exportdir = new File(folderPath);
			if (exportdir.exists() == false) {
				exportdir.mkdirs();
			}

			IDfId id = new DfId(folderid);
			IDfSession session = smanager.getSession();
			IDfClientX clientx = new DfClientX();
			IDfExportOperation operation = clientx.getExportOperation();
			operation.setDestinationDirectory(folderPath);
			operation.disableRegistryUpdates(true);

			String qualification;

			qualification = "select 1 as isfolder, object_name, r_object_id from dm_folder where folder(ID('" + folderid + "')) union select 0 as isfolder, object_name, r_object_id from dm_document where folder(ID('" + folderid + "'))";

			IDfQuery q = clientx.getQuery(); // Create query object
			q.setDQL(qualification); // Give it the query
			col = q.execute(session, IDfQuery.DF_READ_QUERY); // Execute
																// synchronously

			while (col.next()) {
				int isfolder = col.getInt("isfolder");
				String ids = col.getString("r_object_id");
				String name = col.getString("object_name");

				if (isfolder == 0) {
					IDfFile destFile = clientx.getFile(folderPath + System.getProperty("file.separator") + name);
					if (!destFile.exists()) {
						IDfId idObj = clientx.getId(ids);
						IDfSysObject myObj = (IDfSysObject) session.getObject(idObj);
						IDfExportNode node = (IDfExportNode) operation.add(myObj);
					}

				} else {
					String folderpath = folderPath + System.getProperty("file.separator") + name;
					expFolder(ids, exportInfo.isExportrenditionds(), folderpath);
				}

			}
			operation.execute();

		} catch (DfException ee) {
			ee.printStackTrace();
		} finally {
			if (col != null) {
				try {
					col.close();
				} catch (DfException ex) {
					DfLogger.error(this, ex.getMessage(), null, ex);
				}

			}
		}

	}
}
