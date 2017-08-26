/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quickclient.exportlib;

import java.util.ArrayList;


/**
 *
 * @author miksuoma
 */
public class TypeInfoHolder {

    static ArrayList<TypeInfo> typeInfo = new ArrayList();


    private TypeInfoHolder() {
    }

    private static class SingletonHolder {

        public static final TypeInfoHolder INSTANCE = new TypeInfoHolder();
    }

    public static TypeInfoHolder getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public static void addTypeInfo(TypeInfo info) {
        typeInfo.add(info);
    }

    public synchronized static TypeInfo getTypeInfo(String typeName) {
        TypeInfo rval = null;
        for (TypeInfo info: typeInfo) {
            if (info.getTypeName().equals(typeName)) {
                rval = info;
            }
        }
        return rval;
    }


}



