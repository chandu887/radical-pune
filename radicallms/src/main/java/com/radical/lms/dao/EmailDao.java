package com.radical.lms.dao;

import java.util.List;

import com.radical.lms.entity.EmailTimeEntity;
import com.radical.lms.entity.SendEmailEntity;;

public interface EmailDao {
	long getLastEmailTimeInMillis();
	
	void saveEmailTime(EmailTimeEntity emailTimeEntity);
	
	List<SendEmailEntity> getEmailEntries();
	
	void updateEmailEntries(SendEmailEntity emailEntry);
}
