package org.quickclient.classes;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.documentum.com.DfClientX;
import com.documentum.com.IDfClientX;
import com.documentum.fc.client.IDfClient;
import com.documentum.fc.client.IDfDocbaseMap;
import com.documentum.fc.client.IDfDocbrokerClient;
import com.documentum.fc.client.IDfEnumeration;
import com.documentum.fc.client.IDfFolder;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfTypedObject;
import com.documentum.fc.client.IDfUser;
import com.documentum.fc.client.search.IDfPassThroughQuery;
import com.documentum.fc.client.search.IDfQueryBuilder;
import com.documentum.fc.client.search.IDfQueryDefinition;
import com.documentum.fc.client.search.IDfQueryManager;
import com.documentum.fc.client.search.IDfQueryProcessor;
import com.documentum.fc.client.search.IDfResultEntry;
import com.documentum.fc.client.search.IDfResultsSet;
import com.documentum.fc.client.search.IDfSearchService;
import com.documentum.fc.client.search.IDfSmartList;
import com.documentum.fc.client.search.IDfSmartListDefinition;
import com.documentum.fc.client.search.impl.DfSearchService;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLoginInfo;
import com.documentum.fc.common.IDfAttr;
import com.documentum.fc.common.IDfId;
import com.documentum.fc.common.IDfLoginInfo;

public class QueryUtils {
	private static final String SAVED_SEARCHES_FOLDER_NAME = "Saved Searches";

	public static void main(String args[]) throws Exception {
		QueryUtils qu = new QueryUtils();
		qu.runit();

	}

	private void runit() throws Exception {
		IDfSession session = getSession();
		IDfSessionManager smgr = createSessionManager("SANDBOX01", "dmsandbox", "", null, null);
		// IDfSearchSource source = new DfSearchSo
		// IDfQueryManager qm = new DfQueryManager(smgr, searchSourceMap,
		// "SANDBOX01")
		// runQuery("08fffff88014d711", smgr, "SANDBOX01");

		// IDfId newqueryId = createDmQuery("select * from dm_cabinet",
		// "dmquery", "dmquery", "SANDBOX01");
		// IDfResultsSet results = runDmQuery(newqueryId.getId(), smgr,
		// "SANDBOX01");
		// printResults(results);
		//UUIDfId smid = createSmartList("select * from dm_dbo.dm_dual", "smlist", "smlist", "SANDBOX01", true);
		// IDfResultsSet results2 = runSmartList(smid.getId(), smgr,
		// "SANDBOX01");
		// printResults(results2);

		//loadDQLFromSmartList(smid, smgr, "SANDBOX01");

	}

	public static IDfQueryDefinition loadQueryDefinitionFromSmartList(IDfId smid, IDfSessionManager smgr, String repo)  {

		IDfQueryDefinition querydef = null;
		try {
			IDfSearchService ss = new DfSearchService(smgr, repo);
			IDfQueryManager qm = ss.newQueryMgr();
			IDfSession sess = smgr.newSession(repo);
			IDfSysObject sobj = (IDfSysObject) sess.getObject(smid);
			IDfSmartListDefinition def = qm.loadSmartListDefinition(sobj.getContent());
			querydef = def.getQueryDefinition();
		} catch (DfException e) {
			SwingHelper.showErrorMessage("", e.getMessage());
		} catch (IOException e) {
			SwingHelper.showErrorMessage("", e.getMessage());
		}
		return querydef;
	}
	public static String loadDQLFromSmartList(IDfId smid, IDfSessionManager smgr, String repo)  {
		String rval = null;
		try {
			IDfSearchService ss = new DfSearchService(smgr, repo);
			IDfQueryManager qm = ss.newQueryMgr();
			IDfSession sess = smgr.newSession(repo);
			IDfSysObject sobj = (IDfSysObject) sess.getObject(smid);
			IDfSmartListDefinition def = qm.loadSmartListDefinition(sobj.getContent());
			IDfQueryDefinition querydef = def.getQueryDefinition();
			if (querydef.getQueryDefinitionType() == IDfQueryDefinition.QUERY_DEF_TYPE_PASSTHROUGH) {
				IDfPassThroughQuery pass = (IDfPassThroughQuery) querydef;
				rval = pass.getQueryString();
			}
		} catch (DfException e) {

		} catch (IOException e) {

		}
		return rval;
	}

