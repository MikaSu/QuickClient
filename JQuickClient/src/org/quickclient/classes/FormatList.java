/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quickclient.classes;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import java.util.HashMap;
import org.apache.log4j.Logger;

public class FormatList {

    private DocuSessionManager smanager;
    Logger log = Logger.getLogger(FormatList.class);
    private String[][] taulu;

    private FormatList() {
        smanager = DocuSessionManager.getInstance();
        IDfSession session = null;//smanager.getSession();

        IDfCollection col = null;
        IDfCollection col2 = null;
        try {
            session = smanager.getSession();
            IDfQuery query = new DfQuery();
            query.setDQL("select count(*) as cnt from dm_format");
            IDfQuery query2 = new DfQuery();
            col = query.execute(session, DfQuery.CACHE_QUERY);
            col.next();
            int j = col.getInt("cnt");
            taulu = new String[j][3];
            query2.setDQL("select name, dos_extension, description from dm_format order by dos_extension desc");
            col2 = query2.execute(session, DfQuery.CACHE_QUERY);
            String format = "";
            String ext = "";
            String desc = "";
            int counter = 0;
            while (col2.next()) {
                format = col2.getString("name");
                ext = col2.getString("dos_extension");
                desc = col2.getString("description");
                taulu[counter][0] = format;
                taulu[counter][1] = ext;
                taulu[counter][2] = desc;

                counter++;

            }
        } catch (DfException ex) {
            log.error(ex);
            System.exit(100);
        } finally {
            try {
                col.close();
            } catch (DfException ex) {
            }
            try {
                col2.close();
            } catch (DfException ex) {
            }
            if (session != null) {
                smanager.releaseSession(session);
            }
        }
    }
    private static FormatList instance = null;

    public String getFormatFromExtension(String extension) {

        ConfigService cs = ConfigService.getInstance();
        HashMap prefFormats = cs.getPreferredFormats();
        String format = (String) prefFormats.get(extension);
        if (format != null) {
            if (format.length() > 1) {
                return format;
            }
        }
        String jee = "";
        for (int i = 0; i < taulu.length; i++) {
            jee = taulu[i][1];
            if (jee.equals(extension.toLowerCase())) {
                return taulu[i][0];
            }
        }

        return "unknown";
    }

    public static FormatList getInstance() {
        if (instance == null) {
            instance = new FormatList();
        }
        return instance;
    }
}

