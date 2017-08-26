/*
 * SearchFrame.java
 *
 * Created on 30. joulukuuta 2007, 22:55
 */
package org.quickclient.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.quickclient.classes.ActionExecutor;
import org.quickclient.classes.DocInfo;
import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.DokuData;
import org.quickclient.classes.IQuickTableModel;
import org.quickclient.classes.ObjectTableTransferHandler;


/**
 *
 * @author  Administrator
 */
public class SearchFrame extends javax.swing.JInternalFrame implements IQuickTableModel {

    private String selectedID;
    private DefaultTableModel model;
    private TablePopUpMenu myPopUp;
    DocuSessionManager smanager;
    TablePopUpMenu docInfoPopUp;
    PermissionPopUp permPopUp;
    LocationsPopUp locationsPopup;
    private boolean showThumbnails;
    private FormatRenderer formatrenderer;
    private DefaultTableModel relationsModel;

    /** Creates new form SearchFrame */
    public SearchFrame() {
        initComponents();

    }

    public SearchFrame(DefaultTableModel model, boolean showthumb) {
        Cursor cur = new Cursor(Cursor.WAIT_CURSOR);

        setCursor(cur);
        docinfoVisible = false;
        formatrenderer = new FormatRenderer();
        formatrenderer.setShowThumbnails(showthumb);
        DocInfo docinfo = new DocInfo();
        this.showThumbnails = showthumb;
        initComponents();
        smanager = DocuSessionManager.getInstance();
        this.model = model;
        int rowcount = model.getRowCount();
        this.lblRowCount.setText("   " + rowcount + " rows.");
        this.lblRowCount.setVisible(true);
        jScrollPane1.getViewport().setBackground(Color.WHITE);
        initTable();
        initializeVersionTableModel();

        this.locationsModel = docinfo.initializeLocationsTableModel();
        this.permissionlistModel = docinfo.initializePermissionListTableModel();
        this.renditionsModel = docinfo.initializeRenditionsTableModel();
        this.relationsModel = docinfo.initializeRelationsTableModel();
        this.versionsModel = docinfo.initializeLocationsVersionsModel();
        initVersionView();

        objectTable.validate();
        myPopUp = new TablePopUpMenu();
        myPopUp.setTable(objectTable);
        docInfoPopUp = new TablePopUpMenu();
        docInfoPopUp.setTable(tblDocInfo);
        permPopUp = new PermissionPopUp();
        permPopUp.setTable(tblDocInfo);
        locationsPopup = new LocationsPopUp();
        locationsPopup.setTable(tblDocInfo);
        jPanel2.setVisible(false);
        jSplitPane1.setDividerSize(0);
        objectTable.setDragEnabled(true);
        ObjectTableTransferHandler ott = new ObjectTableTransferHandler();
        ott.setJtable(objectTable);
        ott.setJMenu(new JPopupMenu());
        objectTable.setTransferHandler(ott);
        Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
        setCursor(cur2);
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

    private void initializeVersionTableModel() {

        tblDocInfo.getColumnModel().getColumn(0).setCellRenderer(new LockRenderer());
        tblDocInfo.getColumnModel().getColumn(1).setCellRenderer(new FormatRenderer());
        tblDocInfo.getColumnModel().getColumn(0).setPreferredWidth(22);
        tblDocInfo.getColumnModel().getColumn(0).setMaxWidth(22);
        tblDocInfo.getColumnModel().getColumn(1).setPreferredWidth(22);
        tblDocInfo.getColumnModel().getColumn(1).setMaxWidth(22);
        int lastIndex = tblDocInfo.getColumnCount();
        tblDocInfo.getColumnModel().removeColumn(tblDocInfo.getColumnModel().getColumn(lastIndex - 1));
    }

    private void initTable() {
        objectTable.setModel(model);
        objectTable.getColumnModel().getColumn(0).setCellRenderer(new LockRenderer());
        objectTable.getColumnModel().getColumn(1).setCellRenderer(formatrenderer);
        objectTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        objectTable.setEditingRow(1);
        objectTable.setRowHeight(22);
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
        if (showThumbnails) {
            formatrenderer.setShowThumbnails(true);
            objectTable.setRowHeight(110);
            TableColumn col = objectTable.getColumnModel().getColumn(1);
            col.setMaxWidth(110);
            col.setPreferredWidth(110);
            col.setWidth(110);
            showThumbnails = true;
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        objectTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        panelDocInfoMenuBar = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        cmbDocInfoChooser = new javax.swing.JComboBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblDocInfo = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        jToggleButton1 = new javax.swing.JToggleButton();
        lblRowCount = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);

        jSplitPane1.setDividerLocation(333);
        jSplitPane1.setDividerSize(3);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setResizeWeight(0.5);
        jSplitPane1.setOneTouchExpandable(true);

        objectTable.setAutoCreateRowSorter(true);
        objectTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
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
        jScrollPane1.setViewportView(objectTable);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 792, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 332, Short.MAX_VALUE)
        );

        jSplitPane1.setTopComponent(jPanel1);

        panelDocInfoMenuBar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        panelDocInfoMenuBar.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel5.setText("  View: ");
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

