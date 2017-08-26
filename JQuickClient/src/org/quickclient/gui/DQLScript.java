package org.quickclient.gui;

import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.quickclient.classes.ConfigService;
import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.ExJTextArea;
import org.quickclient.classes.SwingHelper;
import org.quickclient.classes.Utils;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfLogger;


public class DQLScript extends JFrame {
	private static final long serialVersionUID = 6626229222514093917L;
	private JPanel contentPane;
	private JTable table;
	private ExJTextArea dqlTEXT;
	private IDfSession session;
	private javax.swing.JPopupMenu dqlScriptPopUp;
	private javax.swing.JMenuItem mnuViewResult;
	private JMenuItem mnuCopyDQL;
	private JMenuItem mnuOpenDQL;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DQLScript frame = new DQLScript();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DQLScript() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 847, 605);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);

		JButton btnParse = new JButton("Parse");
		btnParse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mnuParseDQL();
			}
		});
		toolBar.add(btnParse);

		JButton btnRun = new JButton("Run");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mnuRunactionPerformed(e);
			}
		});
		toolBar.add(btnRun);

		JButton btnContinut = new JButton("Continue");
		toolBar.add(btnContinut);

		JScrollPane scrollPane = new JScrollPane();

		JScrollPane scrollPane_1 = new JScrollPane();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(
				gl_contentPane
						.createSequentialGroup()
						.addGroup(
								gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(toolBar, GroupLayout.PREFERRED_SIZE, 829, GroupLayout.PREFERRED_SIZE)
										.addGroup(gl_contentPane.createSequentialGroup().addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 828, Short.MAX_VALUE).addGap(4)).addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 832, Short.MAX_VALUE)).addGap(0)));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(
				gl_contentPane.createSequentialGroup().addComponent(toolBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 211, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 279, GroupLayout.PREFERRED_SIZE).addGap(39)));

		table = new JTable();
		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Done", "DQL", "Result", "Data" }) {
			Class[] columnTypes = new Class[] { Boolean.class, String.class, Object.class, Object.class };

			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}

			boolean[] columnEditables = new boolean[] { true, false, true, false };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		dqlScriptPopUp = new javax.swing.JPopupMenu();
		mnuViewResult = new JMenuItem();
		mnuViewResult.setText("View Result");
		mnuViewResult.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuViewResultActionPerformed(evt);
			}
		});
		dqlScriptPopUp.add(mnuViewResult);

		mnuCopyDQL = new JMenuItem();
		mnuCopyDQL.setText("Copy DQL");
		mnuCopyDQL.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuCopyDQLActionPerformed(evt);
			}
		});
		dqlScriptPopUp.add(mnuCopyDQL);
		
		mnuOpenDQL = new JMenuItem();
		mnuOpenDQL.setText("Open DQL in Editor");
		mnuOpenDQL.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuOpenDQLActionPerformed(evt);
			}
		});
		dqlScriptPopUp.add(mnuOpenDQL);
		
		table.getColumnModel().getColumn(1).setPreferredWidth(1000);
		table.getColumnModel().getColumn(0).setWidth(20);
		table.getColumnModel().getColumn(2).setPreferredWidth(1000);
		table.getColumnModel().removeColumn(table.getColumnModel().getColumn(3));

		table.setComponentPopupMenu(dqlScriptPopUp);

		scrollPane_1.setViewportView(table);

		dqlTEXT = new ExJTextArea();
		scrollPane.setViewportView(dqlTEXT);
		contentPane.setLayout(gl_contentPane);
		
		String username = ConfigService.getInstance().getUsername();
		String docbasename = ConfigService.getInstance().getDocbasename();
		this.setTitle("DQL Script - " + username + "@" + docbasename);
	}

	protected void mnuOpenDQLActionPerformed(ActionEvent evt) {
		int rowvalues[] = table.getSelectedRows();
		int column = 1;
		int selrowcount = table.getSelectedRowCount();
		if (selrowcount != 1) {
			SwingHelper.showInfoMessage("Please select one row", "Please select one row");
			return;
		}
		String result = "";
		result = (String) table.getValueAt(rowvalues[0], column);
		DqlFrameSyntax frame = new DqlFrameSyntax();
		frame.setDql(result);
		frame.setVisible(true);
		
	}

	protected void mnuCopyDQLActionPerformed(ActionEvent evt) {
		int rowvalues[] = table.getSelectedRows();
		int column = 1;
		int selrowcount = table.getSelectedRowCount();
		if (selrowcount != 1) {
			SwingHelper.showInfoMessage("Please select one row", "Please select one row");
			return;
		}
		String result = "";
		result = (String) table.getValueAt(rowvalues[0], column);
		StringSelection data = new StringSelection(result);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(data, data);
		
	}

	protected void mnuViewResultActionPerformed(ActionEvent evt) {

		int rowvalues[] = table.getSelectedRows();
		int column = 2;
		int selrowcount = table.getSelectedRowCount();
		for (int i = 0; i < selrowcount; i++) {
			DefaultTableModelEx result = null;
			result = (DefaultTableModelEx) table.getValueAt(rowvalues[i], column);
			TableView tv = new TableView();
			tv.setVisible(true);
			tv.setModel(result);
			tv.init();

		}

	}

	protected void mnuRunactionPerformed(ActionEvent e) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		Utils u = new Utils();
		DocuSessionManager smanager = DocuSessionManager.getInstance();
		String repository = smanager.getDocbasename();
		String username = smanager.getUserName();
		for (int i = 0; i < model.getRowCount(); i++) {
			String querys = (String) model.getValueAt(i, 1);
			Boolean bvalue = (Boolean) model.getValueAt(i, 0);
			if (bvalue == Boolean.FALSE) {
				IDfCollection col = null;
				IDfQuery query = new DfQuery();
				query.setDQL(querys);
				DfLogger.info(this, repository + "/" + username + " start execute DQL: " + querys, null, null);
				try {
					long start = System.currentTimeMillis();
					col = query.execute(session, IDfQuery.QUERY);
					long end = System.currentTimeMillis();
					long executiontime = end-start;
					DefaultTableModelEx resmodel = u.getModelFromCollection(session, col);
					model.setValueAt(resmodel, i, 2);
					model.setValueAt(Boolean.TRUE, i, 0);
					DfLogger.info(this, repository + "/" + username + " Result " + resmodel.toString(), null, null);					
					DfLogger.info(this, repository + "/" + username + " End execute DQL, query took " + executiontime, null, null);					
				} catch (DfException e1) {
					DfLogger.error(this, repository + "/" + username + " Result " + e1.getMessage(), null, e1);					
					model.setValueAt(e1.getMessage(), i, 2);
					break;
				}
				try {
					Thread.sleep(70);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				} finally {
					try {
						col.close();
					} catch (DfException e1) {
						e1.printStackTrace();
					}
				}
			}
			table.setModel(model);
			table.validate();
			table.updateUI();
			model.fireTableDataChanged();
		}

		// for (DQLQuery q : querylist) {
		// System.out.println(q.dql);
		// IDfSession session = smanager.getSession();
		// IDfCollection col = null;
		// IDfQuery query = new DfQuery();
		// query.setDQL(q.dql);
		// try {
		// col = query.execute(session, IDfQuery.QUERY);
		// } catch (DfException e1) {
		// break;
		// }
		// }
	}

	protected void mnuParseDQL() {
		this.session = DocuSessionManager.getInstance().getSession();
		String a = dqlTEXT.getText();
		a = a.replace("\r\n", "\n"); // uniform line separator.
		a = a.replaceAll("\n[gG][oO]", ";");
		String split[] = a.split(";");
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0);
		for (int i = 0; i < split.length; i++) {
			String b = split[i];
			System.out.println("st: " + split[i].replace("\n", ""));
			if (b.length() > 3) {
				b = b.replace("\n", "");
				Vector v = new Vector();
				v.add(false);
				v.add(b);
				v.add("-");
				model.addRow(v);
			}
		}
		table.setModel(model);
		table.validate();
	}
}
