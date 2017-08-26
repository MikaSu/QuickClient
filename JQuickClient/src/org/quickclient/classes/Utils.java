/*
 * Utils.java
 *
 * Created on 1. marraskuuta 2006, 10:46
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.quickclient.classes;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;
import org.quickclient.gui.DefaultTableModelEx;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfTypedObject;
import com.documentum.fc.client.search.IDfResultsSet;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLogger;
import com.documentum.fc.common.IDfAttr;
import com.documentum.fc.common.IDfId;


/**
 * 
 * @author Administrator
 */
public class Utils {

	private int counter = 0;
	Logger log = Logger.getLogger(Utils.class);
	private ArrayList<String> foldernames = new ArrayList<String>();

	public ArrayList<String> getFoldernames() {
		return foldernames;
	}

	/** Creates a new instance of Utils */
	public Utils() {
	}

	public static boolean isWindowsServer() {
		DocuSessionManager smanager = DocuSessionManager.getInstance();
		IDfSession sessino = smanager.getSession();
		String serverVersion = "";
		try {
			IDfTypedObject serverConfigObject = sessino.getServerConfig();
			serverVersion = serverConfigObject.getString("r_server_version");
		} catch (DfException e) {
			DfLogger.error(Utils.class, "errored...", null, e);
		} finally {
			if (sessino != null)
				smanager.releaseSession(sessino);
		}
		if (serverVersion.indexOf("Win") != -1)
			return true;
		else
			return false;

	}

