/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.fortica.dctm;

import com.documentum.fc.client.DfType;
import com.documentum.fc.client.IDfACL;
import com.documentum.fc.client.IDfFolder;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.IDfTime;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
	public void setTargetDir(String folder) {
		this.targetDir = folder;
	}

	FolderExporter(IDfFolder f) {
		folder = f;
	}

	public void exportFolder(IDfSession session)
			throws DfException, ParserConfigurationException, FileNotFoundException, IOException {
		String typeName = folder.getTypeName();
		File targetdir = new File(targetDir + "/folders");
		if (!targetdir.exists()) {
			targetdir.mkdir();
		}
		File lookupfile = new File(targetDir + "/folders/lookup.txt");
		if (!lookupfile.exists()) {
			lookupfile.createNewFile();
		}

		File xmlFile = new File(targetDir + "/folders/" + folder.getObjectId().getId() + ".metadata.xml");
		if (xmlFile.exists()) {
			return;
		}

		FileWriter fw = new FileWriter(lookupfile, true);
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

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder loader = factory.newDocumentBuilder();
		Document document = loader.newDocument();

		System.out.println("typeName is: " + typeName);
		TypeInfo typeInfo = TypeInfoHolder.getTypeInfo(typeName);
		System.out.println(typeInfo);
		ArrayList<AttrInfo> attrInfo = typeInfo.getAttributeInfo();
		// System.out.println(attrInfo);
		Element rootElement = document.createElement("document");
		Element permissionElement = document.createElement("permissions");

		rootElement.appendChild(permissionElement);

		IDfACL acl = folder.getACL();
		int accessorcount = acl.getAccessorCount();
		for (int i = 0; i < accessorcount; i++) {

			String accessor = acl.getAccessorName(i);
			int accessorpermit = acl.getAccessorPermit(i);
			boolean isgroup = acl.isGroup(i);

			Element accessorElement = document.createElement("accessor");

			if (isgroup) {
				Element accessorName = document.createElement("name");
				Element permitElement = document.createElement("permission");
				accessorElement.setAttribute("accessortype", "group");
				accessorName.setTextContent(accessor);
				permitElement.setTextContent(String.valueOf(accessorpermit));
				accessorElement.appendChild(accessorName);
				accessorElement.appendChild(permitElement);

			} else {
				Element accessorName = document.createElement("name");
				Element permitElement = document.createElement("permission");
				accessorElement.setAttribute("accessortype", "user");
				accessorName.setTextContent(accessor);
				permitElement.setTextContent(String.valueOf(accessorpermit));
				accessorElement.appendChild(accessorName);
				accessorElement.appendChild(permitElement);
			}
			permissionElement.appendChild(accessorElement);
		}
		document.appendChild(rootElement);
		Element aElement = document.createElement("attributes");
		rootElement.appendChild(aElement);
		for (AttrInfo info : attrInfo) {
			String attrName = info.getAttrName();
			boolean isrep = info.isIsrep();
			int attrType = info.getDatatype();
			Element attributeElement = document.createElement("attribute");
			attributeElement.setAttribute("attribute", attrName);
			attributeElement.setAttribute("isrepeating", String.valueOf(isrep));
			attributeElement.setAttribute("datatype", String.valueOf(attrType));
			aElement.appendChild(attributeElement);
			if (isrep) {
				int valuecount = folder.getValueCount(attrName);
				for (int i = 0; i < valuecount; i++) {
					String strValue = "";
					if (attrType == DfType.DF_TIME) {
						IDfTime time = folder.getRepeatingTime(attrName, i);
						if (!time.toString().equals("nulldate")) {
							Date normiDate = time.getDate();
							DateTime dt = new DateTime(normiDate);
							strValue = dt.toString();
						} else {
							strValue = "nulldate";
						}
					} else {
						strValue = folder.getRepeatingString(attrName, i);
					}
					Element valueElement = document.createElement("value");
					valueElement.setAttribute("repeating-index", String.valueOf(i));
					valueElement.setTextContent(strValue);
					attributeElement.appendChild(valueElement);
				}
			} else {
				String strValue = "";
				if (attrType == DfType.DF_TIME) {
					IDfTime time = folder.getTime(attrName);
					if (!time.toString().equals("nulldate")) {
						Date normiDate = time.getDate();
						DateTime dt = new DateTime(normiDate);
						strValue = dt.toString();
					} else {
						strValue = "nulldate";
					}
				} else {
					strValue = folder.getString(attrName);
				}
				Element valueElement = document.createElement("value");
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
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(
					new File(targetdir.getAbsoluteFile() + "/" + folder.getObjectId().getId() + ".metadata.xml"));
			transformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String folderpath = folder.getRepeatingString("r_folder_path", 0);
		System.out.println("old path:" + folderpath);
		folderpath = PathReplacer.getNewPath(folderpath);
		System.out.println("new path:" + folderpath);
		StringTokenizer t = new StringTokenizer(folderpath, "/");
		String pathbuild = "";
		while (t.hasMoreTokens()) {
			String tstring = t.nextToken();
			pathbuild = pathbuild + "/" + tstring;
			IDfFolder tfolder = session.getFolderByPath(pathbuild);
			if (tfolder != null) {
				FolderExporter ee = new FolderExporter(tfolder);
				ee.setTargetDir(this.targetDir);
				ee.exportFolder(session);
			}
		}
	}
}
