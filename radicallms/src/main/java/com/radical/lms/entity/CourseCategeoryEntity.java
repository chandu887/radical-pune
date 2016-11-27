package com.radical.lms.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "coursecategory")
public class CourseCategeoryEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "categoryid")
	private int categoryId;

	@Column(name = "categoryname")
	private String categeoryName;
	
	@Column(name = "subject")
	private String subject;
	
	@Column(name = "messagebody")
	private String messagebody;
	
	@Column(name = "createdtime")
	private Date createdTime; 

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessagebody() {
		return messagebody;
	}

	public void setMessagebody(String messagebody) {
		this.messagebody = messagebody;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public CourseCategeoryEntity() {

	}

	public CourseCategeoryEntity( String categeoryName) {
		this.categeoryName = categeoryName;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategeoryName() {
		return categeoryName;
	}

	public void setCategeoryName(String categeoryName) {
		this.categeoryName = categeoryName;
	}

}
