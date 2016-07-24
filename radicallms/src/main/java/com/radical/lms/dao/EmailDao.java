package com.radical.lms.dao;

import com.radical.lms.entity.EmailCountEntity;

public interface EmailDao {
	int getLastEmailCount();
	void saveEmailCount(EmailCountEntity emailCountEntity);
}
