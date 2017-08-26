package org.quickclient.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;

import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.ExJTextArea;

import net.miginfocom.swing.MigLayout;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfLogger;
import com.documentum.fc.common.IDfList;


public class APIScriptFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private ExJTextArea textScript;
	private JButton btnRun;
	private JButton closeButton;
	private ExJTextArea textResult;
	private IDfSession session;
	private String idControl;
	private DocuSessionManager smanager;

	public APIScriptFrame() {
		smanager = DocuSessionManager.getInstance();
		initComponents();
	}

	private void initComponents() {

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		this.setSize(400, 700);
		contentPane = new JPanel();
		MigLayout ml = new MigLayout("fill", "[grow]");
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(ml);
		setContentPane(contentPane);

		JPanel scriptPanel = new JPanel();
		JPanel resultPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		scriptPanel.setBorder(new TitledBorder("API Script"));
		resultPanel.setBorder(new TitledBorder("Results"));
		scriptPanel.setLayout(new MigLayout("fill", "[grow][]"));
		resultPanel.setLayout(new MigLayout("", "[]"));
		buttonPanel.setLayout(new MigLayout("rtl", "[]"));

		textScript = new ExJTextArea();
		textResult = new ExJTextArea();
		JScrollPane pane = new JScrollPane();
		pane.setPreferredSize(new Dimension(2000, 2000));
		pane.setViewportView(textScript);
		// scriptPanel.add(textScript, "grow");
		scriptPanel.add(pane);

		JScrollPane paneresult = new JScrollPane();
		paneresult.setPreferredSize(new Dimension(2000, 2000));
		paneresult.setViewportView(textResult);
		// scriptPanel.add(textScript, "grow");
		resultPanel.add(paneresult);

		buttonPanel.setPreferredSize(new Dimension(2000, 40));
		// resultPanel.add(textResult, "grow");

		btnRun = new JButton("Run");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnRunActionPerformed(e);
			}
		});
		scriptPanel.add(btnRun);
		contentPane.add(scriptPanel, "wrap");
		contentPane.add(resultPanel, "wrap");
		contentPane.add(buttonPanel);
		closeButton = new JButton("Close");
		buttonPanel.add(closeButton, "right");
	}

	@SuppressWarnings("deprecation")
	private void executeCommands() {
		String batchStr = null;
		boolean abortScript = false;
		batchStr = textScript.getText();
		String lastId = null;
		String methodStr = null;
		String methodData = null;
		String status = null;
		String cmdResult = null;
		boolean b_result = false;
		String dummy = null;
		String dummyC = ",c,";
		String dummyCurrent = ",current,";

		int cmdId = 0;
		int cmdCallType = 0;
		int cmdSession = 0;

		int getCounter = 0;
		int execCounter = 0;
		int setCounter = 0;

		//String currToken = null;
		session = smanager.getSession();
		String repository = smanager.getDocbasename();
		String username = smanager.getUserName();
		StringBuffer resultsBuf = new StringBuffer(1024);
		try {
			lastId = idControl;
			if ((lastId != null) && (lastId.length() > 0)) {
				b_result = session.apiExec("fetch", lastId);
			}
		} catch (DfException exp) {
		}
		if ((batchStr != null) && (batchStr.length() != 0)) {
			StringTokenizer batchTokener = new StringTokenizer(batchStr, "\n\r");
			while ((batchTokener.hasMoreTokens()) && (abortScript != true)) {
				methodStr = batchTokener.nextToken();
				System.out.println("methodStr:" + methodStr);
				if (methodStr.startsWith("#"))
					continue;
				StringTokenizer lineTokener = new StringTokenizer(methodStr, ",");

				String methodStr1 = lineTokener.nextToken();
				System.out.println("methodStr1: " + methodStr1);
				if ((methodStr1.equals("connect")) || (methodStr1.indexOf(" ") > -1)) {
					resultsBuf.append("API> " + methodStr1);
					if (methodStr1.equals("connect")) {
						resultsBuf.append("\n...\n" + "connect not supported" + "\n");
					} else {
						resultsBuf.append("\n...\n" + "invalid API" + "\n");
					}
				} else {
					String methodStr2 = null;
					if (lineTokener.hasMoreTokens()) {
						dummy = lineTokener.nextToken();
						if (lineTokener.hasMoreTokens()) {
							if ((dummy != null) && (dummy.length() > 0)) {
								int index = 0;
								if (dummy.equalsIgnoreCase("current")) {
									index = methodStr.toLowerCase().indexOf(dummyCurrent);
								} else if (dummy.equalsIgnoreCase("c")) {
									index = methodStr.toLowerCase().indexOf(dummyC);
								}
								methodStr2 = methodStr.substring(index + dummy.length() + 2);
								System.out.println("methodStr2: " + methodStr2);
							}
						}
					}
					resultsBuf.append("API> " + methodStr + "\n");
					try {
						IDfList list = session.apiDesc(methodStr1 + ",c,");
						status = list.getString(0);
						cmdId = list.getInt(1);
						cmdCallType = list.getInt(2);
						cmdSession = list.getInt(3);
						switch (cmdCallType) {
						case 0:
							if ((methodStr1.equals("getservermap")) || (methodStr1.equals("getdocbasemap"))) {
								// StringTokenizer tokenizer = null;
								// if ((methodStr2 != null) &&
								// (methodStr2.length() > 0)) {
								// tokenizer = new StringTokenizer(methodStr2,
								// ",");
								// }
								// if ((tokenizer == null) ||
								// (tokenizer.countTokens() <= 1))
								// {
								// String strDocbroker =
								// AdminUtils.getCurrentDocbrokerName(getDfSession());
								// String docbrokerPort =
								// AdminUtils.getCurrentDocbrokerPort(getDfSession())
								// + "";
								// StringBuffer additionalParamBuffer = new
								// StringBuffer();
								// if ((methodStr2 != null) &&
								// (methodStr2.length() > 0)) {
								// additionalParamBuffer.append(",");
								// }
								// additionalParamBuffer.append(",");
								// additionalParamBuffer.append(strDocbroker);
								// additionalParamBuffer.append(",");
								// additionalParamBuffer.append(docbrokerPort);
								// if ((methodStr2 != null) &&
								// (methodStr2.length() > 0)) {
								// methodStr2 = methodStr2 +
								// additionalParamBuffer.toString();
								// } else {
								// methodStr2 =
								// additionalParamBuffer.toString();
								// }
								// }
							}
							DfLogger.info(this, repository + "/" + username + " API> " + methodStr1 + "," + methodStr2, null, null);
							cmdResult = session.apiGet(methodStr1, methodStr2);
							DfLogger.info(this, repository + "/" + username + " Result: " + cmdResult, null, null);
							resultsBuf.append(cmdResult + "\n");
							System.out.println("apiget result: " + cmdResult);
							if ((methodStr1.equals("create")) || (methodStr1.equals("checkin")) || (methodStr1.equals("retrieve")) || (methodStr1.equals("id")) || (methodStr1.equals("getdocbasemap")) || (methodStr1.equals("getservermap")) || (methodStr1.equals("getdocbrokermap"))) {
								if (cmdResult != null) {
									if (cmdResult.length() != 16) {
										abortScript = true;
										resultsBuf.append("\n\n" + "ERROR, Script aborted" + "\n\n");
										lastId = "";
										idControl = "";
									} else {
										if ((setCounter + execCounter > 0) && (!methodStr1.equals("checkin"))) {
										}
										lastId = cmdResult;
										idControl = cmdResult;
									}
									execCounter = 0;
									setCounter = 0;
								} else {
									String errorMessage = session.apiGet("getmessage", null);
									resultsBuf.append(errorMessage);
								}
							}
							getCounter++;
							break;
						case 1:

							if (batchTokener.hasMoreTokens()) {
								methodData = null;
								methodData = batchTokener.nextToken();
								System.out.println("methodData: " + methodData);
							}
							DfLogger.info(this, repository + "/" + username + " API> " + methodStr1 + "," + methodStr2, null, null);
							DfLogger.info(this, repository + "/" + username + " DATA> " + methodData, null, null);
							b_result = session.apiSet(methodStr1, methodStr2, methodData);
							DfLogger.info(this, repository + "/" + username + " Result: " + b_result, null, null);

							resultsBuf.append("\n" + "SET> " + methodData);
							if (b_result) {
								cmdResult = "OK";
								setCounter++;
							} else {
								abortScript = true;
								resultsBuf.append("\n\n" + "ERROR, Script aborted" + "\n\n");

								setCounter = 0;
							}
							break;
						case 2:
							DfLogger.info(this, repository + "/" + username + " API> " + methodStr1 + "," + methodStr2, null, null);
							b_result = session.apiExec(methodStr1, methodStr2);
							DfLogger.info(this, repository + "/" + username + " Result: " + b_result, null, null);

							if (b_result) {
								if (methodStr1.equals("fetch")) {
									lastId = methodStr2;
									idControl = methodStr2;
								}
								cmdResult = "OK";
								if (methodStr1.equals("save")) {
									execCounter = 0;
									setCounter = 0;
								} else {
									execCounter++;
								}
							} else {
								abortScript = true;
								resultsBuf.append("\n\n" + "ERROR, Script aborted" + "\n\n");
								execCounter = 0;
								setCounter = 0;
							}
							break;
						}
						b_result = false;
					} catch (Exception exp) {
						cmdResult = "Error with command: " + exp.toString();
						abortScript = true;
						cmdResult = cmdResult + "\n\n" + "ERROR, Script aborted" + "\n\n";
						DfLogger.error(this, repository + "/" + username + " Result: " + exp.toString(), null, exp);
					} finally {
						resultsBuf.append("\n...\n" + cmdResult + "\n");
					}
				}
				if (setCounter + execCounter > 0) {
				}
				// TextArea output = (TextArea)getControl("txtRESULTS");
				String lastResults = "";// output.getValue();
				resultsBuf.append(lastResults);
				// output.setValue(resultsBuf.toString());
				textResult.setText(resultsBuf.toString());
			}
		}
		resultsBuf.append("\n...\n" + cmdResult + "\n");
		if (session != null) {
			if (session.isConnected())
				smanager.releaseSession(session);
		}
	}

	protected void btnRunActionPerformed(ActionEvent e) {

		int valcount = textScript.getLineCount();
		System.out.println(valcount);
		for (int y = 0; y < valcount; y++) {
			int start = -1;
			int end = -1;
			try {
				start = textScript.getLineStartOffset(y);
				end = textScript.getLineEndOffset(y);
				String apicmd = textScript.getText(start, end - start);
				int length = apicmd.length();
				apicmd = apicmd.replaceAll("\\n", "");
				System.out.println(apicmd);
			} catch (BadLocationException ex) {

			}

		}
		executeCommands();
	}
}
