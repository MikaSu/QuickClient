/*
 * FileClassLoader.java
 *
 * Created on 6. heinäkuuta 2007, 22:51
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.quickclient.classes;
import java.io.*;
import org.apache.log4j.Logger;
public class FileClassLoader extends ClassLoader {
    
    private String root;
    Logger log = Logger.getLogger(FileClassLoader.class);
    
    public FileClassLoader(String rootDir) {
        if (rootDir == null)
            throw new IllegalArgumentException("Null root directory");
        root = rootDir;
    }
    
    protected Class loadClass(String name, boolean resolve)
    throws ClassNotFoundException {
        
        // Since all support classes of loaded class use same class loader
        // must check subclass cache of classes for things like Object
        
        Class c = findLoadedClass(name);
        if (c == null) {
            try {
                c = findSystemClass(name);
            } catch (Exception e) {
            }
        }
        
        if (c == null) {
            // Convert class name argument to filename
            // Convert package names into subdirectories
            String filename = name.replaceAll("\\\\", "/");
            filename = filename + ".class";
            ////System.out.println("filename" + filename );
            try {
                //.replace ('.', File.separatorChar) + ".class";
                
                byte data[] = loadClassData(filename);
                ////System.out.println(data);
                c = defineClass(name, data, 0, data.length);
                if (c == null) {
                    throw new ClassNotFoundException(name);
                }
            } catch (ClassFormatError ex) {
                log.error(ex);
            } catch (ClassNotFoundException ex) {
                log.error(ex);
            } catch (IOException ex) {
                log.error(ex);
            }
        }
        if (resolve) {
            resolveClass(c);
        }
        return c;
    }
    private byte[] loadClassData(String filename)
    throws IOException {
        
        // Create a file object relative to directory provided
        File f = new File(root, filename);
        // Get size of class file
        int size = (int)f.length();
        
        // Reserve space to read
        byte buff[] = new byte[size];
        
        // Get stream to read from
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        
        // Read in data
        dis.readFully(buff);
        
        // close stream
        dis.close();
        
        // return data
        return buff;
    }
}