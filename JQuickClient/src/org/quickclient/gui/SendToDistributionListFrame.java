/*
 * SendToDistributionListFrame.java
 *
 * Created on 23. joulukuuta 2008, 1:14
 */
package org.quickclient.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;
import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.ExJTextArea;
import org.quickclient.classes.SwingHelper;
import org.quickclient.classes.UserorGroupSelectorData;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfWorkflow;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfList;
import com.documentum.fc.common.IDfId;
import com.documentum.fc.common.IDfList;


/**
 *
 * @author  Mika
 */
public class SendToDistributionListFrame extends javax.swing.JFrame {

    String objId = "";
    DefaultTableModel objectModel = null;
    DefaultTableModel userModel = null;
    Logger log = Logger.getLogger(SendToDistributionListFrame.class);

    /** Creates new form SendToDistributionListFrame */
    public SendToDistributionListFrame() {
        initComponents();
        this.jScrollPane1.getViewport().setBackground(Color.WHITE);
        this.jScrollPane3.getViewport().setBackground(Color.WHITE);
        objectModel = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        userModel = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        userModel.addColumn(".");
        userModel.addColumn("User Name");
        objectModel.addColumn(".");
        objectModel.addColumn("Object Name");
        objectModel.addColumn("id");

        tblFiles.setAutoCreateColumnsFromModel(true);
        tblFiles.setModel(objectModel);
        tblFiles.validate();

        tblUsers.setAutoCreateColumnsFromModel(true);
        tblUsers.setModel(userModel);
        tblUsers.validate();

        tblFiles.getColumnModel().getColumn(0).setCellRenderer(new FormatRenderer());
        tblUsers.getColumnModel().getColumn(0).setCellRenderer(new GroupOrUserRenderer());
        tblFiles.getColumnModel().removeColumn(tblFiles.getColumnModel().getColumn(2));

        TableColumn col = tblFiles.getColumnModel().getColumn(0);
        col.setPreferredWidth(22);
        col.setMaxWidth(22);
        col.setWidth(22);

        TableColumn col2 = tblUsers.getColumnModel().getColumn(0);
        col2.setPreferredWidth(22);
        col2.setMaxWidth(22);

    }

