package com.blog.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;



@Service
public class KafkaConsumer {
/*
	private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
	
	@Autowired
	private NotificationService empHstryService;
	
	@KafkaListener(topics = "topic3", groupId = "myGroup")
	public EmployeeHistory sendMessage(String message) {
		logger.info(String.format("Message sent %s", message));
		NotificationService empH = new NotificationService(message);
		empHstryService.empAdded(empH);
		return empH;
	}*/
}
