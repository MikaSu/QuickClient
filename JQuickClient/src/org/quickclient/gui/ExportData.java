/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.quickclient.gui;

/**
 *
 * @author Mika
 */
public class ExportData {

    private String exportDir;
    private boolean exportrenditionds = false;
    private boolean deepexport = true;
    private int maxMegaBytes = -1;
    private int maxObjCount = -1;

    /**
     * @return the exportDir
     */
    public String getExportDir() {
        return exportDir;
    }

    /**
     * @param exportDir the exportDir to set
     */
    public void setExportDir(String exportDir) {
        this.exportDir = exportDir;
    }

    /**
     * @return the exportrenditionds
     */
    public boolean isExportrenditionds() {
        return exportrenditionds;
    }

    /**
     * @param exportrenditionds the exportrenditionds to set
     */
    public void setExportrenditionds(boolean exportrenditionds) {
        this.exportrenditionds = exportrenditionds;
    }

    /**
     * @return the deepexport
     */
    public boolean isDeepexport() {
        return deepexport;
    }

    /**
     * @param deepexport the deepexport to set
     */
    public void setDeepexport(boolean deepexport) {
        this.deepexport = deepexport;
    }

    /**
     * @return the maxMegaBytes
     */
    public int getMaxMegaBytes() {
        return maxMegaBytes;
    }

    /**
     * @param maxMegaBytes the maxMegaBytes to set
     */
    public void setMaxMegaBytes(int maxMegaBytes) {
        this.maxMegaBytes = maxMegaBytes;
    }

    /**
     * @return the maxObjCount
     */
    public int getMaxObjCount() {
        return maxObjCount;
    }

    /**
     * @param maxObjCount the maxObjCount to set
     */
    public void setMaxObjCount(int maxObjCount) {
        this.maxObjCount = maxObjCount;
    }


}
