/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.quickclient.gui;

import java.util.ArrayList;

/**
 *
 * @author Mika
 */
class MediaTransformationFormats {
private String sourceFormat;
private ArrayList<String> targetFormats = new ArrayList<String>();

    /**
     * @return the sourceFormat
     */
    public String getSourceFormat() {
        return sourceFormat;
    }

    /**
     * @param sourceFormat the sourceFormat to set
     */
    public void setSourceFormat(String sourceFormat) {
        this.sourceFormat = sourceFormat;
    }

    /**
     * @return the targetFormats
     */
    public ArrayList<String> getTargetFormats() {
        return targetFormats;
    }

    /**
     * @param targetFormats the targetFormats to set
     */
    public void setTargetFormats(ArrayList<String> targetFormats) {
        this.targetFormats = targetFormats;
    }
    
    public void appendTargetFormat(String format) {
        this.targetFormats.add(format);
    }

}
