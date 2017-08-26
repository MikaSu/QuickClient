/*
 * TypeRenderer.java
 *
 * Created on 3. huhtikuuta 2007, 23:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
/*
 * FormatRenderer.java
 *
 * Created on 27. maaliskuuta 2007, 23:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.quickclient.gui;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * 
 * @author Administrator
 */
public class TypeRenderer extends DefaultTableCellRenderer {

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		String type = (String) value;
		return this;
	}
}
