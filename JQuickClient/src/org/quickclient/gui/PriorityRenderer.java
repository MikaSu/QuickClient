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
class PriorityRenderer extends DefaultTableCellRenderer {

    public PriorityRenderer() {
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        String xx = (String) value;
        ImageIcon icon = null;
        java.net.URL imageURL = null;
        int prio = Integer.parseInt(xx);
        if (prio < 5) {
            imageURL = FormatRenderer.class.getResource("/i_task_priority_low_16.gif");
        } else if (prio > 4 && prio < 10) {
            imageURL = FormatRenderer.class.getResource("/i_task_priority_medium_16.gif");
        } else {
            imageURL = FormatRenderer.class.getResource("/i_task_priority_high_16.gif");
        }
        if (imageURL != null) {
            icon = new ImageIcon(imageURL);
        }
        setText("");
        setIcon(icon);
        return this;
    }
}
