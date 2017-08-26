/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quickclient.classes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class HTTPClient {

    public String getBuildNumber(String strurl) {
        String jee = "";
        try {
            URL url = new URL(strurl);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(3000);
            urlConnection.setReadTimeout(2000);
            urlConnection.setUseCaches(false);

            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = bufferedReader.readLine();
            while (line != null) {
                ////System.out.println(line);
                jee = line;
                line = bufferedReader.readLine();

            }
            //TODO päin vittua on tämö
            bufferedReader.close();
        } catch (MalformedURLException e) {
            JOptionPane.showMessageDialog(null, e.toString(), "Error occured!", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.toString(), "Error occured!", JOptionPane.ERROR_MESSAGE);
        }
        return jee;
    }
}
