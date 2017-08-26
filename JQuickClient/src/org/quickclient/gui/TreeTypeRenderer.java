/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quickclient.gui;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author Mika
 */
public class TreeTypeRenderer extends DefaultTreeCellRenderer {

	java.net.URL imageURL = null;
	ImageIcon icon = null;

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf,
			int row, boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		String type = "";
		type = getType(value);
		 System.out.println("type:" + type);

		if (type.length() > 1 && type != null) {
			if (type.equals("NOTATYPE")) {
				String special = getSpecialString(value);
				System.out.println("special:" + special);
				if (special.length() > 0) {
					if (special.equals("cabinets")) {
						imageURL = FormatRenderer.class.getResource("cabinets.gif");
					} else if (special.equals("subscriptions")) {
						imageURL = FormatRenderer.class.getResource("/favorites.gif");
					} else if (special.equals("homecabinet")) {
	//					imageURL = FormatRenderer.class.getResource("/15.gif");
						setIcon(UIManager.getIcon("FileChooser.homeFolderIcon"));
					} else if (special.equals("checkedout")) {
						imageURL = FormatRenderer.class.getResource("/72.gif");
					} else if (special.equals("myfiles")) {
						imageURL = FormatRenderer.class.getResource("myfiles.gif");
					} else if (special.equals("myrooms")) {
						imageURL = FormatRenderer.class.getResource("/type/t_dmc_room_16.gif");
					}

				}
			} else {
				imageURL = FormatRenderer.class.getResource("/type/t_" + type + "_16.gif");
			}
		} else {
			imageURL = FormatRenderer.class.getResource("/type/t_dm_folder_16.gif");
		}
		 System.out.println("imageurl:" + imageURL);

		if (imageURL != null) {
			//if (icon != null) {
				icon = new ImageIcon(imageURL);
				setIcon(icon);
			//}
		}
		return this;
	}

	protected String getType(Object value) {
		QuickClientMutableTreeNode node = (QuickClientMutableTreeNode) value;
		if (node.getSpecialString().length() > 0) {
			return "NOTATYPE";
		} else {
			return node.getObjType();
		}
	}

	protected String getSpecialString(Object value) {
		QuickClientMutableTreeNode node = (QuickClientMutableTreeNode) value;
		return node.getSpecialString();
	}
}
