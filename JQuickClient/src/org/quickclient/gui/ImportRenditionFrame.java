package org.quickclient.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.quickclient.classes.DocInfo;
import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.FormatComboItem;
import org.quickclient.classes.IntVerifier;
import org.quickclient.classes.PathVerifier;
import org.quickclient.classes.SwingHelper;

import net.miginfocom.swing.MigLayout;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfFormat;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLogger;


public class ImportRenditionFrame extends JFrame {

	private JPanel contentPane;
	private JTextField path;
	private IDfSession session;
	private JTable renditiontable;
	private JComboBox formatcombo;
	private DefaultTableModel model;
	private JScrollPane scrollPane;
	private String objId;


	private IDfSysObject obj;
	private String format;
	private URL imageURL;
	private ImageIcon icon;
	private String objname;
	private JTextField pagemodifier;
	private JTextField page;
	private JButton cmdaddRendition;
	private JCheckBox chkAnyFormat;


	private void init() throws DfException {
		this.session = DocuSessionManager.getInstance().getSession();
		this.obj = (IDfSysObject) session.getObject(new DfId(objId));
		this.format = obj.getString("a_content_type");
		this.objname = obj.getObjectName();
	}

	/**
	 * Create the frame.
	 */
	public ImportRenditionFrame(String id) {
		this.objId = id;
		try {
			init();
		} catch (DfException e) {
			e.printStackTrace();
		}
		initComponents();
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}
	private void initComponents() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 550, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel mainpanel = new JPanel();
		MigLayout panellayout = new MigLayout();
//		MigLayout panellayout = new MigLayout("debug");
		mainpanel.setLayout(panellayout);
//		JPanel top = new JPanel(new MigLayout("debug"));
//		JPanel middle = new JPanel(new MigLayout("debug"));
//		JPanel bottom = new JPanel(new MigLayout("debug"));

		JPanel top = new JPanel(new MigLayout());
		JPanel middle = new JPanel(new MigLayout());
		JPanel bottom = new JPanel(new MigLayout());
		
		top.setBorder(BorderFactory.createTitledBorder("Current Rendition Details"));
		middle.setBorder(BorderFactory.createTitledBorder("Add Rendition"));
		bottom.setBorder(BorderFactory.createEmptyBorder());

		top.setPreferredSize(new Dimension(1920, 1920));
		middle.setPreferredSize(new Dimension(1920, 1920));
		bottom.setPreferredSize(new Dimension(2000, 400));

