package com.radical.lms.entity;

import java.sql.Blob;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "courses")
public class CourseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "courseid")
	private int courseId;

	@Column(name = "categoryid")
	private int categeoryId;

	@Column(name = "coursename")
	private String courseName;
	
	@Column(name = "subject")
	private String subject;
	
	@Column(name = "messagebody")
	private String messagebody;
	
	@Column(name = "createdtime")
	private Date createdTime;
	
	@Column(name = "updatedtime")
	private Date updatedTime;
	
	@Column(name = "isactive")
	private int isActive;
	
	@Column(name = "mailerpath")
	private String mailerPath;
	
	@Column(name = "type")
	 private String fileType;

	 @Column(name = "content")
	 @Lob
	 private Blob  content;	
	 
	
	
	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Blob getContent() {
		return content;
	}

	public void setContent(Blob content) {
		this.content = content;
	}

	public String getMailerPath() {
		return mailerPath;
	}

	public void setMailerPath(String mailerPath) {
		this.mailerPath = mailerPath;
	}

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

	public CourseEntity() {

	}

	public CourseEntity(int categeoryId, String courseName) {
		this.categeoryId = categeoryId;
		this.courseName = courseName;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public int getCategeoryId() {
		return categeoryId;
	}

	public void setCategeoryId(int categeoryId) {
		this.categeoryId = categeoryId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

}
