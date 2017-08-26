/*
 * SimpleImportFrame.java
 *
 * Created on 2. heinäkuuta 2007, 0:30
 */
package org.quickclient.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.io.File;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;
import org.quickclient.classes.ConfigService;
import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.FileDrop;
import org.quickclient.classes.FormatList;
import org.quickclient.classes.OperationMonitor;

import com.documentum.com.DfClientX;
import com.documentum.com.IDfClientX;
import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.operations.IDfImportNode;
import com.documentum.operations.IDfImportOperation;

/**
 *
 * @author Administrator
 */
public class SimpleImportFrame extends javax.swing.JFrame {

	DocuSessionManager smanager;
	private DefaultTableModel model;
	private FormatRenderer formatrenderer;
	private boolean isloading;
	Logger log = Logger.getLogger(SimpleImportFrame.class);

	private String folderid;

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JCheckBox chkShowProp;

	private javax.swing.JComboBox cmbFormat;

	private javax.swing.JComboBox cmbType;

	private javax.swing.JButton cmdAddFile;

	private javax.swing.JButton cmdCancel;

	private javax.swing.JButton cmdImport;

	private javax.swing.JButton cmdRemoveFile;

	private javax.swing.JTable importTable;

	private javax.swing.JLabel jLabel2;

	private javax.swing.JLabel jLabel3;

	private javax.swing.JLabel jLabel4;

	private javax.swing.JPanel jPanel1;

	private javax.swing.JScrollPane jScrollPane1;

	private javax.swing.JTextField txtObjectName;
	// End of variables declaration//GEN-END:variables

	/** Creates new form SimpleImportFrame */
	public SimpleImportFrame() {
		initComponents();
	}