	public DefaultTableModel getModelFromCollection(IDfSession session, String configname, IDfCollection col, boolean showThumbnails, DefaultTableModel model, ArrayList<QueryFilter> filterset, ArrayList<String> nonsysobjattrs) throws DfException {

		configname = configname == null ? "default" : configname;

		Vector<ListAttribute> listattributes = ConfigService.getInstance().getAttributes(configname).get();
		String tempvalue = "";
		String repvalue = "";
		boolean isvirtualdoc = false;
		counter = 0;

		if (!showThumbnails) {
			int rowcount = 0;
			while (col.next()) {
				String objId = col.getString("r_object_id");
				Vector<Object> vector = new Vector<Object>();
				if (col.hasAttr("r_is_virtual_doc"))
					isvirtualdoc = col.getBoolean("r_is_virtual_doc");
				else
					isvirtualdoc = false;
				if (col.hasAttr("r_lock_owner"))
					vector.add(col.getString("r_lock_owner"));
				else
					vector.add("");
				if (isvirtualdoc) {
					vector.add("virtual,virtual");
				} else {
					vector.add(col.getString("a_content_type") + "," + col.getString("r_object_type"));
				}
				vector.add(col.getString("object_name"));
				for (int i = 0; i < listattributes.size(); i++) {
					String attributeName = "";
					if (listattributes.get(i).type.equals("dm_sysobject")) {
						attributeName = listattributes.get(i).attribute;
						int values = 1;
						values = col.getValueCount(attributeName);
						if (values == 1) {
							vector.add(col.getString(attributeName));
						} else {
							for (int y = 0; y < values; y++) {
								tempvalue = col.getRepeatingString(attributeName, y);
								if (y == 0) {
									repvalue = tempvalue;
								} else {
									repvalue = repvalue + ", " + tempvalue;
								}
							}
							vector.add(repvalue);
							repvalue = "";
						}
					} else {
						// vector.add("humppa\npumppa");
						String result = getResult(session, objId, listattributes.get(i));
						System.out.println("add result: '" + result + "'");
						vector.add(result);
					}
				}
				String iidee = col.getString("r_object_id");
				if (iidee.startsWith("0b"))
					foldernames.add(col.getString("object_name"));
				DokuData data = new DokuData(iidee);
				if (nonsysobjattrs != null) {
					if (nonsysobjattrs.size() > 0) {
						IDfSysObject obj = (IDfSysObject) session.getObject(new DfId(data.getObjID()));
						for (String nonsysobjattr : nonsysobjattrs) {
							if (obj != null) {
								if (obj.hasAttr(nonsysobjattr))
									vector.add(obj.getString(nonsysobjattr));
								else
									vector.add("");
							}
						}
					}
				}
				/*
				 * vector.add(col.getString("a_content_type"));
				 * vector.add(col.getString("r_object_type"));
				 */

				if (filterset == null) {
					vector.add(data);
					model.addRow(vector);
					counter++;
				} else {
					if (filterset.size() == 0) {
						vector.add(data);
						model.addRow(vector);
						counter++;
					} else {
						IDfId dfid = null;
						IDfSysObject obj = null;
						boolean valuefound = false;
						boolean hasallvalues = true;
						dfid = new DfId(col.getString("r_object_id"));
						obj = (IDfSysObject) session.getObject(dfid);
						if (obj != null) {
							for (QueryFilter filt : filterset) {
								valuefound = false;
								valuefound = applyFilter2(obj, filt);
								if (filt.getFiltertype() == QueryFilter.FILTER_TYPE_MAX_ROWS) {
									if (counter > filt.getMaxcount()) {
										return model;
									}
								}
							}
							if (!valuefound) {
								hasallvalues = false;
							}
						}
						if (hasallvalues) {
							vector.add(data);
							model.addRow(vector);
							counter++;
						}
					}
				}
			}
		} else {
			int rowcount = 0;
			while (col.next()) {
				String objId = col.getString("r_object_id");
				Vector<Object> vector = new Vector<Object>();
				vector.add(col.getString("r_lock_owner"));
				vector.add(col.getString("thumbnail_url"));
				vector.add(col.getString("object_name"));
				// for (int i = 0; i < listattributes.size(); i++) {
				for (int i = 0; i < listattributes.size(); i++) {
					String attributeName = "";
					if (listattributes.get(i).type.equals("dm_sysobject")) {
						attributeName = listattributes.get(i).attribute;
						int values = 1;

						values = col.getValueCount(attributeName);

						if (values == 1) {
							vector.add(col.getString(attributeName));
						} else {
							for (int y = 0; y < values; y++) {
								tempvalue = col.getRepeatingString(attributeName, y);
								if (y == 0) {
									repvalue = tempvalue;
								} else {
									repvalue = repvalue + ", " + tempvalue;
								}
							}
							vector.add(repvalue);
							repvalue = "";
						}
					} else {
						String result = getResult(session, objId, listattributes.get(i));
						System.out.println("add result: '" + result + "'");
						vector.add(result);
					}
				}

				/*
				 * vector.add(col.getString("a_content_type"));
				 * vector.add(col.getString("r_object_type"));
				 */
				String iidee = col.getString("r_object_id");
				if (iidee.startsWith("0b"))
					foldernames.add(col.getString("object_name"));
				DokuData data = new DokuData(iidee);
				if (nonsysobjattrs != null) {
					if (nonsysobjattrs.size() > 0) {
						IDfSysObject obj = (IDfSysObject) session.getObject(new DfId(data.getObjID()));
						for (String nonsysobjattr : nonsysobjattrs) {
							if (obj != null) {
								if (obj.hasAttr(nonsysobjattr))
									vector.add(obj.getString(nonsysobjattr));
								else
									vector.add("");
							}
						}
					}
				}
				if (filterset == null) {
					vector.add(data);
					model.addRow(vector);
					counter++;
				} else {
					if (filterset.size() == 0) {
						vector.add(data);
						model.addRow(vector);
						counter++;
					}
					IDfId dfid = null;
					IDfSysObject obj = null;
					boolean valuefound = false;
					boolean hasallvalues = true;
					dfid = new DfId(col.getString("r_object_id"));
					obj = (IDfSysObject) session.getObject(dfid);
					if (obj != null) {
						for (QueryFilter filt : filterset) {
							valuefound = false;
							valuefound = applyFilter2(obj, filt);
							if (filt.getFiltertype() == QueryFilter.FILTER_TYPE_MAX_ROWS) {
								if (counter > filt.getMaxcount()) {
									return model;
								}
							}

						}
						if (!valuefound) {
							hasallvalues = false;
						}
					}
					if (hasallvalues) {
						vector.add(data);
						model.addRow(vector);
						counter++;
					}
				}

			}
		}
		return model;
	}

