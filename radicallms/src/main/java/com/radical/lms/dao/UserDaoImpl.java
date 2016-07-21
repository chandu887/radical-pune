package com.radical.lms.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
		Query query = this.sessionFactory.getCurrentSession().createQuery("from UsersEntity where userName= :userName and password= :password");
		query.setString("userName", userName);
		query.setString("password", passWord);
		UsersEntity user = (UsersEntity) query.uniqueResult();
		if (user != null) {
			return user;
		}
		return null;
	}
}
