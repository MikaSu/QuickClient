/*
 * LoginFrame.java
 *
 * Created on 31. lokakuuta 2006, 0:25
 */
package org.quickclient.gui;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.quickclient.classes.ConfigService;
import org.quickclient.classes.DocuSessionManager;

import com.documentum.com.DfClientX;
import com.documentum.com.IDfClientX;
import com.documentum.fc.client.IDfClient;
import com.documentum.fc.client.IDfDocbaseMap;
import com.documentum.fc.client.IDfDocbrokerClient;
import com.documentum.fc.client.IDfPersistentObject;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.client.IDfTypedObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfLogger;
import com.documentum.fc.common.IDfLoginInfo;

/**
 *
 * @author Administrator
 */
public class LoginFrame extends javax.swing.JFrame {

	/**
	 * @param p
	 * @param h
	 * @param args
	 *            the command line arguments
	 */
	public static IDfSessionManager createSessionManager(final String docbase, final String user, final String pass, final String h, final String p) throws Exception {
		// create Client objects
		final IDfClientX clientx = new DfClientX();
		final IDfClient client = clientx.getLocalClient();
		if (h != null) {
			final IDfTypedObject config = client.getClientConfig();
			config.setString("primary_host", h);
			if (p != null) {
				config.setString("primary_port", p);
			}
		}

		final IDfSessionManager sMgr = client.newSessionManager();
		final IDfLoginInfo loginInfoObj = clientx.getLoginInfo();
		loginInfoObj.setUser(user);
		loginInfoObj.setPassword(pass);
		loginInfoObj.setDomain(null);
		sMgr.setIdentity(docbase, loginInfoObj);
		return sMgr;
	}

	private IDfDocbaseMap docbasemap;
	Logger logger = Logger.getLogger(LoginFrame.class);

	private MDIMainFrame mdimainpointer = null;

	private ComboBoxModel docbasecombomodel;

	private ComboBoxModel servercombomodel;

	IDfDocbrokerClient docbrokerclient;

	private boolean loading;

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JCheckBox chkAlternativeBroker;

	private javax.swing.JComboBox cmbDocbase;

	private javax.swing.JComboBox cmbServer;

	private javax.swing.JButton cmdCancel;

	private javax.swing.JButton cmdGetDataButton;

	private javax.swing.JButton cmdLogin;

	private javax.swing.JLabel jLabel1;

	private javax.swing.JLabel jLabel2;

	private javax.swing.JLabel jLabel3;

	private javax.swing.JLabel jLabel4;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JLabel lblHost;
	private javax.swing.JLabel lblPort;
	private javax.swing.JTextField txtHostName;
	private javax.swing.JPasswordField txtPassword;
	private javax.swing.JTextField txtPort;
	private javax.swing.JTextField txtUserName;

	// End of variables declaration//GEN-END:variables
	public LoginFrame() throws DfException {
		setTitle("Login");
		loading = true;
		initComponents();
		docbasecombomodel = new DefaultComboBoxModel();
		servercombomodel = new DefaultComboBoxModel();
		cmbDocbase.setModel(docbasecombomodel);
		cmbServer.setModel(servercombomodel);
		// try {
		final Cursor cur = new Cursor(Cursor.WAIT_CURSOR);

		setCursor(cur);
		final IDfClientX clientx = new DfClientX();
		docbrokerclient = clientx.getDocbrokerClient();
		if (docbrokerclient != null) {
			docbasemap = docbrokerclient.getDocbaseMap();
			final int valcount = docbasemap.getDocbaseCount();
			if (valcount == 0) {
				JOptionPane.showMessageDialog(null, "No repositories found.", "Error occured!", JOptionPane.ERROR_MESSAGE);
				final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
				setCursor(cur2);
				return;
			}
			for (int i = 0; i < valcount; i++) {
				cmbDocbase.addItem(docbasemap.getDocbaseName(i));
				System.out.println("looking for: " + docbasemap.getDocbaseName(i) + ".password");
				System.out.println("looking for: " + docbasemap.getDocbaseName(i) + ".username");
				final String pass = ConfigService.getInstance().getParameter(docbasemap.getDocbaseName(i) + ".password");
				final String user = ConfigService.getInstance().getParameter(docbasemap.getDocbaseName(i) + ".username");
				txtPassword.setText(pass);
				txtUserName.setText(user);
			}
			final IDfTypedObject servermap = docbasemap.getServerMap(0);
			// //System.out.println(servermap.toString());
			String serverString = "";
			final int servercount = servermap.getValueCount("r_server_name");
			for (int j = 0; j < servercount; j++) {
				final String serverName = servermap.getRepeatingString("r_server_name", j);
				final String serverStatus = servermap.getRepeatingString("r_last_status", j);
				final String serverHost = servermap.getRepeatingString("r_host_name", j);
				final String serverVersion = servermap.getRepeatingString("r_server_version", j);
				serverString = serverName + " (" + serverStatus + ") " + serverHost + " - " + serverVersion;
				// //System.out.println(serverString);
				cmbServer.addItem(serverString);
			}
		}
		/*
		 * } catch (DfException ex) { logger.error("failed to get docbroker",
		 * ex); JOptionPane.showMessageDialog(null, ex.getMessage(),
		 * "Docbroker error", JOptionPane.ERROR_MESSAGE); }
		 */
		final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);

