package com.radical.lms.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.radical.lms.dao.LeadDao;
import com.radical.lms.entity.LeadsEntity;

public class LeadServiceImpl implements LeadService {

	@Autowired
	LeadDao leadDao;
	
	public void saveLead(LeadsEntity leadsEntity) {
		leadDao.saveLead(leadsEntity);
	}
	
}
