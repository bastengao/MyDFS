package com.gaohui.myDfs.core;

import java.io.Serializable;

/**
 * 表示文件系统中目录或者文件的路径。根目录从"/"开始，比如根目录下的"a.txt"表示为"/a.txt"，以此类推。
 * tag:immutable
 * User: Administrator
 * Date: 11-2-3 Time: 下午12:12
 *
 * @author Basten Gao
 */
public class Path implements Serializable {
    public static final String PATH_SEPARATOR = "/";
    private String pathStr;

    public Path(String path) {
        this.pathStr = path;
    }

    public String getPath() {
        return pathStr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Path path = (Path) o;

        if (pathStr != null ? !pathStr.equals(path.pathStr) : path.pathStr != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return pathStr != null ? pathStr.hashCode() : 0;
    }

    @Override
    public String toString() {
        return pathStr;
    }
}
