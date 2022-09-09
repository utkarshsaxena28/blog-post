package com.blog.exception;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ErrorDetails implements Serializable {   // Why we put Serializable

    private Date timestamp;
    private String message;
    private List<String> data;
    private String path;
    private int code;

    public ErrorDetails() {
    }

    public ErrorDetails(Date timestamp, String message, List<String> data, String path, int code) {
        this.timestamp = timestamp;
        this.message = message;
        this.data = data;
        this.path = path;
        this.code = code;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
