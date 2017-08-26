/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quickclient.classes;

import java.awt.Component;
import java.awt.Cursor;

import javax.swing.JDesktopPane;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;
import org.quickclient.gui.SearchFrame;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;


/**
 *
 * @author Mika
 */
public class CustomQueryHelper {

    DocuSessionManager smanager = DocuSessionManager.getInstance();
    Logger log = Logger.getLogger(CustomQueryHelper.class);

    public CustomQueryHelper() {
    }

    public void executeCustomQuery(DefaultTableModel model, JDesktopPane desktop, Component parentComponent, String strQuery, String windowTitle, boolean showThumbnails) {
        IDfQuery query = new DfQuery();
        IDfCollection col = null;
        IDfSession session = null;
        try {
            Cursor cur = new Cursor(Cursor.WAIT_CURSOR);
            if (parentComponent != null)
            parentComponent.setCursor(cur);
            session = smanager.getSession();
            query.setDQL(strQuery);
            long startmillis = System.currentTimeMillis();
            col = query.execute(session, DfQuery.DF_READ_QUERY);
            long stopmillis = System.currentTimeMillis();
            double time = stopmillis - startmillis;
            int counter = 0;
            model.setRowCount(0);

            Utils u = new Utils();
            model = u.getModelFromCollection(session,null,col, showThumbnails, model,null,null);
//            setLblInfo("Found " + u.getCounter() + " items in: " + time / 1000 + " secs.");
            //lblSearchStatus.setText("Found " + counter + " items in: " + time / 1000 + " secs.");
        } catch (DfException ex) {
            log.error(ex);
            SwingHelper.showErrorMessage("Error occurred!", ex.getMessage());

        } finally {
            if (col != null) {
                try {
                    col.close();
                } catch (DfException ex) {
                    log.error(ex);
                }
            }
            if (session != null) {
                smanager.releaseSession(session);
            }
            Cursor cur2 = new Cursor(Cursor.DEFAULT_CURSOR);
            if (parentComponent != null)
            parentComponent.setCursor(cur2);
        }
        SearchFrame sf = new SearchFrame(model, showThumbnails);
        desktop.add(sf);
        //sf.setSize(400, 400);
        sf.setTitle(windowTitle);
        sf.setVisible(true);

    }
}
