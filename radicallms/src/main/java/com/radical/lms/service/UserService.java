package com.radical.lms.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.radical.lms.beans.CourseBean;
import com.radical.lms.beans.DashBoardForm;
import com.radical.lms.beans.LeadsEntityBean;
import com.radical.lms.beans.MailTemplateBean;
import com.radical.lms.entity.CourseCategeoryEntity;
import com.radical.lms.entity.CourseEntity;
import com.radical.lms.entity.LeadsEntity;
import com.radical.lms.entity.SendEmailEntity;
import com.radical.lms.entity.UsersEntity;

public interface UserService {
	
	void loadCache();
	
	void saveOrUpdateUser(UsersEntity usersEntity);

	UsersEntity getUsers(int userId);
	
	UsersEntity getUserByUserName(String name);

	UsersEntity checkLoginDetails(String userName, String passWord);
	
	List<UsersEntity> getUsersList();
	
	List<UsersEntity> getAgentsList();
	
	List getCountByStatusType(DashBoardForm dashBoardForm);

	List<LeadsEntity> getLeadsStatus(DashBoardForm dashBoardForm );

	void getAllCourseCategories();
	
	void getAllCourses();
	
	Map<Integer, String> getLeadSourceMapping();
	
	Map<Integer, Integer> getCoursesCategeoryMapping();
	
	Map<Integer, String> getCourses();
	
	Map<Integer, String> getCourseCategories();
	
	Map<String, Integer> getCategoryNameIdMapping();
	
	Map<String, Integer> getCourseNameIdMapping();
	
	String leadsChangeStatus(List<Integer>changeStatusLeadIdsList,int statusType,String reason);
	
	XSSFWorkbook  downloadLeadsSheet(List<LeadsEntity> leadsEntityBeanList);
	
	List<LeadsEntity> getLeadsListForDownload(List<Integer> downloadLeadIdsList);
	
	List<CourseEntity> getCourseList(int intCategoryId);
	
	CourseEntity getCourseListBasedOnCourseId(int courseId);
	
	CourseCategeoryEntity getCategoryListBasedOnCourseId(int categoryId);
	
	void saveTemplate(CourseCategeoryEntity category);
	
	List<MailTemplateBean> getMailTemplateList(DashBoardForm dashBoardForm);
	
	List<CourseCategeoryEntity> getCategoryList(DashBoardForm dashBoardForm);
	
	int getTemplatesCount(DashBoardForm dashBoardForm);
	
	void sendTemplatedEmail(SendEmailEntity sendEmailEntity);
	
	void sendSms(String sms,String mobileNumber);
	
	List<CourseCategeoryEntity> getCourseCategoriesList();
	
	void saveCategory(CourseCategeoryEntity categoryEntity);
	
	CourseCategeoryEntity getCategoryByCategoryId(int categoryId);
	
	CourseCategeoryEntity getCategoryByCategoryName(String categoryName);
	
	List<CourseEntity> getCoursesList();
	
	List<CourseEntity> getCoursesListForEmailer();
	
	void saveCourse(CourseEntity courseEntity);
	
	CourseEntity getCourseByCourseId(int courseId);
	
	CourseEntity getCourseByCourseName(String courseName);
	
	List<CourseBean> populateCourses(List<CourseEntity> coursesList);
	
	void processUploadBulkLeadsFile(Iterator<Row> rowIterator,int statusColumnIndex);
	
	void downloadXlsFileBasedOnFileName(String filePath,HttpServletResponse response);
	
	void downloadCourseFile(int courseId,HttpServletResponse response);
	
	Map<Integer, String> getStatusMap();
	
	CourseEntity getCourseDataByCourseId(int courseId);
	
}
