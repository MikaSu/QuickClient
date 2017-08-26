package org.quickclient.gui;

import org.fife.ui.autocomplete.DefaultCompletionProvider;

public class APICompletionProvider extends DefaultCompletionProvider {
	protected boolean isValidChar(char ch) {
	    return super.isValidChar(ch) || ch==',';
	}
}
