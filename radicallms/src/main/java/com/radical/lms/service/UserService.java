package com.radical.lms.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.radical.lms.beans.DashBoardForm;
import com.radical.lms.beans.LeadsEntityBean;
import com.radical.lms.entity.CourseCategeoryEntity;
import com.radical.lms.entity.CourseEntity;
import com.radical.lms.entity.LeadsEntity;
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
	
	String leadsChangeStatus(List<Integer>changeStatusLeadIdsList,int statusType,String reason);
	
	XSSFWorkbook  downloadLeadsSheet(List<LeadsEntityBean> leadsEntityBeanList);
	
	List<LeadsEntityBean> getLeadsListForDownload(List<Integer> downloadLeadIdsList);
	
	List<CourseEntity> getCourseList(int intCategoryId);
	
}
