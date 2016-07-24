package com.radical.lms.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.radical.lms.entity.LeadsEntity;

@Repository
public class LeadDaoImpl implements LeadDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Transactional
	public void saveLead(LeadsEntity leadsEntity) {
		Session session = this.sessionFactory.getCurrentSession();
		session.saveOrUpdate(leadsEntity);
	}

}
