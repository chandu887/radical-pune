package com.radical.lms.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "sendemail")
public class SendEmailEntity {

	@Column(name = "receiveremailid")
	private String receiverMailId;

	@Column(name = "senderemailid")
	private String senderMailId;

	@Column(name = "status")
	private int status;

	@Column(name = "createdtime")
	private Date createdTime;

	@Column(name = "emailsendtime")
	private Date emailSendTime;

	public SendEmailEntity() {

	}

	public SendEmailEntity(String receiverMailId, String senderMailId, int status, Date createdTime,
			Date emailSendTime) {
		this.receiverMailId = receiverMailId;
		this.senderMailId = senderMailId;
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

	public String getSenderMailId() {
		return senderMailId;
	}

	public void setSenderMailId(String senderMailId) {
		this.senderMailId = senderMailId;
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
