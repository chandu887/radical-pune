package com.radical.lms.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.radical.lms.entity.EmailTimeEntity;

public class EmailDaoImpl implements EmailDao{
	@Autowired
	private SessionFactory sessionFactory;
	
	@Transactional
	public long getLastEmailTimeInMillis() {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from EmailTimeEntity where id = :id");
		query.setInteger("id", 1);
		EmailTimeEntity emailTimeEntity = (EmailTimeEntity) query.uniqueResult();
		return emailTimeEntity.getLastEmailTime();		
	}
	
	@Transactional
	public void saveEmailTime(EmailTimeEntity emailTimeEntity) {
		Session session = this.sessionFactory.getCurrentSession();
		session.saveOrUpdate(emailTimeEntity);
	}

}
