/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.fortica.dctm;

import java.util.Vector;

import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLogger;

/**
 * 
 * @author miksuoma
 */
public class IdHolder {

	static Vector<String> idlist = new Vector();
	private IdHolder() {
	}

	private static class SingletonHolder {
		public static final IdHolder INSTANCE = new IdHolder();
	}

	public static IdHolder getInstance() {
		return SingletonHolder.INSTANCE;
	}

	public static void addId(String id) {
		if (!idlist.contains(id))
			idlist.add(id);
		else
			System.out.println("Skipped duplicate: " + id);
	}

	public synchronized static String getId() {
		String id = "";
		try {
			id = idlist.remove(0);
			System.out.println("idlist size is:" + idlist.size());
			DfLogger.debug(IdHolder.class, "give id: " + id, null, null);
		} catch (ArrayIndexOutOfBoundsException aiooe) {
			return DfId.DF_NULLID.getId();
		}
		return id;
	}

	public static String getSize() {
		String size = String.valueOf(idlist.size());
		return size;
	}
}
