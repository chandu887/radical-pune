package com.radical.lms.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.radical.lms.dao.LeadDao;
import com.radical.lms.entity.LeadsEntity;

public class LeadServiceImpl implements LeadService {

	@Autowired
	LeadDao leadDao;
	
	@Autowired
	UserService userService;
	
	public void saveLead(LeadsEntity leadsEntity) {
		leadDao.saveLead(leadsEntity);
	}
	
	public LeadsEntity getLeadByLeadId(int leadId) {
		return leadDao.getLeadByLeadId(leadId);
	}
	
	public LeadsEntity getLeadEntityBean(LeadsEntity leadFormEntity, LeadsEntity leadEntity, int courseId) {
		leadEntity.setName(leadFormEntity.getName());
		leadEntity.setMobileNo(leadFormEntity.getMobileNo());
		leadEntity.setEmailId(leadFormEntity.getEmailId());
		leadEntity.setLeadSource(leadFormEntity.getLeadSource());
		leadEntity.setComments(leadFormEntity.getComments());
		leadEntity.setAddress(leadFormEntity.getAddress());
		leadEntity.setArea(leadFormEntity.getArea());
		leadEntity.setCity(leadFormEntity.getCity());
		leadEntity.setLocation(leadFormEntity.getLocation());
		leadEntity.setAssignedTo(leadFormEntity.getAssignedTo());
		leadEntity.setModeofTraining(leadFormEntity.getModeofTraining());
		leadEntity.setTypeofTraining(leadFormEntity.getTypeofTraining());
		leadEntity.setLabels(leadFormEntity.getLabels());
		leadEntity.setCourse(courseId);
		leadEntity.setStatus(leadFormEntity.getStatus());
		leadEntity.setCourseCategeory(leadFormEntity.getCourseCategeory());
		leadEntity.setCreatedDate(new Date());
		leadEntity.setLastUpdatedDate(new Date());
		leadEntity.setCourseName(leadFormEntity.getCourseName());
		return leadEntity;
	}
	
}
