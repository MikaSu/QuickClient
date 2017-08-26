package org.quickclient.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.ExJTextArea;
import org.quickclient.classes.ExJTextField;

import net.miginfocom.swing.MigLayout;

import com.documentum.fc.client.IDfQueueItem;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLogger;


public class InboxNotification extends JFrame {

	private JPanel contentPane;
	private ExJTextField txtEvent;
	private ExJTextField txtItemName;
	private ExJTextField txtItemType;
	private ExJTextArea txtMessage;
	private String itemid;
	private ExJTextField txtSender;

	/**
	 * Create the frame.
	 */
	public InboxNotification() {
		setTitle("Notification view");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[420px]", "[225px][23px]"));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, "cell 0 0,grow");
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Notification", null, panel, null);
		panel.setLayout(new MigLayout("", "[][grow]", "[][][][][grow]"));
		
		JLabel lblNewLabel = new JLabel("Event:");
		panel.add(lblNewLabel, "cell 0 0,alignx trailing");
		
		txtEvent = new ExJTextField();
		txtEvent.setEditable(false);
		panel.add(txtEvent, "cell 1 0,growx");
		txtEvent.setColumns(10);
		
		JLabel lblItemName = new JLabel("Item Name:");
		panel.add(lblItemName, "cell 0 1,alignx trailing");
		
		txtItemName = new ExJTextField();
		txtItemName.setEditable(false);
		panel.add(txtItemName, "cell 1 1,growx,aligny top");
		txtItemName.setColumns(10);
		
		JLabel lblItemType = new JLabel("Item Type:");
		panel.add(lblItemType, "cell 0 2,alignx trailing");
		
		txtItemType = new ExJTextField();
		txtItemType.setEditable(false);
		panel.add(txtItemType, "cell 1 2,growx");
		txtItemType.setColumns(10);
		
		JLabel lblSender = new JLabel("Sender:");
		panel.add(lblSender, "cell 0 3,alignx trailing");
		
		txtSender = new ExJTextField();
		txtSender.setEditable(false);
		panel.add(txtSender, "cell 1 3,growx,aligny top");
		txtSender.setColumns(10);
		
		JLabel lblMessage = new JLabel("Message:");
		panel.add(lblMessage, "cell 0 4,alignx right,aligny top");
		
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, "cell 1 4,grow");
		
		txtMessage = new ExJTextArea();
		txtMessage.setLineWrap(true);
		txtMessage.setEditable(false);
		scrollPane.setViewportView(txtMessage);
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				closeclick();
			}
		});
		contentPane.add(btnClose, "cell 0 1,alignx right,aligny top");
	}

	protected void closeclick() {
		this.dispose();
		
	}

	public void setId(String itemid2) {
		// TODO Auto-generated method stub
	this.itemid=itemid2;
	}

	public void init() {
		IDfSession session = null;
		DocuSessionManager smanager = DocuSessionManager.getInstance();
		try {
			session = smanager.getSession();
			IDfQueueItem qitem = (IDfQueueItem) session.getObject(new DfId(itemid));
			this.txtEvent.setText(qitem.getEvent());
			this.txtItemName.setText(qitem.getItemName());
			this.txtItemType.setText(qitem.getItemType());
			this.txtSender.setText(qitem.getSentBy());
			this.txtMessage.setText(qitem.getMessage());

		} catch (DfException ex) {
			DfLogger.error(this, ex.getMessage(), null,ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
		} finally {
			if (session != null)
				smanager.releaseSession(session);
		}
		
	}	

}
