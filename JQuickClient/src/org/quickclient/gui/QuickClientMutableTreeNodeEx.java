package org.quickclient.gui;
import javax.swing.tree.DefaultMutableTreeNode;
/*
 * QuickClientMutableTreeNode.java
 *
 * Created on October 25, 2006, 9:44 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
public class QuickClientMutableTreeNodeEx extends DefaultMutableTreeNode {
    
    /** Creates a new instance of QuickClientMutableTreeNode */
    public QuickClientMutableTreeNodeEx()  {
        super();
    }
    public QuickClientMutableTreeNodeEx(Object userObject, boolean allowsChildren) {
     super( userObject,  allowsChildren);
    }
    public QuickClientMutableTreeNodeEx(Object userObject) {
        super( userObject);
    }
    

}
