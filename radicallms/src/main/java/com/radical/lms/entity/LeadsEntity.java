package com.radical.lms.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "leads")
public class LeadsEntity {

	@Column(name = "name")
	private String name;

	@Column(name = "mobileno")
	private String mobileNo;

	@Column(name = "emailid")
	private String emailId;

	@Column(name = "status")
	private int status;

	@Column(name = "course")
	private String course;

	@Column(name = "coursecategory")
	private int courseCategeory;

	@Column(name = "leadsource")
	private String leadSource;

	@Column(name = "assignedto")
	private int assignedTo;

	@Column(name = "createdtime")
	private Date createdDate;

	@Column(name = "lastupdatetime")
	private Date lastUpdatedDate;

	public LeadsEntity() {

	}

	public LeadsEntity(String name, String mobileNo, String emailId, int status, String course, int courseCategeory,
			String leadSource, int assignedTo, Date createdDate, Date lastUpdatedDate) {
		this.name = name;
		this.mobileNo = mobileNo;
		this.emailId = emailId;
		this.status = status;
		this.course = course;
		this.courseCategeory = courseCategeory;
		this.leadSource = leadSource;
		this.assignedTo = assignedTo;
		this.createdDate = createdDate;
		this.lastUpdatedDate = lastUpdatedDate;

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public int getCourseCategeory() {
		return courseCategeory;
	}

	public void setCourseCategeory(int courseCategeory) {
		this.courseCategeory = courseCategeory;
	}

	public String getLeadSource() {
		return leadSource;
	}

	public void setLeadSource(String leadSource) {
		this.leadSource = leadSource;
	}

	public int getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(int assignedTo) {
		this.assignedTo = assignedTo;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

}
