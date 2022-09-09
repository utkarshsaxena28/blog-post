package com.blog.http;

import com.blog.entity.Blog;

import javax.validation.Valid;
import java.util.List;

public class Request {

    @Valid
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
