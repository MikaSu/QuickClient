package org.quickclient.classes;
/*
 * NodeData.java
 *
 * Created on October 25, 2006, 9:33 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
public class DokuData {
    
    /** Creates a new instance of NodeData */
    public DokuData() {
    }
    public DokuData(String objectID) {
        this.objID = objectID;
    }
    public void setObjID(String objectID) {
        this.objID = objectID;
    }
    public String getObjID() {
        return objID;
    }
 
    private String objID;
    
    @Override
    public String toString() {
        return objID;
    }
    
}
