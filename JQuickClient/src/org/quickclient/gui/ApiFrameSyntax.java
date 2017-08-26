/*
 * ApiFrame.java
 *
 * Created on 29. lokakuuta 2006, 11:38
 */
package org.quickclient.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.quickclient.classes.ConfigService;
import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.ExJTextArea;
import org.quickclient.classes.FileUtils;
import org.quickclient.classes.SwingHelper;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLogger;
import com.documentum.fc.common.IDfList;

/**
 *
 * @author miksuoma
 *
 */
public class ApiFrameSyntax extends javax.swing.JFrame {
	private static final long serialVersionUID = 1L;
	DocuSessionManager smanager;
	private IDfSession session;
	private String idControl;
	private AbstractTokenMakerFactory atmf;
	private AbstractTokenMakerFactory atmfapi;
	private DQLCompletionProvider provider;
	private DefaultTableModel tablemodel;

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JPopupMenu apiPopUp;

	private javax.swing.JMenuItem attrEditor;

	private javax.swing.JCheckBox chkShowSQL;

	private javax.swing.JButton cmdExecute;

	private javax.swing.JButton cmdReset;

	private javax.swing.JMenuItem copyMenu;

	private javax.swing.JMenuItem dump;

	private javax.swing.JLabel jLabel1;

	private javax.swing.JLabel jLabel2;

	private javax.swing.JPanel jPanel1;

	private javax.swing.JScrollPane jScrollPane1;

	private javax.swing.JScrollPane jScrollPane2;

	private javax.swing.JSplitPane jSplitPane1;

	private javax.swing.JTable tblResults;

	private RSyntaxTextArea txtApiCmd;
	private javax.swing.JTextField txtData;
	private ExJTextArea txtSQL;
	private javax.swing.JMenuItem view;

	// End of variables declaration//GEN-END:variables
	public ApiFrameSyntax() {
		atmf = (AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance();
		atmf.putMapping("text/dql", "org.quickclient.gui.DQLTokenMaker");
		atmfapi = (AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance();
		atmfapi.putMapping("text/dctmapi", "org.quickclient.gui.APITokenMaker");
		InputStream is = null;
		provider = new DQLCompletionProvider();
		try {
			// provider.loadFromXML(DqlFrameSyntax.class.getResourceAsStream("dql.xml"));
			is = ApiFrameSyntax.class.getClassLoader().getResourceAsStream("dctmapi.xml");
			provider.loadFromXML(is);
			is.close();
			// provider.loadFromXML(new
			// File("c:/java/LIBS/dfc65/config/dql.xml"));
		} catch (final IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage(), "Could not load dctmapi.xml for autocompletion", JOptionPane.ERROR_MESSAGE);

		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (final IOException e) {
			}
		}
		initComponents();
		smanager = DocuSessionManager.getInstance();
		tablemodel = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(final int row, final int column) {
				return false;
			}
		};
		tblResults.setModel(tablemodel);
		final Vector vtable = new Vector();
		vtable.add("Result");
		tablemodel.setColumnIdentifiers(vtable);
		final String username = ConfigService.getInstance().getUsername();
		final String docbasename = ConfigService.getInstance().getDocbasename();
		this.setTitle("API - " + username + "@" + docbasename);
	}

	private void attrEditorActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_attrEditorActionPerformed

