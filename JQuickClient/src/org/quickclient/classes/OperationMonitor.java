package org.quickclient.classes;

import javax.swing.ProgressMonitor;

import com.documentum.fc.common.DfException;
import com.documentum.operations.IDfOperation;
import com.documentum.operations.IDfOperationError;
import com.documentum.operations.IDfOperationMonitor;
import com.documentum.operations.IDfOperationNode;
import com.documentum.operations.IDfOperationStep;

public class OperationMonitor implements IDfOperationMonitor {

	private ProgressMonitor progressMonitor;
	private int totalloopcount = 0;

	public OperationMonitor(ProgressMonitor progressMonitor) {
		this.progressMonitor = progressMonitor;
		this.progressMonitor.setMillisToDecideToPopup(100);

	}
	public OperationMonitor( ) {


	}
	@Override
	public int getYesNoAnswer(IDfOperationError question) throws DfException {
		// Called by operation when an error / warning requires a yes or no response. 
		//The monitor can return IDfOperationMonitor.YES, IDfOperationMonitor.NO, or IDfOperationMonitor.ABORT.
		
		System.out.println("yes no answer needed." + question.getMessage());
		return 0;
	}

	@Override
	public int progressReport(IDfOperation operation, int operationPercentDone, IDfOperationStep step, int stepPercentDone, IDfOperationNode node) throws DfException {
		
//		System.out.println("op done" + operationPercentDone);
//		System.out.println("step done " + stepPercentDone);
//		System.out.println(operation.getName());
//		System.out.println(operation.getDescription());
//		IDfProperties props = node.getProperties();
//		IDfList propnames = props.getProperties();
//		for (int i=0;i<propnames.getCount(); i++) {
//			System.out.println(propnames.getString(i));
//		}
//		totalloopcount++;
//		if (progressMonitor != null) {
//			progressMonitor.setProgress(totalloopcount);
//		}
		return 0;
	}

	@Override
	public int reportError(IDfOperationError error) throws DfException {
		System.out.println(error.getMessage());
//		SwingHelper.showErrorMessage("Error occurred", error.getMessage());
		return IDfOperationMonitor.CONTINUE;
	}

}
