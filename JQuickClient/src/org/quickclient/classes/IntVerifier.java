package org.quickclient.classes;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

import org.quickclient.classes.SwingHelper;


public class IntVerifier extends InputVerifier {

	@Override
	public boolean verify(JComponent input) {
		 String text = ((JTextField) input).getText();
	        try {
	        	if (text.length()==0)
	        		return true;
	            Integer.parseInt(text);
	            return true;
	        } catch (NumberFormatException e) {
	        	SwingHelper.showInfoMessage("Invalid Validation Error!", "Invalid input: '" + text + "', integer needed.");
	            return false;
	        }
	}

}
