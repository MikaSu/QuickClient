/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quickclient.classes;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.documentum.com.DfClientX;
import com.documentum.com.IDfClientX;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfVirtualDocument;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.IDfId;
import com.documentum.operations.IDfCheckoutNode;
import com.documentum.operations.IDfCheckoutOperation;

/**
 *
 * @author Administrator
 */
public class FileEdit extends Thread {

    DocuSessionManager smanager;
    private String objid;
    Logger log = Logger.getLogger(FileEdit.class);

    public FileEdit(String objid) {
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

            IDfClientX clientx = new DfClientX();
            IDfCheckoutOperation operation = clientx.getCheckoutOperation();

            if (obj.isCheckedOut() == true) {
                ////System.out.println("Object  is already checked out.");
                return;
            }

            IDfCheckoutNode node;
            if (obj.isVirtualDocument()) {
                IDfVirtualDocument vDoc = obj.asVirtualDocument("CURRENT", false);
                node = (IDfCheckoutNode) operation.add(vDoc);
            } else {
                node = (IDfCheckoutNode) operation.add(obj);
            }

            operation.execute();

            String filePath = node.getFilePath();
            File fileToOpen = new File(filePath);
            try {
                Desktop.getDesktop().edit(fileToOpen);
            } catch (IOException ex) {
                log.error(ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error occured!", JOptionPane.ERROR_MESSAGE);
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
}