	public SimpleImportFrame(final String objid) {
		isloading = true;
		initComponents();
		this.jScrollPane1.getViewport().setBackground(Color.WHITE);
		final FormatList list = FormatList.getInstance();
		importTable.setRowHeight(22);
		formatrenderer = new FormatRenderer();
		formatrenderer.setShowThumbnails(false);
		model = new DefaultTableModel();
		model.addColumn(".");
		model.addColumn("Object Name");
		model.addColumn("Format");
		model.addColumn("Path");
		importTable.setModel(model);
		final TableColumn column = importTable.getColumnModel().getColumn(0);
		column.setCellRenderer(formatrenderer);
		column.setPreferredWidth(22);
		column.setMaxWidth(22);
		final TableColumn column2 = importTable.getColumnModel().getColumn(1);
		column2.setPreferredWidth(200);
		column2.setMaxWidth(200);
		final TableColumn column3 = importTable.getColumnModel().getColumn(2);
		column3.setPreferredWidth(60);
		column3.setMaxWidth(60);
		final TableColumn column4 = importTable.getColumnModel().getColumn(3);
		column4.setPreferredWidth(400);
		column4.setMaxWidth(400);
		new FileDrop(jScrollPane1, new FileDrop.Listener() {

			@Override
			public void filesDropped(final File[] files) {
				final Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
				setCursor(cur);

				final int size = files.length;
				for (int i = 0; i < size; i++) {
					// if (files[i].isFile()) {
					if (files[i].isFile()) {
						final String filepath = files[i].getPath();
						final String filename = files[i].getName();
						// //System.out.println(filepath);
						// //System.out.println(filename);
						final String extensio = getFileExtension(filename);
						final String formaatti = list.getFormatFromExtension(extensio);
						final Vector<String> fileVector = new Vector<String>();
						fileVector.add(formaatti + ",dm_document");
						fileVector.add(getObjectNameProposal(filename));
						fileVector.add(formaatti);
						fileVector.add(filepath);
						// //System.out.println(getFileExtension(filename));
						// //System.out.println(getObjectNameProposal(filename));
						model.addRow(fileVector);

					} else if (files[i].isDirectory()) {
						final String filepath = files[i].getPath();
						final String filename = files[i].getName();
						// //System.out.println(filepath);
						// //System.out.println(filename);
						final String extensio = "";
						getFileExtension(filename);
						final String formaatti = "";
						list.getFormatFromExtension(extensio);
						final Vector<String> fileVector = new Vector<String>();
						fileVector.add(formaatti + ",dm_folder");
						fileVector.add(getObjectNameProposal(filename));
						fileVector.add("folder");
						fileVector.add(filepath);
						// //System.out.println(getFileExtension(filename));
						// //System.out.println(getObjectNameProposal(filename));
						model.addRow(fileVector);

					}
				}
				importTable.setModel(model);
				importTable.validate();
				importTable.setEditingRow(0);
				final Cursor ss = new Cursor(Cursor.DEFAULT_CURSOR);
				setCursor(ss);

			}
		});

		smanager = DocuSessionManager.getInstance();
		setFolderid(objid);
		IDfCollection col = null;
		IDfCollection col2 = null;
		IDfSession session = null;
		try {
			final Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
			setCursor(cur);
			session = smanager.getSession();
			final IDfQuery query = new DfQuery();
			final IDfQuery query2 = new DfQuery();
			query.setDQL("select distinct r_type_name from dmi_type_info where any r_supertype = 'dm_document' order by r_type_name");
			query2.setDQL("select name from dm_format order by name");

			col = query.execute(session, DfQuery.DF_READ_QUERY);
			col2 = query2.execute(session, DfQuery.DF_READ_QUERY);

			while (col.next()) {
				cmbType.addItem(col.getString("r_type_name"));
				cmbType.setSelectedItem("dm_document");
			}

			while (col2.next()) {
				cmbFormat.addItem(col2.getString("name"));
			}
		} catch (final DfException ex) {
			log.error(ex);
		} finally {
			try {
				if (col != null) {
					col.close();
				}
			} catch (final DfException ex) {
				log.error(ex);
			}
			try {
				if (col2 != null) {
					col2.close();
				}
			} catch (final DfException ex) {
				log.error(ex);
				JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);

			}
			if (session != null) {
				smanager.releaseSession(session);
			}
			final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
			setCursor(cur2);
		}
		isloading = false;
		final ConfigService cs = ConfigService.getInstance();
		String importFormat = "";
		importFormat = cs.getParameter(ConfigService.DEFAULT_IMPORT_FORMAT);
		if (importFormat == null) {
			importFormat = "txt";
		}
		if (importFormat.length() > 0 && importFormat != null) {
			cmbType.setSelectedItem(importFormat);
		}
	}

	private void cmbFormatItemStateChanged(final java.awt.event.ItemEvent evt) {// GEN-FIRST:event_cmbFormatItemStateChanged
		if (isloading == false) {
			final int row = importTable.getSelectedRow();
			final String format = (String) cmbFormat.getSelectedItem();
			model.setValueAt(format, row, 2);
			importTable.validate();
		}
	}// GEN-LAST:event_cmbFormatItemStateChanged

	private void cmbFormatMouseWheelMoved(final java.awt.event.MouseWheelEvent evt) {// GEN-FIRST:event_cmbFormatMouseWheelMoved
		if (isloading == false) {
			final int maxindex = cmbFormat.getItemCount();
			if (evt.getWheelRotation() > 0) {
				final int index = cmbFormat.getSelectedIndex();
				if (index < maxindex - 1) {
					cmbFormat.setSelectedIndex(index + 1);
				}
			} else {
				final int index = cmbFormat.getSelectedIndex();
				if (index > 0) {
					cmbFormat.setSelectedIndex(index - 1);
				}
			}
		}
	}// GEN-LAST:event_cmbFormatMouseWheelMoved

	private void cmbTypeMouseWheelMoved(final java.awt.event.MouseWheelEvent evt) {// GEN-FIRST:event_cmbTypeMouseWheelMoved
		final int maxindex = cmbType.getItemCount();
		if (evt.getWheelRotation() > 0) {
			final int index = cmbType.getSelectedIndex();
			if (index < maxindex - 1) {
				cmbType.setSelectedIndex(index + 1);
			}
		} else {
			final int index = cmbType.getSelectedIndex();
			if (index > 0) {
				cmbType.setSelectedIndex(index - 1);
			}
		}
	}// GEN-LAST:event_cmbTypeMouseWheelMoved

	private void cmdAddFileActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdAddFileActionPerformed
		log.debug(evt);
		File selFile;
		final FormatList list = FormatList.getInstance();
		final JFileChooser fc = new JFileChooser();
		fc.showOpenDialog(null);
		selFile = fc.getSelectedFile();
		final String filepath = selFile.getPath();
		final String filename = selFile.getName();
		final String extensio = getFileExtension(filename);
		final String formaatti = list.getFormatFromExtension(extensio);
		final Vector<String> fileVector = new Vector<String>();
		fileVector.add(formaatti + ",dm_document");
		fileVector.add(getObjectNameProposal(filename));
		fileVector.add(formaatti);
		fileVector.add(filepath);
		model.addRow(fileVector);
		importTable.validate();
	}// GEN-LAST:event_cmdAddFileActionPerformed

	private void cmdCancelActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdCancelActionPerformed
		log.debug(evt);
		this.dispose();
	}// GEN-LAST:event_cmdCancelActionPerformed

	private void cmdImportActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdImportActionPerformed
		log.debug(evt);
		IDfSession session = null;
		try {
			final Cursor cur = new Cursor(Cursor.WAIT_CURSOR);

			setCursor(cur);
			session = smanager.getSession();
			final IDfClientX clientx = new DfClientX();
			final IDfImportOperation importop = clientx.getImportOperation();
			importop.setDestinationFolderId(new DfId(folderid));
			importop.setKeepLocalFile(true);
			importop.setSession(session);

			for (int i = 0; i < model.getRowCount(); i++) {
				final String type = (String) cmbType.getSelectedItem();
				final Vector v = (Vector) model.getDataVector().elementAt(i);
				final String objname = (String) v.get(1);
				final String format = (String) v.get(2);
				final String filePath = (String) v.get(3);
				final IDfImportNode importnode = (IDfImportNode) importop.add(filePath);
				importnode.setDocbaseObjectType(type);
				if (!format.equals("folder")) {
					importnode.setFormat(format);
					importnode.setNewObjectName(objname);
				}
			}
			final int count = importop.getNodes().getCount();

			final OperationMonitor opmonitor = new OperationMonitor();
			importop.setOperationMonitor(opmonitor);
			importop.execute();

		} catch (final DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
		} finally {
			if (session != null) {
				smanager.releaseSession(session);
				System.out.println("release done.");
			}
			final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);

			setCursor(cur2);
		}

		this.dispose();

		// JOptionPane.showMessageDialog(null,ex.getMessage(),"Error
		// occured!",JOptionPane.ERROR_MESSAGE);

	}// GEN-LAST:event_cmdImportActionPerformed

	private void cmdRemoveFileActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdRemoveFileActionPerformed
		final int row = importTable.getSelectedRow();
		log.debug(evt);
		model.removeRow(row);
		importTable.validate();
	}// GEN-LAST:event_cmdRemoveFileActionPerformed

	private String getFileExtension(final String fileName) {
		String choppedFilename;
		String ext;
		final int dotPlace = fileName.lastIndexOf('.');

		if (dotPlace >= 0) {
			// possibly empty
			choppedFilename = fileName.substring(0, dotPlace);

			// possibly empty
			ext = fileName.substring(dotPlace + 1);
		} else {
			// was no extension
			choppedFilename = fileName;
			ext = "";
		}
		return ext;
	}

	public String getFolderid() {
		return folderid;
	}

	private String getObjectNameProposal(final String fileName) {
		String choppedFilename;
		String ext = "";
		final int dotPlace = fileName.lastIndexOf('.');

		if (dotPlace >= 0) {
			// possibly empty
			choppedFilename = fileName.substring(0, dotPlace);

			// possibly empty
			ext = fileName.substring(dotPlace + 1);
		} else {
			// was no extension
			choppedFilename = fileName;
		}
		return choppedFilename;
	}

	private void importTableMouseClicked(final java.awt.event.MouseEvent evt) {// GEN-FIRST:event_importTableMouseClicked
		log.debug(evt);
		final int row = importTable.getSelectedRow();

		final Vector v = (Vector) model.getDataVector().elementAt(row);
		final String objname = (String) v.get(1);
		final String format = (String) v.get(2);
		txtObjectName.setText(objname);
		cmbFormat.setSelectedItem(format);
	}// GEN-LAST:event_importTableMouseClicked

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jLabel2 = new javax.swing.JLabel();
		txtObjectName = new javax.swing.JTextField();
		jLabel3 = new javax.swing.JLabel();
		cmbType = new javax.swing.JComboBox();
		cmbFormat = new javax.swing.JComboBox();
		jLabel4 = new javax.swing.JLabel();
		cmdImport = new javax.swing.JButton();
		cmdCancel = new javax.swing.JButton();
		jPanel1 = new javax.swing.JPanel();
		cmdRemoveFile = new javax.swing.JButton();
		cmdAddFile = new javax.swing.JButton();
		jScrollPane1 = new javax.swing.JScrollPane();
		importTable = new javax.swing.JTable();
		chkShowProp = new javax.swing.JCheckBox();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		jLabel2.setText("Name:");

		txtObjectName.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyReleased(final java.awt.event.KeyEvent evt) {
				txtObjectNameKeyReleased(evt);
			}

			@Override
			public void keyTyped(final java.awt.event.KeyEvent evt) {
				txtObjectNameKeyTyped(evt);
			}
		});

		jLabel3.setText("Type:");

		cmbType.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
			@Override
			public void mouseWheelMoved(final java.awt.event.MouseWheelEvent evt) {
				cmbTypeMouseWheelMoved(evt);
			}
		});

		cmbFormat.addItemListener(new java.awt.event.ItemListener() {
			@Override
			public void itemStateChanged(final java.awt.event.ItemEvent evt) {
				cmbFormatItemStateChanged(evt);
			}
		});
		cmbFormat.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
			@Override
			public void mouseWheelMoved(final java.awt.event.MouseWheelEvent evt) {
				cmbFormatMouseWheelMoved(evt);
			}
		});

		jLabel4.setText("Format:");

		cmdImport.setText("Import");
		cmdImport.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdImportActionPerformed(evt);
			}
		});

		cmdCancel.setText("Cancel");
		cmdCancel.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdCancelActionPerformed(evt);
			}
		});

		jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

		cmdRemoveFile.setText("Remove");
		cmdRemoveFile.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdRemoveFileActionPerformed(evt);
			}
		});

		cmdAddFile.setText("Add File");
		cmdAddFile.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdAddFileActionPerformed(evt);
			}
		});

		importTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null } }, new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
		importTable.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(final java.awt.event.MouseEvent evt) {
				importTableMouseClicked(evt);
			}
		});
		jScrollPane1.setViewportView(importTable);

		final org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup().addContainerGap().add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 594, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
				.add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, cmdRemoveFile).add(org.jdesktop.layout.GroupLayout.TRAILING, cmdAddFile)).addContainerGap()));

		jPanel1Layout.linkSize(new java.awt.Component[] { cmdAddFile, cmdRemoveFile }, org.jdesktop.layout.GroupLayout.HORIZONTAL);

		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(jPanel1Layout.createSequentialGroup().addContainerGap().add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE).add(jPanel1Layout.createSequentialGroup().add(cmdAddFile).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(cmdRemoveFile))).addContainerGap()));

		jPanel1Layout.linkSize(new java.awt.Component[] { cmdAddFile, cmdRemoveFile }, org.jdesktop.layout.GroupLayout.VERTICAL);

		chkShowProp.setText("Show Properties Dialog");

		final org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(layout.createSequentialGroup().addContainerGap()
						.add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.add(layout.createSequentialGroup().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(10, 10, 10).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(jLabel2).add(jLabel3))).add(jLabel4)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
										.add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false).add(txtObjectName).add(cmbType, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(cmbFormat, 0, 498, Short.MAX_VALUE))
										.add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(16, 16, 16).add(cmdImport).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(cmdCancel)).add(layout.createSequentialGroup().add(18, 18, 18).add(chkShowProp)))))
						.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(layout.createSequentialGroup().addContainerGap().add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
						.add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel2).add(txtObjectName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(7, 7, 7)
						.add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel3).add(cmbType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(chkShowProp)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
						.add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel4).add(cmbFormat, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(cmdImport).add(cmdCancel)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	public void setFolderid(final String folderid) {
		this.folderid = folderid;
	}

	private void txtObjectNameKeyReleased(final java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtObjectNameKeyReleased
		log.debug(evt);
		final int row = importTable.getSelectedRow();
		final String objname = txtObjectName.getText();
		model.setValueAt(objname, row, 1);
		importTable.validate();
	}// GEN-LAST:event_txtObjectNameKeyReleased

	private void txtObjectNameKeyTyped(final java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtObjectNameKeyTyped
		log.debug(evt);
	}// GEN-LAST:event_txtObjectNameKeyTyped
}
