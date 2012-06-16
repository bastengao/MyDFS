package com.gaohui.myDfs.core;

/**
 * Immutable
 * User: Administrator
 * Date: 11-2-4 Time: 下午6:33
 *
 * @author Basten Gao
 */
public class FileStatus {
    private Path path;
    private boolean isDirectory;

    public FileStatus(Path path, boolean directory) {
        this.path = path;
        isDirectory = directory;
    }

    public Path getPath() {
        return path;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public boolean isFile() {
        return !isDirectory;
    }

    @Override
    public String toString() {
        return "FileStatus{"
                + "path="
                + path
                + ", isDirectory="
                + isDirectory
                + '}';
    }
}
