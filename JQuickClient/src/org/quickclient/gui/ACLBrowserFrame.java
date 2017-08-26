/*
 * ACLBrowserFrame.java
 *
 * Created on 5. marraskuuta 2006, 14:15
 */
package org.quickclient.gui;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;
import org.quickclient.classes.AclBrowserData;
import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.DokuData;
import org.quickclient.classes.ExJTextField;
import org.quickclient.classes.SwingHelper;
import org.quickclient.classes.UserorGroupSelectorData;
import org.quickclient.classes.Utils;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfACL;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfUser;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLogger;
import com.documentum.fc.common.IDfId;

/**
 *
 * @author Administrator
 */
@SuppressWarnings("serial")
public class ACLBrowserFrame extends javax.swing.JFrame {

	DocuSessionManager smanager;
	Logger log = Logger.getLogger(ACLBrowserFrame.class);

	private AclBrowserData aclbrowserdata;

	private DefaultTableModel acllistmodel;

	private DefaultTableModel permissionlistmodel;

	private UserorGroupSelectorData userorgroupselectordata;

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.ButtonGroup buttonGroup1;

	private javax.swing.JCheckBox chkIncludeInternal;

	private javax.swing.JButton cmdClose;

	private javax.swing.JButton cmdEditACL;

	private javax.swing.JButton cmdNewACL;

	private javax.swing.JButton cmdQuery;

	private javax.swing.JButton cmdSelect;

	private javax.swing.JButton cmdSelectUserName;

	private javax.swing.JPanel jPanel1;

	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel4;

	private javax.swing.JScrollPane jScrollPane1;

	private javax.swing.JScrollPane jScrollPane2;

	private javax.swing.JRadioButton radAny;

	private javax.swing.JRadioButton radCurrentUser;

	private javax.swing.JRadioButton radSelectedUser;

	private javax.swing.JRadioButton radSystem;
	private javax.swing.JTable tblAclList;
	private javax.swing.JTable tblPermissionDetails;
	private ExJTextField txtACLFilter;
	private ExJTextField txtUserNameilter;
	private JComboBox acltypecombo;

	/** Creates new form ACLBrowserFrame */
	public ACLBrowserFrame() {
		initComponents();
		smanager = DocuSessionManager.getInstance();

	}

	public ACLBrowserFrame(final ActionListener actionlistener, final AclBrowserData data, final boolean isSelector) {
		initComponents();
		smanager = DocuSessionManager.getInstance();
		final UserorGroupSelectorData s = new UserorGroupSelectorData();
		// //System.out.println("UserorGroupSelectorData: " + s);
		setUserorgroupselectordata(s);
		cmdSelect.addActionListener(actionlistener);
		setAclbrowserdata(data);
		acllistmodel = new DefaultTableModel() {

			@Override
			public boolean isCellEditable(final int row, final int column) {
				return false;
			}
		};
		permissionlistmodel = new DefaultTableModel() {

			@Override
			public boolean isCellEditable(final int row, final int column) {
				return false;
			}
		};
		acllistmodel.addColumn("acl_name");
		acllistmodel.addColumn("Description");
		acllistmodel.addColumn("dokudata");
		tblAclList.setModel(acllistmodel);

		permissionlistmodel.addColumn(".");
		permissionlistmodel.addColumn("Accessor");
		permissionlistmodel.addColumn("Permit");
		permissionlistmodel.addColumn("Extended Permit");

		tblPermissionDetails.setAutoCreateColumnsFromModel(true);
		tblPermissionDetails.setModel(permissionlistmodel);

		if (isSelector) {
			cmdEditACL.setVisible(false);

		} else {
			cmdEditACL.setVisible(true);
			cmdSelect.setVisible(false);
		}
		jScrollPane1.getViewport().setBackground(Color.WHITE);
		jScrollPane2.getViewport().setBackground(Color.WHITE);
		tblPermissionDetails.getColumnModel().getColumn(0).setCellRenderer(new GroupOrUserRenderer());
		final TableColumn col = tblPermissionDetails.getColumnModel().getColumn(0);
		col.setPreferredWidth(22);
		col.setMaxWidth(22);
		col.setWidth(22);

		final TableColumn col1 = tblPermissionDetails.getColumnModel().getColumn(1);
		col1.setPreferredWidth(150);
		col1.setMaxWidth(150);
		final TableColumn col2 = tblPermissionDetails.getColumnModel().getColumn(2);
		col2.setPreferredWidth(100);
		col2.setMaxWidth(100);
	}

