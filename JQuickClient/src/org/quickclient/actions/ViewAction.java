package org.quickclient.actions;

import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.quickclient.classes.ConfigService;
import org.quickclient.classes.CustomQueryHelper;
import org.quickclient.classes.DefaultTableModelCreator;
import org.quickclient.classes.DocuSessionManager;
import org.quickclient.classes.FileUtils;
import org.quickclient.classes.QueryUtils;
import org.quickclient.classes.SwingHelper;
import org.quickclient.classes.Utils;
import org.quickclient.gui.JobEditor;
import org.quickclient.gui.LDAPFrame;
import org.quickclient.gui.MethodFrame;
import org.quickclient.gui.SearchFrame;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.search.IDfQueryDefinition;
import com.documentum.fc.client.search.IDfQueryProcessor;
import com.documentum.fc.client.search.IDfQueryStatus;
import com.documentum.fc.client.search.IDfResultsSet;
import com.documentum.fc.client.search.IDfSearchService;
import com.documentum.fc.client.search.IDfSmartList;
import com.documentum.fc.client.search.impl.DfSearchService;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;

public class ViewAction implements IQuickAction {

	private List<String> idlist;
	private JTable table;

	@Override
	public void execute() throws QCActionException {
		IDfSession session = null;
		final DocuSessionManager smanager = DocuSessionManager.getInstance();
		session = smanager.getSession();
		try {
			final int size = idlist.size();
			for (int i = 0; i < size; i++) {
				final String objid = idlist.get(i);
				final IDfSysObject obj = (IDfSysObject) session.getObject(new DfId(objid));
				final String type = obj.getTypeName();
				if (type.equals("dm_smart_list")) {
					final IDfSmartList sl = (IDfSmartList) obj;
					final String objname = obj.getObjectName();
					final String desc = obj.getTitle();
					if (sl.getQueryDefinitionType().equals(IDfSmartList.TYPE_PASSTHROUGH_QUERY)) {
						final String dql = QueryUtils.loadDQLFromSmartList(new DfId(objid), smanager.getSMgr(), smanager.getDocbasename());
						new CustomQueryHelper().executeCustomQuery((DefaultTableModel) table.getModel(), ConfigService.getInstance().getDesktop(), null, dql, objname + " " + desc, false);
					}
					if (sl.getQueryDefinitionType().equals(IDfSmartList.TYPE_QUERY_BUILDER)) {
						final IDfSearchService ss = new DfSearchService(smanager.getSMgr(), smanager.getDocbasename());
						final IDfQueryDefinition query = QueryUtils.loadQueryDefinitionFromSmartList(new DfId(objid), smanager.getSMgr(), smanager.getDocbasename());
						IDfResultsSet results = null;
						final IDfQueryProcessor process = ss.newQueryProcessor(query, false);
						process.blockingSearch(10000);
						results = process.getResults();
						final IDfQueryStatus status = process.getQueryStatus();
						final Utils u = new Utils();
						DefaultTableModel model = new DefaultTableModelCreator().createModel();
						model = u.getModelFromResultSet(session, results, ConfigService.getInstance().isShowThumbnails(), model, null);
						if (model.getRowCount() > 0) {
							final SearchFrame sf = new SearchFrame(model, ConfigService.getInstance().isShowThumbnails());
							ConfigService.getInstance().getDesktop().add(sf);
							sf.setSize(900, 400);
							sf.setTitle(objname + " " + desc);
							sf.setVisible(true);
						}
					}
				} else if (type.equals("dm_method")) {
					final MethodFrame frame = new MethodFrame();
					frame.setId(objid);
					frame.update();
					frame.setVisible(true);
				} else if (type.equals("dm_job")) {
					final JobEditor editor = new JobEditor(new DfId(objid));
					editor.setVisible(true);
				} else if (type.equals("dm_ldap_config")) {
					final LDAPFrame frame = new LDAPFrame();
					frame.setId(objid);
					frame.initView();
					frame.setVisible(true);
				} else {
					FileUtils.viewFile(objid);
				}
			}
		} catch (final DfException e) {
			SwingHelper.showErrorMessage("Error..", e.getMessage());
		} catch (final InterruptedException e) {
			SwingHelper.showErrorMessage("Your query timed out", e.getMessage());
			Thread.currentThread().interrupt();
		}
	}

	@Override
	public void setIdList(final List<String> idlist) {
		this.idlist = idlist;

	}

	@Override
	public void setTable(final JTable t) {
		this.table = t;

	}

}
