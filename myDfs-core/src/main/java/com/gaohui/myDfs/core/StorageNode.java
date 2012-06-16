package com.gaohui.myDfs.core;

/**
 * User: Administrator
 * Date: 11-2-3 Time: 下午10:00
 *
 * @author Basten Gao
 */
public class StorageNode {
    private String location;
    private int port;

    public StorageNode(String location, int port) {
        this.location = location;
        this.port = port;
    }

    public String getLocation() {
        return location;
    }

    public int getPort() {
        return port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StorageNode that = (StorageNode) o;

        if (port != that.port) return false;
        if (location != null ? !location.equals(that.location) : that.location != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = location != null ? location.hashCode() : 0;
        result = 31 * result + port;
        return result;
    }

    @Override
    public String toString() {
        return location + ":" + port;
    }
}
