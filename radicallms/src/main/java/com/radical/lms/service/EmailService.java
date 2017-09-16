package com.radical.lms.service;

import java.util.List;
import java.util.Properties;

import javax.mail.Store;

import org.springframework.web.multipart.MultipartFile;

import com.radical.lms.entity.CourseEntity;
import com.radical.lms.entity.SendEmailEntity;

public interface EmailService {
	
	void readMailInbox();
	
	Store getStore();
	
	boolean sendMail(String toMailId, String subject, String mailBody);
	
	boolean sendMailWithAttachementDynamically(String toMailId, String subject, String mailBody, CourseEntity courseEntity );
	
	boolean sendMailWithAttachementManually(String toMailId, String subject, String mailBody,MultipartFile file);
	
	List<SendEmailEntity> getEmailEntries();
	
	void updateEmailEntries(SendEmailEntity emailEntry);
	
	Properties getProperties();
}
