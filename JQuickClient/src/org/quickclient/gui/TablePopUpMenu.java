/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quickclient.gui;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.quickclient.actions.IQuickAction;
import org.quickclient.actions.QCActionException;
import org.quickclient.classes.ActionExecutor;
import org.quickclient.classes.ConfigService;
import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.DokuData;
import org.quickclient.classes.IQuickExtension;
import org.quickclient.classes.JMenuItemExt;
import org.quickclient.classes.PluginConfig;
import org.quickclient.classes.PluginHelper;
import org.quickclient.classes.QCFunction;
import org.quickclient.classes.ScriptConfig;
import org.quickclient.classes.ScriptHelper;
import org.quickclient.classes.SwingHelper;
import org.quickclient.classes.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfType;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLogger;
import com.documentum.fc.common.IDfId;

/**
 *
 * @author Administrator
 */
public class TablePopUpMenu extends JPopupMenu {

	private static final String CLASS = "class";
	private JTable table;
	private final DocuSessionManager smanager;
	private JMenuItem mnuOpenRelated;
	private transient Logger log = Logger.getLogger(TablePopUpMenu.class);

	private javax.swing.JMenu Render;

	private javax.swing.JMenu Sign;

	private javax.swing.JMenu Tools;

	private javax.swing.JMenu jMenuLifeCycle;

	private javax.swing.JMenu jMenuSubscriptions;

	private javax.swing.JSeparator jSeparator1;

	private javax.swing.JSeparator jSeparator2;

	private javax.swing.JSeparator jSeparator3;
	private javax.swing.JMenuItem mnuAddDigitalSignature;
	private javax.swing.JMenuItem mnuAddElectronicSignature;
	private javax.swing.JMenuItem mnuAttachLC;
	private javax.swing.JMenuItem mnuSubscribe;
	private javax.swing.JMenuItem mnuUnSubscribe;
	private javax.swing.JMenuItem mnuAttributeEdtior;
	private javax.swing.JMenuItem mnuCancelCheckOut;
	private javax.swing.JMenuItem mnuChangeACL;
	private javax.swing.JMenuItem mnuChangeOwner;
	private javax.swing.JMenuItem mnuChangeType;
	private javax.swing.JMenuItem mnuCheckIn;
	private javax.swing.JMenuItem mnuCheckOut;
	private javax.swing.JMenu mnuCustom;
	private javax.swing.JMenu mnuScripts;
	private javax.swing.JMenu mnuVDM;
	private javax.swing.JMenuItem mnuConvertToVDM;
	private javax.swing.JMenuItem mnuConvertToNONVDM;
	private javax.swing.JMenuItem mnuVDMEdit;
	private javax.swing.JMenu mnuMTS;
	private javax.swing.JMenuItem mnuTransform;
	private javax.swing.JMenuItem mnuMTSResetRenditions;
	private javax.swing.JMenu mnuWorkflow;
	private javax.swing.JMenuItem mnuStartWorkflow;
	private javax.swing.JMenuItem mnuQuickflow;
	private javax.swing.JMenuItem mnuQueue;
	private javax.swing.JMenu mnuCollaboration;
	private javax.swing.JMenuItem mnuCollaborationGovern;
	private javax.swing.JMenuItem mnuDemote;
	private javax.swing.JMenuItem mnuDestroy;
	private javax.swing.JMenuItem mnuEdit;
	private javax.swing.JMenuItem mnuExport;
	private javax.swing.JMenuItem mnuImportRendition;
	private javax.swing.JMenuItem mnuPromote;
	private javax.swing.JMenuItem mnuProperties;
	private javax.swing.JMenuItem mnuRemove;
	private javax.swing.JMenuItem mnuRename;
	private javax.swing.JMenuItem mnuRenderHTML;
	private javax.swing.JMenuItem mnuRenderPDF;
	private javax.swing.JMenuItem mnuResume;
	private javax.swing.JMenuItem mnuShowDump;
	private javax.swing.JMenuItem mnuShowFileSystemPath;

	private javax.swing.JMenuItem mnuSuspend;
	private javax.swing.JMenuItem mnuUnlock;
	private javax.swing.JMenuItem mnuSetFile;
	private javax.swing.JMenuItem mnuVerifyAudit;
	private javax.swing.JMenuItem mnuVerifySignature;
	private javax.swing.JMenuItem mnuView;
	private javax.swing.JMenuItem mnuViewText;
	private javax.swing.JMenuItem mnuAddUserDeletePermit;

