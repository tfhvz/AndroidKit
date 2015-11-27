package com.snicesoft.androidkit;

/**
 * Created by zhuzhe on 15/11/26.
 */
public class Resource extends BaseId {
    private String path;
    private Long size;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "path='" + path + '\'' +
                ", size=" + size +
                '}';
    }
}
