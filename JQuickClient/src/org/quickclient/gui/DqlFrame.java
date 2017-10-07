/*
 * DqlFrame.java
 *
 * Created on 28. lokakuuta 2006, 1:44
 */
package org.quickclient.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.quickclient.actions.QCActionException;
import org.quickclient.actions.ViewInternalAction;
import org.quickclient.classes.AclBrowserData;
import org.quickclient.classes.ConfigService;
import org.quickclient.classes.DQLHistorySingleton;
import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.ExJTextArea;
import org.quickclient.classes.FileUtils;
import org.quickclient.classes.SwingHelper;
import org.quickclient.classes.Utils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfFolder;
import com.documentum.fc.client.IDfPersistentObject;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLogger;
import com.documentum.fc.common.IDfAttr;
import com.documentum.fc.common.IDfId;
import com.documentum.xerces_2_8_0.xml.serialize.OutputFormat;
import com.documentum.xerces_2_8_0.xml.serialize.XMLSerializer;
import com.documentum.xml.xdql.DfXmlQuery;
import com.documentum.xml.xdql.IDfXmlQuery;

@Deprecated
public class DqlFrame extends javax.swing.JFrame {
	private static final long serialVersionUID = 1L;
	DocuSessionManager smanager;
	private Document document;
	private boolean usedocbase = false;

	private final String sb = "";

	DQLHistorySingleton history = DQLHistorySingleton.getInstance();

	private DefaultTableModel tablemodel;

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JCheckBox chkShowSQL;

	private javax.swing.JComboBox cmbAttrCombo;

	private javax.swing.JComboBox cmbQueries;

	private javax.swing.JComboBox cmbQueryCategory;

	private javax.swing.JComboBox cmbTypeCombo;

	private javax.swing.JButton cmdHistoryDown1;

	private javax.swing.JButton cmdHistoryUP;

	private javax.swing.JButton cmdQuery;

	private javax.swing.JButton cmdSaveQuery;

	private javax.swing.JPopupMenu dqlPopUp;

	private javax.swing.JButton jButton1;

	private javax.swing.JButton jButton2;

	private javax.swing.JButton jButton3;

	private javax.swing.JLabel jLabel1;

	private javax.swing.JLabel jLabel2;

	private javax.swing.JLabel jLabel3;

	private javax.swing.JLabel jLabel4;

	private javax.swing.JPanel jPanel1;

	private javax.swing.JPanel jPanel2;

	private javax.swing.JPanel jPanel3;

	private javax.swing.JPanel jPanel4;

	private javax.swing.JScrollPane jScrollPane1;

	private javax.swing.JScrollPane jScrollPane2;

	private javax.swing.JScrollPane jScrollPane4;

	private javax.swing.JSeparator jSeparator1;

	private javax.swing.JSeparator jSeparator2;

	private javax.swing.JPopupMenu.Separator jSeparator3;

	private javax.swing.JSplitPane jSplitPane1;

	private javax.swing.JLabel lblTimer;

	private javax.swing.JMenuItem mnuAttributes;

	private javax.swing.JMenuItem mnuCopy;

	private javax.swing.JMenuItem mnuDestroy;

	private javax.swing.JMenuItem mnuDump;

	private javax.swing.JMenuItem mnuExportExcel;

	private javax.swing.JMenuItem mnuSetAcl;
	private javax.swing.JMenuItem mnuSetAttribute;
	private javax.swing.JMenuItem mnuShowLocations;
	private javax.swing.JMenuItem mnuViewContent;
	private javax.swing.JMenuItem mnuViewInternal;
	private javax.swing.JTable tblResult;
	private ExJTextArea txtDQL;
	private ExJTextArea txtSQL;
	// End of variables declaration//GEN-END:variables
	private int historylocation;
	private JCheckBox chckbxXml;

	/** Creates new form DqlFrame */
	public DqlFrame() {
		initComponents();
		final String username = ConfigService.getInstance().getUsername();
		final String docbasename = ConfigService.getInstance().getDocbasename();
		usedocbase = ConfigService.getInstance().isDqldocbase();
		this.setTitle("DQL - " + username + "@" + docbasename);
		jSplitPane1.setDividerLocation(1.0);
		smanager = DocuSessionManager.getInstance();
		initTypeCombo();
		tablemodel = new DefaultTableModel() {

			@Override
			public boolean isCellEditable(final int row, final int column) {
				return false;
			}
		};
		this.jScrollPane1.getViewport().setBackground(Color.WHITE);
		this.jScrollPane2.getViewport().setBackground(Color.WHITE);
		this.jScrollPane4.getViewport().setBackground(Color.WHITE);
		historylocation = 0;

	}

