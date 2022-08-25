package com.blog.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
public class Blog {

    @Id
    private int blog_id;

    @NotEmpty
    @Size(min = 4, message = "min 4 character required" )
    private String title;

    @NotEmpty
    @Column(length = 10000)
    private String content;

    @NotEmpty
    private String user_id;

    @ManyToOne
    @JoinColumn(name = "FKEY_user_id")
    private User user;

    public Blog() {
    }

    public Blog(int blog_id, String title, String content, String user_id) {
        this.blog_id = blog_id;
        this.title = title;
        this.content = content;
        this.user_id = user_id;
    }


    public int getBlog_id() {
        return blog_id;
    }

    public void setBlog_id(int blog_id) {
        this.blog_id = blog_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "blog_id=" + blog_id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", user_id='" + user_id + '\'' +
                '}';
    }
}
