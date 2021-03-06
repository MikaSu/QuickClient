/*
 * AttrEditorFrame2.java
 *
 * Created on 7. helmikuuta 2008, 0:11
 */
package org.quickclient.gui;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;
import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.ExJTextField;
import org.quickclient.classes.SwingHelper;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.DfType;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfPersistentObject;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfType;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.IDfId;

//import org.quickclient.classes.ExJTextArea;

/**
 * 
 * @author Administrator
 */
public class AttrEditorFrame3Text extends javax.swing.JFrame {

	DocuSessionManager smanager;
	private IDfPersistentObject obj;
	private IDfId id;
	private String strID;
	Logger log = Logger.getLogger(AttrEditorFrame3Text.class);

	/** Creates new form AttrEditorFrame2 */
	public AttrEditorFrame3Text() {
		initComponents();
		jScrollPane1.setLayout(new MigLayout("wrap 2", "[grow][][grow]", "[][shrink 0]"));
		smanager = DocuSessionManager.getInstance();
		initDisplay();
	}

	public AttrEditorFrame3Text(String iidee) {
		this.strID = iidee;
		initComponents();
		jPanel1.setLayout(new MigLayout("wrap 2"));
		smanager = DocuSessionManager.getInstance();
		initDisplay();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		TablePopUp = new javax.swing.JPopupMenu();
		jMenuItem1 = new javax.swing.JMenuItem();
		AttrEditorPopUp = new javax.swing.JPopupMenu();
		mnuAttrEditor = new javax.swing.JMenuItem();
		mnuShowDump = new javax.swing.JMenuItem();
		mnuViewContent = new javax.swing.JMenuItem();
		jSeparator1 = new javax.swing.JSeparator();
		mnuDestroy = new javax.swing.JMenuItem();
		jScrollPane1 = new javax.swing.JScrollPane();
		jPanel1 = new javax.swing.JPanel();
		cmdSave = new javax.swing.JButton();
		cmdClose = new javax.swing.JButton();
		cmdRevert = new javax.swing.JButton();

		jMenuItem1.setText("Item");
		jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItem1ActionPerformed(evt);
			}
		});
		TablePopUp.add(jMenuItem1);

		mnuAttrEditor.setText("Attribute Editor");
		mnuAttrEditor.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseReleased(java.awt.event.MouseEvent evt) {
				mnuAttrEditorMouseReleased(evt);
			}
		});
		mnuAttrEditor.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuAttrEditorActionPerformed(evt);
			}
		});
		AttrEditorPopUp.add(mnuAttrEditor);

		mnuShowDump.setText("Show Dump");
		mnuShowDump.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuShowDumpActionPerformed(evt);
			}
		});
		mnuShowDump.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseReleased(java.awt.event.MouseEvent evt) {
				mnuShowDumpMouseReleased(evt);
			}
		});
		AttrEditorPopUp.add(mnuShowDump);

		mnuViewContent.setText("View Content");
		mnuViewContent.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuViewContentActionPerformed(evt);
			}
		});
		AttrEditorPopUp.add(mnuViewContent);
		AttrEditorPopUp.add(jSeparator1);

		mnuDestroy.setText("Delete Object");
		mnuDestroy.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuDestroyActionPerformed(evt);
			}
		});
		AttrEditorPopUp.add(mnuDestroy);

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

		org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(0, 434, Short.MAX_VALUE));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(0, 507, Short.MAX_VALUE));

		jScrollPane1.setViewportView(jPanel1);

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

		cmdRevert.setText("Revert");
		cmdRevert.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cmdRevertActionPerformed(evt);
			}
		});

		org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
				org.jdesktop.layout.GroupLayout.TRAILING,
				layout.createSequentialGroup()
						.addContainerGap()
						.add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
						.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
						.add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(cmdClose, 0, 0, Short.MAX_VALUE).addContainerGap()).add(layout.createSequentialGroup().add(cmdRevert, 0, 0, Short.MAX_VALUE).addContainerGap())
								.add(layout.createSequentialGroup().add(cmdSave, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 72, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(13, 13, 13)))));

		layout.linkSize(new java.awt.Component[] { cmdClose, cmdRevert, cmdSave }, org.jdesktop.layout.GroupLayout.HORIZONTAL);

		layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
				layout.createSequentialGroup()
						.addContainerGap()
						.add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE)
								.add(layout.createSequentialGroup().add(cmdSave).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(cmdRevert).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(cmdClose))).addContainerGap()));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void cmdSaveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdSaveActionPerformed
		boolean changes = false;
		Cursor cur = new Cursor(Cursor.WAIT_CURSOR);

		setCursor(cur);
		Component[] allcomponents = jPanel1.getComponents();
		for (int i = 0; i < allcomponents.length; i++) {
			if (allcomponents[i] instanceof ExJTextField) {
				JTextField jtf = (JTextField) allcomponents[i];
				String name = jtf.getName();
				Boolean boo = (Boolean) jtf.getClientProperty("haschanged");
				if (boo != null) {
					if (boo.equals(Boolean.TRUE)) {
						try {
							obj.setString(jtf.getName(), jtf.getText());
							changes = true;
						} catch (DfException ex) {
							log.error(ex);
						}
					}
				}
				jtf.putClientProperty("haschanged", false);
			}

			if (allcomponents[i] instanceof JTextArea) {
				JTextArea jt = (JTextArea) allcomponents[i];
				Boolean boo = (Boolean) jt.getClientProperty("haschanged");
				if (boo != null) {
					if (boo.equals(Boolean.TRUE)) {
						try {
							String attrname = jt.getName();
							System.out.println(attrname + " has changed.");
							obj.truncate(attrname, 0);
							int valcount = jt.getLineCount();
							System.out.println(valcount);
							for (int y = 0; y < valcount; y++) {
								int start = -1, end = -1;
								try {
									start = jt.getLineStartOffset(y);
									end = jt.getLineEndOffset(y);
									String jees = jt.getText(start, end - start);
									int length = jees.length();
									jees = jees.replaceAll("\\n", "");
									obj.appendString(attrname, jees);
									System.out.println("append.");
								} catch (BadLocationException ex) {
									log.error(ex);
									// JOptionPane.showMessageDialog(null,
									// ex.getMessage(), "Error occured!",
									// JOptionPane.ERROR_MESSAGE);
								}

							}
							changes = true;
						} catch (DfException ex) {
							log.error(ex);
						}
					}
				}
				jt.putClientProperty("haschanged", false);
			}

		}// GEN-LAST:event_cmdSaveActionPerformed
		if (changes) {
			try {
				obj.save();
			} catch (DfException ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			// System.out.println("no  changes.");
		}
		Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);

		setCursor(cur2);
	}

	private void cmdCloseActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdCloseActionPerformed
		// TODO add your handling code here:
		this.dispose();
	}// GEN-LAST:event_cmdCloseActionPerformed

	private ExJTextField getJTextField(java.awt.event.ActionEvent evt) {
		Object joo = evt.getSource();
		JMenuItem jm = (JMenuItem) joo;
		JPopupMenu menu = (JPopupMenu) jm.getParent();
		String aname = menu.getName();
		Component[] allcomponents = jPanel1.getComponents();
		ExJTextField jtf = null;
		for (int i = 0; i < allcomponents.length; i++) {
			if (allcomponents[i] instanceof ExJTextField) {
				ExJTextField jtfx = (ExJTextField) allcomponents[i];
				String name = jtfx.getName();
				if (aname.equals(name)) {
					jtf = jtfx;
				}
			}
		}
		return jtf;
	}

	private JTextArea getJTextArea(java.awt.event.ActionEvent evt) {
		Object joo = evt.getSource();
		JMenuItem jm = (JMenuItem) joo;
		JPopupMenu menu = (JPopupMenu) jm.getParent();
		String aname = menu.getName();
		Component[] allcomponents = jPanel1.getComponents();
		JTextArea jtf = null;
		for (int i = 0; i < allcomponents.length; i++) {
			if (allcomponents[i] instanceof JTextArea) {
				JTextArea jtfx = (JTextArea) allcomponents[i];
				String name = jtfx.getName();
				if (aname.equals(name)) {
					jtf = jtfx;
				}
			}
		}
		return jtf;
	}

	private void cmdRevertActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdRevertActionPerformed
		// TODO add your handling code here:
		initDisplay();
	}// GEN-LAST:event_cmdRevertActionPerformed

	private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem1ActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_jMenuItem1ActionPerformed

	private void mnuAttrEditorActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuAttrEditorActionPerformed
		JTextField j = getJTextField(evt);
		if (j != null) {
			String objid = j.getText();
			AttrEditorFrame3Text attredit = new AttrEditorFrame3Text(objid);
			attredit.setVisible(true);
		} else {
			JTextArea a = getJTextArea(evt);
			if (a != null) {
				try {
					int lines = a.getLineCount();
					int position = a.getCaretPosition();
					int line = a.getLineOfOffset(position);
					int start = a.getLineStartOffset(line);
					int end = a.getLineEndOffset(line);
					String id = a.getText(start, 16);
					AttrEditorFrame3Text attredit = new AttrEditorFrame3Text(id);
					attredit.setVisible(true);
					System.out.println("@ line: " + line);
				} catch (BadLocationException ex) {
					ex.printStackTrace();
				}
			}

		}
	}// GEN-LAST:event_mnuAttrEditorActionPerformed

	private void mnuAttrEditorMouseReleased(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_mnuAttrEditorMouseReleased
		/*
		 * int row = attrTable.getSelectedRow(); Vector v = (Vector)
		 * tablemodel.getDataVector().elementAt(row); String objid = (String)
		 * v.elementAt(1); IDfId lid = new DfId(objid); AttrEditorFrame attredit
		 * = new AttrEditorFrame(); attredit.setstrID(objid);
		 * attredit.setId(lid); attredit.initDisplay(); // dumpframe.initData();
		 * attredit.setVisible(true);
		 */
	}// GEN-LAST:event_mnuAttrEditorMouseReleased

	private void mnuShowDumpActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuShowDumpActionPerformed
		JTextField j = getJTextField(evt);
		JTextArea a = getJTextArea(evt);
		String objid = null;
		if (j != null) {
			objid = j.getText();
		} else {
			int valcount = a.getRows();
			int caretpos = a.getCaretPosition();
			try {
				int linenum = a.getLineOfOffset(caretpos);

				int start = a.getLineStartOffset(linenum);
				int end = a.getLineEndOffset(linenum);
				String jees = a.getText(start, end - start);
				int length = jees.length();
				jees = jees.replaceAll("\\n", "");
				objid = jees;
			} catch (Exception e) {

			}
		}
		if (DfId.isObjectId(objid)) {
			IDfId id2 = new DfId(objid);
			DumpFrame dumpframe = new DumpFrame();
			dumpframe.setstrID(objid);
			dumpframe.setId(id2);
			dumpframe.initData();
			SwingHelper.centerJFrame(dumpframe);
			dumpframe.setVisible(true);
		}
	}// GEN-LAST:event_mnuShowDumpActionPerformed

	private void mnuShowDumpMouseReleased(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_mnuShowDumpMouseReleased
		/*
		 * int row = attrTable.getSelectedRow(); Vector v = (Vector)
		 * tablemodel.getDataVector().elementAt(row); String objid = (String)
		 * v.elementAt(1); IDfId lid = new DfId(objid); DumpFrame dumpframe =
		 * new DumpFrame(); dumpframe.setstrID(objid); dumpframe.setId(lid);
		 * dumpframe.initData(); dumpframe.setVisible(true);
		 */
	}// GEN-LAST:event_mnuShowDumpMouseReleased

	private void mnuViewContentActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuViewContentActionPerformed
		IDfSession session = null;
		Cursor cur = new Cursor(Cursor.WAIT_CURSOR);

		setCursor(cur);
		JTextField j = getJTextField(evt);
		String objid = j.getText();
		try {
			session = smanager.getSession();
			IDfId lid = new DfId(objid);
			IDfSysObject lobj = (IDfSysObject) session.getObject(lid);
			String filePath = lobj.getFile(null);
			File fileToOpen = new File(filePath);
			try {
				Desktop.getDesktop().open(fileToOpen);
			} catch (IOException ex) {
				log.error(ex);
			}
		} catch (DfException ex) {
			log.error(ex);
		} finally {
			if (session != null) {
				smanager.releaseSession(session);
			}
			Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
			setCursor(cur2);
		}
	}// GEN-LAST:event_mnuViewContentActionPerformed

	private void mnuDestroyActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuDestroyActionPerformed
		IDfSession session = null;
		JTextField j = getJTextField(evt);
		String objid = j.getText();
		Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
		setCursor(cur);
		try {
			session = smanager.getSession();
			IDfId lid = new DfId(objid);
			IDfSysObject lobj = (IDfSysObject) session.getObject(lid);
			lobj.destroy();
		} catch (DfException ex) {
			log.error(ex);
		} finally {
			if (session != null) {
				smanager.releaseSession(session);
			}

			Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
			setCursor(cur2);
		}
	}// GEN-LAST:event_mnuDestroyActionPerformed

	public void initDisplay() {
		Cursor cur = new Cursor(Cursor.WAIT_CURSOR);

		setCursor(cur);
		IDfCollection col = null;
		IDfSession session = null;
		try {
			session = smanager.getSession();
			// //System.out.println("id is: " + id);
			id = new DfId(strID);
			obj = session.getObject(id);
			obj.revert();
			JLabel idLabel = new JLabel("r_object_id");
			Font f = idLabel.getFont();
			idLabel.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));
			jPanel1.add(new JLabel("r_object_id"));
			JTextField objidfield = new ExJTextField();
			objidfield.setName("r_object_id");
			objidfield.putClientProperty("haschanged", false);
			jPanel1.add(new ExJTextField(strID), "width 200:200:300,growy");
			IDfType objtype = obj.getType();
			String objectType = objtype.getName();
			// String objectType = obj.getString("r_object_type");
			// IDfType tyyppi = session.getType(objectType);
			IDfQuery query = new DfQuery();
			String dql = "select attr_name, attr_repeating, attr_type from dm_type where name = '" + objectType + "' order by 1";
			System.out.println(dql);
			query.setDQL(dql);
			col = query.execute(session, DfQuery.DF_QUERY);
			// int attrcount = obj.getAttrCount();
			// for (int i = 0 ; i<attrcount ; i++) {
			while (col.next()) {
				final String attrName = col.getString("attr_name");
				System.out.println(attrName);
				boolean isrepeating = col.getBoolean("attr_repeating");
				int attrType = col.getInt("attr_type");

				if (isrepeating) {
					int cunt = obj.getValueCount(attrName);
					boolean flag = true;
					final JTextArea textarea = new JTextArea();

					textarea.setName(attrName);
					textarea.putClientProperty("haschanged", false);
					textarea.putClientProperty("attrtype", attrType);
					textarea.setEnabled(true);
					JLabel idLabel2 = new JLabel(attrName);
					Font f2 = idLabel2.getFont();
					idLabel2.setFont(f2.deriveFont(f2.getStyle() ^ Font.BOLD));

					jPanel1.add(idLabel2);
					jPanel1.add(textarea, "width 200:200:300,growy");
					for (int j = 0; j < cunt; j++) {
						String attrValue = obj.getRepeatingString(attrName, j);
						String ot = textarea.getText();
						if (ot.length() > 0) {
							textarea.setText(ot + "\n" + attrValue);
						} else {
							textarea.setText(attrValue);
						}
					}
					textarea.validate();
					textarea.validate();

					textarea.addKeyListener(new KeyListener() {

						public void keyTyped(KeyEvent e) {
							JTextArea mummo = (JTextArea) e.getSource();
							mummo.putClientProperty("haschanged", true);
							textarea.validate();
							textarea.repaint();
						}

						public void keyPressed(KeyEvent e) {
						}

						public void keyReleased(KeyEvent e) {
						}
					});
					MouseListener mlistener = new MouseListener() {

						public void mouseClicked(MouseEvent e) {
						}

						public void mousePressed(MouseEvent e) {
						}

						public void mouseReleased(MouseEvent e) {
							int butt = e.getButton();
							JTextArea jtf3 = (JTextArea) e.getSource();
							Integer atttype = (Integer) jtf3.getClientProperty("attrtype");
							if (atttype.intValue() == DfType.DF_ID) {
								if (butt == MouseEvent.BUTTON3) {
									AttrEditorPopUp.setName(attrName);
									AttrEditorPopUp.show(e.getComponent(), e.getX(), e.getY());
								}
							}
						}

						public void mouseEntered(MouseEvent e) {
						}

						public void mouseExited(MouseEvent e) {
						}
					};
					textarea.addMouseListener(mlistener);
				} else {
					JLabel idLabel3 = new JLabel(attrName);
					Font f3 = idLabel3.getFont();
					idLabel3.setFont(f3.deriveFont(f3.getStyle() ^ Font.BOLD));

					jPanel1.add(idLabel3);
					String attrValue = obj.getString(attrName);
					JTextField jtf = new ExJTextField(attrValue);
					jtf.setName(attrName);
					jtf.putClientProperty("haschanged", false);
					jtf.putClientProperty("attrtype", attrType);
					jPanel1.add(jtf, "width 200::300,growy");

					jtf.addKeyListener(new KeyListener() {

						public void keyTyped(KeyEvent e) {
							// //System.out.println(e);
							JTextField mummo = (JTextField) e.getSource();
							mummo.putClientProperty("haschanged", true);

						}

						public void keyPressed(KeyEvent e) {
						}

						public void keyReleased(KeyEvent e) {
						}
					});

					MouseListener mlistener = new MouseListener() {

						public void mouseClicked(MouseEvent e) {
						}

						public void mousePressed(MouseEvent e) {
						}

						public void mouseReleased(MouseEvent e) {
							int butt = e.getButton();
							JTextField jtf3 = (JTextField) e.getSource();
							Integer atttype = (Integer) jtf3.getClientProperty("attrtype");
							if (atttype.intValue() == DfType.DF_ID) {
								if (butt == MouseEvent.BUTTON3) {
									AttrEditorPopUp.setName(attrName);
									AttrEditorPopUp.show(e.getComponent(), e.getX(), e.getY());
								}
							}
						}

						public void mouseEntered(MouseEvent e) {
						}

						public void mouseExited(MouseEvent e) {
						}
					};
					jtf.addMouseListener(mlistener);

				}
			}

		} catch (DfException ex) {

			SwingHelper.showErrorMessage("Error occurred!", ex.getMessage());
			log.error(ex);
		} finally {
			if (col != null) {
				try {
					col.close();
				} catch (DfException ex) {
					log.error(ex);
				}
			}
			if (session != null) {
				smanager.releaseSession(session);
			}
			Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);

			setCursor(cur2);
		}
		this.setTitle("Attributes of object " + this.id.toString());
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JPopupMenu AttrEditorPopUp;
	private javax.swing.JPopupMenu TablePopUp;
	private javax.swing.JButton cmdClose;
	private javax.swing.JButton cmdRevert;
	private javax.swing.JButton cmdSave;
	private javax.swing.JMenuItem jMenuItem1;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JSeparator jSeparator1;
	private javax.swing.JMenuItem mnuAttrEditor;
	private javax.swing.JMenuItem mnuDestroy;
	private javax.swing.JMenuItem mnuShowDump;
	private javax.swing.JMenuItem mnuViewContent;
	// End of variables declaration//GEN-END:variables
}
