/*
 * IQuickExtension.java
 *
 * Created on 6. heinäkuuta 2007, 21:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.quickclient.classes;

import com.documentum.fc.client.IDfSysObject;

/**
 *
 * @author Administrator
 */
public interface IQuickExtension {

    boolean preCondition();
    void execute();
    void setObj(IDfSysObject obj);
    void setSessionManager(DocuSessionManager smanager);
}
