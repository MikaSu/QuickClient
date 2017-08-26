/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quickclient.gui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.TreePath;

import org.quickclient.classes.DocuSessionManager;

import com.documentum.com.DfClientX;
import com.documentum.com.IDfClientX;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfVirtualDocument;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.IDfId;
import com.documentum.operations.IDfCopyNode;
import com.documentum.operations.IDfCopyOperation;

/**
 *
 * @author Administrator
 */
class TreeTransferHandler extends TransferHandler {

	private JTree tree;
	private JPopupMenu popup;
	private final DocuSessionManager smanager = DocuSessionManager.getInstance();
	private String sourceid;
	private String targetid;

	@Override
	public boolean canImport(final TransferHandler.TransferSupport info) {
		// for the demo, we'll only support drops (not clipboard paste)
		if (!info.isDrop()) {
			return false;
		}

		info.setShowDropLocation(true);

		// we only import Strings
		if (!info.isDataFlavorSupported(DataFlavor.stringFlavor)) {
			return false;
		}

		// fetch the drop location
		final JTree.DropLocation dl = (JTree.DropLocation) info.getDropLocation();

		final TreePath path = dl.getPath();

		// we don't support invalid paths or descendants of the names folder
		if (path == null) {
			return false;
		}
		return true;

	}

	@Override
	protected Transferable createTransferable(final JComponent c) {
		final JTree table = (JTree) c;
		final StringBuffer buff = new StringBuffer();
		buff.append("EEEEE");
		return new StringSelection(buff.toString());
	}

	/**
	 * We only support importing strings.
	 */
	/**
	 * Remove the items moved from the list.
	 */
	@Override
	protected void exportDone(final JComponent c, final Transferable data, final int action) {
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

	/**
	 * We support both copy and move actions.
	 */
	@Override
	public int getSourceActions(final JComponent c) {
		return TransferHandler.COPY_OR_MOVE;
	}

	@Override
	public boolean importData(final TransferHandler.TransferSupport info) {
		// if we can't handle the import, say so
		if (!canImport(info)) {
			return false;
		}

		// fetch the drop location
		final JTree.DropLocation dl = (JTree.DropLocation) info.getDropLocation();

		// fetch the data and bail if this fails
		// String sourceid = "";
		/*
		 * try { sourceid = (String)
		 * info.getTransferable().getTransferData(DataFlavor.stringFlavor);
		 * DefaultTableModel model = (DefaultTableModel) jtable.getModel();
		 * Vector v = (Vector) model.getDataVector().elementAt(row); DokuData d
		 * = (DokuData) v.lastElement(); targetid = d.getObjID();
		 * popup.show(jtable, jtable.getMousePosition().x,
		 * jtable.getMousePosition().y);
		 * 
		 * } catch (UnsupportedFlavorException e) { return false; } catch
		 * (IOException e) { return false; }
		 */
		return true;
	}

	void setJTree(final JTree folderTree) {
		this.tree = folderTree;
	}

	void setMenu(final JPopupMenu jPopupMenu) {
		this.popup = jPopupMenu;
		final JMenuItem linkMenu = new JMenuItem("Link");
		final JMenuItem moveMenu = new JMenuItem("Move");
		final JMenuItem copyMenu = new JMenuItem("Copy");
		final JSeparator sep = new JSeparator();
		final JMenuItem cancelMenu = new JMenuItem("Cancel");

		popup.add(linkMenu);
		popup.add(moveMenu);
		// TODO copy olis kiva jos osaa.
		popup.add(copyMenu);
		popup.add(sep);
		popup.add(cancelMenu);

		linkMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				IDfSession session = null;
				try {
					session = smanager.getSession();
					final IDfSysObject sourceobj = (IDfSysObject) session.getObject(new DfId(sourceid));
					sourceobj.link(targetid);
					if (sourceobj.isCheckedOut()) {
						sourceobj.saveLock();
					} else {
						sourceobj.save();
					}
				} catch (final DfException ex) {
				} finally {
					if (session != null) {
						smanager.releaseSession(session);
					}
				}
			}
		});

		moveMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				// System.out.println("sourceid: " + sourceid);
				// System.out.println("targetid: " + targetid);
				IDfSession session = null;
				try {
					session = smanager.getSession();
					final IDfSysObject sourceobj = (IDfSysObject) session.getObject(new DfId(sourceid));
					final Vector idlist = new Vector();
					final int fcount = sourceobj.getFolderIdCount();
					for (int i = 0; i < fcount; i++) {
						final IDfId id = sourceobj.getFolderId(i);
						idlist.add(id.toString());
					}
					for (int j = 0; j < idlist.size(); j++) {
						final String iidee = (String) idlist.get(j);
						sourceobj.unlink(iidee);
					}
					sourceobj.link(targetid);

					if (sourceobj.isCheckedOut()) {
						sourceobj.saveLock();
					} else {
						sourceobj.save();
					}
				} catch (final DfException ex) {
				} finally {
					if (session != null) {
						smanager.releaseSession(session);
					}
				}
			}
		});

		copyMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				// System.out.println("sourceid: " + sourceid);
				// System.out.println("targetid: " + targetid);
				IDfSession session = null;
				try {
					session = smanager.getSession();
					session = smanager.getSession();
					final IDfSysObject sourceobj = (IDfSysObject) session.getObject(new DfId(sourceid));

					final IDfClientX clientx = new DfClientX();
					final IDfCopyOperation operation = clientx.getCopyOperation();
					operation.setDeepFolders(true);
					operation.setDestinationFolderId(new DfId(targetid));
					// add the appropriate object to the operation
					if (sourceobj.isVirtualDocument()) {
						final IDfVirtualDocument vDoc = sourceobj.asVirtualDocument("CURRENT", false);
						final IDfCopyNode node = (IDfCopyNode) operation.add(vDoc);
					} else {
						final IDfCopyNode node = (IDfCopyNode) operation.add(sourceobj);
					}

					// see the Operation- Execute and Check Errors sample code
					operation.execute();
				} catch (final DfException ex) {
				} finally {
					if (session != null) {
						smanager.releaseSession(session);
					}
				}
			}
		});

		cancelMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				// System.out.println("skip.");
			}
		});

	}
}
