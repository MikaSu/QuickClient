/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.fortica.dctm;

import java.io.File;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfLogger;

/**
 * 
 * @author miksuoma
 */
public class ExportWorker extends Thread implements Callable<String> {

	IDfSession session = null;
	Logger log = Logger.getLogger(ExportWorker.class);
	ConfigReader cr = ConfigReader.getInstance();
	private String targetDir;


	public ExportWorker(String threadid, IDfSession session, String targetDir) {
		
		super(threadid);
		this.session = session;
		this.targetDir = targetDir;
		System.out.println("Worker " + threadid + " started.");
		try {
			System.out.println("threadid " + threadid + " got session: " + session.getDMCLSessionId());
		} catch (Exception ex) {
			DfLogger.error(this, "Failed to get session, exiting.", null, ex);
			System.out.println("Doing system.exit, rerun the export set please.");
			System.exit(1);
		}
	}

	@Override
	public void run() {
		if (session != null) {
			String id = "";
			XMLExporter e = new XMLExporter();
			while (true) {
				id = IdHolder.getId();
				if (id != null) { // TODO fiksaa 53
					// make folder for each i_chronicle_id.
					File file = new File(targetDir + "/" + id);
					boolean success = file.mkdir();
					if (success) {
						e.exportVersionTree(session, id, file, targetDir);
					} else {
						System.out.println("Failed to create a folder.");
						System.out.println("Doing system.exit, rerun the export set please.");
						System.exit(1);
					}

				} else {
					DfLogger.error(this, "Invalid id: " + id, null, null);
				}
			}
		}
	}

	@Override
	public String call() throws Exception {
		if (session != null) {
			String id = "";
			XMLExporter e = new XMLExporter();
			while (true) {
				id = IdHolder.getId();
				if (id != null) { // TODO fiksaa 53
					// make folder for each i_chronicle_id.
					File file = new File(targetDir + "/" + id);
					boolean success = file.mkdir();
					if (success) {
						e.exportVersionTree(session, id, file, targetDir);
					} else {
						System.out.println("Failed to create a folder.");
						System.out.println("Doing system.exit, rerun the export set please.");
						return "1";
					}

				} else {
					DfLogger.error(this, "Invalid id: " + id, null, null);
				}
			}
		}
		return "0";
	}
}
