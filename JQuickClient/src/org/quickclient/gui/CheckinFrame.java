/*
 * CheckinFrame.java
 *
 * Created on 26. marraskuuta 2007, 23:20
 */
package org.quickclient.gui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import org.apache.log4j.Logger;
import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.ExJTextArea;
import org.quickclient.classes.OperationMonitor;

import com.documentum.com.DfClientX;
import com.documentum.com.IDfClientX;
import com.documentum.fc.client.DfVersionPolicy;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfVersionLabels;
import com.documentum.fc.client.IDfVersionPolicy;
import com.documentum.fc.client.IDfVirtualDocument;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.IDfId;
import com.documentum.operations.IDfCheckinNode;
import com.documentum.operations.IDfCheckinOperation;
import com.documentum.operations.inbound.DfCheckinOperation;

/**
 *
 * @author Administrator
 */
public class CheckinFrame extends javax.swing.JFrame {

	private static final long serialVersionUID = 1L;
	private IDfId id;
	DocuSessionManager smanager;
	private JTable table;
	Logger log = Logger.getLogger(CheckinFrame.class);
	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.ButtonGroup buttonGroup1;
	private javax.swing.JCheckBox chkKeepLocalFile;
	private javax.swing.JCheckBox chkRetainLock;
	private javax.swing.JButton cmdCancel;
	private javax.swing.JButton cmdCheckin;
	private javax.swing.JButton cmdCheckinFromFile;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JLabel lblBranch;
	private javax.swing.JLabel lblMajor;
	private javax.swing.JLabel lblMinor;
	private javax.swing.JLabel lblSame;
	private javax.swing.JRadioButton rdBranch;
	private javax.swing.JRadioButton rdMajor;
	private javax.swing.JRadioButton rdMinor;
	private javax.swing.JRadioButton rdSame;
	private ExJTextArea txtDescription;
	private javax.swing.JTextField txtFileCheckin;
	private javax.swing.JTextField txtVersionLabel;

	// End of variables declaration//GEN-END:variables
	/** Creates new form CheckinFrame */
	public CheckinFrame() {
		initComponents();
		smanager = DocuSessionManager.getInstance();
		initOwn();
	}

	public CheckinFrame(final IDfId id, final JTable tbl) {
		initComponents();
		this.table = tbl;
		smanager = DocuSessionManager.getInstance();
		this.id = id;
		initOwn();
	}

