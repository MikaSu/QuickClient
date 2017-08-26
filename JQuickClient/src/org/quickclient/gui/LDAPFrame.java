package org.quickclient.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.ExJTextField;
import org.quickclient.classes.SwingHelper;

import net.miginfocom.swing.MigLayout;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfPersistentObject;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLogger;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;


public class LDAPFrame extends JFrame {

	private static final String HASCHANGES = "haschanges";
	private JPanel contentPane;
	private ExJTextField txtName;
	private JComboBox cmbConfigs;
	private ExJTextField txtLDAPHost;
	private ExJTextField txtLDAPPort;
	private ExJTextField txtLDAPDirType;
	private ExJTextField txtBindDN;
	private JPasswordField pwdPass;
	private JButton cmdSave;
	private JPanel ldapAttributePanel;
	private ExJTextField txtPersonObjClass;
	private ExJTextField txtPerSearchBase;
	private ExJTextField txtPerFilter;
	private ExJTextField txtUserSubType;
	private ExJTextField txtGroupObjClass;
	private ExJTextField txtGroupSearchBase;
	private ExJTextField txtGroupFilter;
	private JCheckBox chckbxRenameUsers;
	private JCheckBox chckbxRenameGroups;
	private String objid = null;


	/**
	 * Create the frame.
	 */
	public LDAPFrame() {

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 693, 602);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec.decode("max(115dlu;default):grow"), ColumnSpec.decode("default:grow"), FormFactory.DEFAULT_COLSPEC, FormFactory.DEFAULT_COLSPEC, }, new RowSpec[] { RowSpec.decode("top:30px"), FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"), FormFactory.MIN_ROWSPEC, }));

		JPanel panel_3 = new JPanel();
		contentPane.add(panel_3, "1, 1, 4, 1, left, fill");
		panel_3.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		cmbConfigs = new JComboBox();
		cmbConfigs.setMinimumSize(new Dimension(44, 20));
		cmbConfigs.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				cmbConfigItemStateChanged(arg0);
			}
		});
		cmbConfigs.setModel(new DefaultComboBoxModel(new String[] { "eka", "toka" }));
		panel_3.add(cmbConfigs);

		JButton btnNewButton_2 = new JButton("New button");
		panel_3.add(btnNewButton_2);

		JButton btnNewButton_3 = new JButton("New button");
		panel_3.add(btnNewButton_3);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBorder(new TitledBorder(null, "h", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(tabbedPane, "1, 3, 4, 1, fill, fill");

		ldapAttributePanel = new JPanel();
		ldapAttributePanel.setMinimumSize(new Dimension(30, 10));
		tabbedPane.addTab("Attributes", null, ldapAttributePanel, null);
		ldapAttributePanel.setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec.decode("100px"), ColumnSpec.decode("default:grow"), FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("100px"), FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), ColumnSpec.decode("5px"), },
				new RowSpec[] { RowSpec.decode("top:default"), FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));

		JLabel lblName = new JLabel("Name:");
		ldapAttributePanel.add(lblName, "1, 3, right, default");

		txtName = TextFieldFactory.createJTextField();
		txtName.setText("asdf");
		txtName.setColumns(10);
		ldapAttributePanel.add(txtName, "2, 3, fill, default");

		JLabel lblImportMode = new JLabel("Import Mode:");
		ldapAttributePanel.add(lblImportMode, "4, 3, right, default");

		JComboBox cmbImportMode = new JComboBox();
		ldapAttributePanel.add(cmbImportMode, "6, 3, fill, default");

		JLabel lblLdapHost = new JLabel("LDAP Host:");
		ldapAttributePanel.add(lblLdapHost, "1, 5, right, default");

		txtLDAPHost = TextFieldFactory.createJTextField();
		txtLDAPHost.setText("asdf");
		ldapAttributePanel.add(txtLDAPHost, "2, 5, fill, default");
		txtLDAPHost.setColumns(10);

		JLabel lblLdapPort = new JLabel("LDAP Port:");
		ldapAttributePanel.add(lblLdapPort, "4, 5, right, default");

		txtLDAPPort = TextFieldFactory.createJTextField();
		txtLDAPPort.setText("asdf");
		ldapAttributePanel.add(txtLDAPPort, "6, 5, fill, default");
		txtLDAPPort.setColumns(10);

		JLabel lblLdapType = new JLabel("LDAP type:");
		ldapAttributePanel.add(lblLdapType, "1, 7, right, default");

		txtLDAPDirType = TextFieldFactory.createJTextField();
		txtLDAPDirType.setText("a");
		ldapAttributePanel.add(txtLDAPDirType, "2, 7, fill, default");
		txtLDAPDirType.setColumns(10);

		JLabel lblBindPassword = new JLabel("Bind password:");
		ldapAttributePanel.add(lblBindPassword, "4, 7, right, default");

		pwdPass = new JPasswordField();
		pwdPass.setText("pass");
		ldapAttributePanel.add(pwdPass, "6, 7, fill, default");

		JLabel lblBindDn = new JLabel("Bind DN:");
		ldapAttributePanel.add(lblBindDn, "1, 9, right, default");

		txtBindDN = TextFieldFactory.createJTextField();
		txtBindDN.setText("a");
		ldapAttributePanel.add(txtBindDN, "2, 9, fill, default");
		txtBindDN.setColumns(10);

		JLabel lblPerObjclass = new JLabel("Person objclass:");
		ldapAttributePanel.add(lblPerObjclass, "1, 13, right, default");

		txtPersonObjClass = TextFieldFactory.createJTextField();
		txtPersonObjClass.setText("a");
		ldapAttributePanel.add(txtPersonObjClass, "2, 13, 3, 1, fill, default");
		txtPersonObjClass.setColumns(10);

		chckbxRenameUsers = new JCheckBox("Rename Users");
		ldapAttributePanel.add(chckbxRenameUsers, "6, 13");

		JLabel lblPersonSearchBase = new JLabel("Person search base:");
		ldapAttributePanel.add(lblPersonSearchBase, "1, 15, right, default");

		txtPerSearchBase = TextFieldFactory.createJTextField();
		txtPerSearchBase.setText("a");
		ldapAttributePanel.add(txtPerSearchBase, "2, 15, 5, 1, fill, default");
		txtPerSearchBase.setColumns(10);

		JLabel lblPersonSearchFilter = new JLabel("Person filter:");
		ldapAttributePanel.add(lblPersonSearchFilter, "1, 17, right, default");

		txtPerFilter = TextFieldFactory.createJTextField();
		ldapAttributePanel.add(txtPerFilter, "2, 17, 5, 1, fill, default");
		txtPerFilter.setColumns(10);

		JLabel lblUserSubtype = new JLabel("User subtype:");
		ldapAttributePanel.add(lblUserSubtype, "1, 19, right, default");

		txtUserSubType = TextFieldFactory.createJTextField();
		txtUserSubType.setText("dm_user");
		ldapAttributePanel.add(txtUserSubType, "2, 19, fill, default");
		txtUserSubType.setColumns(10);

		JLabel lblGroupObjclass = new JLabel("Group objclass:");
		ldapAttributePanel.add(lblGroupObjclass, "1, 23, right, default");

		txtGroupObjClass = TextFieldFactory.createJTextField();
		ldapAttributePanel.add(txtGroupObjClass, "2, 23, 3, 1, fill, default");
		txtGroupObjClass.setColumns(10);

		chckbxRenameGroups = new JCheckBox("Rename groups");
		ldapAttributePanel.add(chckbxRenameGroups, "6, 23");

		JLabel lblGroupSearchBase = new JLabel("Group search base:");
		ldapAttributePanel.add(lblGroupSearchBase, "1, 25, right, default");

		txtGroupSearchBase = TextFieldFactory.createJTextField();
		ldapAttributePanel.add(txtGroupSearchBase, "2, 25, 5, 1, fill, default");
		txtGroupSearchBase.setColumns(10);

		JLabel lblGroupFilter = new JLabel("Group filter:");
		ldapAttributePanel.add(lblGroupFilter, "1, 27, right, default");

		txtGroupFilter = TextFieldFactory.createJTextField();
		ldapAttributePanel.add(txtGroupFilter, "2, 27, 5, 1, fill, default");
		txtGroupFilter.setColumns(10);

		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Mapping", null, panel_2, null);
		panel_2.setLayout(new MigLayout("", "[]", "[]"));

		JPanel panel = new JPanel();
		contentPane.add(panel, "1, 4, 4, 1, right, bottom");
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		cmdSave = new JButton("Save");
		cmdSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cmdSaveActionPerformed(arg0);
			}
		});
		panel.add(cmdSave);

		JButton btnNewButton_1 = new JButton("Close");
		panel.add(btnNewButton_1);

		initCombo();
	}

	protected void cmdSaveActionPerformed(ActionEvent arg0) {

		DocuSessionManager dfm = DocuSessionManager.getInstance();
		IDfSession session = dfm.getSession();

		try {
			IDfPersistentObject ldapobj = session.getObjectByQualification("dm_ldap_config where r_object_id = '" + objid + "'");
			if (ldapobj != null) {
				String name = txtName.getText();
				String ldaphost = txtLDAPHost.getText();
				String ldapport = txtLDAPPort.getText();
				String personObjClass = txtPersonObjClass.getText();
				String grpObjClass = txtGroupObjClass.getText();
				String perSearchBase = txtPerSearchBase.getText();
				String grpSearchBase = txtGroupSearchBase.getText();
				String personFilter = txtPerFilter.getText();
				txtGroupFilter.setText(ldapobj.getString("grp_search_filter"));
				txtBindDN.setText(ldapobj.getString("bind_dn"));
				txtUserSubType.setText(ldapobj.getString("user_subtype"));
				chckbxRenameUsers.setSelected(ldapobj.getBoolean("rename_user_option"));
				chckbxRenameGroups.setSelected(ldapobj.getBoolean("rename_group_option"));

				if (haschanges(txtName))
					ldapobj.setString("object_name", name);
				if (haschanges(txtLDAPHost))
					ldapobj.setString("ldap_host", ldaphost);
				if (haschanges(txtLDAPPort))
					ldapobj.setString("port_number", ldapport);
				if (haschanges(txtPersonObjClass))
					ldapobj.setString("person_obj_class", personObjClass);
				if (haschanges(txtGroupObjClass))
					ldapobj.setString("group_obj_class", grpObjClass);
				if (haschanges(txtPerSearchBase))
					ldapobj.setString("per_search_base", perSearchBase);
				ldapobj.save();
			}
		} catch (DfException e) {
			e.printStackTrace();
		} finally {
			dfm.releaseSession(session);
		}

	}

	private boolean haschanges(JTextField jtf) {
		Boolean boo = (Boolean) jtf.getClientProperty(HASCHANGES);
		if (boo != null) {
			if (boo.equals(Boolean.TRUE))
				return true;
			else
				return false;

		}
		return false;
	}

	protected void cmbConfigItemStateChanged(ItemEvent arg0) {
		ComboItem selectedItem = (ComboItem) cmbConfigs.getSelectedItem();
		if (selectedItem != null) {
			this.objid = selectedItem.getAttrName();
			initView();
		}
		resetChangedValues();
	}

	public void initView() {
		if (objid == null) return;
		DocuSessionManager dfm = DocuSessionManager.getInstance();
		IDfSession session = dfm.getSession();
		try {
			IDfPersistentObject ldapobj = session.getObjectByQualification("dm_ldap_config where r_object_id = '" + objid + "'");
			if (ldapobj != null) {
				txtName.setText(ldapobj.getString("object_name"));
				txtLDAPHost.setText(ldapobj.getString("ldap_host"));
				txtLDAPPort.setText(ldapobj.getString("port_number"));
				txtPersonObjClass.setText(ldapobj.getString("person_obj_class"));
				txtGroupObjClass.setText(ldapobj.getString("group_obj_class"));
				txtPerSearchBase.setText(ldapobj.getString("per_search_base"));
				txtGroupSearchBase.setText(ldapobj.getString("grp_search_base"));
				txtPerFilter.setText(ldapobj.getString("per_search_filter"));
				txtGroupFilter.setText(ldapobj.getString("grp_search_filter"));
				txtBindDN.setText(ldapobj.getString("bind_dn"));
				txtUserSubType.setText(ldapobj.getString("user_subtype"));
				chckbxRenameUsers.setSelected(ldapobj.getBoolean("rename_user_option"));
				chckbxRenameGroups.setSelected(ldapobj.getBoolean("rename_group_option"));
			}

		} catch (DfException e) {
			e.printStackTrace();
		} finally {
			dfm.releaseSession(session);
		}

	}

	private void resetChangedValues() {
		Component[] components = ldapAttributePanel.getComponents();
		for (int i = 0; i < components.length; i++) {
			if (components[i].getClass().getName().toString().equals("javax.swing.JTextField")) {
				JTextField j = (JTextField) components[i];
				j.putClientProperty(HASCHANGES, false);
			}
		}
	}

	private void initCombo() {
		DocuSessionManager dfm = DocuSessionManager.getInstance();
		IDfSession session = dfm.getSession();
		IDfCollection col = null;
		cmbConfigs.removeAllItems();
		try {
			IDfQuery query = new DfQuery();
			query.setDQL("select object_name, r_object_id from dm_ldap_config");
			col = query.execute(session, IDfQuery.READ_QUERY);
			while (col.next()) {
				String objname = col.getString("object_name");
				String id = col.getString("r_object_id");
				ComboItem ci = new ComboItem(objname, id, "tur", objname);
				cmbConfigs.addItem(ci);
				if (objid != null) {
					IDfPersistentObject pobj = session.getObject(new DfId(objid));
					String name = pobj.getString("object_name");
					cmbConfigs.setSelectedItem(name);
				}
			}
		} catch (DfException e) {
			DfLogger.error(this, e.getMessage(), null, e);
			SwingHelper.showErrorMessage(e.getMessage(), e.getMessage());
		} finally {
			if (col != null)
				try {
					col.close();
				} catch (DfException e) {
				}
			dfm.releaseSession(session);
		}
		if (cmbConfigs.getItemCount() == 1) {
			this.cmbConfigItemStateChanged(null);
		}
	}

	public void setId(String objid) {
		this.objid = objid;
		
	}
}
