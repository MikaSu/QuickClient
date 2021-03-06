/*
 * SessionsFrame.java
 *
 * Created on 15. tammikuuta 2008, 23:17
 */
package org.quickclient.gui;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;
import org.quickclient.classes.ConfigService;
import org.quickclient.classes.DocuSessionManager;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;

/**
 *
 * @author Administrator
 */
public class SessionsFrame extends javax.swing.JFrame {

	DocuSessionManager smanager;
	DefaultTableModel sessionModel;
	Logger logger = Logger.getLogger(SessionsFrame.class);

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JCheckBox chkActiveOnly;

	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;

	private javax.swing.JMenu jMenu1;

	private javax.swing.JMenuItem jMenuItem1;

	private javax.swing.JMenuItem jMenuItem2;

	private javax.swing.JMenuItem jMenuItem3;

	private javax.swing.JPanel jPanel1;

	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JLabel lblSessionInfo;
	private javax.swing.JPopupMenu sessionPopUp;
	private javax.swing.JTable sessionTable;

	/** Creates new form SessionsFrame */
	public SessionsFrame() {
		initComponents();
		smanager = DocuSessionManager.getInstance();
		sessionModel = new DefaultTableModel();
		final String docbasename = ConfigService.getInstance().getDocbasename();
		this.setTitle("Sessions - " + docbasename);
		this.jScrollPane1.getViewport().setBackground(Color.WHITE);
		refreshData();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		sessionPopUp = new javax.swing.JPopupMenu();
		jMenu1 = new javax.swing.JMenu();
		jMenuItem3 = new javax.swing.JMenuItem();
		jMenuItem2 = new javax.swing.JMenuItem();
		jMenuItem1 = new javax.swing.JMenuItem();
		jScrollPane1 = new javax.swing.JScrollPane();
		sessionTable = new javax.swing.JTable();
		jPanel1 = new javax.swing.JPanel();
		jButton1 = new javax.swing.JButton();
		jButton2 = new javax.swing.JButton();
		lblSessionInfo = new javax.swing.JLabel();
		chkActiveOnly = new javax.swing.JCheckBox();

		jMenu1.setText("Kill Session");

		jMenuItem3.setText("Nice");
		jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				jMenuItem3ActionPerformed(evt);
			}
		});
		jMenu1.add(jMenuItem3);

		jMenuItem2.setText("After current request");
		jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				jMenuItem2ActionPerformed(evt);
			}
		});
		jMenu1.add(jMenuItem2);

		jMenuItem1.setText("Unsafe");
		jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				jMenuItem1ActionPerformed(evt);
			}
		});
		jMenu1.add(jMenuItem1);

		sessionPopUp.add(jMenu1);

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		sessionTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null } }, new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
		sessionTable.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseReleased(final java.awt.event.MouseEvent evt) {
				sessionTableMouseReleased(evt);
			}
		});
		jScrollPane1.setViewportView(sessionTable);

		jButton1.setText("Close");
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});

		jButton2.setText("Refresh");
		jButton2.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				jButton2ActionPerformed(evt);
			}
		});

		lblSessionInfo.setText(" ");

		chkActiveOnly.setText("Show Only Active Sessions");

		final org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup().add(lblSessionInfo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 660, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 32, Short.MAX_VALUE)
				.add(chkActiveOnly, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 177, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(jButton2).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jButton1).addContainerGap()));
		jPanel1Layout
				.setVerticalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup().addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jButton1).add(jButton2).add(lblSessionInfo).add(chkActiveOnly))));

		final org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().addContainerGap()
				.add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 1025, Short.MAX_VALUE)).addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(layout.createSequentialGroup().addContainerGap().add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap()));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void jButton1ActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed
		this.dispose();
	}// GEN-LAST:event_jButton1ActionPerformed

	private void jButton2ActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton2ActionPerformed
		refreshData();
	}// GEN-LAST:event_jButton2ActionPerformed

	private void jMenuItem1ActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem1ActionPerformed
		IDfSession session = null;
		try {
			session = smanager.getSession();
			final int row = sessionTable.getSelectedRow();
			final String sessionId = (String) sessionTable.getValueAt(row, 2);
			final String option = "unsafe";
			final StringBuilder stringBuffer = new StringBuilder(512);
			stringBuffer.append(sessionId);
			stringBuffer.append(",");
			stringBuffer.append(option);
			stringBuffer.append(",");
			stringBuffer.append("Your session was killed by Administrator");
			final boolean retval = session.apiExec("kill", stringBuffer.toString());
		} catch (final DfException e) {
			logger.error("sessionkill", e);
		} finally {
			if (session != null) {
				smanager.releaseSession(session);
			}
		}

	}// GEN-LAST:event_jMenuItem1ActionPerformed

	private void jMenuItem2ActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem2ActionPerformed
		IDfSession session = null;
		try {
			session = smanager.getSession();
			final int row = sessionTable.getSelectedRow();
			final String sessionId = (String) sessionTable.getValueAt(row, 2);

			final String option = "after current request";
			final StringBuilder stringBuffer = new StringBuilder(512);
			stringBuffer.append(sessionId);
			stringBuffer.append(",");
			stringBuffer.append(option);
			stringBuffer.append(",");
			stringBuffer.append("Your session was killed by Administrator");
			final boolean retval = session.apiExec("kill", stringBuffer.toString());
		} catch (final DfException e) {
			logger.error("sessionkill", e);
		} finally {
			if (session != null) {
				smanager.releaseSession(session);
			}
		}
	}// GEN-LAST:event_jMenuItem2ActionPerformed

	private void jMenuItem3ActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem3ActionPerformed
		// add your handling code here:
		IDfSession session = null;
		try {
			session = smanager.getSession();
			final int row = sessionTable.getSelectedRow();
			final String sessionId = (String) sessionTable.getValueAt(row, 2);

			final String option = "nice";
			// option = "after current request";
			// option = "unsafe";
			final StringBuilder stringBuffer = new StringBuilder(512);
			stringBuffer.append(sessionId);
			stringBuffer.append(",");
			stringBuffer.append(option);
			stringBuffer.append(",");
			stringBuffer.append("Your session was killed by Administrator");
			final boolean retval = session.apiExec("kill", stringBuffer.toString());
		} catch (final DfException e) {
			logger.error("sessionkill", e);
		} finally {
			if (session != null) {
				smanager.releaseSession(session);
			}
		}
	}// GEN-LAST:event_jMenuItem3ActionPerformed
		// End of variables declaration//GEN-END:variables

	private void refreshData() {
		IDfSession session = null;
		IDfCollection col = null;
		final Vector<Object> colVector = new Vector<>();
		int total = 0;
		int active = 0;
		int inactive = 0;
		try {
			sessionModel.setRowCount(0);
			String sessionstatus = "";
			session = smanager.getSession();

			final IDfQuery query = new DfQuery();
			query.setDQL("EXECUTE SHOW_SESSIONS");
			col = query.execute(session, DfQuery.DF_QUERY);
			colVector.add("user_name");
			colVector.add("session_status");
			colVector.add("session");
			colVector.add("pid");
			colVector.add("db_session_id");
			colVector.add("client_host");
			colVector.add("start");
			colVector.add("last_used");

			sessionModel.setColumnIdentifiers(colVector);
			while (col.next()) {
				final Vector<Object> rowVector = new Vector<>();

				sessionstatus = col.getString("session_status");
				if (sessionstatus.equalsIgnoreCase("active")) {
					active++;
					for (int x = 0; x < colVector.size(); x++) {
						rowVector.add(col.getRepeatingString((String) colVector.get(x), 0));
					}
				} else {
					inactive++;
					if (!chkActiveOnly.isSelected()) {
						for (int x = 0; x < colVector.size(); x++) {
							rowVector.add(col.getRepeatingString((String) colVector.get(x), 0));
						}
					}
				}
				total++;
				sessionModel.addRow(rowVector);
			}
			sessionTable.setModel(sessionModel);
			sessionTable.validate();
			lblSessionInfo.setText(active + " active sessions. " + inactive + " inactive sessions. (" + total + " total)");
		} catch (final DfException ex) {
			logger.error("sessions", ex);
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
	}

	private void sessionTableMouseReleased(final java.awt.event.MouseEvent evt) {// GEN-FIRST:event_sessionTableMouseReleased
		final int butt = evt.getButton();
		if (butt == MouseEvent.BUTTON3) {
			sessionPopUp.show(evt.getComponent(), evt.getX(), evt.getY());
		}
	}// GEN-LAST:event_sessionTableMouseReleased
}
