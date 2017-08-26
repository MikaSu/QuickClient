/*
 * PropertiesFrame.java
 *
 * Created on 25. kesäkuuta 2007, 23:59
 */
package org.quickclient.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;
import org.quickclient.classes.AclBrowserData;
import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.ExJTextField;
import org.quickclient.classes.Utils;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.DfType;
import com.documentum.fc.client.IDfACL;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfType;
import com.documentum.fc.client.IDfValidator;
import com.documentum.fc.client.IDfValueAssistance;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.IDfId;
import com.documentum.fc.common.IDfList;


/**
 *
 * @author  Administrator
 */
public class PropertiesFrame extends javax.swing.JFrame {

    DocuSessionManager smanager;
    Logger log = Logger.getLogger(PropertiesFrame.class);

    /** Creates new form PropertiesFrame */
    public PropertiesFrame() {
        smanager = DocuSessionManager.getInstance();
        initComponents();

    }

    public void initData() {
        String objType = "";
        IDfSysObject obj = null;
        IDfType type = null;


        txtObjectName.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                txtObjectName.putClientProperty("haschanged", true);
            }

            public void removeUpdate(DocumentEvent e) {
                txtObjectName.putClientProperty("haschanged", true);
            }

            public void changedUpdate(DocumentEvent e) {
            }
        });
        IDfSession session2 = null;
        try {
            session2 = smanager.getSession();
            IDfId id = new DfId(objid);
            obj = (IDfSysObject) session2.getObject(id);

            IDfId polid = obj.getPolicyId();
            if (!polid.toString().equals("0000000000000000")) {
                IDfSysObject pol = (IDfSysObject) session2.getObject(polid);
                lblLCName.setText(pol.getObjectName());
                lblCurrState.setText(obj.getCurrentStateName());
            } else {
                lblLCName.setText("-");
                lblCurrState.setText("-");
            }
            type = obj.getType();
            objType = obj.getString("r_object_type");
            lblCreated.setText(obj.getString("r_creation_date"));
            lblCreator.setText(obj.getString("r_creator_name"));
            lblFormat.setText(obj.getString("a_content_type"));
            txtObjectName.setText(obj.getObjectName());
            txtObjectName.putClientProperty("haschanged", false);

            lblType.setText(objType);
            lblSize.setText(obj.getString("r_content_size"));
            txtObjID.setText(objid);
            lblModified.setText(obj.getString("r_modify_date"));
            lblOwner.setText(obj.getOwnerName());
            DefaultListModel model = new DefaultListModel();
            int ifoldervalue = obj.getValueCount("i_folder_id");
            for (int i = 0; i < ifoldervalue; i++) {
                IDfId iFolderId = obj.getRepeatingId("i_folder_id", i);
                IDfSysObject folderobj = (IDfSysObject) session2.getObject(iFolderId);
                String path = folderobj.getRepeatingString("r_folder_path", 0);
                model.addElement(path);
            }
            lstLocations.setModel(model);
            lstLocations.validate();
        } catch (DfException ex) {
            log.error(ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (session2 != null) {
                smanager.releaseSession(session2);
            }
        }

        IDfCollection col = null;
        IDfCollection col2 = null;
        IDfSession session = null;
        try {
            session = smanager.getSession();
            IDfQuery query = new DfQuery();
            IDfQuery query2 = new DfQuery();
            query.setDQL("select object_name from dm_display_config where attribute_source = '" + objType + "'");
            col = query.execute(session, DfQuery.DF_READ_QUERY);
            while (col.next()) {
                String objname = col.getString("object_name");
                ////System.out.println(objname);

                JPanel newPanel = new javax.swing.JPanel();
                newPanel.setLayout(new MigLayout("wrap 3"));
                //newPanel.setLayout(new GridLayout(0, 3));
                Dimension preferredSize = new Dimension(400, 800);
                newPanel.setPreferredSize(preferredSize);
                LineBorder b = new LineBorder(Color.BLACK);
                newPanel.setBorder(b);
                //  newPanel2.setLayout(new Grid)
                JScrollPane newPanel2 = new JScrollPane(newPanel);

// oli JPanel                JScrollPane newPanel = new javax.swing.JScrollPane();
                jTabbedPane1.addTab(objname, newPanel2);
                // GridLayout experimentLayout = new GridLayout(0, 3);
                //ScrollPaneLayout experimentLayout = new ScrollPaneLayout();
//                SpringLayout layout = new SpringLayout();

                query2.setDQL("select attribute_name from dm_display_config where attribute_source = '" + objType + "' and object_name = '" + objname + "'");
                col2 = query2.execute(session, DfQuery.DF_READ_QUERY);
                int rowcounter = 0;
                while (col2.next()) {
                    rowcounter++;
                    javax.swing.JLabel newLabel = new javax.swing.JLabel();
                    //"";
                    final String attrName = col2.getString("attribute_name");
                    newLabel.setText(attrName);
                    newPanel.add(newLabel);
                    boolean isRepeating = false;
                    isRepeating = type.isTypeAttrRepeating(attrName);
//                    int iTypes = type.getAttrDataType(attrName);
                    int iTypeIndex = type.findTypeAttrIndex(attrName);
                    int iType = 1;
                    if (iTypeIndex != -1) {
                        iType = type.getTypeAttrDataTypeAt(iTypeIndex);
                    }
//                    ////System.out.println("iType: " + iType);
//                    type.isTypeAttrRepeatingAt(iType);
                    IDfValidator v = obj.getValidator();
                    IDfValueAssistance va = v.getValueAssistance(attrName, null);

                    if (va == null) {
                        if (iType == DfType.DF_BOOLEAN) {
                            JCheckBox cb = new JCheckBox();
                            cb.setName(attrName);
                            cb.putClientProperty("haschanged", false);
                            newPanel.add(cb);
                        } else {
                            if (isRepeating) {
                                final JTable table = new JTable();
                                DefaultTableModel model = new DefaultTableModel();
                                model.setColumnCount(1);
                                table.setTableHeader(null);
                                for (int i = 0; i < obj.getValueCount(attrName); i++) {
                                    Vector<Object> joo = new Vector<Object>();
                                    joo.add(obj.getRepeatingString(attrName, i));
                                    model.addRow(joo);
//                                    listmodel.addElement(obj.getRepeatingString(attrName,i));
                                }
                                JScrollPane scrollpane = new JScrollPane();
                                scrollpane.addMouseListener(new java.awt.event.MouseAdapter() {

                                    @Override
                                    public void mouseReleased(java.awt.event.MouseEvent evt) {
                                        ////System.out.println(evt);
                                        JScrollPane component = (JScrollPane) evt.getComponent();
                                        ////System.out.println(component);
                                        for (int i = 0; i < component.getComponentCount(); i++) {
                                            Component jeejee = component.getComponent(i);
                                            ////System.out.println(jeejee);
                                            if (jeejee instanceof JViewport) {
                                                JViewport port = (JViewport) jeejee;
                                                for (int j = 0; j < port.getComponentCount(); j++) {
                                                    Component a = port.getComponent(j);
                                                    if (a instanceof JTable) {
                                                        JTable table = (JTable) a;
                                                        DefaultTableModel model = (DefaultTableModel) table.getModel();
                                                        Vector<String> joo = new Vector<String>();
                                                        joo.add("");
                                                        model.addRow(joo);
                                                        table.validate();
                                                    }
                                                }

                                            }
                                        }
                                    }
                                });

                                scrollpane.setViewportView(table);
                                table.setModel(model);
                                table.validate();
                                newPanel.add(scrollpane, "w 240!, h 120!");
                                table.setName(attrName);
                                table.addMouseListener(new java.awt.event.MouseAdapter() {

                                    @Override
                                    public void mouseReleased(java.awt.event.MouseEvent evt) {
                                        int butt = evt.getButton();
                                        if (butt == MouseEvent.BUTTON3) {
                                            TablePopUp.show(evt.getComponent(), evt.getX(), evt.getY());
                                        }
                                    }
                                });

//                                table.set
//                                JScrollPane scrollpane = new JScrollPane();
//                                scrollpane.setViewportView(list);
//                                list.setModel(listmodel);
//
//                                list.validate();
//                                newPanel.add(scrollpane);
//                                list.setName(attrName);


//                                JList list = new JList();
//                                DefaultListModel listmodel = new DefaultListModel();
//                                for(int i=0;i<obj.getValueCount(attrName);i++) {
//                                    listmodel.addElement(obj.getRepeatingString(attrName,i));
//                                }
//                                JScrollPane scrollpane = new JScrollPane();
//                                scrollpane.setViewportView(list);
//                                list.setModel(listmodel);
//
//                                list.validate();
//                                newPanel.add(scrollpane);
//                                list.setName(attrName);

                            } else {
                                final ExJTextField textField = new ExJTextField();
                                //TODO chekkaa miksi tässä oli QTextField
                                textField.setName(attrName);
                                newPanel.add(textField, "w 240!");
                                textField.setText(obj.getString(attrName));
                                //textField.setHasChanged(false);
                                textField.putClientProperty("haschanged", false);
                                textField.getDocument().addDocumentListener(new DocumentListener() {

                                    public void insertUpdate(DocumentEvent e) {
                                        //   textField.setHasChanged(true);
                                        textField.putClientProperty("haschanged", true);
                                    }

                                    public void removeUpdate(DocumentEvent e) {
                                        // textField.setHasChanged(true);
                                        textField.putClientProperty("haschanged", true);

                                    }

                                    public void changedUpdate(DocumentEvent e) {
                                    }
                                });
                            }
                        }
                    } else {
                        IDfList anus = va.getActualValues();
                        if (!isRepeating) {
                            final JComboBox combo = new JComboBox();
                            newPanel.add(combo, "w 240!");

                            int selval = 0;
                            combo.addItem(" ");
                            String singleVal = obj.getString(attrName);
                            for (int i = 0; i < anus.getCount(); i++) {
                                String vaVal = (String) anus.get(i);
                                if (vaVal.equals(singleVal)) {
                                    selval = i + 1;
                                }
                                combo.addItem(vaVal);
                            }
                            combo.setSelectedIndex(selval);
                            combo.addMouseWheelListener(new MouseWheelListener() {

                                public void mouseWheelMoved(MouseWheelEvent evt) {
                                    ////System.out.println(evt);
                                    int maxindex = combo.getItemCount();
                                    if (evt.getWheelRotation() > 0) {
                                        int index = combo.getSelectedIndex();
                                        if (index < maxindex - 1) {
                                            combo.setSelectedIndex(index + 1);
                                        }
                                    } else {
                                        int index = combo.getSelectedIndex();
                                        if (index > 0) {
                                            combo.setSelectedIndex(index - 1);
                                        }
                                    }
                                }
                            });
                            combo.setName(attrName);
                            combo.addItemListener(new ItemListener() {

                                public void itemStateChanged(ItemEvent e) {
                                    JComboBox combo = (JComboBox) e.getSource();
                                    ////System.out.println(combo.getName());
                                    combo.putClientProperty("haschanged", true);
                                }
                            });
                            combo.putClientProperty("haschanged", false);
                        } else {
                            JList list = new JList();
                            list.putClientProperty("haschanged", false);
                            DefaultListModel listmodel = new DefaultListModel();
                            for (int i = 0; i < obj.getValueCount(attrName); i++) {
                                listmodel.addElement(obj.getRepeatingString(attrName, i));
                            }
                            JScrollPane scrollpane = new JScrollPane();
                            scrollpane.setViewportView(list);
                            list.setModel(listmodel);
                            list.validate();
                            newPanel.add(scrollpane, "w 240!, h 120!");
                            list.setName(attrName);

                        }
                    }

                    if (isRepeating == true && va != null) {
                        JButton butt = new JButton("Choose");
                        butt.setName(attrName);
                        ActionListener buttonlistener = new ActionListener() {

                            public void actionPerformed(ActionEvent e) {
                                JButton button = (JButton) e.getSource();
                                String buttName = button.getName();
                                final Vector repeatingData = new Vector();
                                ActionListener returnlistener = new ActionListener() {

                                    public void actionPerformed(ActionEvent e) {

                                        ////System.out.println(repeatingData);
                                        Component component = null;
                                        Component component2 = null;
                                        String x = "";
                                        String name = "";
                                        JScrollPane anus = (JScrollPane) jTabbedPane1.getSelectedComponent();
                                        JViewport jvp = (JViewport) anus.getComponent(0);
                                        JPanel panel = (JPanel) jvp.getComponent(0);
                                        for (int i = 0; i < panel.getComponentCount(); i++) {
                                            component = panel.getComponent(i);

                                            if (component instanceof JScrollPane) {
                                                JScrollPane jsp = (JScrollPane) component;
                                                JViewport jvp2 = (JViewport) jsp.getComponent(0);
                                                for (int j = 0; j < jvp2.getComponentCount(); j++) {
                                                    component2 = jvp2.getComponent(j);
                                                    if (component2 instanceof JList) {
                                                        JList jlista = (JList) component2;
                                                        name = jlista.getName();
                                                        ////System.out.println(name);
                                                        ////System.out.println("attrName" + attrName);
                                                        if (name.equals(attrName)) {
                                                            jlista.putClientProperty("haschanged", true);
                                                            DefaultListModel listmodel = new DefaultListModel();
                                                            for (int xx = 0; xx < repeatingData.size(); xx++) {
                                                                listmodel.addElement(repeatingData.get(xx));
                                                            }
                                                            jlista.setModel(listmodel);
                                                            jlista.validate();
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                };

                                ////System.out.println(buttName);
                                final IDfSession isessio = smanager.getSession();
                                try {
                                    IDfId id = new DfId(objid);
                                    final IDfSysObject obj2 = (IDfSysObject) isessio.getObject(id);
                                    RepeatingAttrValueChooser frame = new RepeatingAttrValueChooser(obj2, buttName, repeatingData, returnlistener);

                                    frame.setVisible(true);
                                } catch (DfException ex) {
                                    log.error(ex);
                                } finally {
                                    if (isessio != null) {
                                        smanager.releaseSession(isessio);
                                    }
                                }

                            }
                        };

                        butt.addActionListener(buttonlistener);
                        newPanel.add(butt);
                        butt.setVisible(true);
                    } else {
                        newPanel.add(new JLabel(""));
                    }
                }
                if (col2 != null) {
                    col2.close();
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
                    log.error(ex);
                }
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        TablePopUp = new javax.swing.JPopupMenu();
        mnuAddValue = new javax.swing.JMenuItem();
        ViewportPopUp = new javax.swing.JPopupMenu();
        mnuappendValue = new javax.swing.JMenuItem();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstLocations = new javax.swing.JList();
        lblType = new javax.swing.JLabel();
        lblFormat = new javax.swing.JLabel();
        lblSize = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lblOwner = new javax.swing.JLabel();
        cmdChangeOwner = new javax.swing.JButton();
        lblCreated = new javax.swing.JLabel();
        lblModified = new javax.swing.JLabel();
        lblCreator = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblLCName = new javax.swing.JLabel();
        lblCurrState = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtObjectName = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        lblCurrentPermissions = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        cmdChangeACL = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        lblAclDescription = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPermissions = new javax.swing.JTable();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        lblAclIsInternal = new javax.swing.JLabel();
        lblAclName = new javax.swing.JLabel();
        lblAclOwner = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblRenditions = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        txtObjID = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblVersions = new javax.swing.JTable();
        cmdClose = new javax.swing.JButton();
        cmdSave = new javax.swing.JButton();

        mnuAddValue.setText("Item");
        TablePopUp.add(mnuAddValue);

        mnuappendValue.setText("Item");
        ViewportPopUp.add(mnuappendValue);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Properties");

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        jLabel1.setText("Object Name:");

        jLabel2.setText("Type:");

        jLabel3.setText("Format:");

        jLabel4.setText("Size:");

        jLabel5.setText("Locations:");

        jScrollPane1.setViewportView(lstLocations);

        lblType.setText("jLabel6");

        lblFormat.setText("jLabel7");

        lblSize.setText("jLabel8");

        jLabel9.setText("Created:");

        jLabel10.setText("Modified:");

        jLabel11.setText("Creator:");

        jLabel12.setText("Owner:");

        lblOwner.setText("jLabel13");

        cmdChangeOwner.setText("Change Owner");
        cmdChangeOwner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdChangeOwnerActionPerformed(evt);
            }
        });

        lblCreated.setText("jLabel6");

        lblModified.setText("jLabel7");

        lblCreator.setText("jLabel8");

        jLabel6.setText("Lifecycle:");

        lblLCName.setText("jLabel15");

        lblCurrState.setText("jLabel18");

        jLabel7.setText("Current State:");

        org.jdesktop.layout.GroupLayout gl_jPanel1 = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(gl_jPanel1);
        gl_jPanel1.setHorizontalGroup(
            gl_jPanel1.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(gl_jPanel1.createSequentialGroup()
                .add(53, 53, 53)
                .add(gl_jPanel1.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(gl_jPanel1.createSequentialGroup()
                        .add(jLabel4)
                        .addContainerGap())
                    .add(gl_jPanel1.createSequentialGroup()
                        .add(jLabel9)
                        .addContainerGap())
                    .add(gl_jPanel1.createSequentialGroup()
                        .add(gl_jPanel1.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel2)
                            .add(jLabel1)
                            .add(jLabel3)
                            .add(jLabel11)
                            .add(jLabel10)
                            .add(jLabel7)
                            .add(jLabel6)
                            .add(jLabel12)
                            .add(jLabel5))
                        .add(13, 13, 13)
                        .add(gl_jPanel1.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(gl_jPanel1.createSequentialGroup()
                                .add(lblCurrState)
                                .addContainerGap())
                            .add(gl_jPanel1.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(gl_jPanel1.createSequentialGroup()
                                    .add(lblCreator)
                                    .addContainerGap())
                                .add(gl_jPanel1.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(gl_jPanel1.createSequentialGroup()
                                        .add(lblModified)
                                        .addContainerGap())
                                    .add(gl_jPanel1.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(gl_jPanel1.createSequentialGroup()
                                            .add(lblFormat)
                                            .addContainerGap())
                                        .add(org.jdesktop.layout.GroupLayout.TRAILING, gl_jPanel1.createSequentialGroup()
                                            .add(gl_jPanel1.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                                .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
                                                .add(gl_jPanel1.createSequentialGroup()
                                                    .add(lblOwner)
                                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 157, Short.MAX_VALUE)
                                                    .add(cmdChangeOwner))
                                                .add(org.jdesktop.layout.GroupLayout.LEADING, lblType)
                                                .add(org.jdesktop.layout.GroupLayout.LEADING, lblCreated)
                                                .add(org.jdesktop.layout.GroupLayout.LEADING, lblSize)
                                                .add(org.jdesktop.layout.GroupLayout.LEADING, txtObjectName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE))
                                            .add(25, 25, 25)))))
                            .add(gl_jPanel1.createSequentialGroup()
                                .add(lblLCName)
                                .addContainerGap())))))
        );
        gl_jPanel1.setVerticalGroup(
            gl_jPanel1.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(gl_jPanel1.createSequentialGroup()
                .add(37, 37, 37)
                .add(gl_jPanel1.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(txtObjectName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(17, 17, 17)
                .add(gl_jPanel1.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(lblType))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(gl_jPanel1.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblFormat)
                    .add(jLabel3))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(gl_jPanel1.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4)
                    .add(lblSize))
                .add(17, 17, 17)
                .add(gl_jPanel1.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 83, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel5))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(gl_jPanel1.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel9)
                    .add(lblCreated))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(gl_jPanel1.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblModified)
                    .add(jLabel10))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(gl_jPanel1.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblCreator)
                    .add(jLabel11))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(gl_jPanel1.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(cmdChangeOwner)
                    .add(gl_jPanel1.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(lblOwner)
                        .add(jLabel12)))
                .add(19, 19, 19)
                .add(gl_jPanel1.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblLCName)
                    .add(jLabel6))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(gl_jPanel1.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel7)
                    .add(lblCurrState))
                .addContainerGap(127, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("General", jPanel1);

        jLabel13.setText("Your Permission:");

        jLabel14.setText("Permission Set:");

        lblCurrentPermissions.setText("jLabel15");

        jLabel16.setText("Permission Set Owner:");

        cmdChangeACL.setText("Change Permission Set");
        cmdChangeACL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdChangeACLActionPerformed(evt);
            }
        });

        jLabel17.setText("Permission Set Decription:");

        lblAclDescription.setText("jLabel18");

        tblPermissions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Permissions"
            }
        ));
        tblPermissions.setShowHorizontalLines(false);
        tblPermissions.setShowVerticalLines(false);
        jScrollPane2.setViewportView(tblPermissions);

        jLabel19.setText("Current Permissions:");

        jLabel20.setText("Is Internal:");

        lblAclIsInternal.setText("jLabel21");

        lblAclName.setText("jLabel22");

        lblAclOwner.setText("jLabel23");

        org.jdesktop.layout.GroupLayout gl_jPanel3 = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(gl_jPanel3);
        gl_jPanel3.setHorizontalGroup(
            gl_jPanel3.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(gl_jPanel3.createSequentialGroup()
                .add(gl_jPanel3.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(gl_jPanel3.createSequentialGroup()
                        .add(20, 20, 20)
                        .add(gl_jPanel3.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(gl_jPanel3.createSequentialGroup()
                                .add(gl_jPanel3.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jLabel16)
                                    .add(jLabel14)
                                    .add(jLabel13)
                                    .add(jLabel17)
                                    .add(jLabel20))
                                .add(43, 43, 43)
                                .add(gl_jPanel3.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(lblAclName)
                                    .add(lblAclDescription)
                                    .add(lblAclOwner)
                                    .add(lblCurrentPermissions, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 170, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(lblAclIsInternal)))
                            .add(cmdChangeACL))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 108, Short.MAX_VALUE))
                    .add(gl_jPanel3.createSequentialGroup()
                        .addContainerGap()
                        .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE))
                    .add(gl_jPanel3.createSequentialGroup()
                        .addContainerGap()
                        .add(jLabel19)))
                .addContainerGap())
        );
        gl_jPanel3.setVerticalGroup(
            gl_jPanel3.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(gl_jPanel3.createSequentialGroup()
                .add(29, 29, 29)
                .add(gl_jPanel3.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel14)
                    .add(lblAclName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(gl_jPanel3.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, gl_jPanel3.createSequentialGroup()
                        .add(6, 6, 6)
                        .add(cmdChangeACL)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel13)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel16)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel17))
                    .add(gl_jPanel3.createSequentialGroup()
                        .add(lblCurrentPermissions)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lblAclOwner)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lblAclDescription)))
                .add(6, 6, 6)
                .add(gl_jPanel3.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(gl_jPanel3.createSequentialGroup()
                        .add(jLabel20)
                        .add(14, 14, 14)
                        .add(jLabel19))
                    .add(lblAclIsInternal))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 161, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(150, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Permissions", jPanel3);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Renditions"));

        jScrollPane3.setViewportView(tblRenditions);

        jLabel8.setText("Object ID:");

        txtObjID.setEditable(false);
        txtObjID.setEnabled(false);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Versions"));

        tblVersions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3"
            }
        ));
        jScrollPane4.setViewportView(tblVersions);

        org.jdesktop.layout.GroupLayout gl_jPanel4 = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(gl_jPanel4);
        gl_jPanel4.setHorizontalGroup(
            gl_jPanel4.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(gl_jPanel4.createSequentialGroup()
                .addContainerGap()
                .add(gl_jPanel4.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, gl_jPanel4.createSequentialGroup()
                        .add(10, 10, 10)
                        .add(jLabel8)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 56, Short.MAX_VALUE)
                        .add(txtObjID, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 329, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        gl_jPanel4.setVerticalGroup(
            gl_jPanel4.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(gl_jPanel4.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(gl_jPanel4.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(txtObjID, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel8))
                .addContainerGap(62, Short.MAX_VALUE))
        );
        GroupLayout gl_jPanel6 = new GroupLayout(jPanel6);
        gl_jPanel6.setHorizontalGroup(
        	gl_jPanel6.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_jPanel6.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(jScrollPane4, GroupLayout.PREFERRED_SIZE, 413, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        gl_jPanel6.setVerticalGroup(
        	gl_jPanel6.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_jPanel6.createSequentialGroup()
        			.addComponent(jScrollPane4, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap(33, Short.MAX_VALUE))
        );
        jPanel6.setLayout(gl_jPanel6);
        GroupLayout gl_jPanel2 = new GroupLayout(jPanel2);
        gl_jPanel2.setHorizontalGroup(
        	gl_jPanel2.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_jPanel2.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(jScrollPane3, GroupLayout.PREFERRED_SIZE, 413, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        gl_jPanel2.setVerticalGroup(
        	gl_jPanel2.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_jPanel2.createSequentialGroup()
        			.addComponent(jScrollPane3, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap(32, Short.MAX_VALUE))
        );
        jPanel2.setLayout(gl_jPanel2);

        jTabbedPane1.addTab("Advanced", jPanel4);

        cmdClose.setText("Close");
        cmdClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCloseActionPerformed(evt);
            }
        });

        cmdSave.setText("Save");
        cmdSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSaveActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(315, Short.MAX_VALUE)
                .add(cmdSave)
                .add(20, 20, 20)
                .add(cmdClose)
                .add(43, 43, 43))
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 474, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 526, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cmdSave)
                    .add(cmdClose))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void cmdSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSaveActionPerformed
        int selectedTab = jTabbedPane1.getSelectedIndex();
        if (selectedTab == 0) {
            if (txtObjectName.getClientProperty("haschanged").equals(Boolean.TRUE)) {
                IDfSession session = null;
                try {
                    session = smanager.getSession();
                    IDfId id = new DfId(objid);
                    IDfSysObject obj2 = (IDfSysObject) session.getObject(id);
                    obj2.setObjectName(txtObjectName.getText());
                    if (obj2.isCheckedOut()) {
                        obj2.saveLock();
                    } else {
                        obj2.save();
                    }
                    txtObjectName.putClientProperty("haschanged", false);
                } catch (DfException ex) {
                    log.error(ex);
                } finally {
                    if (session != null) {
                        smanager.releaseSession(session);
                    }
                }
            }
        }
        if (selectedTab > 2) {

//            ////System.out.println(jTabbedPane1.getSelectedComponent());
            JScrollPane anus = (JScrollPane) jTabbedPane1.getSelectedComponent();
            JViewport jvp = (JViewport) anus.getComponent(0);
            JPanel panel = (JPanel) jvp.getComponent(0);// (JPanel) anus.getComponent(0); //(JPanel)  jTabbedPane1.getSelectedComponent();
            for (int i = 0; i < panel.getComponentCount(); i++) {
//                    ////System.out.println(panel.getComponent(i));
                Component component = panel.getComponent(i);
                if (component instanceof ExJTextField) {
                    ExJTextField tf = (ExJTextField) component;
                    Object obj = tf.getClientProperty("haschanged");

                    if (tf.getClientProperty("haschanged").equals(Boolean.TRUE)) {
                        String attrName = tf.getName();
                        ////System.out.println("attrName: " + attrName);
                        ////System.out.println("attrValue: " + tf.getText());
                        IDfSession session = null;
                        try {
                            session = smanager.getSession();
                            IDfId id = new DfId(objid);
                            IDfSysObject obj2 = (IDfSysObject) session.getObject(id);
                            obj2.setString(attrName, tf.getText());
                            if (obj2.isCheckedOut()) {
                                obj2.saveLock();
                            } else {
                                obj2.save();
                            }
                        } catch (DfException ex) {
                            log.error(ex);
                        } finally {
                            if (session != null) {
                                smanager.releaseSession(session);
                            }
                        }
                    }
                }
                if (component instanceof JScrollPane) {
                    JScrollPane spane = (JScrollPane) component;
                    for (int j = 0; j < spane.getComponentCount(); j++) {
                        Component com = spane.getComponent(j);
                        if (com instanceof JViewport) {
                            JViewport port = (JViewport) com;
                            for (int x = 0; x < port.getComponentCount(); x++) {
                                Component c = port.getComponent(j);
                                if (c instanceof JList) {
                                    JList list = (JList) c;
                                    ////System.out.println("attrName: " + list.getName());
                                    IDfSession session = null;
                                    try {
                                        session = smanager.getSession();
                                        IDfId id = new DfId(objid);
                                        IDfSysObject obj2 = (IDfSysObject) session.getObject(id);
                                        DefaultListModel model = (DefaultListModel) list.getModel();
                                        obj2.truncate(list.getName(), 0);
                                        for (int ii = 0; ii < model.getSize(); ii++) {
                                            obj2.appendString(list.getName(), (String) model.get(ii));
                                        }
                                        if (obj2.isCheckedOut()) {
                                            obj2.saveLock();
                                        } else {
                                            obj2.save();
                                        }
                                    } catch (DfException ex) {
                                        log.error(ex);
                                    } finally {
                                        if (session != null) {
                                            smanager.releaseSession(session);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }



                if (component instanceof JComboBox) {

                    JComboBox combo = (JComboBox) component;
                    ////System.out.println("haschanged: " + combo.getClientProperty("haschanged"));
                    if (combo.getClientProperty("haschanged").equals(Boolean.TRUE)) {
                        String attrName = combo.getName();
                        ////System.out.println("attrName: " + attrName);
                        ////System.out.println("attrValue: " + combo.getSelectedItem());
                        IDfSession session = null;
                        try {
                            session = smanager.getSession();
                            IDfId id = new DfId(objid);
                            IDfSysObject obj2 = (IDfSysObject) session.getObject(id);
                            obj2.setString(attrName, (String) combo.getSelectedItem());
                            if (obj2.isCheckedOut()) {
                                obj2.saveLock();
                            } else {
                                obj2.save();
                            }
                        } catch (DfException ex) {
                            log.error(ex);
                        } finally {
                            if (session != null) {
                                smanager.releaseSession(session);
                            }
                        }
                    }
                }
            }
        }
    }//GEN-LAST:event_cmdSaveActionPerformed

    private void cmdCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCloseActionPerformed
        this.dispose();
    }//GEN-LAST:event_cmdCloseActionPerformed

    private void cmdChangeACLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdChangeACLActionPerformed

        final String newOwner = "";
        final AclBrowserData x = new AclBrowserData();
        IDfSession session = null;
        try {
            session = smanager.getSession();
            IDfId id = new DfId(objid);
            final IDfSysObject obj2 = (IDfSysObject) session.getObject(id);
            ActionListener al = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    ////System.out.println(e);
                    ////System.out.println(e.getSource().toString());
                    try {
                        obj2.setString("acl_domain", x.getAclDomain());
                        obj2.setString("acl_name", x.getAclName());
                        if (obj2.isCheckedOut()) {
                            obj2.saveLock();
                        } else {
                            obj2.save();
                        }

                    } catch (DfException ex) {
                        log.error(ex);
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
                    }
                    updatePermissions();
                }
            };
            ACLBrowserFrame frame = new ACLBrowserFrame(al, x, true);

            frame.setVisible(true);
        } catch (DfException ex) {
            log.error(ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (session != null) {
                smanager.releaseSession(session);
            }
        }
    }//GEN-LAST:event_cmdChangeACLActionPerformed

    private void updatePermissions() {
        IDfSession session = null;
        try {
            session = smanager.getSession();
            IDfId id = new DfId(objid);
            IDfSysObject obj = (IDfSysObject) session.getObject(id);
            lblAclName.setText(obj.getACLName());
            lblAclOwner.setText(obj.getACLDomain());
            //lblCurrentPermissions.setText(obj.getPermit().);
            Utils util = new Utils();
            int permit = obj.getPermit();

            lblCurrentPermissions.setText(util.intToPermit(permit));

            IDfACL acl = obj.getACL();
            lblAclDescription.setText(acl.getDescription());
            boolean isinternal = acl.getBoolean("r_is_internal");
            if (isinternal) {
                lblAclIsInternal.setText("True");
            } else {
                lblAclIsInternal.setText("False");
            }

            DefaultTableModel permissionlistmodel = new DefaultTableModel() {

                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            permissionlistmodel.addColumn("Accessor");
            permissionlistmodel.addColumn("Permit");
            permissionlistmodel.addColumn("Ext. Permit");
            permissionlistmodel.setRowCount(0);
            tblPermissions.setModel(permissionlistmodel);
            TableColumn col = tblPermissions.getColumnModel().getColumn(1);
            col.setPreferredWidth(30);
            TableColumn col2 = tblPermissions.getColumnModel().getColumn(0);
            col2.setPreferredWidth(50);
            int accessorcount = acl.getAccessorCount();
            for (int i = 0; i < accessorcount; i++) {
                Vector<Object> jeps = new Vector<Object>();
                jeps.add(acl.getRepeatingString("r_accessor_name", i));
                int permits = acl.getRepeatingInt("r_accessor_permit", i);
                jeps.add(util.intToPermit(permits));
                jeps.add(acl.getAccessorXPermitNames(i));
                permissionlistmodel.addRow(jeps);
            }

            tblPermissions.validate();


        } catch (DfException ex) {
            log.error(ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (session != null) {
                smanager.releaseSession(session);
            }
        }
    }

    private void updateAdvanced() {
        IDfCollection coll = null;
        IDfCollection coll2 = null;

        DefaultTableModel renditionmodel = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        DefaultTableModel versionmodel = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        renditionmodel.addColumn("Format");
        renditionmodel.addColumn("Size");
        renditionmodel.setRowCount(0);

        versionmodel.addColumn("r_modify_date");
        versionmodel.addColumn("r_version_label");
        versionmodel.setRowCount(0);

        tblRenditions.setModel(renditionmodel);
        tblVersions.setModel(versionmodel);
        IDfSession session = null;
        try {
            session = smanager.getSession();
            IDfQuery qry = new DfQuery();
            qry.setDQL("select full_format, content_size, set_time, set_file, r_object_id from dmr_content where any (parent_id = '" + objid + "' and page = 0)");
            coll = qry.execute(session, DfQuery.DF_READ_QUERY);
            while (coll.next()) {
                Vector<String> tempVector = new Vector<String>();
                tempVector.add(coll.getString("full_format"));
                tempVector.add(coll.getString("content_size"));
                renditionmodel.addRow(tempVector);
            }

            IDfId id = new DfId(objid);
            final IDfSysObject obj = (IDfSysObject) session.getObject(id);

            String iChronID = obj.getString("i_chronicle_id");

            IDfQuery qry2 = new DfQuery();
            qry2.setDQL("select object_name, r_creation_date, r_version_label, r_modify_date,r_object_id from dm_sysobject(ALL) where i_chronicle_id = '" + iChronID + "' order by r_modify_date, r_object_id");
            coll2 = qry2.execute(session, DfQuery.DF_READ_QUERY);
            while (coll2.next()) {
                int valcount = coll2.getValueCount("r_version_label");
                ////System.out.println(valcount);
                Vector<Object> tempVector = new Vector<Object>();
                tempVector.add(coll2.getAllRepeatingStrings("r_version_label", ", "));
                tempVector.add(coll2.getString("r_modify_date"));
                versionmodel.addRow(tempVector);
            }
        } catch (DfException ex) {
            log.error(ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (coll != null) {
                try {
                    coll.close();
                } catch (DfException ex) {
                    log.error(ex);
                }
            }
            if (coll2 != null) {
                try {
                    coll2.close();
                } catch (DfException ex) {
                    log.error(ex);
                }
            }
            if (session != null) {
                smanager.releaseSession(session);
            }
        }
        tblRenditions.validate();
        tblVersions.validate();
    }

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        ////System.out.println("state changed.");
        ////System.out.println(jTabbedPane1.getSelectedIndex());
        int indexi = jTabbedPane1.getSelectedIndex();
        if (indexi == 1) {
            this.updatePermissions();
        }
        if (indexi == 2) {
            this.updateAdvanced();

        }
        if (indexi > 2) {
            JScrollPane anus = (JScrollPane) jTabbedPane1.getSelectedComponent();
            JViewport jvp = (JViewport) anus.getComponent(0);
            jvp.revalidate();
            JPanel panel = (JPanel) jvp.getComponent(0);// (JPanel) anus.getComponent(0); //(JPanel)  jTabbedPane1.getSelectedComponent();
            panel.revalidate();

        }
        
    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void cmdChangeOwnerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdChangeOwnerActionPerformed




    }//GEN-LAST:event_cmdChangeOwnerActionPerformed
    private String objid;

    public void setID(String id) {
        this.objid = id;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPopupMenu TablePopUp;
    private javax.swing.JPopupMenu ViewportPopUp;
    private javax.swing.JButton cmdChangeACL;
    private javax.swing.JButton cmdChangeOwner;
    private javax.swing.JButton cmdClose;
    private javax.swing.JButton cmdSave;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblAclDescription;
    private javax.swing.JLabel lblAclIsInternal;
    private javax.swing.JLabel lblAclName;
    private javax.swing.JLabel lblAclOwner;
    private javax.swing.JLabel lblCreated;
    private javax.swing.JLabel lblCreator;
    private javax.swing.JLabel lblCurrState;
    private javax.swing.JLabel lblCurrentPermissions;
    private javax.swing.JLabel lblFormat;
    private javax.swing.JLabel lblLCName;
    private javax.swing.JLabel lblModified;
    private javax.swing.JLabel lblOwner;
    private javax.swing.JLabel lblSize;
    private javax.swing.JLabel lblType;
    private javax.swing.JList lstLocations;
    private javax.swing.JMenuItem mnuAddValue;
    private javax.swing.JMenuItem mnuappendValue;
    private javax.swing.JTable tblPermissions;
    private javax.swing.JTable tblRenditions;
    private javax.swing.JTable tblVersions;
    private javax.swing.JTextField txtObjID;
    private javax.swing.JTextField txtObjectName;
    // End of variables declaration//GEN-END:variables
}
