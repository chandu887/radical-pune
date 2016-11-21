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

	public UsersEntity getUsers(String userId) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from UsersEntity where userId= :userId");
		query.setString("userId", userId);
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
		currentStatusList.add(4);
		query.setParameterList("status", currentStatusList);
		List countList = query.list();
		return countList;
	}
	
	private String generateQueryString(DashBoardForm dashBoardForm) {
		String queryStr = " where status in (:status)";
		if (dashBoardForm.getCategory()!=0){
			queryStr += " and courseCategeory = " + dashBoardForm.getCategory();
		}
		if (dashBoardForm.getCourse() != 0) {
			queryStr += " and course = " + dashBoardForm.getCourse();
		}
		if(dashBoardForm.getEmail()!=null){
			queryStr += " and emailId = '" + dashBoardForm.getEmail()+"'";
		}
		if(dashBoardForm.getMobileNumber()!=null){
			queryStr += " and mobileNo = " + dashBoardForm.getMobileNumber();
		}
		if (dashBoardForm.getFromDate() != null && !dashBoardForm.getFromDate().equalsIgnoreCase("")
				&& dashBoardForm.getToDate() != null && !dashBoardForm.getToDate().equalsIgnoreCase("")) {
			try {
			 Date fromDate = new SimpleDateFormat("MM/dd/yyyy").parse(dashBoardForm.getFromDate());
			 Date toDate = new SimpleDateFormat("MM/dd/yyyy").parse(dashBoardForm.getToDate());
		    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			queryStr += " and createdDate BETWEEN '" + formatter.format(fromDate+" 00:00:00") + "' AND '" + formatter.format(toDate+" 23:59:59")+"'";
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
			currentStatusList.add(4);
		}
		
		queryStr += " order by leadiId desc";
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
		Query query = this.sessionFactory.getCurrentSession().createQuery("from CourseCategeoryEntity");
		List<CourseCategeoryEntity> courseCategeory = query.list();
		if (courseCategeory != null && !courseCategeory.isEmpty()) {
			return courseCategeory;
		}
		return null;
	}

	@Transactional
	public List<CourseEntity> getCourses() {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from CourseEntity");
		List<CourseEntity> courseList = query.list();
		if (courseList != null && !courseList.isEmpty()) {
			return courseList;
		}
		return null;
	}
	
	@Transactional
	public List<CourseEntity> getCoursesList(DashBoardForm dashBoardForm) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from CourseEntity where subject IS NOT NULL");
		query.setFirstResult(dashBoardForm.getStartLimit()-1);
		query.setMaxResults(dashBoardForm.getPageLimit());
		List<CourseEntity> courseList = query.list();
		if (courseList != null && !courseList.isEmpty()) {
			return courseList;
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

	public List<LeadsEntity> getLeadsListForDownload(List<Integer> downloadLeadIdsList) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from LeadsEntity where leadiId in (:downloadLeadIdsList)");
		query.setParameterList("downloadLeadIdsList", downloadLeadIdsList);
		List<LeadsEntity> usersList = query.list(); 
		return usersList;
	}

	public List<CourseEntity> getCourseList(int categoryId) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from CourseEntity where categeoryId=:categeoryId");
		query.setInteger("categeoryId", categoryId);
		List<CourseEntity> courseList = query.list(); 
		return courseList;
	}
	
	public CourseEntity getCourseListBasedOnCourseId(int courseId) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from CourseEntity where courseId=:courseId");
		query.setInteger("courseId", courseId);
		CourseEntity courseList = (CourseEntity) query.uniqueResult(); 
		return courseList;
	}
	
	@Transactional
	public void saveTemplate(CourseEntity courseEntity) {
		Session session = this.sessionFactory.getCurrentSession();
		session.saveOrUpdate(courseEntity);
	}
	
	@Transactional
	public void sendTemplatedEmail(SendEmailEntity sendEmailEntity) {
		Session session = this.sessionFactory.getCurrentSession();
		session.save(sendEmailEntity);
	}
	
	@Transactional
	public int getTemplatesCount(DashBoardForm dashBoardForm) {
				
		String query = "select count(*) from CourseEntity where subject IS NOT NULL";
		Query sqlQuery = this.sessionFactory.getCurrentSession().createQuery(query);
		List result = sqlQuery.list();
		long count = 0;
		if (null != result && !result.isEmpty()) {
			count = (Long) result.get(0);
		}
		return (int)count;
		
	}
}
