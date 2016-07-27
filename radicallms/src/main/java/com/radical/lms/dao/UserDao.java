package com.radical.lms.dao;

import java.util.List;

import com.radical.lms.entity.CourseCategeoryEntity;
import com.radical.lms.entity.CourseEntity;
import com.radical.lms.entity.LeadSourcesEntity;
import com.radical.lms.entity.LeadsEntity;
import com.radical.lms.entity.UsersEntity;

public interface UserDao {
	
	UsersEntity getUsers(String userId);

	UsersEntity checkLoginDetails(String userName, String passWord);

	List getCountByStatusType();

	List<LeadsEntity> getLeadsByStatus(int status);
	
	List<CourseCategeoryEntity> getCourseCategories();
	
	List<CourseEntity> getCourses();
	
	List<LeadSourcesEntity> getLeadSources();
}