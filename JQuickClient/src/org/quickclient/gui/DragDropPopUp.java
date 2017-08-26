/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.quickclient.gui;

import javax.swing.JPopupMenu;

/**
 *
 * @author Administrator
 */
public class DragDropPopUp extends JPopupMenu {

    private String sourceid;
    private String targetid;

    public void setSourceid(String sourceid) {
        this.sourceid = sourceid;
    }

    public void setTargetid(String targetid) {
        this.targetid = targetid;
    }
    
}
