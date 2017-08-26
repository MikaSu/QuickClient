/*
 * JMenuItemExt.java
 *
 * Created on 6. heinäkuuta 2007, 23:04
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.quickclient.classes;

/**
 *
 * @author Administrator
 */
public class JMenuItemExt extends javax.swing.JMenuItem{
    
    /** Creates a new instance of JMenuItemExt */
    public JMenuItemExt() {
    }
    
    private String objID;
    private String className;
    private String implementation;
    private String scriptPath;
    
    public String getObjID() {
        return objID;
    }

    public void setObjID(String objID) {
        this.objID = objID;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getImplementation() {
        return implementation;
    }

    public void setImplementation(String implementation) {
        this.implementation = implementation;
    }

    public String getScriptPath() {
        return scriptPath;
    }

    public void setScriptPath(String scriptPath) {
        this.scriptPath = scriptPath;
    }
}
