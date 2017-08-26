/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quickclient.classes;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Administrator
 */
public class PackageTransferHandler extends TransferHandler {

    private int[] indices = null;
    private int addIndex = -1; //Location where items were added


    private int addCount = 0;  //Number of items added.

    private String targetid;
    private String sourceid;
    DefaultTableModel model;

    public void setmodel(DefaultTableModel packageModel) {
        this.model = packageModel;
    }

 

    @Override
    protected Transferable createTransferable(JComponent c) {
        JTable table = (JTable) c;
        StringBuffer buff = new StringBuffer();
        indices = table.getSelectedRows();
        int[] row = table.getSelectedRows();
        Vector idVector = new Vector();
        for (int i = 0; i < row.length; i++) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            Vector v = (Vector) model.getDataVector().elementAt(row[i]);
            ////System.out.println(v);
            ////System.out.println("last element" + v.lastElement());

            DokuData d = (DokuData) v.lastElement();
            String objid = d.getObjID();
            buff.append(objid);
        }


        return new StringSelection(buff.toString());
    }

    /**
     * We support both copy and move actions.
     */
    @Override
    public int getSourceActions(JComponent c) {
        return TransferHandler.COPY_OR_MOVE;
    }

    @Override
     public boolean canImport(TransferHandler.TransferSupport info) {
        // for the demo, we'll only support drops (not clipboard paste)
        if (!info.isDrop()) {
            return false;
        }

        info.setShowDropLocation(true);

        // we only import Strings
        if (!info.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            return false;
        }

        // fetch the drop location
        JTable.DropLocation dl = (JTable.DropLocation) info.getDropLocation();
        
        int row = dl.getRow();


    // we don't support invalid paths or descendants of the names folder

    return true;
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport info) {
        // if we can't handle the import, say so
        if (!canImport(info)) {
            return false;
        }

        // fetch the drop location
        JTable.DropLocation dl = (JTable.DropLocation) info.getDropLocation();
        int row = dl.getRow();

        // fetch the data and bail if this fails
        //String sourceid = "";

        try {
            sourceid = (String) info.getTransferable().getTransferData(DataFlavor.stringFlavor);
            //System.out.println(sourceid);
            Vector v = new Vector();
            v.add(sourceid);
            model.addRow(v);
            
        } catch (UnsupportedFlavorException e) {
            return false;
        } catch (IOException e) {
            return false;
       }
        return true;
    }

    /**
     * We only support importing strings.
     */
    /**
     * Remove the items moved from the list.
     */
    @Override
    protected void exportDone(JComponent c, Transferable data, int action) {
        /* JTable source = (JTable) c;
        DefaultTableModel model = (DefaultTableModel) source.getModel();
        
        if (action == TransferHandler.MOVE) {
        for (int i = indices.length - 1; i >= 0; i--) {
        model.removeRow(i);
        }
        }
        
        indices = null;
        addCount = 0;
        addIndex = -1;*/
    }

}