	public TablePopUpMenu() {
		// initMenu();
		initMenuXML();
		smanager = DocuSessionManager.getInstance();
		updateCustomMenu();
	}

	private void addMenuToParent(final TablePopUpMenu tablePopUpMenu, final JMenu parentmenu, final Node node) {
		final QCFunction func = new QCFunction();
		final NamedNodeMap attrs = node.getAttributes();

		final Node tooltipNode = attrs.getNamedItem("tooltip");
		if (tooltipNode != null) {
			final String tooltip = tooltipNode.getTextContent();
			func.setToolTipText(tooltip);
		}

		final Node sepNode = attrs.getNamedItem("separator");
		if (sepNode != null) {
			if (sepNode.getTextContent().equalsIgnoreCase("true")) {
				if (tablePopUpMenu != null) {
					tablePopUpMenu.add(new JSeparator());
				} else {
					parentmenu.add(new JSeparator());
				}
			}
			return;
		}

		final Node classnameNode = attrs.getNamedItem(CLASS);
		if (classnameNode != null) {
			final String classname = classnameNode.getTextContent();
			func.setImplementingClass(classname);
		}

		final Node labelNode = attrs.getNamedItem("label");
		if (labelNode != null) {
			final String label = labelNode.getTextContent();
			func.setText(label);
		}

		final Node iconNode = attrs.getNamedItem("icon");
		if (iconNode != null) {
			final String iconfile = iconNode.getTextContent();
			final URL imageURL = TablePopUpMenu.class.getResource(iconfile);
			System.out.println("imageURL:" + imageURL);
			if (imageURL != null) {
				final Icon icon = new ImageIcon(imageURL);
				func.setIcon(icon);
			}
		}

		func.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				final QCFunction qf = (QCFunction) arg0.getSource();
				final Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
				getTable().getParent().setCursor(cur);

				final Vector<String> objids = getIDListfromTable();
				final ClassLoader classLoader = TablePopUpMenu.class.getClassLoader();
				try {
					final Class aClass = classLoader.loadClass(qf.getImplementingClass());
					final IQuickAction action = (IQuickAction) aClass.newInstance();
					action.setTable(table);
					action.setIdList(objids);
					action.execute();
				} catch (final QCActionException | InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
					DfLogger.error(this, ex.getMessage(), null, ex);
					SwingHelper.showErrorMessage("Error occurred!", ex.getMessage());
				}
				final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
				getTable().getParent().setCursor(cur2);
				final QCFunction fuck = (QCFunction) arg0.getSource();
				System.out.println(fuck.getText());
			}
		});

		if (tablePopUpMenu != null) {
			tablePopUpMenu.add(func);
		} else {
			parentmenu.add(func);
		}

	}

	public String getIDfromTable() {
		final int row = table.getSelectedRow();

		final DefaultTableModel model = (DefaultTableModel) table.getModel();
		final Vector v = (Vector) model.getDataVector().elementAt(row);
		// //System.out.println(v);
		// //System.out.println("last element" + v.lastElement());

		final DokuData d = (DokuData) v.lastElement();
		final String objid = d.getObjID();
		return objid;
	}

	public Vector<String> getIDListfromTable() {
		final int[] row = table.getSelectedRows();
		final Vector idVector = new Vector();
		for (final int element : row) {
			final DefaultTableModel model = (DefaultTableModel) table.getModel();
			final Vector v = (Vector) model.getDataVector().elementAt(element);
			final DokuData d = (DokuData) v.lastElement();
			final String objid = d.getObjID();
			idVector.add(objid);
		}
		return idVector;
	}

	public JTable getTable() {
		return table;
	}

	private void initMenu() {
		mnuView = new javax.swing.JMenuItem();
		mnuViewText = new javax.swing.JMenuItem();
		mnuSetFile = new javax.swing.JMenuItem();
		mnuEdit = new javax.swing.JMenuItem();
		mnuExport = new javax.swing.JMenuItem();
		mnuOpenRelated = new javax.swing.JMenuItem();
		jMenuLifeCycle = new javax.swing.JMenu();
		jMenuSubscriptions = new javax.swing.JMenu();

		mnuSubscribe = new javax.swing.JMenuItem();
		mnuUnSubscribe = new javax.swing.JMenuItem();

		mnuAttachLC = new javax.swing.JMenuItem();
		mnuPromote = new javax.swing.JMenuItem();
		mnuDemote = new javax.swing.JMenuItem();
		mnuSuspend = new javax.swing.JMenuItem();
		mnuResume = new javax.swing.JMenuItem();
		mnuRemove = new javax.swing.JMenuItem();
		mnuCheckOut = new javax.swing.JMenuItem();
		mnuCheckIn = new javax.swing.JMenuItem();
		mnuCancelCheckOut = new javax.swing.JMenuItem();
		mnuUnlock = new javax.swing.JMenuItem();
		mnuCustom = new javax.swing.JMenu();
		mnuScripts = new javax.swing.JMenu();
		// VDM
		mnuVDM = new javax.swing.JMenu();
		mnuConvertToVDM = new javax.swing.JMenuItem();
		mnuConvertToNONVDM = new javax.swing.JMenuItem();
		mnuVDMEdit = new javax.swing.JMenuItem();

		// MTS
		mnuMTS = new javax.swing.JMenu();
		mnuTransform = new javax.swing.JMenuItem();
		mnuMTSResetRenditions = new javax.swing.JMenuItem();

		// Workflow
		mnuWorkflow = new javax.swing.JMenu();
		mnuStartWorkflow = new javax.swing.JMenuItem();
		mnuQuickflow = new javax.swing.JMenuItem();
		mnuQueue = new javax.swing.JMenuItem();
		// Collabortion
		mnuCollaboration = new javax.swing.JMenu();
		mnuCollaborationGovern = new javax.swing.JMenuItem();

		Sign = new javax.swing.JMenu();
		mnuAddElectronicSignature = new javax.swing.JMenuItem();
		mnuAddDigitalSignature = new javax.swing.JMenuItem();
		jSeparator2 = new javax.swing.JSeparator();
		mnuVerifySignature = new javax.swing.JMenuItem();
		mnuVerifyAudit = new javax.swing.JMenuItem();
		Render = new javax.swing.JMenu();
		mnuRenderPDF = new javax.swing.JMenuItem();
		mnuRenderHTML = new javax.swing.JMenuItem();
		mnuImportRendition = new javax.swing.JMenuItem();
		jSeparator1 = new javax.swing.JSeparator();
		mnuDestroy = new javax.swing.JMenuItem();

		Tools = new javax.swing.JMenu();
		mnuChangeACL = new javax.swing.JMenuItem();
		mnuChangeOwner = new javax.swing.JMenuItem();
		mnuShowFileSystemPath = new javax.swing.JMenuItem();
		mnuChangeType = new javax.swing.JMenuItem();
		mnuShowDump = new javax.swing.JMenuItem();
		mnuAttributeEdtior = new javax.swing.JMenuItem();
		mnuRename = new javax.swing.JMenuItem();
		jSeparator3 = new javax.swing.JSeparator();
		mnuProperties = new javax.swing.JMenuItem();
		mnuAddUserDeletePermit = new javax.swing.JMenuItem();

		mnuView.setMnemonic('v');
		mnuView.setText("View");
		mnuView.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				final Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
				getTable().getParent().setCursor(cur);
				final ActionExecutor ae = new ActionExecutor();
				final Vector objids = getIDListfromTable();
				ae.viewAction(objids);
				final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
				getTable().getParent().setCursor(cur2);
			}

			private void mnuAttributeEdtiorMouseReleased(final java.awt.event.MouseEvent evt) {
			}
		});
		this.add(mnuView);

		mnuViewText.setText("View as Text");
		mnuViewText.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				final Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
				getTable().getParent().setCursor(cur);
				final ActionExecutor ae = new ActionExecutor();
				final Vector objids = getIDListfromTable();
				ae.viewTextAction(objids);
				final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
				getTable().getParent().setCursor(cur2);
			}

			private void mnuAttributeEdtiorMouseReleased(final java.awt.event.MouseEvent evt) {
			}
		});
		this.add(mnuViewText);

		mnuEdit.setMnemonic('e');
		mnuEdit.setText("Edit");
		mnuEdit.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				final ActionExecutor ae = new ActionExecutor();
				final String objid = getIDfromTable();
				ae.editAction(objid, table);
			}
		});
		this.add(mnuEdit);

		mnuExport.setText("Export");
		mnuExport.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				final ActionExecutor ae = new ActionExecutor();
				final String objid = getIDfromTable();
				ae.exportFileAction(objid);
			}
		});
		this.add(mnuExport);

		mnuOpenRelated.setText("Open Related Documents");
		mnuOpenRelated.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final java.awt.event.ActionEvent e) {
				final ActionExecutor ae = new ActionExecutor();
				final String objid = getIDfromTable();
				ae.openRelated(objid);
			}
		});
		this.add(mnuOpenRelated);

		jMenuLifeCycle.setText("Lifecycle");

		mnuAttachLC.setText("Attach");
		mnuAttachLC.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				final ActionExecutor ae = new ActionExecutor();
				final String objid = getIDfromTable();
				ae.attachLCActionAction(objid);

			}
		});
		jMenuLifeCycle.add(mnuAttachLC);

		mnuPromote.setText("Promote");
		mnuPromote.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				final ActionExecutor ae = new ActionExecutor();
				final String objid = getIDfromTable();
				ae.promoteAction(objid);
			}
		});
		jMenuLifeCycle.add(mnuPromote);

		mnuDemote.setText("Demote");
		mnuDemote.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				final ActionExecutor ae = new ActionExecutor();
				final String objid = getIDfromTable();
				ae.demoteAction(objid);
			}
		});
		jMenuLifeCycle.add(mnuDemote);

		mnuSuspend.setText("Suspend");
		mnuSuspend.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				final ActionExecutor ae = new ActionExecutor();
				final String objid = getIDfromTable();
				ae.suspendLCAction(objid);

			}
		});
		jMenuLifeCycle.add(mnuSuspend);

		mnuResume.setText("Resume");
		mnuResume.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				final ActionExecutor ae = new ActionExecutor();
				final String objid = getIDfromTable();
				ae.resumeLCAction(objid);
			}
		});
		jMenuLifeCycle.add(mnuResume);

		mnuRemove.setText("Remove");
		mnuRemove.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				final ActionExecutor ae = new ActionExecutor();
				final String objid = getIDfromTable();
				ae.removeLCAction(objid);

			}
		});
		jMenuLifeCycle.add(mnuRemove);

		this.add(jMenuLifeCycle);

		// COllaboration
		mnuCollaboration.setText("Collaboration");

		mnuCollaborationGovern.setText("Govern");
		mnuCollaborationGovern.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				final ActionExecutor ae = new ActionExecutor();
				final Vector objids = getIDListfromTable();
				ae.govern(objids);
			}
		});

		// Workflow

		mnuWorkflow.setText("Workflow");
		mnuStartWorkflow.setText("Start Workflow");
		mnuStartWorkflow.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				final ActionExecutor ae = new ActionExecutor();
				final String objid = getIDfromTable();
				ae.startWorkflow(objid);
			}
		});
		mnuWorkflow.add(mnuStartWorkflow);

		mnuQuickflow.setText("Quickflow");
		mnuQuickflow.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				final ActionExecutor ae = new ActionExecutor();
				final Vector objids = getIDListfromTable();
				ae.quickflow(objids);
			}
		});
		mnuWorkflow.add(mnuQuickflow);

		mnuQueue.setText("Queue to User");
		mnuQueue.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				final ActionExecutor ae = new ActionExecutor();
				final String objid = getIDfromTable();
				ae.queueToUser(objid);
			}
		});
		mnuWorkflow.add(mnuQueue);

		this.add(mnuWorkflow);

		// MTS
		mnuMTS.setText("MTS");
		mnuTransform.setText("Transform");
		mnuTransform.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				final ActionExecutor ae = new ActionExecutor();
				final String objid = getIDfromTable();
				ae.transform(objid);
			}
		});
		mnuMTSResetRenditions.setText("Reset Renditions");
		mnuMTSResetRenditions.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				final ActionExecutor ae = new ActionExecutor();
				final Vector objidlist = getIDListfromTable();
				ae.resetrenditions(objidlist);
			}
		});
		mnuMTS.add(mnuTransform);
		mnuMTS.add(mnuMTSResetRenditions);
		this.add(mnuMTS);

		// VDM
		mnuVDM.setText("VDM");

		mnuConvertToVDM.setText("Convert to VDM");
		mnuConvertToVDM.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				final ActionExecutor ae = new ActionExecutor();
				final String objid = getIDfromTable();
				ae.converttovdm(objid);
			}
		});
		mnuVDM.add(mnuConvertToVDM);

		mnuConvertToNONVDM.setText("Convert to Normal Document");
		mnuConvertToNONVDM.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				final Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
				getTable().getParent().setCursor(cur);

				final ActionExecutor ae = new ActionExecutor();
				final String objid = getIDfromTable();
				ae.converttononvdm(objid);
				final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
				getTable().getParent().setCursor(cur2);
			}
		});
		mnuVDM.add(mnuConvertToNONVDM);

		mnuVDMEdit.setText("Edit VDM");
		mnuVDMEdit.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				final ActionExecutor ae = new ActionExecutor();
				final String objid = getIDfromTable();
				ae.editvdm(objid);
			}
		});

		mnuVDM.add(mnuVDMEdit);
		this.add(mnuVDM);

		jMenuSubscriptions.setText("Subscriptions");
		mnuSubscribe.setText("Subscribe");
		mnuSubscribe.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				final Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
				getTable().getParent().setCursor(cur);
				final ActionExecutor ae = new ActionExecutor();
				final String objid = getIDfromTable();
				ae.subscribe(objid);
				final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
				getTable().getParent().setCursor(cur2);
			}
		});
		jMenuSubscriptions.add(mnuSubscribe);

		mnuUnSubscribe.setText("Unsubscribe");
		mnuUnSubscribe.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				final Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
				getTable().getParent().setCursor(cur);
				final ActionExecutor ae = new ActionExecutor();
				final String objid = getIDfromTable();
				ae.unsubscribe(objid);
				final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
				getTable().getParent().setCursor(cur2);
			}
		});
		jMenuSubscriptions.add(mnuUnSubscribe);

		this.add(jMenuSubscriptions);

		mnuCheckOut.setText("Checkout");
		mnuCheckOut.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				final Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
				getTable().getParent().setCursor(cur);
				final ActionExecutor ae = new ActionExecutor();
				final String objid = getIDfromTable();
				ae.checkOutAction(objid, table);
				final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
				getTable().getParent().setCursor(cur2);
			}
		});
		this.add(mnuCheckOut);

		mnuCheckIn.setText("Check in");
		mnuCheckIn.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				final ActionExecutor ae = new ActionExecutor();
				final String objid = getIDfromTable();
				ae.checkInAction(objid, table);
			}
		});
		this.add(mnuCheckIn);

		mnuCancelCheckOut.setText("Cancel Checkout");
		mnuCancelCheckOut.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				final Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
				getTable().getParent().setCursor(cur);
				final ActionExecutor ae = new ActionExecutor();
				final String objid = getIDfromTable();
				ae.cancelCheckOutAction(objid, table);
				final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
				getTable().getParent().setCursor(cur2);
			}
		});
		this.add(mnuCancelCheckOut);

		mnuUnlock.setText("Unlock");
		mnuUnlock.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				final Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
				getTable().getParent().setCursor(cur);
				final ActionExecutor ae = new ActionExecutor();
				final String objid = getIDfromTable();
				ae.unlockObjAction(objid);
				final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
				getTable().getParent().setCursor(cur2);
			}
		});
		this.add(mnuUnlock);

		mnuCustom.setText("Plugins");
		this.add(mnuCustom);
		mnuScripts.setText("Scripts");
		this.add(mnuScripts);

		Sign.setText("Signing");

		mnuAddElectronicSignature.setText("Add Electronc Signature");
		mnuAddElectronicSignature.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				final ActionExecutor ae = new ActionExecutor();
				final String objid = getIDfromTable();
				ae.addElectronicSignatureAction(objid);

			}
		});
		Sign.add(mnuAddElectronicSignature);

		mnuAddDigitalSignature.setText("Add Digital Signature");
		mnuAddDigitalSignature.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				final ActionExecutor ae = new ActionExecutor();
				final String objid = getIDfromTable();
				ae.addDigitalSignatureAction(objid);

			}
		});
		Sign.add(mnuAddDigitalSignature);
		Sign.add(jSeparator2);

		mnuVerifySignature.setText("Verify Signature");
		mnuVerifySignature.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				final ActionExecutor ae = new ActionExecutor();
				final String objid = getIDfromTable();
				ae.verifySignatureAction(objid);

			}
		});
		Sign.add(mnuVerifySignature);

		mnuVerifyAudit.setInheritsPopupMenu(true);
		mnuVerifyAudit.setLabel("Verify Audit");
		mnuVerifyAudit.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				final ActionExecutor ae = new ActionExecutor();
				final String objid = getIDfromTable();
				ae.verifyAuditAction(objid);

			}
		});
		Sign.add(mnuVerifyAudit);

		this.add(Sign);

		Render.setText("Add Rendition");

		mnuRenderPDF.setText("Render as PDF");
		mnuRenderPDF.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				final Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
				getTable().getParent().setCursor(cur);
				final ActionExecutor ae = new ActionExecutor();
				final String objid = getIDfromTable();
				ae.renderAsPDFAction(objid);
				final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
				getTable().getParent().setCursor(cur2);
			}
		});
		Render.add(mnuRenderPDF);

		mnuRenderHTML.setText("Render as HTML");
		mnuRenderHTML.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				final Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
				getTable().getParent().setCursor(cur);
				final ActionExecutor ae = new ActionExecutor();
				final String objid = getIDfromTable();
				ae.renderHTMLAction(objid);
				final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
				getTable().getParent().setCursor(cur2);
			}
		});
		Render.add(mnuRenderHTML);

		mnuImportRendition.setText("Import PDF Rendition");
		mnuImportRendition.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				final ActionExecutor ae = new ActionExecutor();
				final String objid = getIDfromTable();
				ae.importRendtionAction(objid);

			}
		});
		Render.add(mnuImportRendition);

		this.add(Render);
		this.add(jSeparator1);

		mnuDestroy.setText("Delete");
		mnuDestroy.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				final Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
				getTable().getParent().setCursor(cur);

				final ActionExecutor ae = new ActionExecutor();
				final Vector objids = getIDListfromTable();
				final int answer = JOptionPane.showConfirmDialog(null, "Destroy selected object(s), Are you sure??", "Confirm", JOptionPane.YES_NO_OPTION);
				if (answer == JOptionPane.YES_OPTION) {
					final boolean success = ae.destroyAction(objids);
					if (success) {
						removeFromTable(objids);
					}
				}
				final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
				getTable().getParent().setCursor(cur2);
			}
		});
		this.add(mnuDestroy);

		Tools.setText("Tools");

		mnuChangeACL.setText("Change Permission Set");
		mnuChangeACL.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				final ActionExecutor ae = new ActionExecutor();
				final Vector objids = getIDListfromTable();
				ae.changeACLAction(objids);
			}
		});
		Tools.add(mnuChangeACL);

		mnuAddUserDeletePermit.setText("Get Delete Permit");
		mnuAddUserDeletePermit.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				final ActionExecutor ae = new ActionExecutor();
				final Vector objids = getIDListfromTable();
				ae.giveDeletePermitForUser(objids);
			}
		});
		Tools.add(mnuAddUserDeletePermit);

		mnuChangeOwner.setText("Change Owner");
		mnuChangeOwner.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				final ActionExecutor ae = new ActionExecutor();
				final String objid = getIDfromTable();
				ae.changeOwnerAction(objid);

			}
		});
		Tools.add(mnuChangeOwner);

		mnuShowFileSystemPath.setText("Show Filesystem Path");
		mnuShowFileSystemPath.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				final ActionExecutor ae = new ActionExecutor();
				final String objid = getIDfromTable();
				ae.showFileSystemPathAction(objid);

			}
		});
		Tools.add(mnuShowFileSystemPath);

		mnuChangeType.setText("Change Object Type");
		mnuChangeType.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				final ActionExecutor ae = new ActionExecutor();
				final String objid = getIDfromTable();
				ae.changeObjectTypeAction(objid);

			}
		});
		Tools.add(mnuChangeType);

		mnuSetFile.setText("Set File");
		mnuSetFile.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				final ActionExecutor ae = new ActionExecutor();
				final String objid = getIDfromTable();
				ae.SetFile(objid);

			}
		});

		Tools.add(mnuSetFile);

		mnuShowDump.setText("Show Dump");
		mnuShowDump.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				final ActionExecutor ae = new ActionExecutor();
				final String objid = getIDfromTable();
				ae.showDumpWindowAction(objid);

			}
		});

		Tools.add(mnuShowDump);

		mnuAttributeEdtior.setText("Attribute Editor");
		mnuAttributeEdtior.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				final ActionExecutor ae = new ActionExecutor();
				final String objid = getIDfromTable();
				ae.attributeEditorAction(objid);

			}
		});

		Tools.add(mnuAttributeEdtior);

		this.add(Tools);

		mnuRename.setText("Rename");
		mnuRename.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				final ActionExecutor ae = new ActionExecutor();
				final String objid = getIDfromTable();
				ae.renameAction(objid);

			}
		});
		this.add(mnuRename);
		this.add(jSeparator3);

		mnuProperties.setText("Properties");
		mnuProperties.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {

			}
		});
		this.add(mnuProperties);
	}

	private void initMenuXML() {
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringComments(true);
		final String funcpath = "/menu/contextmenuitem";

		try {
			final String xmlString = Utils.readFileAsString("contextmenu.xml");
			final DocumentBuilder builder = factory.newDocumentBuilder();
			final Document document = builder.parse(new InputSource(new StringReader(xmlString)));
			final XPath xpath = XPathFactory.newInstance().newXPath();
			final NodeList nodes = (NodeList) xpath.evaluate(funcpath, document, XPathConstants.NODESET);
			for (int i = 0; i < nodes.getLength(); i++) {
				final Node node = nodes.item(i);
				final NodeList childnodes = node.getChildNodes();
				if (childnodes.getLength() > 0) {
					System.out.println(childnodes.getLength());
					final JMenu menu = new JMenu(node.getAttributes().getNamedItem("label").getTextContent());
					this.add(menu);
					for (int j = 0; j < childnodes.getLength(); j++) {
						final Node childNode = childnodes.item(j);
						if (childNode.getNodeName().equals("contextmenuitem")) {
							addMenuToParent(null, menu, childnodes.item(j));
						}
					}
				} else {
					addMenuToParent(this, null, node);
				}
			}
		} catch (final ParserConfigurationException | SAXException | XPathExpressionException | IOException e) {
			e.printStackTrace();
		}
	}

	public void removeFromTable(final Vector v) {
		final DefaultTableModel model = (DefaultTableModel) table.getModel();

		for (int i = 0; i < v.size(); i++) {
			final String id = (String) v.get(i);
			final int modelsize = model.getRowCount();
			for (int j = 0; j < modelsize; j++) {
				final Vector r = (Vector) model.getDataVector().elementAt(j);
				final DokuData d = (DokuData) r.lastElement();
				final String iidee = d.getObjID();
				if (iidee.equals(id)) {
					model.removeRow(j);
					break;
				}
			}
			table.setModel(model);
			table.validate();
		}
	}

	public void setTable(final JTable table) {
		this.table = table;
	}

	public void updateCustomMenu() {
		IDfCollection extcol = null;

		// update filesystem scrtips
		final ConfigService cs = ConfigService.getInstance();
		final ScriptHelper h = cs.getScripthelper();
		final PluginHelper p = cs.getPluginhelper();
		if (h != null) {
			final ArrayList list = h.getScripts();
			for (int i = 0; i < list.size(); i++) {
				final ScriptConfig config = (ScriptConfig) list.get(i);
				final JMenuItemExt mnuNew = new JMenuItemExt();

				mnuNew.setToolTipText("W");
				mnuNew.setName("fsscript" + String.valueOf(i));
				mnuNew.setText(config.getMenuName());
				mnuNew.setImplementation("filegroovy");
				mnuNew.setScriptPath(config.getFilePath());
				mnuScripts.add(mnuNew);
				mnuNew.addActionListener(new java.awt.event.ActionListener() {

					@Override
					public void actionPerformed(final ActionEvent e) {
						final JMenuItemExt menu = (JMenuItemExt) e.getSource();
						// System.out.println(menu.getImplementation());
						// System.out.println(menu.getScriptPath());
						final ActionExecutor ae = new ActionExecutor();
						final Vector objids = getIDListfromTable();
						final String filename = menu.getScriptPath();
						final File f = new File(filename);
						ae.runGroovy(objids, f);
					}
				});

			}
		} else {
			// System.out.println("no filesystem scripts found");
		}

		if (p != null) {
			final ArrayList list = p.getPlugins();
			for (int i = 0; i < list.size(); i++) {
				final PluginConfig config = (PluginConfig) list.get(i);
				final JMenuItemExt mnuNew = new JMenuItemExt();

				mnuNew.setToolTipText("W");
				mnuNew.setName("fsplugin" + String.valueOf(i));
				mnuNew.setText(config.getMenuName());
				mnuNew.setImplementation("fileplugin");
				mnuNew.setClassName(config.getClassName());
				mnuNew.setScriptPath(config.getFilePath());
				mnuCustom.add(mnuNew);
				mnuNew.addActionListener(new java.awt.event.ActionListener() {

					@Override
					public void actionPerformed(final ActionEvent e) {
						try {
							final JMenuItemExt menu = (JMenuItemExt) e.getSource();
							// System.out.println(menu.getImplementation());
							// System.out.println(menu.getScriptPath());
							final ActionExecutor ae = new ActionExecutor();
							final Vector objids = getIDListfromTable();
							final String filepath = menu.getScriptPath();
							final File f = new File(filepath);

							final IDfSession sess = smanager.getSession();
							final String className = menu.getClassName();
							// System.out.println(className);
							final URL[] urls = new URL[1];
							urls[0] = f.toURL();
							final URLClassLoader cLoad = new URLClassLoader(urls);
							final Class customClass = cLoad.loadClass(className);
							final IQuickExtension x = (IQuickExtension) customClass.newInstance();
							final String objid = getIDfromTable();
							final IDfId id = new DfId(objid);
							final IDfSysObject obj = (IDfSysObject) sess.getObject(id);
							x.setObj(obj);
							x.setSessionManager(smanager);
							x.execute();
						} catch (final MalformedURLException | DfException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
							log.error(ex);
						}

					}
				});

			}
		} else {
			// System.out.println("no filesystem plugins found");
		}

		final IDfQuery extQuery = new DfQuery();
		extQuery.setDQL("select object_name, r_object_id, class, menu_name, menu_tooltip, implementation from quick_extension");
		IDfSession session = null;
		try {
			session = smanager.getSession();
			if (session == null) {
				return;
			}
			final IDfType type = session.getType("quick_extension");
			if (type != null) {
				extcol = extQuery.execute(session, DfQuery.DF_READ_QUERY);
				while (extcol.next()) {
					final String objName = extcol.getString("object_name");
					final JMenuItemExt mnuNew = new JMenuItemExt();

					mnuNew.setToolTipText(extcol.getString("menu_tooltip"));
					// mnuNew.setName(extcol.getString("r_object_id") + "|" +
					// extcol.getString("class"));
					mnuNew.setObjID(extcol.getString("r_object_id"));
					mnuNew.setClassName(extcol.getString("class"));
					mnuNew.setName(extcol.getString("menu_name"));
					mnuNew.setText(extcol.getString("menu_name"));
					final String implementation = extcol.getString("implementation");
					mnuNew.setImplementation(implementation);
					if (implementation.equals("java")) {
						mnuCustom.add(mnuNew);
					} else {
						mnuScripts.add(mnuNew);
					}
					mnuNew.addActionListener(new java.awt.event.ActionListener() {

						@Override
						public void actionPerformed(final java.awt.event.ActionEvent evt) {
							try {
								final JMenuItemExt menu = (JMenuItemExt) evt.getSource();
								if (implementation.equals("java")) {
									// //System.out.println(evt);
									// //System.out.println(System.currentTimeMillis());

									final IDfSession sess = smanager.getSession();
									final IDfSysObject classobj = (IDfSysObject) sess.getObject(new DfId(menu.getObjID()));
									final String className = menu.getClassName();
									// //System.out.println(className);
									final String filepath = classobj.getFile(null);
									final File ab_jar = new File(filepath);
									final URL[] urls = new URL[1];
									urls[0] = ab_jar.toURL();
									final URLClassLoader cLoad = new URLClassLoader(urls);
									final Class<?> customClass = cLoad.loadClass(className);
									final IQuickExtension x = (IQuickExtension) customClass.newInstance();
									final String objid = getIDfromTable();
									final IDfId id = new DfId(objid);
									final IDfSysObject obj = (IDfSysObject) sess.getObject(id);

									x.setObj(obj);
									x.setSessionManager(smanager);
									x.execute();
								} else {
									final ActionExecutor ae = new ActionExecutor();
									final String objid = getIDfromTable();
									ae.runGroovy(objid, menu.getObjID());
								}
								/*
								 * String classFileName =
								 * classobj.getFile(null);
								 * ////System.out.println(classFileName);
								 * FileClassLoader loader = new
								 * FileClassLoader("C://Temp"); Class c =
								 * loader.loadClass("Dump"); IQuickExtension x =
								 * (IQuickExtension) c.newInstance(); String
								 * objid = getIDfromTable(); IDfId id= new
								 * DfId(objid); IDfSysObject obj =
								 * (IDfSysObject) sess.getObject(id);
								 * x.execute(obj, sess);
								 */

								// Class Extractor =
								// Class.forName(menu.getName());
								// IQuickExtension x = (IQuickExtension)
								// Extractor.newInstance();
							} catch (final InstantiationException ex) {
								log.error(ex);
							} catch (final ClassNotFoundException ex) {
								log.error(ex);
							} catch (final DfException ex) {
								log.error(ex);
							} catch (final IllegalAccessException ex) {
								log.error(ex);
							} catch (final MalformedURLException ex) {
								log.error(ex);
							}
						}
					});
				}
			}
		} catch (final DfException ex) {
			log.error(ex);
		} finally {
			if (extcol != null) {
				try {
					extcol.close();
				} catch (final DfException ex) {
					log.error(ex);
				}
			}
			if (session != null) {
				smanager.releaseSession(session);
			}
		}
	}
}
