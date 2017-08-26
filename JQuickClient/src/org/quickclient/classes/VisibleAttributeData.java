/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.quickclient.classes;

import java.util.Vector;

/**
 *
 * @author Administrator
 */
public class VisibleAttributeData {
private Vector visibleAttributes;
private Vector sortAttributes;
private boolean sortdescending;

    public Vector getVisibleAttributes() {
        return visibleAttributes;
    }

    public void setVisibleAttributes(Vector visibleAttributes) {
        this.visibleAttributes = visibleAttributes;
    }

    public Vector getSortAttributes() {
        return sortAttributes;
    }

    public void setSortAttributes(Vector sortAttributes) {
        this.sortAttributes = sortAttributes;
    }

    public boolean isSortdescending() {
        return sortdescending;
    }

    public void setSortdescending(boolean sortdescending) {
        this.sortdescending = sortdescending;
    }
}
