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
				.createQuery("select count(*) from LeadsEntity group by status");
		List countList = query.list();
		return countList;
	}

	public List<LeadsEntity> getLeadsByStatus(int status) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from LeadsEntity where status = :status");
		query.setInteger("status", status);
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
}
