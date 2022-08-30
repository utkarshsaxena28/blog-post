package com.blog.http;

import com.blog.entity.Blog;

import java.util.List;

public class Request {

    private Blog blog;

    private int userId;

    public Request() {

    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
