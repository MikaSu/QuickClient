/*
 * VDMFrameInternal.java
 *
 * Created on 9. toukokuuta 2008, 17:16
 */
package org.quickclient.gui;

import java.awt.Cursor;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;
import org.quickclient.classes.ActionExecutor;
import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.DokuData;
import org.quickclient.classes.NodeObj;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfVirtualDocument;
import com.documentum.fc.client.IDfVirtualDocumentNode;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.IDfId;

/**
 *
 * @author Administrator
 */
public class VDMFrameInternal extends javax.swing.JInternalFrame {

	private DocuSessionManager smanager;
	private String objid;
	private JDesktopPane desktop;
	DefaultTableModel model = new DefaultTableModel();
	private final TablePopUpMenu myPopUp;
	private final Logger log = Logger.getLogger(VDMFrameInternal.class);

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JTable attributeTable;

	private javax.swing.JPanel jPanel1;

	private javax.swing.JScrollPane jScrollPane1;

	private javax.swing.JScrollPane jScrollPane2;

	private javax.swing.JSplitPane jSplitPane1;

	private javax.swing.JSplitPane jSplitPane2;

	private javax.swing.JMenuItem mnuCheckIn;

	private javax.swing.JMenuItem mnuCheckOu;

	private javax.swing.JMenuItem mnuEdit;

	private javax.swing.JMenuItem mnuRemoveFromVDM;

	private javax.swing.JMenuItem mnuView;

	private javax.swing.JPopupMenu vdmPopUp;

	private javax.swing.JTree vdmTree;

