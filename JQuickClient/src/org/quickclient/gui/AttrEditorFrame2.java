/*
 * AttrEditorFrame2.java
 *
 * Created on 7. helmikuuta 2008, 0:11
 */
package org.quickclient.gui;

import java.awt.Color;
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
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

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


/**
 * 
 * @author Administrator
 */
public class AttrEditorFrame2 extends javax.swing.JFrame {

	DocuSessionManager smanager;
	private IDfPersistentObject obj;
	private IDfId id;
	private String strID;
	Logger log = Logger.getLogger(AttrEditorFrame2.class);

	/** Creates new form AttrEditorFrame2 */
	public AttrEditorFrame2() {
		initComponents();
		jScrollPane1.setLayout(new MigLayout("wrap 2", "[grow][][grow]", "[][shrink 0]"));
		smanager = DocuSessionManager.getInstance();
		initDisplay();
	}

	public AttrEditorFrame2(String iidee) {
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
		repeatingattradder = new javax.swing.JPopupMenu();
		mnuAppendValue = new javax.swing.JMenuItem();
		mnuInsertBefore = new javax.swing.JMenuItem();
		mnuInsertAfter = new javax.swing.JMenuItem();
		mnuRemoveRepeating = new javax.swing.JMenuItem();
		jSeparator3 = new javax.swing.JSeparator();
		mnuAttrEditor1 = new javax.swing.JMenuItem();
		mnuShowDump1 = new javax.swing.JMenuItem();
		mnuViewContent1 = new javax.swing.JMenuItem();
		jSeparator4 = new javax.swing.JSeparator();
		mnuDestroy1 = new javax.swing.JMenuItem();
		AttrEditorPopUp = new javax.swing.JPopupMenu();
		mnuAttrEditor = new javax.swing.JMenuItem();
		mnuShowDump = new javax.swing.JMenuItem();
		mnuViewContent = new javax.swing.JMenuItem();
		jSeparator1 = new javax.swing.JSeparator();
		mnuDestroy = new javax.swing.JMenuItem();
		jScrollPane1 = new javax.swing.JScrollPane();
		jPanel1 = new javax.swing.JPanel();
		jButton1 = new javax.swing.JButton();
		jButton2 = new javax.swing.JButton();
		jButton3 = new javax.swing.JButton();

		jMenuItem1.setText("Item");
		TablePopUp.add(jMenuItem1);

		mnuAppendValue.setText("Append Value");
		mnuAppendValue.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuAppendValueActionPerformed(evt);
			}
		});
		repeatingattradder.add(mnuAppendValue);

		mnuInsertBefore.setText("Insert Repeating (before)");
		mnuInsertBefore.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuInsertBeforeActionPerformed(evt);
			}
		});
		repeatingattradder.add(mnuInsertBefore);

		mnuInsertAfter.setText("Insert Repeating (after)");
		mnuInsertAfter.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuInsertAfterActionPerformed(evt);
			}
		});
		repeatingattradder.add(mnuInsertAfter);

		mnuRemoveRepeating.setText("Remove");
		mnuRemoveRepeating.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuRemoveRepeatingActionPerformed(evt);
			}
		});
		repeatingattradder.add(mnuRemoveRepeating);
		repeatingattradder.add(jSeparator3);

		mnuAttrEditor1.setText("Attribute Editor");
		mnuAttrEditor1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuAttrEditor1ActionPerformed(evt);
			}
		});
		mnuAttrEditor1.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseReleased(java.awt.event.MouseEvent evt) {
				mnuAttrEditor1MouseReleased(evt);
			}
		});
		repeatingattradder.add(mnuAttrEditor1);

		mnuShowDump1.setText("Show Dump");
		mnuShowDump1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuShowDump1ActionPerformed(evt);
			}
		});
		mnuShowDump1.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseReleased(java.awt.event.MouseEvent evt) {
				mnuShowDump1MouseReleased(evt);
			}
		});
		repeatingattradder.add(mnuShowDump1);

		mnuViewContent1.setText("View Content");
		mnuViewContent1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuViewContent1ActionPerformed(evt);
			}
		});
		repeatingattradder.add(mnuViewContent1);
		repeatingattradder.add(jSeparator4);

		mnuDestroy1.setText("Delete Object");
		mnuDestroy1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuDestroy1ActionPerformed(evt);
			}
		});
		repeatingattradder.add(mnuDestroy1);

		mnuAttrEditor.setText("Attribute Editor");
		mnuAttrEditor.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuAttrEditorActionPerformed(evt);
			}
		});
		mnuAttrEditor.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseReleased(java.awt.event.MouseEvent evt) {
				mnuAttrEditorMouseReleased(evt);
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

		jButton1.setText("Save");
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});

		jButton2.setText("Close");
		jButton2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton2ActionPerformed(evt);
			}
		});

		jButton3.setText("Revert");
		jButton3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton3ActionPerformed(evt);
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
						.add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(jButton2, 0, 0, Short.MAX_VALUE).addContainerGap()).add(layout.createSequentialGroup().add(jButton3, 0, 0, Short.MAX_VALUE).addContainerGap())
								.add(layout.createSequentialGroup().add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 72, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(13, 13, 13)))));

		layout.linkSize(new java.awt.Component[] { jButton1, jButton2, jButton3 }, org.jdesktop.layout.GroupLayout.HORIZONTAL);

		layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
				layout.createSequentialGroup()
						.addContainerGap()
						.add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE)
								.add(layout.createSequentialGroup().add(jButton1).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jButton3).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jButton2))).addContainerGap()));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void mnuAppendValueActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuAppendValueActionPerformed
		JTable table = null;
		table = getJTable(evt);
		table.putClientProperty("haschanged", true);
		// table.putClientProperty("attrtype", attrType);
		DefaultTableModel tmodel = (DefaultTableModel) table.getModel();
		Vector jees = new Vector();
		int row = table.getRowCount();
		jees.add("x");
		Integer attrtype = (Integer) table.getClientProperty("attrtype");
		int iattrType = attrtype.intValue();
		switch (iattrType) {
		case DfType.DF_BOOLEAN:
			jees.add("0");
			break;
		case DfType.DF_DOUBLE:
			jees.add("1.0");
			break;
		case DfType.DF_ID:
			jees.add("0000000000000000");
			break;
		case DfType.DF_INTEGER:
			jees.add("5");
			break;
		case DfType.DF_STRING:
			jees.add("New String");
			break;
		case DfType.DF_TIME:
			jees.add("nulldate");
			break;
		case DfType.DF_UNDEFINED:
			jees.add("wtf?");
			break;
		default:
			// System.out.println("ihmevirhe");
		}

		tmodel.insertRow(row, jees);
		int rowcount = tmodel.getRowCount();
		for (int i = 0; i < rowcount; i++) {
			tmodel.setValueAt("[" + i + "]", i, 0);
		}
		table.validate();
	}// GEN-LAST:event_mnuAppendValueActionPerformed

	private void mnuInsertBeforeActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuInsertBeforeActionPerformed
		JTable table = null;
		table = getJTable(evt);
		table.putClientProperty("haschanged", true);
		DefaultTableModel tmodel = (DefaultTableModel) table.getModel();
		Vector jees = new Vector();
		int row = table.getSelectedRow();
		jees.add("x");
		Integer attrtype = (Integer) table.getClientProperty("attrtype");
		int iattrType = attrtype.intValue();
		switch (iattrType) {
		case DfType.DF_BOOLEAN:
			jees.add("0");
			break;
		case DfType.DF_DOUBLE:
			jees.add("1.0");
			break;
		case DfType.DF_ID:
			jees.add("0000000000000000");
			break;
		case DfType.DF_INTEGER:
			jees.add("5");
			break;
		case DfType.DF_STRING:
			jees.add("New String");
			break;
		case DfType.DF_TIME:
			jees.add("nulldate");
			break;
		case DfType.DF_UNDEFINED:
			jees.add("wtf?");
			break;
		default:
			// System.out.println("ihmevirhe");
		}
		tmodel.insertRow(row, jees);
		int rowcount = tmodel.getRowCount();
		for (int i = 0; i < rowcount; i++) {
			tmodel.setValueAt("[" + i + "]", i, 0);
		}
		table.validate();
	}// GEN-LAST:event_mnuInsertBeforeActionPerformed

	private void mnuInsertAfterActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuInsertAfterActionPerformed
		JTable table = null;
		table = getJTable(evt);
		// //System.out.println(table);
		table.putClientProperty("haschanged", true);
		DefaultTableModel tmodel = (DefaultTableModel) table.getModel();
		Vector jees = new Vector();
		int row = table.getSelectedRow();
		jees.add("x");
		Integer attrtype = (Integer) table.getClientProperty("attrtype");
		int iattrType = attrtype.intValue();
		switch (iattrType) {
		case DfType.DF_BOOLEAN:
			jees.add("0");
			break;
		case DfType.DF_DOUBLE:
			jees.add("1.0");
			break;
		case DfType.DF_ID:
			jees.add("0000000000000000");
			break;
		case DfType.DF_INTEGER:
			jees.add("5");
			break;
		case DfType.DF_STRING:
			jees.add("New String");
			break;
		case DfType.DF_TIME:
			jees.add("nulldate");
			break;
		case DfType.DF_UNDEFINED:
			jees.add("wtf?");
			break;
		default:
			// System.out.println("ihmevirhe");
		}
		tmodel.insertRow(row + 1, jees);
		int rowcount = tmodel.getRowCount();
		for (int i = 0; i < rowcount; i++) {
			tmodel.setValueAt("[" + i + "]", i, 0);
		}
		table.validate();
	}// GEN-LAST:event_mnuInsertAfterActionPerformed

	private void mnuRemoveRepeatingActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuRemoveRepeatingActionPerformed

		JTable table = null;
		table = getJTable(evt);
		table.putClientProperty("haschanged", true);
		// //System.out.println(table);

		DefaultTableModel tmodel = (DefaultTableModel) table.getModel();
		Vector jees = new Vector();
		int row = table.getSelectedRow();
		tmodel.removeRow(row);
		int rowcount = tmodel.getRowCount();
		for (int i = 0; i < rowcount; i++) {
			tmodel.setValueAt("[" + i + "]", i, 0);
		}
		table.validate();
	}// GEN-LAST:event_mnuRemoveRepeatingActionPerformed

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

	private void mnuViewContentActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuViewContentActionPerformed
		IDfSession session = null;
		Cursor cur = new Cursor(Cursor.WAIT_CURSOR);

		setCursor(cur);
		ExJTextField j = getJTextField(evt);
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
		ExJTextField j = getJTextField(evt);
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

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed
		boolean changes = false;
		Cursor cur = new Cursor(Cursor.WAIT_CURSOR);

		setCursor(cur);
		Component[] allcomponents = jPanel1.getComponents();
		for (int i = 0; i < allcomponents.length; i++) {
			if (allcomponents[i] instanceof ExJTextField) {
				ExJTextField jtf = (ExJTextField) allcomponents[i];
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

			if (allcomponents[i] instanceof JScrollPane) {
				JScrollPane jspane = (JScrollPane) allcomponents[i];
				Component[] jee2 = jspane.getComponents();
				for (int j = 0; j < jee2.length; j++) {
					Component temppi = jee2[j];
					if (temppi instanceof JViewport) {
						JViewport jv = (JViewport) temppi;
						Component[] temppi2 = jv.getComponents();
						for (int k = 0; k < temppi2.length; k++) {
							Component paska_anaali = temppi2[k];
							if (paska_anaali instanceof JTable) {
								JTable jt = (JTable) paska_anaali;
								String name = jt.getName();
								Boolean boo = (Boolean) jt.getClientProperty("haschanged");
								if (boo != null) {
									if (boo.equals(Boolean.TRUE)) {
										try {
											obj.truncate(name, 0);
											DefaultTableModel modeli = (DefaultTableModel) jt.getModel();
											for (int x = 0; x < modeli.getRowCount(); x++) {
												String value = (String) modeli.getValueAt(x, 1);
												obj.appendString(name, value);
											}
											changes = true;
										} catch (DfException ex) {
											log.error(ex);
										}
									}
								}
								jt.putClientProperty("haschanged", false);
							}
						}
					}
				}
			}
		}// GEN-LAST:event_jButton1ActionPerformed
		if (changes) {
			try {

				obj.save();
			} catch (DfException ex) {
				log.error(ex);
			}
		} else {
			// System.out.println("no  changes.");
		}
		Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);

		setCursor(cur2);
	}

	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton2ActionPerformed
		// TODO add your handling code here:
		this.dispose();
	}// GEN-LAST:event_jButton2ActionPerformed

	private void mnuAttrEditor1MouseReleased(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_mnuAttrEditor1MouseReleased
		// TODO add your handling code here:
	}// GEN-LAST:event_mnuAttrEditor1MouseReleased

	private void mnuShowDumpMouseReleased(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_mnuShowDumpMouseReleased
		/*
		 * int row = attrTable.getSelectedRow(); Vector v = (Vector)
		 * tablemodel.getDataVector().elementAt(row); String objid = (String)
		 * v.elementAt(1); IDfId lid = new DfId(objid); DumpFrame dumpframe =
		 * new DumpFrame(); dumpframe.setstrID(objid); dumpframe.setId(lid);
		 * dumpframe.initData(); dumpframe.setVisible(true);
		 */
	}// GEN-LAST:event_mnuShowDumpMouseReleased

	private void mnuShowDump1MouseReleased(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_mnuShowDump1MouseReleased
		// TODO add your handling code here:
	}// GEN-LAST:event_mnuShowDump1MouseReleased

	private void mnuViewContent1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuViewContent1ActionPerformed
		IDfSession session = null;
		Cursor cur = new Cursor(Cursor.WAIT_CURSOR);

		setCursor(cur);
		JTable table = getJTable(evt);

		DefaultTableModel tmodel = (DefaultTableModel) table.getModel();
		int row = table.getSelectedRow();
		String objid = (String) tmodel.getValueAt(row, 1);
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
	}// GEN-LAST:event_mnuViewContent1ActionPerformed

	private void mnuDestroy1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuDestroy1ActionPerformed
		IDfSession session = null;
		JTable table = getJTable(evt);
		Cursor cur = new Cursor(Cursor.WAIT_CURSOR);

		setCursor(cur);
		DefaultTableModel tmodel = (DefaultTableModel) table.getModel();
		int row = table.getSelectedRow();
		String objid = (String) tmodel.getValueAt(row, 1);
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
	}// GEN-LAST:event_mnuDestroy1ActionPerformed

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

	private JTable getJTable(java.awt.event.ActionEvent evt) {
		Object joo = evt.getSource();
		JMenuItem jm = (JMenuItem) joo;
		JPopupMenu menu = (JPopupMenu) jm.getParent();
		String aname = menu.getName();
		Component[] jee = jPanel1.getComponents();
		JTable table = null;
		for (int i = 0; i < jee.length; i++) {
			Component temp = jee[i];
			if (temp instanceof JScrollPane) {
				JScrollPane jspane = (JScrollPane) temp;
				Component[] jee2 = jspane.getComponents();
				for (int j = 0; j < jee2.length; j++) {
					Component temppi = jee2[j];
					if (temppi instanceof JViewport) {

						JViewport jv = (JViewport) temppi;
						Component[] temppi2 = jv.getComponents();
						for (int k = 0; k < temppi2.length; k++) {
							Component paska_anaali = temppi2[k];
							if (paska_anaali instanceof JTable) {
								JTable jt = (JTable) paska_anaali;
								if (jt.getName().equals(aname)) {
									table = jt;
								}
							}
						}
					}
				}
			}
		}
		return table;
	}

	private void mnuAttrEditor1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuAttrEditor1ActionPerformed
		JTable table = getJTable(evt);

		DefaultTableModel tmodel = (DefaultTableModel) table.getModel();
		int row = table.getSelectedRow();
		String objid = (String) tmodel.getValueAt(row, 1);
		AttrEditorFrame2 attredit = new AttrEditorFrame2(objid);
		attredit.setVisible(true);
	}// GEN-LAST:event_mnuAttrEditor1ActionPerformed

	private void mnuShowDump1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuShowDump1ActionPerformed
		JTable table = getJTable(evt);
		Cursor cur = new Cursor(Cursor.WAIT_CURSOR);

		setCursor(cur);
		DefaultTableModel tmodel = (DefaultTableModel) table.getModel();
		int row = table.getSelectedRow();
		String objid = (String) tmodel.getValueAt(row, 1);
		IDfId id3 = new DfId(objid);
		DumpFrame dumpframe = new DumpFrame();
		dumpframe.setstrID(objid);
		dumpframe.setId(id3);

		dumpframe.initData();
		SwingHelper.centerJFrame(dumpframe);
		dumpframe.setVisible(true);

		Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
		setCursor(cur2);
	}// GEN-LAST:event_mnuShowDump1ActionPerformed

	private void mnuAttrEditorActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuAttrEditorActionPerformed
		ExJTextField j = getJTextField(evt);
		String objid = j.getText();
		AttrEditorFrame2 attredit = new AttrEditorFrame2(objid);
		attredit.setVisible(true);
	}// GEN-LAST:event_mnuAttrEditorActionPerformed

	private void mnuShowDumpActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuShowDumpActionPerformed
		ExJTextField j = getJTextField(evt);
		String objid = j.getText();
		IDfId id2 = new DfId(objid);
		DumpFrame dumpframe = new DumpFrame();
		dumpframe.setstrID(objid);
		dumpframe.setId(id2);

		dumpframe.initData();
		SwingHelper.centerJFrame(dumpframe);
		dumpframe.setVisible(true);

	}// GEN-LAST:event_mnuShowDumpActionPerformed

	private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton3ActionPerformed
	// TODO add your handling code here:
		initDisplay();
	}// GEN-LAST:event_jButton3ActionPerformed

	public void initDisplay() {
		Cursor cur = new Cursor(Cursor.WAIT_CURSOR);

		setCursor(cur);
		// //System.out.println(id);
		// //System.out.println(strID);
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
			ExJTextField objidfield = new ExJTextField();
			objidfield.setName("r_object_id");
			objidfield.putClientProperty("haschanged", false);
			jPanel1.add(new ExJTextField(strID), "width 200:200:300,growy");
			IDfType objtype = obj.getType();
			String objectType = objtype.getName();
			// String objectType = obj.getString("r_object_type");
			// IDfType tyyppi = session.getType(objectType);
			IDfQuery query = new DfQuery();
			query.setDQL("select attr_name, attr_repeating, attr_type from dm_type where name = '" + objectType + "' order by 1");
			col = query.execute(session, DfQuery.DF_QUERY);
			// int attrcount = obj.getAttrCount();
			// for (int i = 0 ; i<attrcount ; i++) {
			while (col.next()) {
				final String attrName = col.getString("attr_name");

				boolean isrepeating = col.getBoolean("attr_repeating");
				int attrType = col.getInt("attr_type");
				System.out.println("attr_name: " + attrName + " attr_rep: " + isrepeating + " type: " + attrType);
				if (isrepeating) {
					int cunt = obj.getValueCount(attrName);
					boolean flag = true;
					final JTable table = new JTable();

					table.setName(attrName);
					table.putClientProperty("haschanged", false);
					table.putClientProperty("attrtype", attrType);
					table.addMouseListener(new java.awt.event.MouseAdapter() {

						@Override
						public void mouseReleased(java.awt.event.MouseEvent evt) {
							int butt = evt.getButton();
							// //System.out.println("butt on: " + butt);
							if (butt == MouseEvent.BUTTON3) {
								repeatingattradder.setName(attrName);
								repeatingattradder.show(evt.getComponent(), evt.getX(), evt.getY());
							}
						}
					});
					// table.add(repeatingattradder);
					table.setEnabled(true);
					final DefaultTableModel model = new DefaultTableModel();
					JScrollPane scrollpane = new JScrollPane();
					scrollpane.addMouseListener(new java.awt.event.MouseAdapter() {

						@Override
						public void mouseReleased(java.awt.event.MouseEvent evt) {
							// //System.out.println(evt);
							JScrollPane component = (JScrollPane) evt.getComponent();

							for (int i = 0; i < component.getComponentCount(); i++) {
								Component jeejee = component.getComponent(i);
								if (jeejee instanceof JViewport) {
									JViewport port = (JViewport) jeejee;
									for (int j = 0; j < port.getComponentCount(); j++) {
										Component a = port.getComponent(j);
										if (a instanceof JTable) {
											JTable table = (JTable) a;
											DefaultTableModel model = (DefaultTableModel) table.getModel();
											if (model.getRowCount() == 0) {
												Vector<String> joo = new Vector<String>();
												joo.add("[" + 0 + "]");
												joo.add("");
												model.addRow(joo);
												table.validate();
											}
										}
									}

								}
							}
						}
					});
					model.setColumnCount(2);
					table.setTableHeader(null);
					JLabel idLabel2 = new JLabel(attrName);
					Font f2 = idLabel2.getFont();
					idLabel2.setFont(f2.deriveFont(f2.getStyle() ^ Font.BOLD));

					jPanel1.add(idLabel2);
					for (int j = 0; j < cunt; j++) {
						String attrValue = obj.getRepeatingString(attrName, j);
						Vector aa = new Vector();
						aa.add("[" + j + "]");
						aa.add(attrValue);
						model.addRow(aa);
					}
					scrollpane.setViewportView(table);
					scrollpane.getViewport().setBackground(Color.WHITE);
					jPanel1.add(scrollpane, "h 120!,width 200:200:300,growy");

					table.setModel(model);
					table.validate();
					TableColumn column = table.getColumnModel().getColumn(0);
					column.setPreferredWidth(30);
					column.setMaxWidth(40);
					table.validate();

					table.addKeyListener(new KeyListener() {

						public void keyTyped(KeyEvent e) {
							JTable mummo = (JTable) e.getSource();
							mummo.putClientProperty("haschanged", true);
							model.fireTableDataChanged();
							table.validate();
							table.repaint();
						}

						public void keyPressed(KeyEvent e) {
						}

						public void keyReleased(KeyEvent e) {
						}
					});

				} else {
					JLabel idLabel3 = new JLabel(attrName);
					Font f3 = idLabel3.getFont();
					idLabel3.setFont(f3.deriveFont(f3.getStyle() ^ Font.BOLD));

					jPanel1.add(idLabel3);
					String attrValue = obj.getString(attrName);
					ExJTextField jtf = new ExJTextField(attrValue);
					jtf.setName(attrName);
					jtf.putClientProperty("haschanged", false);
					jtf.putClientProperty("attrtype", attrType);
					jPanel1.add(jtf, "width 200::300,growy");

					jtf.addKeyListener(new KeyListener() {

						public void keyTyped(KeyEvent e) {
							// //System.out.println(e);
							ExJTextField mummo = (ExJTextField) e.getSource();
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
							ExJTextField jtf3 = (ExJTextField) e.getSource();
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
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);

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
	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;
	private javax.swing.JButton jButton3;
	private javax.swing.JMenuItem jMenuItem1;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JSeparator jSeparator1;
	private javax.swing.JSeparator jSeparator3;
	private javax.swing.JSeparator jSeparator4;
	private javax.swing.JMenuItem mnuAppendValue;
	private javax.swing.JMenuItem mnuAttrEditor;
	private javax.swing.JMenuItem mnuAttrEditor1;
	private javax.swing.JMenuItem mnuDestroy;
	private javax.swing.JMenuItem mnuDestroy1;
	private javax.swing.JMenuItem mnuInsertAfter;
	private javax.swing.JMenuItem mnuInsertBefore;
	private javax.swing.JMenuItem mnuRemoveRepeating;
	private javax.swing.JMenuItem mnuShowDump;
	private javax.swing.JMenuItem mnuShowDump1;
	private javax.swing.JMenuItem mnuViewContent;
	private javax.swing.JMenuItem mnuViewContent1;
	private javax.swing.JPopupMenu repeatingattradder;
	// End of variables declaration//GEN-END:variables
}
