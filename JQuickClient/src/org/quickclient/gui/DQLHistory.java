package org.quickclient.gui;

import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.quickclient.classes.ExJTextArea;

import net.miginfocom.swing.MigLayout;

public class DQLHistory extends JFrame {

	private JPanel contentPane;
	Vector<String> commandHistory;
	private JList list;
	private RSyntaxTextArea txt;
	private JPopupMenu popupMenu;
	private JMenuItem mntmCopy;
	private JMenuItem mntmCopyToDql;

	/**
	 * Create the frame.
	 */
	public DQLHistory() {
		setTitle("DQLHistory");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow]", "[grow]"));
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, "cell 0 0,grow");
		
		list = new JList();
		list.setToolTipText("Doubleclick to copy selected DQL to query window");
		list.setFont(new Font("Courier New", Font.PLAIN, 12));
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				 if (e.getClickCount() == 2) {
				JList list = (JList) e.getComponent();
				String v = (String) list.getSelectedValue();
				txt.setText(v);
			}
			}
		});
		scrollPane.setViewportView(list);
		
		popupMenu = new JPopupMenu();
		addPopup(list, popupMenu);
		
		mntmCopy = new JMenuItem("Copy");
		mntmCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JList list = (JList) getList();
				String v = (String) list.getSelectedValue();
				txt.setText(v);
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				StringSelection data = new StringSelection(v);
				clipboard.setContents(data, data);
			}
		});
		popupMenu.add(mntmCopy);
		
		mntmCopyToDql = new JMenuItem("Copy to DQL Editor");
		mntmCopyToDql.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JList list = (JList) getList();
				String v = (String) list.getSelectedValue();
				txt.setText(v);
			}
		});
		popupMenu.add(mntmCopyToDql);
	}

	public void setHistory(Vector<String> commandHistory) {
		DefaultListModel listModel = new DefaultListModel();
		for (String nn: commandHistory) {
			listModel.add(0, nn);
		}
		getList().setModel(listModel);
		getList().validate();
		
	}

	public void setComponent(RSyntaxTextArea txtDQL) {
		this.txt = txtDQL;
	}



	public JList getList() {
		return list;
	}
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}

	public void setComponentStd(ExJTextArea txtDQL) {
		// TODO Auto-generated method stub
		
	}
}
