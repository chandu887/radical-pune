package com.radical.lms.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sendemail")
public class SendEmailEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "receiveremailid")
	private String receiverMailId;

	@Column(name = "courseid")
	private int courseId;

	@Column(name = "status")
	private int status;

	@Column(name = "createdtime")
	private Date createdTime;

	@Column(name = "emailsendtime")
	private Date emailSendTime;

	public SendEmailEntity() {

	}

	public SendEmailEntity(String receiverMailId, int courseId, int status, Date createdTime,
			Date emailSendTime) {
		this.receiverMailId = receiverMailId;
		this.courseId = courseId;
		this.status = status;
		this.createdTime = createdTime;
		this.emailSendTime = emailSendTime;

	}

	public String getReceiverMailId() {
		return receiverMailId;
	}

	public void setReceiverMailId(String receiverMailId) {
		this.receiverMailId = receiverMailId;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getEmailSendTime() {
		return emailSendTime;
	}

	public void setEmailSendTime(Date emailSendTime) {
		this.emailSendTime = emailSendTime;
	}

}
