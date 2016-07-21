package com.radical.lms.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "sendsms")
public class SendSmsEntity {

	@Column(name = "receivermobileno")
	private String receiverMobileNo;

	@Column(name = "smstemplateId")
	private int smsTemplateId;

	@Column(name = "status")
	private int status;

	@Column(name = "createdtime")
	private Date createdTime;

	@Column(name = "smssendtime")
	private Date smsSendTime;

	@Column(name = "response")
	private String response;

	public SendSmsEntity() {

	}

	public SendSmsEntity(String receiverMobileNo, int smsTemplateId, int status, Date createdTime, Date smsSendTime,
			String response) {
		this.receiverMobileNo = receiverMobileNo;
		this.smsTemplateId = smsTemplateId;
		this.status = status;
		this.createdTime = createdTime;
		this.smsSendTime = smsSendTime;
		this.response = response;

	}

	public String getReceiverMobileNo() {
		return receiverMobileNo;
	}

	public void setReceiverMobileNo(String receiverMobileNo) {
		this.receiverMobileNo = receiverMobileNo;
	}

	public int getSmsTemplateId() {
		return smsTemplateId;
	}

	public void setSmsTemplateId(int smsTemplateId) {
		this.smsTemplateId = smsTemplateId;
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

	public Date getSmsSendTime() {
		return smsSendTime;
	}

	public void setSmsSendTime(Date smsSendTime) {
		this.smsSendTime = smsSendTime;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

}
