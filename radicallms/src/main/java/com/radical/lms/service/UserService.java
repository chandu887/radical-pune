package com.radical.lms.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.radical.lms.beans.LeadsEntityBean;
import com.radical.lms.entity.CourseCategeoryEntity;
import com.radical.lms.entity.LeadsEntity;
import com.radical.lms.entity.UsersEntity;

public interface UserService {
	
	void loadCache();

	UsersEntity getUsers(String userId);

	UsersEntity checkLoginDetails(String userName, String passWord);

	List getCountByStatusType();

	List<LeadsEntityBean> getLeadsByStatus(int status);

	void getAllCourseCategories();
	
	Map<Integer, String> getLeadSourceMapping();
	
	Map<Integer, Integer> getCoursesCategeoryMapping();
	
	Map<Integer, String> getCourses();
	
	Map<Integer, String> getCourseCategories();
	
	

}
