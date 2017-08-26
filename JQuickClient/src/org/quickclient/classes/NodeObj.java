/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.quickclient.classes;

/**
 *
 * @author Administrator
 */
public class NodeObj {
    private String label;
    private String format;
    private String objId;
    
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
    
    @Override
    public String toString() {
        return label;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

}
