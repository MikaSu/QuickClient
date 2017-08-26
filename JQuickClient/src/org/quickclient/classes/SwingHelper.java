/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quickclient.classes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.quickclient.gui.MessageFrame;

/**
 *
 * @author Mika
 */
public class SwingHelper {
	
	private SwingHelper() {
		
	}

    public static void centerJFrame(JFrame frame) {
        
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        frame.setLocation(screenWidth / 4, screenHeight / 4);
    }
        public static void centerJFrameSmall(JFrame frame) {

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        frame.setLocation(screenWidth / 3, screenHeight / 2);
    }
    public static void showMessage(String message) {
        MessageFrame frame = new MessageFrame(message);
        frame.setBackground(new Color(255,255,255));
        centerJFrameSmall(frame);
        frame.setVisible(true);
        frame.startTimer();
        
    }
    /**
     * 
     * @param string
     * @param message
     */
	public static void showErrorMessage(String string, String message) {
		JOptionPane.showMessageDialog(null, message, "Error occured!", JOptionPane.ERROR_MESSAGE);
		
	}
	/**
	 * 
	 * @param title dialog title
	 * @param message message
	 */
	public static void showInfoMessage(String title, String message) {
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
		
	}
}

