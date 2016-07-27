package com.radical.lms.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radical.lms.beans.LeadsEntityBean;
import com.radical.lms.dao.UserDao;
import com.radical.lms.entity.CourseCategeoryEntity;
import com.radical.lms.entity.CourseEntity;
import com.radical.lms.entity.LeadSourcesEntity;
import com.radical.lms.entity.LeadsEntity;
import com.radical.lms.entity.UsersEntity;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;

	private Properties properties = new Properties();

	private Map<Integer, String> courseCategories = new ConcurrentHashMap<Integer, String>();

	private Map<Integer, String> courses = new ConcurrentHashMap<Integer, String>();

	private Map<Integer, Integer> coursesCategeoryMapping = new ConcurrentHashMap<Integer, Integer>();

	private Map<Integer, String> leadSourceMapping = new ConcurrentHashMap<Integer, String>();

	public void init() {
		loadCache();
	}

	@Transactional
	public void loadCache() {
		getAllCourseCategories();
		getAllCourses();
		getLeadSources();
	}

	@Transactional
	public void getLeadSources() {
		List<LeadSourcesEntity> leadSourcesEntityList = userDao.getLeadSources();
		Map<Integer, String> leadSourceMapping = new ConcurrentHashMap<Integer, String>();
		for (LeadSourcesEntity leadSourcesEntity : leadSourcesEntityList) {
			leadSourceMapping.put(leadSourcesEntity.getLeadsourceId(), leadSourcesEntity.getLeadSourceName());
		}
		setLeadSourceMapping(leadSourceMapping);
	}

	@Transactional
	public void getAllCourses() {
		List<CourseEntity> courseList = userDao.getCourses();
		Map<Integer, String> courses = new ConcurrentHashMap<Integer, String>();
		Map<Integer, Integer> coursesCategeoryMapping = new ConcurrentHashMap<Integer, Integer>();
		for (CourseEntity courseEntity : courseList) {
			courses.put(courseEntity.getCourseId(), courseEntity.getCourseName());
			coursesCategeoryMapping.put(courseEntity.getCourseId(), courseEntity.getCategeoryId());
		}
		setCourses(courses);
		setCoursesCategeoryMapping(coursesCategeoryMapping);
	}

	@Transactional
	public void getAllCourseCategories() {
		List<CourseCategeoryEntity> courseCategeories = userDao.getCourseCategories();
		Map<Integer, String> courseCategories = new ConcurrentHashMap<Integer, String>();
		for (CourseCategeoryEntity courseCategeoryEntity : courseCategeories) {
			courseCategories.put(courseCategeoryEntity.getCategoryId(), courseCategeoryEntity.getCategeoryName());
		}
		setCourseCategories(courseCategories);
	}

	@Transactional
	public UsersEntity getUsers(String userId) {
		return userDao.getUsers(userId);
	}

	@Transactional
	public UsersEntity checkLoginDetails(String userName, String passWord) {
		return userDao.checkLoginDetails(userName, passWord);
	}

	@Transactional
	public List getCountByStatusType() {
		return userDao.getCountByStatusType();
	}

	@Transactional
	public List<LeadsEntityBean> getLeadsByStatus(int statusType) {
		List<LeadsEntity> leads = userDao.getLeadsByStatus(statusType);
		if(leads!=null){
		List<LeadsEntityBean> leadBeanList = new ArrayList<LeadsEntityBean>();
		String status = "";
		for (LeadsEntity leadsEntity : leads) {
			if (leadsEntity.getStatus() == 1) {
				status = "New";
			} else if (leadsEntity.getStatus() == 2) {
				status = "Open";
			} else if (leadsEntity.getStatus() == 3){
				status = "Closed";
			}
			LeadsEntityBean leadsEntityBean = new LeadsEntityBean(status, leadsEntity.getName(),
					leadsEntity.getMobileNo(), leadsEntity.getLeadiId(), getCourses().get(leadsEntity.getCourse()),
					getCourseCategories().get(leadsEntity.getCourseCategeory()),
					getLeadSourceMapping().get(leadsEntity.getLeadSource()),
					Integer.toString(leadsEntity.getAssignedTo()), leadsEntity.getCreatedDate());
			leadBeanList.add(leadsEntityBean);
		}
		return leadBeanList;
		}
		return null;
	}

	public Map<Integer, String> getCourseCategories() {
		return courseCategories;
	}

	public void setCourseCategories(Map<Integer, String> courseCategories) {
		this.courseCategories = courseCategories;
	}

	public Map<Integer, String> getCourses() {
		return courses;
	}

	public void setCourses(Map<Integer, String> courses) {
		this.courses = courses;
	}

	public Map<Integer, Integer> getCoursesCategeoryMapping() {
		return coursesCategeoryMapping;
	}

	public void setCoursesCategeoryMapping(Map<Integer, Integer> coursesCategeoryMapping) {
		this.coursesCategeoryMapping = coursesCategeoryMapping;
	}

	public Map<Integer, String> getLeadSourceMapping() {
		return leadSourceMapping;
	}

	public void setLeadSourceMapping(Map<Integer, String> leadSourceMapping) {
		this.leadSourceMapping = leadSourceMapping;
	}
}