	private void printResults(IDfResultsSet results) {

		ArrayList<String> attrlist = null;
		System.out.println("size: " + results.size());
		while (results.next()) {
			IDfResultEntry entry = results.getResult();
			if (attrlist == null) {
				attrlist = new ArrayList<String>();
				IDfEnumeration attrenum = entry.enumAttrs();
				while (attrenum.hasMoreElements()) {
					IDfAttr attr = (IDfAttr) attrenum.nextElement();
					attrlist.add(attr.getName());
				}
			}

			for (String attrname : attrlist) {
				System.out.print(" '" + attrname + "', value: '" + entry.getString(attrname) + "'");
			}
			System.out.print("\n");

		}

	}

	public IDfResultsSet runDmQuery(String objId, IDfSessionManager smgr, String repo) throws Exception {
		IDfSearchService ss = new DfSearchService(smgr, repo);
		IDfQueryManager qm = ss.newQueryMgr();
		IDfSession sess = smgr.newSession(repo);
		IDfSysObject sobj = (IDfSysObject) sess.getObject(new DfId(objId));
		ByteArrayInputStream bais = sobj.getContent();
		byte[] arr = new byte[bais.available()];
		bais.read(arr);
		IDfPassThroughQuery query = qm.newPassThroughQuery(new String(arr));
		query.addSelectedSource(repo);
		IDfQueryDefinition queryDef = (IDfQueryDefinition) query;
		IDfQueryProcessor queryProcess = ss.newQueryProcessor(queryDef, true);
		IDfResultsSet res = queryProcess.blockingSearch(0);
		System.out.println("runDmQuery, result size: " + res.size());
		return res;
	}

	public IDfId createDmQuery(String dql, String objectName, String desc, String repositoryName) throws Exception {
		IDfId result = null;
		try {
			IDfSession session = getSession();// getSession(repositoryName);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] arr = dql.getBytes("utf-8");
			out.write(arr);
			System.out.println(new String(arr));
			String strSavedSearchFolderId = locateSavedSearchFolder(session);
			IDfSysObject queryobj = (IDfSysObject) session.newObject("dm_query");
			queryobj.setContentType("dm_internal");
			queryobj.setObjectName(objectName);
			queryobj.setContent(out);
			queryobj.link(strSavedSearchFolderId);
			queryobj.save();
			result = queryobj.getObjectId();
		}

		catch (DfException dfe) {
			// log.error(DFSXConstants.smartlist_header);
			// log.error(dfe.getMessage(), dfe);
			throw dfe;
		} finally {
		}

		return result;

	}

	private static String locateSavedSearchFolder(IDfSession session) throws DfException {
		String strSavedSearchFolderId = null;

		IDfUser user = session.getUser(session.getLoginUserName());
		String strDefaultFolder = user.getDefaultFolder();

		IDfFolder defaultFolder = (IDfFolder) session.getObjectByPath(strDefaultFolder);
		if (defaultFolder == null) {
			throw new IllegalStateException();
		}

		String strSavedSearchesFolderPath = strDefaultFolder + "/" + SAVED_SEARCHES_FOLDER_NAME;
		IDfFolder savedSearchesFolder = (IDfFolder) session.getObjectByPath(strSavedSearchesFolderPath);

		if (savedSearchesFolder == null) {
			// create the "Saved Searches" folder
			savedSearchesFolder = (IDfFolder) session.newObject("dm_folder");
			savedSearchesFolder.setObjectName(SAVED_SEARCHES_FOLDER_NAME);
			savedSearchesFolder.link(strDefaultFolder);
			savedSearchesFolder.save();
			strSavedSearchFolderId = savedSearchesFolder.getObjectId().getId();
		} else {
			strSavedSearchFolderId = savedSearchesFolder.getObjectId().getId();
		}

		return strSavedSearchFolderId;
	}

