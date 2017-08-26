/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quickclient.gui;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;
import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.SwingHelper;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.IDfId;

/**
 *
 * @author Mika
 */
public class RenditionsPopUp extends JPopupMenu {

	private final DocuSessionManager smanager;
	private String selectedId;
	private JTable table;
	private final Logger log = Logger.getLogger(RenditionsPopUp.class);

	JMenuItem mnuViewRendition = new javax.swing.JMenuItem();

	JMenuItem mnuDeleteRendition = new javax.swing.JMenuItem();
	JMenuItem mnuDumpDMRContent = new javax.swing.JMenuItem();

	public RenditionsPopUp() {
		initMenu();
		smanager = DocuSessionManager.getInstance();
	}

	private void initMenu() {
		mnuViewRendition.setText("View");
		mnuViewRendition.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuViewRenditionActionPerformed(evt);
			}
		});
		this.add(mnuViewRendition);

		mnuDeleteRendition.setText("Delete Rendition");
		mnuDeleteRendition.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuDeleteRenditionActionPerformed(evt);
			}
		});
		this.add(mnuDeleteRendition);

		this.add(new javax.swing.JSeparator());
		mnuDumpDMRContent.setText("Dump dmr_content");
		mnuDumpDMRContent.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuDumpDMRContentActionPerformed(evt);
			}
		});
		this.add(mnuDumpDMRContent);
	}

	private void mnuDeleteRenditionActionPerformed(final java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
		IDfSession session = null;
		try {
			session = smanager.getSession();
			final int row = table.getSelectedRow();
			final String objid = selectedId;
			final IDfSysObject obj = (IDfSysObject) session.getObject(new DfId(objid));
			final int row2 = table.getSelectedRow();
			final DefaultTableModel m = (DefaultTableModel) table.getModel();
			final Vector v2 = (Vector) m.getDataVector().elementAt(row2);
			final String pagemodifier = (String) v2.get(5);
			final String format = (String) v2.get(2);
			obj.removeRenditionEx2(format, 0, pagemodifier, true);
		} catch (final DfException e) {
			log.error(e);
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);

		} finally {
			if (session != null) {
				smanager.releaseSession(session);
			}

		}
	}

	protected void mnuDumpDMRContentActionPerformed(final ActionEvent evt) {
		IDfSession session = null;
		try {
			session = smanager.getSession();
			final DefaultTableModel m = (DefaultTableModel) table.getModel();
			final int row2 = table.getSelectedRow();
			final Vector v2 = (Vector) m.getDataVector().elementAt(row2);
			final String id = (String) v2.lastElement();
			final IDfId contentid = new DfId(id);
			final DumpFrame d = new DumpFrame();
			d.setId(contentid);
			d.initData();
			d.setVisible(true);
			SwingHelper.centerJFrame(d);
		} finally {
			if (session != null) {
				smanager.releaseSession(session);
			}

		}

	}

	private void mnuViewRenditionActionPerformed(final java.awt.event.ActionEvent evt) {
		// add your handling code here:
		IDfSession session = null;
		try {
			session = smanager.getSession();
			final int row = table.getSelectedRow();
			final String objid = selectedId;
			final IDfSysObject obj = (IDfSysObject) session.getObject(new DfId(objid));
			final int row2 = table.getSelectedRow();
			final DefaultTableModel m = (DefaultTableModel) table.getModel();
			final Vector v2 = (Vector) m.getDataVector().elementAt(row2);
			final String pagemodifier = (String) v2.get(5);
			final String format = (String) v2.get(2);
			// String filePath = obj.getFile(null);
			// String filePath = obj.getFileEx(null, format, 0, false);

			final String filePath = obj.getFileEx2(null, format, 0, pagemodifier, false);
			final File fileToOpen = new File(filePath);
			try {
				Desktop.getDesktop().open(fileToOpen);
			} catch (final IOException ex) {
				log.error(ex);
				JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
			}

		} catch (final DfException e) {
			log.error(e);
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);

		} finally {
			if (session != null) {
				smanager.releaseSession(session);
			}

		}
	}

	public void setSelectedId(final String selectedId) {
		this.selectedId = selectedId;
	}

	void setTable(final JTable tblDocInfo) {
		this.table = tblDocInfo;
	}
}
