/*
 * GroupEditorGroups.java
 *
 * Created on 24. kesäkuuta 2007, 23:27
 */
package org.quickclient.gui;


import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;
import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.GroupsinGroupData;

/**
 *
 * @author  Administrator
 */
public class GroupEditorGroups extends javax.swing.JFrame {

    DocuSessionManager smanager;
    Logger log = Logger.getLogger(GroupEditorGroups.class);
    /** Creates new form GroupEditorGroups */
    public GroupEditorGroups(ActionListener listener, GroupsinGroupData data) {
        initComponents();
        this.jScrollPane1.getViewport().setBackground(Color.WHITE);
        this.jScrollPane2.getViewport().setBackground(Color.WHITE);

        smanager = DocuSessionManager.getInstance();
        DefaultTableModel amodel = new DefaultTableModel() {

            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        amodel.addColumn("Group Name");

        setAvailablemodel(amodel);
        String temp = "";
        cmdOK.addActionListener(listener);
        setGroupdata(data);
        DefaultTableModel model = new DefaultTableModel() {

            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.addColumn("Group Name");
        for (int i = 0; i < data.getGroupMembers().size(); i++) {
            temp = (String) data.getGroupMembers().get(i);
            Vector<Object> v = new Vector<Object>();
            v.add(temp);
            model.addRow(v);
        }

        setGroupmodel(model);
        tblGroupsInGroup.setModel(getGroupmodel());
        tblGroupsInGroup.validate();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cmdRemove = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        txtGroupUserFilter = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblGroupsInGroup = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        cmdSearchButton = new javax.swing.JButton();
        txtuserSearchFileter = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblAvailebleGroups = new javax.swing.JTable();
        cmdAdd = new javax.swing.JButton();
        cmdCancel = new javax.swing.JButton();
        cmdOK = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        cmdRemove.setMnemonic('R');
        cmdRemove.setText("Remove");
        cmdRemove.setDefaultCapable(false);
        cmdRemove.setMargin(new java.awt.Insets(2, 4, 2, 4));
        cmdRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRemoveActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Group Members", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        tblGroupsInGroup.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Groups in Group"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblGroupsInGroup.setShowHorizontalLines(false);
        tblGroupsInGroup.setShowVerticalLines(false);
        jScrollPane1.setViewportView(tblGroupsInGroup);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane1, 0, 0, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, txtGroupUserFilter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 235, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(txtGroupUserFilter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 289, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Available Groups"));

        cmdSearchButton.setText("...");
        cmdSearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSearchButtonActionPerformed(evt);
            }
        });

        tblAvailebleGroups.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Groups"
            }
        ));
        tblAvailebleGroups.setShowHorizontalLines(false);
        tblAvailebleGroups.setShowVerticalLines(false);
        jScrollPane2.setViewportView(tblAvailebleGroups);

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(txtuserSearchFileter, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cmdSearchButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 34, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cmdSearchButton)
                    .add(txtuserSearchFileter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 289, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        cmdAdd.setMnemonic('A');
        cmdAdd.setText("Add");
        cmdAdd.setMargin(new java.awt.Insets(2, 4, 2, 4));
        cmdAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddActionPerformed(evt);
            }
        });

        cmdCancel.setText("Cancel");
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });

        cmdOK.setText("OK");
        cmdOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdOKActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(cmdRemove, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(cmdAdd, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(487, 487, 487)
                        .add(cmdOK)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cmdCancel)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(cmdOK)
                            .add(cmdCancel)))
                    .add(layout.createSequentialGroup()
                        .add(145, 145, 145)
                        .add(cmdAdd)
                        .add(18, 18, 18)
                        .add(cmdRemove)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_cmdCancelActionPerformed

    private void cmdOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdOKActionPerformed

        this.dispose();
    }//GEN-LAST:event_cmdOKActionPerformed

    private void cmdRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRemoveActionPerformed
        int row = tblGroupsInGroup.getSelectedRow();
        getGroupmodel().removeRow(row);
        tblGroupsInGroup.validate();
        Vector<Object> groupmembers = new Vector<Object>();
        for (int i = 0; i < getGroupmodel().getRowCount(); i++) {
            Vector<Object> x = (Vector) getGroupmodel().getDataVector().elementAt(i);
            String Stringi2 = (String) x.elementAt(0);
            groupmembers.add(Stringi2);
        }
        getGroupdata().setGroupMembers(groupmembers);

    }//GEN-LAST:event_cmdRemoveActionPerformed

    private void cmdAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddActionPerformed
        boolean canadd = true;
        int row = tblAvailebleGroups.getSelectedRow();
        Vector v = (Vector) getAvailablemodel().getDataVector().elementAt(row);
        String Stringi = (String) v.elementAt(0);

        for (int i = 0; i < getGroupmodel().getRowCount(); i++) {
            Vector x = (Vector) getGroupmodel().getDataVector().elementAt(i);
            String Stringi2 = (String) x.elementAt(0);
            if (Stringi.equals(Stringi2)) {
                canadd = false;
            }
        }
        if (canadd) {
            getGroupmodel().addRow(v);
            tblGroupsInGroup.validate();
            Vector groupmembers = new Vector();
            for (int i = 0; i < getGroupmodel().getRowCount(); i++) {
                Vector x = (Vector) getGroupmodel().getDataVector().elementAt(i);
                String Stringi2 = (String) x.elementAt(0);
                groupmembers.add(Stringi2);
            }
            getGroupdata().setGroupMembers(groupmembers);
        }
    }//GEN-LAST:event_cmdAddActionPerformed

    private void cmdSearchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSearchButtonActionPerformed

        String queryFilter = txtuserSearchFileter.getText();
        IDfCollection col = null;
        IDfQuery query = new DfQuery();
        getAvailablemodel().setRowCount(0);
        if (queryFilter.length() > 0) {
            query.setDQL("select group_name from dm_group where group_name like '" + queryFilter + "%' order by group_name");
        } else {
            query.setDQL("select group_name from dm_group order by group_name");
        }
        IDfSession session = smanager.getSession();
        try {
            session = smanager.getSession();
            col = query.execute(session, IDfQuery.DF_READ_QUERY);
            while (col.next()) {
                Vector<String> kakkavektor = new Vector<String>();
                String username = col.getString("group_name");
                kakkavektor.add(username);
                getAvailablemodel().addRow(kakkavektor);
            }
            col.close();
        } catch (DfException ex) {
            log.error(ex);
        } finally {
            if (col != null) {
                try {
                    col.close();
                } catch (DfException ex) {
                }
            }
            if (session != null) {
                smanager.releaseSession(session);
            }
        }
        tblAvailebleGroups.setModel(getAvailablemodel());
        tblAvailebleGroups.validate();
    }//GEN-LAST:event_cmdSearchButtonActionPerformed
    private GroupsinGroupData groupdata;
    private DefaultTableModel groupmodel;
    private DefaultTableModel availablemodel;

    public GroupsinGroupData getGroupdata() {
        return groupdata;
    }

    public void setGroupdata(GroupsinGroupData groupdata) {
        this.groupdata = groupdata;
    }

    public DefaultTableModel getGroupmodel() {
        return groupmodel;
    }

    public void setGroupmodel(DefaultTableModel groupmodel) {
        this.groupmodel = groupmodel;
    }

    public DefaultTableModel getAvailablemodel() {
        return availablemodel;
    }

    public void setAvailablemodel(DefaultTableModel availablemodel) {
        this.availablemodel = availablemodel;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdAdd;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdOK;
    private javax.swing.JButton cmdRemove;
    private javax.swing.JButton cmdSearchButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblAvailebleGroups;
    private javax.swing.JTable tblGroupsInGroup;
    private javax.swing.JTextField txtGroupUserFilter;
    private javax.swing.JTextField txtuserSearchFileter;
    // End of variables declaration//GEN-END:variables
}
