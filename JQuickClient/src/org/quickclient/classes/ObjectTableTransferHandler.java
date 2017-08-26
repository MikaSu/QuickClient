/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quickclient.classes;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.TransferHandler;
import javax.swing.table.DefaultTableModel;

import com.documentum.com.DfClientX;
import com.documentum.com.IDfClientX;
import com.documentum.fc.client.IDfRelation;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfVirtualDocument;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLogger;
import com.documentum.fc.common.IDfId;
import com.documentum.operations.IDfCopyNode;
import com.documentum.operations.IDfCopyOperation;

/**
 * 
 * @author Administrator
 */
public class ObjectTableTransferHandler extends TransferHandler {

	private int[] indices = null;
	private int addIndex = -1; // Location where items were added
	private JTable jtable;
	private int addCount = 0; // Number of items added.
	private DocuSessionManager smanager = DocuSessionManager.getInstance();
	private JPopupMenu popup;
	private String targetid;
	private String sourceid;
	JMenuItem linkMenu = null;
	JMenuItem moveMenu = null;
	private JMenuItem copyMenu;

	public void setJMenu(JPopupMenu testPopUp) {
		this.popup = testPopUp;
		linkMenu = new JMenuItem("Link");
		moveMenu = new JMenuItem("Move");
		copyMenu = new JMenuItem("Copy");
		JSeparator sep = new JSeparator();
		JSeparator sep2 = new JSeparator();
		JMenuItem relateMenu = new JMenuItem("Relate");
		JMenuItem cancelMenu = new JMenuItem("Cancel");

		popup.add(linkMenu);
		popup.add(moveMenu);
		popup.add(copyMenu);
		popup.add(sep);
		popup.add(relateMenu);
		popup.add(sep2);
		popup.add(cancelMenu);

		relateMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				IDfSession session = null;
				try {
					session = smanager.getSession();
					IDfSysObject sourceobj = (IDfSysObject) session.getObject(new DfId(sourceid));
					String desc = JOptionPane.showInputDialog("Please enter description for relation");
					IDfRelation newrelation = sourceobj.addChildRelative("peer", new DfId(targetid), "peer relation", true, desc);
					if (newrelation != null)
						DfLogger.info(this, "new relation " + newrelation.getObjectId().getId(), null, null);
				} catch (DfException ex) {
					DfLogger.error(this, ex.getMessage(), null, ex);
					JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occurred.", JOptionPane.ERROR_MESSAGE);
				} finally {
					if (session != null) {
						smanager.releaseSession(session);
					}
				}
			}

		});

		linkMenu.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				IDfSession session = null;
				try {
					session = smanager.getSession();
					IDfSysObject sourceobj = (IDfSysObject) session.getObject(new DfId(sourceid));
					sourceobj.link(targetid);
					if (sourceobj.isCheckedOut()) {
						sourceobj.saveLock();
					} else {
						sourceobj.save();
					}
					DfLogger.info(this, "link " + sourceid + " " + targetid, null, null);
				} catch (DfException ex) {
					DfLogger.error(this, ex.getMessage(), null, ex);
				} finally {
					if (session != null) {
						smanager.releaseSession(session);
					}
				}
			}
		});

		moveMenu.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				IDfSession session = null;
				try {
					session = smanager.getSession();
					IDfSysObject sourceobj = (IDfSysObject) session.getObject(new DfId(sourceid));
					Vector idlist = new Vector();
					int fcount = sourceobj.getFolderIdCount();
					for (int i = 0; i < fcount; i++) {
						IDfId id = sourceobj.getFolderId(i);
						idlist.add(id.toString());
					}
					for (int j = 0; j < idlist.size(); j++) {
						String iidee = (String) idlist.get(j);
						sourceobj.unlink(iidee);
					}
					sourceobj.link(targetid);

					if (sourceobj.isCheckedOut()) {
						sourceobj.saveLock();
					} else {
						sourceobj.save();
					}
					DfLogger.info(this, "move " + sourceid + " " + targetid, null, null);
				} catch (DfException ex) {
					DfLogger.error(this, ex.getMessage(), null, ex);
				} finally {
					if (session != null) {
						smanager.releaseSession(session);
					}
				}
			}
		});

		copyMenu.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// System.out.println("sourceid: " + sourceid);
				// System.out.println("targetid: " + targetid);
				IDfSession session = null;
				try {

					session = smanager.getSession();
					IDfSysObject sourceobj = (IDfSysObject) session.getObject(new DfId(sourceid));

					IDfClientX clientx = new DfClientX();
					IDfCopyOperation operation = clientx.getCopyOperation();
					operation.setDeepFolders(true);
					operation.setDestinationFolderId(new DfId(targetid));
					// add the appropriate object to the operation
					if (sourceobj.isVirtualDocument()) {
						IDfVirtualDocument vDoc = sourceobj.asVirtualDocument("CURRENT", false);
						IDfCopyNode node = (IDfCopyNode) operation.add(vDoc);
					} else {
						IDfCopyNode node = (IDfCopyNode) operation.add(sourceobj);
					}

					// see the Operation- Execute and Check Errors sample code
					operation.execute();
					
					DfLogger.info(this, "copy " + sourceid + " " + targetid + " " + operation.getNewObjects().toString(), null, null);
				} catch (DfException ex) {
					DfLogger.error(this, ex.getMessage(), null, ex);
				} finally {
					if (session != null) {
						smanager.releaseSession(session);
					}
				}
			}
		});

		cancelMenu.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// System.out.println("skip.");
			}
		});

	}

	@Override
	protected Transferable createTransferable(JComponent c) {
		JTable table = (JTable) c;
		StringBuffer buff = new StringBuffer();
		indices = table.getSelectedRows();
		int[] row = table.getSelectedRows();
		Vector idVector = new Vector();
		for (int i = 0; i < row.length; i++) {
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			Vector v = (Vector) model.getDataVector().elementAt(row[i]);
			DokuData d = (DokuData) v.lastElement();
			String objid = d.getObjID();
			buff.append(objid);
		}

		return new StringSelection(buff.toString());
	}

	/**
	 * We support both copy and move actions.
	 */
	@Override
	public int getSourceActions(JComponent c) {
		return TransferHandler.COPY_OR_MOVE;
	}

	@Override
	public boolean canImport(TransferHandler.TransferSupport info) {

		if (!info.isDrop()) {
			linkMenu.setEnabled(false);
			moveMenu.setEnabled(false);
			copyMenu.setEnabled(false);
		} else {
			linkMenu.setEnabled(true);
			moveMenu.setEnabled(true);
			copyMenu.setEnabled(true);
		}

		info.setShowDropLocation(true);

		if (!info.isDataFlavorSupported(DataFlavor.stringFlavor)) {
			return false;
		}

		// fetch the drop location
		JTable.DropLocation dl = (JTable.DropLocation) info.getDropLocation();

		int row = dl.getRow();

		DefaultTableModel model = (DefaultTableModel) jtable.getModel();
		Vector v = (Vector) model.getDataVector().elementAt(row);
		DokuData d = (DokuData) v.lastElement();
		String id = d.getObjID();

		if (id.startsWith("0b") || id.startsWith("0c")) {
			linkMenu.setEnabled(true);
			moveMenu.setEnabled(true);
			copyMenu.setEnabled(true);
		} else {
			linkMenu.setEnabled(false);
			moveMenu.setEnabled(false);
			copyMenu.setEnabled(false);
		}
		return true;
		// return true;
	}

	@Override
	public boolean importData(TransferHandler.TransferSupport info) {
		// if we can't handle the import, say so
		if (!canImport(info)) {
			return false;
		}

		// fetch the drop location
		JTable.DropLocation dl = (JTable.DropLocation) info.getDropLocation();
		int row = dl.getRow();

		// fetch the data and bail if this fails
		// String sourceid = "";

		try {
			sourceid = (String) info.getTransferable().getTransferData(DataFlavor.stringFlavor);
			DefaultTableModel model = (DefaultTableModel) jtable.getModel();
			Vector v = (Vector) model.getDataVector().elementAt(row);
			DokuData d = (DokuData) v.lastElement();
			targetid = d.getObjID();
			popup.show(jtable, jtable.getMousePosition().x, jtable.getMousePosition().y);

		} catch (UnsupportedFlavorException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	/**
	 * We only support importing strings.
	 */
	/**
	 * Remove the items moved from the list.
	 */
	@Override
	protected void exportDone(JComponent c, Transferable data, int action) {
		/*
		 * JTable source = (JTable) c; DefaultTableModel model =
		 * (DefaultTableModel) source.getModel();
		 * 
		 * if (action == TransferHandler.MOVE) { for (int i = indices.length -
		 * 1; i >= 0; i--) { model.removeRow(i); } }
		 * 
		 * indices = null; addCount = 0; addIndex = -1;
		 */
	}

	public void setJtable(JTable jtable) {
		this.jtable = jtable;
	}
}
