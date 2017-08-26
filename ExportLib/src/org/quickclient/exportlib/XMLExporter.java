/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quickclient.exportlib;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.DfType;
import com.documentum.fc.client.IDfACL;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfFolder;
import com.documentum.fc.client.IDfFormat;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfVirtualDocument;
import com.documentum.fc.client.IDfVirtualDocumentNode;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLogger;
import com.documentum.fc.common.IDfId;
import com.documentum.fc.common.IDfTime;

/**
 *
 * @author miksuoma
 */
class XMLExporter {

	Logger log = Logger.getLogger(XMLExporter.class);

	private String folderRoot;

	/**
	 * 
	 * @param session
	 *            Documentum session
	 * @param chronicleId
	 *            root obj id
	 * @param targetdir
	 *            targetdir for versiontree (contains id)
	 * @param targetDir2
	 *            targetdir root
	 */
	public void exportVersionTree(final IDfSession session, final String chronicleId, final File targetdir, final String targetDir2) {
		this.folderRoot = targetDir2;

		try {
			log.info(chronicleId);
			log.info(targetdir.getAbsolutePath());

			final IDfSysObject obj = (IDfSysObject) session.getObjectByQualification("dm_document where i_chronicle_id = '" + chronicleId + "'");
			if (obj != null) {
				final String antecedentId = obj.getAntecedentId().getId();
				log.info("antecedentId: " + antecedentId);
				writeObjXML(session, obj, targetdir, false);
				Updater.updateexportedinfo(session, chronicleId, obj.getTypeName());
				log.info("antecedentId: " + antecedentId);

				if (antecedentId.length() == 16) {
					if (!antecedentId.equals("0000000000000000")) {
						writeOldObj(session, antecedentId, targetdir);
					}
				}
				boolean isvirtualdoc = false;
				if (obj.isVirtualDocument()) {
					isvirtualdoc = true;
				}
				final int rlinkcnt = obj.getLinkCount();
				if (rlinkcnt > 1) {
					isvirtualdoc = true;
				}
				if (isvirtualdoc) {
					final ArrayList<IDfSysObject> virtualchilds = getVirtualChildObjs(session, obj);
					for (final IDfSysObject childObj : virtualchilds) {
						writeObjXML(session, childObj, targetdir, true);
						Updater.updateexportedinfo(session, childObj.getObjectId().getId(), childObj.getTypeName());
					}
				}
			}

		} catch (final DfException | ParserConfigurationException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private void writeObjXML(final IDfSession session, final IDfSysObject obj, final File targetdir, final boolean isvirtualchild) throws DfException, ParserConfigurationException, FileNotFoundException, IOException {
		final String typeName = obj.getTypeName();
		if (isvirtualchild) {
			final String filepath = targetdir.getAbsolutePath();
			final File supportingdocsdir = new File(filepath + "/supporting_docs/");
			supportingdocsdir.mkdir();
			log.info("targetdir: " + supportingdocsdir.getAbsolutePath());
		}
		boolean isvirtualdoc = false;
		if (obj.isVirtualDocument()) {
			isvirtualdoc = true;
		}
		final int rlinkcnt = obj.getLinkCount();
		if (rlinkcnt > 1) {
			isvirtualdoc = true;
		}

		IDfCollection col2 = null;
		final IDfId primarycontentId = obj.getContentsId();
		col2 = obj.getRenditions("full_format, page_modifier, page, content_size,set_time, set_file, r_object_id");
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		final DocumentBuilder loader = factory.newDocumentBuilder();
		final Document document = loader.newDocument();
		final ArrayList<Element> contentElements = new ArrayList<>();
		while (col2.next()) {
			boolean isprimary = false;
			final String format = col2.getString("full_format");
			final IDfFormat idfformat = session.getFormat(format);
			final String extension = idfformat.getDOSExtension();
			final String contentid = col2.getString("r_object_id");
			if (contentid.equals(primarycontentId.getId())) {
				isprimary = true;
			}
			final String page = col2.getString("page");
			final String pageModifier = col2.getString("page_modifier");
			String fileName = targetdir.getAbsolutePath() + "/" + obj.getObjectId().getId() + "." + contentid + "." + pageModifier + "." + format + "." + extension;
			log.info(fileName);
			log.info(pageModifier);
			log.info(fileName + "+" + format + "+" + Integer.parseInt(page) + "+" + pageModifier + "+" + false);
			obj.getFileEx2(fileName, format, Integer.parseInt(page), pageModifier, false);
			final Element content = document.createElement("content");
			content.setAttribute("contentid", contentid);
			content.setAttribute("format", format);
			content.setAttribute("page", page);
			fileName = fileName.replace("\\", "/");
			content.setAttribute("filename", fileName);
			content.setAttribute("pagemodifier", pageModifier);
			content.setAttribute("isprimary", Boolean.toString(isprimary));
			contentElements.add(content);
		}
		col2.close();
		log.info("typeName is: " + typeName);
		final TypeInfo typeInfo = TypeInfoHolder.getTypeInfo(typeName);
		log.info("typeInfo is: " + typeInfo);
		final ArrayList<AttrInfo> attrInfo = typeInfo.getAttributeInfo();
		log.info("attrInfo is: " + attrInfo);
		final Element rootElement = document.createElement("document");
		final Element folderElement = document.createElement("folderpath");
		final Element permissionElement = document.createElement("permissions");
		final Element contentElement = document.createElement("contentfiles");
		for (final Element e : contentElements) {
			contentElement.appendChild(e);
		}
		rootElement.appendChild(folderElement);
		rootElement.appendChild(contentElement);
		rootElement.appendChild(permissionElement);

		if (isvirtualdoc) {
			final Element vdmElement = getVDMElement(session, obj, document);
			rootElement.appendChild(vdmElement);
		} else {
			// insert empty element
			final Element vdmElement = document.createElement("vdmchilds");
			rootElement.appendChild(vdmElement);
		}

		final int foldercount = obj.getValueCount("i_folder_id");
		for (int i = 0; i < foldercount; i++) {
			final IDfId folderid = obj.getRepeatingId("i_folder_id", i);
			final IDfFolder f = (IDfFolder) session.getObject(folderid);
			final int nn = f.getFolderPathCount();
			for (int j = 0; j < nn; j++) {
				String folderPath = f.getFolderPath(j);
				log.info("old path:" + folderPath);
				folderPath = PathReplacer.getNewPath(folderPath);
				log.info("new path:" + folderPath);

				final Element felement = document.createElement("path");
				felement.setAttribute("i_folder_id_index", String.valueOf(i));
				felement.setAttribute("r_folder_path_index", String.valueOf(j));
				felement.setAttribute("path", folderPath);
				felement.setAttribute("r_object_id", folderid.getId());
				folderElement.appendChild(felement);

				log.info("FOLDERPATH: " + folderPath);
				final StringTokenizer st = new StringTokenizer(folderPath, "/");
				String folderpart = "/";
				while (st.hasMoreTokens()) {
					final String token = st.nextToken();
					log.info(token);
					folderpart = folderpart + token;
					log.info("folder is: " + folderpart);
					final FolderExporter exporter = new FolderExporter(f);
					exporter.setTargetDir(this.folderRoot);
					exporter.exportFolder(session);
					folderpart = folderpart + "/";

				}
				final int ancestorcount = f.getAncestorIdCount();
				for (int ac = 0; ac < ancestorcount; ac++) {
					final IDfFolder af = (IDfFolder) session.getObject(new DfId(f.getAncestorId(ac)));
					final FolderExporter exporter = new FolderExporter(af);
					exporter.setTargetDir(this.folderRoot);
					exporter.exportFolder(session);
				}

			}
		}
		final IDfACL acl = obj.getACL();
		final int accessorcount = acl.getAccessorCount();
		final ArrayList<String> addedgroups = new ArrayList<>();
		for (int i = 0; i < accessorcount; i++) {

			final String accessor = acl.getAccessorName(i);
			final int accessorpermit = acl.getAccessorPermit(i);
			final boolean isgroup = acl.isGroup(i);

			final Element accessorElement = document.createElement("accessor");

			if (isgroup) {
				final Element accessorName = document.createElement("name");
				final Element permitElement = document.createElement("permission");
				accessorElement.setAttribute("accessortype", "group");
				accessorName.setTextContent(accessor);
				permitElement.setTextContent(String.valueOf(accessorpermit));
				accessorElement.appendChild(accessorName);
				accessorElement.appendChild(permitElement);

				final ArrayList<String> additionalgroups = getAdditionalGroups(session, acl, accessor);
				for (final String gname : additionalgroups) {

					final Element accessorElementg = document.createElement("accessor");
					final Element accessorNameg = document.createElement("name");
					final Element permitElementg = document.createElement("permission");
					accessorElementg.setAttribute("accessortype", "group");
					accessorNameg.setTextContent(gname);
					permitElementg.setTextContent(String.valueOf(accessorpermit));
					accessorElementg.appendChild(accessorNameg);
					accessorElementg.appendChild(permitElementg);
					if (!addedgroups.contains(gname)) {
						permissionElement.appendChild(accessorElementg);
						addedgroups.add(gname);
					}

				}
			} else {
				final Element accessorName = document.createElement("name");
				final Element permitElement = document.createElement("permission");
				accessorElement.setAttribute("accessortype", "user");
				accessorName.setTextContent(accessor);
				permitElement.setTextContent(String.valueOf(accessorpermit));
				accessorElement.appendChild(accessorName);
				accessorElement.appendChild(permitElement);
			}
			if (!addedgroups.contains(accessor)) {
				permissionElement.appendChild(accessorElement);
				addedgroups.add(accessor);
			}
		}
		document.appendChild(rootElement);
		final Element aElement = document.createElement("attributes");
		rootElement.appendChild(aElement);
		for (final AttrInfo info : attrInfo) {
			final String attrName = info.getAttrName();
			final boolean isrep = info.isIsrep();
			final int attrType = info.getDatatype();
			final Element attributeElement = document.createElement("attribute");
			attributeElement.setAttribute("attribute", attrName);
			attributeElement.setAttribute("isrepeating", String.valueOf(isrep));
			attributeElement.setAttribute("datatype", String.valueOf(attrType));
			aElement.appendChild(attributeElement);
			if (isrep) {
				final int valuecount = obj.getValueCount(attrName);
				for (int i = 0; i < valuecount; i++) {
					String strValue = "";
					if (attrType == DfType.DF_TIME) {
						final IDfTime time = obj.getRepeatingTime(attrName, i);
						if (!time.toString().equals("nulldate")) {
							final Date normiDate = time.getDate();
							final DateTime dt = new DateTime(normiDate);
							strValue = dt.toString();
						} else {
							strValue = "nulldate";
						}
					} else {
						strValue = obj.getRepeatingString(attrName, i);
					}
					final Element valueElement = document.createElement("value");
					valueElement.setAttribute("repeating-index", String.valueOf(i));
					valueElement.setTextContent(strValue);
					attributeElement.appendChild(valueElement);
				}
			} else {
				String strValue = "";
				if (attrType == DfType.DF_TIME) {
					final IDfTime time = obj.getTime(attrName);
					if (!time.toString().equals("nulldate")) {
						final Date normiDate = time.getDate();
						log.info(normiDate);
						final DateTime dt = new DateTime(normiDate);
						strValue = dt.toString();
					} else {
						strValue = "nulldate";
					}
				} else {
					strValue = obj.getString(attrName);
				}
				final Element valueElement = document.createElement("value");
				valueElement.setTextContent(strValue);
				attributeElement.appendChild(valueElement);
			}
		}

		try {
			final TransformerFactory transformerFactory = TransformerFactory.newInstance();
			final Transformer transformer = transformerFactory.newTransformer();
			final DOMSource source = new DOMSource(document);
			final StreamResult result = new StreamResult(new File(targetdir.getAbsoluteFile() + "/" + obj.getObjectId().getId() + ".metadata.xml"));
			transformer.transform(source, result);
		} catch (final TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void writeOldObj(final IDfSession session, final String antecedentId, final File targetdir) throws DfException, ParserConfigurationException, FileNotFoundException, IOException {
		final IDfSysObject oldobj = (IDfSysObject) session.getObject(new DfId(antecedentId));
		log.info("Oldversion: " + oldobj.getObjectId().getId() + " folder: " + targetdir);
		writeObjXML(session, oldobj, targetdir, false); // TODO virtuaalien
														// käsittely. (ei
														// tarvita bof:iin, vain
														// current:it
														// tarvitaan?)
		Updater.updateexportedinfo(session, antecedentId, oldobj.getTypeName());
		final String antId = oldobj.getAntecedentId().getId();
		if (antId.length() == 16) {
			if (!antId.equals("0000000000000000")) {
				DfLogger.debug(this, "version has previous version, continuing the check.", null, null);
				writeOldObj(session, antId, targetdir);
			} else {
				DfLogger.debug(this, "no previous version for " + antecedentId, null, null);
			}
		} else {
			DfLogger.debug(this, "no previous version for " + antecedentId, null, null);
		}
	}

	private ArrayList<String> expandGroupToUserList(final IDfSession session, final String group) throws DfException {
		IDfCollection col = null;
		final ArrayList<String> list = new ArrayList<>();
		final IDfQuery query = new DfQuery();
		try {
			query.setDQL("select distinct i_all_users_names from dm_group where group_name = '" + group + "'");
			col = query.execute(session, DfQuery.DF_READ_QUERY);
			while (col.next()) {
				final String username = col.getString("i_all_users_names");
				if (!list.contains(username)) {
					list.add(username);
				}
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

	private Element getVDMElement(final IDfSession session, final IDfSysObject obj, final Document document) {
		final Element vdmElement = document.createElement("vdmchilds");
		try {
			// IDfSession session = obj.getObjectSession();

			// String objid = obj.getObjectId().getId();
			// String objtype = obj.getTypeName();
			final IDfVirtualDocument vdm = obj.asVirtualDocument("CURRENT", false);
			final IDfVirtualDocumentNode rootNode = vdm.getRootNode();
			final int childCount = rootNode.getChildCount();
			DfLogger.debug(this, "obj has: " + childCount + " children", null, null);
			for (int i = 0; i < childCount; i++) {
				final Element childElement = document.createElement("child");
				vdmElement.appendChild(childElement);
				final IDfVirtualDocumentNode child = rootNode.getChild(i);
				final String binding = child.getBinding();
				DfLogger.debug(this, "Binding: " + child.getBinding(), null, null);
				IDfSysObject cobj = (IDfSysObject) session.getObjectByQualification("dm_document where i_chronicle_id='" + child.getChronId().getId() + "'");
				if (cobj != null) {
					if ((binding != null) && (binding.length() > 0)) {
						cobj = (IDfSysObject) session.getObjectByQualification("dm_document(ALL) where i_chronicle_id='" + child.getChronId().getId() + "' and any r_version_label = '" + binding + "'");
						DfLogger.debug(this, "", null, null);
						if (cobj != null) {
							final Element objidElement = document.createElement("r_object_id");
							final Element objnameElement = document.createElement("object_name");
							final Element objtypeElement = document.createElement("r_object_type");
							final Element bindingElement = document.createElement("binding");
							final Element versionLabels = document.createElement("versionlabels");
							final String childname = cobj.getObjectName();
							final String childid = cobj.getObjectId().getId();
							final String childtype = cobj.getTypeName();
							objidElement.setTextContent(childid);
							objnameElement.setTextContent(childname);
							objtypeElement.setTextContent(childtype);
							bindingElement.setTextContent(binding);
							// String vlabels =
							// cobj.getAllRepeatingStrings("r_version_label",
							// ",");
							final int valuecount = cobj.getValueCount("r_version_label");
							for (int j = 0; j < valuecount; j++) {
								final Element label = document.createElement("label");
								label.setTextContent(cobj.getRepeatingString("r_version_label", j));
								versionLabels.appendChild(label);
							}
							childElement.appendChild(objidElement);
							childElement.appendChild(objnameElement);
							childElement.appendChild(objtypeElement);
							childElement.appendChild(bindingElement);
							childElement.appendChild(versionLabels);
						} else {
							final String jeejee = "Failed to get this child object";

						}
					} else {
						final Element objidElement = document.createElement("r_object_id");
						final Element objnameElement = document.createElement("object_name");
						final Element objtypeElement = document.createElement("r_object_type");
						final Element bindingElement = document.createElement("binding");
						final Element versionLabels = document.createElement("versionlabels");
						final String childname = cobj.getObjectName();
						final String childid = cobj.getObjectId().getId();
						final String childtype = cobj.getTypeName();
						objidElement.setTextContent(childid);
						objnameElement.setTextContent(childname);
						objtypeElement.setTextContent(childtype);
						bindingElement.setTextContent(binding);
						// String vlabels =
						// cobj.getAllRepeatingStrings("r_version_label", ",");
						final int valuecount = cobj.getValueCount("r_version_label");
						for (int j = 0; j < valuecount; j++) {
							final Element label = document.createElement("label");
							label.setTextContent(cobj.getRepeatingString("r_version_label", j));
							versionLabels.appendChild(label);
						}
						childElement.appendChild(objidElement);
						childElement.appendChild(objnameElement);
						childElement.appendChild(objtypeElement);
						childElement.appendChild(bindingElement);
						childElement.appendChild(versionLabels);
					}
				} else {
					final String jeejee = "Failed to get this child object";
				}
			}
		} catch (final DfException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return vdmElement;

	}

	private ArrayList<IDfSysObject> getVirtualChildObjs(final IDfSession session, final IDfSysObject obj) {
		final ArrayList<IDfSysObject> childs = new ArrayList<>();
		try {
			// IDfSession session = obj.getObjectSession();
			final IDfVirtualDocument vdm = obj.asVirtualDocument("CURRENT", false);
			final IDfVirtualDocumentNode rootNode = vdm.getRootNode();
			final int childCount = rootNode.getChildCount();
			DfLogger.debug(this, "obj has: " + childCount + " children", null, null);
			for (int i = 0; i < childCount; i++) {
				final IDfVirtualDocumentNode child = rootNode.getChild(i);
				final String binding = child.getBinding();
				DfLogger.debug(this, "Binding: " + child.getBinding(), null, null);
				IDfSysObject cobj = (IDfSysObject) session.getObjectByQualification("dm_document where i_chronicle_id='" + child.getChronId().getId() + "'");
				if (cobj != null) {
					if ((binding != null) && (binding.length() > 0)) {
						cobj = (IDfSysObject) session.getObjectByQualification("dm_document(ALL) where i_chronicle_id='" + child.getChronId().getId() + "' and any r_version_label = '" + binding + "'");
					} else {
						final String jeejee = "Failed to get this child object";
					}
				}
				childs.add(cobj);
			}
		} catch (final DfException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return childs;
	}

	private ArrayList<String> getAdditionalGroups(final IDfSession session, final IDfACL acl, final String accessor) {

		IDfCollection col = null;
		final ArrayList<String> rval = new ArrayList<>();
		final IDfQuery query = new DfQuery();
		query.setDQL("select distinct group_name from dm_group where any i_supergroups_names = '" + accessor + "'");
		try {
			col = query.execute(session, DfQuery.QUERY);
			while (col.next()) {
				final String group_name = col.getString("group_name");
				if (group_name != null) {
					if (group_name.length() > 1) {
						rval.add(group_name);
					}
				}
			}
		} catch (final DfException ex) {
			ex.printStackTrace();
			System.exit(1);
		} finally {
			if (col != null) {
				try {
					col.close();
				} catch (final DfException ex) {
					// NN
				}
			}
		}

		return rval;

	}
}
