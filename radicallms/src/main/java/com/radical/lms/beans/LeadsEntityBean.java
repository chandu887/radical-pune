package com.radical.lms.beans;

import java.util.Date;

public class LeadsEntityBean {
	private String status;
	private String name;
	private String mobileNo;
	private int enqID;
	private String course;
	private String categeory;
	private String sourceLead;
	private String assignedTo;
	private Date createdTime;
	
	public LeadsEntityBean(String status,String name,String mobileNo,int enqID,String course,String categeory,String sourceLead,String assignedTo,Date createdTime){
		this.status = status;
		this.name = name;
		this.mobileNo = mobileNo;
		this.enqID = enqID;
		this.course = course;
		this.categeory = categeory;
		this.sourceLead = sourceLead;
		this.assignedTo = assignedTo;
		this.createdTime = createdTime;
	}
	
	public LeadsEntityBean(){
		
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public int getEnqID() {
		return enqID;
	}

	public void setEnqID(int enqID) {
		this.enqID = enqID;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public String getCategeory() {
		return categeory;
	}

	public void setCategeory(String categeory) {
		this.categeory = categeory;
	}

	public String getSourceLead() {
		return sourceLead;
	}

	public void setSourceLead(String sourceLead) {
		this.sourceLead = sourceLead;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

}