	private void chkDocbaseActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_chkDocbaseActionPerformed
		if (usedocbase) {
			usedocbase = false;
			ConfigService.getInstance().setDqldocbase(false);
		} else {
			usedocbase = true;
			ConfigService.getInstance().setDqldocbase(true);
		}
		initDQLList();// GEN-LAST:event_chkDocbaseActionPerformed

	}

	private void chkShowSQLActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_chkShowSQLActionPerformed

		if (chkShowSQL.isSelected()) {
			jSplitPane1.setDividerLocation(0.75);
		} else {
			jSplitPane1.setDividerLocation(1.0);

		}

	}// GEN-LAST:event_chkShowSQLActionPerformed

	private void cmbAttrComboItemStateChanged(final java.awt.event.ItemEvent evt) {
	}

	private void cmbQueriesItemStateChanged(final java.awt.event.ItemEvent evt) {// GEN-FIRST:event_cmbQueriesItemStateChanged
		try {
			final String selectedcatetory = (String) cmbQueryCategory.getSelectedItem();
			final String selectedtopic = (String) cmbQueries.getSelectedItem();
			final XPath xpath = XPathFactory.newInstance().newXPath();
			String value = "";
			value = (String) xpath.evaluate("/queries/category[@name='" + selectedcatetory + "']/query[@name='" + selectedtopic + "']/dql", document, XPathConstants.STRING);
			txtDQL.setText(value);
			value = "";
			value = (String) xpath.evaluate("/queries/category[@name='" + selectedcatetory + "']/query[@name='" + selectedtopic + "']/description", document, XPathConstants.STRING);
			// txtDescription.setText(value);

		} catch (final XPathExpressionException ex) {
			DfLogger.error(this, ex.getMessage(), null, ex);

			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
		}
	}// GEN-LAST:event_cmbQueriesItemStateChanged

	private void cmbQueriesMouseWheelMoved(final java.awt.event.MouseWheelEvent evt) {// GEN-FIRST:event_cmbQueriesMouseWheelMoved

		final int maxindex = cmbQueries.getItemCount();
		if (evt.getWheelRotation() > 0) {
			final int index = cmbQueries.getSelectedIndex();
			if (index < maxindex - 1) {
				cmbQueries.setSelectedIndex(index + 1);
			}
		} else {
			final int index = cmbQueries.getSelectedIndex();
			if (index > 0) {
				cmbQueries.setSelectedIndex(index - 1);
			}
		}
	}// GEN-LAST:event_cmbQueriesMouseWheelMoved

	private void cmbQueryCategoryItemStateChanged(final java.awt.event.ItemEvent evt) {// GEN-FIRST:event_cmbQueryCategoryItemStateChanged
		// System.out.println("changed.");
		cmbQueries.removeAllItems();
		try {
			final String selectedvalue = (String) cmbQueryCategory.getSelectedItem();
			final XPath xpath = XPathFactory.newInstance().newXPath();
			NodeList nodes = null;
			nodes = (NodeList) xpath.evaluate("/queries/category[@name='" + selectedvalue + "']/query/@name", document, XPathConstants.NODESET);
			for (int i = 0; i < nodes.getLength(); i++) {
				final Node node = nodes.item(i);
				final String value = node.getNodeValue();
				cmbQueries.addItem(value);
			}
		} catch (final XPathExpressionException ex) {
			DfLogger.error(this, ex.getMessage(), null, ex);

		}
	}// GEN-LAST:event_cmbQueryCategoryItemStateChanged

	private void cmbQueryCategoryMouseWheelMoved(final java.awt.event.MouseWheelEvent evt) {// GEN-FIRST:event_cmbQueryCategoryMouseWheelMoved
		final int maxindex = cmbQueryCategory.getItemCount();
		if (evt.getWheelRotation() > 0) {
			final int index = cmbQueryCategory.getSelectedIndex();
			if (index < maxindex - 1) {
				cmbQueryCategory.setSelectedIndex(index + 1);
			}
		} else {
			final int index = cmbQueryCategory.getSelectedIndex();
			if (index > 0) {
				cmbQueryCategory.setSelectedIndex(index - 1);
			}
		}
	}// GEN-LAST:event_cmbQueryCategoryMouseWheelMoved

	private void cmbTypeComboItemStateChanged(final java.awt.event.ItemEvent evt) {
		IDfSession session = null;
		IDfCollection col = null;
		try {
			session = smanager.getSession();
			final IDfQuery query = new DfQuery();
			query.setDQL("select attr_name from dm_type where name = '" + cmbTypeCombo.getSelectedItem() + "' order by attr_name");
			col = query.execute(session, DfQuery.DF_READ_QUERY);
			cmbAttrCombo.removeAllItems();
			while (col.next()) {
				cmbAttrCombo.addItem(col.getString("attr_name"));
			}
		} catch (final DfException ex) {
			DfLogger.error(this, ex.getMessage(), null, ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
		} finally {
			if (col != null) {
				try {
					col.close();
				} catch (final DfException ex) {
					DfLogger.error(this, ex.getMessage(), null, ex);
				}
			}
			if (session != null) {
				smanager.releaseSession(session);
			}
		}
	}

	private void cmdCopyActionPerformed(final java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
	}

	private void cmdHistoryDown1ActionPerformed(final java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
	}

	private void cmdHistoryUPActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdHistoryUPActionPerformed
		if (historylocation > 0) {
			historylocation = historylocation - 1;
			txtDQL.setText(history.getHistory().get(historylocation));
		}
	}// GEN-LAST:event_cmdHistoryUPActionPerformed

	private void cmdQueryActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdQueryActionPerformed
		// TODO add your handling code here:
		if (this.chckbxXml.isSelected() == false) {
			historylocation = historylocation + 1;
			this.executeQuery();
		} else {
			historylocation = historylocation + 1;
			this.executeXMLQuery();
		}

	}// GEN-LAST:event_cmdQueryActionPerformed

	private void cmdQueryMouseClicked(final java.awt.event.MouseEvent evt) {// GEN-FIRST:event_cmdQueryMouseClicked
	}// GEN-LAST:event_cmdQueryMouseClicked

	private void cmdSaveQueryActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdSaveQueryActionPerformed

		if (document == null) {
			return;
		}
		IDfSession session = null;
		IDfSysObject obj = null;
		final Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
		setCursor(cur);
		try {
			session = smanager.getSession();
			final String selectedcatetory = (String) cmbQueryCategory.getSelectedItem();
			final String selectedtopic = (String) cmbQueries.getSelectedItem();
			final XPath xpath = XPathFactory.newInstance().newXPath();
			Element node = null;
			Node rootnode = null;
			Element node2 = null;
			Node node3 = null;
			Node descNode = null;
			rootnode = (Node) xpath.evaluate("/queries", document, XPathConstants.NODE);
			node = (Element) xpath.evaluate("/queries/category[@name='" + selectedcatetory + "']", document, XPathConstants.NODE);
			if (node == null) {
				node = document.createElement("category");
				final Attr a = document.createAttribute("name");
				a.setTextContent(selectedcatetory);
				node.setAttributeNode(a);
				rootnode.appendChild(node);
			}

			node2 = (Element) xpath.evaluate("/queries/category[@name='" + selectedcatetory + "']/query[@name='" + selectedtopic + "']", document, XPathConstants.NODE);
			if (node2 == null) {
				node2 = document.createElement("query");
				node2.setAttribute("name", selectedtopic);
				final Node dqlnode = document.createElement("dql");
				dqlnode.setTextContent("dql");
				final Node desnode = document.createElement("description");
				node.appendChild(node2);
				node2.appendChild(dqlnode);
				node2.appendChild(desnode);

			}
			node3 = (Node) xpath.evaluate("/queries/category[@name='" + selectedcatetory + "']/query[@name='" + selectedtopic + "']/dql", document, XPathConstants.NODE);
			node3.setTextContent(txtDQL.getText());
			descNode = (Node) xpath.evaluate("/queries/category[@name='" + selectedcatetory + "']/query[@name='" + selectedtopic + "']/description", document, XPathConstants.NODE);
			// descNode.setTextContent(txtDescription.getText());
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			final OutputFormat outputformat = new OutputFormat();
			outputformat.setIndenting(true);
			final XMLSerializer xmlserializer = new XMLSerializer();
			xmlserializer.setOutputFormat(outputformat);
			xmlserializer.setOutputByteStream(baos);
			xmlserializer.serialize(document);
			if (usedocbase) {
				obj = (IDfSysObject) session.getObjectByQualification("dm_document where object_name  ='quickclient_queries' and owner_name = '" + session.getUser("") + "'");
				if (obj == null) {
					obj = (IDfSysObject) session.newObject("dm_document");
					obj.setObjectName("quickclient_queries");
					obj.setContentType("xml");
					obj.save();
					obj.setContent(baos);
					obj.save();
				} else {
					obj.checkout();
					obj.setContent(baos);
					obj.saveLock();
					obj.checkin(false, "");
				}
			} else {
				final File file = new File(ConfigService.getInstance().getDqlfile());
				final FileOutputStream fos = new FileOutputStream(file);
				fos.write(baos.toByteArray());
				fos.flush();
				fos.close();
			}

		} catch (final XPathExpressionException ex) {
			DfLogger.error(this, ex.getMessage(), null, ex);

		} catch (final DfException ex) {
			DfLogger.error(this, ex.getMessage(), null, ex);

		} catch (final IOException ex) {
			DfLogger.error(this, ex.getMessage(), null, ex);

		} finally {
			if (session != null) {
				smanager.releaseSession(session);
				final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);

				setCursor(cur2);
			}
		}
		initDQLList();// GEN-LAST:event_cmdSaveQueryActionPerformed

	}

	private void executeQuery() {
		IDfSession session = null;
		IDfCollection col = null;
		long counteryy = 0;
		final Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
		final String repository = smanager.getDocbasename();
		final String username = smanager.getUserName();
		setCursor(cur);
		try {
			session = smanager.getSession();

			tablemodel = new DefaultTableModel() {

				@Override
				public boolean isCellEditable(final int row, final int column) {
					return false;
				}
			};
			tblResult.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			final Vector<Object> colVector = new Vector<Object>();

			final IDfQuery query = new DfQuery();
			query.setDQL(txtDQL.getText());
			history.addDQL(txtDQL.getText());
			DfLogger.info(this, repository + "/" + username + " start execute DQL: " + txtDQL.getText(), null, null);
			String repValue = "";
			String tempValue = "";
			final long startmillis = System.currentTimeMillis();
			col = query.execute(session, IDfQuery.DF_QUERY);
			final long stopmillis = System.currentTimeMillis();
			final double time = stopmillis - startmillis;
			final Enumeration colenum = col.enumAttrs();
			int counter1 = 0;
			String header = "";
			String singlerow = "";
			while (colenum.hasMoreElements()) {
				final IDfAttr attr = (IDfAttr) colenum.nextElement();
				colVector.add(attr.getName());
				counter1++;
				if (counter1 == 1) {
					header = attr.getName();
				}
			}
			tablemodel.setColumnIdentifiers(colVector);
			while (col.next()) {
				counteryy++;
				final Vector<Object> rowVector = new Vector<Object>();
				for (int x = 0; x < colVector.size(); x++) {
					int values = 1;
					values = col.getValueCount((String) colVector.get(x));
					// //System.out.println(values);
					if (values == 1) {
						rowVector.add(col.getRepeatingString((String) colVector.get(x), 0));
					} else {
						for (int y = 0; y < values; y++) {
							tempValue = col.getRepeatingString((String) colVector.get(x), y);
							if (y == 0) {
								repValue = tempValue;
							} else {
								repValue = repValue + ", " + tempValue;
							}
						}
						rowVector.add(repValue);
						repValue = "";
					}
				}
				tablemodel.addRow(rowVector);
				if (counteryy == 1) {
					if (counteryy == 1) {
						singlerow = header + ":" + rowVector.toString();
					} else {
						singlerow = rowVector.toString();
					}
				}
			}
			if (counteryy == 1) {
				DfLogger.info(this, repository + "/" + username + " 1 row, result: '" + singlerow + "'", null, null);
			} else {
				DfLogger.info(this, repository + "/" + username + " " + counteryy + " results", null, null);
			}
			DfLogger.info(this, repository + "/" + username + " End execute DQL, query took " + time, null, null);
			lblTimer.setText(counteryy + " objects found in: " + time / 1000 + " seconds.");
		} catch (final DfException ex) {
			DfLogger.info(this, repository + "/" + username + " DQL, ERROR " + txtDQL.getText(), null, null);
			DfLogger.error(this, ex.getMessage(), null, ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error Occured!!", JOptionPane.ERROR_MESSAGE);
		} finally {
			if (session != null) {
				smanager.releaseSession(session);
			}
			if (col != null) {
				try {
					col.close();
				} catch (final DfException ex) {
				}
			}
			final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
			setCursor(cur2);
		}
		tblResult.setModel(tablemodel);
		tblResult.validate();
		if (chkShowSQL.isSelected()) {
			txtSQL.setText(getSqlQuery());
		} else {
			txtSQL.setText("");
		}
		final int xx = tblResult.getColumnCount();
		for (int i = 0; i < xx; i++) {
			final TableColumn column = tblResult.getColumnModel().getColumn(i);
			column.setPreferredWidth(150);

		}

	}

	private void executeXMLQuery() {
		IDfSession session = null;
		final IDfCollection col = null;
		final long counteryy = 0;
		final Cursor cur = new Cursor(Cursor.WAIT_CURSOR);

		setCursor(cur);
		try {
			session = smanager.getSession();
			final IDfXmlQuery query = new DfXmlQuery();
			query.setDql(txtDQL.getText());
			history.addDQL(txtDQL.getText());
			DfLogger.info(this, "Start execute XDQL: " + txtDQL.getText(), null, null);
			final long startmillis = System.currentTimeMillis();
			query.execute(IDfQuery.DF_EXEC_QUERY, session);
			final String res = query.getXMLString();
			final TextViewer tv = new TextViewer(false, "Result of: " + txtDQL.getText());
			tv.setText(res);
			tv.setVisible(true);

			final long stopmillis = System.currentTimeMillis();
			final double time = stopmillis - startmillis;
			DfLogger.info(this, counteryy + " results", null, null);
			DfLogger.info(this, "End execute XDQL, query took " + time, null, null);
			lblTimer.setText(counteryy + " objects found in: " + time / 1000 + " seconds.");
		} catch (final DfException ex) {
			DfLogger.info(this, "XDQL, ERROR " + txtDQL.getText(), null, null);
			DfLogger.error(this, ex.getMessage(), null, ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error Occured!!", JOptionPane.ERROR_MESSAGE);
		} finally {
			if (session != null) {
				smanager.releaseSession(session);
			}

			final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
			setCursor(cur2);
		}
		if (chkShowSQL.isSelected()) {
			txtSQL.setText(getSqlQuery());
		} else {
			txtSQL.setText("");
		}
	}

	public JCheckBox getChckbxXml() {
		return chckbxXml;
	}

	@SuppressWarnings("deprecation")
	private String getSqlQuery() {
		String sqlResult = null;
		IDfSession session = null;
		try {
			session = smanager.getSession();
			final String collId = session.apiGet("apply", "NULL,GET_LAST_SQL");
			if (!collId.equals("")) {
				boolean retVal = session.apiExec("next", collId);
				sqlResult = session.apiGet("get", collId + ",result");
				retVal = session.apiExec("close", collId);
			}
			DfLogger.info(this, "DQL, SQL Trace: " + sqlResult, null, null);

		} catch (final DfException ex) {
			DfLogger.error(this, ex.getMessage(), null, ex);
		} finally {
			if (session != null) {
				smanager.releaseSession(session);
			}
		}
		return sqlResult;
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		dqlPopUp = new javax.swing.JPopupMenu();
		mnuCopy = new javax.swing.JMenuItem();
		jSeparator3 = new javax.swing.JPopupMenu.Separator();
		mnuDump = new javax.swing.JMenuItem();
		mnuAttributes = new javax.swing.JMenuItem();
		mnuViewContent = new javax.swing.JMenuItem();
		mnuViewInternal = new javax.swing.JMenuItem();
		mnuShowLocations = new javax.swing.JMenuItem();
		jSeparator1 = new javax.swing.JSeparator();
		mnuDestroy = new javax.swing.JMenuItem();
		jSeparator2 = new javax.swing.JSeparator();
		mnuExportExcel = new javax.swing.JMenuItem();
		mnuSetAttribute = new javax.swing.JMenuItem();
		mnuSetAcl = new javax.swing.JMenuItem();
		jPanel1 = new javax.swing.JPanel();
		lblTimer = new javax.swing.JLabel();
		jPanel2 = new javax.swing.JPanel();
		jSplitPane1 = new javax.swing.JSplitPane();
		jScrollPane2 = new javax.swing.JScrollPane();
		tblResult = new javax.swing.JTable();
		jScrollPane4 = new javax.swing.JScrollPane();
		txtSQL = new ExJTextArea();
		jPanel4 = new javax.swing.JPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		txtDQL = TextFieldFactory.createJTextarea();
		jPanel3 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		cmbQueryCategory = new javax.swing.JComboBox();
		cmbQueries = new javax.swing.JComboBox();
		cmdSaveQuery = new javax.swing.JButton();
		jButton3 = new javax.swing.JButton();
		jLabel3 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		cmbTypeCombo = new javax.swing.JComboBox();
		cmbAttrCombo = new javax.swing.JComboBox();

		mnuCopy.setText("Copy");
		mnuCopy.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuCopyActionPerformed(evt);
			}
		});
		dqlPopUp.add(mnuCopy);
		dqlPopUp.add(jSeparator3);

		mnuDump.setText("Show Dump of this ID");
		mnuDump.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuDumpActionPerformed(evt);
			}
		});
		dqlPopUp.add(mnuDump);

		mnuAttributes.setText("Attribute Editor");
		mnuAttributes.setActionCommand("mnuAttrEdit");
		mnuAttributes.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuAttributesActionPerformed(evt);
			}
		});
		dqlPopUp.add(mnuAttributes);

		mnuViewContent.setText("View Content");
		mnuViewContent.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuViewContentActionPerformed(evt);
			}
		});
		dqlPopUp.add(mnuViewContent);

		mnuViewInternal.setText("View with Internal Viewer");
		mnuViewInternal.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuViewInternalActionPerformed(evt);
			}
		});
		dqlPopUp.add(mnuViewInternal);

		mnuShowLocations.setText("Show Locations");
		mnuShowLocations.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuShowLocationsActionPerformed(evt);
			}
		});
		dqlPopUp.add(mnuShowLocations);
		dqlPopUp.add(jSeparator1);

		mnuDestroy.setText("Destroy Object");
		mnuDestroy.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuDestroyActionPerformed(evt);
			}
		});
		dqlPopUp.add(mnuDestroy);
		dqlPopUp.add(jSeparator2);

		mnuExportExcel.setText("Export Result to Excel");
		mnuExportExcel.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuExportExcelActionPerformed(evt);
			}
		});
		dqlPopUp.add(mnuExportExcel);

		mnuSetAttribute.setText("Set Attribute");
		mnuSetAttribute.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuSetAttributeActionPerformed(evt);
			}
		});
		dqlPopUp.add(mnuSetAttribute);

		mnuSetAcl.setText("Change Acl");
		mnuSetAcl.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuSetAclActionPerformed(evt);
			}
		});
		dqlPopUp.add(mnuSetAcl);

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("DQL");
		setLocationByPlatform(true);

		jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
		jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

		lblTimer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/timer.gif"))); // NOI18N
		lblTimer.setText(" ");
		jPanel1.add(lblTimer);

		jSplitPane1.setDividerLocation(244);
		jSplitPane1.setDividerSize(3);
		jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

		jScrollPane2.setFont(new java.awt.Font("Courier New", 0, 11));

		tblResult.setFont(new java.awt.Font("Courier New", 0, 12));
		tblResult.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { {}, {}, {}, {} }, new String[] {

		}));
		tblResult.setCellSelectionEnabled(true);
		tblResult.setComponentPopupMenu(dqlPopUp);
		jScrollPane2.setViewportView(tblResult);

		jSplitPane1.setLeftComponent(jScrollPane2);

		txtSQL.setColumns(20);
		txtSQL.setEditable(false);
		txtSQL.setRows(5);
		txtSQL.setWrapStyleWord(true);
		jScrollPane4.setViewportView(txtSQL);

		jSplitPane1.setRightComponent(jScrollPane4);

		final org.jdesktop.layout.GroupLayout gl_jPanel2 = new org.jdesktop.layout.GroupLayout(jPanel2);
		jPanel2.setLayout(gl_jPanel2);
		gl_jPanel2.setHorizontalGroup(gl_jPanel2.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 803, Short.MAX_VALUE));
		gl_jPanel2.setVerticalGroup(gl_jPanel2.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jSplitPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 438, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE));

		jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

		txtDQL.setColumns(20);
		txtDQL.setFont(new java.awt.Font("Courier New", 0, 13));
		txtDQL.setLineWrap(true);
		txtDQL.setRows(5);
		txtDQL.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyPressed(final java.awt.event.KeyEvent evt) {
				txtDQLKeyPressed(evt);
			}
		});
		jScrollPane1.setViewportView(txtDQL);

		jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

		jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11));
		jLabel1.setText("Query Category:");

		jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11));
		jLabel2.setText("Query Topic:");

		cmbQueryCategory.setEditable(true);
		cmbQueryCategory.addItemListener(new java.awt.event.ItemListener() {
			@Override
			public void itemStateChanged(final java.awt.event.ItemEvent evt) {
				cmbQueryCategoryItemStateChanged(evt);
			}
		});
		cmbQueryCategory.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
			@Override
			public void mouseWheelMoved(final java.awt.event.MouseWheelEvent evt) {
				cmbQueryCategoryMouseWheelMoved(evt);
			}
		});

		cmbQueries.setEditable(true);
		cmbQueries.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
			@Override
			public void mouseWheelMoved(final java.awt.event.MouseWheelEvent evt) {
				cmbQueriesMouseWheelMoved(evt);
			}
		});
		cmbQueries.addItemListener(new java.awt.event.ItemListener() {
			@Override
			public void itemStateChanged(final java.awt.event.ItemEvent evt) {
				cmbQueriesItemStateChanged(evt);
			}
		});

		cmdSaveQuery.setText("Save Query");
		cmdSaveQuery.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdSaveQueryActionPerformed(evt);
			}
		});

		jButton3.setText("Delete Query");
		jButton3.setMargin(new java.awt.Insets(2, 2, 2, 2));
		jButton3.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				jButton3ActionPerformed(evt);
			}
		});

		jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11));
		jLabel3.setText("Type:");

		jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11));
		jLabel4.setText("Attribute:");

		cmbTypeCombo.addItemListener(new java.awt.event.ItemListener() {
			@Override
			public void itemStateChanged(final java.awt.event.ItemEvent evt) {
				cmbTypeComboItemStateChanged(evt);
			}
		});

		cmbAttrCombo.addItemListener(new java.awt.event.ItemListener() {
			@Override
			public void itemStateChanged(final java.awt.event.ItemEvent evt) {
				cmbAttrComboItemStateChanged(evt);
			}
		});

		final org.jdesktop.layout.GroupLayout gl_jPanel3 = new org.jdesktop.layout.GroupLayout(jPanel3);
		jPanel3.setLayout(gl_jPanel3);
		gl_jPanel3.setHorizontalGroup(gl_jPanel3.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(gl_jPanel3.createSequentialGroup().addContainerGap().add(gl_jPanel3.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel1).add(jLabel2)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
						.add(gl_jPanel3.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false).add(cmbQueryCategory, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(cmbQueries, 0, 166, Short.MAX_VALUE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
						.add(gl_jPanel3.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false).add(cmdSaveQuery, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(jButton3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)).add(28, 28, 28)
						.add(gl_jPanel3.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel3).add(jLabel4)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(gl_jPanel3.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false).add(cmbTypeCombo, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(cmbAttrCombo, 0, 167, Short.MAX_VALUE))
						.addContainerGap(167, Short.MAX_VALUE)));

		gl_jPanel3.linkSize(new java.awt.Component[] { cmbQueries, cmbQueryCategory }, org.jdesktop.layout.GroupLayout.HORIZONTAL);

		gl_jPanel3.setVerticalGroup(gl_jPanel3.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(gl_jPanel3.createSequentialGroup().add(11, 11, 11)
						.add(gl_jPanel3.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
								.add(gl_jPanel3.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE, false).add(jLabel1).add(cmbQueryCategory, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(cmdSaveQuery, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
										.add(cmbTypeCombo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
								.add(jLabel3))
						.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(gl_jPanel3.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel2).add(cmbQueries, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
								.add(jButton3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(cmbAttrCombo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel4))
						.addContainerGap()));

		gl_jPanel3.linkSize(new java.awt.Component[] { cmbQueries, cmbQueryCategory }, org.jdesktop.layout.GroupLayout.VERTICAL);

		final org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(layout.createSequentialGroup().addContainerGap()
						.add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 803, Short.MAX_VALUE).add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 667, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 130, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1,
										org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 803, Short.MAX_VALUE))
						.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING,
				layout.createSequentialGroup().addContainerGap().add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
						.add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE).add(jPanel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
						.add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap()));
		final GridBagLayout gbl_jPanel4 = new GridBagLayout();
		gbl_jPanel4.columnWidths = new int[] { 70, 37, 0 };
		gbl_jPanel4.rowHeights = new int[] { 20, 20, 20, 0, 0 };
		gbl_jPanel4.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		gbl_jPanel4.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		jPanel4.setLayout(gbl_jPanel4);
		jButton2 = new javax.swing.JButton();

		jButton2.setText("Clear");
		jButton2.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				jButton2ActionPerformed(evt);
			}
		});
		cmdHistoryUP = new javax.swing.JButton();

		cmdHistoryUP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fi/fortica/quickclient/gui/small-up17.gif"))); // NOI18N
		cmdHistoryUP.setToolTipText("Browse DQL History");
		cmdHistoryUP.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdHistoryUPActionPerformed(evt);
			}
		});
		cmdQuery = new javax.swing.JButton();

		cmdQuery.setMnemonic('e');
		cmdQuery.setText("Execute");
		cmdQuery.setMargin(new java.awt.Insets(2, 2, 2, 2));
		cmdQuery.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(final java.awt.event.MouseEvent evt) {
				cmdQueryMouseClicked(evt);
			}
		});
		cmdQuery.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdQueryActionPerformed(evt);
			}
		});
		final GridBagConstraints gbc_cmdQuery = new GridBagConstraints();
		gbc_cmdQuery.fill = GridBagConstraints.BOTH;
		gbc_cmdQuery.insets = new Insets(0, 0, 5, 5);
		gbc_cmdQuery.gridx = 0;
		gbc_cmdQuery.gridy = 0;
		jPanel4.add(cmdQuery, gbc_cmdQuery);
		final GridBagConstraints gbc_cmdHistoryUP = new GridBagConstraints();
		gbc_cmdHistoryUP.fill = GridBagConstraints.BOTH;
		gbc_cmdHistoryUP.insets = new Insets(0, 0, 5, 0);
		gbc_cmdHistoryUP.gridx = 1;
		gbc_cmdHistoryUP.gridy = 0;
		jPanel4.add(cmdHistoryUP, gbc_cmdHistoryUP);
		final GridBagConstraints gbc_jButton2 = new GridBagConstraints();
		gbc_jButton2.fill = GridBagConstraints.BOTH;
		gbc_jButton2.insets = new Insets(0, 0, 5, 5);
		gbc_jButton2.gridx = 0;
		gbc_jButton2.gridy = 1;
		jPanel4.add(jButton2, gbc_jButton2);
		jButton1 = new javax.swing.JButton();

		jButton1.setText("History");
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});
		cmdHistoryDown1 = new javax.swing.JButton();

		cmdHistoryDown1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fi/fortica/quickclient/gui/small-down17.gif"))); // NOI18N
		cmdHistoryDown1.setToolTipText("Browse DQL History");
		cmdHistoryDown1.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdHistoryDown1ActionPerformed(evt);
			}
		});
		final GridBagConstraints gbc_cmdHistoryDown1 = new GridBagConstraints();
		gbc_cmdHistoryDown1.fill = GridBagConstraints.BOTH;
		gbc_cmdHistoryDown1.insets = new Insets(0, 0, 5, 0);
		gbc_cmdHistoryDown1.gridx = 1;
		gbc_cmdHistoryDown1.gridy = 1;
		jPanel4.add(cmdHistoryDown1, gbc_cmdHistoryDown1);
		final GridBagConstraints gbc_jButton1 = new GridBagConstraints();
		gbc_jButton1.fill = GridBagConstraints.BOTH;
		gbc_jButton1.insets = new Insets(0, 0, 5, 5);
		gbc_jButton1.gridx = 0;
		gbc_jButton1.gridy = 2;
		jPanel4.add(jButton1, gbc_jButton1);
		chkShowSQL = new javax.swing.JCheckBox();

		chkShowSQL.setText("SQL");
		chkShowSQL.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		chkShowSQL.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				chkShowSQLActionPerformed(evt);
			}
		});
		final GridBagConstraints gbc_chkShowSQL = new GridBagConstraints();
		gbc_chkShowSQL.insets = new Insets(0, 0, 5, 0);
		gbc_chkShowSQL.gridx = 1;
		gbc_chkShowSQL.gridy = 2;
		jPanel4.add(chkShowSQL, gbc_chkShowSQL);

		chckbxXml = new JCheckBox("XML");
		chckbxXml.setToolTipText("Product DQLXM resultL instead of grid");
		final GridBagConstraints gbc_chckbxXml = new GridBagConstraints();
		gbc_chckbxXml.insets = new Insets(0, 0, 0, 5);
		gbc_chckbxXml.gridx = 0;
		gbc_chckbxXml.gridy = 3;
		jPanel4.add(chckbxXml, gbc_chckbxXml);

		pack();
	}// </editor-fold>//GEN-END:initComponents

	public void initDQLList() {
		final Cursor cur = new Cursor(Cursor.WAIT_CURSOR);

		setCursor(cur);
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringComments(true);
		// DocumentBuilder builder;
		final String categoryXpath = "/queries/category/@name";
		NodeList nodes = null;
		IDfSession session = null;
		IDfSysObject obj = null;
		cmbQueryCategory.removeAllItems();
		cmbQueries.removeAllItems();
		try {
			session = smanager.getSession();
			if (usedocbase) {
				obj = (IDfSysObject) session.getObjectByQualification("dm_document where object_name  ='quickclient_queries' and owner_name = '" + session.getUser("") + "'");
				if (obj != null) {
					final ByteArrayInputStream bais = obj.getContent();
					final byte b[] = new byte[bais.available()];
					bais.read(b);
					final String content = new String(b);
					final String xmlString = content;
					final DocumentBuilder builder = factory.newDocumentBuilder();
					document = builder.parse(new InputSource(new StringReader(xmlString)));
					final XPath xpath = XPathFactory.newInstance().newXPath();
					nodes = (NodeList) xpath.evaluate(categoryXpath, document, XPathConstants.NODESET);
					for (int i = 0; i < nodes.getLength(); i++) {
						final Node node = nodes.item(i);
						final String value = node.getNodeValue();
						cmbQueryCategory.addItem(value);
					}
				} else {
				}
			} else {
				final String xmlString = Utils.readFileAsString(ConfigService.getInstance().getDqlfile());
				if (xmlString != null) {
					final DocumentBuilder builder = factory.newDocumentBuilder();
					document = builder.parse(new InputSource(new StringReader(xmlString)));
					final XPath xpath = XPathFactory.newInstance().newXPath();
					nodes = (NodeList) xpath.evaluate(categoryXpath, document, XPathConstants.NODESET);
					for (int i = 0; i < nodes.getLength(); i++) {
						final Node node = nodes.item(i);
						final String value = node.getNodeValue();
						cmbQueryCategory.addItem(value);
					}
				}
			}
		} catch (final ParserConfigurationException ex) {
			DfLogger.error(this, ex.getMessage(), null, ex);
		} catch (final XPathExpressionException ex) {
			DfLogger.error(this, ex.getMessage(), null, ex);
		} catch (final SAXException ex) {
			DfLogger.error(this, ex.getMessage(), null, ex);
		} catch (final IOException ex) {
			DfLogger.error(this, ex.getMessage(), null, ex);
		} catch (final DfException ex) {
			DfLogger.error(this, ex.getMessage(), null, ex);
		} finally {
			if (session != null) {
				smanager.releaseSession(session);

			}
			final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
			setCursor(cur2);
		}
	}

	public void initTypeCombo() {
		IDfSession session = null;
		IDfCollection col = null;
		try {
			session = smanager.getSession();
			final IDfQuery query = new DfQuery();
			query.setDQL("select name from dm_type order by name");
			col = query.execute(session, DfQuery.DF_READ_QUERY);
			while (col.next()) {
				cmbTypeCombo.addItem(col.getString("name"));
			}
		} catch (final DfException ex) {
			DfLogger.error(this, ex.getMessage(), null, ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
		} finally {
			if (col != null) {
				try {
					col.close();
				} catch (final DfException ex) {
					DfLogger.error(this, ex.getMessage(), null, ex);
					;
				}
			}
			if (session != null) {
				smanager.releaseSession(session);
			}
		}
	}

	private void jButton1ActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed

		final DQLHistory dh = new DQLHistory();
		dh.setHistory(history.getHistory());
		dh.setComponentStd(this.txtDQL);
		dh.setVisible(true);
	}// GEN-LAST:event_jButton1ActionPerformed

	private void jButton2ActionPerformed(final java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
		txtDQL.setText("");
	}

	private void jButton3ActionPerformed(final java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
	}

	private void mnuAttributesActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuAttributesActionPerformed

		String objid = "";
		objid = (String) tblResult.getValueAt(tblResult.getSelectedRow(), tblResult.getSelectedColumn());
		final AttrEditorFrame3Text attredit = new AttrEditorFrame3Text(objid);
		attredit.setVisible(true);
	}// GEN-LAST:event_mnuAttributesActionPerformed

	private void mnuCopyActionPerformed(final java.awt.event.ActionEvent evt) {
		final int rowvalues[] = tblResult.getSelectedRows();
		final int[] columns = tblResult.getSelectedColumns();
		final int selrowcount = tblResult.getSelectedRowCount();
		String val = "";
		for (int i = 0; i < selrowcount; i++) {
			for (int j = 0; j < columns.length; j++) {
				if (j > 0) {
					val = val + "\t";
				}
				val = val + (String) tblResult.getValueAt(rowvalues[i], columns[j]);
			}
			val = val + "\n";
		}
		final StringSelection data = new StringSelection(val);
		final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(data, data);

	}

	private void mnuDestroyActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuDestroyActionPerformed
		IDfSession session = null;
		final int rowvalues[] = tblResult.getSelectedRows();
		final int column = tblResult.getSelectedColumn();
		final int selrowcount = tblResult.getSelectedRowCount();
		try {
			session = smanager.getSession();

			final int answer = JOptionPane.showConfirmDialog(this, "Destroy selected object(s), Are you sure??", "Confirm", JOptionPane.YES_NO_OPTION);
			if (answer == JOptionPane.YES_OPTION) {
				for (int i = 0; i < selrowcount; i++) {
					String val = "";
					val = (String) tblResult.getValueAt(rowvalues[i], column);
					final IDfId id = new DfId(val);
					final IDfPersistentObject obj = session.getObject(id);
					obj.destroy();
				}
			}
		} catch (final DfException ex) {
			DfLogger.error(this, ex.getMessage(), null, ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
		} finally {
			if (session != null) {
				smanager.releaseSession(session);
			}
		}
	}// GEN-LAST:event_mnuDestroyActionPerformed

	private void mnuDumpActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuDumpActionPerformed
		// TODO add your handling code here:

		String val = "";
		val = (String) tblResult.getValueAt(tblResult.getSelectedRow(), tblResult.getSelectedColumn());

		final IDfId id = new DfId(val);
		final DumpFrame dumpframe = new DumpFrame();
		dumpframe.setstrID(val);
		dumpframe.setId(id);

		dumpframe.initData();
		SwingHelper.centerJFrame(dumpframe);
		dumpframe.setVisible(true);

	}// GEN-LAST:event_mnuDumpActionPerformed

	private void mnuExportExcelActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuExportExcelActionPerformed
		final String tableName = new String("queryresult" + System.currentTimeMillis());
		// tableName.append(cal.);
		final JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Documentum Export");
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		chooser.setSelectedFile(new File("queryresult.xls"));
		final int returnVal = chooser.showSaveDialog(null);
		File selFile;
		selFile = chooser.getSelectedFile();
		FileOutputStream out = null;
		try {
			final String excelFileName = selFile.getPath();
			final HSSFWorkbook myWorkBook = new HSSFWorkbook();
			final HSSFSheet mySheet = myWorkBook.createSheet();
			HSSFRow myRow = null;
			HSSFCell myCell = null;
			final Cursor cur2 = new Cursor(Cursor.WAIT_CURSOR);
			setCursor(cur2);
			final int colcount = tblResult.getColumnModel().getColumnCount();
			myRow = mySheet.createRow(0);
			for (int j = 0; j < colcount; j++) {
				myCell = myRow.createCell(j);
				final HSSFCellStyle cellStyle = myWorkBook.createCellStyle();
				final HSSFFont font = myWorkBook.createFont();
				font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				cellStyle.setFont(font);
				myCell.setCellStyle(cellStyle);
				myCell.setCellType(Cell.CELL_TYPE_STRING);
				final String colvalue = tblResult.getColumnName(j);
				myCell.setCellValue(colvalue);
			}
			for (int rowI = 0; rowI < tblResult.getRowCount(); rowI++) {
				myRow = mySheet.createRow(rowI + 1);
				for (int j = 0; j < colcount; j++) {
					myCell = myRow.createCell(j);
					myCell.setCellValue((String) tblResult.getValueAt(rowI, j));
				}
			}

			out = new FileOutputStream(excelFileName);
			myWorkBook.write(out);
			out.flush();
		} catch (final FileNotFoundException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
			DfLogger.error(this, ex.getMessage(), null, ex);
		} catch (final IOException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
			DfLogger.error(this, ex.getMessage(), null, ex);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
			final Cursor cur3 = new Cursor(Cursor.DEFAULT_CURSOR);
			setCursor(cur3);
		}
	}// GEN-LAST:event_mnuExportExcelActionPerformed

	private void mnuSetAclActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuSetAclActionPerformed
		final String newOwner = "";
		final AclBrowserData x = new AclBrowserData();
		final int rowvalues[] = tblResult.getSelectedRows();
		final int column = tblResult.getSelectedColumn();
		final int selrowcount = tblResult.getSelectedRowCount();
		final Vector myVector = new Vector();
		for (int i = 0; i < selrowcount; i++) {
			String val = "";
			val = (String) tblResult.getValueAt(rowvalues[i], column);
			myVector.add(val);
		}
		final ActionListener al = new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				// //System.out.println(e);
				// //System.out.println(e.getSource().toString());
				IDfSession session = null;
				try {
					session = smanager.getSession();
					for (int i = 0; i < myVector.size(); i++) {
						final String objid = (String) myVector.get(i);
						final IDfId id = new DfId(objid);
						final IDfSysObject obj = (IDfSysObject) session.getObject(id);
						obj.setString("acl_domain", x.getAclDomain());
						obj.setString("acl_name", x.getAclName());
						if (obj.isCheckedOut()) {
							obj.saveLock();
						} else {
							obj.save();
						}
					}
				} catch (final DfException ex) {
					DfLogger.error(this, ex.getMessage(), null, ex);

					JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
				} finally {
					if (session != null) {
						smanager.releaseSession(session);
					}

				}
			}
		};
		final ACLBrowserFrame frame = new ACLBrowserFrame(al, x, true);
		frame.setVisible(true);

	}// GEN-LAST:event_mnuSetAclActionPerformed

	private void mnuSetAttributeActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuSetAttributeActionPerformed
		final int rowvalues[] = tblResult.getSelectedRows();
		final int column = tblResult.getSelectedColumn();
		final int selrowcount = tblResult.getSelectedRowCount();

		IDfSession session = null;
		IDfPersistentObject obj = null;
		final String attrName = JOptionPane.showInputDialog("Attribute to set");
		final String attrValue = JOptionPane.showInputDialog("Value?");
		try {
			session = smanager.getSession();
			for (int i = 0; i < selrowcount; i++) {
				String val = "";
				val = (String) tblResult.getValueAt(rowvalues[i], column);
				obj = session.getObject(new DfId(val));
				obj.setString(attrName, attrValue);
				obj.save();
			}
		} catch (final DfException ex) {
			DfLogger.error(this, ex.getMessage(), null, ex);

			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);

		} finally {
			if (session != null) {
				smanager.releaseSession(session);
			}
		}
	}// GEN-LAST:event_mnuSetAttributeActionPerformed

	private void mnuShowLocationsActionPerformed(final java.awt.event.ActionEvent evt) {
		IDfSession session = null;
		final int rowvalues[] = tblResult.getSelectedRows();
		final int column = tblResult.getSelectedColumn();
		final int selrowcount = tblResult.getSelectedRowCount();
		try {
			session = smanager.getSession();
			for (int i = 0; i < selrowcount; i++) {
				String val = "";
				val = (String) tblResult.getValueAt(rowvalues[i], column);
				final IDfId id = new DfId(val);
				final IDfPersistentObject obj = session.getObject(id);
				final int valuecount = obj.getValueCount("i_folder_id");
				final ArrayList<String> locations = new ArrayList<String>();
				for (int j = 0; j < valuecount; j++) {
					final String folderid = obj.getRepeatingString("i_folder_id", j);
					final IDfFolder folder = (IDfFolder) session.getObject(new DfId(folderid));
					final int xx = folder.getFolderPathCount();
					for (int k = 0; k < xx; k++) {
						final String location = folder.getFolderPath(k);
						locations.add(location);
					}
				}
				System.out.println(locations);
				final TxtPopup popup = new TxtPopup(400, 400, locations.toString());
				popup.setVisible(true);
				SwingHelper.centerJFrame(popup);

			}

		} catch (final DfException ex) {
			DfLogger.error(this, ex.getMessage(), null, ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
		} finally {
			if (session != null) {
				smanager.releaseSession(session);
			}
		}
	}

	private void mnuViewContentActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuViewContentActionPerformed

		try {
			final Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
			setCursor(cur);

			String val = "";
			val = (String) tblResult.getValueAt(tblResult.getSelectedRow(), tblResult.getSelectedColumn());

			final IDfId id = new DfId(val);
			FileUtils.viewFile(id.toString());

		} finally {

			final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
			setCursor(cur2);
		}
	}// GEN-LAST:event_mnuViewContentActionPerformed

	protected void mnuViewInternalActionPerformed(final ActionEvent evt) {

		final String val = (String) tblResult.getValueAt(tblResult.getSelectedRow(), tblResult.getSelectedColumn());
		final Vector v = new Vector();
		v.add(val);
		final ViewInternalAction vi = new ViewInternalAction();
		vi.setIdList(v);
		try {
			vi.execute();
		} catch (final QCActionException e) {
			DfLogger.error(this, "mnuViewInternalActionPerformed", null, e);
			SwingHelper.showErrorMessage("error...", e.getMessage());
		}
	}

	void setDql(final String string) {
		txtDQL.setText(string);
	}

	private void txtDQLKeyPressed(final java.awt.event.KeyEvent evt) {
		if (evt.getKeyCode() == 10) {
			final int pos = this.txtDQL.getCaretPosition();
			String query = this.txtDQL.getText();
			query = query.replaceAll("[\r\n]", "");
			this.txtDQL.setText(query);
			if (this.chckbxXml.isSelected() == false) {
				this.executeQuery();
			} else {
				this.executeXMLQuery();
			}
			this.txtDQL.setCaretPosition(pos);
		}
	}
}
