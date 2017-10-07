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
import java.net.MalformedURLException;

import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Administrator
 */
public class FormatRenderer extends DefaultTableCellRenderer {

	private static final String GIF_SUFFIX = "_16.gif";
	private boolean showThumbnails;

	/*
	 * @see TableCellRenderer#getTableCellRendererComponent(JTable, Object,
	 * boolean, boolean, int, int)
	 */

	@Override
	public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {

		final String xx = (String) value;
		ImageIcon icon = null;
		if (!showThumbnails) {
			if (xx != null) {
				String type = "";
				final String[] values = xx.split(",");
				final String format = values[0];
				if (values.length > 1) {
					type = values[1];
				}
				java.net.URL imageURL = null;

				if (format.length() > 1) {
					if (format.equals("virtual")) {
						imageURL = FormatRenderer.class.getResource("/type/t_vdoc_16.gif");
					} else {
						imageURL = FormatRenderer.class.getResource("/format/f_" + format + GIF_SUFFIX);
					}
				}
				if (type.length() > 1 && format.length() == 0) {
					imageURL = FormatRenderer.class.getResource("/type/t_" + type + GIF_SUFFIX);
				}
				if (type.length() > 1 && format.equals("dm_internal")) {
					imageURL = FormatRenderer.class.getResource("/type/t_" + type + GIF_SUFFIX);
				}
				if (imageURL != null) {
					icon = new ImageIcon(imageURL);
				}
			}
		} else {

			try {
				icon = new ImageIcon(new java.net.URL(xx));
			} catch (final MalformedURLException ex) {
				// TODO logging
			}
		}
		setText("");
		setIcon(icon);
		return this;
	}

	public boolean isShowThumbnails() {
		return showThumbnails;
	}

	public void setShowThumbnails(final boolean showThumbnails) {
		this.showThumbnails = showThumbnails;
	}
}