	public IDfId createSmartList(IDfSession session, String dql, String objectName, String desc, String repositoryName, boolean ispublic) throws Exception {
		IDfId result = null;
		try {
			
			DfClientX cx = new DfClientX();
			IDfClient client = cx.getLocalClient();
			IDfSearchService searchsvc = client.newSearchService(session.getSessionManager(), session.getDocbaseName());
			IDfQueryManager queryMgr = searchsvc.newQueryMgr();

			IDfPassThroughQuery query = queryMgr.newPassThroughQuery(dql);
			IDfSmartListDefinition sm = queryMgr.newSmartListDefinition();
			sm.setQueryDefinition(query);
			query.addSelectedSource(repositoryName);

			// find the "Saved Searches" folder
			String strSavedSearchFolderId = locateSavedSearchFolder(session);
			IDfSmartList smartList = (IDfSmartList) session.newObject("dm_smart_list");
			smartList.setContentType("dm_internal");
			smartList.setSmartListDefinition(sm);
			smartList.setObjectName(objectName);
			smartList.setTitle(desc);
			// smartList.setBoolean("r_is_public", ispublic);
			smartList.setString(IDfSmartList.ATTRIBUTE_QUERY_TYPE, IDfSmartList.TYPE_PASSTHROUGH_QUERY);
			smartList.link(strSavedSearchFolderId);
			smartList.save();
			result = smartList.getObjectId();
		}

		catch (DfException dfe) {
			// log.error(DFSXConstants.smartlist_header);
			// log.error(dfe.getMessage(), dfe);
			throw dfe;
		} finally {
			
		}

		return result;
	}

	public IDfResultsSet runSmartList(String objId, IDfSessionManager smgr, String repo) throws DfException, IOException, InterruptedException {
		IDfSearchService ss = new DfSearchService(smgr, repo);
		IDfQueryManager qm = ss.newQueryMgr();
		IDfSession sess = smgr.newSession(repo);
		IDfSysObject sobj = (IDfSysObject) sess.getObject(new DfId(objId));
		IDfSmartListDefinition def = qm.loadSmartListDefinition(sobj.getContent());
		IDfQueryProcessor process = ss.newQueryProcessor(def.getQueryDefinition(), true);
		process.blockingSearch(10000);
		IDfResultsSet set = process.getResults();
		return set;
	}

	public IDfSessionManager createSessionManager(String docbase, String user, String pass, String h, String p) throws DfException {
		IDfClientX clientx = new DfClientX();
		IDfClient client = clientx.getLocalClient();
		if (h != null) {
			IDfTypedObject config = client.getClientConfig();
			config.setString("primary_host", h);
			if (p != null) {
				config.setString("primary_port", p);
			}
		}
		IDfSessionManager sMgr = client.newSessionManager();
		IDfLoginInfo loginInfoObj = clientx.getLoginInfo();
		loginInfoObj.setUser(user);
		loginInfoObj.setPassword(pass);
		loginInfoObj.setDomain(null);
		sMgr.setIdentity(docbase, loginInfoObj);
		return sMgr;
	}

	public IDfSession getSession() {
		IDfClientX l = new DfClientX();
		IDfClient client;
		IDfSession session = null;
		try {
			client = l.getLocalClient();
			IDfDocbrokerClient docbrokerclient = l.getDocbrokerClient();
			IDfDocbaseMap docbasemap = docbrokerclient.getDocbaseMap();
			int valcount = docbasemap.getDocbaseCount();
			for (int i = 0; i < valcount; i++) {
				System.out.println(docbasemap.getDocbaseName(i));
			}
			IDfLoginInfo info = new DfLoginInfo();
			info.setUser("x");
			info.setPassword("x");
			session = client.newSession("x", info);

		} catch (DfException e) {
			e.printStackTrace();
		}
		return session;
	}

	public IDfId createSmartList(IDfSession session, IDfQueryBuilder qb, String objectName, String desc, String docbasename, boolean ispublic) throws DfException {
		
		IDfId result = null;
		try {
			
			DfClientX cx = new DfClientX();
			IDfClient client = cx.getLocalClient();
			IDfSearchService searchsvc = client.newSearchService(session.getSessionManager(), session.getDocbaseName());
			IDfQueryManager queryMgr = searchsvc.newQueryMgr();

			IDfSmartListDefinition sm = queryMgr.newSmartListDefinition();
			sm.setQueryDefinition(qb);

			// find the "Saved Searches" folder
			String strSavedSearchFolderId = locateSavedSearchFolder(session);
			IDfSmartList smartList = (IDfSmartList) session.newObject("dm_smart_list");
			smartList.setContentType("dm_internal");
			smartList.setSmartListDefinition(sm);
			smartList.setObjectName(objectName);
			smartList.setTitle(desc);
			// smartList.setBoolean("r_is_public", ispublic);
			smartList.setString(IDfSmartList.ATTRIBUTE_QUERY_TYPE, IDfSmartList.TYPE_QUERY_BUILDER);
			smartList.link(strSavedSearchFolderId);
			smartList.save();
			result = smartList.getObjectId();
		}

		catch (DfException dfe) {
			throw dfe;
		} finally {
			
		}

		return result;
		
	}

}
