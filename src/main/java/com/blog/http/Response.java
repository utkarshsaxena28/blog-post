package com.blog.http;

import com.blog.entity.Blog;

import java.util.List;

public class Response {

    private String message;
    private Blog blog;

    private List<Blog> list;

    public Response() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public List<Blog> getList() {
        return list;
    }

    public void setList(List<Blog> list) {
        this.list = list;
    }

}

