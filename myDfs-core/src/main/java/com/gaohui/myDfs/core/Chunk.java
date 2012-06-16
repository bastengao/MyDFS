package com.gaohui.myDfs.core;

import com.google.common.base.Preconditions;

import java.io.Serializable;

/**
 * 表示文件在storageServer上的本地存储的文件分块
 * User: Administrator
 * Date: 11-2-3 Time: 下午6:20
 *
 * @author Basten Gao
 */
public class Chunk implements Serializable {
    private int ordinal;
    private Path owner;

    /**
     *
     */
    public Chunk() {
    }

    /**
     * @param owner   属于哪个owner
     * @param ordinal chunk序号
     */
    public Chunk(Path owner, int ordinal) {
        Preconditions.checkNotNull(owner, "owner must not be null.");
        Preconditions.checkArgument(ordinal >= 0, "ordinal could not be less than 0.");
        this.ordinal = ordinal;
        this.owner = owner;
    }

    public int getOrdinal() {
        return ordinal;
    }


    public Path getOwner() {
        return owner;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Chunk chunk = (Chunk) o;

        if (ordinal != chunk.ordinal) {
            return false;
        }
        if (!owner.equals(chunk.owner)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = ordinal;
        result = 31 * result + owner.hashCode();
        return result;
    }


    @Override
    public String toString() {
        return "path: " + owner + " ordinal: " + ordinal;
    }
}
