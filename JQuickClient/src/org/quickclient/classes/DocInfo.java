/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quickclient.classes;

import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfACL;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.IDfId;

/**
 *
 * @author Mika
 */
public class DocInfo {

    Logger log = Logger.getLogger(DocInfo.class);

    public DefaultTableModel initializeLocationsTableModel() {
        DefaultTableModel locationsModel = new DefaultTableModel();
        locationsModel.addColumn(".");
        locationsModel.addColumn(".");
        locationsModel.addColumn("location");
        return locationsModel;
    }

    public DefaultTableModel initializeLocationsVersionsModel() {
        DefaultTableModel versionsModel;

        versionsModel = new DefaultTableModel();
        versionsModel.addColumn(".");
        versionsModel.addColumn(".");
        versionsModel.addColumn("object_name");
        versionsModel.addColumn("r_modifier");
        versionsModel.addColumn("log_entry");
        versionsModel.addColumn("r_version_label");
        versionsModel.addColumn("data");
        return versionsModel;
    }

    public DefaultTableModel initializePermissionListTableModel() {

        DefaultTableModel permissionlistModel;
        permissionlistModel = new DefaultTableModel();
        permissionlistModel.addColumn(".");
        permissionlistModel.addColumn(".");
        permissionlistModel.addColumn("Accessor");
        permissionlistModel.addColumn("Permit");
        permissionlistModel.addColumn("Ext. Permit");
        return permissionlistModel;

    }

    public DefaultTableModel initializeRelationsTableModel() {
        DefaultTableModel relationsModel = new DefaultTableModel();
        relationsModel.addColumn(".");
        relationsModel.addColumn("Relation Name");
        relationsModel.addColumn("Child Id");
        relationsModel.addColumn("Parent Id");
        relationsModel.addColumn("Description");
        return relationsModel;
    }

    public DefaultTableModel initializeRenditionsTableModel() {
        DefaultTableModel renditionsModel = new DefaultTableModel();
        renditionsModel.addColumn(".");
        renditionsModel.addColumn(".");
        renditionsModel.addColumn("full_format");
        renditionsModel.addColumn("content_size");
        renditionsModel.addColumn("page");
        renditionsModel.addColumn("page_modifier");
        renditionsModel.addColumn("content_id");
        return renditionsModel;
    }

