/*
 * GroupEditorUsers.java
 *
 * Created on 24. kesäkuuta 2007, 23:26
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
import org.quickclient.classes.UsersInGroupData;

/**
 *
 * @author  Administrator
 */
public class GroupEditorUsers extends javax.swing.JFrame {

    DocuSessionManager smanager;
    Logger log = Logger.getLogger(GroupEditorUsers.class);

    /** Creates new form GroupEditorUsers
     * @param listener
     * @param data
     */
    public GroupEditorUsers(ActionListener listener, UsersInGroupData data) {
        initComponents();
        smanager = DocuSessionManager.getInstance();
        this.jScrollPane1.getViewport().setBackground(Color.WHITE);
        this.jScrollPane2.getViewport().setBackground(Color.WHITE);

        DefaultTableModel amodel = new DefaultTableModel() {

            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        amodel.addColumn("User Name");

        setAvailablemodel(amodel);
        String temp = "";
        cmdOK.addActionListener(listener);
        setGroupdata(data);
        DefaultTableModel model = new DefaultTableModel() {

            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.addColumn("User Name");
        for (int i = 0; i < data.getGroupMembers().size(); i++) {
            temp = (String) data.getGroupMembers().get(i);
            Vector<Object> v = new Vector<Object>();
            v.add(temp);
            model.addRow(v);
        }

        setUsermodel(model);
        tblUsersInGroup.setModel(getUserModel());
        tblUsersInGroup.validate();

    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblUsersInGroup = new javax.swing.JTable();
        txtUserGroupFilter = new javax.swing.JTextField();
        cmdAdd = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        cmdSearchButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAvailableUsers = new javax.swing.JTable();
        txtuserSearchFileter = new javax.swing.JTextField();
        cmdRemove = new javax.swing.JButton();
        cmdOK = new javax.swing.JButton();
        cmdCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Users in Group"));

        tblUsersInGroup.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Users"
            }
        ));
        tblUsersInGroup.setShowHorizontalLines(false);
        tblUsersInGroup.setShowVerticalLines(false);
        jScrollPane2.setViewportView(tblUsersInGroup);

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, txtUserGroupFilter, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup()
                .add(txtUserGroupFilter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 289, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        cmdAdd.setMnemonic('A');
        cmdAdd.setText("Add");
        cmdAdd.setMargin(new java.awt.Insets(2, 4, 2, 4));
        cmdAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Available Users", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        cmdSearchButton1.setText("...");
        cmdSearchButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSearchButton1ActionPerformed(evt);
            }
        });

        tblAvailableUsers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Users"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblAvailableUsers.setShowHorizontalLines(false);
        tblAvailableUsers.setShowVerticalLines(false);
        jScrollPane1.setViewportView(tblAvailableUsers);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                        .add(txtuserSearchFileter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 162, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 7, Short.MAX_VALUE)
                        .add(cmdSearchButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 34, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cmdSearchButton1)
                    .add(txtuserSearchFileter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                .addContainerGap())
        );

        cmdRemove.setMnemonic('R');
        cmdRemove.setText("Remove");
        cmdRemove.setDefaultCapable(false);
        cmdRemove.setMargin(new java.awt.Insets(2, 4, 2, 4));
        cmdRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRemoveActionPerformed(evt);
            }
        });

        cmdOK.setText("OK");
        cmdOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdOKActionPerformed(evt);
            }
        });

        cmdCancel.setText("Cancel");
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(191, 191, 191)
                        .add(cmdOK)
                        .add(16, 16, 16)
                        .add(cmdCancel))
                    .add(layout.createSequentialGroup()
                        .add(6, 6, 6)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(cmdRemove, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(cmdAdd, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(136, 136, 136)
                        .add(cmdAdd)
                        .add(16, 16, 16)
                        .add(cmdRemove))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(cmdOK)
                            .add(cmdCancel))))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_cmdCancelActionPerformed

    private void cmdOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdOKActionPerformed
        this.dispose();
    }//GEN-LAST:event_cmdOKActionPerformed

    private void cmdSearchButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSearchButton1ActionPerformed
        String queryFilter = txtuserSearchFileter.getText();
        IDfCollection col = null;
        IDfSession session = null;
        IDfQuery query = new DfQuery();
        getAvailablemodel().setRowCount(0);
        if (queryFilter.length() > 0) {
            query.setDQL("select user_name from dm_user where r_is_group=0 and user_name like '" + queryFilter + "%' order by user_name");
        } else {
            query.setDQL("select user_name from dm_user where r_is_group=0 order by user_name");
        }
        try {
            session = smanager.getSession();
            col = query.execute(session, IDfQuery.DF_READ_QUERY);
            while (col.next()) {
                Vector<Object> kakkavektor = new Vector<Object>();
                String username = col.getString("user_name");
                kakkavektor.add(username);
                getAvailablemodel().addRow(kakkavektor);
            }
        } catch (DfException ex) {
            log.error(ex);
        } finally {
            if (col != null) {
                try {
                    col.close();
                } catch (DfException e) {
                    log.error(e);
                }
            }
            if (session != null) {
                smanager.releaseSession(session);
            }
        }
        tblAvailableUsers.setModel(getAvailablemodel());
        tblAvailableUsers.validate();
    }//GEN-LAST:event_cmdSearchButton1ActionPerformed

    private void cmdRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRemoveActionPerformed
        int row = tblUsersInGroup.getSelectedRow();
        getUserModel().removeRow(row);
        tblUsersInGroup.validate();
        Vector<Object> groupmembers = new Vector<Object>();
        for (int i = 0; i < getUserModel().getRowCount(); i++) {
            Vector x = (Vector) getUserModel().getDataVector().elementAt(i);
            String Stringi2 = (String) x.elementAt(0);
            groupmembers.add(Stringi2);
        }
        getUserData().setGroupMembers(groupmembers);
    }//GEN-LAST:event_cmdRemoveActionPerformed

    private void cmdAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddActionPerformed
        boolean canadd = true;
        int row = tblAvailableUsers.getSelectedRow();
        Vector v = (Vector) getAvailablemodel().getDataVector().elementAt(row);
        String Stringi = (String) v.elementAt(0);

        for (int i = 0; i < getUserModel().getRowCount(); i++) {
            Vector x = (Vector) getUserModel().getDataVector().elementAt(i);
            String Stringi2 = (String) x.elementAt(0);
            if (Stringi.equals(Stringi2)) {
                canadd = false;
            }
        }
        if (canadd) {
            getUserModel().addRow(v);
            tblUsersInGroup.validate();
            Vector<Object> groupmembers = new Vector<Object>();
            for (int i = 0; i < getUserModel().getRowCount(); i++) {
                Vector<Object> x = (Vector) getUserModel().getDataVector().elementAt(i);
                String Stringi2 = (String) x.elementAt(0);
                groupmembers.add(Stringi2);
            }
            getUserData().setGroupMembers(groupmembers);
        }
    }//GEN-LAST:event_cmdAddActionPerformed
    private UsersInGroupData userdata;
    private DefaultTableModel usermodel;
    private DefaultTableModel availablemodel;

    public UsersInGroupData getUserData() {
        return userdata;
    }

    public void setGroupdata(UsersInGroupData userdata) {
        this.userdata = userdata;
    }

    public DefaultTableModel getUserModel() {
        return usermodel;
    }

    public void setUsermodel(DefaultTableModel usermodel) {
        this.usermodel = usermodel;
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
    private javax.swing.JButton cmdSearchButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblAvailableUsers;
    private javax.swing.JTable tblUsersInGroup;
    private javax.swing.JTextField txtUserGroupFilter;
    private javax.swing.JTextField txtuserSearchFileter;
    // End of variables declaration//GEN-END:variables
}
