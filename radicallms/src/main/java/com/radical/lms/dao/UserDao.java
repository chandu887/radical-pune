package com.radical.lms.dao;

import com.radical.lms.entity.UsersEntity;

public interface UserDao {
	UsersEntity getUsers(String userId);
	
	UsersEntity checkLoginDetails(String userName, String passWord);
}
