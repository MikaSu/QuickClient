/*
 * UserSelectorFrame.java
 *
 * Created on 5. marraskuuta 2006, 20:19
 */
package org.quickclient.gui;


import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;
import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.UserSelectorData;

/**
 *
 * @author  Administrator
 */
public class UserSelectorFrame extends javax.swing.JFrame {

    DocuSessionManager smanager;
    Logger log = Logger.getLogger(UserSelectorFrame.class);
    /** Creates new form UserSelectorFrame */
    public UserSelectorFrame(ActionListener listener, UserSelectorData data) {

        initComponents();
        jScrollPane1.getViewport().setBackground(Color.WHITE);

        smanager = DocuSessionManager.getInstance();
        cmdSelect.addActionListener(listener);
        setUserselectordata(data);
        DefaultTableModel model = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        usermodel = model;
        usermodel.addColumn("User name");

        tblUserList.setModel(usermodel);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtUserFilter = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cmdQuery = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblUserList = new javax.swing.JTable();
        cmdSelect = new javax.swing.JButton();
        cmdCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setLocationByPlatform(true);
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("User Filter"));

        txtUserFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUserFilterActionPerformed(evt);
            }
        });
        txtUserFilter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtUserFilterKeyReleased(evt);
            }
        });

        jLabel1.setText("User Name:");

        cmdQuery.setMnemonic('q');
        cmdQuery.setText("Query");
        cmdQuery.setMargin(new java.awt.Insets(1, 4, 1, 4));
        cmdQuery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdQueryActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txtUserFilter, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cmdQuery)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(txtUserFilter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cmdQuery))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblUserList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "User Name"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblUserList.setShowHorizontalLines(false);
        tblUserList.setShowVerticalLines(false);
        tblUserList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblUserListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblUserList);

        cmdSelect.setText("Select");
        cmdSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSelectActionPerformed(evt);
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
            .add(layout.createSequentialGroup()
                .add(249, 249, 249)
                .add(cmdSelect)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cmdCancel)
                .addContainerGap())
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cmdCancel)
                    .add(cmdSelect))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void txtUserFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUserFilterActionPerformed

        cmdQueryActionPerformed(evt);
    }//GEN-LAST:event_txtUserFilterActionPerformed

    private void cmdSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSelectActionPerformed
        int row = tblUserList.getSelectedRow();
        Vector v = (Vector) usermodel.getDataVector().elementAt(row);
        ////System.out.println(v);
        String Stringi = (String) v.elementAt(0);
        getUserselectordata().setUsername(Stringi);
        this.dispose();
    }//GEN-LAST:event_cmdSelectActionPerformed

    private void cmdQueryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdQueryActionPerformed
        String queryFilter = txtUserFilter.getText();
        IDfCollection col = null;
        IDfQuery query = new DfQuery();
        if (queryFilter.length() > 0) {
            query.setDQL("select user_name from dm_user where r_is_group=0 and user_name like '" + queryFilter + "%' order by user_name");
        } else {
            query.setDQL("select user_name from dm_user order by user_name");
        }

        IDfSession session = null;
        try {
            session = smanager.getSession();
            col = query.execute(session, IDfQuery.DF_READ_QUERY);
            while (col.next()) {
                Vector<String> kakkavektor = new Vector<String>();
                String username = col.getString("user_name");
                kakkavektor.add(username);
                usermodel.addRow(kakkavektor);
                listArray.add(username);

            }
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
        tblUserList.validate();
    }//GEN-LAST:event_cmdQueryActionPerformed

    private void tblUserListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblUserListMouseClicked
        int row = tblUserList.getSelectedRow();
        Vector v = (Vector) usermodel.getDataVector().elementAt(row);
        ////System.out.println(v);
        String Stringi = (String) v.elementAt(0);
        getUserselectordata().setUsername(Stringi);
    }//GEN-LAST:event_tblUserListMouseClicked

private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed

    this.dispose();
}//GEN-LAST:event_cmdCancelActionPerformed

private void txtUserFilterKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUserFilterKeyReleased
    //  add your handling code here:
    String joo = txtUserFilter.getText().toLowerCase();
    usermodel.setRowCount(0);
    for (String foo : listArray) {
        foo = foo.toLowerCase();
        if (foo.contains(joo)) {
            Vector<String> kakkavektor = new Vector<String>();
            kakkavektor.add(foo);
            usermodel.addRow(kakkavektor);
        }
    }
    tblUserList.setModel(usermodel);
    tblUserList.validate();
}//GEN-LAST:event_txtUserFilterKeyReleased
    private ArrayList<String> listArray = new ArrayList<String>();
    private UserSelectorData userselectordata;
    private DefaultTableModel usermodel;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdQuery;
    private javax.swing.JButton cmdSelect;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblUserList;
    private javax.swing.JTextField txtUserFilter;
    // End of variables declaration//GEN-END:variables

    public UserSelectorData getUserselectordata() {
        return userselectordata;
    }

    public void setUserselectordata(UserSelectorData userselectordata) {
        this.userselectordata = userselectordata;
    }
}
