package com.blog.repositiory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.entity.Notification;

public interface NotifyRepo extends JpaRepository<Notification, Integer> {
	
}
