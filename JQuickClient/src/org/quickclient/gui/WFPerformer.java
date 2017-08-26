/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quickclient.gui;

import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
class WFPerformer {

    private ArrayList<String> performers = new ArrayList<String>();
    private String activityName;

    public ArrayList getPerformers() {
        return performers;
    }
    
    public void appendPerformer(String performer) {
        performers.add(performer);
    }

    public void setPerformers(ArrayList<String> performers) {
        this.performers = performers;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
    

}
