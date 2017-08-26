package org.quickclient.classes;



import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTextField;

public class ExJTextField extends JTextField {
	private JPopupMenu popup;
	private JMenuItem mnuCopy = new JMenuItem();
	private JMenuItem mnuCut = new JMenuItem();
	private JMenuItem mnuPaste = new JMenuItem();
	private JMenuItem mnuSelectAll = new JMenuItem();
	public ExJTextField() {
		popup = new JPopupMenu();
		this.setComponentPopupMenu(popup);
		initMenu();
	}
	public ExJTextField(String controltype) {
		super(controltype);
	}
	private void initMenu() {
		mnuCut.setText("Cut");
		mnuCut.addActionListener(new ActionListener()
	    {
		@Override
		public void actionPerformed(ActionEvent e) {
			mnuCutActionPerformed(e);
		}
	    });
	    popup.add(this.mnuCut);
	    //
	    mnuCopy.setText("Copy");
	    mnuCopy.addActionListener(new ActionListener()
	    {
		@Override
		public void actionPerformed(ActionEvent e) {
			mnuCopyActionPerformed(e);
		}
	    });
	    popup.add(this.mnuCopy);
	    //
	    mnuPaste.setText("Paste");
	    mnuPaste.addActionListener(new ActionListener()
	    {
		@Override
		public void actionPerformed(ActionEvent e) {
			mnuPasteActionPerformed(e);
		}
	    });
	    popup.add(this.mnuPaste);
	    //
	    popup.add(new JSeparator());
	    //
	    mnuSelectAll.setText("Select All");
	    mnuSelectAll.addActionListener(new ActionListener()
	    {
		@Override
		public void actionPerformed(ActionEvent e) {
			mnuSelectAllActionPerformed(e);
		}
	    });
	    popup.add(this.mnuSelectAll);
	}
	protected void mnuSelectAllActionPerformed(ActionEvent e) {
		this.selectAll();
	
	}
	protected void mnuPasteActionPerformed(ActionEvent e) {
	    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	    try {
	    	String text = (String) clipboard.getData(DataFlavor.stringFlavor);
			this.replaceSelection(text);
		} catch (UnsupportedFlavorException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}
	protected void mnuCopyActionPerformed(ActionEvent e) {
		String text = this.getSelectedText();
	    StringSelection data = new StringSelection(text);
	    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	    clipboard.setContents(data, data);
		
	}
	protected void mnuCutActionPerformed(ActionEvent e) {
		String text = this.getSelectedText();
	    StringSelection data = new StringSelection(text);
	    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	    clipboard.setContents(data, data);
	    this.replaceSelection("");
	}
}
