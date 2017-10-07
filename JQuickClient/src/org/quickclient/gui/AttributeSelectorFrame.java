/*
 * AttributeSelectorFrame.java
 *
 * Created on 9. joulukuuta 2007, 15:35
 */
package org.quickclient.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.quickclient.classes.ConfigService;
import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.ExJTextField;
import org.quickclient.classes.ListAttribute;
import org.quickclient.classes.ListConfiguration;
import org.quickclient.classes.Utils;
import org.quickclient.classes.VisibleAttributeData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfType;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfLogger;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

/**
 *
 * @author Administrator
 */
public class AttributeSelectorFrame extends javax.swing.JFrame {

	private final DefaultListModel selectedModel = new DefaultListModel();
	private final DefaultListModel allModel = new DefaultListModel();
	private Vector listattributes;
	private ActionListener actionlistener;
	private VisibleAttributeData data;
	private final ExJTextField textField;
	private final JComboBox cmbConfig;
	private final JComboBox cmbType;
	private final DocuSessionManager smanager = DocuSessionManager.getInstance();
	private final JList lstAll;
	private final JList selectedlist;
	private final String selectedConfig = "";
	private final String repo;

	/** Creates new form AttributeSelectorFrame */
	public AttributeSelectorFrame() {
		setPreferredSize(new Dimension(100, 100));
		this.setSize(607, 477);
		this.repo = ConfigService.getInstance().getDocbasename();
		validataterepositoryxpath();
		setTitle("Attributes.");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);

		final JButton cmdClose = new JButton("Close");
		cmdClose.setBounds(499, 421, 91, 23);
		getContentPane().add(cmdClose);

		final JButton cmdSave = new JButton("Save");
		cmdSave.setBounds(396, 421, 91, 23);
		getContentPane().add(cmdSave);

		final JPanel panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel.setBounds(10, 11, 580, 337);
		getContentPane().add(panel);

		final JLabel lblNewLabel = new JLabel("Configuration:");

