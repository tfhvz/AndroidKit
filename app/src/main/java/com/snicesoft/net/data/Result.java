package com.snicesoft.net.data;

import lombok.Data;

@Data
public class Result<T> {
    private String msg;
    private int status;
    private T data;

    @Override
    public String toString() {
        return "Result [msg=" + msg + ", status=" + status + ", data=" + data + "]";
    }

}