    public void setObjids(List<String> objids) {

        IDfSession session = DocuSessionManager.getInstance().getSession();
        for (int i = 0; i < objids.size(); i++) {
            String id = (String) objids.get(i);
            try {
                IDfSysObject obj = (IDfSysObject) session.getObject(new DfId(id));
                Vector v = new Vector();
                v.add(obj.getString("r_object_type") + "," + obj.getContentType());
                v.add(obj.getObjectName());
                v.add(id);
                objectModel.addRow(v);
            } catch (DfException ex) {
                log.error(ex);
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        chkSequentially = new javax.swing.JCheckBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtInstrustions = new ExJTextArea();
        chkAllowRejectToMe = new javax.swing.JCheckBox();
        chkAllowRejecttoPreviousSender = new javax.swing.JCheckBox();
        chkReturnToMe = new javax.swing.JCheckBox();
        chkRequireSignOff = new javax.swing.JCheckBox();
        cmbPriority = new javax.swing.JComboBox();
        cmdCancel = new javax.swing.JButton();
        cmdSend = new javax.swing.JButton();
        cmdRemove = new javax.swing.JButton();
        cmdAdd = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblFiles = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblUsers = new javax.swing.JTable();
        cmdUserGroupChoose = new javax.swing.JButton();
        cmdRemoveAccessor = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel2.setText("Priority:");

        jLabel3.setText("Instructions:");

        chkSequentially.setText("Sent to users sequentially");

        txtInstrustions.setColumns(20);
        txtInstrustions.setRows(5);
        jScrollPane2.setViewportView(txtInstrustions);

        chkAllowRejectToMe.setText("Allow reject to me");

        chkAllowRejecttoPreviousSender.setText("Allow reject to previous sender");

        chkReturnToMe.setText("Return to me");

        chkRequireSignOff.setText("Require signoff");

        cmbPriority.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Low", "Medium", "High" }));
        cmbPriority.setSelectedIndex(1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(chkRequireSignOff)
                    .addComponent(chkSequentially)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chkAllowRejecttoPreviousSender)
                            .addComponent(chkAllowRejectToMe)))
                    .addComponent(chkReturnToMe)
                    .addComponent(cmbPriority, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbPriority, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(chkSequentially)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkAllowRejectToMe)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(chkAllowRejecttoPreviousSender)
                        .addGap(6, 6, 6)
                        .addComponent(chkReturnToMe)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(chkRequireSignOff))
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cmdCancel.setText("Cancel");
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });

        cmdSend.setText("  Send  ");
        cmdSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSendActionPerformed(evt);
            }
        });

        cmdRemove.setText("Remove");

        cmdAdd.setText("   Add   ");

        tblFiles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "File"
            }
        ));
        jScrollPane1.setViewportView(tblFiles);

        tblUsers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Title 1", "Title 2"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tblUsers);

        cmdUserGroupChoose.setText("Add Performers");
        cmdUserGroupChoose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdUserGroupChooseActionPerformed(evt);
            }
        });

        cmdRemoveAccessor.setText("Remove");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(cmdUserGroupChoose)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmdRemoveAccessor))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, 0, 0, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(cmdAdd)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmdRemove)))
                        .addGap(10, 10, 10)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(cmdSend)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdCancel)
                        .addGap(22, 22, 22))))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cmdRemove, cmdRemoveAccessor});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cmdAdd, cmdUserGroupChoose});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmdRemove)
                            .addComponent(cmdAdd))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane3, 0, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmdRemoveAccessor)
                            .addComponent(cmdUserGroupChoose)))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdSend)
                    .addComponent(cmdCancel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSendActionPerformed

        IDfList idlist = new DfList();
        for (int i = 0; i < objectModel.getRowCount(); i++) {
            try {
                Vector v = (Vector) objectModel.getDataVector().elementAt(i);
                System.out.println(v);
                String x = (String) v.elementAt(2);
                idlist.appendId(new DfId(x));
            } catch (DfException ex) {
                log.error(ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
            }
        }

        IDfList userlist = new DfList();
        IDfList grouplist = new DfList();

        for (int j = 0; j < userModel.getRowCount(); j++) {
            try {
                Vector v = (Vector) userModel.getDataVector().elementAt(j);
                String isgroup = (String) v.elementAt(0);
                if (isgroup.equals("1")) {
                    String g = (String) v.elementAt(1);
                    grouplist.appendString(g);
                } else {
                    String u = (String) v.elementAt(1);
                    userlist.appendString(u);
                }
            } catch (DfException ex) {
                log.error(ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
            }
        }
        int flags = 0;
        int priority = 1;
        if (chkSequentially.isSelected()) {
            flags = flags + IDfWorkflow.DF_SEQUENTIAL;
        }
        if (chkAllowRejectToMe.isSelected()) {
            flags = flags + IDfWorkflow.DF_ALLOW_REJECT_INITIATOR;
        }
        if (chkAllowRejecttoPreviousSender.isSelected()) {
            flags = flags + IDfWorkflow.DF_ALLOW_REJECT_PREVIOUS;
        }
        if (chkRequireSignOff.isSelected()) {
            flags = flags + IDfWorkflow.DF_REQ_SIGN_OFF;
        }
        if (chkReturnToMe.isSelected()) {
            flags = flags + IDfWorkflow.DF_REQ_END_NOTIFICATION;
        }
        log.debug("flags: " + flags);

        if (cmbPriority.getSelectedIndex() == 0) {
            priority = 1;
        } else if (cmbPriority.getSelectedIndex() == 1) {
            priority = 5;
        } else if (cmbPriority.getSelectedIndex() == 2) {
            priority = 10;
        }

        IDfSession session = DocuSessionManager.getInstance().getSession();
        try {
        	System.out.println("userlist" + userlist);
        	System.out.println("grouplist" + grouplist);
        	System.out.println("" + txtInstrustions.getText());
        	System.out.println("idlist" + idlist);
        	System.out.println("priority" + priority);
        	System.out.println("flags" + flags );
            IDfId wfid = session.sendToDistributionListEx(userlist, grouplist, txtInstrustions.getText(), idlist, priority, flags);
            System.out.println(wfid);
        } catch (DfException ex) {
            log.error(ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
        }
        this.dispose();
    }//GEN-LAST:event_cmdSendActionPerformed

    private void cmdUserGroupChooseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdUserGroupChooseActionPerformed

        final UserorGroupSelectorData x = new UserorGroupSelectorData();
        ActionListener al = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Vector b = new Vector();
                if (x.isIsgroup()) {
                    b.add("1");
                } else {
                    b.add("0");
                }
                b.add(x.getUserorGroupname());
                userModel.addRow(b);

            }
        };
        UserorGroupSelectorFrame frame = new UserorGroupSelectorFrame(al, x);
        SwingHelper.centerJFrame(frame);
        frame.setVisible(true);


    }//GEN-LAST:event_cmdUserGroupChooseActionPerformed

    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        
        this.dispose();

    }//GEN-LAST:event_cmdCancelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chkAllowRejectToMe;
    private javax.swing.JCheckBox chkAllowRejecttoPreviousSender;
    private javax.swing.JCheckBox chkRequireSignOff;
    private javax.swing.JCheckBox chkReturnToMe;
    private javax.swing.JCheckBox chkSequentially;
    private javax.swing.JComboBox cmbPriority;
    private javax.swing.JButton cmdAdd;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdRemove;
    private javax.swing.JButton cmdRemoveAccessor;
    private javax.swing.JButton cmdSend;
    private javax.swing.JButton cmdUserGroupChoose;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tblFiles;
    private javax.swing.JTable tblUsers;
    private ExJTextArea txtInstrustions;
    // End of variables declaration//GEN-END:variables
}
