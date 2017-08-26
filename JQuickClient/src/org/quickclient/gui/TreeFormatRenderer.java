/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quickclient.gui;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.quickclient.classes.NodeObj;

/**
 *
 * @author Administrator
 */
class TreeFormatRenderer extends DefaultTreeCellRenderer {

	protected String getFormatInfo(final Object value) {
		final DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		final NodeObj nodeInfo = (NodeObj) node.getUserObject();
		return nodeInfo.getFormat();
	}

	@Override
	public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean sel, final boolean expanded, final boolean leaf, final int row, final boolean hasFocus) {
		ImageIcon icon = null;
		java.net.URL imageURL = null;
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		final String jeejee = getFormatInfo(value);
		imageURL = TreeFormatRenderer.class.getResource("format/f_" + jeejee + "_16.gif");
		if (imageURL != null) {
			icon = new ImageIcon(imageURL);
		}

		setIcon(icon);

		return this;
	}
}
