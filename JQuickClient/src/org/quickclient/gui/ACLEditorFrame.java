/*
 * ACLEditorFrame.java
 *
 * Created on 5. marraskuuta 2006, 22:11
 */
package org.quickclient.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JCheckBox;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;
import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.GroupSelectorData;
import org.quickclient.classes.SwingHelper;
import org.quickclient.classes.UserorGroupSelectorData;
import org.quickclient.classes.Utils;

import com.documentum.fc.client.DfPermit;
import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfACL;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfPermit;
import com.documentum.fc.client.IDfPermitType;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfLogger;
import com.documentum.fc.common.IDfList;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;

/**
 *
 * @author Administrator
 */
public class ACLEditorFrame extends javax.swing.JFrame {

	DocuSessionManager smanager;
	Logger log = Logger.getLogger(ACLEditorFrame.class);
	private JMenu jMenuXPermitsDelete;
	private JMenu jMenuXPermitsAdd;
	private JMenuItem mnuAddChangeLoc;
	private JMenuItem mnuAddExecuteProc;
	private JMenuItem mnuAddChangePermit;
	private JMenuItem mnuAddChangeState;
	private JMenuItem mnuAddChangeOwner;
	private JMenuItem mnuAddDeleteObject;
	private JMenuItem mnuAddChangeFolderLinks;
	private JMenuItem mnuDelChangeLoc;
	private JMenuItem mnuDelExecuteProc;
	private JMenuItem mnuDelChangePermit;
	private JMenuItem mnuDelChangeState;
	private JMenuItem mnuDelChangeOwner;
	private JMenuItem mnuDelDeleteObject;
	private JMenuItem mnuDelChangeFolderLinks;
	private String restrictedaccessor;

	private String aclID = "";

	private IDfACL acl;

	private UserorGroupSelectorData userorgroupselectordata;

	private DefaultTableModel permissionmodel;

	private DefaultTableModel restrictionsModel;

	private DefaultTableModel requiredGroupsModel;

	private DefaultTableModel requiredGroupsSetModel;

	private DefaultListModel grouplistmodel;

	private DefaultListModel userlistmodel;

	private boolean isusernamedirty = false;

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JCheckBox chkChangeLocation;

	private javax.swing.JCheckBox chkChangeOwnership;

	private javax.swing.JCheckBox chkChangePermission;

	private javax.swing.JCheckBox chkChangeState;

	private javax.swing.JCheckBox chkDeleteObject;

	private javax.swing.JCheckBox chkExecuteProcedure;

	private javax.swing.JCheckBox chkSelectedGrou;

	private javax.swing.JComboBox cmbACLType;

	private javax.swing.JComboBox cmbPermit;

	private javax.swing.JButton cmdAddGroup;

	private javax.swing.JButton cmdAddRequiredGroup;

	private javax.swing.JButton cmdAddRequiredGroupsSet;

	private javax.swing.JButton cmdAddRestriction;

	private javax.swing.JButton cmdAddUser;

	private javax.swing.JButton cmdGroupQuery;

	private javax.swing.JButton cmdRemoveAccessor;

	private javax.swing.JButton cmdRemoveRequiredGroup;

	private javax.swing.JButton cmdRemoveRequiredGroupsSet;

	private javax.swing.JButton cmdRemoveResctriction;

	private javax.swing.JButton cmdSaveACL;

	private javax.swing.JButton cmdSelectOwner;

	private javax.swing.JButton cmdUserQuery;

	private javax.swing.JButton cmdValidateACL;

	private javax.swing.JButton jButton4;

	private javax.swing.JCheckBox jCheckBox6;

	private javax.swing.JLabel jLabel1;

	private javax.swing.JLabel jLabel2;

	private javax.swing.JLabel jLabel4;

	private javax.swing.JLabel jLabel5;

	private javax.swing.JLabel jLabel6;

	private javax.swing.JLabel jLabel7;

	private javax.swing.JMenu jMenu1;

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

	private javax.swing.JTabbedPane jTabbedPane1;

	private javax.swing.JTable tblRestrictions;

	private javax.swing.JTable tblRequiredGroups;

	private javax.swing.JTable tblRequiredGroupsSet;

	private javax.swing.JLabel lblACLName;

	private javax.swing.JLabel lblInternal;

	private javax.swing.JLabel lblOwner;

	private javax.swing.JList lstGroup;
	private javax.swing.JList lstUser;
	private javax.swing.JMenuItem mnuBrowse;
	private javax.swing.JMenuItem mnuDelete;
	private javax.swing.JMenuItem mnuNone;
	private javax.swing.JMenuItem mnuRead;
	private javax.swing.JMenuItem mnuRelate;
	private javax.swing.JMenuItem mnuRemoveAccessor;
	private javax.swing.JMenuItem mnuVersion;
	private javax.swing.JMenuItem mnuWrite;
	private javax.swing.JPopupMenu popupPermit;
	private javax.swing.JTable tblPermissions;
	private javax.swing.JTextField txtACLDescription;
	private javax.swing.JTextField txtGroupFilter;
	private javax.swing.JTextField txtUserFilter;
	private JPanel panel;
	private JCheckBox chckbxChangeFolderLinks;

