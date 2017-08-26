/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quickclient.classes;

import java.util.Vector;
import org.apache.log4j.Logger;

public class HistoryService {

    private static HistoryService instance;
    Logger log = Logger.getLogger(HistoryService.class);
    private Vector<String> commandHistory = new Vector();

    public HistoryService() {
        log.debug("init.");
    }

    public static HistoryService getInstance() {
        if (instance == null) {
            instance = new HistoryService();
        }
        return instance;
    }

    /**
     * @return the commandHistory
     */
    public Vector<String> getCommandHistory() {
        return commandHistory;
    }

    public void appendToHistory(String dql) {
        commandHistory.add(dql);
    }
}
