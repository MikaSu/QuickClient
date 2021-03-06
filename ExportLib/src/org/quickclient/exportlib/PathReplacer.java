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
public class PathReplacer {

	static ArrayList<String> replaceinfo = new ArrayList();

	private PathReplacer() {
	}

	private static class SingletonHolderx {

		public static final PathReplacer INSTANCE = new PathReplacer();
	}

	public static PathReplacer getInstance() {
		return SingletonHolderx.INSTANCE;
	}

	public static void addPaths(final String paths) {
		replaceinfo.add(paths);
	}

	public synchronized static String getNewPath(final String path) {
		final String rval = "";
		String newpath = path;
		for (final String nn : replaceinfo) {
			final String oldandnew[] = nn.split("@@@@");
			newpath = newpath.replace(oldandnew[0], oldandnew[1]);
		}
		return newpath;
	}
}
