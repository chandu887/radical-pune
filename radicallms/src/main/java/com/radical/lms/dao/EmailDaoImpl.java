package com.radical.lms.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.radical.lms.entity.EmailTimeEntity;
import com.radical.lms.entity.SendEmailEntity;

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

	@Transactional
	public List<SendEmailEntity> getEmailEntries() {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from SendEmailEntity where status = :status");
		query.setInteger("status", 0);
		return query.list();
	}
	
	@Transactional
	public void updateEmailEntries(SendEmailEntity emailEntry) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(emailEntry);
	}
}
