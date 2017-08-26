package org.quickclient.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.ExJTextField;
import org.quickclient.classes.QueryUtils;
import org.quickclient.classes.SwingHelper;

import net.miginfocom.swing.MigLayout;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.search.IDfQueryBuilder;
import com.documentum.fc.common.DfLogger;


public class SmartListSaveFrame extends JFrame {

	private JPanel contentPane;
	private JButton closeButton;
	private IDfSession session;
	private String idControl;
	private DocuSessionManager smanager;
	private ExJTextField objNameField;
	private String dql = null;
	private ExJTextField descField;
	private IDfQueryBuilder qb = null;

	public SmartListSaveFrame() {
		smanager = DocuSessionManager.getInstance();
		initComponents();
	}

	private void initComponents() {

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		this.setSize(400, 300);
		contentPane = new JPanel();
		MigLayout ml = new MigLayout("fill", "[grow]");
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(ml);
		setContentPane(contentPane);

		JPanel scriptPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		scriptPanel.setBorder(new TitledBorder("SmartList"));
		scriptPanel.setLayout(new MigLayout("fill", "[grow][]"));
		buttonPanel.setLayout(new MigLayout("rtl", "[]"));
		JPanel pane = new JPanel();
		pane.setLayout(new MigLayout("fill", "[] []"));
		pane.setPreferredSize(new Dimension(2000, 2000));
		scriptPanel.add(pane);

		pane.add(new JLabel("Object Name: "));
		objNameField = new ExJTextField();
		objNameField.setColumns(21);
		pane.add(objNameField);

		pane.add(new JLabel("Description: "));
		descField = new ExJTextField();
		descField.setColumns(21);
		pane.add(descField);

		buttonPanel.setPreferredSize(new Dimension(2000, 40));
		contentPane.add(scriptPanel, "wrap");
		contentPane.add(buttonPanel);

		closeButton = new JButton("Save");
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cmdSaveActionPerformed(arg0);
			}
		});
		buttonPanel.add(closeButton, "right");

		closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cmdCloseActionPerformed(arg0);
			}
		});
		buttonPanel.add(closeButton, "right");
	}

	protected void cmdCloseActionPerformed(ActionEvent arg0) {
		this.dispose();

	}

	protected void cmdSaveActionPerformed(ActionEvent arg0) {

		QueryUtils u = new QueryUtils();
		DocuSessionManager smanager = DocuSessionManager.getInstance();
		IDfSession session = null;

		try {
			session = smanager.getSession();
			if (dql != null)
				u.createSmartList(session, dql, objNameField.getText(), descField.getText(), smanager.getDocbasename(), false);
			if (qb != null)
				u.createSmartList(session, qb, objNameField.getText(), descField.getText(), smanager.getDocbasename(), false);
		} catch (Exception e) {
			e.printStackTrace();
			SwingHelper.showErrorMessage("Error during save", e.getMessage());
			DfLogger.error(this, "Error during save", null, e);
		} finally {
			smanager.releaseSession(session);
		}
	}

	public void setDql(String dql) {
		this.dql = dql;

	}

	public void setQueryBuilder(IDfQueryBuilder qb) {
		this.qb = qb;

	}

}
