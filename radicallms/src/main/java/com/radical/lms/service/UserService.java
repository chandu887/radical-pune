package com.radical.lms.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.radical.lms.beans.DashBoardForm;
import com.radical.lms.beans.LeadsEntityBean;
import com.radical.lms.entity.CourseEntity;
import com.radical.lms.entity.LeadsEntity;
import com.radical.lms.entity.SendEmailEntity;
import com.radical.lms.entity.UsersEntity;

public interface UserService {
	
	void loadCache();

	UsersEntity getUsers(String userId);

	UsersEntity checkLoginDetails(String userName, String passWord);

	List getCountByStatusType(DashBoardForm dashBoardForm);

	List<LeadsEntityBean> getLeadsStatus(DashBoardForm dashBoardForm );

	void getAllCourseCategories();
	
	Map<Integer, String> getLeadSourceMapping();
	
	Map<Integer, Integer> getCoursesCategeoryMapping();
	
	Map<Integer, String> getCourses();
	
	Map<Integer, String> getCourseCategories();
	
	Map<String, Integer> getCategoryNameIdMapping();
	
	Map<String, Integer> getCourseNameIdMapping();
	
	String leadsChangeStatus(List<Integer>changeStatusLeadIdsList,int statusType,String reason);
	
	XSSFWorkbook  downloadLeadsSheet(List<LeadsEntityBean> leadsEntityBeanList);
	
	List<LeadsEntityBean> getLeadsListForDownload(List<Integer> downloadLeadIdsList);
	
	List<CourseEntity> getCourseList(int intCategoryId);
	
	CourseEntity getCourseListBasedOnCourseId(int courseId);
	
	void saveTemplate(CourseEntity courseEntity);
	
	void sendMail(String mailId);
	
	void sendTemplatedEmail(SendEmailEntity sendEmailEntity);
	
	void sendSms(String sms,String mobileNumber);
	
}
