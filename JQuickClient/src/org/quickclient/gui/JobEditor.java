package org.quickclient.gui;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Date;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.text.BadLocationException;

import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.ExJTextArea;
import org.quickclient.classes.MethodSelectorData;

import com.documentum.fc.client.IDfPersistentObject;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfLogger;
import com.documentum.fc.common.DfTime;
import com.documentum.fc.common.IDfId;
import com.documentum.fc.common.IDfTime;
import com.toedter.calendar.JDateChooser;


public class JobEditor extends JFrame {
	private JPanel jPanel6;
	private JPanel jPanel4;
	private JCheckBox chkPassStandard;
	private JTextField txtDesignatedServer;
	private JButton cmdChooseServer;
	private JLabel jLabel4;
	private JLabel jLabel5;
	private JLabel jLabel6;
	private JLabel jLabel7;
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JTextField txtJobbName;
	private JTextField txtJobCategory;
	private JCheckBox chkActive;
	private JCheckBox chkInactivate;
	private JTextField txtMaxIter;
	private JTextField txtMethod;
	private JButton cmdSelectMethod;
	private JScrollPane jScrollPane2;
	private ExJTextArea txtArgiments;
	private JLabel jLabel3;
	private JTextField txtTraceLevel;
	private JPanel jPanel5;
	private JLabel jLabel8;
	private JDateChooser jDateChooserNextRunDate;
	private JLabel jLabel9;
	private JLabel jLabel10;
	private JComboBox cmbTiming;
	private JTextField txtRunInterval;
	private JTextField jTextField7;
	private JLabel jLabel11;
	private JCheckBox jCheckBox4;
	private JDateChooser jDateChooserActivationDate;
	private JDateChooser jDateChooserExpirantionDate;
	private JButton cmdClose;
	
	private JLabel jLabel12;
	private JPanel jPanel1;
	private JButton cmdSave;
	private IDfId id;

	public JobEditor() {
	
		initComponents();
		
	}
	
	public JobEditor(IDfId iidee) {
		this.id = iidee;
		initComponents();
		initdata();
	}

	private void initComponents() {
		this.setSize(460, 620);
		jPanel6 = new javax.swing.JPanel();
		jPanel6.setBounds(0, 0, 440, 582);
		jPanel4 = new javax.swing.JPanel();
		jPanel4.setBounds(10, 10, 430, 330);
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		txtJobbName = new javax.swing.JTextField();
		txtJobCategory = new javax.swing.JTextField();
		chkActive = new javax.swing.JCheckBox();
		chkInactivate = new javax.swing.JCheckBox();
		txtMaxIter = new javax.swing.JTextField();
		txtMethod = new javax.swing.JTextField();
		cmdSelectMethod = new javax.swing.JButton();
		jScrollPane2 = new javax.swing.JScrollPane();
		txtArgiments = new ExJTextArea();
		txtArgiments.addKeyListener(new KeyListener() {

			public void keyTyped(KeyEvent e) {
				ExJTextArea mummo = (ExJTextArea) e.getSource();
				txtArgiments.putClientProperty("haschanged", "true");
			}

			public void keyPressed(KeyEvent e) {
				ExJTextArea mummo = (ExJTextArea) e.getSource();
				txtArgiments.putClientProperty("haschanged", "true");
			}

			public void keyReleased(KeyEvent e) {
				ExJTextArea mummo = (ExJTextArea) e.getSource();
				txtArgiments.putClientProperty("haschanged", "true");
			}
		});
		txtArgiments.setToolTipText("Job Arguments");
		txtArgiments.putClientProperty("haschanged", false);

		jLabel3 = new javax.swing.JLabel();
		txtTraceLevel = new javax.swing.JTextField();
		chkPassStandard = new javax.swing.JCheckBox();
		jLabel4 = new javax.swing.JLabel();
		txtDesignatedServer = new javax.swing.JTextField();
		cmdChooseServer = new javax.swing.JButton();
		jLabel5 = new javax.swing.JLabel();
		jLabel6 = new javax.swing.JLabel();
		jLabel7 = new javax.swing.JLabel();
		jPanel5 = new javax.swing.JPanel();
		jPanel5.setBounds(10, 340, 430, 180);
		jLabel8 = new javax.swing.JLabel();
		jDateChooserNextRunDate = new com.toedter.calendar.JDateChooser();
		jLabel9 = new javax.swing.JLabel();
		jLabel10 = new javax.swing.JLabel();
		jDateChooserExpirantionDate = new com.toedter.calendar.JDateChooser();
		jDateChooserActivationDate = new com.toedter.calendar.JDateChooser();
		jCheckBox4 = new javax.swing.JCheckBox();
		jLabel11 = new javax.swing.JLabel();
		jTextField7 = new javax.swing.JTextField();
		cmbTiming = new javax.swing.JComboBox();
		txtRunInterval = new javax.swing.JTextField();
		jLabel12 = new javax.swing.JLabel();
		jPanel1 = new javax.swing.JPanel();
		jPanel1.setBounds(20, 531, 410, 40);
		cmdSave = new javax.swing.JButton();
		cmdClose = new javax.swing.JButton();


		this.getContentPane().add(jPanel6);
		jPanel6.setLayout(null);

		jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("General Attributes"));
		jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

		jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		jLabel1.setText("Job Name:");
		jPanel4.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, 60, -1));

		jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		jLabel2.setText("Job Category:");
		jPanel4.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 80, -1));
		jPanel4.add(txtJobbName, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 30, 230, -1));
		jPanel4.add(txtJobCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 60, 230, -1));

		chkActive.setText("Active");
		chkActive.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		chkActive.setMargin(new java.awt.Insets(0, 0, 0, 0));
		jPanel4.add(chkActive, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 90, 70, -1));

		chkInactivate.setText("Inactive in failure");
		chkInactivate.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		chkInactivate.setMargin(new java.awt.Insets(0, 0, 0, 0));
		jPanel4.add(chkInactivate, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 90, -1, -1));
		jPanel4.add(txtMaxIter, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 110, 230, -1));
		jPanel4.add(txtMethod, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 140, 150, -1));

		cmdSelectMethod.setText("Select");
		cmdSelectMethod.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cmdSelectMethodActionPerformed(evt);
			}
		});
		this.getContentPane().setLayout(null);
		jPanel6.setLayout(null);
		jPanel4.add(cmdSelectMethod, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 140, 70, 20));

		txtArgiments.setColumns(20);
		txtArgiments.setFont(new java.awt.Font("Courier", 0, 11));
		txtArgiments.setRows(5);
		jScrollPane2.setViewportView(txtArgiments);

		jPanel4.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 170, 230, -1));

		jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		jLabel3.setText("Trace Level:");
		jPanel4.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 260, 70, -1));
		jPanel4.add(txtTraceLevel, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 260, 70, -1));

		chkPassStandard.setText("Pass standard arguments");
		chkPassStandard.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		chkPassStandard.setMargin(new java.awt.Insets(0, 0, 0, 0));
		jPanel4.add(chkPassStandard, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 263, -1, -1));

		jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		jLabel4.setText("Des. Server:");
		jPanel4.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 290, 70, -1));
		jPanel4.add(txtDesignatedServer, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 290, 150, -1));

		cmdChooseServer.setText("Select");
		jPanel4.add(cmdChooseServer, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 290, 70, 20));

		jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		jLabel5.setText("Arguments:");
		jPanel4.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 170, 70, -1));

		jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		jLabel6.setText("Max iterations:");
		jPanel4.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 90, -1));

		jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		jLabel7.setText("Method:");
		jPanel4.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 140, 70, -1));

		jPanel6.add(jPanel4);


		jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Scheduling"));
		jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

		jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		jLabel8.setText("Activation Date:");
		jPanel5.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 90, -1));

		jDateChooserNextRunDate.setDateFormatString("d.M.yyyy hh:mm:ss");
		jPanel5.add(jDateChooserNextRunDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 90, 180, -1));

		jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		jLabel9.setText("Expiration Date:");
		jPanel5.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 90, -1));

		jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		jLabel10.setText("Next Run Date:");
		jPanel5.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 90, -1));

		jDateChooserExpirantionDate.setDateFormatString("d.M.yyyy hh:mm:ss");
		jPanel5.add(jDateChooserExpirantionDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 60, 180, -1));

		jDateChooserActivationDate.setDateFormatString("d.M.yyyy hh:mm:ss");
		jPanel5.add(jDateChooserActivationDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 30, 180, -1));

		jCheckBox4.setText("Is Continued");
		jCheckBox4.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		jCheckBox4.setMargin(new java.awt.Insets(0, 0, 0, 0));
		jPanel5.add(jCheckBox4, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 150, 80, 20));
		GroupLayout gl_jPanel1 = new GroupLayout(jPanel1);
		gl_jPanel1.setHorizontalGroup(
			gl_jPanel1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_jPanel1.createSequentialGroup()
					.addContainerGap(274, Short.MAX_VALUE)
					.addComponent(cmdSave)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(cmdClose)
					.addGap(6))
		);
		gl_jPanel1.setVerticalGroup(
			gl_jPanel1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_jPanel1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_jPanel1.createParallelGroup(Alignment.BASELINE)
						.addComponent(cmdSave)
						.addComponent(cmdClose))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		jPanel1.setLayout(gl_jPanel1);
		jLabel11.setText("Continuation Interval:");
		jPanel5.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, -1, -1));
		jPanel5.add(jTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 150, 90, -1));

		cmbTiming.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Minutes", "Hours", "Days", "Weeks", "Months", "x day of Week", "x day of Month", "x day of Year" }));
		jPanel5.add(cmbTiming, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 120, 110, -1));

		txtRunInterval.setText("1");
		jPanel5.add(txtRunInterval, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 120, 60, -1));

		jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		jLabel12.setText("Run Job every:");
		jPanel5.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 80, -1));

		jPanel6.add(jPanel5);
		
		cmdSave.setMnemonic('s');
		cmdSave.setText("Save");
		cmdSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cmdSaveActionPerformed(evt);
			}
		});

		cmdClose.setText("Close");
		cmdClose.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cmdCloseActionPerformed(evt);
			}
		});
		
		jPanel6.add(jPanel1);
	}

	private void cmdSelectMethodActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdSelectMethodActionPerformed
		final MethodSelectorData data = new MethodSelectorData();
		ActionListener ax = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtMethod.setText(data.getMethodname());
			}
		};
		MethodSelectorFrame frame = new MethodSelectorFrame(ax, data);
		frame.setVisible(true);
	}

	private void initdata() {
		IDfSession session = null;
		DocuSessionManager smanager = DocuSessionManager.getInstance();

		try {
			session = smanager.getSession();
			txtArgiments.setText("");
			
			IDfPersistentObject obj = session.getObject(this.id);
			if (obj.getBoolean("pass_standard_arguments")) {
				chkPassStandard.setSelected(true);
			} else {
				chkPassStandard.setSelected(false);
			}
			txtJobbName.setText(obj.getString("object_name"));
			txtMethod.setText(obj.getString("method_name"));
			txtDesignatedServer.setText(obj.getString("target_server"));
			txtJobCategory.setText(obj.getString("title"));
			txtMaxIter.setText(obj.getString("max_iterations"));
			txtTraceLevel.setText(obj.getString("method_trace_level"));
			int valcount = obj.getValueCount("method_arguments");
			for (int i = 0; i < valcount; i++) {
				String val = obj.getRepeatingString("method_arguments", i);
				txtArgiments.append(val);
				if (i < valcount - 1) {
					txtArgiments.append("\n");
				}
			}
			String isinactive = obj.getString("is_inactive");
			if (isinactive.equalsIgnoreCase("T")) {
				chkActive.setSelected(false);
			} else {
				chkActive.setSelected(true);
			}
			IDfTime actTime = obj.getTime("start_date");
			Date jee = actTime.getDate();

			jDateChooserActivationDate.setDate(jee);
			// //System.out.println(session.getConnectionConfig().dump());
			IDfTime nextRunDate = obj.getTime("a_next_invocation");
			Date jee2 = nextRunDate.getDate();
			jDateChooserNextRunDate.setDate(jee2);
			IDfTime stopdate = obj.getTime("expiration_date");
			Date jee3 = stopdate.getDate();
			jDateChooserExpirantionDate.setDate(jee3);

			txtRunInterval.setText(obj.getString("run_interval"));
			int runMode = obj.getInt("run_mode");
			switch (runMode) {
			case 1:
				cmbTiming.setSelectedItem("Minutes");
				break;
			case 2:
				cmbTiming.setSelectedItem("Hours");
				break;
			case 3:
				cmbTiming.setSelectedItem("Days");
				break;
			case 4:
				cmbTiming.setSelectedItem("Weeks");
				break;
			case 5:
				cmbTiming.setSelectedItem("Months");
				break;
			case 7:
				cmbTiming.setSelectedItem("x day of Week");
				break;
			case 8:
				cmbTiming.setSelectedItem("x day of Month");
				break;
			case 9:
				cmbTiming.setSelectedItem("x day of Year");
				break;
			default:
			}
		} catch (DfException ex) {
			DfLogger.error(this, null, null, ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
		} finally {
			if (session != null) {
				smanager.releaseSession(session);
			}
		}

	}

	private void cmdSaveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdSaveActionPerformed
		IDfId iidee = id;//new DfId(id);
		IDfSession session = null;
		DocuSessionManager smanager = DocuSessionManager.getInstance();
		try {
			session = smanager.getSession();
			IDfPersistentObject obj = session.getObject(iidee);
			obj.setBoolean("pass_standard_arguments", chkPassStandard.isSelected());
			obj.setString("object_name", txtJobbName.getText());
			obj.setString("target_server", txtDesignatedServer.getText());
			obj.setString("title", txtJobCategory.getText());
			obj.setString("max_iterations", txtMaxIter.getText());
			obj.setString("method_trace_level", txtTraceLevel.getText());
			obj.setString("method_name", txtMethod.getText());
			obj.setString("run_interval", txtRunInterval.getText());

			String xxx = (String) cmbTiming.getSelectedItem();
			if (xxx.equalsIgnoreCase("minutes")) {
				obj.setInt("run_mode", 1);
			}
			if (xxx.equalsIgnoreCase("hours")) {
				obj.setInt("run_mode", 2);
			}
			if (xxx.equalsIgnoreCase("days")) {
				obj.setInt("run_mode", 3);
			}
			if (xxx.equalsIgnoreCase("weeks")) {
				obj.setInt("run_mode", 4);
			}
			if (xxx.equalsIgnoreCase("months")) {
				obj.setInt("run_mode", 5);
			}
			if (xxx.equalsIgnoreCase("x day of Week")) {
				obj.setInt("run_mode", 7);
			}
			if (xxx.equalsIgnoreCase("x day of Month")) {
				obj.setInt("run_mode", 8);
			}
			if (xxx.equalsIgnoreCase("x day of Year")) {
				obj.setInt("run_mode", 9);
			}

			Date actDate = jDateChooserActivationDate.getDate();
			IDfTime actTime = new DfTime(actDate);
			obj.setTime("start_date", actTime);

			Date invDate = jDateChooserNextRunDate.getDate();
			IDfTime invTime = new DfTime(invDate);
			obj.setTime("a_next_invocation", invTime);

			Date stopDate = jDateChooserExpirantionDate.getDate();
			IDfTime stopTime = new DfTime(stopDate);
			obj.setTime("expiration_date", stopTime);
			
			if (txtArgiments.getClientProperty("haschanged").equals("true")) {
				obj.truncate("method_arguments", 0);
				int valcount = txtArgiments.getRows();
				for (int i = 0; i < valcount; i++) {
					int start = -1, end = -1;
					try {
						start = txtArgiments.getLineStartOffset(i);
						end = txtArgiments.getLineEndOffset(i);
						String jees = txtArgiments.getText(start, end - start);
						int length = jees.length();
						jees = jees.replaceAll("\\n", "");
						DfLogger.debug(this, jees, null,null);
						obj.appendString("method_arguments", jees);
					} catch (BadLocationException ex) {
						DfLogger.error(this, ex.getMessage(), null, ex);
						ex.printStackTrace();
					}

				}
			}
			obj.save();
		} catch (DfException ex) {
			DfLogger.error(this, ex.getMessage(), null, ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);

		} finally {
			smanager.releaseSession(session);
		}
		
	}
	
	private void cmdCloseActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdCloseActionPerformed
		this.dispose();
	}


}