	public ACLBrowserFrame(final boolean isSelector) {
		initComponents();
		smanager = DocuSessionManager.getInstance();
		final UserorGroupSelectorData s = new UserorGroupSelectorData();
		// //System.out.println("UserorGroupSelectorData: " + s);
		setUserorgroupselectordata(s);
		acllistmodel = new DefaultTableModel() {

			@Override
			public boolean isCellEditable(final int row, final int column) {
				return false;
			}
		};
		permissionlistmodel = new DefaultTableModel() {

			@Override
			public boolean isCellEditable(final int row, final int column) {
				return false;
			}
		};
		acllistmodel.addColumn("acl_name");
		acllistmodel.addColumn("Description");
		acllistmodel.addColumn("dokudata");
		tblAclList.setModel(acllistmodel);

		permissionlistmodel.addColumn(".");
		permissionlistmodel.addColumn("Accessor");
		permissionlistmodel.addColumn("Permit");
		permissionlistmodel.addColumn("Extended Permit");

		tblPermissionDetails.setAutoCreateColumnsFromModel(true);
		tblPermissionDetails.setModel(permissionlistmodel);

		if (isSelector) {
			cmdEditACL.setVisible(false);

		} else {
			cmdEditACL.setVisible(true);
			cmdSelect.setVisible(false);
		}
		jScrollPane1.getViewport().setBackground(Color.WHITE);
		jScrollPane2.getViewport().setBackground(Color.WHITE);

		tblPermissionDetails.getColumnModel().getColumn(0).setCellRenderer(new GroupOrUserRenderer());
		final TableColumn col = tblPermissionDetails.getColumnModel().getColumn(0);
		col.setPreferredWidth(22);
		col.setMaxWidth(22);
		col.setWidth(22);

		final TableColumn col1 = tblPermissionDetails.getColumnModel().getColumn(1);
		col1.setPreferredWidth(150);
		col1.setMaxWidth(150);
		final TableColumn col2 = tblPermissionDetails.getColumnModel().getColumn(2);
		col2.setPreferredWidth(100);
		col2.setMaxWidth(100);

		final int lastIndex = tblAclList.getColumnCount();
		tblAclList.getColumnModel().removeColumn(tblAclList.getColumnModel().getColumn(lastIndex - 1));

	}

