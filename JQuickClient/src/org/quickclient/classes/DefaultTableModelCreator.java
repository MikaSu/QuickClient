package org.quickclient.classes;

import java.util.ArrayList;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public class DefaultTableModelCreator {

	public DefaultTableModel createModel( ) {
		ConfigService cs = ConfigService.getInstance();
		Vector<ListAttribute> listattributes;
		ArrayList<String> nonsysobjattrs = new ArrayList<String>();
		listattributes = cs.getAttributes(cs.getCurrentListConfigName()).get();
		DefaultTableModel model = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		model.setColumnCount(0);
		model.addColumn(".");
		model.addColumn(".");
		LabelCache lc = LabelCache.getInstance();
		LabelValues lv = lc.getLabels("dm_sysobject");
		model.addColumn(lv.getLabel("object_name"));

		// additional column values
		int xx = listattributes.size();
		for (int i = 0; i < xx; i++) {
			String label = listattributes.get(i).getLabel();
			model.addColumn(label);
		}
		for (String nonsysobjattr : nonsysobjattrs) {
			model.addColumn(nonsysobjattr);
		}
		model.addColumn("data");
		return model;
	}
	
	public DefaultTableModel createModel(String configname) {
		ConfigService cs = ConfigService.getInstance();
		Vector<ListAttribute> listattributes;
		ArrayList<String> nonsysobjattrs = new ArrayList<String>();
		listattributes = cs.getAttributes(configname).get();
		DefaultTableModel model = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		model.setColumnCount(0);
		model.addColumn(".");
		model.addColumn(".");
		LabelCache lc = LabelCache.getInstance();
		LabelValues lv = lc.getLabels("dm_sysobject");
		model.addColumn(lv.getLabel("object_name"));

		// additional column values
		int xx = listattributes.size();
		for (int i = 0; i < xx; i++) {
			String label = listattributes.get(i).getLabel();
			model.addColumn(label);
		}
		for (String nonsysobjattr : nonsysobjattrs) {
			model.addColumn(nonsysobjattr);
		}
		model.addColumn("data");
		return model;
	}

}
