/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quickclient.exportlib;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.DfType;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfFolder;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfLogger;

/**
 * Exports Documentum folder with contents into local filesystem.
 *
 *
 */
public class DocumentExporter {

	private ArrayList<String> whereClauses = new ArrayList<>();
	private ArrayList<String> types = new ArrayList<>();
	private final ConfigReader cr = null;
	private IDfSession session = null;
	private String exportFSPath = null;

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(final String[] args) {
		System.out.println("args[0] " + args[0]);
	}

	public DocumentExporter(final IDfSession session2) {
		this.session = session2;

	}

	private ArrayList<String> getDataList(final IDfSession session, final String dql, final String column) throws DfException {
		IDfCollection col = null;
		final ArrayList<String> list = new ArrayList<String>();
		final IDfQuery query = new DfQuery();
		try {
			query.setDQL(dql);
			col = query.execute(session, DfQuery.DF_READ_QUERY);
			while (col.next()) {
				final String d = col.getString(column);
				list.add(d);
			}
		} catch (final DfException e) {
			e.printStackTrace();
		} finally {
			if (col != null) {
				col.close();
			}
		}
		return list;
	}

	public DocumentExporter() {

	}

	/**
	 * Dump contents of the folder
	 *
	 * @param folder
	 *            folder to dump
	 * @throws DfException
	 *             If everything goes tits up
	 */
	public void dumpDocument(final IDfFolder folder) throws DfException {
		types = getDataList(session, "select distinct r_object_type from dm_sysobject(ALL) where FOLDER(ID('" + folder.getObjectId().getId() + "'), descend)", "r_object_type");
		types.add(folder.getTypeName());
		for (final String type : types) {
			populateTypeData(type);
		}

		getIdsFromQuery(" FOLDER(ID('" + folder.getObjectId().getId() + "'), descend)");
		final ExportWorker callable1 = new ExportWorker("1", session, this.exportFSPath);
		final FutureTask<String> futureTask1 = new FutureTask<String>(callable1);
		final ExecutorService executor = Executors.newFixedThreadPool(1);
		executor.execute(futureTask1);

		while (true) {
			if (futureTask1.isDone()) {
				System.out.println("Done");
				executor.shutdown();
				return;
			}
			try {
				Thread.sleep(1000);
			} catch (final InterruptedException e) {
				// NN
			}
			System.out.println("Waiting for Task to complete");
		}

	}

	private void dumpDocuments() {
		if (cr == null) {
			System.out.println("cannot read queries, exiting");
			System.exit(1);
		}
		whereClauses = readconfig(cr.getProperty("config_dir") + "/queryconfig.txt");
		types = readconfig(cr.getProperty("config_dir") + "/types.txt");
		readReplacePaths(cr.getProperty("config_dir") + "/paths.txt");

		DfLogger.info(this, "Configuration file has: " + whereClauses.size() + " where clauses.", null, null);

		for (final String type : types) {
			populateTypeData(type);
		}

		for (final String whereclause : whereClauses) {
			getIdsFromQuery(whereclause);
		}

		for (int i = 0; i < 1; i++) {
			final String threadid = String.valueOf(i);
			new ExportWorker(threadid, session, "c:/test").start();
		}

	}

	private ArrayList<String> readconfig(final String configfile) {
		final ArrayList<String> v = new ArrayList<>();
		try {
			final FileInputStream fstream = new FileInputStream(configfile);
			final DataInputStream in = new DataInputStream(fstream);
			final BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				System.out.println("read line: " + strLine);
				if (strLine.startsWith("#")) {
					System.out.println("skipped comment.");
				} else {
					if (strLine.length() > 5) {
						v.add(strLine);
					}
				}
			}
			in.close();
		} catch (final Exception e) {
			System.out.println("could not read config, exiting.");
			DfLogger.fatal(this, "could not read config, exiting.", null, e);
			System.exit(1);
		}
		return v;
	}

	private void getIdsFromQuery(final String whereclause) {
		IDfCollection col = null;
		DfLogger.info(this, "get session for query....", null, null);
		final IDfQuery query = new DfQuery();
		final String dql = "select i_chronicle_id from dm_document where " + whereclause;
		DfLogger.info(this, "Executeing query: " + dql, null, null);
		query.setDQL(dql);
		try {
			col = query.execute(session, DfQuery.DF_QUERY);
			while (col.next()) {
				final String id = col.getString("i_chronicle_id");
				if (id != null) { // TODO fiksaa 53
					DfLogger.info(this, "Added id: " + id + " to exportlist.", null, null);
					IdHolder.addId(id);
				} else {
					DfLogger.error(this, "Got invalid id: " + id + " from query: " + dql, null, null);
				}
			}
		} catch (final DfException ex) {
			DfLogger.error(this, "Failure during query for ids. Fatal, will exit.", null, ex);
			System.out.println("Doing system.exit, rerun the export set please.");
			System.exit(1);
		} finally {
			if (col != null) {
				try {
					col.close();
				} catch (final DfException ex) {
					Logger.getLogger(DocumentExporter.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}

	}

	private void populateTypeData(final String type) {
		IDfCollection col = null;
		final IDfQuery query = new DfQuery();
		final String dql = "select attr_name, attr_repeating, attr_type from dm_type where name = '" + type + "' order by attr_name";
		query.setDQL(dql);
		final TypeInfo info = new TypeInfo();
		info.setTypeName(type);
		try {
			col = query.execute(session, DfQuery.DF_QUERY);
			while (col.next()) {
				final AttrInfo attrInfo = new AttrInfo();
				final String attrName = col.getString("attr_name");
				final int attrType = col.getInt("attr_type");
				final boolean attrRepating = col.getBoolean("attr_repeating");
				attrInfo.setAttrName(attrName);
				attrInfo.setDatatype(attrType);
				attrInfo.setIsrep(attrRepating);
				info.appendAttrInfo(attrInfo);
			}
			final AttrInfo attrInfo = new AttrInfo();
			attrInfo.setAttrName("r_object_id");
			attrInfo.setDatatype(DfType.DF_STRING);
			attrInfo.setIsrep(false);
			info.appendAttrInfo(attrInfo);
		} catch (final DfException ex) {
			DfLogger.error(this, "Failure during query for typeinfo. Fatal, will exit.", null, ex);
			System.exit(1);

		} finally {
			if (col != null) {
				try {
					col.close();
				} catch (final DfException ex) {
					Logger.getLogger(DocumentExporter.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
		TypeInfoHolder.addTypeInfo(info);
	}

	private void readReplacePaths(final String configfile) {

		try {
			final FileInputStream fstream = new FileInputStream(configfile);
			final DataInputStream in = new DataInputStream(fstream);
			final BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				System.out.println("read line: " + strLine);
				if (strLine.startsWith("#")) {
					System.out.println("skipped comment.");
				} else {
					if (strLine.length() > 5) {
						PathReplacer.addPaths(strLine);
					}
				}
			}
			in.close();
		} catch (final Exception e) {
			System.out.println("could not read paths, exiting.");
			DfLogger.fatal(this, "could not read paths, exiting.", null, e);
			System.exit(1);
		}

	}

	public void setTargetFSFolder(final String absolutePath) {
		this.exportFSPath = absolutePath;
	}
}
