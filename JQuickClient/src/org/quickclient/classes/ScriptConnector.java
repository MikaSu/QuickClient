package org.quickclient.classes;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;

public class ScriptConnector {

    private DocuSessionManager smanager;
    Binding binding;  // The 'binding' makes instances of the application objects available as 'variables' in the script
    String objid;   // The main application window, the GUI in general
    String[] roots;    // A list of directories to search for Groovy scripts (think of it as a PATH).

    public ScriptConnector(String objid, String filepath) {
        IDfSession session = null;
        try {
            smanager = DocuSessionManager.getInstance();
            
            roots = new String[]{filepath};
            for (int i = 0; i < roots.length; i++) {
                ////System.out.println(roots[i]);
            }


            session = smanager.getSession();
            IDfSysObject obj = (IDfSysObject) session.getObject(new DfId(objid));
            Binding scriptenv = new Binding(); // A new Binding is created ...
            scriptenv.setVariable("objid", objid);
            scriptenv.setVariable("obj", obj);

            this.binding = scriptenv;
        } catch (DfException ex) {
            Logger.getLogger(ScriptConnector.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (session != null) {
                smanager.releaseSession(session);
            }
        }

    }

    // Method to show Groovy related errors/warnings in a dialog window.
    public void showWarning(String message) {
        ////System.out.println(message);
    // JOptionPane.showMessageDialog(null, message, "Groovy-error", JOptionPane.WARNING_MESSAGE);
    }


    // This is the main method called from the application code to start the Groovy integration
    public void runGroovyScript(String filename) {
        GroovyScriptEngine gse = null;
        try {
            gse = new GroovyScriptEngine(roots);   // instanciating the script engine ...
        } catch (IOException ioe) {
            ioe.printStackTrace();
            showWarning("I/O-Exception in starting Groovy engine. Message is:\n" + ioe.getMessage() + "\n" + prepareStackTrace(ioe));
        }

        if (gse != null) {
            try {
                gse.run(filename, binding);      // ... and running the specified script
            } catch (ResourceException re) {
                re.printStackTrace();
                showWarning("ResourceException in calling groovy script '" + filename +
                        "' Message is:\n" + re.getMessage() + "\n" + prepareStackTrace(re));

            } catch (ScriptException se) {
                se.printStackTrace();
                showWarning("ScriptException in calling groovy script '" + filename +
                        "' Message is:\n" + se.getMessage() + "\n" + prepareStackTrace(se));
            }
        }

    }

    // prepare a stacktrace to be shown in an output window
    private String prepareStackTrace(Exception e) {
        Throwable exc = e;
        StringBuffer output = new StringBuffer();
        collectTraces(exc, output);
        if (exc.getCause() != null) {
            exc = exc.getCause();
            output.append("caused by::\n");
            output.append(exc.getMessage());
            output.append("\n");
            collectTraces(exc, output);
        }
        return output.toString();
    }

    private void collectTraces(Throwable e, StringBuffer output) {
        StackTraceElement[] trace = e.getStackTrace();
        for (int i = 0; i < trace.length; i++) {
            output.append(trace[i].toString());
            output.append("\n");
        }
    }

    public void setString(String string) {
        ////System.out.println("setString: " + string);
    }

    public void saveObj() {
        ////System.out.println("save...");
    }

    // ----------------- API to be used inside scripts --------------------

    // create a new dialog to display textual output from running scripts
    /*
    public ScriptOutputDialog newOutputDialog(String title, String tabTitle) {
    return window.newOutputDialog(title, tabTitle);
    }
    // get the panel instance prepared to contain customised GUI elements, e.g. buttons
    public DynamicPanel getDynpanel() {
    return window.getDynamicPanel();
    }
    // get the user menu instance to add custom items and submenus to.
    public JMenu getUsermenu() {
    return window.getSDEUserMenu();
    }
    // create a process to run a shell command in a given directory
    public Process exec(String command, File inDir) {
    Process proc = null;
    try {
    proc = Runtime.getRuntime().exec(command, null, inDir);
    } catch (Exception e) {
    displayExecError(e.toString());
    }
    return proc;
    }
    // create a process to run a shell command
    public Process exec(String command) {
    Process proc = null;
    try {
    proc = Runtime.getRuntime().exec(command);
    } catch (Exception e) {
    displayExecError(e.toString());
    }
    return proc;
    }
    private void displayExecError(String message) {
    ScriptOutputDialog win = window.newOutputDialog("Groovy Error", "Error during exec");
    win.addTabPane("error");
    win.println("error", message);
    }
     */
}
