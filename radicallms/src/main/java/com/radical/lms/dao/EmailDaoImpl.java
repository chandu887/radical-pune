package com.radical.lms.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.radical.lms.entity.EmailCountEntity;

public class EmailDaoImpl implements EmailDao{
	@Autowired
	private SessionFactory sessionFactory;
	
	@Transactional
	public int getLastEmailCount() {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from EmailCountEntity where id = :id");
		query.setInteger("id", 1);
		EmailCountEntity emailCountEntity = (EmailCountEntity) query.uniqueResult();
		return emailCountEntity.getLastEmailCount();		
	}
	
	@Transactional
	public void saveEmailCount(EmailCountEntity emailCountEntity) {
		Session session = this.sessionFactory.getCurrentSession();
		session.saveOrUpdate(emailCountEntity);
	}

}
