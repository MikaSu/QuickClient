/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fi.fortica.dctm;

import java.util.ArrayList;



/**
 *
 * @author miksuoma
 */
public class TypeInfo {
    private String typeName;
    private ArrayList<AttrInfo> attrInfo= new ArrayList<AttrInfo>();

    /**
     * @return the typeName
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * @param typeName the typeName to set
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /**
     * Appends info of attribute type information
     * @param info info
     */
    public void appendAttrInfo(AttrInfo info) {
        attrInfo.add(info);
    }
    
    /**
     * Gets attribute info
     * @return attrInfo
     */
    public ArrayList<AttrInfo> getAttributeInfo() {
        return attrInfo;
    }
}
