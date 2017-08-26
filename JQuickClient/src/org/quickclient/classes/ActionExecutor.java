/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quickclient.classes;

import java.awt.Cursor;
import java.awt.Desktop;
import java.io.File;
import java.util.Vector;

import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;
import org.quickclient.gui.SearchFrame;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLogger;


/**
 * 
 * @author Administrator
 */
public class ActionExecutor {

	DocuSessionManager smanager;
	Logger log = Logger.getLogger(ActionExecutor.class);
	Desktop desktop = Desktop.getDesktop();
	private JDesktopPane desktopPane;

	public ActionExecutor() {
		smanager = DocuSessionManager.getInstance();
	}

	public void SetFile(String objid) {

	}

	public void addDigitalSignatureAction(String objid) {

	}

	public void addElectronicSignatureAction(String objid) {

	}

	public void attachLCActionAction(String objid) {

	}

	public void attributeEditorAction(String objid) {
;
	}

	public void cancelCheckOutAction(String objid, JTable table) {

	}

	public void changeACLAction(Vector objids) {

	}

	public void changeObjectTypeAction(String objid) {
		
	
	}

	public void changeOwnerAction(String objid) {
		
	}

	public void checkInAction(String objid, JTable table) {

	}

	public void checkOutAction(String objid, JTable tbl) {

	}

	public void converttononvdm(String objid) {

	}

	public void converttovdm(String objid) {

	}

	public void demoteAction(String objid) {

	}

	public boolean destroyAction(Vector objids) {
		return false;
	}

	public void editAction(String objid, JTable tbl) {

		// FileEdit fe = new FileEdit(objid);
		// fe.start();
	}

	public void editvdm(String objid) {

	}

	public void exportFileAction(String objid) {

	}

	public void govern(Vector objids) {
		if (objids.size() > 0) {
			IDfSession session = null;
			try {
				for (int i = 0; i < objids.size(); i++) {
					session = smanager.getSession();
					IDfSysObject obj = (IDfSysObject) session.getObject(new DfId((String) objids.get(i)));
					String folderid = obj.getRepeatingString("i_folder_id", 0);

				}
			} catch (DfException ex) {
				log.error(ex);
				SwingHelper.showErrorMessage("Error occurred!", ex.getMessage());

			}
		}
	}

	public void importRendtionAction(String objid) {
		
	
	}

