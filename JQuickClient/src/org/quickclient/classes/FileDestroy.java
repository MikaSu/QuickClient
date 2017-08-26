/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quickclient.classes;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.IDfId;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
public class FileDestroy extends Thread {

    DocuSessionManager smanager;
    private String objid;
    Logger log = Logger.getLogger(FileDestroy.class);
    public FileDestroy(String objid) {
        this.objid = objid;
        smanager = DocuSessionManager.getInstance();
    }

    @Override
    public void run() {
        IDfSession session = null;
        try {
            IDfId id = new DfId(objid);
            session = smanager.getSession();
            IDfSysObject obj = (IDfSysObject) session.getObject(id);
            obj.destroy();
        } catch (DfException ex) {
            log.error(ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (session != null) {
                smanager.releaseSession(session);
            }
        }
    }
}
