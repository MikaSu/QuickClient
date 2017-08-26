/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quickclient.classes;

import java.util.Vector;

/**
 *
 * @author Administrator
 * 
 * 
 */
public class DQLHistorySingleton {

	private Vector<String> commandHistory = new Vector<String>();
    private static DQLHistorySingleton me = null;

    protected DQLHistorySingleton() {
    }

    public static DQLHistorySingleton getInstance() {
        if (me == null) {
            me = new DQLHistorySingleton();
        }
        return me;
    }
    
    public void addDQL(String dql) {
    	commandHistory.add(dql);
    }
    public Vector<String> getHistory() {
    	return commandHistory;
    }

}