	public void openRelated(String objid) {
		IDfSession session = null;
		IDfCollection col = null;
		IDfCollection col2 = null;
		DefaultTableModel model = null;
		Vector listattributes;
		ConfigService cs = ConfigService.getInstance();
		listattributes = cs.getAttributes(cs.getCurrentListConfigName()).get();
		model = new DefaultTableModelCreator().createModel();
		try {
			String standardqueryattributes = "object_name, r_object_id, r_link_cnt, r_object_type, a_content_type, i_is_replica, r_lock_owner, r_is_virtual_doc, i_is_reference";

			session = smanager.getSession();
			String queryStr = "select child_id as relationid from dm_relation where parent_id = '" + objid + "' union select " + "parent_id as relationid from dm_relation where child_id= '" + objid + "'";

			IDfQuery query = new DfQuery();
			log.info(queryStr);
			query.setDQL(queryStr);
			// System.out.println(queryStr);
			Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
			
			col = query.execute(session, DfQuery.DF_READ_QUERY);
			StringBuffer inlist = new StringBuffer();
			while (col.next()) {
				inlist.append("'");
				inlist.append(col.getString("relationid"));
				inlist.append("'");
				inlist.append(",");
			}
			inlist.delete(inlist.lastIndexOf(","), inlist.lastIndexOf(",") + 1);
			DfLogger.debug(this, inlist.toString(), null, null);
			col.close();
			query.setDQL("select " + standardqueryattributes + " from dm_sysobject where r_object_id in (" + inlist.toString() + ")");
			col2 = query.execute(session, DfQuery.QUERY);
			Utils u = new Utils();
			model = u.getModelFromCollection(session, null, col2, false, model, null, null);
		} catch (DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);

		} finally {
			Cursor cur = new Cursor(Cursor.DEFAULT_CURSOR);

			if (col2 != null) {
				try {
					col2.close();
				} catch (DfException ex) {
					ex.printStackTrace();
					log.error(ex);
				}
			}
			if (session != null) {
				smanager.releaseSession(session);
			}
		}
		if (model.getRowCount() > 0) {
			SearchFrame sf = new SearchFrame(model, false);
			cs.getDesktop().add(sf);
			sf.setSize(900, 400);
			sf.setTitle("Search Results...");
			sf.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(null, "No relations found for this document", "Info", JOptionPane.INFORMATION_MESSAGE);
		}

	}

	public void promoteAction(String objid) {

	}

	public void queueToUser(final String objid) {

	}

	public void startWorkflow(String objid) {

	}

	public void transform(String objid) {

	}

	public void unsubscribe(String objid) {

	}

	public void subscribe(String objid) {

	}

	public void removeLCAction(String objid) {

	}

	public void renameAction(String objid) {
	}

	public void renderAsPDFAction(String objid) {

	}

	public void renderHTMLAction(String objid) {

	}

	public void resumeLCAction(String objid) {

	}

	public void runGroovy(String objid, String scriptid) {
		try {
			startScript(objid, scriptid);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Exception in groovy script connection: " + e.getMessage(), "Groovy-error", JOptionPane.WARNING_MESSAGE);
		}
	}

	public void runGroovy(Vector objids, File file) {
		for (int i = 0; i < objids.size(); i++) {
			String objid = (String) objids.get(i);
			try {
				startScript(objid, file);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Exception in groovy script connection: " + e.getMessage(), "Groovy-error", JOptionPane.WARNING_MESSAGE);
			}
		}
		/*
		 * try { startScript(objid, file); } catch (Exception e) {
		 * JOptionPane.showMessageDialog(null,
		 * "Exception in groovy script connection: " + e.getMessage(),
		 * "Groovy-error", JOptionPane.WARNING_MESSAGE); }
		 */
	}

	public void viewTextAction(Vector objids) {

	}

	private void startScript(String objid, String scriptid) {
		IDfSession session = null;
		try {
			session = smanager.getSession();
			IDfSysObject obj = (IDfSysObject) session.getObject(new DfId(scriptid));
			String fileName = obj.getFile(null);
			File file = new File(fileName);
			ScriptConnector connector = new ScriptConnector(objid, file.getPath()); // instanciate
																					// the
																					// connector
																					// ...

			connector.runGroovyScript(file.getName()); // ... and run the plugin
														// script

		} catch (DfException ex) {
			log.error(ex);
			SwingHelper.showErrorMessage("Error occurred!", ex.getMessage());
		} finally {
			if (session != null) {
				smanager.releaseSession(session);
			}

		}
	}

	private void startScript(String objid, File scriptfile) {
		ScriptConnector connector = new ScriptConnector(objid, scriptfile.getPath()); // instanciate
																						// the
																						// connector
																						// ...

		connector.runGroovyScript(scriptfile.getName()); // ... and run the
															// plugin script

	}

	public void showDumpWindowAction(String objid) {


	}

	public void showFileSystemPathAction(String objid) {
		
	}

	public void suspendLCAction(String objid) {

	}

	public void unlockObjAction(String objid) {
	
	}

	public void verifyAuditAction(String objid) {

	}

	public void verifySignatureAction(String objid) {

	}

	public void viewAction(Vector objids) {

	}

	public JDesktopPane getDesktopPane() {
		return desktopPane;
	}

	public void setDesktopPane(JDesktopPane desktopPane) {
		this.desktopPane = desktopPane;
	}

	public void quickflow(Vector<String> objids) {

	}

	public void resetrenditions(Vector<String> objids) {

	}

	public void giveDeletePermitForUser(Vector<String> objids) {
	}
}
