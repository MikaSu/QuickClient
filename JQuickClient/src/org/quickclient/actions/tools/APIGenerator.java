package org.quickclient.actions.tools;

import java.util.List;

import javax.swing.JTable;

import org.quickclient.actions.IQuickAction;
import org.quickclient.actions.QCActionException;
import org.quickclient.classes.DocuSessionManager;
import org.quickclient.gui.TextViewer;

import com.documentum.fc.client.IDfFolder;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLogger;
import com.documentum.fc.common.IDfAttr;
import com.documentum.fc.common.IDfId;


public class APIGenerator implements IQuickAction {
	private List<String> idlist;

	@Override
	public void execute() throws QCActionException {

		DocuSessionManager smanager = DocuSessionManager.getInstance();
		IDfSession session = null;
		StringBuilder sb = new StringBuilder();

		try {
			session = smanager.getSession();
			for (int i = 0; i < this.idlist.size(); i++) {
				String objid = (String) this.idlist.get(i);
				IDfId id = new DfId(objid);
				IDfSysObject obj = (IDfSysObject) session.getObject(id);
				String typename = obj.getTypeName();
				sb.append("create,c," + typename + "\n");
				int acount = obj.getAttrCount();

				for (int j = 0; j < acount; j++) {

					IDfAttr a = obj.getAttr(j);
					String attrname = a.getName();
					boolean addattr = true;

					if (attrname.startsWith("i_") || attrname.startsWith("r_") || attrname.startsWith("a_"))
						addattr = false;
					if (attrname.equals("a_content_type"))
						addattr = true;
					if (addattr) {
						boolean isrepeating = a.isRepeating();
						if (!isrepeating) {
							String value = getSingleSet(a, obj);
							if (value != null)
								sb.append(value);
						} else {
							sb.append(getRepeatingSet(a, obj));
						}
					}

				}
				// he
				int linkcount = obj.getFolderIdCount();
				for (int x = 0; x < linkcount; x++) {
					IDfFolder f = (IDfFolder) session.getObject(obj.getFolderId(x));
					String path = f.getRepeatingString("r_folder_path", 0);
					sb.append("link,c,l," + path + "\n");
					sb.append("save,c,l\n");
					System.out.println(sb.toString());
				}
				sb.append("##### ##### ##### #####\n");

			}
			TextViewer tv = new TextViewer(false);
			tv.setText(sb.toString());
			tv.setVisible(true);
		} catch (DfException e) {
			e.printStackTrace();
		} finally {
			if (session != null)
				smanager.releaseSession(session);
		}

	}

	private String getRepeatingSet(IDfAttr a, IDfSysObject obj) throws DfException {
		StringBuilder sb = new StringBuilder();
		int datatype = a.getDataType();
		String attrname = a.getName();
		if (datatype == IDfAttr.DM_STRING) {
			int vc = obj.getValueCount(attrname);
			for (int i = 0; i < vc; i++) {
				String value = obj.getRepeatingString(attrname, i);
				System.out.println("value: '" + value + "'");
				if (value != null && !value.equals("null") && value.length() > 0)
					sb.append("set,c,l," + attrname + "[" + i + "]\n" + value + "\n");
			}
		} else if (datatype == IDfAttr.DM_ID) {
			IDfId value = obj.getId(attrname);
			if (value.isNull() || value.equals(DfId.DF_NULLID))
				DfLogger.debug(this, "ounou", null, null);
			else
				sb.append("set,c,l," + attrname + "\n" + value.getId() + "\n");
		}

		return sb.toString();

	}

	private String getSingleSet(IDfAttr a, IDfSysObject obj) throws DfException {
		String rval = "";
		int datatype = a.getDataType();
		String attrname = a.getName();
		if (datatype == IDfAttr.DM_STRING) {
			String value = obj.getString(attrname);
			System.out.println("value: '" + value + "'");
			if (value != null && !value.equals("null") && value.length() > 0)
				rval = "set,c,l," + attrname + "\n" + value + "\n";
			else
				rval = null;
		} else if (datatype == IDfAttr.DM_ID) {
			IDfId value = obj.getId(attrname);
			if (value.isNull() || value.equals(DfId.DF_NULLID))
				rval = null;
			else
				rval = "set,c,l," + attrname + "\n" + value.getId() + "\n";
		}

		return rval;
	}

	@Override
	public void setIdList(List<String> idlist) {
		this.idlist = idlist;
	}

	@Override
	public void setTable(JTable arg0) {

	}

}
