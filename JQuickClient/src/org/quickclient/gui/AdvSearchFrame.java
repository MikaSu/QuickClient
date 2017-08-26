/*
 * AdvSearchFrame.java
 *
 * Created on 10. heinäkuuta 2007, 12:45
 */
package org.quickclient.gui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Date;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;
import org.quickclient.classes.ConfigService;
import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.ListAttribute;
import org.quickclient.classes.SwingHelper;
import org.quickclient.classes.Utils;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfType;
import com.documentum.fc.client.IDfValidator;
import com.documentum.fc.client.IDfValueAssistance;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfTime;
import com.documentum.fc.common.IDfList;
import com.documentum.fc.common.IDfTime;
import com.toedter.calendar.JDateChooser;


/**
 * 
 * @author Administrator
 */
public class AdvSearchFrame extends javax.swing.JFrame {

	private DocuSessionManager smanager;
	private Vector<ListAttribute> listattributes;
	private JDesktopPane desktop;
	IDfType tyyppi;
	IDfValidator validator;
	JComboBox vacombo1;
	JComboBox vacombo2;
	JComboBox vacombo3;
	JComboBox vacombo4;
	private boolean ispopulated = false;
	Logger log = Logger.getLogger(AdvSearchFrame.class);
	private String orderByString;

	/** Creates new form AdvSearchFrame */
	public AdvSearchFrame() {
		setTitle("Search");
		smanager = DocuSessionManager.getInstance();
		isloading = true;
		ConfigService cs = ConfigService.getInstance();
		
		this.orderByString = cs.getAttributes(cs.getCurrentListConfigName()).orderby;
		listattributes = cs.getAttributes(cs.getCurrentListConfigName()).get();
		for (int i = 0; i < listattributes.size(); i++) {
			ListAttribute a = listattributes.get(i);
			if (a.type.equals("dm_sysobject"))
				additionalqueryattributes = additionalqueryattributes + ", " + listattributes.get(i).attribute;

		}

		initComponents();

		initializeColumns();

		jdc1 = new JDateChooser();
		jdc2 = new JDateChooser();
		jdc3 = new JDateChooser();
		jdc4 = new JDateChooser();

		jdc1b = new JDateChooser();
		jdc2b = new JDateChooser();
		jdc3b = new JDateChooser();
		jdc4b = new JDateChooser();

		jdc1.setDateFormatString("d.M.yyyy");
		jdc2.setDateFormatString("d.M.yyyy");
		jdc3.setDateFormatString("d.M.yyyy");
		jdc4.setDateFormatString("d.M.yyyy");
		jdc1b.setDateFormatString("d.M.yyyy");
		jdc2b.setDateFormatString("d.M.yyyy");
		jdc3b.setDateFormatString("d.M.yyyy");
		jdc4b.setDateFormatString("d.M.yyyy");

		createdDateFirst.setDateFormatString("d.M.yyyy");
		createdDateLast.setDateFormatString("d.M.yyyy");
		modifiedDateFirst.setDateFormatString("d.M.yyyy");
		modifiedDateLast.setDateFormatString("d.M.yyyy");

		hakuPanel1.removeAll();
		hakuPanel2.removeAll();
		hakuPanel3.removeAll();
		hakuPanel4.removeAll();

		txt1 = new JTextField();
		txt2 = new JTextField();
		txt3 = new JTextField();
		txt4 = new JTextField();

		vacombo1 = new JComboBox();
		vacombo2 = new JComboBox();
		vacombo3 = new JComboBox();
		vacombo4 = new JComboBox();

		Dimension d = new Dimension();
		d.setSize(250, 23);
		txt1.setPreferredSize(d);
		txt2.setPreferredSize(d);
		txt3.setPreferredSize(d);
		txt4.setPreferredSize(d);

		vacombo1.setPreferredSize(d);
		vacombo2.setPreferredSize(d);
		vacombo3.setPreferredSize(d);
		vacombo4.setPreferredSize(d);

		vacombo1.setEditable(true);
		vacombo2.setEditable(true);
		vacombo3.setEditable(true);
		vacombo4.setEditable(true);

		vacombo1.addMouseWheelListener(new MouseWheelListener() {

			public void mouseWheelMoved(MouseWheelEvent e) {
				int maxindex = vacombo1.getItemCount();
				if (e.getWheelRotation() > 0) {
					int index = vacombo1.getSelectedIndex();
					if (index < maxindex - 1) {
						vacombo1.setSelectedIndex(index + 1);
					}
				} else {
					int index = vacombo1.getSelectedIndex();
					if (index > 0) {
						vacombo1.setSelectedIndex(index - 1);
					}
				}
			}
		});

		vacombo2.addMouseWheelListener(new MouseWheelListener() {

			public void mouseWheelMoved(MouseWheelEvent e) {
				int maxindex = vacombo2.getItemCount();
				if (e.getWheelRotation() > 0) {
					int index = vacombo2.getSelectedIndex();
					if (index < maxindex - 1) {
						vacombo2.setSelectedIndex(index + 1);
					}
				} else {
					int index = vacombo2.getSelectedIndex();
					if (index > 0) {
						vacombo2.setSelectedIndex(index - 1);
					}
				}
			}
		});
		vacombo3.addMouseWheelListener(new MouseWheelListener() {

			public void mouseWheelMoved(MouseWheelEvent e) {
				int maxindex = vacombo3.getItemCount();
				if (e.getWheelRotation() > 0) {
					int index = vacombo3.getSelectedIndex();
					if (index < maxindex - 1) {
						vacombo3.setSelectedIndex(index + 1);
					}
				} else {
					int index = vacombo3.getSelectedIndex();
					if (index > 0) {
						vacombo3.setSelectedIndex(index - 1);
					}
				}
			}
		});
		vacombo4.addMouseWheelListener(new MouseWheelListener() {

			public void mouseWheelMoved(MouseWheelEvent e) {
				int maxindex = vacombo4.getItemCount();
				if (e.getWheelRotation() > 0) {
					int index = vacombo4.getSelectedIndex();
					if (index < maxindex - 1) {
						vacombo4.setSelectedIndex(index + 1);
					}
				} else {
					int index = vacombo4.getSelectedIndex();
					if (index > 0) {
						vacombo4.setSelectedIndex(index - 1);
					}
				}
			}
		});
		hakuPanel1.setLayout(new GridLayout(1, 1));
		hakuPanel2.setLayout(new GridLayout(1, 1));
		hakuPanel3.setLayout(new GridLayout(1, 1));
		hakuPanel4.setLayout(new GridLayout(1, 1));

		hakuPanel1.add(txt1);
		txt1.setVisible(true);
		hakuPanel1.revalidate();
		hakuPanel1.repaint();

		cmbAttr2.setVisible(false);
		cmbAttr3.setVisible(false);
		cmbAttr4.setVisible(false);
		cmbEhto2.setVisible(false);
		cmbEhto3.setVisible(false);
		cmbEhto4.setVisible(false);
		cmbANDOR2.setVisible(false);
		cmbANDOR3.setVisible(false);
		cmbANDOR4.setVisible(false);
		populateTypeCombo();
		FolderSelectorData xx = new FolderSelectorData();
		setFolderselectordata(xx);
		cmbAttr1.setSelectedItem("Name");
		isloading = false;
	}

