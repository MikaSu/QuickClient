package org.quickclient.classes;

import javax.swing.InputVerifier;
import javax.swing.JComponent;

public class NullVerifier extends InputVerifier {

	@Override
	public boolean verify(JComponent input) {
		return true;
	}

}
