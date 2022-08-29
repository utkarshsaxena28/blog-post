package com.blog.repositiory;

import com.blog.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.entity.Notification;
import org.springframework.stereotype.Component;

@Component
public interface NotifyRepo extends JpaRepository<Notification, Integer> {
}
