/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ExportDialog.java
 *
 * Created on 4.12.2009, 1:03:44
 */
package org.quickclient.gui;

import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * 
 * @author Mika
 */
public class ExportDialog extends javax.swing.JFrame {

	private ActionListener listener;

	/** Creates new form ExportDialog */
	public ExportDialog() {
		initComponents();
	}

	ExportData x;

	public ExportDialog(ActionListener listener, ExportData x) {
		this.x = x;
		this.listener = listener;
		initComponents();

	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jPanel1 = new javax.swing.JPanel();
		lblExportFolderName = new javax.swing.JLabel();
		jPanel2 = new javax.swing.JPanel();
		txtObjLimit = new javax.swing.JTextField();
		txtSizeLimit = new javax.swing.JTextField();
		jLabel4 = new javax.swing.JLabel();
		jLabel5 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		cmdObjCount = new javax.swing.JButton();
		cmdContentSize = new javax.swing.JButton();
		jPanel4 = new javax.swing.JPanel();
		chkRecurse = new javax.swing.JCheckBox();
		chkRendtitions = new javax.swing.JCheckBox();
		jLabel1 = new javax.swing.JLabel();
		txtExportDir = new javax.swing.JTextField();
		cdBrowse = new javax.swing.JButton();
		cmdExport = new javax.swing.JButton();
		cmdCancel = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

		lblExportFolderName.setText(" ");
		jPanel1.add(lblExportFolderName, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

		jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Export Limits"));

		txtObjLimit.setText(" ");
		txtObjLimit.setEnabled(false);

		txtSizeLimit.setText(" ");
		txtSizeLimit.setEnabled(false);

		jLabel4.setText("Megabytes");

		jLabel5.setText("Objects");

		jLabel2.setText("Limit object count to:");

		jLabel3.setText("Limit export size to:");

		cmdObjCount.setText("Test");
		cmdObjCount.setEnabled(false);

		cmdContentSize.setText("Test");
		cmdContentSize.setEnabled(false);

		javax.swing.GroupLayout gl_jPanel2 = new javax.swing.GroupLayout(jPanel2);
		jPanel2.setLayout(gl_jPanel2);
		gl_jPanel2.setHorizontalGroup(gl_jPanel2.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				gl_jPanel2
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_jPanel2.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel3))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(
								gl_jPanel2
										.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addGroup(
												gl_jPanel2.createSequentialGroup().addComponent(txtObjLimit, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
														.addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
										.addGroup(
												gl_jPanel2.createSequentialGroup().addComponent(txtSizeLimit, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
														.addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))).addGap(46, 46, 46)
						.addGroup(gl_jPanel2.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(cmdContentSize).addComponent(cmdObjCount)).addGap(31, 31, 31)));
		gl_jPanel2.setVerticalGroup(gl_jPanel2.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				gl_jPanel2
						.createSequentialGroup()
						.addGroup(
								gl_jPanel2.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(txtObjLimit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel5)
										.addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cmdObjCount))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(
								gl_jPanel2.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(txtSizeLimit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cmdContentSize))
						.addContainerGap()));

		jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 105, 440, 85));

		jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Options"));

		chkRecurse.setSelected(true);
		chkRecurse.setText("Export subfolders and contents");

		chkRendtitions.setText("Export renditions");
		chkRendtitions.setEnabled(false);
		chkRendtitions.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				chkRendtitionsActionPerformed(evt);
			}
		});

		jLabel1.setText("Target Folder:");

		txtExportDir.setText(" ");

		cdBrowse.setText("Browse");
		cdBrowse.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cdBrowseActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout gl_jPanel4 = new javax.swing.GroupLayout(jPanel4);
		jPanel4.setLayout(gl_jPanel4);
		gl_jPanel4.setHorizontalGroup(gl_jPanel4.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				gl_jPanel4
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								gl_jPanel4
										.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(chkRecurse)
										.addComponent(chkRendtitions)
										.addGroup(
												gl_jPanel4.createSequentialGroup().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txtExportDir, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
														.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(cdBrowse))).addContainerGap()));
		gl_jPanel4.setVerticalGroup(gl_jPanel4.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				gl_jPanel4
						.createSequentialGroup()
						.addGroup(
								gl_jPanel4.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(txtExportDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cdBrowse))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(chkRendtitions).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(chkRecurse).addGap(25, 25, 25)));

		jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 440, 105));

		cmdExport.setText("Export");
		cmdExport.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cmdExportActionPerformed(evt);
			}
		});
		jPanel1.add(cmdExport, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 200, -1, -1));

		cmdCancel.setText("Cancel");
		jPanel1.add(cmdCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 200, -1, -1));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE).addContainerGap()));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void chkRendtitionsActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_chkRendtitionsActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_chkRendtitionsActionPerformed

	private void cdBrowseActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cdBrowseActionPerformed

		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle("Folder export");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		chooser.setAcceptAllFileFilterUsed(false);
		//
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			this.txtExportDir.setText(chooser.getSelectedFile().toString());
		} else {
		}
	}// GEN-LAST:event_cdBrowseActionPerformed

	private void cmdExportActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdExportActionPerformed
		if (txtExportDir.getText().length() > 3) {
			File exportdir = new File(txtExportDir.getText());
			if (exportdir.exists() == false) {
				exportdir.mkdirs();
				JOptionPane.showMessageDialog(null, "New directory was created..", " ", JOptionPane.INFORMATION_MESSAGE);
			}
			x.setDeepexport(chkRecurse.isSelected());
			x.setExportDir(this.txtExportDir.getText());
			x.setExportrenditionds(chkRendtitions.isSelected());
			try {
				x.setMaxMegaBytes(Integer.parseInt(txtSizeLimit.getText()));
			} catch (NumberFormatException e) {
			}
			try {
				x.setMaxObjCount(Integer.parseInt(txtObjLimit.getText()));
			} catch (NumberFormatException e) {
			}
			listener.actionPerformed(evt);
			this.dispose();
		} else {
			JOptionPane.showMessageDialog(null, "Directory missing?", " ", JOptionPane.ERROR_MESSAGE);

		}
	}// GEN-LAST:event_cmdExportActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton cdBrowse;
	private javax.swing.JCheckBox chkRecurse;
	private javax.swing.JCheckBox chkRendtitions;
	private javax.swing.JButton cmdCancel;
	private javax.swing.JButton cmdContentSize;
	private javax.swing.JButton cmdExport;
	private javax.swing.JButton cmdObjCount;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JLabel lblExportFolderName;
	private javax.swing.JTextField txtExportDir;
	private javax.swing.JTextField txtObjLimit;
	private javax.swing.JTextField txtSizeLimit;
	// End of variables declaration//GEN-END:variables
}