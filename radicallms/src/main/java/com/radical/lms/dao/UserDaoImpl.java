package com.radical.lms.dao;

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
import com.radical.lms.entity.CourseCategeoryEntity;
import com.radical.lms.entity.CourseEntity;
import com.radical.lms.entity.LeadSourcesEntity;
import com.radical.lms.entity.LeadsEntity;
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

	public List getCountByStatusType() {
		Query query = this.sessionFactory.getCurrentSession()
				.createQuery("select status , count(*) from LeadsEntity group by status");
		List countList = query.list();
		return countList;
	}

	public List<LeadsEntity> getLeadsByStatus(DashBoardForm dashBoardForm) {
		String queryStr = "from LeadsEntity where status in (:status)";
		List<Integer> currentStatusList = new ArrayList<Integer>();
		if (dashBoardForm.getCurrentStatus() != 0) {
			currentStatusList.add(dashBoardForm.getCurrentStatus());
		} else {
			currentStatusList.add(0);
			currentStatusList.add(1);
			currentStatusList.add(2);
			currentStatusList.add(3);
		}
		if (dashBoardForm.getCourse() != 0) {
			queryStr += " and course = " + dashBoardForm.getCourse();
		}
		if (dashBoardForm.getFromDate() != null && !dashBoardForm.getFromDate().equalsIgnoreCase("")
				&& dashBoardForm.getToDate() != null && !dashBoardForm.getToDate().equalsIgnoreCase("")) {
			queryStr += " and createdDate BETWEEN '" + dashBoardForm.getFromDate() + "' AND '" + dashBoardForm.getToDate()+"'";
		}
		queryStr += " order by leadiId desc";
		Query query = this.sessionFactory.getCurrentSession().createQuery(queryStr);
		query.setParameterList("status", currentStatusList);
		query.setFirstResult(dashBoardForm.getStartLimit()-1);
		query.setMaxResults(dashBoardForm.getPageLimit());
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
}
