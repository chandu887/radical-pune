package com.radical.lms.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.radical.lms.entity.UsersEntity;

public interface UserService {
	
	UsersEntity getUsers(String userId);
	
	UsersEntity checkLoginDetails(String userName, String passWord);
	
}
