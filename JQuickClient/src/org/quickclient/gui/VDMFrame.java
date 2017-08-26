/*
 * VDMFrame.java
 *
 * Created on 27. huhtikuuta 2008, 20:01
 */
package org.quickclient.gui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.quickclient.classes.ActionExecutor;
import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.DokuData;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfVirtualDocument;
import com.documentum.fc.client.IDfVirtualDocumentNode;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;


/**
 *
 * @author  Administrator
 */
public class VDMFrame extends javax.swing.JFrame {

    private DocuSessionManager smanager;
    private String objid;
    DefaultTableModel model = new DefaultTableModel();

    /** Creates new form VDMFrame */
    public VDMFrame(String objid) {
        this.objid = objid;
        initComponents();
        initModel();
        smanager = DocuSessionManager.getInstance();
        initTree();
        vdmTree.setTransferHandler(new TransferHandler() {

            public boolean canImport(TransferHandler.TransferSupport info) {
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
                JTree.DropLocation dl = (JTree.DropLocation) info.getDropLocation();

                TreePath path = dl.getPath();

                // we don't support invalid paths or descendants of the names folder
                if (path == null) {
                    return false;
                }
                return true;
            }

            public boolean importData(TransferHandler.TransferSupport info) {
                // if we can't handle the import, say so
                if (!canImport(info)) {
                    return false;
                }

                // fetch the drop location
                JTree.DropLocation dl = (JTree.DropLocation) info.getDropLocation();

                // fetch the path and child index from the drop location
                TreePath path = dl.getPath();
                int childIndex = dl.getChildIndex();

                // fetch the data and bail if this fails
                String data;
                IDfSession session = null;
                try {
                    session = smanager.getSession();
                    data = (String) info.getTransferable().getTransferData(DataFlavor.stringFlavor);
                    //System.out.println("data: " + data);
                    QuickClientMutableTreeNode treenode = (QuickClientMutableTreeNode) path.getLastPathComponent();
                    //System.out.println("treenode: " + treenode.getDokuDataID());

                    IDfSysObject obj = (IDfSysObject) session.getObject(new DfId(treenode.getDokuDataID()));
                    obj.checkout();
                    IDfSysObject childobj = (IDfSysObject) session.getObject(new DfId(data));
                    IDfVirtualDocument vdm = obj.asVirtualDocument("CURRENT", false);
                    IDfVirtualDocumentNode rootNode = vdm.getRootNode();
                    boolean followAssembly = false;
                    boolean overrideLateBindingValue = false;
                    IDfVirtualDocumentNode nodeChild1 = vdm.addNode(rootNode, null, childobj.getChronicleId(), "CURRENT", followAssembly, overrideLateBindingValue);
                    obj.save();
                } catch (DfException ex) {
                    Logger.getLogger(VDMFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedFlavorException e) {
                    return false;
                } catch (IOException e) {
                    return false;
                } finally {
                    if (session != null) {
                        smanager.releaseSession(session);
                    }
                }
                initTree();
                // if child index is -1, the drop was on top of the path, so we'll
                // treat it as inserting at the end of that path's list of children
                if (childIndex == -1) {
                    childIndex = vdmTree.getModel().getChildCount(path.getLastPathComponent());
                }


                return true;
            }
        });


    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        vdmPopUp = new javax.swing.JPopupMenu();
        mnuView = new javax.swing.JMenuItem();
        mnuEdit = new javax.swing.JMenuItem();
        mnuCheckOu = new javax.swing.JMenuItem();
        mnuCheckIn = new javax.swing.JMenuItem();
        mnuRemoveFromVDM = new javax.swing.JMenuItem();
        constants1 = new org.quickclient.classes.Constants();
        jSplitPane1 = new javax.swing.JSplitPane();
        jSplitPane2 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        attributeTable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        vdmTree = new javax.swing.JTree();

        mnuView.setText("View");
        mnuView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuViewActionPerformed(evt);
            }
        });
        vdmPopUp.add(mnuView);

        mnuEdit.setText("Edit");
        mnuEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuEditActionPerformed(evt);
            }
        });
        vdmPopUp.add(mnuEdit);

        mnuCheckOu.setText("CheckOut");
        mnuCheckOu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuCheckOuActionPerformed(evt);
            }
        });
        vdmPopUp.add(mnuCheckOu);

        mnuCheckIn.setText("CheckIn");
        mnuCheckIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuCheckInActionPerformed(evt);
            }
        });
        vdmPopUp.add(mnuCheckIn);

        mnuRemoveFromVDM.setText("Remove from Virtual Document");
        mnuRemoveFromVDM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuRemoveFromVDMActionPerformed(evt);
            }
        });
        vdmPopUp.add(mnuRemoveFromVDM);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jSplitPane1.setDividerLocation(200);

        jSplitPane2.setDividerLocation(201);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        attributeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Name", "Value"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(attributeTable);

        jSplitPane2.setTopComponent(jScrollPane2);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 533, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 181, Short.MAX_VALUE)
        );

        jSplitPane2.setRightComponent(jPanel1);

        jSplitPane1.setRightComponent(jSplitPane2);

        vdmTree.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                vdmTreeMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                vdmTreeMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(vdmTree);

        jSplitPane1.setLeftComponent(jScrollPane1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 741, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void initModel() {

        model.setRowCount(0);
        attributeTable.setRowHeight(22);
        attributeTable.setAutoCreateColumnsFromModel(true);
        model.setColumnCount(0);
        model.addColumn("Attribute");
        model.addColumn("Attribute Value");

    }

    private void vdmTreeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_vdmTreeMouseClicked


        TreePath treepath = vdmTree.getSelectionPath();
        QuickClientMutableTreeNode node = (QuickClientMutableTreeNode) treepath.getLastPathComponent();
        String id = node.getDokuData().getObjID();
        //System.out.println(id);
        IDfSession session = null;
        model.setRowCount(0);
        try {
            session = smanager.getSession();
            IDfSysObject obj = (IDfSysObject) session.getObject(new DfId(id));
            Vector vvector = new Vector();
            vvector.add("Name");
            vvector.add(obj.getObjectName());
            model.addRow(vvector);

            Vector vvector2 = new Vector();
            vvector2.add("Checked Out by");
            vvector2.add(obj.getString("r_lock_owner"));
            model.addRow(vvector2);

            Vector vvector3 = new Vector();
            vvector3.add("Checked Out Date");
            vvector3.add(obj.getString("r_lock_date"));
            model.addRow(vvector3);

            attributeTable.setModel(model);
            attributeTable.validate();
        } catch (DfException ex) {
            Logger.getLogger(VDMFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_vdmTreeMouseClicked

    private void vdmTreeMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_vdmTreeMouseReleased

        // TODO add your handling code here:
        int butt = evt.getButton();
        if (butt == MouseEvent.BUTTON3) {
            vdmPopUp.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_vdmTreeMouseReleased

    private void mnuViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuViewActionPerformed

        TreePath treepath = vdmTree.getSelectionPath();
        QuickClientMutableTreeNode node = (QuickClientMutableTreeNode) treepath.getLastPathComponent();
        String id = node.getDokuData().getObjID();
        ActionExecutor ae = new ActionExecutor();
        Vector v = new Vector();
        v.add(id);
        ae.viewAction(v);

    }//GEN-LAST:event_mnuViewActionPerformed

    private void mnuEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuEditActionPerformed

        TreePath treepath = vdmTree.getSelectionPath();
        QuickClientMutableTreeNode node = (QuickClientMutableTreeNode) treepath.getLastPathComponent();
        String id = node.getDokuData().getObjID();
        ActionExecutor ae = new ActionExecutor();
        ae.editAction(id, null);

    }//GEN-LAST:event_mnuEditActionPerformed

    private void mnuCheckOuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuCheckOuActionPerformed

        TreePath treepath = vdmTree.getSelectionPath();
        QuickClientMutableTreeNode node = (QuickClientMutableTreeNode) treepath.getLastPathComponent();
        String id = node.getDokuData().getObjID();
        ActionExecutor ae = new ActionExecutor();
        ae.checkOutAction(id, null);

    }//GEN-LAST:event_mnuCheckOuActionPerformed

    private void mnuCheckInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuCheckInActionPerformed

        TreePath treepath = vdmTree.getSelectionPath();
        QuickClientMutableTreeNode node = (QuickClientMutableTreeNode) treepath.getLastPathComponent();
        String id = node.getDokuData().getObjID();
        ActionExecutor ae = new ActionExecutor();
        ae.checkInAction(id, null);

    }//GEN-LAST:event_mnuCheckInActionPerformed

