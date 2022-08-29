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


    private int userid;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FKEY_user_id")
    private User user;

    public Blog() {
    }

    public Blog(int blog_id, String title, String content, int userid) {
        this.blog_id = blog_id;
        this.title = title;
        this.content = content;
        this.userid = userid;
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

    public User getUser() {
        return user;
    }

    public void assignUser(User user){
        this.user = user;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "blog_id=" + blog_id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", user_id='" + userid + '\'' +
                '}';
    }
}