	public void initializeColumns() {
		model = new DefaultTableModel() {

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		model.setColumnCount(0);
		// default column values
		model.addColumn(".");
		model.addColumn(".");
		model.addColumn("object_name");
		// additional column values
		int xx = listattributes.size();

		for (int i = 0; i < xx; i++) {
			model.addColumn(listattributes.get(i));
		}
		model.addColumn("data");
		/*
		 * objectTable.setModel(tablemodel);
		 * objectTable.getColumnModel().getColumn(0).setCellRenderer(new
		 * LockRenderer());
		 * objectTable.getColumnModel().getColumn(1).setCellRenderer(new
		 * FormatRenderer());
		 * objectTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		 * objectTable.setEditingRow(1); objectTable.setRowHeight(22); for (int
		 * i = 0; i < 3; i++) { TableColumn col =
		 * objectTable.getColumnModel().getColumn(i); if (i == 0 || i == 1) {
		 * col.setPreferredWidth(22); col.setMaxWidth(22); } else {
		 * col.setPreferredWidth(200); } } int lastIndex =
		 * objectTable.getColumnCount();
		 * objectTable.getColumnModel().removeColumn
		 * (objectTable.getColumnModel().getColumn(lastIndex - 1));
		 */
	}

	public void populateTypeCombo() {
		IDfCollection col = null;
		// cmdSearch.addActionListener(a);
		IDfSession session = null;
		ComboItem defaultItem = null;
		try {
			session = smanager.getSession();
			if (session == null) {
				JOptionPane.showMessageDialog(null, "Session disconnected, connect and retry.", "Error occured!", JOptionPane.ERROR_MESSAGE);
				this.dispose();
				return;
			}
			IDfQuery query = new DfQuery();
			// query.setDQL("select name from dm_type order by name");
			// TODO is_searchable
			query.setDQL("select type_name, label_text from dmi_dd_type_info where  type_name in (select r_type_name from dmi_type_info where any r_supertype = 'dm_sysobject') order by label_text, type_name");
			col = query.execute(session, DfQuery.DF_READ_QUERY);
			while (col.next()) {
				String label = col.getString("label_text");
				String name = col.getString("type_name");
				String finalLabel = label + " (" + name + ") ";

				ComboItem ci = new ComboItem(finalLabel, name, "", name);
				cmbObjectType.addItem(ci);
				if (name.equals("dm_document")) {
					defaultItem = ci;
				}
			}
			cmbObjectType.setSelectedItem(defaultItem);
			tyyppi = session.getType("dm_document");
			validator = tyyppi.getTypeValidator(null, null);

		} catch (DfException ex) {
			ex.printStackTrace();
			log.error(ex);
		} finally {
			if (col != null) {
				try {
					col.close();
				} catch (DfException ex) {
					ex.printStackTrace();

					log.error(ex);
				}
			}
			if (session != null) {
				smanager.releaseSession(session);
			}
		}
		updateAttributeList("dm_document");
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
		buttonGroup2 = new javax.swing.ButtonGroup();
		jPanel2 = new javax.swing.JPanel();
		jPanel3 = new javax.swing.JPanel();
		cmdBrowse = new javax.swing.JButton();
		txtLocation = new javax.swing.JTextField();
		jLabel3 = new javax.swing.JLabel();
		cmbObjectType = new javax.swing.JComboBox();
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		txtFullText = new javax.swing.JTextField();
		jPanel1 = new javax.swing.JPanel();
		chkModified = new javax.swing.JCheckBox();
		chkCreated = new javax.swing.JCheckBox();
		modifiedEhto = new javax.swing.JComboBox();
		createdEhto = new javax.swing.JComboBox();
		modifiedDateFirst = new com.toedter.calendar.JDateChooser();
		createdDateFirst = new com.toedter.calendar.JDateChooser();
		jLabel6 = new javax.swing.JLabel();
		jLabel7 = new javax.swing.JLabel();
		modifiedDateLast = new com.toedter.calendar.JDateChooser();
		createdDateLast = new com.toedter.calendar.JDateChooser();
		jPanel5 = new javax.swing.JPanel();
		chkOwnerModifier = new javax.swing.JCheckBox();
		cmbUserHakuehto = new javax.swing.JComboBox();
		cmbUsers = new javax.swing.JComboBox();
		jLabel5 = new javax.swing.JLabel();
		jPanel4 = new javax.swing.JPanel();
		cmdRemoveProperty = new javax.swing.JButton();
		cmdRemoveProperty.setBounds(120, 130, 119, 23);
		cmdAddProperty = new javax.swing.JButton();
		cmdAddProperty.setBounds(10, 130, 99, 23);
		chkFTDQL = new javax.swing.JCheckBox();
		chkFTDQL.setBounds(250, 130, 141, 20);
		hakuPanel3 = new javax.swing.JPanel();
		hakuPanel3.setBounds(540, 67, 240, 25);
		hakuPanel2 = new javax.swing.JPanel();
		hakuPanel2.setBounds(540, 37, 240, 25);
		hakuPanel1 = new javax.swing.JPanel();
		hakuPanel1.setBounds(540, 10, 240, 25);
		cmbEhto4 = new javax.swing.JComboBox();
		cmbEhto4.setBounds(450, 100, 80, 22);
		cmbEhto3 = new javax.swing.JComboBox();
		cmbEhto3.setBounds(450, 70, 80, 22);
		cmbEhto2 = new javax.swing.JComboBox();
		cmbEhto2.setBounds(450, 40, 80, 22);
		cmbEhto1 = new javax.swing.JComboBox();
		cmbEhto1.setBounds(450, 13, 80, 22);
		cmbAttr1 = new javax.swing.JComboBox();
		cmbAttr1.setBounds(70, 13, 370, 22);
		cmbAttr2 = new javax.swing.JComboBox();
		cmbAttr2.setBounds(70, 40, 370, 22);
		cmbAttr3 = new javax.swing.JComboBox();
		cmbAttr3.setBounds(70, 70, 370, 22);
		cmbAttr4 = new javax.swing.JComboBox();
		cmbAttr4.setBounds(70, 100, 370, 22);
		cmbANDOR2 = new javax.swing.JComboBox();
		cmbANDOR2.setEnabled(false);
		cmbANDOR2.setBounds(10, 40, 47, 22);
		cmbANDOR3 = new javax.swing.JComboBox();
		cmbANDOR3.setEnabled(false);
		cmbANDOR3.setBounds(10, 70, 47, 22);
		cmbANDOR4 = new javax.swing.JComboBox();
		cmbANDOR4.setEnabled(false);
		cmbANDOR4.setBounds(10, 100, 47, 22);
		jLabel4 = new javax.swing.JLabel();
		jLabel4.setBounds(10, 10, 50, 25);
		chkAllVersions = new javax.swing.JCheckBox();
		chkAllVersions.setBounds(400, 130, 85, 23);
		hakuPanel4 = new javax.swing.JPanel();
		hakuPanel4.setBounds(540, 97, 240, 25);
		chkThumbnails = new javax.swing.JCheckBox();
		chkThumbnails.setBounds(495, 130, 79, 23);
		jPanel6 = new javax.swing.JPanel();
		cmdClose = new javax.swing.JButton();
		cmdSearch = new javax.swing.JButton();
		lblSearchStatus = new javax.swing.JLabel();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

		jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
		getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

		jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

		cmdBrowse.setText("Browse");
		cmdBrowse.setToolTipText("This is BAD, do not use!");
		cmdBrowse.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cmdBrowseActionPerformed(evt);
			}
		});

		jLabel3.setText("Location:");

		cmbObjectType.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
			public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
				cmbObjectTypeMouseWheelMoved(evt);
			}
		});
		cmbObjectType.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
				cmbObjectTypeItemStateChanged(evt);
			}
		});

		jLabel1.setText("Search Type:");

		jLabel2.setText("Document contains:");

		org.jdesktop.layout.GroupLayout gl_jPanel3 = new org.jdesktop.layout.GroupLayout(jPanel3);
		jPanel3.setLayout(gl_jPanel3);
		gl_jPanel3.setHorizontalGroup(gl_jPanel3.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
				gl_jPanel3
						.createSequentialGroup()
						.addContainerGap()
						.add(gl_jPanel3.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel2).add(jLabel3).add(jLabel1))
						.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
						.add(gl_jPanel3.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(cmbObjectType, 0, 667, Short.MAX_VALUE)
								.add(gl_jPanel3.createSequentialGroup().add(txtLocation, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 592, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(cmdBrowse))
								.add(txtFullText, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 667, Short.MAX_VALUE)).addContainerGap()));
		gl_jPanel3.setVerticalGroup(gl_jPanel3.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
				gl_jPanel3.createSequentialGroup().addContainerGap()
						.add(gl_jPanel3.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel1).add(cmbObjectType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
						.add(gl_jPanel3.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel3).add(cmdBrowse).add(txtLocation, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
						.add(gl_jPanel3.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel2).add(txtFullText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
						.addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 790, -1));

		jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

		chkModified.setText("Modified");

		chkCreated.setText("Created");

		modifiedEhto.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "After", "Before", "Between" }));
		modifiedEhto.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
				modifiedEhtoItemStateChanged(evt);
			}
		});

		createdEhto.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "After", "Before", "Between" }));
		createdEhto.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
				createdEhtoItemStateChanged(evt);
			}
		});

		jLabel6.setText("And");

		jLabel7.setText("And");

		modifiedDateLast.setEnabled(false);

		createdDateLast.setEnabled(false);

		org.jdesktop.layout.GroupLayout gl_jPanel1 = new org.jdesktop.layout.GroupLayout(jPanel1);
		jPanel1.setLayout(gl_jPanel1);
		gl_jPanel1.setHorizontalGroup(gl_jPanel1.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
				gl_jPanel1
						.createSequentialGroup()
						.addContainerGap()
						.add(gl_jPanel1.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(chkModified).add(chkCreated))
						.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
						.add(gl_jPanel1.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(modifiedEhto, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
								.add(createdEhto, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
						.add(gl_jPanel1.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(createdDateFirst, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 109, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
								.add(modifiedDateFirst, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 109, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
						.add(10, 10, 10)
						.add(gl_jPanel1.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(jLabel7).add(jLabel6))
						.add(18, 18, 18)
						.add(gl_jPanel1.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(createdDateLast, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 134, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
								.add(modifiedDateLast, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 134, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addContainerGap(324, Short.MAX_VALUE)));

		gl_jPanel1.linkSize(new java.awt.Component[] { createdDateFirst, createdDateLast, modifiedDateFirst, modifiedDateLast }, org.jdesktop.layout.GroupLayout.HORIZONTAL);

		gl_jPanel1.setVerticalGroup(gl_jPanel1.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
				gl_jPanel1
						.createSequentialGroup()
						.addContainerGap()
						.add(gl_jPanel1.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER).add(modifiedEhto, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(chkModified).add(jLabel6)
								.add(modifiedDateFirst, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
								.add(modifiedDateLast, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
						.add(gl_jPanel1.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER).add(createdEhto, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(chkCreated)
								.add(createdDateLast, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel7)
								.add(createdDateFirst, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 790, -1));

		jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

		chkOwnerModifier.setText("Documents");
		chkOwnerModifier.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				chkOwnerModifierActionPerformed(evt);
			}
		});

		cmbUserHakuehto.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Owner", "Modifier", "Creator", "Lock Owner" }));

		cmbUsers.setEditable(true);
		cmbUsers.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
			public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
				cmbUsersMouseWheelMoved(evt);
			}
		});

		jLabel5.setText("is:");

		org.jdesktop.layout.GroupLayout gl_jPanel5 = new org.jdesktop.layout.GroupLayout(jPanel5);
		jPanel5.setLayout(gl_jPanel5);
		gl_jPanel5.setHorizontalGroup(gl_jPanel5.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
				gl_jPanel5.createSequentialGroup().addContainerGap().add(chkOwnerModifier).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(cmbUserHakuehto, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 183, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(18, 18, 18)
						.add(jLabel5).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 196, Short.MAX_VALUE).add(cmbUsers, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 255, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(32, 32, 32)));
		gl_jPanel5.setVerticalGroup(gl_jPanel5.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
				gl_jPanel5
						.createSequentialGroup()
						.addContainerGap()
						.add(gl_jPanel5.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(chkOwnerModifier).add(cmbUserHakuehto, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
								.add(cmbUsers, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel5)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		getContentPane().add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, 790, -1));

		jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

		cmdRemoveProperty.setText("Remove Property");
		cmdRemoveProperty.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cmdRemovePropertyActionPerformed(evt);
			}
		});
		jPanel4.setLayout(null);
		jPanel4.add(cmdRemoveProperty);

		cmdAddProperty.setText("Add Property");
		cmdAddProperty.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cmdAddPropertyActionPerformed(evt);
			}
		});
		jPanel4.add(cmdAddProperty);

		chkFTDQL.setText("I'm Feeling Lucky (FTDQL)");
		chkFTDQL.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		chkFTDQL.setEnabled(false);
		chkFTDQL.setMargin(new java.awt.Insets(0, 0, 0, 0));
		jPanel4.add(chkFTDQL);

		hakuPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.background")));
		hakuPanel3.setLayout(new java.awt.GridBagLayout());
		jPanel4.add(hakuPanel3);

		hakuPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.background")));
		hakuPanel2.setLayout(new java.awt.GridBagLayout());
		jPanel4.add(hakuPanel2);

		hakuPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.background")));
		hakuPanel1.setLayout(new java.awt.GridBagLayout());
		jPanel4.add(hakuPanel1);

		cmbEhto4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "begins with", "is", "contains" }));
		cmbEhto4.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
				cmbEhto4ItemStateChanged(evt);
			}
		});
		jPanel4.add(cmbEhto4);

		cmbEhto3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "begins with", "is", "contains" }));
		cmbEhto3.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
				cmbEhto3ItemStateChanged(evt);
			}
		});
		jPanel4.add(cmbEhto3);

		cmbEhto2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "begins with", "is", "contains" }));
		cmbEhto2.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
				cmbEhto2ItemStateChanged(evt);
			}
		});
		jPanel4.add(cmbEhto2);

		cmbEhto1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "begins with", "is", "contains" }));
		cmbEhto1.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
				cmbEhto1ItemStateChanged(evt);
			}
		});
		jPanel4.add(cmbEhto1);

		cmbAttr1.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
				cmbAttr1ItemStateChanged(evt);
			}
		});
		jPanel4.add(cmbAttr1);

		cmbAttr2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
		cmbAttr2.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
				cmbAttr2ItemStateChanged(evt);
			}
		});
		jPanel4.add(cmbAttr2);

		cmbAttr3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
		cmbAttr3.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
				cmbAttr3ItemStateChanged(evt);
			}
		});
		cmbAttr3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cmbAttr3ActionPerformed(evt);
			}
		});
		jPanel4.add(cmbAttr3);

		cmbAttr4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
		cmbAttr4.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
				cmbAttr4ItemStateChanged(evt);
			}
		});
		jPanel4.add(cmbAttr4);

		cmbANDOR2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "AND", "OR" }));
		jPanel4.add(cmbANDOR2);

		cmbANDOR3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "AND", "OR" }));
		jPanel4.add(cmbANDOR3);

		cmbANDOR4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "AND", "OR" }));
		jPanel4.add(cmbANDOR4);

		jLabel4.setText("Where:");
		jPanel4.add(jLabel4);

		chkAllVersions.setText("All Versions");
		chkAllVersions.setToolTipText("Check this if you want to use only predefined searches below");
		jPanel4.add(chkAllVersions);

		hakuPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.background")));
		hakuPanel4.setLayout(new java.awt.GridBagLayout());
		jPanel4.add(hakuPanel4);

		chkThumbnails.setText("Thumbnails");
		chkThumbnails.setToolTipText("Show Thumbnails in Result");
		jPanel4.add(chkThumbnails);

		getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 790, 160));

		cmdClose.setText("Close");
		cmdClose.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cmdCloseActionPerformed(evt);
			}
		});

		cmdSearch.setText("Search");
		cmdSearch.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cmdSearchActionPerformed(evt);
			}
		});

		lblSearchStatus.setText(" ");
		
		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				btnSaveActionPerformed(evt);
			}
		});

		GroupLayout gl_jPanel6 = new GroupLayout(jPanel6);
		gl_jPanel6.setVerticalGroup(
			gl_jPanel6.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_jPanel6.createSequentialGroup()
					.addContainerGap(14, Short.MAX_VALUE)
					.addComponent(lblSearchStatus, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
				.addGroup(gl_jPanel6.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_jPanel6.createParallelGroup(Alignment.BASELINE)
						.addComponent(cmdSearch, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addComponent(cmdClose)
						.addComponent(btnSave))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_jPanel6.setHorizontalGroup(
			gl_jPanel6.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_jPanel6.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblSearchStatus, GroupLayout.PREFERRED_SIZE, 265, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 274, Short.MAX_VALUE)
					.addComponent(btnSave)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(cmdSearch)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(cmdClose)
					.addContainerGap())
		);
		jPanel6.setLayout(gl_jPanel6);

		getContentPane().add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 420, 790, 45));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	protected void btnSaveActionPerformed(ActionEvent evt) {
		
		String dql = generateDQL();
		SmartListSaveFrame frame = new SmartListSaveFrame();
		frame.setDql(dql);
		frame.setVisible(true);
	}

	private void cmdAddPropertyActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdAddPropertyActionPerformed

		boolean added = false;
		if (cmbAttr2.isVisible() == false) {
			cmbAttr2.setVisible(true);
			txt2.setVisible(true);
			hakuPanel2.setVisible(true);
			hakuPanel2.add(txt2);
			hakuPanel2.revalidate();
			hakuPanel2.repaint();
			cmbANDOR2.setVisible(true);
			cmbEhto2.setVisible(true);
			added = true;
		}
		if (cmbAttr3.isVisible() == false) {
			if (added == false) {
				hakuPanel3.setVisible(true);
				hakuPanel3.add(txt3);
				hakuPanel3.revalidate();
				hakuPanel3.repaint();
				cmbAttr3.setVisible(true);
				txt3.setVisible(true);
				cmbANDOR3.setVisible(true);
				cmbEhto3.setVisible(true);
				added = true;
			}
		}
		if (cmbAttr4.isVisible() == false) {
			if (added == false) {
				hakuPanel4.setVisible(true);
				hakuPanel4.add(txt4);
				hakuPanel4.revalidate();
				hakuPanel4.repaint();
				cmbAttr4.setVisible(true);
				txt4.setVisible(true);
				cmbANDOR4.setVisible(true);
				cmbEhto4.setVisible(true);
				added = true;
			}

		}
	}// GEN-LAST:event_cmdAddPropertyActionPerformed

	private void cmdCloseActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdCloseActionPerformed
		this.dispose();
	}// GEN-LAST:event_cmdCloseActionPerformed

	private void cmdBrowseActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdBrowseActionPerformed
		// //System.out.println("folderselectordata on call: " +
		// getFolderselectordata());
		ActionListener al = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				txtLocation.setText(getFolderselectordata().getFolderPath());
			}
		};
		FolderSelectorFrame frame = new FolderSelectorFrame(al, getFolderselectordata());

		SwingHelper.centerJFrame(frame);
		frame.initAll();
		frame.setVisible(true);
	}// GEN-LAST:event_cmdBrowseActionPerformed

	private void updateAttributeList(String typeName) {
		IDfCollection col = null;
		ComboItem defaultItem = null;
		DefaultComboBoxModel cmodel = new DefaultComboBoxModel();
		DefaultComboBoxModel cmodel2 = new DefaultComboBoxModel();
		DefaultComboBoxModel cmodel3 = new DefaultComboBoxModel();
		DefaultComboBoxModel cmodel4 = new DefaultComboBoxModel();

		DefaultComboBoxModel cmodelorderBy = new DefaultComboBoxModel();

		IDfSession session = null;
		try {
			session = smanager.getSession();
//			System.out.println(typeName);
			if (typeName != null) {
				tyyppi = session.getType(typeName);
				validator = tyyppi.getTypeValidator(null, null);
			}

			IDfQuery query = new DfQuery();
			query.setDQL("select distinct t.attr_repeating,a.label_text, t.attr_type,a.attr_name from dmi_dd_attr_info a, dm_type t where type_name = '" + typeName + "' and a.attr_name = t.attr_name order by 2 enable (row_based)");
			// query.setDQL("select label_text, attr_name from dmi_dd_attr_info where type_name = '"
			// + typeName + "' order by label_text");
			col = query.execute(session, DfQuery.DF_READ_QUERY);
			// LabelCache lc = LabelCache.getInstance();
			// LabelValues lv = lc.getLabels(typeName);

			while (col.next()) {
				// cmbAttr1.addItem(col.getString("attr_name"));
				String label = col.getString("label_text");
				String attrname = col.getString("attr_name");
				boolean repeating = col.getBoolean("attr_repeating");
				String attrtype = col.getString("attr_type");
				String isrep = "0";
				if (repeating) {
					isrep = "1";
				}
				// TODO repeating hajalla.
				ComboItem temp = new ComboItem(label + " (" + attrname + ")", attrname, isrep, attrtype);

				cmodel.addElement(temp);
				cmodel2.addElement(temp);
				cmodel3.addElement(temp);
				cmodel4.addElement(temp);
				if (attrname.equals("object_name")) {
					defaultItem = temp;
				}
				cmodelorderBy.addElement(temp);
			}
		} catch (DfException ex) {
			log.error(ex);
			ex.printStackTrace();
		} finally {
			if (col != null) {
				try {
					col.close();
				} catch (DfException ex) {
					log.error(ex);
					ex.printStackTrace();
				}
			}
			if (session != null) {
				smanager.releaseSession(session);
			}
		}
		cmbAttr1.setModel(cmodel);
		cmbAttr1.validate();
		cmbAttr2.setModel(cmodel2);
		cmbAttr2.validate();
		cmbAttr3.setModel(cmodel3);
		cmbAttr3.validate();
		cmbAttr4.setModel(cmodel4);
		cmbAttr4.validate();
		if (defaultItem != null) {
			cmbAttr1.setSelectedItem(defaultItem);
			cmbAttr2.setSelectedItem(defaultItem);
			cmbAttr3.setSelectedItem(defaultItem);
			cmbAttr4.setSelectedItem(defaultItem);
		}
	}

	private void cmbObjectTypeItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_cmbObjectTypeItemStateChanged
		ComboItem defaultItem = null;
		if (isloading) {
			return;
		}
		if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
			IDfCollection col = null;
			DefaultComboBoxModel cmodel = new DefaultComboBoxModel();
			DefaultComboBoxModel cmodel2 = new DefaultComboBoxModel();
			DefaultComboBoxModel cmodel3 = new DefaultComboBoxModel();
			DefaultComboBoxModel cmodel4 = new DefaultComboBoxModel();

			DefaultComboBoxModel cmodelorderBy = new DefaultComboBoxModel();

			JComboBox combo = (JComboBox) evt.getSource();
			ComboItem ti = (ComboItem) combo.getSelectedItem();
			String typeName = ti.getAttrType();
			IDfSession session = null;
			try {
				session = smanager.getSession();
				// System.out.println(typeName);
				if (typeName != null) {
					tyyppi = session.getType(typeName);
					validator = tyyppi.getTypeValidator(null, null);
				}
				// IDfValueAssistance va = v.getValueAssistance(attrName, null);

				IDfQuery query = new DfQuery();
				// TODO is_searchable
				// query.setDQL("select label_text, attr_name from dmi_dd_attr_info where type_name = '"
				// + typeName + "' order by label_text");
				query.setDQL("select distinct t.attr_repeating,a.label_text, t.attr_type,a.attr_name from dmi_dd_attr_info a, dm_type t where type_name = '" + typeName + "' and a.attr_name = t.attr_name order by 2 enable (row_based)");
				// query.setDQL("select label_text, attr_name from dmi_dd_attr_info where type_name = '"
				// + typeName + "' order by label_text");
				col = query.execute(session, DfQuery.DF_READ_QUERY);
				// LabelCache lc = LabelCache.getInstance();
				// LabelValues lv = lc.getLabels(typeName);

				while (col.next()) {
					// cmbAttr1.addItem(col.getString("attr_name"));
					String label = col.getString("label_text");
					String attrname = col.getString("attr_name");
					boolean repeating = col.getBoolean("attr_repeating");
					String attrtype = col.getString("attr_type");
					String isrep = "0";
					if (repeating) {
						isrep = "1";
					}
					// TODO repeating hajalla.
					ComboItem temp = new ComboItem(label + " (" + attrname + ")", attrname, isrep, attrtype);

					cmodel.addElement(temp);
					cmodel2.addElement(temp);
					cmodel3.addElement(temp);
					cmodel4.addElement(temp);
					if (attrname.equals("object_name")) {
						defaultItem = temp;
					}
					cmodelorderBy.addElement(temp);
				}
			} catch (DfException ex) {
				ex.printStackTrace();

				log.error(ex);
			} finally {
				if (col != null) {
					try {
						col.close();
					} catch (DfException ex) {
						ex.printStackTrace();

						log.error(ex);
					}
				}
				if (session != null) {
					smanager.releaseSession(session);
				}
			}
			cmbAttr1.setModel(cmodel);
			cmbAttr1.validate();
			cmbAttr2.setModel(cmodel2);
			cmbAttr2.validate();
			cmbAttr3.setModel(cmodel3);
			cmbAttr3.validate();
			cmbAttr4.setModel(cmodel4);
			cmbAttr4.validate();
			if (defaultItem != null) {
				cmbAttr1.setSelectedItem(defaultItem);
				cmbAttr2.setSelectedItem(defaultItem);
				cmbAttr3.setSelectedItem(defaultItem);
				cmbAttr4.setSelectedItem(defaultItem);
			}

			// cmbOrderBy.setModel(cmodelorderBy);
			// cmbOrderBy.validate();
			// cmbOrderBy.setSelectedItem("r_modify_date");

		}
	}// GEN-LAST:event_cmbObjectTypeItemStateChanged

	private void cmbObjectTypeMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {// GEN-FIRST:event_cmbObjectTypeMouseWheelMoved
	// ////System.out.println(evt);
		int maxindex = cmbObjectType.getItemCount();
		if (evt.getWheelRotation() > 0) {
			int index = cmbObjectType.getSelectedIndex();
			if (index < maxindex - 1) {
				cmbObjectType.setSelectedIndex(index + 1);
			}
		} else {
			int index = cmbObjectType.getSelectedIndex();
			if (index > 0) {
				cmbObjectType.setSelectedIndex(index - 1);
			}
		}
	}// GEN-LAST:event_cmbObjectTypeMouseWheelMoved

	private String generateDQL() {
		ComboItem tempci = (ComboItem) cmbObjectType.getSelectedItem();
		String objType = tempci.getAttrType();
		cmdSearch.addActionListener(a);
		String queryStr = "";
		String allVersions = "";
		if (chkAllVersions.isSelected()) {
			allVersions = "(ALL)";
		}
		if (additionalqueryattributes.trim().equals(","))
			additionalqueryattributes = "";
		if (txtFullText.getText().length() > 3) {
			if (chkThumbnails.isSelected()) {
				queryStr = "select " + standardqueryattributes + additionalqueryattributes + ",thumbnail_url from " + objType + allVersions + " SEARCH DOCUMENT CONTAINS '" + txtFullText.getText() + "' where 1=1 ";
			} else {
				queryStr = "select " + standardqueryattributes + additionalqueryattributes + " from " + objType + allVersions + " SEARCH DOCUMENT CONTAINS '" + txtFullText.getText() + "' where 1=1 ";
			}
		} else {
			if (chkThumbnails.isSelected()) {
				queryStr = "select " + standardqueryattributes + additionalqueryattributes + ",thumbnail_url from " + objType + allVersions + " where 1=1 ";
			} else {
				queryStr = "select " + standardqueryattributes + additionalqueryattributes + " from " + objType + allVersions + " where 1=1 ";
			}
		}
//		System.out.println(standardqueryattributes);
//		System.out.println(additionalqueryattributes);
//		System.out.println(queryStr);

			StringBuffer buffer = new StringBuffer();
			StringBuffer buffer2 = new StringBuffer();
			StringBuffer buffer3 = new StringBuffer();
			StringBuffer buffer4 = new StringBuffer();

			Object component1 = hakuPanel1.getComponent(0);
//			System.out.println(component1);
			if (component1 instanceof JDateChooser) {
				JDateChooser chooser = (JDateChooser)component1;
				Date selectedDate = chooser.getDate();
				Date selectedDateb = chooser.getDate();
				if (selectedDate != null) {
					IDfTime idftime = new DfTime(selectedDate);
					buffer.append(" and ");
					ComboItem tempitem = (ComboItem) cmbAttr1.getSelectedItem();
					if (tempitem.getAttrRepeating().equalsIgnoreCase("0")) {
						buffer.append(tempitem.getAttrName() + " ");
					} else {
						buffer.append(" any " + tempitem.getAttrName() + " ");
					}
					String ehto = (String) cmbEhto1.getSelectedItem();
					if (ehto.equalsIgnoreCase("is before")) {
						buffer.append(" > DATE('");
						buffer.append(idftime.asString(DfTime.DF_TIME_PATTERN18));
						buffer.append("','" + DfTime.DF_TIME_PATTERN18 + "')");
					} else if (ehto.equalsIgnoreCase("is after")) {
						buffer.append(" < DATE('");
						buffer.append(idftime.asString(DfTime.DF_TIME_PATTERN18));
						buffer.append("','" + DfTime.DF_TIME_PATTERN18 + "')");
					} else if (ehto.equalsIgnoreCase("between")) {
						buffer.append(" > DATE('");
						buffer.append(idftime.asString(DfTime.DF_TIME_PATTERN18));
						buffer.append("','" + DfTime.DF_TIME_PATTERN18 + "')");
						if (selectedDateb != null) {
							buffer.append(" and ");
							if (tempitem.getAttrRepeating().equalsIgnoreCase("0")) {
								buffer.append(tempitem.getAttrName() + " ");
							} else {
								buffer.append(" any " + tempitem.getAttrName() + " ");
							}
							IDfTime idftimeb = new DfTime(selectedDateb);
							buffer.append(" < DATE('");
							buffer.append(idftimeb.asString(DfTime.DF_TIME_PATTERN18));
							buffer.append("','" + DfTime.DF_TIME_PATTERN18 + "')");
						}
					}
				}
			} else if (component1 instanceof JTextField) {
				JTextField textfield = (JTextField)component1;
				if (textfield.getText().length() > 0) {
					buffer.append(" and ");
					ComboItem tempitem = (ComboItem) cmbAttr1.getSelectedItem();
					if (tempitem.getAttrRepeating().equalsIgnoreCase("0")) {
						buffer.append(tempitem.getAttrName() + " ");
					} else {
						buffer.append(" any " + tempitem.getAttrName() + " ");
					}
					String ehto = (String) cmbEhto1.getSelectedItem();
					if (ehto.equalsIgnoreCase("begins with")) {
						buffer.append(" like '");
						buffer.append(textfield.getText());
						buffer.append("%'");
					} else if (ehto.equalsIgnoreCase("is")) {
						buffer.append(" = '");
						buffer.append(txt1.getText());
						buffer.append("'");

					} else if (ehto.equalsIgnoreCase("contains")) {
						buffer.append(" like '%");
						buffer.append(txt1.getText());
						buffer.append("%'");
					}
				}
			} else if (component1 instanceof JComboBox) {
				JComboBox tempcombo = (JComboBox) component1;
				String hakuvalue = tempcombo.getSelectedItem().toString();
				if (hakuvalue.length() > 0) {
					buffer.append(" and ");
					ComboItem tempitem = (ComboItem) cmbAttr1.getSelectedItem();
					if (tempitem.getAttrRepeating().equalsIgnoreCase("0")) {
						buffer.append(tempitem.getAttrName() + " ");
					} else {
						buffer.append(" any " + tempitem.getAttrName() + " ");
					}
					String ehto = (String) cmbEhto1.getSelectedItem();
					if (ehto.equalsIgnoreCase("begins with")) {
						buffer.append(" like '");
						buffer.append(hakuvalue);
						buffer.append("%'");
					} else if (ehto.equalsIgnoreCase("is")) {
						buffer.append(" = '");
						buffer.append(hakuvalue);
						buffer.append("'");

					} else if (ehto.equalsIgnoreCase("contains")) {
						buffer.append(" like '%");
						buffer.append(hakuvalue);
						buffer.append("%'");
					}
				}
			}

			if (cmbAttr2.isVisible()) {
				Object component2 = hakuPanel2.getComponent(0);
				if (component2 instanceof JDateChooser) {
					Date selectedDate = jdc2.getDate();
					Date selectedDateb = jdc2b.getDate();
					if (selectedDate != null) {
						IDfTime idftime = new DfTime(selectedDate);
						buffer.append(" " + (String) cmbANDOR2.getSelectedItem() + " ");
						ComboItem tempitem = (ComboItem) cmbAttr2.getSelectedItem();
						if (tempitem.getAttrRepeating().equalsIgnoreCase("0")) {
							buffer.append(tempitem.getAttrName() + " ");
						} else {
							buffer.append(" any " + tempitem.getAttrName() + " ");
						}
						String ehto = (String) cmbEhto2.getSelectedItem();
						if (ehto.equalsIgnoreCase("is before")) {
							buffer.append(" > DATE('");
							buffer.append(idftime.asString(DfTime.DF_TIME_PATTERN18));
							buffer.append("','" + DfTime.DF_TIME_PATTERN18 + "')");
						} else if (ehto.equalsIgnoreCase("is after")) {
							buffer.append(" < DATE('");
							buffer.append(idftime.asString(DfTime.DF_TIME_PATTERN18));
							buffer.append("','" + DfTime.DF_TIME_PATTERN18 + "')");
						} else if (ehto.equalsIgnoreCase("between")) {
							buffer.append(" > DATE('");
							buffer.append(idftime.asString(DfTime.DF_TIME_PATTERN18));
							buffer.append("','" + DfTime.DF_TIME_PATTERN18 + "')");
							if (selectedDateb != null) {
								buffer.append(" and ");
								if (tempitem.getAttrRepeating().equalsIgnoreCase("0")) {
									buffer.append(tempitem.getAttrName() + " ");
								} else {
									buffer.append(" any " + tempitem.getAttrName() + " ");
								}
								IDfTime idftimeb = new DfTime(selectedDateb);
								buffer.append(" < DATE('");
								buffer.append(idftimeb.asString(DfTime.DF_TIME_PATTERN18));
								buffer.append("','" + DfTime.DF_TIME_PATTERN18 + "')");
							}
						}
					}
				} else if (component2 instanceof JTextField) {
					if (txt2.getText().length() > 0) {
						buffer.append(" " + (String) cmbANDOR2.getSelectedItem() + " ");
						ComboItem tempitem = (ComboItem) cmbAttr2.getSelectedItem();
						if (tempitem.getAttrRepeating().equalsIgnoreCase("0")) {
							buffer.append(tempitem.getAttrName() + " ");
						} else {
							buffer.append(" any " + tempitem.getAttrName() + " ");
						}
						String ehto = (String) cmbEhto2.getSelectedItem();
						if (ehto.equalsIgnoreCase("begins with")) {
							buffer.append(" like '");
							buffer.append(txt2.getText());
							buffer.append("%'");
						} else if (ehto.equalsIgnoreCase("is")) {
							buffer.append(" = '");
							buffer.append(txt2.getText());
							buffer.append("'");

						} else if (ehto.equalsIgnoreCase("contains")) {
							buffer.append(" like '%");
							buffer.append(txt2.getText());
							buffer.append("%'");
						}
					}
				} else if (component2 instanceof JComboBox) {
					String hakuvalue = vacombo2.getSelectedItem().toString();
					if (hakuvalue.length() > 0) {
						buffer.append(" " + (String) cmbANDOR2.getSelectedItem() + " ");
						ComboItem tempitem = (ComboItem) cmbAttr2.getSelectedItem();
						if (tempitem.getAttrRepeating().equalsIgnoreCase("0")) {
							buffer.append(tempitem.getAttrName() + " ");
						} else {
							buffer.append(" any " + tempitem.getAttrName() + " ");
						}
						String ehto = (String) cmbEhto2.getSelectedItem();
						if (ehto.equalsIgnoreCase("begins with")) {
							buffer.append(" like '");
							buffer.append(hakuvalue);
							buffer.append("%'");
						} else if (ehto.equalsIgnoreCase("is")) {
							buffer.append(" = '");
							buffer.append(hakuvalue);
							buffer.append("'");

						} else if (ehto.equalsIgnoreCase("contains")) {
							buffer.append(" like '%");
							buffer.append(hakuvalue);
							buffer.append("%'");
						}
					}
				}
			}

			if (cmbAttr3.isVisible()) {
				Object component3 = hakuPanel3.getComponent(0);
				if (component3 instanceof JDateChooser) {
					Date selectedDate = jdc3.getDate();
					Date selectedDateb = jdc3b.getDate();
					if (selectedDate != null) {
						IDfTime idftime = new DfTime(selectedDate);
						buffer.append(" " + (String) cmbANDOR3.getSelectedItem() + " ");
						ComboItem tempitem = (ComboItem) cmbAttr3.getSelectedItem();
						if (tempitem.getAttrRepeating().equalsIgnoreCase("0")) {
							buffer.append(tempitem.getAttrName() + " ");
						} else {
							buffer.append(" any " + tempitem.getAttrName() + " ");
						}
						String ehto = (String) cmbEhto3.getSelectedItem();
						if (ehto.equalsIgnoreCase("is before")) {
							buffer.append(" > DATE('");
							buffer.append(idftime.asString(DfTime.DF_TIME_PATTERN18));
							buffer.append("','" + DfTime.DF_TIME_PATTERN18 + "')");
						} else if (ehto.equalsIgnoreCase("is after")) {
							buffer.append(" < DATE('");
							buffer.append(idftime.asString(DfTime.DF_TIME_PATTERN18));
							buffer.append("','" + DfTime.DF_TIME_PATTERN18 + "')");
						} else if (ehto.equalsIgnoreCase("between")) {
							buffer.append(" > DATE('");
							buffer.append(idftime.asString(DfTime.DF_TIME_PATTERN18));
							buffer.append("','" + DfTime.DF_TIME_PATTERN18 + "')");
							if (selectedDateb != null) {
								buffer.append(" and ");
								if (tempitem.getAttrRepeating().equalsIgnoreCase("0")) {
									buffer.append(tempitem.getAttrName() + " ");
								} else {
									buffer.append(" any " + tempitem.getAttrName() + " ");
								}
								IDfTime idftimeb = new DfTime(selectedDateb);
								buffer.append(" < DATE('");
								buffer.append(idftimeb.asString(DfTime.DF_TIME_PATTERN18));
								buffer.append("','" + DfTime.DF_TIME_PATTERN18 + "')");
							}
						}
					}
				} else if (component3 instanceof JTextField) {
					if (txt3.getText().length() > 0) {
						buffer.append(" " + (String) cmbANDOR3.getSelectedItem() + " ");
						ComboItem tempitem = (ComboItem) cmbAttr3.getSelectedItem();
						if (tempitem.getAttrRepeating().equalsIgnoreCase("0")) {
							buffer.append(tempitem.getAttrName() + " ");
						} else {
							buffer.append(" any " + tempitem.getAttrName() + " ");
						}
						String ehto = (String) cmbEhto3.getSelectedItem();
						if (ehto.equalsIgnoreCase("begins with")) {
							buffer.append(" like '");
							buffer.append(txt3.getText());
							buffer.append("%'");
						} else if (ehto.equalsIgnoreCase("is")) {
							buffer.append(" = '");
							buffer.append(txt3.getText());
							buffer.append("'");

						} else if (ehto.equalsIgnoreCase("contains")) {
							buffer.append(" like '%");
							buffer.append(txt3.getText());
							buffer.append("%'");
						}
					}
				} else if (component3 instanceof JComboBox) {
					String hakuvalue = vacombo3.getSelectedItem().toString();
					if (hakuvalue.length() > 0) {
						buffer.append(" " + (String) cmbANDOR3.getSelectedItem() + " ");
						ComboItem tempitem = (ComboItem) cmbAttr3.getSelectedItem();
						if (tempitem.getAttrRepeating().equalsIgnoreCase("0")) {
							buffer.append(tempitem.getAttrName() + " ");
						} else {
							buffer.append(" any " + tempitem.getAttrName() + " ");
						}
						String ehto = (String) cmbEhto3.getSelectedItem();
						if (ehto.equalsIgnoreCase("begins with")) {
							buffer.append(" like '");
							buffer.append(hakuvalue);
							buffer.append("%'");
						} else if (ehto.equalsIgnoreCase("is")) {
							buffer.append(" = '");
							buffer.append(hakuvalue);
							buffer.append("'");

						} else if (ehto.equalsIgnoreCase("contains")) {
							buffer.append(" like '%");
							buffer.append(hakuvalue);
							buffer.append("%'");
						}
					}
				}
			}

			if (cmbAttr4.isVisible()) {
				Object component4 = hakuPanel4.getComponent(0);
				if (component4 instanceof JDateChooser) {
					Date selectedDate = jdc4.getDate();
					Date selectedDateb = jdc4b.getDate();
					if (selectedDate != null) {
						IDfTime idftime = new DfTime(selectedDate);
						buffer.append(" " + (String) cmbANDOR4.getSelectedItem() + " ");
						ComboItem tempitem = (ComboItem) cmbAttr4.getSelectedItem();
						if (tempitem.getAttrRepeating().equalsIgnoreCase("0")) {
							buffer.append(tempitem.getAttrName() + " ");
						} else {
							buffer.append(" any " + tempitem.getAttrName() + " ");
						}
						String ehto = (String) cmbEhto4.getSelectedItem();
						if (ehto.equalsIgnoreCase("is before")) {
							buffer.append(" > DATE('");
							buffer.append(idftime.asString(DfTime.DF_TIME_PATTERN18));
							buffer.append("','" + DfTime.DF_TIME_PATTERN18 + "')");
						} else if (ehto.equalsIgnoreCase("is after")) {
							buffer.append(" < DATE('");
							buffer.append(idftime.asString(DfTime.DF_TIME_PATTERN18));
							buffer.append("','" + DfTime.DF_TIME_PATTERN18 + "')");
						} else if (ehto.equalsIgnoreCase("between")) {
							buffer.append(" > DATE('");
							buffer.append(idftime.asString(DfTime.DF_TIME_PATTERN18));
							buffer.append("','" + DfTime.DF_TIME_PATTERN18 + "')");
							if (selectedDateb != null) {
								buffer.append(" and ");
								if (tempitem.getAttrRepeating().equalsIgnoreCase("0")) {
									buffer.append(tempitem.getAttrName() + " ");
								} else {
									buffer.append(" any " + tempitem.getAttrName() + " ");
								}
								IDfTime idftimeb = new DfTime(selectedDateb);
								buffer.append(" < DATE('");
								buffer.append(idftimeb.asString(DfTime.DF_TIME_PATTERN18));
								buffer.append("','" + DfTime.DF_TIME_PATTERN18 + "')");
							}
						}
					}
				} else if (component4 instanceof JTextField) {
					if (txt4.getText().length() > 0) {
						buffer.append(" " + (String) cmbANDOR4.getSelectedItem() + " ");
						ComboItem tempitem = (ComboItem) cmbAttr4.getSelectedItem();
						if (tempitem.getAttrRepeating().equalsIgnoreCase("0")) {
							buffer.append(tempitem.getAttrName() + " ");
						} else {
							buffer.append(" any " + tempitem.getAttrName() + " ");
						}
						String ehto = (String) cmbEhto4.getSelectedItem();
						if (ehto.equalsIgnoreCase("begins with")) {
							buffer.append(" like '");
							buffer.append(txt4.getText());
							buffer.append("%'");
						} else if (ehto.equalsIgnoreCase("is")) {
							buffer.append(" = '");
							buffer.append(txt4.getText());
							buffer.append("'");

						} else if (ehto.equalsIgnoreCase("contains")) {
							buffer.append(" like '%");
							buffer.append(txt4.getText());
							buffer.append("%'");
						}

					}
				} else if (component4 instanceof JComboBox) {
					String hakuvalue = vacombo4.getSelectedItem().toString();
					if (hakuvalue.length() > 0) {
						buffer.append(" " + (String) cmbANDOR4.getSelectedItem() + " ");
						ComboItem tempitem = (ComboItem) cmbAttr4.getSelectedItem();
						if (tempitem.getAttrRepeating().equalsIgnoreCase("0")) {
							buffer.append(tempitem.getAttrName() + " ");
						} else {
							buffer.append(" any " + tempitem.getAttrName() + " ");
						}
						String ehto = (String) cmbEhto4.getSelectedItem();
						if (ehto.equalsIgnoreCase("begins with")) {
							buffer.append(" like '");
							buffer.append(hakuvalue);
							buffer.append("%'");
						} else if (ehto.equalsIgnoreCase("is")) {
							buffer.append(" = '");
							buffer.append(hakuvalue);
							buffer.append("'");

						} else if (ehto.equalsIgnoreCase("contains")) {
							buffer.append(" like '%");
							buffer.append(hakuvalue);
							buffer.append("%'");
						}
					}
				}
			}

			if (chkCreated.isSelected()) {
				Date selectedDate = createdDateFirst.getDate();
				if (selectedDate != null) {
					IDfTime idftime = new DfTime(selectedDate);
					String ehto = (String) createdEhto.getSelectedItem();
					buffer.append(" AND r_creation_date ");
					if (ehto.equalsIgnoreCase("before")) {
						buffer.append(" < DATE('");
						buffer.append(idftime.asString(DfTime.DF_TIME_PATTERN18));
						buffer.append("','" + DfTime.DF_TIME_PATTERN18 + "')");
					} else if (ehto.equalsIgnoreCase("after")) {
						buffer.append(" > DATE('");
						buffer.append(idftime.asString(DfTime.DF_TIME_PATTERN18));
						buffer.append("','" + DfTime.DF_TIME_PATTERN18 + "')");
					} else if (ehto.equalsIgnoreCase("between")) {
						Date selectedDateb = createdDateLast.getDate();

						buffer.append(" > DATE('");
						buffer.append(idftime.asString(DfTime.DF_TIME_PATTERN18));
						buffer.append("','" + DfTime.DF_TIME_PATTERN18 + "')");
						if (selectedDateb != null) {
							buffer.append(" and ");
							buffer.append(" r_creation_date ");
							IDfTime idftimeb = new DfTime(selectedDateb);
							buffer.append(" < DATE('");
							buffer.append(idftimeb.asString(DfTime.DF_TIME_PATTERN18));
							buffer.append("','" + DfTime.DF_TIME_PATTERN18 + "')");
						}
					}
				}
			}
			if (chkModified.isSelected()) {
				Date selectedDate = modifiedDateFirst.getDate();
				if (selectedDate != null) {
					IDfTime idftime = new DfTime(selectedDate);
					String ehto = (String) modifiedEhto.getSelectedItem();
					buffer.append(" AND r_modify_date ");
					if (ehto.equalsIgnoreCase("before")) {
						buffer.append(" < DATE('");
						buffer.append(idftime.asString(DfTime.DF_TIME_PATTERN18));
						buffer.append("','" + DfTime.DF_TIME_PATTERN18 + "')");
					} else if (ehto.equalsIgnoreCase("after")) {
						buffer.append(" > DATE('");
						buffer.append(idftime.asString(DfTime.DF_TIME_PATTERN18));
						buffer.append("','" + DfTime.DF_TIME_PATTERN18 + "')");
					} else if (ehto.equalsIgnoreCase("between")) {
						Date selectedDateb = modifiedDateLast.getDate();

						buffer.append(" > DATE('");
						buffer.append(idftime.asString(DfTime.DF_TIME_PATTERN18));
						buffer.append("','" + DfTime.DF_TIME_PATTERN18 + "')");
						if (selectedDateb != null) {
							buffer.append(" and ");
							buffer.append(" r_modify_date ");
							IDfTime idftimeb = new DfTime(selectedDateb);
							buffer.append(" < DATE('");
							buffer.append(idftimeb.asString(DfTime.DF_TIME_PATTERN18));
							buffer.append("','" + DfTime.DF_TIME_PATTERN18 + "')");
						}
					}
				}

			}
			if (chkOwnerModifier.isSelected()) {
				String hakusana = (String) cmbUserHakuehto.getSelectedItem();
				String hakuehto = "";
				if (hakusana.equalsIgnoreCase("owner")) {
					hakuehto = "owner_name";
				}
				if (hakusana.equalsIgnoreCase("lock owner")) {
					hakuehto = "r_lock_owner";
				}
				if (hakusana.equalsIgnoreCase("modifier")) {
					hakuehto = "r_modifier";
				}
				if (hakusana.equalsIgnoreCase("creator")) {
					hakuehto = "r_creator_name";
				}
				buffer.append(" and ");
				buffer.append(hakuehto);
				buffer.append(" = ");
				buffer.append("'");
				buffer.append((String) cmbUsers.getSelectedItem());
				buffer.append("'");
			}
			queryStr = queryStr + buffer.toString() + buffer2.toString() + buffer3.toString() + buffer4.toString();

			// added 2.4
			String location = txtLocation.getText();
			if (location.length() > 2) {
				queryStr = queryStr + " and FOLDER('" + location + "', descend) ";
			}

			if (chkFTDQL.isSelected()) {
				queryStr = queryStr + " ENABLE(FTDQL) order by score";
			}

			return queryStr;
	}
	
	private void cmdSearchActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdSearchActionPerformed
		IDfCollection col = null;
		int counter = 0;
		model.setRowCount(0);
		String queryStr = generateDQL();
		IDfSession session = null;
		try {
			session = smanager.getSession();
			IDfQuery query = new DfQuery();
			// JOptionPane.showMessageDialog(null, queryStr, "WW!",
			// JOptionPane.OK_OPTION);
			log.info(queryStr);
			query.setDQL(queryStr);
//			System.out.println(queryStr);
			Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
			setCursor(cur);

			long startmillis = System.currentTimeMillis();

			col = query.execute(session, DfQuery.DF_READ_QUERY);
			long stopmillis = System.currentTimeMillis();
			double time = stopmillis - startmillis;

			Utils u = new Utils();
			model = u.getModelFromCollection(session,null,col, chkThumbnails.isSelected(), model,null,null);
			lblSearchStatus.setText("Found " + u.getCounter() + " items in: " + time / 1000 + " secs.");
		} catch (DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);

		} finally {
			Cursor cur = new Cursor(Cursor.DEFAULT_CURSOR);
			setCursor(cur);
			if (col != null) {
				try {
					col.close();
				} catch (DfException ex) {
					ex.printStackTrace();
					log.error(ex);
				}
			}
			if (session != null) {
				smanager.releaseSession(session);
			}
		}
		if (model.getRowCount() > 0) {
			SearchFrame sf = new SearchFrame(model, chkThumbnails.isSelected());
			desktop.add(sf);
			sf.setSize(900, 400);
			sf.setTitle("Search Results...");
			sf.setVisible(true);
		} else {
			// JOptionPane.showMessageDialog(null, "Nothing found..", "Info",
			// JOptionPane.INFORMATION_MESSAGE);
		}

		// model.setRowCount(0);
	}// GEN-LAST:event_cmdSearchActionPerformed

	private void cmbAttr1ItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_cmbAttr1ItemStateChanged
		Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
		setCursor(cur);

		try {
//			System.out.println(tyyppi.getName());
			validator = tyyppi.getTypeValidator(null, null);
			IDfList valist = null;
			vacombo1.removeAllItems();
			ComboItem ci = (ComboItem) cmbAttr1.getSelectedItem();
			boolean hasvalueassistance = false;
			if (validator.hasValueAssistance(ci.getAttrName())) {
//				System.out.println("has value assistance");
				hasvalueassistance = true;
				IDfValueAssistance va = validator.getValueAssistance(ci.getAttrName(), null);
				valist = va.getActualValues();
				for (int i = 0; i < valist.getCount(); i++) {
					vacombo1.addItem(valist.get(i));
				}
			}
			buildAttrGui(ci, hakuPanel1, jdc1, txt1, vacombo1, cmbEhto1, valist);
		} catch (DfException ex) {
			ex.printStackTrace();

			log.error(ex);
		} finally {
			cur = new Cursor(Cursor.DEFAULT_CURSOR);
			setCursor(cur);
		}
	}// GEN-LAST:event_cmbAttr1ItemStateChanged

	private void cmdRemovePropertyActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdRemovePropertyActionPerformed
		boolean removed = false;
		if (cmbAttr4.isVisible() == true) {
			cmbAttr4.setVisible(false);
			txt4.setVisible(false);
			cmbANDOR4.setVisible(false);
			cmbEhto4.setVisible(false);
			hakuPanel4.setVisible(false);
			removed = true;

		}

		if (cmbAttr3.isVisible() == true) {
			if (removed == false) {
				cmbAttr3.setVisible(false);
				txt3.setVisible(false);
				cmbANDOR3.setVisible(false);
				cmbEhto3.setVisible(false);
				hakuPanel3.setVisible(false);
				removed = true;
			}
		}
		if (cmbAttr2.isVisible() == true) {
			if (removed == false) {
				cmbAttr2.setVisible(false);
				txt2.setVisible(false);
				cmbANDOR2.setVisible(false);
				cmbEhto2.setVisible(false);
				hakuPanel2.setVisible(false);
				removed = true;
			}
		}

	}// GEN-LAST:event_cmdRemovePropertyActionPerformed

	private void cmbAttr2ItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_cmbAttr2ItemStateChanged
		Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
		setCursor(cur);

		try {
			IDfList valist = null;
			ComboItem c = (ComboItem) cmbAttr2.getSelectedItem();
			if (validator.hasValueAssistance(c.getAttrName())) {

				IDfValueAssistance va = validator.getValueAssistance(c.getAttrName(), null);
				valist = va.getActualValues();
				for (int i = 0; i < valist.getCount(); i++) {
					vacombo2.addItem(valist.get(i));
				}
			}
			ComboItem ci = (ComboItem) cmbAttr2.getSelectedItem();
			buildAttrGui(ci, hakuPanel2, jdc2, txt2, vacombo2, cmbEhto2, valist);
		} catch (DfException ex) {
			ex.printStackTrace();
			log.error(ex);
		} finally {
			cur = new Cursor(Cursor.WAIT_CURSOR);
			setCursor(cur);

		}

	}// GEN-LAST:event_cmbAttr2ItemStateChanged

	private void buildAttrGui(ComboItem ci, JPanel paneeli, JDateChooser jdc, JTextField jtf, JComboBox vacombo, JComboBox combo, IDfList valist) {
//		System.out.println(ci.getAttrType());
		String attrType = ci.getAttrType();
		// //System.out.println("change.");
		if (attrType.equals("4")) {
			paneeli.removeAll();
			Dimension d = new Dimension();
			d.setSize(250, 23);
			jdc.setPreferredSize(d);
			paneeli.add(jdc);
			paneeli.revalidate();
			paneeli.repaint();

			combo.removeAllItems();
			combo.addItem("is before");
			combo.addItem("is after");
			combo.addItem("between");
		}
		if (attrType.equals("3")) {
			paneeli.removeAll();
			if (valist == null) {
				paneeli.add(jtf);
			} else {
				paneeli.add(vacombo);
			}
			paneeli.revalidate();
			paneeli.repaint();

			combo.removeAllItems();
			combo.addItem("begins with");
			combo.addItem("is");
			combo.addItem("contains");

		}
		if (attrType.equals("2")) {
			paneeli.removeAll();
			if (valist == null) {
				paneeli.add(jtf);
			} else {
				paneeli.add(vacombo);
			}
			paneeli.revalidate();
			paneeli.repaint();

			combo.removeAllItems();
			combo.addItem("begins with");
			combo.addItem("is");
			combo.addItem("contains");

		}
		if (attrType.equals("1")) {
			paneeli.removeAll();
			if (valist == null) {
				paneeli.add(jtf);
			} else {
				paneeli.add(vacombo);
			}
			paneeli.revalidate();
			paneeli.repaint();

			combo.removeAllItems();
			combo.addItem("begins with");
			combo.addItem("is");
			combo.addItem("contains");

		}
		if (attrType.equals("0")) {
			paneeli.removeAll();
			if (valist == null) {
				paneeli.add(jtf);
			} else {
				paneeli.add(vacombo);
			}
			paneeli.revalidate();
			paneeli.repaint();

			combo.removeAllItems();
			combo.addItem("begins with");
			combo.addItem("is");
			combo.addItem("contains");

		}

	}

	private void cmbAttr3ItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_cmbAttr3ItemStateChanged
		Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
		setCursor(cur);

		try {
			IDfList valist = null;
			ComboItem c = (ComboItem) cmbAttr3.getSelectedItem();
			if (validator.hasValueAssistance(c.getAttrName())) {

				IDfValueAssistance va = validator.getValueAssistance(c.getAttrName(), null);
				valist = va.getActualValues();
				for (int i = 0; i < valist.getCount(); i++) {
					vacombo3.addItem(valist.get(i));
				}
			}
			ComboItem ci = (ComboItem) cmbAttr3.getSelectedItem();
			buildAttrGui(ci, hakuPanel3, jdc3, txt3, vacombo3, cmbEhto3, valist);
		} catch (DfException ex) {
			ex.printStackTrace();
			log.error(ex);
		} finally {
			cur = new Cursor(Cursor.DEFAULT_CURSOR);
			setCursor(cur);

		}

	}// GEN-LAST:event_cmbAttr3ItemStateChanged

	private void cmbEhto1ItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_cmbEhto1ItemStateChanged
		String jeejee = (String) cmbEhto1.getSelectedItem();
		if (jeejee != null) {
			if (cmbEhto1.getSelectedItem().equals("between")) {
				// //System.out.println("now in between.");
				hakuPanel1.removeAll();
				hakuPanel1.setLayout(new MigLayout("insets 0"));
				JLabel infoLabel = new JLabel("AND");
				infoLabel.setHorizontalAlignment(JLabel.CENTER);
				hakuPanel1.add(jdc1, "w 100!");
				hakuPanel1.add(infoLabel, "w 30!");
				infoLabel.setVisible(true);
				jdc1.setVisible(true);
				jdc1b.setVisible(true);
				hakuPanel1.add(jdc1b, "w 100!");
				hakuPanel1.revalidate();
				hakuPanel1.repaint();
			}
		}
	}// GEN-LAST:event_cmbEhto1ItemStateChanged

	private void cmbAttr3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmbAttr3ActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_cmbAttr3ActionPerformed

	private void cmbEhto2ItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_cmbEhto2ItemStateChanged
		String jeejee = (String) cmbEhto2.getSelectedItem();
		if (jeejee != null) {
			if (cmbEhto2.getSelectedItem().equals("between")) {
				// //System.out.println("now in between.");
				hakuPanel2.removeAll();
				hakuPanel2.setLayout(new MigLayout("insets 0"));
				JLabel infoLabel = new JLabel("AND");
				infoLabel.setHorizontalAlignment(JLabel.CENTER);
				hakuPanel2.add(jdc2, "w 100!");
				hakuPanel2.add(infoLabel, "w 30!");
				infoLabel.setVisible(true);
				jdc2.setVisible(true);
				jdc2b.setVisible(true);
				hakuPanel2.add(jdc2b, "w 100!");
				hakuPanel2.revalidate();
				hakuPanel2.repaint();
			}
		}
	}// GEN-LAST:event_cmbEhto2ItemStateChanged

	private void cmbEhto3ItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_cmbEhto3ItemStateChanged
		String jeejee = (String) cmbEhto3.getSelectedItem();
		if (jeejee != null) {
			if (cmbEhto3.getSelectedItem().equals("between")) {
				// //System.out.println("now in between.");
				hakuPanel3.removeAll();
				hakuPanel3.setLayout(new MigLayout("insets 0"));
				JLabel infoLabel = new JLabel("AND");
				infoLabel.setHorizontalAlignment(JLabel.CENTER);
				hakuPanel3.add(jdc3, "w 100!");
				hakuPanel3.add(infoLabel, "w 30!");
				infoLabel.setVisible(true);
				jdc3.setVisible(true);
				jdc3b.setVisible(true);
				hakuPanel3.add(jdc3b, "w 100!");
				hakuPanel3.revalidate();
				hakuPanel3.repaint();
			}
		}
	}// GEN-LAST:event_cmbEhto3ItemStateChanged

	private void cmbEhto4ItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_cmbEhto4ItemStateChanged
		String jeejee = (String) cmbEhto4.getSelectedItem();
		if (jeejee != null) {
			if (cmbEhto4.getSelectedItem().equals("between")) {
				// //System.out.println("now in between.");
				hakuPanel4.removeAll();
				hakuPanel4.setLayout(new MigLayout("insets 0"));
				JLabel infoLabel = new JLabel("AND");
				infoLabel.setHorizontalAlignment(JLabel.CENTER);
				hakuPanel4.add(jdc4, "w 100!");
				hakuPanel4.add(infoLabel, "w 30!");
				infoLabel.setVisible(true);
				jdc4.setVisible(true);
				jdc4b.setVisible(true);
				hakuPanel4.add(jdc4b, "w 100!");
				hakuPanel4.revalidate();
				hakuPanel4.repaint();
			}
		}
	}// GEN-LAST:event_cmbEhto4ItemStateChanged

	private void cmbAttr4ItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_cmbAttr4ItemStateChanged
		Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
		setCursor(cur);

		try {
			IDfList valist = null;
			ComboItem c = (ComboItem) cmbAttr4.getSelectedItem();
			if (validator.hasValueAssistance(c.getAttrName())) {

				IDfValueAssistance va = validator.getValueAssistance(c.getAttrName(), null);
				valist = va.getActualValues();
				for (int i = 0; i < valist.getCount(); i++) {
					vacombo4.addItem(valist.get(i));
				}
			}
			ComboItem ci = (ComboItem) cmbAttr4.getSelectedItem();
			buildAttrGui(ci, hakuPanel4, jdc4, txt4, vacombo4, cmbEhto4, valist);
		} catch (DfException ex) {
			ex.printStackTrace();
			log.error(ex);
		} finally {
			cur = new Cursor(Cursor.DEFAULT_CURSOR);
			setCursor(cur);

		}

	}// GEN-LAST:event_cmbAttr4ItemStateChanged

	private void modifiedEhtoItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_modifiedEhtoItemStateChanged
		String value = (String) modifiedEhto.getSelectedItem();
		if (value.equalsIgnoreCase("between")) {
			modifiedDateLast.setEnabled(true);
		} else {
			modifiedDateLast.setEnabled(false);
		}
	}// GEN-LAST:event_modifiedEhtoItemStateChanged

	private void createdEhtoItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_createdEhtoItemStateChanged
		String value = (String) createdEhto.getSelectedItem();
		if (value.equalsIgnoreCase("between")) {
			createdDateLast.setEnabled(true);
		} else {
			createdDateLast.setEnabled(false);
		}
	}// GEN-LAST:event_createdEhtoItemStateChanged

	private void chkOwnerModifierActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_chkOwnerModifierActionPerformed
		if (ispopulated == false) {
			Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
			setCursor(cur);
			IDfSession session = null;
			IDfCollection col = null;
			try {
				session = smanager.getSession();
				IDfQuery query = new DfQuery();
				query.setDQL("select user_name from dm_user order by user_name");
				col = query.execute(session, DfQuery.READ_QUERY);
				while (col.next()) {
					cmbUsers.addItem(col.getString("user_name"));
				}
				ispopulated = true;
			} catch (DfException ex) {
				log.error(ex);
				JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);

			} finally {
				if (col != null) {
					try {
						col.close();
					} catch (DfException ex) {
						ex.printStackTrace();
					}
				}
				if (session != null) {
					smanager.releaseSession(session);
				}
				Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
				setCursor(cur2);
			}
		}
	}// GEN-LAST:event_chkOwnerModifierActionPerformed

	private void cmbUsersMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {// GEN-FIRST:event_cmbUsersMouseWheelMoved
		int maxindex = cmbUsers.getItemCount();
		if (evt.getWheelRotation() > 0) {
			int index = cmbUsers.getSelectedIndex();
			if (index < maxindex - 1) {
				cmbUsers.setSelectedIndex(index + 1);
			}
		} else {
			int index = cmbUsers.getSelectedIndex();
			if (index > 0) {
				cmbUsers.setSelectedIndex(index - 1);
			}
		}
	}// GEN-LAST:event_cmbUsersMouseWheelMoved

	public void setListener(ActionListener cc) {
		this.a = cc;
	}

	public FolderSelectorData getFolderselectordata() {
		return folderselectordata;
	}

	public void setFolderselectordata(FolderSelectorData folderselectordata) {
		this.folderselectordata = folderselectordata;
	}

	private FolderSelectorData folderselectordata;
	DefaultTableModel model;
	ActionListener a;
	private boolean isloading;
	private JDateChooser jdc1;
	private JDateChooser jdc2;
	private JDateChooser jdc3;
	private JDateChooser jdc4;
	private JDateChooser jdc1b;
	private JDateChooser jdc2b;
	private JDateChooser jdc3b;
	private JDateChooser jdc4b;
	private JTextField txt1;
	private JTextField txt2;
	private JTextField txt3;
	private JTextField txt4;
	private String standardqueryattributes = "object_name, r_object_id, r_link_cnt, r_object_type, a_content_type, i_is_replica, r_lock_owner, r_is_virtual_doc, i_is_reference";
	private String additionalqueryattributes = "";
	private String thumbnails = "";
	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.ButtonGroup buttonGroup1;
	private javax.swing.ButtonGroup buttonGroup2;
	private javax.swing.JCheckBox chkAllVersions;
	private javax.swing.JCheckBox chkCreated;
	private javax.swing.JCheckBox chkFTDQL;
	private javax.swing.JCheckBox chkModified;
	private javax.swing.JCheckBox chkOwnerModifier;
	private javax.swing.JCheckBox chkThumbnails;
	private javax.swing.JComboBox cmbANDOR2;
	private javax.swing.JComboBox cmbANDOR3;
	private javax.swing.JComboBox cmbANDOR4;
	private javax.swing.JComboBox cmbAttr1;
	private javax.swing.JComboBox cmbAttr2;
	private javax.swing.JComboBox cmbAttr3;
	private javax.swing.JComboBox cmbAttr4;
	private javax.swing.JComboBox cmbEhto1;
	private javax.swing.JComboBox cmbEhto2;
	private javax.swing.JComboBox cmbEhto3;
	private javax.swing.JComboBox cmbEhto4;
	private javax.swing.JComboBox cmbObjectType;
	private javax.swing.JComboBox cmbUserHakuehto;
	private javax.swing.JComboBox cmbUsers;
	private javax.swing.JButton cmdAddProperty;
	private javax.swing.JButton cmdBrowse;
	private javax.swing.JButton cmdClose;
	private javax.swing.JButton cmdRemoveProperty;
	private javax.swing.JButton cmdSearch;
	private com.toedter.calendar.JDateChooser createdDateFirst;
	private com.toedter.calendar.JDateChooser createdDateLast;
	private javax.swing.JComboBox createdEhto;
	private javax.swing.JPanel hakuPanel1;
	private javax.swing.JPanel hakuPanel2;
	private javax.swing.JPanel hakuPanel3;
	private javax.swing.JPanel hakuPanel4;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JPanel jPanel5;
	private javax.swing.JPanel jPanel6;
	private javax.swing.JLabel lblSearchStatus;
	private com.toedter.calendar.JDateChooser modifiedDateFirst;
	private com.toedter.calendar.JDateChooser modifiedDateLast;
	private javax.swing.JComboBox modifiedEhto;
	private javax.swing.JTextField txtFullText;
	private javax.swing.JTextField txtLocation;
	private JButton btnSave;

	// End of variables declaration//GEN-END:variables

	public JDesktopPane getDesktop() {
		return desktop;
	}

	public void setDesktop(JDesktopPane desktop) {
		this.desktop = desktop;
	}
}