	/** Creates new form ACLEditorFrame */
	public ACLEditorFrame(final IDfACL ac) {
		setResizable(false);

		smanager = DocuSessionManager.getInstance();
		setAcl(ac);
		initComponents();
		final UserorGroupSelectorData data = new UserorGroupSelectorData();
		setUserorgroupselectordata(data);
		@SuppressWarnings("serial")
		final DefaultTableModel mod = new DefaultTableModel() {

			@Override
			public boolean isCellEditable(final int row, final int column) {
				return false;
			}
		};
		final DefaultTableModel mod2 = new DefaultTableModel() {

			@Override
			public boolean isCellEditable(final int row, final int column) {
				return false;
			}
		};
		final DefaultTableModel mod3 = new DefaultTableModel() {

			@Override
			public boolean isCellEditable(final int row, final int column) {
				return false;
			}
		};
		final DefaultTableModel mod4 = new DefaultTableModel() {

			@Override
			public boolean isCellEditable(final int row, final int column) {
				return false;
			}
		};
		setPermissionmodel(mod);
		permissionmodel.addColumn(".");
		permissionmodel.addColumn("Accessor");
		permissionmodel.addColumn("Permit");
		permissionmodel.addColumn("Extended Permit");
		tblPermissions.setModel(permissionmodel);

		this.restrictionsModel = mod2;
		this.requiredGroupsModel = mod3;
		this.requiredGroupsSetModel = mod4;
		restrictionsModel.addColumn(".");
		restrictionsModel.addColumn("Accessor");
		requiredGroupsModel.addColumn(".");
		requiredGroupsModel.addColumn("Accessor");
		requiredGroupsSetModel.addColumn(".");
		requiredGroupsSetModel.addColumn("Accessor");
		tblRestrictions.setModel(restrictionsModel);
		tblRequiredGroups.setModel(requiredGroupsModel);
		tblRequiredGroupsSet.setModel(requiredGroupsSetModel);

		initValues();
		final DefaultListModel lm = new DefaultListModel();
		final DefaultListModel lm2 = new DefaultListModel();
		setGrouplistmodel(lm);
		setUserlistmodel(lm2);
		jPanel4.setLayout(new FormLayout(new ColumnSpec[] { FormSpecs.UNRELATED_GAP_COLSPEC, ColumnSpec.decode("158px"), FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("27px"), }, new RowSpec[] { RowSpec.decode("21px"), FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("173px"), }));
		lstGroup.setModel(grouplistmodel);
		jPanel4.add(jScrollPane2, "2, 3, 3, 1, fill, fill");
		jPanel4.add(txtGroupFilter, "2, 1, fill, top");
		jPanel4.add(cmdGroupQuery, "4, 1, left, top");
		jPanel5.setLayout(new FormLayout(new ColumnSpec[] { FormSpecs.UNRELATED_GAP_COLSPEC, ColumnSpec.decode("147px"), FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("27px"), FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("5px"), }, new RowSpec[] { RowSpec.decode("15px"), FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("21px"), FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("157px"), }));
		lstUser.setModel(userlistmodel);
		jPanel5.add(jScrollPane1, "2, 5, 3, 1, fill, fill");
		jPanel5.add(chkSelectedGrou, "2, 1, 3, 1, fill, top");
		jPanel5.add(txtUserFilter, "2, 3, fill, top");
		jPanel5.add(cmdUserQuery, "4, 3, left, top");
		jPanel1.setLayout(new FormLayout(
				new ColumnSpec[] { FormSpecs.UNRELATED_GAP_COLSPEC, ColumnSpec.decode("218px:grow"), FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("221px"), FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("123px"), FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("85px:grow"), FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("79px"), FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("42px"),
						FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("81px"), },
				new RowSpec[] { FormSpecs.UNRELATED_GAP_ROWSPEC, RowSpec.decode("231px"), FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("top:28px"), FormSpecs.RELATED_GAP_ROWSPEC, new RowSpec(RowSpec.FILL, Sizes.bounded(Sizes.DEFAULT, Sizes.constant("75px", false), Sizes.constant("350px", false)), 1), FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("23px:grow"), }));

		panel = new JPanel();
		jPanel1.add(panel, "2, 4, 13, 1, right, fill");
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		cmdValidateACL = new javax.swing.JButton();
		panel.add(cmdValidateACL);

		cmdValidateACL.setText("Validate ACL");
		cmdAddGroup = new javax.swing.JButton();
		panel.add(cmdAddGroup);

		cmdAddGroup.setText("Add Group");
		cmdAddUser = new javax.swing.JButton();
		panel.add(cmdAddUser);

		cmdAddUser.setText("Add User");
		cmdRemoveAccessor = new javax.swing.JButton();
		panel.add(cmdRemoveAccessor);

		cmdRemoveAccessor.setText("Remove Accessor");
		cmdRemoveAccessor.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdRemoveAccessorActionPerformed(evt);
			}
		});
		cmdAddUser.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdAddUserActionPerformed(evt);
			}
		});
		cmdAddGroup.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdAddGroupActionPerformed(evt);
			}
		});
		cmdValidateACL.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdValidateACLActionPerformed(evt);
			}
		});
		jPanel1.add(jPanel7, "2, 6, 13, 1, left, fill");

		final JPanel panel_1 = new JPanel();
		jPanel1.add(panel_1, "10, 8, 5, 1, fill, fill");
		jButton4 = new javax.swing.JButton();
		panel_1.add(jButton4);

		jButton4.setText("Close");
		cmdSaveACL = new javax.swing.JButton();
		panel_1.add(cmdSaveACL);

		cmdSaveACL.setMnemonic('s');
		cmdSaveACL.setText("Save");
		cmdSaveACL.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdSaveACLActionPerformed(evt);
			}
		});
		jButton4.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				jButton4ActionPerformed(evt);
			}
		});
		jPanel1.add(jPanel3, "2, 2, left, fill");
		jPanel1.add(jPanel4, "4, 2, fill, fill");
		jPanel1.add(jPanel5, "6, 2, 3, 1, left, top");
		jPanel1.add(jPanel6, "10, 2, 5, 1, left, fill");
		jScrollPane1.getViewport().setBackground(Color.WHITE);
		jScrollPane2.getViewport().setBackground(Color.WHITE);
		jScrollPane3.getViewport().setBackground(Color.WHITE);
		jScrollPane4.getViewport().setBackground(Color.WHITE);
		jScrollPane5.getViewport().setBackground(Color.WHITE);
		jScrollPane6.getViewport().setBackground(Color.WHITE);

		tblPermissions.getColumnModel().getColumn(0).setCellRenderer(new GroupOrUserRenderer());
		final TableColumn col = tblPermissions.getColumnModel().getColumn(0);
		col.setPreferredWidth(22);
		col.setMaxWidth(22);
		col.setWidth(22);
		final TableColumn col1 = tblPermissions.getColumnModel().getColumn(1);
		col1.setPreferredWidth(150);
		col1.setMaxWidth(150);
		final TableColumn col2 = tblPermissions.getColumnModel().getColumn(2);
		col2.setPreferredWidth(100);
		col2.setMaxWidth(100);
	}

	protected void addRequiredGroup(final ActionEvent e) {
		final GroupSelectorData usergroupselectordata = new GroupSelectorData();
		final ActionListener a = new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				setRequiredGroup(usergroupselectordata.getGroupName());
			}
		};
		final GroupSelectorFrame frame = new GroupSelectorFrame(a, usergroupselectordata);
		SwingHelper.centerJFrame(frame);
		frame.setVisible(true);

	}

	protected void addRequiredGroupSet(final ActionEvent e) {
		final GroupSelectorData usergroupselectordata = new GroupSelectorData();
		final ActionListener a = new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				setRequiredGroupSet(usergroupselectordata.getGroupName());
			}
		};
		final GroupSelectorFrame frame = new GroupSelectorFrame(a, usergroupselectordata);
		SwingHelper.centerJFrame(frame);
		frame.setVisible(true);

	}

	protected void addRestriction(final ActionEvent evt) {
		final GroupSelectorData usergroupselectordata = new GroupSelectorData();
		final ActionListener a = new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				setrestrictedaccessor(usergroupselectordata.getGroupName());
			}
		};
		final GroupSelectorFrame frame = new GroupSelectorFrame(a, usergroupselectordata);
		SwingHelper.centerJFrame(frame);
		frame.setVisible(true);
	}

	private void cmdAddGroupActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdAddGroupActionPerformed

		final int indexi = lstGroup.getSelectedIndex();
		boolean setstandard = false;
		boolean setextended = false;
		final IDfPermit permit = new DfPermit();
		final IDfPermit extpermit = new DfPermit();
		final String permitString = (String) this.cmbPermit.getSelectedItem();

		if (permitString.equals("Extended Permit")) {
			extpermit.setAccessorName((String) grouplistmodel.getElementAt(indexi));
			setextended = true;
		} else if (permitString.equals("Application Permit")) {
			return; // TODO
		} else if (permitString.equals("Access Restriction")) {
			permit.setAccessorName((String) grouplistmodel.getElementAt(indexi));
			permit.setPermitType(IDfPermitType.ACCESS_RESTRICTION);
			// permit.setPermitValue((String) this.cmbPermit.getSelectedItem());
			setstandard = true;
		} else if (permitString.equals("Extended Restriction")) {
			extpermit.setAccessorName((String) grouplistmodel.getElementAt(indexi));
			extpermit.setPermitType(IDfPermitType.EXTENDED_RESTRICTION);
			setextended = true;
		} else if (permitString.equals("Application Restriction")) {
			return; // TODO
		} else if (permitString.equals("Required Group")) {
			permit.setAccessorName((String) grouplistmodel.getElementAt(indexi));
			permit.setPermitType(IDfPermitType.REQUIRED_GROUP);
			permit.setPermitValue((String) this.cmbPermit.getSelectedItem());
			setstandard = true;
		} else if (permitString.equals("Required Group Set")) {
			permit.setAccessorName((String) grouplistmodel.getElementAt(indexi));
			permit.setPermitType(IDfPermitType.REQUIRED_GROUP_SET);
			permit.setPermitValue((String) this.cmbPermit.getSelectedItem());
			setstandard = true;
		} else {
			permit.setAccessorName((String) grouplistmodel.getElementAt(indexi));
			permit.setPermitType(IDfPermitType.ACCESS_PERMIT);
			permit.setPermitValue((String) this.cmbPermit.getSelectedItem());

			extpermit.setAccessorName((String) grouplistmodel.getElementAt(indexi));
			extpermit.setPermitType(IDfPermitType.EXTENDED_PERMIT);
			setstandard = true;
			setextended = true;
		}
		if (chkChangeLocation.isSelected()) {
			extpermit.setPermitValue(IDfACL.DF_XPERMIT_CHANGE_LOCATION_STR);
		}
		if (chkChangeOwnership.isSelected()) {
			extpermit.setPermitValue(IDfACL.DF_XPERMIT_CHANGE_OWNER_STR);
		}
		if (chkChangePermission.isSelected()) {
			extpermit.setPermitValue(IDfACL.DF_XPERMIT_CHANGE_PERMIT_STR);
		}
		if (chkChangeState.isSelected()) {
			extpermit.setPermitValue(IDfACL.DF_XPERMIT_CHANGE_STATE_STR);
		}
		if (chkExecuteProcedure.isSelected()) {
			extpermit.setPermitValue(IDfACL.DF_XPERMIT_EXECUTE_PROC_STR);
		}
		if (chkDeleteObject.isSelected()) {
			extpermit.setPermitValue(IDfACL.DF_XPERMIT_DELETE_OBJECT_STR);
		}
		if (chckbxChangeFolderLinks.isSelected()) {
			extpermit.setPermitValue(IDfACL.DF_XPERMIT_CHANGE_FOLDER_LINKS_STR);
		}
		try {
			boolean needssave = false;
			if (setstandard) {
				acl.grantPermit(permit);
				needssave = true;
			}
			if (setextended) {
				acl.grantPermit(extpermit);
				needssave = true;
			}
			if (needssave) {
				acl.save();
			}
		} catch (final DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);

		}
		this.initValues();
	}// GEN-LAST:event_cmdAddGroupActionPerformed

	private void cmdAddUserActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdAddUserActionPerformed
		final int indexi = lstUser.getSelectedIndex();
		final IDfPermit permit = new DfPermit();
		boolean setstandard = false;
		boolean setextended = false;
		final String permitString = (String) this.cmbPermit.getSelectedItem();
		if (permitString.equals("Extended Permit")) {
			// permit.setAccessorName((String)
			// userlistmodel.getElementAt(indexi));
			// permit.setPermitType(permit.DF_EXTENDED_PERMIT);
			// permit.setPermitValue(permit.DF_EXTENDED_PERMIT_STR);

			setextended = true;
		} else if (permitString.equals("Application Permit")) {
			return; // TODO
		} else if (permitString.equals("Access Restriction")) {
			permit.setAccessorName((String) userlistmodel.getElementAt(indexi));
			permit.setPermitType(IDfPermitType.ACCESS_RESTRICTION);
			permit.setPermitValue((String) this.cmbPermit.getSelectedItem());
		} else if (permitString.equals("Extended Restriction")) {
			permit.setAccessorName((String) userlistmodel.getElementAt(indexi));
			permit.setPermitType(IDfPermitType.EXTENDED_RESTRICTION);
			permit.setPermitValue((String) this.cmbPermit.getSelectedItem());
		} else if (permitString.equals("Application Restriction")) {
			return; // TODO
		} else if (permitString.equals("Required Group")) {
			permit.setAccessorName((String) userlistmodel.getElementAt(indexi));
			permit.setPermitType(IDfPermit.DF_REQUIRED_GROUP);
			permit.setPermitValue((String) this.cmbPermit.getSelectedItem());
		} else if (permitString.equals("Required Group Set")) {
			permit.setAccessorName((String) userlistmodel.getElementAt(indexi));
			permit.setPermitType(IDfPermit.DF_REQUIRED_GROUP_SET);
			permit.setPermitValue((String) this.cmbPermit.getSelectedItem());
		} else {
			permit.setAccessorName((String) userlistmodel.getElementAt(indexi));
			permit.setPermitType(IDfPermit.DF_ACCESS_PERMIT);
			permit.setPermitValue((String) this.cmbPermit.getSelectedItem());
			setstandard = true;
			setextended = true;
		}

		final IDfPermit extpermit = new DfPermit();
		extpermit.setAccessorName((String) userlistmodel.getElementAt(indexi));
		extpermit.setPermitType(IDfPermit.DF_EXTENDED_PERMIT);

		if (chkChangeLocation.isSelected()) {
			extpermit.setPermitValue(IDfACL.DF_XPERMIT_CHANGE_LOCATION_STR);
		}
		if (chkChangeOwnership.isSelected()) {
			extpermit.setPermitValue(IDfACL.DF_XPERMIT_CHANGE_OWNER_STR);
		}
		if (chkChangePermission.isSelected()) {
			extpermit.setPermitValue(IDfACL.DF_XPERMIT_CHANGE_PERMIT_STR);
		}
		if (chkChangeState.isSelected()) {
			extpermit.setPermitValue(IDfACL.DF_XPERMIT_CHANGE_STATE_STR);
		}
		if (chkExecuteProcedure.isSelected()) {
			extpermit.setPermitValue(IDfACL.DF_XPERMIT_EXECUTE_PROC_STR);
		}
		if (chkDeleteObject.isSelected()) {
			extpermit.setPermitValue(IDfACL.DF_XPERMIT_DELETE_OBJECT_STR);
		}
		if (chckbxChangeFolderLinks.isSelected()) {
			extpermit.setPermitValue(IDfACL.DF_XPERMIT_CHANGE_FOLDER_LINKS_STR);
		}

		try {
			boolean needssave = false;
			if (setstandard) {
				acl.grantPermit(permit);
				needssave = true;
			}
			if (setextended) {
				acl.grantPermit(extpermit);
				needssave = true;
			}
			if (needssave) {
				acl.save();
			}

		} catch (final DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);

		}
		this.initValues();
	}// GEN-LAST:event_cmdAddUserActionPerformed

	private void cmdGroupQueryActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdGroupQueryActionPerformed
		grouplistmodel.setSize(0);
		final String filter = txtGroupFilter.getText();

		final IDfQuery query = new DfQuery();
		IDfCollection col = null;
		IDfSession session = null;
		try {
			session = smanager.getSession();
			query.setDQL("select group_name from dm_group where group_name like '" + filter + "%' order by group_name");
			col = query.execute(session, IDfQuery.DF_READ_QUERY);
			while (col.next()) {
				grouplistmodel.addElement(col.getString("group_name"));
			}
		} catch (final DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);

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
		lstGroup.validate();
	}// GEN-LAST:event_cmdGroupQueryActionPerformed

	private void cmdRemoveAccessorActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdRemoveAccessorActionPerformed
		final String accessor = getAccessorFromList();
		try {
			final IDfList l = acl.getPermissions();
			for (int i = 0; i < l.getCount(); i++) {
				final IDfPermit p = (IDfPermit) l.get(i);
				if (p.getAccessorName().equals(accessor)) {
					acl.revokePermit(p);
					acl.save();
				}
			}
		} catch (final DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);

		}
		this.initValues();
	}// GEN-LAST:event_cmdRemoveAccessorActionPerformed

	private void cmdSaveACLActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdSaveACLActionPerformed

		try {
			if (isusernamedirty) {
				acl.setString("owner_name", lblOwner.getText());
			}
			acl.save();
		} catch (final DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);

		}
	}// GEN-LAST:event_cmdSaveACLActionPerformed

	private void cmdSelectOwnerActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdSelectOwnerActionPerformed
		final ActionListener a = new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				// //System.out.println("EVENT:" + e);
				// //System.out.println("EVENT:" + e.getSource().toString());
				lblOwner.setText(getUserorgroupselectordata().getUserorGroupname());
			}
		};
		final UserorGroupSelectorFrame frame = new UserorGroupSelectorFrame(a, getUserorgroupselectordata());
		SwingHelper.centerJFrame(frame);
		frame.setVisible(true);
		this.isusernamedirty = true;
	}// GEN-LAST:event_cmdSelectOwnerActionPerformed

	private void cmdUserQueryActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdUserQueryActionPerformed

		String groupName = "";
		if (chkSelectedGrou.isSelected()) {
			groupName = (String) lstGroup.getSelectedValue();
		}
		userlistmodel.setSize(0);
		final String filter = txtUserFilter.getText();
		final IDfQuery query = new DfQuery();
		IDfCollection col = null;
		IDfSession session = null;
		try {
			session = smanager.getSession();
			if (groupName.equalsIgnoreCase("")) {
				query.setDQL("select user_name from dm_user where r_is_group=0 and user_name like '" + filter + "%' order by user_name");
			} else {
				query.setDQL("select user_name from dm_user where r_is_group=0 and user_name like '" + filter + "%' and user_name in (select users_names from dm_group where group_name = '" + groupName + "')order by user_name");
			}
			col = query.execute(session, IDfQuery.DF_READ_QUERY);
			while (col.next()) {
				userlistmodel.addElement(col.getString("user_name"));
			}
		} catch (final DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);

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
		lstUser.validate();
	}// GEN-LAST:event_cmdUserQueryActionPerformed

	private void cmdValidateACLActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdValidateACLActionPerformed

		JOptionPane.showMessageDialog(null, "Sorry, not implemented yet.", "Oops!", JOptionPane.INFORMATION_MESSAGE);
		// TODO

	}// GEN-LAST:event_cmdValidateACLActionPerformed

	private String getAccessorFromList() {
		final int accessorindex = tblPermissions.getSelectedRow();
		if (accessorindex == -1) {
			return null;
		}
		final Vector v = (Vector) permissionmodel.getDataVector().elementAt(accessorindex);
		final String accessor = (String) v.elementAt(1);
		return accessor;
	}

	private String getAccessorFromRequiredGroupsSetList() {
		final int accessorindex = tblRequiredGroupsSet.getSelectedRow();
		if (accessorindex == -1) {
			return null;
		}
		final Vector v = (Vector) requiredGroupsSetModel.getDataVector().elementAt(accessorindex);
		final String accessor = (String) v.elementAt(1);
		return accessor;
	}

	private String getAccessorFromRequiredList() {
		final int accessorindex = tblRequiredGroups.getSelectedRow();
		if (accessorindex == -1) {
			return null;
		}
		final Vector v = (Vector) requiredGroupsModel.getDataVector().elementAt(accessorindex);
		final String accessor = (String) v.elementAt(1);
		return accessor;
	}

	private String getAccessorFromRestrictedList() {
		final int accessorindex = tblRestrictions.getSelectedRow();
		if (accessorindex == -1) {
			return null;
		}
		final Vector v = (Vector) restrictionsModel.getDataVector().elementAt(accessorindex);
		final String accessor = (String) v.elementAt(1);
		return accessor;
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

		popupPermit = new javax.swing.JPopupMenu();
		mnuRemoveAccessor = new javax.swing.JMenuItem();
		jMenu1 = new javax.swing.JMenu();
		jMenuXPermitsDelete = new javax.swing.JMenu();
		jMenuXPermitsAdd = new javax.swing.JMenu();
		mnuNone = new javax.swing.JMenuItem();
		mnuBrowse = new javax.swing.JMenuItem();
		mnuRead = new javax.swing.JMenuItem();
		mnuRelate = new javax.swing.JMenuItem();
		mnuVersion = new javax.swing.JMenuItem();
		mnuWrite = new javax.swing.JMenuItem();
		mnuDelete = new javax.swing.JMenuItem();
		jTabbedPane1 = new javax.swing.JTabbedPane();
		jPanel1 = new javax.swing.JPanel();
		jPanel3 = new javax.swing.JPanel();
		jLabel2 = new javax.swing.JLabel();
		lblACLName = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		jLabel5 = new javax.swing.JLabel();
		jLabel6 = new javax.swing.JLabel();
		cmbACLType = new javax.swing.JComboBox();
		jCheckBox6 = new javax.swing.JCheckBox();
		jLabel7 = new javax.swing.JLabel();
		txtACLDescription = new javax.swing.JTextField();
		cmdSelectOwner = new javax.swing.JButton();
		lblOwner = new javax.swing.JLabel();
		lblInternal = new javax.swing.JLabel();
		jPanel4 = new javax.swing.JPanel();
		txtGroupFilter = new javax.swing.JTextField();
		cmdGroupQuery = new javax.swing.JButton();
		jScrollPane2 = new javax.swing.JScrollPane();
		lstGroup = new javax.swing.JList();
		jPanel5 = new javax.swing.JPanel();
		cmdUserQuery = new javax.swing.JButton();
		jScrollPane1 = new javax.swing.JScrollPane();
		lstUser = new javax.swing.JList();
		chkSelectedGrou = new javax.swing.JCheckBox();
		txtUserFilter = new javax.swing.JTextField();
		jPanel6 = new javax.swing.JPanel();
		cmbPermit = new javax.swing.JComboBox();
		jLabel1 = new javax.swing.JLabel();
		chkChangeLocation = new javax.swing.JCheckBox();
		chkChangePermission = new javax.swing.JCheckBox();
		chkChangeState = new javax.swing.JCheckBox();
		chkExecuteProcedure = new javax.swing.JCheckBox();
		chkChangeOwnership = new javax.swing.JCheckBox();
		chkDeleteObject = new javax.swing.JCheckBox();
		jPanel7 = new javax.swing.JPanel();
		jScrollPane3 = new javax.swing.JScrollPane();
		tblPermissions = new javax.swing.JTable();
		jPanel2 = new javax.swing.JPanel();
		jPanel8 = new javax.swing.JPanel();
		jScrollPane4 = new javax.swing.JScrollPane();
		tblRestrictions = new javax.swing.JTable();
		cmdRemoveResctriction = new javax.swing.JButton();
		cmdRemoveResctriction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				removeRestriction(e);
			}
		});
		cmdAddRestriction = new javax.swing.JButton();
		cmdAddRestriction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				addRestriction(e);
			}
		});
		jPanel9 = new javax.swing.JPanel();
		jScrollPane5 = new javax.swing.JScrollPane();
		tblRequiredGroups = new javax.swing.JTable();
		cmdRemoveRequiredGroup = new javax.swing.JButton();
		cmdRemoveRequiredGroup.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				removeRequiredGroup(e);
			}
		});
		cmdAddRequiredGroup = new javax.swing.JButton();
		cmdAddRequiredGroup.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				addRequiredGroup(e);
			}
		});
		jPanel10 = new javax.swing.JPanel();
		jScrollPane6 = new javax.swing.JScrollPane();
		tblRequiredGroupsSet = new javax.swing.JTable();
		cmdRemoveRequiredGroupsSet = new javax.swing.JButton();
		cmdRemoveRequiredGroupsSet.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				removeRequiredGroupSet(e);
			}
		});
		cmdAddRequiredGroupsSet = new javax.swing.JButton();
		cmdAddRequiredGroupsSet.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				addRequiredGroupSet(e);
			}
		});

		mnuRemoveAccessor.setText("Remove Accessor");
		mnuRemoveAccessor.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuRemoveAccessorActionPerformed(evt);
			}
		});
		popupPermit.add(mnuRemoveAccessor);

		jMenu1.setText("Set Permission to");

		mnuNone.setText("NONE");
		mnuNone.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuNoneActionPerformed(evt);
			}
		});
		jMenu1.add(mnuNone);

		mnuBrowse.setText("BROWSE");
		mnuBrowse.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuBrowseActionPerformed(evt);
			}
		});
		jMenu1.add(mnuBrowse);

		mnuRead.setText("READ");
		mnuRead.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuReadActionPerformed(evt);
			}
		});
		jMenu1.add(mnuRead);

		mnuRelate.setText("RELATE");
		mnuRelate.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuRelateActionPerformed(evt);
			}
		});
		jMenu1.add(mnuRelate);

		mnuVersion.setText("VERSION");
		mnuVersion.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuVersionActionPerformed(evt);
			}
		});
		jMenu1.add(mnuVersion);

		mnuWrite.setText("WRITE");
		mnuWrite.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuWriteActionPerformed(evt);
			}
		});
		jMenu1.add(mnuWrite);

		mnuDelete.setText("DELETE");
		mnuDelete.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuDeleteActionPerformed(evt);
			}
		});
		jMenu1.add(mnuDelete);

		popupPermit.add(jMenu1);
		popupPermit.add(new JSeparator());
		popupPermit.add(jMenuXPermitsAdd);
		popupPermit.add(jMenuXPermitsDelete);
		jMenuXPermitsAdd.setText("Add XPermit");
		jMenuXPermitsDelete.setText("Revoke XPermit");

		mnuAddChangeLoc = new JMenuItem("Change Location");
		mnuAddExecuteProc = new JMenuItem("Execute Procedure");
		mnuAddChangePermit = new JMenuItem("Change Permit");
		mnuAddChangeState = new JMenuItem("Change State");
		mnuAddChangeOwner = new JMenuItem("Change Owner");
		mnuAddDeleteObject = new JMenuItem("Delete Object");
		mnuAddChangeFolderLinks = new JMenuItem("Change Folder Links");

		mnuAddChangeLoc.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuAddChangeLocActionPerformed(evt);
			}
		});
		mnuAddExecuteProc.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuAddExecuteProcActionPerformed(evt);
			}
		});
		mnuAddChangePermit.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuAddChangePermitActionPerformed(evt);
			}
		});
		mnuAddChangeState.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuAddChangeStateActionPerformed(evt);
			}
		});
		mnuAddChangeOwner.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuAddChangeOwnerActionPerformed(evt);
			}
		});
		mnuAddDeleteObject.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuAddDeleteObjectActionPerformed(evt);
			}
		});
		mnuAddChangeFolderLinks.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuAddChangeFolderLinksActionPerformed(evt);
			}
		});

		jMenuXPermitsAdd.add(mnuAddChangeLoc);
		jMenuXPermitsAdd.add(mnuAddExecuteProc);
		jMenuXPermitsAdd.add(mnuAddChangePermit);
		jMenuXPermitsAdd.add(mnuAddChangeState);
		jMenuXPermitsAdd.add(mnuAddChangeOwner);
		jMenuXPermitsAdd.add(mnuAddDeleteObject);
		jMenuXPermitsAdd.add(mnuAddChangeFolderLinks);

		mnuDelChangeLoc = new JMenuItem("Change Location");
		mnuDelExecuteProc = new JMenuItem("Execute Procedure");
		mnuDelChangePermit = new JMenuItem("Change Permit");
		mnuDelChangeState = new JMenuItem("Change State");
		mnuDelChangeOwner = new JMenuItem("Change Owner");
		mnuDelDeleteObject = new JMenuItem("Delete Object");
		mnuDelChangeFolderLinks = new JMenuItem("Change Folder Links");

		mnuDelChangeLoc.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuDelChangeLocActionPerformed(evt);
			}
		});
		mnuDelExecuteProc.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuDelExecuteProcActionPerformed(evt);
			}
		});
		mnuDelChangePermit.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuDelChangePermitActionPerformed(evt);
			}
		});
		mnuDelChangeState.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuDelChangeStateActionPerformed(evt);
			}
		});
		mnuDelChangeOwner.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuDelChangeOwnerActionPerformed(evt);
			}
		});
		mnuDelDeleteObject.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuDelDeleteObjectActionPerformed(evt);
			}
		});
		mnuDelChangeFolderLinks.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuDelChangeFolderLinksActionPerformed(evt);
			}
		});

		jMenuXPermitsDelete.add(mnuDelChangeLoc);
		jMenuXPermitsDelete.add(mnuDelExecuteProc);
		jMenuXPermitsDelete.add(mnuDelChangePermit);
		jMenuXPermitsDelete.add(mnuDelChangeState);
		jMenuXPermitsDelete.add(mnuDelChangeOwner);
		jMenuXPermitsDelete.add(mnuDelDeleteObject);
		jMenuXPermitsDelete.add(mnuDelChangeFolderLinks);

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setLocationByPlatform(true);

		jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("General Attributes"));
		jPanel3.setAlignmentX(0.2F);
		jPanel3.setAlignmentY(0.2F);

		jLabel2.setText("Name:");

		lblACLName.setText("NAME");

		jLabel4.setText("Owner:");

		jLabel5.setText("Internal:");

		jLabel6.setText("ACL Class:");

		cmbACLType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Regular ACL", "Template ACL", "Template Instance", "Public ACL" }));

		jCheckBox6.setText("Globally Managed");
		jCheckBox6.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		jCheckBox6.setMargin(new java.awt.Insets(0, 0, 0, 0));

		jLabel7.setText("Description:");

		cmdSelectOwner.setText("...");
		cmdSelectOwner.setMargin(new java.awt.Insets(1, 1, 1, 1));
		cmdSelectOwner.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdSelectOwnerActionPerformed(evt);
			}
		});

		lblOwner.setText("OWNER");

		lblInternal.setText("INTERNAL");

		jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Groups"));

		cmdGroupQuery.setText("...");
		cmdGroupQuery.setMargin(new java.awt.Insets(1, 4, 1, 4));
		cmdGroupQuery.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdGroupQueryActionPerformed(evt);
			}
		});

		jScrollPane2.setViewportView(lstGroup);

		jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Users"));

		cmdUserQuery.setText("...");
		cmdUserQuery.setMargin(new java.awt.Insets(1, 4, 1, 4));
		cmdUserQuery.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdUserQueryActionPerformed(evt);
			}
		});

		jScrollPane1.setViewportView(lstUser);

		chkSelectedGrou.setText("From Selected Group");
		chkSelectedGrou.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		chkSelectedGrou.setMargin(new java.awt.Insets(0, 0, 0, 0));

		jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Permission"));

		cmbPermit.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "NONE", "BROWSE", "READ", "RELATE", "VERSION", "WRITE", "DELETE", "Extended Permit", "Application Permit", "Access Restriction", "Extended Restriction", "Application Restriction", "Required Group", "Required Group Set" }));

		jLabel1.setText("Extended Permissions");

		chkChangeLocation.setText("Change Location");
		chkChangeLocation.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		chkChangeLocation.setMargin(new java.awt.Insets(0, 0, 0, 0));

		chkChangePermission.setText("Change Permission");
		chkChangePermission.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		chkChangePermission.setMargin(new java.awt.Insets(0, 0, 0, 0));

		chkChangeState.setText("Change State");
		chkChangeState.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		chkChangeState.setMargin(new java.awt.Insets(0, 0, 0, 0));

		chkExecuteProcedure.setText("Execute Procedure");
		chkExecuteProcedure.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		chkExecuteProcedure.setMargin(new java.awt.Insets(1, 0, 1, 0));

		chkChangeOwnership.setText("Change Ownership");
		chkChangeOwnership.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		chkChangeOwnership.setMargin(new java.awt.Insets(0, 0, 0, 0));

		chkDeleteObject.setText("Delete Object");
		chkDeleteObject.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		chkDeleteObject.setMargin(new java.awt.Insets(0, 0, 0, 0));

		chckbxChangeFolderLinks = new JCheckBox("Change Folder Links");
		chkDeleteObject.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		chckbxChangeFolderLinks.setMargin(new Insets(0, 0, 0, 0));

		final GroupLayout jPanel6Layout = new GroupLayout(jPanel6);
		jPanel6Layout
				.setHorizontalGroup(jPanel6Layout.createParallelGroup(Alignment.LEADING)
						.addGroup(jPanel6Layout.createSequentialGroup().addContainerGap().addGroup(
								jPanel6Layout.createParallelGroup(Alignment.LEADING).addComponent(cmbPermit, GroupLayout.PREFERRED_SIZE, 138, GroupLayout.PREFERRED_SIZE).addComponent(jLabel1).addComponent(chkChangeLocation).addComponent(chkChangePermission).addComponent(chkChangeState).addComponent(chkExecuteProcedure).addComponent(chkChangeOwnership).addComponent(chkDeleteObject).addComponent(chckbxChangeFolderLinks))
								.addContainerGap(44, Short.MAX_VALUE)));
		jPanel6Layout.setVerticalGroup(jPanel6Layout.createParallelGroup(Alignment.LEADING)
				.addGroup(jPanel6Layout.createSequentialGroup().addContainerGap().addComponent(cmbPermit, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(jLabel1).addPreferredGap(ComponentPlacement.RELATED).addComponent(chkChangeLocation).addPreferredGap(ComponentPlacement.RELATED).addComponent(chkChangePermission)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(chkChangeState).addPreferredGap(ComponentPlacement.RELATED).addComponent(chkExecuteProcedure).addPreferredGap(ComponentPlacement.RELATED).addComponent(chkChangeOwnership).addPreferredGap(ComponentPlacement.RELATED).addComponent(chkDeleteObject).addPreferredGap(ComponentPlacement.RELATED).addComponent(chckbxChangeFolderLinks).addGap(0, 0,
								Short.MAX_VALUE)));
		jPanel6.setLayout(jPanel6Layout);

		jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Permissions"));

		jScrollPane3.setBackground(new java.awt.Color(255, 255, 255));

		tblPermissions.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null } }, new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
		tblPermissions.setShowHorizontalLines(false);
		tblPermissions.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseReleased(final java.awt.event.MouseEvent evt) {
				tblPermissionsMouseReleased(evt);
			}
		});
		jScrollPane3.setViewportView(tblPermissions);

		final GroupLayout jPanel7Layout = new GroupLayout(jPanel7);
		jPanel7Layout.setVerticalGroup(jPanel7Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel7Layout.createSequentialGroup().addComponent(jScrollPane3, GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE).addGap(6)));
		jPanel7Layout.setHorizontalGroup(jPanel7Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel7Layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane3, GroupLayout.DEFAULT_SIZE, 853, Short.MAX_VALUE).addContainerGap()));
		jPanel7.setLayout(jPanel7Layout);
		jPanel3.setLayout(new FormLayout(new ColumnSpec[] { FormSpecs.UNRELATED_GAP_COLSPEC, ColumnSpec.decode("42px"), FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, ColumnSpec.decode("70px"), ColumnSpec.decode("46px"), ColumnSpec.decode("21px"), FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("5px"), }, new RowSpec[] { RowSpec.decode("14px"), FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("21px"),
				FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("14px"), FormSpecs.PARAGRAPH_GAP_ROWSPEC, RowSpec.decode("14px"), FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("22px"), FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("15px"), FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("14px"), FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("20px"), }));
		jPanel3.add(jLabel5, "2, 5, left, top");
		jPanel3.add(jLabel7, "2, 13, 3, 1, left, top");
		jPanel3.add(jLabel6, "2, 7, 3, 1, left, top");
		jPanel3.add(jCheckBox6, "2, 11, 3, 1, left, top");
		jPanel3.add(jLabel2, "2, 1, left, top");
		jPanel3.add(lblACLName, "4, 1, left, top");
		jPanel3.add(lblInternal, "4, 5, left, top");
		jPanel3.add(lblOwner, "4, 3, left, center");
		jPanel3.add(cmbACLType, "2, 9, 3, 1, fill, top");
		jPanel3.add(jLabel4, "2, 3, left, center");
		jPanel3.add(cmdSelectOwner, "6, 3, left, top");
		jPanel3.add(txtACLDescription, "2, 15, 5, 1, fill, top");

		jTabbedPane1.addTab("Standard features", jPanel1);

		jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Usage Restrictions (Denies access)"));

		tblRestrictions.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null } }, new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
		jScrollPane4.setViewportView(tblRestrictions);

		cmdRemoveResctriction.setText("Remove");

		cmdAddRestriction.setText("Add");

		final org.jdesktop.layout.GroupLayout jPanel8Layout = new org.jdesktop.layout.GroupLayout(jPanel8);
		jPanel8.setLayout(jPanel8Layout);
		jPanel8Layout.setHorizontalGroup(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel8Layout.createSequentialGroup().addContainerGap()
				.add(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE).add(jPanel8Layout.createSequentialGroup().add(cmdAddRestriction).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(cmdRemoveResctriction))).addContainerGap()));

		jPanel8Layout.linkSize(new java.awt.Component[] { cmdAddRestriction, cmdRemoveResctriction }, org.jdesktop.layout.GroupLayout.HORIZONTAL);

		jPanel8Layout.setVerticalGroup(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel8Layout.createSequentialGroup().add(jScrollPane4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 122, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
				.add(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(cmdRemoveResctriction).add(cmdAddRestriction)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Required Groups (Membership in all of the following groups is required for access)"));

		tblRequiredGroups.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null } }, new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
		jScrollPane5.setViewportView(tblRequiredGroups);

		cmdRemoveRequiredGroup.setText("Remove");

		cmdAddRequiredGroup.setText("Add");

		final org.jdesktop.layout.GroupLayout jPanel9Layout = new org.jdesktop.layout.GroupLayout(jPanel9);
		jPanel9.setLayout(jPanel9Layout);
		jPanel9Layout.setHorizontalGroup(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel9Layout.createSequentialGroup().addContainerGap()
				.add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE).add(jPanel9Layout.createSequentialGroup().add(cmdAddRequiredGroup).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(cmdRemoveRequiredGroup))).addContainerGap()));

		jPanel9Layout.linkSize(new java.awt.Component[] { cmdAddRequiredGroup, cmdRemoveRequiredGroup }, org.jdesktop.layout.GroupLayout.HORIZONTAL);

		jPanel9Layout.setVerticalGroup(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING,
				jPanel9Layout.createSequentialGroup().add(jScrollPane5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(cmdRemoveRequiredGroup).add(cmdAddRequiredGroup)).addContainerGap()));

		jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Required Groups Set (Membership in one of the following groups is required for access)"));

		tblRequiredGroupsSet.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Title 1" }));
		jScrollPane6.setViewportView(tblRequiredGroupsSet);

		cmdRemoveRequiredGroupsSet.setText("Remove");

		cmdAddRequiredGroupsSet.setText("Add");

		final org.jdesktop.layout.GroupLayout jPanel10Layout = new org.jdesktop.layout.GroupLayout(jPanel10);
		jPanel10.setLayout(jPanel10Layout);
		jPanel10Layout.setHorizontalGroup(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel10Layout.createSequentialGroup().addContainerGap()
				.add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE).add(jPanel10Layout.createSequentialGroup().add(cmdAddRequiredGroupsSet).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(cmdRemoveRequiredGroupsSet))).addContainerGap()));

		jPanel10Layout.linkSize(new java.awt.Component[] { cmdAddRequiredGroupsSet, cmdRemoveRequiredGroupsSet }, org.jdesktop.layout.GroupLayout.HORIZONTAL);

		jPanel10Layout.setVerticalGroup(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING,
				jPanel10Layout.createSequentialGroup().add(jScrollPane6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(cmdRemoveRequiredGroupsSet).add(cmdAddRequiredGroupsSet)).addContainerGap()));

		final GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
		jPanel2Layout.linkSize(SwingConstants.VERTICAL, new Component[] { jPanel8, jPanel9, jPanel10 });
		jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addComponent(jPanel8, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(jPanel9, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.RELATED).addComponent(jPanel10, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addContainerGap(92, Short.MAX_VALUE)));
		jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(Alignment.TRAILING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(
				jPanel2Layout.createParallelGroup(Alignment.TRAILING, false).addComponent(jPanel10, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jPanel9, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jPanel8, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addContainerGap(342, Short.MAX_VALUE)));
		jPanel2.setLayout(jPanel2Layout);

		jTabbedPane1.addTab("Extended Features", jPanel2);

		final org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(jTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE).addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(jTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 692, Short.MAX_VALUE).addContainerGap()));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	public void initValues() {
		permissionmodel.setRowCount(0);
		restrictionsModel.setRowCount(0);
		requiredGroupsModel.setRowCount(0);
		requiredGroupsSetModel.setRowCount(0);
		try {
			final Utils util = new Utils();
			txtACLDescription.setText(acl.getDescription());
			lblACLName.setText(acl.getObjectName());
			lblOwner.setText(acl.getString("owner_name"));
			final boolean isinternal = acl.getBoolean("r_is_internal");
			if (isinternal) {
				lblInternal.setText("yes");
			} else {
				lblInternal.setText("no");
			}

			final int accessorcount = acl.getAccessorCount();
			for (int i = 0; i < accessorcount; i++) {
				final Vector<Object> permissionVector = new Vector<Object>();
				Vector<Object> restrictionsVector = null;
				Vector<Object> requiredGroupsVector = null;
				Vector<Object> requiredGroupsSetVector = null;

				final boolean isgroup = acl.getRepeatingBoolean("r_is_group", i);
				if (isgroup) {
					permissionVector.add("1");
				} else {
					permissionVector.add("0");
				}

				permissionVector.add(acl.getRepeatingString("r_accessor_name", i));
				final int permit = acl.getRepeatingInt("r_accessor_permit", i);
				final int permittype = acl.getRepeatingInt("r_permit_type", i);
				if (permittype == 0) {
					permissionVector.add(util.intToPermit(permit));
					permissionVector.add(acl.getAccessorXPermitNames(i));
				} else if (permittype == 1) {
					permissionVector.add("Extended Permit");
					permissionVector.add(acl.getAccessorXPermitNames(i));
				} else if (permittype == 2) {
					permissionVector.add("Application Permit");
					permissionVector.add("");
				} else if (permittype == 3) {
					permissionVector.add("Access Restriction");
					permissionVector.add("");
					restrictionsVector = new Vector<Object>();
					if (isgroup) {
						restrictionsVector.add("1");
					} else {
						restrictionsVector.add("0");
					}
					restrictionsVector.add(acl.getRepeatingString("r_accessor_name", i));
				} else if (permittype == 4) {
					permissionVector.add("Extended Restriction");
					permissionVector.add(acl.getAccessorXPermitNames(i));
				} else if (permittype == 5) {
					permissionVector.add("Application Restriction");
					permissionVector.add("");
				} else if (permittype == 6) {
					permissionVector.add("Required Group");
					permissionVector.add("");
					requiredGroupsVector = new Vector<Object>();
					if (isgroup) {
						requiredGroupsVector.add("1");
					} else {
						requiredGroupsVector.add("0");
					}
					requiredGroupsVector.add(acl.getRepeatingString("r_accessor_name", i));
				} else if (permittype == 7) {
					requiredGroupsSetVector = new Vector<Object>();
					if (isgroup) {
						requiredGroupsSetVector.add("1");
					} else {
						requiredGroupsSetVector.add("0");
					}
					permissionVector.add("Required Group Set");
					permissionVector.add("");
					requiredGroupsSetVector.add(acl.getRepeatingString("r_accessor_name", i));
				}
				permissionmodel.addRow(permissionVector);
				if (restrictionsVector != null) {
					restrictionsModel.addRow(restrictionsVector);
				}
				if (requiredGroupsVector != null) {
					requiredGroupsModel.addRow(requiredGroupsVector);
				}
				if (requiredGroupsSetVector != null) {
					requiredGroupsSetModel.addRow(requiredGroupsSetVector);
				}
			}
		} catch (final DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);

		} finally {
			tblPermissions.validate();
		}

	}

	private void jButton4ActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton4ActionPerformed
		this.dispose();
	}// GEN-LAST:event_jButton4ActionPerformed

	protected void mnuAddChangeFolderLinksActionPerformed(final ActionEvent evt) {
		final IDfPermit extpermit = new DfPermit();
		final String accessor = getAccessorFromList();
		extpermit.setAccessorName(accessor);
		extpermit.setPermitType(IDfPermit.DF_EXTENDED_PERMIT);
		extpermit.setPermitValue(IDfACL.DF_XPERMIT_CHANGE_FOLDER_LINKS_STR);
		try {
			acl.grantPermit(extpermit);
			acl.save();
			this.initValues();

		} catch (final DfException e) {
			SwingHelper.showErrorMessage("error", e.getMessage());
			DfLogger.error(this, "error on revoke", null, e);
		}

	}

	protected void mnuAddChangeLocActionPerformed(final ActionEvent evt) {
		final IDfPermit extpermit = new DfPermit();
		final String accessor = getAccessorFromList();
		extpermit.setAccessorName(accessor);
		extpermit.setPermitType(IDfPermit.DF_EXTENDED_PERMIT);
		extpermit.setPermitValue(IDfACL.DF_XPERMIT_CHANGE_LOCATION_STR);
		try {
			acl.grantPermit(extpermit);
			acl.save();
			this.initValues();

		} catch (final DfException e) {
			SwingHelper.showErrorMessage("error", e.getMessage());
			DfLogger.error(this, "error on revoke", null, e);
		}

	}

	protected void mnuAddChangeOwnerActionPerformed(final ActionEvent evt) {
		final IDfPermit extpermit = new DfPermit();
		final String accessor = getAccessorFromList();
		extpermit.setAccessorName(accessor);
		extpermit.setPermitType(IDfPermit.DF_EXTENDED_PERMIT);
		extpermit.setPermitValue(IDfACL.DF_XPERMIT_CHANGE_OWNER_STR);
		try {
			acl.grantPermit(extpermit);
			acl.save();
			this.initValues();

		} catch (final DfException e) {
			SwingHelper.showErrorMessage("error", e.getMessage());
			DfLogger.error(this, "error on revoke", null, e);
		}

	}

	protected void mnuAddChangePermitActionPerformed(final ActionEvent evt) {
		final IDfPermit extpermit = new DfPermit();
		final String accessor = getAccessorFromList();
		extpermit.setAccessorName(accessor);
		extpermit.setPermitType(IDfPermit.DF_EXTENDED_PERMIT);
		extpermit.setPermitValue(IDfACL.DF_XPERMIT_CHANGE_PERMIT_STR);
		try {
			acl.grantPermit(extpermit);
			acl.save();
			this.initValues();

		} catch (final DfException e) {
			SwingHelper.showErrorMessage("error", e.getMessage());
			DfLogger.error(this, "error on revoke", null, e);
		}

	}

	protected void mnuAddChangeStateActionPerformed(final ActionEvent evt) {
		final IDfPermit extpermit = new DfPermit();
		final String accessor = getAccessorFromList();
		extpermit.setAccessorName(accessor);
		extpermit.setPermitType(IDfPermit.DF_EXTENDED_PERMIT);
		extpermit.setPermitValue(IDfACL.DF_XPERMIT_CHANGE_STATE_STR);
		try {
			acl.grantPermit(extpermit);
			acl.save();
			this.initValues();

		} catch (final DfException e) {
			SwingHelper.showErrorMessage("error", e.getMessage());
			DfLogger.error(this, "error on revoke", null, e);
		}

	}

	protected void mnuAddDeleteObjectActionPerformed(final ActionEvent evt) {
		final IDfPermit extpermit = new DfPermit();
		final String accessor = getAccessorFromList();
		extpermit.setAccessorName(accessor);
		extpermit.setPermitType(IDfPermit.DF_EXTENDED_PERMIT);
		extpermit.setPermitValue(IDfACL.DF_XPERMIT_DELETE_OBJECT_STR);
		try {
			acl.grantPermit(extpermit);
			acl.save();
			this.initValues();

		} catch (final DfException e) {
			SwingHelper.showErrorMessage("error", e.getMessage());
			DfLogger.error(this, "error on revoke", null, e);
		}

	}

	protected void mnuAddExecuteProcActionPerformed(final ActionEvent evt) {
		final IDfPermit extpermit = new DfPermit();
		final String accessor = getAccessorFromList();
		extpermit.setAccessorName(accessor);
		extpermit.setPermitType(IDfPermit.DF_EXTENDED_PERMIT);
		extpermit.setPermitValue(IDfACL.DF_XPERMIT_EXECUTE_PROC_STR);
		try {
			acl.grantPermit(extpermit);
			acl.save();
			this.initValues();

		} catch (final DfException e) {
			SwingHelper.showErrorMessage("error", e.getMessage());
			DfLogger.error(this, "error on revoke", null, e);
		}

	}

	private void mnuBrowseActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuBrowseActionPerformed
		final String accessor = getAccessorFromList();
		try {
			acl.grant(accessor, IDfACL.DF_PERMIT_BROWSE, null);
			acl.save();
		} catch (final DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);

		}
		this.initValues();
	}// GEN-LAST:event_mnuBrowseActionPerformed

	protected void mnuDelChangeFolderLinksActionPerformed(final ActionEvent evt) {
		final IDfPermit extpermit = new DfPermit();
		final String accessor = getAccessorFromList();
		extpermit.setAccessorName(accessor);
		extpermit.setPermitType(IDfPermit.DF_EXTENDED_PERMIT);

		extpermit.setPermitValue(IDfACL.DF_XPERMIT_CHANGE_FOLDER_LINKS_STR);
		try {
			acl.revokePermit(extpermit);
			acl.save();
			this.initValues();
		} catch (final DfException e) {
			SwingHelper.showErrorMessage("error", e.getMessage());
			DfLogger.error(this, "error on revoke", null, e);
		}

	}

	protected void mnuDelChangeLocActionPerformed(final ActionEvent evt) {
		final IDfPermit extpermit = new DfPermit();
		final String accessor = getAccessorFromList();
		extpermit.setAccessorName(accessor);
		extpermit.setPermitType(IDfPermit.DF_EXTENDED_PERMIT);
		extpermit.setPermitValue(IDfACL.DF_XPERMIT_CHANGE_LOCATION_STR);
		try {
			acl.revokePermit(extpermit);
			acl.save();
			this.initValues();

		} catch (final DfException e) {
			SwingHelper.showErrorMessage("error", e.getMessage());
			DfLogger.error(this, "error on revoke", null, e);
		}

	}

	protected void mnuDelChangeOwnerActionPerformed(final ActionEvent evt) {
		final IDfPermit extpermit = new DfPermit();
		final String accessor = getAccessorFromList();
		extpermit.setAccessorName(accessor);
		extpermit.setPermitType(IDfPermit.DF_EXTENDED_PERMIT);
		extpermit.setPermitValue(IDfACL.DF_XPERMIT_CHANGE_OWNER_STR);
		try {
			acl.revokePermit(extpermit);
			acl.save();
			this.initValues();

		} catch (final DfException e) {
			SwingHelper.showErrorMessage("error", e.getMessage());
			DfLogger.error(this, "error on revoke", null, e);
		}

	}

	protected void mnuDelChangePermitActionPerformed(final ActionEvent evt) {
		final IDfPermit extpermit = new DfPermit();
		final String accessor = getAccessorFromList();
		extpermit.setAccessorName(accessor);
		extpermit.setPermitType(IDfPermit.DF_EXTENDED_PERMIT);
		extpermit.setPermitValue(IDfACL.DF_XPERMIT_CHANGE_PERMIT_STR);
		try {
			acl.revokePermit(extpermit);
			acl.save();
			this.initValues();

		} catch (final DfException e) {
			SwingHelper.showErrorMessage("error", e.getMessage());
			DfLogger.error(this, "error on revoke", null, e);
		}

	}

	protected void mnuDelChangeStateActionPerformed(final ActionEvent evt) {
		final IDfPermit extpermit = new DfPermit();
		final String accessor = getAccessorFromList();
		extpermit.setAccessorName(accessor);
		extpermit.setPermitType(IDfPermit.DF_EXTENDED_PERMIT);
		extpermit.setPermitValue(IDfACL.DF_XPERMIT_CHANGE_STATE_STR);
		try {
			acl.revokePermit(extpermit);
			acl.save();
			this.initValues();

		} catch (final DfException e) {
			SwingHelper.showErrorMessage("error", e.getMessage());
			DfLogger.error(this, "error on revoke", null, e);
		}

	}

	protected void mnuDelDeleteObjectActionPerformed(final ActionEvent evt) {
		final IDfPermit extpermit = new DfPermit();
		final String accessor = getAccessorFromList();
		extpermit.setAccessorName(accessor);
		extpermit.setPermitType(IDfPermit.DF_EXTENDED_PERMIT);

		extpermit.setPermitValue(IDfACL.DF_XPERMIT_DELETE_OBJECT_STR);
		try {
			acl.revokePermit(extpermit);
			acl.save();
			this.initValues();

		} catch (final DfException e) {
			SwingHelper.showErrorMessage("error", e.getMessage());
			DfLogger.error(this, "error on revoke", null, e);
		}

	}

	private void mnuDeleteActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuDeleteActionPerformed
		final String accessor = getAccessorFromList();
		try {
			acl.grant(accessor, IDfACL.DF_PERMIT_DELETE, null);
			acl.save();
		} catch (final DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);

		}
		this.initValues();
	}// GEN-LAST:event_mnuDeleteActionPerformed

	protected void mnuDelExecuteProcActionPerformed(final ActionEvent evt) {
		final IDfPermit extpermit = new DfPermit();
		final String accessor = getAccessorFromList();
		extpermit.setAccessorName(accessor);
		extpermit.setPermitType(IDfPermit.DF_EXTENDED_PERMIT);
		extpermit.setPermitValue(IDfACL.DF_XPERMIT_EXECUTE_PROC_STR);
		try {
			acl.revokePermit(extpermit);
			acl.save();
			this.initValues();

		} catch (final DfException e) {
			SwingHelper.showErrorMessage("error", e.getMessage());
			DfLogger.error(this, "error on revoke", null, e);
		}

	}

	private void mnuNoneActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuNoneActionPerformed
		final String accessor = getAccessorFromList();
		try {
			acl.grant(accessor, IDfACL.DF_PERMIT_NONE, null);
			acl.save();
		} catch (final DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);

		}
		this.initValues();
	}// GEN-LAST:event_mnuNoneActionPerformed

	private void mnuReadActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuReadActionPerformed
		final String accessor = getAccessorFromList();
		try {
			acl.grant(accessor, IDfACL.DF_PERMIT_READ, null);
			acl.save();
		} catch (final DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);

		}
		this.initValues();
	}// GEN-LAST:event_mnuReadActionPerformed

	private void mnuRelateActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuRelateActionPerformed
		final String accessor = getAccessorFromList();
		try {
			acl.grant(accessor, IDfACL.DF_PERMIT_RELATE, null);
			acl.save();
		} catch (final DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);

		}
		this.initValues();
	}// GEN-LAST:event_mnuRelateActionPerformed

	private void mnuRemoveAccessorActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuRemoveAccessorActionPerformed
		final String accessor = getAccessorFromList();
		try {

			final IDfList l = acl.getPermissions();
			for (int i = 0; i < l.getCount(); i++) {
				final IDfPermit p = (IDfPermit) l.get(i);
				if (p.getAccessorName().equals(accessor)) {
					acl.revokePermit(p);
					acl.save();
				}
			}
			// acl.revoke(accessor, null);
			// acl.save();
		} catch (final DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);

		}
		this.initValues();
	}// GEN-LAST:event_mnuRemoveAccessorActionPerformed

	private void mnuVersionActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuVersionActionPerformed
		final String accessor = getAccessorFromList();
		try {
			acl.grant(accessor, IDfACL.DF_PERMIT_VERSION, null);
			acl.save();
		} catch (final DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);

		}
		this.initValues();
	}// GEN-LAST:event_mnuVersionActionPerformed

	private void mnuWriteActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuWriteActionPerformed
		final String accessor = getAccessorFromList();

		try {
			acl.grant(accessor, IDfACL.DF_PERMIT_WRITE, null);
			acl.save();
		} catch (final DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);

		}
		this.initValues();
	}// GEN-LAST:event_mnuWriteActionPerformed

	protected void removeRequiredGroup(final ActionEvent e) {
		final IDfPermit extpermit = new DfPermit();
		final String accessor = getAccessorFromRequiredList();
		extpermit.setAccessorName(accessor);
		extpermit.setPermitType(IDfPermit.DF_REQUIRED_GROUP);
		try {
			acl.revokePermit(extpermit);
			acl.save();
			this.initValues();
		} catch (final DfException ex) {
			SwingHelper.showErrorMessage("error", ex.getMessage());
			DfLogger.error(this, "error on revoke", null, ex);
		}

	}

	protected void removeRequiredGroupSet(final ActionEvent e) {
		final IDfPermit extpermit = new DfPermit();
		final String accessor = getAccessorFromRequiredGroupsSetList();
		extpermit.setAccessorName(accessor);
		extpermit.setPermitType(IDfPermit.DF_REQUIRED_GROUP_SET);
		try {
			acl.revokePermit(extpermit);
			acl.save();
			this.initValues();
		} catch (final DfException ex) {
			SwingHelper.showErrorMessage("error", ex.getMessage());
			DfLogger.error(this, "error on revoke", null, ex);
		}

	}

	protected void removeRestriction(final ActionEvent evt) {
		final IDfPermit extpermit = new DfPermit();
		final String accessor = getAccessorFromRestrictedList();
		extpermit.setAccessorName(accessor);
		extpermit.setPermitType(IDfPermit.DF_ACCESS_RESTRICTION);
		try {
			acl.revokePermit(extpermit);
			acl.save();
			this.initValues();
		} catch (final DfException e) {
			SwingHelper.showErrorMessage("error", e.getMessage());
			DfLogger.error(this, "error on revoke", null, e);
		}

	}

	public void setAcl(final IDfACL acl) {
		this.acl = acl;
	}

	public void setAclID(final String aclID) {
		this.aclID = aclID;
	}

	public void setGrouplistmodel(final DefaultListModel grouplistmodel) {
		this.grouplistmodel = grouplistmodel;
	}

	public void setPermissionmodel(final DefaultTableModel permissionmodel) {
		this.permissionmodel = permissionmodel;
	}

	protected void setRequiredGroup(final String groupName) {
		final IDfPermit extpermit = new DfPermit();
		if (groupName == null) {
			return;
		}
		extpermit.setAccessorName(groupName);
		extpermit.setPermitType(IDfPermit.DF_REQUIRED_GROUP);
		try {
			acl.grantPermit(extpermit);
			acl.save();
			this.initValues();
		} catch (final DfException e) {
			SwingHelper.showErrorMessage("error", e.getMessage());
			DfLogger.error(this, "error on grant", null, e);
		}

	}

	protected void setRequiredGroupSet(final String groupName) {
		final IDfPermit extpermit = new DfPermit();
		if (groupName == null) {
			return;
		}
		extpermit.setAccessorName(groupName);
		extpermit.setPermitType(IDfPermit.DF_REQUIRED_GROUP_SET);
		try {
			acl.grantPermit(extpermit);
			acl.save();
			this.initValues();
		} catch (final DfException e) {
			SwingHelper.showErrorMessage("error", e.getMessage());
			DfLogger.error(this, "error on grant", null, e);
		}

	}

	protected void setrestrictedaccessor(final String groupName) {
		final IDfPermit extpermit = new DfPermit();
		if (groupName == null) {
			return;
		}
		extpermit.setAccessorName(groupName);
		extpermit.setPermitType(IDfPermit.DF_EXTENDED_RESTRICTION);
		try {
			acl.grantPermit(extpermit);
			acl.save();
			this.initValues();
		} catch (final DfException e) {
			SwingHelper.showErrorMessage("error", e.getMessage());
			DfLogger.error(this, "error on grant", null, e);
		}

	}

	public void setUserlistmodel(final DefaultListModel userlistmodel) {
		this.userlistmodel = userlistmodel;
	}

	public void setUserorgroupselectordata(final UserorGroupSelectorData userorgroupselectordata) {
		this.userorgroupselectordata = userorgroupselectordata;
	}

	private void tblPermissionsMouseReleased(final java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tblPermissionsMouseReleased
		final int butt = evt.getButton();
		// //System.out.println("butt on: " + butt);
		if (butt == MouseEvent.BUTTON3) {
			popupPermit.show(evt.getComponent(), evt.getX(), evt.getY());
		}
	}// GEN-LAST:event_tblPermissionsMouseReleased
}