		final int i = tblResults.getSelectedRow();
		final Vector v = (Vector) tablemodel.getDataVector().elementAt(i);
		final String objid = (String) v.elementAt(0);
		final AttrEditorFrame3Text attredit = new AttrEditorFrame3Text(objid);
		SwingHelper.centerJFrame(attredit);
		attredit.setVisible(true);

	}// GEN-LAST:event_attrEditorActionPerformed

	private void chkShowSQLActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_chkShowSQLActionPerformed
	}// GEN-LAST:event_chkShowSQLActionPerformed

	private void cmdExecuteActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdExecuteActionPerformed

		executeAPI();
	}// GEN-LAST:event_cmdExecuteActionPerformed

	private void cmdExecuteMouseClicked(final java.awt.event.MouseEvent evt) {// GEN-FIRST:event_cmdExecuteMouseClicked
	}// GEN-LAST:event_cmdExecuteMouseClicked

	private void cmdResetActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdResetActionPerformed
		this.txtApiCmd.setText("");
		this.txtData.setText("");
	}// GEN-LAST:event_cmdResetActionPerformed

	private void copyMenuActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_copyMenuActionPerformed
		final Toolkit tk = getToolkit();
		final int i = tblResults.getSelectedRow();
		final Vector v = (Vector) tablemodel.getDataVector().elementAt(i);
		// //System.out.println(v);
		final String d = (String) v.elementAt(0);
		final StringSelection data = new StringSelection(d);
		final Clipboard clipboard = tk.getSystemClipboard();
		clipboard.setContents(data, data);

	}// GEN-LAST:event_copyMenuActionPerformed

	private void dumpActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_dumpActionPerformed

		final int i = tblResults.getSelectedRow();
		final Vector v = (Vector) tablemodel.getDataVector().elementAt(i);
		// //System.out.println(v);
		final String d = (String) v.elementAt(0);
		final DumpFrame frame = new DumpFrame();

		frame.setId(new DfId(d));

		frame.initData();
		SwingHelper.centerJFrame(frame);
		frame.setVisible(true);

	}// GEN-LAST:event_dumpActionPerformed

	@SuppressWarnings("deprecation")
	private void executeAPI() {
		String lastId = null;
		String methodStr = null;
		String methodData = null;
		String status = null;
		String cmdResult = null;
		boolean b_result = false;
		String dummy = null;
		int cmdId = 0;
		int cmdCallType = 0;
		int cmdSession = 0;
		int getCounter = 0;
		int execCounter = 0;
		int setCounter = 0;
		final String batchStr = txtApiCmd.getText();
		String currToken = null;
		final StringBuffer resultsBuf = new StringBuffer(1024);
		session = smanager.getSession();
		final String repository = smanager.getDocbasename();
		final String username = smanager.getUserName();
		try {
			lastId = idControl;
			if (lastId != null && lastId.length() > 0) {
				b_result = session.apiExec("fetch", lastId);
			}
		} catch (final DfException exp) {
		} finally {
			if (session != null) {
				smanager.releaseSession(session);
			}
		}
		boolean abortScript = false;
		if (batchStr != null && batchStr.length() != 0) {
			for (final StringTokenizer batchTokener = new StringTokenizer(batchStr, "\n\r"); batchTokener.hasMoreTokens() && !abortScript;) {
				methodStr = new String(batchTokener.nextToken());
				if (methodStr == null && batchTokener.hasMoreTokens()) {
					methodStr = new String(batchTokener.nextToken());
				}
				final StringTokenizer lineTokener = new StringTokenizer(methodStr, ",");
				final String methodStr1 = lineTokener.nextToken();
				if (methodStr1.equals("connect") || methodStr1.indexOf(" ") > -1) {
					resultsBuf.append(methodStr1);
				} else {
					dummy = lineTokener.nextToken();
					final StringBuffer restOfParams = new StringBuffer(512);
					for (; lineTokener.hasMoreTokens(); restOfParams.append(currToken)) {
						currToken = lineTokener.nextToken();
						if (restOfParams.length() > 0) {
							restOfParams.append(",");
						}
					}

					final String methodStr2 = new String(restOfParams.toString());
					resultsBuf.append(methodStr);
					try {
						final IDfSession session = smanager.getSession();
						final IDfList list = session.apiDesc(methodStr1 + ",c,");
						status = list.getString(0);
						cmdId = list.getInt(1);
						cmdCallType = list.getInt(2);
						cmdSession = list.getInt(3);
						switch (cmdCallType) {
						default:
							break;

						case 0: // '\0'
							DfLogger.info(this, repository + "/" + username + " API> " + methodStr1 + "," + methodStr2, null, null);
							cmdResult = session.apiGet(methodStr1, methodStr2);
							DfLogger.info(this, repository + "/" + username + " Result: " + cmdResult, null, null);
							if (methodStr1.equals("dump")) {
								final TextViewer v = new TextViewer(false);
								v.setText(cmdResult);
								v.setTitle("Dump from API Window");
								v.setVisible(true);
							}
							if (methodStr1.equals("create") || methodStr1.equals("checkin") || methodStr1.equals("retrieve") || methodStr1.equals("id") || methodStr1.equals("getdocbasemap") || methodStr1.equals("getservermap") || methodStr1.equals("getdocbrokermap")) {
								if (cmdResult != null) {
									if (cmdResult.length() != 16) {
										abortScript = true;
										resultsBuf.append("aborted");
										lastId = "";
										idControl = "";
									} else {
										if (setCounter + execCounter > 0) {
											if (methodStr1.equals("checkin")) {
												;
											}
										}
										lastId = cmdResult;
										idControl = cmdResult;
									}
									execCounter = 0;
									setCounter = 0;
								} else {
									final String errorMessage = session.apiGet("getmessage", null);
									resultsBuf.append(errorMessage);
								}
							}
							getCounter++;
							break;

						case 1: // '\001'
							if (0 == 1) {
								if (batchTokener.hasMoreTokens()) {
									methodData = null;
									methodData = batchTokener.nextToken();
								}
							} else {
								methodData = txtData.getText();
							}
							DfLogger.info(this, repository + "/" + username + " API> " + methodStr1 + "," + methodStr2, null, null);
							DfLogger.info(this, repository + "/" + username + " DATA> " + methodData, null, null);
							b_result = session.apiSet(methodStr1, methodStr2, methodData);
							DfLogger.info(this, repository + "/" + username + " Result: " + b_result, null, null);

							resultsBuf.append(methodData);
							if (b_result) {
								cmdResult = "OK";
								setCounter++;
								break;
							}
							abortScript = true;

							setCounter = 0;
							break;

						case 2: // '\002'
							DfLogger.info(this, repository + "/" + username + " API> " + methodStr1 + "," + methodStr2, null, null);
							b_result = session.apiExec(methodStr1, methodStr2);
							DfLogger.info(this, repository + "/" + username + " Result: " + b_result, null, null);

							if (b_result) {
								cmdResult = "OK";
								if (methodStr1.equals("save")) {
									execCounter = 0;
									setCounter = 0;
								} else {
									execCounter++;
								}
								break;
							}
							abortScript = true;
							resultsBuf.append("\n\n" + "ERROR, aborted" + "\n\n");
							execCounter = 0;
							setCounter = 0;
							break;
						}
						b_result = false;
						smanager.releaseSession(session);
					} catch (final DfException exp) {
						cmdResult = "[ERROR!]" + exp.toString();
						abortScript = true;
						DfLogger.error(this, repository + "/" + username + " Result: " + exp.toString(), null, exp);

					} finally {
						if (session != null) {
							smanager.releaseSession(session);
						}
						final Vector<Object> vv = new Vector<Object>();
						// //System.out.println("cmdResult on: " + cmdResult);
						vv.add(cmdResult);
						tablemodel.insertRow(0, vv);
						final Vector<Object> xx = new Vector<Object>();
						xx.add(batchStr);
						tablemodel.insertRow(0, xx);
						tblResults.validate();
						if (chkShowSQL.isSelected()) {
							final String sql = getSqlQuery();
							txtSQL.setText(sql);
							DfLogger.info(this, repository + "/" + username + " SQL> " + sql, null, null);
						} else {
							txtSQL.setText("");
						}

					}
				}
			}

		}
	}

	private String getSqlQuery() {
		String sqlResult = null;
		try {
			final IDfSession session = smanager.getSession();
			final String collId = session.apiGet("apply", "NULL,GET_LAST_SQL");
			if (!collId.equals("")) {
				boolean retVal = session.apiExec("next", collId);
				sqlResult = session.apiGet("get", collId + ",result");
				retVal = session.apiExec("close", collId);
			}
			smanager.releaseSession(session);
		} catch (final DfException exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				smanager.releaseSession(session);
			}
		}
		return sqlResult;
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		apiPopUp = new javax.swing.JPopupMenu();
		copyMenu = new javax.swing.JMenuItem();
		dump = new javax.swing.JMenuItem();
		attrEditor = new javax.swing.JMenuItem();
		view = new javax.swing.JMenuItem();
		jSplitPane1 = new javax.swing.JSplitPane();
		jScrollPane1 = new javax.swing.JScrollPane();
		tblResults = new javax.swing.JTable();
		jScrollPane2 = new javax.swing.JScrollPane();
		txtSQL = new ExJTextArea();
		jPanel1 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		txtApiCmd = new RSyntaxTextArea();
		txtApiCmd.setBorder(UIManager.getBorder("FormattedTextField.border"));
		txtApiCmd.setCurrentLineHighlightColor(Color.WHITE);
		final AutoCompletion ac = new AutoCompletion(provider);
		ac.setParameterAssistanceEnabled(true);
		ac.setAutoActivationDelay(100);
		ac.setAutoActivationEnabled(true);
		ac.setShowDescWindow(true);
		ac.setAutoCompleteSingleChoices(false);
		ac.install(txtApiCmd);

		txtApiCmd.setSyntaxEditingStyle("text/dctmapi");
		txtApiCmd.setHighlightCurrentLine(false);

		txtData = new javax.swing.JTextField();
		cmdExecute = new javax.swing.JButton();
		chkShowSQL = new javax.swing.JCheckBox();
		cmdReset = new javax.swing.JButton();

		copyMenu.setText("Copy");
		copyMenu.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				copyMenuActionPerformed(evt);
			}
		});
		apiPopUp.add(copyMenu);

		dump.setText("Dump");
		dump.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				dumpActionPerformed(evt);
			}
		});
		apiPopUp.add(dump);

		attrEditor.setText("Open in Attribute Editor");
		attrEditor.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				attrEditorActionPerformed(evt);
			}
		});
		apiPopUp.add(attrEditor);

		view.setText("View Contents");
		view.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				viewActionPerformed(evt);
			}
		});
		apiPopUp.add(view);

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		jSplitPane1.setDividerLocation(350);
		jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

		tblResults.setFont(new java.awt.Font("Courier New", 0, 11));
		tblResults.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

		}, new String[] { "Title 1" }));
		tblResults.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(final java.awt.event.MouseEvent evt) {
				tblResultsMouseClicked(evt);
			}

			@Override
			public void mouseReleased(final java.awt.event.MouseEvent evt) {
				tblResultsMouseReleased(evt);
			}
		});
		jScrollPane1.setViewportView(tblResults);

		jSplitPane1.setTopComponent(jScrollPane1);

		txtSQL.setColumns(20);
		txtSQL.setFont(new java.awt.Font("Courier New", 0, 13));
		txtSQL.setLineWrap(true);
		txtSQL.setRows(5);
		jScrollPane2.setViewportView(txtSQL);

		jSplitPane1.setRightComponent(jScrollPane2);

		jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

		jLabel1.setText("Command:");

		jLabel2.setText("Data:");

		txtApiCmd.setFont(new java.awt.Font("Courier New", 0, 11)); // NOI18N
		txtApiCmd.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyPressed(final java.awt.event.KeyEvent evt) {
				txtApiCmdKeyPressed(evt);
			}
		});

		txtData.setFont(new java.awt.Font("Courier New", 0, 11)); // NOI18N

		cmdExecute.setMnemonic('e');
		cmdExecute.setText("Execute");
		cmdExecute.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(final java.awt.event.MouseEvent evt) {
				cmdExecuteMouseClicked(evt);
			}
		});
		cmdExecute.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdExecuteActionPerformed(evt);
			}
		});

		chkShowSQL.setMnemonic('S');
		chkShowSQL.setText("Show SQL");
		chkShowSQL.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		chkShowSQL.setMargin(new java.awt.Insets(0, 0, 0, 0));
		chkShowSQL.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				chkShowSQLActionPerformed(evt);
			}
		});

		cmdReset.setText("Clear");
		cmdReset.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdResetActionPerformed(evt);
			}
		});

		final GroupLayout gl_jPanel1 = new GroupLayout(jPanel1);
		gl_jPanel1.setHorizontalGroup(gl_jPanel1.createParallelGroup(Alignment.LEADING).addGroup(gl_jPanel1.createSequentialGroup().addContainerGap().addGroup(gl_jPanel1.createParallelGroup(Alignment.TRAILING).addComponent(jLabel1).addComponent(jLabel2)).addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_jPanel1.createParallelGroup(Alignment.LEADING).addComponent(txtApiCmd, GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE).addComponent(txtData, GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE)).addPreferredGap(ComponentPlacement.RELATED).addGroup(gl_jPanel1.createParallelGroup(Alignment.LEADING).addComponent(chkShowSQL).addComponent(cmdExecute).addComponent(cmdReset)).addGap(16)));
		gl_jPanel1.setVerticalGroup(gl_jPanel1.createParallelGroup(Alignment.LEADING).addGroup(gl_jPanel1.createSequentialGroup().addContainerGap().addGroup(gl_jPanel1.createParallelGroup(Alignment.BASELINE).addComponent(jLabel1).addComponent(txtApiCmd, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(cmdExecute)).addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_jPanel1.createParallelGroup(Alignment.BASELINE).addComponent(txtData, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(jLabel2).addComponent(cmdReset)).addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(chkShowSQL)));
		gl_jPanel1.linkSize(SwingConstants.HORIZONTAL, new Component[] { cmdExecute, cmdReset });
		jPanel1.setLayout(gl_jPanel1);

		final org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().addContainerGap()
				.add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.LEADING, jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 587, Short.MAX_VALUE)).addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(layout.createSequentialGroup().addContainerGap().add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE).addContainerGap()));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void tblResultsMouseClicked(final java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tblResultsMouseClicked
		if (evt.getClickCount() == 2) {
			final String value = (String) tblResults.getModel().getValueAt(tblResults.getSelectedRow(), 0);
			txtApiCmd.setText(txtApiCmd.getText() + value);
		}
	}// GEN-LAST:event_tblResultsMouseClicked

	private void tblResultsMouseReleased(final java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tblResultsMouseReleased
		final int butt = evt.getButton();
		if (butt == MouseEvent.BUTTON3) {
			final int i = tblResults.getSelectedRow();
			final Vector v = (Vector) tablemodel.getDataVector().elementAt(i);

			final String d = (String) v.elementAt(0);
			dump.setVisible(true);
			attrEditor.setVisible(true);
			apiPopUp.show(evt.getComponent(), evt.getX(), evt.getY());
		}
	}// GEN-LAST:event_tblResultsMouseReleased

	private void txtApiCmdKeyPressed(final java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtApiCmdKeyPressed

		if (evt.getKeyCode() == 10) {
			executeAPI();
		}

	}// GEN-LAST:event_txtApiCmdKeyPressed

	private void viewActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_viewActionPerformed

		final int i = tblResults.getSelectedRow();
		final Vector v = (Vector) tablemodel.getDataVector().elementAt(i);
		// //System.out.println(v);
		final String objid = (String) v.elementAt(0);
		FileUtils.viewFile(objid);
	}// GEN-LAST:event_viewActionPerformed
}
