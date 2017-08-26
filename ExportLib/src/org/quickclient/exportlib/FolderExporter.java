/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quickclient.exportlib;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
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

import org.joda.time.DateTime;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.documentum.fc.client.DfType;
import com.documentum.fc.client.IDfACL;
import com.documentum.fc.client.IDfFolder;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.IDfTime;

/**
 * 
 * @author miksuoma
 */
@SuppressWarnings("restriction")
public class FolderExporter {

	IDfFolder folder = null;
	ConfigReader cr = ConfigReader.getInstance();
	String targetDir = "";

	/**
	 * Set location (folder) of export in local filesystem
	 * 
	 * @param folder
	 *            absolule folderpath
	 */
	public void setTargetDir(final String folder) {
		this.targetDir = folder;
	}

	FolderExporter(final IDfFolder f) {
		folder = f;
	}

	public void exportFolder(final IDfSession session) throws DfException, ParserConfigurationException, FileNotFoundException, IOException {
		final String typeName = folder.getTypeName();
		final File targetdir = new File(targetDir + "/folders");
		if (!targetdir.exists()) {
			targetdir.mkdir();
		}
		final File lookupfile = new File(targetDir + "/folders/lookup.txt");
		if (!lookupfile.exists()) {
			lookupfile.createNewFile();
		}

		final File xmlFile = new File(targetDir + "/folders/" + folder.getObjectId().getId() + ".metadata.xml");
		if (xmlFile.exists()) {
			return;
		}

		final FileWriter fw = new FileWriter(lookupfile, true);
		String folderPath = folder.getRepeatingString("r_folder_path", 0);
		System.out.println("old path:" + folderPath);
		folderPath = PathReplacer.getNewPath(folderPath);
		System.out.println("new path:" + folderPath);

		fw.write(folder.getObjectId().getId() + "," + folderPath + "\n");// appends
																			// the
																			// string
																			// to
																			// the
																			// file

		fw.close();

		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		final DocumentBuilder loader = factory.newDocumentBuilder();
		final Document document = loader.newDocument();

		System.out.println("typeName is: " + typeName);
		final TypeInfo typeInfo = TypeInfoHolder.getTypeInfo(typeName);
		System.out.println(typeInfo);
		final ArrayList<AttrInfo> attrInfo = typeInfo.getAttributeInfo();
		// System.out.println(attrInfo);
		final Element rootElement = document.createElement("document");
		final Element permissionElement = document.createElement("permissions");

		rootElement.appendChild(permissionElement);

		final IDfACL acl = folder.getACL();
		final int accessorcount = acl.getAccessorCount();
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

			} else {
				final Element accessorName = document.createElement("name");
				final Element permitElement = document.createElement("permission");
				accessorElement.setAttribute("accessortype", "user");
				accessorName.setTextContent(accessor);
				permitElement.setTextContent(String.valueOf(accessorpermit));
				accessorElement.appendChild(accessorName);
				accessorElement.appendChild(permitElement);
			}
			permissionElement.appendChild(accessorElement);
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
				final int valuecount = folder.getValueCount(attrName);
				for (int i = 0; i < valuecount; i++) {
					String strValue = "";
					if (attrType == DfType.DF_TIME) {
						final IDfTime time = folder.getRepeatingTime(attrName, i);
						if (!time.toString().equals("nulldate")) {
							final Date normiDate = time.getDate();
							final DateTime dt = new DateTime(normiDate);
							strValue = dt.toString();
						} else {
							strValue = "nulldate";
						}
					} else {
						strValue = folder.getRepeatingString(attrName, i);
					}
					final Element valueElement = document.createElement("value");
					valueElement.setAttribute("repeating-index", String.valueOf(i));
					valueElement.setTextContent(strValue);
					attributeElement.appendChild(valueElement);
				}
			} else {
				String strValue = "";
				if (attrType == DfType.DF_TIME) {
					final IDfTime time = folder.getTime(attrName);
					if (!time.toString().equals("nulldate")) {
						final Date normiDate = time.getDate();
						final DateTime dt = new DateTime(normiDate);
						strValue = dt.toString();
					} else {
						strValue = "nulldate";
					}
				} else {
					strValue = folder.getString(attrName);
				}
				final Element valueElement = document.createElement("value");
				valueElement.setTextContent(strValue);
				attributeElement.appendChild(valueElement);
			}
		}
		// OutputFormat format = new OutputFormat((Document) document);
		// format.setLineSeparator(LineSeparator.Unix);
		// format.setIndenting(true);
		// format.setLineWidth(0);
		// format.setPreserveSpace(true);
		// XMLSerializer serializer = new XMLSerializer(new
		// FileOutputStream(targetdir.getAbsoluteFile() + "/" +
		// folder.getObjectId().getId() + ".metadata.xml"), format);
		// serializer.asDOMSerializer();
		// serializer.serialize(document);

		// write the content into xml file
		try {
			final TransformerFactory transformerFactory = TransformerFactory.newInstance();
			final Transformer transformer = transformerFactory.newTransformer();
			final DOMSource source = new DOMSource(document);
			final StreamResult result = new StreamResult(new File(targetdir.getAbsoluteFile() + "/" + folder.getObjectId().getId() + ".metadata.xml"));
			transformer.transform(source, result);
		} catch (final TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String folderpath = folder.getRepeatingString("r_folder_path", 0);
		System.out.println("old path:" + folderpath);
		folderpath = PathReplacer.getNewPath(folderpath);
		System.out.println("new path:" + folderpath);
		final StringTokenizer t = new StringTokenizer(folderpath, "/");
		String pathbuild = "";
		while (t.hasMoreTokens()) {
			final String tstring = t.nextToken();
			pathbuild = pathbuild + "/" + tstring;
			final IDfFolder tfolder = session.getFolderByPath(pathbuild);
			if (tfolder != null) {
				final FolderExporter ee = new FolderExporter(tfolder);
				ee.setTargetDir(this.targetDir);
				ee.exportFolder(session);
			}
		}
	}
}
