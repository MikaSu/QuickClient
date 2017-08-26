/*
 * AclBrowserData.java
 *
 * Created on 5. marraskuuta 2006, 14:46
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.quickclient.classes;

/**
 *
 * @author Administrator
 */
public class AclBrowserData {
    
    /** Creates a new instance of AclBrowserData */
    public AclBrowserData() {
    }
    
    private String aclName;
    private String aclDomain;

    public String getAclName() {
        return aclName;
    }

    public void setAclName(String aclName) {
        this.aclName = aclName;
    }

    public String getAclDomain() {
        return aclDomain;
    }

    public void setAclDomain(String aclDomain) {
        this.aclDomain = aclDomain;
    }
    
}
