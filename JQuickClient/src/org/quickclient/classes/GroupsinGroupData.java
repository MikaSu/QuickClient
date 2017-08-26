/*
 * GroupsinGroupsData.java
 *
 * Created on 24. kesäkuuta 2007, 23:53
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
public class GroupsinGroupData {
    
    /** Creates a new instance of GroupsinGroupsData */
    public GroupsinGroupData() {
        
    }
    
    private Vector groupMembers;
    
    public Vector getGroupMembers() {
        return groupMembers;
    }
    
    public void setGroupMembers(Vector groupmembers) {
        this.groupMembers = groupmembers;
    }
    
}
