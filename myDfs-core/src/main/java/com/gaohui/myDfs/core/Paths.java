package com.gaohui.myDfs.core;

import java.util.Arrays;

/**
 * User: Administrator
 * Date: 11-2-4 Time: 下午4:50
 *
 * @author Basten Gao
 */
public class Paths {
    /**
     * root path
     */
    public static final Path ROOT = new Path("/");

    /**
     * 查看是否是后裔。  "/a/b/c/d"  is a descendant of  "/a/b"
     *
     * @param upPath
     * @param downPath
     * @return
     */
    public static boolean isDescendant(Path upPath, Path downPath) {
        return downPath.getPath().startsWith(upPath.getPath());
    }

    /**
     * 查看是否是孩子。 "/a/b/c" is a child of "/a/b"
     *
     * @param parent
     * @param child
     * @return
     */
    public static boolean isChildren(Path parent, Path child) {
        //TODO
        return false;
    }

    /**
     * 查看是否是兄弟。 "/a/b/c" is a sibling of "/a/b/d"
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean isSibling(Path a, Path b) {
        //TODO
        return false;
    }

    public static String[] breaks(String path) {
        String[] names = path.split("/");
        if (names.length <= 1) {
            return new String[0];
        }

        return Arrays.copyOfRange(names, 1, names.length);
    }
}
