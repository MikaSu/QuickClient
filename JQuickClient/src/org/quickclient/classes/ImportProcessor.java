package org.quickclient.classes;

import javax.swing.SwingWorker;

import com.documentum.operations.IDfImportOperation;


public class ImportProcessor extends SwingWorker<String, String> {

	private IDfImportOperation operation;

	public ImportProcessor(IDfImportOperation importop) {
		this.operation = importop;
	}

	@Override
	protected String doInBackground() throws Exception {
		operation.execute();
		return "";
	}

}
