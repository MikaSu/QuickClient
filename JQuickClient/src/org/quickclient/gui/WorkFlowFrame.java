/*
 * WorkFlowFrame.java
 *
 * Created on 22. kesäkuuta 2008, 11:49
 */
package org.quickclient.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;
import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.ExJTextArea;
import org.quickclient.classes.PackageTransferHandler;
import org.quickclient.classes.SwingHelper;
import org.quickclient.classes.UserorGroupSelectorData;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfActivity;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfProcess;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfWorkflow;
import com.documentum.fc.client.IDfWorkflowBuilder;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfList;
import com.documentum.fc.common.IDfId;
import com.documentum.fc.common.IDfList;


/**
 * 
 * @author Administrator
 */
public class WorkFlowFrame extends javax.swing.JFrame {

	IDfWorkflowBuilder wfBldrObj = null;
	private DocuSessionManager smanager;
	private String objid;
	DefaultTableModel model = new DefaultTableModel();
	DefaultTableModel docmodel = new DefaultTableModel();
	DefaultTableModel packageModel = new DefaultTableModel();
	private String processID;
	private UserorGroupSelectorData selectordata = new UserorGroupSelectorData();
	private HashMap<String, DefaultTableModel> activityModel = new HashMap<String, DefaultTableModel>();
	IDfProcess process = null;
	Logger log = Logger.getLogger(WorkFlowFrame.class);

	/** Creates new form WorkFlowFrame */
	public WorkFlowFrame() {
		initComponents();
	}

	public WorkFlowFrame(String objid) {
		initComponents();
		this.objid = objid;
		initModel();
		smanager = DocuSessionManager.getInstance();
		IDfSession session = smanager.getSession();
		String format = "";
		String objname = "";
		String versionLabels = "";
		try {
			IDfSysObject obj = (IDfSysObject) session.getObject(new DfId(objid));
			format = obj.getFormat().getName();
			objname = obj.getObjectName();
			versionLabels = obj.getAllRepeatingStrings("r_version_label", ", ");
		} catch (DfException ex) {
			log.error(ex);
		}
		Vector<String> v = new Vector<String>();
		v.add(format + ",c");
		v.add(objname);
		v.add(versionLabels);

		packageModel.addRow(v);

		this.jScrollPane1.getViewport().setBackground(Color.WHITE);
		this.jScrollPane2.getViewport().setBackground(Color.WHITE);
		this.jScrollPane3.getViewport().setBackground(Color.WHITE);
		this.jScrollPane4.getViewport().setBackground(Color.WHITE);
		updateTableAll();
	}

	public void initModel() {
		model.setRowCount(0);
		tblWorkFlowTemplates.setRowHeight(22);
		tblWorkFlowTemplates.setAutoCreateColumnsFromModel(true);
		tblWorkFlowTemplates.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tblWorkFlowTemplates.setEditingRow(1);

		model.setColumnCount(0);
		model.addColumn(".");
		model.addColumn("Name");
		model.addColumn("Data");

		tblWorkFlowTemplates.setModel(model);
		tblWorkFlowTemplates.getColumnModel().getColumn(0).setCellRenderer(new FormatRenderer());

		TableColumn col = tblWorkFlowTemplates.getColumnModel().getColumn(0);
		col.setPreferredWidth(22);
		col.setMaxWidth(22);

		int lastIndex = tblWorkFlowTemplates.getColumnCount();
		tblWorkFlowTemplates.getColumnModel().removeColumn(tblWorkFlowTemplates.getColumnModel().getColumn(lastIndex - 1));
		DefaultTableModel performerModel = new DefaultTableModel();
		performerModel.setColumnCount(0);
		performerModel.addColumn("Name");
		tblPerformers.setModel(performerModel);

		packageModel.addColumn(".");
		packageModel.addColumn("Name");
		packageModel.addColumn("Version");
		packageModel.addColumn("data");
		tblPackages.setModel(packageModel);
		tblPackages.getColumnModel().getColumn(0).setCellRenderer(new FormatRenderer());
		tblPackages.getColumnModel().getColumn(0).setPreferredWidth(22);
		tblPackages.getColumnModel().getColumn(0).setMaxWidth(22);
		int lastIndexx = tblPackages.getColumnCount();
		tblPackages.getColumnModel().removeColumn(tblPackages.getColumnModel().getColumn(lastIndexx - 1));

		PackageTransferHandler ptt = new PackageTransferHandler();
		tblPackages.setTransferHandler(ptt);
		ptt.setmodel(packageModel);
	}

