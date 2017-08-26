/*f
 * MainJFrame.java
 *
 * Created on October 24, 2006, 10:51 PM
 */
package org.quickclient.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.dnd.DragSource;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;
import org.quickclient.classes.ActionExecutor;
import org.quickclient.classes.ConfigService;
import org.quickclient.classes.DefaultTableModelCreator;
import org.quickclient.classes.DocInfo;
import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.DokuData;
import org.quickclient.classes.ExportListener;
import org.quickclient.classes.IQuickTableModel;
import org.quickclient.classes.ListAttribute;
import org.quickclient.classes.ObjectTableTransferHandler;
import org.quickclient.classes.QueryFilter;
import org.quickclient.classes.SwingHelper;
import org.quickclient.classes.Utils;

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
import com.documentum.fc.common.IDfId;

import javax.swing.JComboBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;

/**
 * 
 * @author Administrator
 */
public class MainJFrame extends javax.swing.JInternalFrame implements IQuickTableModel, TreeSelectionListener {

	private static final String DUMMYNODE = "dummynode";
	Logger log = Logger.getLogger(MainJFrame.class);
	DocInfo docinfo = new DocInfo();
	private Vector<ListAttribute> listattributes;
	private boolean showdocinfo;
	DocuSessionManager smanager;
	TablePopUpMenu myPopUp;
	TablePopUpMenu docInfoPopUp;
	PermissionPopUp permPopUp;
	LocationsPopUp locationsPopup;
	RenditionsPopUp renditionsPopup;
	private boolean docinfoVisible;
	private FormatRenderer formatrenderer;
	private ActionListener treeChangeListener;
	private QuickClientMutableTreeNode checkedout;
	private static int TBL_PERM_COLUMN = 2;
	private QuickClientMutableTreeNode myfiles;
	private QuickClientMutableTreeNode myrooms;
	private boolean additionalfilter;
	private ArrayList<QueryFilter> queryfilter = new ArrayList<QueryFilter>();
	private QueryFilterEditor editorx = null;
	private String orderbyString = "";
	private ArrayList<String> nonsysobjattrs = new ArrayList<String>();

	/** Creates new form MainJFrame */
	public MainJFrame() {
		Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
		setCursor(cur);
		docinfoVisible = false;
		initComponents();
		DragSource ds = new DragSource();
		objectTable.setDragEnabled(true);
		folderTree.setDragEnabled(true);
		formatrenderer = new FormatRenderer();
		formatrenderer.setShowThumbnails(false);
		showThumbnails = false;
		showdocinfo = false;

		docInfoPopUp = new TablePopUpMenu();
		docInfoPopUp.setTable(tblDocInfo);
		permPopUp = new PermissionPopUp();
		permPopUp.setTable(tblDocInfo);
		locationsPopup = new LocationsPopUp();
		locationsPopup.setTable(tblDocInfo);
		renditionsPopup = new RenditionsPopUp();
		renditionsPopup.setTable(tblDocInfo);
		smanager = DocuSessionManager.getInstance();
		ConfigService cs = ConfigService.getInstance();
		for (String name: cs.getListConfigNames()) {
			cmbConfigNames.addItem(name);
		}
		
		readqueryattributes();
		
		folderTree.setModel(treemodel);
		this.jScrollPane4.getViewport().setBackground(Color.WHITE);
		this.jScrollPane1.getViewport().setBackground(Color.WHITE);
		panelDocInfo.setVisible(false);
		jSplitPane3.setDividerSize(0);

		/*
		 * java.net.URL imageURL = null; java.net.URL imageURL2 = null;
		 * java.net.URL imageURL3 = null; Icon leafIcon = null; Icon openIcon =
		 * null; Icon closedIcon = null; imageURL =
		 * FormatRenderer.class.getResource("fclosed256.gif"); imageURL2 =
		 * FormatRenderer.class.getResource("fopen256.gif"); imageURL3 =
		 * FormatRenderer.class.getResource("fclosed256.gif"); if (imageURL !=
		 * null) { leafIcon = new ImageIcon(imageURL); } if (imageURL2 != null)
		 * { openIcon = new ImageIcon(imageURL2); } if (imageURL3 != null) {
		 * closedIcon = new ImageIcon(imageURL3); }
		 * 
		 * 
		 * if (leafIcon != null) { DefaultTreeCellRenderer renderer = new
		 * DefaultTreeCellRenderer(); renderer.setLeafIcon(leafIcon);
		 * renderer.setOpenIcon(openIcon); renderer.setClosedIcon(closedIcon);
		 * folderTree.setCellRenderer(renderer); }
		 */
		folderTree.setCellRenderer(new TreeTypeRenderer());
		initializeColumns();

		this.locationsModel = docinfo.initializeLocationsTableModel();
		this.permissionlistModel = docinfo.initializePermissionListTableModel();
		this.renditionsModel = docinfo.initializeRenditionsTableModel();
		this.relationsModel = docinfo.initializeRelationsTableModel();
		this.versionsModel = docinfo.initializeLocationsVersionsModel();
		initVersionView();

		initAll();

		ObjectTableTransferHandler ott = new ObjectTableTransferHandler();
		ott.setJtable(objectTable);
		ott.setJMenu(new JPopupMenu());
		objectTable.setTransferHandler(ott);

		TreeTransferHandler tth = new TreeTransferHandler();
		tth.setJTree(this.folderTree);
		tth.setMenu(new JPopupMenu());
		folderTree.setTransferHandler(tth);
		// DCINFO_CURRENT_VALUE = DCINFO_VERSIONS;
		// initDocbaseConnection();

		myPopUp = new TablePopUpMenu();
		myPopUp.setTable(objectTable);
		Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);

		setCursor(cur2);
	}

	private void readqueryattributes() {
		ConfigService cs = ConfigService.getInstance();
		this.orderbyString = cs.getAttributes(cmbConfigNames.getSelectedItem().toString()).getOrderBy();
		listattributes = cs.getAttributes(cmbConfigNames.getSelectedItem().toString()).get();
		additionalqueryattributes = "";
		for (int i = 0; i < listattributes.size(); i++) {
			ListAttribute a = listattributes.get(i);
			if (a.type.equals("dm_sysobject"))
				additionalqueryattributes = additionalqueryattributes + ", " + listattributes.get(i).attribute;
			
		}
	}

	public void initializeColumns() {
		
		objectTable.setAutoCreateColumnsFromModel(true);
		tablemodel = new DefaultTableModelCreator().createModel(cmbConfigNames.getSelectedItem().toString());
		objectTable.setModel(tablemodel);
		objectTable.getColumnModel().getColumn(0).setCellRenderer(new LockRenderer());
		// objectTable.getColumnModel().getColumn(1).setCellRenderer(new
		// FormatRenderer());
		objectTable.getColumnModel().getColumn(1).setCellRenderer(formatrenderer);
		// objectTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		objectTable.setEditingRow(1);
		objectTable.setRowHeight(22);
		tblDocInfo.setRowHeight(22);

		for (int i = 0; i < 3; i++) {
			TableColumn col = objectTable.getColumnModel().getColumn(i);
			if (i == 0 || i == 1) {
				col.setPreferredWidth(22);
				col.setMaxWidth(22);
			} else {
				col.setPreferredWidth(200);
			}
		}

		int lastIndex = objectTable.getColumnCount();
		objectTable.getColumnModel().removeColumn(objectTable.getColumnModel().getColumn(lastIndex - 1));

	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jComboBox1 = new javax.swing.JComboBox();
		jComboBox2 = new javax.swing.JComboBox();
		treePopUpMenu = new javax.swing.JPopupMenu();
		jMenu1 = new javax.swing.JMenu();
		mnuNewDocument = new javax.swing.JMenuItem();
		mnuNewCabinet = new javax.swing.JMenuItem();
		mnuNewRoom = new javax.swing.JMenuItem();
		mnuNewFolder = new javax.swing.JMenuItem();
		mnuImportTree = new javax.swing.JMenuItem();
		mnuExport = new javax.swing.JMenuItem();
		mnuTRename = new javax.swing.JMenuItem();
		mnuDelete = new javax.swing.JMenuItem();
		mnuShowContents = new javax.swing.JMenuItem();
		mnuAttributes = new javax.swing.JMenuItem();
		jDialog1 = new javax.swing.JDialog();
		testPopUp = new javax.swing.JPopupMenu();
		jMenuItem4 = new javax.swing.JMenuItem();
		jPanel2 = new javax.swing.JPanel();
		jSplitPane2 = new javax.swing.JSplitPane();
		jSplitPane3 = new javax.swing.JSplitPane();
		jScrollPane4 = new javax.swing.JScrollPane();
		objectTable = new javax.swing.JTable();
		panelDocInfo = new javax.swing.JPanel();
		panelDocInfoMenuBar = new javax.swing.JPanel();
		jLabel5 = new javax.swing.JLabel();
		cmbDocInfoChooser = new javax.swing.JComboBox();
		jPanel5 = new javax.swing.JPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		tblDocInfo = new javax.swing.JTable();
		jScrollPane3 = new javax.swing.JScrollPane();
		folderTree = new javax.swing.JTree();
		jToolBar1 = new javax.swing.JToolBar();
		cmdCheckIn = new JToggleButton();
		cmdCheckIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (showAllVersions) {
					showAllVersions = false;
				} else {
					showAllVersions = true;
				}
			}
		});
		cmdCancelCheckout = new JToggleButton();
		cmdCancelCheckout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (showThumbnails) {
					formatrenderer.setShowThumbnails(false);
					objectTable.setRowHeight(22);
					TableColumn col = objectTable.getColumnModel().getColumn(1);
					col.setPreferredWidth(22);
					col.setMaxWidth(22);
					showThumbnails = false;
				} else {
					formatrenderer.setShowThumbnails(true);
					String size = ConfigService.getInstance().getParameter(ConfigService.PARAM_THUMBNAIL_SIZE);
					if (size.equals(""))
						size = "100";
					objectTable.setRowHeight(Integer.parseInt(size));
					TableColumn col = objectTable.getColumnModel().getColumn(1);
					col.setMaxWidth(Integer.parseInt(size));
					col.setPreferredWidth(Integer.parseInt(size));
					col.setWidth(Integer.parseInt(size));
					showThumbnails = true;

				}
			}
		});
		cmdToggleDocInfo = new javax.swing.JToggleButton();
		jPanel3 = new javax.swing.JPanel();

		jMenu1.setText("New...");
		