		setCursor(cur2);
		loading = false;
	}

	/** Creates new form LoginFrame */
	public LoginFrame(final String docbase) {
		initComponents();
	}

	private void chkAlternativeBrokerActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_chkAlternativeBrokerActionPerformed
		if (chkAlternativeBroker.isSelected()) {
			txtHostName.setEnabled(true);
			txtPort.setEnabled(true);
			lblPort.setEnabled(true);
			lblHost.setEnabled(true);
			cmdGetDataButton.setVisible(true);
			cmdGetDataButton.setEnabled(true);
		} else {
			txtHostName.setEnabled(false);
			txtPort.setEnabled(false);
			lblPort.setEnabled(false);
			lblHost.setEnabled(false);
			cmdGetDataButton.setVisible(false);
		}
	}// GEN-LAST:event_chkAlternativeBrokerActionPerformed

	private void cmbDocbaseActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmbDocbaseActionPerformed

	}// GEN-LAST:event_cmbDocbaseActionPerformed

	private void cmbDocbaseItemStateChanged(final java.awt.event.ItemEvent evt) {// GEN-FIRST:event_cmbDocbaseItemStateChanged
		if (!loading) {
			final int idx = cmbDocbase.getSelectedIndex();
			try {
				final Cursor cur = new Cursor(Cursor.WAIT_CURSOR);

				setCursor(cur);
				cmbServer.removeAllItems();
				final IDfTypedObject servermap = docbasemap.getServerMap(idx);
				// //System.out.println(servermap.toString());
				String serverString = "";
				final int servercount = servermap.getValueCount("r_server_name");
				for (int j = 0; j < servercount; j++) {
					final String serverName = servermap.getRepeatingString("r_server_name", j);
					final String serverStatus = servermap.getRepeatingString("r_last_status", j);
					final String serverHost = servermap.getRepeatingString("r_host_name", j);
					final String serverVersion = servermap.getRepeatingString("r_server_version", j);
					serverString = serverName + " (" + serverStatus + ") " + serverHost + " - " + serverVersion;
					// //System.out.println(serverString);
					cmbServer.addItem(serverString);
				}
			} catch (final DfException ex) {
				DfLogger.error(this, ex.getMessage(), null, ex);
			}
			final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);

			setCursor(cur2);
		}
	}// GEN-LAST:event_cmbDocbaseItemStateChanged

	private void cmbDocbaseMouseWheelMoved(final java.awt.event.MouseWheelEvent evt) {// GEN-FIRST:event_cmbDocbaseMouseWheelMoved

		final int maxindex = cmbDocbase.getItemCount();
		if (evt.getWheelRotation() > 0) {
			final int index = cmbDocbase.getSelectedIndex();
			if (index < maxindex - 1) {
				cmbDocbase.setSelectedIndex(index + 1);
			}
		} else {
			final int index = cmbDocbase.getSelectedIndex();
			if (index > 0) {
				cmbDocbase.setSelectedIndex(index - 1);
			}
		}
	}// GEN-LAST:event_cmbDocbaseMouseWheelMoved

	private void cmdCancelActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdCancelActionPerformed
		this.dispose();
	}// GEN-LAST:event_cmdCancelActionPerformed

	private void cmdGetDataButtonActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdGetDataButtonActionPerformed
		docbasecombomodel = new DefaultComboBoxModel();
		servercombomodel = new DefaultComboBoxModel();
		cmbDocbase.setModel(docbasecombomodel);
		cmbServer.setModel(servercombomodel);
		try {
			final Cursor cur = new Cursor(Cursor.WAIT_CURSOR);

			setCursor(cur);
			final IDfClientX clientx = new DfClientX();
			final IDfClient client = clientx.getLocalClient();

			final String hostName = txtHostName.getText();
			final String portNumber = txtPort.getText();
			final IDfTypedObject config = client.getClientConfig();
			config.setString("primary_host", hostName);
			config.setString("primary_port", portNumber);

			docbasemap = client.getDocbaseMapEx("", hostName, portNumber);
			final int valcount = docbasemap.getDocbaseCount();
			for (int i = 0; i < valcount; i++) {
				cmbDocbase.addItem(docbasemap.getDocbaseName(i));
			}
			final IDfTypedObject servermap = docbasemap.getServerMap(0);
			// //System.out.println(servermap.toString());
			String serverString = "";
			final int servercount = servermap.getValueCount("r_server_name");
			for (int j = 0; j < servercount; j++) {
				final String serverName = servermap.getRepeatingString("r_server_name", j);
				final String serverStatus = servermap.getRepeatingString("r_last_status", j);
				final String serverHost = servermap.getRepeatingString("r_host_name", j);
				final String serverVersion = servermap.getRepeatingString("r_server_version", j);
				serverString = serverName + " (" + serverStatus + ") " + serverHost + " - " + serverVersion;
				// //System.out.println(serverString);
				cmbServer.addItem(serverString);
			}

		} catch (final DfException ex) {
			logger.error("failed to get docbroker", ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Docbroker error", JOptionPane.ERROR_MESSAGE);
		} finally {
			final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);

			setCursor(cur2);
		}
	}// GEN-LAST:event_cmdGetDataButtonActionPerformed

	private void cmdLoginActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdLoginActionPerformed
		doLogin();

	}// GEN-LAST:event_cmdLoginActionPerformed

	private void doLogin() {
		final String docbase = (String) cmbDocbase.getSelectedItem();
		final String username = txtUserName.getText();
		final char[] input = txtPassword.getPassword();
		final String passu = new String(input);
		IDfSessionManager sessionmanager;
		IDfSession session = null;
		if (username.length() == 0 || passu.length() == 0) {
			return;
		}
		try {
			final Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
			setCursor(cur);
			sessionmanager = createSessionManager(docbase, username, passu, null, null);
			session = sessionmanager.getSession(docbase);

			if (session != null) {
				DocuSessionManager.getInstance().setSMgr(sessionmanager);
				DocuSessionManager.getInstance().setDocbasename(docbase);
				DocuSessionManager.getInstance().setUserName(session.getLoginUserName());
				this.setVisible(false);
				ConfigService.getInstance().setLoggedInUser(session.getUser(session.getLoginUserName()));
				final String defFolder = session.getUser(session.getLoginUserName()).getDefaultFolder();
				IDfPersistentObject po = null;
				po = session.getObjectByPath(defFolder);
				ConfigService.getInstance().setHomeFolderId(po.getString("r_object_id"));
				ConfigService.getInstance().setUserName(session.getLoginUserName());
				ConfigService.getInstance().setDocbaseName(session.getDocbaseName());
				final MDIMainFrame mainframe = MDIMainFrame.getInstance();
				if (mainframe != null) {
					MDIMainFrame.getInstance().setTitle(session.getLoginUserName() + "@" + session.getDocbaseName());
					MDIMainFrame.getInstance().initAfterConnection();
				}
				sessionmanager.release(session);
			}
		} catch (final Exception ex) {
			logger.error("session create", ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Login Failed!", JOptionPane.ERROR_MESSAGE);
		} finally {
			final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);

			setCursor(cur2);
		}

	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jPanel1 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		lblHost = new javax.swing.JLabel();
		cmbDocbase = new javax.swing.JComboBox();
		cmbServer = new javax.swing.JComboBox();
		txtUserName = TextFieldFactory.createJTextField();
		txtHostName = TextFieldFactory.createJTextField();
		lblPort = new javax.swing.JLabel();
		txtPort = TextFieldFactory.createJTextField();
		txtPassword = new javax.swing.JPasswordField();
		txtPassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(final KeyEvent e) {
				// System.out.println(e);
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					doLogin();
				}
			}
		});
		txtPassword.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				cmdLoginActionPerformed(e);
			}
		});
		cmdLogin = new javax.swing.JButton();
		cmdCancel = new javax.swing.JButton();
		chkAlternativeBroker = new javax.swing.JCheckBox();
		chkAlternativeBroker.setFont(new Font("Tahoma", Font.PLAIN, 11));
		cmdGetDataButton = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setLocationByPlatform(true);

		jLabel1.setText("Docbase:");

		jLabel2.setText("Server:");

		jLabel3.setText("Username:");

		jLabel4.setText("Password:");

		lblHost.setText("Hostname:");
		lblHost.setEnabled(false);

		cmbDocbase.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "docubase" }));
		cmbDocbase.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
			@Override
			public void mouseWheelMoved(final java.awt.event.MouseWheelEvent evt) {
				cmbDocbaseMouseWheelMoved(evt);
			}
		});
		cmbDocbase.addItemListener(new java.awt.event.ItemListener() {
			@Override
			public void itemStateChanged(final java.awt.event.ItemEvent evt) {
				cmbDocbaseItemStateChanged(evt);
			}
		});
		cmbDocbase.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmbDocbaseActionPerformed(evt);
			}
		});

		txtUserName.setText("dmadmin");

		txtHostName.setEnabled(false);

		lblPort.setText("Port:");
		lblPort.setEnabled(false);

		txtPort.setText("1489");
		txtPort.setEnabled(false);

		cmdLogin.setMnemonic('l');
		cmdLogin.setText("Login");
		cmdLogin.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdLoginActionPerformed(evt);
			}
		});

		cmdCancel.setMnemonic('c');
		cmdCancel.setText("Cancel");
		cmdCancel.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdCancelActionPerformed(evt);
			}
		});

		chkAlternativeBroker.setText("Set broker host");
		chkAlternativeBroker.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		chkAlternativeBroker.setMargin(new java.awt.Insets(0, 0, 0, 0));
		chkAlternativeBroker.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				chkAlternativeBrokerActionPerformed(evt);
			}
		});

		cmdGetDataButton.setText("Get Data");
		cmdGetDataButton.setEnabled(false);
		cmdGetDataButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdGetDataButtonActionPerformed(evt);
			}
		});

		final org.jdesktop.layout.GroupLayout gl_jPanel1 = new org.jdesktop.layout.GroupLayout(jPanel1);
		jPanel1.setLayout(gl_jPanel1);
		gl_jPanel1.setHorizontalGroup(gl_jPanel1.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(gl_jPanel1.createSequentialGroup().add(gl_jPanel1.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(lblHost).add(jLabel4).add(jLabel3).add(jLabel2).add(jLabel1)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
						.add(gl_jPanel1.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(cmbServer, 0, 330, Short.MAX_VALUE).add(cmbDocbase, 0, 330, Short.MAX_VALUE).add(txtUserName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE).add(txtPassword, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
								.add(org.jdesktop.layout.GroupLayout.TRAILING, gl_jPanel1.createSequentialGroup().add(chkAlternativeBroker).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 65, Short.MAX_VALUE).add(cmdLogin).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(cmdCancel).add(8, 8, 8))
								.add(gl_jPanel1.createSequentialGroup().add(txtHostName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 162, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(lblPort).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
										.add(txtPort, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 39, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(cmdGetDataButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)))
						.addContainerGap()));
		gl_jPanel1.setVerticalGroup(gl_jPanel1.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(gl_jPanel1.createSequentialGroup().addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(gl_jPanel1.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel1).add(cmbDocbase, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(gl_jPanel1.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel2).add(cmbServer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
						.add(gl_jPanel1.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel3).add(txtUserName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
						.add(gl_jPanel1.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel4).add(txtPassword, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
						.add(gl_jPanel1.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(lblHost).add(txtHostName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(lblPort)
								.add(txtPort, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(cmdGetDataButton))
						.add(gl_jPanel1.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(gl_jPanel1.createSequentialGroup().add(18, 18, 18).add(gl_jPanel1.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(cmdLogin).add(cmdCancel))).add(gl_jPanel1.createSequentialGroup().addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(chkAlternativeBroker))).addContainerGap()));

		final GroupLayout layout = new GroupLayout(getContentPane());
		layout.setVerticalGroup(layout.createParallelGroup(Alignment.TRAILING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
		getContentPane().setLayout(layout);

		pack();
	}// </editor-fold>//GEN-END:initComponents

	void setpointer(final MDIMainFrame aThis) {
		mdimainpointer = aThis;
	}

	@Override
	public void setTitle(final String title) {
		super.setTitle(title);
	}
}