	public DefaultTableModelEx getModelFromCollection(IDfSession session, IDfCollection col) throws DfException {
		String repValue = "";
		String tempValue = "";

		DefaultTableModelEx tablemodel = new DefaultTableModelEx();
		Vector<Object> colVector = new Vector<Object>();
		Enumeration colenum = col.enumAttrs();
		int counter1 = 0;
		String header = "";
		while (colenum.hasMoreElements()) {
			IDfAttr attr = (IDfAttr) colenum.nextElement();
			colVector.add(attr.getName());
			counter1++;
			if (counter1 == 1) {
				header = attr.getName();
			}
		}
		tablemodel.setColumnIdentifiers(colVector);
		String singlerow = "";
		int counter = 0;
		while (col.next()) {
			counter++;
			Vector<Object> rowVector = new Vector<Object>();
			for (int x = 0; x < colVector.size(); x++) {
				int values = 1;
				values = col.getValueCount((String) colVector.get(x));
				// //System.out.println(values);
				if (values == 1) {
					rowVector.add(col.getRepeatingString((String) colVector.get(x), 0));
				} else {
					for (int y = 0; y < values; y++) {
						tempValue = col.getRepeatingString((String) colVector.get(x), y);
						if (y == 0) {
							repValue = tempValue;
						} else {
							repValue = repValue + ", " + tempValue;
						}
					}
					rowVector.add(repValue);
					repValue = "";
				}
			}
			tablemodel.addRow(rowVector);
			if (counter == 1) {
				if (counter1 == 1)
					singlerow = header + ":" + rowVector.toString();
				else
					singlerow = rowVector.toString();
			}
		}
		if (counter == 0) {
			tablemodel.setSinglelineresult("no rows returned");
		}
		if (counter == 1) {
			tablemodel.setSinglelineresult(singlerow);
		}
		return tablemodel;
	}

	private String getResult(IDfSession session, String objId, ListAttribute listAttribute) {

		IDfQuery query = new DfQuery();
		IDfCollection col = null;
		StringBuffer sb = new StringBuffer();
		try {
			String a = listAttribute.attribute;
			String b = listAttribute.type;
			String qry = "select " + a + " from " + b + " where r_object_id = '" + objId + "'";
			System.out.println(qry);
			query.setDQL(qry);
			col = query.execute(session, DfQuery.DF_READ_QUERY);
			while (col.next()) {
				String value = col.getString(a);
				if (value != null)
					sb.append(col.getString(a));
				else
					sb.append("");
			}
		} catch (DfException ex) {
			log.error(ex);
			SwingHelper.showErrorMessage("Error occurred!", ex.getMessage());
		} finally {
			if (col != null) {
				try {
					col.close();
				} catch (DfException ex) {
					log.error(ex);
				}
			}
		}
		return sb.toString();
	}

