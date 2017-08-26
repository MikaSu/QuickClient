/*
 * UsersInGroupData.java
 *
 * Created on 25. kesäkuuta 2007, 1:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.quickclient.classes;

import java.util.Vector;

/**
 *
 * @author Administrator
 */
public class UsersInGroupData {
    
    /** Creates a new instance of UsersInGroupData */
    public UsersInGroupData() {
    }
    
    private Vector groupMembers;
    
    public Vector getGroupMembers() {
        return groupMembers;
    }
    
    public void setGroupMembers(Vector groupmembers) {
        this.groupMembers = groupmembers;
    }
    
}
