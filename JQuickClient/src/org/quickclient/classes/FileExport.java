/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quickclient.classes;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;

/**
 *
 * @author Administrator
 */
public class FileExport extends Thread {

    private IDfSysObject obj;
    private File file;

    public FileExport(File file, IDfSysObject obj) {
        this.file = file;
        this.obj = obj;
    }

    @Override
    public void run() {
        try {
            obj.getFile(file.toString());
        } catch (DfException ex) {
            Logger.getLogger(FileExport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
