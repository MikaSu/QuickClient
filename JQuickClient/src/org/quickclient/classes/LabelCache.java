/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quickclient.classes;

import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
public class LabelCache {

    private static HashMap<String,LabelValues> typeValues;
    private static LabelCache instance = null;
    Logger logger = Logger.getLogger(LabelCache.class);

    public static LabelCache getInstance() {
        if (instance == null) {
            typeValues = new HashMap<java.lang.String,org.quickclient.classes.LabelValues>();
            instance = new LabelCache();
        }
        return instance;
    }
    
    public LabelValues getLabels(String typeName) {
        
        LabelValues rval = null;
        if (typeValues.containsKey(typeName)) {
            rval = typeValues.get(typeName);
        } else {
            logger.info("getting new labels for: " + typeName);
            LabelValues l = new LabelValues(typeName);
            typeValues.put(typeName, l);
            rval = l;
        }
        return rval;
    }
    
}
