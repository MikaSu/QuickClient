/*
 * UserSelectorData.java
 *
 * Created on 5. marraskuuta 2006, 20:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.quickclient.classes;

/**
 *
 * @author Administrator
 */
public class UserorGroupSelectorData {
    
    /** Creates a new instance of UserSelectorData */
    public UserorGroupSelectorData() {
    }
    
    private String username;
    private boolean isgroup;

    public String getUserorGroupname() {
        return username;
    }

    public void setUserorGroupname(String username) {
        this.username = username;
    }

    public boolean isIsgroup() {
        return isgroup;
    }

    public void setIsgroup(boolean isgroup) {
        this.isgroup = isgroup;
    }
    
}
