package com.radical.lms.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "leads")
public class LeadsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "leadid")
	private int leadiId;

	@Column(name = "name")
	private String name;

	public int getLeadiId() {
		return leadiId;
	}

	public void setLeadiId(int leadiId) {
		this.leadiId = leadiId;
	}

	@Column(name = "mobileno")
	private String mobileNo;

	@Column(name = "emailid")
	private String emailId;

	@Column(name = "status")
	private int status;

	@Column(name = "course")
	private int course;

	@Column(name = "coursecategory")
	private int courseCategeory;

	@Column(name = "leadsource")
	private int leadSource;

	@Column(name = "assignedto")
	private int assignedTo;

	@Column(name = "createdtime")
	private Date createdDate;

	@Column(name = "lastupdatetime")
	private Date lastUpdatedDate;

	@Column(name = "city")
	private String city;
	
	public LeadsEntity() {

	}

	public LeadsEntity(String name, String mobileNo, String emailId, int status, int course, int courseCategeory,
			int leadSource, int assignedTo, Date createdDate, String city) {
		super();
		this.name = name;
		this.mobileNo = mobileNo;
		this.emailId = emailId;
		this.status = status;
		this.course = course;
		this.courseCategeory = courseCategeory;
		this.leadSource = leadSource;
		this.assignedTo = assignedTo;
		this.createdDate = createdDate;
		this.city = city;
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

	public int getCourseCategeory() {
		return courseCategeory;
	}

	public void setCourseCategeory(int courseCategeory) {
		this.courseCategeory = courseCategeory;
	}

	public int getAssignedTo() {
		return assignedTo;
	}

	public int getCourse() {
		return course;
	}

	public void setCourse(int course) {
		this.course = course;
	}

	public int getLeadSource() {
		return leadSource;
	}

	public void setLeadSource(int leadSource) {
		this.leadSource = leadSource;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}



	
}