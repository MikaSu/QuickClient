/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.quickclient.gui;

/**
 *
 * @author Administrator
 */
public class ComboItem {
private String attrName;
private String attrRepeating;
private String attrType;
private String attrLabel;

public ComboItem(String attrLabel, String _attrName, String _attrRepeating, String _attrType) {
    attrName = _attrName;
    attrType = _attrType;
    attrRepeating = _attrRepeating;
    this.attrLabel = attrLabel;
}

    @Override
public String toString() {
    return attrLabel;
}
    public String getAttrName() {
        return attrName;
    }


    public String getAttrType() {
        return attrType;
    }

    public String getAttrRepeating() {
        return attrRepeating;
    }

    

}