	private void cmdCloseActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdCloseActionPerformed
		this.dispose();
	}// GEN-LAST:event_cmdCloseActionPerformed

	private void cmdEditACLActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdEditACLActionPerformed
		IDfSession session = null;
		try {
			final int row = tblAclList.getSelectedRow();
			if (row == -1) {
				return;
			}
			session = smanager.getSession();
			final Vector v = (Vector) acllistmodel.getDataVector().elementAt(row);
			final DokuData d = (DokuData) v.elementAt(2);
			final String aclid = d.getObjID();
			final IDfId id = new DfId(aclid);
			final IDfACL acl = (IDfACL) session.getObject(id);
			final ACLEditorFrame frame = new ACLEditorFrame(acl);
			SwingHelper.centerJFrame(frame);
			frame.setVisible(true);
		} catch (final DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);

		} finally {
			if (session != null) {
				smanager.releaseSession(session);
			}
		}

	}// GEN-LAST:event_cmdEditACLActionPerformed

	private void cmdNewACLActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdNewACLActionPerformed

		smanager = DocuSessionManager.getInstance();
		final IDfSession session = smanager.getSession();
		IDfUser luser = null;
		try {
			luser = session.getUser(null);
			final String aclname = JOptionPane.showInputDialog("Acl name?");
			final IDfACL newacl = (IDfACL) session.newObject("dm_acl");
			newacl.setObjectName(aclname);
			if (luser.isSuperUser()) {
				newacl.setDomain("dm_dbo");
			} else {
				newacl.setDomain(luser.getUserLoginName());
			}
			newacl.save();
			final ACLEditorFrame frame = new ACLEditorFrame(newacl);
			SwingHelper.centerJFrame(frame);
			frame.setVisible(true);
		} catch (final DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
		}

	}// GEN-LAST:event_cmdNewACLActionPerformed

	private void cmdQueryActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdQueryActionPerformed
		acllistmodel.setNumRows(0);
		IDfCollection col = null;
		IDfSession session = null;
		try {
			session = smanager.getSession();
			String docbaseOnwer = "";
			docbaseOnwer = session.getDocbaseOwnerName();
			String aquery = "";
			final String aclFilter = txtACLFilter.getText();
			final String userFilter = txtUserNameilter.getText();

			final String value = acltypecombo.getSelectedItem().toString();
			String additionalpredicate = "";
			if (!value.equals("Any")) {
				if (value.equalsIgnoreCase("regular acl")) {
					additionalpredicate = "acl_class = 0 and";
				} else if (value.equalsIgnoreCase("template acl")) {
					additionalpredicate = "acl_class = 1 and";
				} else if (value.equalsIgnoreCase("template instance")) {
					additionalpredicate = "acl_class = 2 and";
				} else if (value.equalsIgnoreCase("public acl")) {
					additionalpredicate = "acl_class = 3 and";
				}
			}

			if (radSystem.isSelected()) {
				aquery = "select object_name, description, r_object_id from dm_acl where " + additionalpredicate + " object_name like '" + aclFilter + "%' and owner_name = '" + docbaseOnwer + "' order by object_name";
			}
			if (radCurrentUser.isSelected()) {
				aquery = "select object_name, description, r_object_id from dm_acl where " + additionalpredicate + " object_name like '" + aclFilter + "%' and owner_name = USER order by object_name";
			}
			if (radAny.isSelected()) {
				aquery = "select object_name, description, r_object_id from dm_acl where " + additionalpredicate + " object_name like '" + aclFilter + "%' order by object_name";
			}
			if (radSelectedUser.isSelected()) {
				aquery = "select object_name, description, r_object_id from dm_acl where " + additionalpredicate + " object_name like '" + aclFilter + "%' and owner_name = '" + userFilter + "' order by object_name";
			}

			final IDfQuery query = new DfQuery();

			query.setDQL(aquery);
			col = query.execute(session, IDfQuery.DF_READ_QUERY);
			while (col.next()) {
				final Vector<Object> joo = new Vector<Object>();
				joo.add(col.getString("object_name"));
				joo.add(col.getString("description"));
				final DokuData data = new DokuData(col.getString("r_object_id"));
				joo.add(data);
				acllistmodel.addRow(joo);
			}

		} catch (final DfException ex) {
			DfLogger.error(this, ex.getMessage(), null, ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);

		} finally {
			if (col != null) {
				try {
					col.close();
				} catch (final DfException ex) {
					DfLogger.error(this, ex.getMessage(), null, ex);
				}
			}
			if (session != null) {
				smanager.releaseSession(session);
			}
			tblAclList.validate();
		}

	}// GEN-LAST:event_cmdQueryActionPerformed

	private void cmdSelectActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdSelectActionPerformed

		this.dispose();
	}// GEN-LAST:event_cmdSelectActionPerformed

	private void cmdSelectUserNameActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdSelectUserNameActionPerformed
		final ActionListener a = e -> txtUserNameilter.setText(userorgroupselectordata.getUserorGroupname());
		// //System.out.println(this.userorgroupselectordata);
		final UserorGroupSelectorFrame frame = new UserorGroupSelectorFrame(a, this.getUserorgroupselectordata());
		SwingHelper.centerJFrame(frame);
		frame.setVisible(true);
	}// GEN-LAST:event_cmdSelectUserNameActionPerformed

	public AclBrowserData getAclbrowserdata() {
		return aclbrowserdata;
	}

	public UserorGroupSelectorData getUserorgroupselectordata() {
		return userorgroupselectordata;
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		buttonGroup1 = new javax.swing.ButtonGroup();
		jPanel1 = new javax.swing.JPanel();
		txtACLFilter = new ExJTextField();
		cmdQuery = new javax.swing.JButton();
		chkIncludeInternal = new javax.swing.JCheckBox();
		jPanel2 = new javax.swing.JPanel();
		radSystem = new javax.swing.JRadioButton();
		radCurrentUser = new javax.swing.JRadioButton();
		radAny = new javax.swing.JRadioButton();
		radAny.setSelected(true);
		radSelectedUser = new javax.swing.JRadioButton();
		txtUserNameilter = new ExJTextField();
		cmdSelectUserName = new javax.swing.JButton();
		jPanel3 = new javax.swing.JPanel();
		jScrollPane2 = new javax.swing.JScrollPane();
		tblPermissionDetails = new javax.swing.JTable();
		jPanel4 = new javax.swing.JPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		tblAclList = new javax.swing.JTable();
		cmdSelect = new javax.swing.JButton();
		cmdEditACL = new javax.swing.JButton();
		cmdClose = new javax.swing.JButton();
		cmdNewACL = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Permission Set Browser");
		setLocationByPlatform(true);

		jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("ACL Filter"));
		jPanel1.setName("ACL Frame"); // NOI18N

		cmdQuery.setText("Query");
		cmdQuery.setMargin(new java.awt.Insets(0, 8, 0, 8));
		cmdQuery.addActionListener(evt -> cmdQueryActionPerformed(evt));

		chkIncludeInternal.setText("Include Internal");
		chkIncludeInternal.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		chkIncludeInternal.setMargin(new java.awt.Insets(0, 0, 0, 0));

		acltypecombo = new JComboBox();
		acltypecombo.setModel(new DefaultComboBoxModel(new String[] { "Any", "Regular ACL", "Template ACL", "Template instance", "Public ACL" }));

		final JLabel lblNewLabel = new JLabel("Type:");

		final JLabel lblName = new JLabel("Name:");

		final GroupLayout gl_jPanel1 = new GroupLayout(jPanel1);
		gl_jPanel1.setHorizontalGroup(gl_jPanel1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_jPanel1.createSequentialGroup().addContainerGap().addGroup(gl_jPanel1.createParallelGroup(Alignment.LEADING).addComponent(lblNewLabel).addComponent(lblName)).addGap(22).addGroup(gl_jPanel1.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_jPanel1.createSequentialGroup().addComponent(acltypecombo, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(ComponentPlacement.RELATED).addComponent(chkIncludeInternal)).addGroup(gl_jPanel1.createSequentialGroup().addComponent(txtACLFilter, GroupLayout.PREFERRED_SIZE, 214, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(cmdQuery)))
						.addContainerGap()));
		gl_jPanel1.setVerticalGroup(gl_jPanel1.createParallelGroup(Alignment.LEADING).addGroup(gl_jPanel1.createSequentialGroup().addGroup(gl_jPanel1.createParallelGroup(Alignment.BASELINE).addComponent(txtACLFilter, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(lblName).addComponent(cmdQuery)).addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_jPanel1.createParallelGroup(Alignment.BASELINE).addComponent(chkIncludeInternal).addComponent(acltypecombo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(lblNewLabel)).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		jPanel1.setLayout(gl_jPanel1);

		jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("ACL Owner"));

		buttonGroup1.add(radSystem);
		radSystem.setText("System");
		radSystem.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		radSystem.setMargin(new java.awt.Insets(0, 0, 0, 0));

		buttonGroup1.add(radCurrentUser);
		radCurrentUser.setText("Current User");
		radCurrentUser.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		radCurrentUser.setMargin(new java.awt.Insets(0, 0, 0, 0));

		buttonGroup1.add(radAny);
		radAny.setText("Any");
		radAny.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		radAny.setMargin(new java.awt.Insets(0, 0, 0, 0));

		buttonGroup1.add(radSelectedUser);
		radSelectedUser.setText("Selected User");
		radSelectedUser.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		radSelectedUser.setMargin(new java.awt.Insets(0, 0, 0, 0));

		cmdSelectUserName.setText("Select");
		cmdSelectUserName.setMargin(new java.awt.Insets(0, 8, 0, 8));
		cmdSelectUserName.addActionListener(evt -> cmdSelectUserNameActionPerformed(evt));

		final org.jdesktop.layout.GroupLayout gl_jPanel2 = new org.jdesktop.layout.GroupLayout(jPanel2);
		jPanel2.setLayout(gl_jPanel2);
		gl_jPanel2.setHorizontalGroup(gl_jPanel2.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, gl_jPanel2.createSequentialGroup().addContainerGap().add(gl_jPanel2.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(org.jdesktop.layout.GroupLayout.LEADING, txtUserNameilter, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
				.add(org.jdesktop.layout.GroupLayout.LEADING, radSystem).add(org.jdesktop.layout.GroupLayout.LEADING, radCurrentUser).add(org.jdesktop.layout.GroupLayout.LEADING, radAny).add(org.jdesktop.layout.GroupLayout.LEADING, gl_jPanel2.createSequentialGroup().add(radSelectedUser).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 199, Short.MAX_VALUE).add(cmdSelectUserName))).addContainerGap()));
		gl_jPanel2.setVerticalGroup(gl_jPanel2.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(gl_jPanel2.createSequentialGroup().add(radSystem).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(radCurrentUser).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(radAny).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
				.add(gl_jPanel2.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(radSelectedUser).add(cmdSelectUserName)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(txtUserNameilter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap()));

		jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Permission Set Details"));

		jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));

		tblPermissionDetails.setAutoCreateColumnsFromModel(false);
		tblPermissionDetails.setAutoCreateRowSorter(true);
		tblPermissionDetails.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null } }, new String[] { "Accessor", "Permit", "Extended permit" }));
		tblPermissionDetails.setGridColor(new java.awt.Color(0, 0, 0));
		tblPermissionDetails.setShowHorizontalLines(false);
		jScrollPane2.setViewportView(tblPermissionDetails);

		final org.jdesktop.layout.GroupLayout gl_jPanel3 = new org.jdesktop.layout.GroupLayout(jPanel3);
		jPanel3.setLayout(gl_jPanel3);
		gl_jPanel3.setHorizontalGroup(gl_jPanel3.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(gl_jPanel3.createSequentialGroup().addContainerGap().add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 816, Short.MAX_VALUE).addContainerGap()));
		gl_jPanel3.setVerticalGroup(gl_jPanel3.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(gl_jPanel3.createSequentialGroup().add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE).addContainerGap()));

		jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
		jScrollPane1.setOpaque(false);

		tblAclList.setAutoCreateRowSorter(true);
		tblAclList.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

		}, new String[] { "ACL Name", "Description" }));
		tblAclList.setGridColor(new java.awt.Color(0, 0, 0));
		tblAclList.setShowHorizontalLines(false);
		tblAclList.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseReleased(final java.awt.event.MouseEvent evt) {
				tblAclListMouseReleased(evt);
			}
		});
		tblAclList.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyReleased(final java.awt.event.KeyEvent evt) {
				tblAclListKeyReleased(evt);
			}
		});
		jScrollPane1.setViewportView(tblAclList);

		final org.jdesktop.layout.GroupLayout gl_jPanel4 = new org.jdesktop.layout.GroupLayout(jPanel4);
		jPanel4.setLayout(gl_jPanel4);
		gl_jPanel4.setHorizontalGroup(gl_jPanel4.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE));
		gl_jPanel4.setVerticalGroup(gl_jPanel4.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(gl_jPanel4.createSequentialGroup().addContainerGap().add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)));

		cmdSelect.setMnemonic('s');
		cmdSelect.setText("Select");
		cmdSelect.addActionListener(evt -> cmdSelectActionPerformed(evt));

		cmdEditACL.setMnemonic('e');
		cmdEditACL.setText("Edit");
		cmdEditACL.addActionListener(evt -> cmdEditACLActionPerformed(evt));

		cmdClose.setText("Close");
		cmdClose.addActionListener(evt -> cmdCloseActionPerformed(evt));

		cmdNewACL.setText("Create New Acl");
		cmdNewACL.addActionListener(evt -> cmdNewACLActionPerformed(evt));

		final org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(layout.createSequentialGroup().addContainerGap()
						.add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(org.jdesktop.layout.GroupLayout.LEADING, jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.add(layout.createSequentialGroup().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
								.addContainerGap()).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().add(10, 10, 10).add(cmdNewACL).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 509, Short.MAX_VALUE).add(cmdEditACL).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(cmdSelect).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(cmdClose).add(29, 29, 29)))));

		layout.linkSize(new java.awt.Component[] { cmdClose, cmdEditACL, cmdSelect }, org.jdesktop.layout.GroupLayout.HORIZONTAL);

		layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(layout.createSequentialGroup().addContainerGap()
						.add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
								.add(layout.createSequentialGroup().add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
										org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
								.add(jPanel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(cmdSelect).add(cmdEditACL).add(cmdClose).add(cmdNewACL)).addContainerGap()));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	public void setAclbrowserdata(final AclBrowserData aclbrowserdata) {
		this.aclbrowserdata = aclbrowserdata;
	}

	public void setUserorgroupselectordata(final UserorGroupSelectorData userorgroupselectordata) {
		this.userorgroupselectordata = userorgroupselectordata;
	}

	private void tblAclListKeyReleased(final java.awt.event.KeyEvent evt) {// GEN-FIRST:event_tblAclListKeyReleased
		// TODO add your handling code here:
		this.updatePermissionList();
	}// GEN-LAST:event_tblAclListKeyReleased

	private void tblAclListMouseReleased(final java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tblAclListMouseReleased

		this.updatePermissionList();

	}// GEN-LAST:event_tblAclListMouseReleased

	private void updatePermissionList() {
		String aclname = "";
		String aclDomain = "";
		IDfSession session = null;
		try {
			session = smanager.getSession();
			permissionlistmodel.setRowCount(0);
			final int row = tblAclList.getSelectedRow();
			final Vector v = (Vector) acllistmodel.getDataVector().elementAt(row);
			final DokuData d = (DokuData) v.elementAt(2);
			final String aclid = d.getObjID();
			final IDfId id = new DfId(aclid);
			final IDfACL acl = (IDfACL) session.getObject(id);
			aclname = acl.getObjectName();
			aclDomain = acl.getString("owner_name");
			boolean i_has_required_groups = false;
			boolean i_has_required_group_set = false;
			i_has_required_groups = acl.getBoolean("i_has_required_groups");
			i_has_required_group_set = acl.getBoolean("i_has_required_group_set");
			final int accessorcount = acl.getAccessorCount();
			final Utils utils = new Utils();
			for (int i = 0; i < accessorcount; i++) {
				final Vector<Object> jeps = new Vector<Object>();

				final boolean isgroup = acl.getRepeatingBoolean("r_is_group", i);
				if (isgroup) {
					jeps.add("1");
				} else {
					jeps.add("0");
				}

				jeps.add(acl.getRepeatingString("r_accessor_name", i));
				final int permit = acl.getRepeatingInt("r_accessor_permit", i);
				final int permittype = acl.getRepeatingInt("r_permit_type", i);
				if (permittype == 0) {
					jeps.add(utils.intToPermit(permit));
					jeps.add(acl.getAccessorXPermitNames(i));
				} else if (permittype == 1) {
					jeps.add("Extended Permit");
					jeps.add(acl.getAccessorXPermitNames(i));
				} else if (permittype == 2) {
					jeps.add("Application Permit");
					jeps.add("");
				} else if (permittype == 3) {
					jeps.add("Access Restriction");
					jeps.add("");
				} else if (permittype == 4) {
					jeps.add("Extended Restriction");
					jeps.add(acl.getAccessorXPermitNames(i));
				} else if (permittype == 5) {
					jeps.add("Application Restriction");
					jeps.add("");
				} else if (permittype == 6) {
					jeps.add("Required Group");
					jeps.add("");
				} else if (permittype == 7) {
					jeps.add("Required Group Set");
					jeps.add("");
				}
				permissionlistmodel.addRow(jeps);
			}
		} catch (final DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);

		} finally {
			tblPermissionDetails.validate();
			if (session != null) {
				smanager.releaseSession(session);
			}
		}
		if (aclbrowserdata != null) {
			aclbrowserdata.setAclDomain(aclDomain);
			aclbrowserdata.setAclName(aclname);
		}

	}
}
