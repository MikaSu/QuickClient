package org.quickclient.classes;

import java.io.File;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

import org.quickclient.classes.SwingHelper;


public class PathVerifier extends InputVerifier {

	@Override
	public boolean verify(JComponent input) {
		String text = ((JTextField) input).getText();
		if (text.length() == 0)
			return true;
		File f = new File(text);
		if (f.exists())
			if (f.isFile())
				return true;
		SwingHelper.showInfoMessage("Invalid Validation Error!", "Invalid input: '" + text + "', Valid file path needed");
		return false;

	}

}