	private void cmdCancelActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdCancelActionPerformed
		this.dispose();
	}// GEN-LAST:event_cmdCancelActionPerformed

	private void cmdCheckinActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdCheckinActionPerformed
		IDfSession session = null;
		try {
			session = smanager.getSession();
			IDfCheckinNode node;
			final IDfSysObject obj = (IDfSysObject) session.getObject(id);
			final IDfClientX clientx = new DfClientX();
			final IDfCheckinOperation operation = clientx.getCheckinOperation();
			operation.setOperationMonitor(new OperationMonitor());
			if (chkKeepLocalFile.isSelected()) {
				operation.setKeepLocalFile(true);
			}
			if (chkRetainLock.isSelected()) {
				operation.setRetainLock(true);
			}
			if (obj.isVirtualDocument()) {
				final IDfVirtualDocument vDoc = obj.asVirtualDocument("CURRENT", false);
				node = (IDfCheckinNode) operation.add(vDoc);
			} else {
				node = (IDfCheckinNode) operation.add(obj);
			}
			if (txtVersionLabel.getText().length() > 0) {
				node.setVersionLabels(txtVersionLabel.getText());
			}
			if (txtFileCheckin.getText().length() > 3) {
				node.setFilePath(txtFileCheckin.getText());
			}
			if (rdMinor.isSelected()) {
				operation.setCheckinVersion(DfCheckinOperation.NEXT_MINOR);
			}
			if (rdMajor.isSelected()) {
				operation.setCheckinVersion(DfCheckinOperation.NEXT_MINOR);
			}
			if (rdSame.isSelected()) {
				operation.setCheckinVersion(DfCheckinOperation.SAME_VERSION);
			}
			if (rdBranch.isSelected()) {
				operation.setCheckinVersion(DfCheckinOperation.BRANCH_VERSION);
			}

			operation.execute();
			if (table != null) {
				final int row = table.getSelectedRow();
				table.setValueAt("", row, 0);
				table.validate();
			}
		} catch (final DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
		} finally {
			if (session != null) {
				smanager.releaseSession(session);
			}
		}
		this.dispose();
	}// GEN-LAST:event_cmdCheckinActionPerformed

	private void cmdCheckinFromFileActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdCheckinFromFileActionPerformed
		final String exportName = "";
		File selFile;
		final JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("File to Checkin");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		// chooser.setSelectedFile(new File(exportName));
		final int returnVal = chooser.showOpenDialog(this);
		selFile = chooser.getSelectedFile();
		txtFileCheckin.setText(selFile.toString());
	}

	private void initComponents() {

		buttonGroup1 = new javax.swing.ButtonGroup();
		jPanel1 = new javax.swing.JPanel();
		cmdCheckin = new javax.swing.JButton();
		cmdCancel = new javax.swing.JButton();
		jPanel2 = new javax.swing.JPanel();
		rdMinor = new javax.swing.JRadioButton();
		rdMajor = new javax.swing.JRadioButton();
		rdSame = new javax.swing.JRadioButton();
		rdBranch = new javax.swing.JRadioButton();
		lblMinor = new javax.swing.JLabel();
		lblMajor = new javax.swing.JLabel();
		lblSame = new javax.swing.JLabel();
		lblBranch = new javax.swing.JLabel();
		jPanel3 = new javax.swing.JPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		txtDescription = new ExJTextArea();
		jPanel4 = new javax.swing.JPanel();
		chkRetainLock = new javax.swing.JCheckBox();
		chkKeepLocalFile = new javax.swing.JCheckBox();
		cmdCheckinFromFile = new javax.swing.JButton();
		txtFileCheckin = new javax.swing.JTextField();
		txtVersionLabel = new javax.swing.JTextField();
		jLabel2 = new javax.swing.JLabel();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		cmdCheckin.setText("Checkin");
		cmdCheckin.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdCheckinActionPerformed(evt);
			}
		});

		cmdCancel.setText("Cancel");
		cmdCancel.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdCancelActionPerformed(evt);
			}
		});

		final org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup().addContainerGap(433, Short.MAX_VALUE).add(cmdCheckin).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(cmdCancel).addContainerGap()));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(jPanel1Layout.createSequentialGroup().addContainerGap().add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(cmdCancel).add(cmdCheckin, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Checkin as Version"));

		buttonGroup1.add(rdMinor);
		rdMinor.setText("Minor Version");

		buttonGroup1.add(rdMajor);
		rdMajor.setText("Major Version");

		buttonGroup1.add(rdSame);
		rdSame.setText("Same Version");

		buttonGroup1.add(rdBranch);
		rdBranch.setText("Branch Version");

		lblMinor.setText("jLabel3");

		lblMajor.setText("jLabel4");

		lblSame.setText("jLabel5");

		lblBranch.setText("jLabel6");

		final org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().add(rdMinor).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 113, Short.MAX_VALUE).add(lblMinor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 43, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
						.add(jPanel2Layout.createSequentialGroup().add(rdMajor).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 110, Short.MAX_VALUE).add(lblMajor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 44, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
						.add(jPanel2Layout.createSequentialGroup().add(rdSame).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 112, Short.MAX_VALUE).add(lblSame, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 44, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
						.add(jPanel2Layout.createSequentialGroup().add(rdBranch).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 103, Short.MAX_VALUE).add(lblBranch, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 45, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))));
		jPanel2Layout
				.setVerticalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(rdMinor).add(lblMinor)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(lblMajor).add(rdMajor))
						.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(lblSame).add(rdSame)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(lblBranch).add(rdBranch)).addContainerGap(15, Short.MAX_VALUE)));

		jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Description of Changes"));

		txtDescription.setColumns(20);
		txtDescription.setRows(5);
		jScrollPane1.setViewportView(txtDescription);

		final org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
		jPanel3.setLayout(jPanel3Layout);
		jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE));
		jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE));

		jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

		chkRetainLock.setText("Retain Lock");

		chkKeepLocalFile.setText("Keep Local file");

		cmdCheckinFromFile.setText("Checkin from File:");
		cmdCheckinFromFile.setMargin(new java.awt.Insets(2, 2, 2, 2));
		cmdCheckinFromFile.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdCheckinFromFileActionPerformed(evt);
			}
		});

		txtVersionLabel.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				txtVersionLabelActionPerformed(evt);
			}
		});

		jLabel2.setText("Version Label:");

		final org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
		jPanel4.setLayout(jPanel4Layout);
		jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(jPanel4Layout.createSequentialGroup().addContainerGap()
						.add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
								.add(jPanel4Layout.createSequentialGroup().add(chkKeepLocalFile, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 288, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(chkRetainLock, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 198, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
								.add(jPanel4Layout.createSequentialGroup().add(cmdCheckinFromFile).add(18, 18, 18).add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false).add(txtFileCheckin).add(txtVersionLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE))))
						.add(3, 3, 3))
				.add(jPanel4Layout.createSequentialGroup().add(16, 16, 16).add(jLabel2).addContainerGap(492, Short.MAX_VALUE)));
		jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING,
				jPanel4Layout.createSequentialGroup().addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel2).add(txtVersionLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(cmdCheckinFromFile).add(txtFileCheckin, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
						.add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(chkKeepLocalFile).add(chkRetainLock)).add(30, 30, 30)));

		final org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(layout.createSequentialGroup().addContainerGap()
						.add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.add(layout.createSequentialGroup().add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).add(jPanel4, 0, 587,
										Short.MAX_VALUE))
						.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(layout.createSequentialGroup().addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 103, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void initOwn() {
		rdMinor.setSelected(true);
		final IDfCollection col = null;
		IDfSession session = null;
		try {
			session = smanager.getSession();
			final IDfSysObject obj = (IDfSysObject) session.getObject(id);
			final IDfVersionPolicy versionpolicy = obj.getVersionPolicy();

			final IDfVersionLabels labels = obj.getVersionLabels();
			final String versionLabel = labels.getImplicitVersionLabel();
			if (versionpolicy.canVersion(DfVersionPolicy.DF_SAME_VERSION)) {
				lblSame.setText(versionpolicy.getSameLabel());
			} else {
				lblSame.setText("N/A");
				rdSame.setEnabled(false);
			}
			if (versionpolicy.canVersion(DfVersionPolicy.DF_NEXT_MINOR)) {
				lblMinor.setText(versionpolicy.getNextMinorLabel());
			} else {
				lblMinor.setText("N/A");
				rdMinor.setEnabled(false);
			}
			if (versionpolicy.canVersion(DfVersionPolicy.DF_BRANCH_VERSION)) {
				lblBranch.setText(versionpolicy.getBranchLabel());
			} else {
				lblBranch.setText("N/A");
				rdBranch.setEnabled(false);
			}
			if (versionpolicy.canVersion(DfVersionPolicy.DF_NEXT_MAJOR)) {
				lblMajor.setText(versionpolicy.getNextMajorLabel());
			} else {
				lblMajor.setText("N/A");
				rdMajor.setEnabled(false);
			}

		} catch (final DfException ex) {
			log.error(ex);
		} finally {
			if (col != null) {
				try {
					col.close();
				} catch (final DfException ex) {
					log.error(ex);
				}
			}
			if (session != null) {
				smanager.releaseSession(session);
			}
		}
	}

	private void txtVersionLabelActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtVersionLabelActionPerformed

	}
}
