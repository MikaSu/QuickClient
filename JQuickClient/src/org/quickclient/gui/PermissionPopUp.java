/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quickclient.gui;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;
import org.quickclient.classes.DocInfo;
import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.SwingHelper;
import org.quickclient.classes.UserorGroupSelectorData;

import com.documentum.fc.client.DfACL;
import com.documentum.fc.client.IDfACL;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.IDfId;


/**
 *
 * @author Mika
 */
public class PermissionPopUp extends JPopupMenu {

    private JTable table;
    private DocuSessionManager smanager;
    private JMenuItem mnuRemovePermit;
    private JMenu mnuPermitMenu;
    private JMenuItem mnuNone;
    private JMenuItem mnuBrowse;
    private JMenuItem mnuRead;
    private JMenuItem mnuRelate;
    private JMenuItem mnuVersion;
    private JSeparator jSeparator1;
    private JMenuItem mnuDel;
    private JMenuItem mnuWrite;
    private JMenuItem mnuAddAccessor;
    private String selectedId;
    Logger log = Logger.getLogger(PermissionPopUp.class);
    private JSeparator jSeparator2;
    private JMenuItem mnuEditACL;

    public PermissionPopUp() {
        initMenu();
        smanager = DocuSessionManager.getInstance();

    }

    void setTable(JTable tblDocInfo) {
        this.table = tblDocInfo;
    }

