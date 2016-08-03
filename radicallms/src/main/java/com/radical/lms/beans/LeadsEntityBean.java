package com.radical.lms.beans;

import java.util.Date;

public class LeadsEntityBean {
	private int enqID;
	private String name;
	private String mobileNo;
	private String emailId;
	private String status;
	private String course;
	private String categeory;
	private String sourceLead;
	private String assignedTo;
	private String createdTime;
	private String updatedTime;
	private String city;
	private String comments;
	private String reason;
	private String address;
	private String area;
	private String location;
	private String modeOfTraining;
	private String typeOfTraining;	
	
	

	public LeadsEntityBean(int enqID, String name, String mobileNo, String emailId, String status, String course,
			String categeory, String sourceLead, String assignedTo, String createdTime, String updatedTime, String city,
			String comments, String reason, String address, String area, String location, String modeOfTraining,
			String typeOfTraining) {
		super();
		this.enqID = enqID;
		this.name = name;
		this.mobileNo = mobileNo;
		this.emailId = emailId;
		this.status = status;
		this.course = course;
		this.categeory = categeory;
		this.sourceLead = sourceLead;
		this.assignedTo = assignedTo;
		this.createdTime = createdTime;
		this.updatedTime = updatedTime;
		this.city = city;
		this.comments = comments;
		this.reason = reason;
		this.address = address;
		this.area = area;
		this.location = location;
		this.modeOfTraining = modeOfTraining;
		this.typeOfTraining = typeOfTraining;
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

	

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getModeOfTraining() {
		return modeOfTraining;
	}

	public void setModeOfTraining(String modeOfTraining) {
		this.modeOfTraining = modeOfTraining;
	}

	public String getTypeOfTraining() {
		return typeOfTraining;
	}

	public void setTypeOfTraining(String typeOfTraining) {
		this.typeOfTraining = typeOfTraining;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public String getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}

}