//		try {
//		   InputStream is = new FileInputStream("c:/windows/fonts/calibri.ttf");
//		    Font font = Font.createFont(Font.TRUETYPE_FONT, is);
//		    Font fontBase = font.deriveFont(13f);
//		    objectTable.setFont(fontBase);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		

		mnuNewDocument.setText("Document");
		mnuNewDocument.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuNewDocumentActionPerformed(evt);
			}
		});
		jMenu1.add(mnuNewDocument);

		mnuNewCabinet.setText("Cabinet");
		mnuNewCabinet.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuNewCabinetActionPerformed(evt);
			}
		});
		jMenu1.add(mnuNewCabinet);

		mnuNewRoom.setText("Room");
		mnuNewRoom.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuNewRoomActionPerformed(evt);
			}
		});
		jMenu1.add(mnuNewRoom);

		mnuNewFolder.setText("Folder");
		mnuNewFolder.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuNewFolderActionPerformed(evt);
			}
		});
		jMenu1.add(mnuNewFolder);

		treePopUpMenu.add(jMenu1);

		mnuImportTree.setText("Import");
		mnuImportTree.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuImportTreeActionPerformed(evt);
			}
		});
		treePopUpMenu.add(mnuImportTree);

		mnuExport.setText("Export Folder");
		mnuExport.setToolTipText("Export contents of the Folder");
		mnuExport.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuExportActionPerformed(evt);
			}
		});
		treePopUpMenu.add(mnuExport);

		mnuTRename.setText("Rename");
		mnuTRename.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuTRenameActionPerformed(evt);
			}
		});
		treePopUpMenu.add(mnuTRename);

		mnuDelete.setText("Delete");
		mnuDelete.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuDeleteActionPerformed(evt);
			}
		});
		treePopUpMenu.add(mnuDelete);

		mnuShowContents.setText("Show Contents");
		mnuShowContents.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuShowContentsActionPerformed(evt);
			}
		});
		treePopUpMenu.add(mnuShowContents);
		treePopUpMenu.add(new JSeparator());

		mnuAttributes.setText("Attributes");
		mnuAttributes.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuAttributesActinPerformed(evt);
			}
		});
		treePopUpMenu.add(mnuAttributes);
		org.jdesktop.layout.GroupLayout jDialog1Layout = new org.jdesktop.layout.GroupLayout(jDialog1.getContentPane());
		jDialog1.getContentPane().setLayout(jDialog1Layout);
		jDialog1Layout.setHorizontalGroup(jDialog1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(0, 400, Short.MAX_VALUE));
		jDialog1Layout.setVerticalGroup(jDialog1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(0, 300, Short.MAX_VALUE));

		jMenuItem4.setText("Item");
		jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItem4ActionPerformed(evt);
			}
		});
		testPopUp.add(jMenuItem4);

		setClosable(true);
		setForeground(new java.awt.Color(153, 153, 255));
		setIconifiable(true);
		setMaximizable(true);
		setResizable(true);
		setTitle("Docbase Browser");

		jSplitPane2.setDividerLocation(150);
		jSplitPane2.setDividerSize(3);

		jSplitPane3.setDividerLocation(300);
		jSplitPane3.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

		objectTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

		}, new String[] { "", "", "Object Name" }));
		objectTable.setColumnSelectionAllowed(true);
		objectTable.setGridColor(new java.awt.Color(204, 204, 204));
		objectTable.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		objectTable.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				objectTableMouseClicked(evt);
			}

			public void mousePressed(java.awt.event.MouseEvent evt) {
				objectTableMousePressed(evt);
			}

			public void mouseReleased(java.awt.event.MouseEvent evt) {
				objectTableMouseReleased(evt);
			}
		});
		objectTable.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				objectTableKeyReleased(evt);
			}
		});
		jScrollPane4.setViewportView(objectTable);

		jSplitPane3.setTopComponent(jScrollPane4);

		panelDocInfoMenuBar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
		panelDocInfoMenuBar.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

		jLabel5.setText("    View:");
		panelDocInfoMenuBar.add(jLabel5);

		cmbDocInfoChooser.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Versions", "Discussions", "Locations", "Permissions", "Renditions", "Relations" }));
		cmbDocInfoChooser.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
			public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
				cmbDocInfoChooserMouseWheelMoved(evt);
			}
		});
		cmbDocInfoChooser.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
				cmbDocInfoChooserItemStateChanged(evt);
			}
		});
		panelDocInfoMenuBar.add(cmbDocInfoChooser);

		tblDocInfo.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { {}, {}, {}, {} }, new String[] {

		}));
		tblDocInfo.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				tblDocInfoMousePressed(evt);
			}

			public void mouseReleased(java.awt.event.MouseEvent evt) {
				tblDocInfoMouseReleased(evt);
			}
		});
		tblDocInfo.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				tblDocInfoKeyReleased(evt);
			}
		});
		jScrollPane1.setViewportView(tblDocInfo);

		org.jdesktop.layout.GroupLayout gl_jPanel5 = new org.jdesktop.layout.GroupLayout(jPanel5);
		jPanel5.setLayout(gl_jPanel5);
		gl_jPanel5.setHorizontalGroup(gl_jPanel5.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 596, Short.MAX_VALUE));
		gl_jPanel5.setVerticalGroup(gl_jPanel5.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE));

		org.jdesktop.layout.GroupLayout gl_panelDocInfo = new org.jdesktop.layout.GroupLayout(panelDocInfo);
		panelDocInfo.setLayout(gl_panelDocInfo);
		gl_panelDocInfo.setHorizontalGroup(gl_panelDocInfo.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(panelDocInfoMenuBar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 596, Short.MAX_VALUE)
				.add(jPanel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		gl_panelDocInfo.setVerticalGroup(gl_panelDocInfo.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
				gl_panelDocInfo.createSequentialGroup().add(panelDocInfoMenuBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 32, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
						.add(jPanel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		jSplitPane3.setRightComponent(panelDocInfo);

		jSplitPane2.setRightComponent(jSplitPane3);

		folderTree.setRootVisible(false);
		folderTree.setShowsRootHandles(true);
		folderTree.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				folderTreeMouseClicked(evt);
			}

			public void mouseReleased(java.awt.event.MouseEvent evt) {
				folderTreeMouseReleased(evt);
			}

			@Override
			public void mousePressed(MouseEvent evt) {
				folderTreeMousePressed(evt);
			}
		});
		folderTree.addTreeExpansionListener(new javax.swing.event.TreeExpansionListener() {
			public void treeCollapsed(javax.swing.event.TreeExpansionEvent evt) {
			}

			public void treeExpanded(javax.swing.event.TreeExpansionEvent evt) {
				folderTreeTreeExpanded(evt);
			}
		});
		jScrollPane3.setViewportView(folderTree);

		jSplitPane2.setLeftComponent(jScrollPane3);

		jToolBar1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
		jToolBar1.setRollover(true);

		cmdCheckIn.setFont(new java.awt.Font("Tahoma", 1, 11));
		cmdCheckIn.setText(" All Versions ");
		cmdCheckIn.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		cmdCheckIn.setFocusable(false);
		cmdCheckIn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		cmdCheckIn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		jToolBar1.add(cmdCheckIn);

		cmdCancelCheckout.setFont(new java.awt.Font("Tahoma", 1, 11));
		cmdCancelCheckout.setText(" Thumbnails ");
		cmdCancelCheckout.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		cmdCancelCheckout.setFocusable(false);
		cmdCancelCheckout.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		cmdCancelCheckout.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		jToolBar1.add(cmdCancelCheckout);

		cmdToggleDocInfo.setFont(new java.awt.Font("Tahoma", 1, 11));
		cmdToggleDocInfo.setText("  Info Panel  ");
		cmdToggleDocInfo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		cmdToggleDocInfo.setFocusable(false);
		cmdToggleDocInfo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		cmdToggleDocInfo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		cmdToggleDocInfo.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cmdToggleDocInfoActionPerformed(evt);
			}
		});
		jToolBar1.add(cmdToggleDocInfo);

		GroupLayout gl_jPanel2 = new GroupLayout(jPanel2);
		gl_jPanel2.setHorizontalGroup(gl_jPanel2.createParallelGroup(Alignment.LEADING).addComponent(jToolBar1, GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE).addComponent(jSplitPane2, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE));
		gl_jPanel2.setVerticalGroup(gl_jPanel2.createParallelGroup(Alignment.LEADING).addGroup(gl_jPanel2.createSequentialGroup().addComponent(jToolBar1, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE).addGap(3).addComponent(jSplitPane2, GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)));
		jPanel2.setLayout(gl_jPanel2);

		JToggleButton tglbtnApplyFilter = new JToggleButton(" Apply Filter ");
		tglbtnApplyFilter.setHorizontalAlignment(SwingConstants.RIGHT);
		tglbtnApplyFilter.setHorizontalTextPosition(SwingConstants.CENTER);
		tglbtnApplyFilter.setAlignmentX(Component.RIGHT_ALIGNMENT);
		tglbtnApplyFilter.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				if (additionalfilter == true) {
					additionalfilter = false;
					if (editorx != null)
						editorx.dispose();
				} else {
					additionalfilter = true;
					QueryFilterEditor editor = new QueryFilterEditor();
					editor.setQueryfilter(queryfilter);
					editor.init();
					editor.setVisible(true);
					editorx = editor;
				}

			}
		});
		tglbtnApplyFilter.setToolTipText("Additional filter, applied to objectlist");
		tglbtnApplyFilter.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		tglbtnApplyFilter.setFont(new Font("Tahoma", Font.BOLD, 11));
		jToolBar1.add(tglbtnApplyFilter);
		
		lblListConfiguration = new JLabel("  View config: ");
		jToolBar1.add(lblListConfiguration);
		
		cmbConfigNames = new JComboBox();
		cmbConfigNames.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent evt) {
				int maxindex = cmbConfigNames.getItemCount();
				if (evt.getWheelRotation() > 0) {
					int index = cmbConfigNames.getSelectedIndex();
					if (index < maxindex - 1) {
						cmbConfigNames.setSelectedIndex(index + 1);
					}
				} else {
					int index = cmbConfigNames.getSelectedIndex();
					if (index > 0) {
						cmbConfigNames.setSelectedIndex(index - 1);
					}
				}
				cmbConfigNamesItemStateChanged();
			}
		});
		cmbConfigNames.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				cmbConfigNamesItemStateChanged();
			}
		});
		jToolBar1.add(cmbConfigNames);

		jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

		lblInfoLabel = new JLabel("Browser Info");
		lblInfoLabel.setAlignmentY(Component.TOP_ALIGNMENT);

		org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
				org.jdesktop.layout.GroupLayout.TRAILING,
				layout.createSequentialGroup().add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
						.add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)));
		jPanel3.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		jPanel3.add(lblInfoLabel);

		pack();
	}// </editor-fold>//GEN-END:initComponents

	protected void cmbConfigNamesItemStateChanged() {
		System.out.println("e" + cmbConfigNames.getSelectedItem().toString());
		ConfigService.getInstance().setCurrentListConfigName(cmbConfigNames.getSelectedItem().toString());
		readqueryattributes();
		initializeColumns();
		updateTree();
	}

	private void initVersionView() {

		tblDocInfo.setModel(versionsModel);
		tblDocInfo.getColumnModel().getColumn(0).setCellRenderer(new LockRenderer());
		tblDocInfo.getColumnModel().getColumn(1).setCellRenderer(new FormatRenderer());
		tblDocInfo.getColumnModel().getColumn(0).setPreferredWidth(22);
		tblDocInfo.getColumnModel().getColumn(0).setMaxWidth(22);
		tblDocInfo.getColumnModel().getColumn(1).setPreferredWidth(22);
		tblDocInfo.getColumnModel().getColumn(1).setMaxWidth(22);
		int lastIndex = tblDocInfo.getColumnCount();
		tblDocInfo.getColumnModel().removeColumn(tblDocInfo.getColumnModel().getColumn(lastIndex - 1));

	}

	public String getIDfromTable() {
		int row = objectTable.getSelectedRow();
		if (row != -1) {
			Vector v = (Vector) tablemodel.getDataVector().elementAt(row);
			DokuData d = (DokuData) v.lastElement();
			String objid = d.getObjID();
			return objid;
		} else {
			return "";
		}
	}

	public void initAll() {

		// TODO docbase nimi root objektiin?
		root = new QuickClientMutableTreeNode("Cabinets");
		treemodel = new DefaultTreeModel(root);
		cabinets = new QuickClientMutableTreeNode("Cabinets");
		cabinets.setSpecialString("cabinets");
		subs = new QuickClientMutableTreeNode("Subscriptions");
		subs.setSpecialString("subscriptions");
		checkedout = new QuickClientMutableTreeNode("Checked Out");
		checkedout.setSpecialString("checkedout");
		myrooms = new QuickClientMutableTreeNode("My Rooms");
		myrooms.setSpecialString("myrooms");
		myfiles = new QuickClientMutableTreeNode("My Files");
		myfiles.setSpecialString("myfiles");
		homecabinetnode = new QuickClientMutableTreeNode("Home Cabinet");
		homecabinetnode.setSpecialString("homecabinet");
		homecabinetnode.setDokuData(new DokuData(ConfigService.getInstance().getHomeFolderId()));
		root.add(homecabinetnode);
		root.add(subs);
		root.add(myrooms);
		root.add(myfiles);
		root.add(checkedout);
		root.add(cabinets);

		treemodel.insertNodeInto(new QuickClientMutableTreeNode("WW" + "W"), subs, 0);
		treemodel.insertNodeInto(new QuickClientMutableTreeNode("WW" + "W"), homecabinetnode, 0);
		treemodel.insertNodeInto(new QuickClientMutableTreeNode("WW" + "W"), myrooms, 0);
		initTreeView();

		// //System.out.println("model: " + treemodel);
		folderTree.setModel(treemodel);
		folderTree.validate();

		// get new menuitems...
		// updateCustomMenu();

		folderTree.addTreeSelectionListener(this);
	}

	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton2ActionPerformed
	}// GEN-LAST:event_jButton2ActionPerformed

	private void folderTreeTreeExpanded(javax.swing.event.TreeExpansionEvent evt) {// GEN-FIRST:event_folderTreeTreeExpanded
		Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
		setCursor(cur);
		TreePath path = evt.getPath();
		QuickClientMutableTreeNode currNode = (QuickClientMutableTreeNode) path.getLastPathComponent();
		Vector<QuickClientMutableTreeNode> myVector = new Vector<QuickClientMutableTreeNode>();
		int ccount = currNode.getChildCount();
		for (int i = 0; i < ccount; i++) {
			myVector.add((QuickClientMutableTreeNode) currNode.getChildAt(i));
		}

		if (currNode.getLevel() == 1) {
			String foo = (String) currNode.getUserObject();
			if (foo.equalsIgnoreCase("subscriptions")) {
				updateSubscrptionsTree(currNode, myVector);
			}
			if (foo.equalsIgnoreCase("my rooms")) {
				updateRoomsTree(currNode, myVector);
			}

			if (foo.equalsIgnoreCase("home cabinet")) {
				updateHomeTree(currNode, myVector);
			}

			Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
			setCursor(cur2);
			return;

		}

		String objID = currNode.getDokuDataID();
		IDfSession session = null;
		IDfCollection col = null;

		try {
			session = smanager.getSession();
			IDfQuery query = new DfQuery();
			query.setDQL("select r_object_id, object_name,r_link_cnt, r_object_type from dm_folder where any i_folder_id = '" + objID + "' order by object_name desc");
			col = query.execute(session, IDfQuery.DF_READ_QUERY);
			while (col.next()) {
				String newID = col.getString("r_object_id");
				String objName = col.getString("object_name");
				int rLinkCnt = col.getInt("r_link_cnt");
				QuickClientMutableTreeNode newNode = new QuickClientMutableTreeNode(objName);
				DokuData data = new DokuData(newID);
				newNode.setDokuData(data);
				newNode.setObjType(col.getString("r_object_type"));
				treemodel.insertNodeInto(newNode, currNode, 0);
				if (rLinkCnt > 0) {
					QuickClientMutableTreeNode tempNode = new QuickClientMutableTreeNode(objName + "W");
					tempNode.setSpecialString(MainJFrame.DUMMYNODE);
					treemodel.insertNodeInto(tempNode, newNode, 0);
				}

			}
		} catch (DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
		} finally {
			if (col != null) {
				try {
					col.close();
				} catch (DfException ex) {
				}
			}
			if (session != null) {
				smanager.releaseSession(session);
			}

		}

		Iterator i = myVector.iterator();
		while (i.hasNext()) {
			QuickClientMutableTreeNode a = (QuickClientMutableTreeNode) i.next();
			treemodel.removeNodeFromParent(a);
		}
		// treemodel.removeNodeFromParent(currNode2);

		folderTree.validate();
		Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
		setCursor(cur2);

	}// GEN-LAST:event_folderTreeTreeExpanded

	private void updateRoomsTree(QuickClientMutableTreeNode currNode, Vector<QuickClientMutableTreeNode> myVector) {
		String q = "select " + standardqueryattributes + additionalqueryattributes + " from dmc_room order by object_name,r_object_id";
		IDfSession session = null;
		IDfCollection col = null;
		try {
			session = smanager.getSession();
			IDfQuery query = new DfQuery();
			query.setDQL(q);
			col = query.execute(session, IDfQuery.DF_READ_QUERY);
			while (col.next()) {
				String newID = col.getString("r_object_id");
				String objName = col.getString("object_name");
				int rLinkCnt = col.getInt("r_link_cnt");
				QuickClientMutableTreeNode newNode = new QuickClientMutableTreeNode(objName);
				DokuData data = new DokuData(newID);
				newNode.setDokuData(data);
				newNode.setObjType(col.getString("r_object_type"));
				treemodel.insertNodeInto(newNode, currNode, 0);
				if (rLinkCnt > 0) {
					QuickClientMutableTreeNode tempNode = new QuickClientMutableTreeNode(objName + "W");
					tempNode.setSpecialString(MainJFrame.DUMMYNODE);
					treemodel.insertNodeInto(tempNode, newNode, 0);
				}

			}
		} catch (DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
		} finally {
			if (col != null) {
				try {
					col.close();
				} catch (DfException ex) {
				}
			}
			if (session != null) {
				smanager.releaseSession(session);
			}

		}
		Iterator i = myVector.iterator();
		while (i.hasNext()) {
			QuickClientMutableTreeNode a = (QuickClientMutableTreeNode) i.next();
			treemodel.removeNodeFromParent(a);
		}
		// treemodel.removeNodeFromParent(currNode2);

		folderTree.validate();

	}

	private void mnuTRenameActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuTRenameActionPerformed
		String objid;
		TreePath path = folderTree.getSelectionPath();
		if (path == null) {
			return;
		}

		QuickClientMutableTreeNode selectedNode = (QuickClientMutableTreeNode) path.getLastPathComponent();
		objid = selectedNode.getDokuDataID();
		IDfSession session = null;
		try {
			session = smanager.getSession();
			IDfId id = new DfId(objid);
			IDfSysObject obj = (IDfSysObject) session.getObject(id);
			String objectName = obj.getString("object_name");
			String newObjectName = (String) JOptionPane.showInputDialog("Enter New Name", objectName);
			obj.setString("object_name", newObjectName);
			if (obj.isCheckedOut()) {
				obj.saveLock();
			} else {
				obj.save();
			}

		} catch (DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
		} finally {
			if (session != null) {
				smanager.releaseSession(session);
			}

		}

	}// GEN-LAST:event_mnuTRenameActionPerformed

	private void mnuImportTreeActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuImportTreeActionPerformed
		String objid;
		TreePath path = folderTree.getSelectionPath();
		if (path == null) {
			return;
		}

		QuickClientMutableTreeNode selectedNode = (QuickClientMutableTreeNode) path.getLastPathComponent();
		objid = selectedNode.getDokuDataID();
		SimpleImportFrame frame = new SimpleImportFrame(objid);
		SwingHelper.centerJFrame(frame);
		frame.setVisible(true);
	}// GEN-LAST:event_mnuImportTreeActionPerformed

	@SuppressWarnings("unchecked")
	private void objectTableMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_objectTableMouseClicked
		String configname = cmbConfigNames.getSelectedItem().toString();
		if (evt.getClickCount() == 2) {
			String objid = getIDfromTable();
			if (!objid.startsWith("0b") && !objid.startsWith("0c")) {
				ActionExecutor ex = new ActionExecutor();
				Vector v = new Vector();
				v.add(objid);
				ex.viewAction(v);
			} else {
				String objID = objid;
				IDfSession sess = null;
				IDfCollection col = null;
				IDfQuery query = new DfQuery();
				if (showThumbnails) {
					if (showAllVersions) {
						query.setDQL("select " + standardqueryattributes + additionalqueryattributes + ",thumbnail_url from dm_sysobject(ALL) where any i_folder_id = '" + objID + "' order by " + orderbyString + ",r_object_id");
					} else {
						query.setDQL("select " + standardqueryattributes + additionalqueryattributes + ",thumbnail_url from dm_sysobject where any i_folder_id = '" + objID + "' order by " + orderbyString + ",r_object_id");
					}

				} else {
					if (showAllVersions) {
						query.setDQL("select " + standardqueryattributes + additionalqueryattributes + " from dm_sysobject(ALL) where any i_folder_id = '" + objID + "' order by " + orderbyString + ",r_object_id");
					} else {
						query.setDQL("select " + standardqueryattributes + additionalqueryattributes + " from dm_sysobject where any i_folder_id = '" + objID + "' order by " + orderbyString + ",r_object_id");
					}

				}
				try {
					sess = smanager.getSession();
					String tempvalue = "";
					String repvalue = "";
					Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
					setCursor(cur);
					tablemodel.setRowCount(0);
					col = query.execute(sess, IDfQuery.DF_READ_QUERY);
					Utils util = new Utils();
					if (additionalfilter)
						tablemodel = util.getModelFromCollection(sess, configname, col, showThumbnails, tablemodel, queryfilter, nonsysobjattrs);
					else
						tablemodel = util.getModelFromCollection(sess, configname, col, showThumbnails, tablemodel, null, nonsysobjattrs);

					objectTable.validate();
				} catch (DfException ex) {
					log.error(ex);
				} finally {
					if (col != null) {
						try {
							col.close();
						} catch (DfException ex) {
							log.error(ex);
						}

					}
					if (sess != null) {
						smanager.releaseSession(sess);
					}

					Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
					setCursor(cur2);
				}
			}

		}
		if (evt.getClickCount() == 1) {
			selectedID = getIDfromTable();
			if (docinfoVisible) {
				String valittu = (String) cmbDocInfoChooser.getSelectedItem();
				if (DCINFO_CURRENT_VALUE == DCINFO_LOCATIONS) {
					updateLocationsModel();
					DCINFO_PREVIOUS_VALUE = DCINFO_CURRENT_VALUE;
				}

				if (DCINFO_CURRENT_VALUE == DCINFO_VERSIONS) {
					updateVersionsModel();
					DCINFO_PREVIOUS_VALUE = DCINFO_CURRENT_VALUE;
				}

				if (DCINFO_CURRENT_VALUE == DCINFO_RENDITIONS) {
					updateRenditionsModel();
				}

				if (DCINFO_CURRENT_VALUE == DCINFO_PERMISSIONS) {
					updatePermissionModel();
				}

				if (DCINFO_CURRENT_VALUE == DCINFO_RELATIONS) {
					updateRelationsModel();
				}

			}
		}
	}// GEN-LAST:event_objectTableMouseClicked

	private void updateLocationsModel() {
		DocInfo info = new DocInfo();
		String objid = getIDfromTable();

		locationsModel = info.updateLocationsModel(objid, locationsModel);
		tblDocInfo.setModel(locationsModel);
		tblDocInfo.validate();
	}

	private void folderTreeMousePressed(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_folderTreeMouseReleased
		System.out.println("MousePressed");
		int butt = evt.getButton();
		if (butt == MouseEvent.BUTTON3) {
			// treePopUpMenu.show(evt.getComponent(), evt.getX(), evt.getY());
			int row = folderTree.getClosestRowForLocation(evt.getX(), evt.getY());
			folderTree.setSelectionRow(row);
		} else {
			updateTree();
			System.out.println("updateTree called from MousePressed.");
		}
	}

	private void folderTreeMouseReleased(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_folderTreeMouseReleased
		int butt = evt.getButton();
		if (butt == MouseEvent.BUTTON3) {
			treePopUpMenu.show(evt.getComponent(), evt.getX(), evt.getY());
		}
	}// GEN-LAST:event_folderTreeMouseReleased

	private void objectTableMouseReleased(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_objectTableMouseReleased
		int butt = evt.getButton();
		// //System.out.println("butt on: " + butt);
		if (butt == MouseEvent.BUTTON3) {
			myPopUp.show(evt.getComponent(), evt.getX(), evt.getY());
		}
	}// GEN-LAST:event_objectTableMouseReleased

	private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jButton2MouseClicked
	}// GEN-LAST:event_jButton2MouseClicked

	private void tblDocInfoMouseReleased(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tblDocInfoMouseReleased
		// TODO add your handling code here:
		int butt = evt.getButton();
		// //System.out.println("butt on: " + butt);
		if (butt == MouseEvent.BUTTON3) {
			if (DCINFO_CURRENT_VALUE == DCINFO_VERSIONS) {
				docInfoPopUp.show(evt.getComponent(), evt.getX(), evt.getY());
			}

			if (DCINFO_CURRENT_VALUE == DCINFO_PERMISSIONS) {
				// PermissionsPopUp.show(evt.getComponent(), evt.getX(),
				// evt.getY());
				permPopUp.setSelectedId(getIDfromTable());
				permPopUp.show(evt.getComponent(), evt.getX(), evt.getY());
			}

			if (DCINFO_CURRENT_VALUE == DCINFO_LOCATIONS) {
				// locationsPopUp.show(evt.getComponent(), evt.getX(),
				// evt.getY());
				locationsPopup.setSelectedId(getIDfromTable());
				locationsPopup.show(evt.getComponent(), evt.getX(), evt.getY());
			}

			if (DCINFO_CURRENT_VALUE == DCINFO_RENDITIONS) {
				// renditionsPopUp.show(evt.getComponent(), evt.getX(),
				// evt.getY());
				renditionsPopup.setSelectedId(getIDfromTable());
				renditionsPopup.show(evt.getComponent(), evt.getX(), evt.getY());

			}

		}
	}// GEN-LAST:event_tblDocInfoMouseReleased

	private void updatePermissionModel() {
		IDfSession session = null;
		String objid = getIDfromTable();
		DocInfo info = new DocInfo();
		permissionlistModel = info.updatePermissionsModel(objid, permissionlistModel);
		tblDocInfo.setModel(permissionlistModel);
		tblDocInfo.validate();

	}

	private void cmbDocInfoChooserItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_cmbDocInfoChooserItemStateChanged
		String value = (String) cmbDocInfoChooser.getSelectedItem();

		if (value.equalsIgnoreCase("versions")) {
			DCINFO_PREVIOUS_VALUE = DCINFO_CURRENT_VALUE;
			DCINFO_CURRENT_VALUE = DCINFO_VERSIONS;
			updateVersionsModel();

			tblDocInfo.setModel(versionsModel);
			tblDocInfo.getColumnModel().getColumn(0).setCellRenderer(new LockRenderer());
			tblDocInfo.getColumnModel().getColumn(1).setCellRenderer(new FormatRenderer());
			tblDocInfo.getColumnModel().getColumn(0).setPreferredWidth(22);
			tblDocInfo.getColumnModel().getColumn(0).setMaxWidth(22);
			tblDocInfo.getColumnModel().getColumn(1).setPreferredWidth(22);
			tblDocInfo.getColumnModel().getColumn(1).setMaxWidth(22);

		}

		if (value.equalsIgnoreCase("renditions")) {
			DCINFO_PREVIOUS_VALUE = DCINFO_CURRENT_VALUE;
			DCINFO_CURRENT_VALUE = DCINFO_RENDITIONS;
			updateRenditionsModel();
			tblDocInfo.setModel(renditionsModel);
			tblDocInfo.getColumnModel().getColumn(0).setCellRenderer(new LockRenderer());
			tblDocInfo.getColumnModel().getColumn(1).setCellRenderer(new FormatRenderer());
			tblDocInfo.getColumnModel().getColumn(0).setPreferredWidth(22);
			tblDocInfo.getColumnModel().getColumn(0).setMaxWidth(22);
			tblDocInfo.getColumnModel().getColumn(1).setPreferredWidth(22);
			tblDocInfo.getColumnModel().getColumn(1).setMaxWidth(22);
		}

		if (value.equalsIgnoreCase("relations")) {
			DCINFO_PREVIOUS_VALUE = DCINFO_CURRENT_VALUE;
			DCINFO_CURRENT_VALUE = DCINFO_RELATIONS;
			updateRelationsModel();
			tblDocInfo.setModel(relationsModel);
			tblDocInfo.getColumnModel().getColumn(0).setCellRenderer(new FormatRenderer());
			tblDocInfo.getColumnModel().getColumn(0).setPreferredWidth(22);
			tblDocInfo.getColumnModel().getColumn(0).setMaxWidth(22);
		}

		if (value.equalsIgnoreCase("permissions")) {
			DCINFO_PREVIOUS_VALUE = DCINFO_CURRENT_VALUE;
			DCINFO_CURRENT_VALUE = DCINFO_PERMISSIONS;
			updatePermissionModel();

			tblDocInfo.setModel(permissionlistModel);
			tblDocInfo.getColumnModel().getColumn(0).setPreferredWidth(22);
			tblDocInfo.getColumnModel().getColumn(0).setMaxWidth(22);
			tblDocInfo.getColumnModel().getColumn(1).setPreferredWidth(22);
			tblDocInfo.getColumnModel().getColumn(1).setMaxWidth(22);
			tblDocInfo.getColumnModel().getColumn(1).setCellRenderer(new GroupOrUserRenderer());

		}

		if (value.equalsIgnoreCase("discussions")) {
			DCINFO_PREVIOUS_VALUE = DCINFO_CURRENT_VALUE;
			DCINFO_CURRENT_VALUE = DCINFO_DISCUSSIONS;
		}

		if (value.equalsIgnoreCase("locations")) {
			DCINFO_PREVIOUS_VALUE = DCINFO_CURRENT_VALUE;
			DCINFO_CURRENT_VALUE = DCINFO_LOCATIONS;
			updateLocationsModel();

			tblDocInfo.setModel(locationsModel);
			tblDocInfo.getColumnModel().getColumn(0).setCellRenderer(new LockRenderer());
			tblDocInfo.getColumnModel().getColumn(1).setCellRenderer(new FormatRenderer());
			tblDocInfo.getColumnModel().getColumn(0).setPreferredWidth(22);
			tblDocInfo.getColumnModel().getColumn(0).setMaxWidth(22);
			tblDocInfo.getColumnModel().getColumn(1).setPreferredWidth(22);
			tblDocInfo.getColumnModel().getColumn(1).setMaxWidth(22);
		}

	}// GEN-LAST:event_cmbDocInfoChooserItemStateChanged

	private void cmdCheckInActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdCheckInActionPerformed

		Cursor cur = new Cursor(Cursor.WAIT_CURSOR);

		setCursor(cur);
		ActionExecutor ae = new ActionExecutor();
		String objid = getIDfromTable();
		ae.checkInAction(objid, objectTable);
		Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
		setCursor(cur2);
	}// GEN-LAST:event_cmdCheckInActionPerformed

	private void cmdCancelCheckoutActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdCancelCheckoutActionPerformed

		Cursor cur = new Cursor(Cursor.WAIT_CURSOR);

		setCursor(cur);
		ActionExecutor ae = new ActionExecutor();
		String objid = getIDfromTable();
		ae.cancelCheckOutAction(objid, objectTable);
		Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);

		setCursor(cur2);
	}// GEN-LAST:event_cmdCancelCheckoutActionPerformed

	private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem4ActionPerformed
		// add your handling code here:
	}// GEN-LAST:event_jMenuItem4ActionPerformed

	private void mnuNewDocumentActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuNewDocumentActionPerformed
		String objid;
		TreePath path = folderTree.getSelectionPath();
		if (path == null) {
			return;
		}

		QuickClientMutableTreeNode selectedNode = (QuickClientMutableTreeNode) path.getLastPathComponent();
		objid = selectedNode.getDokuDataID();
		NewDocumentFrame frame = new NewDocumentFrame(objid);

		SwingHelper.centerJFrame(frame);
		frame.setVisible(true);

	}// GEN-LAST:event_mnuNewDocumentActionPerformed

	private void folderTreeMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_folderTreeMouseClicked
		System.out.println("MouseClicked");
		int butt = evt.getButton();
		if (butt == MouseEvent.BUTTON1) {
			// System.out.println("updateTree called from MouseClicked.");
			// updateTree();

		}
	}// GEN-LAST:event_folderTreeMouseClicked

	private void mnuDeleteActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuDeleteActionPerformed
		String objid;
		TreePath path = folderTree.getSelectionPath();
		if (path == null) {
			return;
		}

		QuickClientMutableTreeNode selectedNode = (QuickClientMutableTreeNode) path.getLastPathComponent();
		objid = selectedNode.getDokuDataID();
		IDfSession session = null;
		try {
			session = smanager.getSession();
			IDfId id = new DfId(objid);
			IDfSysObject obj = (IDfSysObject) session.getObject(id);
			obj.destroy();
			treemodel.removeNodeFromParent(selectedNode);
			folderTree.setModel(treemodel);
			folderTree.validate();
		} catch (DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
		} finally {
			if (session != null) {
				smanager.releaseSession(session);
			}

		}
	}// GEN-LAST:event_mnuDeleteActionPerformed

	private void mnuAttributesActinPerformed(java.awt.event.ActionEvent evt) {
		TreePath path = folderTree.getSelectionPath();
		if (path == null)
			return;
		QuickClientMutableTreeNode selectedNode = (QuickClientMutableTreeNode) path.getLastPathComponent();
		String objid = selectedNode.getDokuDataID();
		AttrEditorFrame3Text a = new AttrEditorFrame3Text(objid);
		a.setVisible(true);
	}

	private void mnuShowContentsActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuDeleteActionPerformed
		String configname = cmbConfigNames.getSelectedItem().toString();
		IDfSession sess = null;
		String objid;
		TreePath path = folderTree.getSelectionPath();
		if (path == null) {
			return;
		}

		QuickClientMutableTreeNode selectedNode = (QuickClientMutableTreeNode) path.getLastPathComponent();
		objid = selectedNode.getDokuDataID();
		IDfCollection col = null;
		IDfQuery query = new DfQuery();
		if (showThumbnails) {
			if (showAllVersions) {
				query.setDQL("select " + standardqueryattributes + additionalqueryattributes + ",thumbnail_url from dm_sysobject(ALL) where FOLDER(ID('" + objid + "'), descend)  order by object_name,r_object_id");
			} else {
				query.setDQL("select " + standardqueryattributes + additionalqueryattributes + ",thumbnail_url from dm_sysobject where FOLDER(ID('" + objid + "'), descend)  order by object_name,r_object_id");
			}

		} else {
			if (showAllVersions) {
				query.setDQL("select " + standardqueryattributes + additionalqueryattributes + " from dm_sysobject(ALL) where FOLDER(ID('" + objid + "'), descend)  order by object_name,r_object_id");
			} else {
				query.setDQL("select " + standardqueryattributes + additionalqueryattributes + " from dm_sysobject where FOLDER(ID('" + objid + "'), descend) ' order by object_name,r_object_id");
			}

		}
		try {
			sess = smanager.getSession();
			String tempvalue = "";
			String repvalue = "";
			Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
			setCursor(cur);
			tablemodel.setRowCount(0);
			col = query.execute(sess, IDfQuery.DF_READ_QUERY);
			Utils util = new Utils();
			if (additionalfilter)
				tablemodel = util.getModelFromCollection(sess, configname, col, showThumbnails, tablemodel, queryfilter, nonsysobjattrs);
			else
				tablemodel = util.getModelFromCollection(sess, configname, col, showThumbnails, tablemodel, null, nonsysobjattrs);

			objectTable.validate();
			lblInfoLabel.setText(String.valueOf(objectTable.getRowCount()));
		} catch (DfException ex) {
			log.error(ex);
		} finally {
			if (col != null) {
				try {
					col.close();
				} catch (DfException ex) {
					log.error(ex);
				}

			}
			if (sess != null) {
				smanager.releaseSession(sess);
			}

			Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
			setCursor(cur2);
		}
		// String objid;
		// TreePath path = folderTree.getSelectionPath();
		// if (path == null) {
		// return;
		// }
		//
		// QuickClientMutableTreeNode selectedNode =
		// (QuickClientMutableTreeNode) path.getLastPathComponent();
		// objid = selectedNode.getDokuDataID();
		// MDIMainFrame.getInstance().executeCustomSearch("where FOLDER(ID('" +
		// objid + "'), descend) ", "Content of folder", this.showAllVersions);
	}

	private void mnuNewFolderActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuNewFolderActionPerformed
		String objid;
		TreePath path = folderTree.getSelectionPath();
		if (path == null) {
			return;
		}

		QuickClientMutableTreeNode selectedNode = (QuickClientMutableTreeNode) path.getLastPathComponent();
		objid = selectedNode.getDokuDataID();
		IDfSession session = null;
		try {
			session = smanager.getSession();
			IDfFolder folder = (IDfFolder) session.newObject("dm_folder");
			String objname = JOptionPane.showInputDialog("Folder name?");
			folder.setObjectName(objname);
			folder.link(objid);
			folder.save();
			QuickClientMutableTreeNode newfoldernode = new QuickClientMutableTreeNode(objname);
			newfoldernode.setDokuData(new DokuData(folder.getObjectId().getId()));
			treemodel.insertNodeInto(newfoldernode, selectedNode, 0);
			folderTree.setModel(treemodel);
			folderTree.validate();
		} catch (DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
		} finally {
			if (session != null) {
				smanager.releaseSession(session);
			}

		}
	}// GEN-LAST:event_mnuNewFolderActionPerformed

	private void mnuNewCabinetActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuNewCabinetActionPerformed

		IDfSession session = null;
		try {
			session = smanager.getSession();
			IDfSysObject cab = (IDfFolder) session.newObject("dm_cabinet");
			String objname = JOptionPane.showInputDialog("Cabinet name?");
			cab.setObjectName(objname);
			cab.save();
			QuickClientMutableTreeNode newcabnode = new QuickClientMutableTreeNode(objname);
			newcabnode.setDokuData(new DokuData(cab.getObjectId().getId()));
			newcabnode.setObjType("dm_cabinet");
			treemodel.insertNodeInto(newcabnode, cabinets, 0);
			folderTree.setModel(treemodel);
			folderTree.validate();
		} catch (DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
		} finally {
			if (session != null) {
				smanager.releaseSession(session);
			}

		}
	}// GEN-LAST:event_mnuNewCabinetActionPerformed

	private void mnuNewRoomActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuNewRoomActionPerformed
		String objid;
		TreePath path = folderTree.getSelectionPath();
		if (path == null) {
			return;
		}

		QuickClientMutableTreeNode selectedNode = (QuickClientMutableTreeNode) path.getLastPathComponent();
		objid = selectedNode.getDokuDataID();
		IDfSession session = null;
		try {
			session = smanager.getSession();

			IDfFolder folder = (IDfFolder) session.newObject("dmc_room");
			String objname = JOptionPane.showInputDialog("Room name?");
			folder.setObjectName(objname);
			folder.link(objid);
			folder.save();
			QuickClientMutableTreeNode newfoldernode = new QuickClientMutableTreeNode(objname);
			newfoldernode.setDokuData(new DokuData(folder.getObjectId().getId()));
			treemodel.insertNodeInto(newfoldernode, selectedNode, 0);
			folderTree.setModel(treemodel);
			folderTree.validate();
		} catch (DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
		} finally {
			if (session != null) {
				smanager.releaseSession(session);
			}

		}

	}// GEN-LAST:event_mnuNewRoomActionPerformed

	private void objectTableMousePressed(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_objectTableMousePressed

		int[] rows = objectTable.getSelectedRows();
		System.out.println(rows.length);
		if (rows.length < 2) {
			int butt = evt.getButton();
			if (butt == MouseEvent.BUTTON3) {
				int row = objectTable.rowAtPoint(new Point(evt.getX(), evt.getY()));
				ListSelectionModel selectionModel = objectTable.getSelectionModel();
				selectionModel.setSelectionInterval(row, row);
			}
		}
	}// GEN-LAST:event_objectTableMousePressed

	private void tblDocInfoMousePressed(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tblDocInfoMousePressed
		Point p = evt.getPoint();
		int row = tblDocInfo.rowAtPoint(p);
		ListSelectionModel selectionModel = tblDocInfo.getSelectionModel();
		selectionModel.setSelectionInterval(row, row);
	}// GEN-LAST:event_tblDocInfoMousePressed

	private void mnuExportActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuExportActionPerformed

		final ExportData x = new ExportData();
		// try {
		final String objid;

		TreePath path = folderTree.getSelectionPath();
		if (path == null) {
			return;
		}

		QuickClientMutableTreeNode selectedNode = (QuickClientMutableTreeNode) path.getLastPathComponent();
		objid = selectedNode.getDokuDataID();

		ExportListener listener = new ExportListener();
		listener.setExportData(x);
		listener.setObjid(objid);
		ExportDialog frame = new ExportDialog(listener, x);
		SwingHelper.centerJFrame(frame);
		frame.setVisible(true);

	}// GEN-LAST:event_mnuExportActionPerformed

	private void cmdToggleDocInfoActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdToggleDocInfoActionPerformed
		if (docinfoVisible) {
			panelDocInfo.setVisible(false);
			jSplitPane3.setDividerSize(0);
			docinfoVisible = false;
		} else {
			panelDocInfo.setVisible(true);
			jSplitPane3.setDividerSize(2);
			jSplitPane3.setDividerLocation(0.66);
			docinfoVisible = true;
		}
	}// GEN-LAST:event_cmdToggleDocInfoActionPerformed

	private void tblDocInfoKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_tblDocInfoKeyReleased
	}// GEN-LAST:event_tblDocInfoKeyReleased

	private void objectTableKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_objectTableKeyReleased
		selectedID = getIDfromTable();
		if (docinfoVisible) {
			String valittu = (String) cmbDocInfoChooser.getSelectedItem();
			if (DCINFO_CURRENT_VALUE == DCINFO_LOCATIONS) {
				updateLocationsModel();
				DCINFO_PREVIOUS_VALUE = DCINFO_CURRENT_VALUE;
			}

			if (DCINFO_CURRENT_VALUE == DCINFO_VERSIONS) {
				updateVersionsModel();
				DCINFO_PREVIOUS_VALUE = DCINFO_CURRENT_VALUE;
			}

			if (DCINFO_CURRENT_VALUE == DCINFO_RENDITIONS) {
				updateRenditionsModel();
			}

			if (DCINFO_CURRENT_VALUE == DCINFO_PERMISSIONS) {
				updatePermissionModel();
			}

			if (DCINFO_CURRENT_VALUE == DCINFO_RELATIONS) {
				updateRelationsModel();
			}

		}
	}// GEN-LAST:event_objectTableKeyReleased

	private void cmbDocInfoChooserMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {// GEN-FIRST:event_cmbDocInfoChooserMouseWheelMoved

		int maxindex = cmbDocInfoChooser.getItemCount();
		if (evt.getWheelRotation() > 0) {
			int index = cmbDocInfoChooser.getSelectedIndex();
			if (index < maxindex - 1) {
				cmbDocInfoChooser.setSelectedIndex(index + 1);
			}
		} else {
			int index = cmbDocInfoChooser.getSelectedIndex();
			if (index > 0) {
				cmbDocInfoChooser.setSelectedIndex(index - 1);
			}
		}

	}// GEN-LAST:event_cmbDocInfoChooserMouseWheelMoved

	public Vector getIDListfromTable() {
		int[] row = objectTable.getSelectedRows();
		Vector<String> idVector = new Vector<String>();
		for (int i = 0; i < row.length; i++) {
			DefaultTableModel model = (DefaultTableModel) objectTable.getModel();
			Vector v = (Vector) model.getDataVector().elementAt(row[i]);
			DokuData d = (DokuData) v.lastElement();
			String objid = d.getObjID();
			idVector.add(objid);
		}

		return idVector;
	}

	private void initTreeView() {
		IDfCollection col = null;
		try {
			Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
			setCursor(cur);
			IDfQuery query = new DfQuery();
			query.setDQL("select r_object_id, object_name, r_link_cnt, r_object_type from dm_cabinet order by object_name desc");
			IDfSession sess = smanager.getSession();
			if (sess == null)
				return;
			col = query.execute(sess, DfQuery.DF_READ_QUERY);

			while (col.next()) {
				String oName = col.getString("object_name");
				int rLinkCnt = col.getInt("r_link_cnt");
				QuickClientMutableTreeNode newNode = new QuickClientMutableTreeNode(oName);
				DokuData data = new DokuData(col.getString("r_object_id"));
				newNode.setDokuData(data);
				newNode.setObjType(col.getString("r_object_type"));
				treemodel.insertNodeInto(newNode, cabinets, 0);
				if (rLinkCnt > 0) {
					QuickClientMutableTreeNode tempNode = new QuickClientMutableTreeNode(oName);
					tempNode.setSpecialString(MainJFrame.DUMMYNODE);
					treemodel.insertNodeInto(tempNode, newNode, 0);
				}

			}
			folderTree.setModel(treemodel);
			folderTree.validate();
			folderTree.setVisible(true);
			smanager.releaseSession(sess);
		} catch (DfException ex) {
			log.error(ex);
		} finally {
			try {
				if (col != null)
					col.close();
			} catch (DfException ex) {
			}
			Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
			setCursor(cur2);
		}

	}

	private void initDocbaseConnection() {
		LoginFrame lf;
		try {
			lf = new LoginFrame();
			lf.setVisible(true);
			lf.setAlwaysOnTop(true);
		} catch (DfException ex) {
			SwingHelper.showErrorMessage(ex.getMessage(), ex.getMessage());
			DfLogger.error(this, ex.getMessage(), null, ex);
		}

	}

	private String docbasename;
	private static int DCINFO_NONE = 0;
	private static int DCINFO_VERSIONS = 1;
	private static int DCINFO_RENDITIONS = 2;
	private static int DCINFO_RELATIONS = 3;
	private static int DCINFO_PERMISSIONS = 4;
	private static int DCINFO_DISCUSSIONS = 5;
	private static int DCINFO_LOCATIONS = 6;
	private int DCINFO_PREVIOUS_VALUE = 0;
	private int DCINFO_CURRENT_VALUE = DCINFO_VERSIONS;
	DefaultTableModel versionsModel;
	DefaultTableModel permissionlistModel;
	DefaultTableModel renditionsModel;
	DefaultTableModel relationsModel;
	DefaultTableModel locationsModel;
	private String selectedID;
	// Variables declaration - do not modify//GEN-BEGIN:variables

	private javax.swing.JComboBox cmbDocInfoChooser;
	private JToggleButton cmdCancelCheckout;
	private JToggleButton cmdCheckIn;
	private javax.swing.JToggleButton cmdToggleDocInfo;
	private javax.swing.JTree folderTree;
	private javax.swing.JComboBox jComboBox1;
	private javax.swing.JComboBox jComboBox2;
	private javax.swing.JDialog jDialog1;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JMenu jMenu1;

	private javax.swing.JMenuItem jMenuItem4;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel5;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane3;
	private javax.swing.JScrollPane jScrollPane4;
	private javax.swing.JSplitPane jSplitPane2;
	private javax.swing.JSplitPane jSplitPane3;

	private javax.swing.JToolBar jToolBar1;
	private javax.swing.JMenuItem mnuDelete;
	private javax.swing.JMenuItem mnuShowContents;
	private javax.swing.JMenuItem mnuAttributes;
	private javax.swing.JMenuItem mnuExport;
	private javax.swing.JMenuItem mnuImportTree;
	private javax.swing.JMenuItem mnuNewCabinet;
	private javax.swing.JMenuItem mnuNewDocument;
	private javax.swing.JMenuItem mnuNewFolder;
	private javax.swing.JMenuItem mnuNewRoom;
	private javax.swing.JMenuItem mnuTRename;
	private javax.swing.JTable objectTable;
	private javax.swing.JPanel panelDocInfo;
	private javax.swing.JPanel panelDocInfoMenuBar;
	private javax.swing.JTable tblDocInfo;
	private javax.swing.JPopupMenu testPopUp;
	private javax.swing.JPopupMenu treePopUpMenu;
	// End of variables declaration//GEN-END:variables
	private DefaultTreeModel treemodel;
	private DefaultTableModel tablemodel;
	private QuickClientMutableTreeNode root;
	private QuickClientMutableTreeNode cabinets; // = new
													// QuickClientMutableTreeNode("Cabinets");
	private QuickClientMutableTreeNode subs;
	private QuickClientMutableTreeNode homecabinetnode;
	private DefaultListModel userlistModel;
	private ListSelectionListener listlistener;
	private String standardqueryattributes = "object_name, r_object_id, r_link_cnt, r_object_type, a_content_type, i_is_replica, r_lock_owner, r_is_virtual_doc, i_is_reference";
	private String additionalqueryattributes = "";
	private boolean showAllVersions;
	private boolean showThumbnails;
	private JLabel lblInfoLabel;
	private JLabel lblListConfiguration;
	private JComboBox cmbConfigNames;
	private QuickClientMutableTreeNode lastselectedNode;

	public String getDocbasename() {
		return docbasename;
	}

	public void setDocbasename(String docbasename) {
		this.docbasename = docbasename;
	}

	private void updateHomeTree(QuickClientMutableTreeNode currNode, Vector<QuickClientMutableTreeNode> myVector) {
		String q = "";
		String objID = currNode.getDokuDataID();
		q = "select " + standardqueryattributes + " from dm_folder where any i_folder_id = '" + objID + "' order by object_name,r_object_id desc";
		IDfSession session = null;
		IDfCollection col = null;
		try {
			session = smanager.getSession();
			IDfQuery query = new DfQuery();
			query.setDQL(q);
			col = query.execute(session, IDfQuery.DF_READ_QUERY);
			while (col.next()) {
				String newID = col.getString("r_object_id");
				String objName = col.getString("object_name");
				int rLinkCnt = col.getInt("r_link_cnt");
				QuickClientMutableTreeNode newNode = new QuickClientMutableTreeNode(objName);
				DokuData data = new DokuData(newID);
				newNode.setDokuData(data);
				newNode.setObjType(col.getString("r_object_type"));
				treemodel.insertNodeInto(newNode, currNode, 0);
				if (rLinkCnt > 0) {
					QuickClientMutableTreeNode tempNode = new QuickClientMutableTreeNode(objName + "W");
					tempNode.setSpecialString(MainJFrame.DUMMYNODE);
					treemodel.insertNodeInto(tempNode, newNode, 0);
				}

			}
		} catch (DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
		} finally {
			if (col != null) {
				try {
					col.close();
				} catch (DfException ex) {
				}
			}
			if (session != null) {
				smanager.releaseSession(session);
			}

		}
		Iterator i = myVector.iterator();
		while (i.hasNext()) {
			QuickClientMutableTreeNode a = (QuickClientMutableTreeNode) i.next();
			treemodel.removeNodeFromParent(a);
		}
		// treemodel.removeNodeFromParent(currNode2);

		folderTree.validate();
	}

	private void updateRelationsModel() {
		Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
		setCursor(cur);
		String objid2 = getIDfromTable();
		DocInfo info = new DocInfo();
		relationsModel = info.updateRelationsModel(objid2, relationsModel);
		tblDocInfo.setModel(relationsModel);
		tblDocInfo.validate();
		Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
		setCursor(cur2);
	}

	private void updateRenditionsModel() {
		Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
		setCursor(cur);
		String objid2 = getIDfromTable();
		DocInfo info = new DocInfo();
		renditionsModel = info.updateRenditionsModel(objid2, renditionsModel);
		tblDocInfo.setModel(renditionsModel);
		tblDocInfo.validate();
		Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
		setCursor(cur2);

	}

	private void updateSubscrptionsTree(QuickClientMutableTreeNode currNode, Vector myVector) {
		String q = "select " + standardqueryattributes + additionalqueryattributes
				+ " from dm_folder where r_object_id in (select r.parent_id from dm_relation r, dm_user u  where r.relation_name='dm_subscription' and u.user_name = USER and u.r_object_id = r.child_id) order by object_name,r_object_id";
		IDfSession session = null;
		IDfCollection col = null;
		try {
			session = smanager.getSession();
			IDfQuery query = new DfQuery();
			query.setDQL(q);
			col = query.execute(session, IDfQuery.DF_READ_QUERY);
			while (col.next()) {
				String newID = col.getString("r_object_id");
				String objName = col.getString("object_name");
				int rLinkCnt = col.getInt("r_link_cnt");
				QuickClientMutableTreeNode newNode = new QuickClientMutableTreeNode(objName);
				DokuData data = new DokuData(newID);
				newNode.setDokuData(data);
				newNode.setObjType(col.getString("r_object_type"));
				treemodel.insertNodeInto(newNode, currNode, 0);
				if (rLinkCnt > 0) {
					QuickClientMutableTreeNode tempNode = new QuickClientMutableTreeNode(objName + "W");
					tempNode.setSpecialString(MainJFrame.DUMMYNODE);
					treemodel.insertNodeInto(tempNode, newNode, 0);
				}

			}
		} catch (DfException ex) {
			log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
		} finally {
			if (col != null) {
				try {
					col.close();
				} catch (DfException ex) {
				}
			}
			if (session != null) {
				smanager.releaseSession(session);
			}

		}
		Iterator i = myVector.iterator();
		while (i.hasNext()) {
			QuickClientMutableTreeNode a = (QuickClientMutableTreeNode) i.next();
			treemodel.removeNodeFromParent(a);
		}
		// treemodel.removeNodeFromParent(currNode2);

		folderTree.validate();
	}

	public void valueChanged(TreeSelectionEvent e) {
		/*
		 * String objID = null; IDfSession sess = null; JTree tree = (JTree)
		 * folderTree; TreePath path = tree.getSelectionPath(); if (path ==
		 * null) { return; } QuickClientMutableTreeNode selectedNode =
		 * (QuickClientMutableTreeNode) path.getLastPathComponent();
		 * 
		 * if (selectedNode.getLevel() == 1) { String nodename = (String)
		 * selectedNode.getUserObject(); if
		 * (nodename.equalsIgnoreCase("subscriptions")) { Cursor cur = new
		 * Cursor(Cursor.WAIT_CURSOR); setCursor(cur); String q = ""; q =
		 * "select " + standardqueryattributes + additionalqueryattributes +
		 * " from dm_sysobject where r_object_id in (select r.parent_id from dm_relation r, dm_user u  where r.relation_name='dm_subscription' and u.user_name = USER and u.r_object_id = r.child_id) order by object_name,r_object_id"
		 * ; if (showThumbnails) { if (showAllVersions) { q = "select " +
		 * standardqueryattributes + additionalqueryattributes +
		 * ",thumbnail_url  from dm_sysobject(ALL) where r_object_id in (select r.parent_id from dm_relation r, dm_user u  where r.relation_name='dm_subscription' and u.user_name = USER and u.r_object_id = r.child_id) order by object_name,r_object_id"
		 * ; } else { q = "select " + standardqueryattributes +
		 * additionalqueryattributes +
		 * ",thumbnail_url  from dm_sysobject where r_object_id in (select r.parent_id from dm_relation r, dm_user u  where r.relation_name='dm_subscription' and u.user_name = USER and u.r_object_id = r.child_id) order by object_name,r_object_id"
		 * ; } } else { if (showAllVersions) { q = "select " +
		 * standardqueryattributes + additionalqueryattributes +
		 * " from dm_sysobject(ALL) where r_object_id in (select r.parent_id from dm_relation r, dm_user u  where r.relation_name='dm_subscription' and u.user_name = USER and u.r_object_id = r.child_id) order by object_name,r_object_id"
		 * ; } else { q = "select " + standardqueryattributes +
		 * additionalqueryattributes +
		 * " from dm_sysobject where r_object_id in (select r.parent_id from dm_relation r, dm_user u  where r.relation_name='dm_subscription' and u.user_name = USER and u.r_object_id = r.child_id) order by object_name,r_object_id"
		 * ; } } IDfCollection col = null; IDfQuery query = new DfQuery();
		 * query.setDQL(q); try { sess = smanager.getSession();
		 * tablemodel.setRowCount(0); col = query.execute(sess,
		 * query.DF_READ_QUERY); Enumeration colenum = col.enumAttrs();
		 * 
		 * String tempvalue = ""; String repvalue = ""; Utils util = new
		 * Utils(); tablemodel = util.getModelFromCollection(col,
		 * showThumbnails, tablemodel); objectTable.validate(); } catch
		 * (DfException ex) { log.error(ex); } finally { if (col != null) { try
		 * { col.close(); } catch (DfException ex) { log.error(ex); } } if (sess
		 * != null) { smanager.releaseSession(sess); } Cursor cur2 = new
		 * Cursor(Cursor.DEFAULT_CURSOR); setCursor(cur2); } }
		 * 
		 * if (nodename.equalsIgnoreCase("home cabinet")) { objID =
		 * selectedNode.getDokuDataID(); if (objID == null) { return; } Cursor
		 * cur = new Cursor(Cursor.WAIT_CURSOR); setCursor(cur); String q = "";
		 * if (showThumbnails) { if (showAllVersions) { q = "select " +
		 * standardqueryattributes + additionalqueryattributes +
		 * ",thumbnail_url from dm_sysobject(ALL) where any i_folder_id = '" +
		 * objID + "' order by object_name,r_object_id"; } else { q = "select "
		 * + standardqueryattributes + additionalqueryattributes +
		 * ",thumbnail_url from dm_sysobject where any i_folder_id = '" + objID
		 * + "' order by object_name,r_object_id"; } } else { if
		 * (showAllVersions) { q = "select " + standardqueryattributes +
		 * additionalqueryattributes +
		 * " from dm_sysobject(ALL) where any i_folder_id = '" + objID +
		 * "' order by object_name,r_object_id"; } else { q = "select " +
		 * standardqueryattributes + additionalqueryattributes +
		 * " from dm_sysobject where any i_folder_id = '" + objID +
		 * "' order by object_name,r_object_id"; } } IDfCollection col = null;
		 * IDfQuery query = new DfQuery(); query.setDQL(q); try { sess =
		 * smanager.getSession(); String tempvalue = ""; String repvalue = "";
		 * 
		 * tablemodel.setRowCount(0); col = query.execute(sess,
		 * query.DF_READ_QUERY); Utils util = new Utils(); tablemodel =
		 * util.getModelFromCollection(col, showThumbnails, tablemodel);
		 * objectTable.validate(); } catch (DfException ex) { log.error(ex); }
		 * finally { if (col != null) { try { col.close(); } catch (DfException
		 * ex) { log.error(ex); } } if (sess != null) {
		 * smanager.releaseSession(sess); } Cursor cur2 = new
		 * Cursor(Cursor.DEFAULT_CURSOR); setCursor(cur2);
		 * 
		 * } } if (nodename.equalsIgnoreCase("cabinets")) { Cursor cur = new
		 * Cursor(Cursor.WAIT_CURSOR); setCursor(cur); String q = ""; if
		 * (showThumbnails) { q = "select " + standardqueryattributes +
		 * additionalqueryattributes +
		 * ",thumbnail_url from dm_cabinet order by object_name, r_object_id desc"
		 * ; } else { q = "select " + standardqueryattributes +
		 * additionalqueryattributes +
		 * " from dm_cabinet order by object_name, r_object_id desc"; }
		 * IDfCollection col = null; IDfQuery query = new DfQuery();
		 * query.setDQL(q); try { sess = smanager.getSession();
		 * tablemodel.setRowCount(0); col = query.execute(sess,
		 * query.DF_READ_QUERY); Utils util = new Utils(); tablemodel =
		 * util.getModelFromCollection(col, showThumbnails, tablemodel);
		 * 
		 * objectTable.validate(); } catch (DfException ex) { log.error(ex); }
		 * finally { if (col != null) { try { col.close(); } catch (DfException
		 * ex) { log.error(ex); } } if (sess != null) {
		 * smanager.releaseSession(sess); } Cursor cur2 = new
		 * Cursor(Cursor.DEFAULT_CURSOR); setCursor(cur2);
		 * 
		 * } } return; } objID = selectedNode.getDokuDataID(); if (objID ==
		 * null) { return; } IDfCollection col = null; IDfQuery query = new
		 * DfQuery(); if (showThumbnails) { if (showAllVersions) {
		 * query.setDQL("select " + standardqueryattributes +
		 * additionalqueryattributes +
		 * ",thumbnail_url from dm_sysobject(ALL) where any i_folder_id = '" +
		 * objID + "' order by r_object_id"); } else { query.setDQL("select " +
		 * standardqueryattributes + additionalqueryattributes +
		 * ",thumbnail_url from dm_sysobject where any i_folder_id = '" + objID
		 * + "' order by r_object_id"); } } else { if (showAllVersions) {
		 * query.setDQL("select " + standardqueryattributes +
		 * additionalqueryattributes +
		 * " from dm_sysobject(ALL) where any i_folder_id = '" + objID +
		 * "' order by r_object_id"); } else { query.setDQL("select " +
		 * standardqueryattributes + additionalqueryattributes +
		 * " from dm_sysobject where any i_folder_id = '" + objID +
		 * "' order by r_object_id"); } } try { sess = smanager.getSession();
		 * String tempvalue = ""; String repvalue = ""; Cursor cur = new
		 * Cursor(Cursor.WAIT_CURSOR); setCursor(cur);
		 * tablemodel.setRowCount(0); //System.out.println("kysely: " +
		 * query.getDQL()); col = query.execute(sess, query.DF_READ_QUERY);
		 * Utils util = new Utils(); tablemodel =
		 * util.getModelFromCollection(col, showThumbnails, tablemodel);
		 * 
		 * objectTable.validate(); } catch (DfException ex) { log.error(ex); }
		 * finally { if (col != null) { try { col.close(); } catch (DfException
		 * ex) { log.error(ex); } } if (sess != null) {
		 * smanager.releaseSession(sess); } Cursor cur2 = new
		 * Cursor(Cursor.DEFAULT_CURSOR); setCursor(cur2); }
		 */
	}

	private void updateTree() {

		String objID = null;
		String configname = cmbConfigNames.getSelectedItem().toString();
		IDfSession sess = null;
		JTree tree = (JTree) folderTree;
		TreePath path = tree.getSelectionPath();
		if (path == null) {
			return;
		}
		Utils util = new Utils();
		QuickClientMutableTreeNode selectedNode = (QuickClientMutableTreeNode) path.getLastPathComponent();
		lastselectedNode = selectedNode;
		int childcount = selectedNode.getChildCount();
		for (int ii = 0; ii < childcount; ii++) {
			QuickClientMutableTreeNode childnode = (QuickClientMutableTreeNode) selectedNode.getChildAt(ii);
			System.out.println(childnode.toString());
		}
		if (selectedNode.getLevel() == 1) {
			String nodename = (String) selectedNode.getUserObject();
			if (nodename.equalsIgnoreCase("checked out")) {
				Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
				setCursor(cur);
				String q = "";
				q = "select " + standardqueryattributes + additionalqueryattributes + " from dm_sysobject where r_lock_owner = USER order by object_name, r_object_id";
				if (showThumbnails) {
					if (showAllVersions) {
						q = "select " + standardqueryattributes + additionalqueryattributes + ",thumbnail_url  from dm_sysobject(ALL) where r_lock_owner = USER order by object_name, r_object_id";
					} else {
						q = "select " + standardqueryattributes + additionalqueryattributes + ",thumbnail_url  from dm_sysobject where r_lock_owner = USER order by object_name, r_object_id";
					}

				} else {
					if (showAllVersions) {
						q = "select " + standardqueryattributes + additionalqueryattributes + " from dm_sysobject(ALL) where r_lock_owner = USER order by object_name, r_object_id";
					} else {
						q = "select " + standardqueryattributes + additionalqueryattributes + " from dm_sysobject where r_lock_owner = USER order by object_name, r_object_id";
					}

				}
				IDfCollection col = null;
				IDfQuery query = new DfQuery();
				query.setDQL(q);
				try {
					sess = smanager.getSession();
					tablemodel.setRowCount(0);
					col = query.execute(sess, IDfQuery.DF_READ_QUERY);
//					Enumeration colenum = col.enumAttrs();
//
//					String tempvalue = "";
//					String repvalue = "";

					if (additionalfilter)
						tablemodel = util.getModelFromCollection(sess,configname, col, showThumbnails, tablemodel, null, nonsysobjattrs);
					else
						tablemodel = util.getModelFromCollection(sess, configname, col, showThumbnails, tablemodel, queryfilter, nonsysobjattrs);
					objectTable.validate();
					lblInfoLabel.setText(String.valueOf(objectTable.getRowCount()));
				} catch (DfException ex) {
					log.error(ex);
					SwingHelper.showErrorMessage(ex.getMessage(), ex.getMessage());
				} finally {
					if (col != null) {
						try {
							col.close();
						} catch (DfException ex) {
							log.error(ex);
							SwingHelper.showErrorMessage(ex.getMessage(), ex.getMessage());
						}

					}
					if (sess != null) {
						smanager.releaseSession(sess);
					}

					Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
					setCursor(cur2);
				}

			}
			if (nodename.equalsIgnoreCase("my rooms")) {
				Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
				setCursor(cur);
				String q = "";
				q = "select " + standardqueryattributes + additionalqueryattributes + " from dmc_room order by object_name,r_object_id";
				if (showThumbnails) {

					q = "select " + standardqueryattributes + additionalqueryattributes + ",thumbnail_url from dmc_room order by object_name,r_object_id";

				} else {
					q = "select " + standardqueryattributes + additionalqueryattributes + " from dmc_room order by object_name,r_object_id";

				}
				IDfCollection col = null;
				IDfQuery query = new DfQuery();
				query.setDQL(q);
				try {
					sess = smanager.getSession();
					tablemodel.setRowCount(0);
					col = query.execute(sess, IDfQuery.DF_READ_QUERY);
					Enumeration colenum = col.enumAttrs();

					String tempvalue = "";
					String repvalue = "";

					if (additionalfilter)
						tablemodel = util.getModelFromCollection(sess, configname, col, showThumbnails, tablemodel, queryfilter, nonsysobjattrs);
					else
						tablemodel = util.getModelFromCollection(sess, configname, col, showThumbnails, tablemodel, null, nonsysobjattrs);
					objectTable.validate();
					lblInfoLabel.setText(String.valueOf(objectTable.getRowCount()));
				} catch (DfException ex) {
					log.error(ex);
					SwingHelper.showErrorMessage(ex.getMessage(), ex.getMessage());
				} finally {
					if (col != null) {
						try {
							col.close();
						} catch (DfException ex) {
							log.error(ex);
						}

					}
					if (sess != null) {
						smanager.releaseSession(sess);
					}

					Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
					setCursor(cur2);
				}

			}
			if (nodename.equalsIgnoreCase("subscriptions")) {
				Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
				setCursor(cur);
				String q = "";
				q = "select " + standardqueryattributes + additionalqueryattributes
						+ " from dm_sysobject where r_object_id in (select r.parent_id from dm_relation r, dm_user u  where r.relation_name='dm_subscription' and u.user_name = USER and u.r_object_id = r.child_id) order by object_name,r_object_id";
				if (showThumbnails) {
					if (showAllVersions) {
						q = "select " + standardqueryattributes + additionalqueryattributes
								+ ",thumbnail_url  from dm_sysobject(ALL) where r_object_id in (select r.parent_id from dm_relation r, dm_user u  where r.relation_name='dm_subscription' and u.user_name = USER and u.r_object_id = r.child_id) order by object_name,r_object_id";
					} else {
						q = "select " + standardqueryattributes + additionalqueryattributes
								+ ",thumbnail_url  from dm_sysobject where r_object_id in (select r.parent_id from dm_relation r, dm_user u  where r.relation_name='dm_subscription' and u.user_name = USER and u.r_object_id = r.child_id) order by object_name,r_object_id";
					}

				} else {
					if (showAllVersions) {
						q = "select " + standardqueryattributes + additionalqueryattributes
								+ " from dm_sysobject(ALL) where r_object_id in (select r.parent_id from dm_relation r, dm_user u  where r.relation_name='dm_subscription' and u.user_name = USER and u.r_object_id = r.child_id) order by object_name,r_object_id";
					} else {
						q = "select " + standardqueryattributes + additionalqueryattributes
								+ " from dm_sysobject where r_object_id in (select r.parent_id from dm_relation r, dm_user u  where r.relation_name='dm_subscription' and u.user_name = USER and u.r_object_id = r.child_id) order by object_name,r_object_id";
					}

				}
				IDfCollection col = null;
				IDfQuery query = new DfQuery();
				query.setDQL(q);
				try {
					sess = smanager.getSession();
					tablemodel.setRowCount(0);
					col = query.execute(sess, IDfQuery.DF_READ_QUERY);
					Enumeration colenum = col.enumAttrs();

					String tempvalue = "";
					String repvalue = "";

					if (additionalfilter)
						tablemodel = util.getModelFromCollection(sess, configname, col, showThumbnails, tablemodel, queryfilter, nonsysobjattrs);
					else
						tablemodel = util.getModelFromCollection(sess, configname, col, showThumbnails, tablemodel, null, nonsysobjattrs);
					objectTable.validate();
					lblInfoLabel.setText(String.valueOf(objectTable.getRowCount()));
				} catch (DfException ex) {
					log.error(ex);
					SwingHelper.showErrorMessage(ex.getMessage(), ex.getMessage());
				} finally {
					if (col != null) {
						try {
							col.close();
						} catch (DfException ex) {
							log.error(ex);
						}

					}
					if (sess != null) {
						smanager.releaseSession(sess);
					}

					Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
					setCursor(cur2);
				}

			}
			if (nodename.equalsIgnoreCase("my files")) {

				
				Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
				setCursor(cur);
				String q = "";
				if (showThumbnails) {
					if (showAllVersions) {
						q = "select " + standardqueryattributes + additionalqueryattributes + ",thumbnail_url from dm_sysobject(ALL) where owner_name = USER order by object_name,r_object_id";
					} else {
						q = "select " + standardqueryattributes + additionalqueryattributes + ",thumbnail_url from dm_sysobject where owner_name = USER order by object_name,r_object_id";
					}

				} else {
					if (showAllVersions) {
						q = "select " + standardqueryattributes + additionalqueryattributes + " from dm_sysobject(ALL) where owner_name = USER order by object_name,r_object_id";
					} else {
						q = "select " + standardqueryattributes + additionalqueryattributes + " from dm_sysobject where owner_name = USER order by object_name,r_object_id";
					}
				}
				IDfCollection col = null;
				IDfQuery query = new DfQuery();
				query.setDQL(q);
				try {
					sess = smanager.getSession();
					String tempvalue = "";
					String repvalue = "";

					IDfPersistentObject pobj = sess.getObjectByQualification("dm_server_config");
					if (pobj != null) {
						String installowner = pobj.getString("r_install_owner");
						String currentuser = DocuSessionManager.getInstance().getUserName();
						if (installowner.equals(currentuser)) {
							SwingHelper.showInfoMessage("Will not perform", "This functionality has been disabled when logged on as installation owner, as operation might take very long time");
							return;
						}
					}
					
					tablemodel.setRowCount(0);
					col = query.execute(sess, IDfQuery.DF_READ_QUERY);

					if (additionalfilter)
						tablemodel = util.getModelFromCollection(sess, configname, col, showThumbnails, tablemodel, queryfilter, nonsysobjattrs);
					else
						tablemodel = util.getModelFromCollection(sess, configname, col, showThumbnails, tablemodel, null, nonsysobjattrs);
					objectTable.validate();
					lblInfoLabel.setText(String.valueOf(objectTable.getRowCount()));
				} catch (DfException ex) {
					log.error(ex);
					SwingHelper.showErrorMessage(ex.getMessage(), ex.getMessage());
				} finally {
					if (col != null) {
						try {
							col.close();
						} catch (DfException ex) {
							log.error(ex);
						}

					}
					if (sess != null) {
						smanager.releaseSession(sess);
					}

					Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
					setCursor(cur2);

				}

			}
			if (nodename.equalsIgnoreCase("home cabinet")) {
				objID = selectedNode.getDokuDataID();
				if (objID == null) {
					return;
				}

				Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
				setCursor(cur);
				String q = "";
				if (showThumbnails) {
					if (showAllVersions) {
						q = "select " + standardqueryattributes + additionalqueryattributes + ",thumbnail_url from dm_sysobject(ALL) where any i_folder_id = '" + objID + "' order by object_name,r_object_id";
					} else {
						q = "select " + standardqueryattributes + additionalqueryattributes + ",thumbnail_url from dm_sysobject where any i_folder_id = '" + objID + "' order by object_name,r_object_id";
					}

				} else {
					if (showAllVersions) {
						q = "select " + standardqueryattributes + additionalqueryattributes + " from dm_sysobject(ALL) where any i_folder_id = '" + objID + "' order by object_name,r_object_id";
					} else {
						q = "select " + standardqueryattributes + additionalqueryattributes + " from dm_sysobject where any i_folder_id = '" + objID + "' order by object_name,r_object_id";
					}

				}
				IDfCollection col = null;
				IDfQuery query = new DfQuery();
				query.setDQL(q);
				try {
					sess = smanager.getSession();
					String tempvalue = "";
					String repvalue = "";

					tablemodel.setRowCount(0);
					col = query.execute(sess, IDfQuery.DF_READ_QUERY);

					if (additionalfilter)
						tablemodel = util.getModelFromCollection(sess, configname, col, showThumbnails, tablemodel, queryfilter, nonsysobjattrs);
					else
						tablemodel = util.getModelFromCollection(sess, configname, col, showThumbnails, tablemodel, null, nonsysobjattrs);
					objectTable.validate();
					lblInfoLabel.setText(String.valueOf(objectTable.getRowCount()));
				} catch (DfException ex) {
					log.error(ex);
					SwingHelper.showErrorMessage(ex.getMessage(), ex.getMessage());
				} finally {
					if (col != null) {
						try {
							col.close();
						} catch (DfException ex) {
							log.error(ex);
						}

					}
					if (sess != null) {
						smanager.releaseSession(sess);
					}

					Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
					setCursor(cur2);

				}

			}
			if (nodename.equalsIgnoreCase("cabinets")) {
				Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
				setCursor(cur);
				String q = "";
				if (showThumbnails) {
					q = "select " + standardqueryattributes + additionalqueryattributes + ",thumbnail_url from dm_cabinet order by object_name, r_object_id desc";
				} else {
					q = "select " + standardqueryattributes + additionalqueryattributes + " from dm_cabinet order by object_name, r_object_id desc";
				}

				IDfCollection col = null;
				IDfQuery query = new DfQuery();
				query.setDQL(q);
				try {
					sess = smanager.getSession();
					if (sess == null)
						return;
					tablemodel.setRowCount(0);
					col = query.execute(sess, IDfQuery.DF_READ_QUERY);

					if (additionalfilter)
						tablemodel = util.getModelFromCollection(sess, configname, col, showThumbnails, tablemodel, queryfilter, nonsysobjattrs);
					else
						tablemodel = util.getModelFromCollection(sess, configname, col, showThumbnails, tablemodel, null, nonsysobjattrs);

					objectTable.validate();
					lblInfoLabel.setText(String.valueOf(objectTable.getRowCount()));
				} catch (DfException ex) {
					log.error(ex);
					SwingHelper.showErrorMessage(ex.getMessage(), ex.getMessage());
				} finally {
					if (col != null) {
						try {
							col.close();
						} catch (DfException ex) {
							log.error(ex);
						}

					}
					if (sess != null) {
						smanager.releaseSession(sess);
					}

					Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
					setCursor(cur2);

				}

			}

			return;
		}

		objID = selectedNode.getDokuDataID();
		if (objID == null) {
			return;
		}

		IDfCollection col = null;
		IDfQuery query = new DfQuery();
		if (showThumbnails) {
			if (showAllVersions) {
				query.setDQL("select " + standardqueryattributes + additionalqueryattributes + ",thumbnail_url from dm_sysobject(ALL) where any i_folder_id = '" + objID + "' order by " + orderbyString + ",r_object_id");
			} else {
				query.setDQL("select " + standardqueryattributes + additionalqueryattributes + ",thumbnail_url from dm_sysobject where any i_folder_id = '" + objID + "' order by " + orderbyString + ",r_object_id");
			}

		} else {
			if (showAllVersions) {
				query.setDQL("select " + standardqueryattributes + additionalqueryattributes + " from dm_sysobject(ALL) where any i_folder_id = '" + objID + "' order by " + orderbyString + ",r_object_id");
			} else {
				query.setDQL("select " + standardqueryattributes + additionalqueryattributes + " from dm_sysobject where any i_folder_id = '" + objID + "' order by " + orderbyString + ",r_object_id");
			}

		}
		try {
			sess = smanager.getSession();
			String tempvalue = "";
			String repvalue = "";
			Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
			setCursor(cur);
			tablemodel.setRowCount(0);
			col = query.execute(sess, IDfQuery.DF_READ_QUERY);

			if (additionalfilter)
				tablemodel = util.getModelFromCollection(sess, configname, col, showThumbnails, tablemodel, queryfilter, nonsysobjattrs);
			else
				tablemodel = util.getModelFromCollection(sess, configname, col, showThumbnails, tablemodel, null, nonsysobjattrs);

			objectTable.validate();
			lblInfoLabel.setText(String.valueOf(objectTable.getRowCount()));
		} catch (DfException ex) {
			log.error(ex);
			SwingHelper.showErrorMessage(ex.getMessage(), ex.getMessage());
		} finally {
			if (col != null) {
				try {
					col.close();
				} catch (DfException ex) {
					log.error(ex);
				}

			}
			if (sess != null) {
				smanager.releaseSession(sess);
			}

			Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
			setCursor(cur2);
		}
		if (util != null) {
			System.out.println(util.getFoldernames());
			int childcount1 = selectedNode.getChildCount();
			ArrayList<QuickClientMutableTreeNode> delelist = new ArrayList<QuickClientMutableTreeNode>();
			for (int ii = 0; ii < childcount1; ii++) {
				QuickClientMutableTreeNode childnode = (QuickClientMutableTreeNode) selectedNode.getChildAt(ii);
				String nodename = childnode.toString();
				if (!util.getFoldernames().contains(nodename)) {
					if (!childnode.getSpecialString().equals(MainJFrame.DUMMYNODE))
						delelist.add(childnode);
				}

			}
			for (QuickClientMutableTreeNode node : delelist) {
				treemodel.removeNodeFromParent(node);
			}

		}
	}

	public ActionListener getTreeChangeListener() {
		return treeChangeListener;
	}

	private void updateVersionsModel() {
		Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
		setCursor(cur);
		DocInfo info = new DocInfo();
		String objid = getIDfromTable();
		if (objid.length() == 16) {
			versionsModel = info.updateVersionsModel(objid, versionsModel);
			tblDocInfo.setModel(versionsModel);
			tblDocInfo.validate();
		}
		Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
		setCursor(cur2);
	}
}
