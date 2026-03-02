package com.message.central.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.message.central.entities.User;

@Service
public class EmailService {
	
	@Autowired
	JavaMailSender javaMail;
	
	@Value("${spring.mail.username}")
	private String from;
	
	public void sendWelcomeEmail(User user) {
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setFrom(from);
		message.setTo(user.getEmail());
		message.setSubject("Welcome to Message Central");
		message.setText(
				"Hello " + user.getName() + ",\n\n" +
	            "Your account has been successfully created!\n\n" +
	            "We're glad to have you here.\n\n" +
	            "Message Central"
	        );
		
		javaMail.send(message);
	}
}