	private int getPackageCount(IDfSession sess, IDfActivity activity) throws DfException {
		int x = activity.getValueCount("r_port_type");
		HashMap<String, String> p = new HashMap<String, String>();
		for (int i = 0; i < x; i++) {
			if (activity.getRepeatingString("r_port_type", i).equals("INPUT")) {
				p.put(activity.getRepeatingString("r_package_name", i), activity.getRepeatingString("r_package_name", i));
			}
		}
		return p.size();
	}

	private Vector getPackageInputPortIndexes(IDfSession sess, IDfActivity activity) throws DfException {
		int x = activity.getValueCount("r_port_type");
		Vector<String> resHolder = new Vector<String>();
		Vector<Integer> indexHolder = new Vector<Integer>();
		for (int i = 0; i < x; i++) {
			if (activity.getRepeatingString("r_port_type", i).equals("INPUT")) {
				String joo = activity.getRepeatingString("r_package_name", i);
				if (!resHolder.contains(joo)) {
					resHolder.add(joo);
					indexHolder.add(new Integer(i));
				}
			}
		}
		return indexHolder;
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

		tabPane = new javax.swing.JTabbedPane();
		jPanel1 = new javax.swing.JPanel();
		jPanel3 = new javax.swing.JPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		tblWorkFlowTemplates = new javax.swing.JTable();
		jToggleButton1 = new javax.swing.JToggleButton();
		jToggleButton2 = new javax.swing.JToggleButton();
		jToggleButton3 = new javax.swing.JToggleButton();
		jButton1 = new javax.swing.JButton();
		jButton2 = new javax.swing.JButton();
		cmdNext1 = new javax.swing.JButton();
		jPanel2 = new javax.swing.JPanel();
		performerPanel = new javax.swing.JPanel();
		jScrollPane3 = new javax.swing.JScrollPane();
		tblPerformers = new javax.swing.JTable();
		cmdAddPerformer = new javax.swing.JButton();
		cmdRemovePerformer = new javax.swing.JButton();
		jLabel1 = new javax.swing.JLabel();
		activityCombo = new javax.swing.JComboBox();
		jButton3 = new javax.swing.JButton();
		jButton4 = new javax.swing.JButton();
		jPanel4 = new javax.swing.JPanel();
		jScrollPane2 = new javax.swing.JScrollPane();
		txtMessage = new ExJTextArea();
		jPanel5 = new javax.swing.JPanel();
		packagePanel = new javax.swing.JPanel();
		jScrollPane4 = new javax.swing.JScrollPane();
		tblPackages = new javax.swing.JTable();
		cmdAddPackage = new javax.swing.JButton();
		cmdRemovePackage = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
		jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

		tblWorkFlowTemplates.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

		}, new String[] {

		}));
		jScrollPane1.setViewportView(tblWorkFlowTemplates);

		jPanel3.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, 460, 270));

		jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconwell_all.gif"))); // NOI18N
		jToggleButton1.setSelected(true);
		jToggleButton1.setText("All");
		jToggleButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		jToggleButton1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
		jToggleButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jToggleButton1ActionPerformed(evt);
			}
		});
		jPanel3.add(jToggleButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 70, 60));

		jToggleButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconwell_subscriptions.gif"))); // NOI18N
		jToggleButton2.setText("Subscribed");
		jToggleButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		jToggleButton2.setMargin(new java.awt.Insets(2, 2, 2, 2));
		jToggleButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		jPanel3.add(jToggleButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 70, 60));

		jToggleButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconwell_select.gif"))); // NOI18N
		jToggleButton3.setText("Own ");
		jToggleButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		jToggleButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		jPanel3.add(jToggleButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 70, 60));

		jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconwell_folders.gif"))); // NOI18N
		jButton1.setText("Browse");
		jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		jPanel3.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 70, -1));

		jButton2.setText("Cancel");
		jButton2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton2ActionPerformed(evt);
			}
		});

		cmdNext1.setText("Next");
		cmdNext1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cmdNext1ActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				jPanel1Layout
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								jPanel1Layout
										.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 571, Short.MAX_VALUE)
										.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
												jPanel1Layout.createSequentialGroup().addComponent(cmdNext1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton2)))
						.addContainerGap()));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButton2).addComponent(cmdNext1)).addContainerGap()));

		tabPane.addTab("Workflow Templates", jPanel1);

		performerPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Performers"));

		tblPerformers.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { {}, {}, {}, {} }, new String[] {

		}));
		tblPerformers.setDragEnabled(true);
		jScrollPane3.setViewportView(tblPerformers);

		cmdAddPerformer.setText("Add Performer");
		cmdAddPerformer.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cmdAddPerformerActionPerformed(evt);
			}
		});

		cmdRemovePerformer.setText("Remove Performer");

		jLabel1.setText("Activity:");

		activityCombo.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
				activityComboItemStateChanged(evt);
			}
		});
		activityCombo.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				activityComboActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout performerPanelLayout = new javax.swing.GroupLayout(performerPanel);
		performerPanel.setLayout(performerPanelLayout);
		performerPanelLayout.setHorizontalGroup(performerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				performerPanelLayout
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								performerPanelLayout
										.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 539, Short.MAX_VALUE)
										.addGroup(
												javax.swing.GroupLayout.Alignment.TRAILING,
												performerPanelLayout.createSequentialGroup().addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
														.addComponent(activityCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 110, Short.MAX_VALUE).addComponent(cmdAddPerformer)
														.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cmdRemovePerformer))).addContainerGap()));
		performerPanelLayout.setVerticalGroup(performerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				performerPanelLayout
						.createSequentialGroup()
						.addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(
								performerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(cmdRemovePerformer).addComponent(cmdAddPerformer).addComponent(jLabel1)
										.addComponent(activityCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		jButton3.setText("jButton3");
		jButton3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton3ActionPerformed(evt);
			}
		});

		jButton4.setText("tuukan");
		jButton4.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton4ActionPerformed(evt);
			}
		});

		jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Message"));

		txtMessage.setColumns(20);
		txtMessage.setRows(5);
		jScrollPane2.setViewportView(txtMessage);

		javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
		jPanel4.setLayout(jPanel4Layout);
		jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 539, Short.MAX_VALUE).addContainerGap()));
		jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				jPanel4Layout.createSequentialGroup().addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(10, Short.MAX_VALUE)));

		javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				jPanel2Layout
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(performerPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addGroup(jPanel2Layout.createSequentialGroup().addComponent(jButton4).addGap(18, 18, 18).addComponent(jButton3))).addContainerGap()));
		jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				jPanel2Layout.createSequentialGroup().addContainerGap().addComponent(performerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE).addGap(18, 18, 18).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButton3).addComponent(jButton4)).addContainerGap()));

		tabPane.addTab("Performers", jPanel2);

		packagePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Packages"));

		tblPackages.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { {}, {}, {}, {} }, new String[] {

		}));
		jScrollPane4.setViewportView(tblPackages);

		cmdAddPackage.setText("Add Package");
		cmdAddPackage.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cmdAddPackageActionPerformed(evt);
			}
		});

		cmdRemovePackage.setText("Remove Package");
		cmdRemovePackage.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cmdRemovePackageActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout packagePanelLayout = new javax.swing.GroupLayout(packagePanel);
		packagePanel.setLayout(packagePanelLayout);
		packagePanelLayout.setHorizontalGroup(packagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				packagePanelLayout
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								packagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(packagePanelLayout.createSequentialGroup().addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 539, Short.MAX_VALUE).addContainerGap())
										.addGroup(packagePanelLayout.createSequentialGroup().addComponent(cmdAddPackage).addGap(18, 18, 18).addComponent(cmdRemovePackage).addGap(18, 18, 18)))));
		packagePanelLayout.setVerticalGroup(packagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				packagePanelLayout.createSequentialGroup().addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(packagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(cmdAddPackage).addComponent(cmdRemovePackage)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
		jPanel5.setLayout(jPanel5Layout);
		jPanel5Layout.setHorizontalGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
				jPanel5Layout.createSequentialGroup().addContainerGap().addComponent(packagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
		jPanel5Layout.setVerticalGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				jPanel5Layout.createSequentialGroup().addContainerGap().addComponent(packagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(161, Short.MAX_VALUE)));

		tabPane.addTab("Packages", jPanel5);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(tabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 596, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
				layout.createSequentialGroup().addComponent(tabPane, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	public String getIDfromTable() {
		int row = tblWorkFlowTemplates.getSelectedRow();
		Vector v = (Vector) model.getDataVector().elementAt(row);
		String objidx = (String) v.lastElement();
		return objidx;
	}

	private void cmdNext1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdNext1ActionPerformed

		Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
		setCursor(cur);

		processID = getIDfromTable();
		IDfSession session = smanager.getSession();
		try {
			process = (IDfProcess) session.getObject(new DfId(processID));

			wfBldrObj = session.newWorkflowBuilder(process.getObjectId());
			IDfList idList = wfBldrObj.getStartActivityIds();
			System.out.println("startactivity idlist size: " + idList.getCount());
			System.out.println("Chooser performer for Activities: ");
			activityModel.clear();
			int cnt = process.getActChooseByCount();

			int cnt2 = process.getActChooseForCount();
			int cnt3 = process.getActChooseNameCount();
			System.out.println(cnt);
			System.out.println(cnt2);
			System.out.println(cnt3);
			for (int i = 0; i < idList.getCount(); i++) {
				String jeejee = wfBldrObj.getStartActivityNames().getString(i);
				DefaultTableModel modelx = new DefaultTableModel();
				modelx.setColumnCount(0);
				modelx.addColumn("Name");
				System.out.println("jeejee: " + jeejee);
				activityModel.put(jeejee, modelx);

			}

			Set x = activityModel.keySet();
			Iterator joo = x.iterator();
			while (joo.hasNext()) {
				activityCombo.addItem(joo.next());
			}
			tabPane.setSelectedIndex(1);
			tabPane.setEnabledAt(0, false);
			// System.out.println("XX");
		} catch (DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);

		}
		cur = new Cursor(Cursor.DEFAULT_CURSOR);
		setCursor(cur);
	}// GEN-LAST:event_cmdNext1ActionPerformed

	private void cmdAddPerformerActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdAddPerformerActionPerformed

		ActionListener listener = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Vector<String> v = new Vector<String>();
				String jee = "";
				jee = selectordata.getUserorGroupname();
				if (jee != null)
					if (jee.trim().length() > 0) {
						String activityName = (String) activityCombo.getSelectedItem();
						DefaultTableModel model = activityModel.get(activityName);
						v.add(jee);
						model.addRow(v);
					}
			}
		};
		UserorGroupSelectorFrame frame = new UserorGroupSelectorFrame(listener, selectordata);
		//SwingHelper sh = new SwingHelper();
		SwingHelper.centerJFrame(frame);
		frame.setVisible(true);

	}// GEN-LAST:event_cmdAddPerformerActionPerformed

	private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton3ActionPerformed
	// TDO add your handling code here:
		String note = "testinote";
		try {
			boolean result = true;
			IDfSession session = smanager.getSession();
			String[] objectIds = new String[1];
			// r_object_id of the package1
			// objectIds[0] = package_meta_data.get("r_object_id").toString();
			// r_object_id of the workflowtest1
			// String processId =
			// workflow_meta_data.get("processId").toString();
			// String note = workflow_meta_data.get("note").toString();

			// Convert the list of object Ids into the necessary form.

			DfList componentIdList = new DfList();
			for (int i = 0; i < objectIds.length; i++) {

				componentIdList.append(new DfId(objectIds[i]));

			}

			// notePersistent - Set this to true if you want the
			// note defined in noteText to accompany the package
			// through the entire workflow. Set this false if you only
			// want the note to go to performers of the activities immediately
			// following the start activity. The notePersistent flag has no
			// effect if noteText is empty.
			boolean notePersistence = true;

			// Create a workflow builder for the specified process.

			IDfWorkflowBuilder workflowBuilder = session.newWorkflowBuilder(new DfId(processID));

			// Fetch the start activity and check a few limitations. We
			// currently only support a single start activity.

			IDfList idList = workflowBuilder.getStartActivityIds();
			IDfActivity activity = (IDfActivity) session.getObject((DfId) idList.get(0));
			if (idList.getCount() > 1) {
				// System.out.println("ERROR: Currently we support only one start activity!");
			}
			// Find the input port. Note that this command can only
			// support start activities with a single input port.

			int inputPortIndex = 0;
			boolean inputPortFound = false;
			String startActivity = "";
			String start = (String) activityCombo.getSelectedItem();
			System.out.println("start in combo: " + start);
			for (int i = 0; i < activity.getPortCount(); i++) {
				if (activity.getPortType(i).compareTo("INPUT") == 0) {
					if (inputPortFound == false) {
						inputPortFound = true;
						inputPortIndex = i;
						startActivity = activity.getObjectName();
						System.out.println("start in loop: " + startActivity);
					} else {
						// System.out.println("ERROR: Currently we support only one input port!");
					}
				}
			}

			;

			workflowBuilder.initWorkflow();
			IDfWorkflow wf = workflowBuilder.getWorkflow();
			setPerformers(session, wf);
			workflowBuilder.runWorkflow();
			System.out.println(activity.getObjectName() + " : " + activity.getPortName(inputPortIndex) + " : " + activity.getPackageName(inputPortIndex) + " : " + activity.getPackageType(inputPortIndex));
			// Insert the initial package to get things flowing.
			workflowBuilder.addPackage(activity.getObjectName(), 
					activity.getPortName(inputPortIndex), 
					activity.getPackageName(inputPortIndex), 
					activity.getPackageType(inputPortIndex), note, notePersistence, componentIdList);

			// Return status.
		} catch (DfException ex) {
			log.error(ex);
		}
	}// GEN-LAST:event_jButton3ActionPerformed

	private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton4ActionPerformed
		try {
			IDfSession sess = smanager.getSession();
			IDfList startActivityIdList = wfBldrObj.getStartActivityIds();

			// Initialize the workflow and start it running.
			wfBldrObj.initWorkflow();
			wfBldrObj.runWorkflow();
			IDfWorkflow workflow = wfBldrObj.getWorkflow();
			setPerformers(sess, workflow);
			IDfList componentIdList = new DfList();
			componentIdList.append(new DfId(objid));

			// Insert the initial package to get things flowing.
			for (int x = 0; x < startActivityIdList.getCount(); x++) {
				IDfId id = startActivityIdList.getId(x);
				IDfActivity activity = (IDfActivity) sess.getObject(id);

				int packagecount = getPackageCount(sess, activity);
				if (packagecount == 1) {
					wfBldrObj.addPackage(activity.getObjectName(), activity.getPortName(0), activity.getPackageName(0), activity.getPackageType(0), txtMessage.getText(), false, componentIdList);
				} else {
					int asize = packageModel.getRowCount();
					if (asize < packagecount) {
						JOptionPane.showMessageDialog(null, "Not enough packages, this workflow needs " + packagecount + " packages", "Error occured!", JOptionPane.INFORMATION_MESSAGE);
					} else {

						Vector<Integer> indices = getPackageInputPortIndexes(sess, activity);
						for (int i = 0; i < indices.size(); i++) {
							Integer tempinteger = indices.get(i);
							int index = tempinteger.intValue();
							wfBldrObj.addPackage(activity.getObjectName(), activity.getPortName(index), activity.getPackageName(index), activity.getPackageType(index), txtMessage.getText(), false, componentIdList);
						}

					}
				}
			}
			/*
			 * 
			 * 
			 * setPerformers
			 * 
			 * public void setPerformers(String actName, IDfList userGroupList)
			 * throws DfException
			 * 
			 * Sets the performers attributes for the specified activity name.
			 * 
			 * The values for the performer list depends on the performer type
			 * of the specified activity. Here is the valid values for each
			 * performer type:
			 * 
			 * Performer Type Valid performers 0 - Workflow Supervisor No effect
			 * 1 - Repository Owner No effect 2 - Last Performer No effect 3 -
			 * Specific User A single user name 4 - All Users From Group A
			 * single group name 5 - Single User From Group A single group name
			 * 6 - Least Loaded User From Group A single group name 8, 9 - Some
			 * Users (Sequentially) Any numbers of user names and/or group names
			 * (Note: For each group name provided, only one task will be
			 * generated and the first user who acquires the task owns the
			 * task.) 10 - Single User From Work Queue A single group name
			 * 
			 * 
			 * Parameters: actName - Activity name for which the performers is
			 * being set userGroupList - List of performers which contains user
			 * names and/or group names Throws: DfException - if server error
			 * occurs
			 * 
			 * 
			 * 
			 * 
			 * / /* IDfActivity actObj1 = null; for (int i = 0; i <
			 * list.getCount(); i++) { //System.out.println(list.get(i)); String
			 * iidee = (String)list.get(i); actObj1 =
			 * (IDfActivity)sess.getObject(new DfId(iidee)); } IDfList lista =
			 * new DfList(); lista.append(sysobj.getObjectId().getId());
			 * wfBldrObj.addPackage(actObj1.getObjectName(),
			 * actObj1.getPortName(0), sysobj.getObjectName(),
			 * "publish_request", null, false, lista);
			 */
			// return objid;
		} catch (Exception ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);

		}

		this.dispose();

	}// GEN-LAST:event_jButton4ActionPerformed

	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton2ActionPerformed
		this.dispose();
	}// GEN-LAST:event_jButton2ActionPerformed

	private void cmdAddPackageActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdAddPackageActionPerformed

	}// GEN-LAST:event_cmdAddPackageActionPerformed

	private void cmdRemovePackageActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdRemovePackageActionPerformed

	}// GEN-LAST:event_cmdRemovePackageActionPerformed

	private void activityComboActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_activityComboActionPerformed
	}// GEN-LAST:event_activityComboActionPerformed

	private void activityComboItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_activityComboItemStateChanged

		// System.out.println("dada");
		DefaultTableModel modellocel = activityModel.get(activityCombo.getSelectedItem());
		// System.out.println(modellocel);

		tblPerformers.setModel(modellocel);
	}// GEN-LAST:event_activityComboItemStateChanged

	private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jToggleButton1ActionPerformed
	// TODO add your handling code here:
	}// GEN-LAST:event_jToggleButton1ActionPerformed
		// Variables declaration - do not modify//GEN-BEGIN:variables

	private javax.swing.JComboBox activityCombo;
	private javax.swing.JButton cmdAddPackage;
	private javax.swing.JButton cmdAddPerformer;
	private javax.swing.JButton cmdNext1;
	private javax.swing.JButton cmdRemovePackage;
	private javax.swing.JButton cmdRemovePerformer;
	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;
	private javax.swing.JButton jButton3;
	private javax.swing.JButton jButton4;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JPanel jPanel5;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JScrollPane jScrollPane3;
	private javax.swing.JScrollPane jScrollPane4;
	private javax.swing.JToggleButton jToggleButton1;
	private javax.swing.JToggleButton jToggleButton2;
	private javax.swing.JToggleButton jToggleButton3;
	private javax.swing.JPanel packagePanel;
	private javax.swing.JPanel performerPanel;
	private javax.swing.JTabbedPane tabPane;
	private javax.swing.JTable tblPackages;
	private javax.swing.JTable tblPerformers;
	private javax.swing.JTable tblWorkFlowTemplates;
	private ExJTextArea txtMessage;

	// End of variables declaration//GEN-END:variables

	private void setPerformers(IDfSession sess, IDfWorkflow workflow) throws DfException {

		int count = activityModel.size();
		Set keyset = activityModel.keySet();
		Iterator it = keyset.iterator();
		while (it.hasNext()) {
			String activity = (String) it.next();
			DefaultTableModel x = activityModel.get(activity);
			IDfList list = new DfList();
			for (int i = 0; i < x.getRowCount(); i++) {
				list.appendString((String) x.getValueAt(i, 0));
			}
			workflow.setPerformers(activity, list);
		}

	}

	// End of variables declaration

	private void updateTableAll() {
		IDfSession session = smanager.getSession();
		IDfCollection col = null;

		IDfQuery query = new DfQuery();
		model.setRowCount(0);
		query.setDQL("SELECT * FROM dm_process o WHERE (r_definition_state=2) AND owner_name in ('dmadmin', 'docubase') ORDER BY object_name");
		try {
			col = query.execute(session, DfQuery.DF_READ_QUERY);
			while (col.next()) {
				String objid = col.getString("r_object_id");
				String objname = col.getString("object_name");
				Vector<String> v = new Vector<String>();
				v.add(",dm_process");
				v.add(objname);
				v.add(objid);
				model.addRow(v);
			}
			tblWorkFlowTemplates.setModel(model);
			tblWorkFlowTemplates.validate();
		} catch (DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);

		} finally {
			try {
				col.close();
			} catch (DfException ex) {
			}
			if (session != null) {
				smanager.releaseSession(session);
			}
		}
	}
}
