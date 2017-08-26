/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quickclient.exportlib;

import java.io.File;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfLogger;

/**
 *

 */
public class ExportWorker extends Thread implements Callable<String> {

	Logger log = Logger.getLogger(ExportWorker.class);
	IDfSession session = null;;
	ConfigReader cr = ConfigReader.getInstance();
	private final String targetDir;

	public ExportWorker(final String threadid, final IDfSession session, final String targetDir) {

		super(threadid);
		this.session = session;
		this.targetDir = targetDir;
		log.debug("Worker " + threadid + " started.");
		try {
			log.debug("threadid " + threadid + " got session: " + session.getSessionId());
		} catch (final Exception ex) {
			DfLogger.error(this, "Failed to get session, exiting.", null, ex);
			log.error("Doing system.exit, rerun the export set please.");
			System.exit(1);
		}
	}

	@Override
	public void run() {
		if (session != null) {
			String id = "";
			final XMLExporter e = new XMLExporter();
			while (true) {
				id = IdHolder.getId();
				if (id != null) { // TODO fiksaa 53
					// make folder for each i_chronicle_id.
					final File file = new File(targetDir + "/" + id);
					final boolean success = file.mkdir();
					if (success) {
						e.exportVersionTree(session, id, file, targetDir);
					} else {
						log.error("Failed to create a folder.");
						log.error("Doing system.exit, rerun the export set please.");
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
			final XMLExporter e = new XMLExporter();
			while (true) {
				id = IdHolder.getId();
				if (id != null) { // TODO fiksaa 53
					// make folder for each i_chronicle_id.
					final File file = new File(targetDir + "/" + id);
					final boolean success = file.mkdir();
					if (success) {
						e.exportVersionTree(session, id, file, targetDir);
					} else {
						log.error("Failed to create a folder.");
						log.error("Doing system.exit, rerun the export set please.");
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
