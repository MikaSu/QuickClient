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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
public class LabelValues {

    private HashMap<String, String> labelValues;
    private DocuSessionManager smanager;
    Logger logger = Logger.getLogger(LabelValues.class);

    public LabelValues(String typeName) {
        smanager = DocuSessionManager.getInstance();
        IDfSession session = null;
        IDfCollection col = null;

        IDfQuery query = new DfQuery();
        query.setDQL("select attr_name,label_text from dmi_dd_attr_info where type_name = '" + typeName + "'");
        try {
            session = smanager.getSession();
            col = query.execute(session, DfQuery.DF_QUERY);
            String labelText = "";
            String attrName = "";
            labelValues = new HashMap<String, String>();
            while (col.next()) {
                labelText = col.getString("label_text");
                attrName = col.getString("attr_name");
                labelValues.put(attrName, labelText);
            }
        } catch (DfException ex) {
            logger.error("failed to get labels", ex);
        } finally {
            try {
                col.close();
            } catch (DfException ex) {
            logger.error("failed to close collection", ex);
            }
            if (session != null) {
                smanager.releaseSession(session);
            }
        }

    }

    public HashMap<String, String> getLabelValues() {
        return labelValues;
    }

    public void setLabelValues(HashMap<String, String> labelValues) {
        this.labelValues = labelValues;
    }
    
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        Collection<String> col = labelValues.values();
        Iterator<String> i = col.iterator();
        while (i.hasNext()) {
            sb.append(i.next());
        }
        return sb.toString();
    }
    
    public String getLabel(String attrName) {  
        return (String) labelValues.get(attrName);
    }
    
}
