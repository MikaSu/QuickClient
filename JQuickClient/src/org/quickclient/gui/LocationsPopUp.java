/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quickclient.gui;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.SwingHelper;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.IDfId;


/**
 *
 * @author Mika
 */
public class LocationsPopUp extends JPopupMenu {

    private DocuSessionManager smanager;
    private String selectedId;
    private JTable table;

    public LocationsPopUp() {
        initMenu();
        smanager = DocuSessionManager.getInstance();
    }
    
    JMenuItem mnuUnLink = new javax.swing.JMenuItem();
    JMenuItem mnuAddLink = new javax.swing.JMenuItem();

    private void initMenu() {
        mnuUnLink.setText("Unlink From Here...");
        mnuUnLink.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuUnLinkActionPerformed(evt);
            }
        });
        this.add(mnuUnLink);

        mnuAddLink.setText("Add Link");
        mnuAddLink.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuAddLinkActionPerformed(evt);
            }
        });
        this.add(mnuAddLink);

    }

    private void mnuUnLinkActionPerformed(java.awt.event.ActionEvent evt) {
        IDfSession session = null;
        Cursor cur = new Cursor(Cursor.WAIT_CURSOR);

        setCursor(cur);
        try {
            session = smanager.getSession();
            IDfId id = new DfId(selectedId);
            IDfSysObject obj = (IDfSysObject) session.getObject(id);
            int row = table.getSelectedRow();
            DefaultTableModel m = (DefaultTableModel) table.getModel();
            Vector v = (Vector) m.getDataVector().elementAt(row);
            String folderPath = (String) v.get(2);
            obj.unlink(folderPath);
            if (obj.isCheckedOut()) {
                obj.saveLock();
            } else {
                obj.save();
            }

            m.removeRow(row);
            table.setModel(m);
            table.validate();
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Error Occured!!", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (session != null) {
                smanager.releaseSession(session);
            }

            Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);

            setCursor(cur2);
        }
    }

    private void mnuAddLinkActionPerformed(java.awt.event.ActionEvent evt) {

        final FolderSelectorData fsd = new FolderSelectorData();
        ActionListener a = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            	
                IDfSession session = null;
                session =
                        smanager.getSession();
                ////System.out.println(fsd.getFolderPath());
                try {
                    IDfId id = new DfId(selectedId);
                    IDfSysObject obj = (IDfSysObject) session.getObject(id);
                    String folderPath = fsd.getFolderPath();
                    obj.link(folderPath);
                    if (obj.isCheckedOut()) {
                        obj.saveLock();
                    } else {
                        obj.save();
                    }
                    DefaultTableModel m = (DefaultTableModel) table.getModel();
                    Vector<String> v = new Vector<String>();
                    v.add("");
                    v.add(",dm_folder");
                    v.add(folderPath);
                    m.addRow(v);
                    table.setModel(m);
                    table.validate();
                } catch (DfException exception) {
                    JOptionPane.showMessageDialog(null, exception.getMessage(), "Error Occured!!", JOptionPane.ERROR_MESSAGE);
                } finally {
                    if (session != null) {
                        smanager.releaseSession(session);
                    }

                }
            }
        };
        FolderSelectorFrame fsf = new FolderSelectorFrame(a, fsd);
        
		SwingHelper.centerJFrame(fsf);
        fsf.setVisible(true);

    }

    public void setSelectedId(String selectedId) {
        this.selectedId = selectedId;
    }

    void setTable(JTable tblDocInfo) {
        this.table = tblDocInfo;
    }
}
