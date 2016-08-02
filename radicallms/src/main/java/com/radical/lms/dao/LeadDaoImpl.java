package com.radical.lms.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.radical.lms.entity.LeadsEntity;
import com.radical.lms.entity.UsersEntity;

@Repository
public class LeadDaoImpl implements LeadDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Transactional
	public void saveLead(LeadsEntity leadsEntity) {
		Session session = this.sessionFactory.getCurrentSession();
		session.saveOrUpdate(leadsEntity);
	}
	
	@Transactional
	public LeadsEntity getLeadByLeadId(int leadId) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from LeadsEntity where leadiId= :leadiId");
		query.setInteger("leadiId", leadId);
		LeadsEntity lead = (LeadsEntity) query.uniqueResult();
		return lead;
	}

}
