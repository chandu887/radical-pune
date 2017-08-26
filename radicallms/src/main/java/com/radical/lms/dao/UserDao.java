package com.radical.lms.dao;

import java.util.List;

import com.radical.lms.beans.DashBoardForm;
import com.radical.lms.beans.LeadsEntityBean;
import com.radical.lms.entity.CourseCategeoryEntity;
import com.radical.lms.entity.CourseEntity;
import com.radical.lms.entity.LeadSourcesEntity;
import com.radical.lms.entity.LeadsEntity;
import com.radical.lms.entity.SendEmailEntity;
import com.radical.lms.entity.UsersEntity;

public interface UserDao {
	
	void saveOrUpdateUser(UsersEntity usersEntity);
	
	UsersEntity getUsers(int userId);

	UsersEntity getUserByUserName(String name);
	
	UsersEntity checkLoginDetails(String userName, String passWord);
	
	List<UsersEntity> getUsersList();

	List getCountByStatusType(DashBoardForm dashBoardForm);

	List<LeadsEntity> getLeadsByStatus(DashBoardForm dashBoardForm );
	
	List<CourseCategeoryEntity> getCourseCategories();
	
	List<CourseEntity> getCourses();
	
	List<CourseEntity> getCoursesList(DashBoardForm dashBoardForm, boolean flag);
	
	List<CourseCategeoryEntity> getCategoryList(DashBoardForm dashBoardForm, boolean flag);
	
	List<LeadSourcesEntity> getLeadSources();
	
	String leadsChangeStatus(List<Integer>changeStatusLeadIdsList,int statusType,String reason);
	
	String getAssignedToName(int assignedToId);
	
	List<LeadsEntity> getLeadsListForDownload(List<Integer> downloadLeadIdsList);
	
	List<CourseEntity> getCourseList(int intCategoryId);
	
	CourseEntity getCourseListBasedOnCourseId(int courseId);
	
	CourseCategeoryEntity getCategoryListBasedOnCourseId(int categoryId);
	
	void saveTemplate(CourseCategeoryEntity categoryEntity);
	
	void sendTemplatedEmail(SendEmailEntity sendEmailEntity);
	
	int getTemplatesCount(DashBoardForm dashBoardForm);
	
	List<CourseCategeoryEntity> getCourseCategoriesList();
	
	void saveCategory(CourseCategeoryEntity categoryEntity);
	
	CourseCategeoryEntity getCategoryByCategoryId(int categoryId);
	
	CourseCategeoryEntity getCategoryByCategoryName(String categoryName);
	
	List<CourseEntity> getCoursesList();
	
	void saveCourse(CourseEntity courseEntity);
	
	CourseEntity getCourseByCourseId(int courseId);
	
	CourseEntity getCourseByCourseName(String courseName);

}