    private void mnuRemovePermitActionPerformed(java.awt.event.ActionEvent evt) {

        int row = table.getSelectedRow();
        String accessor = getAccessorFromTable();
        if (accessor.equals("dm_world") || accessor.equals("dm_owner")) {
            JOptionPane.showMessageDialog(null, "Cannot remove dm_world or dm_owner", "", JOptionPane.INFORMATION_MESSAGE);
            return;

        }

        IDfSession session = null;
        Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
        setCursor(cur);
        try {
            session = smanager.getSession();
            IDfId id = new DfId(selectedId);
            IDfSysObject obj = (IDfSysObject) session.getObject(id);
            obj.revoke(accessor, null);
            if (obj.isCheckedOut()) {
                obj.saveLock();
            } else {
                obj.save();
            }
            DefaultTableModel m = (DefaultTableModel) table.getModel();
            m.removeRow(row);
            table.setModel(m);
            table.validate();
        } catch (DfException exception) {
            log.error(exception);
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Error Occured!!", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (session != null) {
                smanager.releaseSession(session);
            }
            Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
            setCursor(cur2);
        }
    }

    private void initMenu() {
        mnuRemovePermit = new javax.swing.JMenuItem();
        mnuPermitMenu = new javax.swing.JMenu();
        mnuNone = new javax.swing.JMenuItem();
        mnuBrowse = new javax.swing.JMenuItem();
        mnuRead = new javax.swing.JMenuItem();
        mnuRelate = new javax.swing.JMenuItem();
        mnuVersion = new javax.swing.JMenuItem();
        mnuWrite = new javax.swing.JMenuItem();
        mnuDel = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        mnuAddAccessor = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        //jSeparator1 = new javax.swing.JSeparator();
        mnuEditACL = new javax.swing.JMenuItem();

        mnuRemovePermit.setText("Remove");
        mnuRemovePermit.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuRemovePermitActionPerformed(evt);
            }
        });
        this.add(mnuRemovePermit);

        mnuPermitMenu.setText("Set Permission to");

        mnuNone.setText("None");
        mnuNone.setToolTipText("No permissions");
        mnuNone.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuNoneActionPerformed(evt);
            }
        });
        mnuPermitMenu.add(mnuNone);

        mnuBrowse.setText("Browse");
        mnuBrowse.setToolTipText("Can view metadata");
        mnuBrowse.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuBrowseActionPerformed(evt);
            }
        });
        mnuPermitMenu.add(mnuBrowse);

        mnuRead.setText("Read");
        mnuRead.setToolTipText("Can read content of the document");
        mnuRead.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuReadActionPerformed(evt);
            }
        });
        mnuPermitMenu.add(mnuRead);

        mnuRelate.setText("Relate");
        mnuRelate.setToolTipText("Can create relations, i.e. Subscribe doc");
        mnuRelate.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuRelateActionPerformed(evt);
            }
        });
        mnuPermitMenu.add(mnuRelate);

        mnuVersion.setText("Version");
        mnuVersion.setToolTipText("Can view and create new Version");
        mnuVersion.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuVersionActionPerformed(evt);
            }
        });
        mnuPermitMenu.add(mnuVersion);

        mnuWrite.setText("Write");
        mnuWrite.setToolTipText("Can modify this version");
        mnuWrite.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuWriteActionPerformed(evt);
            }
        });
        mnuPermitMenu.add(mnuWrite);

        mnuDel.setText("Delete");
        mnuDel.setToolTipText("Can remove this document");
        mnuDel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuDelActionPerformed(evt);
            }
        });
        mnuPermitMenu.add(mnuDel);

        this.add(mnuPermitMenu);
        this.add(jSeparator1);

        mnuAddAccessor.setText("Add Accessor");
        mnuAddAccessor.setToolTipText("Add Person or Group to Permission set");
        mnuAddAccessor.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuAddAccessorActionPerformed(evt);
            }
        });
        this.add(mnuAddAccessor);

        this.add(jSeparator2);
        mnuEditACL.setText("Edit this ACL");
        mnuEditACL.setToolTipText("Edit this ACL in Editor");
        mnuEditACL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {

                mnuEditACL(e);
            }
        });
        this.add(mnuEditACL);

    }

    private void mnuEditACL(java.awt.event.ActionEvent evt) {


        IDfSession session = null;
        try {
            session = smanager.getSession();
            IDfId id = new DfId(selectedId);
            IDfSysObject obj = (IDfSysObject) session.getObject(id);
            IDfACL acl = (IDfACL) obj.getACL();
            ACLEditorFrame frame = new ACLEditorFrame(acl);
            SwingHelper.centerJFrame(frame);
            frame.setVisible(true);
        } catch (DfException ex) {
            log.error(ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (session != null) {
                smanager.releaseSession(session);
            }
        }

    }

    private void mnuNoneActionPerformed(java.awt.event.ActionEvent evt) {

        String accessor = getAccessorFromTable();
        setAccessorPermitTo(accessor, IDfACL.DF_PERMIT_NONE);
        updatePermissionModel();
    }

    private void mnuAddAccessorActionPerformed(java.awt.event.ActionEvent evt) {

        /*
        Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
        getTable().getParent().setCursor(cur);
        ActionExecutor ae = new ActionExecutor();
        Vector objids = getIDListfromTable();
        ae.viewTextAction(objids);
        Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
        getTable().getParent().setCursor(cur2);
         */
        Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
        table.getParent().setCursor(cur);
        final UserorGroupSelectorData x = new UserorGroupSelectorData();
        IDfSession session = null;
        try {
            session = smanager.getSession();
            IDfId id = new DfId(selectedId);
            final IDfSysObject obj = (IDfSysObject) session.getObject(id);
            ActionListener al = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    try {
                        String accessor = x.getUserorGroupname();
                        obj.grant(accessor, DfACL.DF_PERMIT_NONE, null);
                        if (obj.isCheckedOut()) {
                            obj.saveLock();
                        } else {
                            obj.save();
                        }

                    } catch (DfException ex) {
                        log.error(ex);
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
                    }

                }
            };
            UserorGroupSelectorFrame frame = new UserorGroupSelectorFrame(al, x);
            SwingHelper.centerJFrame(frame);
            frame.setVisible(true);
        } catch (DfException ex) {
            log.error(ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (session != null) {
                smanager.releaseSession(session);
            }

        }
        Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
        table.getParent().setCursor(cur2);

        updatePermissionModel();
        table.validate();
    }

    private String getAccessorFromTable() {

        int row = table.getSelectedRow();
        String accessor = (String) table.getModel().getValueAt(row, 2);
        return accessor;
    }

    /**
     * @param selectedId the selectedId to set
     */
    public void setSelectedId(String selectedId) {
        this.selectedId = selectedId;
    }

    private void setAccessorPermitTo(String accessor, int permit) {
        IDfSession session = null;
        Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
        setCursor(cur);
        try {
            session = smanager.getSession();
            IDfId id = new DfId(selectedId);
            IDfSysObject obj = (IDfSysObject) session.getObject(id);
            obj.grant(accessor, permit, null);
            if (obj.isCheckedOut()) {
                obj.saveLock();
            } else {
                obj.save();
            }
        } catch (Exception ex) {
            log.error(ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error Occured!!", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (session != null) {
                smanager.releaseSession(session);
            }
            Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
            setCursor(cur2);
        }

    }

    private void updatePermissionModel() {
        IDfSession session = null;
        String objid = selectedId;
        DocInfo info = new DocInfo();
        DefaultTableModel oldModel = (DefaultTableModel) table.getModel();
        DefaultTableModel permissionlistModel = info.updatePermissionsModel(objid, oldModel);
        table.setModel(permissionlistModel);
        table.validate();

    }

    private void mnuBrowseActionPerformed(java.awt.event.ActionEvent evt) {
        String accessor = getAccessorFromTable();
        setAccessorPermitTo(accessor, IDfACL.DF_PERMIT_BROWSE);
        updatePermissionModel();
        table.validate();

    }

    private void mnuReadActionPerformed(java.awt.event.ActionEvent evt) {
        String accessor = getAccessorFromTable();
        setAccessorPermitTo(accessor, IDfACL.DF_PERMIT_READ);
        updatePermissionModel();
        table.validate();
    }

    private void mnuRelateActionPerformed(java.awt.event.ActionEvent evt) {
        String accessor = getAccessorFromTable();
        setAccessorPermitTo(accessor, IDfACL.DF_PERMIT_RELATE);
        updatePermissionModel();
        table.validate();
    }

    private void mnuVersionActionPerformed(java.awt.event.ActionEvent evt) {
        String accessor = getAccessorFromTable();
        setAccessorPermitTo(accessor, IDfACL.DF_PERMIT_VERSION);
        updatePermissionModel();
        table.validate();
    }

    private void mnuWriteActionPerformed(java.awt.event.ActionEvent evt) {
        String accessor = getAccessorFromTable();
        setAccessorPermitTo(accessor, IDfACL.DF_PERMIT_WRITE);
        updatePermissionModel();
        table.validate();
    }

    private void mnuDelActionPerformed(java.awt.event.ActionEvent evt) {
        String accessor = getAccessorFromTable();
        setAccessorPermitTo(accessor, IDfACL.DF_PERMIT_DELETE);
        updatePermissionModel();
        table.validate();
    }
}
