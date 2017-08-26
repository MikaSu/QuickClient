package org.quickclient.gui;

import java.awt.event.ActionEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.quickclient.classes.ExJTextArea;
import org.quickclient.classes.ExJTextField;
import org.quickclient.classes.Utils;


public class TextPopUp extends JPopupMenu {
	private JMenuItem mnuCut;
	private JMenuItem mnuCopy;
	private JMenuItem mnuPaste;
	private JMenuItem mnuSelectAll;
	private ExJTextField textField = null;
	private ExJTextArea textArea = null;

	public TextPopUp() {
		initMenu();
	}

	public TextPopUp(ExJTextField textField) {
		this.textField = textField;
	}

	private void initMenu() {
		mnuCut = new javax.swing.JMenuItem();
		mnuCut.setText("Cut");
		mnuCut.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cutPerformed(evt);
			}
		});
		this.add(mnuCut);
		
		mnuCopy = new javax.swing.JMenuItem();
		mnuCopy.setText("Copy");
		mnuCopy.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				copyPerformed(evt);
			}
		});
		this.add(mnuCopy);
		
		mnuPaste = new javax.swing.JMenuItem();
		mnuPaste.setText("Paste");
		mnuPaste.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				pastePerformed(evt);
			}
		});
		this.add(mnuPaste);
		
	}

	protected void pastePerformed(ActionEvent evt) {
		if (textField != null) {
			textField.setText(Utils.getClipboard());
		}
		if (textArea != null) {
			textArea.setText(Utils.getClipboard());
		}
	}

	protected void copyPerformed(ActionEvent evt) {
		if (textField != null) {
			Utils.setClipboard(textField.getText());
		}
		if (textArea != null) {
			Utils.setClipboard(textArea.getText());
		}
	}

	protected void cutPerformed(ActionEvent evt) {
		System.out.println("CUT!");
		if (textField != null) {
			Utils.setClipboard(textField.getText());
			textField.setText("");
		}
		if (textArea != null) {
			Utils.setClipboard(textArea.getText());
			textArea.setText("");			
		}
	}

	public void setTextField(ExJTextField textField2) {
		this.textField = textField2;
	}

	public void setTextArea(ExJTextArea textArea) {
		this.textArea = textArea;
		
	}
}
