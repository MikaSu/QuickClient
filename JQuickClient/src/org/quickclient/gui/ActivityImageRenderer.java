/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quickclient.gui;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author miksuoma
 */
class ActivityImageRenderer extends DefaultTableCellRenderer {

    public ActivityImageRenderer() {
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        String xx = (String) value;
        ImageIcon icon = null;
        java.net.URL imageURL = null;
        if (xx.equals("0")) {
            imageURL = FormatRenderer.class.getResource("/user2.gif");
        } else {
            imageURL = FormatRenderer.class.getResource("/user2_inactive.gif");
        }
        if (imageURL != null) {
            icon = new ImageIcon(imageURL);
        } 
        setText("");
        setIcon(icon);
        return this;
    }
}
