package com.radical.lms.service;

import java.util.List;

import javax.mail.Store;

import org.springframework.web.multipart.MultipartFile;

import com.radical.lms.entity.SendEmailEntity;

public interface EmailService {
	
	void readMailInbox();
	
	Store getStore();
	
	boolean sendMail(String toMailId, String subject, String mailBody);
	
	boolean sendMailWithAttachement(String toMailId, String subject, String mailBody,MultipartFile file);
	
	List<SendEmailEntity> getEmailEntries();
	
	void updateEmailEntries(SendEmailEntity emailEntry);
}
