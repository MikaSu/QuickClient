/*
 * UsergroupManagementFrame.java
 *
 * Created on 26. lokakuuta 2006, 21:40
 */
package org.quickclient.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import org.quickclient.classes.AclBrowserData;
import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.DokuData;
import org.quickclient.classes.ExJTextArea;
import org.quickclient.classes.ExJTextField;
import org.quickclient.classes.GroupSelectorData;
import org.quickclient.classes.GroupsinGroupData;
import org.quickclient.classes.SwingHelper;
import org.quickclient.classes.UserSelectorData;
import org.quickclient.classes.UsersInGroupData;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.DfUser;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfFolder;
import com.documentum.fc.client.IDfGroup;
import com.documentum.fc.client.IDfPersistentObject;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfUser;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLogger;
import com.documentum.fc.common.IDfId;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

/**
 *
 * @author Administrator
 */
public class UsergroupManagementFrame extends javax.swing.JFrame {

	DocuSessionManager smanager;
	Logger log = Logger.getLogger(UsergroupManagementFrame.class);

	/**
	 * @param args
	 *            the command line arguments
	 */
	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JCheckBox chkGroupisPrivate;

	private javax.swing.JCheckBox chkIsDymanic;

	private javax.swing.JCheckBox chkUserActive;

	private javax.swing.JCheckBox chkWFEnabled;

	private javax.swing.JComboBox cmbAliasSet;

	private javax.swing.JComboBox cmbClientCapability;

	private javax.swing.JComboBox cmbGroupClass;

	private javax.swing.JComboBox cmbGroupSource;

	private javax.swing.JComboBox cmbUserAliasSet;

	private javax.swing.JComboBox cmbUserPrivileges;

	private javax.swing.JComboBox cmbuserSource;

	private javax.swing.JButton cmdDeleteGroup;

	private javax.swing.JButton cmdDeleteUser;

	private javax.swing.JButton cmdGroupQuery;

	private javax.swing.JButton cmdNewGroup;

	private javax.swing.JButton cmdNewUser;

	private javax.swing.JButton cmdRenameGroup;

	private javax.swing.JButton cmdSaveGroup;

	private javax.swing.JButton cmdSearchUser;

	private javax.swing.JButton cmdSelectACL;

	private javax.swing.JButton cmdSelectDefaultFolder;

	private javax.swing.JButton cmdSelectGroups;

	private javax.swing.JButton cmdSelectUsers;

	private javax.swing.JButton cmdUserRename;

	private javax.swing.JButton cmdUserSelectDefaultGroup;

	private javax.swing.JButton cmdValidate;

	private javax.swing.JButton cmdviewGroupRenameLog;

	private javax.swing.JButton cmdviewUserRenameLog;

	private javax.swing.JTable groupTable;

	private javax.swing.JButton jButton1;

	private javax.swing.JButton jButton10;

	private javax.swing.JButton jButton2;

	private javax.swing.JButton jButton9;

	private javax.swing.JLabel jLabel1;

	private javax.swing.JLabel jLabel10;

	private javax.swing.JLabel jLabel11;

	private javax.swing.JLabel jLabel12;

	private javax.swing.JLabel jLabel13;

	private javax.swing.JLabel jLabel14;

	private javax.swing.JLabel jLabel15;

	private javax.swing.JLabel jLabel16;
	private javax.swing.JLabel jLabel17;
	private javax.swing.JLabel jLabel18;
	private javax.swing.JLabel jLabel19;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel20;
	private javax.swing.JLabel jLabel21;
	private javax.swing.JLabel jLabel22;
	private javax.swing.JLabel jLabel23;
	private javax.swing.JLabel jLabel24;
	private javax.swing.JLabel jLabel25;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JLabel jLabel8;
	private javax.swing.JLabel jLabel9;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel10;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JPanel jPanel5;
	private javax.swing.JPanel jPanel6;
	private javax.swing.JPanel jPanel7;
	private javax.swing.JPanel jPanel8;
	private javax.swing.JPanel jPanel9;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JScrollPane jScrollPane3;
	private javax.swing.JScrollPane jScrollPane4;
	private javax.swing.JScrollPane jScrollPane5;
	private javax.swing.JScrollPane jScrollPane6;
	private javax.swing.JScrollPane jScrollPane7;
	private javax.swing.JTabbedPane jTabbedPane1;
	private javax.swing.JList lstGroups;
	private javax.swing.JList lstUsers;
	private javax.swing.JMenuItem mnuDeleteGroupRename;
	private javax.swing.JMenuItem mnuDeleteUserRename;
	private javax.swing.JMenuItem mnuViewGroupRename;
	private javax.swing.JMenuItem mnuViewUserRename;
	private javax.swing.JPopupMenu popupGruopRename;
	private javax.swing.JPopupMenu popupUserRename;
	private javax.swing.JTable tblGroupRename;
	private javax.swing.JTable tblUserRename;
	private javax.swing.JTextField txtDefaultFolder;
	private javax.swing.JTextField txtDefaultGroup;
	private javax.swing.JTextField txtGroupAddress;
	private ExJTextArea txtGroupDescription;
	private javax.swing.JTextField txtGroupFilter;
	private javax.swing.JTextField txtGroupName;
	private javax.swing.JTextField txtGroupOwner;
	private javax.swing.JTextField txtUserACL;
	private javax.swing.JTextField txtUserDbName;
	private javax.swing.JTextField txtUserDescription;
	private javax.swing.JTextField txtUserFilter;
	private javax.swing.JTextField txtUserName;
	private javax.swing.JTextField txtUserOsDomain;
	private javax.swing.JTextField txtUserOsName;
	private javax.swing.JTextField txtUserPassword;
	private javax.swing.JTable userTable;
	// End of variables declaration//GEN-END:variables
	private DefaultTableModel usertablemodel;
	private FolderSelectorData folderselectordata;
	private DefaultTableModel grouptablemodel;
	private DefaultListModel userlistmodel;
	private DefaultListModel grouplistmodel;
	private AclBrowserData acldata;
	private GroupSelectorData groupselectordata;
	private UserSelectorData userselectordata;
	private DefaultTableModel grouprenamemodel;
	private DefaultTableModel userrenamemodel;
	private JLabel label;
	private ExJTextField txtUserAddress;
	private JLabel label_1;
	private ExJTextField txtLoginName;