	private boolean applyFilter2(IDfSysObject obj, QueryFilter filt) throws DfException {
		boolean valuefound = false;
		if (filt.getFiltertype() == QueryFilter.FILTER_TYPE_MAX_ROWS)
			return true;

		if (obj.hasAttr(filt.getAttributename())) {
			if (obj.isAttrRepeating(filt.getAttributename())) {
				if (filt.getFiltertype() == QueryFilter.FILTER_TYPE_EXACT_MATCH) {
					if (filt.isMatchcase()) {
						int jeps = obj.findString(filt.getAttributename(), filt.getRequiredvalue());
						if (jeps != -1) {
							valuefound = true;
						}
					} else {
						int valuecount = obj.getValueCount(filt.getAttributename());
						for (int i = 0; i < valuecount; i++) {
							String testvalue = obj.getRepeatingString(filt.getAttributename(), i);
							if (testvalue.equalsIgnoreCase(filt.getRequiredvalue())) {
								valuefound = true;
							}
						}
					}
				} else if (filt.getFiltertype() == QueryFilter.FILTER_TYPE_BEGINS_WITH) {
					if (filt.isMatchcase()) {
						int valuecount = obj.getValueCount(filt.getAttributename());
						for (int i = 0; i < valuecount; i++) {
							String testvalue = obj.getRepeatingString(filt.getAttributename(), i);
							if (testvalue.startsWith(filt.getRequiredvalue())) {
								valuefound = true;
							}
						}
					} else {
						int valuecount = obj.getValueCount(filt.getAttributename());
						for (int i = 0; i < valuecount; i++) {
							String testvalue = obj.getRepeatingString(filt.getAttributename(), i);
							testvalue = testvalue.toLowerCase();
							if (testvalue.startsWith(filt.getRequiredvalue().toLowerCase())) {
								valuefound = true;
							}
						}
					}
				} else if (filt.getFiltertype() == QueryFilter.FILTER_TYPE_CONTAINS) {
					if (filt.isMatchcase()) {
						int valuecount = obj.getValueCount(filt.getAttributename());
						for (int i = 0; i < valuecount; i++) {
							String testvalue = obj.getRepeatingString(filt.getAttributename(), i);
							if (testvalue.contains(filt.getRequiredvalue())) {
								valuefound = true;
							}
						}
					} else {
						int valuecount = obj.getValueCount(filt.getAttributename());
						for (int i = 0; i < valuecount; i++) {
							String testvalue = obj.getRepeatingString(filt.getAttributename(), i);
							testvalue = testvalue.toLowerCase();
							if (testvalue.contains(filt.getRequiredvalue().toLowerCase())) {
								valuefound = true;
							}
						}
					}
				} else if (filt.getFiltertype() == QueryFilter.FILTER_TYPE_ENDS_WITH) {
					if (filt.isMatchcase()) {
						int valuecount = obj.getValueCount(filt.getAttributename());
						for (int i = 0; i < valuecount; i++) {
							String testvalue = obj.getRepeatingString(filt.getAttributename(), i);
							if (testvalue.endsWith(filt.getRequiredvalue())) {
								valuefound = true;
							}
						}
					} else {
						int valuecount = obj.getValueCount(filt.getAttributename());
						for (int i = 0; i < valuecount; i++) {
							String testvalue = obj.getRepeatingString(filt.getAttributename(), i);
							testvalue = testvalue.toLowerCase();
							if (testvalue.endsWith(filt.getRequiredvalue().toLowerCase())) {
								valuefound = true;
							}
						}
					}
				} else if (filt.getFiltertype() == QueryFilter.FILTER_TYPE_REGEX) {
					int valuecount = obj.getValueCount(filt.getAttributename());
					for (int i = 0; i < valuecount; i++) {
						String testvalue = obj.getRepeatingString(filt.getAttributename(), i);
						Pattern p = Pattern.compile(filt.getRequiredvalue());
						Matcher m = p.matcher(testvalue);
						valuefound = m.find();
					}
				}
			} else {

				if (filt.getFiltertype() == QueryFilter.FILTER_TYPE_EXACT_MATCH) {
					if (filt.isMatchcase()) {
						String testString = obj.getString(filt.getAttributename());
						if (testString.equals(filt.getRequiredvalue()))
							valuefound = true;
					} else {
						String testString = obj.getString(filt.getAttributename());
						if (testString.toLowerCase().equals(filt.getRequiredvalue().toLowerCase()))
							valuefound = true;
					}
				} else if (filt.getFiltertype() == QueryFilter.FILTER_TYPE_BEGINS_WITH) {
					if (filt.isMatchcase()) {
						String testString = obj.getString(filt.getAttributename());
						if (testString.startsWith(filt.getRequiredvalue()))
							valuefound = true;
					} else {
						String testString = obj.getString(filt.getAttributename());
						if (testString.toLowerCase().startsWith(filt.getRequiredvalue().toLowerCase()))
							valuefound = true;
					}
				} else if (filt.getFiltertype() == QueryFilter.FILTER_TYPE_CONTAINS) {
					if (filt.isMatchcase()) {
						String testString = obj.getString(filt.getAttributename());
						if (testString.contains(filt.getRequiredvalue()))
							valuefound = true;
					} else {
						String testString = obj.getString(filt.getAttributename());
						if (testString.toLowerCase().contains(filt.getRequiredvalue().toLowerCase()))
							valuefound = true;
					}
				} else if (filt.getFiltertype() == QueryFilter.FILTER_TYPE_ENDS_WITH) {
					if (filt.isMatchcase()) {
						String testString = obj.getString(filt.getAttributename());
						if (testString.endsWith(filt.getRequiredvalue()))
							valuefound = true;
					} else {
						String testString = obj.getString(filt.getAttributename());
						if (testString.toLowerCase().endsWith(filt.getRequiredvalue().toLowerCase()))
							valuefound = true;
					}
				} else if (filt.getFiltertype() == QueryFilter.FILTER_TYPE_REGEX) {
					String testString = obj.getString(filt.getAttributename());
					Pattern p = Pattern.compile(filt.getRequiredvalue());
					Matcher m = p.matcher(testString);
					valuefound = m.find();
				}
			}

		}
		return valuefound;
	}

	public String intToPermit(int iPermit) {
		String strPermit = "";
		switch (iPermit) {
		case 1:
			strPermit = "NONE";
			break;
		case 2:
			strPermit = "BROWSE";
			break;
		case 3:
			strPermit = "READ";
			break;
		case 4:
			strPermit = "RELATE";
			break;
		case 5:
			strPermit = "VERSION";
			break;
		case 6:
			strPermit = "WRITE";
			break;
		case 7:
			strPermit = "DELETE";
			break;
		}
		return strPermit;
	}

