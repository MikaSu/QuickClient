package org.quickclient.gui;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.ExJTextField;
import org.quickclient.classes.FileUtils;
import org.quickclient.classes.SwingHelper;
import org.quickclient.classes.Utils;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfPersistentObject;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfList;
import com.documentum.fc.common.DfLogger;
import com.documentum.fc.common.IDfList;


public class LogViewerFrame extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private ExJTextField textField;
	private JCheckBox chckbxListFiles;
	private JCheckBox chckbxOpenLogIn;
	private JPanel panel_2;
	private JScrollPane scrollPane;

	/**
	 * Create the frame.
	 */
	public LogViewerFrame() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 685, 393);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 653, 68);
		contentPane.add(panel);
		panel.setLayout(null);

		chckbxOpenLogIn = new JCheckBox("Open Log in Viewer");
		chckbxOpenLogIn.setBounds(16, 7, 188, 23);
		panel.add(chckbxOpenLogIn);

		textField = new ExJTextField();
		textField.setBounds(48, 40, 442, 20);
		panel.add(textField);
		textField.setColumns(10);

		JButton btnViewFolder = new JButton("View Folder");
		btnViewFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnViewFolderActionPerformed(e);
			}
		});
		btnViewFolder.setBounds(538, 39, 103, 23);
		panel.add(btnViewFolder);

		chckbxListFiles = new JCheckBox("List Files");
		chckbxListFiles.setSelected(true);
		chckbxListFiles.setToolTipText("List also files, otherwise only directories are shown in the list");
		chckbxListFiles.setBounds(231, 7, 97, 23);
		panel.add(chckbxListFiles);

		JLabel lblPath = new JLabel("Path:");
		lblPath.setBounds(16, 43, 46, 14);
		panel.add(lblPath);

		JButton button = new JButton("..");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonActionPerformed(e);
			}
		});
		button.setToolTipText("change dir to parent");
		button.setMargin(new Insets(2, 4, 2, 4));
		button.setBounds(502, 39, 24, 23);
		panel.add(button);

		panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Files", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_2.setBounds(8, 88, 655, 206);
		contentPane.add(panel_2);
		panel_2.setLayout(null);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(8, 20, 635, 172);
		panel_2.add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(new Object[][] { { null, null, null }, }, new String[] { "Type", "Size", "Name" }) {
			Class[] columnTypes = new Class[] { String.class, String.class, String.class };

			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(40);
		table.getColumnModel().getColumn(0).setMaxWidth(60);
		table.getColumnModel().getColumn(1).setPreferredWidth(90);
		table.getColumnModel().getColumn(1).setMaxWidth(90);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_1.setBounds(0, 324, 663, 22);
		contentPane.add(panel_1);
		panel_1.setLayout(null);

		JLabel lblNewLabel = new JLabel("-");
		lblNewLabel.setBounds(10, 0, 534, 20);
		panel_1.add(lblNewLabel);

		JButton btnViewContent = new JButton("View Content");
		btnViewContent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnViewContentActionPerformed(e);
			}
		});
		btnViewContent.setBounds(406, 296, 123, 23);
		contentPane.add(btnViewContent);

		JButton btnNewButton = new JButton("Exit");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnNewButtonActionPerformed(e);
			}
		});
		btnNewButton.setBounds(540, 296, 123, 23);
		contentPane.add(btnNewButton);

		readdir(null);
	}

	protected void buttonActionPerformed(ActionEvent e) {

		String foo = textField.getText();
		if (Utils.isWindowsServer()) {
			String pieces[] = foo.split("\\");
			String finaali  ="";
			for (int i=0;i<pieces.length-1;i++) {
				finaali = finaali + pieces[i] + "\\";
			}
			textField.setText(finaali);
			readdir(finaali);

		} else {
			String pieces[] = foo.split("/");
			String finaali  ="";
			for (int i=0;i<pieces.length-1;i++) {
				finaali = finaali + pieces[i] + "/";
			}
			textField.setText(finaali);
			readdir(finaali);
		}
		
	}

	protected void btnViewContentActionPerformed(ActionEvent e) {

		int row = table.getSelectedRow();
		String type = (String) table.getModel().getValueAt(row, 0);
		String name = (String) table.getModel().getValueAt(row, 2);
		System.out.println(type + name);
		if (type.equals("dir")) {
			String foo = textField.getText();
			if (Utils.isWindowsServer()) {
				foo = foo + "\\" + name;
			} else {
				foo = foo + "/" + name;
			}
			textField.setText(foo);
			readdir(foo);
		} else if (type.equals("file")) {
			
			DocuSessionManager smanager = DocuSessionManager.getInstance();
			IDfSession session = null;
			IDfCollection col = null;
			try {
				String filename = "";
				String foo = textField.getText();
				if (Utils.isWindowsServer()) {
					filename = foo + "\\" + name;
				} else {
					filename = foo + "/" + name;
				}
				session = smanager.getSession();
				IDfSysObject newobj = (IDfSysObject) session.newObject("dm_document");
				newobj.setObjectName("ADMDOC_FOR_VIEWING");
				newobj.link("/Temp");
				newobj.setACLDomain("dm_dbo");
				newobj.setACLName("dm_acl_superusers");
				newobj.save();
				String dql = "update dm_document object setfile '" + filename + "' with content_format = 'crtext' where r_object_id = '" + newobj.getObjectId().getId() + "'";
				IDfQuery query = new DfQuery();
				query.setDQL(dql);
				col = query.execute(session, IDfQuery.DF_QUERY);
				FileUtils.viewFile(newobj.getObjectId().getId());
				
				while (col.next()) {
					DfLogger.info(this, "update adm doc content...\n" + col.dump(), null, null);
				}
			} catch (DfException ex) {
				DfLogger.error(this, "Error.", null, ex);
				SwingHelper.showErrorMessage("Error.", ex.getMessage());
			} finally {
				if (col !=null) {
					try {
						col.close();
					} catch (DfException e1) {
						e1.printStackTrace();
					}
				}
				smanager.releaseSession(session);
			}

		}
	}

	protected void btnNewButtonActionPerformed(ActionEvent e) {
		this.dispose();

	}

	protected void btnViewFolderActionPerformed(ActionEvent e) {
		readdir(textField.getText());
	}

	private void readdir(String path) {
		DocuSessionManager smanager = DocuSessionManager.getInstance();
		/*
		 * try { IDfSession session = smanager.getSession(); String applyCommand
		 * = "SERVER_DISK_DEVICES"; IDfCollection collection =
		 * session.apply(null, applyCommand, null, null, null); if (collection
		 * != null) { while (collection.next()) {
		 * System.out.println(collection.getString("path_seperator")); }
		 * collection.close(); } } catch (DfException exp) {
		 * exp.printStackTrace(); }
		 */
		boolean showFiles = chckbxListFiles.isSelected();
		IDfCollection col = null;
		IDfSession session= null;
		try {
			session = smanager.getSession();
			String pathvalue = "";
			if (path == null) {
				IDfPersistentObject locobj = session.getObjectByQualification("dm_location where object_name = 'log'");
				pathvalue = locobj.getString("file_system_path");
				textField.setText(pathvalue);
			} else {
				pathvalue = path;
			}
			String applyCommand = "SERVER_DIR";
			IDfList argname = new DfList();
			IDfList argtype = new DfList();
			IDfList argvalue = new DfList();

			argname.appendString("LIST_FILE");
			argtype.appendString("B");
			if (showFiles == true) {
				argvalue.appendString("TRUE");
			} else {
				argvalue.appendString("FALSE");
			}

			argname.appendString("DIRECTORY");
			argtype.appendString("S");
			argvalue.appendString(pathvalue);

			
			try {
				col = session.apply(null, applyCommand, argname, argtype, argvalue);
			} catch (DfException exp) {
				SwingHelper.showErrorMessage("Error.", exp.getMessage());
				DfLogger.error(this, exp.getMessage(), null, exp);
			}
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.setRowCount(0);
			if (col != null) {
				while (col.next()) {
					int attrCount = col.getValueCount("name");
					if (attrCount > 0) {
						for (int index = 0; index < attrCount; index++) {
							String fileName = col.getRepeatingString("name", index);
							String type = col.getRepeatingString("type", index);
							String size = col.getRepeatingString("size", index);
							Vector<String> a = new Vector<String>();
							a.add(type);
							a.add(size);
							a.add(fileName);
							model.addRow(a);
						}
					}
				}


			}
		} catch (DfException ex) {
			DfLogger.error(this, ex.getMessage(), null, ex);
			SwingHelper.showErrorMessage("Error", ex.getMessage());
		} finally {
			if (col != null) {
				try {
					col.close();
				} catch (DfException e) {
					DfLogger.error(this, e.getMessage(), null, e);
				}
			}
			smanager.releaseSession(session);
		}

	}
}