    public DefaultTableModel updateLocationsModel(String objid, DefaultTableModel model) {
        DocuSessionManager smanager = DocuSessionManager.getInstance();

        IDfSession session = null;
        IDfCollection col = null;
        if (objid.length()==16) {
        IDfId id = new DfId(objid);
        try {
            session = smanager.getSession();
            IDfSysObject obj = (IDfSysObject) session.getObject(id);
            model.setRowCount(0);
            int ifoldervalue = obj.getValueCount("i_folder_id");
            for (int i = 0; i < ifoldervalue; i++) {
                Vector<String> v = new Vector<String>();
                IDfId iFolderId = obj.getRepeatingId("i_folder_id", i);
                IDfSysObject folderobj = (IDfSysObject) session.getObject(iFolderId);
                String path = folderobj.getRepeatingString("r_folder_path", 0);
                v.add("");
                v.add(",dm_folder");
                v.add(path);
                model.addRow(v);
            }
        } catch (DfException ex) {
            log.error(ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (session != null) {
                smanager.releaseSession(session);
            }
        }
        }
        return model;
    }

    public DefaultTableModel updatePermissionsModel(String objid, DefaultTableModel permissionlistModel) {
        DocuSessionManager smanager = DocuSessionManager.getInstance();

        IDfSession session = null;

        Utils util = new Utils();
        permissionlistModel.setRowCount(0);
        if (objid.length()==16) {
        try {
                    session = smanager.getSession();
            IDfId id = new DfId(objid);
            IDfSysObject objx = (IDfSysObject) session.getObject(id);
            IDfACL acl = objx.getACL();
            String permit = "";
            int accessorcount = acl.getAccessorCount();
            for (int i = 0; i < accessorcount; i++) {
                Vector<Object> jeps = new Vector<Object>();
                jeps.add("");

                boolean isgroup = acl.getRepeatingBoolean("r_is_group", i);
                if (isgroup) {
                    jeps.add("1");
                } else {
                    jeps.add("0");
                }


                jeps.add(acl.getAccessorName(i));


                int iPermit = acl.getAccessorPermit(i);
                int permittype = acl.getRepeatingInt("r_permit_type", i);
                if (permittype == 0) {
                    jeps.add(util.intToPermit(iPermit));
                    jeps.add(acl.getAccessorXPermitNames(i));
                } else if (permittype == 1) {
                    jeps.add("Extended Permit");
                    jeps.add(acl.getAccessorXPermitNames(i));
                } else if (permittype == 2) {
                    jeps.add("Application Permit");
                    jeps.add("");
                } else if (permittype == 3) {
                    jeps.add("Access Restriction");
                    jeps.add("");
                } else if (permittype == 4) {
                    jeps.add("Extended Restriction");
                    jeps.add(acl.getAccessorXPermitNames(i));
                } else if (permittype == 5) {
                    jeps.add("Application Restriction");
                    jeps.add("");
                } else if (permittype == 6) {
                    jeps.add("Required Group");
                    jeps.add("");
                } else if (permittype == 7) {
                    jeps.add("Required Group Set");
                    jeps.add("");
                }

                jeps.add(util.intToPermit(iPermit));
                jeps.add(acl.getAccessorXPermitNames(i));
                permissionlistModel.addRow(jeps);


            }
        } catch (DfException ex) {
            log.error(ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (session != null) {
                smanager.releaseSession(session);
            }
        }
        }
        return permissionlistModel;
    }

    public DefaultTableModel updateRelationsModel() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public DefaultTableModel updateRelationsModel(String objid2, DefaultTableModel relationsModel) {
        DocuSessionManager smanager = DocuSessionManager.getInstance();
        IDfSession session2 = null;
        IDfCollection col = null;
        if (objid2.length()==16) {
        try {

            session2 = smanager.getSession();
            IDfQuery query = new DfQuery();
            query.setDQL("select relation_name, parent_id, child_id, description from dm_relation where parent_id = '" + objid2 + "' or child_id = '" + objid2 + "'");
            relationsModel.setRowCount(0);
            col = query.execute(session2, DfQuery.DF_READ_QUERY);
            while (col.next()) {
                /*
                relationsModel.addColumn("Relation Name");
                relationsModel.addColumn("Child Id");
                relationsModel.addColumn("Parent Id");
                relationsModel.addColumn("Description"); */
                Vector<String> v = new Vector<String>();
                v.add(",dm_relation");
                v.add(col.getString("relation_name"));
                v.add(col.getString("parent_id"));
                v.add(col.getString("child_id"));
                v.add(col.getString("description"));
                relationsModel.addRow(v);
            }

        } catch (DfException ex) {
            log.error(ex);
        } finally {
            if (session2 != null) {
                smanager.releaseSession(session2);
            }
            if (col != null) {
                try {
                    col.close();
                } catch (DfException ex) {
                    log.error(ex);
                }
            }

        }
        }
        return relationsModel;
    }

    public DefaultTableModel updateRenditionsModel(String objid2, DefaultTableModel renditionsModel) {
        DocuSessionManager smanager = DocuSessionManager.getInstance();
        if (objid2.length()==16) {
        IDfId id = new DfId(objid2);
        IDfSession session2 = null;
        IDfCollection col2 = null;
        try {

            session2 = smanager.getSession();
            IDfSysObject obj = (IDfSysObject) session2.getObject(id);
            col2 = obj.getRenditions("full_format, page_modifier, page, content_size,set_time, set_file, r_object_id");
            renditionsModel.setRowCount(0);
            while (col2.next()) {
                Vector<String> v = new Vector<String>();
                v.add(".");
                v.add(col2.getString("full_format") + ",qazwsxedc");
                v.add(col2.getString("full_format"));
                v.add(col2.getString("content_size"));
                v.add(col2.getString("page"));
                v.add(col2.getString("page_modifier"));
                v.add(col2.getString("r_object_id"));
                renditionsModel.addRow(v);
                ////System.out.println("page: " + col2.getString("page"));
                ////System.out.println("page_modifier: " + col2.getString("page_modifier"));
            }

        } catch (DfException ex) {
            log.error(ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (session2 != null) {
                smanager.releaseSession(session2);
            }
            if (col2 != null) {
                try {
                    col2.close();
                } catch (DfException ex) {
                    log.error(ex);

                }
            }
        }
        }
        return renditionsModel;
    }

    public DefaultTableModel updateVersionsModel(String objid, DefaultTableModel versionsModel) {
        DocuSessionManager smanager = DocuSessionManager.getInstance();

        IDfSession session = null;
        IDfCollection col = null;
        if (objid.length()==16) {
        try {

            versionsModel.setRowCount(0);
            IDfId id = new DfId(objid);
            session = smanager.getSession();
            IDfSysObject obj = (IDfSysObject) session.getObject(id);
            col = obj.getVersions(null);

            while (col.next()) {
                Vector<Object> v = new Vector<Object>();
                v.add(col.getString("r_lock_owner"));
                v.add(col.getString("a_content_type") + "," + col.getString("r_object_type"));
                v.add(col.getString("object_name"));
                v.add(col.getString("r_modifier"));
                v.add(col.getString("log_entry"));
                v.add(col.getAllRepeatingStrings("r_version_label", ", "));
                DokuData data = new DokuData(col.getString("r_object_id"));
                v.add(data);
                versionsModel.addRow(v);
            }


        } catch (DfException ex) {
            log.error(ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (session != null) {
                smanager.releaseSession(session);
            }
            if (col != null) {
                try {
                    col.close();
                } catch (DfException ex) {
                    log.error(ex);

                }
            }
        }
        }
        return versionsModel;
    }
}
