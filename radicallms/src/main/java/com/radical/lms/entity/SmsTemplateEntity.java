package com.radical.lms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "smstemplates")
public class SmsTemplateEntity {

	@Column(name = "smstemplate")
	private String smsTemplate;

	public SmsTemplateEntity() {

	}

	public SmsTemplateEntity(String smsTemplate) {
		this.smsTemplate = smsTemplate;
	}

	public String getSmsTemplate() {
		return smsTemplate;
	}

	public void setSmsTemplate(String smsTemplate) {
		this.smsTemplate = smsTemplate;
	}

}