	/**
	 * Creates new form UsergroupManagementFrame
	 */
	public UsergroupManagementFrame() {
		this.setTitle("User / Group Management.");
		initComponents();
		smanager = DocuSessionManager.getInstance();
		this.jScrollPane1.getViewport().setBackground(Color.WHITE);
		this.jScrollPane2.getViewport().setBackground(Color.WHITE);
		this.jScrollPane3.getViewport().setBackground(Color.WHITE);
		this.jScrollPane4.getViewport().setBackground(Color.WHITE);

		userrenamemodel = new DefaultTableModel() {

			@Override
			public boolean isCellEditable(final int row, final int column) {
				return false;
			}
		};
		grouprenamemodel = new DefaultTableModel() {

			@Override
			public boolean isCellEditable(final int row, final int column) {
				return false;
			}
		};

		grouprenamemodel.addColumn("Old Name");
		grouprenamemodel.addColumn("New Name");
		grouprenamemodel.addColumn("Creation Date");
		grouprenamemodel.addColumn("Modify Date");
		grouprenamemodel.addColumn("id");

		tblGroupRename.setModel(grouprenamemodel);
		tblUserRename.setModel(userrenamemodel);

		userlistmodel = new DefaultListModel();

		lstUsers.setModel(userlistmodel);
		lstUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		grouplistmodel = new DefaultListModel();
		lstGroups.setModel(grouplistmodel);
		lstGroups.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		final GroupLayout gl_jPanel10 = new GroupLayout(jPanel10);
		gl_jPanel10.setHorizontalGroup(gl_jPanel10.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_jPanel10.createSequentialGroup()
						.addGroup(gl_jPanel10.createParallelGroup(Alignment.LEADING).addGroup(gl_jPanel10.createSequentialGroup().addGap(79).addGroup(gl_jPanel10.createParallelGroup(Alignment.LEADING).addComponent(chkGroupisPrivate).addComponent(chkIsDymanic)))
								.addGroup(gl_jPanel10.createSequentialGroup().addGap(30).addComponent(jLabel22).addGap(4).addComponent(cmbAliasSet, GroupLayout.PREFERRED_SIZE, 269, GroupLayout.PREFERRED_SIZE)).addGroup(gl_jPanel10.createSequentialGroup().addGap(18).addComponent(jLabel21).addGap(4).addComponent(jScrollPane7, GroupLayout.PREFERRED_SIZE, 269, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_jPanel10.createSequentialGroup().addGap(39).addComponent(jLabel19).addGap(4).addComponent(txtGroupOwner, GroupLayout.PREFERRED_SIZE, 233, GroupLayout.PREFERRED_SIZE).addGap(6).addComponent(jButton10, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_jPanel10.createSequentialGroup().addGap(37).addComponent(jLabel18).addGap(4).addComponent(jScrollPane6, GroupLayout.PREFERRED_SIZE, 233, GroupLayout.PREFERRED_SIZE).addGap(6).addComponent(cmdSelectGroups, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_jPanel10.createSequentialGroup().addGap(44).addComponent(jLabel20).addGap(4).addComponent(jScrollPane5, GroupLayout.PREFERRED_SIZE, 233, GroupLayout.PREFERRED_SIZE).addGap(6).addComponent(cmdSelectUsers, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_jPanel10.createSequentialGroup().addComponent(jLabel17).addGap(4).addComponent(txtGroupAddress, GroupLayout.PREFERRED_SIZE, 269, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_jPanel10.createSequentialGroup().addGap(1).addComponent(jLabel16, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE).addGap(4).addComponent(cmbGroupClass, GroupLayout.PREFERRED_SIZE, 269, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_jPanel10.createSequentialGroup().addGap(6).addComponent(jLabel25).addGap(4).addComponent(cmbGroupSource, GroupLayout.PREFERRED_SIZE, 269, GroupLayout.PREFERRED_SIZE)).addGroup(gl_jPanel10.createSequentialGroup().addGap(12).addComponent(jLabel15).addGap(4).addComponent(txtGroupName, GroupLayout.PREFERRED_SIZE, 269, GroupLayout.PREFERRED_SIZE)))
						.addGap(28)));
		gl_jPanel10.setVerticalGroup(gl_jPanel10.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_jPanel10.createSequentialGroup().addGap(11).addGroup(gl_jPanel10.createParallelGroup(Alignment.LEADING).addGroup(gl_jPanel10.createSequentialGroup().addGap(3).addComponent(jLabel15)).addComponent(txtGroupName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addGap(6)
						.addGroup(gl_jPanel10.createParallelGroup(Alignment.LEADING).addGroup(gl_jPanel10.createSequentialGroup().addGap(4).addComponent(jLabel25)).addComponent(cmbGroupSource, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addGap(6)
						.addGroup(gl_jPanel10.createParallelGroup(Alignment.LEADING).addGroup(gl_jPanel10.createSequentialGroup().addGap(4).addComponent(jLabel16)).addComponent(cmbGroupClass, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addGap(6)
						.addGroup(gl_jPanel10.createParallelGroup(Alignment.LEADING).addGroup(gl_jPanel10.createSequentialGroup().addGap(3).addComponent(jLabel17)).addComponent(txtGroupAddress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addGap(6)
						.addGroup(gl_jPanel10.createParallelGroup(Alignment.LEADING).addComponent(jLabel20).addComponent(jScrollPane5, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE).addGroup(gl_jPanel10.createSequentialGroup().addGap(65).addComponent(cmdSelectUsers))).addGap(8)
						.addGroup(gl_jPanel10.createParallelGroup(Alignment.LEADING).addComponent(jLabel18).addComponent(jScrollPane6, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE).addGroup(gl_jPanel10.createSequentialGroup().addGap(64).addComponent(cmdSelectGroups))).addGap(6)
						.addGroup(gl_jPanel10.createParallelGroup(Alignment.LEADING).addGroup(gl_jPanel10.createSequentialGroup().addGap(3).addComponent(jLabel19)).addComponent(txtGroupOwner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(jButton10)).addGap(6)
						.addGroup(gl_jPanel10.createParallelGroup(Alignment.LEADING).addComponent(jLabel21).addComponent(jScrollPane7, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addGap(6)
						.addGroup(gl_jPanel10.createParallelGroup(Alignment.LEADING).addGroup(gl_jPanel10.createSequentialGroup().addGap(4).addComponent(jLabel22)).addComponent(cmbAliasSet, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addGap(4).addComponent(chkIsDymanic, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE).addGap(6).addComponent(chkGroupisPrivate)));
		jPanel10.setLayout(gl_jPanel10);

		final FolderSelectorData xx = new FolderSelectorData();
		setFolderselectordata(xx);
		final AclBrowserData adata = new AclBrowserData();
		this.acldata = adata;
		final GroupSelectorData s = new GroupSelectorData();
		final UserSelectorData x = new UserSelectorData();
		setGroupselectordata(s);
		setUserselectordata(x);

		usertablemodel = new DefaultTableModel() {

			@Override
			public boolean isCellEditable(final int row, final int column) {
				return false;
			}
		};
		userTable.setModel(usertablemodel);
		userTable.setSelectionMode(1);
		usertablemodel.addColumn(".");
		usertablemodel.addColumn("User Name");
		usertablemodel.addColumn("data");
		//
		userTable.setAutoCreateColumnsFromModel(true);
		userTable.setModel(usertablemodel);
		final GroupLayout gl_jPanel6 = new GroupLayout(jPanel6);
		gl_jPanel6.setHorizontalGroup(gl_jPanel6.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_jPanel6.createSequentialGroup().addGap(10)
						.addGroup(gl_jPanel6.createParallelGroup(Alignment.LEADING).addGroup(gl_jPanel6.createSequentialGroup().addComponent(jScrollPane2, 0, 0, Short.MAX_VALUE).addContainerGap()).addComponent(jLabel23)
								.addGroup(gl_jPanel6.createSequentialGroup().addComponent(txtUserFilter, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(cmdSearchUser, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap())
								.addGroup(gl_jPanel6.createSequentialGroup().addComponent(cmdUserRename, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(cmdDeleteUser, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE).addContainerGap()).addComponent(cmdNewUser, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE))));
		gl_jPanel6.setVerticalGroup(gl_jPanel6.createParallelGroup(Alignment.LEADING).addGroup(gl_jPanel6.createSequentialGroup().addComponent(jLabel23).addGap(2).addGroup(gl_jPanel6.createParallelGroup(Alignment.BASELINE).addComponent(txtUserFilter, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(cmdSearchUser)).addGap(7)
				.addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 391, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addGroup(gl_jPanel6.createParallelGroup(Alignment.BASELINE).addComponent(cmdUserRename).addComponent(cmdDeleteUser)).addGap(6).addComponent(cmdNewUser)));
		jPanel6.setLayout(gl_jPanel6);
		userTable.validate();

		grouptablemodel = new DefaultTableModel() {

			@Override
			public boolean isCellEditable(final int row, final int column) {
				return false;
			}
		};

		groupTable.setModel(grouptablemodel);
		grouptablemodel.addColumn("Group Name");
		grouptablemodel.addColumn("data");
		//
		groupTable.setAutoCreateColumnsFromModel(true);
		groupTable.setModel(grouptablemodel);
		groupTable.validate();

		userTable.getColumnModel().getColumn(0).setCellRenderer(new ActivityImageRenderer());

		for (int i = 0; i < 1; i++) {
			final TableColumn col = userTable.getColumnModel().getColumn(i);
			if (i == 0 || i == 1) {
				col.setPreferredWidth(22);
				col.setMaxWidth(22);
			}
		}

		userTable.getColumnModel().removeColumn(userTable.getColumnModel().getColumn(2));
		groupTable.getColumnModel().removeColumn(groupTable.getColumnModel().getColumn(1));

	}

	private void cmdDeleteGroupActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdDeleteGroupActionPerformed
		final int row = groupTable.getSelectedRow();
		final Vector v = (Vector) grouptablemodel.getDataVector().elementAt(row);
		// //System.out.println(v);
		final DokuData d = (DokuData) v.elementAt(1);
		final String userid = d.getObjID();
		IDfSession session = null;
		try {
			session = smanager.getSession();
			final IDfId id = new DfId(userid);
			final IDfGroup group = (IDfGroup) session.getObject(id);
			group.destroy();
			grouptablemodel.removeRow(row);
			groupTable.validate();
		} catch (final DfException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
			DfLogger.error(this, ex.getMessage(), null, ex);
		} finally {
			if (session != null) {
				smanager.releaseSession(session);
			}

		}
	}// GEN-LAST:event_cmdDeleteGroupActionPerformed

	private void cmdDeleteUserActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdDeleteUserActionPerformed

		final int answer = JOptionPane.showConfirmDialog(this, "Destroy selected user, Are you sure??", "Confirm", JOptionPane.YES_NO_OPTION);
		if (answer == JOptionPane.YES_OPTION) {

			final int row = userTable.getSelectedRow();
			final Vector v = (Vector) usertablemodel.getDataVector().elementAt(row);
			final DokuData d = (DokuData) v.elementAt(2);
			final String userid = d.getObjID();
			IDfSession session = null;
			try {
				session = smanager.getSession();
				final IDfId id = new DfId(userid);
				final IDfUser user = (IDfUser) session.getObject(id);
				user.destroy();
				usertablemodel.removeRow(row);
				userTable.validate();
			} catch (final DfException ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
				log.error(ex);
			} finally {
				if (session != null) {
					smanager.releaseSession(session);
				}
			}
		}

	}// GEN-LAST:event_cmdDeleteUserActionPerformed

	private void cmdGroupQueryActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdGroupQueryActionPerformed
		final String groupFilter = txtGroupFilter.getText();
		grouptablemodel.setRowCount(0);
		IDfCollection col = null;
		IDfSession session = null;
		try {
			session = smanager.getSession();
			final IDfQuery query = new DfQuery();
			query.setDQL("select group_name, r_object_id from dm_group where group_name like '" + groupFilter + "%' order by group_name ENABLE (RETURN_TOP 1000)");
			col = query.execute(session, IDfQuery.DF_READ_QUERY);
			while (col.next()) {
				final Vector<Object> vx = new Vector<Object>();
				final String jep = col.getString("group_name");
				vx.add(jep);
				final DokuData data = new DokuData(col.getString("r_object_id"));
				vx.add(data);
				grouptablemodel.addRow(vx);
			}
		} catch (final DfException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
			log.error(ex);
		} finally {
			if (session != null) {
				smanager.releaseSession(session);
			}
			if (col != null) {
				try {
					col.close();
				} catch (final DfException e) {
				}
			}
		}
		groupTable.setModel(grouptablemodel);
		groupTable.validate();

	}// GEN-LAST:event_cmdGroupQueryActionPerformed

	private void cmdNewGroupActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdNewGroupActionPerformed
		txtGroupName.setEditable(true);
	}// GEN-LAST:event_cmdNewGroupActionPerformed

	private void cmdNewUserActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdNewUserActionPerformed

		txtUserName.setEditable(true);

		txtUserName.setText("");

		txtDefaultFolder.setText("");
		txtDefaultGroup.setText("docu");
		txtUserAddress.setText("replaceme@test");
		txtUserDbName.setText("");
		txtLoginName.setText("");
		txtUserDescription.setText("");
		txtUserOsDomain.setText("");
		txtUserOsName.setText("");
		acldata.setAclDomain("dm_dbo");
		acldata.setAclName("Global User Default ACL");
		txtLoginName.setText("");
		txtUserACL.setText(acldata.getAclName() + "@" + acldata.getAclDomain());
		cmbUserPrivileges.setSelectedIndex(0);

		chkUserActive.setSelected(true);
		chkWFEnabled.setSelected(true);

	}// GEN-LAST:event_cmdNewUserActionPerformed

	private void cmdRenameGroupActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdRenameGroupActionPerformed

		final int row = groupTable.getSelectedRow();
		final Vector v = (Vector) grouptablemodel.getDataVector().elementAt(row);
		// //System.out.println(v);
		final DokuData d = (DokuData) v.elementAt(1);
		final String userid = d.getObjID();
		IDfSession session = null;
		try {
			session = smanager.getSession();
			final IDfId id = new DfId(userid);
			final IDfGroup group = (IDfGroup) session.getObject(id);
			final String newUserName = JOptionPane.showInputDialog("Enter New Groupname for: " + group.getGroupName());

			final IDfFolder folder = session.getFolderByPath("/System/Sysadmin/GroupRename");
			if (folder == null) {
				final IDfSysObject sobj = (IDfSysObject) session.newObject("dm_folder");
				sobj.setString("object_name", "GroupRename");
				sobj.setString("acl_domain", "dm_dbo");
				sobj.setString("acl_name", "dm_acl_superusers");
				sobj.link("/System/Sysadmin");
				sobj.save();
			}

			final IDfSysObject jrobj = (IDfSysObject) session.newObject("dm_job_request");
			jrobj.setString("object_name", "GroupRename");
			jrobj.setString("job_name", "dm_GroupRename");
			jrobj.setString("method_name", "dm_GroupRename");
			jrobj.setBoolean("request_completed", false);
			jrobj.setRepeatingString("arguments_keys", 0, "OldGroupName");
			jrobj.setRepeatingString("arguments_keys", 1, "NewGroupName");
			jrobj.setRepeatingString("arguments_keys", 2, "report_only");
			jrobj.setRepeatingString("arguments_keys", 3, "unlock_locked_obj");
			jrobj.setRepeatingString("arguments_values", 0, group.getGroupName());
			jrobj.setRepeatingString("arguments_values", 1, newUserName);
			jrobj.setRepeatingString("arguments_values", 2, "F");
			jrobj.setRepeatingString("arguments_values", 3, "T");

			jrobj.link("/System/Sysadmin/GroupRename");
			jrobj.save();

			final IDfPersistentObject jobobj = session.getObjectByQualification("dm_job where object_name = 'dm_GroupRename'");
			if (jobobj != null) {
				jobobj.setInt("run_now", 1);
				jobobj.save();
			}

		} catch (final DfException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
			DfLogger.error(this, ex.getMessage(), null, ex);
		} finally {
			if (session != null) {
				smanager.releaseSession(session);
			}

		}

	}// GEN-LAST:event_cmdRenameGroupActionPerformed

	private void cmdSaveGroupActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdSaveGroupActionPerformed

		IDfSession session = null;
		try {
			session = smanager.getSession();
			final String groupname = txtGroupName.getText();
			IDfGroup group = null;
			if (txtGroupName.isEditable()) {
				final IDfGroup tempgroup = session.getGroup(groupname);
				if (tempgroup == null) {
					group = (IDfGroup) session.newObject("dm_group");
					group.setGroupName(groupname);
				} else {
					JOptionPane.showMessageDialog(null, "Group with name '" + groupname + "' already exists.", "Duplicate name found.", JOptionPane.INFORMATION_MESSAGE);
					return;

				}
			} else {
				group = session.getGroup(groupname);
			}

			group.setString("group_source", (String) cmbGroupSource.getSelectedItem());
			group.setGroupAddress(txtGroupAddress.getText());
			group.setDescription(txtGroupDescription.getText());
			group.setOwnerName(txtGroupOwner.getText());
			group.truncate("users_names", 0);
			for (int i = 0; i < userlistmodel.size(); i++) {
				group.addUser((String) userlistmodel.get(i));
			}
			group.truncate("groups_names", 0);
			for (int i = 0; i < grouplistmodel.size(); i++) {
				group.addGroup((String) grouplistmodel.get(i));
			}
			group.setPrivate(chkGroupisPrivate.isSelected());
			group.setDynamic(chkIsDymanic.isSelected());
			group.save();
			SwingHelper.showMessage("Group Saved");
		} catch (final DfException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
			log.error(ex);
		} finally {
			if (session != null) {
				smanager.releaseSession(session);
			}
		}
	}// GEN-LAST:event_cmdSaveGroupActionPerformed

	private void cmdSearchUserActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdSearchUserActionPerformed

		usertablemodel.setRowCount(0);
		final String filter = txtUserFilter.getText();
		IDfSession session = null;
		IDfCollection col = null;
		try {
			session = smanager.getSession();
			final IDfQuery query = new DfQuery();
			System.out.println("'" + filter + "'");
			if (filter.length() < 1) {
				query.setDQL("select user_state, r_object_id, user_name from dm_user where r_is_group=0 order by user_name ENABLE (RETURN_TOP 1000)");
			} else {
				query.setDQL("select user_state, r_object_id, user_name from dm_user where r_is_group=0 and " + "(user_name like '" + filter + "%' or user_os_name like '" + filter + "%' or user_login_name like  '" + filter + "%') " + "order by user_name ENABLE (RETURN_TOP 100)");
			}
			col = query.execute(session, IDfQuery.DF_READ_QUERY);
			while (col.next()) {
				final Vector<Object> vx = new Vector<Object>();
				final String state = col.getString("user_state");
				vx.add(state);
				final String jep = col.getString("user_name");
				vx.add(jep);
				final DokuData data = new DokuData(col.getString("r_object_id"));
				vx.add(data);
				usertablemodel.addRow(vx);
			}

			userTable.setModel(usertablemodel);
			userTable.validate();
		} catch (final Exception ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
			DfLogger.error(this, ex.getMessage(), null, ex);
		} finally {
			if (col != null) {
				try {
					col.close();
				} catch (final DfException ex) {
				}
			}
			if (session != null) {
				smanager.releaseSession(session);
			}

		}

	}// GEN-LAST:event_cmdSearchUserActionPerformed

	private void cmdSelectACLActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdSelectACLActionPerformed

		final ActionListener a = new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				txtUserACL.setText(acldata.getAclName() + "@" + acldata.getAclDomain());
			}
		};
		final ACLBrowserFrame frame = new ACLBrowserFrame(a, acldata, true);
		frame.setVisible(true);

	}// GEN-LAST:event_cmdSelectACLActionPerformed

	private void cmdSelectDefaultFolderActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdSelectDefaultFolderActionPerformed
		// TODO add your handling code here:

		// //System.out.println("folderselectordata on call: " +
		// getFolderselectordata());
		final ActionListener a = new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				txtDefaultFolder.setText(getFolderselectordata().getFolderPath());
			}
		};
		final FolderSelectorFrame frame = new FolderSelectorFrame(a, getFolderselectordata());
		SwingHelper.centerJFrame(frame);
		frame.initAll();
		frame.setVisible(true);

	}// GEN-LAST:event_cmdSelectDefaultFolderActionPerformed

	private void cmdSelectGroupsActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdSelectGroupsActionPerformed

		final GroupsinGroupData gigData = new GroupsinGroupData();
		final Vector<Object> groupVector = new Vector<Object>();
		final ListModel model = lstGroups.getModel();
		final int modelsize = model.getSize();
		for (int i = 0; i < modelsize; i++) {
			groupVector.add(model.getElementAt(i));
		}
		gigData.setGroupMembers(groupVector);
		final ActionListener a = new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				// //System.out.println(e);
				// //System.out.println(e.getSource().toString());
				grouplistmodel.clear();
				for (int i = 0; i < gigData.getGroupMembers().size(); i++) {
					final String username = (String) gigData.getGroupMembers().get(i);
					// //System.out.println(username);
					grouplistmodel.addElement(username);
				}
				lstGroups.validate();
			}
		};
		final GroupEditorGroups frame = new GroupEditorGroups(a, gigData);
		SwingHelper.centerJFrame(frame);
		frame.setVisible(true);
	}// GEN-LAST:event_cmdSelectGroupsActionPerformed

	private void cmdSelectUsersActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdSelectUsersActionPerformed
		final UsersInGroupData uigData = new UsersInGroupData();
		final Vector<Object> userVector = new Vector<Object>();
		final ListModel model = lstUsers.getModel();
		final int modelsize = model.getSize();
		for (int i = 0; i < modelsize; i++) {
			userVector.add(model.getElementAt(i));
		}
		uigData.setGroupMembers(userVector);
		final ActionListener a = new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				// //System.out.println(e);
				// //System.out.println(e.getSource().toString());
				userlistmodel.clear();
				for (int i = 0; i < uigData.getGroupMembers().size(); i++) {
					final String username = (String) uigData.getGroupMembers().get(i);
					// //System.out.println(username);
					userlistmodel.addElement(username);
				}
				lstGroups.validate();
			}
		};
		final GroupEditorUsers frame = new GroupEditorUsers(a, uigData);
		// frame.setSession(session);
		SwingHelper.centerJFrame(frame);
		frame.setVisible(true);
	}// GEN-LAST:event_cmdSelectUsersActionPerformed