		cmbConfig = new JComboBox();
		cmbConfig.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(final ItemEvent arg0) {
				cmbConfigItemStateChanged(arg0);
			}
		});

		final JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Available Attributes", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));

		final JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Selected Attributes", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));

		final JButton btnNewButton_2 = new JButton("Add >>");
		btnNewButton_2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				cmdAddActionPerformed(arg0);
			}
		});

		final JButton btnRemove = new JButton("Remove <<");
		btnRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				cmdRemoveActionPerformed(arg0);
			}
		});

		final JButton cmdDelete = new JButton("Delete");
		cmdDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				cmdDeleteActionPerformed(e);
			}
		});
		cmdDelete.setToolTipText("Deletes selected configuration");

		final JButton btnNewButton = new JButton("Create");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				cmdNewConfigActionPerformed(arg0);
			}
		});

		final JScrollPane scrollPane_1 = new JScrollPane();
		final GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(gl_panel_2.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_2.createSequentialGroup().addContainerGap().addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE).addContainerGap()));
		gl_panel_2.setVerticalGroup(gl_panel_2.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_2.createSequentialGroup().addContainerGap().addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE).addContainerGap()));

		selectedlist = new JList();
		scrollPane_1.setViewportView(selectedlist);
		panel_2.setLayout(gl_panel_2);

		final JScrollPane scrollPane = new JScrollPane();

		cmbType = new JComboBox();
		cmbType.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(final ItemEvent arg0) {
				cmbTypeitemStateChanged(arg0);
			}
		});
		final GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING).addGroup(gl_panel_1.createSequentialGroup().addContainerGap().addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING).addComponent(cmbType, Alignment.LEADING, 0, 188, Short.MAX_VALUE).addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)).addContainerGap()));
		gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_1.createSequentialGroup().addContainerGap().addComponent(cmbType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 211, GroupLayout.PREFERRED_SIZE).addContainerGap()));

		lstAll = new JList();
		scrollPane.setViewportView(lstAll);
		panel_1.setLayout(gl_panel_1);
		panel.setLayout(new FormLayout(new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("69px"), FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, ColumnSpec.decode("148px"), FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("69px"), FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("18px"), ColumnSpec.decode("18px"), ColumnSpec.decode("220px"), },
				new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("23px"), FormSpecs.LINE_GAP_ROWSPEC, RowSpec.decode("104px"), FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("177px"), }));
		panel.add(lblNewLabel, "2, 2, left, center");
		panel.add(cmbConfig, "4, 2, fill, top");
		panel.add(panel_1, "2, 4, 3, 3, left, top");
		panel.add(btnRemove, "6, 6, 3, 1, left, top");
		panel.add(btnNewButton_2, "6, 4, 3, 1, fill, bottom");
		panel.add(panel_2, "10, 4, 1, 3, fill, fill");
		panel.add(btnNewButton, "6, 2, right, top");
		panel.add(cmdDelete, "8, 2, 3, 1, left, top");

		final JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(null, "Order by", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_3.setBounds(10, 354, 580, 56);
		getContentPane().add(panel_3);

		final JLabel lblNewLabel_1 = new JLabel("Attributes:");

		textField = new ExJTextField();
		textField.setColumns(10);

		final JCheckBox chckbxNewCheckBox = new JCheckBox("Descending order");
		final GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(gl_panel_3.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_3.createSequentialGroup().addContainerGap().addComponent(lblNewLabel_1).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(textField, GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE).addPreferredGap(ComponentPlacement.RELATED).addComponent(chckbxNewCheckBox).addGap(99)));
		gl_panel_3.setVerticalGroup(gl_panel_3.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_3.createSequentialGroup().addGroup(gl_panel_3.createParallelGroup(Alignment.BASELINE).addComponent(lblNewLabel_1).addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(chckbxNewCheckBox)).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		panel_3.setLayout(gl_panel_3);
		initComponents();
	}

	private void addAttribute(final String configname, final String attrname, final String typename) {

		try {
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringComments(true);
			Document document = null;
			final String xmlString = Utils.readFileAsString("listattributes.xml");
			if (xmlString != null) {
				final DocumentBuilder builder = factory.newDocumentBuilder();
				document = builder.parse(new InputSource(new StringReader(xmlString)));
			} else {
				return;
			}
			final String categoryXpath = "/listattributes/repository[@name='" + this.repo + "']/config[@id='" + configname + "']";
			Node confignode = null;
			final XPath xpath = XPathFactory.newInstance().newXPath();
			confignode = (Node) xpath.evaluate(categoryXpath, document, XPathConstants.NODE);

			final Element newNode = document.createElement("attribute");
			newNode.setAttribute("name", attrname);
			newNode.setAttribute("type", typename);
			confignode.appendChild(newNode);

			selectedlist.setModel(selectedModel);
			selectedlist.validate();

			saveDocument(document);

			readConfig();
		} catch (final SAXException | XPathExpressionException | TransformerFactoryConfigurationError | TransformerException | IOException | ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected void cmbConfigItemStateChanged(final ItemEvent arg0) {

		final String itemname = (String) cmbConfig.getSelectedItem();
		selectedModel.removeAllElements();
		final Vector<ListConfiguration> config = new Vector<ListConfiguration>();
		final Vector<String> confignames = new Vector<String>();
		try {
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringComments(true);
			Document document = null;
			final String configXpath = "/listattributes/repository[@name='" + this.repo + "']/config";
			NodeList nodes = null;
			final String xmlString = Utils.readFileAsString("listattributes.xml");
			if (xmlString != null) {
				final DocumentBuilder builder = factory.newDocumentBuilder();
				document = builder.parse(new InputSource(new StringReader(xmlString)));
				final XPath xpath = XPathFactory.newInstance().newXPath();
				nodes = (NodeList) xpath.evaluate(configXpath, document, XPathConstants.NODESET);
				for (int i = 0; i < nodes.getLength(); i++) {
					final Node node = nodes.item(i);
					final String configid = node.getAttributes().getNamedItem("id").getNodeValue();
					confignames.add(configid);
				}
			}
			for (final String confname : confignames) {
				if (confname.equals(itemname)) {
					final String categoryXpath = "/listattributes/repository[@name='" + this.repo + "']/config[@id='" + confname + "']/attribute";
					NodeList nodes2 = null;
					final ListConfiguration listconfig = new ListConfiguration(confname);
					final XPath xpath = XPathFactory.newInstance().newXPath();
					nodes2 = (NodeList) xpath.evaluate(categoryXpath, document, XPathConstants.NODESET);
					for (int i = 0; i < nodes2.getLength(); i++) {
						final Node node = nodes2.item(i);
						final NamedNodeMap attrs = node.getAttributes();
						final String type = attrs.getNamedItem("type").getNodeValue();
						final String attribute = attrs.getNamedItem("name").getNodeValue();
						final Node labelnode = attrs.getNamedItem("label");
						final ListAttribute a = null;
						String label = null;
						final String nn = type + "." + attribute;
						selectedModel.addElement(nn);
						if (labelnode != null) {
							label = labelnode.getNodeValue();
						} else {
						}
						listconfig.append(a);
					}
					selectedlist.setModel(selectedModel);
					selectedlist.validate();
				}
			}

		} catch (final SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected void cmbTypeitemStateChanged(final ItemEvent itemevent) {

		IDfCollection col = null;
		IDfSession session = null;
		final String typename = (String) cmbType.getSelectedItem();
		allModel.removeAllElements();
		try {
			session = smanager.getSession();
			final IDfType type = session.getType(typename);
			final IDfQuery query = new DfQuery();
			final String qry = "select attr_name from dm_type where name = '" + typename + "' and any attr_name not in (select attr_name from dm_type where name = '" + type.getSuperName() + "') order by attr_name enable (row_based)";
			System.out.println(qry);
			query.setDQL(qry);
			col = query.execute(session, DfQuery.DF_CACHE_QUERY);
			while (col.next()) {
				allModel.addElement(col.getString("attr_name"));
			}
			this.lstAll.setModel(allModel);
			this.lstAll.validate();
		} catch (final DfException ex) {
			Logger.getLogger(AttributeSelectorFrame.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			if (col != null) {
				try {
					col.close();
				} catch (final DfException e) {

				}
			}
			if (session != null) {
				smanager.releaseSession(session);
			}
		}

	}

	protected void cmdAddActionPerformed(final ActionEvent arg0) {

		final int index = lstAll.getSelectedIndex();
		if (index == -1) {
			return;
		}

		final String value = (String) lstAll.getSelectedValue();
		final String configname = (String) cmbConfig.getSelectedItem();
		final String typename = (String) cmbType.getSelectedItem();
		System.out.println(value + " " + configname);
		addAttribute(configname, value, typename);
	}

	protected void cmdDeleteActionPerformed(final ActionEvent ae) {

		final String newconfigname = (String) cmbConfig.getSelectedItem();
		try {
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringComments(true);
			Document document = null;
			final String xmlString = Utils.readFileAsString("listattributes.xml");
			if (xmlString != null) {
				final DocumentBuilder builder = factory.newDocumentBuilder();
				document = builder.parse(new InputSource(new StringReader(xmlString)));
			} else {
				return;
			}

			final String rootXpath = "/listattributes/repository[@name='" + this.repo + "']/config[@id='" + newconfigname + "']";
			Node lanode = null;
			final XPath xpath = XPathFactory.newInstance().newXPath();
			lanode = (Node) xpath.evaluate(rootXpath, document, XPathConstants.NODE);
			lanode.getParentNode().removeChild(lanode);
			saveDocument(document);

			readConfig();
		} catch (final SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected void cmdNewConfigActionPerformed(final ActionEvent arg0) {
		final String newconfigname = JOptionPane.showInputDialog("Enter name for configuration");
		try {
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringComments(true);
			Document document = null;
			final String xmlString = Utils.readFileAsString("listattributes.xml");
			if (xmlString != null) {
				final DocumentBuilder builder = factory.newDocumentBuilder();
				document = builder.parse(new InputSource(new StringReader(xmlString)));
			} else {
				return;
			}

			final String rootXpath = "/listattributes/repository[@name='" + this.repo + "']";
			Node lanode = null;
			final XPath xpath = XPathFactory.newInstance().newXPath();
			lanode = (Node) xpath.evaluate(rootXpath, document, XPathConstants.NODE);

			final Element newNode = document.createElement("config");
			newNode.setAttribute("id", newconfigname);
			lanode.appendChild(newNode);

			saveDocument(document);

			readConfig();
		} catch (final SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected void cmdRemoveActionPerformed(final ActionEvent arg0) {

		final int index = selectedlist.getSelectedIndex();
		if (index == -1) {
			return;
		}

		final String value = (String) selectedlist.getSelectedValue();
		final String configname = (String) cmbConfig.getSelectedItem();
		System.out.println(value + " " + configname);
		removeAttribute(configname, value);
	}

	public void initAllAttributes() {
		IDfCollection col = null;
		IDfSession session = null;
		try {
			session = smanager.getSession();
			final IDfQuery query = new DfQuery();
			query.setDQL("select attr_name from dm_type where name = 'dm_sysobject' order by attr_name");
			col = query.execute(session, DfQuery.DF_CACHE_QUERY);
			while (col.next()) {
				allModel.addElement(col.getString("attr_name"));
			}

			// lstAll.setModel(allModel);
		} catch (final DfException ex) {
			DfLogger.error(this, null, null, ex);
		} finally {
			if (col != null) {
				try {
					col.close();
				} catch (final DfException e) {

				}
			}
			if (session != null) {
				smanager.releaseSession(session);
			}
		}

	}

	private void initComponents() {
		readConfig();
		initTypeList();
	}

	private void initTypeList() {

		IDfCollection col = null;
		IDfSession session = null;
		final boolean customdone = false;
		try {
			session = smanager.getSession();
			final IDfQuery query = new DfQuery();
			query.setDQL("select name, '1' as orderb from dm_type where any attr_name = 'r_object_type' " + "and super_name not in ('dm_sysobject' )and name not like 'dm_%' union select name, '2' as orderb " + "from dm_type where any attr_name = 'r_object_type' and super_name not in ('dm_sysobject' ) and name like 'dm%' order by 2");
			col = query.execute(session, DfQuery.DF_CACHE_QUERY);
			while (col.next()) {
				cmbType.addItem(col.getString("name"));
			}
		} catch (final DfException ex) {
			DfLogger.error(this, null, null, ex);
		} finally {
			if (col != null) {
				try {
					col.close();
				} catch (final DfException e) {

				}
			}
			if (session != null) {
				smanager.releaseSession(session);
			}
		}

	}

	private void readConfig() {
		final Vector<ListConfiguration> config = new Vector<ListConfiguration>();
		final Vector<String> confignames = new Vector<String>();
		try {
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringComments(true);
			Document document = null;
			final String configXpath = "/listattributes/repository[@name='" + this.repo + "']/config";
			NodeList nodes = null;
			final String xmlString = Utils.readFileAsString("listattributes.xml");
			if (xmlString != null) {
				final DocumentBuilder builder = factory.newDocumentBuilder();
				document = builder.parse(new InputSource(new StringReader(xmlString)));
				final XPath xpath = XPathFactory.newInstance().newXPath();
				nodes = (NodeList) xpath.evaluate(configXpath, document, XPathConstants.NODESET);
				cmbConfig.removeAllItems();
				for (int i = 0; i < nodes.getLength(); i++) {
					final Node node = nodes.item(i);
					final String configid = node.getAttributes().getNamedItem("id").getNodeValue();
					confignames.add(configid);
					cmbConfig.addItem(configid);
				}
			}

		} catch (final SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void removeAttribute(final String configname, final String value) {
		try {
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringComments(true);
			Document document = null;
			final String xmlString = Utils.readFileAsString("listattributes.xml");
			if (xmlString != null) {
				final DocumentBuilder builder = factory.newDocumentBuilder();
				document = builder.parse(new InputSource(new StringReader(xmlString)));
			} else {
				return;
			}

			final String categoryXpath = "/listattributes/repository[@name='" + this.repo + "']/config[@id='" + configname + "']/attribute";
			NodeList nodes2 = null;
			final XPath xpath = XPathFactory.newInstance().newXPath();
			nodes2 = (NodeList) xpath.evaluate(categoryXpath, document, XPathConstants.NODESET);
			for (int i = 0; i < nodes2.getLength(); i++) {
				final Node node = nodes2.item(i);
				final NamedNodeMap attrs = node.getAttributes();
				final String type = attrs.getNamedItem("type").getNodeValue();
				final String attribute = attrs.getNamedItem("name").getNodeValue();
				final String testvalue = type + "." + attribute;
				System.out.println("testvalue" + testvalue);
				System.out.println("value" + value);
				if (testvalue.equals(value)) {
					System.out.println("remove");
					node.getParentNode().removeChild(node);
				}
			}
			selectedlist.setModel(selectedModel);
			selectedlist.validate();

			saveDocument(document);

			readConfig();

		} catch (final SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void saveDocument(final Document document) throws TransformerFactoryConfigurationError, TransformerException, IOException {

		final StringWriter output = new StringWriter();
		final Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.transform(new DOMSource(document), new StreamResult(output));
		final String xml = output.toString();
		final File savefile = new File("listattributes.xml");
		final FileOutputStream fos = new FileOutputStream(savefile);
		fos.write(xml.getBytes());
		fos.flush();
		fos.close();
	}

	protected void validataterepositoryxpath() {
		try {
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringComments(true);
			Document document = null;
			final String xmlString = Utils.readFileAsString("listattributes.xml");
			if (xmlString != null) {
				final DocumentBuilder builder = factory.newDocumentBuilder();
				document = builder.parse(new InputSource(new StringReader(xmlString)));
			} else {
				return;
			}

			Node lanode = null;
			final XPath xpath = XPathFactory.newInstance().newXPath();
			lanode = (Node) xpath.evaluate("/listattributes", document, XPathConstants.NODE);
			if (lanode == null) {
				final Element newNode = document.createElement("listattributes");
				document.appendChild(newNode);
			}

			Node reponode = null;
			reponode = (Node) xpath.evaluate("/listattributes/repository[@name='" + this.repo + "']", document, XPathConstants.NODE);
			if (reponode == null) {
				final Element newNode = document.createElement("repository");
				newNode.setAttribute("name", this.repo);
				if (lanode != null) {
					lanode.appendChild(newNode);
				}
			}

			saveDocument(document);

		} catch (final SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
