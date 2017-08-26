package org.quickclient.gui;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.quickclient.classes.ExJTextArea;
import org.quickclient.classes.ExJTextField;

public final class TextFieldFactory {

	public static ExJTextArea createJTextarea() {
		final ExJTextArea textArea = new ExJTextArea();
		textArea.putClientProperty("haschanges", false);
		final TextPopUp pu = new TextPopUp();
		pu.setTextArea(textArea);
		textArea.setComponentPopupMenu(pu);
		return textArea;
	}

	/**
	 * @wbp.factory
	 */
	public static ExJTextField createJTextField() {
		final ExJTextField textField = new ExJTextField();
		textField.putClientProperty("haschanges", false);
		textField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(final DocumentEvent arg0) {
				textField.putClientProperty("haschanges", true);
			}

			@Override
			public void insertUpdate(final DocumentEvent arg0) {
				textField.putClientProperty("haschanges", true);
			}

			@Override
			public void removeUpdate(final DocumentEvent arg0) {
				textField.putClientProperty("haschanges", true);
			}
		});
		textField.setColumns(10);
		final TextPopUp pu = new TextPopUp();
		pu.setTextField(textField);
		textField.setComponentPopupMenu(pu);
		return textField;
	}

	private TextFieldFactory() {
		// LL
	}
}