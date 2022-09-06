package com.blog.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
public class Blog {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int blogId;

    @NotEmpty
    @Size(min = 4, message = "min 4 character required" )
    private String title;

    @NotEmpty(message = "content can not be  null")
    @Column(length = 10000)
    private String content;

    private int userId;

    public Blog() {
    }

    public Blog(int blogId, String title, String content, int userId) {
        this.blogId = blogId;
        this.title = title;
        this.content = content;
        this.userId = userId;
    }


    public int getBlogId() {
        return blogId;
    }

    public void setBlogId(int blogId) {
        this.blogId = blogId;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "blog_id=" + blogId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", user_id='" + userId + '\'' +
                '}';
    }
}
