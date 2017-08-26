/*
 * LockRenderer.java
 *
 * Created on 28. maaliskuuta 2007, 1:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.quickclient.gui;


import java.awt.Component;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.quickclient.classes.DocuSessionManager;

/**
 *
 * @author Administrator
 */
public class LockRenderer extends DefaultTableCellRenderer {

    private URL imageURL;
    /*
     * @see TableCellRenderer#getTableCellRendererComponent(JTable, Object, boolean, boolean, int, int)
     */

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        ImageIcon icon = null;
        if (value.toString().length() > 1) {
            String u = DocuSessionManager.getInstance().getUserName();
            if(u.equals(value.toString())) {
            imageURL = FormatRenderer.class.getResource("/key.gif");
            } else {
            imageURL = FormatRenderer.class.getResource("i_locked_by_another_16.gif");                
            }
        } else {
            imageURL = FormatRenderer.class.getResource("iconsx/i_locked_by_another_16.gif");            
        }
        if (imageURL != null) {
            icon = new ImageIcon(imageURL);
        }
        setText("");
        if (icon != null) {
            setIcon(icon);
        } else {
            setIcon(null);
        }
        return this;
    }
}
