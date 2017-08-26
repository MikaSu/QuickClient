/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quickclient.classes;

import java.util.ArrayList;

public class ScriptHelper {

    private ArrayList<ScriptConfig> scripts;

    public ScriptHelper() {
        scripts = new ArrayList();
    }
    
    public void add(ScriptConfig config) {
        scripts.add(config);
    }
    public ArrayList getScripts() {
        return scripts;
    }
}