		path = new JTextField(32);
		JButton browsepath = new JButton("Browse");
		browsepath.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				browserpathActionPerformed(e);
			}
		});
		formatcombo = new JComboBox();

		renditiontable = new JTable();
		DocInfo info = new DocInfo();
		this.model = info.initializeRenditionsTableModel();
		this.model = info.updateRenditionsModel(objId, this.model);
		this.renditiontable.setModel(this.model);
		this.renditiontable.getColumnModel().getColumn(0).setCellRenderer(new LockRenderer());
		this.renditiontable.getColumnModel().getColumn(1).setCellRenderer(new FormatRenderer());
		this.renditiontable.getColumnModel().getColumn(0).setPreferredWidth(22);
		this.renditiontable.getColumnModel().getColumn(0).setMaxWidth(22);
		this.renditiontable.getColumnModel().getColumn(1).setPreferredWidth(22);
		this.renditiontable.getColumnModel().getColumn(1).setMaxWidth(22);

		this.renditiontable.validate();

		scrollPane = new JScrollPane();
		scrollPane.setAutoscrolls(true);
		renditiontable.setAutoResizeMode(0);
		this.scrollPane.setViewportView(renditiontable);
		System.out.println(format);
		imageURL = ImportRenditionFrame.class.getResource("/fi/fortica/quickclient/gui/format/f_" + format + "_32.gif");
		System.out.println(imageURL);
		if (imageURL != null) {
			icon = new ImageIcon(imageURL);
		}
		System.out.println(icon);
		renditiontable.setModel(model);
		MigLayout toplayout = new MigLayout("debug");
		top.setLayout(toplayout);

		top.add(new JLabel(icon), "spany 2");
		top.add(new JLabel("Object Name: " + objname), "wrap");
		top.add(new JLabel("Format: " + format), "wrap");
		top.add(scrollPane, "span,wrap");

		middle.add(new JLabel("Path:"));
		middle.add(path, "grow");
		middle.add(browsepath, "wrap");

		middle.add(new JLabel("Format:"));
		middle.add(formatcombo, "grow");

		chkAnyFormat = new JCheckBox("Any");
		chkAnyFormat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chkAnyActionPerformed(e);
			}
		});

		middle.add(chkAnyFormat, "wrap");
		middle.add(new JLabel("Page Modifier:"));
		this.pagemodifier = new JTextField(15);
		middle.add(pagemodifier, "grow, wrap");
		middle.add(new JLabel("Page:"));
		this.page = new JTextField(15);
		IntVerifier verifier = new IntVerifier();
		page.setInputVerifier(verifier);
		
		PathVerifier pverifier = new PathVerifier();
		path.setInputVerifier(pverifier);
		
		middle.add(page, "grow, wrap");

		this.cmdaddRendition = new JButton("Add");
		middle.add(new JLabel(""));
		cmdaddRendition.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cmdaddRenditionActionPerformed(e);
			}
		});
		middle.add(cmdaddRendition);

		JButton button = new JButton("Close");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmdCloseActionPerformed(e);
			}
		});

		bottom.add(button, "right");
		mainpanel.add(top, "north");
		mainpanel.add(middle, "north");
		mainpanel.add(bottom, "south");

		contentPane.add(mainpanel);

	}

	protected void cmdaddRenditionActionPerformed(ActionEvent e) {
		try {
			String filePath = path.getText();
			FormatComboItem item = (FormatComboItem) formatcombo.getSelectedItem();
			String format = item.getName();
			String pageModifier = pagemodifier.getText();
			String pageNumber = page.getText();

			if (pageModifier.length() == 0 && pageNumber.length() == 0) {
				obj.addRendition(filePath, format);
				obj.save();
			}
			if (pageModifier.length() > 0 && pageNumber.length() == 0) {
				obj.addRenditionEx2(filePath, format, 0, pageModifier, null, true, false, false);
			}
			if (pageModifier.length() > 0 && pageNumber.length() > 0) {
				int pagen = Integer.parseInt(pageNumber);
				obj.addRenditionEx2(filePath, format, pagen, pageModifier, null, true, false, false);
			}
			if (pageModifier.length() == 0 && pageNumber.length() > 0) {
				int pagen = Integer.parseInt(pageNumber);
				obj.addRenditionEx2(filePath, format, pagen, null, null, true, false, false);
			}
			DocInfo info = new DocInfo();
			model = info.updateRenditionsModel(objId, model);
			renditiontable.setModel(this.model);
			renditiontable.validate();
		} catch (DfException ex) {
			SwingHelper.showErrorMessage("Error in rendition add!", ex.getMessage());
			DfLogger.error(this, "Error in rendition add!", null, ex);
		}
	}

	protected void chkAnyActionPerformed(ActionEvent e) {

		if (chkAnyFormat.isSelected()) {
			updateFormatCombo(null);
		} else {
			String filePath = path.getText();
			if (filePath.length() > 0) {
				String dosext = "";
				int i = filePath.lastIndexOf(".");
				if (i > -1) {
					dosext = filePath.substring(i + 1);
				}
				updateFormatCombo(dosext);
			}
		}

	}

	protected void cmdCloseActionPerformed(ActionEvent e) {
		try {
			init();
		} catch (DfException e1) {
			e1.printStackTrace();
		}

	}

	protected void browserpathActionPerformed(ActionEvent e) {
		JFileChooser fc = new JFileChooser();
		fc.showOpenDialog(null);
		File selFile = fc.getSelectedFile();
		String absPath = selFile.getAbsolutePath();
		path.setText(absPath);
		String dosext = "";
		int i = absPath.lastIndexOf(".");
		if (i > -1) {
			dosext = absPath.substring(i + 1);
		}
		updateFormatCombo(dosext);
	}

	private void updateFormatCombo(String de) {
		String dosExt = de == null ? "" : de;
		ArrayList<IDfFormat> formats = null;
		if (dosExt.length() > 0) {
			formats = getFormats(dosExt);
		} else {
			formats = getFormats("");
		}
		formatcombo.removeAllItems();
		try {
			if (formats.size() > 0) {
				for (IDfFormat f : formats) {
					FormatComboItem fci = new FormatComboItem(f);
					formatcombo.addItem(fci);
				}
			}
		} catch (DfException ex) {
			SwingHelper.showErrorMessage("Error in updateFormatCombo!", ex.getMessage());
			DfLogger.error(this, "Error in updateFormatCombo!", null, ex);

		}
		formatcombo.validate();
	}

	private ArrayList<IDfFormat> getFormats(String de) {
		String dosExt = de == null ? "" : de;
		ArrayList<IDfFormat> al = new ArrayList<IDfFormat>();
		IDfCollection col = null;
		IDfQuery query = new DfQuery();
		if (dosExt.length() == 0)
			query.setDQL("select name from dm_format order by description");
		else
			query.setDQL("select name from dm_format where dos_extension = '" + dosExt + "' order by description");
		try {
			col = query.execute(session, IDfQuery.DF_QUERY);
			while (col.next()) {
				String name = col.getString("name");
				IDfFormat f = session.getFormat(name);
				al.add(f);
			}
		} catch (DfException ex) {
			SwingHelper.showErrorMessage("Error in getFormats!", ex.getMessage());
			DfLogger.error(this, "Error in getFormats!", null, ex);
		} finally {
			if (col != null)
				try {
					col.close();
				} catch (DfException e) {
				}
		}
		return al;
	}

}
