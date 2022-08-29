package com.blog.kafka;

import com.blog.entity.Notification;
import com.blog.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;



@Service
public class KafkaConsumer {

	private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
	
	@Autowired
	private NotificationService notificationService;
	
	@KafkaListener(topics = "TopicBlog", groupId = "myGroup1")
	public Notification sendMessage(String message) {
		logger.info(String.format("******************** Message sent %s *************************", message));
		Notification notice = new Notification(message);
		notificationService.notificationAdded(notice);
		return notice;
	}
}
