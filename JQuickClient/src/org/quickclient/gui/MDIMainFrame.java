/*
 * MDIMainFrame.java
 *
 * Created on 29. joulukuuta 2007, 23:53
 */
package org.quickclient.gui;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.quickclient.classes.AclBrowserData;
import org.quickclient.classes.ConfigService;
import org.quickclient.classes.CustomQueryHelper;
import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.HTTPClient;
import org.quickclient.classes.ListAttribute;
import org.quickclient.classes.MDIDesktopPane;
import org.quickclient.classes.SwingHelper;
import org.quickclient.classes.UserorGroupSelectorData;
import org.quickclient.classes.Utils;

import com.documentum.com.DfClientX;
import com.documentum.com.IDfClientX;
import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfClient;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfPersistentObject;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.client.IDfTypedObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfLogger;

/**
 *
 * @author Administrator
 */
public class MDIMainFrame extends javax.swing.JFrame {

	public static MDIMainFrame me;
	static String docbroker = "";
	static String docbase = "";
	static String user = "";
	static String pass = "";

	public static MDIMainFrame getInstance() {
		return me;
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(final String args[]) {

		int ind = 0;
		for (final String arg : args) {
			if (arg.equals("-docbroker")) {
				docbroker = args[ind + 1];
			}
			if (arg.equals("-docbase")) {
				docbase = args[ind + 1];
			}
			if (arg.equals("-user")) {
				user = args[ind + 1];
			}
			if (arg.equals("-pass")) {
				pass = args[ind + 1];
			}
			ind++;
		}
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				final MDIMainFrame m = new MDIMainFrame();
				SwingHelper.centerJFrame(m);
				m.setVisible(true);
			}
		});
	}

	DocuSessionManager smanager;

	DefaultTableModel model;

	private boolean showThumbnails;

	Logger log = Logger.getLogger(MDIMainFrame.class);

	private Vector<ListAttribute> listattributes;

	private javax.swing.JInternalFrame bframe;

	private final String standardqueryattributes = "object_name, r_object_id, r_link_cnt, r_object_type, a_content_type, i_is_replica, r_lock_owner, r_is_virtual_doc, i_is_reference";

	private String additionalqueryattributes = "";

	private javax.swing.JButton cmdAdvSearch;

	private javax.swing.JButton cmdNewBrowser;

	private javax.swing.JButton cmdQuickSearch;

	public javax.swing.JDesktopPane desktopPane;

	private javax.swing.JMenuItem exitMenuItem;

	private javax.swing.JMenu fileMenu;

	private javax.swing.JButton jButton1;

	private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;

	private javax.swing.JLabel jLabel1;

	private javax.swing.JMenu jMenu1;

	private javax.swing.JMenu jMenu2;

	private javax.swing.JMenu jMenu3;

	private javax.swing.JMenu jMenu4;

	private javax.swing.JMenuItem jMenuItem1;

	private javax.swing.JMenuItem jMenuItem10;

	private javax.swing.JMenuItem jMenuItem11;

	private javax.swing.JMenuItem jMenuItem6;

	private javax.swing.JMenuItem jMenuItem7;

	private javax.swing.JMenuItem jMenuItem8;

	private javax.swing.JMenuItem jMenuItem9;

	private javax.swing.JSeparator jSeparator1;

	private javax.swing.JToolBar toolbar;

	private javax.swing.JMenuBar menuBar;

	private javax.swing.JMenuItem mnuLocations;

	private javax.swing.JMenu mnuOptions;

	private javax.swing.JMenuItem mnuRelatedDoc;

	private javax.swing.JMenuItem mnuSearchACL;

	private javax.swing.JMenuItem mnuSearchChronId;

	private javax.swing.JMenuItem mnuSearchCustom;

	private javax.swing.JMenuItem mnuSearchOwner;

	private javax.swing.JMenuItem mnuSerachObjId;
	private javax.swing.JMenuItem mnuServices;
	private javax.swing.JMenuItem mnuShowSessions1;
	private javax.swing.JMenuItem mnuTypeEdit;

	private javax.swing.JMenuItem mnuUserMgmt;
	private javax.swing.JMenuItem mnuVisibleAttributes;
	private javax.swing.JTextField txtSearch;
	private JMenuItem mntmDisconnect;
	private JMenuItem mntmUtilities;

	/** Creates new form MDIMainFrame */
	public MDIMainFrame() {
		me = this;

		try {
			log.debug("start");
			UIManager.setLookAndFeel(new com.jgoodies.looks.plastic.PlasticXPLookAndFeel());
			// UIManager.setLookAndFeel(new WindowsLookAndFeel());
		} catch (final Exception e) {
			log.error(e);
		}
		final ConfigService cs = ConfigService.getInstance();
		showThumbnails = false;
		initComponents();
		menuBar.add(new WindowMenu(desktopPane));
		cs.setDesktop(desktopPane);
		initDocbaseConnection();
		smanager = DocuSessionManager.getInstance();
		if (smanager.getDocbasename() != null) {
			this.setTitle(smanager.getUserName() + "@" + smanager.getDocbasename());
		}
		cs.saveProperties();

		final Enumeration enumx = Logger.getRootLogger().getAllAppenders();
		while (enumx.hasMoreElements()) {
			final Appender app = (Appender) enumx.nextElement();
			if (app instanceof FileAppender) {
				System.out.println("Logging to file=" + ((FileAppender) app).getFile());
			}
		}
	}

	private void checkForUpdates(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem6ActionPerformed

		final HTTPClient client = new HTTPClient();
		final String s = client.getBuildNumber("http://quickclient.org/build.txt");
		log.debug(s);
		if (s == null || s.length() == 0) {
			JOptionPane.showMessageDialog(null, "Error, occurred during check, sorry", "", JOptionPane.PLAIN_MESSAGE);
			return;
		}
		final String[] cc = s.split("-");
		String ee = cc[1];
		ee = ee.replaceAll("b", "");
		log.debug(ee);

		final String currentVersion = MDIMainFrame.class.getPackage().getImplementationVersion();
		final String[] ccx = currentVersion.split("-");
		String eex = ccx[1];
		eex = eex.replaceAll("b", "");

		final int webversion = Integer.parseInt(ee);
		final int myversion = Integer.parseInt(eex);
		if (webversion > myversion) {
			JOptionPane.showMessageDialog(null, "Your version: " + currentVersion + "\nAvailable Version: " + s, "Version Info", JOptionPane.PLAIN_MESSAGE);
		} else if (webversion == myversion) {
			JOptionPane.showMessageDialog(null, "Your version: " + currentVersion + "\nAvailable Version: " + s, "Version Info", JOptionPane.PLAIN_MESSAGE);
		} else if (webversion < myversion) {
			JOptionPane.showMessageDialog(null, "?? Fatal ??" + currentVersion, "Oh no", JOptionPane.PLAIN_MESSAGE);
		}

	}// GEN-LAST:event_jMenuItem6ActionPerformed

	private void cmdAdvSearchActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdAdvSearchActionPerformed
		final AdvSearchFrameExpr search = new AdvSearchFrameExpr();

		SwingHelper.centerJFrame(search);
		search.setVisible(true);
		search.setDesktop(desktopPane);
	}// GEN-LAST:event_cmdAdvSearchActionPerformed

	private void cmdNewBrowserActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdNewBrowserActionPerformed
		final MainJFrame bf = new MainJFrame();
		desktopPane.add(bf);
		// bf.setSize(600, 400);
		bf.setVisible(true);
		bf.setResizable(true);
	}// GEN-LAST:event_cmdNewBrowserActionPerformed

	private void cmdQuickSearchActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdQuickSearchActionPerformed
		doQuickSearch();

	}// GEN-LAST:event_cmdQuickSearchActionPerformed

	private void doQuickSearch() {
		IDfCollection col = null;
		IDfSession session = null;
		DefaultTableModel quicksearchtablemodel = new DefaultTableModel();
		quicksearchtablemodel.addColumn(".");
		quicksearchtablemodel.addColumn(".");
		quicksearchtablemodel.addColumn("object_name");
		// additional column values
		final int listsize = listattributes.size();
		for (int i = 0; i < listsize; i++) {
			quicksearchtablemodel.addColumn(listattributes.get(i));
		}
		quicksearchtablemodel.addColumn("data");
		quicksearchtablemodel.setRowCount(0);
		String queryStr = "";

		if (showThumbnails) {
			queryStr = "select " + standardqueryattributes + additionalqueryattributes + ",thumbnail_url  from dm_document SEARCH DOCUMENT CONTAINS '";
		} else {
			queryStr = "select " + standardqueryattributes + additionalqueryattributes + " from dm_document SEARCH DOCUMENT CONTAINS '";
		}
		final String quickString = txtSearch.getText();
		queryStr = queryStr + quickString + "'";
		final String tempvalue = "";
		final String repvalue = "";
		try {
			final Cursor cur = new Cursor(Cursor.WAIT_CURSOR);

			setCursor(cur);
			final IDfQuery query = new DfQuery();
			query.setDQL(queryStr);
			session = smanager.getSession();
			col = query.execute(session, DfQuery.DF_READ_QUERY);
			final Utils util = new Utils();
			quicksearchtablemodel = util.getModelFromCollection(session, null, col, showThumbnails, quicksearchtablemodel, null, null);
			final SearchFrame sf = new SearchFrame(quicksearchtablemodel, showThumbnails);
			desktopPane.add(sf);
			sf.setSize(400, 400);
			sf.setVisible(true);
		} catch (final DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
			final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);

			setCursor(cur2);
		} finally {
			if (col != null) {
				try {
					col.close();
				} catch (final DfException ex) {
					log.error(ex);
				}
			}
			if (session != null) {
				smanager.releaseSession(session);
			}
			final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);

			setCursor(cur2);
		}
	}

	public void executeCustomSearch(final String whereClause, final String title, final boolean showallversions) {
		String queryStr = "";
		String allVersion = "";
		if (showallversions) {
			allVersion = "(ALL)";
		}
		if (showThumbnails) {
			queryStr = "select " + standardqueryattributes + additionalqueryattributes + " from dm_sysobject" + allVersion + " " + whereClause;
		} else {
			queryStr = "select " + standardqueryattributes + additionalqueryattributes + ",thumbnail_url from dm_sysobject" + allVersion + " " + whereClause;
		}
		final Cursor cur = new Cursor(Cursor.WAIT_CURSOR);

		setCursor(cur);
		new CustomQueryHelper().executeCustomQuery(model, desktopPane, me, queryStr, "Related documents", showThumbnails);

		// executeCustomQuery(queryStr, "Result of custom query: " + cp);
		final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);

		setCursor(cur2);
	}

	private void exitMenuItemActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_exitMenuItemActionPerformed
		try {
			IDfSession session = null;
			session = smanager.getSession();
			session.purgeLocalFiles();
			System.exit(0);// GEN-LAST:event_exitMenuItemActionPerformed
		} catch (final DfException ex) {
			// Logger.getLogger(MDIMainFrame.class.getName()).log(Level.SEVERE,
			// null, ex);
		}
	}

	private JInternalFrame[] getAllFrames() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	public JDesktopPane getDesktopPane() {
		return desktopPane;
	}

	public void initAfterConnection() {
		final ConfigService cs = ConfigService.getInstance();
		listattributes = cs.getAttributes(cs.getCurrentListConfigName()).get();
		for (int i = 0; i < listattributes.size(); i++) {
			final ListAttribute a = listattributes.get(i);
			if (a.type.equals("dm_sysobject")) {
				additionalqueryattributes = additionalqueryattributes + ", " + listattributes.get(i).attribute;
			}
		}
		initializeColumns();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		desktopPane = new MDIDesktopPane();
		toolbar = new javax.swing.JToolBar();
		toolbar.setBorder(new EmptyBorder(2, 1, 1, 2));
		toolbar.setAlignmentY(Component.CENTER_ALIGNMENT);
		cmdNewBrowser = new javax.swing.JButton();
		jButton1 = new javax.swing.JButton();
		jButton1.setMinimumSize(new Dimension(35, 20));
		jButton1.setPreferredSize(new Dimension(45, 20));
		jButton1.setMaximumSize(new Dimension(45, 20));
		jButton1.setMargin(new Insets(4, 20, 4, 20));
		cmdAdvSearch = new javax.swing.JButton();
		cmdAdvSearch.setMargin(new Insets(4, 14, 4, 14));
		jLabel1 = new javax.swing.JLabel();
		txtSearch = new javax.swing.JTextField();
		txtSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					doQuickSearch();
				}
			}
		});
		jLabel1.setLabelFor(txtSearch);
		cmdQuickSearch = new javax.swing.JButton();
		menuBar = new javax.swing.JMenuBar();
		fileMenu = new javax.swing.JMenu();
		exitMenuItem = new javax.swing.JMenuItem();
		jMenu2 = new javax.swing.JMenu();
		jMenu3 = new javax.swing.JMenu();
		mnuSerachObjId = new javax.swing.JMenuItem();
		mnuSearchCustom = new javax.swing.JMenuItem();
		mnuSearchChronId = new javax.swing.JMenuItem();
		mnuSearchACL = new javax.swing.JMenuItem();
		mnuSearchOwner = new javax.swing.JMenuItem();
		mnuRelatedDoc = new javax.swing.JMenuItem();
		jMenuItem1 = new javax.swing.JMenuItem();
		mnuOptions = new javax.swing.JMenu();
		mnuVisibleAttributes = new javax.swing.JMenuItem();
		jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
		jMenu4 = new javax.swing.JMenu();
		mnuUserMgmt = new javax.swing.JMenuItem();
		jMenuItem10 = new javax.swing.JMenuItem();
		jMenuItem8 = new javax.swing.JMenuItem();
		jMenuItem9 = new javax.swing.JMenuItem();
		jMenuItem11 = new javax.swing.JMenuItem();
		mnuShowSessions1 = new javax.swing.JMenuItem();
		mnuServices = new javax.swing.JMenuItem();
		mnuLocations = new javax.swing.JMenuItem();
		mnuTypeEdit = new javax.swing.JMenuItem();
		jMenu1 = new javax.swing.JMenu();
		jMenuItem6 = new javax.swing.JMenuItem();
		jSeparator1 = new javax.swing.JSeparator();
		jMenuItem7 = new javax.swing.JMenuItem();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("QuickClient");
		setLocationByPlatform(true);

		toolbar.setFloatable(false);
		toolbar.setRollover(true);

		final URL iUrl = getClass().getResource("/52.gif");
		if (iUrl != null) {
			final ImageIcon i = new javax.swing.ImageIcon(iUrl);
			if (i != null) {
				cmdNewBrowser.setIcon(i); // NOI18N
			}
		}
		cmdNewBrowser.setText("Browser");
		cmdNewBrowser.setToolTipText("Opens new Docbase browser");
		cmdNewBrowser.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		cmdNewBrowser.setFocusable(false);
		cmdNewBrowser.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		cmdNewBrowser.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdNewBrowserActionPerformed(evt);
			}
		});
		toolbar.add(cmdNewBrowser);

		final URL aUrl = getClass().getResource("");
		if (aUrl != null) {
			final ImageIcon a = new javax.swing.ImageIcon(aUrl);
			if (a != null) {
				jButton1.setIcon(a); // NOI18N
			}
		}
		jButton1.setText("Inbox");
		jButton1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		jButton1.setFocusable(false);
		jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
		jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});
		toolbar.add(jButton1);

		final URL sUrl = getClass().getResource("/search.gif");
		if (sUrl != null) {
			final ImageIcon s = new javax.swing.ImageIcon(sUrl); // NOI18N
			if (s != null) {
				cmdAdvSearch.setIcon(s); // NOI18N
			}
		}
		cmdAdvSearch.setText("Search");
		cmdAdvSearch.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		cmdAdvSearch.setFocusable(false);
		cmdAdvSearch.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		cmdAdvSearch.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdAdvSearchActionPerformed(evt);
			}
		});
		toolbar.add(cmdAdvSearch);

		jLabel1.setText("                                          Quick Search: ");
		toolbar.add(jLabel1);
		toolbar.add(txtSearch);

		cmdQuickSearch.setText("  Go  ");
		cmdQuickSearch.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		cmdQuickSearch.setFocusable(false);
		cmdQuickSearch.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		cmdQuickSearch.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		cmdQuickSearch.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				cmdQuickSearchActionPerformed(evt);
			}
		});
		toolbar.add(cmdQuickSearch);

		fileMenu.setText("File");

		exitMenuItem.setText("Exit");
		exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				exitMenuItemActionPerformed(evt);
			}
		});

		mntmDisconnect = new JMenuItem("Disconnect");
		mntmDisconnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				smanager.setSMgr(null);
			}
		});
		fileMenu.add(mntmDisconnect);
		fileMenu.add(exitMenuItem);

		menuBar.add(fileMenu);

		jMenu2.setText("Searches");

		jMenu3.setText("Quick Searches");

		mnuSerachObjId.setText("using r_object_id");
		mnuSerachObjId.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuSerachObjIdActionPerformed(evt);
			}
		});
		jMenu3.add(mnuSerachObjId);

		mnuSearchCustom.setText("using custom predicate");
		mnuSearchCustom.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuSearchCustomActionPerformed(evt);
			}
		});
		jMenu3.add(mnuSearchCustom);

		mnuSearchChronId.setText("using i_chronicle_id");
		mnuSearchChronId.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuSearchChronIdActionPerformed(evt);
			}
		});
		jMenu3.add(mnuSearchChronId);

		mnuSearchACL.setText("using acl name");
		mnuSearchACL.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuSearchACLActionPerformed(evt);
			}
		});
		jMenu3.add(mnuSearchACL);

		mnuSearchOwner.setText("using owner name");
		mnuSearchOwner.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuSearchOwnerActionPerformed(evt);
			}
		});
		jMenu3.add(mnuSearchOwner);

		mnuRelatedDoc.setText("Related Documents");
		mnuRelatedDoc.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuRelatedDocActionPerformed(evt);
			}
		});
		jMenu3.add(mnuRelatedDoc);

		jMenuItem1.setText("Images without Thumbnail");
		jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				jMenuItem1ActionPerformed(evt);
			}
		});
		jMenu3.add(jMenuItem1);

		jMenu2.add(jMenu3);

		menuBar.add(jMenu2);

		mnuOptions.setText("Options");

		mnuVisibleAttributes.setText("Visible Attributes");
		mnuVisibleAttributes.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuVisibleAttributesActionPerformed(evt);
			}
		});
		mnuOptions.add(mnuVisibleAttributes);

		jCheckBoxMenuItem1.setText("Show Thumbnails in Search Results");
		jCheckBoxMenuItem1.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				jCheckBoxMenuItem1ActionPerformed(evt);
			}
		});
		mnuOptions.add(jCheckBoxMenuItem1);

		menuBar.add(mnuOptions);

		jMenu4.setText("Admin Tools");

		final URL groupUrl = getClass().getResource("/group.gif");
		if (groupUrl != null) {
			mnuUserMgmt.setIcon(new javax.swing.ImageIcon(groupUrl)); // NOI18N
		}
		mnuUserMgmt.setText("User Management");
		mnuUserMgmt.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuUserMgmtActionPerformed(evt);
			}
		});
		jMenu4.add(mnuUserMgmt);

		final URL jobUrl = getClass().getResource("/job.gif");
		if (jobUrl != null) {
			jMenuItem10.setIcon(new javax.swing.ImageIcon(jobUrl)); // NOI18N
		}
		jMenuItem10.setText("Job Management");
		jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				jMenuItem10ActionPerformed(evt);
			}
		});

		final JMenuItem mntmAclEditor = new JMenuItem("ACL Editor");
		mntmAclEditor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent evt) {
				mntmAclEditorActionPerformed(evt);
			}
		});
		jMenu4.add(mntmAclEditor);
		jMenu4.add(jMenuItem10);

		final URL job2Url = getClass().getResource("/job.gif");
		if (job2Url != null) {
			jMenuItem8.setIcon(new javax.swing.ImageIcon(job2Url)); // NOI18N
		}
		jMenuItem8.setText("Method Editor");
		jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				jMenuItem8ActionPerformed(evt);
			}
		});
		jMenu4.add(jMenuItem8);

		jMenuItem9.setText("DQL Editor");
		jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				jMenuItem9ActionPerformed(evt);
			}
		});
		jMenu4.add(jMenuItem9);

		jMenuItem11.setText("API");
		jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				jMenuItem11ActionPerformed(evt);
			}
		});

		final JMenuItem mntmDqlScript = new JMenuItem("DQL Script");
		mntmDqlScript.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				mnuDQLScriptActionPerformed(arg0);
			}
		});
		jMenu4.add(mntmDqlScript);
		jMenu4.add(jMenuItem11);

		mnuShowSessions1.setText("Show Sessions");
		mnuShowSessions1.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuShowSessions1ActionPerformed(evt);
			}
		});

		final JMenuItem mntmApiScript = new JMenuItem("API Script");
		mntmApiScript.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent evt) {
				mnuApiScriptActionPerformed(evt);
			}
		});
		jMenu4.add(mntmApiScript);
		jMenu4.add(mnuShowSessions1);

		mnuServices.setText("Start / Stop Services");
		mnuServices.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuServicesActionPerformed(evt);
			}
		});
		jMenu4.add(mnuServices);

		mnuLocations.setText("Locations / Mount Points");
		mnuLocations.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuLocationsActionPerformed(evt);
			}
		});
		jMenu4.add(mnuLocations);

		mnuTypeEdit.setText("Type Editor");
		mnuTypeEdit.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuTypeEditActionPerformed(evt);
			}
		});
		jMenu4.add(mnuTypeEdit);

		menuBar.add(jMenu4);

		final JMenuItem mntmLogViewer = new JMenuItem("Log Viewer");
		mntmLogViewer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				mntmLogViewerActionPerformed(e);
			}
		});
		jMenu4.add(mntmLogViewer);

		mntmUtilities = new JMenuItem("Utilities");
		mntmUtilities.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				mntmUtilitiesActionPerformed(e);
			}
		});
		jMenu4.add(mntmUtilities);

		final JMenuItem mntmLdap = new JMenuItem("LDAP");
		mntmLdap.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				final LDAPFrame frame = new LDAPFrame();
				frame.setVisible(true);
			}
		});
		jMenu4.add(mntmLdap);

		jMenu1.setText("Help");

		jMenuItem6.setText("Check for Updates..");
		jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				checkForUpdates(evt);
			}
		});
		jMenu1.add(jMenuItem6);
		jMenu1.add(jSeparator1);

		jMenuItem7.setText("About..");
		jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				jMenuItem7ActionPerformed(evt);
			}
		});
		jMenu1.add(jMenuItem7);

		menuBar.add(jMenu1);

		setJMenuBar(menuBar);

		final GroupLayout layout = new GroupLayout(getContentPane());
		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(toolbar, GroupLayout.DEFAULT_SIZE, 764, Short.MAX_VALUE).addComponent(desktopPane, GroupLayout.DEFAULT_SIZE, 764, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(toolbar, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(desktopPane, GroupLayout.DEFAULT_SIZE, 656, Short.MAX_VALUE)));
		getContentPane().setLayout(layout);

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void initDocbaseConnection() {
		LoginFrame lf;

		if (docbase.length() > 0 || user.length() > 0 || pass.length() > 0) {
			boolean dynamicdocboker = false;
			String h = "docbroker";
			String p = "1489";
			try {
				if (docbroker.length() > 0) {
					dynamicdocboker = true;
					String[] ouh = null;
					if (docbroker.contains(":")) {
						ouh = docbroker.split(":");
						h = ouh[0];
						p = ouh[1];
					}
					final IDfClientX clientx = new DfClientX();
					final IDfClient client = clientx.getLocalClient();
					final IDfTypedObject config = client.getClientConfig();
					config.setString("primary_host", h);
					config.setString("primary_port", p);
				}

				IDfSessionManager sessionmanager;
				IDfSession session = null;

				final Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
				setCursor(cur);
				sessionmanager = LoginFrame.createSessionManager(docbase, user, pass, h, p);
				session = sessionmanager.getSession(docbase);

				if (session != null) {
					DocuSessionManager.getInstance().setSMgr(sessionmanager);
					DocuSessionManager.getInstance().setDocbasename(docbase);
					DocuSessionManager.getInstance().setUserName(session.getLoginUserName());
					this.setVisible(false);
					ConfigService.getInstance().setLoggedInUser(session.getUser(session.getLoginUserName()));
					final String defFolder = session.getUser(session.getLoginUserName()).getDefaultFolder();
					IDfPersistentObject po = null;
					po = session.getObjectByPath(defFolder);
					ConfigService.getInstance().setHomeFolderId(po.getString("r_object_id"));
					ConfigService.getInstance().setUserName(session.getLoginUserName());
					ConfigService.getInstance().setDocbaseName(session.getDocbaseName());
					MDIMainFrame.getInstance().setTitle(session.getLoginUserName() + "@" + session.getDocbaseName());
					sessionmanager.release(session);
					initAfterConnection();
				}
			} catch (final Exception ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage(), "Login Failed!", JOptionPane.ERROR_MESSAGE);
			} finally {
				final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
				setCursor(cur2);
			}
		} else {
			try {
				lf = new LoginFrame();
				SwingHelper.centerJFrame(lf);
				lf.setVisible(true);
				lf.setAlwaysOnTop(true);
				lf.setpointer(this);
			} catch (final DfException ex) {
				SwingHelper.showErrorMessage(ex.getMessage(), ex.getMessage());
				DfLogger.error(this, ex.getMessage(), null, ex);
			}
		}
	}

	public void initializeColumns() {
		model = new DefaultTableModel() {

			@Override
			public boolean isCellEditable(final int row, final int column) {
				return false;
			}
		};
		model.setColumnCount(0);
		// default column values
		model.addColumn(".");
		model.addColumn(".");
		model.addColumn("object_name");
		// additional column values
		final int xx = listattributes.size();
		for (int i = 0; i < xx; i++) {
			model.addColumn(listattributes.get(i));
		}
		model.addColumn("data");
	}

	private void jButton1ActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed

		final InboxFrame inf = new InboxFrame();

		desktopPane.add(inf);
		// bf.setSize(600, 400);
		inf.setVisible(true);
		inf.setResizable(true);
	}// GEN-LAST:event_jButton1ActionPerformed

	private void jCheckBoxMenuItem1ActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jCheckBoxMenuItem1ActionPerformed
		if (showThumbnails) {
			showThumbnails = false;
		} else {
			showThumbnails = true;
		}
	}// GEN-LAST:event_jCheckBoxMenuItem1ActionPerformed

	private void jMenuItem10ActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem10ActionPerformed
		final JobListFrame joblist = new JobListFrame();
		SwingHelper.centerJFrame(joblist);
		joblist.setVisible(true);

	}// GEN-LAST:event_jMenuItem10ActionPerformed

	private void jMenuItem11ActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem11ActionPerformed

		final ApiFrameSyntax api = new ApiFrameSyntax();
		SwingHelper.centerJFrame(api);
		api.setVisible(true);

	}// GEN-LAST:event_jMenuItem11ActionPerformed

	private void jMenuItem1ActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem1ActionPerformed

		final String imageformats = ConfigService.getInstance().getParameter("imageformats");
		final StringBuffer formatpart = new StringBuffer();
		if (imageformats != null) {
			if (imageformats.length() > 3) {
				final String splitted[] = imageformats.split(",");
				for (int i = 0; i < splitted.length; i++) {
					formatpart.append("'");
					formatpart.append(splitted[i]);
					formatpart.append("'");
					if (i < splitted.length - 1) {
						formatpart.append(",");
					}
				}
			} else {
				formatpart.append("'gif','jpg','jpeg'");
			}
		}
		String queryStr = "select r_lock_owner, a_content_type, r_object_type, object_name, owner_name, r_object_id from dm_document c WHERE a_content_type in (" + formatpart.toString() + ") and NOT EXISTS (SELECT r_object_id FROM dmr_content WHERE ANY parent_id = c.r_object_id AND full_format = 'jpeg_th')";
		if (showThumbnails) {
			queryStr = "select " + standardqueryattributes + additionalqueryattributes + ",thumbnail_url from dm_document c WHERE a_content_type in (" + formatpart.toString() + ") and NOT EXISTS (SELECT r_object_id FROM dmr_content WHERE ANY parent_id = c.r_object_id AND full_format = 'jpeg_th')";
		} else {
			queryStr = "select " + standardqueryattributes + additionalqueryattributes + " from dm_document c WHERE a_content_type in (" + formatpart.toString() + ") and NOT EXISTS (SELECT r_object_id FROM dmr_content WHERE ANY parent_id = c.r_object_id AND full_format = 'jpeg_th')";
		}

		final Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
		setCursor(cur);
		new CustomQueryHelper().executeCustomQuery(model, desktopPane, me, queryStr, "Missing Thumbnails", showThumbnails);
		final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
		setCursor(cur2);

	}// GEN-LAST:event_jMenuItem1ActionPerformed

	private void jMenuItem7ActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem7ActionPerformed

		final String xx = MDIMainFrame.class.getPackage().getImplementationVersion();

		JOptionPane.showMessageDialog(null, "QuickClient, build no: " + xx + "\n This application uses free icons from http://www.aha-soft.com/", "About...", JOptionPane.INFORMATION_MESSAGE);

	}// GEN-LAST:event_jMenuItem7ActionPerformed

	private void jMenuItem8ActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem8ActionPerformed

		final MethodFrame ff = new MethodFrame();
		SwingHelper.centerJFrame(ff);
		ff.setVisible(true);
	}// GEN-LAST:event_jMenuItem8ActionPerformed

	private void jMenuItem9ActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem9ActionPerformed
		// TODO add your handling code here:

		final DqlFrameSyntax dql = new DqlFrameSyntax();
		SwingHelper.centerJFrame(dql);
		dql.setVisible(true);
		dql.initDQLList();

	}// GEN-LAST:event_jMenuItem9ActionPerformed

	protected void mntmAclEditorActionPerformed(final ActionEvent evt) {

		final ACLBrowserFrame aclbrowser = new ACLBrowserFrame(false);
		SwingHelper.centerJFrame(aclbrowser);
		aclbrowser.setVisible(true);
	}

	protected void mntmLogViewerActionPerformed(final ActionEvent e) {

		final LogViewerFrame f = new LogViewerFrame();
		f.setVisible(true);
		SwingHelper.centerJFrame(f);

	}

	protected void mntmUtilitiesActionPerformed(final ActionEvent e) {
		final AdminUtils f = new AdminUtils();
		f.setVisible(true);
		SwingHelper.centerJFrame(f);

	}

	protected void mnuApiScriptActionPerformed(final ActionEvent evt) {
		final APIScriptFrame api = new APIScriptFrame();
		SwingHelper.centerJFrame(api);
		api.setVisible(true);

	}

	protected void mnuDQLScriptActionPerformed(final ActionEvent arg0) {
		// Auto-generated method stub

		final DQLScript dqlscript = new DQLScript();
		SwingHelper.centerJFrame(dqlscript);
		dqlscript.setVisible(true);
	}

	private void mnuLocationsActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuLocationsActionPerformed

		final LocationFrame locations = new LocationFrame();
		SwingHelper.centerJFrame(locations);
		locations.setVisible(true);

	}// GEN-LAST:event_mnuLocationsActionPerformed

	private void mnuRelatedDocActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuRelatedDocActionPerformed
		// TODO
		final String queryStr = "";
		if (showThumbnails) {
			// queryStr = "select " + standardqueryattributes +
			// additionalqueryattributes +
			// " from dm_sysobject where owner_name = '" + ownerName + "'";
		} else {
			// queryStr = "select " + standardqueryattributes +
			// additionalqueryattributes +
			// ",thumbnail_url from dm_sysobject where owner_name = '" +
			// ownerName + "'";
		}
		final Cursor cur = new Cursor(Cursor.WAIT_CURSOR);

		setCursor(cur);
		// executeCustomQuery(queryStr, "Documents owned by: " + ownerName);
		// new CustomQueryHelper().executeCustomQuery(model, desktopPane, me,
		// queryStr, "Related Docs of: ", showThumbnails);

		final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);

		setCursor(cur2);

	}// GEN-LAST:event_mnuRelatedDocActionPerformed

	private void mnuSearchACLActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuSearchACLActionPerformed
		final AclBrowserData abd = new AclBrowserData();
		final ActionListener al = new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				final String acldomain = abd.getAclDomain();
				final String aclname = abd.getAclName();
				String queryStr = "select r_lock_owner, a_content_type, r_object_type, object_name, owner_name, r_object_id from dm_document where acl_name = '" + aclname + "' and acl_domain = '" + acldomain + "'";
				if (showThumbnails) {
					queryStr = "select " + standardqueryattributes + additionalqueryattributes + " from dm_sysobject where acl_name = '" + aclname + "' and acl_domain = '" + acldomain + "'";
				} else {
					queryStr = "select " + standardqueryattributes + additionalqueryattributes + ",thumbnail_url from dm_sysobject where acl_name = '" + aclname + "' and acl_domain = '" + acldomain + "'";
				}
				final Cursor cur = new Cursor(Cursor.WAIT_CURSOR);

				setCursor(cur);
				new CustomQueryHelper().executeCustomQuery(model, desktopPane, me, queryStr, "search with acl: " + aclname, showThumbnails);

				// executeCustomQuery(queryStr, "Documents with acl: " +
				// aclname);
				final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);

				setCursor(cur2);
			}
		};
		final ACLBrowserFrame abf = new ACLBrowserFrame(al, abd, true);
		SwingHelper.centerJFrame(abf);
		abf.setVisible(true);
		abf.setAclbrowserdata(abd);

	}// GEN-LAST:event_mnuSearchACLActionPerformed

	private void mnuSearchChronIdActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuSearchChronIdActionPerformed
		final String id = JOptionPane.showInputDialog("Enter i_chronicle_id");
		if (id != null) {
			String queryStr = "select r_lock_owner, a_content_type, r_object_type, object_name, owner_name, r_object_id from dm_document where i_chronicle_id  ='" + id + "'";
			if (showThumbnails) {
				queryStr = "select " + standardqueryattributes + additionalqueryattributes + " from dm_sysobject where i_chronicle_id  ='" + id + "'";
			} else {
				queryStr = "select " + standardqueryattributes + additionalqueryattributes + ",thumbnail_url from dm_sysobject where i_chronicle_id  ='" + id + "'";
			}
			if (id.length() == 16) {
				final Cursor cur = new Cursor(Cursor.WAIT_CURSOR);

				setCursor(cur);
				new CustomQueryHelper().executeCustomQuery(model, desktopPane, me, queryStr, "Search with chronicle ID: " + id, showThumbnails);

				// executeCustomQuery(queryStr, "");
				final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);

				setCursor(cur2);
			}
		}
	}// GEN-LAST:event_mnuSearchChronIdActionPerformed

	private void mnuSearchCustomActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuSearchCustomActionPerformed
		final String cp = JOptionPane.showInputDialog("Enter Custom predicate i.e. \"where object_name like '%foo%'\"");
		if (cp != null) {
			String queryStr = "";
			if (showThumbnails) {
				queryStr = "select " + standardqueryattributes + additionalqueryattributes + " from dm_sysobject " + cp;
			} else {
				queryStr = "select " + standardqueryattributes + additionalqueryattributes + ",thumbnail_url from dm_sysobject " + cp;
			}
			final Cursor cur = new Cursor(Cursor.WAIT_CURSOR);

			setCursor(cur);
			new CustomQueryHelper().executeCustomQuery(model, desktopPane, me, queryStr, "Custom predicate: " + cp, showThumbnails);

			// executeCustomQuery(queryStr, "Result of custom query: " + cp);
			final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);

			setCursor(cur2);
		}

	}// GEN-LAST:event_mnuSearchCustomActionPerformed

	private void mnuSearchOwnerActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuSearchOwnerActionPerformed
		// TODO add your handling code here:

		final UserorGroupSelectorData x = new UserorGroupSelectorData();
		final ActionListener al = new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				final String ownerName = x.getUserorGroupname();
				String queryStr = "";
				if (showThumbnails) {
					queryStr = "select " + standardqueryattributes + additionalqueryattributes + " from dm_sysobject where owner_name = '" + ownerName + "'";
				} else {
					queryStr = "select " + standardqueryattributes + additionalqueryattributes + ",thumbnail_url from dm_sysobject where owner_name = '" + ownerName + "'";
				}
				final Cursor cur = new Cursor(Cursor.WAIT_CURSOR);

				setCursor(cur);
				// executeCustomQuery(queryStr, "Documents owned by: " +
				// ownerName);
				new CustomQueryHelper().executeCustomQuery(model, desktopPane, me, queryStr, "Documents where owner is: " + ownerName, showThumbnails);

				final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);

				setCursor(cur2);
			}
		};
		final UserorGroupSelectorFrame frame = new UserorGroupSelectorFrame(al, x);
		SwingHelper.centerJFrame(frame);
		frame.setVisible(true);

	}// GEN-LAST:event_mnuSearchOwnerActionPerformed

	private void mnuSerachObjIdActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuSerachObjIdActionPerformed
		final String id = JOptionPane.showInputDialog("Enter r_object_id");
		String queryStr = "";
		if (id != null) {
			if (showThumbnails) {
				queryStr = "select " + standardqueryattributes + additionalqueryattributes + " from dm_sysobject(ALL) where r_object_id  ='" + id + "'";
			} else {
				queryStr = "select " + standardqueryattributes + additionalqueryattributes + ",thumbnail_url from dm_sysobject(ALL) where r_object_id  ='" + id + "'";
			}

			if (id.length() == 16) {
				final Cursor cur = new Cursor(Cursor.WAIT_CURSOR);

				setCursor(cur);
				new CustomQueryHelper().executeCustomQuery(model, desktopPane, me, queryStr, "ObjectId search with ID: " + id, showThumbnails);

				// executeCustomQuery(queryStr,"");
				final Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);

				setCursor(cur2);
			}
		}
	}// GEN-LAST:event_mnuSerachObjIdActionPerformed

	private void mnuServicesActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuServicesActionPerformed

		final IDfSession session = smanager.getSession();
		try {
			final String serverhost = session.getServerConfig().getString("r_host_name");
			final String serverversion = session.getServerConfig().getString("r_server_version");
			if (serverversion.toLowerCase().contains("win32")) {
				final List<String> command = new ArrayList<String>();
				command.add("cmd.exe");
				command.add("/C");
				command.add("c:\\softa\\sysinternals\\psservice.exe");
				command.add("\\\\" + serverhost);
				command.add("-u");
				command.add("dmadmin");
				command.add("-p");
				command.add("dmadmin");
				final ProcessBuilder builder = new ProcessBuilder(command);
				final Map<String, String> environ = builder.environment();
				final Process process = builder.start();
				final InputStream is = process.getInputStream();
				final InputStreamReader isr = new InputStreamReader(is);
				final BufferedReader br = new BufferedReader(isr);
				String line;
				while ((line = br.readLine()) != null) {
					// System.out.println(line);
				}
			}
		} catch (final IOException ex) {
			log.error(ex);
		} catch (final DfException ex) {
			log.error(ex);
		}

	}// GEN-LAST:event_mnuServicesActionPerformed

	private void mnuShowSessions1ActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuShowSessions1ActionPerformed

		final SessionsFrame sf = new SessionsFrame();
		SwingHelper.centerJFrame(sf);
		sf.setVisible(true);
	}// GEN-LAST:event_mnuShowSessions1ActionPerformed

	private void mnuTypeEditActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuTypeEditActionPerformed

		final TypeEditor t = new TypeEditor();
		SwingHelper.centerJFrame(t);
		t.setVisible(true);

	}// GEN-LAST:event_mnuTypeEditActionPerformed

	private void mnuUserMgmtActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuUserMgmtActionPerformed
		final UsergroupManagementFrame ugFrame = new UsergroupManagementFrame();
		SwingHelper.centerJFrame(ugFrame);
		ugFrame.setVisible(true);

	}// GEN-LAST:event_mnuUserMgmtActionPerformed

	private void mnuVisibleAttributesActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuVisibleAttributesActionPerformed
		final AttributeSelectorFrame frame = new AttributeSelectorFrame();
		SwingHelper.centerJFrame(frame);
		frame.setVisible(true);

	}// GEN-LAST:event_mnuVisibleAttributesActionPerformed
}
