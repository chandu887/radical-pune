package com.radical.lms.service;

import java.util.List;

import javax.mail.Store;

import com.radical.lms.entity.SendEmailEntity;

public interface EmailService {
	
	void readMailInbox();
	
	Store getStore();
	
	boolean sendMail(String toMailId, String subject, String mailBody);
	
	List<SendEmailEntity> getEmailEntries();
	
	void updateEmailEntries(SendEmailEntity emailEntry);
}
