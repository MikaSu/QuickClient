/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quickclient.exportlib;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.DfType;
import com.documentum.fc.client.IDfACL;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfFolder;
import com.documentum.fc.client.IDfFormat;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfUser;
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

	private String folderRoot;

	/**
	 * 
	 * @param session Documentum session
	 * @param chronicle_id root obj id
	 * @param targetdir targetdir for versiontree (contains id)
	 * @param targetDir2 targetdir root
	 */
    public void exportVersionTree(IDfSession session, String chronicle_id, File targetdir, String targetDir2) {
    	this.folderRoot = targetDir2;
    	
        try {
            System.out.println(chronicle_id);
            System.out.println(targetdir.getAbsolutePath());

            IDfSysObject obj = (IDfSysObject) session.getObjectByQualification("dm_document where i_chronicle_id = '" + chronicle_id + "'");
            if (obj != null) {
                String antecedentId = obj.getAntecedentId().getId();
                System.out.println("antecedentId: " + antecedentId);
                writeObjXML(session, obj, targetdir, false);
                Updater.updateexportedinfo(session,chronicle_id,obj.getTypeName());
                System.out.println("antecedentId: " + antecedentId);

                if (antecedentId.length() == 16) {
                    if (!antecedentId.equals("0000000000000000")) {
                        writeOldObj(session, antecedentId, targetdir);
                    }
                }
                boolean isvirtualdoc = false;
                if (obj.isVirtualDocument()) {
                    isvirtualdoc = true;
                }
                int rlinkcnt = obj.getLinkCount();
                if (rlinkcnt > 1) {
                    isvirtualdoc = true;
                }
                if (isvirtualdoc) {
                    ArrayList<IDfSysObject> virtualchilds = getVirtualChildObjs(session, obj);
                    for (IDfSysObject childObj : virtualchilds) {
                        writeObjXML(session, childObj, targetdir, true);
                        Updater.updateexportedinfo(session,childObj.getObjectId().getId(),childObj.getTypeName());
                    }
                }
            }

        } catch (DfException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @SuppressWarnings("restriction")
	private void writeObjXML(IDfSession session, IDfSysObject obj, File targetdir, boolean isvirtualchild) throws DfException, ParserConfigurationException, FileNotFoundException, IOException {
        String typeName = obj.getTypeName();
        if (isvirtualchild) {
            String filepath = targetdir.getAbsolutePath();
            targetdir = new File(filepath + "/supporting_docs/");
            targetdir.mkdir();
            System.out.println("targetdir: " + targetdir.getAbsolutePath());
        }
        boolean isvirtualdoc = false;
        if (obj.isVirtualDocument()) {
            isvirtualdoc = true;
        }
        int rlinkcnt = obj.getLinkCount();
        if (rlinkcnt > 1) {
            isvirtualdoc = true;
        }

        IDfCollection col2 = null;
        IDfId primarycontentId = obj.getContentsId();
        col2 = obj.getRenditions("full_format, page_modifier, page, content_size,set_time, set_file, r_object_id");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder loader = factory.newDocumentBuilder();
        Document document = loader.newDocument();
        ArrayList<Element> contentElements = new ArrayList<Element>();
        while (col2.next()) {
            boolean isprimary = false;
            String format = col2.getString("full_format");
            IDfFormat idfformat = (IDfFormat) session.getFormat(format);
            String extension = idfformat.getDOSExtension();
            String contentid = col2.getString("r_object_id");
            if (contentid.equals(primarycontentId.getId())) {
                isprimary = true;
            }
            String page = col2.getString("page");
            String pageModifier = col2.getString("page_modifier");
            String fileName = targetdir.getAbsolutePath() + "/" + obj.getObjectId().getId() + "." + contentid + "." + pageModifier + "." + format + "." + extension;
            System.out.println(fileName);
            System.out.println(pageModifier);
            System.out.println(fileName + "+" + format + "+" + Integer.parseInt(page) + "+" + pageModifier + "+" + false);
            obj.getFileEx2(fileName, format, Integer.parseInt(page), pageModifier, false);
            //obj.getFileEx(fileName, format, Integer.parseInt(page), true);
            Element content = document.createElement("content");
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
        System.out.println("typeName is: " + typeName);
        TypeInfo typeInfo = TypeInfoHolder.getTypeInfo(typeName);
        System.out.println("typeInfo is: " + typeInfo);
        ArrayList<AttrInfo> attrInfo = typeInfo.getAttributeInfo();
        System.out.println("attrInfo is: "+ attrInfo);
        Element rootElement = document.createElement("document");
        Element folderElement = document.createElement("folderpath");
        Element permissionElement = document.createElement("permissions");
        Element contentElement = document.createElement("contentfiles");
        for (Element e : contentElements) {
            contentElement.appendChild(e);
        }
        rootElement.appendChild(folderElement);
        rootElement.appendChild(contentElement);
        rootElement.appendChild(permissionElement);

        if (isvirtualdoc) {
            Element vdmElement = getVDMElement(session, obj, document);
            rootElement.appendChild(vdmElement);
        } else {
            //insert empty element
            Element vdmElement = document.createElement("vdmchilds");
            rootElement.appendChild(vdmElement);
        }

        int foldercount = obj.getValueCount("i_folder_id");
        for (int i = 0; i < foldercount; i++) {
            IDfId folderid = obj.getRepeatingId("i_folder_id", i);
            IDfFolder f = (IDfFolder) session.getObject(folderid);
            int nn = f.getFolderPathCount();
            for (int j = 0; j < nn; j++) {
                String folderPath = f.getFolderPath(j);
                System.out.println("old path:"+folderPath);
                folderPath = PathReplacer.getNewPath(folderPath);
                                System.out.println("new path:"+folderPath);

                Element felement = document.createElement("path");
                felement.setAttribute("i_folder_id_index", String.valueOf(i));
                felement.setAttribute("r_folder_path_index", String.valueOf(j));
                felement.setAttribute("path", folderPath);
                felement.setAttribute("r_object_id", folderid.getId());
                folderElement.appendChild(felement);

                System.out.println("FOLDERPATH: " + folderPath);
                StringTokenizer st = new StringTokenizer(folderPath, "/");
                String folderpart = "/";
                while (st.hasMoreTokens()) {
                    String token = st.nextToken();
                    System.out.println(token);
                    folderpart = folderpart + token;
                    System.out.println("folder is: " + folderpart);
                    FolderExporter exporter = new FolderExporter(f);
                    exporter.setTargetDir(this.folderRoot);
                    exporter.exportFolder(session);
                    folderpart = folderpart + "/";

                }
                int ancestorcount = f.getAncestorIdCount();
                for (int ac = 0;ac < ancestorcount;ac++) {
                    IDfFolder af = (IDfFolder) session.getObject(new DfId(f.getAncestorId(ac)));
                    FolderExporter exporter = new FolderExporter(af);
                    exporter.setTargetDir(this.folderRoot);
                    exporter.exportFolder(session);
                }
                
                
            }
        }
        IDfACL acl = obj.getACL();
        int accessorcount = acl.getAccessorCount();
        ArrayList<String> addedgroups = new ArrayList<String>();
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

                ArrayList<String> additionalgroups = getAdditionalGroups(session, acl, accessor);
                for (String gname : additionalgroups) {

                    Element accessorElementg = document.createElement("accessor");
                    Element accessorNameg = document.createElement("name");
                    Element permitElementg = document.createElement("permission");
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
                Element accessorName = document.createElement("name");
                Element permitElement = document.createElement("permission");
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
                int valuecount = obj.getValueCount(attrName);
                for (int i = 0; i < valuecount; i++) {
                    String strValue = "";
                    if (attrType == DfType.DF_TIME) {
                        IDfTime time = obj.getRepeatingTime(attrName, i);
                        if (!time.toString().equals("nulldate")) {
                            Date normiDate = time.getDate();
                            DateTime dt = new DateTime(normiDate);
                            strValue = dt.toString();
                        } else {
                            strValue = "nulldate";
                        }
                    } else {
                        strValue = obj.getRepeatingString(attrName, i);
                    }
                    Element valueElement = document.createElement("value");
                    valueElement.setAttribute("repeating-index", String.valueOf(i));
                    valueElement.setTextContent(strValue);
                    attributeElement.appendChild(valueElement);
                }
            } else {
                String strValue = "";
                if (attrType == DfType.DF_TIME) {
                    IDfTime time = obj.getTime(attrName);
                    if (!time.toString().equals("nulldate")) {
                        Date normiDate = time.getDate();
                        System.out.println(normiDate);
                        DateTime dt = new DateTime(normiDate);
                        strValue = dt.toString();
                    } else {
                        strValue = "nulldate";
                    }
                } else {
                    strValue = obj.getString(attrName);
                }
                Element valueElement = document.createElement("value");
                valueElement.setTextContent(strValue);
                attributeElement.appendChild(valueElement);
            }
        }
        
        
//        OutputFormat format = new OutputFormat((Document) document);
//        format.setLineSeparator(LineSeparator.Unix);
//        format.setIndenting(true);
//        format.setLineWidth(0);
//        format.setPreserveSpace(true);
//        XMLSerializer serializer = new XMLSerializer(new FileOutputStream(targetdir.getAbsoluteFile() + "/" + obj.getObjectId().getId() + ".metadata.xml"), format);
//        serializer.asDOMSerializer();
//        serializer.serialize(document);
        
        try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(
					new File(targetdir.getAbsoluteFile() + "/" + obj.getObjectId().getId() + ".metadata.xml"));
			transformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }

    private void writeOldObj(IDfSession session, String antecedentId, File targetdir) throws DfException, ParserConfigurationException, FileNotFoundException, IOException {
        IDfSysObject oldobj = (IDfSysObject) session.getObject(new DfId(antecedentId));
        System.out.println("Oldversion: " + oldobj.getObjectId().getId() + " folder: " + targetdir);
        writeObjXML(session, oldobj, targetdir, false); //TODO virtuaalien käsittely. (ei tarvita bof:iin, vain current:it tarvitaan?)
        Updater.updateexportedinfo(session,antecedentId,oldobj.getTypeName());
        String antId = oldobj.getAntecedentId().getId();
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

    private ArrayList<String> expandGroupToUserList(IDfSession session, String group) throws DfException {
        IDfCollection col = null;
        ArrayList<String> list = new ArrayList<String>();
        IDfQuery query = new DfQuery();
        try {
            query.setDQL("select distinct i_all_users_names from dm_group where group_name = '" + group + "'");
            col = query.execute(session, DfQuery.DF_READ_QUERY);
            while (col.next()) {
                String username = col.getString("i_all_users_names");
                IDfUser user = session.getUser(username);
                if (!list.contains(username)) {
                    list.add(username);
                }
            }
        } catch (DfException e) {
            e.printStackTrace();
        } finally {
            if (col != null) {
                col.close();
            }
        }
        return list;
    }

    private Element getVDMElement(IDfSession session, IDfSysObject obj, Document document) {
        Element vdmElement = document.createElement("vdmchilds");
        try {
            //IDfSession session = obj.getObjectSession();

            //          String objid = obj.getObjectId().getId();
            //         String objtype = obj.getTypeName();
            IDfVirtualDocument vdm = obj.asVirtualDocument("CURRENT", false);
            IDfVirtualDocumentNode rootNode = vdm.getRootNode();
            int childCount = rootNode.getChildCount();
            DfLogger.debug(this, "obj has: " + childCount + " children", null, null);
            for (int i = 0; i < childCount; i++) {
                Element childElement = document.createElement("child");
                vdmElement.appendChild(childElement);
                IDfVirtualDocumentNode child = rootNode.getChild(i);
                String binding = child.getBinding();
                DfLogger.debug(this, "Binding: " + child.getBinding(), null, null);
                IDfSysObject cobj = (IDfSysObject) session.getObjectByQualification("dm_document where i_chronicle_id='" + child.getChronId().getId() + "'");
                if (cobj != null) {
                    if (binding != null && binding.length() > 0) {
                        cobj = (IDfSysObject) session.getObjectByQualification("dm_document(ALL) where i_chronicle_id='" + child.getChronId().getId() + "' and any r_version_label = '" + binding + "'");
                        DfLogger.debug(this, "", null, null);
                        if (cobj != null) {
                            Element objidElement = document.createElement("r_object_id");
                            Element objnameElement = document.createElement("object_name");
                            Element objtypeElement = document.createElement("r_object_type");
                            Element bindingElement = document.createElement("binding");
                            Element versionLabels = document.createElement("versionlabels");
                            String childname = cobj.getObjectName();
                            String childid = cobj.getObjectId().getId();
                            String childtype = cobj.getTypeName();
                            objidElement.setTextContent(childid);
                            objnameElement.setTextContent(childname);
                            objtypeElement.setTextContent(childtype);
                            bindingElement.setTextContent(binding);
//                            String vlabels = cobj.getAllRepeatingStrings("r_version_label", ",");
                            int valuecount = cobj.getValueCount("r_version_label");
                            for (int j = 0; j < valuecount; j++) {
                                Element label = document.createElement("label");
                                label.setTextContent(cobj.getRepeatingString("r_version_label", j));
                                versionLabels.appendChild(label);
                            }
                            childElement.appendChild(objidElement);
                            childElement.appendChild(objnameElement);
                            childElement.appendChild(objtypeElement);
                            childElement.appendChild(bindingElement);
                            childElement.appendChild(versionLabels);
                        } else {
                            String jeejee = "Failed to get this child object";
                        }
                    } else {
                        Element objidElement = document.createElement("r_object_id");
                        Element objnameElement = document.createElement("object_name");
                        Element objtypeElement = document.createElement("r_object_type");
                        Element bindingElement = document.createElement("binding");
                        Element versionLabels = document.createElement("versionlabels");
                        String childname = cobj.getObjectName();
                        String childid = cobj.getObjectId().getId();
                        String childtype = cobj.getTypeName();
                        objidElement.setTextContent(childid);
                        objnameElement.setTextContent(childname);
                        objtypeElement.setTextContent(childtype);
                        bindingElement.setTextContent(binding);
//                            String vlabels = cobj.getAllRepeatingStrings("r_version_label", ",");
                        int valuecount = cobj.getValueCount("r_version_label");
                        for (int j = 0; j < valuecount; j++) {
                            Element label = document.createElement("label");
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
                    String jeejee = "Failed to get this child object";
                }
            }
        } catch (DfException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return vdmElement;

    }

    private ArrayList<IDfSysObject> getVirtualChildObjs(IDfSession session, IDfSysObject obj) {
        ArrayList<IDfSysObject> childs = new ArrayList<IDfSysObject>();
        try {
            //IDfSession session = obj.getObjectSession();
            IDfVirtualDocument vdm = obj.asVirtualDocument("CURRENT", false);
            IDfVirtualDocumentNode rootNode = vdm.getRootNode();
            int childCount = rootNode.getChildCount();
            DfLogger.debug(this, "obj has: " + childCount + " children", null, null);
            for (int i = 0; i < childCount; i++) {
                IDfVirtualDocumentNode child = rootNode.getChild(i);
                String binding = child.getBinding();
                DfLogger.debug(this, "Binding: " + child.getBinding(), null, null);
                IDfSysObject cobj = (IDfSysObject) session.getObjectByQualification("dm_document where i_chronicle_id='" + child.getChronId().getId() + "'");
                if (cobj != null) {
                    if (binding != null && binding.length() > 0) {
                        cobj = (IDfSysObject) session.getObjectByQualification("dm_document(ALL) where i_chronicle_id='" + child.getChronId().getId() + "' and any r_version_label = '" + binding + "'");
                    } else {
                        String jeejee = "Failed to get this child object";
                    }
                }
                childs.add(cobj);
            }
        } catch (DfException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return childs;
    }

    private ArrayList<String> getAdditionalGroups(IDfSession session, IDfACL acl, String accessor) {

        IDfCollection col = null;
        ArrayList<String> rval = new ArrayList<String>();
        IDfQuery query = new DfQuery();
        query.setDQL("select distinct group_name from dm_group where any i_supergroups_names = '" + accessor + "'");
        try {
            col = query.execute(session, DfQuery.QUERY);
            while (col.next()) {
                String group_name = col.getString("group_name");
                if (group_name != null) {
                    if (group_name.length() > 1) {
                        rval.add(group_name);
}
                }
            }
        } catch (DfException ex) {
            //Logger.getLogger(XMLExporter.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            System.exit(1);
        } finally {
            if (col != null) {
                try {
                    col.close();
                } catch (DfException ex) {
              //      Logger.getLogger(XMLExporter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return rval;

    }
}
