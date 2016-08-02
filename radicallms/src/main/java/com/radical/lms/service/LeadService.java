package com.radical.lms.service;

import com.radical.lms.entity.LeadsEntity;

public interface LeadService {
	
	void saveLead(LeadsEntity leadsEntity);
	
	LeadsEntity getLeadByLeadId(int leadId);
	
}