	public static void setClipboard(String aString) {
		StringSelection stringSelection = new StringSelection(aString);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, null);
	}

	public static String readFileAsString(String filePath) throws java.io.IOException {
		if (filePath != null) {
			File f = new File(filePath);
			if (f.exists()) {
				StringBuffer fileData = new StringBuffer(1000);
				BufferedReader reader = new BufferedReader(new FileReader(filePath));
				char[] buf = new char[1024];
				int numRead = 0;
				while ((numRead = reader.read(buf)) != -1) {
					String readData = String.valueOf(buf, 0, numRead);
					fileData.append(readData);
					buf = new char[1024];
				}
				reader.close();
				return fileData.toString();
			} else {
				System.out.println(filePath + " does not exist.");
				DfLogger.error(Utils.class, filePath + " does not exist.", null, null);
				return null;
			}
		} else {
			return null;
		}
	}

	public static String getClipboard() {
		String result = "";
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		// odd: the Object param of getContents is not currently used
		Transferable contents = clipboard.getContents(null);
		boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
		if (hasTransferableText) {
			try {
				result = (String) contents.getTransferData(DataFlavor.stringFlavor);
			} catch (UnsupportedFlavorException ex) {
				// highly unlikely since we are using a standard DataFlavor
				// //System.out.println(ex);
				DfLogger.error(Utils.class, null, null, ex);
			} catch (IOException ex) {
				// //System.out.println(ex);
				DfLogger.error(Utils.class, null, null, ex);
			}
		}
		return result;
	}

	public int getCounter() {
		return counter;
	}

	public static String md5sum(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(fis);
		return md5;
	}

	// public DefaultTableModel getModelFromResultSet(IDfSession session,
	// IDfQueryBuilder builder, IDfResultsSet results, boolean selected,
	// DefaultTableModel tablemodel, ArrayList<String> nonsysobjattrs) throws
	// DfException {
	public DefaultTableModel getModelFromResultSet(IDfSession session, IDfResultsSet results, boolean selected, DefaultTableModel tablemodel, ArrayList<String> nonsysobjattrs) throws DfException {
		String tempvalue = "";
		String repvalue = "";
		ConfigService cs = ConfigService.getInstance();
		Vector<ListAttribute> listattributes = cs.getAttributes(cs.getCurrentListConfigName()).get();
		int counter1 = 0;
		boolean isvirtualdoc = false;

		int counter = 0;
		while (results.next()) {
			String objId = results.getResult().getString("r_object_id");
			Vector<Object> rowVector = new Vector<Object>();
			if (results.getResult().hasAttr("r_is_virtual_doc"))
				isvirtualdoc = results.getResult().getBoolean("r_is_virtual_doc");
			else
				isvirtualdoc = false;
			if (results.getResult().hasAttr("r_lock_owner"))
				rowVector.add(results.getResult().getString("r_lock_owner"));
			else
				rowVector.add("");
			if (isvirtualdoc) {
				rowVector.add("virtual,virtual");
			} else {
				rowVector.add(results.getResult().getString("a_content_type") + "," + results.getResult().getString("r_object_type"));
			}
			rowVector.add(results.getResult().getString("object_name"));

			for (int i = 0; i < listattributes.size(); i++) {
				String attributeName = "";
				if (listattributes.get(i).type.equals("dm_sysobject")) {
					attributeName = listattributes.get(i).attribute;
					int values = 1;
					values = results.getResult().getValueCount(attributeName);
					if (values == 1) {
						rowVector.add(results.getResult().getString(attributeName));
					} else {
						for (int y = 0; y < values; y++) {
							tempvalue = results.getResult().getRepeatingString(attributeName, y);
							if (y == 0) {
								repvalue = tempvalue;
							} else {
								repvalue = repvalue + ", " + tempvalue;
							}
						}
						rowVector.add(repvalue);
						repvalue = "";
					}
				} else {
					// vector.add("humppa\npumppa");
					String result = getResult(session, objId, listattributes.get(i));
					System.out.println("add result: '" + result + "'");
					rowVector.add(result);
				}
			}
			String iidee = results.getResult().getString("r_object_id");
			if (iidee.startsWith("0b"))
				foldernames.add(results.getResult().getString("object_name"));
			DokuData data = new DokuData(iidee);
			if (nonsysobjattrs != null) {
				if (nonsysobjattrs.size() > 0) {
					IDfSysObject obj = (IDfSysObject) session.getObject(new DfId(data.getObjID()));
					for (String nonsysobjattr : nonsysobjattrs) {
						if (obj != null) {
							if (obj.hasAttr(nonsysobjattr))
								rowVector.add(obj.getString(nonsysobjattr));
							else
								rowVector.add("");
						}
					}
				}
			}
			rowVector.add(data);
			tablemodel.addRow(rowVector);
		}

		return tablemodel;

	}
}
