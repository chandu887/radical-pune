package com.radical.lms.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.radical.lms.beans.DashBoardForm;
import com.radical.lms.beans.LeadsEntityBean;
import com.radical.lms.entity.CourseCategeoryEntity;
import com.radical.lms.entity.CourseEntity;
import com.radical.lms.entity.LeadSourcesEntity;
import com.radical.lms.entity.LeadsEntity;
import com.radical.lms.entity.SendEmailEntity;
import com.radical.lms.entity.UsersEntity;

@Repository
public class UserDaoImpl implements UserDao {
	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	public void saveOrUpdateUser(UsersEntity usersEntity) {
		Session session = this.sessionFactory.getCurrentSession();
		session.saveOrUpdate(usersEntity);
	}
	
	@Transactional
	public UsersEntity getUsers(int userId) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from UsersEntity where userId= :userId");
		query.setInteger("userId", userId);
		List<UsersEntity> userList = query.list();
		if (null != userList && !userList.isEmpty()) {
			return userList.get(0);
		}
		return null;
	}
	
	@Transactional
	public UsersEntity getUserByUserName(String name) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from UsersEntity where userName= :userName");
		query.setString("userName", name);
		List<UsersEntity> userList = query.list();
		if (null != userList && !userList.isEmpty()) {
			return userList.get(0);
		}
		return null;
	}

	@Transactional
	public UsersEntity checkLoginDetails(String userName, String passWord) {
		Query query = this.sessionFactory.getCurrentSession()
				.createQuery("from UsersEntity where userName= :userName and password= :password");
		query.setString("userName", userName);
		query.setString("password", passWord);
		UsersEntity user = (UsersEntity) query.uniqueResult();
		if (user != null) {
			return user;
		}
		return null;
	}
	
	@Transactional
	public List<UsersEntity> getUsersList() {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from UsersEntity where roleId = 1");
		return query.list();
	}
	
	@Transactional
	public List<UsersEntity> getAgentsList() {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from UsersEntity where roleId = 2");
		return query.list();
	}

	public List getCountByStatusType(DashBoardForm dashBoardForm) {
		String queryStr = "select status , count(*) from LeadsEntity ";
		queryStr += generateQueryString(dashBoardForm);
		queryStr += " group by status";
		Query query = this.sessionFactory.getCurrentSession()
				.createQuery(queryStr);
		List<Integer> currentStatusList = new ArrayList<Integer>();
		currentStatusList.add(1);
		currentStatusList.add(2);
		currentStatusList.add(3);
		if(dashBoardForm.getEmail() == null && dashBoardForm.getMobileNumber() == null){
			currentStatusList.add(4);
		}
		currentStatusList.add(5);
		query.setParameterList("status", currentStatusList);
		List countList = query.list();
		return countList;
	}
	
	private String generateQueryString(DashBoardForm dashBoardForm) {
		String queryStr = " where status in (:status)";
		if (dashBoardForm.getCourse() != 0) {
			queryStr += " and course = " + dashBoardForm.getCourse();
		} else if (dashBoardForm.getCategory() != 0){
			queryStr += " and courseCategeory = " + dashBoardForm.getCategory();
		}
		
		if(null != dashBoardForm.getModeOfTraining()){
			queryStr += " and modeofTraining = '"+dashBoardForm.getModeOfTraining()+"'";
		}
		
		/*if(dashBoardForm.getCurrentStatus() != 0 ){
			queryStr += " and status = '"+dashBoardForm.getCurrentStatus()+"'";
		}*/
		
		if(null != dashBoardForm.getTypeOfTraining()){
			queryStr += " and typeofTraining = '"+dashBoardForm.getTypeOfTraining()+"'";
		}
		if(dashBoardForm.getLeadSource()!=0){
			queryStr += " and leadSource = " + dashBoardForm.getLeadSource();
		}
		
		if(null != dashBoardForm.getLocation()){
			queryStr += " and location = '" + dashBoardForm.getLocation()+"'";
		}
		
		if(dashBoardForm.getAssignedTo()!=0){
			queryStr += " and assignedTo = " + dashBoardForm.getAssignedTo();
		}
		
		if(null != dashBoardForm.getLabels()){
			queryStr += " and labels = '"+dashBoardForm.getLabels()+"'";
		}
		
		if(dashBoardForm.getEmail()!=null){
			queryStr += " and emailId = '" + dashBoardForm.getEmail()+"'";
		}
		if(dashBoardForm.getMobileNumber()!=null){
			queryStr += " and mobileNo = " + dashBoardForm.getMobileNumber();
		}
		if(dashBoardForm.getLeadId()!=0){
			queryStr += " and leadid = " + dashBoardForm.getLeadId();
		}
		if(dashBoardForm.getName() != null){
			queryStr += " and name = '"+dashBoardForm.getName()+"'";
		}
		if (dashBoardForm.getFromDate() != null && !dashBoardForm.getFromDate().equalsIgnoreCase("")
				&& dashBoardForm.getToDate() != null && !dashBoardForm.getToDate().equalsIgnoreCase("")) {
			try {
			 Date fromDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(dashBoardForm.getFromDate()+" 00:00:00");
			 Date toDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(dashBoardForm.getToDate()+" 23:59:59");
			 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			 queryStr += " and createdDate BETWEEN '" + formatter.format(fromDate) + "' AND '" + formatter.format(toDate)+"'";
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return queryStr;
	}

	public List<LeadsEntity> getLeadsByStatus(DashBoardForm dashBoardForm) {
		String queryStr = "from LeadsEntity ";
		queryStr += generateQueryString(dashBoardForm);
		List<Integer> currentStatusList = new ArrayList<Integer>();
		if (dashBoardForm.getCurrentStatus() != 0) {
			currentStatusList.add(dashBoardForm.getCurrentStatus());
		} else {
			currentStatusList.add(1);
			currentStatusList.add(2);
			currentStatusList.add(3);
			if(dashBoardForm.getEmail() == null && dashBoardForm.getMobileNumber() == null){
				
				currentStatusList.add(4);
			}
			currentStatusList.add(5);
		}
		
		queryStr += " order by createdDate desc";
		Query query = this.sessionFactory.getCurrentSession().createQuery(queryStr);
		query.setParameterList("status", currentStatusList);
		if(dashBoardForm.getFilterType() !=1 ){
			query.setFirstResult(dashBoardForm.getStartLimit()-1);
			query.setMaxResults(dashBoardForm.getPageLimit());
		}
		List<LeadsEntity> leads = query.list();
		if (leads != null && !leads.isEmpty()) {
			return leads;
		}
		return null;
	}

	@Transactional
	public List<CourseCategeoryEntity> getCourseCategories() {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from CourseCategeoryEntity where isActive=1");
		List<CourseCategeoryEntity> courseCategeory = query.list();
		if (courseCategeory != null && !courseCategeory.isEmpty()) {
			return courseCategeory;
		}
		return null;
	}

	@Transactional
	public List<CourseEntity> getCourses() {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from CourseEntity where isActive=1");
		List<CourseEntity> courseList = query.list();
		if (courseList != null && !courseList.isEmpty()) {
			return courseList;
		}
		return null;
	}
	
	@Transactional
	public List<CourseEntity> getCoursesList(DashBoardForm dashBoardForm , boolean flag) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from CourseEntity where subject IS NOT NULL and isActive=1");
		if (flag) {			
			query.setFirstResult(dashBoardForm.getStartLimit()-1);
			query.setMaxResults(dashBoardForm.getPageLimit());
		}
		List<CourseEntity> courseList = query.list();
		if (courseList != null && !courseList.isEmpty()) {
			return courseList;
		}
		return null;
	}
	
	@Transactional
	public List<CourseCategeoryEntity> getCategoryList(DashBoardForm dashBoardForm , boolean flag) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from CourseCategeoryEntity where mailerPath IS NOT NULL and isActive=1");
		if (flag) {			
			query.setFirstResult(dashBoardForm.getStartLimit()-1);
			query.setMaxResults(dashBoardForm.getPageLimit());
		}
		List<CourseCategeoryEntity> categoryList = query.list();
		if (categoryList != null && !categoryList.isEmpty()) {
			return categoryList;
		}
		return null;
	}

	@Transactional
	public List<LeadSourcesEntity> getLeadSources() {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from LeadSourcesEntity");
		List<LeadSourcesEntity> leadSourcesEntityList = query.list();
		if (leadSourcesEntityList != null && !leadSourcesEntityList.isEmpty()) {
			return leadSourcesEntityList;
		}
		return null;
	}

	public String leadsChangeStatus(List<Integer> changeStatusLeadIdsList, int statusType, String reason) {
		Query query = this.sessionFactory.getCurrentSession().createQuery(
				"update LeadsEntity set status=:status ,reason=:reason where leadiId in (:changeStatusLeadIdsList)");
		query.setInteger("status", statusType);
		query.setString("reason", reason);
		query.setParameterList("changeStatusLeadIdsList", changeStatusLeadIdsList);
		Integer changeStatusResult = query.executeUpdate();
		String result = "";
		if (changeStatusResult > 0) {
			result = "Leads Status Changed Successfully.";
		} else {
			result = "Leads Status Change failed.";
		}

		// TODO Auto-generated method stub
		return null;
	}

	public String getAssignedToName(int assignedToId) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("select userName from UsersEntity where userId= :userId");
		query.setInteger("userId", assignedToId);
		List<String> userIdList = query.list();
		if (null != userIdList && !userIdList.isEmpty()) {
			return userIdList.get(0);
		}
		return null;
	}

	@Transactional
	public List getAllUsersList() {
		String query = "select userid, username from users";
		Query sqlQuery = this.sessionFactory.getCurrentSession().createSQLQuery(query);
		return sqlQuery.list();
	}
	
	public List<LeadsEntity> getLeadsListForDownload(List<Integer> downloadLeadIdsList) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from LeadsEntity where leadiId in (:downloadLeadIdsList)");
		query.setParameterList("downloadLeadIdsList", downloadLeadIdsList);
		List<LeadsEntity> usersList = query.list(); 
		return usersList;
	}

	public List getCourseList(int categoryId) {
		/*Query query = this.sessionFactory.getCurrentSession().createQuery("from CourseEntity where categeoryId=:categeoryId and isActive=1");
		query.setInteger("categeoryId", categoryId);
		List<CourseEntity> courseList = query.list(); 
		return courseList;*/
		
		String query = "select courseid, coursename from courses where categoryid= :categoryid";
		Query sqlQuery = this.sessionFactory.getCurrentSession().createSQLQuery(query);
		sqlQuery.setParameter("categoryid", categoryId);
		return sqlQuery.list();

	}
	
	public CourseEntity getCourseListBasedOnCourseId(int courseId) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from CourseEntity where courseId=:courseId");
		query.setInteger("courseId", courseId);
		CourseEntity courseList = (CourseEntity) query.uniqueResult(); 
		return courseList;
	}
	
	public CourseCategeoryEntity getCategoryListBasedOnCourseId(int categoryId) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from CourseCategeoryEntity where categoryId=:categoryId and isActive=1");
		query.setInteger("categoryId", categoryId);
		CourseCategeoryEntity categoryList = (CourseCategeoryEntity) query.uniqueResult(); 
		return categoryList;
	}
	
	@Transactional
	public void saveTemplate(CourseCategeoryEntity categoryEntity) {
		Session session = this.sessionFactory.getCurrentSession();
		session.saveOrUpdate(categoryEntity);
	}
	
	@Transactional
	public void sendTemplatedEmail(SendEmailEntity sendEmailEntity) {
		Session session = this.sessionFactory.getCurrentSession();
		session.save(sendEmailEntity);
	}
	
	@Transactional
	public int getTemplatesCount(DashBoardForm dashBoardForm) {
				
		String query = "select count(*) from CourseCategeoryEntity where mailerPath IS NOT NULL and isActive=1";
		Query sqlQuery = this.sessionFactory.getCurrentSession().createQuery(query);
		List result = sqlQuery.list();
		long count = 0;
		if (null != result && !result.isEmpty()) {
			count = (Long) result.get(0);
		}
		return (int)count;
		
	}
	
	@Transactional
	public List<CourseCategeoryEntity> getCourseCategoriesList() {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from CourseCategeoryEntity");
		List<CourseCategeoryEntity> courseCategeory = query.list();
		if (courseCategeory != null && !courseCategeory.isEmpty()) {
			return courseCategeory;
		}
		return null;
	}
	
	@Transactional
	public void saveCategory(CourseCategeoryEntity categoryEntity) {
		Session session = this.sessionFactory.getCurrentSession();
		session.saveOrUpdate(categoryEntity);
	}
	
	@Transactional
	public CourseCategeoryEntity getCategoryByCategoryId(int categoryId) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from CourseCategeoryEntity where categoryId=:categoryId");
		query.setInteger("categoryId", categoryId);
		CourseCategeoryEntity categoryList = (CourseCategeoryEntity) query.uniqueResult(); 
		return categoryList;
	}
	
	@Transactional
	public CourseCategeoryEntity getCategoryByCategoryName(String categoryName) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from CourseCategeoryEntity where categeoryName=:categeoryName");
		query.setString("categeoryName", categoryName);
		CourseCategeoryEntity category = (CourseCategeoryEntity) query.uniqueResult(); 
		return category;
	}
	
	@Transactional
	public List<CourseEntity> getCoursesList() {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from CourseEntity");
		List<CourseEntity> courses= query.list();
		if (courses != null && !courses.isEmpty()) {
			return courses;
		}
		return null;
	}
	
	@Transactional
	public List<CourseEntity> getCoursesListForEmailer() {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from CourseEntity where mailerPath IS NOT NULL and isActive=1");
		List<CourseEntity> courses= query.list();
		if (courses != null && !courses.isEmpty()) {
			return courses;
		}
		return null;
	}
	
	@Transactional
	public void saveCourse(CourseEntity courseEntity) {
		Session session = this.sessionFactory.getCurrentSession();
		session.saveOrUpdate(courseEntity);
	}
	
	@Transactional
	public CourseEntity getCourseByCourseId(int courseId) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from CourseEntity where courseId=:courseId and isActive=1");
		query.setInteger("courseId", courseId);
		CourseEntity course = (CourseEntity) query.uniqueResult(); 
		return course;
	}
	
	@Transactional
	public CourseEntity getCourseByCourseName(String courseName) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from CourseEntity where courseName=:courseName");
		query.setString("courseName", courseName);
		CourseEntity course = (CourseEntity) query.uniqueResult(); 
		return course;
	}
}