private void mnuRemoveFromVDMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuRemoveFromVDMActionPerformed

    /*
     *  try
        {
            IDfSession dfSession = getDfSession();
            ArgumentList arguments = getInitArgs();
            String rootObjectId = arguments.get("vdmRootObjectId");
            VdmStore vdmStore = VdmStore.getInstance();
            IDfVirtualDocument dfVirtualDocument = vdmStore.get(dfSession, rootObjectId, true);
            String nodeIdArgument = arguments.get("nodeId");
            DfId dfId = new DfId(nodeIdArgument);
            IDfVirtualDocumentNode dfVirtualDocumentNode = dfVirtualDocument.getNodeFromNodeId(dfId);
            IDfVirtualDocumentNode parentNode = dfVirtualDocumentNode.getParent();
            IDfBasicAttributes dfBasicAttributes = parentNode.getBasicAttributes();
            IDfId parentId = dfBasicAttributes.getObjectId();
            IDfSysObject parentObject = (IDfSysObject)dfSession.getObject(parentId);
            String parentLockOwner = parentObject.getLockOwner();
            String currentUser = dfSession.getLoginUserName();
            if(currentUser.equals(parentLockOwner))
            {
                ModifiedVDMNodes modifiedVdmNodes = ModifiedVDMNodes.getInstance();
                parentObjectId = parentId.getId();
                modifiedVdmNodes.prepareObjectForModification(parentObjectId);
                dfVirtualDocument.removeNode(dfVirtualDocumentNode);
                String parentNodeId = parentNode.getId().getId();
                modifiedVdmNodes.addModifiedNode(parentObjectId, rootObjectId, parentNodeId);
                setClientEvent("ChangeMade", null);
                changesCommittedSuccessfully = true;
     */

    IDfSession session = null;
    try {
        session = smanager.getSession();
        String childid;
        String parentid;
        TreePath path = vdmTree.getSelectionPath();
        if (path == null) {
            return;
        }
        QuickClientMutableTreeNode selectedNode = (QuickClientMutableTreeNode) path.getLastPathComponent();
        QuickClientMutableTreeNode parentNode = (QuickClientMutableTreeNode) path.getParentPath().getLastPathComponent();
        childid = selectedNode.getDokuDataID();
        parentid = parentNode.getDokuDataID();
        //System.out.println("parentid:" + parentid);
        //System.out.println("cid:" + childid);
        
        IDfSysObject parentobj = (IDfSysObject) session.getObject(new DfId(parentid));
        parentobj.checkout();
        IDfSysObject childobj = (IDfSysObject) session.getObject(new DfId(childid));
        IDfVirtualDocument vdm = parentobj.asVirtualDocument("CURRENT", false);
        IDfVirtualDocumentNode rootNode = vdm.getRootNode();
        boolean followAssembly = false;
        boolean overrideLateBindingValue = false;
        //vdm.removeNode(rootNode)
        IDfVirtualDocumentNode dfVirtualDocumentNode = vdm.getNodeFromNodeId(new DfId(childid));
        vdm.removeNode(dfVirtualDocumentNode);
        
        //IDfVirtualDocumentNode childNode = rootNode.
        //vdm.removeNode(childNode);
        parentobj.save();
    } catch (DfException ex) {
        Logger.getLogger(VDMFrame.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
        if (session != null) {
            smanager.releaseSession(session);
        }
    }
    initTree();


}//GEN-LAST:event_mnuRemoveFromVDMActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable attributeTable;
    private org.quickclient.classes.Constants constants1;
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

    private void initTree() {

        IDfSession session = null;
        QuickClientMutableTreeNode root;
        QuickClientMutableTreeNode c;
        DefaultTreeModel treemodel;

        ////System.out.println("model: " + treemodel);
        try {
            session = smanager.getSession();
            IDfSysObject obj = (IDfSysObject) session.getObject(new DfId(objid));
            IDfVirtualDocument vdm = obj.asVirtualDocument("CURRENT", false);
            IDfVirtualDocumentNode rootNode = vdm.getRootNode();
            root = new QuickClientMutableTreeNode(obj.getObjectName());
            DokuData dd = new DokuData();
            String iidee = obj.getString("r_object_id");
            dd.setObjID(iidee);
            root.setDokuData(dd);
            treemodel = new DefaultTreeModel(root);
            int childCount = rootNode.getChildCount();
            for (int i = 0; i < childCount; i++) {
                IDfVirtualDocumentNode child = rootNode.getChild(i);
                //System.out.println(child.getChronId().toString());
                IDfSysObject curChild = (IDfSysObject) session.getObjectByQualification("dm_sysobject where i_chronicle_id = '" + child.getChronId().toString() + "'");
                c = new QuickClientMutableTreeNode(curChild.getObjectName());
                DokuData dd2 = new DokuData();
                dd2.setObjID(curChild.getString("r_object_id"));
                c.setDokuData(dd2);
                root.add(c);
            }
            vdmTree.setModel(treemodel);
        } catch (DfException ex) {
            Logger.getLogger(VDMFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        vdmTree.validate();
    }
}
