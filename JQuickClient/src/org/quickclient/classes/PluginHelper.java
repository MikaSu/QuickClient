/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quickclient.classes;

import java.util.ArrayList;

public class PluginHelper {

    private ArrayList<PluginConfig> plugins;

    public PluginHelper() {
        plugins = new ArrayList<PluginConfig>();
    }
    
    public void add(PluginConfig config) {
        plugins.add(config);
    }
    public ArrayList<PluginConfig> getPlugins() {
        return plugins;
    }
}