	// End of variables declaration//GEN-END:variables
	/** Creates new form VDMFrameInternal */
	public VDMFrameInternal(final String objid) {
		smanager = DocuSessionManager.getInstance();
		initComponents();
		this.objid = objid;
		vdmTree.setCellRenderer(new TreeFormatRenderer());
		initModel();
		initTree();
		myPopUp = new TablePopUpMenu();
		myPopUp.setTable(attributeTable);
		final int lastIndex = attributeTable.getColumnCount();
		vdmTree.setTransferHandler(new TransferHandler() {

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

				// we don't support invalid paths or descendants of the names
				// folder
				if (path == null) {
					return false;
				}
				return true;
			}

			@Override
			public boolean importData(final TransferHandler.TransferSupport info) {
				// if we can't handle the import, say so
				if (!canImport(info)) {
					return false;
				}

				// fetch the drop location
				final JTree.DropLocation dl = (JTree.DropLocation) info.getDropLocation();

				// fetch the path and child index from the drop location
				final TreePath path = dl.getPath();
				int childIndex = dl.getChildIndex();

				// fetch the data and bail if this fails
				String data;
				IDfSession session = null;
				try {
					session = smanager.getSession();
					data = (String) info.getTransferable().getTransferData(DataFlavor.stringFlavor);

					final QuickClientMutableTreeNode treenode = (QuickClientMutableTreeNode) path.getLastPathComponent();

					final IDfSysObject obj = (IDfSysObject) session.getObject(new DfId(treenode.getDokuDataID()));
					obj.checkout();
					final IDfSysObject childobj = (IDfSysObject) session.getObject(new DfId(data));
					final IDfVirtualDocument vdm = obj.asVirtualDocument("CURRENT", false);
					final IDfVirtualDocumentNode rootNode = vdm.getRootNode();
					final boolean followAssembly = false;
					final boolean overrideLateBindingValue = false;
					final IDfVirtualDocumentNode nodeChild1 = vdm.addNode(rootNode, null, childobj.getChronicleId(), "CURRENT", followAssembly, overrideLateBindingValue);
					obj.save();
				} catch (final DfException ex) {
					log.error(ex);
					JOptionPane.showMessageDialog(null, ex.getMessage(), "Error Occured!!", JOptionPane.ERROR_MESSAGE);
				} catch (final UnsupportedFlavorException e) {
					return false;
				} catch (final IOException e) {
					return false;
				} finally {
					if (session != null) {
						smanager.releaseSession(session);
					}
				}
				initTree();
				// if child index is -1, the drop was on top of the path, so
				// we'll
				// treat it as inserting at the end of that path's list of
				// children
				if (childIndex == -1) {
					childIndex = vdmTree.getModel().getChildCount(path.getLastPathComponent());
				}
				return true;
			}
		});

	}

	private void attributeTableMouseReleased(final java.awt.event.MouseEvent evt) {// GEN-FIRST:event_attributeTableMouseReleased
		// add your handling code here:

		final int butt = evt.getButton();
		//// System.out.println("butt on: " + butt);
		if (butt == MouseEvent.BUTTON3) {
			myPopUp.show(evt.getComponent(), evt.getX(), evt.getY());
		}

	}// GEN-LAST:event_attributeTableMouseReleased

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		vdmPopUp = new javax.swing.JPopupMenu();
		mnuView = new javax.swing.JMenuItem();
		mnuEdit = new javax.swing.JMenuItem();
		mnuCheckOu = new javax.swing.JMenuItem();
		mnuCheckIn = new javax.swing.JMenuItem();
		mnuRemoveFromVDM = new javax.swing.JMenuItem();
		jSplitPane1 = new javax.swing.JSplitPane();
		jSplitPane2 = new javax.swing.JSplitPane();
		jScrollPane2 = new javax.swing.JScrollPane();
		attributeTable = new javax.swing.JTable();
		jPanel1 = new javax.swing.JPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		vdmTree = new javax.swing.JTree();

		mnuView.setText("View");
		mnuView.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuViewActionPerformed(evt);
			}
		});
		vdmPopUp.add(mnuView);

		mnuEdit.setText("Edit");
		mnuEdit.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuEditActionPerformed(evt);
			}
		});
		vdmPopUp.add(mnuEdit);

		mnuCheckOu.setText("CheckOut");
		mnuCheckOu.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuCheckOuActionPerformed(evt);
			}
		});
		vdmPopUp.add(mnuCheckOu);

		mnuCheckIn.setText("CheckIn");
		mnuCheckIn.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuCheckInActionPerformed(evt);
			}
		});
		vdmPopUp.add(mnuCheckIn);

		mnuRemoveFromVDM.setText("Remove from Virtual Document");
		mnuRemoveFromVDM.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuRemoveFromVDMActionPerformed(evt);
			}
		});
		vdmPopUp.add(mnuRemoveFromVDM);

		setClosable(true);
		setTitle("VDM Editor");

		jSplitPane1.setDividerLocation(200);

		jSplitPane2.setDividerLocation(201);
		jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

		attributeTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null }, { null, null }, { null, null }, { null, null } }, new String[] { "Name", "Value" }) {
			boolean[] canEdit = new boolean[] { false, false };

			@Override
			public boolean isCellEditable(final int rowIndex, final int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		attributeTable.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseReleased(final java.awt.event.MouseEvent evt) {
				attributeTableMouseReleased(evt);
			}
		});
		jScrollPane2.setViewportView(attributeTable);

		jSplitPane2.setTopComponent(jScrollPane2);

		final javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 545, Short.MAX_VALUE));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 356, Short.MAX_VALUE));

		jSplitPane2.setRightComponent(jPanel1);

		jSplitPane1.setRightComponent(jSplitPane2);

		vdmTree.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(final java.awt.event.MouseEvent evt) {
				vdmTreeMouseClicked(evt);
			}

			@Override
			public void mouseReleased(final java.awt.event.MouseEvent evt) {
				vdmTreeMouseReleased(evt);
			}
		});
		jScrollPane1.setViewportView(vdmTree);

		jSplitPane1.setLeftComponent(jScrollPane1);

		final javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 630, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void initModel() {

		model.setRowCount(0);
		attributeTable.setRowHeight(22);
		attributeTable.setAutoCreateColumnsFromModel(false);
		model.setColumnCount(0);
		model.addColumn("Attribute");
		model.addColumn("Attribute Value");
		model.addColumn("Data");

	}

	private void initTree() {
		final Cursor cur = new Cursor(Cursor.WAIT_CURSOR);

		setCursor(cur);
		IDfSession session = null;
		QuickClientMutableTreeNode root;
		QuickClientMutableTreeNode c;
		DefaultTreeModel treemodel;

		//// System.out.println("model: " + treemodel);
		try {
			session = smanager.getSession();
			final IDfSysObject obj = (IDfSysObject) session.getObject(new DfId(objid));
			final IDfVirtualDocument vdm = obj.asVirtualDocument("CURRENT", false);
			final IDfVirtualDocumentNode rootNode = vdm.getRootNode();
			this.setTitle("VDMEditor editing - '" + obj.getObjectName() + "'");
			root = new QuickClientMutableTreeNode();
			final DokuData dd = new DokuData();
			final String iidee = obj.getString("r_object_id");
			dd.setObjID(iidee);
			root.setDokuData(dd);
			final NodeObj o = new NodeObj();
			o.setFormat(obj.getString("a_content_type"));
			o.setLabel(obj.getObjectName());
			o.setObjId("123123");
			root.setUserObject(o);
			treemodel = new DefaultTreeModel(root);
			final int childCount = rootNode.getChildCount();
			for (int i = 0; i < childCount; i++) {
				final IDfVirtualDocumentNode child = rootNode.getChild(i);
				// System.out.println(child.getChronId().toString());
				final IDfSysObject curChild = (IDfSysObject) session.getObjectByQualification("dm_sysobject where i_chronicle_id = '" + child.getChronId().toString() + "'");
				c = new QuickClientMutableTreeNode();
				final NodeObj o2 = new NodeObj();
				o2.setFormat(curChild.getString("a_content_type"));
				o2.setLabel(curChild.getObjectName());
				o2.setObjId("123123");
				c.setUserObject(o2);
				final DokuData dd2 = new DokuData();
				dd2.setObjID(curChild.getString("r_object_id"));
				c.setDokuData(dd2);
				root.add(c);
			}
			vdmTree.setModel(treemodel);
		} catch (final DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error Occured!!", JOptionPane.ERROR_MESSAGE);
		} finally {
			if (session != null) {
				smanager.releaseSession(session);
			}
			final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
			setCursor(cur2);
		}
		vdmTree.validate();
	}

	private void mnuCheckInActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuCheckInActionPerformed
		final TreePath treepath = vdmTree.getSelectionPath();
		final QuickClientMutableTreeNode node = (QuickClientMutableTreeNode) treepath.getLastPathComponent();
		final String id = node.getDokuData().getObjID();
		final ActionExecutor ae = new ActionExecutor();
		ae.checkInAction(id, null);
	}// GEN-LAST:event_mnuCheckInActionPerformed

	private void mnuCheckOuActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuCheckOuActionPerformed
		final TreePath treepath = vdmTree.getSelectionPath();
		final QuickClientMutableTreeNode node = (QuickClientMutableTreeNode) treepath.getLastPathComponent();
		final String id = node.getDokuData().getObjID();
		final ActionExecutor ae = new ActionExecutor();
		ae.checkOutAction(id, null);
	}// GEN-LAST:event_mnuCheckOuActionPerformed

	private void mnuEditActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuEditActionPerformed
		final TreePath treepath = vdmTree.getSelectionPath();
		final QuickClientMutableTreeNode node = (QuickClientMutableTreeNode) treepath.getLastPathComponent();
		final String id = node.getDokuData().getObjID();
		final ActionExecutor ae = new ActionExecutor();
		ae.editAction(id, null);
	}// GEN-LAST:event_mnuEditActionPerformed

	private void mnuRemoveFromVDMActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuRemoveFromVDMActionPerformed
		IDfSession session = null;
		final Cursor cur = new Cursor(Cursor.WAIT_CURSOR);

		setCursor(cur);
		try {
			session = smanager.getSession();
			String childid;
			String parentid;
			final TreePath path = vdmTree.getSelectionPath();
			if (path == null) {
				return;
			}
			final QuickClientMutableTreeNode selectedNode = (QuickClientMutableTreeNode) path.getLastPathComponent();
			final QuickClientMutableTreeNode parentNode = (QuickClientMutableTreeNode) path.getParentPath().getLastPathComponent();
			childid = selectedNode.getDokuDataID();
			parentid = parentNode.getDokuDataID();
			// System.out.println("parentid:" + parentid);
			// System.out.println("cid:" + childid);

			final IDfSysObject parentobj = (IDfSysObject) session.getObject(new DfId(parentid));
			parentobj.checkout();
			final IDfSysObject childobj = (IDfSysObject) session.getObject(new DfId(childid));
			final IDfVirtualDocument vdm = parentobj.asVirtualDocument("CURRENT", false);
			final IDfVirtualDocumentNode rootNode = vdm.getRootNode();

			final int x = rootNode.getChildCount();
			// System.out.println("count on : " + x);
			IDfId nodeToRemove = null;
			for (int i = 0; i < x; i++) {
				final IDfVirtualDocumentNode xx = rootNode.getChild(i);
				// System.out.println(xx.getId());
				final IDfVirtualDocumentNode childNode = vdm.getNodeFromNodeId(xx.getId());
				// System.out.println(childNode);
				final IDfSysObject dfSysObject = childNode.getSelectedObject();
				// System.out.println(dfSysObject.getObjectName());
				if (childid.equals(dfSysObject.getString("r_object_id"))) {

					nodeToRemove = xx.getId();
				}
			}
			final IDfVirtualDocumentNode childNodeRemove = vdm.getNodeFromNodeId(nodeToRemove);
			vdm.removeNode(childNodeRemove);
			parentobj.saveLock();
			parentobj.checkin(false, "");
		} catch (final DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error Occured!!", JOptionPane.ERROR_MESSAGE);
		} finally {
			if (session != null) {
				smanager.releaseSession(session);
			}
			final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
			setCursor(cur2);
		}
		initTree();
	}// GEN-LAST:event_mnuRemoveFromVDMActionPerformed

	private void mnuViewActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuViewActionPerformed
		final TreePath treepath = vdmTree.getSelectionPath();
		final QuickClientMutableTreeNode node = (QuickClientMutableTreeNode) treepath.getLastPathComponent();
		final String id = node.getDokuData().getObjID();
		final ActionExecutor ae = new ActionExecutor();
		final Vector v = new Vector();
		v.add(id);
		ae.viewAction(v);
	}// GEN-LAST:event_mnuViewActionPerformed

	public void setDesktop(final JDesktopPane desktopPane) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	private void vdmTreeMouseClicked(final java.awt.event.MouseEvent evt) {// GEN-FIRST:event_vdmTreeMouseClicked
		// TODO add your handling code here:

		final TreePath treepath = vdmTree.getSelectionPath();
		final QuickClientMutableTreeNode node = (QuickClientMutableTreeNode) treepath.getLastPathComponent();
		final String id = node.getDokuData().getObjID();
		// System.out.println(id);
		IDfSession session = null;
		model.setRowCount(0);
		try {
			session = smanager.getSession();
			final IDfSysObject obj = (IDfSysObject) session.getObject(new DfId(id));
			final Vector vvector = new Vector();
			vvector.add("Name");
			vvector.add(obj.getObjectName());
			vvector.add(node.getDokuData());

			model.addRow(vvector);

			final Vector vvector2 = new Vector();
			vvector2.add("Checked Out by");
			vvector2.add(obj.getString("r_lock_owner"));
			vvector2.add(node.getDokuData());

			model.addRow(vvector2);

			final Vector vvector3 = new Vector();
			vvector3.add("Checked Out Date");
			vvector3.add(obj.getString("r_lock_date"));
			vvector3.add(node.getDokuData());

			model.addRow(vvector3);

			attributeTable.setModel(model);
			attributeTable.validate();
		} catch (final DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error Occured!!", JOptionPane.ERROR_MESSAGE);
		}
	}// GEN-LAST:event_vdmTreeMouseClicked

	private void vdmTreeMouseReleased(final java.awt.event.MouseEvent evt) {// GEN-FIRST:event_vdmTreeMouseReleased
		final int butt = evt.getButton();
		if (butt == MouseEvent.BUTTON3) {
			vdmPopUp.show(evt.getComponent(), evt.getX(), evt.getY());
		}
	}// GEN-LAST:event_vdmTreeMouseReleased
}
