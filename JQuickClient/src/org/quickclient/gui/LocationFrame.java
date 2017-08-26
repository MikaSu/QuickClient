/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * LocationFrame.java
 *
 * Created on 21.12.2009, 23:00:45
 */
package org.quickclient.gui;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;
import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.ExJTextField;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfPersistentObject;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;

/**
 * 
 * @author Mika
 */
public class LocationFrame extends javax.swing.JFrame {

	Logger log = Logger.getLogger(LocationFrame.class);

	/** Creates new form LocationFrame */
	public LocationFrame() {
		initComponents();
		initView();
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

		jScrollPane1 = new javax.swing.JScrollPane();
		locationPane = new javax.swing.JPanel();
		cmdNewBase = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		javax.swing.GroupLayout locationPaneLayout = new javax.swing.GroupLayout(locationPane);
		locationPane.setLayout(locationPaneLayout);
		locationPaneLayout.setHorizontalGroup(locationPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 591, Short.MAX_VALUE));
		locationPaneLayout.setVerticalGroup(locationPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 335, Short.MAX_VALUE));

		jScrollPane1.setViewportView(locationPane);

		cmdNewBase.setText("New Base Dir..");
		cmdNewBase.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cmdNewBaseActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
				layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 595, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cmdNewBase).addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE).addComponent(cmdNewBase)).addContainerGap()));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void cmdNewBaseActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdNewBaseActionPerformed

		String defaults = "";
		int lkm = locationPane.getComponentCount();
		for (int i = 0; i < lkm; i++) {
			Component foofoo = locationPane.getComponent(i);
			if (foofoo instanceof ExJTextField) {
				ExJTextField fff = (ExJTextField) foofoo;
				defaults = fff.getText();
			}
		}
		String oldbase = (String) JOptionPane.showInputDialog("Old Base Directory", defaults);
		String newbase = (String) JOptionPane.showInputDialog("New Base Direcotry");
		if (oldbase != null)
			if (newbase != null)
				for (int i = 0; i < lkm; i++) {
					Component foofoo = locationPane.getComponent(i);
					if (foofoo instanceof ExJTextField) {
						ExJTextField fff = (ExJTextField) foofoo;
						String old = fff.getText();
						String newv = old.replace(oldbase, newbase);
						fff.setText(newv);
					}
				}

		JOptionPane.showMessageDialog(this, "New Base directory set\nReview changes, and save new values!", "Outstanding success!", JOptionPane.INFORMATION_MESSAGE);

	}// GEN-LAST:event_cmdNewBaseActionPerformed
		// Variables declaration - do not modify//GEN-BEGIN:variables

	private javax.swing.JButton cmdNewBase;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JPanel locationPane;

	// End of variables declaration//GEN-END:variables

	class ButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			// System.out.println("hei vaan");
			JButton hoo = (JButton) e.getSource();
			String id = hoo.getName();
			String attrName = "";
			String attrValue = "";
			JPanel paneeli = (JPanel) hoo.getParent();
			int lkm = paneeli.getComponentCount();
			for (int i = 0; i < lkm; i++) {
				Component foofoo = paneeli.getComponent(i);
				if (foofoo instanceof ExJTextField) {
					ExJTextField fff = (ExJTextField) foofoo;
					if (fff.getName().equals(id)) {
						attrValue = fff.getText();
					}
				}
			}
			IDfSession session = DocuSessionManager.getInstance().getSession();
			try {
				IDfPersistentObject obj = session.getObject(new DfId(id));
				obj.setString("file_system_path", attrValue);
				obj.save();
			} catch (DfException ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	private void initView() {
		locationPane.setLayout(new MigLayout("wrap 3"));
		JLabel mloclabel = new JLabel("Mount Points:");
		Font f = mloclabel.getFont();
		mloclabel.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));
		locationPane.add(mloclabel, "wrap");
		IDfSession session = null;

		IDfCollection col = null;
		try {
			session = DocuSessionManager.getInstance().getSession();
			IDfQuery query = new DfQuery();
			query.setDQL("select r_object_id,object_name, file_system_path from dm_mount_point order by object_name");
			col = query.execute(session, DfQuery.DF_READ_QUERY);
			while (col.next()) {
				String on = col.getString("object_name");
				String fs = col.getString("file_system_path");
				JLabel label = new JLabel(on);
				Font fx = label.getFont();
				label.setFont(fx.deriveFont(fx.getStyle() ^ Font.BOLD));
				ExJTextField jtf = new ExJTextField(fs);
				JButton button = new JButton("Save");
				label.setName(col.getString("r_object_id"));

				jtf.setName(col.getString("r_object_id"));
				button.setName(col.getString("r_object_id"));
				locationPane.add(label);
				locationPane.add(jtf, "w 350");
				locationPane.add(button);
				button.addActionListener(new ButtonListener());
			}
		} catch (DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
		} finally {
			if (col != null) {
				try {
					col.close();
				} catch (DfException ex) {
					log.error(ex);
				}
			}
			if (session != null) {
				DocuSessionManager.getInstance().releaseSession(session);
			}
		}

		JSeparator separ = new JSeparator(JSeparator.HORIZONTAL);
		locationPane.add(separ, "w 350,span 3");

		JLabel locsLabel = new JLabel("Locations:");
		Font fx22 = locsLabel.getFont();
		locsLabel.setFont(fx22.deriveFont(fx22.getStyle() ^ Font.BOLD));
		locationPane.add(locsLabel, "wrap");

		IDfCollection col2 = null;
		try {
			session = DocuSessionManager.getInstance().getSession();
			IDfQuery query = new DfQuery();
			query.setDQL("select r_object_id,object_name, file_system_path from dm_location order by object_name");
			col2 = query.execute(session, DfQuery.DF_READ_QUERY);
			while (col2.next()) {
				String on = col2.getString("object_name");
				String fs = col2.getString("file_system_path");
				JLabel label = new JLabel(on);
				Font fx = label.getFont();
				label.setFont(fx.deriveFont(fx.getStyle() ^ Font.BOLD));
				ExJTextField jtf = new ExJTextField(fs);
				JButton button = new JButton("Save");
				label.setName(col2.getString("r_object_id"));
				jtf.setName(col2.getString("r_object_id"));
				button.setName(col2.getString("r_object_id"));
				locationPane.add(label);
				locationPane.add(jtf, "w 350");
				locationPane.add(button);
				button.addActionListener(new ButtonListener());
			}
		} catch (DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
		} finally {
			if (col2 != null) {
				try {
					col2.close();
				} catch (DfException ex) {
					log.error(ex);
				}
			}
			if (session != null) {
				DocuSessionManager.getInstance().releaseSession(session);
			}
		}
	}
}