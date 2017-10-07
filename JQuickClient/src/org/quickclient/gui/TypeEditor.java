/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TypeEditor.java
 *
 * Created on 25.12.2011, 9:30:21
 */
package org.quickclient.gui;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;
import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.DokuData;
import org.quickclient.classes.ExJTextArea;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfPersistentObject;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfType;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLogger;
import com.documentum.fc.common.IDfAttr;
import com.documentum.fc.common.IDfId;
import com.documentum.fc.common.IDfList;

/**
 *
 * @author miksuoma
 */
public class TypeEditor extends javax.swing.JFrame {

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(final String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				new TypeEditor().setVisible(true);
			}
		});
	}

	private final DefaultTreeModel treemodel;
	DocuSessionManager smanager;
	Logger log = Logger.getLogger(TypeEditor.class);
	private final QuickClientMutableTreeNode root;

	private String selectedType = "";

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JCheckBox chkRepeating;

	private javax.swing.JComboBox cmbType;

	private javax.swing.JButton cmdAddAttr;

	private javax.swing.JButton cmdClse;

	private javax.swing.JButton cmdCreateDQL;

	private javax.swing.JButton cmdExecute;

	private javax.swing.JButton cmdRemoveAttr;

	private javax.swing.JButton cmdVA;

	private javax.swing.JLabel jLabel1;

	private javax.swing.JLabel jLabel12;

	private javax.swing.JLabel jLabel13;

	private javax.swing.JLabel jLabel14;

	private javax.swing.JLabel jLabel15;

	private javax.swing.JLabel jLabel2;

	private javax.swing.JLabel jLabel3;

	private javax.swing.JLabel jLabel6;

	private javax.swing.JLabel jLabel7;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JPanel jPanel5;
	private javax.swing.JPanel jPanel6;
	private javax.swing.JPanel jPanel7;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JScrollPane jScrollPane3;
	private javax.swing.JLabel lblName;
	private javax.swing.JLabel lblStorage;
	private javax.swing.JLabel lblSuperType;
	private javax.swing.JList lstAttrs;
	private javax.swing.JTree treeTypes;
	private javax.swing.JTextField txtComment;
	private ExJTextArea txtDQL;
	private javax.swing.JTextField txtHelp;
	private javax.swing.JTextField txtLabel;
	private javax.swing.JTextField txtLength;
	private javax.swing.JTextField txtName;
	private JLabel lblSuperType_1;
	private JComboBox cmbSuperType;
	private JTextField txtNewTypeName;

	/** Creates new form TypeEditor */
	public TypeEditor() {
		smanager = DocuSessionManager.getInstance();

		initComponents();

		final java.net.URL imageURL = null;
		final java.net.URL imageURL2 = null;
		final java.net.URL imageURL3 = null;
		final Icon leafIcon = UIManager.getIcon("Tree.leafIcon");
		final Icon openIcon = UIManager.getIcon("Tree.openIcon");
		final Icon closedIcon = UIManager.getIcon("Tree.closedIcon");
		// imageURL = FormatRenderer.class.getResource("fclosed256.gif");
		// imageURL2 = FormatRenderer.class.getResource("fopen256.gif");
		// imageURL3 = FormatRenderer.class.getResource("fclosed256.gif");
		// if (imageURL != null) {
		// leafIcon = new ImageIcon(imageURL);
		// }
		// if (imageURL2 != null) {
		// openIcon = new ImageIcon(imageURL2);
		// }
		// if (imageURL3 != null) {
		// closedIcon = new ImageIcon(imageURL3);
		// }
		//
		// if (leafIcon != null) {
		final DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		renderer.setLeafIcon(leafIcon);
		renderer.setOpenIcon(openIcon);
		renderer.setClosedIcon(closedIcon);
		treeTypes.setCellRenderer(renderer);
		// }
		// Tree.collapsedIcon;
		// Tree.openIcon;

		root = new QuickClientMutableTreeNode("Persistent object");
		treemodel = new DefaultTreeModel(root);
		initAll();
		lblName.setText("");
		lblStorage.setText("");
		lblSuperType.setText("");

	}

	private void cmdAddAttrActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdAddAttrActionPerformed
		// add your handling code here:
		log.debug(evt);
		final int selectedIndex = lstAttrs.getSelectedIndex();
		final DefaultListModel m = (DefaultListModel) lstAttrs.getModel();
		if (selectedIndex == -1) {
			return;
		}
		final String value = (String) m.get(selectedIndex);
		if (!value.equals("*NEW*")) {
			return;
		}

		final String label = txtLabel.getText();
		final String comment = txtComment.getText();
		final String helptext = txtHelp.getText();

		if (selectedType.length() == 0) {
			return;
		}
		if (txtName.getText().length() == 0) {
			return;
		}
		final String datatype = cmbType.getSelectedItem().toString();

		String dql = "alter type " + selectedType + " add " + txtName.getText();
		if (datatype.equals("STRING")) {
			dql = dql + " CHAR(" + txtLength.getText() + ") ";
		} else if (datatype.equals("ID")) {
			dql = dql + " ID ";
		} else if (datatype.equals("TIME")) {
			dql = dql + " DATE ";
		} else if (datatype.equals("INTEGER")) {
			dql = dql + " INTEGER ";
		} else if (datatype.equals("BOOLEAN")) {
			dql = dql + " BOOLEAN ";
		} else if (datatype.equals("DOUBLE")) {
			dql = dql + " DOUBLE ";
		}
		if (chkRepeating.isSelected()) {
			dql = dql + "repeating ";
		}

		if (helptext.length() > 0 || label.length() > 0 || comment.length() > 0) {
			dql = dql + "(";
			boolean commaneeded = false;
			if (helptext.length() > 0) {
				dql = dql + "SET help_text='" + helptext + "'";
				commaneeded = true;
			}
			if (label.length() > 0) {
				if (commaneeded) {
					dql = dql + ", ";
				}
				dql = dql + "SET label_text='" + label + "'";
			}
			if (comment.length() > 0) {
				if (commaneeded) {
					dql = dql + ", ";
				}
				dql = dql + "SET comment_text='" + comment + "'";
			}

			dql = dql + ")";
		}
		txtDQL.setText(dql);
	}// GEN-LAST:event_cmdAddAttrActionPerformed

	public void cmdClseActionPerformed(final ActionEvent e) {
		this.dispose();
	}

	private void cmdCreateDQLActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdCreateDQLActionPerformed

		final TreePath path = treeTypes.getSelectionPath();
		if (path == null) {
			return;
		}

		final QuickClientMutableTreeNode selectedNode = (QuickClientMutableTreeNode) path.getLastPathComponent();
		final String typename = selectedNode.getSpecialString();
		System.out.println("typename: " + typename);
		IDfSession session = null;
		String supertypename = "NULL";
		IDfCollection col = null;
		IDfCollection colx = null;
		try {
			session = smanager.getSession();
			String attrtypename = "";
			final IDfType type = session.getType(typename);
			final IDfType supertype = type.getSuperType();
			if (supertype != null) {
				supertypename = supertype.getName();
			}

			final StringBuffer sb = new StringBuffer();
			sb.append("create type " + typename + " with supertype " + supertypename);
			sb.append("\n");
			sb.append("go\n");

			final IDfQuery q = new DfQuery();
			final ArrayList<String> supertypeattrs = new ArrayList<String>();
			q.setDQL("select attr_name  from dm_type where name = '" + supertypename + "'");
			colx = q.execute(session, IDfQuery.DF_QUERY);
			while (colx.next()) {
				supertypeattrs.add(colx.getString("attr_name"));
			}

			final IDfQuery query = new DfQuery();
			query.setDQL("select attr_name, attr_repeating, attr_type, attr_length from dm_type where name = '" + typename + "'");
			col = query.execute(session, IDfQuery.DF_QUERY);
			while (col.next()) {

				final String attrname = col.getString("attr_name");
				if (!supertypeattrs.contains(attrname)) {
					final boolean isrepeating = col.getBoolean("attr_repeating");
					final int attrtype = col.getInt("attr_type");
					final String alen = col.getString("attr_length");
					switch (attrtype) {
					case IDfAttr.DM_BOOLEAN:
						attrtypename = "BOOLEAN";
						break;
					case IDfAttr.DM_DOUBLE:
						attrtypename = "DOUBLE";
						break;
					case IDfAttr.DM_ID:
						attrtypename = "ID";
						break;
					case IDfAttr.DM_INTEGER:
						attrtypename = "INTEGER";
						break;
					case IDfAttr.DM_STRING:
						attrtypename = "CHAR(" + alen + ")";
						break;
					case IDfAttr.DM_TIME:
						attrtypename = "DATE";
						break;
					case IDfAttr.DM_UNDEFINED:

					default:

					}

					sb.append("alter type " + typename + " add " + attrname + " " + attrtypename + " ");
					if (isrepeating) {
						sb.append("repeating");
					}
					sb.append("\ngo\n");
				}
				IDfCollection col2 = null;

				final IDfQuery query2 = new DfQuery();
				query2.setDQL("select label_text,help_text,comment_text,is_searchable from dmi_dd_attr_info where type_name = '" + typename + "' and attr_name = '" + attrname + "'");
				col2 = query2.execute(session, DfQuery.DF_QUERY);
				while (col2.next()) {

					final String label = col2.getString("label_text");
					final String help = col2.getString("help_text");
					final String comment = col2.getString("comment_text");
					final String isSearchable = col2.getString("is_searchable");

					sb.append("alter type " + typename + " modify (" + attrname + " (set label_text='" + label + "'))");
					sb.append("\ngo\n");
					sb.append("alter type " + typename + " modify (" + attrname + " (set help_text='" + help + "'))");
					sb.append("\ngo\n");
					sb.append("alter type " + typename + " modify (" + attrname + " (set comment_text='" + comment + "'))");
					sb.append("\ngo\n");
					sb.append("alter type " + typename + " modify (" + attrname + " (set is_searchable=" + isSearchable + "))");
					sb.append("\ngo\n");

				}
				col2.close();

			}
			final TextViewer tv = new TextViewer(false);
			tv.setText(sb.toString());
			tv.setVisible(true);
		} catch (final DfException ex) {
			log.error(ex);
		} finally {
			if (session != null) {
				smanager.releaseSession(session);
			}
			if (colx != null) {
				try {
					colx.close();
				} catch (final DfException ex) {
					DfLogger.error(this, "Failed to close collection", null, ex);
				}
			}
			if (col != null) {
				try {
					col.close();
				} catch (final DfException ex) {
					DfLogger.error(this, "Failed to close collection", null, ex);
				}
			}
		}

	}// GEN-LAST:event_cmdCreateDQLActionPerformed

	private void cmdExecuteActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdExecuteActionPerformed

		final String dql = txtDQL.getText();
		IDfCollection col = null;
		IDfSession session = null;
		session = smanager.getSession();

		final IDfQuery query = new DfQuery();
		query.setDQL(dql);
		try {
			System.out.println(session.getClientConfig().dump());
			col = query.execute(session, DfQuery.QUERY);
			while (col.next()) {
				txtDQL.setText(col.dump());

			}
		} catch (final DfException ex) {
			txtDQL.setText(ex.getStackTraceAsString());
			log.error(ex);
		} finally {
			if (col != null) {
				try {
					col.close();
				} catch (final DfException ex) {
					log.error(ex);
				}
			}
			smanager.releaseSession(session);
		}

	}// GEN-LAST:event_cmdExecuteActionPerformed

	private void cmdRemoveAttrActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdRemoveAttrActionPerformed
		// add your handling code here:
		final int selectedIndex = lstAttrs.getSelectedIndex();
		final DefaultListModel m = (DefaultListModel) lstAttrs.getModel();
		if (selectedIndex == -1) {
			return;
		}
		final String value = (String) m.get(selectedIndex);
		if (value.equals("*NEW*")) {
			return;
		}

		final String dql = "alter type " + selectedType + " drop " + value;
		txtDQL.setText(dql);

	}// GEN-LAST:event_cmdRemoveAttrActionPerformed

	private void cmdVAActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdVAActionPerformed

		final TreePath path = treeTypes.getSelectionPath();
		if (path == null) {
			return;
		}
		final QuickClientMutableTreeNode selectedNode = (QuickClientMutableTreeNode) path.getLastPathComponent();
		final String typename = selectedNode.getSpecialString();
		System.out.println("typename: " + typename);
		IDfSession session = null;
		IDfCollection col = null;
		final IDfList valist;
		try {
			session = smanager.getSession();
			final IDfType type = session.getType(typename);
			final StringBuffer sb = new StringBuffer();
			final IDfQuery query = new DfQuery();
			query.setDQL("select attr_name, attr_repeating, attr_type, attr_length from dm_type where name = '" + typename + "'");
			col = query.execute(session, IDfQuery.DF_QUERY);

			while (col.next()) {

				final String attrname = col.getString("attr_name");
				final int attrindex = type.findTypeAttrIndex(attrname);
				final IDfAttr attr = type.getTypeAttr(attrindex);

				IDfCollection col2 = null;
				final IDfQuery query2 = new DfQuery();
				query2.setDQL("select cond_value_assist from dmi_dd_attr_info where type_name = '" + typename + "' and attr_name = '" + attrname + "'");
				col2 = query2.execute(session, DfQuery.DF_QUERY);
				while (col2.next()) {
					final IDfId condid = col2.getId("cond_value_assist");
					if (condid != null) {
						if (condid != DfId.DF_NULLID) {
							final IDfPersistentObject condobj = session.getObject(condid);
							final String defaultid = condobj.getString("default_id");
							if (defaultid.startsWith("5c")) {
								boolean allowcaching = false;
								boolean completelist = false;
								String queryattr = "";
								String querystr = "";
								final IDfPersistentObject vaobj = session.getObject(new DfId(defaultid));
								allowcaching = vaobj.getBoolean("allow_caching");
								completelist = vaobj.getBoolean("complete_list");
								queryattr = vaobj.getString("query_attribute");
								querystr = vaobj.getString("query_string");
								sb.append("alter type ").append(typename).append(" modify (").append(attrname).append(" (");
								sb.append(" value assistance is qry '");
								sb.append(querystr.replace("'", "''"));
								sb.append("' ");
								sb.append("qry attr = ").append(queryattr);
								if (completelist) {
									sb.append(" is complete");
								} else {
									sb.append(" is not complete");
								}
								sb.append("))");
								sb.append("\ngo\n");

							} else {
								final IDfPersistentObject vaobj = session.getObject(new DfId(defaultid));
								boolean completelist = false;
								int validvaluescount = 0;
								completelist = vaobj.getBoolean("complete_list");
								validvaluescount = vaobj.getValueCount("valid_values");
								String values = "";
								for (int i = 0; i < validvaluescount; i++) {
									if (i == 0) {
										values = values + "'" + vaobj.getRepeatingString("valid_values", i);
									} else {
										values = values + "','" + vaobj.getRepeatingString("valid_values", i) + "'";
									}
								}
								sb.append("alter type ");
								sb.append(typename);
								sb.append(" modify (");
								sb.append(attrname);
								sb.append(" (value assistance is list(");
								sb.append(values);
								sb.append(")");
								if (completelist) {
									sb.append(" is complete");
								} else {
									sb.append(" is not complete");
								}
								sb.append("))");
								sb.append("\ngo\n");
							}
						}
					}
				}
				col2.close();
			}

			final TextViewer tv = new TextViewer(false);
			tv.setText(sb.toString());
			tv.setVisible(true);
		} catch (final DfException ex) {
			log.error(ex);
		} finally {
			try {
				col.close();
			} catch (final DfException e) {
				// ss
			}
			if (session != null) {
				smanager.releaseSession(session);
			}

		}

	}// GEN-LAST:event_cmdVAActionPerformed

	protected void createNewType() {

		final String supertype = this.cmbSuperType.getSelectedItem().toString();
		final String typename = this.txtNewTypeName.getText();
		final String DQL = "create type " + typename + " with supertype " + supertype;
		this.txtDQL.setText(DQL);

	}

	public void initAll() {
		initTreeView();
		initTypeCombo();
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
		jScrollPane1 = new javax.swing.JScrollPane();
		jScrollPane1.setBounds(16, 20, 252, 283);
		treeTypes = new javax.swing.JTree();
		jPanel2 = new javax.swing.JPanel();
		cmdCreateDQL = new javax.swing.JButton();
		cmdVA = new javax.swing.JButton();
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		lblName = new javax.swing.JLabel();
		lblSuperType = new javax.swing.JLabel();
		lblStorage = new javax.swing.JLabel();
		jPanel4 = new javax.swing.JPanel();
		jPanel5 = new javax.swing.JPanel();
		jLabel6 = new javax.swing.JLabel();
		txtName = new javax.swing.JTextField();
		jLabel12 = new javax.swing.JLabel();
		cmbType = new javax.swing.JComboBox();
		jLabel13 = new javax.swing.JLabel();
		txtLength = new javax.swing.JTextField();
		jPanel3 = new javax.swing.JPanel();
		cmdAddAttr = new javax.swing.JButton();
		cmdRemoveAttr = new javax.swing.JButton();
		jLabel14 = new javax.swing.JLabel();
		txtLabel = new javax.swing.JTextField();
		chkRepeating = new javax.swing.JCheckBox();
		jScrollPane2 = new javax.swing.JScrollPane();
		lstAttrs = new javax.swing.JList();
		jLabel15 = new javax.swing.JLabel();
		txtHelp = new javax.swing.JTextField();
		jLabel7 = new javax.swing.JLabel();
		txtComment = new javax.swing.JTextField();
		jPanel6 = new javax.swing.JPanel();
		jScrollPane3 = new javax.swing.JScrollPane();
		txtDQL = new ExJTextArea();
		cmdExecute = new javax.swing.JButton();
		jPanel7 = new javax.swing.JPanel();
		cmdClse = new javax.swing.JButton();
		cmdClse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				cmdClseActionPerformed(e);
			}
		});

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

		jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Type Tree"));

		treeTypes.addTreeExpansionListener(new javax.swing.event.TreeExpansionListener() {
			@Override
			public void treeCollapsed(final javax.swing.event.TreeExpansionEvent evt) {
				// not used here
			}

			@Override
			public void treeExpanded(final javax.swing.event.TreeExpansionEvent evt) {
				treeTypesTreeExpanded(evt);
			}
		});
		treeTypes.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
			@Override
			public void valueChanged(final javax.swing.event.TreeSelectionEvent evt) {
				treeTypesValueChanged(evt);
			}
		});
		jScrollPane1.setViewportView(treeTypes);

		getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 280, 320));
		jPanel1.setLayout(null);
		jPanel1.add(jScrollPane1);

		jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Information"));

		cmdCreateDQL.setText("Creation DQL");
		cmdCreateDQL.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdCreateDQLActionPerformed(evt);
			}
		});

		cmdVA.setText("Value Assistance");
		cmdVA.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdVAActionPerformed(evt);
			}
		});

		jLabel1.setText("Name:");

		jLabel2.setText("Supertype:");

		jLabel3.setText("Default Storage:");

		lblName.setText("lblName");

		lblSuperType.setText("lblSupertype");

		lblStorage.setText("lblStorage");

		final javax.swing.GroupLayout gl_jPanel2 = new javax.swing.GroupLayout(jPanel2);
		jPanel2.setLayout(gl_jPanel2);
		gl_jPanel2.setHorizontalGroup(gl_jPanel2.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(gl_jPanel2.createSequentialGroup().addContainerGap().addGroup(gl_jPanel2.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(cmdCreateDQL).addComponent(jLabel1).addComponent(jLabel2).addComponent(jLabel3)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(gl_jPanel2.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(lblName, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE).addComponent(cmdVA).addComponent(lblSuperType, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE).addComponent(lblStorage, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)).addContainerGap()));
		gl_jPanel2.setVerticalGroup(gl_jPanel2.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
				gl_jPanel2.createSequentialGroup().addGroup(gl_jPanel2.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(lblName)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(gl_jPanel2.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(lblSuperType))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(gl_jPanel2.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(lblStorage)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
						.addGroup(gl_jPanel2.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(cmdCreateDQL).addComponent(cmdVA)).addContainerGap()));

		getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 10, 280, 170));

		jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("New Type"));

		lblSuperType_1 = new JLabel("Super Type:");
		lblSuperType_1.setBounds(19, 20, 59, 14);

		getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 180, 280, 150));
		jPanel4.setLayout(null);
		jPanel4.add(lblSuperType_1);

		cmbSuperType = new JComboBox();
		cmbSuperType.setBounds(85, 16, 183, 22);
		jPanel4.add(cmbSuperType);

		final JButton btnCreate = new JButton("Create");
		btnCreate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				createNewType();
			}
		});
		btnCreate.setBounds(177, 70, 91, 23);
		jPanel4.add(btnCreate);

		final JLabel lblTypeName = new JLabel("Type Name:");
		lblTypeName.setBounds(10, 45, 68, 14);
		jPanel4.add(lblTypeName);

		txtNewTypeName = new JTextField();
		txtNewTypeName.setBounds(85, 42, 183, 20);
		jPanel4.add(txtNewTypeName);
		txtNewTypeName.setColumns(10);

		jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

		jLabel6.setText("Name:");

		txtName.setText(" ");

		jLabel12.setText("Type:");

		cmbType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "STRING", "INTEGER", "BOOLEAN", "DOUBLE", "ID", "TIME" }));

		jLabel13.setText("Length:");

		txtLength.setText(" ");
		txtLength.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				txtLengthActionPerformed(evt);
			}
		});

		cmdAddAttr.setText("Add");
		cmdAddAttr.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdAddAttrActionPerformed(evt);
			}
		});

		cmdRemoveAttr.setText("Remove");
		cmdRemoveAttr.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdRemoveAttrActionPerformed(evt);
			}
		});

		final javax.swing.GroupLayout gl_jPanel3 = new javax.swing.GroupLayout(jPanel3);
		jPanel3.setLayout(gl_jPanel3);
		gl_jPanel3.setHorizontalGroup(
				gl_jPanel3.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(gl_jPanel3.createSequentialGroup().addContainerGap(15, Short.MAX_VALUE).addGroup(gl_jPanel3.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(cmdRemoveAttr, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(cmdAddAttr, javax.swing.GroupLayout.Alignment.TRAILING)).addContainerGap()));

		gl_jPanel3.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { cmdAddAttr, cmdRemoveAttr });

		gl_jPanel3.setVerticalGroup(gl_jPanel3.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(gl_jPanel3.createSequentialGroup().addGap(40, 40, 40).addComponent(cmdAddAttr).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(cmdRemoveAttr).addContainerGap(80, Short.MAX_VALUE)));

		jLabel14.setText("Label:");

		txtLabel.setText(" ");

		chkRepeating.setText("Repeating");

		lstAttrs.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			@Override
			public void valueChanged(final javax.swing.event.ListSelectionEvent evt) {
				lstAttrsValueChanged(evt);
			}
		});
		jScrollPane2.setViewportView(lstAttrs);

		jLabel15.setText("Help text:");

		txtHelp.setText(" ");

		jLabel7.setText("Comment:");

		txtComment.setText(" ");

		final javax.swing.GroupLayout gl_jPanel5 = new javax.swing.GroupLayout(jPanel5);
		jPanel5.setLayout(gl_jPanel5);
		gl_jPanel5
				.setHorizontalGroup(
						gl_jPanel5.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(
										gl_jPanel5.createSequentialGroup().addContainerGap()
												.addGroup(gl_jPanel5.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel6).addComponent(jLabel12).addComponent(jLabel13).addComponent(jLabel14).addGroup(gl_jPanel5.createSequentialGroup().addComponent(jLabel15).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
														.addGroup(gl_jPanel5.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtHelp, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
																.addComponent(txtLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtLength, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cmbType, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(chkRepeating)))
														.addGroup(gl_jPanel5.createSequentialGroup().addComponent(jLabel7).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(txtComment, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)))
												.addGap(10, 10, 10).addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE).addContainerGap()));

		gl_jPanel5.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { cmbType, txtHelp, txtLabel, txtLength, txtName });

		gl_jPanel5.setVerticalGroup(gl_jPanel5.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(gl_jPanel5.createSequentialGroup().addContainerGap()
						.addGroup(gl_jPanel5.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(gl_jPanel5.createSequentialGroup().addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE).addContainerGap())
								.addGroup(gl_jPanel5.createSequentialGroup()
										.addGroup(gl_jPanel5.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addGroup(gl_jPanel5.createSequentialGroup().addGroup(gl_jPanel5.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel6).addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
														.addGroup(gl_jPanel5.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel12).addComponent(cmbType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
														.addGroup(gl_jPanel5.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel13).addComponent(txtLength, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
														.addGroup(gl_jPanel5.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel14).addComponent(txtLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
														.addGroup(gl_jPanel5.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel15).addComponent(txtHelp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
														.addGroup(gl_jPanel5.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel7).addComponent(txtComment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
														.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(chkRepeating)))
										.addGap(9, 9, 9)))));

		getContentPane().add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 560, 200));

		jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

		txtDQL.setColumns(20);
		txtDQL.setRows(3);
		jScrollPane3.setViewportView(txtDQL);

		cmdExecute.setText("Execute");
		cmdExecute.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdExecuteActionPerformed(evt);
			}
		});

		final javax.swing.GroupLayout gl_jPanel6 = new javax.swing.GroupLayout(jPanel6);
		jPanel6.setLayout(gl_jPanel6);
		gl_jPanel6.setHorizontalGroup(gl_jPanel6.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, gl_jPanel6.createSequentialGroup().addContainerGap().addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 457, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cmdExecute).addContainerGap()));
		gl_jPanel6.setVerticalGroup(gl_jPanel6.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
				gl_jPanel6.createSequentialGroup().addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(gl_jPanel6.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cmdExecute)).addContainerGap()));

		getContentPane().add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 544, 560, 90));

		cmdClse.setText("Close");

		final javax.swing.GroupLayout gl_jPanel7 = new javax.swing.GroupLayout(jPanel7);
		jPanel7.setLayout(gl_jPanel7);
		gl_jPanel7.setHorizontalGroup(gl_jPanel7.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, gl_jPanel7.createSequentialGroup().addContainerGap(489, Short.MAX_VALUE).addComponent(cmdClse).addContainerGap()));
		gl_jPanel7.setVerticalGroup(gl_jPanel7.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, gl_jPanel7.createSequentialGroup().addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(cmdClse).addContainerGap()));

		getContentPane().add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 630, 560, 40));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void initTreeView() {
		IDfSession session = null;
		IDfCollection col = null;

		try {
			session = smanager.getSession();
			final IDfQuery query = new DfQuery();
			query.setDQL("select name, r_object_id, super_name from dm_type where super_name is nullstring order by name desc");

			col = query.execute(session, IDfQuery.DF_READ_QUERY);

			while (col.next()) {
				final String kakka = col.getString("name");
				final QuickClientMutableTreeNode newNode = new QuickClientMutableTreeNode(kakka);
				final DokuData data = new DokuData(col.getString("r_object_id"));
				newNode.setSpecialString(col.getString("name"));
				newNode.setDokuData(data);
				treemodel.insertNodeInto(newNode, root, 0);

				final QuickClientMutableTreeNode tempNode = new QuickClientMutableTreeNode(kakka);
				treemodel.insertNodeInto(tempNode, newNode, 0);

			}
			treeTypes.setModel(treemodel);
			treeTypes.validate();
			treeTypes.setRootVisible(true);

			treeTypes.setVisible(true);

		} catch (final DfException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
			log.error(ex);
		} finally {
			if (col != null) {
				try {
					col.close();
				} catch (final DfException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
					log.error(ex);
				}
			}
			if (session != null) {
				smanager.releaseSession(session);
			}
		}
	}

	private void initTypeCombo() {
		IDfSession session = null;
		IDfCollection col = null;

		try {
			session = smanager.getSession();
			final IDfQuery query = new DfQuery();
			query.setDQL("select name from dm_type order by 1");
			col = query.execute(session, IDfQuery.DF_READ_QUERY);
			cmbSuperType.addItem("NULL");
			while (col.next()) {
				final String kakka = col.getString("name");
				cmbSuperType.addItem(kakka);
			}

		} catch (final DfException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
			log.error(ex);
		} finally {
			if (col != null) {
				try {
					col.close();
				} catch (final DfException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
					log.error(ex);
				}
			}
			if (session != null) {
				smanager.releaseSession(session);
			}
		}
	}

	private void lstAttrsValueChanged(final javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_lstAttrsValueChanged

		final int selectedIndex = lstAttrs.getSelectedIndex();
		final DefaultListModel m = (DefaultListModel) lstAttrs.getModel();
		if (selectedIndex == -1) {
			return;
		}
		final String value = (String) m.get(selectedIndex);
		IDfCollection col = null;
		IDfSession session = null;
		System.out.println(value);
		if (value.equals("*NEW*")) {
			txtComment.setText("");
			txtHelp.setText("");
			txtLabel.setText("");
			txtLength.setText("32");
			txtName.setText("");
		} else {
			session = smanager.getSession();
			final IDfQuery q = new DfQuery();
			q.setDQL("select * from dmi_dd_attr_info where type_name = '" + selectedType + "' and attr_name = '" + value + "'");
			try {
				final IDfType t = session.getType(selectedType);
				final int ai = t.findTypeAttrIndex(value);
				final IDfAttr a = t.getTypeAttr(ai);
				final boolean isrep = a.isRepeating();
				col = q.execute(session, DfQuery.DF_QUERY);
				while (col.next()) {
					final String attrname = col.getString("attr_name");
					final String label = col.getString("label_text");
					final String help = col.getString("help_text");
					final String comment = col.getString("comment_text");
					final String len = col.getString("domain_length");
					final int attrtype = col.getInt("domain_type");
					txtComment.setText(comment);
					txtHelp.setText(help);
					txtLabel.setText(label);
					txtLength.setText(len);
					txtName.setText(attrname);

					switch (attrtype) {
					case IDfAttr.DM_BOOLEAN:
						cmbType.setSelectedItem("BOOLEAN");
						break;
					case IDfAttr.DM_DOUBLE:
						cmbType.setSelectedItem("DOUBLE");
						break;
					case IDfAttr.DM_ID:
						cmbType.setSelectedItem("ID");
						break;
					case IDfAttr.DM_INTEGER:
						cmbType.setSelectedItem("INTEGER");
						break;
					case IDfAttr.DM_STRING:
						cmbType.setSelectedItem("STRING");
						break;
					case IDfAttr.DM_TIME:
						cmbType.setSelectedItem("TIME");
						break;
					}

				}
				if (isrep) {
					chkRepeating.setSelected(true);
				} else {
					chkRepeating.setSelected(false);
				}
			} catch (final DfException ex) {
				log.error(ex);
			} finally {
				if (session != null) {
					smanager.releaseSession(session);
				}
				if (col != null) {
					try {
						col.close();
					} catch (final DfException ex) {
						ex.printStackTrace();
					}
				}
			}

		}
	}// GEN-LAST:event_lstAttrsValueChanged

	private void treeTypesTreeExpanded(final javax.swing.event.TreeExpansionEvent evt) {// GEN-FIRST:event_treeTypesTreeExpanded

		final Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
		setCursor(cur);
		final TreePath path = evt.getPath();
		final QuickClientMutableTreeNode currNode = (QuickClientMutableTreeNode) path.getLastPathComponent();
		final Vector<Object> myVector = new Vector<Object>();
		final int ccount = currNode.getChildCount();
		for (int i = 0; i < ccount; i++) {
			myVector.add(currNode.getChildAt(i));
		}

		final String nodename = currNode.getSpecialString();
		IDfSession session = null;
		IDfCollection col = null;

		try {
			session = smanager.getSession();
			final IDfQuery query = new DfQuery();
			query.setDQL("select r_object_id, name from dm_type where super_name = '" + nodename + "' order by name desc");
			col = query.execute(session, IDfQuery.DF_READ_QUERY);
			while (col.next()) {
				final String newID = col.getString("r_object_id");
				final String objName = col.getString("name");
				final QuickClientMutableTreeNode newNode = new QuickClientMutableTreeNode(objName);
				final DokuData data = new DokuData(newID);
				newNode.setSpecialString(objName);
				newNode.setDokuData(data);
				treemodel.insertNodeInto(newNode, currNode, 0);

				final QuickClientMutableTreeNode tempNode = new QuickClientMutableTreeNode(objName + "W");
				treemodel.insertNodeInto(tempNode, newNode, 0);

			}
		} catch (final DfException ex) {
			log.error(ex);
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

		final Iterator i = myVector.iterator();
		while (i.hasNext()) {
			final QuickClientMutableTreeNode a = (QuickClientMutableTreeNode) i.next();
			treemodel.removeNodeFromParent(a);
		}
		treeTypes.validate();
		final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
		setCursor(cur2);

	}// GEN-LAST:event_treeTypesTreeExpanded

	private void treeTypesValueChanged(final javax.swing.event.TreeSelectionEvent evt) {// GEN-FIRST:event_treeTypesValueChanged
		// add your handling code here:

		final TreePath path = treeTypes.getSelectionPath();
		if (path == null) {
			return;
		}
		final QuickClientMutableTreeNode selectedNode = (QuickClientMutableTreeNode) path.getLastPathComponent();
		final String typename = selectedNode.getSpecialString();
		// System.out.println("typename: " + typename);
		if (typename.equals("Persistent object")) {
			cmbSuperType.setSelectedItem("NULL");
		} else {
			cmbSuperType.setSelectedItem(typename);

			this.selectedType = typename;
			lblName.setText(typename);
			IDfSession session = null;
			IDfCollection col = null;
			final DefaultListModel model = new DefaultListModel();

			try {
				session = smanager.getSession();
				final IDfType t = session.getType(typename);
				if (t != null) {
					final String supertype = t.getSuperName();
					lblSuperType.setText(supertype);
					final IDfQuery q = new DfQuery();
					q.setDQL("select attr_name from dm_type where name = '" + typename + "' order by 1");
					col = q.execute(session, DfQuery.DF_QUERY);
					model.addElement("*NEW*");
					while (col.next()) {
						final String attr = col.getString("attr_name");
						model.addElement(attr);
					}
				}
			} catch (final DfException ex) {
				// TODO
			} finally {
				if (col != null) {
					try {
						col.close();
					} catch (final DfException ex) {
						ex.printStackTrace();
					}
				}
				if (session != null) {
					smanager.releaseSession(session);
				}
				lstAttrs.setModel(model);
				lstAttrs.validate();
			}
		}

	}// GEN-LAST:event_treeTypesValueChanged

	private void txtLengthActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtLengthActionPerformed
		// add your handling code here:
	}// GEN-LAST:event_txtLengthActionPerformed
}