	private void cmdUserRenameActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdUserRenameActionPerformed

		final int row = userTable.getSelectedRow();
		final Vector v = (Vector) usertablemodel.getDataVector().elementAt(row);
		// //System.out.println(v);
		final DokuData d = (DokuData) v.elementAt(2);
		final String userid = d.getObjID();
		IDfSession session = null;
		try {
			session = smanager.getSession();
			final IDfId id = new DfId(userid);
			final IDfUser user = (IDfUser) session.getObject(id);
			final String newUserName = JOptionPane.showInputDialog("Enter New User Name for: " + user.getUserName());

			final IDfFolder folder = session.getFolderByPath("/System/Sysadmin/UserRename");
			if (folder == null) {
				final IDfSysObject sobj = (IDfSysObject) session.newObject("dm_folder");
				sobj.setString("object_name", "UserRename");
				sobj.setString("acl_domain", "dm_dbo");
				sobj.setString("acl_name", "dm_acl_superusers");
				sobj.link("/System/Sysadmin");
				sobj.save();
			}

			final IDfSysObject jrobj = (IDfSysObject) session.newObject("dm_job_request");
			jrobj.setString("object_name", "UserRename");
			jrobj.setString("job_name", "dm_UserRename");
			jrobj.setString("method_name", "dm_UserRename");
			jrobj.setBoolean("request_completed", false);
			jrobj.setRepeatingString("arguments_keys", 0, "OldUserName");
			jrobj.setRepeatingString("arguments_keys", 1, "NewUserName");
			jrobj.setRepeatingString("arguments_keys", 2, "report_only");
			jrobj.setRepeatingString("arguments_keys", 3, "unlock_locked_obj");
			jrobj.setRepeatingString("arguments_values", 0, user.getUserName());
			jrobj.setRepeatingString("arguments_values", 1, newUserName);
			jrobj.setRepeatingString("arguments_values", 2, "F");
			jrobj.setRepeatingString("arguments_values", 3, "T");

			jrobj.link("/System/Sysadmin/UserRename");
			jrobj.save();

			final IDfPersistentObject jobobj = session.getObjectByQualification("dm_job where object_name = 'dm_UserRename'");
			if (jobobj != null) {
				jobobj.setInt("run_now", 1);
				jobobj.save();
			}

		} catch (final DfException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
			log.error(ex);
		} finally {
			if (session != null) {
				smanager.releaseSession(session);
			}

		}

	}// GEN-LAST:event_cmdUserRenameActionPerformed

	private void cmdUserSelectDefaultGroupActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdUserSelectDefaultGroupActionPerformed
		final ActionListener a = new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				// //System.out.println(e);
				// //System.out.println(e.getSource().toString());
				txtDefaultGroup.setText(groupselectordata.getGroupName());
			}
		};
		final GroupSelectorFrame frame = new GroupSelectorFrame(a, groupselectordata);
		SwingHelper.centerJFrame(frame);
		frame.setVisible(true);
	}// GEN-LAST:event_cmdUserSelectDefaultGroupActionPerformed

	private void cmdValidateActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdValidateActionPerformed
	}// GEN-LAST:event_cmdValidateActionPerformed

	private void cmdviewGroupRenameLogActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdviewGroupRenameLogActionPerformed
		final String objid = getIDfromGroupTable();
		viewLog(objid);
	}// GEN-LAST:event_cmdviewGroupRenameLogActionPerformed

	private void cmdviewUserRenameLogActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdviewUserRenameLogActionPerformed
		final String objid = getIDfromUserTable();
		viewLog(objid);
	}// GEN-LAST:event_cmdviewUserRenameLogActionPerformed

	private void deleteLog(final String objid) {
		IDfSession session = null;
		try {
			final int answer = JOptionPane.showConfirmDialog(this, "Delete selected object?", "Confirm", JOptionPane.YES_NO_OPTION);
			if (answer == JOptionPane.YES_OPTION) {
				final Cursor cur = new Cursor(Cursor.WAIT_CURSOR);

				setCursor(cur);
				session = smanager.getSession();
				final IDfId id = new DfId(objid);
				final IDfSysObject obj = (IDfSysObject) session.getObject(id);
				obj.destroy();
				updateGroupRenameTable();
				updateUserRenameTable();
			}
		} catch (final DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
		} finally {
			if (session != null) {
				smanager.releaseSession(session);
			}
			final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);

			setCursor(cur2);
		}
	}

	public FolderSelectorData getFolderselectordata() {
		return folderselectordata;
	}

	public GroupSelectorData getGroupselectordata() {
		return groupselectordata;
	}

	private String getIDfromGroupTable() {
		final int row = tblGroupRename.getSelectedRow();
		String objid = "0000000000000000";
		if (row != -1) {
			final Vector v = (Vector) grouprenamemodel.getDataVector().elementAt(row);
			// //System.out.println(v);
			final DokuData d = (DokuData) v.elementAt(5);
			objid = d.getObjID();
		}
		return objid;

	}

	private String getIDfromUserTable() {

		String objid = "0000000000000000";
		final int row = tblUserRename.getSelectedRow();
		if (row != -1) {
			final Vector v = (Vector) userrenamemodel.getDataVector().elementAt(row);
			// //System.out.println(v);
			final DokuData d = (DokuData) v.elementAt(5);
			objid = d.getObjID();

		}
		return objid;

	}

	public UserSelectorData getUserselectordata() {
		return userselectordata;
	}

	private void groupTableMouseReleased(final java.awt.event.MouseEvent evt) {// GEN-FIRST:event_groupTableMouseReleased
		txtGroupName.setEditable(false);
		final Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
		setCursor(cur);
		userlistmodel.clear();
		grouplistmodel.clear();
		final Vector<String> users = new Vector<String>();
		final Vector<String> groups = new Vector<String>();
		final int row = groupTable.getSelectedRow();
		final Vector v = (Vector) grouptablemodel.getDataVector().elementAt(row);
		// //System.out.println(v);
		final DokuData d = (DokuData) v.elementAt(1);
		final String groupid = d.getObjID();
		IDfSession session = null;
		try {
			final IDfId id = new DfId(groupid);
			session = smanager.getSession();

			final IDfGroup group = (IDfGroup) session.getObject(id);
			cmbGroupSource.setSelectedItem(group.getString("group_source"));
			txtGroupName.setText(group.getString("group_name"));
			txtGroupDescription.setText(group.getString("description"));
			txtGroupAddress.setText(group.getString("group_address"));
			txtGroupOwner.setText(group.getString("owner_name"));
			final int userCount = group.getValueCount("users_names");
			for (int i = 0; i < userCount; i++) {
				users.add(group.getRepeatingString("users_names", i));
			}
			Collections.sort(users);
			for (final String s : users) {
				userlistmodel.addElement(s);
			}

			final int groupCount = group.getValueCount("groups_names");
			for (int j = 0; j < groupCount; j++) {
				// grouplistmodel.addElement(group.getRepeatingString("groups_names",
				// j));
				groups.add(group.getRepeatingString("groups_names", j));
			}
			Collections.sort(groups);
			for (final String s : groups) {
				grouplistmodel.addElement(s);
			}

			final boolean isprivate = group.getBoolean("is_private");

			if (isprivate) {
				chkGroupisPrivate.setSelected(true);
			} else {
				chkGroupisPrivate.setSelected(false);
			}

			final boolean isdynamic = group.getDynamic();
			if (isdynamic) {
				chkIsDymanic.setSelected(true);
			} else {
				chkIsDymanic.setSelected(false);
			}

			lstUsers.validate();
			lstGroups.validate();
		} catch (final DfException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
			DfLogger.error(this, ex.getMessage(), null, ex);
		} finally {
			final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
			setCursor(cur2);
			if (session != null) {
				smanager.releaseSession(session);
			}

		}
	}// GEN-LAST:event_groupTableMouseReleased

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {
		final java.awt.GridBagConstraints gridBagConstraints;

		popupUserRename = new javax.swing.JPopupMenu();
		mnuViewUserRename = new javax.swing.JMenuItem();
		mnuDeleteUserRename = new javax.swing.JMenuItem();
		popupGruopRename = new javax.swing.JPopupMenu();
		mnuViewGroupRename = new javax.swing.JMenuItem();
		mnuDeleteGroupRename = new javax.swing.JMenuItem();
		jTabbedPane1 = new javax.swing.JTabbedPane();
		jPanel2 = new javax.swing.JPanel();
		jPanel6 = new javax.swing.JPanel();
		jScrollPane2 = new javax.swing.JScrollPane();
		userTable = new javax.swing.JTable();
		cmdUserRename = new javax.swing.JButton();
		cmdDeleteUser = new javax.swing.JButton();
		txtUserFilter = TextFieldFactory.createJTextField();
		cmdSearchUser = new javax.swing.JButton();
		jLabel23 = new javax.swing.JLabel();
		cmdNewUser = new javax.swing.JButton();
		jButton1 = new javax.swing.JButton();
		jButton2 = new javax.swing.JButton();
		jPanel7 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		txtUserName = TextFieldFactory.createJTextField();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		jLabel6 = new javax.swing.JLabel();
		jLabel5 = new javax.swing.JLabel();
		jLabel7 = new javax.swing.JLabel();
		jLabel8 = new javax.swing.JLabel();
		jLabel9 = new javax.swing.JLabel();
		jLabel10 = new javax.swing.JLabel();
		jLabel11 = new javax.swing.JLabel();
		jLabel12 = new javax.swing.JLabel();
		jLabel13 = new javax.swing.JLabel();
		jLabel14 = new javax.swing.JLabel();
		chkWFEnabled = new javax.swing.JCheckBox();
		chkUserActive = new javax.swing.JCheckBox();
		cmbClientCapability = new javax.swing.JComboBox();
		cmbUserAliasSet = new javax.swing.JComboBox();
		txtUserDescription = TextFieldFactory.createJTextField();
		txtUserDbName = TextFieldFactory.createJTextField();
		txtDefaultFolder = TextFieldFactory.createJTextField();
		cmbUserPrivileges = new javax.swing.JComboBox();
		txtUserACL = TextFieldFactory.createJTextField();
		txtDefaultGroup = TextFieldFactory.createJTextField();
		txtUserPassword = new javax.swing.JTextField();
		cmbuserSource = new javax.swing.JComboBox();
		txtUserOsDomain = TextFieldFactory.createJTextField();
		txtUserOsName = TextFieldFactory.createJTextField();
		cmdSelectDefaultFolder = new javax.swing.JButton();
		cmdSelectACL = new javax.swing.JButton();
		cmdUserSelectDefaultGroup = new javax.swing.JButton();
		jPanel3 = new javax.swing.JPanel();
		jPanel1 = new javax.swing.JPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		groupTable = new javax.swing.JTable();
		txtGroupFilter = new javax.swing.JTextField();
		cmdGroupQuery = new javax.swing.JButton();
		jLabel24 = new javax.swing.JLabel();
		cmdRenameGroup = new javax.swing.JButton();
		cmdDeleteGroup = new javax.swing.JButton();
		cmdNewGroup = new javax.swing.JButton();
		cmdValidate = new javax.swing.JButton();
		jPanel10 = new javax.swing.JPanel();
		jLabel15 = new javax.swing.JLabel();
		jLabel16 = new javax.swing.JLabel();
		jLabel17 = new javax.swing.JLabel();
		jLabel20 = new javax.swing.JLabel();
		jScrollPane5 = new javax.swing.JScrollPane();
		lstUsers = new javax.swing.JList();
		txtGroupName = new javax.swing.JTextField();
		cmbGroupClass = new javax.swing.JComboBox();
		txtGroupAddress = new javax.swing.JTextField();
		jLabel18 = new javax.swing.JLabel();
		jScrollPane6 = new javax.swing.JScrollPane();
		lstGroups = new javax.swing.JList();
		jLabel19 = new javax.swing.JLabel();
		txtGroupOwner = new javax.swing.JTextField();
		jLabel21 = new javax.swing.JLabel();
		cmbAliasSet = new javax.swing.JComboBox();
		jLabel22 = new javax.swing.JLabel();
		chkGroupisPrivate = new javax.swing.JCheckBox();
		jScrollPane7 = new javax.swing.JScrollPane();
		txtGroupDescription = new ExJTextArea();
		txtGroupDescription.setFont(new Font("Monospaced", Font.PLAIN, 11));
		cmdSelectUsers = new javax.swing.JButton();
		cmdSelectGroups = new javax.swing.JButton();
		jButton10 = new javax.swing.JButton();
		jLabel25 = new javax.swing.JLabel();
		cmbGroupSource = new javax.swing.JComboBox();
		chkIsDymanic = new javax.swing.JCheckBox();
		cmdSaveGroup = new javax.swing.JButton();
		jButton9 = new javax.swing.JButton();
		jPanel4 = new javax.swing.JPanel();
		jPanel8 = new javax.swing.JPanel();
		jScrollPane3 = new javax.swing.JScrollPane();
		tblUserRename = new javax.swing.JTable();
		cmdviewUserRenameLog = new javax.swing.JButton();
		jPanel5 = new javax.swing.JPanel();
		cmdviewGroupRenameLog = new javax.swing.JButton();
		jPanel9 = new javax.swing.JPanel();
		jScrollPane4 = new javax.swing.JScrollPane();
		tblGroupRename = new javax.swing.JTable();

		mnuViewUserRename.setText("View");
		mnuViewUserRename.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuViewUserRenameActionPerformed(evt);
			}
		});
		popupUserRename.add(mnuViewUserRename);

		mnuDeleteUserRename.setText("Delete");
		mnuDeleteUserRename.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuDeleteUserRenameActionPerformed(evt);
			}
		});
		popupUserRename.add(mnuDeleteUserRename);

		mnuViewGroupRename.setText("View");
		mnuViewGroupRename.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuViewGroupRenameActionPerformed(evt);
			}
		});
		popupGruopRename.add(mnuViewGroupRename);

		mnuDeleteGroupRename.setText("Delete");
		mnuDeleteGroupRename.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuDeleteGroupRenameActionPerformed(evt);
			}
		});
		popupGruopRename.add(mnuDeleteGroupRename);

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setLocationByPlatform(true);

		jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
			@Override
			public void stateChanged(final javax.swing.event.ChangeEvent evt) {
				jTabbedPane1StateChanged(evt);
			}
		});

		jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Users"));

		jScrollPane2.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseReleased(final java.awt.event.MouseEvent evt) {
				jScrollPane2MouseReleased(evt);
			}
		});

		userTable.setAutoCreateColumnsFromModel(false);
		userTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

		}, new String[] { "UserName" }) {
			boolean[] canEdit = new boolean[] { false };

			@Override
			public boolean isCellEditable(final int rowIndex, final int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		userTable.setGridColor(new java.awt.Color(255, 255, 255));
		userTable.setRowHeight(20);
		userTable.setShowHorizontalLines(false);
		userTable.setShowVerticalLines(false);
		userTable.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseReleased(final java.awt.event.MouseEvent evt) {
				userTableMouseReleased(evt);
			}
		});
		jScrollPane2.setViewportView(userTable);

		cmdUserRename.setMnemonic('r');
		cmdUserRename.setText("Rename");
		cmdUserRename.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdUserRenameActionPerformed(evt);
			}
		});

		cmdDeleteUser.setMnemonic('d');
		cmdDeleteUser.setText("Delete");
		cmdDeleteUser.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdDeleteUserActionPerformed(evt);
			}
		});

		cmdSearchUser.setText("GO");
		cmdSearchUser.setMargin(new java.awt.Insets(1, 8, 1, 8));
		cmdSearchUser.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdSearchUserActionPerformed(evt);
			}
		});

		jLabel23.setText("User Filter:");

		cmdNewUser.setText("New User");
		cmdNewUser.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdNewUserActionPerformed(evt);
			}
		});

		jButton1.setText("Close");
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});

		jButton2.setText("Save");
		jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(final java.awt.event.MouseEvent evt) {
				jButton2MouseClicked(evt);
			}
		});
		jButton2.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				jButton2ActionPerformed(evt);
			}
		});

		jPanel7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		jPanel7.setLayout(new FormLayout(new ColumnSpec[] { FormSpecs.UNRELATED_GAP_COLSPEC, ColumnSpec.decode("81px"), FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, ColumnSpec.decode("196px:grow"), ColumnSpec.decode("18px"), ColumnSpec.decode("43px"), FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, },
				new RowSpec[] { RowSpec.decode("21px"), RowSpec.decode("20px"), FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("20px"), FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("20px"), FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("20px"), FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("20px"), FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("22px"),
						FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("20px"), FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("21px"), FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("21px"), FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("22px"), FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("21px"), FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("20px"),
						FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("20px"), FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("22px"), FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("15px"), FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("15px"), FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("15px"), }));

		jLabel1.setLabelFor(txtUserName);
		jLabel1.setText("User Name:");
		jPanel7.add(jLabel1, "2, 2, left, top");

		txtUserName.setEditable(false);
		jPanel7.add(txtUserName, "4, 2, 3, 1, fill, bottom");

		jLabel2.setLabelFor(txtUserOsName);
		jLabel2.setText("Os Name:");
		jPanel7.add(jLabel2, "2, 4, left, top");

		jLabel3.setLabelFor(txtUserOsDomain);
		jLabel3.setText("User Domain:");
		jPanel7.add(jLabel3, "2, 6, left, top");

		label = new JLabel();
		label.setText("User Address:");
		jPanel7.add(label, "2, 8, left, top");

		txtUserAddress = TextFieldFactory.createJTextField();
		jPanel7.add(txtUserAddress, "4, 8, 3, 1, fill, default");

		label_1 = new JLabel();
		label_1.setText("Login Name:");
		jPanel7.add(label_1, "2, 12, left, top");

		txtLoginName = TextFieldFactory.createJTextField();
		jPanel7.add(txtLoginName, "4, 12, 3, 1, fill, default");

		jLabel6.setLabelFor(txtUserPassword);
		jLabel6.setText("User Password:");
		jPanel7.add(jLabel6, "2, 14, left, top");

		jLabel5.setLabelFor(cmbuserSource);
		jLabel5.setText("User Source:");
		jPanel7.add(jLabel5, "2, 10, left, top");

		jLabel7.setLabelFor(txtDefaultGroup);
		jLabel7.setText("Default Group:");
		jPanel7.add(jLabel7, "2, 16, left, top");

		jLabel8.setLabelFor(txtUserACL);
		jLabel8.setText("User ACL:");
		jPanel7.add(jLabel8, "2, 18, left, top");

		jLabel9.setLabelFor(cmbUserPrivileges);
		jLabel9.setText("User Privileges:");
		jPanel7.add(jLabel9, "2, 20, left, top");

		final JLabel lblExtPrivileges = new JLabel("Audit Privileges:");
		jPanel7.add(lblExtPrivileges, "2, 22, left, top");

		final JComboBox cmbXPrivileges = new JComboBox();
		jPanel7.add(cmbXPrivileges, "4, 22, 3, 1, fill, default");

		jLabel10.setLabelFor(txtDefaultFolder);
		jLabel10.setText("Default Folder:");
		jPanel7.add(jLabel10, "2, 24, left, top");

		jLabel11.setLabelFor(txtUserDbName);
		jLabel11.setText("User DB Name:");
		jPanel7.add(jLabel11, "2, 26, left, top");

		jLabel12.setLabelFor(txtUserDescription);
		jLabel12.setText("Description:");
		jPanel7.add(jLabel12, "2, 28, left, top");

		jLabel13.setLabelFor(cmbUserAliasSet);
		jLabel13.setText("Alias Set:");
		jPanel7.add(jLabel13, "2, 30, left, top");

		jLabel14.setLabelFor(cmbClientCapability);
		jLabel14.setText("Client Capability:");
		jPanel7.add(jLabel14, "2, 32, left, top");

		chkWFEnabled.setSelected(true);
		chkWFEnabled.setText("Workflow Enabled");
		chkWFEnabled.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		chkWFEnabled.setMargin(new java.awt.Insets(0, 0, 0, 0));
		jPanel7.add(chkWFEnabled, "4, 36, left, top");

		chkUserActive.setSelected(true);
		chkUserActive.setText("User is Active");
		chkUserActive.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		chkUserActive.setMargin(new java.awt.Insets(0, 0, 0, 0));
		jPanel7.add(chkUserActive, "4, 34, left, top");

		cmbClientCapability.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Consumer", "Contributor", "Coordinator", "System Administrator", "None" }));
		jPanel7.add(cmbClientCapability, "4, 32, 3, 1, fill, top");
		jPanel7.add(cmbUserAliasSet, "4, 30, 3, 1, fill, top");
		jPanel7.add(txtUserDescription, "4, 28, 3, 1, fill, top");
		jPanel7.add(txtUserDbName, "4, 26, 3, 1, fill, top");
		jPanel7.add(txtDefaultFolder, "4, 24, fill, top");

		cmbUserPrivileges.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "NONE", "Create Type", "Create Cabinet", "Create Group", "System Administrator", "Superuser" }));
		jPanel7.add(cmbUserPrivileges, "4, 20, 3, 1, fill, top");

		txtUserACL.setText("Global User Default ACL");
		jPanel7.add(txtUserACL, "4, 18, fill, top");
		jPanel7.add(txtDefaultGroup, "4, 16, fill, top");

		txtUserPassword.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(final java.awt.event.MouseEvent evt) {
				txtUserPasswordMouseClicked(evt);
			}
		});
		jPanel7.add(txtUserPassword, "4, 14, 3, 1, fill, top");

		cmbuserSource.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " ", "LDAP", "inline password" }));
		jPanel7.add(cmbuserSource, "4, 10, 3, 1, fill, top");
		jPanel7.add(txtUserOsDomain, "4, 6, 3, 1, fill, top");
		jPanel7.add(txtUserOsName, "4, 4, 3, 1, fill, top");

		cmdSelectDefaultFolder.setText("Select");
		cmdSelectDefaultFolder.setMargin(new java.awt.Insets(1, 4, 1, 4));
		cmdSelectDefaultFolder.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdSelectDefaultFolderActionPerformed(evt);
			}
		});
		jPanel7.add(cmdSelectDefaultFolder, "6, 24, fill, top");

		cmdSelectACL.setText("Select");
		cmdSelectACL.setMargin(new java.awt.Insets(1, 4, 1, 4));
		cmdSelectACL.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdSelectACLActionPerformed(evt);
			}
		});
		jPanel7.add(cmdSelectACL, "6, 18, fill, center");

		cmdUserSelectDefaultGroup.setText("Select");
		cmdUserSelectDefaultGroup.setMargin(new java.awt.Insets(1, 4, 1, 4));
		cmdUserSelectDefaultGroup.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdUserSelectDefaultGroupActionPerformed(evt);
			}
		});
		jPanel7.add(cmdUserSelectDefaultGroup, "6, 16, fill, center");

		final org.jdesktop.layout.GroupLayout gl_jPanel2 = new org.jdesktop.layout.GroupLayout(jPanel2);
		jPanel2.setLayout(gl_jPanel2);
		gl_jPanel2.setHorizontalGroup(gl_jPanel2.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(gl_jPanel2.createSequentialGroup().addContainerGap()
						.add(gl_jPanel2.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
								.add(gl_jPanel2.createSequentialGroup().add(jPanel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 370, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap()).add(
										org.jdesktop.layout.GroupLayout.TRAILING, gl_jPanel2.createSequentialGroup().add(jButton2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 72, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
												.addContainerGap()))));
		gl_jPanel2.setVerticalGroup(gl_jPanel2.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING,
				gl_jPanel2.createSequentialGroup().addContainerGap().add(gl_jPanel2.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE).add(jPanel6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
						.add(gl_jPanel2.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jButton1).add(jButton2)).addContainerGap()));
		jPanel7.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[] { txtUserOsName, txtUserOsDomain, cmbuserSource, txtUserPassword, txtDefaultGroup, txtUserACL, jLabel1, cmbUserPrivileges, txtDefaultFolder, txtUserDbName, txtUserDescription, cmbUserAliasSet, cmbClientCapability, txtUserName, jLabel2, jLabel3, jLabel6, jLabel5, jLabel7, jLabel8, jLabel9, jLabel10, jLabel11, jLabel12, jLabel13, jLabel14,
				chkWFEnabled, chkUserActive, cmdSelectDefaultFolder, cmdSelectACL, cmdUserSelectDefaultGroup }));

		jTabbedPane1.addTab("User Management", jPanel2);

		jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Groups"));

		groupTable.setAutoCreateColumnsFromModel(false);
		groupTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null } }, new String[] { "Group Name" }) {
			boolean[] canEdit = new boolean[] { false };

			@Override
			public boolean isCellEditable(final int rowIndex, final int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		groupTable.setCellSelectionEnabled(true);
		groupTable.setShowHorizontalLines(false);
		groupTable.setShowVerticalLines(false);
		groupTable.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseReleased(final java.awt.event.MouseEvent evt) {
				groupTableMouseReleased(evt);
			}
		});
		jScrollPane1.setViewportView(groupTable);

		cmdGroupQuery.setText("GO");
		cmdGroupQuery.setMargin(new java.awt.Insets(1, 8, 1, 8));
		cmdGroupQuery.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdGroupQueryActionPerformed(evt);
			}
		});

		jLabel24.setText("Group Filter:");

		cmdRenameGroup.setText("Rename");
		cmdRenameGroup.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdRenameGroupActionPerformed(evt);
			}
		});

		cmdDeleteGroup.setText("Delete");
		cmdDeleteGroup.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdDeleteGroupActionPerformed(evt);
			}
		});

		cmdNewGroup.setText("New Group");
		cmdNewGroup.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdNewGroupActionPerformed(evt);
			}
		});

		cmdValidate.setText("Validate");
		cmdValidate.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdValidateActionPerformed(evt);
			}
		});

		final GroupLayout gl_jPanel1 = new GroupLayout(jPanel1);
		gl_jPanel1.setHorizontalGroup(gl_jPanel1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_jPanel1.createSequentialGroup().addContainerGap()
						.addGroup(gl_jPanel1.createParallelGroup(Alignment.LEADING).addComponent(jScrollPane1, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE).addGroup(Alignment.TRAILING, gl_jPanel1.createSequentialGroup().addComponent(txtGroupFilter, GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE).addPreferredGap(ComponentPlacement.RELATED).addComponent(cmdGroupQuery))
								.addGroup(Alignment.TRAILING, gl_jPanel1.createSequentialGroup().addGroup(gl_jPanel1.createParallelGroup(Alignment.TRAILING, false).addComponent(cmdRenameGroup, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(cmdNewGroup)).addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(gl_jPanel1.createParallelGroup(Alignment.LEADING, false).addComponent(cmdValidate, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(cmdDeleteGroup, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)))
								.addComponent(jLabel24))
						.addContainerGap()));
		gl_jPanel1.setVerticalGroup(gl_jPanel1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_jPanel1.createSequentialGroup().addComponent(jLabel24).addPreferredGap(ComponentPlacement.RELATED).addGroup(gl_jPanel1.createParallelGroup(Alignment.BASELINE).addComponent(cmdGroupQuery).addComponent(txtGroupFilter, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(gl_jPanel1.createParallelGroup(Alignment.BASELINE).addComponent(cmdRenameGroup).addComponent(cmdDeleteGroup)).addPreferredGap(ComponentPlacement.RELATED).addGroup(gl_jPanel1.createParallelGroup(Alignment.BASELINE).addComponent(cmdNewGroup).addComponent(cmdValidate))
						.addContainerGap()));
		jPanel1.setLayout(gl_jPanel1);

		jPanel10.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

		jLabel15.setLabelFor(txtGroupName);
		jLabel15.setText("Group Name:");
		jLabel15.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

		jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel16.setText("Group Class:");
		jLabel16.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

		jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel17.setLabelFor(txtGroupAddress);
		jLabel17.setText("Group Address:");
		jLabel17.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

		jLabel20.setText("Users:");
		jLabel20.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

		jScrollPane5.setViewportView(lstUsers);

		txtGroupName.setEditable(false);

		cmbGroupClass.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Group", "Role", "Domain" }));

		jLabel18.setLabelFor(lstGroups);
		jLabel18.setText("Groups:");

		jScrollPane6.setViewportView(lstGroups);

		jLabel19.setLabelFor(txtGroupOwner);
		jLabel19.setText("Owner:");

		jLabel21.setLabelFor(txtGroupDescription);
		jLabel21.setText("Description:");

		jLabel22.setText("Alias Set:");

		chkGroupisPrivate.setText("Is Private");
		chkGroupisPrivate.setBorder(null);
		chkGroupisPrivate.setMargin(new java.awt.Insets(0, 0, 0, 0));

		txtGroupDescription.setColumns(20);
		txtGroupDescription.setLineWrap(true);
		txtGroupDescription.setRows(3);
		txtGroupDescription.setWrapStyleWord(true);
		jScrollPane7.setViewportView(txtGroupDescription);

		cmdSelectUsers.setText("...");
		cmdSelectUsers.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdSelectUsersActionPerformed(evt);
			}
		});

		cmdSelectGroups.setText("...");
		cmdSelectGroups.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdSelectGroupsActionPerformed(evt);
			}
		});

		jButton10.setText("...");
		jButton10.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				jButton10ActionPerformed(evt);
			}
		});

		jLabel25.setText("Group Source:");

		cmbGroupSource.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " ", " LDAP" }));
		cmbGroupSource.setPreferredSize(new java.awt.Dimension(59, 22));

		chkIsDymanic.setText("Is Dynamic");
		chkIsDymanic.setMargin(new java.awt.Insets(0, 0, 0, 0));

		cmdSaveGroup.setText("Save");
		cmdSaveGroup.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdSaveGroupActionPerformed(evt);
			}
		});

		jButton9.setText("Close");
		jButton9.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				jButton9ActionPerformed(evt);
			}
		});

		final GroupLayout gl_jPanel3 = new GroupLayout(jPanel3);
		gl_jPanel3.setHorizontalGroup(gl_jPanel3.createParallelGroup(Alignment.LEADING).addGroup(gl_jPanel3.createSequentialGroup().addContainerGap().addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, 227, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_jPanel3.createParallelGroup(Alignment.TRAILING).addGroup(gl_jPanel3.createSequentialGroup().addComponent(cmdSaveGroup, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(jButton9, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE).addContainerGap()).addComponent(jPanel10, 0, 380, Short.MAX_VALUE))));
		gl_jPanel3.setVerticalGroup(gl_jPanel3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_jPanel3.createSequentialGroup().addContainerGap().addGroup(gl_jPanel3.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_jPanel3.createSequentialGroup().addComponent(jPanel10, GroupLayout.DEFAULT_SIZE, 554, Short.MAX_VALUE).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(gl_jPanel3.createParallelGroup(Alignment.BASELINE).addComponent(jButton9).addComponent(cmdSaveGroup))).addComponent(jPanel1, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE))
						.addContainerGap()));
		jPanel3.setLayout(gl_jPanel3);

		jTabbedPane1.addTab("Group Management", jPanel3);

		tblUserRename.setAutoCreateColumnsFromModel(false);
		tblUserRename.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

		}, new String[] { "Old Name", "New Name", "Creation Date", "Modify Date" }));
		tblUserRename.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseReleased(final java.awt.event.MouseEvent evt) {
				tblUserRenameMouseReleased(evt);
			}
		});
		jScrollPane3.setViewportView(tblUserRename);

		final org.jdesktop.layout.GroupLayout gl_jPanel8 = new org.jdesktop.layout.GroupLayout(jPanel8);
		jPanel8.setLayout(gl_jPanel8);
		gl_jPanel8.setHorizontalGroup(gl_jPanel8.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 603, Short.MAX_VALUE));
		gl_jPanel8.setVerticalGroup(gl_jPanel8.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE));

		cmdviewUserRenameLog.setMnemonic('v');
		cmdviewUserRenameLog.setText("View");
		cmdviewUserRenameLog.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdviewUserRenameLogActionPerformed(evt);
			}
		});

		final org.jdesktop.layout.GroupLayout gl_jPanel4 = new org.jdesktop.layout.GroupLayout(jPanel4);
		jPanel4.setLayout(gl_jPanel4);
		gl_jPanel4.setHorizontalGroup(gl_jPanel4.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(gl_jPanel4.createSequentialGroup().addContainerGap().add(gl_jPanel4.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel8, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.TRAILING, cmdviewUserRenameLog)).addContainerGap()));
		gl_jPanel4.setVerticalGroup(gl_jPanel4.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(gl_jPanel4.createSequentialGroup().addContainerGap().add(jPanel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 34, Short.MAX_VALUE).add(cmdviewUserRenameLog).addContainerGap()));

		jTabbedPane1.addTab("User Rename Logs", jPanel4);

		cmdviewGroupRenameLog.setMnemonic('v');
		cmdviewGroupRenameLog.setText("View");
		cmdviewGroupRenameLog.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdviewGroupRenameLogActionPerformed(evt);
			}
		});

		tblGroupRename.setAutoCreateColumnsFromModel(false);
		tblGroupRename.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

		}, new String[] { "Old Name", "New Name", "Creation Date", "Modify Date" }));
		tblGroupRename.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseReleased(final java.awt.event.MouseEvent evt) {
				tblGroupRenameMouseReleased(evt);
			}
		});
		jScrollPane4.setViewportView(tblGroupRename);

		final org.jdesktop.layout.GroupLayout gl_jPanel9 = new org.jdesktop.layout.GroupLayout(jPanel9);
		jPanel9.setLayout(gl_jPanel9);
		gl_jPanel9.setHorizontalGroup(gl_jPanel9.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 603, Short.MAX_VALUE));
		gl_jPanel9.setVerticalGroup(gl_jPanel9.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(gl_jPanel9.createSequentialGroup().add(jScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 496, Short.MAX_VALUE).addContainerGap()));

		final org.jdesktop.layout.GroupLayout gl_jPanel5 = new org.jdesktop.layout.GroupLayout(jPanel5);
		jPanel5.setLayout(gl_jPanel5);
		gl_jPanel5.setHorizontalGroup(gl_jPanel5.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(gl_jPanel5.createSequentialGroup().addContainerGap().add(gl_jPanel5.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel9, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.TRAILING, cmdviewGroupRenameLog)).addContainerGap()));
		gl_jPanel5.setVerticalGroup(gl_jPanel5.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(gl_jPanel5.createSequentialGroup().addContainerGap().add(jPanel9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 26, Short.MAX_VALUE)
				.add(cmdviewGroupRenameLog, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap()));

		jTabbedPane1.addTab("Group Rename Logs", jPanel5);

		final GroupLayout layout = new GroupLayout(getContentPane());
		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jTabbedPane1, GroupLayout.DEFAULT_SIZE, 628, Short.MAX_VALUE).addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jTabbedPane1, GroupLayout.PREFERRED_SIZE, 635, GroupLayout.PREFERRED_SIZE).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		getContentPane().setLayout(layout);

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void jButton10ActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton10ActionPerformed
		final ActionListener ax = new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				// //System.out.println(e);
				// //System.out.println(e.getSource().toString());
				txtGroupOwner.setText(getUserselectordata().getUsername());
			}
		};
		final UserSelectorFrame frame = new UserSelectorFrame(ax, getUserselectordata());
		SwingHelper.centerJFrame(frame);
		frame.setVisible(true);
	}// GEN-LAST:event_jButton10ActionPerformed

	private void jButton1ActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed
		this.dispose();
	}// GEN-LAST:event_jButton1ActionPerformed

	private void jButton2ActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton2ActionPerformed
		// TODO add your handling code here:
		final String username = txtUserName.getText();
		IDfSession session = null;
		IDfUser user = null;
		try {
			session = smanager.getSession();
			if (txtUserName.isEditable()) {
				final IDfUser tempuser = session.getUser(username);
				if (tempuser == null) {
					user = (IDfUser) session.newObject("dm_user");
					user.setUserName(username);
					txtUserName.setEditable(false);
				} else {
					JOptionPane.showMessageDialog(null, "User with name '" + username + "' already exists.", "Duplicate name found.", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
			} else {
				user = session.getUser(username);
			}

			user.setUserOSName(txtUserOsName.getText(), null);
			user.setString("user_os_domain", txtUserOsDomain.getText());
			user.setUserAddress(txtUserAddress.getText());
			user.setUserSourceAsString((String) cmbuserSource.getSelectedItem());
			user.setString("user_password", txtUserPassword.getText());
			user.setUserGroupName(txtDefaultGroup.getText());
			user.setDescription(txtUserDescription.getText());
			user.setUserLoginName(txtLoginName.getText());
			user.setDefaultACLEx(acldata.getAclDomain(), acldata.getAclName());
			user.setDefaultFolder(txtDefaultFolder.getText(), false);
			user.setUserDBName(txtUserDbName.getText());
			if (chkUserActive.isSelected()) {
				user.setUserState(0, false);
			} else {
				user.setUserState(1, false);
			}

			if (chkWFEnabled.isSelected()) {
				user.setWorkflowDisabled(false);

			} else {
				user.setWorkflowDisabled(true);
			}

			final String cmbValue = (String) cmbClientCapability.getSelectedItem();
			if (cmbValue.equalsIgnoreCase("consumer")) {
				user.setClientCapability(DfUser.DF_CAPABILITY_CONSUMER);
			}

			if (cmbValue.equalsIgnoreCase("contributor")) {
				user.setClientCapability(DfUser.DF_CAPABILITY_CONTRIBUTOR);
			}

			if (cmbValue.equalsIgnoreCase("coordinator")) {
				user.setClientCapability(DfUser.DF_CAPABILITY_COORDINATOR);
			}

			if (cmbValue.equalsIgnoreCase("system administrator")) {
				user.setClientCapability(DfUser.DF_CAPABILITY_SYSTEM_ADMIN);
			}

			if (cmbValue.equalsIgnoreCase("none")) {
				user.setClientCapability(DfUser.DF_CAPABILITY_NONE);
			}

			user.save();
			SwingHelper.showMessage("User Saved");
		} catch (final DfException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
			log.error(ex);
		} finally {
			if (session != null) {
				smanager.releaseSession(session);
			}

		}
	}// GEN-LAST:event_jButton2ActionPerformed

	private void jButton2MouseClicked(final java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jButton2MouseClicked
		// add your handling code here:
	}// GEN-LAST:event_jButton2MouseClicked

	private void jButton9ActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton9ActionPerformed
		this.dispose();
	}// GEN-LAST:event_jButton9ActionPerformed

	private void jScrollPane2MouseReleased(final java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jScrollPane2MouseReleased

	}// GEN-LAST:event_jScrollPane2MouseReleased

	private void jTabbedPane1StateChanged(final javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_jTabbedPane1StateChanged
		final int indexi = jTabbedPane1.getSelectedIndex();
		if (indexi == 2) {
			this.updateUserRenameTable();
		}

		if (indexi == 3) {
			this.updateGroupRenameTable();

		}

	}// GEN-LAST:event_jTabbedPane1StateChanged

	private void mnuDeleteGroupRenameActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuDeleteGroupRenameActionPerformed
		final String objid = getIDfromGroupTable();
		if (!objid.equals("0000000000000000")) {
			deleteLog(objid);
		}

	}// GEN-LAST:event_mnuDeleteGroupRenameActionPerformed

	private void mnuDeleteUserRenameActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuDeleteUserRenameActionPerformed
		final String objid = getIDfromUserTable();
		if (!objid.equals("0000000000000000")) {
			deleteLog(objid);
		}
	}// GEN-LAST:event_mnuDeleteUserRenameActionPerformed

	private void mnuViewGroupRenameActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuViewGroupRenameActionPerformed
		final String objid = getIDfromGroupTable();
		if (!objid.equals("0000000000000000")) {
			viewLog(objid);
		}
	}// GEN-LAST:event_mnuViewGroupRenameActionPerformed

	private void mnuViewUserRenameActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuViewUserRenameActionPerformed
		final String objid = getIDfromUserTable();
		if (!objid.equals("0000000000000000")) {
			viewLog(objid);
		}
	}// GEN-LAST:event_mnuViewUserRenameActionPerformed

	public void setFolderselectordata(final FolderSelectorData folderselectordata) {
		this.folderselectordata = folderselectordata;
	}

	public void setGroupselectordata(final GroupSelectorData groupselectordata) {
		this.groupselectordata = groupselectordata;
	}

	public void setUserselectordata(final UserSelectorData userselectordata) {
		this.userselectordata = userselectordata;
	}

	private void tblGroupRenameMouseReleased(final java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tblGroupRenameMouseReleased
		final int butt = evt.getButton();
		if (butt == MouseEvent.BUTTON3) {
			popupGruopRename.show(evt.getComponent(), evt.getX(), evt.getY());
		}

	}// GEN-LAST:event_tblGroupRenameMouseReleased

	private void tblUserRenameMouseReleased(final java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tblUserRenameMouseReleased
		final int butt = evt.getButton();
		if (butt == MouseEvent.BUTTON3) {
			popupUserRename.show(evt.getComponent(), evt.getX(), evt.getY());
		}
	}// GEN-LAST:event_tblUserRenameMouseReleased

	private void txtUserPasswordMouseClicked(final java.awt.event.MouseEvent evt) {// GEN-FIRST:event_txtUserPasswordMouseClicked
		// add your handling code here:
	}// GEN-LAST:event_txtUserPasswordMouseClicked

	public void updateGroupRenameTable() {

		grouprenamemodel.setRowCount(0);
		tblGroupRename.setRowHeight(22);

		tblGroupRename.setAutoCreateColumnsFromModel(true);
		grouprenamemodel.setColumnCount(0);
		grouprenamemodel.addColumn(".");
		grouprenamemodel.addColumn("Old Name");
		grouprenamemodel.addColumn("New Name");
		grouprenamemodel.addColumn("Creation Date");
		grouprenamemodel.addColumn("Modify Date");
		grouprenamemodel.addColumn("dd");
		final TableColumn colu = tblGroupRename.getColumnModel().getColumn(0);
		colu.setPreferredWidth(22);
		colu.setMaxWidth(22);

		final int lastIndex = tblGroupRename.getColumnCount();
		tblGroupRename.getColumnModel().removeColumn(tblGroupRename.getColumnModel().getColumn(lastIndex - 1));
		final FormatRenderer formatrenderer = new FormatRenderer();
		formatrenderer.setShowThumbnails(false);

		tblGroupRename.getColumnModel().getColumn(0).setCellRenderer(formatrenderer);

		final String qry = "select r_object_id, r_creation_date, r_modify_date , a_content_type, arguments_values, arguments_keys from dm_job_request where folder('/System/Sysadmin/GroupRename') order by r_creation_date, r_object_id";
		IDfCollection col = null;
		IDfSession session = null;
		final IDfQuery query = new DfQuery();
		query.setDQL(qry);
		try {
			session = smanager.getSession();
			col = query.execute(session, IDfQuery.DF_READ_QUERY);
			while (col.next()) {
				String oldName = "";
				String newName = "";
				final int valcount = col.getValueCount("arguments_values");
				for (int i = 0; i < valcount; i++) {
					final String argname = col.getRepeatingString("arguments_keys", i);
					if (argname.equalsIgnoreCase("oldgroupname")) {
						oldName = col.getRepeatingString("arguments_values", i);
					}

					if (argname.equalsIgnoreCase("newgroupname")) {
						newName = col.getRepeatingString("arguments_values", i);
					}

				}
				final Vector<Object> joo = new Vector<Object>();
				joo.add(col.getString("a_content_type") + ",dm_document");

				joo.add(oldName);
				joo.add(newName);
				joo.add(col.getString("r_creation_date"));
				joo.add(col.getString("r_modify_date"));
				final DokuData data = new DokuData(col.getString("r_object_id"));
				joo.add(data);
				grouprenamemodel.addRow(joo);
			}

			tblGroupRename.validate();

		} catch (final DfException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
			log.error(ex);
		} finally {
			if (col != null) {
				try {
					col.close();
				} catch (final DfException e) {
				}
			}
			if (session != null) {
				smanager.releaseSession(session);
			}

		}
	}

	public void updateUserRenameTable() {
		userrenamemodel.setRowCount(0);
		tblUserRename.setRowHeight(22);
		tblUserRename.setAutoCreateColumnsFromModel(true);
		userrenamemodel.setColumnCount(0);
		userrenamemodel.addColumn(".");
		userrenamemodel.addColumn("Old Name");
		userrenamemodel.addColumn("New Name");
		userrenamemodel.addColumn("Creation Date");
		userrenamemodel.addColumn("Modify Date");
		userrenamemodel.addColumn("dd");
		final TableColumn colu = tblUserRename.getColumnModel().getColumn(0);
		colu.setPreferredWidth(22);
		colu.setMaxWidth(22);

		final int lastIndex = tblUserRename.getColumnCount();
		tblUserRename.getColumnModel().removeColumn(tblUserRename.getColumnModel().getColumn(lastIndex - 1));
		final FormatRenderer formatrenderer = new FormatRenderer();
		formatrenderer.setShowThumbnails(false);

		tblUserRename.getColumnModel().getColumn(0).setCellRenderer(formatrenderer);

		final String qry = "select r_object_id, r_creation_date, r_modify_date, a_content_type, arguments_values, arguments_keys from dm_job_request where folder('/System/Sysadmin/UserRename') order by r_creation_date, r_object_id";
		IDfCollection col = null;
		IDfSession session = null;
		final IDfQuery query = new DfQuery();
		query.setDQL(qry);
		try {
			session = smanager.getSession();
			col = query.execute(session, IDfQuery.DF_READ_QUERY);
			while (col.next()) {
				String oldName = "";
				String newName = "";
				final int valcount = col.getValueCount("arguments_values");
				for (int i = 0; i < valcount; i++) {
					final String argname = col.getRepeatingString("arguments_keys", i);
					if (argname.equalsIgnoreCase("oldusername")) {
						oldName = col.getRepeatingString("arguments_values", i);
					}

					if (argname.equalsIgnoreCase("newusername")) {
						newName = col.getRepeatingString("arguments_values", i);
					}

				}
				final Vector joo = new Vector();
				joo.add(col.getString("a_content_type") + ",dm_document");
				joo.add(oldName);
				joo.add(newName);
				joo.add(col.getString("r_creation_date"));
				joo.add(col.getString("r_modify_date"));
				final DokuData data = new DokuData(col.getString("r_object_id"));
				joo.add(data);
				userrenamemodel.addRow(joo);
			}

			tblUserRename.setModel(userrenamemodel);
			tblUserRename.validate();
		} catch (final DfException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
			log.error(ex);
		} finally {
			if (col != null) {
				try {
					col.close();
				} catch (final DfException e) {
				}
			}
			if (session != null) {
				smanager.releaseSession(session);
			}

		}

	}

	private void userTableMouseReleased(final java.awt.event.MouseEvent evt) {// GEN-FIRST:event_userTableMouseReleased

		txtUserName.setEditable(false);
		final Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
		setCursor(cur);
		final int row = userTable.getSelectedRow();
		final Vector v = (Vector) usertablemodel.getDataVector().elementAt(row);
		final DokuData d = (DokuData) v.elementAt(2);
		final String userid = d.getObjID();
		IDfSession session = null;
		try {
			session = smanager.getSession();
			final IDfId id = new DfId(userid);
			final IDfUser user = (IDfUser) session.getObject(id);
			txtUserPassword.setText("");
			txtUserName.setText(user.getUserName());
			txtUserOsName.setText(user.getUserOSName());
			txtUserOsDomain.setText(user.getUserOSDomain());
			txtUserAddress.setText(user.getUserAddress());
			txtUserDescription.setText(user.getString("description"));
			txtDefaultFolder.setText(user.getDefaultFolder());
			txtDefaultGroup.setText(user.getUserGroupName());
			txtUserACL.setText(user.getACLName() + "@" + user.getACLDomain());
			acldata.setAclName(user.getACLName());
			acldata.setAclDomain(user.getACLDomain());
			txtUserDbName.setText(user.getUserDBName());
			txtUserDescription.setText(user.getDescription());
			txtLoginName.setText(user.getUserLoginName());
			final int userPrivileges = user.getUserPrivileges();
			switch (userPrivileges) {
			case 0:
				cmbUserPrivileges.setSelectedIndex(0);
				break;

			case 1:
				cmbUserPrivileges.setSelectedIndex(1);
				break;

			case 2:
				cmbUserPrivileges.setSelectedIndex(2);
				break;

			case 4:
				cmbUserPrivileges.setSelectedIndex(3);
				break;

			case 8:
				cmbUserPrivileges.setSelectedIndex(4);
				break;

			case 16:
				cmbUserPrivileges.setSelectedIndex(5);
				break;

			}

			final int userstate = user.getUserState();
			final boolean workflow_disabled = user.getBoolean("workflow_disabled");
			if (userstate == 1) {
				chkUserActive.setSelected(false);
			} else {
				chkUserActive.setSelected(true);
			}

			if (workflow_disabled) {
				chkWFEnabled.setSelected(false);
			} else {
				chkWFEnabled.setSelected(true);
			}

			final String joo = user.getUserSourceAsString();
			if (joo.length() < 2) {
				this.cmbuserSource.setSelectedItem(" ");
			} else {
				this.cmbuserSource.setSelectedItem(joo);
			}

			final int cc = user.getClientCapability();
			switch (cc) {
			case 0:
				cmbClientCapability.setSelectedIndex(0);
				break;

			case 1:
				cmbClientCapability.setSelectedIndex(0);
				break;

			case 2:
				cmbClientCapability.setSelectedIndex(1);
				break;

			case 4:
				cmbClientCapability.setSelectedIndex(2);
				break;

			case 8:
				cmbClientCapability.setSelectedIndex(3);
				break;

			}
			// dumpTextArea.setText(dump);
			// dumpTextArea.setBounds(400,400,400,400);
			// dumpFrame.setVisible(true);

		} catch (final DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);

		} finally {
			final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
			setCursor(cur2);
			if (session != null) {
				smanager.releaseSession(session);
			}

		}
	}// GEN-LAST:event_userTableMouseReleased

	private void viewLog(final String objid) {
		// //System.out.println("objid:" + objid);
		IDfSession session = null;
		final Cursor cur = new Cursor(Cursor.WAIT_CURSOR);

		setCursor(cur);
		try {
			session = smanager.getSession();
			final IDfId id = new DfId(objid);
			final IDfSysObject obj = (IDfSysObject) session.getObject(id);

			final String filePath = obj.getFile(null);
			final File fileToOpen = new File(filePath);
			try {
				Desktop.getDesktop().open(fileToOpen);
			} catch (final IOException ex) {
				log.error(ex);
			}

		} catch (final DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
		} finally {
			if (session != null) {
				smanager.releaseSession(session);
			}

			final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);

			setCursor(cur2);
		}
	}
}
