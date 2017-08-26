/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quickclient.exportlib;

import java.util.ArrayList;

/**
 *
 */
public class TypeInfoHolder {

	static ArrayList<TypeInfo> typeInfo = new ArrayList<>();

	private TypeInfoHolder() {
	}

	private static class SingletonHolder {

		public static final TypeInfoHolder INSTANCE = new TypeInfoHolder();
	}

	public static TypeInfoHolder getInstance() {
		return SingletonHolder.INSTANCE;
	}

	public static void addTypeInfo(final TypeInfo info) {
		typeInfo.add(info);
	}

	public static synchronized TypeInfo getTypeInfo(final String typeName) {
		TypeInfo rval = null;
		for (final TypeInfo info : typeInfo) {
			if (info.getTypeName().equals(typeName)) {
				rval = info;
			}
		}
		return rval;
	}

}
