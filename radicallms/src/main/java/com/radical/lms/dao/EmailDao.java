package com.radical.lms.dao;

import com.radical.lms.entity.EmailTimeEntity;;

public interface EmailDao {
	long getLastEmailTimeInMillis();
	void saveEmailTime(EmailTimeEntity emailTimeEntity);
}
