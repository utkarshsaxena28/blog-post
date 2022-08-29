package com.blog.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.entity.Notification;
import com.blog.repositiory.NotifyRepo;

@Service
@Transactional
public class NotificationService {
	
	@Autowired
	private NotifyRepo notifyRepo;

	// Adding the employee or Posting the employee
	public Notification notificationAdded(Notification nty) {
		Notification added = notifyRepo.save(nty);
		return added;
	}

}
