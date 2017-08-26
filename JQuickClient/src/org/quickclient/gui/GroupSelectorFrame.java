/*
 * GroupSelectorFrame.java
 *
 * Created on 5. marraskuuta 2006, 20:35
 */
package org.quickclient.gui;


import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;
import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.GroupSelectorData;

/**
 *
 * @author  Administrator
 */
public class GroupSelectorFrame extends javax.swing.JFrame {

    DocuSessionManager smanager;
    Logger log = Logger.getLogger(GroupSelectorFrame.class);
    /** Creates new form GroupSelectorFrame */
    public GroupSelectorFrame(ActionListener listener, GroupSelectorData data) {
        initComponents();
        smanager = DocuSessionManager.getInstance();
        cmdSelect.addActionListener(listener);
        this.groupdata = data;
        DefaultTableModel model = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        setGroupmodel(model);
        getGroupmodel().addColumn("User Name");
        tblGroupList.setModel(getGroupmodel());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtUserFilter = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cmdQuery = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblGroupList = new javax.swing.JTable();
        cmdSelect = new javax.swing.JButton();
        cmdCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Group Filter"));

        txtUserFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUserFilterActionPerformed(evt);
            }
        });

        jLabel1.setText("Group Name:");

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

        tblGroupList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Group Name"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblGroupList.setShowHorizontalLines(false);
        tblGroupList.setShowVerticalLines(false);
        tblGroupList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGroupListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblGroupList);

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
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 256, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cmdCancel)
                    .add(cmdSelect))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
// TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_cmdCancelActionPerformed

    private void txtUserFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUserFilterActionPerformed
// TODO add your handling code here:
        cmdQueryActionPerformed(evt);
    }//GEN-LAST:event_txtUserFilterActionPerformed

    private void tblGroupListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGroupListMouseClicked

        int row = tblGroupList.getSelectedRow();
        Vector v = (Vector) getGroupmodel().getDataVector().elementAt(row);
        ////System.out.println(v);
        String Stringi = (String) v.elementAt(0);
        ////System.out.println(Stringi);
        getGroupdata().setGroupName(Stringi);
    }//GEN-LAST:event_tblGroupListMouseClicked

    private void cmdSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSelectActionPerformed

        int row = tblGroupList.getSelectedRow();
        Vector v = (Vector) getGroupmodel().getDataVector().elementAt(row);
        ////System.out.println(v);
        String Stringi = (String) v.elementAt(0);
        getGroupdata().setGroupName(Stringi);
        this.dispose();
    }//GEN-LAST:event_cmdSelectActionPerformed

    private void cmdQueryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdQueryActionPerformed

        String queryFilter = txtUserFilter.getText();
        IDfCollection col = null;
        IDfQuery query = new DfQuery();
        if (queryFilter.length() > 0) {
            query.setDQL("select group_name from dm_group where group_name like '" + queryFilter + "%' order by group_name");
        } else {
            query.setDQL("select group_name from dm_group order by group_name");
        }

        IDfSession session = null;
        try {
            session = smanager.getSession();
            col = query.execute(session, IDfQuery.DF_READ_QUERY);
            while (col.next()) {
                Vector<Object> kakkavektor = new Vector<Object>();
                String username = col.getString("group_name");
                kakkavektor.add(username);
                groupmodel.addRow(kakkavektor);
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
        tblGroupList.validate();
        
    }//GEN-LAST:event_cmdQueryActionPerformed
    private GroupSelectorData groupdata;
    private DefaultTableModel groupmodel;

    public GroupSelectorData getGroupdata() {
        return groupdata;
    }

 

    public DefaultTableModel getGroupmodel() {
        return groupmodel;
    }

    public void setGroupmodel(DefaultTableModel groupmodel) {
        this.groupmodel = groupmodel;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdQuery;
    private javax.swing.JButton cmdSelect;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblGroupList;
    private javax.swing.JTextField txtUserFilter;
    // End of variables declaration//GEN-END:variables
}
