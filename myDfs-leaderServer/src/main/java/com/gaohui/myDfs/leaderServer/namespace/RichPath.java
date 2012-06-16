package com.gaohui.myDfs.leaderServer.namespace;

import com.gaohui.myDfs.core.Path;

import java.io.Serializable;

/**
 * User: Administrator
 * Date: 11-4-26 Time: 下午10:30
 *
 * @author Basten Gao
 */
public class RichPath implements Serializable {
    private Path path;
    private boolean directory;

    public RichPath(Path path, boolean directory) {
        this.path = path;
        this.directory = directory;
    }

    public Path getPath() {
        return path;
    }

    public boolean isDirectory() {
        return directory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RichPath richPath = (RichPath) o;

        if (directory != richPath.directory) return false;
        if (!path.equals(richPath.path)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = path.hashCode();
        result = 31 * result + (directory ? 1 : 0);
        return result;
    }
}