        tblDocInfo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Title 1", "Title 2"
            }
        ));
        tblDocInfo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblDocInfoMouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tblDocInfo);

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelDocInfoMenuBar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 792, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 792, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(panelDocInfoMenuBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE))
        );

        jSplitPane1.setRightComponent(jPanel2);

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 794, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE)
        );

        jToolBar1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jToggleButton1.setFont(new java.awt.Font("Tahoma", 1, 11));
        jToggleButton1.setText("  Info Panel  ");
        jToggleButton1.setToolTipText("Toggle Docinfo On/Off");
        jToggleButton1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jToggleButton1.setFocusable(false);
        jToggleButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButton1);

        lblRowCount.setText("jLabel1");
        jToolBar1.add(lblRowCount);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jToolBar1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 794, Short.MAX_VALUE)
            .add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jToolBar1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void objectTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_objectTableMouseReleased
        int butt = evt.getButton();
        if (butt == MouseEvent.BUTTON3) {
            myPopUp.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_objectTableMouseReleased

    private void cmbDocInfoChooserItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbDocInfoChooserItemStateChanged
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

    }//GEN-LAST:event_cmbDocInfoChooserItemStateChanged

    private void tblDocInfoMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDocInfoMouseReleased

        int butt = evt.getButton();
        if (butt == MouseEvent.BUTTON3) {
            if (DCINFO_CURRENT_VALUE == DCINFO_VERSIONS) {
                docInfoPopUp.show(evt.getComponent(), evt.getX(), evt.getY());
            }
            if (DCINFO_CURRENT_VALUE == DCINFO_PERMISSIONS) {
                //PermissionsPopUp.show(evt.getComponent(), evt.getX(), evt.getY());
                permPopUp.setSelectedId(getIDfromTable());
                permPopUp.show(evt.getComponent(), evt.getX(), evt.getY());
            }
            if (DCINFO_CURRENT_VALUE == DCINFO_LOCATIONS) {
                //locationsPopUp.show(evt.getComponent(), evt.getX(), evt.getY());
                //locationsPopUp.show(evt.getComponent(), evt.getX(), evt.getY());
                locationsPopup.setSelectedId(getIDfromTable());
                locationsPopup.show(evt.getComponent(), evt.getX(), evt.getY());
            }
            if (DCINFO_CURRENT_VALUE == DCINFO_RENDITIONS) {
//                renditionsPopUp.show(evt.getComponent(), evt.getX(), evt.getY());
            }
        }
    }//GEN-LAST:event_tblDocInfoMouseReleased

    private void objectTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_objectTableMouseClicked


        if (evt.getClickCount() == 2) {
            String objid = getIDfromTable();
            if (!objid.startsWith("0b") && !objid.startsWith("0c")) {
                ActionExecutor ex = new ActionExecutor();
                Vector v = new Vector();
                v.add(objid);
                ex.viewAction(v);
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
    }//GEN-LAST:event_objectTableMouseClicked

private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
    if (docinfoVisible) {
        jPanel2.setVisible(false);
        jSplitPane1.setDividerSize(0);
        docinfoVisible = false;
    } else {
        jPanel2.setVisible(true);
        jSplitPane1.setDividerSize(2);
        jSplitPane1.setDividerLocation(0.66);
        docinfoVisible = true;
    }
}//GEN-LAST:event_jToggleButton1ActionPerformed

private void objectTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_objectTableMousePressed
/*    Point p = evt.getPoint();
    int row = objectTable.rowAtPoint(p);
    ListSelectionModel selectionModel = objectTable.getSelectionModel();
    selectionModel.setSelectionInterval(row, row);*/
}//GEN-LAST:event_objectTableMousePressed

private void objectTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_objectTableKeyReleased
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
}//GEN-LAST:event_objectTableKeyReleased

private void cmbDocInfoChooserMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_cmbDocInfoChooserMouseWheelMoved
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
}//GEN-LAST:event_cmbDocInfoChooserMouseWheelMoved
    private boolean docinfoVisible;
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
    DefaultTableModel locationsModel;
    DefaultTableModel permissionlistModel;
    DefaultTableModel renditionsModel;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbDocInfoChooser;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblRowCount;
    private javax.swing.JTable objectTable;
    private javax.swing.JPanel panelDocInfoMenuBar;
    private javax.swing.JTable tblDocInfo;
    // End of variables declaration//GEN-END:variables

    public DefaultTableModel getModel() {
        return model;
    }

    public void setModel(DefaultTableModel model) {
        this.model = model;
    }

    public String getIDfromTable() {
        int row = objectTable.getSelectedRow();
        String objid = "";
        if (row != -1) {
        Vector v = (Vector) model.getDataVector().elementAt(row);
        System.out.println(v);
        DokuData d = (DokuData) v.lastElement();
        objid = d.getObjID();
        
        }
        return objid;
    }

    private void updateLocationsModel() {
        DocInfo info = new DocInfo();
        String objid = getIDfromTable();
        locationsModel = info.updateLocationsModel(objid, locationsModel);
        tblDocInfo.setModel(locationsModel);
        tblDocInfo.validate();
    }

    private void updatePermissionModel() {

        String objid = getIDfromTable();
        DocInfo info = new DocInfo();
        permissionlistModel = info.updatePermissionsModel(objid, permissionlistModel);
        tblDocInfo.setModel(permissionlistModel);
        tblDocInfo.validate();

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

    private void updateVersionsModel() {
        Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
        setCursor(cur);
        DocInfo info = new DocInfo();
        String objid = getIDfromTable();
        versionsModel = info.updateVersionsModel(objid, versionsModel);
        tblDocInfo.setModel(versionsModel);
        tblDocInfo.validate();
        Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
        setCursor(cur2);
    }
}
