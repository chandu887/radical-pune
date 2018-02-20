package com.radical.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.radical.lms.beans.SulekhaLead;
import com.radical.lms.entity.CourseCategeoryEntity;
import com.radical.lms.entity.LeadsEntity;
import com.radical.lms.service.LeadService;
import com.radical.lms.service.UserService;

@RestController

public class ApiController {

	@Autowired
	private LeadService leadService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/lead", method = RequestMethod.POST)
	public ResponseEntity<String> createSulekhaLead(@RequestBody SulekhaLead lead) {
		try {
			LeadsEntity leadsEntity = new LeadsEntity();
			leadsEntity.setName(lead.getUserName());
			leadsEntity.setMobileNo(lead.getUserMobile());
			leadsEntity.setCity(lead.getUserCity());
			leadsEntity.setEmailId(lead.getUserEmail());
			leadsEntity.setComments(lead.getUserComments());
			Integer courseCategeory = userService.getCategoryNameIdMapping().get(lead.getKeywords());
			if (courseCategeory != null) {				
				leadsEntity.setCourseCategeory(courseCategeory);
			}
			leadService.saveLead(leadsEntity);
		} catch (Exception e) {
			return new ResponseEntity<String>("failure", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>("success", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/lead", method = RequestMethod.GET)
	public ResponseEntity<String> createJustDailLead(@RequestParam("name") String name, @RequestParam("mobile") String mobile,
			@RequestParam("phone") String phone, @RequestParam("email") String email, @RequestParam("category") String category, @RequestParam("city") String city,
			@RequestParam("area") String area) {
		
		LeadsEntity leadsEntity = new LeadsEntity();
		leadsEntity.setName(name);
		leadsEntity.setMobileNo(mobile);
		leadsEntity.setLandLineNumber(phone);
		leadsEntity.setEmailId(email);
		Integer courseCategeory = userService.getCategoryNameIdMapping().get(category);
		if (courseCategeory != null) {				
			leadsEntity.setCourseCategeory(courseCategeory);
		}
		leadsEntity.setCity(city);
		leadsEntity.setArea(area);
		
		leadService.saveLead(leadsEntity);
		return new ResponseEntity<String>("RECEIVED", HttpStatus.OK);
	}
}
