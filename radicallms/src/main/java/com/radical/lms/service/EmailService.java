package com.radical.lms.service;

import javax.mail.Store;

public interface EmailService {
	
	void readMailInbox();
	
	Store getStore();
}
