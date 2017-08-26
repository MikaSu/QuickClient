package org.quickclient.gui;

import org.fife.ui.autocomplete.DefaultCompletionProvider;

public class DQLCompletionProvider extends DefaultCompletionProvider {
	protected boolean isValidChar(char ch) {
	    return super.isValidChar(ch) || ch=='.'|| ch==' ';
	}
}
