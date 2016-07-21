package com.radical.lms.service;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radical.lms.dao.UserDao;
import com.radical.lms.entity.UsersEntity;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;

	private Properties properties = new Properties();

	@Transactional
	public UsersEntity getUsers(String userId) {
		return userDao.getUsers(userId);
	}
	
	@Transactional
	public UsersEntity checkLoginDetails(String userName, String passWord) {
		return userDao.checkLoginDetails(userName, passWord);
	}
}
